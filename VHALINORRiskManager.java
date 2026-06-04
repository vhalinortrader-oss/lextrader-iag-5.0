// VHALINORRiskManager.java
package vhalinor.risk;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// ============================================================================
// ENUMS E TIPOS
// ============================================================================

enum RiskLevel {
    ULTRA_CONSERVATIVE("ULTRA_CONSERVATIVE"),
    CONSERVATIVE("CONSERVATIVE"),
    MODERATE("MODERATE"),
    AGGRESSIVE("AGGRESSIVE"),
    ULTRA_AGGRESSIVE("ULTRA_AGGRESSIVE");
    
    private final String value;
    
    RiskLevel(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}

enum RiskAlert {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL"),
    EMERGENCY("EMERGENCY");
    
    private final String value;
    
    RiskAlert(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}

// ============================================================================
// CLASSES DE DADOS
// ============================================================================

class RiskMetrics {
    private double var95;
    private double var99;
    private double expectedShortfall;
    private double sharpeRatio;
    private double sortinoRatio;
    private double maxDrawdown;
    private double volatility;
    private double beta;
    private double alpha;
    private double informationRatio;
    private double calmarRatio;
    private double omegaRatio;
    
    public RiskMetrics() {
        this.var95 = 0.0;
        this.var99 = 0.0;
        this.expectedShortfall = 0.0;
        this.sharpeRatio = 0.0;
        this.sortinoRatio = 0.0;
        this.maxDrawdown = 0.0;
        this.volatility = 0.0;
        this.beta = 0.0;
        this.alpha = 0.0;
        this.informationRatio = 0.0;
        this.calmarRatio = 0.0;
        this.omegaRatio = 0.0;
    }
    
    // Getters and Setters
    public double getVar95() { return var95; }
    public void setVar95(double var95) { this.var95 = var95; }
    
    public double getVar99() { return var99; }
    public void setVar99(double var99) { this.var99 = var99; }
    
    public double getExpectedShortfall() { return expectedShortfall; }
    public void setExpectedShortfall(double expectedShortfall) { this.expectedShortfall = expectedShortfall; }
    
    public double getSharpeRatio() { return sharpeRatio; }
    public void setSharpeRatio(double sharpeRatio) { this.sharpeRatio = sharpeRatio; }
    
    public double getSortinoRatio() { return sortinoRatio; }
    public void setSortinoRatio(double sortinoRatio) { this.sortinoRatio = sortinoRatio; }
    
    public double getMaxDrawdown() { return maxDrawdown; }
    public void setMaxDrawdown(double maxDrawdown) { this.maxDrawdown = maxDrawdown; }
    
    public double getVolatility() { return volatility; }
    public void setVolatility(double volatility) { this.volatility = volatility; }
    
    public double getBeta() { return beta; }
    public void setBeta(double beta) { this.beta = beta; }
    
    public double getAlpha() { return alpha; }
    public void setAlpha(double alpha) { this.alpha = alpha; }
    
    public double getInformationRatio() { return informationRatio; }
    public void setInformationRatio(double informationRatio) { this.informationRatio = informationRatio; }
    
    public double getCalmarRatio() { return calmarRatio; }
    public void setCalmarRatio(double calmarRatio) { this.calmarRatio = calmarRatio; }
    
    public double getOmegaRatio() { return omegaRatio; }
    public void setOmegaRatio(double omegaRatio) { this.omegaRatio = omegaRatio; }
}

class PositionRisk {
    private String symbol;
    private double positionSize;
    private double entryPrice;
    private double currentPrice;
    private double stopLoss;
    private double takeProfit;
    private double riskAmount;
    private double riskPercentage;
    private double unrealizedPnl;
    private double riskScore;
    private double confidenceLevel;
    
    public PositionRisk(String symbol, double positionSize, double entryPrice,
                       double currentPrice, double stopLoss, double takeProfit,
                       double riskAmount, double riskPercentage, double unrealizedPnl,
                       double riskScore, double confidenceLevel) {
        this.symbol = symbol;
        this.positionSize = positionSize;
        this.entryPrice = entryPrice;
        this.currentPrice = currentPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.riskAmount = riskAmount;
        this.riskPercentage = riskPercentage;
        this.unrealizedPnl = unrealizedPnl;
        this.riskScore = riskScore;
        this.confidenceLevel = confidenceLevel;
    }
    
