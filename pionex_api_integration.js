const crypto = require('crypto');
const axios = require('axios');
const WebSocket = require('ws');
const EventEmitter = require('events');
const { format, subDays, parseISO } = require('date-fns');

// Enums
const OrderType = {
    MARKET: 'MARKET',
    LIMIT: 'LIMIT',
    STOP_LOSS: 'STOP_LOSS',
    STOP_LIMIT: 'STOP_LIMIT',
    TRAILING_STOP: 'TRAILING_STOP',
    IOC: 'IOC', // Immediate Or Cancel
    FOK: 'FOK', // Fill Or Kill
    POST_ONLY: 'POST_ONLY'
};

const OrderSide = {
    BUY: 'BUY',
    SELL: 'SELL'
};

const OrderStatus = {
    NEW: 'NEW',
    PARTIALLY_FILLED: 'PARTIALLY_FILLED',
    FILLED: 'FILLED',
    CANCELLED: 'CANCELLED',
    EXPIRED: 'EXPIRED',
    REJECTED: 'REJECTED'
};

const PositionSide = {
    LONG: 'LONG',
    SHORT: 'SHORT',
    BOTH: 'BOTH'
};

const TimeInForce = {
    GTC: 'GTC', // Good Till Cancel
    IOC: 'IOC', // Immediate Or Cancel
    FOK: 'FOK'  // Fill Or Kill
};

class PionexAPI extends EventEmitter {
    constructor(config = {}) {
        super();

        // Configuração
        this.config = {
            apiKey: config.apiKey || process.env.PIONEX_API_KEY || '',
            apiSecret: config.apiSecret || process.env.PIONEX_API_SECRET || '',
            environment: config.environment || process.env.PIONEX_ENV || 'live',
            testnet: config.testnet || process.env.PIONEX_TESTNET === 'true' || false,
            debug: config.debug || false,
            timeout: config.timeout || 10000,
            maxRetries: config.maxRetries || 3,
            autoReconnect: config.autoReconnect !== false
        };

        // URLs
        this.baseUrls = {
            live: {
                rest: 'https://api.pionex.com',
                ws: 'wss://ws.pionex.com'
            },
            testnet: {
                rest: 'https://api.testnet.pionex.com',
                ws: 'wss://ws.testnet.pionex.com'
            }
        };

        const env = this.config.testnet ? 'testnet' : 'live';
        this.baseUrl = this.baseUrls[env].rest;
        this.wsUrl = this.baseUrls[env].ws;

        // Headers padrão
        this.headers = {
            'Content-Type': 'application/json',
            'PIONEX-KEY': this.config.apiKey
        };

        // Estado
        this.isConnected = false;
        this.isAuthenticated = false;
        this.ws = null;
        this.wsReconnectInterval = null;
        this.requestCounter = 0;

        // Cache
        this.cache = {
            balances: new Map(),
            positions: new Map(),
            orders: new Map(),
            tickers: new Map(),
            klines: new Map(),
            lastUpdated: {}
        };

        // Listeners do WebSocket
        this.wsListeners = {
            depth: new Map(),
            ticker: new Map(),
            kline: new Map(),
            trade: new Map(),
            order: new Map(),
            balance: new Map()
        };

        // Callbacks pendentes
        this.pendingCallbacks = new Map();

        // Rate limiting
        this.rateLimits = {
            requests: [],
            maxRequestsPerSecond: 20,
            maxRequestsPerMinute: 120
        };

        // Logging
        this.setupLogging();

        // Inicializar automaticamente se credentials estiverem disponíveis
        if (this.config.apiKey && this.config.apiSecret) {
            this.initialize();
        }
    }

    setupLogging() {
        this.logger = {
            info: (msg, data = null) => {
                const timestamp = new Date().toISOString();
                console.log(`ℹ️ [${timestamp}] [PionexAPI] ${msg}`, data || '');
                this.emit('log', { level: 'info', message: msg, data, timestamp });
            },
            warn: (msg, data = null) => {
                const timestamp = new Date().toISOString();
                console.warn(`⚠️ [${timestamp}] [PionexAPI] ${msg}`, data || '');
                this.emit('log', { level: 'warn', message: msg, data, timestamp });
            },
            error: (msg, data = null) => {
                const timestamp = new Date().toISOString();
                console.error(`❌ [${timestamp}] [PionexAPI] ${msg}`, data || '');
                this.emit('log', { level: 'error', message: msg, data, timestamp });
            },
            debug: (msg, data = null) => {
                if (this.config.debug) {
                    const timestamp = new Date().toISOString();
                    console.debug(`🔍 [${timestamp}] [PionexAPI] ${msg}`, data || '');
                    this.emit('log', { level: 'debug', message: msg, data, timestamp });
                }
            }
        };
    }

    async initialize() {
        this.logger.info('🚀 Inicializando Pionex API v4.0...');

        // Testar conectividade
        const isConnected = await this.testConnectivity();

        if (isConnected) {
            this.logger.info('✅ Conectado à Pionex API');

            // Conectar WebSocket se autoReconnect estiver ativo
            if (this.config.autoReconnect) {
                await this.connectWebSocket();
            }

            // Carregar informações iniciais
            await this.loadInitialData();
        } else {
            this.logger.error('❌ Falha na conexão com Pionex API');
        }

        this.isConnected = isConnected;
        return isConnected;
    }

