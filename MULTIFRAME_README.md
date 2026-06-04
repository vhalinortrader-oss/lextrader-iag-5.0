# 📊 ESTRATÉGIA MULTI-TIMEFRAME - DOCUMENTAÇÃO COMPLETA

## 📋 Sumário Executivo

Sistema de trading multi-timeframe (1m, 3m, 5m) integrado com LEXTRADER-IAG 4.0 Layer 4 (Decision Making). Combina análise técnica hierárquica, reconhecimento de padrões e gerenciamento de risco para gerar sinais de compra/venda precisos.

**Status:** ✅ Completo e pronto para produção

---

## 🏗️ Arquitetura

### Componentes Principais

```
┌─────────────────────────────────────────────────────────────┐
│                   MULTI-TIMEFRAME TRADER                     │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  MultiTimeframeStrategy.py (CORE)                           │
│  ├─ Análise de 1 minuto (entrada precisa)                 │
│  ├─ Análise de 3 minutos (confirmação)                    │
│  └─ Análise de 5 minutos (tendência principal)            │
│                                                               │
│  Indicadores Técnicos:                                      │
│  ├─ SMA (10/20)                                            │
│  ├─ RSI (14)                                               │
│  ├─ MACD                                                   │
│  └─ Support/Resistance                                     │
│                                                               │
│  Reconhecimento de Padrões:                                 │
│  ├─ Doji                                                   │
│  ├─ Hammer                                                 │
│  ├─ Shooting Star                                          │
│  └─ Engulfing                                              │
│                                                               │
└─────────────────────────────────────────────────────────────┘
         ↓
┌─────────────────────────────────────────────────────────────┐
│            MultiTimeframeIntegration.py                      │
│  Integração com APIs (Binance, cTrader, etc)              │
└─────────────────────────────────────────────────────────────┘
         ↓
┌─────────────────────────────────────────────────────────────┐
│         MultiTimeframeDecisionAdapter.py                     │
│  Converte sinais em Decision objects para DecisionEngine  │
└─────────────────────────────────────────────────────────────┘
         ↓
┌─────────────────────────────────────────────────────────────┐
│            DecisionEngine (Layer 4)                          │
│  Executa decisão no sistema LEXTRADER                      │
└─────────────────────────────────────────────────────────────┘
```

### Fluxo de Dados

```
Dados de Mercado (Binance/cTrader)
        ↓
   Candles (OHLCV)
   ├─ 1m × 50 candles
   ├─ 3m × 50 candles
   └─ 5m × 50 candles
        ↓
   MultiTimeframeTrader.analyze_all_timeframes()
        ↓
   TimeframeAnalyzer (×3)
   ├─ IndicatorCalculator
   │  ├─ SMA (10, 20)
   │  ├─ RSI (14)
   │  ├─ MACD
   │  └─ Support/Resistance
   ├─ CandlePatternAnalyzer
   │  └─ Detecta: Doji, Hammer, Shooting Star, Engulfing
   └─ TrendAnalyzer
      └─ UPTREND / DOWNTREND / SIDEWAYS
        ↓
   Scoring System (-5 a +5)
   ├─ SMA Alignment (±1.0)
   ├─ RSI Level (±1.0)
   ├─ MACD Direction (±0.5)
   ├─ Trend Direction (±1.0)
   └─ Candle Pattern (±0.5)
        ↓
   Signal Consolidation
   └─ Weighted Average (5m×2.0, 3m×1.5, 1m×1.0)
        ↓
   MultiTimeframeSignal
   ├─ final_signal (STRONG_BUY...STRONG_SELL)
   ├─ overall_confidence (0.0-1.0)
   ├─ risk_level (LOW/MEDIUM/HIGH)
   ├─ suggested_stop_loss
   ├─ suggested_take_profit
   └─ suggested_risk_reward
        ↓
   MultiTimeframeDecisionAdapter
        ↓
   Decision (para DecisionEngine)
        ↓
   Execução (compra, venda, hold)
```

