package modulo_emocional.Decisao;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ⚙️ CONFIGURAÇÃO MULTI-TIMEFRAME
 * ================================
 * 
 * Arquivo de configuração centralizado para estratégia multi-timeframe
 * Define padrões, limites e ajustes finos
 * 
 * Uso:
 *     MTFConfig config = MTFConfig.getConfig();
 *     System.out.println(config.getTimeframes());
 */

// ============================================================
// ENUMS
// ============================================================

enum TimeframeType {
    ONE_MINUTE("1m"),
    THREE_MINUTES("3m"),
    FIVE_MINUTES("5m");
    
    private final String value;
    
    TimeframeType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}

enum RiskLevel {
    LOW,
    MEDIUM,
    HIGH
}

enum DecisionPriority {
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW
}

enum SignalType {
    STRONG_BUY,
    BUY,
    NEUTRAL,
    WEAK_SELL,
    SELL
}

// ============================================================
// VALIDATION RESULT
// ============================================================

class ValidationResult {
    private final boolean valid;
    private final String message;
    
    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
    
    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
    
    @Override
    public String toString() {
        return String.format("Valid: %b - %s", valid, message);
    }
}

// ============================================================
// MTF CONFIGURATION CLASS
// ============================================================

class MTFConfig {
    // ═══════════════════════════════════════════════════════════════════════════
    // TIMEFRAMES E PESOS
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Timeframes a analisar
    private List<String> timeframes;
    
    // Pesos para consolidação de sinais (5m mais importante)
    private Map<String, Double> timeframeWeights;
    
    // Mínimo de candles necessários por timeframe
    private int minCandlesPerTimeframe;
    
    // ═══════════════════════════════════════════════════════════════════════════
    // INDICADORES TÉCNICOS
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Períodos SMA
    private int smaFastPeriod;    // SMA rápida
    private int smaSlowPeriod;    // SMA lenta
    
    // RSI
    private int rsiPeriod;
    private double rsiOverbought;  // RSI > 70 = sobrecomprado
    private double rsiOversold;    // RSI < 30 = sobrevendido
    
    // MACD
    private int macdFastPeriod;
    private int macdSlowPeriod;
    private int macdSignalPeriod;
    
    // Support/Resistance
    private int supportResistanceLookback;  // Candles anteriores para análise
    
    // ═══════════════════════════════════════════════════════════════════════════
    // PADRÕES DE VELA
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Proporção para Doji (corpo < 10% do range total)
    private double dojiBodyRatio;
    
    // Proporção para Hammer (corpo no topo, pavio longo)
    private double hammerBodyTopRatio;
    private double hammerWickRatio;
    
    // Proporção para Shooting Star (corpo no fundo, pavio longo)
    private double shootingStarBodyBottomRatio;
    private double shootingStarWickRatio;
    
    // ═══════════════════════════════════════════════════════════════════════════
    // CONSOLIDAÇÃO E DECISÃO
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Limites de score para sinal
    private Map<String, Double> signalThresholds;
    
    // Conflito de sinais define risco
    private Map<Integer, String> conflictRiskLevels;
    
    // RSI extremo indica alto risco
    private double extremeRsiThreshold;  // RSI < 15 ou > 85 = risco alto
    
    // ═══════════════════════════════════════════════════════════════════════════
    // GERENCIAMENTO DE RISCO
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Stop Loss como percentual do preço
    private double stopLossPercentage;  // 2% abaixo do suporte
    
    // Take Profit como percentual do preço
    private double takeProfitPercentage;  // 3% acima da resistência
    
    // Razão risk/reward mínima aceitável
    private double minRiskRewardRatio;  // Lucro esperado >= 1.5x o risco
    
    // ═══════════════════════════════════════════════════════════════════════════
    // SCORING DE SINAIS
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Pesos dos fatores no score (-5 a +5)
    private Map<String, Double> scoreFactors;
    
    // ═══════════════════════════════════════════════════════════════════════════
    // CONFIANÇA
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Mapeamento de score para confiança (0.0 - 1.0)
    private String confidenceFormula;  // score -5 a +5 → 0.0 a 1.0
    
    // ═══════════════════════════════════════════════════════════════════════════
    // INTEGRAÇÃO DECISIONENGINE
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Mapeamento de confiança para prioridade
    private Map<String, DecisionPriority> priorityByConfidence;
    
    // Redução de prioridade por risco
    private Map<String, Double> priorityRiskReduction;
    