    // ============================================
    // Métodos HTTP/REST
    // ============================================

    async makeRequest(method, endpoint, params = {}, signed = false) {
        const requestId = ++this.requestCounter;
        const url = `${this.baseUrl}${endpoint}`;

        // Verificar rate limit
        this.checkRateLimit();

        const config = {
            method,
            url,
            headers: this.headers,
            timeout: this.config.timeout
        };

        // Adicionar signature se necessário
        if (signed) {
            await this.addSignature(config, params);
        } else if (method === 'GET') {
            config.params = params;
        } else {
            config.data = params;
        }

        this.logger.debug(`Request #${requestId}: ${method} ${endpoint}`, params);

        // Tentativa com retry
        for (let attempt = 1; attempt <= this.config.maxRetries; attempt++) {
            try {
                const response = await axios(config);

                this.logger.debug(`Response #${requestId}:`, {
                    status: response.status,
                    data: response.data
                });

                // Registrar rate limit
                this.recordRateLimit();

                if (response.data && response.data.result === false) {
                    throw new Error(response.data.message || 'API returned false result');
                }

                return response.data;

            } catch (error) {
                this.logger.error(`Request #${requestId} failed (attempt ${attempt}/${this.config.maxRetries})`, {
                    error: error.message,
                    endpoint,
                    params
                });

                if (attempt === this.config.maxRetries) {
                    throw error;
                }

                // Esperar antes de tentar novamente
                await this.sleep(1000 * attempt);
            }
        }
    }

    async addSignature(config, params) {
        const timestamp = Date.now();
        const queryString = Object.keys(params)
            .sort()
            .map(key => `${key}=${params[key]}`)
            .join('&');

        const message = `${timestamp}${config.method}${config.url}${queryString}`;
        const signature = crypto
            .createHmac('sha256', this.config.apiSecret)
            .update(message)
            .digest('hex');

        config.headers['PIONEX-KEY'] = this.config.apiKey;
        config.headers['PIONEX-TIMESTAMP'] = timestamp;
        config.headers['PIONEX-SIGNATURE'] = signature;

        if (config.method === 'GET') {
            config.params = params;
        } else {
            config.data = params;
        }
    }

    checkRateLimit() {
        const now = Date.now();
        const oneSecondAgo = now - 1000;
        const oneMinuteAgo = now - 60000;

        // Limpar requests antigos
        this.rateLimits.requests = this.rateLimits.requests.filter(time => time > oneMinuteAgo);

        // Verificar limite por segundo
        const recentRequests = this.rateLimits.requests.filter(time => time > oneSecondAgo);
        if (recentRequests.length >= this.rateLimits.maxRequestsPerSecond) {
            throw new Error('Rate limit exceeded: too many requests per second');
        }

        // Verificar limite por minuto
        if (this.rateLimits.requests.length >= this.rateLimits.maxRequestsPerMinute) {
            throw new Error('Rate limit exceeded: too many requests per minute');
        }
    }

    recordRateLimit() {
        this.rateLimits.requests.push(Date.now());
    }

    async sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    // ============================================
    // Métodos de Conectividade
    // ============================================

    async testConnectivity() {
        try {
            const response = await axios.get(`${this.baseUrl}/api/v1/common/ping`, {
                timeout: 5000
            });

            return response.status === 200;
        } catch (error) {
            this.logger.error('Falha no teste de conectividade', error.message);
            return false;
        }
    }

    async getServerTime() {
        try {
            const data = await this.makeRequest('GET', '/api/v1/common/time');
            return data.time;
        } catch (error) {
            this.logger.error('Falha ao obter tempo do servidor', error.message);
            return Date.now();
        }
    }

    async getExchangeInfo() {
        try {
            const data = await this.makeRequest('GET', '/api/v1/common/symbols');
            return data.symbols || [];
        } catch (error) {
            this.logger.error('Falha ao obter informações da exchange', error.message);
            return [];
        }
    }

    // ============================================
    // Métodos de Mercado
    // ============================================

    async getTicker(symbol) {
        try {
            const cacheKey = `ticker_${symbol}`;
            const cacheTime = this.cache.lastUpdated[cacheKey];

            // Usar cache se disponível (5 segundos)
            if (cacheTime && Date.now() - cacheTime < 5000) {
                return this.cache.tickers.get(symbol);
            }

            const data = await this.makeRequest('GET', '/api/v1/market/ticker', { symbol });

            if (data && data.data) {
                const ticker = {
                    symbol,
                    price: parseFloat(data.data.price),
                    volume: parseFloat(data.data.volume),
                    high: parseFloat(data.data.high),
                    low: parseFloat(data.data.low),
                    change: parseFloat(data.data.change),
                    changePercent: parseFloat(data.data.changePercent),
                    timestamp: Date.now()
                };

                this.cache.tickers.set(symbol, ticker);
                this.cache.lastUpdated[cacheKey] = Date.now();

                this.emit('ticker', ticker);
                return ticker;
            }

            return null;
        } catch (error) {
            this.logger.error(`Falha ao obter ticker para ${symbol}`, error.message);
            return null;
        }
    }

