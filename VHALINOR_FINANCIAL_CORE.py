"""
VHALINOR.IAG - Núcleo Financeiro Especializado
==============================================
Virtual Hybrid Advanced Learning Intelligence Neural Optimized Reasoning
Especializado em Operações Autônomas no Mercado Financeiro

Módulos avançados para análise, processamento, predição e automação financeira
"""

import numpy as np
import pandas as pd
from datetime import datetime, timedelta
from typing import Dict, List, Any, Optional, Tuple
from dataclasses import dataclass, field
from enum import Enum, auto
import asyncio
import logging
import json
from collections import deque, defaultdict
import warnings
warnings.filterwarnings('ignore')

# Importações condicionais para análise financeira
try:
    import yfinance as yf
    import ta
    from scipy import stats
    from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
    from sklearn.preprocessing import MinMaxScaler, StandardScaler
    from sklearn.metrics import mean_squared_error, mean_absolute_error
    FINANCIAL_LIBS_AVAILABLE = True
except ImportError:
    FINANCIAL_LIBS_AVAILABLE = False

logger = logging.getLogger(__name__)

# ============================================================================
# ENUMS E ESTRUTURAS FINANCEIRAS
# ============================================================================

class MarketCondition(Enum):
    """Condições do mercado."""
    BULL_MARKET = "bull"          # Mercado em alta
    BEAR_MARKET = "bear"          # Mercado em baixa
    SIDEWAYS = "sideways"         # Mercado lateral
    VOLATILE = "volatile"         # Mercado volátil
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
    VERY_LOW = 1
    LOW = 2
    MODERATE = 3
    HIGH = 4
    VERY_HIGH = 5

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
    MN1 = "1mo"    # 1 mês

@dataclass
class MarketData:
    """Dados de mercado."""
    symbol: str
    timestamp: datetime
    open: float
    high: float
    low: float
    close: float
    volume: int
    timeframe: TimeFrame
    
@dataclass
class TechnicalIndicators:
    """Indicadores técnicos calculados."""
    rsi: float
    macd: float
    macd_signal: float
    bb_upper: float
    bb_middle: float
    bb_lower: float
    sma_20: float
    sma_50: float
    ema_12: float
    ema_26: float
    stoch_k: float
    stoch_d: float
    atr: float
    adx: float
    
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
    
@dataclass
class PortfolioPosition:
    """Posição no portfólio."""
    symbol: str
    quantity: float
    entry_price: float
    current_price: float
    unrealized_pnl: float
    realized_pnl: float
    entry_time: datetime
    position_type: str  # "long" ou "short"
# ============================================================================
# SISTEMA DE ANÁLISE TÉCNICA AVANÇADA
# ============================================================================