    // Getters and Setters
    public String getSymbol() { return symbol; }
    public double getPositionSize() { return positionSize; }
    public double getEntryPrice() { return entryPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    public double getStopLoss() { return stopLoss; }
    public double getTakeProfit() { return takeProfit; }
    public double getRiskAmount() { return riskAmount; }
    public double getRiskPercentage() { return riskPercentage; }
    public double getUnrealizedPnl() { return unrealizedPnl; }
    public void setUnrealizedPnl(double unrealizedPnl) { this.unrealizedPnl = unrealizedPnl; }
    public double getRiskScore() { return riskScore; }
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    public double getConfidenceLevel() { return confidenceLevel; }
}

class TradeRecord {
    private String symbol;
    private double entryPrice;
    private double exitPrice;
    private double positionSize;
    private double pnl;
    private double riskAmount;
    private double returnPercentage;
    private LocalDateTime exitTime;
    private String reason;
    private double riskScore;
    
    public TradeRecord(String symbol, double entryPrice, double exitPrice,
                      double positionSize, double pnl, double riskAmount,
                      double returnPercentage, LocalDateTime exitTime,
                      String reason, double riskScore) {
        this.symbol = symbol;
        this.entryPrice = entryPrice;
        this.exitPrice = exitPrice;
        this.positionSize = positionSize;
        this.pnl = pnl;
        this.riskAmount = riskAmount;
        this.returnPercentage = returnPercentage;
        this.exitTime = exitTime;
        this.reason = reason;
        this.riskScore = riskScore;
    }
    
    // Getters
    public String getSymbol() { return symbol; }
    public double getEntryPrice() { return entryPrice; }
    public double getExitPrice() { return exitPrice; }
    public double getPositionSize() { return positionSize; }
    public double getPnl() { return pnl; }
    public double getRiskAmount() { return riskAmount; }
    public double getReturnPercentage() { return returnPercentage; }
    public LocalDateTime getExitTime() { return exitTime; }
    public String getReason() { return reason; }
    public double getRiskScore() { return riskScore; }
}

class Alert {
    private LocalDateTime timestamp;
    private String level;
    private String title;
    private String message;
    private boolean acknowledged;
    
    public Alert(RiskAlert level, String title, String message) {
        this.timestamp = LocalDateTime.now();
        this.level = level.getValue();
        this.title = title;
        this.message = message;
        this.acknowledged = false;
    }
    
    // Getters and Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLevel() { return level; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
}

// ============================================================================
// LOGGER SIMPLIFICADO
// ============================================================================

class RiskLogger {
    private String name;
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public RiskLogger(String name) {
        this.name = name;
    }
    
    public void info(String message) {
        log("INFO", message);
    }
    
    public void warning(String message) {
        log("WARNING", message);
    }
    
    public void error(String message) {
        log("ERROR", message);
    }
    
    public void error(String message, Exception e) {
        log("ERROR", message + ": " + e.getMessage());
        e.printStackTrace();
    }
    
    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        System.out.printf("%s - %s - %s - %s%n", timestamp, name, level, message);
    }
}

// ============================================================================
// VHALINOR RISK MANAGER PRINCIPAL
// ============================================================================

public class VHALINORRiskManager {
    private static final RiskLogger logger = new RiskLogger("VHALINOR_Risk");
    
    private double initialCapital;
    private double currentCapital;
    private double availableCapital;
    
    private Map<String, Object> config;
    private Map<String, PositionRisk> positions;
    private List<TradeRecord> tradeHistory;
    private RiskMetrics riskMetrics;
    private List<Alert> alerts;
    
    private ExecutorService executor;
    private Random random = new Random();
    
    /**
     * Inicializa o gerenciador de risco
     */
    public VHALINORRiskManager() {
        this(100000.0, null);
    }
    
    public VHALINORRiskManager(double initialCapital) {
        this(initialCapital, null);
    }
    