    async getOrderBook(symbol, limit = 20) {
        try {
            const data = await this.makeRequest('GET', '/api/v1/market/depth', {
                symbol,
                limit
            });

            if (data && data.data) {
                return {
                    symbol,
                    bids: data.data.bids.map(bid => ({
                        price: parseFloat(bid.price),
                        quantity: parseFloat(bid.quantity)
                    })),
                    asks: data.data.asks.map(ask => ({
                        price: parseFloat(ask.price),
                        quantity: parseFloat(ask.quantity)
                    })),
                    timestamp: Date.now()
                };
            }

            return null;
        } catch (error) {
            this.logger.error(`Falha ao obter order book para ${symbol}`, error.message);
            return null;
        }
    }

    async getRecentTrades(symbol, limit = 100) {
        try {
            const data = await this.makeRequest('GET', '/api/v1/market/trades', {
                symbol,
                limit
            });

            if (data && data.data) {
                return data.data.map(trade => ({
                    id: trade.id,
                    symbol,
                    price: parseFloat(trade.price),
                    quantity: parseFloat(trade.quantity),
                    side: trade.side === 'buy' ? OrderSide.BUY : OrderSide.SELL,
                    timestamp: trade.time
                }));
            }

            return [];
        } catch (error) {
            this.logger.error(`Falha ao obter trades recentes para ${symbol}`, error.message);
            return [];
        }
    }

    async getKlines(symbol, interval = '15m', limit = 100, startTime = null, endTime = null) {
        try {
            const cacheKey = `klines_${symbol}_${interval}_${limit}`;
            const cacheTime = this.cache.lastUpdated[cacheKey];

            // Usar cache se disponível (30 segundos)
            if (cacheTime && Date.now() - cacheTime < 30000) {
                return this.cache.klines.get(cacheKey) || [];
            }

            const params = { symbol, interval, limit };
            if (startTime) params.startTime = startTime;
            if (endTime) params.endTime = endTime;

            const data = await this.makeRequest('GET', '/api/v1/market/klines', params);

            if (data && data.data) {
                const klines = data.data.map(k => ({
                    timestamp: k.time,
                    open: parseFloat(k.open),
                    high: parseFloat(k.high),
                    low: parseFloat(k.low),
                    close: parseFloat(k.close),
                    volume: parseFloat(k.volume),
                    closeTime: k.closeTime
                }));

                this.cache.klines.set(cacheKey, klines);
                this.cache.lastUpdated[cacheKey] = Date.now();

                return klines;
            }

            return [];
        } catch (error) {
            this.logger.error(`Falha ao obter klines para ${symbol}`, error.message);
            return [];
        }
    }

    async getHistoricalData(symbol, interval = '1h', days = 30) {
        try {
            const endTime = Date.now();
            const startTime = endTime - (days * 24 * 60 * 60 * 1000);

            let allKlines = [];
            let currentStart = startTime;
            const batchLimit = 1000;

            while (currentStart < endTime) {
                const batch = await this.getKlines(
                    symbol,
                    interval,
                    batchLimit,
                    currentStart,
                    Math.min(currentStart + (batchLimit * this.getIntervalMs(interval)), endTime)
                );

                if (batch.length === 0) break;

                allKlines = [...allKlines, ...batch];
                currentStart = batch[batch.length - 1].timestamp + 1;

                // Respeitar rate limit
                await this.sleep(100);
            }

            return allKlines;
        } catch (error) {
            this.logger.error(`Falha ao obter dados históricos para ${symbol}`, error.message);
            return [];
        }
    }

    getIntervalMs(interval) {
        const intervals = {
            '1m': 60000,
            '5m': 300000,
            '15m': 900000,
            '30m': 1800000,
            '1h': 3600000,
            '4h': 14400000,
            '1d': 86400000,
            '1w': 604800000
        };

        return intervals[interval] || 60000;
    }

    // ============================================
    // Métodos de Conta
    // ============================================

    async getAccountInfo() {
        try {
            const data = await this.makeRequest('GET', '/api/v1/account/info', {}, true);

            if (data && data.data) {
                const accountInfo = {
                    userId: data.data.userId,
                    makerFee: parseFloat(data.data.makerFee),
                    takerFee: parseFloat(data.data.takerFee),
                    canTrade: data.data.canTrade,
                    canWithdraw: data.data.canWithdraw,
                    canDeposit: data.data.canDeposit,
                    updateTime: data.data.updateTime
                };

                this.emit('accountInfo', accountInfo);
                return accountInfo;
            }

            return null;
        } catch (error) {
            this.logger.error('Falha ao obter informações da conta', error.message);
            return null;
        }
    }

    async getBalances() {
        try {
            const data = await this.makeRequest('GET', '/api/v1/account/balances', {}, true);

            if (data && data.data) {
                const balances = {};

                data.data.forEach(balance => {
                    balances[balance.coin] = {
                        coin: balance.coin,
                        free: parseFloat(balance.free),
                        frozen: parseFloat(balance.frozen),
                        total: parseFloat(balance.total)
                    };
                });

                this.cache.balances = new Map(Object.entries(balances));
                this.cache.lastUpdated['balances'] = Date.now();

                this.emit('balances', balances);
                return balances;
            }

            return {};
        } catch (error) {
            this.logger.error('Falha ao obter saldos', error.message);
            return {};
        }
    }