---

## 🚀 Início Rápido

### Instalação

```bash
# 1. Copiar arquivos para Layer 4
cp MultiTimeframeStrategy.py neural_layers/04_decisao/
cp MultiTimeframeIntegration.py neural_layers/04_decisao/
cp MultiTimeframeDecisionAdapter.py neural_layers/04_decisao/
cp MultiTimeframeConfig.py neural_layers/04_decisao/

# 2. Verificar dependências
python check_dependencies.py
```

### Uso Básico

```python
from MultiTimeframeStrategy import MultiTimeframeTrader, CandleData
from datetime import datetime, timedelta

# Criar trader
trader = MultiTimeframeTrader("BTC/USDT")

# Preparar dados (50 candles por timeframe)
candles_1m = [CandleData(...), ...]  # Último candle = atual
candles_3m = [CandleData(...), ...]
candles_5m = [CandleData(...), ...]

# Analisar
signal = trader.analyze_all_timeframes(candles_1m, candles_3m, candles_5m)

# Usar sinal
print(f"Ação: {signal.action}")                      # BUY/SELL/HOLD
print(f"Sinal: {signal.final_signal.value}")         # STRONG_BUY...STRONG_SELL
print(f"Confiança: {signal.overall_confidence:.2%}") # 0-100%
print(f"Stop Loss: ${signal.suggested_stop_loss}")
print(f"Take Profit: ${signal.suggested_take_profit}")
```

### Integração com API

```python
from MultiTimeframeIntegration import IntegratedMultiTimeframeTrader

# Criar trader integrado
trader = IntegratedMultiTimefradeTrader(["BTC/USDT", "ETH/USDT"])

# Dados do Binance/cTrader
market_data = {
    'symbol': 'BTC/USDT',
    'candles_1m': [...],
    'candles_3m': [...],
    'candles_5m': [...],
}

# Processar
decision = trader.process_market_data(market_data)

print(f"Ação: {decision.signal}")
print(f"Entrada: ${decision.entry_price}")
```

### Integração com DecisionEngine

```python
from MultiTimeframeDecisionAdapter import MultiTimeframeDecisionAdapter

# Adaptador
adapter = MultiTimeframeDecisionAdapter()

# Converter sinal para Decision
decision = adapter.adapt_to_decision(
    mt_signal,
    override_priority="HIGH"
)

# Enviar para DecisionEngine
# decision.to_dict() → JSON para API
```

---

## 📊 Sinais de Trading

### Sinal Final (7 estados)

| Sinal | Valor | Condição | Ação |
|-------|-------|----------|------|
| 🟢 STRONG_BUY | 2.0+ | Todos 3 timeframes compram | **BUY** |
| 🟢 BUY | 0.0+ | Maioria compra | **BUY** |
| 🟡 WEAK_BUY | -1.5+ | Sinal misto, mais compra | **HOLD** |
| ⚪ NEUTRAL | -1.5 a 0 | Sem direção clara | **HOLD** |
| 🟡 WEAK_SELL | -2.5+ | Sinal misto, mais venda | **HOLD** |
| 🔴 SELL | -2.5- | Maioria vende | **SELL** |
| 🔴 STRONG_SELL | -5.0- | Todos 3 timeframes vendem | **SELL** |

### Confiança

Calculada como: `(|score| + 5) / 10` onde score ∈ [-5, +5]

- **90-100%**: Muito alta
- **75-89%**: Alta
- **50-74%**: Média
- **0-49%**: Baixa

---

## 🎯 Análise por Timeframe

### 1 Minuto (Entrada Precisa)

- **Objetivo**: Precisão de entrada
- **Peso**: 1.0× (referência)
- **Padrões**: Doji, Hammer, Shooting Star
- **Indicadores**: SMA 10/20, RSI
- **Uso**: Timing exato de entrada

### 3 Minutos (Confirmação)