    public VHALINORRiskManager(double initialCapital, Map<String, Object> config) {
        this.initialCapital = initialCapital;
        this.currentCapital = initialCapital;
        this.availableCapital = initialCapital;
        
        this.config = config != null ? config : defaultConfig();
        this.positions = new ConcurrentHashMap<>();
        this.tradeHistory = new ArrayList<>();
        this.riskMetrics = new RiskMetrics();
        this.alerts = new ArrayList<>();
        
        this.executor = Executors.newFixedThreadPool(4);
        
        logger.info("VHALINOR Risk Manager inicializado");
        logger.info(String.format("Capital inicial: $%,.2f", initialCapital));
    }
    
    /**
     * Configurações padrão do sistema
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> defaultConfig() {
        Map<String, Object> config = new HashMap<>();
        
        config.put("max_risk_per_trade", 0.02);  // 2% por trade
        config.put("max_daily_risk", 0.05);      // 5% por dia
        config.put("max_portfolio_risk", 0.15);  // 15% do portfólio
        config.put("max_positions", 10);         // Máximo de posições
        config.put("max_correlation", 0.7);       // Correlação máxima
        config.put("stop_loss_multiplier", 2.0);  // Multiplicador ATR
        config.put("take_profit_ratio", 2.0);     // Risk/Reward ratio
        config.put("risk_level", RiskLevel.MODERATE);
        config.put("enable_dynamic_sizing", true);
        config.put("enable_correlation_check", true);
        config.put("enable_stress_testing", true);
        
        Map<String, Double> alertThresholds = new HashMap<>();
        alertThresholds.put("drawdown", 0.10);
        alertThresholds.put("daily_loss", 0.03);
        alertThresholds.put("position_risk", 0.05);
        config.put("alert_thresholds", alertThresholds);
        
        return config;
    }
    
    // ========================================================================
    // ANÁLISE DE RISCO DE POSIÇÃO
    // ========================================================================
    
    /**
     * Calcula tamanho ideal da posição usando Kelly Criterion modificado
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> calculatePositionSize(String symbol, double entryPrice,
                                                     double stopLoss, double confidence) {
        return calculatePositionSize(symbol, entryPrice, stopLoss, confidence, null);
    }
    
    public Map<String, Object> calculatePositionSize(String symbol, double entryPrice,
                                                     double stopLoss, double confidence,
                                                     Double customRisk) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Risco por trade
            double riskPerTrade = customRisk != null ? customRisk :
                (double) config.get("max_risk_per_trade");
            
            // Calcula risco por unidade
            double riskPerUnit = Math.abs(entryPrice - stopLoss);
            if (Math.abs(riskPerUnit) < 1e-10) {
                result.put("error", "Stop loss igual ao preço de entrada");
                return result;
            }
            
            // Capital arriscado
            double riskCapital = availableCapital * riskPerTrade;
            
            // Ajuste por confiança (Kelly Criterion modificado)
            double kellyMultiplier = calculateKellyMultiplier(confidence);
            double adjustedRisk = riskCapital * kellyMultiplier;
            
            // Tamanho da posição
            double positionSize = adjustedRisk / riskPerUnit;
            
            // Verifica limites
            double maxPositionValue = availableCapital * 0.3; // 30% máximo
            double positionValue = positionSize * entryPrice;
            
            if (positionValue > maxPositionValue) {
                positionSize = maxPositionValue / entryPrice;
                adjustedRisk = positionSize * riskPerUnit;
            }
            
            // Calcula take profit
            double takeProfit = calculateTakeProfit(entryPrice, stopLoss,
                (double) config.get("take_profit_ratio"));
            
            // Métricas da posição
            double riskPercentage = (adjustedRisk / availableCapital) * 100;
            double rewardPotential = Math.abs(takeProfit - entryPrice) * positionSize;
            double riskRewardRatio = adjustedRisk > 0 ? rewardPotential / adjustedRisk : 0;
            
            boolean approved = validatePositionRisk(riskPercentage, symbol);
            
            result.put("symbol", symbol);
            result.put("position_size", round(positionSize, 6));
            result.put("position_value", round(positionValue, 2));
            result.put("risk_amount", round(adjustedRisk, 2));
            result.put("risk_percentage", round(riskPercentage, 3));
            result.put("entry_price", entryPrice);
            result.put("stop_loss", stopLoss);
            result.put("take_profit", takeProfit);
            result.put("risk_reward_ratio", round(riskRewardRatio, 2));
            result.put("confidence_used", confidence);
            result.put("kelly_multiplier", round(kellyMultiplier, 3));
            result.put("approved", approved);
            
            logger.info(String.format("Posição calculada para %s: %s", symbol, result));
            
        } catch (Exception e) {
            logger.error("Erro ao calcular posição para " + symbol, e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Calcula multiplicador Kelly baseado na confiança
     */
    private double calculateKellyMultiplier(double confidence) {
        // Kelly Criterion: f = (bp - q) / b
        // Simplificado para trading: f = confidence * 2 - 1
        // Limitado entre 0.1 e 1.5
        return Math.max(0.1, Math.min(1.5, confidence * 1.8));
    }
    