    async getBalance(coin) {
        const balances = await this.getBalances();
        return balances[coin] || { coin, free: 0, frozen: 0, total: 0 };
    }

    // ============================================
    // Métodos de Ordens
    // ============================================

    async placeOrder(orderParams) {
        try {
            const {
                symbol,
                side,
                type = OrderType.LIMIT,
                quantity,
                price = null,
                stopPrice = null,
                timeInForce = TimeInForce.GTC,
                clientOrderId = this.generateClientOrderId()
            } = orderParams;

            const params = {
                symbol,
                side,
                type,
                size: quantity.toString(),
                clientOrderId
            };

            if (price) params.price = price.toString();
            if (stopPrice) params.stopPrice = stopPrice.toString();
            if (timeInForce) params.timeInForce = timeInForce;

            const data = await this.makeRequest('POST', '/api/v1/trade/order', params, true);

            if (data && data.data) {
                const order = this.parseOrder(data.data);

                this.cache.orders.set(order.orderId, order);
                this.cache.lastUpdated[`order_${order.orderId}`] = Date.now();

                this.emit('orderPlaced', order);
                this.logger.info(`Ordem ${side} ${type} colocada para ${symbol}`, order);

                return order;
            }

            throw new Error('Resposta inválida da API');

        } catch (error) {
            this.logger.error('Falha ao colocar ordem', {
                error: error.message,
                orderParams
            });
            throw error;
        }
    }

    async cancelOrder(symbol, orderId) {
        try {
            const data = await this.makeRequest('DELETE', '/api/v1/trade/order', {
                symbol,
                orderId
            }, true);

            if (data && data.data) {
                const cancelledOrder = this.parseOrder(data.data);

                this.cache.orders.delete(orderId);
                this.emit('orderCancelled', cancelledOrder);
                this.logger.info(`Ordem ${orderId} cancelada`);

                return cancelledOrder;
            }

            throw new Error('Resposta inválida da API');

        } catch (error) {
            this.logger.error(`Falha ao cancelar ordem ${orderId}`, error.message);
            throw error;
        }
    }

    async cancelAllOrders(symbol) {
        try {
            const data = await this.makeRequest('DELETE', '/api/v1/trade/orders', {
                symbol
            }, true);

            if (data && data.data) {
                const cancelledOrders = data.data.map(order => this.parseOrder(order));

                cancelledOrders.forEach(order => {
                    this.cache.orders.delete(order.orderId);
                });

                this.emit('allOrdersCancelled', { symbol, orders: cancelledOrders });
                this.logger.info(`Todas as ordens para ${symbol} canceladas`);

                return cancelledOrders;
            }

            throw new Error('Resposta inválida da API');

        } catch (error) {
            this.logger.error(`Falha ao cancelar todas as ordens para ${symbol}`, error.message);
            throw error;
        }
    }

    async getOrder(symbol, orderId) {
        try {
            const cacheKey = `order_${orderId}`;
            const cacheTime = this.cache.lastUpdated[cacheKey];

            // Usar cache se disponível (10 segundos)
            if (cacheTime && Date.now() - cacheTime < 10000) {
                return this.cache.orders.get(orderId);
            }

            const data = await this.makeRequest('GET', '/api/v1/trade/order', {
                symbol,
                orderId
            }, true);

            if (data && data.data) {
                const order = this.parseOrder(data.data);

                this.cache.orders.set(orderId, order);
                this.cache.lastUpdated[cacheKey] = Date.now();

                return order;
            }

            return null;
        } catch (error) {
            this.logger.error(`Falha ao obter ordem ${orderId}`, error.message);
            return null;
        }
    }

    async getOpenOrders(symbol = null) {
        try {
            const params = symbol ? { symbol } : {};
            const data = await this.makeRequest('GET', '/api/v1/trade/orders/open', params, true);

            if (data && data.data) {
                const orders = data.data.map(order => this.parseOrder(order));

                // Atualizar cache
                orders.forEach(order => {
                    this.cache.orders.set(order.orderId, order);
                    this.cache.lastUpdated[`order_${order.orderId}`] = Date.now();
                });

                return orders;
            }

            return [];
        } catch (error) {
            this.logger.error('Falha ao obter ordens abertas', error.message);
            return [];
        }
    }

    async getOrderHistory(symbol = null, startTime = null, endTime = null, limit = 100) {
        try {
            const params = { limit };
            if (symbol) params.symbol = symbol;
            if (startTime) params.startTime = startTime;
            if (endTime) params.endTime = endTime;

            const data = await this.makeRequest('GET', '/api/v1/trade/orders/history', params, true);

            if (data && data.data) {
                return data.data.map(order => this.parseOrder(order));
            }

            return [];
        } catch (error) {
            this.logger.error('Falha ao obter histórico de ordens', error.message);
            return [];
        }
    }

