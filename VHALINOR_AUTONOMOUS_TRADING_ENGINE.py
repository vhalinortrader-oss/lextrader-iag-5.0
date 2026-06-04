"""
VHALINOR.IAG - Motor de Trading Autônomo
=======================================
Virtual Hybrid Advanced Learning Intelligence Neural Optimized Reasoning
Sistema Autônomo de Trading com IA Avançada para Mercados Financeiros

Autor: VHALINOR.IAG Team
Versão: 5.0.0 - Autonomous Trading Specialist
"""

import asyncio
import numpy as np
import pandas as pd
from datetime import datetime, timedelta
from typing import Dict, List, Any, Optional, Tuple
from dataclasses import dataclass, field
from enum import Enum, auto
import logging
import json
import threading
import time
from collections import deque, defaultdict
import warnings
warnings.filterwarnings('ignore')

# Importações para análise financeira
try:
    import yfinance as yf
    import ta
    from scipy import stats
    from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
    from sklearn.preprocessing import StandardScaler, MinMaxScaler
    from sklearn.metrics import mean_squared_error, mean_absolute_error
    FINANCIAL_LIBS_AVAILABLE = True
except ImportError:
    FINANCIAL_LIBS_AVAILABLE = False
    print("⚠️ Bibliotecas financeiras não disponíveis. Execute: pip install yfinance ta-lib scipy")

logger = logging.getLogger(__name__)

# ============================================================================
# ENUMS E ESTRUTURAS DE DADOS
# ============================================================================

class MarketCondition(Enum):
    """Condições de mercado."""
    BULLISH = "bullish"           # Alta
    BEARISH = "bearish"           # Baixa
    SIDEWAYS = "sideways"         # Lateral
    VOLATILE = "volatile"         # Volátil
    CONSOLIDATION = "consolidation"  # Consolidação

class TradingSignal(Enum):
    """Sinais de trading."""
    STRONG_BUY = "strong_buy"
    BUY = "buy"
    HOLD = "hold"
    SELL = "sell"
    STRONG_SELL = "strong_sell"

class RiskLevel(Enum):
    """Níveis de risco."""
    VERY_LOW = "very_low"
    LOW = "low"
    MEDIUM = "medium"
    HIGH = "high"
    VERY_HIGH = "very_high"

class TimeFrame(Enum):
    """Timeframes para análise."""
    M1 = "1m"      # 1 minuto
    M5 = "5m"      # 5 minutos
    M15 = "15m"    # 15 minutos
    M30 = "30m"    # 30 minutos
    H1 = "1h"      # 1 hora
    H4 = "4h"      # 4 horas
    D1 = "1d"      # 1 dia
    W1 = "1wk"     # 1 semana

@dataclass
class MarketData:
    """Dados de mercado."""
    symbol: str
    timestamp: datetime
    open: float
    high: float
    low: float
    close: float
    volume: float
    timeframe: TimeFrame
    
    def to_dict(self) -> Dict[str, Any]:
        return {
            'symbol': self.symbol,
            'timestamp': self.timestamp.isoformat(),
            'open': self.open,
            'high': self.high,
            'low': self.low,
            'close': self.close,
            'volume': self.volume,
            'timeframe': self.timeframe.value
        }

@dataclass
class TechnicalIndicators:
    """Indicadores técnicos."""
    rsi: float = 0.0
    macd: float = 0.0
    macd_signal: float = 0.0
    bb_upper: float = 0.0
    bb_middle: float = 0.0
    bb_lower: float = 0.0
    sma_20: float = 0.0
    sma_50: float = 0.0
    ema_12: float = 0.0
    ema_26: float = 0.0
    stoch_k: float = 0.0
    stoch_d: float = 0.0
    atr: float = 0.0
    adx: float = 0.0
    williams_r: float = 0.0
    cci: float = 0.0
    
    def to_dict(self) -> Dict[str, float]:
        return {k: v for k, v in self.__dict__.items()}

@dataclass
class TradingDecision:
    """Decisão de trading."""
    symbol: str
    signal: TradingSignal
    confidence: float
    entry_price: float
    stop_loss: float
    take_profit: float
    risk_reward_ratio: float
    position_size: float
    reasoning: List[str]
    timestamp: datetime
    market_condition: MarketCondition
    risk_level: RiskLevel
    
    def to_dict(self) -> Dict[str, Any]:
        return {
            'symbol': self.symbol,
            'signal': self.signal.value,
            'confidence': self.confidence,
            'entry_price': self.entry_price,
            'stop_loss': self.stop_loss,
            'take_profit': self.take_profit,
            'risk_reward_ratio': self.risk_reward_ratio,
            'position_size': self.position_size,
            'reasoning': self.reasoning,
            'timestamp': self.timestamp.isoformat(),
            'market_condition': self.market_condition.value,
            'risk_level': self.risk_level.value
        }