    /**
     * Calcula take profit baseado no ratio risk/reward
     */
    private double calculateTakeProfit(double entry, double stop, double ratio) {
        double riskAmount = Math.abs(entry - stop);
        double rewardAmount = riskAmount * ratio;
        
        if (entry > stop) {  // Long position
            return entry + rewardAmount;
        } else {  // Short position
            return entry - rewardAmount;
        }
    }
    
    /**
     * Valida se a posição está dentro dos limites de risco
     */
    @SuppressWarnings("unchecked")
    private boolean validatePositionRisk(double riskPercentage, String symbol) {
        double maxRiskPerTrade = (double) config.get("max_risk_per_trade") * 100;
        
        // Verifica risco por trade
        if (riskPercentage > maxRiskPerTrade) {
            logger.warning(String.format("Risco por trade excedido para %s: %.2f%%", 
                symbol, riskPercentage));
            return false;
        }
        
        // Verifica número máximo de posições
        int maxPositions = (int) config.get("max_positions");
        if (positions.size() >= maxPositions) {
            logger.warning(String.format("Número máximo de posições atingido: %d", positions.size()));
            return false;
        }
        
        // Verifica correlação (se habilitado)
        boolean enableCorrelation = (boolean) config.get("enable_correlation_check");
        if (enableCorrelation) {
            if (checkCorrelationRisk(symbol)) {
                logger.warning("Risco de correlação alto para " + symbol);
                return false;
            }
        }
        
        return true;
    }
    
    // ========================================================================
    // GESTÃO DE PORTFÓLIO
    // ========================================================================
    
    /**
     * Adiciona nova posição ao portfólio
     */
    public boolean addPosition(Map<String, Object> positionData) {
        try {
            String symbol = (String) positionData.get("symbol");
            
            // Cria objeto de risco da posição
            PositionRisk positionRisk = new PositionRisk(
                symbol,
                (double) positionData.get("position_size"),
                (double) positionData.get("entry_price"),
                (double) positionData.get("entry_price"),
                (double) positionData.get("stop_loss"),
                (double) positionData.get("take_profit"),
                (double) positionData.get("risk_amount"),
                (double) positionData.get("risk_percentage"),
                0.0,
                calculatePositionRiskScore(positionData),
                (double) positionData.getOrDefault("confidence_used", 0.7)
            );
            
            positions.put(symbol, positionRisk);
            availableCapital -= (double) positionData.get("position_value");
            
            logger.info("Posição adicionada: " + symbol);
            updatePortfolioMetrics();
            
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao adicionar posição", e);
            return false;
        }
    }
    
    /**
     * Atualiza preço atual de uma posição
     */
    public void updatePositionPrice(String symbol, double currentPrice) {
        PositionRisk position = positions.get(symbol);
        if (position != null) {
            position.setCurrentPrice(currentPrice);
            
            // Calcula P&L não realizado
            if (position.getPositionSize() > 0) {  // Long
                position.setUnrealizedPnl((currentPrice - position.getEntryPrice()) * 
                    position.getPositionSize());
            } else {  // Short
                position.setUnrealizedPnl((position.getEntryPrice() - currentPrice) * 
                    Math.abs(position.getPositionSize()));
            }
            
            // Atualiza score de risco
            position.setRiskScore(calculateCurrentRiskScore(position));
            
            // Verifica alertas
            checkPositionAlerts(position);
        }
    }
    