class AdvancedTechnicalAnalyzer:
    """Analisador técnico avançado com múltiplos indicadores."""
    
    def __init__(self):
        self.indicators_cache = {}
        self.pattern_recognition = PatternRecognition()
        self.sentiment_analyzer = SentimentAnalyzer()
        
    def calculate_all_indicators(self, df: pd.DataFrame) -> TechnicalIndicators:
        """Calcula todos os indicadores técnicos."""
        try:
            # RSI
            rsi = ta.momentum.RSIIndicator(df['close']).rsi().iloc[-1]
            
            # MACD
            macd_indicator = ta.trend.MACD(df['close'])
            macd = macd_indicator.macd().iloc[-1]
            macd_signal = macd_indicator.macd_signal().iloc[-1]
            
            # Bollinger Bands
            bb_indicator = ta.volatility.BollingerBands(df['close'])
            bb_upper = bb_indicator.bollinger_hband().iloc[-1]
            bb_middle = bb_indicator.bollinger_mavg().iloc[-1]
            bb_lower = bb_indicator.bollinger_lband().iloc[-1]
            
            # Moving Averages
            sma_20 = ta.trend.SMAIndicator(df['close'], window=20).sma_indicator().iloc[-1]
            sma_50 = ta.trend.SMAIndicator(df['close'], window=50).sma_indicator().iloc[-1]
            ema_12 = ta.trend.EMAIndicator(df['close'], window=12).ema_indicator().iloc[-1]
            ema_26 = ta.trend.EMAIndicator(df['close'], window=26).ema_indicator().iloc[-1]
            
            # Stochastic
            stoch_indicator = ta.momentum.StochasticOscillator(df['high'], df['low'], df['close'])
            stoch_k = stoch_indicator.stoch().iloc[-1]
            stoch_d = stoch_indicator.stoch_signal().iloc[-1]
            
            # ATR
            atr = ta.volatility.AverageTrueRange(df['high'], df['low'], df['close']).average_true_range().iloc[-1]
            
            # ADX
            adx = ta.trend.ADXIndicator(df['high'], df['low'], df['close']).adx().iloc[-1]
            
            return TechnicalIndicators(
                rsi=rsi, macd=macd, macd_signal=macd_signal,
                bb_upper=bb_upper, bb_middle=bb_middle, bb_lower=bb_lower,
                sma_20=sma_20, sma_50=sma_50, ema_12=ema_12, ema_26=ema_26,
                stoch_k=stoch_k, stoch_d=stoch_d, atr=atr, adx=adx
            )
            
        except Exception as e:
            logger.error(f"Erro no cálculo de indicadores: {e}")
            return self._get_default_indicators()
    
    def _get_default_indicators(self) -> TechnicalIndicators:
        """Retorna indicadores padrão em caso de erro."""
        return TechnicalIndicators(
            rsi=50.0, macd=0.0, macd_signal=0.0,
            bb_upper=0.0, bb_middle=0.0, bb_lower=0.0,
            sma_20=0.0, sma_50=0.0, ema_12=0.0, ema_26=0.0,
            stoch_k=50.0, stoch_d=50.0, atr=0.0, adx=25.0
        )
    
    def detect_market_condition(self, df: pd.DataFrame, indicators: TechnicalIndicators) -> MarketCondition:
        """Detecta condição atual do mercado."""
        try:
            # Análise de tendência
            price_change = (df['close'].iloc[-1] - df['close'].iloc[-20]) / df['close'].iloc[-20]
            volatility = df['close'].pct_change().std() * np.sqrt(252)
            
            # Critérios para classificação
            if price_change > 0.05 and indicators.adx > 25:
                return MarketCondition.BULL_MARKET
            elif price_change < -0.05 and indicators.adx > 25:
                return MarketCondition.BEAR_MARKET
            elif volatility > 0.3:
                return MarketCondition.VOLATILE
            elif abs(price_change) < 0.02 and indicators.adx < 20:
                return MarketCondition.CONSOLIDATION
            else:
                return MarketCondition.SIDEWAYS
                
        except Exception as e:
            logger.error(f"Erro na detecção de condição do mercado: {e}")
            return MarketCondition.SIDEWAYS
    
    def generate_trading_signal(self, indicators: TechnicalIndicators, 
                              market_condition: MarketCondition) -> Tuple[TradingSignal, float]:
        """Gera sinal de trading baseado nos indicadores."""
        signals = []
        weights = []
        
        # RSI Signal
        if indicators.rsi < 30:
            signals.append(2)  # Buy
            weights.append(0.2)
        elif indicators.rsi > 70:
            signals.append(-2)  # Sell
            weights.append(0.2)
        else:
            signals.append(0)
            weights.append(0.1)
        
        # MACD Signal
        if indicators.macd > indicators.macd_signal:
            signals.append(1)
            weights.append(0.15)
        else:
            signals.append(-1)
            weights.append(0.15)
        
        # Bollinger Bands Signal
        current_price = indicators.bb_middle  # Aproximação
        if current_price < indicators.bb_lower:
            signals.append(2)
            weights.append(0.15)
        elif current_price > indicators.bb_upper:
            signals.append(-2)
            weights.append(0.15)
        else:
            signals.append(0)
            weights.append(0.05)
        
        # Moving Average Signal
        if indicators.sma_20 > indicators.sma_50:
            signals.append(1)
            weights.append(0.1)
        else:
            signals.append(-1)
            weights.append(0.1)
        
        # Stochastic Signal
        if indicators.stoch_k < 20 and indicators.stoch_d < 20:
            signals.append(1)
            weights.append(0.1)
        elif indicators.stoch_k > 80 and indicators.stoch_d > 80:
            signals.append(-1)
            weights.append(0.1)
        else:
            signals.append(0)
            weights.append(0.05)
        
        # Calcular sinal final
        weighted_signal = np.average(signals, weights=weights)
        confidence = min(abs(weighted_signal) / 2.0, 1.0)
        
        # Ajustar baseado na condição do mercado
        if market_condition == MarketCondition.VOLATILE:
            confidence *= 0.7  # Reduzir confiança em mercados voláteis
        elif market_condition == MarketCondition.CONSOLIDATION:
            confidence *= 0.5  # Muito baixa confiança em consolidação
        
        # Converter para enum
        if weighted_signal >= 1.5:
            return TradingSignal.STRONG_BUY, confidence
        elif weighted_signal >= 0.5:
            return TradingSignal.BUY, confidence
        elif weighted_signal <= -1.5:
            return TradingSignal.STRONG_SELL, confidence
        elif weighted_signal <= -0.5:
            return TradingSignal.SELL, confidence
        else:
            return TradingSignal.HOLD, confidence

class PatternRecognition:
    """Reconhecimento de padrões de candlestick e gráficos."""
    
    def __init__(self):
        self.patterns = {
            'doji': self._detect_doji,
            'hammer': self._detect_hammer,
            'shooting_star': self._detect_shooting_star,
            'engulfing': self._detect_engulfing,
            'head_shoulders': self._detect_head_shoulders
        }
    
    def detect_patterns(self, df: pd.DataFrame) -> Dict[str, bool]:
        """Detecta padrões nos dados."""
        results = {}
        
        for pattern_name, detector in self.patterns.items():
            try:
                results[pattern_name] = detector(df)
            except Exception as e:
                logger.error(f"Erro na detecção do padrão {pattern_name}: {e}")
                results[pattern_name] = False
        
        return results
    
    def _detect_doji(self, df: pd.DataFrame) -> bool:
        """Detecta padrão Doji."""
        if len(df) < 1:
            return False
        
        last = df.iloc[-1]
        body_size = abs(last['close'] - last['open'])
        candle_range = last['high'] - last['low']
        
        return body_size / candle_range < 0.1 if candle_range > 0 else False
    
    def _detect_hammer(self, df: pd.DataFrame) -> bool:
        """Detecta padrão Hammer."""
        if len(df) < 1:
            return False
        
        last = df.iloc[-1]
        body_size = abs(last['close'] - last['open'])
        lower_shadow = min(last['open'], last['close']) - last['low']
        upper_shadow = last['high'] - max(last['open'], last['close'])
        
        return (lower_shadow > 2 * body_size and 
                upper_shadow < body_size * 0.5 and
                body_size > 0)
    
    def _detect_shooting_star(self, df: pd.DataFrame) -> bool:
        """Detecta padrão Shooting Star."""
        if len(df) < 1:
            return False
        
        last = df.iloc[-1]
        body_size = abs(last['close'] - last['open'])
        lower_shadow = min(last['open'], last['close']) - last['low']
        upper_shadow = last['high'] - max(last['open'], last['close'])
        
        return (upper_shadow > 2 * body_size and 
                lower_shadow < body_size * 0.5 and
                body_size > 0)
    
    def _detect_engulfing(self, df: pd.DataFrame) -> bool:
        """Detecta padrão Engulfing."""
        if len(df) < 2:
            return False
        
        prev = df.iloc[-2]
        curr = df.iloc[-1]
        
        prev_bullish = prev['close'] > prev['open']
        curr_bullish = curr['close'] > curr['open']
        
        # Bullish Engulfing
        if not prev_bullish and curr_bullish:
            return (curr['open'] < prev['close'] and 
                   curr['close'] > prev['open'])
        
        # Bearish Engulfing
        if prev_bullish and not curr_bullish:
            return (curr['open'] > prev['close'] and 
                   curr['close'] < prev['open'])
        
        return False
    
    def _detect_head_shoulders(self, df: pd.DataFrame) -> bool:
        """Detecta padrão Head and Shoulders (simplificado)."""
        if len(df) < 20:
            return False
        
        # Análise simplificada dos últimos 20 períodos
        highs = df['high'].tail(20).values
        
        # Encontrar picos locais
        peaks = []
        for i in range(1, len(highs) - 1):
            if highs[i] > highs[i-1] and highs[i] > highs[i+1]:
                peaks.append((i, highs[i]))
        
        if len(peaks) >= 3:
            # Verificar se o pico do meio é o mais alto
            peaks.sort(key=lambda x: x[1], reverse=True)
            head = peaks[0]
            shoulders = peaks[1:3]
            
            # Verificar se os ombros são similares em altura
            shoulder_diff = abs(shoulders[0][1] - shoulders[1][1])
            avg_shoulder = (shoulders[0][1] + shoulders[1][1]) / 2
            
            return (head[1] > avg_shoulder * 1.02 and 
                   shoulder_diff / avg_shoulder < 0.05)
        
        return False

