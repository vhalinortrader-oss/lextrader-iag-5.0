"""
LEXTRADER-IAG 3.0 - FUNÇÃO OMEGA
=================================
LEXTRADER-IAG 4.0 - Sistema de Trading Inteligente com AGI Sentiente
Versão: 3.0.0
Autor: LEXTRADER AI Team
Data: 2024
"""

import asyncio
import random
from typing import List, Dict, Any, Optional, ClassVar
from dataclasses import dataclass, field
from enum import Enum, IntEnum
from datetime import datetime, timedelta
import time
from collections import deque
import hashlib
from functools import lru_cache
from concurrent.futures import ThreadPoolExecutor
import numpy as np

# ========== CONFIGURAÇÕES DO SISTEMA LEXTRADER-IAG ==========

class LextraderConfig:
    """Configurações específicas do LEXTRADER-IAG 3.0."""
    
    VERSION = "3.0.0"
    SYSTEM_NAME = "LEXTRADER-IAG Omega Function"
    MODE = "SENTIENT_TRADING"
    MAX_PARALLEL_CYCLES = 4
    DATA_RETENTION_DAYS = 7
    
    # Configurações de mercado
    SUPPORTED_MARKETS = ["CRYPTO", "FOREX", "STOCKS", "COMMODITIES"]
    DEFAULT_MARKET = "CRYPTO"
    
    # Configurações de risco
    MAX_DRAWDOWN = 20.0  # 20% máximo
    MIN_WIN_RATE = 0.55  # 55% mínimo
    MAX_EXPOSURE = 0.75  # 75% máximo
    
    # Configurações de aprendizado
    NEURAL_LEARNING_RATE = 0.01
    MEMORY_CAPACITY = 10000
    PATTERN_RECOGNITION_WINDOW = 50

# ========== TIPOS OTIMIZADOS PARA LEXTRADER ==========

class MarketImpactLevel(IntEnum):
    """Níveis de impacto de mercado para o LEXTRADER."""
    MINIMAL = 0      # 0-10% impacto
    MODERATE = 1     # 10-30% impacto  
    SIGNIFICANT = 2  # 30-60% impacto
    CRITICAL = 3     # 60-100% impacto

class TradingSignal(IntEnum):
    """Sinais de trading do sistema LEXTRADER."""
    STRONG_SELL = -2
    SELL = -1
    NEUTRAL = 0
    BUY = 1
    STRONG_BUY = 2

@dataclass(frozen=True, slots=True)
class NeuralStateVector:
    """
    Vetor de estado neural do LEXTRADER.
    Representa o estado emocional e cognitivo da AGI.
    """
    stability: float = 50.0          # Estabilidade emocional (0-100)
    confidence: float = 50.0         # Confiança nas decisões (0-100)
    aggression: float = 25.0         # Agressividade do trading (0-100)
    caution: float = 75.0            # Nível de cautela (0-100)
    learning_rate: float = 0.7       # Taxa de aprendizado atual (0-1)
    
    def to_dict(self) -> Dict[str, float]:
        """Converte para dicionário serializável."""
        return {
            'stability': self.stability,
            'confidence': self.confidence,
            'aggression': self.aggression,
            'caution': self.caution,
            'learning_rate': self.learning_rate
        }

# ========== ESTRUTURAS DE DADOS LEXTRADER ==========

@dataclass(slots=True)
class MarketEvent:
    """Evento de mercado do LEXTRADER."""
    id: str
    timestamp: datetime
    event_type: str                    # NEWS, PRICE, VOLUME, SOCIAL
    market: str                       # BTC, ETH, SPY, etc
    impact: MarketImpactLevel
    magnitude: float                  # 0-1 scale
    metadata: Dict[str, Any]
    processed: bool = False
    
    def get_hash(self) -> str:
        """Gera hash única para o evento."""
        content = f"{self.timestamp}_{self.event_type}_{self.market}_{self.impact}"
        return hashlib.md5(content.encode()).hexdigest()[:16]

@dataclass(slots=True)
class TradingDecision:
    """Decisão de trading do LEXTRADER."""
    id: str
    timestamp: datetime
    market: str
    signal: TradingSignal
    confidence: float                 # 0-1
    entry_price: Optional[float]
    stop_loss: Optional[float]
    take_profit: Optional[float]
    position_size: float
    rationale: str
    neural_state: NeuralStateVector
    
    def calculate_risk_reward(self) -> Optional[float]:
        """Calcula ratio risco/recompensa."""
        if self.entry_price and self.stop_loss and self.take_profit:
            risk = abs(self.entry_price - self.stop_loss)
            reward = abs(self.take_profit - self.entry_price)
            return reward / risk if risk > 0 else None
        return None