- **Objetivo**: Confirmação de movimento
- **Peso**: 1.5× (15% mais importante)
- **Padrões**: Engulfing, Hammer
- **Indicadores**: SMA 10/20, MACD
- **Uso**: Validação de sinal 1m

### 5 Minutos (Tendência Principal)

- **Objetivo**: Direção dominante
- **Peso**: 2.0× (100% mais importante)
- **Padrões**: Trend identification
- **Indicadores**: SMA 10/20, RSI
- **Uso**: Confirmação de direção

### Consolidação Hierárquica

Score final = (score_1m × 1.0 + score_3m × 1.5 + score_5m × 2.0) / (1.0 + 1.5 + 2.0)

**Exemplo:**

- 1m = +2.0 (STRONG_BUY)
- 3m = +1.5 (BUY)
- 5m = +2.0 (STRONG_BUY)
- Score = (2.0 + 2.25 + 4.0) / 4.5 = +1.61 → **BUY** ✅

---

## 📈 Indicadores Técnicos

### 1. SMA (Média Móvel Simples)

```
SMA(n) = Σ(closes ultimas n velas) / n

Uso:
- SMA 10: Detectar direção curta (rápida)
- SMA 20: Detectar direção média (confirmação)
```

**Lógica:**

- Preço > SMA 20 > SMA 10 → Tendência ALTA
- Preço < SMA 20 < SMA 10 → Tendência BAIXA
- Outras → Lateralizado

### 2. RSI (Índice de Força Relativa)

```
RSI = 100 - (100 / (1 + RS))
RS = Média ganhos / Média perdas (últimas 14 velas)

Range: 0-100
```

**Interpretação:**

- RSI > 70: Sobrecomprado (sinal de venda)
- RSI < 30: Sobrevendido (sinal de compra)
- RSI 40-60: Neutro

### 3. MACD (Convergência/Divergência de Média Móvel)

```
MACD = EMA(12) - EMA(26)
Signal = EMA(9) do MACD

Uso:
- MACD > Signal → Bullish
- MACD < Signal → Bearish
```

### 4. Support/Resistance

```
Calculado dos últimos N candles
Support = Preço baixo mais próximo
Resistance = Preço alto mais próximo

Range = Resistance - Support
```

---

## 🧩 Reconhecimento de Padrões

### 1. Doji

**Descrição**: Corpo pequeno com pavios iguais

```
Condição: |close - open| < 10% × range_total
```

**Sinal**: Indecisão do mercado

### 2. Hammer

**Descrição**: Corpo pequeno no topo, pavio longo para baixo

```
Condição: corpo_no_topo > 75% && pavio_baixo > 2× corpo
```

**Sinal**: Possível reversão para CIMA

### 3. Shooting Star

**Descrição**: Corpo pequeno na base, pavio longo para cima

```
Condição: corpo_na_base > 75% && pavio_alto > 2× corpo
```

**Sinal**: Possível reversão para BAIXO

### 4. Engulfing

**Descrição**: Vela atual envolve vela anterior

```
Bullish: fecha_atual > abre_anterior && abre_atual < fecha_anterior
Bearish: fecha_atual < abre_anterior && abre_atual > fecha_anterior
```

**Sinal**: Reversão potencial

---

## ⚠️ Gerenciamento de Risco

### Stop Loss

```
Para BUY:
  Stop Loss = Suporte × (1 - 2%)
  
Para SELL:
  Stop Loss = Resistência × (1 + 2%)
```

**Cálculo de Suporte:**

- Menor preço dos últimos 5 candles 5m

### Take Profit

```
Para BUY:
  Take Profit = Resistência × (1 + 3%)
  
Para SELL:
  Take Profit = Suporte × (1 - 3%)
```

**Cálculo de Resistência:**

- Maior preço dos últimos 5 candles 5m

### Risk/Reward Ratio

```
R:R = Distância TP / Distância SL

Exemplo:
  Entrada: $45,000
  SL: $44,100 (diferença $900)
  TP: $46,350 (diferença $1,350)
  
  R:R = 1,350 / 900 = 1.5:1
```