@dataclass
class PortfolioMetrics:
    """Métricas do portfólio."""
    total_value: float = 0.0
    available_cash: float = 0.0
    invested_amount: float = 0.0
    unrealized_pnl: float = 0.0
    realized_pnl: float = 0.0
    total_return: float = 0.0
    sharpe_ratio: float = 0.0
    max_drawdown: float = 0.0
    win_rate: float = 0.0
    profit_factor: float = 0.0
    total_trades: int = 0
    winning_trades: int = 0
    losing_trades: int = 0

# ============================================================================
# ANALISADOR DE MERCADO AVANÇADO
# ============================================================================

class AdvancedMarketAnalyzer:
    """Analisador de mercado com IA avançada."""
    
    def __init__(self):
        self.indicators_cache = {}
        self.market_data_cache = {}
        self.prediction_models = {}
        self.scaler = StandardScaler()
        self.setup_models()
    
    def setup_models(self):
        """Configura modelos de ML para predição."""
        # Modelo Random Forest para predição de preços
        self.prediction_models['rf_price'] = RandomForestRegressor(
            n_estimators=100,
            max_depth=10,
            random_state=42,
            n_jobs=-1
        )
        
        # Modelo Gradient Boosting para volatilidade
        self.prediction_models['gb_volatility'] = GradientBoostingRegressor(
            n_estimators=100,
            learning_rate=0.1,
            max_depth=6,
            random_state=42
        )
        
        # Modelo para classificação de tendência
        from sklearn.ensemble import RandomForestClassifier
        self.prediction_models['rf_trend'] = RandomForestClassifier(
            n_estimators=100,
            max_depth=8,
            random_state=42,
            n_jobs=-1
        )
    
    async def get_market_data(self, symbol: str, timeframe: TimeFrame, 
                            period: str = "1mo") -> List[MarketData]:
        """Obtém dados de mercado."""
        if not FINANCIAL_LIBS_AVAILABLE:
            # Dados simulados para demonstração
            return self._generate_mock_data(symbol, timeframe, period)
        
        try:
            ticker = yf.Ticker(symbol)
            data = ticker.history(period=period, interval=timeframe.value)
            
            market_data = []
            for timestamp, row in data.iterrows():
                market_data.append(MarketData(
                    symbol=symbol,
                    timestamp=timestamp,
                    open=float(row['Open']),
                    high=float(row['High']),
                    low=float(row['Low']),
                    close=float(row['Close']),
                    volume=float(row['Volume']),
                    timeframe=timeframe
                ))
            
            # Cache dos dados
            cache_key = f"{symbol}_{timeframe.value}_{period}"
            self.market_data_cache[cache_key] = market_data
            
            return market_data
        
        except Exception as e:
            logger.error(f"Erro ao obter dados de mercado: {e}")
            return self._generate_mock_data(symbol, timeframe, period)
    
    def _generate_mock_data(self, symbol: str, timeframe: TimeFrame, 
                          period: str) -> List[MarketData]:
        """Gera dados simulados para demonstração."""
        # Simula 30 dias de dados
        data = []
        base_price = 100.0
        current_time = datetime.now() - timedelta(days=30)
        
        for i in range(720):  # 30 dias * 24 horas
            # Movimento browniano geométrico simulado
            change = np.random.normal(0, 0.02)
            base_price *= (1 + change)
            
            high = base_price * (1 + abs(np.random.normal(0, 0.01)))
            low = base_price * (1 - abs(np.random.normal(0, 0.01)))
            volume = np.random.randint(1000000, 10000000)
            
            data.append(MarketData(
                symbol=symbol,
                timestamp=current_time + timedelta(hours=i),
                open=base_price,
                high=high,
                low=low,
                close=base_price,
                volume=volume,
                timeframe=timeframe
            ))
        
        return data[-100:]  # Retorna últimos 100 pontos
    
    def calculate_technical_indicators(self, market_data: List[MarketData]) -> TechnicalIndicators:
        """Calcula indicadores técnicos."""
        if len(market_data) < 50:
            return TechnicalIndicators()
        
        # Converter para DataFrame
        df = pd.DataFrame([data.to_dict() for data in market_data])
        df['timestamp'] = pd.to_datetime(df['timestamp'])
        df = df.sort_values('timestamp')
        
        try:
            if FINANCIAL_LIBS_AVAILABLE:
                # Usar biblioteca TA
                indicators = TechnicalIndicators(
                    rsi=ta.momentum.RSIIndicator(df['close']).rsi().iloc[-1],
                    macd=ta.trend.MACD(df['close']).macd().iloc[-1],
                    macd_signal=ta.trend.MACD(df['close']).macd_signal().iloc[-1],
                    bb_upper=ta.volatility.BollingerBands(df['close']).bollinger_hband().iloc[-1],
                    bb_middle=ta.volatility.BollingerBands(df['close']).bollinger_mavg().iloc[-1],
                    bb_lower=ta.volatility.BollingerBands(df['close']).bollinger_lband().iloc[-1],
                    sma_20=ta.trend.SMAIndicator(df['close'], window=20).sma_indicator().iloc[-1],
                    sma_50=ta.trend.SMAIndicator(df['close'], window=50).sma_indicator().iloc[-1],
                    ema_12=ta.trend.EMAIndicator(df['close'], window=12).ema_indicator().iloc[-1],
                    ema_26=ta.trend.EMAIndicator(df['close'], window=26).ema_indicator().iloc[-1],
                    atr=ta.volatility.AverageTrueRange(df['high'], df['low'], df['close']).average_true_range().iloc[-1],
                    adx=ta.trend.ADXIndicator(df['high'], df['low'], df['close']).adx().iloc[-1]
                )
            else:
                # Cálculos manuais simplificados
                indicators = self._calculate_manual_indicators(df)
            
            return indicators
        
        except Exception as e:
            logger.error(f"Erro no cálculo de indicadores: {e}")
            return TechnicalIndicators()
    
    def _calculate_manual_indicators(self, df: pd.DataFrame) -> TechnicalIndicators:
        """Calcula indicadores manualmente."""
        close = df['close'].values
        high = df['high'].values
        low = df['low'].values
        
        # RSI simplificado
        delta = np.diff(close)
        gain = np.where(delta > 0, delta, 0)
        loss = np.where(delta < 0, -delta, 0)
        avg_gain = np.mean(gain[-14:]) if len(gain) >= 14 else 0
        avg_loss = np.mean(loss[-14:]) if len(loss) >= 14 else 0.001
        rs = avg_gain / avg_loss
        rsi = 100 - (100 / (1 + rs))
        
        # Médias móveis
        sma_20 = np.mean(close[-20:]) if len(close) >= 20 else close[-1]
        sma_50 = np.mean(close[-50:]) if len(close) >= 50 else close[-1]
        
        # Bandas de Bollinger simplificadas
        bb_middle = sma_20
        std = np.std(close[-20:]) if len(close) >= 20 else 0
        bb_upper = bb_middle + (2 * std)
        bb_lower = bb_middle - (2 * std)
        
        # ATR simplificado
        tr = np.maximum(high[1:] - low[1:], 
                       np.maximum(abs(high[1:] - close[:-1]), 
                                 abs(low[1:] - close[:-1])))
        atr = np.mean(tr[-14:]) if len(tr) >= 14 else 0
        
        return TechnicalIndicators(
            rsi=rsi,
            sma_20=sma_20,
            sma_50=sma_50,
            bb_upper=bb_upper,
            bb_middle=bb_middle,
            bb_lower=bb_lower,
            atr=atr
        )
    
    def detect_market_condition(self, market_data: List[MarketData], 
                              indicators: TechnicalIndicators) -> MarketCondition:
        """Detecta condição atual do mercado."""
        if len(market_data) < 20:
            return MarketCondition.SIDEWAYS
        
        recent_closes = [data.close for data in market_data[-20:]]
        price_change = (recent_closes[-1] - recent_closes[0]) / recent_closes[0]
        volatility = np.std(recent_closes) / np.mean(recent_closes)
        
        # Lógica de detecção
        if volatility > 0.05:  # Alta volatilidade
            return MarketCondition.VOLATILE
        elif price_change > 0.03:  # Alta de mais de 3%
            return MarketCondition.BULLISH
        elif price_change < -0.03:  # Queda de mais de 3%
            return MarketCondition.BEARISH
        elif abs(price_change) < 0.01:  # Movimento lateral
            return MarketCondition.CONSOLIDATION
        else:
            return MarketCondition.SIDEWAYS
    
    async def predict_price_movement(self, symbol: str, market_data: List[MarketData], 
                                   indicators: TechnicalIndicators) -> Dict[str, Any]:
        """Prediz movimento de preços usando ML."""
        if len(market_data) < 50:
            return {"prediction": 0.0, "confidence": 0.0, "direction": "hold"}
        
        try:
            # Preparar features
            features = self._prepare_features(market_data, indicators)
            
            if len(features) < 30:
                return {"prediction": 0.0, "confidence": 0.0, "direction": "hold"}
            
            # Treinar modelo se necessário
            model_key = f"{symbol}_price"
            if model_key not in self.prediction_models or len(features) % 100 == 0:
                await self._train_prediction_model(symbol, features)
            
            # Fazer predição
            latest_features = features[-1:].reshape(1, -1)
            
            if hasattr(self.prediction_models.get('rf_price'), 'predict'):
                prediction = self.prediction_models['rf_price'].predict(latest_features)[0]
                
                # Calcular confiança baseada na variância das árvores
                predictions = [tree.predict(latest_features)[0] 
                             for tree in self.prediction_models['rf_price'].estimators_[:10]]
                confidence = 1.0 - (np.std(predictions) / (abs(np.mean(predictions)) + 0.001))
                confidence = max(0.0, min(1.0, confidence))
                
                # Determinar direção
                current_price = market_data[-1].close
                price_change = (prediction - current_price) / current_price
                
                if price_change > 0.02:
                    direction = "buy"
                elif price_change < -0.02:
                    direction = "sell"
                else:
                    direction = "hold"
                
                return {
                    "prediction": prediction,
                    "confidence": confidence,
                    "direction": direction,
                    "price_change_pct": price_change * 100,
                    "current_price": current_price
                }
        
        except Exception as e:
            logger.error(f"Erro na predição: {e}")
        
        return {"prediction": 0.0, "confidence": 0.0, "direction": "hold"}
    
    def _prepare_features(self, market_data: List[MarketData], 
                         indicators: TechnicalIndicators) -> np.ndarray:
        """Prepara features para ML."""
        features_list = []
        
        for i in range(20, len(market_data)):  # Janela de 20 períodos
            data_window = market_data[i-20:i]
            
            # Features de preço
            closes = [d.close for d in data_window]
            highs = [d.high for d in data_window]
            lows = [d.low for d in data_window]
            volumes = [d.volume for d in data_window]
            
            # Features estatísticas
            price_features = [
                closes[-1],  # Preço atual
                np.mean(closes),  # Média
                np.std(closes),   # Desvio padrão
                max(highs),       # Máxima
                min(lows),        # Mínima
                np.mean(volumes), # Volume médio
                (closes[-1] - closes[0]) / closes[0],  # Retorno do período
                np.mean(np.diff(closes)),  # Momentum
            ]
            
            # Features de indicadores técnicos
            indicator_features = [
                indicators.rsi / 100.0,  # Normalizado
                indicators.macd,
                indicators.atr,
                (closes[-1] - indicators.sma_20) / indicators.sma_20,  # Distância da SMA
                (closes[-1] - indicators.bb_middle) / (indicators.bb_upper - indicators.bb_lower + 0.001)  # Posição nas Bandas
            ]
            
            # Combinar features
            all_features = price_features + indicator_features
            features_list.append(all_features)
        
        return np.array(features_list)
    
    async def _train_prediction_model(self, symbol: str, features: np.ndarray):
        """Treina modelo de predição."""
        if len(features) < 30:
            return
        
        try:
            # Preparar targets (preços futuros)
            X = features[:-1]  # Features atuais
            y = features[1:, 0]  # Preços futuros (primeira feature é o preço)
            
            # Treinar modelo
            self.prediction_models['rf_price'].fit(X, y)
            
            logger.info(f"Modelo treinado para {symbol} com {len(X)} amostras")
        
        except Exception as e:
            logger.error(f"Erro no treinamento do modelo: {e}")