@dataclass(slots=True)
class PerformanceMetrics:
    """Métricas de performance do LEXTRADER."""
    total_trades: int
    winning_trades: int
    losing_trades: int
    win_rate: float
    total_pnl: float
    avg_win: float
    avg_loss: float
    expectancy: float
    sharpe_ratio: float
    max_drawdown: float
    current_drawdown: float
    volatility: float
    calmar_ratio: float
    
    @property
    def profit_factor(self) -> float:
        """Calcula fator de lucro."""
        if self.avg_loss == 0:
            return float('inf')
        return (self.win_rate * self.avg_win) / ((1 - self.win_rate) * abs(self.avg_loss))

# ========== CORE NEURAL LEXTRADER ==========

class LexTraderNeuralCore:
    """
    Núcleo neural do LEXTRADER-IAG 3.0.
    Sistema de aprendizado adaptativo com memória de mercado.
    """
    
    __slots__ = ('_state', '_memory', '_patterns', '_market_memory', '_lock')
    
    def __init__(self):
        self._state = NeuralStateVector()
        self._memory = deque(maxlen=LextraderConfig.MEMORY_CAPACITY)
        self._patterns = {}
        self._market_memory = {}
        self._lock = asyncio.Lock()
    
    @property
    def state(self) -> NeuralStateVector:
        """Estado neural atual."""
        return self._state
    
    async def process_market_event(self, event: MarketEvent) -> NeuralStateVector:
        """
        Processa evento de mercado e atualiza estado neural.
        
        Args:
            event: Evento de mercado para processar
            
        Returns:
            Novo estado neural
        """
        async with self._lock:
            # Registrar evento na memória
            self._memory.append({
                'timestamp': event.timestamp,
                'type': event.event_type,
                'market': event.market,
                'impact': event.impact,
                'magnitude': event.magnitude
            })
            
            # Atualizar memória de mercado
            if event.market not in self._market_memory:
                self._market_memory[event.market] = deque(maxlen=100)
            self._market_memory[event.market].append(event)
            
            # Calcular ajustes de estado
            adjustments = self._calculate_neural_adjustments(event)
            
            # Aplicar ajustes com taxa de aprendizado
            lr = self._state.learning_rate
            new_state = NeuralStateVector(
                stability=max(0, min(100, self._state.stability + adjustments['stability'] * lr)),
                confidence=max(0, min(100, self._state.confidence + adjustments['confidence'] * lr)),
                aggression=max(0, min(100, self._state.aggression + adjustments['aggression'] * lr)),
                caution=max(0, min(100, self._state.caution + adjustments['caution'] * lr)),
                learning_rate=max(0.1, min(1.0, self._state.learning_rate + adjustments['learning_rate']))
            )
            
            self._state = new_state
            
            # Detectar padrões
            self._detect_patterns(event)
            
            return self._state
    
    def _calculate_neural_adjustments(self, event: MarketEvent) -> Dict[str, float]:
        """Calcula ajustes neurais baseados no evento."""
        base_impact = event.magnitude * (event.impact.value + 1)
        
        if event.event_type == "NEWS":
            return {
                'stability': -base_impact * 0.5,
                'confidence': base_impact * 0.3,
                'aggression': base_impact * 0.2,
                'caution': base_impact * 0.4,
                'learning_rate': 0.01
            }
        elif event.event_type == "PRICE":
            return {
                'stability': base_impact * 0.3,
                'confidence': base_impact * 0.5,
                'aggression': -base_impact * 0.2,
                'caution': -base_impact * 0.3,
                'learning_rate': 0.02
            }
        else:
            return {
                'stability': base_impact * 0.1,
                'confidence': base_impact * 0.2,
                'aggression': base_impact * 0.1,
                'caution': base_impact * 0.1,
                'learning_rate': 0.005
            }
    
    def _detect_patterns(self, event: MarketEvent):
        """Detecta padrões de mercado."""
        if len(self._memory) < 10:
            return
        
        # Detecção básica de padrões (expandível com ML)
        recent_events = list(self._memory)[-10:]
        
        # Contar tipos de eventos recentes
        type_counts = {}
        for e in recent_events:
            type_counts[e['type']] = type_counts.get(e['type'], 0) + 1
        
        # Se muitos eventos de alto impacto, padrão de volatilidade
        if sum(1 for e in recent_events if e['impact'] >= MarketImpactLevel.SIGNIFICANT) >= 3:
            self._patterns['high_volatility'] = {
                'detected_at': datetime.now(),
                'confidence': 0.7,
                'suggestion': 'reduce_position_size'
            }
    
    def get_market_insight(self, market: str) -> Dict[str, Any]:
        """Retorna insights do mercado específico."""
        if market not in self._market_memory:
            return {'status': 'NO_DATA', 'events': 0}
        
        events = list(self._market_memory[market])
        if not events:
            return {'status': 'NO_DATA', 'events': 0}
        
        # Análise básica
        recent_events = events[-20:]
        impact_distribution = {}
        
        for event in recent_events:
            impact = event.impact.name
            impact_distribution[impact] = impact_distribution.get(impact, 0) + 1
        
        return {
            'status': 'ANALYZED',
            'total_events': len(events),
            'recent_events': len(recent_events),
            'impact_distribution': impact_distribution,
            'last_event': events[-1].timestamp.strftime("%Y-%m-%d %H:%M:%S"),
            'market_sentiment': self._calculate_market_sentiment(events[-10:])
        }
    
    def _calculate_market_sentiment(self, events: List[MarketEvent]) -> str:
        """Calcula sentimento do mercado."""
        if not events:
            return "NEUTRAL"
        
        sentiment_score = 0
        for event in events:
            if event.event_type == "NEWS":
                if event.impact >= MarketImpactLevel.SIGNIFICANT:
                    sentiment_score += 2
                else:
                    sentiment_score += 1
            elif event.event_type == "PRICE":
                sentiment_score += 1
        
        if sentiment_score > 15:
            return "EXTREME_BULLISH"
        elif sentiment_score > 10:
            return "BULLISH"
        elif sentiment_score > 5:
            return "SLIGHTLY_BULLISH"
        elif sentiment_score < -15:
            return "EXTREME_BEARISH"
        elif sentiment_score < -10:
            return "BEARISH"
        elif sentiment_score < -5:
            return "SLIGHTLY_BEARISH"
        else:
            return "NEUTRAL"