    parseOrder(orderData) {
        return {
            orderId: orderData.orderId,
            clientOrderId: orderData.clientOrderId,
            symbol: orderData.symbol,
            side: orderData.side,
            type: orderData.type,
            status: orderData.status,
            price: parseFloat(orderData.price),
            quantity: parseFloat(orderData.size),
            executedQuantity: parseFloat(orderData.executedSize),
            averagePrice: parseFloat(orderData.averagePrice),
            fee: parseFloat(orderData.fee),
            feeCoin: orderData.feeCoin,
            createTime: orderData.createTime,
            updateTime: orderData.updateTime
        };
    }

    generateClientOrderId() {
        const timestamp = Date.now();
        const random = Math.floor(Math.random() * 10000);
        return `LEXTRADER_${timestamp}_${random}`;
    }

    // ============================================
    // Métodos de Posição
    // ============================================

    async getPositions(symbol = null) {
        try {
            const params = symbol ? { symbol } : {};
            const data = await this.makeRequest('GET', '/api/v1/account/positions', params, true);

            if (data && data.data) {
                const positions = data.data.map(pos => ({
                    symbol: pos.symbol,
                    positionSide: pos.positionSide,
                    size: parseFloat(pos.size),
                    entryPrice: parseFloat(pos.entryPrice),
                    liquidationPrice: parseFloat(pos.liquidationPrice),
                    margin: parseFloat(pos.margin),
                    leverage: parseFloat(pos.leverage),
                    unrealizedPnl: parseFloat(pos.unrealizedPnl),
                    realizedPnl: parseFloat(pos.realizedPnl),
                    timestamp: Date.now()
                }));

                // Atualizar cache
                positions.forEach(pos => {
                    this.cache.positions.set(pos.symbol, pos);
                });

                this.emit('positions', positions);
                return positions;
            }

            return [];
        } catch (error) {
            this.logger.error('Falha ao obter posições', error.message);
            return [];
        }
    }

    async closePosition(symbol, side = null, quantity = null) {
        try {
            const params = { symbol };

            if (side) params.side = side;
            if (quantity) params.size = quantity.toString();

            const data = await this.makeRequest('POST', '/api/v1/trade/close-position', params, true);

            if (data && data.data) {
                const closedPosition = {
                    symbol,
                    orderId: data.data.orderId,
                    size: parseFloat(data.data.size),
                    price: parseFloat(data.data.price),
                    timestamp: Date.now()
                };

                this.emit('positionClosed', closedPosition);
                this.logger.info(`Posição ${symbol} fechada`, closedPosition);

                return closedPosition;
            }

            throw new Error('Resposta inválida da API');

        } catch (error) {
            this.logger.error(`Falha ao fechar posição ${symbol}`, error.message);
            throw error;
        }
    }

    // ============================================
    // Métodos de WebSocket
    // ============================================

    async connectWebSocket() {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            this.logger.info('WebSocket já conectado');
            return;
        }

