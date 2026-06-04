package modulo_emocional.Sensório;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Pionex API Integration - Layer 1 (Sensorial)
 * Versão Java convertida e superpotenciada do Python original
 */
public class PionexAPI {
    
    // Enums para tipos de dados
    public enum OrderType {
        MARKET("MARKET"),
        LIMIT("LIMIT"),
        STOP_LOSS("STOP_LOSS"),
        STOP_LOSS_LIMIT("STOP_LOSS_LIMIT"),
        TAKE_PROFIT("TAKE_PROFIT"),
        TAKE_PROFIT_LIMIT("TAKE_PROFIT_LIMIT")
    }
    
    public enum OrderSide {
        BUY("BUY"),
        SELL("SELL")
    }
    
    public enum BotType {
        GRID_TRADING("GRID_TRADING"),
        DCA_BOT("DCA_BOT"),
        REBALANCING("REBALANCING"),
        ARBITRAGE("ARBITRAGE"),
        LEVERAGED_GRID("LEVERAGED_GRID"),
        INFINITY_GRID("INFINITY_GRID"),
        MARTINGALE("MARTINGALE")
    }
    
    // Estrutura de dados para ordem
    public static class Order {
        private final String orderId;
        private final String symbol;
        private final OrderSide side;
        private final OrderType type;
        private final double quantity;
        private final double price;
        private final Double stopPrice;
        private final LocalDateTime timestamp;
        private final String status;
        private final Map<String, Object> metadata;
        
        public Order(String orderId, String symbol, OrderSide side, OrderType type,
                   double quantity, double price, Double stopPrice, String status) {
            this.orderId = orderId;
            this.symbol = symbol;
            this.side = side;
            this.type = type;
            this.quantity = quantity;
            this.price = price;
            this.stopPrice = stopPrice;
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.metadata = new HashMap<>();
        }
        
        // Getters
        public String getOrderId() { return orderId; }
        public String getSymbol() { return symbol; }
        public OrderSide getSide() { return side; }
        public OrderType getType() { return type; }
        public double getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public Double getStopPrice() { return stopPrice; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getStatus() { return status; }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("symbol", symbol);
            map.put("side", side.toString());
            map.put("type", type.toString());
            map.put("quantity", quantity);
            map.put("price", price);
            map.put("stopPrice", stopPrice);
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            map.put("status", status);
            map.put("metadata", metadata);
            return map;
        }
    }
    
    // Estrutura de dados para ticker
    public static class Ticker {
        private final String symbol;
        private final double price;
        private final double bid;
        private final double ask;
        private final double volume;
        private final double change;
        private final double changePercent;
        private final double high;
        private final double low;
        private final LocalDateTime timestamp;
        
        public Ticker(String symbol, double price, double bid, double ask, double volume,
                    double change, double changePercent, double high, double low) {
            this.symbol = symbol;
            this.price = price;
            this.bid = bid;
            this.ask = ask;
            this.volume = volume;
            this.change = change;
            this.changePercent = changePercent;
            this.high = high;
            this.low = low;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getSymbol() { return symbol; }
        public double getPrice() { return price; }
        public double getBid() { return bid; }
        public double getAsk() { return ask; }
        public double getVolume() { return volume; }
        public double getChange() { return change; }
        public double getChangePercent() { return changePercent; }
        public double getHigh() { return high; }
        public double getLow() { return low; }
        public LocalDateTime getTimestamp() { return timestamp; }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("symbol", symbol);
            map.put("price", price);
            map.put("bid", bid);
            map.put("ask", ask);
            map.put("volume", volume);
            map.put("change", change);
            map.put("changePercent", changePercent);
            map.put("high", high);
            map.put("low", low);
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return map;
        }
    }
    
    // Estrutura de dados para balance
    public static class Balance {
        private final String asset;
        private final double free;
        private final double locked;
        private final double total;
        
        public Balance(String asset, double free, double locked, double total) {
            this.asset = asset;
            this.free = free;
            this.locked = locked;
            this.total = total;
        }
        