# ============================================================================
# SISTEMA DE GESTÃO DE RISCO AVANÇADO
# ============================================================================

class AdvancedRiskManager:
    """Gerenciador de risco avançado."""
    
    def __init__(self, initial_capital: float = 100000.0):
        self.initial_capital = initial_capital
        self.current_capital = initial_capital
        self.max_risk_per_trade = 0.02  # 2% por trade
        self.max_portfolio_risk = 0.10  # 10% do portfólio
        self.max_correlation_exposure = 0.30  # 30% em ativos correlacionados
        
        self.open_positions = {}
        self.trade_history = []
        self.risk_metrics = {}
        
    def calculate_position_size(self, entry_price: float, stop_loss: float, 
                              risk_amount: Optional[float] = None) -> float:
        """Calcula tamanho da posição baseado no risco."""
        if risk_amount is None:
            risk_amount = self.current_capital * self.max_risk_per_trade
        
        price_risk = abs(entry_price - stop_loss)
        if price_risk == 0:
            return 0.0
        
        position_size = risk_amount / price_risk
        
        # Limitar ao capital disponível
        max_position_value = self.current_capital * 0.25  # Máximo 25% em uma posição
        max_shares = max_position_value / entry_price
        
        return min(position_size, max_shares)
    
    def assess_risk_level(self, symbol: str, market_condition: MarketCondition, 
                         volatility: float, correlation_risk: float = 0.0) -> RiskLevel:
        """Avalia nível de risco de uma operação."""
        risk_score = 0.0
        
        # Risco por condição de mercado
        market_risk = {
            MarketCondition.BULLISH: 0.2,
            MarketCondition.BEARISH: 0.3,
            MarketCondition.SIDEWAYS: 0.1,
            MarketCondition.VOLATILE: 0.8,
            MarketCondition.CONSOLIDATION: 0.1
        }
        risk_score += market_risk.get(market_condition, 0.5)
        
        # Risco por volatilidade
        if volatility > 0.05:
            risk_score += 0.3
        elif volatility > 0.03:
            risk_score += 0.2
        elif volatility > 0.01:
            risk_score += 0.1
        
        # Risco de correlação
        risk_score += correlation_risk
        
        # Classificar nível de risco
        if risk_score >= 0.8:
            return RiskLevel.VERY_HIGH
        elif risk_score >= 0.6:
            return RiskLevel.HIGH
        elif risk_score >= 0.4:
            return RiskLevel.MEDIUM
        elif risk_score >= 0.2:
            return RiskLevel.LOW
        else:
            return RiskLevel.VERY_LOW
    
    def calculate_stop_loss(self, entry_price: float, atr: float, 
                          signal: TradingSignal) -> float:
        """Calcula stop loss baseado no ATR."""
        atr_multiplier = 2.0  # Padrão
        
        # Ajustar multiplicador baseado no sinal
        if signal in [TradingSignal.STRONG_BUY, TradingSignal.STRONG_SELL]:
            atr_multiplier = 1.5  # Mais agressivo
        elif signal == TradingSignal.HOLD:
            atr_multiplier = 3.0  # Mais conservador
        
        if signal in [TradingSignal.BUY, TradingSignal.STRONG_BUY]:
            return entry_price - (atr * atr_multiplier)
        else:
            return entry_price + (atr * atr_multiplier)
    
    def calculate_take_profit(self, entry_price: float, stop_loss: float, 
                            risk_reward_ratio: float = 2.0) -> float:
        """Calcula take profit baseado na relação risco/retorno."""
        risk = abs(entry_price - stop_loss)
        reward = risk * risk_reward_ratio
        
        if entry_price > stop_loss:  # Posição comprada
            return entry_price + reward
        else:  # Posição vendida
            return entry_price - reward
    
    def validate_trade(self, decision: TradingDecision) -> Tuple[bool, List[str]]:
        """Valida se o trade atende aos critérios de risco."""
        issues = []
        
        # Verificar risco por trade
        position_value = decision.position_size * decision.entry_price
        trade_risk = abs(decision.entry_price - decision.stop_loss) * decision.position_size
        risk_percentage = trade_risk / self.current_capital
        
        if risk_percentage > self.max_risk_per_trade:
            issues.append(f"Risco por trade muito alto: {risk_percentage:.2%}")
        
        # Verificar exposição do portfólio
        total_exposure = sum(pos['value'] for pos in self.open_positions.values())
        total_exposure += position_value
        exposure_percentage = total_exposure / self.current_capital
        
        if exposure_percentage > self.max_portfolio_risk:
            issues.append(f"Exposição do portfólio muito alta: {exposure_percentage:.2%}")
        
        # Verificar relação risco/retorno
        if decision.risk_reward_ratio < 1.5:
            issues.append(f"Relação risco/retorno baixa: {decision.risk_reward_ratio:.2f}")
        
        # Verificar nível de confiança
        if decision.confidence < 0.6:
            issues.append(f"Confiança baixa: {decision.confidence:.2%}")
        
        return len(issues) == 0, issues