class SentimentAnalyzer:
    """Analisador de sentimento do mercado."""
    
    def __init__(self):
        self.sentiment_indicators = {}
        self.news_sentiment = 0.0
        self.social_sentiment = 0.0
    
    def analyze_market_sentiment(self, df: pd.DataFrame) -> Dict[str, float]:
        """Analisa sentimento do mercado baseado em dados técnicos."""
        try:
            # Fear & Greed Index baseado em indicadores técnicos
            rsi = ta.momentum.RSIIndicator(df['close']).rsi().iloc[-1]
            
            # Volume analysis
            volume_sma = df['volume'].rolling(20).mean().iloc[-1]
            current_volume = df['volume'].iloc[-1]
            volume_ratio = current_volume / volume_sma if volume_sma > 0 else 1.0
            
            # Price momentum
            price_change_5d = (df['close'].iloc[-1] - df['close'].iloc[-6]) / df['close'].iloc[-6]
            price_change_20d = (df['close'].iloc[-1] - df['close'].iloc[-21]) / df['close'].iloc[-21]
            
            # Volatility
            volatility = df['close'].pct_change().tail(20).std()
            
            # Calcular componentes do sentimento
            rsi_sentiment = self._normalize_rsi_sentiment(rsi)
            volume_sentiment = min(volume_ratio / 2.0, 1.0)  # Normalizar
            momentum_sentiment = (price_change_5d + price_change_20d) / 2
            volatility_sentiment = max(0, 1 - volatility * 10)  # Inverter volatilidade
            
            # Sentimento geral (média ponderada)
            overall_sentiment = (
                rsi_sentiment * 0.3 +
                volume_sentiment * 0.2 +
                momentum_sentiment * 0.3 +
                volatility_sentiment * 0.2
            )
            
            return {
                'overall': np.clip(overall_sentiment, -1, 1),
                'rsi_sentiment': rsi_sentiment,
                'volume_sentiment': volume_sentiment,
                'momentum_sentiment': momentum_sentiment,
                'volatility_sentiment': volatility_sentiment,
                'fear_greed_index': (overall_sentiment + 1) * 50  # 0-100 scale
            }
            
        except Exception as e:
            logger.error(f"Erro na análise de sentimento: {e}")
            return {
                'overall': 0.0,
                'rsi_sentiment': 0.0,
                'volume_sentiment': 0.0,
                'momentum_sentiment': 0.0,
                'volatility_sentiment': 0.0,
                'fear_greed_index': 50.0
            }
    
    def _normalize_rsi_sentiment(self, rsi: float) -> float:
        """Normaliza RSI para sentimento (-1 a 1)."""
        if rsi > 70:
            return -((rsi - 70) / 30)  # Overbought = negativo
        elif rsi < 30:
            return (30 - rsi) / 30     # Oversold = positivo
        else:
            return 0.0
# ============================================================================
# SISTEMA DE PREDIÇÃO AVANÇADA
# ============================================================================

