package com.lextrader.iag4.prediction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Dashboard Avançado de Predição
 * Versão Java convertida do Python original (Streamlit)
 */
public class AdvancedPredictionDashboard {
    
    private static final Logger logger = LoggerFactory.getLogger(AdvancedPredictionDashboard.class);
    
    // Enums para tipos de dados
    public enum SeverityLevel {
        CRITICAL, HIGH, MEDIUM, LOW
    }
    
    public enum ResourceType {
        CPU, MEMORY, DISK, NETWORK
    }
    
    // Estrutura de dados para resultado de predição
    public static class PredictionResult {
        private final ResourceType resourceType;
        private final List<Double> predictions;
        private final double confidence;
        private final LocalDateTime timestamp;
        private final Map<String, Object> metadata;
        
        public PredictionResult(ResourceType resourceType, List<Double> predictions, 
                              double confidence, Map<String, Object> metadata) {
            this.resourceType = resourceType;
            this.predictions = new ArrayList<>(predictions);
            this.confidence = Math.min(1.0, Math.max(0.0, confidence));
            this.timestamp = LocalDateTime.now();
            this.metadata = new HashMap<>(metadata);
        }
        
        // Getters
        public ResourceType getResourceType() { return resourceType; }
        public List<Double> getPredictions() { return new ArrayList<>(predictions); }
        public double getConfidence() { return confidence; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    }
    
    // Estrutura de dados para incidente do sistema
    public static class SystemIncident {
        private final String type;
        private final String description;
        private final SeverityLevel severity;
        private final double probability;
        private final LocalDateTime timestamp;
        private final Map<String, Object> details;
        
        public SystemIncident(String type, String description, SeverityLevel severity,
                            double probability, Map<String, Object> details) {
            this.type = type;
            this.description = description;
            this.severity = severity;
            this.probability = Math.min(1.0, Math.max(0.0, probability));
            this.timestamp = LocalDateTime.now();
            this.details = new HashMap<>(details);
        }
        
        // Getters
        public String getType() { return type; }
        public String getDescription() { return description; }
        public SeverityLevel getSeverity() { return severity; }
        public double getProbability() { return probability; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getDetails() { return new HashMap<>(details); }
    }
    
    // Estrutura de dados para risco de segurança
    public static class SecurityRisk {
        private final String type;
        private final String level;
        private final String description;
        private final double probability;
        private final LocalDateTime timestamp;
        private final Map<String, Object> riskFactors;
        
        public SecurityRisk(String type, String level, String description,
                           double probability, Map<String, Object> riskFactors) {
            this.type = type;
            this.level = level;
            this.description = description;
            this.probability = Math.min(1.0, Math.max(0.0, probability));
            this.timestamp = LocalDateTime.now();
            this.riskFactors = new HashMap<>(riskFactors);
        }
        
        // Getters
        public String getType() { return type; }
        public String getLevel() { return level; }
        public String getDescription() { return description; }
        public double getProbability() { return probability; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getRiskFactors() { return new HashMap<>(riskFactors); }
    }
    
    // Estrutura de dados para métricas do sistema
    public static class SystemMetrics {
        private final Map<ResourceType, Double> currentUsage;
        private final Map<ResourceType, List<Double>> historicalData;
        private final Map<ResourceType, Double> predictions;
        private final double overallHealth;
        private final LocalDateTime timestamp;
        
        public SystemMetrics(Map<ResourceType, Double> currentUsage,
                           Map<ResourceType, List<Double>> historicalData,
                           Map<ResourceType, Double> predictions) {
            this.currentUsage = new HashMap<>(currentUsage);
            this.historicalData = new HashMap<>();
            historicalData.forEach((type, data) -> {
                this.historicalData.put(type, new ArrayList<>(data));
            });
            this.predictions = new HashMap<>(predictions);
            this.overallHealth = calculateOverallHealth();
            this.timestamp = LocalDateTime.now();
        }
        
        private double calculateOverallHealth() {
            double totalUsage = currentUsage.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
            
            return Math.max(0.0, 100.0 - totalUsage / currentUsage.size() * 100);
        }
        