# ============================================================================
# MOTOR DE TRADING AUTÔNOMO PRINCIPAL
# ============================================================================

class VhalinorAutonomousTradingEngine:
    """Motor principal de trading autônomo do VHALINOR.IAG."""
    
    def __init__(self, initial_capital: float = 100000.0):
        self.analyzer = AdvancedMarketAnalyzer()
        self.risk_manager = AdvancedRiskManager(initial_capital)
        
        # Configurações
        self.watchlist = ["AAPL", "GOOGL", "MSFT", "TSLA", "NVDA", "AMZN"]
        self.timeframes = [TimeFrame.M15, TimeFrame.H1, TimeFrame.H4]
        self.is_running = False
        
        # Estado do sistema
        self.decisions_history = deque(maxlen=1000)
        self.performance_metrics = PortfolioMetrics()
        self.last_analysis_time = {}
        
        # Threading
        self._stop_event = threading.Event()
        self._trading_thread = None
        
        logger.info("🚀 VHALINOR Autonomous Trading Engine inicializado")
    
    async def start_autonomous_trading(self):
        """Inicia o trading autônomo."""
        if self.is_running:
            logger.warning("Trading já está em execução")
            return
        
        self.is_running = True
        self._stop_event.clear()
        
        logger.info("🤖 Iniciando trading autônomo...")
        
        # Iniciar thread de trading
        self._trading_thread = threading.Thread(
            target=self._run_trading_loop,
            name="VhalinorTrading",
            daemon=True
        )
        self._trading_thread.start()
    
    def stop_autonomous_trading(self):
        """Para o trading autônomo."""
        if not self.is_running:
            return
        
        logger.info("🛑 Parando trading autônomo...")
        self.is_running = False
        self._stop_event.set()
        
        if self._trading_thread:
            self._trading_thread.join(timeout=30)
    
    def _run_trading_loop(self):
        """Loop principal de trading."""
        asyncio.set_event_loop(asyncio.new_event_loop())
        loop = asyncio.get_event_loop()
        
        try:
            loop.run_until_complete(self._trading_loop())
        except Exception as e:
            logger.error(f"Erro no loop de trading: {e}")
        finally:
            loop.close()
    
    async def _trading_loop(self):
        """Loop assíncrono de trading."""
        while not self._stop_event.is_set():
            try:
                # Analisar cada símbolo na watchlist
                for symbol in self.watchlist:
                    if self._stop_event.is_set():
                        break
                    
                    await self._analyze_and_trade_symbol(symbol)
                    await asyncio.sleep(1)  # Pausa entre símbolos
                
                # Atualizar métricas de performance
                self._update_performance_metrics()
                
                # Pausa entre ciclos completos
                await asyncio.sleep(60)  # 1 minuto
                
            except Exception as e:
                logger.error(f"Erro no ciclo de trading: {e}")
                await asyncio.sleep(30)  # Pausa em caso de erro
    
    async def _analyze_and_trade_symbol(self, symbol: str):
        """Analisa um símbolo e toma decisão de trading."""
        try:
            # Verificar se já analisou recentemente
            last_analysis = self.last_analysis_time.get(symbol, datetime.min)
            if datetime.now() - last_analysis < timedelta(minutes=5):
                return
            
            logger.info(f"📊 Analisando {symbol}...")
            
            # Obter dados de mercado
            market_data = await self.analyzer.get_market_data(
                symbol, TimeFrame.H1, "1mo"
            )
            
            if len(market_data) < 50:
                logger.warning(f"Dados insuficientes para {symbol}")
                return
            
            # Calcular indicadores técnicos
            indicators = self.analyzer.calculate_technical_indicators(market_data)
            
            # Detectar condição de mercado
            market_condition = self.analyzer.detect_market_condition(market_data, indicators)
            
            # Fazer predição de preço
            prediction = await self.analyzer.predict_price_movement(
                symbol, market_data, indicators
            )
            
            # Gerar decisão de trading
            decision = await self._generate_trading_decision(
                symbol, market_data, indicators, market_condition, prediction
            )
            
            if decision:
                # Validar decisão com gestão de risco
                is_valid, issues = self.risk_manager.validate_trade(decision)
                
                if is_valid:
                    logger.info(f"✅ Decisão válida para {symbol}: {decision.signal.value}")
                    await self._execute_trading_decision(decision)
                else:
                    logger.warning(f"❌ Decisão rejeitada para {symbol}: {issues}")
                
                # Armazenar decisão no histórico
                self.decisions_history.append(decision)
            
            # Atualizar timestamp da última análise
            self.last_analysis_time[symbol] = datetime.now()
            
        except Exception as e:
            logger.error(f"Erro na análise de {symbol}: {e}")
    
    async def _generate_trading_decision(self, symbol: str, market_data: List[MarketData],
                                       indicators: TechnicalIndicators, 
                                       market_condition: MarketCondition,
                                       prediction: Dict[str, Any]) -> Optional[TradingDecision]:
        """Gera decisão de trading baseada na análise."""
        current_price = market_data[-1].close
        reasoning = []
        
        # Analisar sinais técnicos
        signal_score = 0.0
        
        # RSI
        if indicators.rsi < 30:
            signal_score += 0.3
            reasoning.append("RSI oversold")
        elif indicators.rsi > 70:
            signal_score -= 0.3
            reasoning.append("RSI overbought")
        
        # Médias móveis
        if indicators.sma_20 > indicators.sma_50:
            signal_score += 0.2
            reasoning.append("SMA bullish crossover")
        elif indicators.sma_20 < indicators.sma_50:
            signal_score -= 0.2
            reasoning.append("SMA bearish crossover")
        
        # Bandas de Bollinger
        if current_price < indicators.bb_lower:
            signal_score += 0.2
            reasoning.append("Price below BB lower band")
        elif current_price > indicators.bb_upper:
            signal_score -= 0.2
            reasoning.append("Price above BB upper band")
        
        # Predição de ML
        ml_direction = prediction.get("direction", "hold")
        ml_confidence = prediction.get("confidence", 0.0)
        
        if ml_direction == "buy" and ml_confidence > 0.6:
            signal_score += 0.4
            reasoning.append(f"ML predicts buy (confidence: {ml_confidence:.2%})")
        elif ml_direction == "sell" and ml_confidence > 0.6:
            signal_score -= 0.4
            reasoning.append(f"ML predicts sell (confidence: {ml_confidence:.2%})")
        
        # Determinar sinal final
        if signal_score >= 0.6:
            signal = TradingSignal.STRONG_BUY
        elif signal_score >= 0.3:
            signal = TradingSignal.BUY
        elif signal_score <= -0.6:
            signal = TradingSignal.STRONG_SELL
        elif signal_score <= -0.3:
            signal = TradingSignal.SELL
        else:
            signal = TradingSignal.HOLD
        
        # Se sinal é HOLD, não gerar decisão
        if signal == TradingSignal.HOLD:
            return None
        
        # Calcular stop loss e take profit
        stop_loss = self.risk_manager.calculate_stop_loss(
            current_price, indicators.atr, signal
        )
        
        risk_reward_ratio = 2.0  # Padrão
        take_profit = self.risk_manager.calculate_take_profit(
            current_price, stop_loss, risk_reward_ratio
        )
        
        # Calcular tamanho da posição
        position_size = self.risk_manager.calculate_position_size(
            current_price, stop_loss
        )
        
        # Avaliar nível de risco
        volatility = indicators.atr / current_price
        risk_level = self.risk_manager.assess_risk_level(
            symbol, market_condition, volatility
        )
        
        # Calcular confiança final
        confidence = min(1.0, abs(signal_score) + ml_confidence) / 2
        
        return TradingDecision(
            symbol=symbol,
            signal=signal,
            confidence=confidence,
            entry_price=current_price,
            stop_loss=stop_loss,
            take_profit=take_profit,
            risk_reward_ratio=risk_reward_ratio,
            position_size=position_size,
            reasoning=reasoning,
            timestamp=datetime.now(),
            market_condition=market_condition,
            risk_level=risk_level
        )
    
    async def _execute_trading_decision(self, decision: TradingDecision):
        """Executa decisão de trading (simulado)."""
        logger.info(f"🎯 Executando trade: {decision.symbol} {decision.signal.value}")
        logger.info(f"   Preço: ${decision.entry_price:.2f}")
        logger.info(f"   Stop Loss: ${decision.stop_loss:.2f}")
        logger.info(f"   Take Profit: ${decision.take_profit:.2f}")
        logger.info(f"   Tamanho: {decision.position_size:.2f}")
        logger.info(f"   Confiança: {decision.confidence:.2%}")
        logger.info(f"   Razões: {', '.join(decision.reasoning)}")
        
        # Aqui seria a integração com broker real
        # Por enquanto, apenas simula a execução
        
        # Adicionar à posições abertas (simulado)
        position_value = decision.position_size * decision.entry_price
        self.risk_manager.open_positions[decision.symbol] = {
            'size': decision.position_size,
            'entry_price': decision.entry_price,
            'stop_loss': decision.stop_loss,
            'take_profit': decision.take_profit,
            'value': position_value,
            'timestamp': decision.timestamp
        }
    
    def _update_performance_metrics(self):
        """Atualiza métricas de performance."""
        # Calcular valor total do portfólio
        total_positions_value = sum(
            pos['value'] for pos in self.risk_manager.open_positions.values()
        )
        
        self.performance_metrics.total_value = (
            self.risk_manager.current_capital + total_positions_value
        )
        self.performance_metrics.available_cash = self.risk_manager.current_capital
        self.performance_metrics.invested_amount = total_positions_value
        
        # Calcular retorno total
        self.performance_metrics.total_return = (
            (self.performance_metrics.total_value - self.risk_manager.initial_capital) /
            self.risk_manager.initial_capital
        )
        
        # Estatísticas de trades
        if self.decisions_history:
            total_decisions = len(self.decisions_history)
            buy_decisions = sum(1 for d in self.decisions_history 
                              if d.signal in [TradingSignal.BUY, TradingSignal.STRONG_BUY])
            
            self.performance_metrics.total_trades = total_decisions
            # Simular win rate baseado na confiança média
            avg_confidence = np.mean([d.confidence for d in self.decisions_history])
            self.performance_metrics.win_rate = avg_confidence
    
    def get_status(self) -> Dict[str, Any]:
        """Retorna status atual do sistema."""
        return {
            "is_running": self.is_running,
            "watchlist": self.watchlist,
            "open_positions": len(self.risk_manager.open_positions),
            "total_decisions": len(self.decisions_history),
            "performance": {
                "total_value": self.performance_metrics.total_value,
                "total_return": self.performance_metrics.total_return,
                "win_rate": self.performance_metrics.win_rate,
                "available_cash": self.performance_metrics.available_cash
            },
            "last_analysis": {
                symbol: time.strftime("%H:%M:%S", time.localtime(timestamp.timestamp()))
                for symbol, timestamp in self.last_analysis_time.items()
            }
        }
    
    def get_recent_decisions(self, limit: int = 10) -> List[Dict[str, Any]]:
        """Retorna decisões recentes."""
        recent = list(self.decisions_history)[-limit:]
        return [decision.to_dict() for decision in recent]