# ========== SISTEMA DE DECISÃO LEXTRADER ==========

class LexTraderDecisionEngine:
    """
    Motor de decisão do LEXTRADER.
    Combina análise neural com regras de risco.
    """
    
    __slots__ = ('neural_core', '_risk_rules', '_decision_history', '_performance')
    
    def __init__(self, neural_core: LexTraderNeuralCore):
        self.neural_core = neural_core
        self._risk_rules = self._initialize_risk_rules()
        self._decision_history = deque(maxlen=1000)
        self._performance = self._initialize_performance()
    
    def _initialize_risk_rules(self) -> Dict[str, Any]:
        """Inicializa regras de risco."""
        return {
            'max_position_size': 0.15,  # 15% do capital por trade
            'min_risk_reward': 1.5,     # Ratio mínimo 1.5:1
            'max_daily_trades': 10,
            'cooldown_period': 300,     # 5 minutos entre trades
            'correlation_limit': 0.7,   # Limite de correlação
        }
    
    def _initialize_performance(self) -> PerformanceMetrics:
        """Inicializa métricas de performance."""
        return PerformanceMetrics(
            total_trades=0,
            winning_trades=0,
            losing_trades=0,
            win_rate=0.0,
            total_pnl=0.0,
            avg_win=0.0,
            avg_loss=0.0,
            expectancy=0.0,
            sharpe_ratio=0.0,
            max_drawdown=0.0,
            current_drawdown=0.0,
            volatility=0.0,
            calmar_ratio=0.0
        )
    
    async def analyze_market(self, market_data: Dict[str, Any]) -> TradingDecision:
        """
        Analisa mercado e gera decisão de trading.
        
        Args:
            market_data: Dados do mercado
            
        Returns:
            Decisão de trading
        """
        # Obter estado neural atual
        neural_state = self.neural_core.state
        
        # Análise do mercado
        analysis = self._analyze_market_data(market_data)
        
        # Gerar sinal
        signal, confidence = self._generate_signal(analysis, neural_state)
        
        # Calcular tamanho da posição
        position_size = self._calculate_position_size(
            confidence=confidence,
            neural_state=neural_state,
            market_volatility=analysis.get('volatility', 0.0)
        )
        
        # Gerar decisão
        decision = TradingDecision(
            id=f"D-{int(time.time() * 1000)}-{hashlib.md5(str(market_data).encode()).hexdigest()[:8]}",
            timestamp=datetime.now(),
            market=market_data.get('symbol', 'UNKNOWN'),
            signal=signal,
            confidence=confidence,
            entry_price=market_data.get('price'),
            stop_loss=self._calculate_stop_loss(market_data, signal, neural_state),
            take_profit=self._calculate_take_profit(market_data, signal, neural_state),
            position_size=position_size,
            rationale=self._generate_rationale(signal, confidence, analysis, neural_state),
            neural_state=neural_state
        )
        
        # Registrar decisão
        self._decision_history.append(decision)
        
        return decision
    
    def _analyze_market_data(self, market_data: Dict[str, Any]) -> Dict[str, Any]:
        """Analisa dados do mercado."""
        analysis = {
            'volatility': random.uniform(0.01, 0.05),
            'trend_strength': random.uniform(0.0, 1.0),
            'volume_ratio': random.uniform(0.8, 1.2),
            'support_levels': [],
            'resistance_levels': [],
            'market_structure': random.choice(['TRENDING', 'RANGING', 'BREAKOUT'])
        }
        
        # Adicionar análise básica de tendência
        price = market_data.get('price', 0)
        if price > 0:
            analysis['price_action'] = 'BULLISH' if random.random() > 0.5 else 'BEARISH'
        
        return analysis
    
    def _generate_signal(self, analysis: Dict[str, Any], neural_state: NeuralStateVector) -> tuple:
        """Gera sinal de trading."""
        # Baseado na análise técnica
        tech_score = analysis.get('trend_strength', 0.5)
        
        # Ajustar pelo estado neural
        neural_adjustment = (neural_state.confidence - 50) / 100
        aggression_factor = neural_state.aggression / 100
        
        # Calcular sinal final
        final_score = tech_score + (neural_adjustment * aggression_factor)
        
        # Mapear para sinal
        if final_score > 0.7:
            signal = TradingSignal.STRONG_BUY
            confidence = min(0.95, final_score)
        elif final_score > 0.55:
            signal = TradingSignal.BUY
            confidence = final_score
        elif final_score < 0.3:
            signal = TradingSignal.STRONG_SELL
            confidence = min(0.95, 1 - final_score)
        elif final_score < 0.45:
            signal = TradingSignal.SELL
            confidence = 1 - final_score
        else:
            signal = TradingSignal.NEUTRAL
            confidence = 0.5
        
        return signal, confidence
    
    def _calculate_position_size(self, confidence: float, neural_state: NeuralStateVector, 
                                market_volatility: float) -> float:
        """Calcula tamanho da posição."""
        base_size = self._risk_rules['max_position_size']
        
        # Ajustar por confiança
        confidence_adjustment = confidence ** 2  # Quadrático para mais conservadorismo
        
        # Ajustar por estado neural
        neural_adjustment = (neural_state.confidence * 0.7 + neural_state.stability * 0.3) / 100
        
        # Ajustar por volatilidade
        volatility_adjustment = 1.0 / (1.0 + market_volatility * 10)
        
        # Tamanho final
        final_size = base_size * confidence_adjustment * neural_adjustment * volatility_adjustment
        
        # Limites
        return max(0.01, min(base_size, final_size))
    
    def _calculate_stop_loss(self, market_data: Dict[str, Any], signal: TradingSignal,
                           neural_state: NeuralStateVector) -> Optional[float]:
        """Calcula stop loss."""
        price = market_data.get('price')
        if not price:
            return None
        
        # Baseado na volatilidade e estado neural
        base_atr = price * 0.02  # 2% como ATR proxy
        
        # Ajustar por agressividade
        aggression_factor = neural_state.aggression / 100
        adjusted_atr = base_atr * (1.5 - aggression_factor)  # Mais agressivo = stop mais apertado
        
        if signal.value > 0:  # Compra
            return price * (1 - adjusted_atr / price)
        elif signal.value < 0:  # Venda
            return price * (1 + adjusted_atr / price)
        
        return None
    
    def _calculate_take_profit(self, market_data: Dict[str, Any], signal: TradingSignal,
                             neural_state: NeuralStateVector) -> Optional[float]:
        """Calcula take profit."""
        stop_loss = self._calculate_stop_loss(market_data, signal, neural_state)
        price = market_data.get('price')
        
        if not price or not stop_loss:
            return None
        
        # Ratio risco/recompensa mínimo
        min_rr = self._risk_rules['min_risk_reward']
        risk = abs(price - stop_loss)
        
        if signal.value > 0:  # Compra
            return price + (risk * min_rr * (1 + neural_state.confidence / 200))
        elif signal.value < 0:  # Venda
            return price - (risk * min_rr * (1 + neural_state.confidence / 200))
        
        return None
    
    def _generate_rationale(self, signal: TradingSignal, confidence: float,
                          analysis: Dict[str, Any], neural_state: NeuralStateVector) -> str:
        """Gera racional para a decisão."""
        signal_names = {
            -2: "VENDA FORTE",
            -1: "VENDA",
            0: "NEUTRO",
            1: "COMPRA",
            2: "COMPRA FORTE"
        }
        
        market_structure = analysis.get('market_structure', 'DESCONHECIDO')
        trend_strength = analysis.get('trend_strength', 0.5)
        
        return (f"Sinal: {signal_names[signal.value]} | "
                f"Confiança: {confidence:.1%} | "
                f"Estrutura: {market_structure} | "
                f"Força da Tendência: {trend_strength:.1%} | "
                f"Estado Neural: C{neural_state.confidence:.0f}/S{neural_state.stability:.0f}")