    /**
     * Fecha uma posição e calcula resultados
     */
    public Map<String, Object> closePosition(String symbol, double exitPrice, String reason) {
        Map<String, Object> result = new HashMap<>();
        
        PositionRisk position = positions.get(symbol);
        if (position == null) {
            result.put("error", "Posição " + symbol + " não encontrada");
            return result;
        }
        
        // Calcula resultado final
        double pnl;
        if (position.getPositionSize() > 0) {  // Long
            pnl = (exitPrice - position.getEntryPrice()) * position.getPositionSize();
        } else {  // Short
            pnl = (position.getEntryPrice() - exitPrice) * Math.abs(position.getPositionSize());
        }
        
        // Atualiza capital
        double positionValue = Math.abs(position.getPositionSize()) * position.getEntryPrice();
        availableCapital += positionValue + pnl;
        currentCapital += pnl;
        
        // Registra no histórico
        double returnPercentage = position.getRiskAmount() > 0 ? 
            (pnl / position.getRiskAmount()) * 100 : 0;
        
        TradeRecord tradeRecord = new TradeRecord(
            symbol,
            position.getEntryPrice(),
            exitPrice,
            position.getPositionSize(),
            pnl,
            position.getRiskAmount(),
            returnPercentage,
            LocalDateTime.now(),
            reason,
            position.getRiskScore()
        );
        
        tradeHistory.add(tradeRecord);
        
        // Remove posição
        positions.remove(symbol);
        
        logger.info(String.format("Posição fechada: %s, P&L: $%.2f", symbol, pnl));
        updatePortfolioMetrics();
        
        result.put("symbol", symbol);
        result.put("pnl", pnl);
        result.put("return_percentage", returnPercentage);
        
        return result;
    }
    
    // ========================================================================
    // ANÁLISE DE RISCO DO PORTFÓLIO
    // ========================================================================
    
    /**
     * Calcula métricas de risco do portfólio
     */
    public RiskMetrics calculatePortfolioRisk() {
        if (tradeHistory.isEmpty()) {
            return riskMetrics;
        }
        
        try {
            // Extrai retornos
            double[] returns = tradeHistory.stream()
                .mapToDouble(TradeRecord::getReturnPercentage)
                .map(r -> r / 100.0)
                .toArray();
            
            if (returns.length == 0) {
                return riskMetrics;
            }
            
            // VaR (Value at Risk)
            double var95 = percentile(returns, 5) * currentCapital;
            double var99 = percentile(returns, 1) * currentCapital;
            
            // Expected Shortfall (CVaR) - 95%
            double threshold95 = percentile(returns, 5);
            double[] tail95 = Arrays.stream(returns)
                .filter(r -> r <= threshold95)
                .toArray();
            double es95 = tail95.length > 0 ? 
                Arrays.stream(tail95).average().orElse(0) * currentCapital : 0;
            
            // Sharpe Ratio
            double riskFreeRate = 0.02 / 252; // 2% anual
            double meanReturn = Arrays.stream(returns).average().orElse(0);
            double stdReturn = standardDeviation(returns);
            double sharpe = stdReturn > 0 ? (meanReturn - riskFreeRate) / stdReturn : 0;
            
            // Sortino Ratio
            double[] downsideReturns = Arrays.stream(returns)
                .filter(r -> r < 0)
                .toArray();
            double downsideStd = standardDeviation(downsideReturns);
            double sortino = downsideStd > 0 ? (meanReturn - riskFreeRate) / downsideStd : 0;
            
            // Maximum Drawdown
            double[] cumReturns = new double[returns.length];
            cumReturns[0] = 1 + returns[0];
            for (int i = 1; i < returns.length; i++) {
                cumReturns[i] = cumReturns[i-1] * (1 + returns[i]);
            }
            
            double maxDrawdown = 0;
            double peak = cumReturns[0];
            for (double cum : cumReturns) {
                if (cum > peak) {
                    peak = cum;
                }
                double drawdown = (cum - peak) / peak;
                if (drawdown < maxDrawdown) {
                    maxDrawdown = drawdown;
                }
            }
            
            // Volatilidade anualizada
            double volatility = stdReturn * Math.sqrt(252);
            
            // Atualiza métricas
            riskMetrics.setVar95(var95);
            riskMetrics.setVar99(var99);
            riskMetrics.setExpectedShortfall(es95);
            riskMetrics.setSharpeRatio(sharpe);
            riskMetrics.setSortinoRatio(sortino);
            riskMetrics.setMaxDrawdown(maxDrawdown);
            riskMetrics.setVolatility(volatility);
            riskMetrics.setBeta(calculateBeta());
            riskMetrics.setAlpha(calculateAlpha());
            riskMetrics.setInformationRatio(calculateInformationRatio());
            riskMetrics.setCalmarRatio(calculateCalmarRatio());
            riskMetrics.setOmegaRatio(calculateOmegaRatio());
            
        } catch (Exception e) {
            logger.error("Erro ao calcular risco do portfólio", e);
        }
        
        return riskMetrics;
    }
    