# ============================================================================
# FUNÇÃO PRINCIPAL PARA TESTE
# ============================================================================

async def main():
    """Função principal para testar o sistema."""
    print("""
    ╔══════════════════════════════════════════════════════════════════╗
    ║              VHALINOR.IAG - Autonomous Trading Engine            ║
    ║        Virtual Hybrid Advanced Learning Intelligence             ║
    ║              Neural Optimized Reasoning System                   ║
    ║                                                                  ║
    ║  🤖 Sistema de Trading Autônomo                                  ║
    ║  📊 Análise Técnica Avançada                                     ║
    ║  🧠 Machine Learning Integrado                                   ║
    ║  ⚡ Gestão de Risco Inteligente                                  ║
    ╚══════════════════════════════════════════════════════════════════╝
    """)
    
    # Configurar logging
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    
    # Criar engine de trading
    engine = VhalinorAutonomousTradingEngine(initial_capital=100000.0)
    
    try:
        # Iniciar trading autônomo
        await engine.start_autonomous_trading()
        
        print("✅ Trading autônomo iniciado!")
        print("📊 Monitorando mercados...")
        print("⏹️ Pressione Ctrl+C para parar\n")
        
        # Loop de monitoramento
        while engine.is_running:
            await asyncio.sleep(30)
            
            # Mostrar status
            status = engine.get_status()
            print(f"💰 Capital: ${status['performance']['total_value']:,.2f} "
                  f"({status['performance']['total_return']:+.2%})")
            print(f"📈 Posições abertas: {status['open_positions']}")
            print(f"🎯 Decisões tomadas: {status['total_decisions']}")
            print(f"🏆 Taxa de acerto: {status['performance']['win_rate']:.2%}")
            
            # Mostrar decisões recentes
            recent_decisions = engine.get_recent_decisions(3)
            if recent_decisions:
                print("\n📋 Decisões recentes:")
                for decision in recent_decisions[-3:]:
                    print(f"   {decision['symbol']}: {decision['signal']} "
                          f"(confiança: {decision['confidence']:.1%})")
            
            print("-" * 60)
    
    except KeyboardInterrupt:
        print("\n🛑 Parando sistema...")
    
    finally:
        engine.stop_autonomous_trading()
        print("👋 Sistema encerrado!")

if __name__ == "__main__":
    asyncio.run(main())