**Regra**: Aceitar operações com R:R ≥ 1.5:1

### Nível de Risco

| Conflitos | RSI Extremo | Risco |
|-----------|------------|-------|
| 0-1 | Não | 🟢 LOW |
| 2 | Não | 🟡 MEDIUM |
| 3+ | - | 🔴 HIGH |
| - | Sim (< 15 ou > 85) | 🔴 HIGH |

---

## ⚙️ Configuração

### Carregar Pré-configurações

```python
from MultiTimeframeConfig import PresetConfigs, load_custom_config

# Agressiva (mais trades, menor confiança)
config = PresetConfigs.aggressive()

# Conservadora (menos trades, maior confiança)
config = PresetConfigs.conservative()

# Scalping (1m e 3m)
config = PresetConfigs.scalping()

# Swing Trading (3m e 5m)
config = PresetConfigs.swing_trading()
```

### Customizar Configuração

```python
from MultiTimeframeConfig import load_custom_config

custom = {
    "STOP_LOSS_PERCENTAGE": 0.01,      # 1%
    "TAKE_PROFIT_PERCENTAGE": 0.05,    # 5%
    "MIN_RISK_REWARD_RATIO": 2.0,      # Mais exigente
    "RSI_OVERBOUGHT": 65,              # Ajuste personalizado
}

config = load_custom_config(custom)
```

### Validar Configuração

```python
from MultiTimeframeConfig import ConfigValidator

config = get_config()
is_valid, msg = ConfigValidator.validate(config)

if is_valid:
    print("✅ Configuração válida")
else:
    print(f"❌ Erro: {msg}")
```

---

## 📊 Decisões e Prioridades

### Estrutura Decision

```python
{
    "decision_id": "MTF_DECISION_1_1234567890",
    "timestamp": "2025-01-19T14:30:45.123456",
    "source": "MULTI_TIMEFRAME_TRADER",
    
    "action": "BUY",                    # BUY/SELL/HOLD
    "symbol": "BTC/USDT",
    
    "priority": "HIGH",                 # CRITICAL/HIGH/MEDIUM/LOW
    "confidence": 0.85,                 # 0.0-1.0
    
    "analysis_type": "MULTI_TIMEFRAME_1m_3m_5m",
    "timeframes": {
        "1m": {"signal": "BUY", "confidence": 0.75, "rsi": 55},
        "3m": {"signal": "BUY", "confidence": 0.85, "rsi": 58},
        "5m": {"signal": "BUY", "confidence": 0.88, "rsi": 60},
    },
    
    "entry_price": 45000.00,
    "stop_loss": 44100.00,              # 2% abaixo suporte
    "take_profit": 46350.00,            # 3% acima resistência
    "risk_reward_ratio": 1.5,
    "risk_level": "MEDIUM",             # LOW/MEDIUM/HIGH
    
    "reasoning": "COMPRA - Confiança ALTA - Alinhamento 3/3 timeframes"
}
```

### Cálculo de Prioridade

```
1. Calcular confiança ajustada (penalty por risco)
   adjusted_conf = conf × PRIORITY_RISK_REDUCTION[risk_level]

2. Mapear para prioridade
   if 0.9 <= adjusted_conf <= 1.0: CRITICAL
   if 0.75 <= adjusted_conf < 0.9: HIGH
   if 0.5 <= adjusted_conf < 0.75: MEDIUM
   if 0 <= adjusted_conf < 0.5: LOW
```

---

## 📝 Exemplo de Saída

