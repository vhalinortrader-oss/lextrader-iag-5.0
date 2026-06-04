package modulo_emocional.Decisao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Risk Manager AGI - Sistema Avançado de Gestão de Risco
 * ==================================================
 * Versão Java do RiskManagerAGI.py - Sistema autônomo de gestão de risco
 * com integração AGI, detecção de anomalias, predição neural quântica
 * e mitigação automática de riscos
 */

// Enums
enum ProtectiveMeasureType {
    REDUCE_SIZE,
    HEDGE,
    DIVERSIFY,
    STOP_TRADING
}

enum MeasureStatus {
    PENDING,
    ACTIVE,
    EXECUTED,
    CANCELLED
}

enum StrategyStatus {
    HEALTHY,
    DEGRADING,
    CRITICAL
}

// Classes de Dados
class RiskConfig {
    public double maxDrawdown = 0.05;
    public double positionLimit = 0.02;
    public double maxCorrelation = 0.7;
    public double volatilityLimit = 0.15;
    public boolean autoHedge = true;
    
    @Override
    public String toString() {
        return String.format("RiskConfig{maxDrawdown=%.3f, positionLimit=%.3f, maxCorrelation=%.2f, volatilityLimit=%.3f, autoHedge=%s}", 
            maxDrawdown, positionLimit, maxCorrelation, volatilityLimit, autoHedge);
    }
}

class RiskAssessment {
    public double drawdown;
    public double exposure;
    public double volatility;
    public double correlation;
    public double riskScore = 0.0;
    public boolean isAnomaly = false;
    public LocalDateTime timestamp;
    
    public RiskAssessment(double drawdown, double exposure, double volatility, double correlation) {
        this.drawdown = drawdown;
        this.exposure = exposure;
        this.volatility = volatility;
        this.correlation = correlation;
        this.timestamp = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("RiskAssessment{drawdown=%.3f, exposure=%.3f, volatility=%.3f, correlation=%.3f, riskScore=%.1f, anomaly=%s}", 
            drawdown, exposure, volatility, correlation, riskScore, isAnomaly);
    }
}

class ProtectiveMeasure {
    public String id;
    public ProtectiveMeasureType type;
    public String instrument;
    public double ratio;
    public MeasureStatus status;
    public LocalDateTime timestamp;
    public String description;
    
    public ProtectiveMeasure(String id, ProtectiveMeasureType type, String instrument, double ratio, 
                           MeasureStatus status, LocalDateTime timestamp, String description) {
        this.id = id;
        this.type = type;
        this.instrument = instrument;
        this.ratio = ratio;
        this.status = status;
        this.timestamp = timestamp;
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("ProtectiveMeasure{id='%s', type=%s, instrument='%s', ratio=%.2f, status=%s}", 
            id, type, instrument, ratio, status);
    }
}

class StrategyHealth {
    public double winRate;
    public double profitFactor;
    public double riskAdjustedReturn;
    public double consistencyScore;
    public StrategyStatus status;
    
    public StrategyHealth(double winRate, double profitFactor, double riskAdjustedReturn, 
                         double consistencyScore, StrategyStatus status) {
        this.winRate = winRate;
        this.profitFactor = profitFactor;
        this.riskAdjustedReturn = riskAdjustedReturn;
        this.consistencyScore = consistencyScore;
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("StrategyHealth{winRate=%.2f, profitFactor=%.2f, riskAdjustedReturn=%.2f, consistency=%.1f, status=%s}", 
            winRate, profitFactor, riskAdjustedReturn, consistencyScore, status);
    }
}

class MarketDataPoint {
    public LocalDateTime timestamp;
    public double price;
    public double volume;
    public double volatility;
    
    public MarketDataPoint(LocalDateTime timestamp, double price, double volume, double volatility) {
        this.timestamp = timestamp;
        this.price = price;
        this.volume = volume;
        this.volatility = volatility;
    }
    
