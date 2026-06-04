// VhalinorConfig.java
package vhalinor.config;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Enums
enum SystemMode {
    DEVELOPMENT("development"),
    PRODUCTION("production"),
    TESTING("testing"),
    DEMO("demo");
    
    private final String value;
    
    SystemMode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static SystemMode fromValue(String value) {
        for (SystemMode mode : values()) {
            if (mode.value.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return DEVELOPMENT;
    }
}

enum PerformanceMode {
    ECO("eco"),
    BALANCED("balanced"),
    PERFORMANCE("performance"),
    QUANTUM("quantum");
    
    private final String value;
    
    PerformanceMode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static PerformanceMode fromValue(String value) {
        for (PerformanceMode mode : values()) {
            if (mode.value.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return BALANCED;
    }
}

// Main Configuration Class
class VhalinorConfig {
    // Informações do sistema
    private String systemName;
    private String version;
    private String fullName;
    
    // Modos de operação
    private SystemMode systemMode;
    private PerformanceMode performanceMode;
    
    // Caminhos do sistema
    private String basePath;
    private String quantumPath;
    private String modelsPath;
    private String logsPath;
    private String dataPath;
    
    // Configurações de rede neural
    private int maxNeurons;
    private int maxConnections;
    private double activationThreshold;
    private double learningRate;
    
    // Configurações de memória
    private int memoryCapacityMb;
    private int cacheSize;
    private double cleanupThreshold;
    
    // Configurações quânticas
    private boolean quantumEnabled;
    private int quantumQubits;
    private int quantumDepth;
    private double entanglementThreshold;
    
    // Configurações de ML
    private boolean mlEnabled;
    private int batchSize;
    private int trainingInterval;
    private double anomalyThreshold;
    
    // Configurações de interface
    private boolean guiEnabled;
    private double updateInterval;
    private int chartHistory;
    
    // Configurações de energia
    private double initialEnergy;
    private double energyRegenRate;
    private double energyConsumptionRate;
    
    // Configurações de logging
    private String logLevel;
    private boolean logToFile;
    private boolean logToConsole;
    private int maxLogSizeMb;
    
    // Configurações de segurança
    private boolean enableEncryption;
    private boolean apiKeyRequired;
    private int maxConcurrentUsers;
    
    // Construtor padrão
    public VhalinorConfig() {
        // Informações do sistema
        this.systemName = "VHALINOR.IAG";
        this.version = "4.5.0";
        this.fullName = "Virtual Hybrid Advanced Learning Intelligence Neural Optimized Reasoning";
        
        // Modos de operação
        this.systemMode = SystemMode.DEVELOPMENT;
        this.performanceMode = PerformanceMode.BALANCED;
        
        // Caminhos do sistema
        this.basePath = "./vhalinor_iag";
        this.quantumPath = "./quantum";
        this.modelsPath = "./models";
        this.logsPath = "./logs";
        this.dataPath = "./data";
        
        // Configurações de rede neural
        this.maxNeurons = 10000;
        this.maxConnections = 50000;
        this.activationThreshold = 0.5;
        this.learningRate = 0.001;
        
        // Configurações de memória
        this.memoryCapacityMb = 100;
        this.cacheSize = 1000;
        this.cleanupThreshold = 0.8;
        
        // Configurações quânticas
        this.quantumEnabled = true;
        this.quantumQubits = 5;
        this.quantumDepth = 3;
        this.entanglementThreshold = 0.7;
        
        // Configurações de ML
        this.mlEnabled = true;
        this.batchSize = 100;
        this.trainingInterval = 3600; // segundos
        this.anomalyThreshold = -0.5;
        
        // Configurações de interface
        this.guiEnabled = true;
        this.updateInterval = 1.0; // segundos
        this.chartHistory = 100;
        
        // Configurações de energia
        this.initialEnergy = 1000.0;
        this.energyRegenRate = 0.1;
        this.energyConsumptionRate = 0.01;
        
        // Configurações de logging
        this.logLevel = "INFO";
        this.logToFile = true;
        this.logToConsole = true;
        this.maxLogSizeMb = 10;
        
        // Configurações de segurança
        this.enableEncryption = false;
        this.apiKeyRequired = false;
        this.maxConcurrentUsers = 10;
        
        // Inicialização pós-criação
        createDirectories();
        validateConfig();
    }
    
    // Construtor personalizado
    public VhalinorConfig(SystemMode systemMode, PerformanceMode performanceMode,
                         int maxNeurons, int memoryCapacityMb, String logLevel) {
        this(); // Chama o construtor padrão primeiro
        this.systemMode = systemMode;
        this.performanceMode = performanceMode;
        this.maxNeurons = maxNeurons;
        this.memoryCapacityMb = memoryCapacityMb;
        this.logLevel = logLevel;
        
        // Revalidar após mudanças
        validateConfig();
    }
    
    // Cria diretórios necessários
    private void createDirectories() {
        List<String> directories = Arrays.asList(
            basePath, quantumPath, modelsPath, logsPath, dataPath
        );
        
        for (String dir : directories) {
            try {
                Path path = Paths.get(dir);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar diretório " + dir + ": " + e.getMessage());
            }
        }
    }
    
    // Valida configurações
    private void validateConfig() {
        if (maxNeurons <= 0) {
            throw new IllegalArgumentException("maxNeurons deve ser positivo");
        }
        
        if (learningRate <= 0 || learningRate >= 1) {
            throw new IllegalArgumentException("learningRate deve estar entre 0 e 1");
        }
        
        if (memoryCapacityMb <= 0) {
            throw new IllegalArgumentException("memoryCapacityMb deve ser positivo");
        }
        
        if (quantumQubits < 1 || quantumQubits > 20) {
            throw new IllegalArgumentException("quantumQubits deve estar entre 1 e 20");
        }
    }
    
    // Converte para mapa
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        
        map.put("system_name", systemName);
        map.put("version", version);
        map.put("full_name", fullName);
        map.put("system_mode", systemMode.getValue());
        map.put("performance_mode", performanceMode.getValue());
        
        Map<String, String> paths = new LinkedHashMap<>();
        paths.put("base", basePath);
        paths.put("quantum", quantumPath);
        paths.put("models", modelsPath);
        paths.put("logs", logsPath);
        paths.put("data", dataPath);
        map.put("paths", paths);
        
        Map<String, Object> neuralConfig = new LinkedHashMap<>();
        neuralConfig.put("max_neurons", maxNeurons);
        neuralConfig.put("max_connections", maxConnections);
        neuralConfig.put("activation_threshold", activationThreshold);
        neuralConfig.put("learning_rate", learningRate);
        map.put("neural_config", neuralConfig);
        
        Map<String, Object> memoryConfig = new LinkedHashMap<>();
        memoryConfig.put("capacity_mb", memoryCapacityMb);
        memoryConfig.put("cache_size", cacheSize);
        memoryConfig.put("cleanup_threshold", cleanupThreshold);
        map.put("memory_config", memoryConfig);
        
        Map<String, Object> quantumConfig = new LinkedHashMap<>();
        quantumConfig.put("enabled", quantumEnabled);
        quantumConfig.put("qubits", quantumQubits);
        quantumConfig.put("depth", quantumDepth);
        quantumConfig.put("entanglement_threshold", entanglementThreshold);
        map.put("quantum_config", quantumConfig);
        
        Map<String, Object> mlConfig = new LinkedHashMap<>();
        mlConfig.put("enabled", mlEnabled);
        mlConfig.put("batch_size", batchSize);
        mlConfig.put("training_interval", trainingInterval);
        mlConfig.put("anomaly_threshold", anomalyThreshold);
        map.put("ml_config", mlConfig);
        
        return map;
    }
    
    // Getters and Setters
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public SystemMode getSystemMode() { return systemMode; }
    public void setSystemMode(SystemMode systemMode) { 
        this.systemMode = systemMode; 
        validateConfig();
    }
    
    public PerformanceMode getPerformanceMode() { return performanceMode; }
    public void setPerformanceMode(PerformanceMode performanceMode) { 
        this.performanceMode = performanceMode; 
    }
    
    public String getBasePath() { return basePath; }
    public void setBasePath(String basePath) { 
        this.basePath = basePath; 
        createDirectories();
    }
    
    public String getQuantumPath() { return quantumPath; }
    public void setQuantumPath(String quantumPath) { 
        this.quantumPath = quantumPath; 
        createDirectories();
    }
    
    public String getModelsPath() { return modelsPath; }
    public void setModelsPath(String modelsPath) { 
        this.modelsPath = modelsPath; 
        createDirectories();
    }
    
    public String getLogsPath() { return logsPath; }
    public void setLogsPath(String logsPath) { 
        this.logsPath = logsPath; 
        createDirectories();
    }
    
    public String getDataPath() { return dataPath; }
    public void setDataPath(String dataPath) { 
        this.dataPath = dataPath; 
        createDirectories();
    }
    
    public int getMaxNeurons() { return maxNeurons; }
    public void setMaxNeurons(int maxNeurons) { 
        this.maxNeurons = maxNeurons; 
        validateConfig();
    }
    
    public int getMaxConnections() { return maxConnections; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
    
    public double getActivationThreshold() { return activationThreshold; }
    public void setActivationThreshold(double activationThreshold) { 
        this.activationThreshold = activationThreshold; 
    }
    
    public double getLearningRate() { return learningRate; }
    public void setLearningRate(double learningRate) { 
        this.learningRate = learningRate; 
        validateConfig();
    }
    
    public int getMemoryCapacityMb() { return memoryCapacityMb; }
    public void setMemoryCapacityMb(int memoryCapacityMb) { 
        this.memoryCapacityMb = memoryCapacityMb; 
        validateConfig();
    }
    
    public int getCacheSize() { return cacheSize; }
    public void setCacheSize(int cacheSize) { this.cacheSize = cacheSize; }
    
    public double getCleanupThreshold() { return cleanupThreshold; }
    public void setCleanupThreshold(double cleanupThreshold) { this.cleanupThreshold = cleanupThreshold; }
    
    public boolean isQuantumEnabled() { return quantumEnabled; }
    public void setQuantumEnabled(boolean quantumEnabled) { this.quantumEnabled = quantumEnabled; }
    
    public int getQuantumQubits() { return quantumQubits; }
    public void setQuantumQubits(int quantumQubits) { 
        this.quantumQubits = quantumQubits; 
        validateConfig();
    }
    
    public int getQuantumDepth() { return quantumDepth; }
    public void setQuantumDepth(int quantumDepth) { this.quantumDepth = quantumDepth; }
    
    public double getEntanglementThreshold() { return entanglementThreshold; }
    public void setEntanglementThreshold(double entanglementThreshold) { 
        this.entanglementThreshold = entanglementThreshold; 
    }
    
    public boolean isMlEnabled() { return mlEnabled; }
    public void setMlEnabled(boolean mlEnabled) { this.mlEnabled = mlEnabled; }
    
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
    
    public int getTrainingInterval() { return trainingInterval; }
    public void setTrainingInterval(int trainingInterval) { this.trainingInterval = trainingInterval; }
    
    public double getAnomalyThreshold() { return anomalyThreshold; }
    public void setAnomalyThreshold(double anomalyThreshold) { this.anomalyThreshold = anomalyThreshold; }
    
    public boolean isGuiEnabled() { return guiEnabled; }
    public void setGuiEnabled(boolean guiEnabled) { this.guiEnabled = guiEnabled; }
    
    public double getUpdateInterval() { return updateInterval; }
    public void setUpdateInterval(double updateInterval) { this.updateInterval = updateInterval; }
    
    public int getChartHistory() { return chartHistory; }
    public void setChartHistory(int chartHistory) { this.chartHistory = chartHistory; }
    
    public double getInitialEnergy() { return initialEnergy; }
    public void setInitialEnergy(double initialEnergy) { this.initialEnergy = initialEnergy; }
    
    public double getEnergyRegenRate() { return energyRegenRate; }
    public void setEnergyRegenRate(double energyRegenRate) { this.energyRegenRate = energyRegenRate; }
    
    public double getEnergyConsumptionRate() { return energyConsumptionRate; }
    public void setEnergyConsumptionRate(double energyConsumptionRate) { 
        this.energyConsumptionRate = energyConsumptionRate; 
    }
    
    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    
    public boolean isLogToFile() { return logToFile; }
    public void setLogToFile(boolean logToFile) { this.logToFile = logToFile; }
    
    public boolean isLogToConsole() { return logToConsole; }
    public void setLogToConsole(boolean logToConsole) { this.logToConsole = logToConsole; }
    
    public int getMaxLogSizeMb() { return maxLogSizeMb; }
    public void setMaxLogSizeMb(int maxLogSizeMb) { this.maxLogSizeMb = maxLogSizeMb; }
    
    public boolean isEnableEncryption() { return enableEncryption; }
    public void setEnableEncryption(boolean enableEncryption) { this.enableEncryption = enableEncryption; }
    
    public boolean isApiKeyRequired() { return apiKeyRequired; }
    public void setApiKeyRequired(boolean apiKeyRequired) { this.apiKeyRequired = apiKeyRequired; }
    
    public int getMaxConcurrentUsers() { return maxConcurrentUsers; }
    public void setMaxConcurrentUsers(int maxConcurrentUsers) { this.maxConcurrentUsers = maxConcurrentUsers; }
}

// Neuron Configuration Class
class NeuronConfig {
    // Thresholds
    public static final double SENSORY_THRESHOLD = 0.3;
    public static final double PROCESSING_THRESHOLD = 0.5;
    public static final double MEMORY_THRESHOLD = 0.7;
    public static final double DECISION_THRESHOLD = 0.8;
    public static final double OUTPUT_THRESHOLD = 0.6;
    public static final double QUANTUM_THRESHOLD = 0.9;
    
    // Energy costs
    private static final Map<String, Double> ENERGY_COSTS = new HashMap<>();
    
    static {
        ENERGY_COSTS.put("sensory", 0.01);
        ENERGY_COSTS.put("processing", 0.02);
        ENERGY_COSTS.put("memory", 0.03);
        ENERGY_COSTS.put("decision", 0.05);
        ENERGY_COSTS.put("output", 0.02);
        ENERGY_COSTS.put("quantum", 0.1);
    }
    
    public static double getEnergyCost(String neuronType) {
        return ENERGY_COSTS.getOrDefault(neuronType, 0.01);
    }
}

// Quantum Configuration Class
class QuantumConfig {
    public static final int DEFAULT_QUBITS = 5;
    public static final int MAX_QUBITS = 10;
    public static final int DEFAULT_DEPTH = 3;
    public static final int MAX_DEPTH = 10;
    
    private static final Map<String, Double> GATE_PROBABILITIES = new HashMap<>();
    
    static {
        GATE_PROBABILITIES.put("H", 0.3);    // Hadamard
        GATE_PROBABILITIES.put("X", 0.2);    // Pauli-X
        GATE_PROBABILITIES.put("Y", 0.1);    // Pauli-Y
        GATE_PROBABILITIES.put("Z", 0.1);    // Pauli-Z
        GATE_PROBABILITIES.put("CNOT", 0.3); // CNOT
    }
    
    public static double getGateProbability(String gate) {
        return GATE_PROBABILITIES.getOrDefault(gate, 0.0);
    }
    
    public static List<String> getAvailableGates() {
        return new ArrayList<>(GATE_PROBABILITIES.keySet());
    }
}

// ML Configuration Class
class MLConfig {
    public static final int DEFAULT_BATCH_SIZE = 100;
    public static final int MIN_TRAINING_SAMPLES = 50;
    public static final int MAX_TRAINING_SAMPLES = 10000;
    
    public static final double ANOMALY_CONTAMINATION = 0.1;
    public static final int PCA_COMPONENTS = 10;
    public static final int KMEANS_CLUSTERS = 8;
    
    public static final int FEATURE_CACHE_SIZE = 1000;
    public static final int PREDICTION_CACHE_TTL = 3600; // segundos
    
    private MLConfig() {} // Classe utilitária - não instanciável
}

// Configuration Factory
class ConfigFactory {
    
    // Configurações predefinidas
    private static final Map<String, VhalinorConfig> PREDEFINED_CONFIGS = new HashMap<>();
    
    static {
        // Configuração padrão
        PREDEFINED_CONFIGS.put("default", new VhalinorConfig());
        
        // Configuração para desenvolvimento
        VhalinorConfig devConfig = new VhalinorConfig(
            SystemMode.DEVELOPMENT,
            PerformanceMode.BALANCED,
            1000,
            50,
            "DEBUG"
        );
        PREDEFINED_CONFIGS.put("dev", devConfig);
        PREDEFINED_CONFIGS.put("development", devConfig);
        
        // Configuração para produção
        VhalinorConfig prodConfig = new VhalinorConfig(
            SystemMode.PRODUCTION,
            PerformanceMode.PERFORMANCE,
            50000,
            500,
            "WARNING"
        );
        prodConfig.setEnableEncryption(true);
        PREDEFINED_CONFIGS.put("prod", prodConfig);
        PREDEFINED_CONFIGS.put("production", prodConfig);
        
        // Configuração para testes
        VhalinorConfig testConfig = new VhalinorConfig(
            SystemMode.TESTING,
            PerformanceMode.ECO,
            100,
            10,
            "ERROR"
        );
        testConfig.setGuiEnabled(false);
        PREDEFINED_CONFIGS.put("test", testConfig);
        PREDEFINED_CONFIGS.put("testing", testConfig);
        
        // Configuração para demo
        VhalinorConfig demoConfig = new VhalinorConfig(
            SystemMode.DEMO,
            PerformanceMode.BALANCED,
            5000,
            100,
            "INFO"
        );
        demoConfig.setQuantumEnabled(true);
        PREDEFINED_CONFIGS.put("demo", demoConfig);
    }
    
    public static VhalinorConfig getConfig(String mode) {
        return PREDEFINED_CONFIGS.getOrDefault(mode.toLowerCase(), 
               PREDEFINED_CONFIGS.get("default"));
    }
    
    public static VhalinorConfig loadFromEnvironment() {
        VhalinorConfig config = new VhalinorConfig();
        
        // Sobrescreve com variáveis de ambiente se existirem
        String mode = System.getenv("VHALINOR_MODE");
        if (mode != null) {
            config.setSystemMode(SystemMode.fromValue(mode));
        }
        
        String performance = System.getenv("VHALINOR_PERFORMANCE");
        if (performance != null) {
            config.setPerformanceMode(PerformanceMode.fromValue(performance));
        }
        
        String maxNeurons = System.getenv("VHALINOR_MAX_NEURONS");
        if (maxNeurons != null) {
            try {
                config.setMaxNeurons(Integer.parseInt(maxNeurons));
            } catch (NumberFormatException e) {
                System.err.println("VHALINOR_MAX_NEURONS inválido: " + maxNeurons);
            }
        }
        
        String memoryMb = System.getenv("VHALINOR_MEMORY_MB");
        if (memoryMb != null) {
            try {
                config.setMemoryCapacityMb(Integer.parseInt(memoryMb));
            } catch (NumberFormatException e) {
                System.err.println("VHALINOR_MEMORY_MB inválido: " + memoryMb);
            }
        }
        
        String logLevel = System.getenv("VHALINOR_LOG_LEVEL");
        if (logLevel != null) {
            config.setLogLevel(logLevel);
        }
        
        return config;
    }
}

// JSON Utilities (simplified)
class JsonUtils {
    
    public static String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            
            sb.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value instanceof Map) {
                sb.append(mapToJson((Map<String, Object>) value));
            } else {
                sb.append("\"").append(value.toString()).append("\"");
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    public static String prettyPrint(Map<String, Object> map) {
        String json = mapToJson(map);
        return formatJson(json);
    }
    
    private static String formatJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean inQuote = false;
        
        for (char c : json.toCharArray()) {
            switch (c) {
                case '"':
                    inQuote = !inQuote;
                    formatted.append(c);
                    break;
                case '{':
                case '[':
                    formatted.append(c);
                    if (!inQuote) {
                        formatted.append("\n");
                        indentLevel++;
                        formatted.append("  ".repeat(indentLevel));
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuote) {
                        formatted.append("\n");
                        indentLevel--;
                        formatted.append("  ".repeat(indentLevel));
                    }
                    formatted.append(c);
                    break;
                case ',':
                    formatted.append(c);
                    if (!inQuote) {
                        formatted.append("\n");
                        formatted.append("  ".repeat(indentLevel));
                    }
                    break;
                case ':':
                    formatted.append(c).append(" ");
                    break;
                default:
                    formatted.append(c);
            }
        }
        
        return formatted.toString();
    }
}

// Configuration File Manager
class ConfigFileManager {
    
    public static void saveConfigToFile(VhalinorConfig config, String filepath) {
        try {
            Map<String, Object> configMap = config.toMap();
            String json = JsonUtils.prettyPrint(configMap);
            
            Files.write(Paths.get(filepath), json.getBytes());
            System.out.println("✅ Configuração salva em " + filepath);
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar configuração: " + e.getMessage());
        }
    }
    
    public static VhalinorConfig loadConfigFromFile(String filepath) {
        try {
            Path path = Paths.get(filepath);
            if (!Files.exists(path)) {
                System.out.println("Arquivo " + filepath + " não encontrado, usando configuração padrão");
                return new VhalinorConfig();
            }
            
            String content = new String(Files.readAllBytes(path));
            // Simplified JSON parsing - in production use a proper JSON library
            VhalinorConfig config = new VhalinorConfig();
            
            // Basic parsing (simplified)
            if (content.contains("\"system_mode\"")) {
                String mode = extractValue(content, "system_mode");
                config.setSystemMode(SystemMode.fromValue(mode));
            }
            
            if (content.contains("\"performance_mode\"")) {
                String mode = extractValue(content, "performance_mode");
                config.setPerformanceMode(PerformanceMode.fromValue(mode));
            }
            
            if (content.contains("\"max_neurons\"")) {
                String value = extractValue(content, "max_neurons");
                try {
                    config.setMaxNeurons(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
            
            return config;
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar configuração: " + e.getMessage());
            return new VhalinorConfig();
        }
    }
    
    private static String extractValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start < 0) return "";
        
        start += search.length();
        while (start < json.length() && 
               (json.charAt(start) == ' ' || json.charAt(start) == '"')) {
            start++;
        }
        
        int end = start;
        while (end < json.length() && json.charAt(end) != ',' && 
               json.charAt(end) != '}' && json.charAt(end) != '\n') {
            end++;
        }
        
        return json.substring(start, end).trim().replace("\"", "");
    }
}

// Main Configuration System
public class VhalinorConfigSystem {
    
    public static void main(String[] args) {
        System.out.println("VHALINOR.IAG - Sistema de Configuração");
        System.out.println("=".repeat(50));
        
        // Configuração padrão
        VhalinorConfig config = ConfigFactory.getConfig("default");
        System.out.println("Sistema: " + config.getSystemName());
        System.out.println("Versão: " + config.getVersion());
        System.out.println("Nome completo: " + config.getFullName());
        System.out.println("Modo: " + config.getSystemMode().getValue());
        System.out.println("Performance: " + config.getPerformanceMode().getValue());
        
        // Salvar configuração
        ConfigFileManager.saveConfigToFile(config, "vhalinor_config.json");
        
        // Testar diferentes modos
        System.out.println("\n📊 Configurações disponíveis:");
        String[] modes = {"dev", "prod", "test", "demo"};
        for (String mode : modes) {
            VhalinorConfig cfg = ConfigFactory.getConfig(mode);
            System.out.printf("  %s: %d neurônios, %dMB memória%n",
                mode, cfg.getMaxNeurons(), cfg.getMemoryCapacityMb());
        }
        
        // Mostrar configurações quânticas
        System.out.println("\n⚛️ Configurações Quânticas:");
        System.out.println("  Qubits padrão: " + QuantumConfig.DEFAULT_QUBITS);
        System.out.println("  Qubits máximos: " + QuantumConfig.MAX_QUBITS);
        System.out.println("  Portas disponíveis: " + QuantumConfig.getAvailableGates());
        
        // Mostrar configurações de ML
        System.out.println("\n🤖 Configurações de ML:");
        System.out.println("  Batch size padrão: " + MLConfig.DEFAULT_BATCH_SIZE);
        System.out.println("  Amostras mínimas: " + MLConfig.MIN_TRAINING_SAMPLES);
        System.out.println("  Componentes PCA: " + MLConfig.PCA_COMPONENTS);
        
        // Testar carregamento de ambiente
        System.out.println("\n🌍 Configurações de ambiente:");
        VhalinorConfig envConfig = ConfigFactory.loadFromEnvironment();
        System.out.println("  Modo: " + envConfig.getSystemMode().getValue());
        System.out.println("  Performance: " + envConfig.getPerformanceMode().getValue());
        
        System.out.println("\n🎯 Sistema VHALINOR.IAG configurado com sucesso!");
    }
}