        // Getters
        public Map<ResourceType, Double> getCurrentUsage() { return new HashMap<>(currentUsage); }
        public Map<ResourceType, List<Double>> getHistoricalData() { 
            Map<ResourceType, List<Double>> copy = new HashMap<>();
            historicalData.forEach((type, data) -> {
                copy.put(type, new ArrayList<>(data));
            });
            return copy;
        }
        public Map<ResourceType, Double> getPredictions() { return new HashMap<>(predictions); }
        public double getOverallHealth() { return overallHealth; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    // Estrutura de dados para alerta
    public static class Alert {
        private final String id;
        private final String title;
        private final String message;
        private final SeverityLevel severity;
        private final boolean isActive;
        private final LocalDateTime timestamp;
        private final Map<String, Object> context;
        
        public Alert(String id, String title, String message, SeverityLevel severity,
                    boolean isActive, Map<String, Object> context) {
            this.id = id;
            this.title = title;
            this.message = message;
            this.severity = severity;
            this.isActive = isActive;
            this.timestamp = LocalDateTime.now();
            this.context = new HashMap<>(context);
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public SeverityLevel getSeverity() { return severity; }
        public boolean isActive() { return isActive; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getContext() { return new HashMap<>(context); }
    }
    
    // Estado do dashboard
    private final Map<ResourceType, PredictionResult> predictionResults;
    private final List<SystemIncident> systemIncidents;
    private final List<SecurityRisk> securityRisks;
    private final Map<String, Alert> activeAlerts;
    private final Map<ResourceType, List<Double>> historicalMetrics;
    private final AdvancedPredictionService predictionService;
    
    public AdvancedPredictionDashboard() {
        this.predictionResults = new ConcurrentHashMap<>();
        this.systemIncidents = new ArrayList<>();
        this.securityRisks = new ArrayList<>();
        this.activeAlerts = new ConcurrentHashMap<>();
        this.historicalMetrics = new ConcurrentHashMap<>();
        this.predictionService = new AdvancedPredictionService();
        
        initializeHistoricalData();
        
        logger.info("📊 Dashboard Avançado de Predição inicializado");
    }
    
    /**
     * Inicializa dados históricos simulados
     */
    private void initializeHistoricalData() {
        for (ResourceType type : ResourceType.values()) {
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                data.add(ThreadLocalRandom.current().nextDouble(20, 80));
            }
            historicalMetrics.put(type, data);
        }
    }
    
    /**
     * Atualiza predições para todos os recursos
     */
    public void updatePredictions() {
        for (ResourceType type : ResourceType.values()) {
            updatePredictionForResource(type);
        }
        
        logger.info("🔮 Predições atualizadas para {} recursos", ResourceType.values().length);
    }
    
    /**
     * Atualiza predição para um recurso específico
     */
    private void updatePredictionForResource(ResourceType type) {
        // Prepara dados de input
        Map<String, Object> inputData = prepareInputData(type);
        
        // Gera predição usando o serviço
        var prediction = predictionService.generatePrediction(
            convertToPredictionType(type), inputData
        );
        
        if (prediction != null) {
            // Simula múltiplos pontos de predição
            List<Double> predictions = new ArrayList<>();
            for (int i = 0; i < 24; i++) { // Próximas 24 horas
                double baseValue = prediction.getValue();
                double variation = ThreadLocalRandom.current().nextGaussian() * 0.1;
                predictions.add(Math.max(0, Math.min(100, baseValue + variation * 10)));
            }
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("model", prediction.getMetadata().get("model_name"));
            metadata.put("confidence_factors", prediction.getSupportingFactors());
            
            PredictionResult result = new PredictionResult(type, predictions, 
                                                         prediction.getConfidence(), metadata);
            
            predictionResults.put(type, result);
            
            // Verifica se precisa gerar alerta
            checkForAlerts(type, result);
        }
    }
    
    /**
     * Prepara dados de input para predição
     */
    private Map<String, Object> prepareInputData(ResourceType type) {
        Map<String, Object> inputData = new HashMap<>();
        
        List<Double> historicalData = historicalMetrics.get(type);
        if (historicalData != null && !historicalData.isEmpty()) {
            // Adiciona dados históricos recentes
            int recentCount = Math.min(10, historicalData.size());
            List<Double> recentData = historicalData.subList(
                historicalData.size() - recentCount, historicalData.size()
            );
            
            inputData.put("historical_values", recentData);
            inputData.put("current_value", recentData.get(recentData.size() - 1));
            inputData.put("avg_value", recentData.stream().mapToDouble(Double::doubleValue).average().orElse(0));
            inputData.put("trend", calculateTrend(recentData));
            inputData.put("volatility", calculateVolatility(recentData));
        }
        
        // Adiciona métricas específicas do tipo
        switch (type) {
            case CPU:
                inputData.put("core_count", Runtime.getRuntime().availableProcessors());
                inputData.put("load_average", ThreadLocalRandom.current().nextDouble(0.5, 2.0));
                break;
            case MEMORY:
                Runtime runtime = Runtime.getRuntime();
                long totalMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();
                inputData.put("total_memory", totalMemory);
                inputData.put("free_memory", freeMemory);
                inputData.put("used_memory", totalMemory - freeMemory);
                break;
            case DISK:
                inputData.put("disk_io", ThreadLocalRandom.current().nextDouble(0, 100));
                inputData.put("disk_space", ThreadLocalRandom.current().nextDouble(0, 100));
                break;
            case NETWORK:
                inputData.put("bandwidth", ThreadLocalRandom.current().nextDouble(0, 1000));
                inputData.put("latency", ThreadLocalRandom.current().nextDouble(1, 100));
                break;
        }
        
        return inputData;
    }
    
    /**
     * Converte ResourceType para PredictionType
     */
    private AdvancedPredictionService.PredictionType convertToPredictionType(ResourceType type) {
        switch (type) {
            case CPU:
            case MEMORY:
                return AdvancedPredictionService.PredictionType.VOLATILITY;
            case DISK:
                return AdvancedPredictionService.PredictionType.VOLUME;
            case NETWORK:
                return AdvancedPredictionService.PredictionType.TREND;
            default:
                return AdvancedPredictionService.PredictionType.PRICE_MOVEMENT;
        }
    }
    
    /**
     * Calcula tendência dos dados
     */
    private double calculateTrend(List<Double> data) {
        if (data.size() < 2) return 0.0;
        
        double firstHalf = data.subList(0, data.size() / 2).stream()
                .mapToDouble(Double::doubleValue).average().orElse(0);
        double secondHalf = data.subList(data.size() / 2, data.size()).stream()
                .mapToDouble(Double::doubleValue).average().orElse(0);
        
        return secondHalf - firstHalf;
    }
    
    /**
     * Calcula volatilidade dos dados
     */
    private double calculateVolatility(List<Double> data) {
        if (data.size() < 2) return 0.0;
        
        double mean = data.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = data.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .average().orElse(0);
        
        return Math.sqrt(variance);
    }
    
    /**
     * Verifica se precisa gerar alertas
     */
    private void checkForAlerts(ResourceType type, PredictionResult result) {
        List<Double> predictions = result.getPredictions();
        if (predictions.isEmpty()) return;
        
        // Verifica picos previstos
        double maxPrediction = predictions.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double avgPrediction = predictions.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        
        if (maxPrediction > 90) {
            createAlert(type, "ALTO USO PREVISTO", 
                       String.format("Uso de %s pode atingir %.1f%% nas próximas horas", type, maxPrediction),
                       SeverityLevel.HIGH);
        }
        
        if (avgPrediction > 80) {
            createAlert(type, "USO ELEVADO SUSTENTADO",
                       String.format("Uso médio de %s previsto em %.1f%%", type, avgPrediction),
                       SeverityLevel.MEDIUM);
        }
        
        // Verifica anomalias
        double currentUsage = getCurrentUsage(type);
        if (Math.abs(maxPrediction - currentUsage) > 30) {
            createAlert(type, "ANOMALIA DETECTADA",
                       String.format("Variação anormal prevista para %s: %.1f%% → %.1f%%", 
                                   type, currentUsage, maxPrediction),
                       SeverityLevel.LOW);
        }
    }
    
    /**
     * Cria um alerta
     */
    private void createAlert(ResourceType type, String title, String message, SeverityLevel severity) {
        String alertId = type.toString() + "_" + System.currentTimeMillis();
        
        Map<String, Object> context = new HashMap<>();
        context.put("resource_type", type);
        context.put("current_usage", getCurrentUsage(type));
        context.put("prediction", predictionResults.get(type));
        
        Alert alert = new Alert(alertId, title, message, severity, true, context);
        activeAlerts.put(alertId, alert);
        
        logger.warn("🚨 Alerta criado: {} - {}", title, message);
    }
    
    /**
     * Obtém uso atual do recurso (simulado)
     */
    private double getCurrentUsage(ResourceType type) {
        List<Double> historical = historicalMetrics.get(type);
        if (historical != null && !historical.isEmpty()) {
            return historical.get(historical.size() - 1);
        }
        return ThreadLocalRandom.current().nextDouble(20, 80);
    }
    
    /**
     * Detecta incidentes no sistema
     */
    public void detectSystemIncidents() {
        // Simula detecção de incidentes
        if (ThreadLocalRandom.current().nextDouble() < 0.1) { // 10% de chance
            String[] incidentTypes = {"CPU_OVERLOAD", "MEMORY_LEAK", "DISK_FULL", "NETWORK_FAILURE"};
            String type = incidentTypes[ThreadLocalRandom.current().nextInt(incidentTypes.length)];
            
            String description = generateIncidentDescription(type);
            SeverityLevel severity = generateIncidentSeverity();
            double probability = ThreadLocalRandom.current().nextDouble(0.3, 0.9);
            
            Map<String, Object> details = new HashMap<>();
            details.put("affected_systems", Arrays.asList("trading_engine", "prediction_service"));
            details.put("estimated_downtime", ThreadLocalRandom.current().nextInt(5, 60));
            details.put("impact_level", severity.toString());
            
            SystemIncident incident = new SystemIncident(type, description, severity, probability, details);
            systemIncidents.add(incident);
            
            logger.warn("⚠️ Incidente detectado: {} - {}", type, description);
        }
        
        // Limita tamanho da lista
        if (systemIncidents.size() > 100) {
            systemIncidents.subList(0, 50).clear();
        }
    }
    
    /**
     * Gera descrição de incidente
     */
    private String generateIncidentDescription(String type) {
        switch (type) {
            case "CPU_OVERLOAD":
                return "Uso excessivo de CPU detectado";
            case "MEMORY_LEAK":
                return "Vazamento de memória identificado";
            case "DISK_FULL":
                return "Espaço em disco esgotando";
            case "NETWORK_FAILURE":
                return "Falha de conectividade de rede";
            default:
                return "Incidente genérico do sistema";
        }
    }
    
    /**
     * Gera severidade de incidente
     */
    private SeverityLevel generateIncidentSeverity() {
        double rand = ThreadLocalRandom.current().nextDouble();
        if (rand < 0.1) return SeverityLevel.CRITICAL;
        if (rand < 0.3) return SeverityLevel.HIGH;
        if (rand < 0.6) return SeverityLevel.MEDIUM;
        return SeverityLevel.LOW;
    }
    
    /**
     * Analisa riscos de segurança
     */
    public void analyzeSecurityRisks() {
        // Simula análise de riscos de segurança
        if (ThreadLocalRandom.current().nextDouble() < 0.15) { // 15% de chance
            String[] riskTypes = {"UNAUTHORIZED_ACCESS", "DATA_BREACH", "DDOS_ATTACK", "MALWARE"};
            String type = riskTypes[ThreadLocalRandom.current().nextInt(riskTypes.length)];
            
            String level = generateRiskLevel();
            String description = generateRiskDescription(type);
            double probability = ThreadLocalRandom.current().nextDouble(0.1, 0.8);
            
            Map<String, Object> riskFactors = new HashMap<>();
            riskFactors.put("source_ip", "192.168.1." + ThreadLocalRandom.current().nextInt(255));
            riskFactors.put("attack_vector", type);
            riskFactors.put("affected_assets", Arrays.asList("database", "api_server"));
            
            SecurityRisk risk = new SecurityRisk(type, level, description, probability, riskFactors);
            securityRisks.add(risk);
            
            logger.error("🔒 Risco de segurança: {} - {}", type, description);
        }
        
        // Limita tamanho da lista
        if (securityRisks.size() > 50) {
            securityRisks.subList(0, 25).clear();
        }
    }
    
    /**
     * Gera nível de risco
     */
    private String generateRiskLevel() {
        double rand = ThreadLocalRandom.current().nextDouble();
        if (rand < 0.2) return "CRITICAL";
        if (rand < 0.4) return "HIGH";
        if (rand < 0.7) return "MEDIUM";
        return "LOW";
    }
    
    /**
     * Gera descrição de risco
     */
    private String generateRiskDescription(String type) {
        switch (type) {
            case "UNAUTHORIZED_ACCESS":
                return "Tentativa de acesso não autorizado detectada";
            case "DATA_BREACH":
                return "Possível vazamento de dados identificado";
            case "DDOS_ATTACK":
                return "Ataque de negação de serviço em andamento";
            case "MALWARE":
                return "Software malicioso detectado no sistema";
            default:
                return "Risco de segurança genérico";
        }
    }
    
    /**
     * Gera métricas atuais do sistema
     */
    public SystemMetrics generateSystemMetrics() {
        Map<ResourceType, Double> currentUsage = new HashMap<>();
        Map<ResourceType, List<Double>> historicalData = new HashMap<>();
        Map<ResourceType, Double> predictions = new HashMap<>();
        
        for (ResourceType type : ResourceType.values()) {
            // Uso atual
            currentUsage.put(type, getCurrentUsage(type));
            
            // Dados históricos
            historicalData.put(type, new ArrayList<>(historicalMetrics.get(type)));
            
            // Predições
            PredictionResult result = predictionResults.get(type);
            if (result != null && !result.getPredictions().isEmpty()) {
                predictions.put(type, result.getPredictions().get(0)); // Primeira predição
            } else {
                predictions.put(type, ThreadLocalRandom.current().nextDouble(20, 80));
            }
        }
        
        return new SystemMetrics(currentUsage, historicalData, predictions);
    }
    
    /**
     * Obtém resultados de predição
     */
    public Map<ResourceType, PredictionResult> getPredictionResults() {
        return new HashMap<>(predictionResults);
    }
    
    /**
     * Obtém incidentes do sistema
     */
    public List<SystemIncident> getSystemIncidents() {
        return new ArrayList<>(systemIncidents);
    }
    
    /**
     * Obtém riscos de segurança
     */
    public List<SecurityRisk> getSecurityRisks() {
        return new ArrayList<>(securityRisks);
    }
    
    /**
     * Obtém alertas ativos
     */
    public Map<String, Alert> getActiveAlerts() {
        return new HashMap<>(activeAlerts);
    }
    
    /**
     * Remove um alerta
     */
    public void dismissAlert(String alertId) {
        Alert alert = activeAlerts.get(alertId);
        if (alert != null) {
            // Em Java, criaríamos uma nova instância com isActive = false
            // Por simplicidade, apenas removemos do mapa
            activeAlerts.remove(alertId);
            logger.info("✅ Alerta removido: {}", alertId);
        }
    }
    
    /**
     * Obtém estatísticas do dashboard
     */
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long criticalAlerts = activeAlerts.values().stream()
                .filter(alert -> alert.getSeverity() == SeverityLevel.CRITICAL)
                .count();
        
        long highSeverityIncidents = systemIncidents.stream()
                .filter(incident -> incident.getSeverity() == SeverityLevel.HIGH)
                .count();
        
        long criticalSecurityRisks = securityRisks.stream()
                .filter(risk -> "CRITICAL".equals(risk.getLevel()))
                .count();
        
        double avgPredictionConfidence = predictionResults.values().stream()
                .mapToDouble(PredictionResult::getConfidence)
                .average()
                .orElse(0.0);
        
        stats.put("totalAlerts", activeAlerts.size());
        stats.put("criticalAlerts", criticalAlerts);
        stats.put("totalIncidents", systemIncidents.size());
        stats.put("highSeverityIncidents", highSeverityIncidents);
        stats.put("totalSecurityRisks", securityRisks.size());
        stats.put("criticalSecurityRisks", criticalSecurityRisks);
        stats.put("activePredictions", predictionResults.size());
        stats.put("avgPredictionConfidence", avgPredictionConfidence);
        
        return stats;
    }
    
    /**
     * Limpa dados antigos
     */
    public void cleanupOldData(int maxAgeHours) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(maxAgeHours);
        
        // Remove incidentes antigos
        systemIncidents.removeIf(incident -> incident.getTimestamp().isBefore(cutoff));
        
        // Remove riscos de segurança antigos
        securityRisks.removeIf(risk -> risk.getTimestamp().isBefore(cutoff));
        
        // Remove alertas antigos (exceto críticos)
        activeAlerts.entrySet().removeIf(entry -> {
            Alert alert = entry.getValue();
            return alert.getTimestamp().isBefore(cutoff) && 
                   alert.getSeverity() != SeverityLevel.CRITICAL;
        });
        
        logger.info("🧹 Limpeza de dados antigos do dashboard concluída");
    }
}