    @Override
    public String toString() {
        return String.format("MarketDataPoint{price=%.2f, volume=%.0f, volatility=%.3f}", price, volume, volatility);
    }
}

// Simulações das dependências externas
class EmotionVector {
    public double stability;
    public double confidence;
    public double anxiety;
    
    public EmotionVector() {
        this.stability = 50 + Math.random() * 50;
        this.confidence = 50 + Math.random() * 50;
        this.anxiety = Math.random() * 50;
    }
    
    @Override
    public String toString() {
        return String.format("EmotionVector{stability=%.1f, confidence=%.1f, anxiety=%.1f}", 
            stability, confidence, anxiety);
    }
}

class SentientCore {
    private static final Logger logger = Logger.getLogger(SentientCore.class.getName());
    
    public EmotionVector getVector() {
        return new EmotionVector();
    }
    
    public void perceiveReality(double volatility, double fearIntensity) {
        // Simulação da percepção de realidade
        logger.info(String.format("[AGI Perception] Volatility: %.2f, Fear: %.2f", volatility, fearIntensity));
    }
}

class NeuralPrediction {
    public double prediction;
    
    public NeuralPrediction(double prediction) {
        this.prediction = prediction;
    }
    
    @Override
    public String toString() {
        return String.format("NeuralPrediction{prediction=%.4f}", prediction);
    }
}

class QuantumNeuralNetwork {
    private boolean initialized = false;
    private static final Logger logger = Logger.getLogger(QuantumNeuralNetwork.class.getName());
    
    public void initialize() {
        this.initialized = true;
        logger.info("🧠 Quantum Neural Network Initialized");
    }
    
    public NeuralPrediction predict(List<Double> inputs) {
        // Simulação da predição neural
        // Em um sistema real, esta seria uma rede neural quântica
        double weightedSum = inputs.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double noise = -0.1 + Math.random() * 0.2;
        double prediction = Math.tanh(weightedSum * 2 + noise);
        return new NeuralPrediction(prediction);
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}

// Classe AnomalyDetector
class AnomalyDetector {
    private List<RiskAssessment> history = new ArrayList<>();
    private double[] mean = {0.0, 0.0, 0.0, 0.0}; // Drawdown, Exposure, Volatility, Correlation
    private double[] stdDev = {1.0, 1.0, 1.0, 1.0};
    private static final Logger logger = Logger.getLogger(AnomalyDetector.class.getName());
    
    public boolean detect(RiskAssessment metrics) {
        // Calculate Z-Scores
        double zDrawdown = stdDev[0] != 0 ? Math.abs((metrics.drawdown - mean[0]) / stdDev[0]) : 0;
        double zExposure = stdDev[1] != 0 ? Math.abs((metrics.exposure - mean[1]) / stdDev[1]) : 0;
        double zVolatility = stdDev[2] != 0 ? Math.abs((metrics.volatility - mean[2]) / stdDev[2]) : 0;
        
        // Update history
        history.add(metrics);
        if (history.size() > 100) {
            history.remove(0);
        }
        recalibrate();
        
        // Anomaly Threshold (Aggregated Z-Score)
        double totalAnomalyScore = zDrawdown + zExposure + zVolatility;
        boolean isAnomaly = totalAnomalyScore > 6.0; // Heuristic threshold
        
        if (isAnomaly) {
            logger.warning(String.format("Anomaly detected: Z-Score=%.2f (Drawdown=%.2f, Exposure=%.2f, Volatility=%.2f)", 
                totalAnomalyScore, zDrawdown, zExposure, zVolatility));
        }
        
        return isAnomaly;
    }
    
    private void recalibrate() {
        if (history.size() < 10) {
            return;
        }
        
        // Calculate statistics for each metric
        calcStats('drawdown', 0);
        calcStats('exposure', 1);
        calcStats('volatility', 2);
        calcStats('correlation', 3);
    }
    