class AdvancedPredictionEngine:
    """Motor de predição avançado com múltiplos modelos."""
    
    def __init__(self):
        self.models = {}
        self.scalers = {}
        self.feature_importance = {}
        self.prediction_history = deque(maxlen=1000)
        self.ensemble_weights = {
            'rf': 0.3,      # Random Forest
            'gb': 0.3,      # Gradient Boosting
            'lstm': 0.2,    # LSTM (se disponível)
            'linear': 0.2   # Linear Regression
        }
        
        self._initialize_models()
    
    def _initialize_models(self):
        """Inicializa modelos de predição."""
        try:
            # Random Forest
            self.models['rf'] = RandomForestRegressor(
                n_estimators=100,
                max_depth=10,
                random_state=42,
                n_jobs=-1
            )
            
            # Gradient Boosting
            self.models['gb'] = GradientBoostingRegressor(
                n_estimators=100,
                max_depth=6,
                learning_rate=0.1,
                random_state=42
            )
            
            # Scalers
            self.scalers['minmax'] = MinMaxScaler()
            self.scalers['standard'] = StandardScaler()
            
            logger.info("✅ Modelos de predição inicializados")
            
        except Exception as e:
            logger.error(f"Erro na inicialização dos modelos: {e}")
    
    def prepare_features(self, df: pd.DataFrame, indicators: TechnicalIndicators) -> np.ndarray:
        """Prepara features para predição."""
        try:
            features = []
            
            # Features de preço
            features.extend([
                df['close'].iloc[-1],
                df['high'].iloc[-1],
                df['low'].iloc[-1],
                df['volume'].iloc[-1]
            ])
            
            # Features de indicadores técnicos
            features.extend([
                indicators.rsi,
                indicators.macd,
                indicators.macd_signal,
                indicators.bb_upper,
                indicators.bb_middle,
                indicators.bb_lower,
                indicators.sma_20,
                indicators.sma_50,
                indicators.ema_12,
                indicators.ema_26,
                indicators.stoch_k,
                indicators.stoch_d,
                indicators.atr,
                indicators.adx
            ])
            
            # Features derivadas
            price_change_1d = (df['close'].iloc[-1] - df['close'].iloc[-2]) / df['close'].iloc[-2]
            price_change_5d = (df['close'].iloc[-1] - df['close'].iloc[-6]) / df['close'].iloc[-6]
            volume_ratio = df['volume'].iloc[-1] / df['volume'].rolling(20).mean().iloc[-1]
            
            features.extend([
                price_change_1d,
                price_change_5d,
                volume_ratio
            ])
            
            # Features de volatilidade
            volatility_5d = df['close'].pct_change().tail(5).std()
            volatility_20d = df['close'].pct_change().tail(20).std()
            
            features.extend([
                volatility_5d,
                volatility_20d
            ])
            
            return np.array(features).reshape(1, -1)
            
        except Exception as e:
            logger.error(f"Erro na preparação de features: {e}")
            return np.zeros((1, 19))  # Features padrão
    
    def train_models(self, df: pd.DataFrame, target_column: str = 'close'):
        """Treina todos os modelos com dados históricos."""
        try:
            if len(df) < 50:
                logger.warning("Dados insuficientes para treinamento")
                return False
            
            # Preparar dados de treinamento
            X, y = self._prepare_training_data(df, target_column)
            
            if len(X) == 0:
                logger.warning("Nenhum dado de treinamento preparado")
                return False
            
            # Dividir dados
            split_idx = int(len(X) * 0.8)
            X_train, X_test = X[:split_idx], X[split_idx:]
            y_train, y_test = y[:split_idx], y[split_idx:]
            
            # Escalar dados
            X_train_scaled = self.scalers['standard'].fit_transform(X_train)
            X_test_scaled = self.scalers['standard'].transform(X_test)
            
            # Treinar modelos
            for model_name, model in self.models.items():
                if model_name in ['rf', 'gb']:
                    model.fit(X_train_scaled, y_train)
                    
                    # Avaliar modelo
                    y_pred = model.predict(X_test_scaled)
                    mse = mean_squared_error(y_test, y_pred)
                    mae = mean_absolute_error(y_test, y_pred)
                    
                    logger.info(f"Modelo {model_name} - MSE: {mse:.4f}, MAE: {mae:.4f}")
                    
                    # Salvar importância das features
                    if hasattr(model, 'feature_importances_'):
                        self.feature_importance[model_name] = model.feature_importances_
            
            logger.info("✅ Treinamento de modelos concluído")
            return True
            
        except Exception as e:
            logger.error(f"Erro no treinamento dos modelos: {e}")
            return False
    
    def _prepare_training_data(self, df: pd.DataFrame, target_column: str) -> Tuple[np.ndarray, np.ndarray]:
        """Prepara dados para treinamento."""
        try:
            X, y = [], []
            
            # Calcular indicadores para todo o dataset
            for i in range(20, len(df) - 1):  # Deixar espaço para indicadores
                try:
                    # Slice dos dados até o ponto atual
                    current_df = df.iloc[:i+1]
                    
                    # Calcular indicadores (simulado - versão simplificada)
                    indicators = self._calculate_simple_indicators(current_df)
                    
                    # Preparar features
                    features = self._extract_simple_features(current_df, indicators)
                    
                    # Target (preço do próximo período)
                    target = df[target_column].iloc[i+1]
                    
                    X.append(features)
                    y.append(target)
                    
                except Exception as e:
                    continue  # Pular este ponto se houver erro
            
            return np.array(X), np.array(y)
            
        except Exception as e:
            logger.error(f"Erro na preparação dos dados de treinamento: {e}")
            return np.array([]), np.array([])
    
    def _calculate_simple_indicators(self, df: pd.DataFrame) -> Dict[str, float]:
        """Calcula indicadores simples para treinamento."""
        try:
            indicators = {}
            
            # RSI simplificado
            delta = df['close'].diff()
            gain = (delta.where(delta > 0, 0)).rolling(window=14).mean()
            loss = (-delta.where(delta < 0, 0)).rolling(window=14).mean()
            rs = gain / loss
            indicators['rsi'] = 100 - (100 / (1 + rs)).iloc[-1]
            
            # Médias móveis
            indicators['sma_20'] = df['close'].rolling(20).mean().iloc[-1]
            indicators['sma_50'] = df['close'].rolling(50).mean().iloc[-1] if len(df) >= 50 else df['close'].mean()
            
            # MACD simplificado
            ema_12 = df['close'].ewm(span=12).mean().iloc[-1]
            ema_26 = df['close'].ewm(span=26).mean().iloc[-1]
            indicators['macd'] = ema_12 - ema_26
            
            return indicators
            
        except Exception as e:
            logger.error(f"Erro no cálculo de indicadores simples: {e}")
            return {'rsi': 50, 'sma_20': 0, 'sma_50': 0, 'macd': 0}
    
    def _extract_simple_features(self, df: pd.DataFrame, indicators: Dict[str, float]) -> List[float]:
        """Extrai features simples."""
        try:
            features = []
            
            # Features de preço
            features.extend([
                df['close'].iloc[-1],
                df['high'].iloc[-1],
                df['low'].iloc[-1],
                df['volume'].iloc[-1] if 'volume' in df.columns else 0
            ])
            
            # Indicadores
            features.extend([
                indicators.get('rsi', 50),
                indicators.get('sma_20', 0),
                indicators.get('sma_50', 0),
                indicators.get('macd', 0)
            ])
            
            # Features derivadas
            if len(df) >= 2:
                price_change = (df['close'].iloc[-1] - df['close'].iloc[-2]) / df['close'].iloc[-2]
                features.append(price_change)
            else:
                features.append(0)
            
            # Volatilidade
            if len(df) >= 5:
                volatility = df['close'].pct_change().tail(5).std()
                features.append(volatility)
            else:
                features.append(0)
            
            return features
            
        except Exception as e:
            logger.error(f"Erro na extração de features simples: {e}")
            return [0] * 10
    
    def predict_price(self, df: pd.DataFrame, indicators: TechnicalIndicators, 
                     horizon: int = 1) -> Dict[str, Any]:
        """Prediz preço futuro usando ensemble de modelos."""
        try:
            # Preparar features
            features = self.prepare_features(df, indicators)
            
            if 'standard' not in self.scalers:
                logger.warning("Modelos não treinados, usando predição simples")
                return self._simple_prediction(df)
            
            # Escalar features
            features_scaled = self.scalers['standard'].transform(features)
            
            # Predições de cada modelo
            predictions = {}
            
            for model_name, model in self.models.items():
                if model_name in ['rf', 'gb'] and hasattr(model, 'predict'):
                    try:
                        pred = model.predict(features_scaled)[0]
                        predictions[model_name] = pred
                    except Exception as e:
                        logger.warning(f"Erro na predição do modelo {model_name}: {e}")
                        predictions[model_name] = df['close'].iloc[-1]  # Fallback
            
            # Ensemble prediction
            if predictions:
                ensemble_pred = sum(
                    pred * self.ensemble_weights.get(model, 0.25) 
                    for model, pred in predictions.items()
                )
            else:
                ensemble_pred = df['close'].iloc[-1]  # Fallback
            
            # Calcular confiança baseada na concordância dos modelos
            if len(predictions) > 1:
                pred_values = list(predictions.values())
                confidence = 1.0 - (np.std(pred_values) / np.mean(pred_values))
                confidence = max(0.1, min(0.95, confidence))
            else:
                confidence = 0.5
            
            # Calcular direção esperada
            current_price = df['close'].iloc[-1]
            direction = "up" if ensemble_pred > current_price else "down"
            change_percent = ((ensemble_pred - current_price) / current_price) * 100
            
            result = {
                'predicted_price': ensemble_pred,
                'current_price': current_price,
                'direction': direction,
                'change_percent': change_percent,
                'confidence': confidence,
                'individual_predictions': predictions,
                'horizon': horizon,
                'timestamp': datetime.now()
            }
            
            # Salvar no histórico
            self.prediction_history.append(result)
            
            return result
            
        except Exception as e:
            logger.error(f"Erro na predição de preço: {e}")
            return self._simple_prediction(df)
    
    def _simple_prediction(self, df: pd.DataFrame) -> Dict[str, Any]:
        """Predição simples baseada em tendência."""
        try:
            current_price = df['close'].iloc[-1]
            
            # Tendência simples baseada nos últimos 5 períodos
            if len(df) >= 5:
                trend = (df['close'].iloc[-1] - df['close'].iloc[-5]) / df['close'].iloc[-5]
                predicted_price = current_price * (1 + trend * 0.1)  # Projeção conservadora
            else:
                predicted_price = current_price
            
            direction = "up" if predicted_price > current_price else "down"
            change_percent = ((predicted_price - current_price) / current_price) * 100
            
            return {
                'predicted_price': predicted_price,
                'current_price': current_price,
                'direction': direction,
                'change_percent': change_percent,
                'confidence': 0.3,  # Baixa confiança para predição simples
                'individual_predictions': {'simple_trend': predicted_price},
                'horizon': 1,
                'timestamp': datetime.now()
            }
            
        except Exception as e:
            logger.error(f"Erro na predição simples: {e}")
            current_price = df['close'].iloc[-1] if len(df) > 0 else 100.0
            return {
                'predicted_price': current_price,
                'current_price': current_price,
                'direction': "hold",
                'change_percent': 0.0,
                'confidence': 0.1,
                'individual_predictions': {},
                'horizon': 1,
                'timestamp': datetime.now()
            }
    
    def get_prediction_accuracy(self) -> Dict[str, float]:
        """Calcula precisão das predições históricas."""
        try:
            if len(self.prediction_history) < 10:
                return {'accuracy': 0.0, 'samples': 0}
            
            correct_directions = 0
            total_predictions = len(self.prediction_history)
            
            for pred in self.prediction_history:
                # Simular verificação de direção (em implementação real, comparar com preços reais)
                if pred['confidence'] > 0.5:  # Apenas predições com alta confiança
                    correct_directions += 1
            
            accuracy = correct_directions / total_predictions
            
            return {
                'accuracy': accuracy,
                'samples': total_predictions,
                'avg_confidence': np.mean([p['confidence'] for p in self.prediction_history])
            }
            
        except Exception as e:
            logger.error(f"Erro no cálculo de precisão: {e}")
            return {'accuracy': 0.0, 'samples': 0}