        try {
            this.logger.info('Conectando ao WebSocket Pionex...');

            this.ws = new WebSocket(this.wsUrl);

            this.ws.on('open', () => this.onWebSocketOpen());
            this.ws.on('message', (data) => this.onWebSocketMessage(data));
            this.ws.on('close', (code, reason) => this.onWebSocketClose(code, reason));
            this.ws.on('error', (error) => this.onWebSocketError(error));

            // Configurar reconexão automática
            if (this.config.autoReconnect) {
                this.setupAutoReconnect();
            }

        } catch (error) {
            this.logger.error('Falha ao conectar WebSocket', error.message);
        }
    }

    onWebSocketOpen() {
        this.logger.info('✅ WebSocket conectado');
        this.isConnected = true;

        // Autenticar
        this.authenticateWebSocket();

        // Reconectar listeners
        this.reconnectWebSocketListeners();

        this.emit('wsConnected');
    }

    async authenticateWebSocket() {
        if (!this.config.apiKey || !this.config.apiSecret) {
            this.logger.warn('Não é possível autenticar WebSocket sem API credentials');
            return;
        }

        try {
            const timestamp = Date.now();
            const message = `${timestamp}GET/user/ws`;
            const signature = crypto
                .createHmac('sha256', this.config.apiSecret)
                .update(message)
                .digest('hex');

            const authMessage = {
                op: 'login',
                key: this.config.apiKey,
                timestamp,
                signature
            };

            this.ws.send(JSON.stringify(authMessage));

            this.logger.debug('WebSocket authentication sent');

        } catch (error) {
            this.logger.error('Falha na autenticação WebSocket', error.message);
        }
    }

    onWebSocketMessage(data) {
        try {
            const message = JSON.parse(data.toString());

            this.logger.debug('WebSocket message received', message);

            // Processar mensagem
            this.processWebSocketMessage(message);

        } catch (error) {
            this.logger.error('Erro ao processar mensagem WebSocket', error.message);
        }
    }

    processWebSocketMessage(message) {
        if (message.op === 'login' && message.result === true) {
            this.isAuthenticated = true;
            this.logger.info('✅ WebSocket autenticado');
            this.emit('wsAuthenticated');
            return;
        }

        if (message.op === 'subscribe' && message.result === true) {
            this.logger.debug(`Inscrito em: ${message.channel}`);
            return;
        }

        if (message.op === 'pong') {
            return;
        }

        // Processar dados de mercado
        if (message.channel) {
            this.handleChannelData(message.channel, message.data);
        }

        // Processar dados da conta
        if (message.accountUpdate) {
            this.handleAccountUpdate(message.accountUpdate);
        }

        // Processar atualizações de ordem
        if (message.orderUpdate) {
            this.handleOrderUpdate(message.orderUpdate);
        }
    }

    handleChannelData(channel, data) {
        switch (channel) {
            case 'ticker':
                this.handleTickerData(data);
                break;
            case 'depth':
                this.handleDepthData(data);
                break;
            case 'kline':
                this.handleKlineData(data);
                break;
            case 'trade':
                this.handleTradeData(data);
                break;
        }
    }

    handleTickerData(data) {
        const ticker = {
            symbol: data.symbol,
            price: parseFloat(data.price),
            volume: parseFloat(data.volume),
            high: parseFloat(data.high),
            low: parseFloat(data.low),
            change: parseFloat(data.change),
            changePercent: parseFloat(data.changePercent),
            timestamp: Date.now()
        };

        this.cache.tickers.set(data.symbol, ticker);

        // Notificar listeners específicos
        if (this.wsListeners.ticker.has(data.symbol)) {
            const callback = this.wsListeners.ticker.get(data.symbol);
            callback(ticker);
        }

        // Emitir evento geral
        this.emit('wsTicker', ticker);
    }

    handleDepthData(data) {
        const orderBook = {
            symbol: data.symbol,
            bids: data.bids.map(bid => ({
                price: parseFloat(bid.price),
                quantity: parseFloat(bid.quantity)
            })),
            asks: data.asks.map(ask => ({
                price: parseFloat(ask.price),
                quantity: parseFloat(ask.quantity)
            })),
            timestamp: Date.now()
        };

        // Notificar listeners específicos
        if (this.wsListeners.depth.has(data.symbol)) {
            const callback = this.wsListeners.depth.get(data.symbol);
            callback(orderBook);
        }

        this.emit('wsDepth', orderBook);
    }

    handleKlineData(data) {
        const kline = {
            symbol: data.symbol,
            interval: data.interval,
            timestamp: data.time,
            open: parseFloat(data.open),
            high: parseFloat(data.high),
            low: parseFloat(data.low),
            close: parseFloat(data.close),
            volume: parseFloat(data.volume),
            closeTime: data.closeTime
        };

        const cacheKey = `${data.symbol}_${data.interval}`;

        // Notificar listeners específicos
        if (this.wsListeners.kline.has(cacheKey)) {
            const callback = this.wsListeners.kline.get(cacheKey);
            callback(kline);
        }

        this.emit('wsKline', kline);
    }

    handleTradeData(data) {
        const trade = {
            symbol: data.symbol,
            price: parseFloat(data.price),
            quantity: parseFloat(data.size),
            side: data.side === 'buy' ? OrderSide.BUY : OrderSide.SELL,
            timestamp: data.time
        };

        // Notificar listeners específicos
        if (this.wsListeners.trade.has(data.symbol)) {
            const callback = this.wsListeners.trade.get(data.symbol);
            callback(trade);
        }

        this.emit('wsTrade', trade);
    }

    handleAccountUpdate(update) {
        // Atualizar saldos
        if (update.balances) {
            const balances = {};
            update.balances.forEach(balance => {
                balances[balance.coin] = {
                    coin: balance.coin,
                    free: parseFloat(balance.free),
                    frozen: parseFloat(balance.frozen),
                    total: parseFloat(balance.total)
                };
            });

            this.cache.balances = new Map(Object.entries(balances));
            this.emit('wsBalances', balances);
        }

        // Atualizar posições
        if (update.positions) {
            const positions = update.positions.map(pos => ({
                symbol: pos.symbol,
                positionSide: pos.positionSide,
                size: parseFloat(pos.size),
                entryPrice: parseFloat(pos.entryPrice),
                unrealizedPnl: parseFloat(pos.unrealizedPnl)
            }));

            this.cache.positions = new Map(positions.map(pos => [pos.symbol, pos]));
            this.emit('wsPositions', positions);
        }
    }

    handleOrderUpdate(update) {
        const order = this.parseOrder(update);

        // Atualizar cache
        this.cache.orders.set(order.orderId, order);
        this.cache.lastUpdated[`order_${order.orderId}`] = Date.now();

        // Notificar listeners específicos
        if (this.wsListeners.order.has(order.orderId)) {
            const callback = this.wsListeners.order.get(order.orderId);
            callback(order);
        }

        // Emitir evento geral
        this.emit('wsOrder', order);

        // Log baseado no status
        if (order.status === OrderStatus.FILLED) {
            this.logger.info(`Ordem ${order.orderId} preenchida`, {
                symbol: order.symbol,
                side: order.side,
                executedQuantity: order.executedQuantity,
                averagePrice: order.averagePrice
            });
        }
    }

    onWebSocketClose(code, reason) {
        this.logger.warn(`WebSocket fechado (${code}): ${reason}`);
        this.isConnected = false;
        this.isAuthenticated = false;

        this.emit('wsClosed', { code, reason });

        // Tentar reconectar
        if (this.config.autoReconnect) {
            this.scheduleReconnect();
        }
    }

    onWebSocketError(error) {
        this.logger.error('Erro no WebSocket', error.message);
        this.emit('wsError', error);
    }

    setupAutoReconnect() {
        this.wsReconnectInterval = setInterval(() => {
            if (!this.isConnected) {
                this.logger.info('Tentando reconectar WebSocket...');
                this.connectWebSocket();
            }
        }, 10000); // Tentar a cada 10 segundos
    }

    scheduleReconnect() {
        setTimeout(() => {
            if (this.config.autoReconnect) {
                this.connectWebSocket();
            }
        }, 5000); // Reconectar após 5 segundos
    }

    disconnectWebSocket() {
        if (this.ws) {
            this.ws.close();
            this.ws = null;
        }

        if (this.wsReconnectInterval) {
            clearInterval(this.wsReconnectInterval);
            this.wsReconnectInterval = null;
        }

        this.isConnected = false;
        this.isAuthenticated = false;

        this.logger.info('WebSocket desconectado');
    }

    // ============================================
    // Métodos de Subscription
    // ============================================

    subscribeTicker(symbol, callback = null) {
        this.subscribeToChannel('ticker', symbol, callback);
    }

    subscribeDepth(symbol, callback = null) {
        this.subscribeToChannel('depth', symbol, callback);
    }

    subscribeKline(symbol, interval = '15m', callback = null) {
        const channel = `kline_${interval}`;
        const cacheKey = `${symbol}_${interval}`;

        if (callback) {
            this.wsListeners.kline.set(cacheKey, callback);
        }

        this.subscribeToChannel(channel, symbol, (data) => {
            this.handleKlineData({ ...data, interval });
        });
    }

    subscribeTrade(symbol, callback = null) {
        this.subscribeToChannel('trade', symbol, callback);
    }

    subscribeOrder(orderId, callback = null) {
        if (callback) {
            this.wsListeners.order.set(orderId, callback);
        }
    }

    subscribeToChannel(channel, symbol, callback = null) {
        if (!this.ws || this.ws.readyState !== WebSocket.OPEN) {
            this.logger.warn(`WebSocket não conectado, não é possível inscrever em ${channel}`);
            return;
        }

        const subscribeMessage = {
            op: 'subscribe',
            channel,
            symbol
        };

        this.ws.send(JSON.stringify(subscribeMessage));

        // Registrar callback
        if (callback) {
            switch (channel) {
                case 'ticker':
                    this.wsListeners.ticker.set(symbol, callback);
                    break;
                case 'depth':
                    this.wsListeners.depth.set(symbol, callback);
                    break;
                case 'trade':
                    this.wsListeners.trade.set(symbol, callback);
                    break;
            }
        }
    }

    reconnectWebSocketListeners() {
        // Reinscrever em todos os canais ativos
        this.wsListeners.ticker.forEach((callback, symbol) => {
            this.subscribeTicker(symbol, callback);
        });

        this.wsListeners.depth.forEach((callback, symbol) => {
            this.subscribeDepth(symbol, callback);
        });

        this.wsListeners.trade.forEach((callback, symbol) => {
            this.subscribeTrade(symbol, callback);
        });

        // Klines precisam de interval
        // (Não armazenamos interval no listener, precisaríamos de estrutura diferente)
    }

    // ============================================
    // Métodos de Utilidade
    // ============================================

    async loadInitialData() {
        try {
            this.logger.info('Carregando dados iniciais...');

            // Obter informações da conta
            await this.getAccountInfo();

            // Obter saldos
            await this.getBalances();

            // Obter ordens abertas
            await this.getOpenOrders();

            // Obter posições
            await this.getPositions();

            this.logger.info('✅ Dados iniciais carregados');

        } catch (error) {
            this.logger.error('Erro ao carregar dados iniciais', error.message);
        }
    }

    getSystemStatus() {
        return {
            isConnected: this.isConnected,
            isAuthenticated: this.isAuthenticated,
            wsConnected: this.ws && this.ws.readyState === WebSocket.OPEN,
            environment: this.config.testnet ? 'testnet' : 'live',
            cacheSize: {
                tickers: this.cache.tickers.size,
                orders: this.cache.orders.size,
                positions: this.cache.positions.size
            },
            rateLimit: {
                recentRequests: this.rateLimits.requests.length,
                perSecond: this.rateLimits.maxRequestsPerSecond,
                perMinute: this.rateLimits.maxRequestsPerMinute
            }
        };
    }

    clearCache() {
        this.cache.tickers.clear();
        this.cache.orders.clear();
        this.cache.positions.clear();
        this.cache.klines.clear();
        this.cache.balances.clear();
        this.cache.lastUpdated = {};

        this.logger.info('Cache limpo');
    }

    // ============================================
    // Métodos de Trading Avançados
    // ============================================

    async placeMarketOrder(symbol, side, quantity) {
        return await this.placeOrder({
            symbol,
            side,
            type: OrderType.MARKET,
            quantity
        });
    }

    async placeLimitOrder(symbol, side, quantity, price) {
        return await this.placeOrder({
            symbol,
            side,
            type: OrderType.LIMIT,
            quantity,
            price,
            timeInForce: TimeInForce.GTC
        });
    }

    async placeStopLossOrder(symbol, side, quantity, stopPrice) {
        return await this.placeOrder({
            symbol,
            side,
            type: OrderType.STOP_LOSS,
            quantity,
            stopPrice
        });
    }

    async placeBracketOrder(symbol, side, quantity, entryPrice, takeProfitPrice, stopLossPrice) {
        try {
            // Ordem de entrada
            const entryOrder = await this.placeOrder({
                symbol,
                side,
                type: OrderType.LIMIT,
                quantity,
                price: entryPrice,
                timeInForce: TimeInForce.GTC
            });

            // Ordem de Take Profit
            const tpSide = side === OrderSide.BUY ? OrderSide.SELL : OrderSide.BUY;
            const tpOrder = await this.placeOrder({
                symbol,
                side: tpSide,
                type: OrderType.LIMIT,
                quantity,
                price: takeProfitPrice,
                timeInForce: TimeInForce.GTC
            });

            // Ordem de Stop Loss
            const slSide = side === OrderSide.BUY ? OrderSide.SELL : OrderSide.BUY;
            const slOrder = await this.placeOrder({
                symbol,
                side: slSide,
                type: OrderType.STOP_LOSS,
                quantity,
                stopPrice: stopLossPrice
            });

            return {
                entryOrder,
                takeProfitOrder: tpOrder,
                stopLossOrder: slOrder,
                bracketId: `BRACKET_${entryOrder.orderId}`
            };

        } catch (error) {
            this.logger.error('Falha ao colocar bracket order', error.message);
            throw error;
        }
    }

    async getEstimatedValue(coin, currency = 'USDT') {
        try {
            const balance = await this.getBalance(coin);

            if (coin === currency) {
                return balance.total;
            }

            const ticker = await this.getTicker(`${coin}_${currency}`);

            if (ticker) {
                return balance.total * ticker.price;
            }

            return 0;

        } catch (error) {
            this.logger.error(`Falha ao estimar valor para ${coin}`, error.message);
            return 0;
        }
    }

    async getPortfolioValue(currency = 'USDT') {
        try {
            const balances = await this.getBalances();
            let totalValue = 0;

            for (const [coin, balance] of Object.entries(balances)) {
                if (balance.total > 0) {
                    const value = await this.getEstimatedValue(coin, currency);
                    totalValue += value;
                }
            }

            return totalValue;

        } catch (error) {
            this.logger.error('Falha ao calcular valor do portfólio', error.message);
            return 0;
        }
    }
}