    private void calcStats(String fieldName, int index) {
        List<Double> values = new ArrayList<>();
        
        switch (fieldName) {
            case 'drawdown':
                values = history.stream().map(r -> r.drawdown).collect(Collectors.toList());
                break;
            case 'exposure':
                values = history.stream().map(r -> r.exposure).collect(Collectors.toList());
                break;
            case 'volatility':
                values = history.stream().map(r -> r.volatility).collect(Collectors.toList());
                break;
            case 'correlation':
                values = history.stream().map(r -> r.correlation).collect(Collectors.toList());
                break;
        }
        
        if (values.isEmpty()) {
            return;
        }
        
        double meanVal = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = values.stream()
            .mapToDouble(v -> Math.pow(v - meanVal, 2))
            .average().orElse(0.0);
        
        mean[index] = meanVal;
        stdDev[index] = variance > 0 ? Math.sqrt(variance) : 0.001; // Avoid division by zero
    }
    
    public int getHistorySize() {
        return history.size();
    }
    
    public double[] getMean() {
        return mean.clone();
    }
    
    public double[] getStdDev() {
        return stdDev.clone();
    }
}

// Classe principal AutonomousRiskManager
class AutonomousRiskManager {
    private static final Logger logger = Logger.getLogger(AutonomousRiskManager.class.getName());
    
    private RiskConfig config;
    private List<ProtectiveMeasure> activeMeasures = new ArrayList<>();
    private StrategyHealth strategyHealth;
    private AnomalyDetector anomalyDetector;
    private QuantumNeuralNetwork riskPredictor;
    private SentientCore sentientCore;
    private List<String> logHistory = new ArrayList<>();
    private Random random = new Random();
    
    public AutonomousRiskManager() {
        this.config = new RiskConfig();
        this.strategyHealth = new StrategyHealth(
            0.6, 1.5, 1.2, 80.0, StrategyStatus.HEALTHY
        );
        this.anomalyDetector = new AnomalyDetector();
        this.riskPredictor = new QuantumNeuralNetwork();
        this.sentientCore = new SentientCore();
        
        riskPredictor.initialize();
        
        logger.info("🛡️ Autonomous Risk Manager Initialized (AGI-Linked)");
    }
    
    // --- MAIN MONITORING LOOP ---
    
    public CompletableFuture<RiskAssessment> monitorRealTimeRisk(Map<String, Object> positionData) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Adapt config based on AGI state
                adaptToSentientState();
                
                RiskAssessment metrics = new RiskAssessment(
                    (Double) positionData.getOrDefault("drawdown", 0.0),
                    (Double) positionData.getOrDefault("exposure", 0.0),
                    (Double) positionData.getOrDefault("volatility", 0.0),
                    (Double) positionData.getOrDefault("correlation", 0.0)
                );
                
                // 1. Detect Anomaly
                metrics.isAnomaly = anomalyDetector.detect(metrics);
                
                // 2. Predictive Risk (Quantum)
                NeuralPrediction prediction = riskPredictor.predict(Arrays.asList(
                    metrics.drawdown,
                    metrics.exposure,
                    metrics.volatility,
                    metrics.correlation
                ));
                
                // 3. Calculate Global Risk Score (0-100)
                metrics.riskScore = Math.min(100.0, (
                    (metrics.drawdown * 400) +
                    (metrics.volatility * 200) +
                    (prediction.prediction * 20)
                ));
                
                // 4. Trigger Mitigation if needed
                if (metrics.isAnomaly || 
                    metrics.riskScore > 80 || 
                    metrics.drawdown > config.maxDrawdown) {
                    triggerRiskMitigation(metrics);
                }
                