    private double percentile(double[] values, double percentile) {
        if (values.length == 0) return 0;
        
        double[] sorted = values.clone();
        Arrays.sort(sorted);
        
        int index = (int) Math.ceil(percentile / 100.0 * sorted.length) - 1;
        return sorted[Math.max(0, Math.min(sorted.length - 1, index))];
    }
    
    private double standardDeviation(double[] values) {
        if (values.length < 2) return 0;
        
        double mean = Arrays.stream(values).average().orElse(0);
        double variance = Arrays.stream(values)
            .map(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0);
        
        return Math.sqrt(variance);
    }
    
    private double calculateBeta() {
        // Implementação simplificada - em produção usar benchmark real
        return 1.0;
    }
    
    private double calculateAlpha() {
        // Implementação simplificada
        return 0.0;
    }
    
    private double calculateInformationRatio() {
        // Implementação simplificada
        return 0.0;
    }
    
    private double calculateCalmarRatio() {
        if (Math.abs(riskMetrics.getMaxDrawdown()) > 1e-10) {
            double annualReturn = calculateAnnualReturn();
            return annualReturn / Math.abs(riskMetrics.getMaxDrawdown());
        }
        return 0.0;
    }
    
    private double calculateOmegaRatio() {
        // Implementação simplificada
        return 0.0;
    }
    
    private double calculateAnnualReturn() {
        if (tradeHistory.isEmpty()) return 0.0;
        
        double totalReturn = (currentCapital - initialCapital) / initialCapital;
        // Simplificado - assumindo 1 ano
        return totalReturn;
    }
    
    // ========================================================================
    // SISTEMA DE ALERTAS
    // ========================================================================
    
    private void checkPositionAlerts(PositionRisk position) {
        // Alerta de stop loss próximo
        double currentRisk = Math.abs(position.getCurrentPrice() - position.getStopLoss()) / 
            position.getCurrentPrice();
        if (currentRisk < 0.02) {  // 2%
            createAlert(
                RiskAlert.HIGH,
                "Stop loss próximo para " + position.getSymbol(),
                String.format("Distância atual: %.2f%%", currentRisk * 100)
            );
        }
        
        // Alerta de P&L negativo significativo
        if (position.getUnrealizedPnl() < -position.getRiskAmount() * 0.8) {
            createAlert(
                RiskAlert.CRITICAL,
                "Perda significativa em " + position.getSymbol(),
                String.format("P&L: $%.2f", position.getUnrealizedPnl())
            );
        }
    }
    
    private void createAlert(RiskAlert level, String title, String message) {
        Alert alert = new Alert(level, title, message);
        alerts.add(alert);
        logger.warning(String.format("ALERTA %s: %s - %s", level.getValue(), title, message));
    }
    
    public List<Alert> getActiveAlerts() {
        return alerts.stream()
            .filter(a -> !a.isAcknowledged())
            .collect(Collectors.toList());
    }
    
    public boolean acknowledgeAlert(int alertIndex) {
        try {
            if (alertIndex >= 0 && alertIndex < alerts.size()) {
                alerts.get(alertIndex).setAcknowledged(true);
                return true;
            }
        } catch (Exception e) {
            // Ignorar
        }
        return false;
    }
    