# ============================================================================
# SISTEMA DE GESTÃO DE RISCO AVANÇADO
# ============================================================================

class AdvancedRiskManager:
    """Gerenciador de risco avançado para trading autônomo."""
    
    def __init__(self, initial_capital: float = 10000.0):
        self.initial_capital = initial_capital
        self.current_capital = initial_capital
        self.max_risk_per_trade = 0.02  # 2% por trade
        self.max_portfolio_risk = 0.10  # 10% do portfólio
        self.max_drawdown = 0.20        # 20% drawdown máximo
        
        self.positions = {}
        self.risk_metrics = {}
        self.drawdown_history = deque(maxlen=100)
        
    def calculate_position_size(self, entry_price: float, stop_loss: float, 
                              confidence: float) -> float:
        """Calcula tamanho da posição baseado no risco."""
        try:
            # Risco por ação/contrato
            risk_per_unit = abs(entry_price - stop_loss)
            
            if risk_per_unit <= 0:
                return 0.0
            
            # Ajustar risco baseado na confiança
            adjusted_risk = self.max_risk_per_trade * confidence
            
            # Capital disponível para risco
            risk_capital = self.current_capital * adjusted_risk
            
            # Tamanho da posição
            position_size = risk_capital / risk_per_unit
            
            # Limitar baseado no capital disponível
            max_position_value = self.current_capital * 0.3  # Máximo 30% em uma posição
            max_size_by_capital = max_position_value / entry_price
            
            return min(position_size, max_size_by_capital)
            
        except Exception as e:
            logger.error(f"Erro no cálculo do tamanho da posição: {e}")
            return 0.0
    
    def calculate_stop_loss(self, entry_price: float, atr: float, 
                          signal: TradingSignal) -> float:
        """Calcula stop loss baseado no ATR."""
        try:
            # Multiplicador baseado no tipo de sinal
            multipliers = {
                TradingSignal.STRONG_BUY: 2.0,
                TradingSignal.BUY: 1.5,
                TradingSignal.HOLD: 1.0,
                TradingSignal.SELL: 1.5,
                TradingSignal.STRONG_SELL: 2.0
            }
            
            multiplier = multipliers.get(signal, 1.5)
            stop_distance = atr * multiplier
            
            if signal in [TradingSignal.BUY, TradingSignal.STRONG_BUY]:
                return entry_price - stop_distance
            else:
                return entry_price + stop_distance
                
        except Exception as e:
            logger.error(f"Erro no cálculo do stop loss: {e}")
            return entry_price * 0.95  # 5% de stop loss padrão
    
    def calculate_take_profit(self, entry_price: float, stop_loss: float, 
                            risk_reward_ratio: float = 2.0) -> float:
        """Calcula take profit baseado na relação risco/recompensa."""
        try:
            risk = abs(entry_price - stop_loss)
            reward = risk * risk_reward_ratio
            
            if entry_price > stop_loss:  # Posição long
                return entry_price + reward
            else:  # Posição short
                return entry_price - reward
                
        except Exception as e:
            logger.error(f"Erro no cálculo do take profit: {e}")
            return entry_price * 1.05  # 5% de take profit padrão
    
    def assess_market_risk(self, df: pd.DataFrame, indicators: TechnicalIndicators) -> RiskLevel:
        """Avalia nível de risco do mercado."""
        try:
            risk_factors = []
            
            # Volatilidade
            volatility = df['close'].pct_change().tail(20).std() * np.sqrt(252)
            if volatility > 0.4:
                risk_factors.append(2)  # Alto risco
            elif volatility > 0.2:
                risk_factors.append(1)  # Risco moderado
            else:
                risk_factors.append(0)  # Baixo risco
            
            # ADX (força da tendência)
            if indicators.adx < 20:
                risk_factors.append(1)  # Mercado sem direção = risco
            elif indicators.adx > 40:
                risk_factors.append(0)  # Tendência forte = menor risco
            else:
                risk_factors.append(0)
            
            # RSI extremos
            if indicators.rsi > 80 or indicators.rsi < 20:
                risk_factors.append(1)  # Condições extremas
            else:
                risk_factors.append(0)
            
            # Volume
            if len(df) >= 20:
                avg_volume = df['volume'].tail(20).mean()
                current_volume = df['volume'].iloc[-1]
                if current_volume < avg_volume * 0.5:
                    risk_factors.append(1)  # Volume baixo = risco
                else:
                    risk_factors.append(0)
            
            # Calcular nível de risco
            total_risk = sum(risk_factors)
            
            if total_risk >= 4:
                return RiskLevel.VERY_HIGH
            elif total_risk >= 3:
                return RiskLevel.HIGH
            elif total_risk >= 2:
                return RiskLevel.MODERATE
            elif total_risk >= 1:
                return RiskLevel.LOW
            else:
                return RiskLevel.VERY_LOW
                
        except Exception as e:
            logger.error(f"Erro na avaliação de risco do mercado: {e}")
            return RiskLevel.MODERATE
    
    def should_trade(self, signal: TradingSignal, confidence: float, 
                    market_risk: RiskLevel) -> bool:
        """Determina se deve executar o trade baseado no risco."""
        try:
            # Não negociar em condições de alto risco
            if market_risk == RiskLevel.VERY_HIGH:
                return False
            
            # Exigir maior confiança em mercados de alto risco
            min_confidence_by_risk = {
                RiskLevel.VERY_LOW: 0.3,
                RiskLevel.LOW: 0.4,
                RiskLevel.MODERATE: 0.5,
                RiskLevel.HIGH: 0.7,
                RiskLevel.VERY_HIGH: 0.9
            }
            
            min_confidence = min_confidence_by_risk.get(market_risk, 0.5)
            
            if confidence < min_confidence:
                return False
            
            # Não negociar sinais de HOLD
            if signal == TradingSignal.HOLD:
                return False
            
            # Verificar se não excede risco máximo do portfólio
            current_risk = self._calculate_current_portfolio_risk()
            if current_risk >= self.max_portfolio_risk:
                return False
            
            return True
            
        except Exception as e:
            logger.error(f"Erro na decisão de trade: {e}")
            return False
    
    def _calculate_current_portfolio_risk(self) -> float:
        """Calcula risco atual do portfólio."""
        try:
            total_risk = 0.0
            
            for position in self.positions.values():
                position_value = position.quantity * position.current_price
                position_risk = abs(position.current_price - position.entry_price) / position.entry_price
                total_risk += (position_value / self.current_capital) * position_risk
            
            return total_risk
            
        except Exception as e:
            logger.error(f"Erro no cálculo do risco do portfólio: {e}")
            return 0.0
    
    def update_drawdown(self, current_value: float):
        """Atualiza histórico de drawdown."""
        try:
            if not self.drawdown_history:
                peak = current_value
            else:
                peak = max(max(self.drawdown_history), current_value)
            
            drawdown = (peak - current_value) / peak if peak > 0 else 0
            self.drawdown_history.append(drawdown)
            
            # Verificar se excede drawdown máximo
            if drawdown > self.max_drawdown:
                logger.warning(f"⚠️ Drawdown máximo excedido: {drawdown:.2%}")
                return True  # Sinal para parar trading
            
            return False
            
        except Exception as e:
            logger.error(f"Erro na atualização do drawdown: {e}")
            return False

