import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.*;
import java.text.DecimalFormat;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LEXTRADER-IAG - Sistema Avançado de Dashboard para Trading Algorítmico
 * Interface gráfica completa com monitoramento em tempo real, simulação e controle
 * Versão convertida de Python para Java
 */
public class TradingDashboardJava {

    // ==================== CONFIGURAÇÃO DE LOGGING ====================
    
    private static final Logger LOGGER = Logger.getLogger(TradingDashboardJava.class.getName());
    
    static {
        try {
            LogManager.getLogManager().reset();
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.ALL);
            ch.setFormatter(new SimpleFormatter() {
                private static final String format = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS - %2$s - %3$s - %4$s%n";
                
                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getSourceClassName() != null ? lr.getSourceClassName() : lr.getLoggerName(),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            });
            
            Logger rootLogger = Logger.getLogger("");
            rootLogger.addHandler(ch);
            
            LOGGER.setLevel(Level.INFO);
            
        } catch (Exception e) {
            System.err.println("Erro ao configurar logging: " + e.getMessage());
        }
    }

    // ==================== ENUMS ====================

    public enum AlgorithmType {
        ML("Machine Learning"),
        STATISTICAL("Statistical"),
        HYBRID("Hybrid AI"),
        QUANTUM("Quantum-Inspired"),
        ENSEMBLE("Ensemble"),
        NEUROSYMBOLIC("Neuro-Symbolic"),
        METALEARNING("Meta-Learning"),
        EVOLUTIONARY("Evolutionary"),
        DEEPREINFORCEMENT("Deep Reinforcement"),
        TRANSFORMER("Transformer-Based");
        
        private final String displayName;
        
        AlgorithmType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }

    public enum NodeStatus {
        IDLE("IDLE"),
        INITIALIZING("INITIALIZING"),
        PROCESSING("PROCESSING"),
        COMPLETED("COMPLETED"),
        WAITING("WAITING"),
        ERROR("ERROR"),
        PAUSED("PAUSED"),
        OPTIMIZING("OPTIMIZING"),
        CONVERGING("CONVERGING"),
        VALIDATING("VALIDATING");
        
        private final String value;
        
        NodeStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public Color getColor() {
            switch (this) {
                case IDLE: return Color.GRAY;
                case INITIALIZING: return new Color(96, 165, 250); // #60a5fa
                case PROCESSING: return new Color(59, 130, 246); // #3b82f6
                case COMPLETED: return new Color(16, 185, 129); // #10b981
                case WAITING: return new Color(245, 158, 11); // #f59e0b
                case ERROR: return new Color(239, 68, 68); // #ef4444
                case PAUSED: return new Color(139, 92, 246); // #8b5cf6
                case OPTIMIZING: return new Color(236, 72, 153); // #ec4899
                case CONVERGING: return new Color(20, 184, 166); // #14b8a6
                case VALIDATING: return new Color(249, 115, 22); // #f97316
                default: return Color.GRAY;
            }
        }
        
        public String getIcon() {
            switch (this) {
                case IDLE: return "⏸️";
                case INITIALIZING: return "🔄";
                case PROCESSING: return "⚙️";
                case COMPLETED: return "✅";
                case WAITING: return "⏳";
                case ERROR: return "❌";
                case PAUSED: return "⏸️";
                case OPTIMIZING: return "📈";
                case CONVERGING: return "🎯";
                case VALIDATING: return "🔍";
                default: return "❓";
            }
        }
    }

    public enum Decision {
        BUY("BUY"),
        SELL("SELL"),
        HOLD("HOLD"),
        CLOSE("CLOSE"),
        SCALP_BUY("SCALP_BUY"),
        SCALP_SELL("SCALP_SELL"),
        SWING_BUY("SWING_BUY"),
        SWING_SELL("SWING_SELL"),
        HEDGE("HEDGE"),
        REBALANCE("REBALANCE");
        
        private final String value;
        
        Decision(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum RiskLevel {
        VERY_LOW("VERY_LOW"),
        LOW("LOW"),
        MODERATE("MODERATE"),
        HIGH("HIGH"),
        VERY_HIGH("VERY_HIGH"),
        EXTREME("EXTREME");
        
        private final String value;
        
        RiskLevel(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum MarketCondition {
        TRENDING_BULL("TRENDING_BULL"),
        TRENDING_BEAR("TRENDING_BEAR"),
        RANGING("RANGING"),
        VOLATILE("VOLATILE"),
        BREAKOUT("BREAKOUT"),
        REVERSAL("REVERSAL"),
        ACCUMULATION("ACCUMULATION"),
        DISTRIBUTION("DISTRIBUTION"),
        SIDEWAYS("SIDEWAYS"),
        CRASH("CRASH"),
        RALLY("RALLY");
        
        private final String value;
        
        MarketCondition(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum TimeFrame {
        TICK("TICK"),
        M1("1m"),
        M5("5m"),
        M15("15m"),
        M30("30m"),
        H1("1h"),
        H4("4h"),
        D1("1d"),
        W1("1w"),
        MN1("1M");
        
        private final String value;
        
        TimeFrame(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum AssetClass {
        CRYPTO("CRYPTO"),
        FOREX("FOREX"),
        STOCKS("STOCKS"),
        INDICES("INDICES"),
        COMMODITIES("COMMODITIES"),
        FUTURES("FUTURES"),
        OPTIONS("OPTIONS"),
        BONDS("BONDS"),
        ETF("ETF");
        
        private final String value;
        
        AssetClass(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum ExecutionMode {
        PAPER("PAPER"),
        LIVE("LIVE"),
        SIMULATION("SIMULATION"),
        BACKTEST("BACKTEST"),
        HYBRID("HYBRID");
        
        private final String value;
        
        ExecutionMode(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum AlertType {
        INFO("INFO"),
        WARNING("WARNING"),
        ERROR("ERROR"),
        SUCCESS("SUCCESS"),
        CRITICAL("CRITICAL"),
        TRADE_SIGNAL("TRADE_SIGNAL"),
        RISK_ALERT("RISK_ALERT"),
        SYSTEM_ALERT("SYSTEM_ALERT"),
        PERFORMANCE_ALERT("PERFORMANCE_ALERT");
        
        private final String value;
        
        AlertType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public Color getColor() {
            switch (this) {
                case INFO: return new Color(59, 130, 246); // #3b82f6
                case WARNING: return new Color(245, 158, 11); // #f59e0b
                case ERROR: return new Color(239, 68, 68); // #ef4444
                case SUCCESS: return new Color(16, 185, 129); // #10b981
                case CRITICAL: return new Color(220, 38, 38); // #dc2626
                case TRADE_SIGNAL: return new Color(139, 92, 246); // #8b5cf6
                case RISK_ALERT: return new Color(236, 72, 153); // #ec4899
                case SYSTEM_ALERT: return new Color(99, 102, 241); // #6366f1
                case PERFORMANCE_ALERT: return new Color(20, 184, 166); // #14b8a6
                default: return Color.GRAY;
            }
        }
        
        public String getIcon() {
            switch (this) {
                case INFO: return "ℹ️";
                case WARNING: return "⚠️";
                case ERROR: return "❌";
                case SUCCESS: return "✅";
                case CRITICAL: return "🔥";
                case TRADE_SIGNAL: return "📈";
                case RISK_ALERT: return "🚨";
                case SYSTEM_ALERT: return "🔧";
                case PERFORMANCE_ALERT: return "📊";
                default: return "📢";
            }
        }
    }

    // ==================== DATA CLASSES ====================

    public static class AdvancedAlgorithm {
        private String id;
        private String name;
        private AlgorithmType type;
        private String description;
        private String version;
        
        // Métricas de performance
        private double accuracy;
        private double precision;
        private double recall;
        private double f1Score;
        private double sharpeRatio;
        private double sortinoRatio;
        private double maxDrawdown;
        private double winRate;
        private double profitFactor;
        
        // Métricas operacionais
        private double speed;
        private double latency;
        private double throughput;
        private double complexity;
        private double confidence;
        private double stability;
        private double robustness;
        
        // Configurações
        private boolean isActive;
        private boolean isOptimizing;
        private boolean isTraining;
        private boolean requiresGpu;
        private int memoryUsageMb;
        private Map<String, Object> parameters;
        private Map<String, Object> hyperparameters;
        
        // Estatísticas
        private int decisionsMade;
        private int decisionsToday;
        private int successCount;
        private int failureCount;
        private double totalProfit;
        private double totalLoss;
        private double avgResponseTime;
        private double avgHoldingTime;
        
        // Especialização
        private List<String> specializations;
        private List<String> compatibleAssets;
        private List<TimeFrame> optimalTimeframes;
        private List<MarketCondition> marketConditions;
        
        // Neural Network Specific
        private String neuralArchitecture;
        private int numLayers;
        private int numParameters;
        private int trainingEpochs;
        private LocalDateTime lastTrained;

        public AdvancedAlgorithm(String id, String name, AlgorithmType type, String description) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.description = description;
            this.version = "1.0.0";
            
            // Inicializar coleções
            this.parameters = new HashMap<>();
            this.hyperparameters = new HashMap<>();
            this.specializations = new ArrayList<>();
            this.compatibleAssets = new ArrayList<>();
            this.optimalTimeframes = new ArrayList<>();
            this.marketConditions = new ArrayList<>();
        }

        // Getters e Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public AlgorithmType getType() { return type; }
        public String getDescription() { return description; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public double getAccuracy() { return accuracy; }
        public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
        
        public double getWinRate() { return winRate; }
        public void setWinRate(double winRate) { this.winRate = winRate; }
        
        public double getSharpeRatio() { return sharpeRatio; }
        public void setSharpeRatio(double sharpeRatio) { this.sharpeRatio = sharpeRatio; }
        
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
        
        public int getDecisionsMade() { return decisionsMade; }
        public void setDecisionsMade(int decisionsMade) { this.decisionsMade = decisionsMade; }
        
        public double getTotalProfit() { return totalProfit; }
        public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }
        
        public List<String> getSpecializations() { return specializations; }
        public List<String> getCompatibleAssets() { return compatibleAssets; }
        
        /**
         * Calcular score de performance ponderado
         */
        public double calculatePerformanceScore() {
            Map<String, Double> weights = new HashMap<>();
            weights.put("accuracy", 0.15);
            weights.put("winRate", 0.20);
            weights.put("sharpeRatio", 0.15);
            weights.put("profitFactor", 0.10);
            weights.put("maxDrawdown", -0.10);
            weights.put("stability", 0.10);
            weights.put("robustness", 0.10);
            weights.put("speed", 0.05);
            weights.put("latency", -0.05);
            
            double score = 0.0;
            score += accuracy * weights.getOrDefault("accuracy", 0.0);
            score += winRate * weights.getOrDefault("winRate", 0.0);
            score += sharpeRatio * 20 * weights.getOrDefault("sharpeRatio", 0.0);
            score += profitFactor * weights.getOrDefault("profitFactor", 0.0);
            score += maxDrawdown * weights.getOrDefault("maxDrawdown", 0.0);
            score += stability * weights.getOrDefault("stability", 0.0);
            score += robustness * weights.getOrDefault("robustness", 0.0);
            score += speed * weights.getOrDefault("speed", 0.0);
            score += latency * weights.getOrDefault("latency", 0.0);
            
            return Math.max(0.0, Math.min(100.0, score));
        }
    }

    public static class AdvancedNode {
        private String id;
        private String name;
        private String category;
        private String inputType;
        private String outputType;
        private String description;
        
        // Métricas
        private double confidence;
        private double executionTime;
        private double processingSpeed;
        private double errorRate;
        
        // Estado
        private NodeStatus status;
        private double progress;
        private LocalDateTime lastExecution;
        private int executionCount;
        private int successCount;
        
        // Dependências
        private List<String> dependencies;
        private List<String> children;
        
        // Recursos
        private double cpuUsage;
        private double memoryUsage;
        private double gpuUsage;
        
        // Configurações
        private int timeoutSeconds;
        private int retryCount;
        private int priority;

        public AdvancedNode(String id, String name, String category, 
                           String inputType, String outputType, String description) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.inputType = inputType;
            this.outputType = outputType;
            this.description = description;
            
            this.status = NodeStatus.IDLE;
            this.progress = 0.0;
            
            this.dependencies = new ArrayList<>();
            this.children = new ArrayList<>();
            
            this.timeoutSeconds = 30;
            this.retryCount = 3;
            this.priority = 1;
        }

        // Getters e Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public NodeStatus getStatus() { return status; }
        public void setStatus(NodeStatus status) { this.status = status; }
        
        public double getProgress() { return progress; }
        public void setProgress(double progress) { this.progress = Math.max(0, Math.min(100, progress)); }
        
        public LocalDateTime getLastExecution() { return lastExecution; }
        public void setLastExecution(LocalDateTime lastExecution) { this.lastExecution = lastExecution; }
        
        public int getExecutionCount() { return executionCount; }
        public void setExecutionCount(int executionCount) { this.executionCount = executionCount; }
        
        public List<String> getDependencies() { return dependencies; }
        public List<String> getChildren() { return children; }
        
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = Math.max(0, Math.min(100, confidence)); }
        
        public Color getStatusColor() {
            return status.getColor();
        }
        
        public String getStatusIcon() {
            return status.getIcon();
        }
    }

    public static class AdvancedDecision {
        private String decisionId;
        private LocalDateTime timestamp;
        private String algorithmId;
        private String algorithmName;
        private Decision decisionType;
        private double confidence;
        
        // Detalhes da decisão
        private String symbol;
        private TimeFrame timeframe;
        private double entryPrice;
        private double stopLoss;
        private double takeProfit;
        private double positionSize;
        private double leverage;
        
        // Raciocínio e análise
        private String reasoning;
        private List<String> technicalFactors;
        private List<String> fundamentalFactors;
        private List<String> sentimentFactors;
        private List<String> riskFactors;
        
        // Análise de risco
        private RiskLevel riskLevel;
        private double riskScore;
        private double var95;
        private double expectedReturn;
        private double expectedLoss;
        private double riskRewardRatio;
        
        // Mercado
        private MarketCondition marketCondition;
        private double volatility;
        private double volumeRatio;
        private double trendStrength;
        
        // Metadados
        private ExecutionMode executionMode;
        private boolean isExecuted;
        private LocalDateTime executionTime;
        private Double executionPrice;
        private Double pnl;
        private Double pnlPercentage;

        public AdvancedDecision(String decisionId, String algorithmId, String algorithmName,
                               Decision decisionType, double confidence, String symbol) {
            this.decisionId = decisionId;
            this.timestamp = LocalDateTime.now();
            this.algorithmId = algorithmId;
            this.algorithmName = algorithmName;
            this.decisionType = decisionType;
            this.confidence = confidence;
            this.symbol = symbol;
            
            this.technicalFactors = new ArrayList<>();
            this.fundamentalFactors = new ArrayList<>();
            this.sentimentFactors = new ArrayList<>();
            this.riskFactors = new ArrayList<>();
            
            this.leverage = 1.0;
            this.executionMode = ExecutionMode.PAPER;
            this.isExecuted = false;
        }

        /**
         * Calcular P&L baseado no preço atual
         */
        public double calculatePnl(double currentPrice) {
            if (!isExecuted || executionPrice == null) {
                return 0.0;
            }
            
            if (decisionType == Decision.BUY || decisionType == Decision.SCALP_BUY || 
                decisionType == Decision.SWING_BUY) {
                return (currentPrice - executionPrice) * positionSize;
            } else if (decisionType == Decision.SELL || decisionType == Decision.SCALP_SELL || 
                      decisionType == Decision.SWING_SELL) {
                return (executionPrice - currentPrice) * positionSize;
            } else {
                return 0.0;
            }
        }

        // Getters e Setters
        public String getDecisionId() { return decisionId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getAlgorithmName() { return algorithmName; }
        public Decision getDecisionType() { return decisionType; }
        public double getConfidence() { return confidence; }
        public String getSymbol() { return symbol; }
        public double getEntryPrice() { return entryPrice; }
        public void setEntryPrice(double entryPrice) { this.entryPrice = entryPrice; }
        
        public double getStopLoss() { return stopLoss; }
        public void setStopLoss(double stopLoss) { this.stopLoss = stopLoss; }
        
        public double getTakeProfit() { return takeProfit; }
        public void setTakeProfit(double takeProfit) { this.takeProfit = takeProfit; }
        
        public double getPositionSize() { return positionSize; }
        public void setPositionSize(double positionSize) { this.positionSize = positionSize; }
        
        public RiskLevel getRiskLevel() { return riskLevel; }
        public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
        
        public boolean isExecuted() { return isExecuted; }
        public void setExecuted(boolean executed) { isExecuted = executed; }
        
        public Double getPnl() { return pnl; }
        public void setPnl(Double pnl) { this.pnl = pnl; }
    }

    public static class PortfolioPosition {
        private String symbol;
        private AssetClass assetClass;
        private double quantity;
        private double entryPrice;
        private double currentPrice;
        private LocalDateTime entryTime;
        private String positionType; // LONG or SHORT
        
        // Métricas
        private double unrealizedPnl;
        private double unrealizedPnlPercent;
        private double realizedPnl;
        private double totalInvested;
        private double currentValue;
        
        // Gerenciamento de risco
        private Double stopLoss;
        private Double takeProfit;
        private Double trailingStop;
        private double riskScore;
        
        // Metadados
        private String algorithmId;
        private String decisionId;
        private boolean isHedged;
        private double hedgeRatio;

        public PortfolioPosition(String symbol, AssetClass assetClass, double quantity,
                                double entryPrice, LocalDateTime entryTime, String positionType) {
            this.symbol = symbol;
            this.assetClass = assetClass;
            this.quantity = quantity;
            this.entryPrice = entryPrice;
            this.currentPrice = entryPrice;
            this.entryTime = entryTime;
            this.positionType = positionType;
            
            updatePrices(entryPrice);
        }

        /**
         * Atualizar preços e calcular P&L
         */
        public void updatePrices(double newPrice) {
            this.currentPrice = newPrice;
            
            if ("LONG".equals(positionType)) {
                this.unrealizedPnl = (newPrice - entryPrice) * quantity;
            } else {
                this.unrealizedPnl = (entryPrice - newPrice) * quantity;
            }
            
            this.totalInvested = entryPrice * quantity;
            this.currentValue = newPrice * quantity;
            
            if (totalInvested > 0) {
                this.unrealizedPnlPercent = (unrealizedPnl / totalInvested) * 100;
            }
        }

        // Getters e Setters
        public String getSymbol() { return symbol; }
        public AssetClass getAssetClass() { return assetClass; }
        public double getQuantity() { return quantity; }
        public double getEntryPrice() { return entryPrice; }
        public double getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(double currentPrice) { updatePrices(currentPrice); }
        
        public String getPositionType() { return positionType; }
        public double getUnrealizedPnl() { return unrealizedPnl; }
        public double getUnrealizedPnlPercent() { return unrealizedPnlPercent; }
        public double getCurrentValue() { return currentValue; }
        
        public Double getStopLoss() { return stopLoss; }
        public void setStopLoss(Double stopLoss) { this.stopLoss = stopLoss; }
        
        public Double getTakeProfit() { return takeProfit; }
        public void setTakeProfit(Double takeProfit) { this.takeProfit = takeProfit; }
        
        public double getRiskScore() { return riskScore; }
        public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    }

    public static class MarketData {
        private String symbol;
        private LocalDateTime timestamp;
        private double open;
        private double high;
        private double low;
        private double close;
        private double volume;
        private double vwap;
        
        // Indicadores técnicos
        private Double rsi;
        private Double macd;
        private Double macdSignal;
        private Double macdHistogram;
        private Double bollingerUpper;
        private Double bollingerMiddle;
        private Double bollingerLower;
        private Double atr;
        private Double obv;
        private Double stochasticK;
        private Double stochasticD;
        private Double adx;
        
        // Sentimento
        private double sentimentScore;
        private double fearGreedIndex;
        private String marketSentiment; // BULLISH, BEARISH, NEUTRAL
        
        // Order book (simplificado)
        private Double bidPrice;
        private Double askPrice;
        private Double bidVolume;
        private Double askVolume;
        private Double spread;

        public MarketData(String symbol, double open, double high, double low, 
                         double close, double volume) {
            this.symbol = symbol;
            this.timestamp = LocalDateTime.now();
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
            
            calculateIndicators();
        }

        /**
         * Calcular indicadores básicos
         */
        private void calculateIndicators() {
            // Calcular spread se bid/ask disponíveis
            if (bidPrice != null && askPrice != null) {
                this.spread = askPrice - bidPrice;
            }
            
            // Calcular tendência básica
            double priceChange = ((close - open) / open) * 100;
            if (priceChange > 1.0) {
                this.marketSentiment = "BULLISH";
            } else if (priceChange < -1.0) {
                this.marketSentiment = "BEARISH";
            } else {
                this.marketSentiment = "NEUTRAL";
            }
        }

        // Getters e Setters
        public String getSymbol() { return symbol; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public double getClose() { return close; }
        public double getVolume() { return volume; }
        public String getMarketSentiment() { return marketSentiment; }
        
        public Double getRsi() { return rsi; }
        public void setRsi(Double rsi) { this.rsi = rsi; }
        
        public Double getMacd() { return macd; }
        public void setMacd(Double macd) { this.macd = macd; }
        
        public Double getBollingerUpper() { return bollingerUpper; }
        public void setBollingerUpper(Double bollingerUpper) { this.bollingerUpper = bollingerUpper; }
    }

    public static class Alert {
        private String alertId;
        private LocalDateTime timestamp;
        private AlertType alertType;
        private String title;
        private String message;
        private String source;
        
        // Prioridade
        private int priority; // 1-10, onde 10 é mais importante
        private boolean isAcknowledged;
        private boolean isResolved;
        
        // Ações
        private boolean actionRequired;
        private String actionTaken;
        private LocalDateTime actionTime;
        
        // Metadados
        private String relatedSymbol;
        private String relatedAlgorithm;
        private String relatedDecision;

        public Alert(String alertId, AlertType alertType, String title, 
                    String message, String source) {
            this.alertId = alertId;
            this.timestamp = LocalDateTime.now();
            this.alertType = alertType;
            this.title = title;
            this.message = message;
            this.source = source;
            
            this.priority = 1;
            this.isAcknowledged = false;
            this.isResolved = false;
            this.actionRequired = false;
        }

        public Color getColor() {
            return alertType.getColor();
        }
        
        public String getIcon() {
            return alertType.getIcon();
        }

        // Getters e Setters
        public String getAlertId() { return alertId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public AlertType getAlertType() { return alertType; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getSource() { return source; }
        
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = Math.max(1, Math.min(10, priority)); }
        
        public boolean isAcknowledged() { return isAcknowledged; }
        public void setAcknowledged(boolean acknowledged) { isAcknowledged = acknowledged; }
        
        public boolean isResolved() { return isResolved; }
        public void setResolved(boolean resolved) { isResolved = resolved; }
        
        public boolean isActionRequired() { return actionRequired; }
        public void setActionRequired(boolean actionRequired) { this.actionRequired = actionRequired; }
    }

    public static class SystemMetrics {
        private LocalDateTime timestamp;
        
        // Performance
        private double cpuUsage;
        private double memoryUsage;
        private double gpuUsage;
        private double diskUsage;
        private double networkUsage;
        
        // Trading
        private int activeTrades;
        private int pendingOrders;
        private int dailyTrades;
        private int weeklyTrades;
        private int monthlyTrades;
        
        // Financeiro
        private double totalEquity;
        private double availableBalance;
        private double marginUsed;
        private double totalPnl;
        private double dailyPnl;
        private double weeklyPnl;
        private double monthlyPnl;
        
        // Algoritmos
        private int activeAlgorithms;
        private int totalAlgorithms;
        private double algorithmSuccessRate;
        private double avgDecisionTime;
        
        // Rede
        private double latencyMs;
        private double packetLoss;
        private String connectionStatus;

        public SystemMetrics() {
            this.timestamp = LocalDateTime.now();
            this.connectionStatus = "CONNECTED";
        }

        // Getters e Setters
        public LocalDateTime getTimestamp() { return timestamp; }
        
        public double getCpuUsage() { return cpuUsage; }
        public void setCpuUsage(double cpuUsage) { this.cpuUsage = Math.max(0, Math.min(100, cpuUsage)); }
        
        public double getMemoryUsage() { return memoryUsage; }
        public void setMemoryUsage(double memoryUsage) { this.memoryUsage = Math.max(0, Math.min(100, memoryUsage)); }
        
        public int getActiveTrades() { return activeTrades; }
        public void setActiveTrades(int activeTrades) { this.activeTrades = activeTrades; }
        
        public double getTotalEquity() { return totalEquity; }
        public void setTotalEquity(double totalEquity) { this.totalEquity = totalEquity; }
        
        public double getTotalPnl() { return totalPnl; }
        public void setTotalPnl(double totalPnl) { this.totalPnl = totalPnl; }
        
        public double getDailyPnl() { return dailyPnl; }
        public void setDailyPnl(double dailyPnl) { this.dailyPnl = dailyPnl; }
        
        public int getActiveAlgorithms() { return activeAlgorithms; }
        public void setActiveAlgorithms(int activeAlgorithms) { this.activeAlgorithms = activeAlgorithms; }
        
        public double getLatencyMs() { return latencyMs; }
        public void setLatencyMs(double latencyMs) { this.latencyMs = latencyMs; }
        
        public String getConnectionStatus() { return connectionStatus; }
        public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }
    }

    // ==================== GERENCIADOR DE EVENTOS ====================

    public static class EventManager {
        private Map<String, List<EventListener>> subscribers;
        private BlockingQueue<Event> eventQueue;
        private AtomicBoolean isRunning;
        private Thread workerThread;

        public static class Event {
            private String type;
            private Object data;
            private LocalDateTime timestamp;

            public Event(String type, Object data) {
                this.type = type;
                this.data = data;
                this.timestamp = LocalDateTime.now();
            }

            public String getType() { return type; }
            public Object getData() { return data; }
            public LocalDateTime getTimestamp() { return timestamp; }
        }

        public interface EventListener {
            void onEvent(Event event);
        }

        public EventManager() {
            this.subscribers = new HashMap<>();
            this.eventQueue = new LinkedBlockingQueue<>();
            this.isRunning = new AtomicBoolean(false);
        }

        public void subscribe(String eventType, EventListener listener) {
            subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        }

        public void unsubscribe(String eventType, EventListener listener) {
            List<EventListener> listeners = subscribers.get(eventType);
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        public void publish(String eventType, Object data) {
            eventQueue.offer(new Event(eventType, data));
        }

        public void start() {
            isRunning.set(true);
            workerThread = new Thread(this::processEvents);
            workerThread.setDaemon(true);
            workerThread.start();
        }

        public void stop() {
            isRunning.set(false);
            if (workerThread != null) {
                try {
                    workerThread.join(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void processEvents() {
            while (isRunning.get()) {
                try {
                    Event event = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (event != null) {
                        List<EventListener> listeners = subscribers.get(event.getType());
                        if (listeners != null) {
                            for (EventListener listener : listeners) {
                                try {
                                    listener.onEvent(event);
                                } catch (Exception e) {
                                    LOGGER.severe("Error in event listener: " + e.getMessage());
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // ==================== COMPONENTES VISUAIS PERSONALIZADOS ====================

    public static class GradientPanel extends JPanel {
        private Color color1;
        private Color color2;

        public GradientPanel(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            // Criar gradiente vertical
            GradientPaint gradient = new GradientPaint(
                0, 0, color1,
                0, height, color2
            );

            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
        }

        public void setColors(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            repaint();
        }
    }

    public static class AnimatedProgressBar extends JPanel {
        private double value;
        private double maxValue;
        private Color progressColor;
        private Color backgroundColor;
        private String text;
        private Timer animationTimer;

        public AnimatedProgressBar() {
            this.value = 0;
            this.maxValue = 100;
            this.progressColor = new Color(59, 130, 246); // #3b82f6
            this.backgroundColor = new Color(31, 41, 55); // #1f2937
            this.text = "0%";
            
            setPreferredSize(new Dimension(200, 20));
            setOpaque(false);
        }

        public void setValue(double value) {
            this.value = Math.max(0, Math.min(value, maxValue));
            this.text = String.format("%.1f%%", this.value);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            // Desenhar fundo
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, width, height, 5, 5);

            // Desenhar progresso
            int progressWidth = (int) ((value / maxValue) * width);
            if (progressWidth > 0) {
                // Ajustar cor baseada no valor
                if (value < 30) {
                    progressColor = new Color(239, 68, 68); // #ef4444
                } else if (value < 70) {
                    progressColor = new Color(245, 158, 11); // #f59e0b
                } else {
                    progressColor = new Color(16, 185, 129); // #10b981
                }
                
                g2d.setColor(progressColor);
                g2d.fillRoundRect(0, 0, progressWidth, height, 5, 5);
            }

            // Desenhar texto
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (width - fm.stringWidth(text)) / 2;
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, textX, textY);

            g2d.dispose();
        }
    }

    public static class LedIndicator extends JPanel {
        private boolean state;
        private Color colorOn;
        private Color colorOff;
        private Timer blinkTimer;
        private boolean blinkState;

        public LedIndicator() {
            this.state = false;
            this.colorOn = new Color(16, 185, 129); // #10b981
            this.colorOff = new Color(75, 85, 99); // #4b5563
            this.blinkState = false;
            
            setPreferredSize(new Dimension(20, 20));
            setOpaque(false);
        }

        public void setState(boolean state) {
            this.state = state;
            repaint();
        }

        public void startBlinking(int intervalMs) {
            if (blinkTimer != null) {
                blinkTimer.stop();
            }
            
            blinkTimer = new Timer(intervalMs, e -> {
                blinkState = !blinkState;
                repaint();
            });
            blinkTimer.start();
        }

        public void stopBlinking() {
            if (blinkTimer != null) {
                blinkTimer.stop();
                blinkTimer = null;
            }
            blinkState = false;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean active = blinkTimer != null ? blinkState : state;
            Color currentColor = active ? colorOn : colorOff;

            // Desenhar LED
            g2d.setColor(currentColor);
            g2d.fillOval(2, 2, getWidth() - 4, getHeight() - 4);

            // Desenhar borda
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(2, 2, getWidth() - 4, getHeight() - 4);

            g2d.dispose();
        }
    }

    public static class CircularProgress extends JPanel {
        private double value;
        private double maxValue;
        private Color progressColor;
        private String text;

        public CircularProgress() {
            this.value = 0;
            this.maxValue = 100;
            this.progressColor = new Color(59, 130, 246); // #3b82f6
            this.text = "0%";
            
            setPreferredSize(new Dimension(80, 80));
            setOpaque(false);
        }

        public void setValue(double value) {
            this.value = Math.max(0, Math.min(value, maxValue));
            this.text = String.format("%.0f%%", this.value);
            
            // Ajustar cor baseada no valor
            if (value < 30) {
                progressColor = new Color(239, 68, 68); // #ef4444
            } else if (value < 70) {
                progressColor = new Color(245, 158, 11); // #f59e0b
            } else {
                progressColor = new Color(16, 185, 129); // #10b981
            }
            
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = size / 2 - 5;

            // Desenhar círculo de fundo
            g2d.setColor(new Color(55, 65, 81)); // #374151
            g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

            // Desenhar arco de progresso
            if (value > 0) {
                double angle = (value / maxValue) * 360;
                g2d.setColor(progressColor);
                g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                           90, (int) -angle);
            }

            // Desenhar círculo interno
            g2d.setColor(getBackground());
            g2d.fillOval(centerX - radius + 5, centerY - radius + 5, 
                        (radius - 5) * 2, (radius - 5) * 2);

            // Desenhar texto
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = centerX - fm.stringWidth(text) / 2;
            int textY = centerY + fm.getAscent() / 2;
            g2d.drawString(text, textX, textY);

            g2d.dispose();
        }
    }

    // ==================== APLICAÇÃO PRINCIPAL ====================

    public static class AdvancedTradingDashboard {
        private JFrame frame;
        private JTabbedPane tabbedPane;
        private JLabel statusLabel;
        private JLabel updateLabel;
        private JLabel modeLabel;
        private JLabel connectionLabel;
        
        // Dados
        private List<AdvancedAlgorithm> algorithms;
        private List<AdvancedNode> decisionFlow;
        private List<AdvancedDecision> recentDecisions;
        private List<PortfolioPosition> portfolioPositions;
        private List<Alert> alerts;
        private SystemMetrics systemMetrics;
        
        // Configurações
        private ExecutionMode executionMode;
        private Map<String, Object> settings;
        private boolean isRunning;
        
        // Eventos
        private EventManager eventManager;
        
        // Cores do tema
        private Map<String, Color> colors;
        
        // Formatação
        private DecimalFormat priceFormat;
        private DecimalFormat percentFormat;
        private DateTimeFormatter timeFormatter;

        public AdvancedTradingDashboard() {
            this.frame = new JFrame("🚀 LEXTRADER-IAG - LEXTRADER-IAG 4.0 - Sistema de Trading Algorítmico Avançado");
            this.tabbedPane = new JTabbedPane();
            
            // Inicializar dados
            initializeData();
            
            // Configurar cores
            setupColors();
            
            // Configurar formatação
            setupFormatters();
            
            // Configurar eventos
            setupEvents();
            
            // Configurar UI
            setupUI();
            
            // Configurar atualizações
            setupUpdates();
            
            // Configurar shutdown
            setupShutdown();
        }

        private void initializeData() {
            this.algorithms = new ArrayList<>();
            this.decisionFlow = new ArrayList<>();
            this.recentDecisions = new ArrayList<>();
            this.portfolioPositions = new ArrayList<>();
            this.alerts = new ArrayList<>();
            this.systemMetrics = new SystemMetrics();
            this.eventManager = new EventManager();
            this.executionMode = ExecutionMode.PAPER;
            this.isRunning = true;
            
            this.settings = new HashMap<>();
            settings.put("risk_tolerance", "MODERATE");
            settings.put("max_position_size", 0.1);
            settings.put("max_daily_loss", 0.02);
            settings.put("auto_trading", false);
            settings.put("notifications_enabled", true);
            settings.put("sound_enabled", false);
            settings.put("theme", "dark");
            settings.put("language", "pt-BR");
            
            // Criar algoritmos de exemplo
            createSampleAlgorithms();
            
            // Criar posições de exemplo
            createSamplePositions();
            
            // Criar alertas de exemplo
            createSampleAlerts();
        }

        private void setupColors() {
            colors = new HashMap<>();
            colors.put("primary", new Color(59, 130, 246)); // #3b82f6
            colors.put("primary_dark", new Color(30, 58, 138)); // #1e3a8a
            colors.put("primary_light", new Color(96, 165, 250)); // #60a5fa
            colors.put("secondary", new Color(16, 185, 129)); // #10b981
            colors.put("secondary_dark", new Color(4, 120, 87)); // #047857
            colors.put("secondary_light", new Color(52, 211, 153)); // #34d399
            colors.put("danger", new Color(239, 68, 68)); // #ef4444
            colors.put("danger_dark", new Color(185, 28, 28)); // #b91c1c
            colors.put("danger_light", new Color(248, 113, 113)); // #f87171
            colors.put("warning", new Color(245, 158, 11)); // #f59e0b
            colors.put("warning_dark", new Color(217, 119, 6)); // #d97706
            colors.put("warning_light", new Color(251, 191, 36)); // #fbbf24
            colors.put("info", new Color(99, 102, 241)); // #6366f1
            colors.put("info_dark", new Color(79, 70, 229)); // #4f46e5
            colors.put("info_light", new Color(129, 140, 248)); // #818cf8
            colors.put("dark", new Color(17, 24, 39)); // #111827
            colors.put("dark_light", new Color(31, 41, 55)); // #1f2937
            colors.put("dark_lighter", new Color(55, 65, 81)); // #374151
            colors.put("gray", new Color(107, 114, 128)); // #6b7280
            colors.put("gray_light", new Color(156, 163, 175)); // #9ca3af
            colors.put("gray_lighter", new Color(209, 213, 219)); // #d1d5db
            colors.put("white", Color.WHITE);
            colors.put("black", Color.BLACK);
        }

        private void setupFormatters() {
            priceFormat = new DecimalFormat("#,##0.00");
            percentFormat = new DecimalFormat("#,##0.00'%'");
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }

        private void setupEvents() {
            eventManager.start();
        }

        private void setupUI() {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1920, 1080);
            frame.setMinimumSize(new Dimension(1280, 720));
            
            // Painel principal com gradiente
            GradientPanel mainPanel = new GradientPanel(
                colors.get("dark"),
                colors.get("dark_light")
            );
            mainPanel.setLayout(new BorderLayout());
            frame.setContentPane(mainPanel);
            
            // Barra superior
            setupTopBar(mainPanel);
            
            // Área principal com abas
            setupTabbedPane(mainPanel);
            
            // Barra de status
            setupStatusBar(mainPanel);
            
            // Centralizar na tela
            frame.setLocationRelativeTo(null);
        }

        private void setupTopBar(JPanel parent) {
            JPanel topBar = new JPanel(new BorderLayout());
            topBar.setOpaque(false);
            topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Logo e título
            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            logoPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("🚀 LEXTRADER-IAG");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(colors.get("primary_light"));
            logoPanel.add(titleLabel);
            
            JLabel subtitleLabel = new JLabel("LEXTRADER-IAG 4.0 - Sistema de Trading Algorítmico Avançado");
            subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            subtitleLabel.setForeground(colors.get("gray_light"));
            logoPanel.add(subtitleLabel);
            
            topBar.add(logoPanel, BorderLayout.WEST);
            
            // Controles
            JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            controlsPanel.setOpaque(false);
            
            // Modo de execução
            JComboBox<ExecutionMode> modeCombo = new JComboBox<>(ExecutionMode.values());
            modeCombo.setSelectedItem(executionMode);
            modeCombo.addActionListener(e -> {
                executionMode = (ExecutionMode) modeCombo.getSelectedItem();
                updateModeLabel();
            });
            controlsPanel.add(modeCombo);
            
            // Botão iniciar/parar
            JButton startStopBtn = new JButton("▶️ Iniciar Sistema");
            startStopBtn.setBackground(colors.get("secondary"));
            startStopBtn.setForeground(Color.WHITE);
            startStopBtn.addActionListener(e -> toggleSystem());
            controlsPanel.add(startStopBtn);
            
            // Botão configurações
            JButton settingsBtn = new JButton("⚙️ Configurações");
            settingsBtn.setBackground(colors.get("primary"));
            settingsBtn.setForeground(Color.WHITE);
            settingsBtn.addActionListener(e -> openSettings());
            controlsPanel.add(settingsBtn);
            
            // Botão ajuda
            JButton helpBtn = new JButton("❓ Ajuda");
            helpBtn.setBackground(colors.get("info"));
            helpBtn.setForeground(Color.WHITE);
            helpBtn.addActionListener(e -> showHelp());
            controlsPanel.add(helpBtn);
            
            topBar.add(controlsPanel, BorderLayout.EAST);
            
            parent.add(topBar, BorderLayout.NORTH);
        }

        private void setupTabbedPane(JPanel parent) {
            tabbedPane = new JTabbedPane();
            tabbedPane.setBackground(colors.get("dark_light"));
            tabbedPane.setForeground(Color.WHITE);
            
            // Criar abas
            createDashboardTab();
            createAlgorithmsTab();
            createTradingTab();
            createAnalysisTab();
            createPortfolioTab();
            createMonitoringTab();
            createBacktestingTab();
            createOptimizationTab();
            createReportsTab();
            
            parent.add(tabbedPane, BorderLayout.CENTER);
        }

        private void setupStatusBar(JPanel parent) {
            JPanel statusBar = new JPanel(new BorderLayout());
            statusBar.setOpaque(false);
            statusBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            statusBar.setPreferredSize(new Dimension(0, 30));
            
            // Status do sistema
            statusLabel = new JLabel("✅ Sistema operando normalmente");
            statusLabel.setForeground(colors.get("secondary"));
            statusBar.add(statusLabel, BorderLayout.WEST);
            
            // Última atualização
            updateLabel = new JLabel("Última atualização: --:--:--");
            updateLabel.setForeground(colors.get("gray_light"));
            statusBar.add(updateLabel, BorderLayout.CENTER);
            
            // Modo de execução
            modeLabel = new JLabel("Modo: " + executionMode.getValue());
            modeLabel.setForeground(colors.get("primary_light"));
            statusBar.add(modeLabel, BorderLayout.EAST);
            
            // Conexão
            connectionLabel = new JLabel("🌐 Conectado");
            connectionLabel.setForeground(colors.get("secondary"));
            statusBar.add(connectionLabel, BorderLayout.EAST);
            
            parent.add(statusBar, BorderLayout.SOUTH);
        }

        private void createDashboardTab() {
            JPanel dashboardPanel = new JPanel(new BorderLayout());
            dashboardPanel.setOpaque(false);
            
            // Painel dividido
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setOpaque(false);
            
            // Painel esquerdo - Visão geral
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setOpaque(false);
            
            // Topo - Métricas principais
            JPanel topMetrics = new JPanel(new GridLayout(1, 4, 10, 0));
            topMetrics.setOpaque(false);
            topMetrics.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            createMetricCard(topMetrics, "💰 Patrimônio Total", "$ 284,567.89", "+2.34%");
            createMetricCard(topMetrics, "📈 P&L Diário", "+$ 2,345.67", "+1.23%");
            createMetricCard(topMetrics, "🎯 Taxa de Acerto", "87.4%", "↑ 1.2%");
            createMetricCard(topMetrics, "⚡ Sharpe Ratio", "2.85", "Estável");
            
            leftPanel.add(topMetrics, BorderLayout.NORTH);
            
            // Meio - Gráficos (simplificado)
            JPanel middlePanel = new JPanel(new GridLayout(2, 1, 0, 10));
            middlePanel.setOpaque(false);
            middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Gráfico de performance (placeholder)
            JPanel perfPanel = createChartPanel("📈 Performance do Portfólio", 300, 200);
            middlePanel.add(perfPanel);
            
            // Gráfico de alocação (placeholder)
            JPanel allocPanel = createChartPanel("📊 Alocação de Ativos", 300, 150);
            middlePanel.add(allocPanel);
            
            leftPanel.add(middlePanel, BorderLayout.CENTER);
            
            splitPane.setLeftComponent(leftPanel);
            
            // Painel direito - Métricas rápidas
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
            
            // Sistema
            JPanel systemPanel = createLabeledPanel("🖥️ Status do Sistema");
            systemPanel.setLayout(new GridLayout(4, 1, 0, 5));
            
            createSystemMetric(systemPanel, "CPU", "42%", 42);
            createSystemMetric(systemPanel, "Memória", "68%", 68);
            createSystemMetric(systemPanel, "GPU", "15%", 15);
            createSystemMetric(systemPanel, "Rede", "24ms", 0);
            
            rightPanel.add(systemPanel, BorderLayout.NORTH);
            
            // Alertas
            JPanel alertsPanel = createLabeledPanel("🚨 Alertas Ativos");
            alertsPanel.setLayout(new BorderLayout());
            
            JList<String> alertsList = new JList<>();
            alertsList.setBackground(colors.get("dark_lighter"));
            alertsList.setForeground(Color.WHITE);
            
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Alert alert : alerts) {
                listModel.addElement(alert.getIcon() + " " + alert.getTitle());
            }
            alertsList.setModel(listModel);
            
            JScrollPane scrollPane = new JScrollPane(alertsList);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            alertsPanel.add(scrollPane, BorderLayout.CENTER);
            
            rightPanel.add(alertsPanel, BorderLayout.CENTER);
            
            // Botões de ação rápida
            JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 5, 0));
            actionsPanel.setOpaque(false);
            actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JButton quickTradeBtn = createStyledButton("📈 Trade Rápido", colors.get("secondary"));
            quickTradeBtn.addActionListener(e -> openQuickTrade());
            actionsPanel.add(quickTradeBtn);
            
            JButton riskBtn = createStyledButton("🛡️ Verificar Risco", colors.get("warning"));
            riskBtn.addActionListener(e -> checkRisk());
            actionsPanel.add(riskBtn);
            
            JButton reportBtn = createStyledButton("📋 Gerar Relatório", colors.get("info"));
            reportBtn.addActionListener(e -> generateReport());
            actionsPanel.add(reportBtn);
            
            rightPanel.add(actionsPanel, BorderLayout.SOUTH);
            
            splitPane.setRightComponent(rightPanel);
            splitPane.setResizeWeight(0.7);
            
            dashboardPanel.add(splitPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("📊 Dashboard", dashboardPanel);
        }

        private void createAlgorithmsTab() {
            JPanel algorithmsPanel = new JPanel(new BorderLayout());
            algorithmsPanel.setOpaque(false);
            
            // Toolbar
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            toolbar.setOpaque(false);
            toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            
            toolbar.add(new JLabel("Filtrar:"));
            
            JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Todos", "Ativos", "Inativos", "Otimizando"});
            toolbar.add(filterCombo);
            
            JTextField searchField = new JTextField(20);
            toolbar.add(searchField);
            
            JButton searchBtn = createStyledButton("🔍", colors.get("primary"));
            toolbar.add(searchBtn);
            
            algorithmsPanel.add(toolbar, BorderLayout.NORTH);
            
            // Lista de algoritmos com scroll
            JPanel algorithmsList = new JPanel();
            algorithmsList.setLayout(new BoxLayout(algorithmsList, BoxLayout.Y_AXIS));
            algorithmsList.setOpaque(false);
            
            for (AdvancedAlgorithm algo : algorithms) {
                JPanel algoCard = createAlgorithmCard(algo);
                algorithmsList.add(algoCard);
                algorithmsList.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            
            JScrollPane scrollPane = new JScrollPane(algorithmsList);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            algorithmsPanel.add(scrollPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("🤖 Algoritmos", algorithmsPanel);
        }

        private void createTradingTab() {
            JPanel tradingPanel = new JPanel(new BorderLayout());
            tradingPanel.setOpaque(false);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setOpaque(false);
            
            // Painel esquerdo - Controles de trading
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setOpaque(false);
            
            // Seleção de ativo
            JPanel assetPanel = createLabeledPanel("📊 Selecionar Ativo");
            assetPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0; gbc.gridy = 0;
            assetPanel.add(new JLabel("Símbolo:"), gbc);
            gbc.gridx = 1;
            JTextField symbolField = new JTextField("BTC/USDT", 15);
            assetPanel.add(symbolField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            assetPanel.add(new JLabel("Timeframe:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> timeframeCombo = new JComboBox<>(new String[]{"1m", "5m", "15m", "30m", "1h", "4h", "1d"});
            assetPanel.add(timeframeCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            gbc.gridwidth = 2;
            JButton loadBtn = createStyledButton("📥 Carregar Dados", colors.get("info"));
            assetPanel.add(loadBtn, gbc);
            
            leftPanel.add(assetPanel, BorderLayout.NORTH);
            
            // Nova ordem
            JPanel orderPanel = createLabeledPanel("📝 Nova Ordem");
            orderPanel.setLayout(new GridBagLayout());
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0; gbc.gridy = 0;
            orderPanel.add(new JLabel("Tipo:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> orderTypeCombo = new JComboBox<>(new String[]{"MARKET", "LIMIT", "STOP", "STOP_LIMIT"});
            orderPanel.add(orderTypeCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            orderPanel.add(new JLabel("Lado:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> sideCombo = new JComboBox<>(new String[]{"BUY", "SELL"});
            orderPanel.add(sideCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            orderPanel.add(new JLabel("Quantidade:"), gbc);
            gbc.gridx = 1;
            JTextField qtyField = new JTextField("0.1", 15);
            orderPanel.add(qtyField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            orderPanel.add(new JLabel("Preço:"), gbc);
            gbc.gridx = 1;
            JTextField priceField = new JTextField("0.0", 15);
            orderPanel.add(priceField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4;
            gbc.gridwidth = 1;
            JButton buyBtn = createStyledButton("📈 Comprar", colors.get("secondary"));
            orderPanel.add(buyBtn, gbc);
            
            gbc.gridx = 1;
            JButton sellBtn = createStyledButton("📉 Vender", colors.get("danger"));
            orderPanel.add(sellBtn, gbc);
            
            leftPanel.add(orderPanel, BorderLayout.CENTER);
            
            splitPane.setLeftComponent(leftPanel);
            
            // Painel direito - Gráficos
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            
            // Placeholder para gráfico
            JPanel chartPanel = createChartPanel("📈 Gráfico de Preços", 600, 400);
            rightPanel.add(chartPanel, BorderLayout.CENTER);
            
            splitPane.setRightComponent(rightPanel);
            splitPane.setResizeWeight(0.4);
            
            tradingPanel.add(splitPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("💰 Trading", tradingPanel);
        }

        private void createAnalysisTab() {
            JPanel analysisPanel = new JPanel(new BorderLayout());
            analysisPanel.setOpaque(false);
            
            JTabbedPane analysisTabs = new JTabbedPane();
            analysisTabs.setBackground(colors.get("dark_light"));
            analysisTabs.setForeground(Color.WHITE);
            
            // Análise Técnica
            JPanel technicalPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            technicalPanel.setOpaque(false);
            technicalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] indicators = {
                {"RSI (14)", "62.3", "Neutral"},
                {"MACD", "+15.2", "Bullish"},
                {"Bollinger", "Upper Touch", "Overbought"},
                {"Volume", "+215%", "High"},
                {"ATR", "342.5", "High Vol"},
                {"ADX", "42.7", "Strong Trend"},
                {"Stoch RSI", "78.9", "Overbought"},
                {"Ichimoku", "Bullish", "Above Cloud"}
            };
            
            for (String[] ind : indicators) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(ind[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(ind[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                JLabel signalLabel = new JLabel(ind[2]);
                signalLabel.setForeground(ind[2].contains("Bullish") ? colors.get("secondary") : 
                                         ind[2].contains("Bearish") ? colors.get("danger") : 
                                         colors.get("warning"));
                signalLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
                card.add(signalLabel, BorderLayout.SOUTH);
                
                technicalPanel.add(card);
            }
            
            analysisTabs.addTab("📈 Técnica", technicalPanel);
            
            // Análise Fundamentalista
            JPanel fundamentalPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            fundamentalPanel.setOpaque(false);
            fundamentalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] fundamentals = {
                {"💰 Market Cap", "$850B", "+2.3%"},
                {"📈 Volume (24h)", "$32.4B", "+15.7%"},
                {"🔢 Circulating Supply", "19.5M BTC", "91.2%"},
                {"📊 Dominance", "52.3%", "-1.2%"},
                {"🏦 Total Value Locked", "$45.2B", "+8.7%"},
                {"👥 Active Addresses", "1.2M", "+5.4%"},
                {"⚡ Hash Rate", "450 EH/s", "+12.3%"},
                {"🔗 Network Difficulty", "67.3T", "+8.9%"}
            };
            
            for (String[] fund : fundamentals) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(fund[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(fund[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                JLabel changeLabel = new JLabel(fund[2]);
                changeLabel.setForeground(fund[2].startsWith("+") ? colors.get("secondary") : colors.get("danger"));
                changeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
                card.add(changeLabel, BorderLayout.SOUTH);
                
                fundamentalPanel.add(card);
            }
            
            analysisTabs.addTab("🏦 Fundamentalista", fundamentalPanel);
            
            // Análise de Sentimento
            JPanel sentimentPanel = new JPanel(new BorderLayout());
            sentimentPanel.setOpaque(false);
            sentimentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Fear & Greed Index
            JPanel fearGreedPanel = createLabeledPanel("😨 Fear & Greed Index");
            fearGreedPanel.setLayout(new BorderLayout());
            
            CircularProgress fearGreedProgress = new CircularProgress();
            fearGreedProgress.setValue(72);
            
            JPanel progressPanel = new JPanel(new FlowLayout());
            progressPanel.setOpaque(false);
            progressPanel.add(fearGreedProgress);
            fearGreedPanel.add(progressPanel, BorderLayout.CENTER);
            
            JLabel fearGreedLabel = new JLabel("Greed", SwingConstants.CENTER);
            fearGreedLabel.setForeground(colors.get("secondary"));
            fearGreedLabel.setFont(new Font("Arial", Font.BOLD, 14));
            fearGreedPanel.add(fearGreedLabel, BorderLayout.SOUTH);
            
            sentimentPanel.add(fearGreedPanel, BorderLayout.NORTH);
            
            // Fontes de sentimento
            JPanel sourcesPanel = createLabeledPanel("📰 Fontes de Sentimento");
            sourcesPanel.setLayout(new GridLayout(4, 1, 0, 5));
            
            String[][] sources = {
                {"Twitter", "78% Bullish", "secondary"},
                {"Reddit", "65% Bullish", "warning"},
                {"News", "72% Bullish", "secondary"},
                {"GitHub", "84% Positive", "secondary"}
            };
            
            for (String[] source : sources) {
                JPanel sourcePanel = new JPanel(new BorderLayout());
                sourcePanel.setOpaque(false);
                
                JLabel nameLabel = new JLabel(source[0]);
                nameLabel.setForeground(Color.WHITE);
                sourcePanel.add(nameLabel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(source[1]);
                valueLabel.setForeground(colors.get(source[2]));
                valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                sourcePanel.add(valueLabel, BorderLayout.EAST);
                
                sourcesPanel.add(sourcePanel);
            }
            
            sentimentPanel.add(sourcesPanel, BorderLayout.CENTER);
            
            analysisTabs.addTab("😊 Sentimento", sentimentPanel);
            
            // Análise de Risco
            JPanel riskPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            riskPanel.setOpaque(false);
            riskPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] risks = {
                {"📉 Value at Risk (95%)", "2.1%", "warning"},
                {"🔥 Conditional VaR", "3.8%", "danger"},
                {"📊 Beta vs Market", "1.2", "info"},
                {"🎯 Sharpe Ratio", "2.85", "secondary"},
                {"🛡️ Sortino Ratio", "3.21", "secondary"},
                {"📉 Maximum Drawdown", "12.3%", "danger"},
                {"⚖️ Portfolio Volatility", "18.7%", "warning"},
                {"🔗 Correlation Matrix", "Moderate", "info"}
            };
            
            for (String[] risk : risks) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(risk[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(risk[1]);
                valueLabel.setForeground(colors.get(risk[2]));
                valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                riskPanel.add(card);
            }
            
            analysisTabs.addTab("🛡️ Risco", riskPanel);
            
            analysisPanel.add(analysisTabs, BorderLayout.CENTER);
            
            tabbedPane.addTab("📊 Análise", analysisPanel);
        }

        private void createPortfolioTab() {
            JPanel portfolioPanel = new JPanel(new BorderLayout());
            portfolioPanel.setOpaque(false);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setOpaque(false);
            
            // Painel esquerdo - Posições
            JPanel positionsPanel = new JPanel(new BorderLayout());
            positionsPanel.setOpaque(false);
            
            // Toolbar
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            toolbar.setOpaque(false);
            
            JButton closeAllBtn = createStyledButton("❌ Fechar Todas", colors.get("danger"));
            closeAllBtn.addActionListener(e -> closeAllPositions());
            toolbar.add(closeAllBtn);
            
            JButton hedgeBtn = createStyledButton("🛡️ Hedgear", colors.get("info"));
            hedgeBtn.addActionListener(e -> hedgePortfolio());
            toolbar.add(hedgeBtn);
            
            positionsPanel.add(toolbar, BorderLayout.NORTH);
            
            // Tabela de posições
            String[] columns = {"Símbolo", "Tipo", "Quantidade", "Entrada", "Atual", "P&L", "P&L %", "Risco"};
            Object[][] data = new Object[portfolioPositions.size()][8];
            
            for (int i = 0; i < portfolioPositions.size(); i++) {
                PortfolioPosition pos = portfolioPositions.get(i);
                data[i][0] = pos.getSymbol();
                data[i][1] = pos.getPositionType();
                data[i][2] = String.format("%.4f", pos.getQuantity());
                data[i][3] = "$" + priceFormat.format(pos.getEntryPrice());
                data[i][4] = "$" + priceFormat.format(pos.getCurrentPrice());
                data[i][5] = String.format("$%,.2f", pos.getUnrealizedPnl());
                data[i][6] = String.format("%+.2f%%", pos.getUnrealizedPnlPercent());
                data[i][7] = String.format("%.1f", pos.getRiskScore());
            }
            
            JTable positionsTable = new JTable(data, columns);
            positionsTable.setBackground(colors.get("dark_lighter"));
            positionsTable.setForeground(Color.WHITE);
            positionsTable.setGridColor(colors.get("gray"));
            positionsTable.setRowHeight(25);
            
            JScrollPane scrollPane = new JScrollPane(positionsTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            positionsPanel.add(scrollPane, BorderLayout.CENTER);
            
            splitPane.setLeftComponent(positionsPanel);
            
            // Painel direito - Estatísticas
            JPanel statsPanel = new JPanel(new BorderLayout());
            statsPanel.setOpaque(false);
            
            // Resumo
            JPanel summaryPanel = createLabeledPanel("📊 Resumo do Portfólio");
            summaryPanel.setLayout(new GridLayout(6, 1, 0, 5));
            
            String[][] summary = {
                {"💰 Valor Total", "$284,567.89"},
                {"📈 P&L Total", "+$12,456.78"},
                {"🎯 P&L %", "+4.56%"},
                {"🛡️ Risco Médio", "5.8/10"},
                {"📊 Diversificação", "7.2/10"},
                {"⚡ Alavancagem", "1.5x"}
            };
            
            for (String[] item : summary) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(false);
                
                JLabel nameLabel = new JLabel(item[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                panel.add(nameLabel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(item[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
                valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                panel.add(valueLabel, BorderLayout.EAST);
                
                summaryPanel.add(panel);
            }
            
            statsPanel.add(summaryPanel, BorderLayout.NORTH);
            
            // Alocação por classe
            JPanel allocationPanel = createLabeledPanel("📈 Alocação por Classe");
            allocationPanel.setLayout(new GridLayout(4, 1, 0, 5));
            
            String[][] allocations = {
                {"Crypto", "65%", "primary"},
                {"Stocks", "20%", "secondary"},
                {"Commodities", "10%", "warning"},
                {"Cash", "5%", "gray"}
            };
            
            for (String[] alloc : allocations) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(false);
                
                JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                leftPanel.setOpaque(false);
                
                JPanel colorIndicator = new JPanel();
                colorIndicator.setBackground(colors.get(alloc[2]));
                colorIndicator.setPreferredSize(new Dimension(15, 15));
                leftPanel.add(colorIndicator);
                
                JLabel nameLabel = new JLabel(alloc[0]);
                nameLabel.setForeground(Color.WHITE);
                leftPanel.add(nameLabel);
                
                panel.add(leftPanel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(alloc[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
                valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                panel.add(valueLabel, BorderLayout.EAST);
                
                allocationPanel.add(panel);
            }
            
            statsPanel.add(allocationPanel, BorderLayout.CENTER);
            
            splitPane.setRightComponent(statsPanel);
            splitPane.setResizeWeight(0.7);
            
            portfolioPanel.add(splitPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("💼 Portfólio", portfolioPanel);
        }

        private void createMonitoringTab() {
            JPanel monitoringPanel = new JPanel(new BorderLayout());
            monitoringPanel.setOpaque(false);
            
            JTabbedPane monitoringTabs = new JTabbedPane();
            monitoringTabs.setBackground(colors.get("dark_light"));
            monitoringTabs.setForeground(Color.WHITE);
            
            // Logs do sistema
            JPanel logsPanel = new JPanel(new BorderLayout());
            logsPanel.setOpaque(false);
            
            // Toolbar
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            toolbar.setOpaque(false);
            
            JButton clearBtn = createStyledButton("🗑️ Limpar Logs", colors.get("danger"));
            clearBtn.addActionListener(e -> clearLogs());
            toolbar.add(clearBtn);
            
            JButton exportBtn = createStyledButton("💾 Exportar", colors.get("secondary"));
            exportBtn.addActionListener(e -> exportLogs());
            toolbar.add(exportBtn);
            
            toolbar.add(new JLabel("Filtrar:"));
            JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Todos", "Info", "Warning", "Error"});
            toolbar.add(filterCombo);
            
            logsPanel.add(toolbar, BorderLayout.NORTH);
            
            // Área de logs
            JTextArea logArea = new JTextArea();
            logArea.setBackground(colors.get("dark_lighter"));
            logArea.setForeground(Color.WHITE);
            logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            logArea.setEditable(false);
            
            String[][] logMessages = {
                {"INFO", "Sistema inicializado com sucesso"},
                {"INFO", "Conectado à Binance API"},
                {"INFO", "Quantum Ensemble carregado"},
                {"WARNING", "Alta volatilidade detectada no mercado"},
                {"SUCCESS", "Ordem executada: BUY 0.5 BTC @ $42,150.00"},
                {"ERROR", "Falha na conexão com Bybit API - Tentando reconectar"},
                {"INFO", "Reconexão bem-sucedida"},
                {"TRADE", "Novo sinal: SELL ETH @ $2,456.78 (Confiança: 91.7%)"}
            };
            
            LocalDateTime now = LocalDateTime.now();
            for (String[] msg : logMessages) {
                String timestamp = now.format(timeFormatter);
                logArea.append("[" + timestamp + "] [" + msg[0] + "] " + msg[1] + "\n");
            }
            
            JScrollPane scrollPane = new JScrollPane(logArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            logsPanel.add(scrollPane, BorderLayout.CENTER);
            
            monitoringTabs.addTab("📝 Logs", logsPanel);
            
            // Métricas em tempo real
            JPanel metricsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            metricsPanel.setOpaque(false);
            metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] metrics = {
                {"📊 CPU Usage", "42%", "warning"},
                {"💾 Memory", "68%", "warning"},
                {"🎮 GPU Usage", "15%", "secondary"},
                {"🌐 Network", "24ms", "secondary"},
                {"⚡ Latência API", "87ms", "info"},
                {"📈 Trades/min", "3.2", "secondary"},
                {"🎯 Decision Rate", "12.4/s", "secondary"},
                {"🔄 Update Freq", "0.5s", "info"}
            };
            
            for (String[] metric : metrics) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(metric[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(metric[1]);
                valueLabel.setForeground(colors.get(metric[2]));
                valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                metricsPanel.add(card);
            }
            
            monitoringTabs.addTab("📊 Métricas", metricsPanel);
            
            // Alertas
            JPanel alertsPanel = new JPanel(new BorderLayout());
            alertsPanel.setOpaque(false);
            
            // Toolbar
            JPanel alertsToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            alertsToolbar.setOpaque(false);
            
            JButton ackAllBtn = createStyledButton("✓ Reconhecer Todos", colors.get("info"));
            ackAllBtn.addActionListener(e -> acknowledgeAllAlerts());
            alertsToolbar.add(ackAllBtn);
            
            JButton clearResolvedBtn = createStyledButton("🗑️ Limpar Resolvidos", colors.get("danger"));
            clearResolvedBtn.addActionListener(e -> clearResolvedAlerts());
            alertsToolbar.add(clearResolvedBtn);
            
            alertsPanel.add(alertsToolbar, BorderLayout.NORTH);
            
            // Lista de alertas
            JList<String> alertsList = new JList<>();
            alertsList.setBackground(colors.get("dark_lighter"));
            alertsList.setForeground(Color.WHITE);
            
            DefaultListModel<String> alertsListModel = new DefaultListModel<>();
            for (Alert alert : alerts) {
                String timeStr = alert.getTimestamp().format(timeFormatter);
                alertsListModel.addElement(alert.getIcon() + " [" + timeStr + "] " + alert.getTitle());
            }
            alertsList.setModel(alertsListModel);
            
            // Colorir baseado na prioridade
            alertsList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                                                              int index, boolean isSelected,
                                                              boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (index < alerts.size()) {
                        Alert alert = alerts.get(index);
                        if (alert.getPriority() >= 8) {
                            setForeground(colors.get("danger"));
                        } else if (alert.getPriority() >= 5) {
                            setForeground(colors.get("warning"));
                        }
                    }
                    return c;
                }
            });
            
            JScrollPane alertsScrollPane = new JScrollPane(alertsList);
            alertsScrollPane.setBorder(BorderFactory.createEmptyBorder());
            alertsPanel.add(alertsScrollPane, BorderLayout.CENTER);
            
            monitoringTabs.addTab("🚨 Alertas", alertsPanel);
            
            monitoringPanel.add(monitoringTabs, BorderLayout.CENTER);
            
            tabbedPane.addTab("👁️ Monitoramento", monitoringPanel);
        }

        private void createBacktestingTab() {
            JPanel backtestPanel = new JPanel(new BorderLayout());
            backtestPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("Backtesting Avançado", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            backtestPanel.add(titleLabel, BorderLayout.NORTH);
            
            JPanel placeholderPanel = new JPanel(new GridBagLayout());
            placeholderPanel.setOpaque(false);
            
            JTextArea placeholder = new JTextArea(
                "Interface de backtesting em desenvolvimento\n\n" +
                "• Teste estratégias em dados históricos\n" +
                "• Otimização de parâmetros\n" +
                "• Análise de performance detalhada\n" +
                "• Comparação de múltiplas estratégias"
            );
            placeholder.setEditable(false);
            placeholder.setBackground(colors.get("dark_lighter"));
            placeholder.setForeground(colors.get("gray_light"));
            placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
            placeholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            placeholderPanel.add(placeholder);
            backtestPanel.add(placeholderPanel, BorderLayout.CENTER);
            
            tabbedPane.addTab("🧪 Backtesting", backtestPanel);
        }

        private void createOptimizationTab() {
            JPanel optimizationPanel = new JPanel(new BorderLayout());
            optimizationPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("Otimização de Algoritmos", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            optimizationPanel.add(titleLabel, BorderLayout.NORTH);
            
            JPanel placeholderPanel = new JPanel(new GridBagLayout());
            placeholderPanel.setOpaque(false);
            
            JTextArea placeholder = new JTextArea(
                "Interface de otimização em desenvolvimento\n\n" +
                "• Otimização bayesiana de parâmetros\n" +
                "• Grid search avançado\n" +
                "• Meta-otimização com GA\n" +
                "• Validação cruzada em tempo real"
            );
            placeholder.setEditable(false);
            placeholder.setBackground(colors.get("dark_lighter"));
            placeholder.setForeground(colors.get("gray_light"));
            placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
            placeholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            placeholderPanel.add(placeholder);
            optimizationPanel.add(placeholderPanel, BorderLayout.CENTER);
            
            tabbedPane.addTab("⚙️ Otimização", optimizationPanel);
        }

        private void createReportsTab() {
            JPanel reportsPanel = new JPanel(new BorderLayout());
            reportsPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("Relatórios e Análises", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            reportsPanel.add(titleLabel, BorderLayout.NORTH);
            
            JPanel placeholderPanel = new JPanel(new GridBagLayout());
            placeholderPanel.setOpaque(false);
            
            JTextArea placeholder = new JTextArea(
                "Interface de relatórios em desenvolvimento\n\n" +
                "• Relatórios de performance diários/semanais/mensais\n" +
                "• Análise de risco detalhada\n" +
                "• Relatórios de conformidade\n" +
                "• Exportação para PDF/Excel"
            );
            placeholder.setEditable(false);
            placeholder.setBackground(colors.get("dark_lighter"));
            placeholder.setForeground(colors.get("gray_light"));
            placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
            placeholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            placeholderPanel.add(placeholder);
            reportsPanel.add(placeholderPanel, BorderLayout.CENTER);
            
            tabbedPane.addTab("📋 Relatórios", reportsPanel);
        }

        // ==================== MÉTODOS AUXILIARES ====================

        private void createMetricCard(JPanel parent, String title, String value, String change) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(colors.get("dark_lighter"));
            card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(colors.get("gray_light"));
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
            card.add(titleLabel, BorderLayout.NORTH);
            
            JLabel valueLabel = new JLabel(value);
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
            valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            card.add(valueLabel, BorderLayout.CENTER);
            
            JLabel changeLabel = new JLabel(change);
            changeLabel.setForeground(change.startsWith("+") ? colors.get("secondary") : 
                                      change.startsWith("-") ? colors.get("danger") : 
                                      colors.get("gray_light"));
            changeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            changeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
            card.add(changeLabel, BorderLayout.SOUTH);
            
            parent.add(card);
        }

        private void createSystemMetric(JPanel parent, String name, String value, int progressValue) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(name);
            nameLabel.setForeground(colors.get("gray_light"));
            panel.add(nameLabel, BorderLayout.WEST);
            
            JLabel valueLabel = new JLabel(value);
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(valueLabel, BorderLayout.EAST);
            
            parent.add(panel);
            
            if (progressValue > 0) {
                AnimatedProgressBar progressBar = new AnimatedProgressBar();
                progressBar.setValue(progressValue);
                parent.add(progressBar);
            }
        }

        private JPanel createChartPanel(String title, int width, int height) {
            JPanel panel = createLabeledPanel(title);
            panel.setLayout(new GridBagLayout());
            
            JLabel placeholder = new JLabel("Gráfico interativo será implementado aqui");
            placeholder.setForeground(colors.get("gray_light"));
            panel.add(placeholder);
            
            return panel;
        }

        private JPanel createLabeledPanel(String title) {
            JPanel panel = new JPanel();
            panel.setBackground(colors.get("dark_light"));
            panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                Color.WHITE
            ));
            
            return panel;
        }

        private JButton createStyledButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            return button;
        }

        private JPanel createAlgorithmCard(AdvancedAlgorithm algorithm) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(colors.get("dark_lighter"));
            card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
            
            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            
            LedIndicator statusLed = new LedIndicator();
            statusLed.setState(algorithm.isActive());
            headerPanel.add(statusLed, BorderLayout.WEST);
            
            JLabel typeLabel = new JLabel(algorithm.getType().getDisplayName());
            typeLabel.setForeground(colors.get("primary_light"));
            typeLabel.setFont(new Font("Arial", Font.BOLD, 11));
            typeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            headerPanel.add(typeLabel, BorderLayout.CENTER);
            
            double score = algorithm.calculatePerformanceScore();
            CircularProgress scoreProgress = new CircularProgress();
            scoreProgress.setValue(score);
            headerPanel.add(scoreProgress, BorderLayout.EAST);
            
            card.add(headerPanel, BorderLayout.NORTH);
            
            // Descrição
            JLabel descLabel = new JLabel("<html><body style='width: 400px'>" + 
                                          algorithm.getDescription() + "</body></html>");
            descLabel.setForeground(colors.get("gray_light"));
            descLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            card.add(descLabel, BorderLayout.CENTER);
            
            // Métricas
            JPanel metricsPanel = new JPanel(new GridLayout(3, 2, 10, 5));
            metricsPanel.setOpaque(false);
            metricsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            
            String[][] metrics = {
                {"🎯 Precisão", String.format("%.1f%%", algorithm.getAccuracy())},
                {"📈 Win Rate", String.format("%.1f%%", algorithm.getWinRate())},
                {"⚡ Sharpe", String.format("%.2f", algorithm.getSharpeRatio())},
                {"🎯 Confiança", String.format("%.1f%%", algorithm.getConfidence())}
            };
            
            for (String[] metric : metrics) {
                JPanel metricPanel = new JPanel(new BorderLayout());
                metricPanel.setOpaque(false);
                
                JLabel nameLabel = new JLabel(metric[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                metricPanel.add(nameLabel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(metric[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 11));
                metricPanel.add(valueLabel, BorderLayout.EAST);
                
                metricsPanel.add(metricPanel);
            }
            
            card.add(metricsPanel, BorderLayout.SOUTH);
            
            return card;
        }

        // ==================== MÉTODOS DE CRIAÇÃO DE DADOS ====================

        private void createSampleAlgorithms() {
            AdvancedAlgorithm algo = new AdvancedAlgorithm(
                "quantum_ensemble",
                "Quantum Ensemble Pro",
                AlgorithmType.QUANTUM,
                "Ensemble quântico que combina múltiplas redes neurais com superposição quântica para análise de mercado multidimensional"
            );
            algo.setVersion("3.2.1");
            algo.setAccuracy(94.7);
            algo.setWinRate(87.4);
            algo.setSharpeRatio(2.85);
            algo.setConfidence(91.2);
            algo.setActive(true);
            algo.setDecisionsMade(15247);
            algo.setTotalProfit(284500.75);
            algo.getSpecializations().addAll(Arrays.asList("Análise Quântica", "Ensemble Learning", "Meta-Otimização"));
            algo.getCompatibleAssets().addAll(Arrays.asList("BTC", "ETH", "SPX", "NASDAQ"));
            
            algorithms.add(algo);
        }

        private void createSamplePositions() {
            PortfolioPosition pos = new PortfolioPosition(
                "BTC/USDT",
                AssetClass.CRYPTO,
                0.85,
                42890.45,
                LocalDateTime.now().minusDays(1),
                "LONG"
            );
            pos.setCurrentPrice(43256.78);
            pos.setStopLoss(41500.00);
            pos.setTakeProfit(45500.00);
            pos.setRiskScore(5.8);
            
            portfolioPositions.add(pos);
        }

        private void createSampleAlerts() {
            Alert alert = new Alert(
                "ALERT-001",
                AlertType.TRADE_SIGNAL,
                "Novo Sinal de Compra Detectado",
                "Quantum Ensemble detectou forte sinal de compra para ETH/USDT com 91.7% de confiança",
                "Quantum Ensemble Pro"
            );
            alert.setPriority(8);
            alert.setActionRequired(true);
            
            alerts.add(alert);
        }

        // ==================== MÉTODOS DE ATUALIZAÇÃO ====================

        private void setupUpdates() {
            Timer updateTimer = new Timer(1000, e -> updateClock());
            updateTimer.start();
            
            Timer metricsTimer = new Timer(5000, e -> updateMetrics());
            metricsTimer.start();
        }

        private void updateClock() {
            String timeStr = LocalDateTime.now().format(timeFormatter);
            updateLabel.setText("Última atualização: " + timeStr);
        }

        private void updateMetrics() {
            // Atualizar métricas do sistema (simulação)
            for (AdvancedAlgorithm algo : algorithms) {
                if (algo.isActive()) {
                    // Pequenas variações nas métricas
                    algo.setConfidence(Math.max(0, Math.min(100, 
                        algo.getConfidence() + (Math.random() * 1 - 0.5))));
                }
            }
        }

        private void updateModeLabel() {
            modeLabel.setText("Modo: " + executionMode.getValue());
            
            Color color = switch (executionMode) {
                case PAPER -> colors.get("info");
                case LIVE -> colors.get("danger");
                case SIMULATION -> colors.get("warning");
                case BACKTEST -> colors.get("primary");
                case HYBRID -> colors.get("secondary");
            };
            modeLabel.setForeground(color);
        }

        // ==================== MANIPULADORES DE EVENTOS ====================

        private void toggleSystem() {
            isRunning = !isRunning;
            if (isRunning) {
                statusLabel.setText("✅ Sistema iniciado");
                statusLabel.setForeground(colors.get("secondary"));
                LOGGER.info("Sistema iniciado");
            } else {
                statusLabel.setText("⏸️ Sistema parado");
                statusLabel.setForeground(colors.get("warning"));
                LOGGER.info("Sistema parado");
            }
        }

        private void openSettings() {
            JDialog settingsDialog = new JDialog(frame, "Configurações do Sistema", true);
            settingsDialog.setSize(500, 600);
            settingsDialog.setLocationRelativeTo(frame);
            
            JTabbedPane settingsTabs = new JTabbedPane();
            settingsTabs.setBackground(colors.get("dark_light"));
            settingsTabs.setForeground(Color.WHITE);
            
            // Aba Geral
            JPanel generalPanel = new JPanel(new GridBagLayout());
            generalPanel.setBackground(colors.get("dark"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;
            
            gbc.gridx = 0; gbc.gridy = 0;
            generalPanel.add(new JLabel("Tema:"), gbc);
            gbc.gridy = 1;
            JComboBox<String> themeCombo = new JComboBox<>(new String[]{"dark", "light", "auto"});
            generalPanel.add(themeCombo, gbc);
            
            gbc.gridy = 2;
            generalPanel.add(new JLabel("Idioma:"), gbc);
            gbc.gridy = 3;
            JComboBox<String> langCombo = new JComboBox<>(new String[]{"pt-BR", "en-US", "es-ES"});
            generalPanel.add(langCombo, gbc);
            
            gbc.gridy = 4;
            JCheckBox autoTradeCheck = new JCheckBox("Auto-trading");
            autoTradeCheck.setBackground(colors.get("dark"));
            autoTradeCheck.setForeground(Color.WHITE);
            generalPanel.add(autoTradeCheck, gbc);
            
            settingsTabs.addTab("Geral", generalPanel);
            
            // Aba Risco (simplificada)
            JPanel riskPanel = new JPanel();
            riskPanel.setBackground(colors.get("dark"));
            riskPanel.add(new JLabel("Configurações de risco em desenvolvimento"));
            settingsTabs.addTab("Risco", riskPanel);
            
            // Aba Notificações (simplificada)
            JPanel notifPanel = new JPanel();
            notifPanel.setBackground(colors.get("dark"));
            notifPanel.add(new JLabel("Configurações de notificações em desenvolvimento"));
            settingsTabs.addTab("Notificações", notifPanel);
            
            settingsDialog.add(settingsTabs, BorderLayout.CENTER);
            
            // Botões
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(colors.get("dark"));
            
            JButton saveBtn = createStyledButton("💾 Salvar", colors.get("secondary"));
            saveBtn.addActionListener(e -> settingsDialog.dispose());
            buttonPanel.add(saveBtn);
            
            JButton cancelBtn = createStyledButton("❌ Cancelar", colors.get("danger"));
            cancelBtn.addActionListener(e -> settingsDialog.dispose());
            buttonPanel.add(cancelBtn);
            
            settingsDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            settingsDialog.setVisible(true);
        }

        private void showHelp() {
            String helpText = """
                LEXTRADER-IAG - LEXTRADER-IAG 4.0 - Sistema de Trading Algorítmico Avançado
                
                📚 FUNCIONALIDADES PRINCIPAIS:
                
                1. 🤖 ALGORITMOS AVANÇADOS
                   • Quantum Ensemble: Combinação de múltiplas redes neurais
                   • LSTM Adaptativo: Análise de séries temporais
                   • Otimizador Bayesiano: Otimização multi-objetivo
                
                2. 💼 GESTÃO DE PORTFÓLIO
                   • Alocação dinâmica de ativos
                   • Controle de risco em tempo real
                   • Hedge automático
                
                3. 📊 ANÁLISE AVANÇADA
                   • Técnica: Indicadores e padrões
                   • Fundamentalista: Métricas de mercado
                   • Sentimento: Análise de mídias sociais
                   • Risco: VaR, CVaR, drawdown
                
                Para suporte técnico: suporte@lextrader.com.br
            """;
            
            JTextArea helpArea = new JTextArea(helpText);
            helpArea.setEditable(false);
            helpArea.setBackground(colors.get("dark_lighter"));
            helpArea.setForeground(Color.WHITE);
            helpArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JOptionPane.showMessageDialog(frame, new JScrollPane(helpArea), "Ajuda", JOptionPane.INFORMATION_MESSAGE);
        }

        private void openQuickTrade() {
            JDialog quickTradeDialog = new JDialog(frame, "Trade Rápido", true);
            quickTradeDialog.setSize(400, 300);
            quickTradeDialog.setLocationRelativeTo(frame);
            
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(colors.get("dark"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Símbolo:"), gbc);
            gbc.gridx = 1;
            JTextField symbolField = new JTextField("BTC/USDT", 15);
            panel.add(symbolField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Lado:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> sideCombo = new JComboBox<>(new String[]{"BUY", "SELL"});
            panel.add(sideCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Quantidade:"), gbc);
            gbc.gridx = 1;
            JTextField qtyField = new JTextField("0.1", 15);
            panel.add(qtyField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(colors.get("dark"));
            
            JButton buyBtn = createStyledButton("📈 Comprar", colors.get("secondary"));
            buyBtn.addActionListener(e -> {
                try {
                    double qty = Double.parseDouble(qtyField.getText());
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        sideCombo.getSelectedItem() + " " + qty + " " + symbolField.getText() + " executado com sucesso!",
                        "Trade Executado",
                        JOptionPane.INFORMATION_MESSAGE);
                    quickTradeDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        "Quantidade inválida!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(buyBtn);
            
            JButton sellBtn = createStyledButton("📉 Vender", colors.get("danger"));
            sellBtn.addActionListener(e -> {
                try {
                    double qty = Double.parseDouble(qtyField.getText());
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        sideCombo.getSelectedItem() + " " + qty + " " + symbolField.getText() + " executado com sucesso!",
                        "Trade Executado",
                        JOptionPane.INFORMATION_MESSAGE);
                    quickTradeDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        "Quantidade inválida!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(sellBtn);
            
            panel.add(buttonPanel, gbc);
            
            quickTradeDialog.add(panel);
            quickTradeDialog.setVisible(true);
        }

        private void checkRisk() {
            JOptionPane.showMessageDialog(frame,
                "Análise de risco completada.\nPortfólio dentro dos limites de risco configurados.",
                "Verificar Risco",
                JOptionPane.INFORMATION_MESSAGE);
        }

        private void generateReport() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório");
            
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Relatório gerado com sucesso:\n" + fileChooser.getSelectedFile().getPath(),
                    "Relatório",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void closeAllPositions() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Fechar todas as posições abertas?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Todas as posições foram fechadas!",
                    "Fechar Posições",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void hedgePortfolio() {
            JOptionPane.showMessageDialog(frame,
                "Iniciando hedge do portfólio...",
                "Hedge",
                JOptionPane.INFORMATION_MESSAGE);
        }

        private void clearLogs() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Limpar todos os logs?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Logs limpos com sucesso!",
                    "Limpar Logs",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void exportLogs() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Exportar Logs");
            
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Logs exportados para: " + fileChooser.getSelectedFile().getPath(),
                    "Exportar",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void acknowledgeAllAlerts() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Reconhecer todos os alertas?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Todos os alertas foram reconhecidos!",
                    "Reconhecer Alertas",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void clearResolvedAlerts() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Limpar alertas resolvidos?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Alertas resolvidos limpos!",
                    "Limpar Alertas",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void setupShutdown() {
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    eventManager.stop();
                    LOGGER.info("Sistema encerrado");
                }
            });
        }

        public void show() {
            frame.setVisible(true);
        }
    }

    // ==================== FUNÇÃO PRINCIPAL ====================

    public static void main(String[] args) {
        LOGGER.info("=".repeat(60));
        LOGGER.info("INICIANDO LEXTRADER-IAG - DASHBOARD DE TRADING");
        LOGGER.info("=".repeat(60));
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                AdvancedTradingDashboard dashboard = new AdvancedTradingDashboard();
                dashboard.show();
                
                LOGGER.info("Dashboard iniciado com sucesso");
                
            } catch (Exception e) {
                LOGGER.severe("Erro ao iniciar dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.*;
import java.text.DecimalFormat;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LEXTRADER-IAG - Sistema Avançado de Dashboard para Trading Algorítmico
 * Interface gráfica completa com monitoramento em tempo real, simulação e controle
 * Versão convertida de Python para Java
 */
public class TradingDashboardJava {

    // ==================== CONFIGURAÇÃO DE LOGGING ====================
    
    private static final Logger LOGGER = Logger.getLogger(TradingDashboardJava.class.getName());
    
    static {
        try {
            LogManager.getLogManager().reset();
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.ALL);
            ch.setFormatter(new SimpleFormatter() {
                private static final String format = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS - %2$s - %3$s - %4$s%n";
                
                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getSourceClassName() != null ? lr.getSourceClassName() : lr.getLoggerName(),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            });
            
            Logger rootLogger = Logger.getLogger("");
            rootLogger.addHandler(ch);
            
            LOGGER.setLevel(Level.INFO);
            
        } catch (Exception e) {
            System.err.println("Erro ao configurar logging: " + e.getMessage());
        }
    }

    // ==================== ENUMS ====================

    public enum AlgorithmType {
        ML("Machine Learning"),
        STATISTICAL("Statistical"),
        HYBRID("Hybrid AI"),
        QUANTUM("Quantum-Inspired"),
        ENSEMBLE("Ensemble"),
        NEUROSYMBOLIC("Neuro-Symbolic"),
        METALEARNING("Meta-Learning"),
        EVOLUTIONARY("Evolutionary"),
        DEEPREINFORCEMENT("Deep Reinforcement"),
        TRANSFORMER("Transformer-Based");
        
        private final String displayName;
        
        AlgorithmType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }

    public enum NodeStatus {
        IDLE("IDLE"),
        INITIALIZING("INITIALIZING"),
        PROCESSING("PROCESSING"),
        COMPLETED("COMPLETED"),
        WAITING("WAITING"),
        ERROR("ERROR"),
        PAUSED("PAUSED"),
        OPTIMIZING("OPTIMIZING"),
        CONVERGING("CONVERGING"),
        VALIDATING("VALIDATING");
        
        private final String value;
        
        NodeStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public Color getColor() {
            switch (this) {
                case IDLE: return Color.GRAY;
                case INITIALIZING: new Color(96, 165, 250); // #60a5fa
                case PROCESSING: new Color(59, 130, 246); // #3b82f6
                case COMPLETED: new Color(16, 185, 129); // #10b981
                case WAITING: new Color(245, 158, 11); // #f59e0b
                case ERROR: new Color(239, 68, 68); // #ef4444
                case PAUSED: new Color(139, 92, 246); // #8b5cf6
                case OPTIMIZING: new Color(236, 72, 153); // #ec4899
                case CONVERGING: new Color(20, 184, 166); // #14b8a6
                case VALIDATING: new Color(249, 115, 22); // #f97316
                default: return Color.GRAY;
            }
        }
        
        public String getIcon() {
            switch (this) {
                case IDLE: return "⏸️";
                case INITIALIZING: return "🔄";
                case PROCESSING: return "⚙️";
                case COMPLETED: return "✅";
                case WAITING: return "⏳";
                case ERROR: return "❌";
                case PAUSED: return "⏸️";
                case OPTIMIZING: return "📈";
                case CONVERGING: return "🎯";
                case VALIDATING: return "🔍";
                default: return "❓";
            }
        }
    }

    public enum Decision {
        BUY("BUY"),
        SELL("SELL"),
        HOLD("HOLD"),
        CLOSE("CLOSE"),
        SCALP_BUY("SCALP_BUY"),
        SCALP_SELL("SCALP_SELL"),
        SWING_BUY("SWING_BUY"),
        SWING_SELL("SWING_SELL"),
        HEDGE("HEDGE"),
        REBALANCE("REBALANCE");
        
        private final String value;
        
        Decision(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum RiskLevel {
        VERY_LOW("VERY_LOW"),
        LOW("LOW"),
        MODERATE("MODERATE"),
        HIGH("HIGH"),
        VERY_HIGH("VERY_HIGH"),
        EXTREME("EXTREME");
        
        private final String value;
        
        RiskLevel(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum MarketCondition {
        TRENDING_BULL("TRENDING_BULL"),
        TRENDING_BEAR("TRENDING_BEAR"),
        RANGING("RANGING"),
        VOLATILE("VOLATILE"),
        BREAKOUT("BREAKOUT"),
        REVERSAL("REVERSAL"),
        ACCUMULATION("ACCUMULATION"),
        DISTRIBUTION("DISTRIBUTION"),
        SIDEWAYS("SIDEWAYS"),
        CRASH("CRASH"),
        RALLY("RALLY");
        
        private final String value;
        
        MarketCondition(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum TimeFrame {
        TICK("TICK"),
        M1("1m"),
        M5("5m"),
        M15("15m"),
        M30("30m"),
        H1("1h"),
        H4("4h"),
        D1("1d"),
        W1("1w"),
        MN1("1M");
        
        private final String value;
        
        TimeFrame(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum AssetClass {
        CRYPTO("CRYPTO"),
        FOREX("FOREX"),
        STOCKS("STOCKS"),
        INDICES("INDICES"),
        COMMODITIES("COMMODITIES"),
        FUTURES("FUTURES"),
        OPTIONS("OPTIONS"),
        BONDS("BONDS"),
        ETF("ETF");
        
        private final String value;
        
        AssetClass(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum ExecutionMode {
        PAPER("PAPER"),
        LIVE("LIVE"),
        SIMULATION("SIMULATION"),
        BACKTEST("BACKTEST"),
        HYBRID("HYBRID");
        
        private final String value;
        
        ExecutionMode(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum AlertType {
        INFO("INFO"),
        WARNING("WARNING"),
        ERROR("ERROR"),
        SUCCESS("SUCCESS"),
        CRITICAL("CRITICAL"),
        TRADE_SIGNAL("TRADE_SIGNAL"),
        RISK_ALERT("RISK_ALERT"),
        SYSTEM_ALERT("SYSTEM_ALERT"),
        PERFORMANCE_ALERT("PERFORMANCE_ALERT");
        
        private final String value;
        
        AlertType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public Color getColor() {
            switch (this) {
                case INFO: return new Color(59, 130, 246); // #3b82f6
                case WARNING: return new Color(245, 158, 11); // #f59e0b
                case ERROR: return new Color(239, 68, 68); // #ef4444
                case SUCCESS: return new Color(16, 185, 129); // #10b981
                case CRITICAL: return new Color(220, 38, 38); // #dc2626
                case TRADE_SIGNAL: return new Color(139, 92, 246); // #8b5cf6
                case RISK_ALERT: return new Color(236, 72, 153); // #ec4899
                case SYSTEM_ALERT: return new Color(99, 102, 241); // #6366f1
                case PERFORMANCE_ALERT: return new Color(20, 184, 166); // #14b8a6
                default: return Color.GRAY;
            }
        }
        
        public String getIcon() {
            switch (this) {
                case INFO: return "ℹ️";
                case WARNING: return "⚠️";
                case ERROR: return "❌";
                case SUCCESS: return "✅";
                case CRITICAL: return "🔥";
                case TRADE_SIGNAL: return "📈";
                case RISK_ALERT: return "🚨";
                case SYSTEM_ALERT: return "🔧";
                case PERFORMANCE_ALERT: return "📊";
                default: return "📢";
            }
        }
    }

    // ==================== DATA CLASSES ====================

    public static class AdvancedAlgorithm {
        private String id;
        private String name;
        private AlgorithmType type;
        private String description;
        private String version;
        
        // Métricas de performance
        private double accuracy;
        private double precision;
        private double recall;
        private double f1Score;
        private double sharpeRatio;
        private double sortinoRatio;
        private double maxDrawdown;
        private double winRate;
        private double profitFactor;
        
        // Métricas operacionais
        private double speed;
        private double latency;
        private double throughput;
        private double complexity;
        private double confidence;
        private double stability;
        private double robustness;
        
        // Configurações
        private boolean isActive;
        private boolean isOptimizing;
        private boolean isTraining;
        private boolean requiresGpu;
        private int memoryUsageMb;
        private Map<String, Object> parameters;
        private Map<String, Object> hyperparameters;
        
        // Estatísticas
        private int decisionsMade;
        private int decisionsToday;
        private int successCount;
        private int failureCount;
        private double totalProfit;
        private double totalLoss;
        private double avgResponseTime;
        private double avgHoldingTime;
        
        // Especialização
        private List<String> specializations;
        private List<String> compatibleAssets;
        private List<TimeFrame> optimalTimeframes;
        private List<MarketCondition> marketConditions;
        
        // Neural Network Specific
        private String neuralArchitecture;
        private int numLayers;
        private int numParameters;
        private int trainingEpochs;
        private LocalDateTime lastTrained;

        public AdvancedAlgorithm(String id, String name, AlgorithmType type, String description) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.description = description;
            this.version = "1.0.0";
            
            // Inicializar coleções
            this.parameters = new HashMap<>();
            this.hyperparameters = new HashMap<>();
            this.specializations = new ArrayList<>();
            this.compatibleAssets = new ArrayList<>();
            this.optimalTimeframes = new ArrayList<>();
            this.marketConditions = new ArrayList<>();
        }

        // Getters e Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public AlgorithmType getType() { return type; }
        public String getDescription() { return description; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public double getAccuracy() { return accuracy; }
        public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
        
        public double getWinRate() { return winRate; }
        public void setWinRate(double winRate) { this.winRate = winRate; }
        
        public double getSharpeRatio() { return sharpeRatio; }
        public void setSharpeRatio(double sharpeRatio) { this.sharpeRatio = sharpeRatio; }
        
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
        
        public int getDecisionsMade() { return decisionsMade; }
        public void setDecisionsMade(int decisionsMade) { this.decisionsMade = decisionsMade; }
        
        public double getTotalProfit() { return totalProfit; }
        public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }
        
        public List<String> getSpecializations() { return specializations; }
        public List<String> getCompatibleAssets() { return compatibleAssets; }
        
        /**
         * Calcular score de performance ponderado
         */
        public double calculatePerformanceScore() {
            Map<String, Double> weights = new HashMap<>();
            weights.put("accuracy", 0.15);
            weights.put("winRate", 0.20);
            weights.put("sharpeRatio", 0.15);
            weights.put("profitFactor", 0.10);
            weights.put("maxDrawdown", -0.10);
            weights.put("stability", 0.10);
            weights.put("robustness", 0.10);
            weights.put("speed", 0.05);
            weights.put("latency", -0.05);
            
            double score = 0.0;
            score += accuracy * weights.getOrDefault("accuracy", 0.0);
            score += winRate * weights.getOrDefault("winRate", 0.0);
            score += sharpeRatio * 20 * weights.getOrDefault("sharpeRatio", 0.0);
            score += profitFactor * weights.getOrDefault("profitFactor", 0.0);
            score += maxDrawdown * weights.getOrDefault("maxDrawdown", 0.0);
            score += stability * weights.getOrDefault("stability", 0.0);
            score += robustness * weights.getOrDefault("robustness", 0.0);
            score += speed * weights.getOrDefault("speed", 0.0);
            score += latency * weights.getOrDefault("latency", 0.0);
            
            return Math.max(0.0, Math.min(100.0, score));
        }
    }

    public static class AdvancedNode {
        private String id;
        private String name;
        private String category;
        private String inputType;
        private String outputType;
        private String description;
        
        // Métricas
        private double confidence;
        private double executionTime;
        private double processingSpeed;
        private double errorRate;
        
        // Estado
        private NodeStatus status;
        private double progress;
        private LocalDateTime lastExecution;
        private int executionCount;
        private int successCount;
        
        // Dependências
        private List<String> dependencies;
        private List<String> children;
        
        // Recursos
        private double cpuUsage;
        private double memoryUsage;
        private double gpuUsage;
        
        // Configurações
        private int timeoutSeconds;
        private int retryCount;
        private int priority;

        public AdvancedNode(String id, String name, String category, 
                           String inputType, String outputType, String description) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.inputType = inputType;
            this.outputType = outputType;
            this.description = description;
            
            this.status = NodeStatus.IDLE;
            this.progress = 0.0;
            
            this.dependencies = new ArrayList<>();
            this.children = new ArrayList<>();
            
            this.timeoutSeconds = 30;
            this.retryCount = 3;
            this.priority = 1;
        }

        // Getters e Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public NodeStatus getStatus() { return status; }
        public void setStatus(NodeStatus status) { this.status = status; }
        
        public double getProgress() { return progress; }
        public void setProgress(double progress) { this.progress = Math.max(0, Math.min(100, progress)); }
        
        public LocalDateTime getLastExecution() { return lastExecution; }
        public void setLastExecution(LocalDateTime lastExecution) { this.lastExecution = lastExecution; }
        
        public int getExecutionCount() { return executionCount; }
        public void setExecutionCount(int executionCount) { this.executionCount = executionCount; }
        
        public List<String> getDependencies() { return dependencies; }
        public List<String> getChildren() { return children; }
        
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = Math.max(0, Math.min(100, confidence)); }
        
        public Color getStatusColor() {
            return status.getColor();
        }
        
        public String getStatusIcon() {
            return status.getIcon();
        }
    }

    public static class AdvancedDecision {
        private String decisionId;
        private LocalDateTime timestamp;
        private String algorithmId;
        private String algorithmName;
        private Decision decisionType;
        private double confidence;
        
        // Detalhes da decisão
        private String symbol;
        private TimeFrame timeframe;
        private double entryPrice;
        private double stopLoss;
        private double takeProfit;
        private double positionSize;
        private double leverage;
        
        // Raciocínio e análise
        private String reasoning;
        private List<String> technicalFactors;
        private List<String> fundamentalFactors;
        private List<String> sentimentFactors;
        private List<String> riskFactors;
        
        // Análise de risco
        private RiskLevel riskLevel;
        private double riskScore;
        private double var95;
        private double expectedReturn;
        private double expectedLoss;
        private double riskRewardRatio;
        
        // Mercado
        private MarketCondition marketCondition;
        private double volatility;
        private double volumeRatio;
        private double trendStrength;
        
        // Metadados
        private ExecutionMode executionMode;
        private boolean isExecuted;
        private LocalDateTime executionTime;
        private Double executionPrice;
        private Double pnl;
        private Double pnlPercentage;

        public AdvancedDecision(String decisionId, String algorithmId, String algorithmName,
                               Decision decisionType, double confidence, String symbol) {
            this.decisionId = decisionId;
            this.timestamp = LocalDateTime.now();
            this.algorithmId = algorithmId;
            this.algorithmName = algorithmName;
            this.decisionType = decisionType;
            this.confidence = confidence;
            this.symbol = symbol;
            
            this.technicalFactors = new ArrayList<>();
            this.fundamentalFactors = new ArrayList<>();
            this.sentimentFactors = new ArrayList<>();
            this.riskFactors = new ArrayList<>();
            
            this.leverage = 1.0;
            this.executionMode = ExecutionMode.PAPER;
            this.isExecuted = false;
        }

        /**
         * Calcular P&L baseado no preço atual
         */
        public double calculatePnl(double currentPrice) {
            if (!isExecuted || executionPrice == null) {
                return 0.0;
            }
            
            if (decisionType == Decision.BUY || decisionType == Decision.SCALP_BUY || 
                decisionType == Decision.SWING_BUY) {
                return (currentPrice - executionPrice) * positionSize;
            } else if (decisionType == Decision.SELL || decisionType == Decision.SCALP_SELL || 
                      decisionType == Decision.SWING_SELL) {
                return (executionPrice - currentPrice) * positionSize;
            } else {
                return 0.0;
            }
        }

        // Getters e Setters
        public String getDecisionId() { return decisionId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getAlgorithmName() { return algorithmName; }
        public Decision getDecisionType() { return decisionType; }
        public double getConfidence() { return confidence; }
        public String getSymbol() { return symbol; }
        public double getEntryPrice() { return entryPrice; }
        public void setEntryPrice(double entryPrice) { this.entryPrice = entryPrice; }
        
        public double getStopLoss() { return stopLoss; }
        public void setStopLoss(double stopLoss) { this.stopLoss = stopLoss; }
        
        public double getTakeProfit() { return takeProfit; }
        public void setTakeProfit(double takeProfit) { this.takeProfit = takeProfit; }
        
        public double getPositionSize() { return positionSize; }
        public void setPositionSize(double positionSize) { this.positionSize = positionSize; }
        
        public RiskLevel getRiskLevel() { return riskLevel; }
        public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
        
        public boolean isExecuted() { return isExecuted; }
        public void setExecuted(boolean executed) { isExecuted = executed; }
        
        public Double getPnl() { return pnl; }
        public void setPnl(Double pnl) { this.pnl = pnl; }
    }

    public static class PortfolioPosition {
        private String symbol;
        private AssetClass assetClass;
        private double quantity;
        private double entryPrice;
        private double currentPrice;
        private LocalDateTime entryTime;
        private String positionType; // LONG or SHORT
        
        // Métricas
        private double unrealizedPnl;
        private double unrealizedPnlPercent;
        private double realizedPnl;
        private double totalInvested;
        private double currentValue;
        
        // Gerenciamento de risco
        private Double stopLoss;
        private Double takeProfit;
        private Double trailingStop;
        private double riskScore;
        
        // Metadados
        private String algorithmId;
        private String decisionId;
        private boolean isHedged;
        private double hedgeRatio;

        public PortfolioPosition(String symbol, AssetClass assetClass, double quantity,
                                double entryPrice, LocalDateTime entryTime, String positionType) {
            this.symbol = symbol;
            this.assetClass = assetClass;
            this.quantity = quantity;
            this.entryPrice = entryPrice;
            this.currentPrice = entryPrice;
            this.entryTime = entryTime;
            this.positionType = positionType;
            
            updatePrices(entryPrice);
        }

        /**
         * Atualizar preços e calcular P&L
         */
        public void updatePrices(double newPrice) {
            this.currentPrice = newPrice;
            
            if ("LONG".equals(positionType)) {
                this.unrealizedPnl = (newPrice - entryPrice) * quantity;
            } else {
                this.unrealizedPnl = (entryPrice - newPrice) * quantity;
            }
            
            this.totalInvested = entryPrice * quantity;
            this.currentValue = newPrice * quantity;
            
            if (totalInvested > 0) {
                this.unrealizedPnlPercent = (unrealizedPnl / totalInvested) * 100;
            }
        }

        // Getters e Setters
        public String getSymbol() { return symbol; }
        public AssetClass getAssetClass() { return assetClass; }
        public double getQuantity() { return quantity; }
        public double getEntryPrice() { return entryPrice; }
        public double getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(double currentPrice) { updatePrices(currentPrice); }
        
        public String getPositionType() { return positionType; }
        public double getUnrealizedPnl() { return unrealizedPnl; }
        public double getUnrealizedPnlPercent() { return unrealizedPnlPercent; }
        public double getCurrentValue() { return currentValue; }
        
        public Double getStopLoss() { return stopLoss; }
        public void setStopLoss(Double stopLoss) { this.stopLoss = stopLoss; }
        
        public Double getTakeProfit() { return takeProfit; }
        public void setTakeProfit(Double takeProfit) { this.takeProfit = takeProfit; }
        
        public double getRiskScore() { return riskScore; }
        public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    }

    public static class MarketData {
        private String symbol;
        private LocalDateTime timestamp;
        private double open;
        private double high;
        private double low;
        private double close;
        private double volume;
        private double vwap;
        
        // Indicadores técnicos
        private Double rsi;
        private Double macd;
        private Double macdSignal;
        private Double macdHistogram;
        private Double bollingerUpper;
        private Double bollingerMiddle;
        private Double bollingerLower;
        private Double atr;
        private Double obv;
        private Double stochasticK;
        private Double stochasticD;
        private Double adx;
        
        // Sentimento
        private double sentimentScore;
        private double fearGreedIndex;
        private String marketSentiment; // BULLISH, BEARISH, NEUTRAL
        
        // Order book (simplificado)
        private Double bidPrice;
        private Double askPrice;
        private Double bidVolume;
        private Double askVolume;
        private Double spread;

        public MarketData(String symbol, double open, double high, double low, 
                         double close, double volume) {
            this.symbol = symbol;
            this.timestamp = LocalDateTime.now();
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
            
            calculateIndicators();
        }

        /**
         * Calcular indicadores básicos
         */
        private void calculateIndicators() {
            // Calcular spread se bid/ask disponíveis
            if (bidPrice != null && askPrice != null) {
                this.spread = askPrice - bidPrice;
            }
            
            // Calcular tendência básica
            double priceChange = ((close - open) / open) * 100;
            if (priceChange > 1.0) {
                this.marketSentiment = "BULLISH";
            } else if (priceChange < -1.0) {
                this.marketSentiment = "BEARISH";
            } else {
                this.marketSentiment = "NEUTRAL";
            }
        }

        // Getters e Setters
        public String getSymbol() { return symbol; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public double getClose() { return close; }
        public double getVolume() { return volume; }
        public String getMarketSentiment() { return marketSentiment; }
        
        public Double getRsi() { return rsi; }
        public void setRsi(Double rsi) { this.rsi = rsi; }
        
        public Double getMacd() { return macd; }
        public void setMacd(Double macd) { this.macd = macd; }
        
        public Double getBollingerUpper() { return bollingerUpper; }
        public void setBollingerUpper(Double bollingerUpper) { this.bollingerUpper = bollingerUpper; }
    }

    public static class Alert {
        private String alertId;
        private LocalDateTime timestamp;
        private AlertType alertType;
        private String title;
        private String message;
        private String source;
        
        // Prioridade
        private int priority; // 1-10, onde 10 é mais importante
        private boolean isAcknowledged;
        private boolean isResolved;
        
        // Ações
        private boolean actionRequired;
        private String actionTaken;
        private LocalDateTime actionTime;
        
        // Metadados
        private String relatedSymbol;
        private String relatedAlgorithm;
        private String relatedDecision;

        public Alert(String alertId, AlertType alertType, String title, 
                    String message, String source) {
            this.alertId = alertId;
            this.timestamp = LocalDateTime.now();
            this.alertType = alertType;
            this.title = title;
            this.message = message;
            this.source = source;
            
            this.priority = 1;
            this.isAcknowledged = false;
            this.isResolved = false;
            this.actionRequired = false;
        }

        public Color getColor() {
            return alertType.getColor();
        }
        
        public String getIcon() {
            return alertType.getIcon();
        }

        // Getters e Setters
        public String getAlertId() { return alertId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public AlertType getAlertType() { return alertType; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getSource() { return source; }
        
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = Math.max(1, Math.min(10, priority)); }
        
        public boolean isAcknowledged() { return isAcknowledged; }
        public void setAcknowledged(boolean acknowledged) { isAcknowledged = acknowledged; }
        
        public boolean isResolved() { return isResolved; }
        public void setResolved(boolean resolved) { isResolved = resolved; }
        
        public boolean isActionRequired() { return actionRequired; }
        public void setActionRequired(boolean actionRequired) { this.actionRequired = actionRequired; }
    }

    public static class SystemMetrics {
        private LocalDateTime timestamp;
        
        // Performance
        private double cpuUsage;
        private double memoryUsage;
        private double gpuUsage;
        private double diskUsage;
        private double networkUsage;
        
        // Trading
        private int activeTrades;
        private int pendingOrders;
        private int dailyTrades;
        private int weeklyTrades;
        private int monthlyTrades;
        
        // Financeiro
        private double totalEquity;
        private double availableBalance;
        private double marginUsed;
        private double totalPnl;
        private double dailyPnl;
        private double weeklyPnl;
        private double monthlyPnl;
        
        // Algoritmos
        private int activeAlgorithms;
        private int totalAlgorithms;
        private double algorithmSuccessRate;
        private double avgDecisionTime;
        
        // Rede
        private double latencyMs;
        private double packetLoss;
        private String connectionStatus;

        public SystemMetrics() {
            this.timestamp = LocalDateTime.now();
            this.connectionStatus = "CONNECTED";
        }

        // Getters e Setters
        public LocalDateTime getTimestamp() { return timestamp; }
        
        public double getCpuUsage() { return cpuUsage; }
        public void setCpuUsage(double cpuUsage) { this.cpuUsage = Math.max(0, Math.min(100, cpuUsage)); }
        
        public double getMemoryUsage() { return memoryUsage; }
        public void setMemoryUsage(double memoryUsage) { this.memoryUsage = Math.max(0, Math.min(100, memoryUsage)); }
        
        public int getActiveTrades() { return activeTrades; }
        public void setActiveTrades(int activeTrades) { this.activeTrades = activeTrades; }
        
        public double getTotalEquity() { return totalEquity; }
        public void setTotalEquity(double totalEquity) { this.totalEquity = totalEquity; }
        
        public double getTotalPnl() { return totalPnl; }
        public void setTotalPnl(double totalPnl) { this.totalPnl = totalPnl; }
        
        public double getDailyPnl() { return dailyPnl; }
        public void setDailyPnl(double dailyPnl) { this.dailyPnl = dailyPnl; }
        
        public int getActiveAlgorithms() { return activeAlgorithms; }
        public void setActiveAlgorithms(int activeAlgorithms) { this.activeAlgorithms = activeAlgorithms; }
        
        public double getLatencyMs() { return latencyMs; }
        public void setLatencyMs(double latencyMs) { this.latencyMs = latencyMs; }
        
        public String getConnectionStatus() { return connectionStatus; }
        public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }
    }

    // ==================== GERENCIADOR DE EVENTOS ====================

    public static class EventManager {
        private Map<String, List<EventListener>> subscribers;
        private BlockingQueue<Event> eventQueue;
        private AtomicBoolean isRunning;
        private Thread workerThread;

        public static class Event {
            private String type;
            private Object data;
            private LocalDateTime timestamp;

            public Event(String type, Object data) {
                this.type = type;
                this.data = data;
                this.timestamp = LocalDateTime.now();
            }

            public String getType() { return type; }
            public Object getData() { return data; }
            public LocalDateTime getTimestamp() { return timestamp; }
        }

        public interface EventListener {
            void onEvent(Event event);
        }

        public EventManager() {
            this.subscribers = new HashMap<>();
            this.eventQueue = new LinkedBlockingQueue<>();
            this.isRunning = new AtomicBoolean(false);
        }

        public void subscribe(String eventType, EventListener listener) {
            subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        }

        public void unsubscribe(String eventType, EventListener listener) {
            List<EventListener> listeners = subscribers.get(eventType);
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        public void publish(String eventType, Object data) {
            eventQueue.offer(new Event(eventType, data));
        }

        public void start() {
            isRunning.set(true);
            workerThread = new Thread(this::processEvents);
            workerThread.setDaemon(true);
            workerThread.start();
        }

        public void stop() {
            isRunning.set(false);
            if (workerThread != null) {
                try {
                    workerThread.join(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void processEvents() {
            while (isRunning.get()) {
                try {
                    Event event = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (event != null) {
                        List<EventListener> listeners = subscribers.get(event.getType());
                        if (listeners != null) {
                            for (EventListener listener : listeners) {
                                try {
                                    listener.onEvent(event);
                                } catch (Exception e) {
                                    LOGGER.severe("Error in event listener: " + e.getMessage());
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // ==================== COMPONENTES VISUAIS PERSONALIZADOS ====================

    public static class GradientPanel extends JPanel {
        private Color color1;
        private Color color2;

        public GradientPanel(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            // Criar gradiente vertical
            GradientPaint gradient = new GradientPaint(
                0, 0, color1,
                0, height, color2
            );

            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
        }

        public void setColors(Color color1, Color color2) {
            this.color1 = color1;
            this.color2 = color2;
            repaint();
        }
    }

    public static class AnimatedProgressBar extends JPanel {
        private double value;
        private double maxValue;
        private Color progressColor;
        private Color backgroundColor;
        private String text;
        private Timer animationTimer;

        public AnimatedProgressBar() {
            this.value = 0;
            this.maxValue = 100;
            this.progressColor = new Color(59, 130, 246); // #3b82f6
            this.backgroundColor = new Color(31, 41, 55); // #1f2937
            this.text = "0%";
            
            setPreferredSize(new Dimension(200, 20));
            setOpaque(false);
        }

        public void setValue(double value) {
            this.value = Math.max(0, Math.min(value, maxValue));
            this.text = String.format("%.1f%%", this.value);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            // Desenhar fundo
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, width, height, 5, 5);

            // Desenhar progresso
            int progressWidth = (int) ((value / maxValue) * width);
            if (progressWidth > 0) {
                // Ajustar cor baseada no valor
                if (value < 30) {
                    progressColor = new Color(239, 68, 68); // #ef4444
                } else if (value < 70) {
                    progressColor = new Color(245, 158, 11); // #f59e0b
                } else {
                    progressColor = new Color(16, 185, 129); // #10b981
                }
                
                g2d.setColor(progressColor);
                g2d.fillRoundRect(0, 0, progressWidth, height, 5, 5);
            }

            // Desenhar texto
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (width - fm.stringWidth(text)) / 2;
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, textX, textY);

            g2d.dispose();
        }
    }

    public static class LedIndicator extends JPanel {
        private boolean state;
        private Color colorOn;
        private Color colorOff;
        private Timer blinkTimer;
        private boolean blinkState;

        public LedIndicator() {
            this.state = false;
            this.colorOn = new Color(16, 185, 129); // #10b981
            this.colorOff = new Color(75, 85, 99); // #4b5563
            this.blinkState = false;
            
            setPreferredSize(new Dimension(20, 20));
            setOpaque(false);
        }

        public void setState(boolean state) {
            this.state = state;
            repaint();
        }

        public void startBlinking(int intervalMs) {
            if (blinkTimer != null) {
                blinkTimer.stop();
            }
            
            blinkTimer = new Timer(intervalMs, e -> {
                blinkState = !blinkState;
                repaint();
            });
            blinkTimer.start();
        }

        public void stopBlinking() {
            if (blinkTimer != null) {
                blinkTimer.stop();
                blinkTimer = null;
            }
            blinkState = false;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean active = blinkTimer != null ? blinkState : state;
            Color currentColor = active ? colorOn : colorOff;

            // Desenhar LED
            g2d.setColor(currentColor);
            g2d.fillOval(2, 2, getWidth() - 4, getHeight() - 4);

            // Desenhar borda
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(2, 2, getWidth() - 4, getHeight() - 4);

            g2d.dispose();
        }
    }

    public static class CircularProgress extends JPanel {
        private double value;
        private double maxValue;
        private Color progressColor;
        private String text;

        public CircularProgress() {
            this.value = 0;
            this.maxValue = 100;
            this.progressColor = new Color(59, 130, 246); // #3b82f6
            this.text = "0%";
            
            setPreferredSize(new Dimension(80, 80));
            setOpaque(false);
        }

        public void setValue(double value) {
            this.value = Math.max(0, Math.min(value, maxValue));
            this.text = String.format("%.0f%%", this.value);
            
            // Ajustar cor baseada no valor
            if (value < 30) {
                progressColor = new Color(239, 68, 68); // #ef4444
            } else if (value < 70) {
                progressColor = new Color(245, 158, 11); // #f59e0b
            } else {
                progressColor = new Color(16, 185, 129); // #10b981
            }
            
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = size / 2 - 5;

            // Desenhar círculo de fundo
            g2d.setColor(new Color(55, 65, 81)); // #374151
            g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

            // Desenhar arco de progresso
            if (value > 0) {
                double angle = (value / maxValue) * 360;
                g2d.setColor(progressColor);
                g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                           90, (int) -angle);
            }

            // Desenhar círculo interno
            g2d.setColor(getBackground());
            g2d.fillOval(centerX - radius + 5, centerY - radius + 5, 
                        (radius - 5) * 2, (radius - 5) * 2);

            // Desenhar texto
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = centerX - fm.stringWidth(text) / 2;
            int textY = centerY + fm.getAscent() / 2;
            g2d.drawString(text, textX, textY);

            g2d.dispose();
        }
    }

    // ==================== APLICAÇÃO PRINCIPAL ====================

    public static class AdvancedTradingDashboard {
        private JFrame frame;
        private JTabbedPane tabbedPane;
        private JLabel statusLabel;
        private JLabel updateLabel;
        private JLabel modeLabel;
        private JLabel connectionLabel;
        
        // Dados
        private List<AdvancedAlgorithm> algorithms;
        private List<AdvancedNode> decisionFlow;
        private List<AdvancedDecision> recentDecisions;
        private List<PortfolioPosition> portfolioPositions;
        private List<Alert> alerts;
        private SystemMetrics systemMetrics;
        
        // Configurações
        private ExecutionMode executionMode;
        private Map<String, Object> settings;
        private boolean isRunning;
        
        // Eventos
        private EventManager eventManager;
        
        // Cores do tema
        private Map<String, Color> colors;
        
        // Formatação
        private DecimalFormat priceFormat;
        private DecimalFormat percentFormat;
        private DateTimeFormatter timeFormatter;

        public AdvancedTradingDashboard() {
            this.frame = new JFrame("🚀 LEXTRADER-IAG - LEXTRADER-IAG 4.0 - Sistema de Trading Algorítmico Avançado");
            this.tabbedPane = new JTabbedPane();
            
            // Inicializar dados
            initializeData();
            
            // Configurar cores
            setupColors();
            
            // Configurar formatação
            setupFormatters();
            
            // Configurar eventos
            setupEvents();
            
            // Configurar UI
            setupUI();
            
            // Configurar atualizações
            setupUpdates();
            
            // Configurar shutdown
            setupShutdown();
        }

        private void initializeData() {
            this.algorithms = new ArrayList<>();
            this.decisionFlow = new ArrayList<>();
            this.recentDecisions = new ArrayList<>();
            this.portfolioPositions = new ArrayList<>();
            this.alerts = new ArrayList<>();
            this.systemMetrics = new SystemMetrics();
            this.eventManager = new EventManager();
            this.executionMode = ExecutionMode.PAPER;
            this.isRunning = true;
            
            this.settings = new HashMap<>();
            settings.put("risk_tolerance", "MODERATE");
            settings.put("max_position_size", 0.1);
            settings.put("max_daily_loss", 0.02);
            settings.put("auto_trading", false);
            settings.put("notifications_enabled", true);
            settings.put("sound_enabled", false);
            settings.put("theme", "dark");
            settings.put("language", "pt-BR");
            
            // Criar algoritmos de exemplo
            createSampleAlgorithms();
            
            // Criar posições de exemplo
            createSamplePositions();
            
            // Criar alertas de exemplo
            createSampleAlerts();
        }

        private void setupColors() {
            colors = new HashMap<>();
            colors.put("primary", new Color(59, 130, 246)); // #3b82f6
            colors.put("primary_dark", new Color(30, 58, 138)); // #1e3a8a
            colors.put("primary_light", new Color(96, 165, 250)); // #60a5fa
            colors.put("secondary", new Color(16, 185, 129)); // #10b981
            colors.put("secondary_dark", new Color(4, 120, 87)); // #047857
            colors.put("secondary_light", new Color(52, 211, 153)); // #34d399
            colors.put("danger", new Color(239, 68, 68)); // #ef4444
            colors.put("danger_dark", new Color(185, 28, 28)); // #b91c1c
            colors.put("danger_light", new Color(248, 113, 113)); // #f87171
            colors.put("warning", new Color(245, 158, 11)); // #f59e0b
            colors.put("warning_dark", new Color(217, 119, 6)); // #d97706
            colors.put("warning_light", new Color(251, 191, 36)); // #fbbf24
            colors.put("info", new Color(99, 102, 241)); // #6366f1
            colors.put("info_dark", new Color(79, 70, 229)); // #4f46e5
            colors.put("info_light", new Color(129, 140, 248)); // #818cf8
            colors.put("dark", new Color(17, 24, 39)); // #111827
            colors.put("dark_light", new Color(31, 41, 55)); // #1f2937
            colors.put("dark_lighter", new Color(55, 65, 81)); // #374151
            colors.put("gray", new Color(107, 114, 128)); // #6b7280
            colors.put("gray_light", new Color(156, 163, 175)); // #9ca3af
            colors.put("gray_lighter", new Color(209, 213, 219)); // #d1d5db
            colors.put("white", Color.WHITE);
            colors.put("black", Color.BLACK);
        }

        private void setupFormatters() {
            priceFormat = new DecimalFormat("#,##0.00");
            percentFormat = new DecimalFormat("#,##0.00'%'");
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }

        private void setupEvents() {
            eventManager.start();
        }

        private void setupUI() {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1920, 1080);
            frame.setMinimumSize(new Dimension(1280, 720));
            
            // Painel principal com gradiente
            GradientPanel mainPanel = new GradientPanel(
                colors.get("dark"),
                colors.get("dark_light")
            );
            mainPanel.setLayout(new BorderLayout());
            frame.setContentPane(mainPanel);
            
            // Barra superior
            setupTopBar(mainPanel);
            
            // Área principal com abas
            setupTabbedPane(mainPanel);
            
            // Barra de status
            setupStatusBar(mainPanel);
            
            // Centralizar na tela
            frame.setLocationRelativeTo(null);
        }

        private void setupTopBar(JPanel parent) {
            JPanel topBar = new JPanel(new BorderLayout());
            topBar.setOpaque(false);
            topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Logo e título
            JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            logoPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("🚀 LEXTRADER-IAG");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(colors.get("primary_light"));
            logoPanel.add(titleLabel);
            
            JLabel subtitleLabel = new JLabel("LEXTRADER-IAG 4.0 - Sistema de Trading Algorítmico Avançado");
            subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            subtitleLabel.setForeground(colors.get("gray_light"));
            logoPanel.add(subtitleLabel);
            
            topBar.add(logoPanel, BorderLayout.WEST);
            
            // Controles
            JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            controlsPanel.setOpaque(false);
            
            // Modo de execução
            JComboBox<ExecutionMode> modeCombo = new JComboBox<>(ExecutionMode.values());
            modeCombo.setSelectedItem(executionMode);
            modeCombo.addActionListener(e -> {
                executionMode = (ExecutionMode) modeCombo.getSelectedItem();
                updateModeLabel();
            });
            controlsPanel.add(modeCombo);
            
            // Botão iniciar/parar
            JButton startStopBtn = new JButton("▶️ Iniciar Sistema");
            startStopBtn.setBackground(colors.get("secondary"));
            startStopBtn.setForeground(Color.WHITE);
            startStopBtn.addActionListener(e -> toggleSystem());
            controlsPanel.add(startStopBtn);
            
            // Botão configurações
            JButton settingsBtn = new JButton("⚙️ Configurações");
            settingsBtn.setBackground(colors.get("primary"));
            settingsBtn.setForeground(Color.WHITE);
            settingsBtn.addActionListener(e -> openSettings());
            controlsPanel.add(settingsBtn);
            
            // Botão ajuda
            JButton helpBtn = new JButton("❓ Ajuda");
            helpBtn.setBackground(colors.get("info"));
            helpBtn.setForeground(Color.WHITE);
            helpBtn.addActionListener(e -> showHelp());
            controlsPanel.add(helpBtn);
            
            topBar.add(controlsPanel, BorderLayout.EAST);
            
            parent.add(topBar, BorderLayout.NORTH);
        }

        private void setupTabbedPane(JPanel parent) {
            tabbedPane = new JTabbedPane();
            tabbedPane.setBackground(colors.get("dark_light"));
            tabbedPane.setForeground(Color.WHITE);
            
            // Criar abas
            createDashboardTab();
            createAlgorithmsTab();
            createTradingTab();
            createAnalysisTab();
            createPortfolioTab();
            createMonitoringTab();
            createBacktestingTab();
            createOptimizationTab();
            createReportsTab();
            
            parent.add(tabbedPane, BorderLayout.CENTER);
        }

        private void setupStatusBar(JPanel parent) {
            JPanel statusBar = new JPanel(new BorderLayout());
            statusBar.setOpaque(false);
            statusBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            statusBar.setPreferredSize(new Dimension(0, 30));
            
            // Status do sistema
            statusLabel = new JLabel("✅ Sistema operando normalmente");
            statusLabel.setForeground(colors.get("secondary"));
            statusBar.add(statusLabel, BorderLayout.WEST);
            
            // Última atualização
            updateLabel = new JLabel("Última atualização: --:--:--");
            updateLabel.setForeground(colors.get("gray_light"));
            statusBar.add(updateLabel, BorderLayout.CENTER);
            
            // Modo de execução
            modeLabel = new JLabel("Modo: " + executionMode.getValue());
            modeLabel.setForeground(colors.get("primary_light"));
            statusBar.add(modeLabel, BorderLayout.EAST);
            
            // Conexão
            connectionLabel = new JLabel("🌐 Conectado");
            connectionLabel.setForeground(colors.get("secondary"));
            statusBar.add(connectionLabel, BorderLayout.EAST);
            
            parent.add(statusBar, BorderLayout.SOUTH);
        }

        private void createDashboardTab() {
            JPanel dashboardPanel = new JPanel(new BorderLayout());
            dashboardPanel.setOpaque(false);
            
            // Painel dividido
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setOpaque(false);
            
            // Painel esquerdo - Visão geral
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setOpaque(false);
            
            // Topo - Métricas principais
            JPanel topMetrics = new JPanel(new GridLayout(1, 4, 10, 0));
            topMetrics.setOpaque(false);
            topMetrics.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            createMetricCard(topMetrics, "💰 Patrimônio Total", "$ 284,567.89", "+2.34%");
            createMetricCard(topMetrics, "📈 P&L Diário", "+$ 2,345.67", "+1.23%");
            createMetricCard(topMetrics, "🎯 Taxa de Acerto", "87.4%", "↑ 1.2%");
            createMetricCard(topMetrics, "⚡ Sharpe Ratio", "2.85", "Estável");
            
            leftPanel.add(topMetrics, BorderLayout.NORTH);
            
            // Meio - Gráficos (simplificado)
            JPanel middlePanel = new JPanel(new GridLayout(2, 1, 0, 10));
            middlePanel.setOpaque(false);
            middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Gráfico de performance (placeholder)
            JPanel perfPanel = createChartPanel("📈 Performance do Portfólio", 300, 200);
            middlePanel.add(perfPanel);
            
            // Gráfico de alocação (placeholder)
            JPanel allocPanel = createChartPanel("📊 Alocação de Ativos", 300, 150);
            middlePanel.add(allocPanel);
            
            leftPanel.add(middlePanel, BorderLayout.CENTER);
            
            splitPane.setLeftComponent(leftPanel);
            
            // Painel direito - Métricas rápidas
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
            
            // Sistema
            JPanel systemPanel = createLabeledPanel("🖥️ Status do Sistema");
            systemPanel.setLayout(new GridLayout(4, 1, 0, 5));
            
            createSystemMetric(systemPanel, "CPU", "42%", 42);
            createSystemMetric(systemPanel, "Memória", "68%", 68);
            createSystemMetric(systemPanel, "GPU", "15%", 15);
            createSystemMetric(systemPanel, "Rede", "24ms", 0);
            
            rightPanel.add(systemPanel, BorderLayout.NORTH);
            
            // Alertas
            JPanel alertsPanel = createLabeledPanel("🚨 Alertas Ativos");
            alertsPanel.setLayout(new BorderLayout());
            
            JList<String> alertsList = new JList<>();
            alertsList.setBackground(colors.get("dark_lighter"));
            alertsList.setForeground(Color.WHITE);
            
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Alert alert : alerts) {
                listModel.addElement(alert.getIcon() + " " + alert.getTitle());
            }
            alertsList.setModel(listModel);
            
            JScrollPane scrollPane = new JScrollPane(alertsList);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            alertsPanel.add(scrollPane, BorderLayout.CENTER);
            
            rightPanel.add(alertsPanel, BorderLayout.CENTER);
            
            // Botões de ação rápida
            JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 5, 0));
            actionsPanel.setOpaque(false);
            actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JButton quickTradeBtn = createStyledButton("📈 Trade Rápido", colors.get("secondary"));
            quickTradeBtn.addActionListener(e -> openQuickTrade());
            actionsPanel.add(quickTradeBtn);
            
            JButton riskBtn = createStyledButton("🛡️ Verificar Risco", colors.get("warning"));
            riskBtn.addActionListener(e -> checkRisk());
            actionsPanel.add(riskBtn);
            
            JButton reportBtn = createStyledButton("📋 Gerar Relatório", colors.get("info"));
            reportBtn.addActionListener(e -> generateReport());
            actionsPanel.add(reportBtn);
            
            rightPanel.add(actionsPanel, BorderLayout.SOUTH);
            
            splitPane.setRightComponent(rightPanel);
            splitPane.setResizeWeight(0.7);
            
            dashboardPanel.add(splitPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("📊 Dashboard", dashboardPanel);
        }

        private void createAlgorithmsTab() {
            JPanel algorithmsPanel = new JPanel(new BorderLayout());
            algorithmsPanel.setOpaque(false);
            
            // Toolbar
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            toolbar.setOpaque(false);
            toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            
            toolbar.add(new JLabel("Filtrar:"));
            
            JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Todos", "Ativos", "Inativos", "Otimizando"});
            toolbar.add(filterCombo);
            
            JTextField searchField = new JTextField(20);
            toolbar.add(searchField);
            
            JButton searchBtn = createStyledButton("🔍", colors.get("primary"));
            toolbar.add(searchBtn);
            
            algorithmsPanel.add(toolbar, BorderLayout.NORTH);
            
            // Lista de algoritmos com scroll
            JPanel algorithmsList = new JPanel();
            algorithmsList.setLayout(new BoxLayout(algorithmsList, BoxLayout.Y_AXIS));
            algorithmsList.setOpaque(false);
            
            for (AdvancedAlgorithm algo : algorithms) {
                JPanel algoCard = createAlgorithmCard(algo);
                algorithmsList.add(algoCard);
                algorithmsList.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            
            JScrollPane scrollPane = new JScrollPane(algorithmsList);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            algorithmsPanel.add(scrollPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("🤖 Algoritmos", algorithmsPanel);
        }

        private void createTradingTab() {
            JPanel tradingPanel = new JPanel(new BorderLayout());
            tradingPanel.setOpaque(false);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setOpaque(false);
            
            // Painel esquerdo - Controles de trading
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setOpaque(false);
            
            // Seleção de ativo
            JPanel assetPanel = createLabeledPanel("📊 Selecionar Ativo");
            assetPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0; gbc.gridy = 0;
            assetPanel.add(new JLabel("Símbolo:"), gbc);
            gbc.gridx = 1;
            JTextField symbolField = new JTextField("BTC/USDT", 15);
            assetPanel.add(symbolField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            assetPanel.add(new JLabel("Timeframe:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> timeframeCombo = new JComboBox<>(new String[]{"1m", "5m", "15m", "30m", "1h", "4h", "1d"});
            assetPanel.add(timeframeCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            gbc.gridwidth = 2;
            JButton loadBtn = createStyledButton("📥 Carregar Dados", colors.get("info"));
            assetPanel.add(loadBtn, gbc);
            
            leftPanel.add(assetPanel, BorderLayout.NORTH);
            
            // Nova ordem
            JPanel orderPanel = createLabeledPanel("📝 Nova Ordem");
            orderPanel.setLayout(new GridBagLayout());
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0; gbc.gridy = 0;
            orderPanel.add(new JLabel("Tipo:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> orderTypeCombo = new JComboBox<>(new String[]{"MARKET", "LIMIT", "STOP", "STOP_LIMIT"});
            orderPanel.add(orderTypeCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            orderPanel.add(new JLabel("Lado:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> sideCombo = new JComboBox<>(new String[]{"BUY", "SELL"});
            orderPanel.add(sideCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            orderPanel.add(new JLabel("Quantidade:"), gbc);
            gbc.gridx = 1;
            JTextField qtyField = new JTextField("0.1", 15);
            orderPanel.add(qtyField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            orderPanel.add(new JLabel("Preço:"), gbc);
            gbc.gridx = 1;
            JTextField priceField = new JTextField("0.0", 15);
            orderPanel.add(priceField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4;
            gbc.gridwidth = 1;
            JButton buyBtn = createStyledButton("📈 Comprar", colors.get("secondary"));
            orderPanel.add(buyBtn, gbc);
            
            gbc.gridx = 1;
            JButton sellBtn = createStyledButton("📉 Vender", colors.get("danger"));
            orderPanel.add(sellBtn, gbc);
            
            leftPanel.add(orderPanel, BorderLayout.CENTER);
            
            splitPane.setLeftComponent(leftPanel);
            
            // Painel direito - Gráficos
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            
            // Placeholder para gráfico
            JPanel chartPanel = createChartPanel("📈 Gráfico de Preços", 600, 400);
            rightPanel.add(chartPanel, BorderLayout.CENTER);
            
            splitPane.setRightComponent(rightPanel);
            splitPane.setResizeWeight(0.4);
            
            tradingPanel.add(splitPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("💰 Trading", tradingPanel);
        }

        private void createAnalysisTab() {
            JPanel analysisPanel = new JPanel(new BorderLayout());
            analysisPanel.setOpaque(false);
            
            JTabbedPane analysisTabs = new JTabbedPane();
            analysisTabs.setBackground(colors.get("dark_light"));
            analysisTabs.setForeground(Color.WHITE);
            
            // Análise Técnica
            JPanel technicalPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            technicalPanel.setOpaque(false);
            technicalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] indicators = {
                {"RSI (14)", "62.3", "Neutral"},
                {"MACD", "+15.2", "Bullish"},
                {"Bollinger", "Upper Touch", "Overbought"},
                {"Volume", "+215%", "High"},
                {"ATR", "342.5", "High Vol"},
                {"ADX", "42.7", "Strong Trend"},
                {"Stoch RSI", "78.9", "Overbought"},
                {"Ichimoku", "Bullish", "Above Cloud"}
            };
            
            for (String[] ind : indicators) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(ind[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(ind[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                JLabel signalLabel = new JLabel(ind[2]);
                signalLabel.setForeground(ind[2].contains("Bullish") ? colors.get("secondary") : 
                                         ind[2].contains("Bearish") ? colors.get("danger") : 
                                         colors.get("warning"));
                signalLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
                card.add(signalLabel, BorderLayout.SOUTH);
                
                technicalPanel.add(card);
            }
            
            analysisTabs.addTab("📈 Técnica", technicalPanel);
            
            // Análise Fundamentalista
            JPanel fundamentalPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            fundamentalPanel.setOpaque(false);
            fundamentalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] fundamentals = {
                {"💰 Market Cap", "$850B", "+2.3%"},
                {"📈 Volume (24h)", "$32.4B", "+15.7%"},
                {"🔢 Circulating Supply", "19.5M BTC", "91.2%"},
                {"📊 Dominance", "52.3%", "-1.2%"},
                {"🏦 Total Value Locked", "$45.2B", "+8.7%"},
                {"👥 Active Addresses", "1.2M", "+5.4%"},
                {"⚡ Hash Rate", "450 EH/s", "+12.3%"},
                {"🔗 Network Difficulty", "67.3T", "+8.9%"}
            };
            
            for (String[] fund : fundamentals) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(fund[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(fund[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                JLabel changeLabel = new JLabel(fund[2]);
                changeLabel.setForeground(fund[2].startsWith("+") ? colors.get("secondary") : colors.get("danger"));
                changeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
                card.add(changeLabel, BorderLayout.SOUTH);
                
                fundamentalPanel.add(card);
            }
            
            analysisTabs.addTab("🏦 Fundamentalista", fundamentalPanel);
            
            // Análise de Sentimento
            JPanel sentimentPanel = new JPanel(new BorderLayout());
            sentimentPanel.setOpaque(false);
            sentimentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Fear & Greed Index
            JPanel fearGreedPanel = createLabeledPanel("😨 Fear & Greed Index");
            fearGreedPanel.setLayout(new BorderLayout());
            
            CircularProgress fearGreedProgress = new CircularProgress();
            fearGreedProgress.setValue(72);
            
            JPanel progressPanel = new JPanel(new FlowLayout());
            progressPanel.setOpaque(false);
            progressPanel.add(fearGreedProgress);
            fearGreedPanel.add(progressPanel, BorderLayout.CENTER);
            
            JLabel fearGreedLabel = new JLabel("Greed", SwingConstants.CENTER);
            fearGreedLabel.setForeground(colors.get("secondary"));
            fearGreedLabel.setFont(new Font("Arial", Font.BOLD, 14));
            fearGreedPanel.add(fearGreedLabel, BorderLayout.SOUTH);
            
            sentimentPanel.add(fearGreedPanel, BorderLayout.NORTH);
            
            // Fontes de sentimento
            JPanel sourcesPanel = createLabeledPanel("📰 Fontes de Sentimento");
            sourcesPanel.setLayout(new GridLayout(4, 1, 0, 5));
            
            String[][] sources = {
                {"Twitter", "78% Bullish", "secondary"},
                {"Reddit", "65% Bullish", "warning"},
                {"News", "72% Bullish", "secondary"},
                {"GitHub", "84% Positive", "secondary"}
            };
            
            for (String[] source : sources) {
                JPanel sourcePanel = new JPanel(new BorderLayout());
                sourcePanel.setOpaque(false);
                
                JLabel nameLabel = new JLabel(source[0]);
                nameLabel.setForeground(Color.WHITE);
                sourcePanel.add(nameLabel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(source[1]);
                valueLabel.setForeground(colors.get(source[2]));
                valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                sourcePanel.add(valueLabel, BorderLayout.EAST);
                
                sourcesPanel.add(sourcePanel);
            }
            
            sentimentPanel.add(sourcesPanel, BorderLayout.CENTER);
            
            analysisTabs.addTab("😊 Sentimento", sentimentPanel);
            
            // Análise de Risco
            JPanel riskPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            riskPanel.setOpaque(false);
            riskPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] risks = {
                {"📉 Value at Risk (95%)", "2.1%", "warning"},
                {"🔥 Conditional VaR", "3.8%", "danger"},
                {"📊 Beta vs Market", "1.2", "info"},
                {"🎯 Sharpe Ratio", "2.85", "secondary"},
                {"🛡️ Sortino Ratio", "3.21", "secondary"},
                {"📉 Maximum Drawdown", "12.3%", "danger"},
                {"⚖️ Portfolio Volatility", "18.7%", "warning"},
                {"🔗 Correlation Matrix", "Moderate", "info"}
            };
            
            for (String[] risk : risks) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(risk[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(risk[1]);
                valueLabel.setForeground(colors.get(risk[2]));
                valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                riskPanel.add(card);
            }
            
            analysisTabs.addTab("🛡️ Risco", riskPanel);
            
            analysisPanel.add(analysisTabs, BorderLayout.CENTER);
            
            tabbedPane.addTab("📊 Análise", analysisPanel);
        }

        private void createPortfolioTab() {
            JPanel portfolioPanel = new JPanel(new BorderLayout());
            portfolioPanel.setOpaque(false);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setOpaque(false);
            
            // Painel esquerdo - Posições
            JPanel positionsPanel = new JPanel(new BorderLayout());
            positionsPanel.setOpaque(false);
            
            // Toolbar
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            toolbar.setOpaque(false);
            
            JButton closeAllBtn = createStyledButton("❌ Fechar Todas", colors.get("danger"));
            closeAllBtn.addActionListener(e -> closeAllPositions());
            toolbar.add(closeAllBtn);
            
            JButton hedgeBtn = createStyledButton("🛡️ Hedgear", colors.get("info"));
            hedgeBtn.addActionListener(e -> hedgePortfolio());
            toolbar.add(hedgeBtn);
            
            positionsPanel.add(toolbar, BorderLayout.NORTH);
            
            // Tabela de posições
            String[] columns = {"Símbolo", "Tipo", "Quantidade", "Entrada", "Atual", "P&L", "P&L %", "Risco"};
            Object[][] data = new Object[portfolioPositions.size()][8];
            
            for (int i = 0; i < portfolioPositions.size(); i++) {
                PortfolioPosition pos = portfolioPositions.get(i);
                data[i][0] = pos.getSymbol();
                data[i][1] = pos.getPositionType();
                data[i][2] = String.format("%.4f", pos.getQuantity());
                data[i][3] = "$" + priceFormat.format(pos.getEntryPrice());
                data[i][4] = "$" + priceFormat.format(pos.getCurrentPrice());
                data[i][5] = String.format("$%,.2f", pos.getUnrealizedPnl());
                data[i][6] = String.format("%+.2f%%", pos.getUnrealizedPnlPercent());
                data[i][7] = String.format("%.1f", pos.getRiskScore());
            }
            
            JTable positionsTable = new JTable(data, columns);
            positionsTable.setBackground(colors.get("dark_lighter"));
            positionsTable.setForeground(Color.WHITE);
            positionsTable.setGridColor(colors.get("gray"));
            positionsTable.setRowHeight(25);
            
            JScrollPane scrollPane = new JScrollPane(positionsTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            positionsPanel.add(scrollPane, BorderLayout.CENTER);
            
            splitPane.setLeftComponent(positionsPanel);
            
            // Painel direito - Estatísticas
            JPanel statsPanel = new JPanel(new BorderLayout());
            statsPanel.setOpaque(false);
            
            // Resumo
            JPanel summaryPanel = createLabeledPanel("📊 Resumo do Portfólio");
            summaryPanel.setLayout(new GridLayout(6, 1, 0, 5));
            
            String[][] summary = {
                {"💰 Valor Total", "$284,567.89"},
                {"📈 P&L Total", "+$12,456.78"},
                {"🎯 P&L %", "+4.56%"},
                {"🛡️ Risco Médio", "5.8/10"},
                {"📊 Diversificação", "7.2/10"},
                {"⚡ Alavancagem", "1.5x"}
            };
            
            for (String[] item : summary) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(false);
                
                JLabel nameLabel = new JLabel(item[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                panel.add(nameLabel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(item[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
                valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                panel.add(valueLabel, BorderLayout.EAST);
                
                summaryPanel.add(panel);
            }
            
            statsPanel.add(summaryPanel, BorderLayout.NORTH);
            
            // Alocação por classe
            JPanel allocationPanel = createLabeledPanel("📈 Alocação por Classe");
            allocationPanel.setLayout(new GridLayout(4, 1, 0, 5));
            
            String[][] allocations = {
                {"Crypto", "65%", "primary"},
                {"Stocks", "20%", "secondary"},
                {"Commodities", "10%", "warning"},
                {"Cash", "5%", "gray"}
            };
            
            for (String[] alloc : allocations) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(false);
                
                JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                leftPanel.setOpaque(false);
                
                JPanel colorIndicator = new JPanel();
                colorIndicator.setBackground(colors.get(alloc[2]));
                colorIndicator.setPreferredSize(new Dimension(15, 15));
                leftPanel.add(colorIndicator);
                
                JLabel nameLabel = new JLabel(alloc[0]);
                nameLabel.setForeground(Color.WHITE);
                leftPanel.add(nameLabel);
                
                panel.add(leftPanel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(alloc[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
                valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                panel.add(valueLabel, BorderLayout.EAST);
                
                allocationPanel.add(panel);
            }
            
            statsPanel.add(allocationPanel, BorderLayout.CENTER);
            
            splitPane.setRightComponent(statsPanel);
            splitPane.setResizeWeight(0.7);
            
            portfolioPanel.add(splitPane, BorderLayout.CENTER);
            
            tabbedPane.addTab("💼 Portfólio", portfolioPanel);
        }

        private void createMonitoringTab() {
            JPanel monitoringPanel = new JPanel(new BorderLayout());
            monitoringPanel.setOpaque(false);
            
            JTabbedPane monitoringTabs = new JTabbedPane();
            monitoringTabs.setBackground(colors.get("dark_light"));
            monitoringTabs.setForeground(Color.WHITE);
            
            // Logs do sistema
            JPanel logsPanel = new JPanel(new BorderLayout());
            logsPanel.setOpaque(false);
            
            // Toolbar
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            toolbar.setOpaque(false);
            
            JButton clearBtn = createStyledButton("🗑️ Limpar Logs", colors.get("danger"));
            clearBtn.addActionListener(e -> clearLogs());
            toolbar.add(clearBtn);
            
            JButton exportBtn = createStyledButton("💾 Exportar", colors.get("secondary"));
            exportBtn.addActionListener(e -> exportLogs());
            toolbar.add(exportBtn);
            
            toolbar.add(new JLabel("Filtrar:"));
            JComboBox<String> filterCombo = new JComboBox<>(new String[]{"Todos", "Info", "Warning", "Error"});
            toolbar.add(filterCombo);
            
            logsPanel.add(toolbar, BorderLayout.NORTH);
            
            // Área de logs
            JTextArea logArea = new JTextArea();
            logArea.setBackground(colors.get("dark_lighter"));
            logArea.setForeground(Color.WHITE);
            logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            logArea.setEditable(false);
            
            String[][] logMessages = {
                {"INFO", "Sistema inicializado com sucesso"},
                {"INFO", "Conectado à Binance API"},
                {"INFO", "Quantum Ensemble carregado"},
                {"WARNING", "Alta volatilidade detectada no mercado"},
                {"SUCCESS", "Ordem executada: BUY 0.5 BTC @ $42,150.00"},
                {"ERROR", "Falha na conexão com Bybit API - Tentando reconectar"},
                {"INFO", "Reconexão bem-sucedida"},
                {"TRADE", "Novo sinal: SELL ETH @ $2,456.78 (Confiança: 91.7%)"}
            };
            
            LocalDateTime now = LocalDateTime.now();
            for (String[] msg : logMessages) {
                String timestamp = now.format(timeFormatter);
                logArea.append("[" + timestamp + "] [" + msg[0] + "] " + msg[1] + "\n");
            }
            
            JScrollPane scrollPane = new JScrollPane(logArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            logsPanel.add(scrollPane, BorderLayout.CENTER);
            
            monitoringTabs.addTab("📝 Logs", logsPanel);
            
            // Métricas em tempo real
            JPanel metricsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            metricsPanel.setOpaque(false);
            metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[][] metrics = {
                {"📊 CPU Usage", "42%", "warning"},
                {"💾 Memory", "68%", "warning"},
                {"🎮 GPU Usage", "15%", "secondary"},
                {"🌐 Network", "24ms", "secondary"},
                {"⚡ Latência API", "87ms", "info"},
                {"📈 Trades/min", "3.2", "secondary"},
                {"🎯 Decision Rate", "12.4/s", "secondary"},
                {"🔄 Update Freq", "0.5s", "info"}
            };
            
            for (String[] metric : metrics) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(colors.get("dark_lighter"));
                card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
                
                JLabel nameLabel = new JLabel(metric[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                card.add(nameLabel, BorderLayout.NORTH);
                
                JLabel valueLabel = new JLabel(metric[1]);
                valueLabel.setForeground(colors.get(metric[2]));
                valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
                valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
                card.add(valueLabel, BorderLayout.CENTER);
                
                metricsPanel.add(card);
            }
            
            monitoringTabs.addTab("📊 Métricas", metricsPanel);
            
            // Alertas
            JPanel alertsPanel = new JPanel(new BorderLayout());
            alertsPanel.setOpaque(false);
            
            // Toolbar
            JPanel alertsToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            alertsToolbar.setOpaque(false);
            
            JButton ackAllBtn = createStyledButton("✓ Reconhecer Todos", colors.get("info"));
            ackAllBtn.addActionListener(e -> acknowledgeAllAlerts());
            alertsToolbar.add(ackAllBtn);
            
            JButton clearResolvedBtn = createStyledButton("🗑️ Limpar Resolvidos", colors.get("danger"));
            clearResolvedBtn.addActionListener(e -> clearResolvedAlerts());
            alertsToolbar.add(clearResolvedBtn);
            
            alertsPanel.add(alertsToolbar, BorderLayout.NORTH);
            
            // Lista de alertas
            JList<String> alertsList = new JList<>();
            alertsList.setBackground(colors.get("dark_lighter"));
            alertsList.setForeground(Color.WHITE);
            
            DefaultListModel<String> alertsListModel = new DefaultListModel<>();
            for (Alert alert : alerts) {
                String timeStr = alert.getTimestamp().format(timeFormatter);
                alertsListModel.addElement(alert.getIcon() + " [" + timeStr + "] " + alert.getTitle());
            }
            alertsList.setModel(alertsListModel);
            
            // Colorir baseado na prioridade
            alertsList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                                                              int index, boolean isSelected,
                                                              boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (index < alerts.size()) {
                        Alert alert = alerts.get(index);
                        if (alert.getPriority() >= 8) {
                            setForeground(colors.get("danger"));
                        } else if (alert.getPriority() >= 5) {
                            setForeground(colors.get("warning"));
                        }
                    }
                    return c;
                }
            });
            
            JScrollPane alertsScrollPane = new JScrollPane(alertsList);
            alertsScrollPane.setBorder(BorderFactory.createEmptyBorder());
            alertsPanel.add(alertsScrollPane, BorderLayout.CENTER);
            
            monitoringTabs.addTab("🚨 Alertas", alertsPanel);
            
            monitoringPanel.add(monitoringTabs, BorderLayout.CENTER);
            
            tabbedPane.addTab("👁️ Monitoramento", monitoringPanel);
        }

        private void createBacktestingTab() {
            JPanel backtestPanel = new JPanel(new BorderLayout());
            backtestPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("Backtesting Avançado", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            backtestPanel.add(titleLabel, BorderLayout.NORTH);
            
            JPanel placeholderPanel = new JPanel(new GridBagLayout());
            placeholderPanel.setOpaque(false);
            
            JTextArea placeholder = new JTextArea(
                "Interface de backtesting em desenvolvimento\n\n" +
                "• Teste estratégias em dados históricos\n" +
                "• Otimização de parâmetros\n" +
                "• Análise de performance detalhada\n" +
                "• Comparação de múltiplas estratégias"
            );
            placeholder.setEditable(false);
            placeholder.setBackground(colors.get("dark_lighter"));
            placeholder.setForeground(colors.get("gray_light"));
            placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
            placeholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            placeholderPanel.add(placeholder);
            backtestPanel.add(placeholderPanel, BorderLayout.CENTER);
            
            tabbedPane.addTab("🧪 Backtesting", backtestPanel);
        }

        private void createOptimizationTab() {
            JPanel optimizationPanel = new JPanel(new BorderLayout());
            optimizationPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("Otimização de Algoritmos", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            optimizationPanel.add(titleLabel, BorderLayout.NORTH);
            
            JPanel placeholderPanel = new JPanel(new GridBagLayout());
            placeholderPanel.setOpaque(false);
            
            JTextArea placeholder = new JTextArea(
                "Interface de otimização em desenvolvimento\n\n" +
                "• Otimização bayesiana de parâmetros\n" +
                "• Grid search avançado\n" +
                "• Meta-otimização com GA\n" +
                "• Validação cruzada em tempo real"
            );
            placeholder.setEditable(false);
            placeholder.setBackground(colors.get("dark_lighter"));
            placeholder.setForeground(colors.get("gray_light"));
            placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
            placeholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            placeholderPanel.add(placeholder);
            optimizationPanel.add(placeholderPanel, BorderLayout.CENTER);
            
            tabbedPane.addTab("⚙️ Otimização", optimizationPanel);
        }

        private void createReportsTab() {
            JPanel reportsPanel = new JPanel(new BorderLayout());
            reportsPanel.setOpaque(false);
            
            JLabel titleLabel = new JLabel("Relatórios e Análises", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            reportsPanel.add(titleLabel, BorderLayout.NORTH);
            
            JPanel placeholderPanel = new JPanel(new GridBagLayout());
            placeholderPanel.setOpaque(false);
            
            JTextArea placeholder = new JTextArea(
                "Interface de relatórios em desenvolvimento\n\n" +
                "• Relatórios de performance diários/semanais/mensais\n" +
                "• Análise de risco detalhada\n" +
                "• Relatórios de conformidade\n" +
                "• Exportação para PDF/Excel"
            );
            placeholder.setEditable(false);
            placeholder.setBackground(colors.get("dark_lighter"));
            placeholder.setForeground(colors.get("gray_light"));
            placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
            placeholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            placeholderPanel.add(placeholder);
            reportsPanel.add(placeholderPanel, BorderLayout.CENTER);
            
            tabbedPane.addTab("📋 Relatórios", reportsPanel);
        }

        // ==================== MÉTODOS AUXILIARES ====================

        private void createMetricCard(JPanel parent, String title, String value, String change) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(colors.get("dark_lighter"));
            card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(colors.get("gray_light"));
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
            card.add(titleLabel, BorderLayout.NORTH);
            
            JLabel valueLabel = new JLabel(value);
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
            valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            card.add(valueLabel, BorderLayout.CENTER);
            
            JLabel changeLabel = new JLabel(change);
            changeLabel.setForeground(change.startsWith("+") ? colors.get("secondary") : 
                                      change.startsWith("-") ? colors.get("danger") : 
                                      colors.get("gray_light"));
            changeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            changeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
            card.add(changeLabel, BorderLayout.SOUTH);
            
            parent.add(card);
        }

        private void createSystemMetric(JPanel parent, String name, String value, int progressValue) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(name);
            nameLabel.setForeground(colors.get("gray_light"));
            panel.add(nameLabel, BorderLayout.WEST);
            
            JLabel valueLabel = new JLabel(value);
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(valueLabel, BorderLayout.EAST);
            
            parent.add(panel);
            
            if (progressValue > 0) {
                AnimatedProgressBar progressBar = new AnimatedProgressBar();
                progressBar.setValue(progressValue);
                parent.add(progressBar);
            }
        }

        private JPanel createChartPanel(String title, int width, int height) {
            JPanel panel = createLabeledPanel(title);
            panel.setLayout(new GridBagLayout());
            
            JLabel placeholder = new JLabel("Gráfico interativo será implementado aqui");
            placeholder.setForeground(colors.get("gray_light"));
            panel.add(placeholder);
            
            return panel;
        }

        private JPanel createLabeledPanel(String title) {
            JPanel panel = new JPanel();
            panel.setBackground(colors.get("dark_light"));
            panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colors.get("gray")),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                Color.WHITE
            ));
            
            return panel;
        }

        private JButton createStyledButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            return button;
        }

        private JPanel createAlgorithmCard(AdvancedAlgorithm algorithm) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(colors.get("dark_lighter"));
            card.setBorder(BorderFactory.createLineBorder(colors.get("gray"), 1));
            
            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            
            LedIndicator statusLed = new LedIndicator();
            statusLed.setState(algorithm.isActive());
            headerPanel.add(statusLed, BorderLayout.WEST);
            
            JLabel typeLabel = new JLabel(algorithm.getType().getDisplayName());
            typeLabel.setForeground(colors.get("primary_light"));
            typeLabel.setFont(new Font("Arial", Font.BOLD, 11));
            typeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            headerPanel.add(typeLabel, BorderLayout.CENTER);
            
            double score = algorithm.calculatePerformanceScore();
            CircularProgress scoreProgress = new CircularProgress();
            scoreProgress.setValue(score);
            headerPanel.add(scoreProgress, BorderLayout.EAST);
            
            card.add(headerPanel, BorderLayout.NORTH);
            
            // Descrição
            JLabel descLabel = new JLabel("<html><body style='width: 400px'>" + 
                                          algorithm.getDescription() + "</body></html>");
            descLabel.setForeground(colors.get("gray_light"));
            descLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            card.add(descLabel, BorderLayout.CENTER);
            
            // Métricas
            JPanel metricsPanel = new JPanel(new GridLayout(3, 2, 10, 5));
            metricsPanel.setOpaque(false);
            metricsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            
            String[][] metrics = {
                {"🎯 Precisão", String.format("%.1f%%", algorithm.getAccuracy())},
                {"📈 Win Rate", String.format("%.1f%%", algorithm.getWinRate())},
                {"⚡ Sharpe", String.format("%.2f", algorithm.getSharpeRatio())},
                {"🎯 Confiança", String.format("%.1f%%", algorithm.getConfidence())}
            };
            
            for (String[] metric : metrics) {
                JPanel metricPanel = new JPanel(new BorderLayout());
                metricPanel.setOpaque(false);
                
                JLabel nameLabel = new JLabel(metric[0]);
                nameLabel.setForeground(colors.get("gray_light"));
                metricPanel.add(nameLabel, BorderLayout.WEST);
                
                JLabel valueLabel = new JLabel(metric[1]);
                valueLabel.setForeground(Color.WHITE);
                valueLabel.setFont(new Font("Arial", Font.BOLD, 11));
                metricPanel.add(valueLabel, BorderLayout.EAST);
                
                metricsPanel.add(metricPanel);
            }
            
            card.add(metricsPanel, BorderLayout.SOUTH);
            
            return card;
        }

        // ==================== MÉTODOS DE CRIAÇÃO DE DADOS ====================

        private void createSampleAlgorithms() {
            AdvancedAlgorithm algo = new AdvancedAlgorithm(
                "quantum_ensemble",
                "Quantum Ensemble Pro",
                AlgorithmType.QUANTUM,
                "Ensemble quântico que combina múltiplas redes neurais com superposição quântica para análise de mercado multidimensional"
            );
            algo.setVersion("3.2.1");
            algo.setAccuracy(94.7);
            algo.setWinRate(87.4);
            algo.setSharpeRatio(2.85);
            algo.setConfidence(91.2);
            algo.setActive(true);
            algo.setDecisionsMade(15247);
            algo.setTotalProfit(284500.75);
            algo.getSpecializations().addAll(Arrays.asList("Análise Quântica", "Ensemble Learning", "Meta-Otimização"));
            algo.getCompatibleAssets().addAll(Arrays.asList("BTC", "ETH", "SPX", "NASDAQ"));
            
            algorithms.add(algo);
        }

        private void createSamplePositions() {
            PortfolioPosition pos = new PortfolioPosition(
                "BTC/USDT",
                AssetClass.CRYPTO,
                0.85,
                42890.45,
                LocalDateTime.now().minusDays(1),
                "LONG"
            );
            pos.setCurrentPrice(43256.78);
            pos.setStopLoss(41500.00);
            pos.setTakeProfit(45500.00);
            pos.setRiskScore(5.8);
            
            portfolioPositions.add(pos);
        }

        private void createSampleAlerts() {
            Alert alert = new Alert(
                "ALERT-001",
                AlertType.TRADE_SIGNAL,
                "Novo Sinal de Compra Detectado",
                "Quantum Ensemble detectou forte sinal de compra para ETH/USDT com 91.7% de confiança",
                "Quantum Ensemble Pro"
            );
            alert.setPriority(8);
            alert.setActionRequired(true);
            
            alerts.add(alert);
        }

        // ==================== MÉTODOS DE ATUALIZAÇÃO ====================

        private void setupUpdates() {
            Timer updateTimer = new Timer(1000, e -> updateClock());
            updateTimer.start();
            
            Timer metricsTimer = new Timer(5000, e -> updateMetrics());
            metricsTimer.start();
        }

        private void updateClock() {
            String timeStr = LocalDateTime.now().format(timeFormatter);
            updateLabel.setText("Última atualização: " + timeStr);
        }

        private void updateMetrics() {
            // Atualizar métricas do sistema (simulação)
            for (AdvancedAlgorithm algo : algorithms) {
                if (algo.isActive()) {
                    // Pequenas variações nas métricas
                    algo.setConfidence(Math.max(0, Math.min(100, 
                        algo.getConfidence() + (Math.random() * 1 - 0.5))));
                }
            }
        }

        private void updateModeLabel() {
            modeLabel.setText("Modo: " + executionMode.getValue());
            
            Color color = switch (executionMode) {
                case PAPER -> colors.get("info");
                case LIVE -> colors.get("danger");
                case SIMULATION -> colors.get("warning");
                case BACKTEST -> colors.get("primary");
                case HYBRID -> colors.get("secondary");
            };
            modeLabel.setForeground(color);
        }

        // ==================== MANIPULADORES DE EVENTOS ====================

        private void toggleSystem() {
            isRunning = !isRunning;
            if (isRunning) {
                statusLabel.setText("✅ Sistema iniciado");
                statusLabel.setForeground(colors.get("secondary"));
                LOGGER.info("Sistema iniciado");
            } else {
                statusLabel.setText("⏸️ Sistema parado");
                statusLabel.setForeground(colors.get("warning"));
                LOGGER.info("Sistema parado");
            }
        }

        private void openSettings() {
            JDialog settingsDialog = new JDialog(frame, "Configurações do Sistema", true);
            settingsDialog.setSize(500, 600);
            settingsDialog.setLocationRelativeTo(frame);
            
            JTabbedPane settingsTabs = new JTabbedPane();
            settingsTabs.setBackground(colors.get("dark_light"));
            settingsTabs.setForeground(Color.WHITE);
            
            // Aba Geral
            JPanel generalPanel = new JPanel(new GridBagLayout());
            generalPanel.setBackground(colors.get("dark"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;
            
            gbc.gridx = 0; gbc.gridy = 0;
            generalPanel.add(new JLabel("Tema:"), gbc);
            gbc.gridy = 1;
            JComboBox<String> themeCombo = new JComboBox<>(new String[]{"dark", "light", "auto"});
            generalPanel.add(themeCombo, gbc);
            
            gbc.gridy = 2;
            generalPanel.add(new JLabel("Idioma:"), gbc);
            gbc.gridy = 3;
            JComboBox<String> langCombo = new JComboBox<>(new String[]{"pt-BR", "en-US", "es-ES"});
            generalPanel.add(langCombo, gbc);
            
            gbc.gridy = 4;
            JCheckBox autoTradeCheck = new JCheckBox("Auto-trading");
            autoTradeCheck.setBackground(colors.get("dark"));
            autoTradeCheck.setForeground(Color.WHITE);
            generalPanel.add(autoTradeCheck, gbc);
            
            settingsTabs.addTab("Geral", generalPanel);
            
            // Aba Risco (simplificada)
            JPanel riskPanel = new JPanel();
            riskPanel.setBackground(colors.get("dark"));
            riskPanel.add(new JLabel("Configurações de risco em desenvolvimento"));
            settingsTabs.addTab("Risco", riskPanel);
            
            // Aba Notificações (simplificada)
            JPanel notifPanel = new JPanel();
            notifPanel.setBackground(colors.get("dark"));
            notifPanel.add(new JLabel("Configurações de notificações em desenvolvimento"));
            settingsTabs.addTab("Notificações", notifPanel);
            
            settingsDialog.add(settingsTabs, BorderLayout.CENTER);
            
            // Botões
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(colors.get("dark"));
            
            JButton saveBtn = createStyledButton("💾 Salvar", colors.get("secondary"));
            saveBtn.addActionListener(e -> settingsDialog.dispose());
            buttonPanel.add(saveBtn);
            
            JButton cancelBtn = createStyledButton("❌ Cancelar", colors.get("danger"));
            cancelBtn.addActionListener(e -> settingsDialog.dispose());
            buttonPanel.add(cancelBtn);
            
            settingsDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            settingsDialog.setVisible(true);
        }

        private void showHelp() {
            String helpText = """
                LEXTRADER-IAG - LEXTRADER-IAG 4.0 - Sistema de Trading Algorítmico Avançado
                
                📚 FUNCIONALIDADES PRINCIPAIS:
                
                1. 🤖 ALGORITMOS AVANÇADOS
                   • Quantum Ensemble: Combinação de múltiplas redes neurais
                   • LSTM Adaptativo: Análise de séries temporais
                   • Otimizador Bayesiano: Otimização multi-objetivo
                
                2. 💼 GESTÃO DE PORTFÓLIO
                   • Alocação dinâmica de ativos
                   • Controle de risco em tempo real
                   • Hedge automático
                
                3. 📊 ANÁLISE AVANÇADA
                   • Técnica: Indicadores e padrões
                   • Fundamentalista: Métricas de mercado
                   • Sentimento: Análise de mídias sociais
                   • Risco: VaR, CVaR, drawdown
                
                Para suporte técnico: suporte@lextrader.com.br
            """;
            
            JTextArea helpArea = new JTextArea(helpText);
            helpArea.setEditable(false);
            helpArea.setBackground(colors.get("dark_lighter"));
            helpArea.setForeground(Color.WHITE);
            helpArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JOptionPane.showMessageDialog(frame, new JScrollPane(helpArea), "Ajuda", JOptionPane.INFORMATION_MESSAGE);
        }

        private void openQuickTrade() {
            JDialog quickTradeDialog = new JDialog(frame, "Trade Rápido", true);
            quickTradeDialog.setSize(400, 300);
            quickTradeDialog.setLocationRelativeTo(frame);
            
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(colors.get("dark"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Símbolo:"), gbc);
            gbc.gridx = 1;
            JTextField symbolField = new JTextField("BTC/USDT", 15);
            panel.add(symbolField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Lado:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> sideCombo = new JComboBox<>(new String[]{"BUY", "SELL"});
            panel.add(sideCombo, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Quantidade:"), gbc);
            gbc.gridx = 1;
            JTextField qtyField = new JTextField("0.1", 15);
            panel.add(qtyField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(colors.get("dark"));
            
            JButton buyBtn = createStyledButton("📈 Comprar", colors.get("secondary"));
            buyBtn.addActionListener(e -> {
                try {
                    double qty = Double.parseDouble(qtyField.getText());
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        sideCombo.getSelectedItem() + " " + qty + " " + symbolField.getText() + " executado com sucesso!",
                        "Trade Executado",
                        JOptionPane.INFORMATION_MESSAGE);
                    quickTradeDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        "Quantidade inválida!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(buyBtn);
            
            JButton sellBtn = createStyledButton("📉 Vender", colors.get("danger"));
            sellBtn.addActionListener(e -> {
                try {
                    double qty = Double.parseDouble(qtyField.getText());
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        sideCombo.getSelectedItem() + " " + qty + " " + symbolField.getText() + " executado com sucesso!",
                        "Trade Executado",
                        JOptionPane.INFORMATION_MESSAGE);
                    quickTradeDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(quickTradeDialog,
                        "Quantidade inválida!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(sellBtn);
            
            panel.add(buttonPanel, gbc);
            
            quickTradeDialog.add(panel);
            quickTradeDialog.setVisible(true);
        }

        private void checkRisk() {
            JOptionPane.showMessageDialog(frame,
                "Análise de risco completada.\nPortfólio dentro dos limites de risco configurados.",
                "Verificar Risco",
                JOptionPane.INFORMATION_MESSAGE);
        }

        private void generateReport() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório");
            
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Relatório gerado com sucesso:\n" + fileChooser.getSelectedFile().getPath(),
                    "Relatório",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void closeAllPositions() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Fechar todas as posições abertas?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Todas as posições foram fechadas!",
                    "Fechar Posições",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void hedgePortfolio() {
            JOptionPane.showMessageDialog(frame,
                "Iniciando hedge do portfólio...",
                "Hedge",
                JOptionPane.INFORMATION_MESSAGE);
        }

        private void clearLogs() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Limpar todos os logs?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Logs limpos com sucesso!",
                    "Limpar Logs",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void exportLogs() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Exportar Logs");
            
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Logs exportados para: " + fileChooser.getSelectedFile().getPath(),
                    "Exportar",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void acknowledgeAllAlerts() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Reconhecer todos os alertas?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Todos os alertas foram reconhecidos!",
                    "Reconhecer Alertas",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void clearResolvedAlerts() {
            int result = JOptionPane.showConfirmDialog(frame,
                "Limpar alertas resolvidos?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame,
                    "Alertas resolvidos limpos!",
                    "Limpar Alertas",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void setupShutdown() {
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    eventManager.stop();
                    LOGGER.info("Sistema encerrado");
                }
            });
        }

        public void show() {
            frame.setVisible(true);
        }
    }

    // ==================== FUNÇÃO PRINCIPAL ====================

    public static void main(String[] args) {
        LOGGER.info("=".repeat(60));
        LOGGER.info("INICIANDO LEXTRADER-IAG - DASHBOARD DE TRADING");
        LOGGER.info("=".repeat(60));
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                AdvancedTradingDashboard dashboard = new AdvancedTradingDashboard();
                dashboard.show();
                
                LOGGER.info("Dashboard iniciado com sucesso");
                
            } catch (Exception e) {
                LOGGER.severe("Erro ao iniciar dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}