                return metrics;
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in real-time risk monitoring", e);
                throw new RuntimeException("Risk monitoring failed", e);
            }
        });
    }
    
    private void adaptToSentientState() {
        EmotionVector emotion = sentientCore.getVector();
        
        // If AGI is Anxious/Defensive -> Tighten Risk Limits
        if (emotion.stability < 40 || emotion.confidence < 30) {
            config.maxDrawdown = 0.03; // Tighten to 3%
            config.volatilityLimit = 0.10;
            if (random.nextDouble() > 0.9) {
                log("AGI Anxiety detected: Risk parameters tightened.");
            }
        }
        // If AGI is Confident/Focused -> Relax Limits slightly
        else if (emotion.confidence > 80 && emotion.stability > 70) {
            config.maxDrawdown = 0.07; // Relax to 7%
            config.volatilityLimit = 0.20;
        } else {
            // Reset to defaults
            config.maxDrawdown = 0.05;
            config.volatilityLimit = 0.15;
        }
    }
    
    private void triggerRiskMitigation(RiskAssessment risks) {
        List<ProtectiveMeasure> actions = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        
        // Action: Reduce Size
        if (risks.drawdown > config.maxDrawdown) {
            actions.add(new ProtectiveMeasure(
                String.format("MIT-%d-1", currentTime.toEpochSecond(java.time.ZoneOffset.UTC)),
                ProtectiveMeasureType.REDUCE_SIZE,
                "GLOBAL",
                0.5, // Cut positions by half
                MeasureStatus.EXECUTED,
                currentTime,
                String.format("Drawdown critical (%.2f%%). Reducing exposure.", risks.drawdown * 100)
            ));
            sentientCore.perceiveReality(risks.volatility * 2, -100);
        }
        
        // Action: Hedge
        if (risks.volatility > config.volatilityLimit && config.autoHedge) {
            actions.add(new ProtectiveMeasure(
                String.format("MIT-%d-2", currentTime.toEpochSecond(java.time.ZoneOffset.UTC)),
                ProtectiveMeasureType.HEDGE,
                "BTC-PERP-SHORT",
                1.0,
                MeasureStatus.ACTIVE,
                currentTime,
                String.format("Volatility spike (%.2f%%). Hedge activated.", risks.volatility * 100)
            ));
        }
        
        // Update active measures (keep last 20)
        activeMeasures = new ArrayList<>(actions);
        activeMeasures.addAll(this.activeMeasures.subList(0, Math.min(20 - actions.size(), this.activeMeasures.size())));
        
        if (!actions.isEmpty()) {
            String actionTypes = actions.stream()
                .map(a -> a.type.name())
                .collect(Collectors.joining(", "));
            log(String.format("Mitigation Triggered: %s", actionTypes));
        }
    }
    
    // --- STRATEGY HEALTH & CORRECTION ---
    
    public void monitorStrategyHealth(Map<String, Double> stats) {
        double winRate = stats.getOrDefault("winRate", strategyHealth.winRate);
        double profitFactor = stats.getOrDefault("profitFactor", strategyHealth.profitFactor);
        
        strategyHealth.winRate = winRate;
        strategyHealth.profitFactor = profitFactor;
        
        // Simple health logic
        if (winRate < 0.4 || profitFactor < 1.0) {
            strategyHealth.status = StrategyStatus.CRITICAL;
            triggerStrategyAdjustment("Performance degradation detected.");
        } else if (winRate < 0.5) {
            strategyHealth.status = StrategyStatus.DEGRADING;
        } else {
            strategyHealth.status = StrategyStatus.HEALTHY;
        }
    }
    
    private void triggerStrategyAdjustment(String reason) {
        log(String.format("Strategy Correction: %s", reason));
        // In a real system, this would modify Swarm Agent parameters via the Creator module
        // For simulation, we log an adjustment recommendation
        ProtectiveMeasure adjustment = new ProtectiveMeasure(
            String.format("ADJ-%d", LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)),
            ProtectiveMeasureType.DIVERSIFY,
            "STRATEGY_PARAMS",
            0.0,
            MeasureStatus.PENDING,
            LocalDateTime.now(),
            "Recommended: Tighten entry filters, increase stop-loss distance."
        );
        activeMeasures.add(0, adjustment);
    }
    
    // --- LOGGING ---
    
    public void log(String msg) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logEntry = String.format("[%s] %s", timestamp, msg);
        logHistory.add(0, logEntry);
        if (logHistory.size() > 50) {
            logHistory.remove(logHistory.size() - 1);
        }
        System.out.println(logEntry); // Also print to console
    }
    
    public List<String> getLogs() {
        return new ArrayList<>(logHistory);
    }
    
    public Map<String, Object> getHealthMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("strategy_health", strategyHealth);
        metrics.put("active_measures", activeMeasures.size());
        metrics.put("config", config);
        metrics.put("risk_history_size", anomalyDetector.getHistorySize());
        return metrics;
    }
    
    // Getters
    public RiskConfig getConfig() {
        return config;
    }
    
    public List<ProtectiveMeasure> getActiveMeasures() {
        return new ArrayList<>(activeMeasures);
    }
    
    public StrategyHealth getStrategyHealth() {
        return strategyHealth;
    }
    
    public AnomalyDetector getAnomalyDetector() {
        return anomalyDetector;
    }
    
    public QuantumNeuralNetwork getRiskPredictor() {
        return riskPredictor;
    }
    
    public SentientCore getSentientCore() {
        return sentientCore;
    }
    
    // Setters
    public void setConfig(RiskConfig config) {
        this.config = config;
    }
    
    public void setSentientCore(SentientCore sentientCore) {
        this.sentientCore = sentientCore;
    }
}