    // ═══════════════════════════════════════════════════════════════════════════
    // FILTROS E VALIDAÇÕES
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Validação de dados
    private double minVolume;  // Volume mínimo
    private double maxSpread;  // Spread máximo aceitável (1%)
    
    // ═══════════════════════════════════════════════════════════════════════════
    // LOGGING E MONITORAMENTO
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Nível de logging
    private String logLevel;
    
    // Monitorar com detalhe (DEBUG verboso)
    private boolean debugMode;
    
    // Armazenar histórico de decisões
    private boolean storeHistory;
    private int maxHistorySize;
    
    // ═══════════════════════════════════════════════════════════════════════════
    // OTIMIZAÇÃO E MACHINE LEARNING (FUTURO)
    // ═══════════════════════════════════════════════════════════════════════════
    
    // Habilitar otimização baseada em backtesting
    private boolean enableMlOptimization;
    
    // Período de aprendizado (em dias)
    private int mlLearningPeriod;
    
    // Taxa de adaptação (0.0 - 1.0, quanto mais alta, mais rápido adapta)
    private double mlAdaptationRate;
    
    // Construtor padrão
    public MTFConfig() {
        initializeDefaults();
    }
    
    private void initializeDefaults() {
        // Timeframes e pesos
        this.timeframes = new ArrayList<>(Arrays.asList("1m", "3m", "5m"));
        this.timeframeWeights = new HashMap<>();
        timeframeWeights.put("1m", 1.0);   // Entrada precisa
        timeframeWeights.put("3m", 1.5);   // Confirmação
        timeframeWeights.put("5m", 2.0);   // Tendência principal
        this.minCandlesPerTimeframe = 20;
        
        // Indicadores técnicos
        this.smaFastPeriod = 10;    // SMA rápida
        this.smaSlowPeriod = 20;    // SMA lenta
        this.rsiPeriod = 14;
        this.rsiOverbought = 70.0;  // RSI > 70 = sobrecomprado
        this.rsiOversold = 30.0;    // RSI < 30 = sobrevendido
        this.macdFastPeriod = 12;
        this.macdSlowPeriod = 26;
        this.macdSignalPeriod = 9;
        this.supportResistanceLookback = 5;  // Candles anteriores para análise
        
        // Padrões de vela
        this.dojiBodyRatio = 0.1;
        this.hammerBodyTopRatio = 0.75;
        this.hammerWickRatio = 2.0;
        this.shootingStarBodyBottomRatio = 0.75;
        this.shootingStarWickRatio = 2.0;
        
        // Consolidação e decisão
        this.signalThresholds = new HashMap<>();
        signalThresholds.put("STRONG_BUY", 2.0);      // score >= 2.0
        signalThresholds.put("BUY", 0.0);             // score >= 0.0
        signalThresholds.put("NEUTRAL", -1.5);        // score >= -1.5
        signalThresholds.put("WEAK_SELL", -2.5);      // score >= -2.5
        signalThresholds.put("SELL", -2.5);           // score < -2.5
        
        this.conflictRiskLevels = new HashMap<>();
        conflictRiskLevels.put(0, "LOW");      // Sem conflitos → risco baixo
        conflictRiskLevels.put(1, "LOW");      // 1 conflito → risco baixo
        conflictRiskLevels.put(2, "MEDIUM");   // 2 conflitos → risco médio
        conflictRiskLevels.put(3, "HIGH");     // 3+ conflitos → risco alto
        
        this.extremeRsiThreshold = 15.0;  // RSI < 15 ou > 85 = risco alto
        
        // Gerenciamento de risco
        this.stopLossPercentage = 0.02;  // 2% abaixo do suporte
        this.takeProfitPercentage = 0.03;  // 3% acima da resistência
        this.minRiskRewardRatio = 1.5;  // Lucro esperado >= 1.5x o risco
        
        // Scoring de sinais
        this.scoreFactors = new HashMap<>();
        scoreFactors.put("sma_alignment", 1.0);      // Alinhamento com SMAs
        scoreFactors.put("rsi_level", 1.0);          // Nível de RSI
        scoreFactors.put("macd_direction", 0.5);     // Direção do MACD
        scoreFactors.put("trend_direction", 1.0);    // Direção da tendência
        scoreFactors.put("candle_pattern", 0.5);     // Padrão de vela
        
        // Confiança
        this.confidenceFormula = "(abs(score) + 5) / 10";  // score -5 a +5 → 0.0 a 1.0
        
        // Integração DecisionEngine
        this.priorityByConfidence = new HashMap<>();
        priorityByConfidence.put("0.9-1.0", DecisionPriority.CRITICAL);
        priorityByConfidence.put("0.75-0.9", DecisionPriority.HIGH);
        priorityByConfidence.put("0.5-0.75", DecisionPriority.MEDIUM);
        priorityByConfidence.put("0.0-0.5", DecisionPriority.LOW);
        
        this.priorityRiskReduction = new HashMap<>();
        priorityRiskReduction.put("LOW", 1.0);      // Sem redução
        priorityRiskReduction.put("MEDIUM", 0.85);  // Reduz 15%
        priorityRiskReduction.put("HIGH", 0.7);     // Reduz 30%
        
        // Filtros e validações
        this.minVolume = 0.0;  // Volume mínimo
        this.maxSpread = 0.01;  // Spread máximo aceitável (1%)
        
        // Logging e monitoramento
        this.logLevel = "INFO";
        this.debugMode = false;
        this.storeHistory = true;
        this.maxHistorySize = 100;
        
        // Otimização e Machine Learning
        this.enableMlOptimization = false;
        this.mlLearningPeriod = 30;
        this.mlAdaptationRate = 0.1;
    }
    