        // Getters
        public String getAsset() { return asset; }
        public double getFree() { return free; }
        public double getLocked() { return locked; }
        public double getTotal() { return total; }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("asset", asset);
            map.put("free", free);
            map.put("locked", locked);
            map.put("total", total);
            return map;
        }
    }
    
    // Estrutura de dados para candlestick
    public static class Candlestick {
        private final LocalDateTime timestamp;
        private final double open;
        private final double high;
        private final double low;
        private final double close;
        private final double volume;
        
        public Candlestick(LocalDateTime timestamp, double open, double high, double low, double close, double volume) {
            this.timestamp = timestamp;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }
        
        // Getters
        public LocalDateTime getTimestamp() { return timestamp; }
        public double getOpen() { return open; }
        public double getHigh() { return high; }
        public double getLow() { return low; }
        public double getClose() { return close; }
        public double getVolume() { return volume; }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            map.put("open", open);
            map.put("high", high);
            map.put("low", low);
            map.put("close", close);
            map.put("volume", volume);
            return map;
        }
    }
    
    // Estado da API
    private final String apiKey;
    private final String apiSecret;
    private final String environment;
    private final String baseUrl;
    private final Map<String, String> headers;
    private final HttpClient httpClient;
    private final Map<String, Object> cache;
    private final ScheduledExecutorService scheduler;
    private final Map<String, CompletableFuture<Void>> activeTasks;
    private final AtomicInteger totalRequests;
    private final AtomicInteger successfulRequests;
    private final AtomicInteger failedRequests;
    
    public PionexAPI(String apiKey, String apiSecret, String environment) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.environment = environment != null ? environment : "live";
        this.baseUrl = environment.equals("testnet") ? 
            "https://api.testnet.pionex.com" : "https://api.pionex.com";
        this.headers = new HashMap<>();
        this.headers.put("Content-Type", "application/json");
        this.headers.put("PIONEX-KEY", apiKey);
        this.cache = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.activeTasks = new HashMap<>();
        this.totalRequests = new AtomicInteger(0);
        this.successfulRequests = new AtomicInteger(0);
        this.failedRequests = new AtomicInteger(0);
        
        try {
            this.httpClient = HttpClient.newHttpClient();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar HttpClient", e);
        }
        
        System.out.println("🚀 PionexAPI inicializada");
        System.out.println("🌐 Ambiente: " + this.environment);
        System.out.println("🔑 API Key: " + apiKey.substring(0, 8) + "...");
        System.out.println("🔐 API Secret: " + apiSecret.substring(0, 8) + "...");
    }
    
    /**
     * Gera assinatura HMAC SHA256
     */
    private String generateSignature(String queryString, String timestamp) {
        try {
            String message = timestamp + queryString;
            SecretKeySpec keySpec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] signatureBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexSignature = new StringBuilder();
            for (byte b : signatureBytes) {
                hexSignature.append(String.format("%02x", b));
            }
            
            return hexSignature.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Erro ao gerar assinatura", e);
        }
    }
    
    /**
     * Faz requisição HTTP para a API
     */
    private CompletableFuture<Optional<Map<String, Object>>> makeRequest(String method, String endpoint,
                                                                         Map<String, String> params,
                                                                         Map<String, Object> data,
                                                                         boolean signed) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                totalRequests.incrementAndGet();
                
                String url = baseUrl + endpoint;
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("PIONEX-TIMESTAMP", String.valueOf(System.currentTimeMillis()));
                
                if (signed) {
                    String queryString = "";
                    if (params != null && !params.isEmpty()) {
                        queryString = URLEncoder.encode(params, StandardCharsets.UTF_8);
                    }
                    
                    String signature = generateSignature(queryString, 
                                    requestBuilder.build().headers().firstValue("PIONEX-TIMESTAMP").get());
                    requestBuilder.header("PIONEX-SIGNATURE", signature);
                }
                
                if (method.equals("GET")) {
                    if (params != null && !params.isEmpty()) {
                        url += "?" + queryString;
                    }
                    requestBuilder.GET();
                } else if (method.equals("POST")) {
                    String requestBody = "";
                    if (data != null) {
                        requestBody = mapToJson(data);
                    }
                    requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
                } else if (method.equals("DELETE")) {
                    if (params != null && !params.isEmpty()) {
                        url += "?" + queryString;
                    }
                    requestBuilder.DELETE();
                }
                
                HttpRequest request = requestBuilder.build();
                
                logger.info("📡 Requisição " + method + " " + url);
                
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    successfulRequests.incrementAndGet();
                    String responseBody = response.body();
                    Map<String, Object> result = parseJson(responseBody);
                    
                    logger.info("✅ Resposta " + method + " " + endpoint + " recebida");
                    return Optional.of(result);
                } else {
                    failedRequests.incrementAndGet();
                    logger.error("❌ Erro na requisição " + method + " " + endpoint + 
                                ": " + response.statusCode() + " - " + response.body());
                    return Optional.empty();
                }
                
            } catch (Exception e) {
                failedRequests.incrementAndGet();
                logger.error("❌ Exceção na requisição " + method + " " + endpoint + ": " + e.getMessage());
                return Optional.empty();
            }
        });
    }
    
    /**
     * Converte mapa para JSON
     */
    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":").append(entry.getValue()).append("\"");
            first = false;
        }
        json.append("}");
        return json.toString();
    }
    
    /**
     * Faz parse simples de JSON
     */
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (json.startsWith("{") && json.endsWith("}")) {
                String content = json.substring(1, json.length() - 1);
                String[] pairs = content.split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replace("\"", "");
                        String value = keyValue[1].trim().replace("\"", "");
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("❌ Erro ao parsear JSON: " + e.getMessage());
        }
        return map;
    }
    
    /**
     * Obtém informações da conta
     */
    public CompletableFuture<Optional<Map<String, Object>>> getAccountInfo() {
        return makeRequest("GET", "/api/v1/account", null, null, true);
    }
    
    /**
     * Obtém saldos
     */
    public CompletableFuture<List<Balance>> getBalances() {
        return makeRequest("GET", "/api/v1/account/balances", null, null, true)
                .thenApply(result -> result.map(r -> {
                    List<Balance> balances = new ArrayList<>();
                    if (r.containsKey("result")) {
                        Map<String, Object> resultData = (Map<String, Object>) r.get("result");
                        if (resultData.containsKey("balances")) {
                            List<Map<String, Object>> balancesData = (List<Map<String, Object>>) resultData.get("balances");
                            for (Map<String, Object> balanceData : balancesData) {
                                Balance balance = new Balance(
                                    (String) balanceData.get("asset"),
                                    ((Number) balanceData.get("free")).doubleValue(),
                                    ((Number) balanceData.get("locked")).doubleValue(),
                                    ((Number) balanceData.get("total")).doubleValue()
                                );
                                balances.add(balance);
                            }
                        }
                    }
                    return balances;
                }))
                .orElse(new ArrayList<>());
    }
    
    /**
     * Obtém informações da exchange
     */
    public CompletableFuture<Optional<Map<String, Object>>> getExchangeInfo() {
        return makeRequest("GET", "/api/v1/common/symbols", null, null, true);
    }
    
    /**
     * Obtém símbolos disponíveis
     */
    public CompletableFuture<List<String>> getSymbols() {
        return getExchangeInfo()
                .thenApply(result -> result.map(r -> {
                    List<String> symbols = new ArrayList<>();
                    if (r.containsKey("result")) {
                        Map<String, Object> resultData = (Map<String, Object>) r.get("result");
                        if (resultData.containsKey("symbols")) {
                            List<String> symbolsData = (List<String>) resultData.get("symbols");
                            symbols.addAll(symbolsData);
                        }
                    }
                    return symbols;
                }))
                .orElse(new ArrayList<>());
    }
    
    /**
     * Obtém ticker de um símbolo
     */
    public CompletableFuture<Optional<Ticker>> getTicker(String symbol) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        
        return makeRequest("GET", "/api/v1/market/ticker", params, null, true)
                .thenApply(result -> result.map(r -> {
                    if (r.containsKey("result")) {
                        Map<String, Object> tickerData = (Map<String, Object>) r.get("result");
                        return Optional.of(new Ticker(
                                symbol,
                                ((Number) tickerData.get("price")).doubleValue(),
                                ((Number) tickerData.get("bidPrice")).doubleValue(),
                                ((Number) tickerData.get("askPrice")).doubleValue(),
                                ((Number) tickerData.get("volume")).doubleValue(),
                                ((Number) tickerData.get("priceChange")).doubleValue(),
                                ((Number) tickerData.get("priceChangePercent")).doubleValue(),
                                ((Number) tickerData.get("highPrice")).doubleValue(),
                                ((Number) tickerData.get("lowPrice")).doubleValue()
                        ));
                    }
                    return Optional.empty();
                }))
                .orElse(Optional.empty());
    }
    
    /**
     * Obtém todos os tickers
     */
    public CompletableFuture<List<Ticker>> getAllTickers() {
        return makeRequest("GET", "/api/v1/market/tickers", null, null, true)
                .thenApply(result -> result.map(r -> {
                    List<Ticker> tickers = new ArrayList<>();
                    if (r.containsKey("result")) {
                        List<Map<String, Object>> tickersData = (List<Map<String, Object>>) r.get("result");
                        for (Map<String, Object> tickerData : tickersData) {
                            tickers.add(new Ticker(
                                    (String) tickerData.get("symbol"),
                                    ((Number) tickerData.get("price")).doubleValue(),
                                    ((Number) tickerData.get("bidPrice")).doubleValue(),
                                    ((Number) tickerData.get("askPrice")).doubleValue(),
                                    ((Number) tickerData.get("volume")).doubleValue(),
                                    ((Number) tickerData.get("priceChange")).doubleValue(),
                                    ((Number) tickerData.get("priceChangePercent")).doubleValue(),
                                    ((Number) tickerData.get("highPrice")).doubleValue(),
                                    ((Number) tickerData.get("lowPrice")).doubleValue()
                            ));
                        }
                    }
                    return tickers;
                }))
                .orElse(new ArrayList<>());
    }
    
    /**
     * Obtém order book
     */
    public CompletableFuture<Optional<Map<String, Object>>> getOrderBook(String symbol, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("limit", String.valueOf(limit));
        
        return makeRequest("GET", "/api/v1/market/depth", params, null, true)
                .thenApply(result -> result.map(r -> {
                    if (r.containsKey("result")) {
                        return Optional.of(r.get("result"));
                    }
                    return Optional.empty();
                }))
                .orElse(Optional.empty());
    }
    
    /**
     * Obtém dados OHLCV
     */
    public CompletableFuture<List<Candlestick>> getKlines(String symbol, String interval, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("interval", interval);
        params.put("limit", String.valueOf(limit));
        
        return makeRequest("GET", "/api/v1/market/klines", params, null, true)
                .thenApply(result -> result.map(r -> {
                    List<Candlestick> candlesticks = new ArrayList<>();
                    if (r.containsKey("result")) {
                        List<List<Object>> klinesData = (List<List<Object>>) r.get("result");
                        for (List<Object> kline : klinesData) {
                            if (kline.size() >= 6) {
                                Candlestick candlestick = new Candlestick(
                                        LocalDateTime.parse((String) kline.get(0)),
                                        ((Number) kline.get(1)).doubleValue(),
                                        ((Number) kline.get(2)).doubleValue(),
                                        ((Number) kline.get(3)).doubleValue(),
                                        ((Number) kline.get(4)).doubleValue(),
                                        ((Number) kline.get(5)).doubleValue()
                                );
                                candlesticks.add(candlestick);
                            }
                        }
                    }
                    return candlesticks;
                }))
                .orElse(new ArrayList<>());
    }
    
    /**
     * Coloca ordem manual
     */
    public CompletableFuture<Optional<Order>> placeOrder(String symbol, OrderSide side, OrderType type,
                                                 double quantity, double price, Double stopPrice) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("symbol", symbol);
        orderData.put("side", side.toString());
        orderData.put("type", type.toString());
        orderData.put("quantity", String.valueOf(quantity));
        
        if (price != 0) {
            orderData.put("price", String.valueOf(price));
        }
        
        if (stopPrice != null) {
            orderData.put("stopPrice", String.valueOf(stopPrice));
        }
        
        return makeRequest("POST", "/api/v1/trade/order", null, orderData, true)
                .thenApply(result -> result.map(r -> {
                    if (r.containsKey("result")) {
                        Map<String, Object> orderResult = (Map<String, Object>) r.get("result");
                        return Optional.of(new Order(
                                (String) orderResult.get("orderId"),
                                symbol,
                                side,
                                type,
                                quantity,
                                price,
                                stopPrice,
                                (String) orderResult.get("status"))
                        ));
                    }
                    return Optional.empty();
                }))
                .orElse(Optional.empty());
    }
    
    /**
     * Coloca ordem a mercado
     */
    public CompletableFuture<Optional<Order>> placeMarketOrder(String symbol, OrderSide side, double quantity) {
        return placeOrder(symbol, side, OrderType.MARKET, quantity, 0, null);
    }
    
    /**
     * Coloca ordem limitada
     */
    public CompletableFuture<Optional<Order>> placeLimitOrder(String symbol, OrderSide side, double quantity, double price) {
        return placeOrder(symbol, side, OrderType.LIMIT, quantity, price, null);
    }
    
    /**
     * Obtém ordem
     */
    public CompletableFuture<Optional<Order>> getOrder(String symbol, String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        
        return makeRequest("GET", "/api/v1/trade/order", params, null, true)
                .thenApply(result -> result.map(r -> {
                    if (r.containsKey("result")) {
                        Map<String, Object> orderResult = (Map<String, Object>) r.get("result");
                        return Optional.of(new Order(
                                orderId,
                                symbol,
                                OrderSide.valueOf((String) orderResult.get("side")),
                                OrderType.valueOf((String) orderResult.get("type")),
                                ((Number) orderResult.get("quantity")).doubleValue(),
                                ((Number) orderResult.get("price")).doubleValue(),
                                orderResult.containsKey("stopPrice") ? 
                                    ((Number) orderResult.get("stopPrice")).doubleValue() : null,
                                (String) orderResult.get("status"))
                        ));
                    }
                    return Optional.empty();
                }))
                .orElse(Optional.empty());
    }
    
    /**
     * Cancela ordem
     */
    public CompletableFuture<Boolean> cancelOrder(String symbol, String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        
        return makeRequest("DELETE", "/api/v1/trade/order", params, null, true)
                .thenApply(result -> result.map(r -> {
                    boolean success = r.containsKey("result");
                    if (success) {
                        logger.info("✅ Ordem " + orderId + " cancelada");
                    } else {
                        logger.error("❌ Falha ao cancelar ordem " + orderId);
                    }
                    return success;
                }))
                .orElse(false);
    }
    
    /**
     * Obtém ordens abertas
     */
    public CompletableFuture<List<Order>> getOpenOrders(String symbol) {
        Map<String, String> params = new HashMap<>();
        if (symbol != null) {
            params.put("symbol", symbol);
        }
        
        return makeRequest("GET", "/api/v1/trade/openOrders", params, null, true)
                .thenApply(result -> result.map(r -> {
                    List<Order> orders = new ArrayList<>();
                    if (r.containsKey("result")) {
                        List<Map<String, Object>> ordersData = (List<Map<String, Object>>) r.get("result");
                        for (Map<String, Object> orderData : ordersData) {
                            orders.add(new Order(
                                    (String) orderData.get("orderId"),
                                    (String) orderData.get("symbol"),
                                    OrderSide.valueOf((String) orderData.get("side")),
                                    OrderType.valueOf((String) orderData.get("type")),
                                    ((Number) orderData.get("quantity")).doubleValue(),
                                    ((Number) orderData.get("price")).doubleValue(),
                                    orderData.containsKey("stopPrice") ? 
                                        ((Number) orderData.get("stopPrice")).doubleValue() : null,
                                    (String) orderData.get("status"))
                            ));
                        }
                    }
                    return orders;
                }))
                .orElse(new ArrayList<>());
    }
    
    /**
     * Obtém histórico de ordens
     */
    public CompletableFuture<List<Order>> getOrderHistory(String symbol, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(limit));
        if (symbol != null) {
            params.put("symbol", symbol);
        }
        
        return makeRequest("GET", "/api/v1/trade/allOrders", params, null, true)
                .thenApply(result -> result.map(r -> {
                    List<Order> orders = new ArrayList<>();
                    if (r.containsKey("result")) {
                        List<Map<String, Object>> ordersData = (List<Map<String, Object>>) r.get("result");
                        for (Map<String, Object> orderData : ordersData) {
                            orders.add(new Order(
                                    (String) orderData.get("orderId"),
                                    (String) orderData.get("symbol"),
                                    OrderSide.valueOf((String) orderData.get("side")),
                                    OrderType.valueOf((String) orderData.get("type")),
                                    ((Number) orderData.get("quantity")).doubleValue(),
                                    ((Number) orderData.get("price")).doubleValue(),
                                    orderData.containsKey("stopPrice") ? 
                                        ((Number) orderData.get("stopPrice")).doubleValue() : null,
                                    (String) orderData.get("status"))
                            ));
                        }
                    }
                    return orders;
                }))
                .orElse(new ArrayList<>());
    }
    
    /**
     * Obtém estatísticas da API
     */
    public Map<String, Object> getAPIStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("environment", environment);
        stats.put("base_url", baseUrl);
        stats.put("total_requests", totalRequests.get());
        stats.put("successful_requests", successfulRequests.get());
        stats.put("failed_requests", failedRequests.get());
        stats.put("success_rate", totalRequests.get() > 0 ? 
                   (double) successfulRequests.get() / totalRequests.get() * 100 : 0.0);
        stats.put("cache_size", cache.size());
        stats.put("active_tasks", activeTasks.size());
        
        return stats;
    }
    
    /**
     * Testa conectividade
     */
    public CompletableFuture<Boolean> testConnectivity() {
        return makeRequest("GET", "/api/v1/common/ping", null, null, true)
                .thenApply(result -> {
                    boolean success = result.isPresent();
                    if (success) {
                        logger.info("✅ Conectividade com Pionex OK");
                    } else {
                        logger.error("❌ Falha na conectividade com Pionex");
                    }
                    return success;
                }))
                .orElse(false);
    }
    
    /**
     * Obtém status do sistema
     */
    public CompletableFuture<Optional<Map<String, Object>>> getSystemStatus() {
        return makeRequest("GET", "/api/v1/common/systemStatus", null, null, true);
    }
    
    /**
     * Método principal para demonstração
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("🚀 DEMONSTRAÇÃO DA PIONEX API");
        System.out.println("=".repeat(80));
        
        // Configuração (em produção, usar credenciais reais)
        PionexAPI api = new PionexAPI("your_api_key", "your_api_secret", "live");
        
        // Testa conectividade
        api.testConnectivity().thenAccept(connected -> {
            System.out.println("🔌 Conectividade: " + (connected ? "OK" : "FALHA"));
        });
        
        // Obtém informações da conta
        api.getAccountInfo().thenAccept(accountInfo -> {
            System.out.println("👤 Informações da conta: " + (accountInfo.isPresent() ? "OK" : "FALHA"));
        });
        
        // Obtém tickers
        api.getAllTickers().thenAccept(tickers -> {
            System.out.println("📈 Tickers obtidos: " + tickers.size());
        });
        
        // Exibe estatísticas
        Map<String, Object> stats = api.getAPIStatistics();
        System.out.println("\n📊 Estatísticas da API:");
        stats.forEach((key, value) -> System.out.println("  " + key + ": " + value));
        
        System.out.println("\n✅ DEMONSTRAÇÃO CONCLUÍDA!");
        System.out.println("=".repeat(80));
    }
}