    // ========================================================================
    // UTILITÁRIOS E RELATÓRIOS
    // ========================================================================
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPortfolioSummary() {
        double totalRisk = positions.values().stream()
            .mapToDouble(PositionRisk::getRiskAmount)
            .sum();
        
        double totalUnrealized = positions.values().stream()
            .mapToDouble(PositionRisk::getUnrealizedPnl)
            .sum();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("capital_inicial", initialCapital);
        summary.put("capital_atual", currentCapital);
        summary.put("capital_disponivel", availableCapital);
        summary.put("posicoes_ativas", positions.size());
        summary.put("risco_total", totalRisk);
        summary.put("pnl_nao_realizado", totalUnrealized);
        summary.put("drawdown_atual", (initialCapital - currentCapital) / initialCapital);
        summary.put("utilizacao_capital", (initialCapital - availableCapital) / initialCapital);
        summary.put("alertas_ativos", getActiveAlerts().size());
        summary.put("trades_realizados", tradeHistory.size());
        
        return summary;
    }
    
    private double calculatePositionRiskScore(Map<String, Object> positionData) {
        double score = 50.0; // Base
        
        // Ajusta por risco percentual
        double riskPct = (double) positionData.get("risk_percentage");
        if (riskPct > 3) {
            score += 20;
        } else if (riskPct > 2) {
            score += 10;
        } else if (riskPct < 1) {
            score -= 10;
        }
        
        // Ajusta por confiança
        double confidence = (double) positionData.getOrDefault("confidence_used", 0.7);
        if (confidence > 0.8) {
            score -= 15;
        } else if (confidence < 0.6) {
            score += 15;
        }
        
        return Math.max(0, Math.min(100, score));
    }
    
    private double calculateCurrentRiskScore(PositionRisk position) {
        double baseScore = position.getRiskScore();
        
        // Ajusta por P&L atual
        if (position.getUnrealizedPnl() < -position.getRiskAmount() * 0.5) {
            baseScore += 20;
        } else if (position.getUnrealizedPnl() > position.getRiskAmount()) {
            baseScore -= 10;
        }
        
        return Math.max(0, Math.min(100, baseScore));
    }
    
    private boolean checkCorrelationRisk(String symbol) {
        // Implementação simplificada - em produção usar correlação real
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void updatePortfolioMetrics() {
        calculatePortfolioRisk();
        
        // Verifica alertas de portfólio
        Map<String, Object> summary = getPortfolioSummary();
        Map<String, Double> thresholds = (Map<String, Double>) config.get("alert_thresholds");
        
        double drawdown = (double) summary.get("drawdown_atual");
        if (drawdown > thresholds.get("drawdown")) {
            createAlert(
                RiskAlert.CRITICAL,
                "Drawdown elevado",
                String.format("Drawdown atual: %.2f%%", drawdown * 100)
            );
        }
    }
    
    /**
     * Exporta relatório de risco completo
     */
    @SuppressWarnings("unchecked")
    public String exportRiskReport() {
        return exportRiskReport(null);
    }
    
    public String exportRiskReport(String filename) {
        if (filename == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            filename = "vhalinor_risk_report_" + timestamp + ".json";
        }
        
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("timestamp", LocalDateTime.now().toString());
        report.put("portfolio_summary", getPortfolioSummary());
        
        Map<String, Object> riskMetricsMap = new HashMap<>();
        riskMetricsMap.put("var_95", riskMetrics.getVar95());
        riskMetricsMap.put("var_99", riskMetrics.getVar99());
        riskMetricsMap.put("expected_shortfall", riskMetrics.getExpectedShortfall());
        riskMetricsMap.put("sharpe_ratio", riskMetrics.getSharpeRatio());
        riskMetricsMap.put("sortino_ratio", riskMetrics.getSortinoRatio());
        riskMetricsMap.put("max_drawdown", riskMetrics.getMaxDrawdown());
        riskMetricsMap.put("volatility", riskMetrics.getVolatility());
        report.put("risk_metrics", riskMetricsMap);
        
        Map<String, Map<String, Object>> positionsMap = new HashMap<>();
        for (Map.Entry<String, PositionRisk> entry : positions.entrySet()) {
            PositionRisk pos = entry.getValue();
            Map<String, Object> posData = new HashMap<>();
            posData.put("risk_amount", pos.getRiskAmount());
            posData.put("unrealized_pnl", pos.getUnrealizedPnl());
            posData.put("risk_score", pos.getRiskScore());
            positionsMap.put(entry.getKey(), posData);
        }
        report.put("active_positions", positionsMap);
        
        List<Map<String, Object>> alertsList = new ArrayList<>();
        for (Alert alert : getActiveAlerts()) {
            Map<String, Object> alertData = new HashMap<>();
            alertData.put("timestamp", alert.getTimestamp().toString());
            alertData.put("level", alert.getLevel());
            alertData.put("title", alert.getTitle());
            alertData.put("message", alert.getMessage());
            alertsList.add(alertData);
        }
        report.put("active_alerts", alertsList);
        
        report.put("config", config);
        
        try {
            String json = mapToJson(report, 0);
            Files.write(Paths.get(filename), json.getBytes());
            logger.info("Relatório de risco exportado: " + filename);
        } catch (IOException e) {
            logger.error("Erro ao exportar relatório", e);
        }
        
        return filename;
    }
    
    private String mapToJson(Map<String, Object> map, int indent) {
        StringBuilder sb = new StringBuilder();
        String indentStr = "  ".repeat(indent);
        
        sb.append("{\n");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",\n");
            }
            first = false;
            