    // ============================================================
    // GETTERS E SETTERS
    // ============================================================
    
    public List<String> getTimeframes() { return new ArrayList<>(timeframes); }
    public void setTimeframes(List<String> timeframes) { this.timeframes = new ArrayList<>(timeframes); }
    
    public Map<String, Double> getTimeframeWeights() { return new HashMap<>(timeframeWeights); }
    public void setTimeframeWeights(Map<String, Double> timeframeWeights) { this.timeframeWeights = new HashMap<>(timeframeWeights); }
    
    public int getMinCandlesPerTimeframe() { return minCandlesPerTimeframe; }
    public void setMinCandlesPerTimeframe(int minCandlesPerTimeframe) { this.minCandlesPerTimeframe = minCandlesPerTimeframe; }
    
    public int getSmaFastPeriod() { return smaFastPeriod; }
    public void setSmaFastPeriod(int smaFastPeriod) { this.smaFastPeriod = smaFastPeriod; }
    
    public int getSmaSlowPeriod() { return smaSlowPeriod; }
    public void setSmaSlowPeriod(int smaSlowPeriod) { this.smaSlowPeriod = smaSlowPeriod; }
    
    public int getRsiPeriod() { return rsiPeriod; }
    public void setRsiPeriod(int rsiPeriod) { this.rsiPeriod = rsiPeriod; }
    
    public double getRsiOverbought() { return rsiOverbought; }
    public void setRsiOverbought(double rsiOverbought) { this.rsiOverbought = rsiOverbought; }
    
    public double getRsiOversold() { return rsiOversold; }
    public void setRsiOversold(double rsiOversold) { this.rsiOversold = rsiOversold; }
    
    public int getMacdFastPeriod() { return macdFastPeriod; }
    public void setMacdFastPeriod(int macdFastPeriod) { this.macdFastPeriod = macdFastPeriod; }
    
    public int getMacdSlowPeriod() { return macdSlowPeriod; }
    public void setMacdSlowPeriod(int macdSlowPeriod) { this.macdSlowPeriod = macdSlowPeriod; }
    
    public int getMacdSignalPeriod() { return macdSignalPeriod; }
    public void setMacdSignalPeriod(int macdSignalPeriod) { this.macdSignalPeriod = macdSignalPeriod; }
    
    public int getSupportResistanceLookback() { return supportResistanceLookback; }
    public void setSupportResistanceLookback(int supportResistanceLookback) { this.supportResistanceLookback = supportResistanceLookback; }
    
    public double getDojiBodyRatio() { return dojiBodyRatio; }
    public void setDojiBodyRatio(double dojiBodyRatio) { this.dojiBodyRatio = dojiBodyRatio; }
    
    public double getHammerBodyTopRatio() { return hammerBodyTopRatio; }
    public void setHammerBodyTopRatio(double hammerBodyTopRatio) { this.hammerBodyTopRatio = hammerBodyTopRatio; }
    
    public double getHammerWickRatio() { return hammerWickRatio; }
    public void setHammerWickRatio(double hammerWickRatio) { this.hammerWickRatio = hammerWickRatio; }
    
