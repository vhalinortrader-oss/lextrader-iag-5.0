package modulo_emocional.Decisao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * 🔄 ADAPTADOR DE DECISÃO MULTI-TIMEFRAME
 * ========================================
 * 
 * Converte MultiTimeframeSignal em Decision objects compatíveis com DecisionEngine
 * 
 * Uso:
 *     MultiTimeframeDecisionAdapter adapter = new MultiTimeframeDecisionAdapter();
 *     
 *     // Converter sinais multi-timeframe em decisões
 *     Decision decision = adapter.adaptToDecision(multiTimeframeSignal, "HIGH");
 */

// ============================================================
// ENUMS (se não existirem em outros arquivos)
// ============================================================

enum TradeSignal {
    STRONG_BUY,
    BUY,
    WEAK_BUY,
    NEUTRAL,
    WEAK_SELL,
    SELL,
    STRONG_SELL
}

enum Trend {
    UPTREND,
    DOWNTREND,
    SIDEWAYS
}

enum CandlePattern {
    DOJI,
    HAMMER,
    SHOOTING_STAR,
    ENGULFING,
    UNKNOWN
}

// ============================================================
// DATA CLASSES
// ============================================================

class TimeframeSignal {
    private TradeSignal signal;
    private Trend trend;
    private double confidence;
    private double rsi;
    private CandlePattern candlePattern;
    private Double sma10;
    private Double sma20;
    private Double macd;
    private Double support;
    private Double resistance;
    
    public TimeframeSignal(TradeSignal signal, Trend trend, double confidence, double rsi) {
        this.signal = signal;
        this.trend = trend;
        this.confidence = confidence;
        this.rsi = rsi;
        this.candlePattern = CandlePattern.UNKNOWN;
    }
    
    // Getters and setters
    public TradeSignal getSignal() { return signal; }
    public void setSignal(TradeSignal signal) { this.signal = signal; }
    
    public Trend getTrend() { return trend; }
    public void setTrend(Trend trend) { this.trend = trend; }
    
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    public double getRsi() { return rsi; }
    public void setRsi(double rsi) { this.rsi = rsi; }
    
    public CandlePattern getCandlePattern() { return candlePattern; }
    public void setCandlePattern(CandlePattern candlePattern) { this.candlePattern = candlePattern; }
    
    public Double getSma10() { return sma10; }
    public void setSma10(Double sma10) { this.sma10 = sma10; }
    
    public Double getSma20() { return sma20; }
    public void setSma20(Double sma20) { this.sma20 = sma20; }
    
    public Double getMacd() { return macd; }
    public void setMacd(Double macd) { this.macd = macd; }
    
    public Double getSupport() { return support; }
    public void setSupport(Double support) { this.support = support; }
    
    public Double getResistance() { return resistance; }
    public void setResistance(Double resistance) { this.resistance = resistance; }
}

class MultiTimeframeSignal {
    private String symbol;
    private TradeSignal finalSignal;
    private double overallConfidence;
    private String riskLevel;
    private double suggestedStopLoss;
    private double suggestedTakeProfit;
    private double suggestedRiskReward;
    private Double entryPrice;
    private String reason;
    
    private TimeframeSignal signal1m;
    private TimeframeSignal signal3m;
    private TimeframeSignal signal5m;
    
    public MultiTimeframeSignal(String symbol) {
        this.symbol = symbol;
        this.finalSignal = TradeSignal.NEUTRAL;
        this.overallConfidence = 0.5;
        this.riskLevel = "MEDIUM";
        this.suggestedStopLoss = 0.0;
        this.suggestedTakeProfit = 0.0;
        this.suggestedRiskReward = 1.0;
    }
    
    // Getters and setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public TradeSignal getFinalSignal() { return finalSignal; }
    public void setFinalSignal(TradeSignal finalSignal) { this.finalSignal = finalSignal; }
    