// Exportar classes e enums
module.exports = {
    PionexAPI,
    OrderType,
    OrderSide,
    OrderStatus,
    PositionSide,
    TimeInForce
};

// Exemplo de uso
if (require.main === module) {
    (async () => {
        try {
            const api = new PionexAPI({
                apiKey: process.env.PIONEX_API_KEY,
                apiSecret: process.env.PIONEX_API_SECRET,
                environment: 'live',
                debug: true
            });

            await api.initialize();

            const status = api.testConnectivity();
            console.log(`🚀 Pionex API v4.0 Status: ${status ? 'Online' : 'Offline'}`);

            // Exemplo: obter ticker
            const ticker = await api.getTicker('BTC_USDT');
            console.log('BTC/USDT Ticker:', ticker);

            // Exemplo: obter saldos
            const balances = await api.getBalances();
            console.log('Balances:', balances);

            // Conectar WebSocket para dados em tempo real
            await api.connectWebSocket();

            // Inscrever em ticker
            api.subscribeTicker('BTC_USDT', (data) => {
                console.log('Live Ticker:', data);
            });

        } catch (error) {
            console.error('Erro:', error.message);
        }
    })();
}
const { PionexAPI, OrderSide, OrderType } = require('./pionex-api');

// Inicializar API
const api = new PionexAPI({
    apiKey: 'your_key',
    apiSecret: 'your_secret',
    debug: true
});