    public double getShootingStarBodyBottomRatio() { return shootingStarBodyBottomRatio; }
    public void setShootingStarBodyBottomRatio(double shootingStarBodyBottomRatio) { this.shootingStarBodyBottomRatio = shootingStarBodyBottomRatio; }
    
    public double getShootingStarWickRatio() { return shootingStarWickRatio; }
    public void setShootingStarWickRatio(double shootingStarWickRatio) { this.shootingStarWickRatio = shootingStarWickRatio; }
    
    public Map<String, Double> getSignalThresholds() { return new HashMap<>(signalThresholds); }
    public void setSignalThresholds(Map<String, Double> signalThresholds) { this.signalThresholds = new HashMap<>(signalThresholds); }
    
    public Map<Integer, String> getConflictRiskLevels() { return new HashMap<>(conflictRiskLevels); }
    public void setConflictRiskLevels(Map<Integer, String> conflictRiskLevels) { this.conflictRiskLevels = new HashMap<>(conflictRiskLevels); }
    
    public double getExtremeRsiThreshold() { return extremeRsiThreshold; }
    public void setExtremeRsiThreshold(double extremeRsiThreshold) { this.extremeRsiThreshold = extremeRsiThreshold; }
    
    public double getStopLossPercentage() { return stopLossPercentage; }
    public void setStopLossPercentage(double stopLossPercentage) { this.stopLossPercentage = stopLossPercentage; }
    
    public double getTakeProfitPercentage() { return takeProfitPercentage; }
    public void setTakeProfitPercentage(double takeProfitPercentage) { this.takeProfitPercentage = takeProfitPercentage; }
    
    public double getMinRiskRewardRatio() { return minRiskRewardRatio; }
    public void setMinRiskRewardRatio(double minRiskRewardRatio) { this.minRiskRewardRatio = minRiskRewardRatio; }
    
    public Map<String, Double> getScoreFactors() { return new HashMap<>(scoreFactors); }
    public void setScoreFactors(Map<String, Double> scoreFactors) { this.scoreFactors = new HashMap<>(scoreFactors); }
    
    public String getConfidenceFormula() { return confidenceFormula; }
    public void setConfidenceFormula(String confidenceFormula) { this.confidenceFormula = confidenceFormula; }
    
    public Map<String, DecisionPriority> getPriorityByConfidence() { return new HashMap<>(priorityByConfidence); }
    public void setPriorityByConfidence(Map<String, DecisionPriority> priorityByConfidence) { this.priorityByConfidence = new HashMap<>(priorityByConfidence); }
    
    public Map<String, Double> getPriorityRiskReduction() { return new HashMap<>(priorityRiskReduction); }
    public void setPriorityRiskReduction(Map<String, Double> priorityRiskReduction) { this.priorityRiskReduction = new HashMap<>(priorityRiskReduction); }
    
    public double getMinVolume() { return minVolume; }
    public void setMinVolume(double minVolume) { this.minVolume = minVolume; }
    
    public double getMaxSpread() { return maxSpread; }
    public void setMaxSpread(double maxSpread) { this.maxSpread = maxSpread; }
    
    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    
    public boolean isDebugMode() { return debugMode; }
    public void setDebugMode(boolean debugMode) { this.debugMode = debugMode; }
    
    public boolean isStoreHistory() { return storeHistory; }
    public void setStoreHistory(boolean storeHistory) { this.storeHistory = storeHistory; }
    
    public int getMaxHistorySize() { return maxHistorySize; }
    public void setMaxHistorySize(int maxHistorySize) { this.maxHistorySize = maxHistorySize; }
    
    public boolean isEnableMlOptimization() { return enableMlOptimization; }
    public void setEnableMlOptimization(boolean enableMlOptimization) { this.enableMlOptimization = enableMlOptimization; }
    
    public int getMlLearningPeriod() { return mlLearningPeriod; }
    public void setMlLearningPeriod(int mlLearningPeriod) { this.mlLearningPeriod = mlLearningPeriod; }
    
    public double getMlAdaptationRate() { return mlAdaptationRate; }
    public void setMlAdaptationRate(double mlAdaptationRate) { this.mlAdaptationRate = mlAdaptationRate; }
    
    // ============================================================
    // GLOBAL CONFIG INSTANCE
    // ============================================================
    
    private static MTFConfig globalConfig = null;
    
    public static MTFConfig getConfig() {
        if (globalConfig == null) {
            globalConfig = new MTFConfig();
        }
        return globalConfig;
    }
    