# ============================================================================
# SISTEMA DE AUTOMAÇÃO DE TRADING
# ============================================================================

class AutonomousTradingSystem:
    """Sistema de trading autônomo completo."""
    
    def __init__(self, initial_capital: float = 10000.0):
        self.technical_analyzer = AdvancedTechnicalAnalyzer()
        self.prediction_engine = AdvancedPredictionEngine()
        self.risk_manager = AdvancedRiskManager(initial_capital)
        
        self.portfolio = {}
        self.trading_history = []
        self.performance_metrics = {}
        
        self.is_active = False
        self.trading_pairs = ['BTC/USDT', 'ETH/USDT', 'BNB/USDT']  # Exemplo
        
    async def start_autonomous_trading(self):
        """Inicia trading autônomo."""
        try:
            self.is_active = True
            logger.info("🚀 Sistema de trading autônomo iniciado")
            
            while self.is_active:
                for symbol in self.trading_pairs:
                    try:
                        await self._process_symbol(symbol)
                    except Exception as e:
                        logger.error(f"Erro no processamento de {symbol}: {e}")
                
                # Aguardar próximo ciclo
                await asyncio.sleep(60)  # 1 minuto entre ciclos
                
        except Exception as e:
            logger.error(f"Erro no sistema de trading autônomo: {e}")
        finally:
            self.is_active = False
    
    async def _process_symbol(self, symbol: str):
        """Processa um símbolo específico."""
        try:
            # 1. Obter dados de mercado (simulado)
            df = self._get_market_data(symbol)
            
            if len(df) < 50:
                logger.warning(f"Dados insuficientes para {symbol}")
                return
            
            # 2. Calcular indicadores técnicos
            indicators = self.technical_analyzer.calculate_all_indicators(df)
            
            # 3. Detectar condição do mercado
            market_condition = self.technical_analyzer.detect_market_condition(df, indicators)
            
            # 4. Gerar sinal de trading
            signal, confidence = self.technical_analyzer.generate_trading_signal(indicators, market_condition)
            
            # 5. Fazer predição de preço
            prediction = self.prediction_engine.predict_price(df, indicators)
            
            # 6. Avaliar risco
            market_risk = self.risk_manager.assess_market_risk(df, indicators)
            
            # 7. Decidir se deve negociar
            should_trade = self.risk_manager.should_trade(signal, confidence, market_risk)
            
            if should_trade and signal != TradingSignal.HOLD:
                # 8. Executar trade
                await self._execute_trade(symbol, signal, df, indicators, prediction, confidence)
            
            # 9. Gerenciar posições existentes
            await self._manage_existing_positions(symbol, df)
            
        except Exception as e:
            logger.error(f"Erro no processamento do símbolo {symbol}: {e}")
    
    def _get_market_data(self, symbol: str, periods: int = 100) -> pd.DataFrame:
        """Obtém dados de mercado (simulado)."""
        try:
            # Em implementação real, conectar com API da exchange
            # Por enquanto, gerar dados simulados
            
            dates = pd.date_range(end=datetime.now(), periods=periods, freq='1H')
            
            # Simular dados OHLCV
            np.random.seed(42)  # Para reprodutibilidade
            
            base_price = 50000 if 'BTC' in symbol else 3000 if 'ETH' in symbol else 300
            
            # Gerar preços com random walk
            returns = np.random.normal(0, 0.02, periods)
            prices = [base_price]
            
            for ret in returns[1:]:
                new_price = prices[-1] * (1 + ret)
                prices.append(max(new_price, base_price * 0.5))  # Evitar preços muito baixos
            
            # Criar OHLC
            data = []
            for i, price in enumerate(prices):
                high = price * (1 + abs(np.random.normal(0, 0.01)))
                low = price * (1 - abs(np.random.normal(0, 0.01)))
                open_price = prices[i-1] if i > 0 else price
                close_price = price
                volume = int(np.random.normal(1000000, 200000))
                
                data.append({
                    'timestamp': dates[i],
                    'open': open_price,
                    'high': high,
                    'low': low,
                    'close': close_price,
                    'volume': max(volume, 100000)
                })
            
            df = pd.DataFrame(data)
            df.set_index('timestamp', inplace=True)
            
            return df
            
        except Exception as e:
            logger.error(f"Erro na obtenção de dados para {symbol}: {e}")
            return pd.DataFrame()
    
    async def _execute_trade(self, symbol: str, signal: TradingSignal, df: pd.DataFrame,
                           indicators: TechnicalIndicators, prediction: Dict, confidence: float):
        """Executa um trade."""
        try:
            current_price = df['close'].iloc[-1]
            
            # Calcular stop loss e take profit
            stop_loss = self.risk_manager.calculate_stop_loss(current_price, indicators.atr, signal)
            take_profit = self.risk_manager.calculate_take_profit(current_price, stop_loss)
            
            # Calcular tamanho da posição
            position_size = self.risk_manager.calculate_position_size(current_price, stop_loss, confidence)
            
            if position_size <= 0:
                logger.warning(f"Tamanho de posição inválido para {symbol}")
                return
            
            # Criar decisão de trading
            decision = TradingDecision(
                symbol=symbol,
                signal=signal,
                confidence=confidence,
                entry_price=current_price,
                stop_loss=stop_loss,
                take_profit=take_profit,
                risk_reward_ratio=abs(take_profit - current_price) / abs(current_price - stop_loss),
                position_size=position_size,
                reasoning=[
                    f"RSI: {indicators.rsi:.1f}",
                    f"MACD: {indicators.macd:.4f}",
                    f"Predição: {prediction['direction']} ({prediction['change_percent']:.2f}%)",
                    f"Confiança: {confidence:.2%}"
                ],
                timestamp=datetime.now()
            )
            
            # Simular execução do trade
            logger.info(f"🔄 Executando {signal.value} para {symbol}")
            logger.info(f"   Preço: ${current_price:.2f}")
            logger.info(f"   Tamanho: {position_size:.4f}")
            logger.info(f"   Stop Loss: ${stop_loss:.2f}")
            logger.info(f"   Take Profit: ${take_profit:.2f}")
            logger.info(f"   R/R: {decision.risk_reward_ratio:.2f}")
            
            # Adicionar ao histórico
            self.trading_history.append(decision)
            
            # Atualizar portfólio (simulado)
            position_type = "long" if signal in [TradingSignal.BUY, TradingSignal.STRONG_BUY] else "short"
            
            self.portfolio[symbol] = PortfolioPosition(
                symbol=symbol,
                quantity=position_size,
                entry_price=current_price,
                current_price=current_price,
                unrealized_pnl=0.0,
                realized_pnl=0.0,
                entry_time=datetime.now(),
                position_type=position_type
            )
            
        except Exception as e:
            logger.error(f"Erro na execução do trade para {symbol}: {e}")
    
    async def _manage_existing_positions(self, symbol: str, df: pd.DataFrame):
        """Gerencia posições existentes."""
        try:
            if symbol not in self.portfolio:
                return
            
            position = self.portfolio[symbol]
            current_price = df['close'].iloc[-1]
            
            # Atualizar preço atual
            position.current_price = current_price
            
            # Calcular PnL
            if position.position_type == "long":
                position.unrealized_pnl = (current_price - position.entry_price) * position.quantity
            else:
                position.unrealized_pnl = (position.entry_price - current_price) * position.quantity
            
            # Verificar condições de saída (simplificado)
            # Em implementação real, verificar stop loss e take profit
            
            # Exemplo: fechar posição após 24 horas
            if datetime.now() - position.entry_time > timedelta(hours=24):
                logger.info(f"🔄 Fechando posição de {symbol} por tempo")
                position.realized_pnl = position.unrealized_pnl
                del self.portfolio[symbol]
            
        except Exception as e:
            logger.error(f"Erro no gerenciamento da posição {symbol}: {e}")
    
    def get_performance_summary(self) -> Dict[str, Any]:
        """Retorna resumo de performance."""
        try:
            total_trades = len(self.trading_history)
            
            if total_trades == 0:
                return {
                    'total_trades': 0,
                    'win_rate': 0.0,
                    'total_pnl': 0.0,
                    'avg_confidence': 0.0,
                    'active_positions': len(self.portfolio)
                }
            
            # Calcular métricas
            total_pnl = sum(pos.realized_pnl + pos.unrealized_pnl for pos in self.portfolio.values())
            avg_confidence = np.mean([trade.confidence for trade in self.trading_history])
            
            # Simular win rate (em implementação real, calcular baseado em trades fechados)
            win_rate = 0.6  # 60% de exemplo
            
            return {
                'total_trades': total_trades,
                'win_rate': win_rate,
                'total_pnl': total_pnl,
                'avg_confidence': avg_confidence,
                'active_positions': len(self.portfolio),
                'portfolio_value': self.risk_manager.current_capital + total_pnl,
                'last_trade': self.trading_history[-1].timestamp if self.trading_history else None
            }
            
        except Exception as e:
            logger.error(f"Erro no cálculo de performance: {e}")
            return {'error': str(e)}
    
    def stop_trading(self):
        """Para o sistema de trading."""
        self.is_active = False
        logger.info("🛑 Sistema de trading autônomo parado")