    public double getOverallConfidence() { return overallConfidence; }
    public void setOverallConfidence(double overallConfidence) { this.overallConfidence = overallConfidence; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public double getSuggestedStopLoss() { return suggestedStopLoss; }
    public void setSuggestedStopLoss(double suggestedStopLoss) { this.suggestedStopLoss = suggestedStopLoss; }
    
    public double getSuggestedTakeProfit() { return suggestedTakeProfit; }
    public void setSuggestedTakeProfit(double suggestedTakeProfit) { this.suggestedTakeProfit = suggestedTakeProfit; }
    
    public double getSuggestedRiskReward() { return suggestedRiskReward; }
    public void setSuggestedRiskReward(double suggestedRiskReward) { this.suggestedRiskReward = suggestedRiskReward; }
    
    public Double getEntryPrice() { return entryPrice; }
    public void setEntryPrice(Double entryPrice) { this.entryPrice = entryPrice; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public TimeframeSignal getSignal1m() { return signal1m; }
    public void setSignal1m(TimeframeSignal signal1m) { this.signal1m = signal1m; }
    
    public TimeframeSignal getSignal3m() { return signal3m; }
    public void setSignal3m(TimeframeSignal signal3m) { this.signal3m = signal3m; }
    
    public TimeframeSignal getSignal5m() { return signal5m; }
    public void setSignal5m(TimeframeSignal signal5m) { this.signal5m = signal5m; }
}

// ============================================================
// DECISION CLASS
// ============================================================

class Decision {
    /** Estrutura de decisão compatível com DecisionEngine */
    
    // Identificadores
    private String decisionId;
    private LocalDateTime timestamp;
    private String source;  // "MULTI_TIMEFRAME_TRADER"
    
    // Ação
    private String action;  // "BUY", "SELL", "HOLD"
    private String symbol;
    
    // Prioridade
    private String priority;  // "CRITICAL", "HIGH", "MEDIUM", "LOW"
    private double confidence;  // 0.0 - 1.0
    
    // Análise
    private String analysisType;  // "MULTI_TIMEFRAME_1m_3m_5m"
    private Map<String, Object> timeframes;  // Sinais por timeframe
    
    // Execução
    private double entryPrice;
    private double stopLoss;
    private double takeProfit;
    private double riskRewardRatio;
    private String riskLevel;  // "LOW", "MEDIUM", "HIGH"
    
    // Metadata
    private String reasoning;
    private Map<String, Object> technicalIndicators;
    private MultiTimeframeSignal rawSignal;
    
    public Decision(String decisionId, LocalDateTime timestamp, String source, String action, String symbol) {
        this.decisionId = decisionId;
        this.timestamp = timestamp;
        this.source = source;
        this.action = action;
        this.symbol = symbol;
        this.priority = "MEDIUM";
        this.confidence = 0.5;
        this.analysisType = "MULTI_TIMEFRAME_1m_3m_5m";
        this.timeframes = new HashMap<>();
        this.entryPrice = 0.0;
        this.stopLoss = 0.0;
        this.takeProfit = 0.0;
        this.riskRewardRatio = 1.0;
        this.riskLevel = "MEDIUM";
        this.reasoning = "";
        this.technicalIndicators = new HashMap<>();
    }
    
    public Map<String, Object> toDict() {
        /** Converte para dicionário */
        Map<String, Object> data = new HashMap<>();
        data.put("decisionId", decisionId);
        data.put("timestamp", timestamp.format(DateTimeFormatter.ISO_DATE_TIME));
        data.put("source", source);
        data.put("action", action);
        data.put("symbol", symbol);
        data.put("priority", priority);
        data.put("confidence", confidence);
        data.put("analysisType", analysisType);
        data.put("timeframes", timeframes);
        data.put("entryPrice", entryPrice);
        data.put("stopLoss", stopLoss);
        data.put("takeProfit", takeProfit);
        data.put("riskRewardRatio", riskRewardRatio);
        data.put("riskLevel", riskLevel);
        data.put("reasoning", reasoning);
        data.put("technicalIndicators", technicalIndicators);
        // Não serializar objeto bruto
        return data;
    }
    
    // Getters and setters
    public String getDecisionId() { return decisionId; }
    public void setDecisionId(String decisionId) { this.decisionId = decisionId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    
    public Map<String, Object> getTimeframes() { return timeframes; }
    public void setTimeframes(Map<String, Object> timeframes) { this.timeframes = timeframes; }
    
    public double getEntryPrice() { return entryPrice; }
    public void setEntryPrice(double entryPrice) { this.entryPrice = entryPrice; }
    
    public double getStopLoss() { return stopLoss; }
    public void setStopLoss(double stopLoss) { this.stopLoss = stopLoss; }
    
    public double getTakeProfit() { return takeProfit; }
    public void setTakeProfit(double takeProfit) { this.takeProfit = takeProfit; }
    
    public double getRiskRewardRatio() { return riskRewardRatio; }
    public void setRiskRewardRatio(double riskRewardRatio) { this.riskRewardRatio = riskRewardRatio; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    
    public Map<String, Object> getTechnicalIndicators() { return technicalIndicators; }
    public void setTechnicalIndicators(Map<String, Object> technicalIndicators) { this.technicalIndicators = technicalIndicators; }
    
    public MultiTimeframeSignal getRawSignal() { return rawSignal; }
    public void setRawSignal(MultiTimeframeSignal rawSignal) { this.rawSignal = rawSignal; }
}

// ============================================================
// MULTI-TIMEFRAME DECISION ADAPTER
// ============================================================

class MultiTimeframeDecisionAdapter {
    /** Adaptador de sinais multi-timeframe para decisões */
    
    private static final Logger logger = Logger.getLogger(MultiTimeframeDecisionAdapter.class.getName());
    
    // Mapeamento de prioridade baseado em confiança
    private static final Map<String, String> PRIORITY_MAP = new HashMap<>();
    static {
        PRIORITY_MAP.put("0.9-1.0", "CRITICAL");   // Muito alto
        PRIORITY_MAP.put("0.75-0.9", "HIGH");      // Alto
        PRIORITY_MAP.put("0.5-0.75", "MEDIUM");    // Médio
        PRIORITY_MAP.put("0.0-0.5", "LOW");        // Baixo
    }
    
    private String version;
    private int decisionCount;
    
    public MultiTimeframeDecisionAdapter(String version) {
        this.version = version;
        this.decisionCount = 0;
        logger.info("Adaptador de decisão multi-timeframe inicializado (v" + version + ")");
    }
    
    public MultiTimeframeDecisionAdapter() {
        this("1.0");
    }
    
    public Decision adaptToDecision(
        MultiTimeframeSignal mtSignal,
        String overridePriority,
        Map<String, Object> additionalContext
    ) {
        /**
         * Converte MultiTimeframeSignal em Decision
         * 
         * Args:
         *     mtSignal: MultiTimeframeSignal da estratégia
         *     overridePriority: Sobrescrever prioridade calculada
         *     additionalContext: Contexto adicional para reasoning
         * 
         * Returns:
         *     Decision objeto
         */
        
        decisionCount++;
        String decisionId = "MTF_DECISION_" + decisionCount + "_" + System.currentTimeMillis();
        
        // Mapear ação
        String action = mapAction(mtSignal.getFinalSignal());
        
        // Calcular prioridade
        String priority = overridePriority != null ? overridePriority : 
            calculatePriority(action, mtSignal.getOverallConfidence(), mtSignal.getRiskLevel());
        
        // Extrair indicadores técnicos
        Map<String, Object> technicalIndicators = extractTechnicalIndicators(mtSignal);
        
        // Gerar reasoning
        String reasoning = generateReasoning(mtSignal, additionalContext);
        
        // Extrair dados por timeframe
        Map<String, Object> timeframes = new HashMap<>();
        
        if (mtSignal.getSignal1m() != null) {
            Map<String, Object> tf1m = new HashMap<>();
            tf1m.put("signal", mtSignal.getSignal1m().getSignal().toString());
            tf1m.put("trend", mtSignal.getSignal1m().getTrend().toString());
            tf1m.put("confidence", mtSignal.getSignal1m().getConfidence());
            tf1m.put("rsi", mtSignal.getSignal1m().getRsi());
            tf1m.put("candlePattern", mtSignal.getSignal1m().getCandlePattern() != null ? 
                mtSignal.getSignal1m().getCandlePattern().toString() : "UNKNOWN");
            timeframes.put("1m", tf1m);
        }
        
        if (mtSignal.getSignal3m() != null) {
            Map<String, Object> tf3m = new HashMap<>();
            tf3m.put("signal", mtSignal.getSignal3m().getSignal().toString());
            tf3m.put("trend", mtSignal.getSignal3m().getTrend().toString());
            tf3m.put("confidence", mtSignal.getSignal3m().getConfidence());
            tf3m.put("rsi", mtSignal.getSignal3m().getRsi());
            tf3m.put("candlePattern", mtSignal.getSignal3m().getCandlePattern() != null ? 
                mtSignal.getSignal3m().getCandlePattern().toString() : "UNKNOWN");
            timeframes.put("3m", tf3m);
        }
        
        if (mtSignal.getSignal5m() != null) {
            Map<String, Object> tf5m = new HashMap<>();
            tf5m.put("signal", mtSignal.getSignal5m().getSignal().toString());
            tf5m.put("trend", mtSignal.getSignal5m().getTrend().toString());
            tf5m.put("confidence", mtSignal.getSignal5m().getConfidence());
            tf5m.put("rsi", mtSignal.getSignal5m().getRsi());
            tf5m.put("candlePattern", mtSignal.getSignal5m().getCandlePattern() != null ? 
                mtSignal.getSignal5m().getCandlePattern().toString() : "UNKNOWN");
            timeframes.put("5m", tf5m);
        }
        
        // Criar decisão
        Decision decision = new Decision(decisionId, LocalDateTime.now(), "MULTI_TIMEFRAME_TRADER", action, mtSignal.getSymbol());
        decision.setPriority(priority);
        decision.setConfidence(mtSignal.getOverallConfidence());
        decision.setAnalysisType("MULTI_TIMEFRAME_1m_3m_5m");
        decision.setTimeframes(timeframes);
        decision.setEntryPrice(mtSignal.getEntryPrice() != null ? mtSignal.getEntryPrice() : 0.0);
        decision.setStopLoss(mtSignal.getSuggestedStopLoss());
        decision.setTakeProfit(mtSignal.getSuggestedTakeProfit());
        decision.setRiskRewardRatio(mtSignal.getSuggestedRiskReward());
        decision.setRiskLevel(mtSignal.getRiskLevel());
        decision.setReasoning(reasoning);
        decision.setTechnicalIndicators(technicalIndicators);
        decision.setRawSignal(mtSignal);
        
        logger.fine("Decisão criada: " + decisionId + " - " + action + " " + mtSignal.getSymbol() + " (" + priority + ")");
        
        return decision;
    }
    
    public Decision adaptToDecision(MultiTimeframeSignal mtSignal, String overridePriority) {
        return adaptToDecision(mtSignal, overridePriority, null);
    }
    
    public Decision adaptToDecision(MultiTimeframeSignal mtSignal) {
        return adaptToDecision(mtSignal, null, null);
    }
    
    private String mapAction(TradeSignal signal) {
        /** Mapeia TradeSignal para ação */
        
        Map<TradeSignal, String> signalMapping = new HashMap<>();
        signalMapping.put(TradeSignal.STRONG_BUY, "BUY");
        signalMapping.put(TradeSignal.BUY, "BUY");
        signalMapping.put(TradeSignal.WEAK_BUY, "BUY");
        signalMapping.put(TradeSignal.NEUTRAL, "HOLD");
        signalMapping.put(TradeSignal.WEAK_SELL, "SELL");
        signalMapping.put(TradeSignal.SELL, "SELL");
        signalMapping.put(TradeSignal.STRONG_SELL, "SELL");
        
        return signalMapping.getOrDefault(signal, "HOLD");
    }
    
    private String calculatePriority(String action, double confidence, String riskLevel) {
        /** Calcula prioridade baseada em confiança e risco */
        
        // Ajustar confiança por risco
        double adjustedConfidence = confidence;
        
        if ("HIGH".equals(riskLevel)) {
            adjustedConfidence *= 0.7;  // Reduzir prioridade em alto risco
        } else if ("MEDIUM".equals(riskLevel)) {
            adjustedConfidence *= 0.85;
        }
        // LOW não ajusta
        
        // Mapear para prioridade
        if (adjustedConfidence >= 0.9) {
            return "CRITICAL";
        } else if (adjustedConfidence >= 0.75) {
            return "HIGH";
        } else if (adjustedConfidence >= 0.5) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private Map<String, Object> extractTechnicalIndicators(MultiTimeframeSignal mtSignal) {
        /** Extrai indicadores técnicos dos sinais */
        
        Map<String, Object> indicators = new HashMap<>();
        
        // SMA 10
        Map<String, Object> sma10 = new HashMap<>();
        if (mtSignal.getSignal1m() != null) {
            sma10.put("1m", mtSignal.getSignal1m().getSma10());
        }
        if (mtSignal.getSignal3m() != null) {
            sma10.put("3m", mtSignal.getSignal3m().getSma10());
        }
        if (mtSignal.getSignal5m() != null) {
            sma10.put("5m", mtSignal.getSignal5m().getSma10());
        }
        indicators.put("sma_10", sma10);
        
        // SMA 20
        Map<String, Object> sma20 = new HashMap<>();
        if (mtSignal.getSignal1m() != null) {
            sma20.put("1m", mtSignal.getSignal1m().getSma20());
        }
        if (mtSignal.getSignal3m() != null) {
            sma20.put("3m", mtSignal.getSignal3m().getSma20());
        }
        if (mtSignal.getSignal5m() != null) {
            sma20.put("5m", mtSignal.getSignal5m().getSma20());
        }
        indicators.put("sma_20", sma20);
        
        // RSI
        Map<String, Object> rsi = new HashMap<>();
        if (mtSignal.getSignal1m() != null) {
            rsi.put("1m", mtSignal.getSignal1m().getRsi());
        }
        if (mtSignal.getSignal3m() != null) {
            rsi.put("3m", mtSignal.getSignal3m().getRsi());
        }
        if (mtSignal.getSignal5m() != null) {
            rsi.put("5m", mtSignal.getSignal5m().getRsi());
        }
        indicators.put("rsi", rsi);
        
        // MACD
        Map<String, Object> macd = new HashMap<>();
        if (mtSignal.getSignal1m() != null) {
            macd.put("1m", mtSignal.getSignal1m().getMacd());
        }
        if (mtSignal.getSignal3m() != null) {
            macd.put("3m", mtSignal.getSignal3m().getMacd());
        }
        if (mtSignal.getSignal5m() != null) {
            macd.put("5m", mtSignal.getSignal5m().getMacd());
        }
        indicators.put("macd", macd);
        
        // Support
        Map<String, Object> support = new HashMap<>();
        if (mtSignal.getSignal1m() != null) {
            support.put("1m", mtSignal.getSignal1m().getSupport());
        }
        if (mtSignal.getSignal3m() != null) {
            support.put("3m", mtSignal.getSignal3m().getSupport());
        }
        if (mtSignal.getSignal5m() != null) {
            support.put("5m", mtSignal.getSignal5m().getSupport());
        }
        indicators.put("support", support);
        
        // Resistance
        Map<String, Object> resistance = new HashMap<>();
        if (mtSignal.getSignal1m() != null) {
            resistance.put("1m", mtSignal.getSignal1m().getResistance());
        }
        if (mtSignal.getSignal3m() != null) {
            resistance.put("3m", mtSignal.getSignal3m().getResistance());
        }
        if (mtSignal.getSignal5m() != null) {
            resistance.put("5m", mtSignal.getSignal5m().getResistance());
        }
        indicators.put("resistance", resistance);
        
        return indicators;
    }
    
    private String generateReasoning(MultiTimeframeSignal mtSignal, Map<String, Object> additionalContext) {
        /** Gera raciocínio textual para a decisão */
        
        List<String> parts = new ArrayList<>();
        
        // Ação principal
        Map<TradeSignal, String> actionMap = new HashMap<>();
        actionMap.put(TradeSignal.STRONG_BUY, "🟢 COMPRA FORTE");
        actionMap.put(TradeSignal.BUY, "🟢 COMPRA");
        actionMap.put(TradeSignal.WEAK_BUY, "🟡 COMPRA FRACA");
        actionMap.put(TradeSignal.NEUTRAL, "⚪ NEUTRO");
        actionMap.put(TradeSignal.WEAK_SELL, "🟡 VENDA FRACA");
        actionMap.put(TradeSignal.SELL, "🔴 VENDA");
        actionMap.put(TradeSignal.STRONG_SELL, "🔴 VENDA FORTE");
        
        parts.add(actionMap.getOrDefault(mtSignal.getFinalSignal(), "❓ DESCONHECIDO"));
        
        // Análise de confiança
        if (mtSignal.getOverallConfidence() >= 0.9) {
            parts.add("- Confiança MUITO ALTA (>90%)");
        } else if (mtSignal.getOverallConfidence() >= 0.75) {
            parts.add("- Confiança ALTA (>75%)");
        } else if (mtSignal.getOverallConfidence() >= 0.5) {
            parts.add("- Confiança MÉDIA (>50%)");
        } else {
            parts.add("- Confiança BAIXA (<50%)");
        }
        
        // Análise de risco
        Map<String, String> riskMap = new HashMap<>();
        riskMap.put("LOW", "🟢 RISCO BAIXO");
        riskMap.put("MEDIUM", "🟡 RISCO MÉDIO");
        riskMap.put("HIGH", "🔴 RISCO ALTO");
        parts.add("- " + riskMap.getOrDefault(mtSignal.getRiskLevel(), "❓ RISCO DESCONHECIDO"));
        
        // Alinhamento de timeframes
        int alignCount = 0;
        if (mtSignal.getSignal1m() != null && 
            (mtSignal.getSignal1m().getSignal() == TradeSignal.BUY || 
             mtSignal.getSignal1m().getSignal() == TradeSignal.STRONG_BUY)) {
            alignCount++;
        }
        if (mtSignal.getSignal3m() != null && 
            (mtSignal.getSignal3m().getSignal() == TradeSignal.BUY || 
             mtSignal.getSignal3m().getSignal() == TradeSignal.STRONG_BUY)) {
            alignCount++;
        }
        if (mtSignal.getSignal5m() != null && 
            (mtSignal.getSignal5m().getSignal() == TradeSignal.BUY || 
             mtSignal.getSignal5m().getSignal() == TradeSignal.STRONG_BUY)) {
            alignCount++;
        }
        
        parts.add("- Alinhamento: " + alignCount + "/3 timeframes em compra");
        
        // Tendência
        Map<Trend, String> trendMap = new HashMap<>();
        trendMap.put(Trend.UPTREND, "📈 TENDÊNCIA ALTA");
        trendMap.put(Trend.DOWNTREND, "📉 TENDÊNCIA BAIXA");
        trendMap.put(Trend.SIDEWAYS, "➡️  LATERALIZADO");
        
        if (mtSignal.getSignal5m() != null) {
            parts.add("- Tendência 5m: " + trendMap.getOrDefault(mtSignal.getSignal5m().getTrend(), "DESCONHECIDA"));
        }
        
        // Risk/Reward
        parts.add("- Risk/Reward: " + String.format("%.2f", mtSignal.getSuggestedRiskReward()) + ":1");
        
        // Contexto adicional
        if (additionalContext != null) {
            for (Map.Entry<String, Object> entry : additionalContext.entrySet()) {
                parts.add("- " + entry.getKey() + ": " + entry.getValue());
            }
        }
        
        // Motivo original (se disponível)
        if (mtSignal.getReason() != null && !mtSignal.getReason().isEmpty()) {
            parts.add("\nMotivo detalhado: " + mtSignal.getReason());
        }
        
        return String.join("\n", parts);
    }
    
    public List<Decision> adaptMultipleSignals(
        List<MultiTimeframeSignal> signals,
        String overridePriority
    ) {
        /** Converte múltiplos sinais em decisões */
        
        List<Decision> decisions = new ArrayList<>();
        for (MultiTimeframeSignal signal : signals) {
            decisions.add(adaptToDecision(signal, overridePriority));
        }
        return decisions;
    }
    
    public List<Decision> adaptMultipleSignals(List<MultiTimeframeSignal> signals) {
        return adaptMultipleSignals(signals, null);
    }
    
    // Getters
    public String getVersion() { return version; }
    public int getDecisionCount() { return decisionCount; }
}

// ============================================================
// MAIN CLASS FOR DEMONSTRATION
// ============================================================

public class MultiTimeframeDecisionAdapter {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ADAPTADOR MULTI-TIMEFRAME PARA DECISIONENGINE");
        System.out.println("=".repeat(70) + "\n");
        
        // Criar adaptador
        MultiTimeframeDecisionAdapter adapter = new MultiTimeframeDecisionAdapter();
        
        // Criar dados de teste
        MultiTimeframeSignal btcSignal = createTestSignal("BTC/USDT", 45000.0);
        MultiTimeframeSignal ethSignal = createTestSignal("ETH/USDT", 2500.0);
        
        System.out.println("Adaptando sinais para Decision objects...\n");
        
        // Adaptar decisões
        List<Decision> decisions = adapter.adaptMultipleSignals(
            Arrays.asList(btcSignal, ethSignal)
        );
        
        for (Decision decision : decisions) {
            System.out.println("─".repeat(70));
            System.out.println("🔷 Decision ID: " + decision.getDecisionId());
            System.out.println("   Ação: " + decision.getAction() + " " + decision.getSymbol());
            System.out.println("   Prioridade: " + decision.getPriority());
            System.out.println("   Confiança: " + String.format("%.0f%%", decision.getConfidence() * 100));
            System.out.println("   Risco: " + decision.getRiskLevel());
            
            System.out.println("\n📊 Preços:");
            System.out.println("   Entrada: $" + String.format("%.2f", decision.getEntryPrice()));
            System.out.println("   Stop Loss: $" + String.format("%.2f", decision.getStopLoss()));
            System.out.println("   Take Profit: $" + String.format("%.2f", decision.getTakeProfit()));
            System.out.println("   Risk/Reward: " + String.format("%.2f", decision.getRiskRewardRatio()) + ":1");
            
            System.out.println("\n📝 Raciocínio:");
            for (String line : decision.getReasoning().split("\n")) {
                System.out.println("   " + line);
            }
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("JSON para DecisionEngine:");
        System.out.println("=".repeat(70) + "\n");
        
        if (!decisions.isEmpty()) {
            System.out.println(mapToJson(decisions.get(0).toDict()));
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
    }
    
    private static MultiTimeframeSignal createTestSignal(String symbol, double basePrice) {
        MultiTimeframeSignal signal = new MultiTimeframeSignal(symbol);
        
        // Criar sinais de teste para cada timeframe
        TimeframeSignal signal1m = new TimeframeSignal(TradeSignal.BUY, Trend.UPTREND, 0.8, 65.0);
        signal1m.setSma10(basePrice * 0.99);
        signal1m.setSma20(basePrice * 0.98);
        signal1m.setMacd(0.5);
        signal1m.setSupport(basePrice * 0.97);
        signal1m.setResistance(basePrice * 1.03);
        signal1m.setCandlePattern(CandlePattern.HAMMER);
        
        TimeframeSignal signal3m = new TimeframeSignal(TradeSignal.BUY, Trend.UPTREND, 0.75, 62.0);
        signal3m.setSma10(basePrice * 0.995);
        signal3m.setSma20(basePrice * 0.985);
        signal3m.setMacd(0.3);
        signal3m.setSupport(basePrice * 0.975);
        signal3m.setResistance(basePrice * 1.025);
        
        TimeframeSignal signal5m = new TimeframeSignal(TradeSignal.WEAK_BUY, Trend.SIDEWAYS, 0.6, 58.0);
        signal5m.setSma10(basePrice * 1.0);
        signal5m.setSma20(basePrice * 0.99);
        signal5m.setMacd(0.1);
        signal5m.setSupport(basePrice * 0.98);
        signal5m.setResistance(basePrice * 1.02);
        
        signal.setSignal1m(signal1m);
        signal.setSignal3m(signal3m);
        signal.setSignal5m(signal5m);
        
        signal.setFinalSignal(TradeSignal.BUY);
        signal.setOverallConfidence(0.78);
        signal.setRiskLevel("MEDIUM");
        signal.setEntryPrice(basePrice);
        signal.setSuggestedStopLoss(basePrice * 0.98);
        signal.setSuggestedTakeProfit(basePrice * 1.03);
        signal.setSuggestedRiskReward(1.5);
        signal.setReason("Alinhamento positivo em timeframes curtos com RSI favorável");
        
        return signal;
    }
    
    private static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",\n");
            }
            first = false;
            
            json.append("  \"").append(entry.getKey()).append("\": ");
            
            Object value = entry.getValue();
            if (value instanceof Map) {
                json.append(mapToJson((Map<String, Object>) value));
            } else if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else {
                json.append(value);
            }
        }
        
        json.append("\n}");
        return json.toString();
    }
}