    public static void resetConfig() {
        globalConfig = new MTFConfig();
    }
    
    public static MTFConfig loadCustomConfig(Map<String, Object> configDict) {
        globalConfig = new MTFConfig();
        // Aqui você poderia implementar lógica para carregar valores customizados
        // do mapa configDict para a configuração
        return globalConfig;
    }
    
    // ============================================================
    // UTILITY METHODS
    // ============================================================
    
    public double calculateConfidence(double score) {
        // Implementação da fórmula: (abs(score) + 5) / 10
        return (Math.abs(score) + 5.0) / 10.0;
    }
    
    public DecisionPriority getPriorityByConfidence(double confidence) {
        if (confidence >= 0.9) return DecisionPriority.CRITICAL;
        if (confidence >= 0.75) return DecisionPriority.HIGH;
        if (confidence >= 0.5) return DecisionPriority.MEDIUM;
        return DecisionPriority.LOW;
    }
    
    public double applyRiskReduction(double baseValue, String riskLevel) {
        Double reduction = priorityRiskReduction.get(riskLevel);
        if (reduction != null) {
            return baseValue * reduction;
        }
        return baseValue;
    }
    
    @Override
    public String toString() {
        return String.format("""
            MTFConfig {
                timeframes: %s,
                minCandlesPerTimeframe: %d,
                smaFastPeriod: %d, smaSlowPeriod: %d,
                rsiPeriod: %d, rsiOverbought: %.1f, rsiOversold: %.1f,
                stopLossPercentage: %.3f, takeProfitPercentage: %.3f,
                minRiskRewardRatio: %.2f
            }
            """, 
            timeframes, minCandlesPerTimeframe, 
            smaFastPeriod, smaSlowPeriod,
            rsiPeriod, rsiOverbought, rsiOversold,
            stopLossPercentage, takeProfitPercentage,
            minRiskRewardRatio
        );
    }
}

// ============================================================
// PRESET CONFIGURATIONS
// ============================================================

class PresetConfigs {
    /** Configurações pré-otimizadas para diferentes cenários */
    
    public static MTFConfig aggressive() {
        /** Configuração agressiva (mais trades, menor confiança mínima) */
        MTFConfig config = new MTFConfig();
        config.setMinRiskRewardRatio(1.0);  // Mais flexível
        config.setStopLossPercentage(0.03);  // 3% (mais tolerância)
        return config;
    }
    
    public static MTFConfig conservative() {
        /** Configuração conservadora (menos trades, maior confiança) */
        MTFConfig config = new MTFConfig();
        config.setMinRiskRewardRatio(2.0);  // Mais exigente
        config.setStopLossPercentage(0.01);  // 1% (menos tolerância)
        config.setExtremeRsiThreshold(10.0);  // Mais sensível a extremos
        return config;
    }
    
    public static MTFConfig scalping() {
        /** Configuração para scalping (1m e 3m apenas) */
        MTFConfig config = new MTFConfig();
        config.setTimeframes(new ArrayList<>(Arrays.asList("1m", "3m")));
        
        Map<String, Double> weights = new HashMap<>();
        weights.put("1m", 1.5);
        weights.put("3m", 1.0);
        config.setTimeframeWeights(weights);
        
        config.setTakeProfitPercentage(0.01);  // 1% (lucro rápido)
        return config;
    }
    
    public static MTFConfig swingTrading() {
        /** Configuração para swing trading (3m e 5m principalmente) */
        MTFConfig config = new MTFConfig();
        config.setTimeframes(new ArrayList<>(Arrays.asList("3m", "5m")));
        
        Map<String, Double> weights = new HashMap<>();
        weights.put("3m", 1.0);
        weights.put("5m", 2.0);
        config.setTimeframeWeights(weights);
        
        config.setTakeProfitPercentage(0.05);  // 5% (lucro maiores)
        return config;
    }
}

// ============================================================
// CONFIGURATION VALIDATOR
// ============================================================

class ConfigValidator {
    /** Valida configuração */
    