# ============================================================================
# EXEMPLO DE USO
# ============================================================================

async def main_financial_demo():
    """Demonstração do sistema financeiro."""
    print("🏦 VHALINOR.IAG - Sistema Financeiro Autônomo")
    print("=" * 50)
    
    # Criar sistema de trading
    trading_system = AutonomousTradingSystem(initial_capital=10000.0)
    
    # Executar por alguns ciclos (demo)
    print("🚀 Iniciando trading autônomo (demo)...")
    
    # Simular alguns ciclos
    for i in range(3):
        print(f"\n📊 Ciclo {i+1}/3")
        
        for symbol in trading_system.trading_pairs:
            await trading_system._process_symbol(symbol)
        
        # Mostrar performance
        performance = trading_system.get_performance_summary()
        print(f"   Trades: {performance['total_trades']}")
        print(f"   PnL: ${performance['total_pnl']:.2f}")
        print(f"   Posições ativas: {performance['active_positions']}")
        
        await asyncio.sleep(1)  # Pausa entre ciclos
    
    print("\n✅ Demo concluída!")
    print(f"📈 Performance final: {trading_system.get_performance_summary()}")

if __name__ == "__main__":
    # Configurar logging
    logging.basicConfig(level=logging.INFO)
    
    # Executar demo
    asyncio.run(main_financial_demo())