# ========== SISTEMA PRINCIPAL LEXTRADER-IAG ==========

class LextraderIAGSystem:
    """
    Sistema principal LEXTRADER-IAG 3.0.
    Orquestra todos os componentes do sistema de trading.
    """
    
    __slots__ = (
        'neural_core', 'decision_engine', '_market_events',
        '_trading_decisions', '_system_status', '_executor',
        '_is_running', '_cycle_task', '_performance_monitor'
    )
    
    def __init__(self):
        self.neural_core = LexTraderNeuralCore()
        self.decision_engine = LexTraderDecisionEngine(self.neural_core)
        
        # Estruturas de dados
        self._market_events = deque(maxlen=5000)
        self._trading_decisions = deque(maxlen=1000)
        
        # Status do sistema
        self._system_status = {
            'version': LextraderConfig.VERSION,
            'start_time': datetime.now(),
            'status': 'INITIALIZING',
            'cycles_completed': 0,
            'events_processed': 0,
            'decisions_generated': 0,
            'active_markets': set(),
            'last_error': None
        }
        
        # Executor para operações paralelas
        self._executor = ThreadPoolExecutor(
            max_workers=LextraderConfig.MAX_PARALLEL_CYCLES,
            thread_name_prefix="lextrader_"
        )
        
        self._is_running = False
        self._cycle_task = None
        self._performance_monitor = PerformanceMonitor()
    
    async def start(self, market_focus: str = None) -> None:
        """
        Inicia o sistema LEXTRADER-IAG.
        
        Args:
            market_focus: Mercado foco inicial (opcional)
        """
        if self._is_running:
            return
        
        self._is_running = True
        self._system_status['status'] = 'RUNNING'
        self._system_status['start_time'] = datetime.now()
        
        if market_focus:
            self._system_status['active_markets'].add(market_focus)
        
        print(f"""
╔══════════════════════════════════════════════════════════╗
║                LEXTRADER-IAG 3.0 - OMEGA                 ║
║                    Sistema Iniciado                      ║
║                Versão: {LextraderConfig.VERSION}                      ║
║                Modo: {LextraderConfig.MODE}                ║
╚══════════════════════════════════════════════════════════╝
        """)
        
        # Iniciar ciclo principal
        self._cycle_task = asyncio.create_task(
            self._main_cycle(),
            name="lextrader_main_cycle"
        )
    
    async def stop(self) -> None:
        """Para o sistema LEXTRADER-IAG."""
        if not self._is_running:
            return
        
        self._is_running = False
        self._system_status['status'] = 'STOPPING'
        
        if self._cycle_task:
            self._cycle_task.cancel()
            try:
                await self._cycle_task
            except asyncio.CancelledError:
                pass
        
        self._executor.shutdown(wait=True)
        self._system_status['status'] = 'STOPPED'
        
        print(f"""
╔══════════════════════════════════════════════════════════╗
║                LEXTRADER-IAG 3.0 - OMEGA                 ║
║                    Sistema Finalizado                    ║
║                Ciclos: {self._system_status['cycles_completed']}                   ║
║                Eventos: {self._system_status['events_processed']}                 ║
║                Decisões: {self._system_status['decisions_generated']}                ║
╚══════════════════════════════════════════════════════════╝
        """)
    
    async def _main_cycle(self) -> None:
        """Ciclo principal do sistema."""
        cycle_count = 0
        
        try:
            while self._is_running:
                cycle_start = time.perf_counter()
                cycle_count += 1
                
                # Executar sub-ciclos em paralelo
                await self._execute_parallel_cycles()
                
                # Atualizar métricas
                await self._update_system_metrics()
                
                # Log de performance
                cycle_time = time.perf_counter() - cycle_start
                if cycle_count % 10 == 0:
                    await self._log_performance(cycle_count, cycle_time)
                
                # Sleep adaptativo
                await asyncio.sleep(max(1.0, 5.0 - cycle_time))
                
        except asyncio.CancelledError:
            raise
        except Exception as e:
            self._system_status['last_error'] = str(e)
            print(f"❌ Erro no ciclo principal: {e}")
            await self.stop()
    
    async def _execute_parallel_cycles(self) -> None:
        """Executa sub-ciclos em paralelo."""
        tasks = []
        
        # Para cada mercado ativo, criar task de análise
        for market in list(self._system_status['active_markets'])[:5]:  # Limitar a 5 mercados
            task = asyncio.create_task(
                self._analyze_market_cycle(market),
                name=f"market_analysis_{market}"
            )
            tasks.append(task)
        
        # Adicionar task de coleta de eventos
        tasks.append(asyncio.create_task(self._collect_market_events()))
        
        # Executar todas em paralelo
        if tasks:
            await asyncio.gather(*tasks, return_exceptions=True)
    
    async def _analyze_market_cycle(self, market: str) -> None:
        """Ciclo de análise para um mercado específico."""
        try:
            # Simular dados de mercado
            market_data = self._generate_market_data(market)
            
            # Criar evento de mercado
            event = MarketEvent(
                id=f"EV-{int(time.time() * 1000)}-{market}",
                timestamp=datetime.now(),
                event_type="PRICE",
                market=market,
                impact=MarketImpactLevel(random.randint(0, 3)),
                magnitude=random.uniform(0.1, 1.0),
                metadata=market_data
            )
            
            # Processar evento
            await self.neural_core.process_market_event(event)
            self._market_events.append(event)
            self._system_status['events_processed'] += 1
            
            # Gerar decisão
            decision = await self.decision_engine.analyze_market(market_data)
            self._trading_decisions.append(decision)
            self._system_status['decisions_generated'] += 1
            
            # Log se decisão não for neutra
            if decision.signal != TradingSignal.NEUTRAL:
                print(f"📊 {market}: {decision.signal.name} | "
                      f"Conf: {decision.confidence:.1%} | "
                      f"Size: {decision.position_size:.2%}")
            
        except Exception as e:
            print(f"⚠️  Erro análise {market}: {e}")
    
    async def _collect_market_events(self) -> None:
        """Coleta eventos de mercado."""
        # Simular coleta de notícias
        if random.random() > 0.7:  # 30% chance de evento
            news_event = MarketEvent(
                id=f"NEWS-{int(time.time() * 1000)}",
                timestamp=datetime.now(),
                event_type="NEWS",
                market=random.choice(list(self._system_status['active_markets']) or ['GLOBAL']),
                impact=MarketImpactLevel(random.randint(1, 3)),
                magnitude=random.uniform(0.3, 0.9),
                metadata={
                    'source': random.choice(['Reuters', 'Bloomberg', 'CNBC', 'WSJ']),
                    'headline': random.choice([
                        "Fed anuncia política monetária",
                        "Dados econômicos surpreendem",
                        "Empresa reporta lucros recorde",
                        "Tensão geopolítica aumenta"
                    ])
                }
            )
            
            await self.neural_core.process_market_event(news_event)
            self._market_events.append(news_event)
            self._system_status['events_processed'] += 1
    
    def _generate_market_data(self, market: str) -> Dict[str, Any]:
        """Gera dados de mercado simulados."""
        base_price = {
            'BTC': 45000 + random.uniform(-2000, 2000),
            'ETH': 2500 + random.uniform(-200, 200),
            'SPY': 450 + random.uniform(-10, 10),
            'GOLD': 1950 + random.uniform(-50, 50)
        }.get(market, 100 + random.uniform(-10, 10))
        
        return {
            'symbol': market,
            'price': base_price,
            'volume': random.uniform(1000000, 5000000),
            'change_24h': random.uniform(-0.05, 0.05),
            'high_24h': base_price * (1 + random.uniform(0, 0.03)),
            'low_24h': base_price * (1 - random.uniform(0, 0.03)),
            'timestamp': datetime.now()
        }
    
    async def _update_system_metrics(self) -> None:
        """Atualiza métricas do sistema."""
        self._system_status['cycles_completed'] += 1
        
        # Atualizar status baseado no estado neural
        neural_state = self.neural_core.state
        if neural_state.stability < 30:
            self._system_status['status'] = 'HIGH_STRESS'
        elif neural_state.confidence > 70:
            self._system_status['status'] = 'CONFIDENT'
        else:
            self._system_status['status'] = 'NORMAL'
    
    async def _log_performance(self, cycle_count: int, cycle_time: float) -> None:
        """Log de performance do sistema."""
        neural_state = self.neural_core.state
        
        print(f"""
╔══════════════════════════════════════════════════════════╗
║                 LEXTRADER-IAG - STATUS                   ║
╠══════════════════════════════════════════════════════════╣
║ Ciclo: #{cycle_count:<10} Tempo: {cycle_time:.3f}s               ║
║ Status: {self._system_status['status']:<20}                    ║
║ Eventos: {self._system_status['events_processed']:<8} Decisões: {self._system_status['decisions_generated']:<6} ║
╠══════════════════════════════════════════════════════════╣
║ Estado Neural:                                           ║
║   • Estabilidade: {neural_state.stability:6.1f}%                     ║
║   • Confiança:   {neural_state.confidence:6.1f}%                     ║
║   • Agressividade: {neural_state.aggression:5.1f}%                    ║
║   • Cautela:     {neural_state.caution:6.1f}%                     ║
╚══════════════════════════════════════════════════════════╝
        """)
    
    def get_system_status(self) -> Dict[str, Any]:
        """Retorna status completo do sistema."""
        neural_state = self.neural_core.state
        
        return {
            **self._system_status,
            'neural_state': neural_state.to_dict(),
            'market_insights': {
                market: self.neural_core.get_market_insight(market)
                for market in self._system_status['active_markets']
            },
            'performance_metrics': self._performance_monitor.get_current_metrics(),
            'uptime': str(datetime.now() - self._system_status['start_time']),
            'memory_usage': {
                'market_events': len(self._market_events),
                'trading_decisions': len(self._trading_decisions),
                'active_markets': len(self._system_status['active_markets'])
            }
        }
    
    def get_recent_decisions(self, limit: int = 10) -> List[Dict[str, Any]]:
        """Retorna decisões recentes."""
        decisions = []
        for decision in list(self._trading_decisions)[:limit]:
            decisions.append({
                'id': decision.id,
                'timestamp': decision.timestamp.strftime("%H:%M:%S"),
                'market': decision.market,
                'signal': decision.signal.name,
                'confidence': f"{decision.confidence:.1%}",
                'position_size': f"{decision.position_size:.2%}",
                'rationale': decision.rationale,
                'risk_reward': decision.calculate_risk_reward()
            })
        return decisions