    public static ValidationResult validate(MTFConfig config) {
        /**
         * Valida configuração
         * 
         * Returns:
         *     ValidationResult com validação e mensagem de erro
         */
        
        // Verificar timeframes
        if (config.getTimeframes().isEmpty()) {
            return new ValidationResult(false, "TIMEFRAMES não pode estar vazio");
        }
        
        if (config.getTimeframes().size() != config.getTimeframeWeights().size()) {
            return new ValidationResult(false, "Número de TIMEFRAMES não corresponde a TIMEFRAME_WEIGHTS");
        }
        
        // Verificar pesos
        for (Double weight : config.getTimeframeWeights().values()) {
            if (weight <= 0) {
                return new ValidationResult(false, "Todos os pesos devem ser positivos");
            }
        }
        
        // Verificar períodos
        if (config.getSmaFastPeriod() >= config.getSmaSlowPeriod()) {
            return new ValidationResult(false, "SMA_FAST_PERIOD deve ser menor que SMA_SLOW_PERIOD");
        }
        
        if (config.getRsiPeriod() < 1) {
            return new ValidationResult(false, "RSI_PERIOD deve ser >= 1");
        }
        
        // Verificar limiares RSI
        if (!(0 < config.getRsiOversold() && config.getRsiOversold() < config.getRsiOverbought() && config.getRsiOverbought() < 100)) {
            return new ValidationResult(false, "RSI_OVERSOLD deve estar entre 0 e RSI_OVERBOUGHT, que deve estar entre RSI_OVERSOLD e 100");
        }
        
        // Verificar percentuais de stop/profit
        if (!(0 < config.getStopLossPercentage() && config.getStopLossPercentage() < 1)) {
            return new ValidationResult(false, "STOP_LOSS_PERCENTAGE deve estar entre 0 e 1");
        }
        
        if (!(0 < config.getTakeProfitPercentage() && config.getTakeProfitPercentage() < 1)) {
            return new ValidationResult(false, "TAKE_PROFIT_PERCENTAGE deve estar entre 0 e 1");
        }
        
        // Verificar min candles
        if (config.getMinCandlesPerTimeframe() < 2) {
            return new ValidationResult(false, "MIN_CANDLES_PER_TIMEFRAME deve ser >= 2");
        }
        
        return new ValidationResult(true, "Configuração válida");
    }
}

// ============================================================
// MAIN CLASS FOR DEMONSTRATION
// ============================================================

public class MultiTimeframeConfig {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CONFIGURAÇÃO MULTI-TIMEFRAME - EXEMPLOS");
        System.out.println("=".repeat(70) + "\n");
        
        // Padrão
        System.out.println("📋 CONFIGURAÇÃO PADRÃO:");
        MTFConfig configDefault = MTFConfig.getConfig();
        ValidationResult validationDefault = ConfigValidator.validate(configDefault);
        System.out.println("   " + validationDefault + "\n");
        
        // Agressiva
        System.out.println("🔴 CONFIGURAÇÃO AGRESSIVA:");
        MTFConfig configAggressive = PresetConfigs.aggressive();
        ValidationResult validationAggressive = ConfigValidator.validate(configAggressive);
        System.out.println("   " + validationAggressive + "\n");
        
        // Conservadora
        System.out.println("🟢 CONFIGURAÇÃO CONSERVADORA:");
        MTFConfig configConservative = PresetConfigs.conservative();
        ValidationResult validationConservative = ConfigValidator.validate(configConservative);
        System.out.println("   " + validationConservative + "\n");
        
        // Scalping
        System.out.println("⚡ CONFIGURAÇÃO SCALPING:");
        MTFConfig configScalping = PresetConfigs.scalping();
        ValidationResult validationScalping = ConfigValidator.validate(configScalping);
        System.out.println("   " + validationScalping);
        System.out.println("   Timeframes: " + configScalping.getTimeframes() + "\n");
        
        // Swing Trading
        System.out.println("📈 CONFIGURAÇÃO SWING TRADING:");
        MTFConfig configSwing = PresetConfigs.swingTrading();
        ValidationResult validationSwing = ConfigValidator.validate(configSwing);
        System.out.println("   " + validationSwing);
        System.out.println("   Timeframes: " + configSwing.getTimeframes() + "\n");
        
        // Demonstrar cálculo de confiança
        System.out.println("🧮 DEMONSTRAÇÃO DE CÁLCULO:");
        MTFConfig demo = MTFConfig.getConfig();
        double[] testScores = {-3.0, -1.0, 0.0, 1.5, 3.0};
        
        for (double score : testScores) {
            double confidence = demo.calculateConfidence(score);
            DecisionPriority priority = demo.getPriorityByConfidence(confidence);
            System.out.printf("   Score: %.1f → Confiança: %.2f → Prioridade: %s%n", 
                score, confidence, priority);
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
    }
}