// Classe Principal para Demonstração
public class RiskManagerAGIApp {
    
    private static AutonomousRiskManager riskManagerAGI = new AutonomousRiskManager();
    
    public static void main(String[] args) {
        System.out.println("🚀 Simulação do Gestor de Risco Autônomo AGI");
        
        try {
            // Executar 5 ciclos de monitoramento
            for (int i = 0; i < 5; i++) {
                System.out.println("\n" + "=".repeat(50));
                System.out.printf("Ciclo de Monitoramento %d%n", i + 1);
                System.out.println("=".repeat(50));
                
                simulateMonitoring();
                
                // Mostrar logs recentes
                if (i == 4) { // Mostrar logs no último ciclo
                    System.out.println("\n📋 Logs Recentes:");
                    List<String> logs = riskManagerAGI.getLogs();
                    for (int j = 0; j < Math.min(5, logs.size()); j++) {
                        System.out.println("  " + logs.get(j));
                    }
                }
                
                Thread.sleep(1000); // Simular intervalo entre ciclos
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Simulação interrompida");
        } catch (Exception e) {
            System.err.println("Erro na simulação: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void simulateMonitoring() {
        /** Simula o monitoramento de risco em tempo real */
        
        // Dados de posição simulados
        Map<String, Object> positionData = new HashMap<>();
        positionData.put("drawdown", 0.01 + Math.random() * 0.07);
        positionData.put("exposure", 0.01 + Math.random() * 0.04);
        positionData.put("volatility", 0.05 + Math.random() * 0.20);
        positionData.put("correlation", 0.1 + Math.random() * 0.8);
        
        // Monitorar risco
        try {
            RiskAssessment riskAssessment = riskManagerAGI.monitorRealTimeRisk(positionData).get();
            
            System.out.println("\n🔍 Risk Assessment:");
            System.out.printf("  Drawdown: %.3f%n", riskAssessment.drawdown);
            System.out.printf("  Exposure: %.3f%n", riskAssessment.exposure);
            System.out.printf("  Volatility: %.3f%n", riskAssessment.volatility);
            System.out.printf("  Risk Score: %.1f%n", riskAssessment.riskScore);
            System.out.printf("  Anomaly Detected: %s%n", riskAssessment.isAnomaly);
            
            // Monitorar saúde da estratégia
            Map<String, Double> strategyStats = new HashMap<>();
            strategyStats.put("winRate", 0.3 + Math.random() * 0.4);
            strategyStats.put("profitFactor", 0.8 + Math.random() * 1.2);
            
            riskManagerAGI.monitorStrategyHealth(strategyStats);
            
            System.out.println("\n🏥 Strategy Health:");
            System.out.printf("  Status: %s%n", riskManagerAGI.getStrategyHealth().status);
            System.out.printf("  Win Rate: %.2f%n", riskManagerAGI.getStrategyHealth().winRate);
            System.out.printf("  Profit Factor: %.2f%n", riskManagerAGI.getStrategyHealth().profitFactor);
            
        } catch (Exception e) {
            System.err.println("Erro no monitoramento: " + e.getMessage());
        }
    }
    
    /**
     * Método utilitário para uso programático
     */
    public static AutonomousRiskManager createRiskManager() {
        return new AutonomousRiskManager();
    }
    
    /**
     * Método utilitário para demonstração avançada
     */
    public static void runAdvancedDemo() {
        System.out.println("🔬 DEMONSTRAÇÃO AVANÇADA DO RISK MANAGER AGI");
        System.out.println("=".repeat(60));
        
        AutonomousRiskManager manager = new AutonomousRiskManager();
        
        try {
            // Testar configuração
            System.out.println("\n⚙️ Testando Configuração:");
            RiskConfig config = manager.getConfig();
            System.out.println("   " + config.toString());
            
            // Testar estado emocional
            System.out.println("\n🧠 Testando Estado Emocional AGI:");
            EmotionVector emotion = manager.getSentientCore().getVector();
            System.out.println("   " + emotion.toString());
            
            // Testar detecção de anomalias
            System.out.println("\n🔍 Testando Detecção de Anomalias:");
            testAnomalyDetection(manager);
            
            // Testar predição neural
            System.out.println("\n🧮 Testando Predição Neural Quântica:");
            testNeuralPrediction(manager);
            
            // Testar mitigação de risco
            System.out.println("\n🛡️ Testando Mitigação de Risco:");
            testRiskMitigation(manager);
            
            // Testar saúde da estratégia
            System.out.println("\n🏥 Testando Saúde da Estratégia:");
            testStrategyHealth(manager);
            
            System.out.println("\n✅ Demonstração avançada concluída!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro na demonstração avançada: " + e.getMessage());
        }
    }
    
    private static void testAnomalyDetection(AutonomousRiskManager manager) {
        /** Testa detecção de anomalias com diferentes cenários */
        
        AnomalyDetector detector = manager.getAnomalyDetector();
        
        // Cenário 1: Valores normais
        RiskAssessment normal = new RiskAssessment(0.02, 0.03, 0.08, 0.4);
        boolean isAnomaly1 = detector.detect(normal);
        System.out.printf("   Normal: Drawdown=%.3f, Anomaly=%s%n", normal.drawdown, isAnomaly1);
        
        // Cenário 2: Valores elevados
        RiskAssessment elevated = new RiskAssessment(0.08, 0.06, 0.25, 0.8);
        boolean isAnomaly2 = detector.detect(elevated);
        System.out.printf("   Elevado: Drawdown=%.3f, Anomaly=%s%n", elevated.drawdown, isAnomaly2);
        
        // Cenário 3: Valores extremos
        RiskAssessment extreme = new RiskAssessment(0.15, 0.10, 0.40, 0.95);
        boolean isAnomaly3 = detector.detect(extreme);
        System.out.printf("   Extremo: Drawdown=%.3f, Anomaly=%s%n", extreme.drawdown, isAnomaly3);
    }
    
    private static void testNeuralPrediction(AutonomousRiskManager manager) {
        /** Testa predição neural quântica */
        
        QuantumNeuralNetwork network = manager.getRiskPredictor();
        
        List<Double> inputs = Arrays.asList(0.05, 0.03, 0.12, 0.6);
        NeuralPrediction prediction = network.predict(inputs);
        
        System.out.printf("   Inputs: %s%n", inputs.stream().map(d -> String.format("%.3f", d)).collect(Collectors.joining(", ")));
        System.out.printf("   Prediction: %.4f%n", prediction.prediction);
        System.out.printf("   Network Initialized: %s%n", network.isInitialized());
    }
    
    private static void testRiskMitigation(AutonomousRiskManager manager) {
        /** Testa sistema de mitigação de risco */
        
        // Criar cenário de risco crítico
        Map<String, Object> criticalData = new HashMap<>();
        criticalData.put("drawdown", 0.10); // Acima do limite
        criticalData.put("exposure", 0.05);
        criticalData.put("volatility", 0.20);
        criticalData.put("correlation", 0.8);
        
        try {
            RiskAssessment assessment = manager.monitorRealTimeRisk(criticalData).get();
            
            System.out.printf("   Risk Score: %.1f%n", assessment.riskScore);
            System.out.printf("   Anomaly Detected: %s%n", assessment.isAnomaly);
            System.out.printf("   Active Measures: %d%n", manager.getActiveMeasures().size());
            
            if (!manager.getActiveMeasures().isEmpty()) {
                System.out.println("   Measures:");
                for (ProtectiveMeasure measure : manager.getActiveMeasures()) {
                    System.out.printf("      • %s: %s%n", measure.type, measure.description);
                }
            }
            
        } catch (Exception e) {
            System.err.println("   Erro na mitigação: " + e.getMessage());
        }
    }
    
    private static void testStrategyHealth(AutonomousRiskManager manager) {
        /** Testa monitoramento de saúde da estratégia */
        
        // Testar diferentes cenários de saúde
        Map<String, Double> healthyStats = new HashMap<>();
        healthyStats.put("winRate", 0.65);
        healthyStats.put("profitFactor", 1.8);
        
        manager.monitorStrategyHealth(healthyStats);
        System.out.printf("   Healthy: WinRate=%.2f, Status=%s%n", 
            manager.getStrategyHealth().winRate, manager.getStrategyHealth().status);
        
        Map<String, Double> degradingStats = new HashMap<>();
        degradingStats.put("winRate", 0.45);
        degradingStats.put("profitFactor", 1.2);
        
        manager.monitorStrategyHealth(degradingStats);
        System.out.printf("   Degrading: WinRate=%.2f, Status=%s%n", 
            manager.getStrategyHealth().winRate, manager.getStrategyHealth().status);
        
        Map<String, Double> criticalStats = new HashMap<>();
        criticalStats.put("winRate", 0.35);
        criticalStats.put("profitFactor", 0.9);
        
        manager.monitorStrategyHealth(criticalStats);
        System.out.printf("   Critical: WinRate=%.2f, Status=%s%n", 
            manager.getStrategyHealth().winRate, manager.getStrategyHealth().status);
    }
    
    /**
     * Método utilitário para verificação de saúde
     */
    public static Map<String, Object> healthCheck(AutonomousRiskManager manager) {
        /** Retorna status de saúde do sistema */
        Map<String, Object> health = new HashMap<>();
        
        try {
            String status = "HEALTHY";
            if (manager.getStrategyHealth().status == StrategyStatus.CRITICAL) {
                status = "CRITICAL";
            } else if (manager.getStrategyHealth().status == StrategyStatus.DEGRADING) {
                status = "WARNING";
            }
            
            health.put("status", status);
            health.put("timestamp", System.currentTimeMillis());
            health.put("strategy_health", manager.getStrategyHealth());
            health.put("active_measures", manager.getActiveMeasures().size());
            health.put("risk_history_size", manager.getAnomalyDetector().getHistorySize());
            health.put("config", manager.getConfig());
            
            Map<String, Object> metrics = manager.getHealthMetrics();
            health.putAll(metrics);
            
        } catch (Exception e) {
            health.put("status", "UNHEALTHY");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
        }
        
        return health;
    }
    
    /**
     * Método utilitário para obter instância global
     */
    public static AutonomousRiskManager getInstance() {
        return riskManagerAGI;
    }
}