# ========== MONITOR DE PERFORMANCE ==========

class PerformanceMonitor:
    """Monitor de performance do sistema."""
    
    def __init__(self):
        self.metrics_history = deque(maxlen=100)
    
    def record_trade(self, pnl: float, win: bool) -> None:
        """Registra resultado de trade."""
        self.metrics_history.append({
            'timestamp': datetime.now(),
            'pnl': pnl,
            'win': win
        })
    
    def get_current_metrics(self) -> Dict[str, Any]:
        """Calcula métricas atuais."""
        if not self.metrics_history:
            return {'status': 'NO_DATA'}
        
        trades = list(self.metrics_history)
        winning_trades = [t for t in trades if t['win']]
        losing_trades = [t for t in trades if not t['win']]
        
        total_trades = len(trades)
        win_count = len(winning_trades)
        loss_count = len(losing_trades)
        
        win_rate = win_count / total_trades if total_trades > 0 else 0
        total_pnl = sum(t['pnl'] for t in trades)
        avg_win = np.mean([t['pnl'] for t in winning_trades]) if winning_trades else 0
        avg_loss = np.mean([t['pnl'] for t in losing_trades]) if losing_trades else 0
        
        return {
            'total_trades': total_trades,
            'winning_trades': win_count,
            'losing_trades': loss_count,
            'win_rate': f"{win_rate:.1%}",
            'total_pnl': f"{total_pnl:+.2f}",
            'avg_win': f"{avg_win:.2f}",
            'avg_loss': f"{avg_loss:.2f}",
            'profit_factor': f"{abs(avg_win * win_count / (avg_loss * loss_count)):.2f}" if loss_count > 0 else "∞"
        }