```
═══════════════════════════════════════════════════════════════
ANÁLISE MULTI-TIMEFRAME: BTC/USDT
═══════════════════════════════════════════════════════════════

🔷 1 MINUTO
  Sinal: BUY
  Tendência: UPTREND
  RSI: 55.3
  Confiança: 0.75 (75%)
  Padrão: Hammer
  
🔷 3 MINUTOS
  Sinal: BUY
  Tendência: UPTREND
  RSI: 58.1
  Confiança: 0.85 (85%)
  Padrão: Engulfing
  
🔷 5 MINUTOS
  Sinal: BUY
  Tendência: UPTREND
  RSI: 60.2
  Confiança: 0.88 (88%)
  Padrão: None

═══════════════════════════════════════════════════════════════

✅ DECISÃO FINAL: BUY

Confiança geral: 0.83 (83%)
Risco: LOW
Razão: Todos 3 timeframes em compra (alinhamento perfeito)

💰 EXECUÇÃO
  Entrada: $45,000.00
  Stop Loss: $44,100.00 (2% abaixo suporte)
  Take Profit: $46,350.00 (3% acima resistência)
  Risk/Reward: 1.5:1 ✓
  
═══════════════════════════════════════════════════════════════
```

---

## 🔗 Integração com DecisionEngine

### Flow de Integração

```python
# 1. Receber dados de mercado
from NeuralBus import NeuralBus
bus = NeuralBus.get_instance()
market_data = bus.get_service("MarketDataProvider").get_data()

# 2. Processar com multi-timeframe
from MultiTimeframeIntegration import IntegratedMultiTimeframeTrader
trader = IntegratedMultiTimeframeTrader()
mt_decision = trader.process_market_data(market_data)

# 3. Adaptar para Decision
from MultiTimeframeDecisionAdapter import MultiTimeframeDecisionAdapter
adapter = MultiTimeframeDecisionAdapter()
decision = adapter.adapt_to_decision(mt_decision.multi_timeframe_analysis)

# 4. Enviar para DecisionEngine
from DecisionEngine import DecisionEngine
engine = DecisionEngine()
engine.add_decision(decision)
result = engine.execute()

# 5. Executar trade
from AutoTrader import AutoTrader
trader = AutoTrader()
trade = trader.execute(result)
```

---

## 🐛 Troubleshooting

### Problema: "Dados insuficientes"

**Solução**: Assegurar 50+ candles por timeframe

### Problema: "ModuleNotFoundError: MultiTimeframeStrategy"

**Solução**: Copiar arquivos para `neural_layers/04_decisao/`

### Problema: Sinais muito indecisivos (muitos HOLD)

**Solução**: Usar configuração agressiva ou ajustar thresholds

### Problema: Sinais inconsistentes entre runs

**Solução**: Verificar sincronização de timestamps, garantir dados consistentes

---

## 📚 Referências Técnicas

### Fórmulas Principais

**Score Consolidado:**

```
score = (s₁ₘ × w₁ₘ + s₃ₘ × w₃ₘ + s₅ₘ × w₅ₘ) / (w₁ₘ + w₃ₘ + w₅ₘ)
score ∈ [-5, +5]
```

**Confiança:**

```
conf = (|score| + 5) / 10
conf ∈ [0, 1]
```

**Risk/Reward:**

```
R:R = |TP - Entry| / |SL - Entry|
```

---

## 🎓 Best Practices

1. **Sempre validar dados**: Verificar volume, spread, consistência
2. **Usar Stop Loss**: Nunca negociar sem proteção
3. **Respeitar Risk/Reward**: Mínimo 1.5:1
4. **Monitorar Risk Level**: Evitar HIGH risk trades
5. **Backtest**: Validar performance histórica
6. **Diversificar**: Múltiplos símbolos, não tudo em um
7. **Manter logs**: Rastrear todas as operações
8. **Revisar periodicamente**: Ajustar configuração conforme dados

---

## 📞 Suporte

Para problemas ou dúvidas:

1. Verificar logs em `lextrader.log`
2. Executar validação: `check_setup.py`
3. Testar com dados de teste: `MultiTimeframeStrategy.py` main
4. Consultar `neural_layers/README.md`

---

**Versão:** 1.0  
**Data:** 2025-01-19  
**Status:** ✅ Production Ready