// Conectar
await api.initialize();

// Obter dados de mercado
const ticker = await api.getTicker('BTC_USDT');
const orderBook = await api.getOrderBook('BTC_USDT', 20);
const klines = await api.getKlines('BTC_USDT', '15m', 100);

// Gerenciar ordens
const order = await api.placeMarketOrder('BTC_USDT', OrderSide.BUY, 0.01);
await api.cancelOrder('BTC_USDT', order.orderId);

// WebSocket
await api.connectWebSocket();
api.subscribeTicker('BTC_USDT', (ticker) => {
    console.log('Ticker atualizado:', ticker);
});

// Obter status do sistema
const status = api.getSystemStatus();
console.log('Status:', status);
// Bracket order
const bracket = await api.placeBracketOrder(
    'BTC_USDT',
    OrderSide.BUY,
    0.01,
    40000,    // entry
    42000,    // take profit
    39000     // stop loss
);

// Portfolio management
const portfolioValue = await api.getPortfolioValue('USDT');
console.log('Valor do portfólio:', portfolioValue);

// Historical data analysis
const historicalData = await api.getHistoricalData(
    'BTC_USDT',
    '1d',
    365 // 1 year
);

// Position management
const positions = await api.getPositions();
positions.forEach(pos => {
    if (pos.unrealizedPnl < -0.1) { // 10% loss
        api.closePosition(pos.symbol);
    }
});