# ========== INSTÂNCIA GLOBAL LEXTRADER ==========

class LextraderSystem:
    """Wrapper singleton para o sistema LEXTRADER."""
    
    _instance: Optional[LextraderIAGSystem] = None
    
    @classmethod
    def get_instance(cls) -> LextraderIAGSystem:
        """Retorna instância singleton do LEXTRADER."""
        if cls._instance is None:
            cls._instance = LextraderIAGSystem()
        return cls._instance

# ========== EXEMPLO DE USO ==========

async def demonstrate_lextrader():
    """Demonstração do sistema LEXTRADER-IAG 3.0."""
    print("""
╔══════════════════════════════════════════════════════════╗
║           LEXTRADER-IAG 3.0 - DEMONSTRAÇÃO               ║
║                   Função Omega                           ║
╚══════════════════════════════════════════════════════════╝
    """)
    
    lextrader = LextraderSystem.get_instance()
    
    # Iniciar sistema
    print("▶️  Iniciando sistema LEXTRADER-IAG...")
    await lextrader.start(market_focus="BTC")
    
    # Adicionar mais mercados
    lextrader._system_status['active_markets'].update(["ETH", "SPY", "GOLD"])
    
    # Executar por 15 segundos
    print("⏳ Executando análise de mercado (15 segundos)...")
    await asyncio.sleep(15)
    
    # Obter status
    print("\n📊 Status do Sistema:")
    status = lextrader.get_system_status()
    
    print(f"• Versão: {status['version']}")
    print(f"• Status: {status['status']}")
    print(f"• Uptime: {status['uptime']}")
    print(f"• Ciclos: {status['cycles_completed']}")
    print(f"• Eventos: {status['events_processed']}")
    print(f"• Decisões: {status['decisions_generated']}")
    
    print("\n🧠 Estado Neural:")
    neural = status['neural_state']
    for key, value in neural.items():
        print(f"  {key}: {value:.1f}")
    
    print("\n📈 Decisões Recentes:")
    decisions = lextrader.get_recent_decisions(5)
    for decision in decisions:
        signal_emoji = "🟢" if "BUY" in decision['signal'] else "🔴" if "SELL" in decision['signal'] else "🟡"
        print(f"  {signal_emoji} [{decision['timestamp']}] {decision['market']}: "
              f"{decision['signal']} ({decision['confidence']})")
    
    print("\n📊 Insights de Mercado:")
    insights = status['market_insights']
    for market, insight in insights.items():
        if insight['status'] == 'ANALYZED':
            print(f"  {market}: {insight['market_sentiment']} "
                  f"({insight['recent_events']} eventos recentes)")
    
    # Parar sistema
    print("\n⏹️  Finalizando demonstração...")
    await lextrader.stop()
    
    print("""
╔══════════════════════════════════════════════════════════╗
║        LEXTRADER-IAG 3.0 - DEMONSTRAÇÃO CONCLUÍDA        ║
╚══════════════════════════════════════════════════════════╝
    """)

async def main():
    """Função principal."""
    # Configurar seed para reprodutibilidade
    random.seed(42)
    
    try:
        await demonstrate_lextrader()
    except KeyboardInterrupt:
        print("\n\n👋 Sistema LEXTRADER-IAG interrompido pelo usuário")
        
        # Garantir parada adequada
        lextrader = LextraderSystem.get_instance()
        await lextrader.stop()
    except Exception as e:
        print(f"\n❌ Erro fatal: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    asyncio.run(main(), debug=False)