            sb.append(indentStr).append("  \"").append(entry.getKey()).append("\": ");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value instanceof Map) {
                sb.append(mapToJson((Map<String, Object>) value, indent + 1));
            } else if (value instanceof List) {
                sb.append(listToJson((List<?>) value, indent + 1));
            } else {
                sb.append("\"").append(value.toString()).append("\"");
            }
        }
        
        sb.append("\n").append(indentStr).append("}");
        return sb.toString();
    }
    
    private String listToJson(List<?> list, int indent) {
        StringBuilder sb = new StringBuilder();
        String indentStr = "  ".repeat(indent);
        
        sb.append("[\n");
        boolean first = true;
        
        for (Object item : list) {
            if (!first) {
                sb.append(",\n");
            }
            first = false;
            
            sb.append(indentStr).append("  ");
            
            if (item instanceof String) {
                sb.append("\"").append(item).append("\"");
            } else if (item instanceof Number || item instanceof Boolean) {
                sb.append(item);
            } else if (item instanceof Map) {
                sb.append(mapToJson((Map<String, Object>) item, indent + 1));
            } else {
                sb.append("\"").append(item.toString()).append("\"");
            }
        }
        
        sb.append("\n").append(indentStr).append("]");
        return sb.toString();
    }
    
    private double round(double value, int decimals) {
        double scale = Math.pow(10, decimals);
        return Math.round(value * scale) / scale;
    }
    
    // ========================================================================
    // GETTERS
    // ========================================================================
    
    public Map<String, Object> getConfig() { return config; }
    public Map<String, PositionRisk> getPositions() { return positions; }
    public List<TradeRecord> getTradeHistory() { return tradeHistory; }
    public RiskMetrics getRiskMetrics() { return riskMetrics; }
    public double getCurrentCapital() { return currentCapital; }
    public double getAvailableCapital() { return availableCapital; }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("VHALINOR Risk Manager encerrado");
    }
}

// ============================================================================
// EXEMPLO DE USO
// ============================================================================

class VHALINORRiskDemo {
    
    public static void main(String[] args) {
        // Inicializa o gerenciador
        VHALINORRiskManager riskManager = new VHALINORRiskManager(100000.0);
        
        System.out.println("=== VHALINOR RISK MANAGER ===");
        
        // Exemplo de cálculo de posição
        Map<String, Object> positionCalc = riskManager.calculatePositionSize(
            "EURUSD", 1.1000, 1.0950, 0.75
        );
        
        System.out.println("Cálculo de posição: " + positionCalc);
        
        // Adiciona posição se aprovada
        if (positionCalc.containsKey("approved") && (boolean) positionCalc.get("approved")) {
            boolean added = riskManager.addPosition(positionCalc);
            if (added) {
                System.out.println("Posição adicionada ao portfólio");
            }
        }
        
        // Resumo do portfólio
        Map<String, Object> summary = riskManager.getPortfolioSummary();
        System.out.println("Resumo do portfólio: " + summary);
        
        // Exporta relatório
        String reportFile = riskManager.exportRiskReport();
        System.out.println("Relatório exportado: " + reportFile);
        
        riskManager.shutdown();
    }
}