// AutonomousModuleConfig.java
package lextrader.autonomous.config;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// ============================================================================
// ENUMS
// ============================================================================

/**
 * Frequências de execução pré-definidas
 */
enum ExecutionFrequency {
    REAL_TIME("real_time", 0),
    EVERY_MINUTE("60", 60),
    EVERY_5_MINUTES("300", 300),
    EVERY_15_MINUTES("900", 900),
    EVERY_HOUR("3600", 3600),
    EVERY_4_HOURS("14400", 14400),
    EVERY_DAY("86400", 86400),
    EVERY_WEEK("604800", 604800),
    EVERY_MONTH("2592000", 2592000);
    
    private final String value;
    private final int seconds;
    
    ExecutionFrequency(String value, int seconds) {
        this.value = value;
        this.seconds = seconds;
    }
    
    public String getValue() {
        return value;
    }
    
    public int getSeconds() {
        return seconds;
    }
    
    public static ExecutionFrequency fromValue(String value) {
        for (ExecutionFrequency freq : values()) {
            if (freq.value.equals(value)) {
                return freq;
            }
        }
        return EVERY_HOUR;
    }
}

/**
 * Ambiente de execução
 */
enum Environment {
    PRODUCTION("production"),
    STAGING("staging"),
    DEVELOPMENT("development");
    
    private final String value;
    
    Environment(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static Environment fromValue(String value) {
        for (Environment env : values()) {
            if (env.value.equals(value)) {
                return env;
            }
        }
        return PRODUCTION;
    }
}

/**
 * Níveis de log
 */
enum LogLevel {
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    CRITICAL("CRITICAL");
    
    private final String value;
    
    LogLevel(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}

// ============================================================================
// CLASSES DE CONFIGURAÇÃO
// ============================================================================

/**
 * Configuração de uma ação autônoma
 */
class ActionConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private String actionType;
    private String priority;
    private String frequency;
    private boolean enabled;
    private int timeout;
    private int retryCount;
    private List<String> dependencies;
    private Map<String, Object> parameters;
    
    public ActionConfig() {
        this.name = "";
        this.description = "";
        this.actionType = "";
        this.priority = "MEDIUM";
        this.frequency = ExecutionFrequency.EVERY_HOUR.getValue();
        this.enabled = true;
        this.timeout = 300;
        this.retryCount = 3;
        this.dependencies = new ArrayList<>();
        this.parameters = new HashMap<>();
    }
    
    public ActionConfig(String name, String description, String actionType,
                       String priority, String frequency, boolean enabled,
                       int timeout, int retryCount, List<String> dependencies,
                       Map<String, Object> parameters) {
        this.name = name;
        this.description = description;
        this.actionType = actionType;
        this.priority = priority;
        this.frequency = frequency;
        this.enabled = enabled;
        this.timeout = timeout;
        this.retryCount = retryCount;
        this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
        this.parameters = parameters != null ? parameters : new HashMap<>();
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    
    /**
     * Converte para mapa
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("action_type", actionType);
        map.put("priority", priority);
        map.put("frequency", frequency);
        map.put("enabled", enabled);
        map.put("timeout", timeout);
        map.put("retry_count", retryCount);
        map.put("dependencies", dependencies);
        map.put("parameters", parameters);
        return map;
    }
    
    /**
     * Cria a partir de mapa
     */
    @SuppressWarnings("unchecked")
    public static ActionConfig fromMap(Map<String, Object> map) {
        ActionConfig config = new ActionConfig();
        
        if (map.containsKey("name")) config.name = (String) map.get("name");
        if (map.containsKey("description")) config.description = (String) map.get("description");
        if (map.containsKey("action_type")) config.actionType = (String) map.get("action_type");
        if (map.containsKey("priority")) config.priority = (String) map.get("priority");
        if (map.containsKey("frequency")) config.frequency = (String) map.get("frequency");
        if (map.containsKey("enabled")) config.enabled = (Boolean) map.get("enabled");
        if (map.containsKey("timeout")) config.timeout = ((Number) map.get("timeout")).intValue();
        if (map.containsKey("retry_count")) config.retryCount = ((Number) map.get("retry_count")).intValue();
        if (map.containsKey("dependencies")) config.dependencies = (List<String>) map.get("dependencies");
        if (map.containsKey("parameters")) config.parameters = (Map<String, Object>) map.get("parameters");
        
        return config;
    }
}

/**
 * Configuração completa do módulo autônomo
 */
class ModuleConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Configurações gerais
    private String moduleName;
    private String version;
    private String environment;
    
    // Configurações de execução
    private int maxConcurrentActions;
    private int healthCheckInterval;
    private int performanceLogInterval;
    
    // Configurações de logging
    private String logLevel;
    private String logFile;
    private int logMaxSize;
    private int logBackupCount;
    
    // Configurações de alerta
    private List<String> alertChannels;
    private boolean emailAlerts;
    private List<String> emailRecipients;
    private String slackWebhook;
    private String telegramBotToken;
    private String telegramChatId;
    
    // Configurações de monitoramento
    private boolean enableResourceMonitoring;
    private double cpuThreshold;
    private double memoryThreshold;
    private double diskThreshold;
    
    // Configurações de recuperação
    private boolean autoRecovery;
    private int maxFailuresBeforeDisable;
    private int failureCooldown;
    
    // Configurações de persistência
    private boolean persistActionHistory;
    private int historyRetentionDays;
    private int backupInterval;
    
    // Ações configuradas
    private List<ActionConfig> actions;
    
    public ModuleConfig() {
        // Configurações padrão
        this.moduleName = "LEXTRADER-IAG Autonomous Module";
        this.version = "4.0.0";
        this.environment = Environment.PRODUCTION.getValue();
        
        this.maxConcurrentActions = 10;
        this.healthCheckInterval = 60;
        this.performanceLogInterval = 300;
        
        this.logLevel = LogLevel.INFO.getValue();
        this.logFile = "autonomous_module.log";
        this.logMaxSize = 10 * 1024 * 1024; // 10MB
        this.logBackupCount = 5;
        
        this.alertChannels = new ArrayList<>();
        this.alertChannels.add("console");
        this.emailAlerts = false;
        this.emailRecipients = new ArrayList<>();
        this.slackWebhook = null;
        this.telegramBotToken = null;
        this.telegramChatId = null;
        
        this.enableResourceMonitoring = true;
        this.cpuThreshold = 80.0;
        this.memoryThreshold = 85.0;
        this.diskThreshold = 90.0;
        
        this.autoRecovery = true;
        this.maxFailuresBeforeDisable = 5;
        this.failureCooldown = 300;
        
        this.persistActionHistory = true;
        this.historyRetentionDays = 30;
        this.backupInterval = 86400; // 24 horas
        
        this.actions = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    
    public int getMaxConcurrentActions() { return maxConcurrentActions; }
    public void setMaxConcurrentActions(int maxConcurrentActions) { this.maxConcurrentActions = maxConcurrentActions; }
    
    public int getHealthCheckInterval() { return healthCheckInterval; }
    public void setHealthCheckInterval(int healthCheckInterval) { this.healthCheckInterval = healthCheckInterval; }
    
    public int getPerformanceLogInterval() { return performanceLogInterval; }
    public void setPerformanceLogInterval(int performanceLogInterval) { this.performanceLogInterval = performanceLogInterval; }
    
    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    
    public String getLogFile() { return logFile; }
    public void setLogFile(String logFile) { this.logFile = logFile; }
    
    public int getLogMaxSize() { return logMaxSize; }
    public void setLogMaxSize(int logMaxSize) { this.logMaxSize = logMaxSize; }
    
    public int getLogBackupCount() { return logBackupCount; }
    public void setLogBackupCount(int logBackupCount) { this.logBackupCount = logBackupCount; }
    
    public List<String> getAlertChannels() { return alertChannels; }
    public void setAlertChannels(List<String> alertChannels) { this.alertChannels = alertChannels; }
    
    public boolean isEmailAlerts() { return emailAlerts; }
    public void setEmailAlerts(boolean emailAlerts) { this.emailAlerts = emailAlerts; }
    
    public List<String> getEmailRecipients() { return emailRecipients; }
    public void setEmailRecipients(List<String> emailRecipients) { this.emailRecipients = emailRecipients; }
    
    public String getSlackWebhook() { return slackWebhook; }
    public void setSlackWebhook(String slackWebhook) { this.slackWebhook = slackWebhook; }
    
    public String getTelegramBotToken() { return telegramBotToken; }
    public void setTelegramBotToken(String telegramBotToken) { this.telegramBotToken = telegramBotToken; }
    
    public String getTelegramChatId() { return telegramChatId; }
    public void setTelegramChatId(String telegramChatId) { this.telegramChatId = telegramChatId; }
    
    public boolean isEnableResourceMonitoring() { return enableResourceMonitoring; }
    public void setEnableResourceMonitoring(boolean enableResourceMonitoring) { this.enableResourceMonitoring = enableResourceMonitoring; }
    
    public double getCpuThreshold() { return cpuThreshold; }
    public void setCpuThreshold(double cpuThreshold) { this.cpuThreshold = cpuThreshold; }
    
    public double getMemoryThreshold() { return memoryThreshold; }
    public void setMemoryThreshold(double memoryThreshold) { this.memoryThreshold = memoryThreshold; }
    
    public double getDiskThreshold() { return diskThreshold; }
    public void setDiskThreshold(double diskThreshold) { this.diskThreshold = diskThreshold; }
    
    public boolean isAutoRecovery() { return autoRecovery; }
    public void setAutoRecovery(boolean autoRecovery) { this.autoRecovery = autoRecovery; }
    
    public int getMaxFailuresBeforeDisable() { return maxFailuresBeforeDisable; }
    public void setMaxFailuresBeforeDisable(int maxFailuresBeforeDisable) { this.maxFailuresBeforeDisable = maxFailuresBeforeDisable; }
    
    public int getFailureCooldown() { return failureCooldown; }
    public void setFailureCooldown(int failureCooldown) { this.failureCooldown = failureCooldown; }
    
    public boolean isPersistActionHistory() { return persistActionHistory; }
    public void setPersistActionHistory(boolean persistActionHistory) { this.persistActionHistory = persistActionHistory; }
    
    public int getHistoryRetentionDays() { return historyRetentionDays; }
    public void setHistoryRetentionDays(int historyRetentionDays) { this.historyRetentionDays = historyRetentionDays; }
    
    public int getBackupInterval() { return backupInterval; }
    public void setBackupInterval(int backupInterval) { this.backupInterval = backupInterval; }
    
    public List<ActionConfig> getActions() { return actions; }
    public void setActions(List<ActionConfig> actions) { this.actions = actions; }
    
    /**
     * Adiciona uma ação à configuração
     */
    public void addAction(ActionConfig action) {
        this.actions.add(action);
    }
    
    /**
     * Remove uma ação pelo nome
     */
    public boolean removeAction(String actionName) {
        return actions.removeIf(a -> a.getName().equals(actionName));
    }
    
    /**
     * Busca uma ação pelo nome
     */
    public Optional<ActionConfig> getAction(String actionName) {
        return actions.stream()
            .filter(a -> a.getName().equals(actionName))
            .findFirst();
    }
    
    /**
     * Converte para mapa
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        
        map.put("module_name", moduleName);
        map.put("version", version);
        map.put("environment", environment);
        map.put("max_concurrent_actions", maxConcurrentActions);
        map.put("health_check_interval", healthCheckInterval);
        
        List<Map<String, Object>> actionMaps = actions.stream()
            .map(action -> {
                Map<String, Object> actionMap = new LinkedHashMap<>();
                actionMap.put("name", action.getName());
                actionMap.put("action_type", action.getActionType());
                actionMap.put("frequency", action.getFrequency());
                actionMap.put("enabled", action.isEnabled());
                return actionMap;
            })
            .collect(Collectors.toList());
        
        map.put("actions", actionMaps);
        
        return map;
    }
    
    /**
     * Cria configuração a partir de mapa
     */
    @SuppressWarnings("unchecked")
    public static ModuleConfig fromMap(Map<String, Object> map) {
        ModuleConfig config = new ModuleConfig();
        
        if (map.containsKey("module_name")) config.moduleName = (String) map.get("module_name");
        if (map.containsKey("version")) config.version = (String) map.get("version");
        if (map.containsKey("environment")) config.environment = (String) map.get("environment");
        if (map.containsKey("max_concurrent_actions")) 
            config.maxConcurrentActions = ((Number) map.get("max_concurrent_actions")).intValue();
        if (map.containsKey("health_check_interval")) 
            config.healthCheckInterval = ((Number) map.get("health_check_interval")).intValue();
        
        if (map.containsKey("actions")) {
            List<Map<String, Object>> actionMaps = (List<Map<String, Object>>) map.get("actions");
            config.actions = actionMaps.stream()
                .map(ActionConfig::fromMap)
                .collect(Collectors.toList());
        }
        
        return config;
    }
    
    /**
     * Retorna configuração padrão
     */
    public static ModuleConfig getDefaultConfig() {
        ModuleConfig config = new ModuleConfig();
        
        // Ações padrão
        config.addAction(new ActionConfig(
            "market_analysis",
            "Análise contínua de mercado",
            "MARKET_ANALYSIS",
            "HIGH",
            ExecutionFrequency.EVERY_5_MINUTES.getValue(),
            true,
            180,
            3,
            new ArrayList<>(),
            new HashMap<>()
        ));
        
        config.addAction(new ActionConfig(
            "portfolio_rebalance",
            "Rebalanceamento de portfólio",
            "PORTFOLIO_REBALANCE",
            "MEDIUM",
            ExecutionFrequency.EVERY_DAY.getValue(),
            true,
            600,
            3,
            new ArrayList<>(),
            new HashMap<>()
        ));
        
        config.addAction(new ActionConfig(
            "risk_assessment",
            "Avaliação de risco",
            "RISK_ASSESSMENT",
            "CRITICAL",
            ExecutionFrequency.EVERY_MINUTE.getValue(),
            true,
            120,
            5,
            new ArrayList<>(),
            new HashMap<>()
        ));
        
        config.addAction(new ActionConfig(
            "strategy_optimization",
            "Otimização de estratégias",
            "STRATEGY_OPTIMIZATION",
            "LOW",
            ExecutionFrequency.EVERY_WEEK.getValue(),
            true,
            1800,
            2,
            new ArrayList<>(),
            new HashMap<>()
        ));
        
        config.addAction(new ActionConfig(
            "data_collection",
            "Coleta de dados",
            "DATA_COLLECTION",
            "MEDIUM",
            ExecutionFrequency.EVERY_HOUR.getValue(),
            true,
            900,
            4,
            new ArrayList<>(),
            new HashMap<>()
        ));
        
        config.addAction(new ActionConfig(
            "model_training",
            "Treinamento de modelos",
            "MODEL_TRAINING",
            "BACKGROUND",
            ExecutionFrequency.EVERY_MONTH.getValue(),
            true,
            7200,
            3,
            new ArrayList<>(),
            new HashMap<>()
        ));
        
        return config;
    }
}

// ============================================================================
// GERENCIADOR DE CONFIGURAÇÃO
// ============================================================================

/**
 * Gerenciador de configuração do módulo autônomo
 */
class ConfigManager {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    private final String configFile;
    private ModuleConfig config;
    private final Map<String, Object> metadata;
    private final List<ConfigChangeListener> listeners;
    
    /**
     * Interface para listeners de mudança de configuração
     */
    public interface ConfigChangeListener {
        void onConfigChanged(String key, Object oldValue, Object newValue);
        void onActionAdded(ActionConfig action);
        void onActionRemoved(String actionName);
        void onActionUpdated(String actionName, ActionConfig oldAction, ActionConfig newAction);
    }
    
    /**
     * Construtor
     */
    public ConfigManager() {
        this("autonomous_config.json");
    }
    
    public ConfigManager(String configFile) {
        this.configFile = configFile;
        this.metadata = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.config = null;
        
        // Metadados iniciais
        metadata.put("created_at", LocalDateTime.now().toString());
        metadata.put("last_modified", LocalDateTime.now().toString());
        metadata.put("load_count", 0);
    }
    
    /**
     * Carrega configuração do arquivo
     */
    public ModuleConfig loadConfig() {
        Path path = Paths.get(configFile);
        
        if (Files.exists(path)) {
            try {
                String content = new String(Files.readAllBytes(path));
                Map<String, Object> data = parseJson(content);
                
                config = ModuleConfig.fromMap(data);
                
                // Atualizar metadados
                metadata.put("last_loaded", LocalDateTime.now().toString());
                metadata.merge("load_count", 1, (old, val) -> ((Integer) old) + 1);
                
                logInfo("Configuração carregada de " + configFile);
                
            } catch (IOException e) {
                logError("Erro ao carregar configuração", e);
                config = ModuleConfig.getDefaultConfig();
            }
        } else {
            logInfo("Arquivo de configuração não encontrado. Criando configuração padrão.");
            config = ModuleConfig.getDefaultConfig();
            saveConfig();
        }
        
        return config;
    }
    
    /**
     * Salva configuração no arquivo
     */
    public void saveConfig() {
        if (config == null) {
            logWarning("Nenhuma configuração para salvar");
            return;
        }
        
        try {
            Map<String, Object> data = config.toMap();
            
            // Adicionar metadados
            Map<String, Object> fullData = new LinkedHashMap<>(data);
            fullData.put("_metadata", metadata);
            fullData.put("_timestamp", LocalDateTime.now().toString());
            
            String json = toJson(fullData, true);
            Files.write(Paths.get(configFile), json.getBytes());
            
            // Atualizar metadados
            metadata.put("last_saved", LocalDateTime.now().toString());
            
            logInfo("Configuração salva em " + configFile);
            
        } catch (IOException e) {
            logError("Erro ao salvar configuração", e);
        }
    }
    
    /**
     * Obtém a configuração atual
     */
    public ModuleConfig getConfig() {
        if (config == null) {
            config = loadConfig();
        }
        return config;
    }
    
    /**
     * Atualiza configuração
     */
    public void updateConfig(Map<String, Object> updates) {
        if (config == null) {
            config = getConfig();
        }
        
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object newValue = entry.getValue();
            Object oldValue = getValueByPath(key);
            
            if (setValueByPath(key, newValue)) {
                notifyConfigChanged(key, oldValue, newValue);
            }
        }
        
        saveConfig();
    }
    
    /**
     * Obtém valor por caminho (ex: "actions[0].name")
     */
    private Object getValueByPath(String path) {
        // Implementação simplificada
        return null;
    }
    
    /**
     * Define valor por caminho
     */
    private boolean setValueByPath(String path, Object value) {
        // Implementação simplificada
        return true;
    }
    
    /**
     * Adiciona uma ação
     */
    public void addAction(ActionConfig action) {
        if (config == null) {
            config = getConfig();
        }
        
        config.addAction(action);
        notifyActionAdded(action);
        saveConfig();
    }
    
    /**
     * Remove uma ação pelo nome
     */
    public boolean removeAction(String actionName) {
        if (config == null) {
            config = getConfig();
        }
        
        boolean removed = config.removeAction(actionName);
        if (removed) {
            notifyActionRemoved(actionName);
            saveConfig();
        }
        return removed;
    }
    
    /**
     * Atualiza uma ação
     */
    public boolean updateAction(String actionName, ActionConfig updatedAction) {
        if (config == null) {
            config = getConfig();
        }
        
        Optional<ActionConfig> existing = config.getAction(actionName);
        if (existing.isPresent()) {
            ActionConfig oldAction = existing.get();
            config.removeAction(actionName);
            config.addAction(updatedAction);
            notifyActionUpdated(actionName, oldAction, updatedAction);
            saveConfig();
            return true;
        }
        
        return false;
    }
    
    /**
     * Exporta configuração como JSON
     */
    public String exportConfig() {
        if (config == null) {
            config = getConfig();
        }
        
        Map<String, Object> data = config.toMap();
        data.put("_metadata", metadata);
        data.put("_exported_at", LocalDateTime.now().toString());
        
        return toJson(data, true);
    }
    
    /**
     * Importa configuração de JSON
     */
    @SuppressWarnings("unchecked")
    public boolean importConfig(String jsonConfig) {
        try {
            Map<String, Object> data = parseJson(jsonConfig);
            ModuleConfig newConfig = ModuleConfig.fromMap(data);
            
            if (data.containsKey("_metadata")) {
                metadata.putAll((Map<String, Object>) data.get("_metadata"));
            }
            
            this.config = newConfig;
            saveConfig();
            
            logInfo("Configuração importada com sucesso");
            return true;
            
        } catch (Exception e) {
            logError("Erro ao importar configuração", e);
            return false;
        }
    }
    
    /**
     * Reseta para configuração padrão
     */
    public void resetToDefault() {
        this.config = ModuleConfig.getDefaultConfig();
        saveConfig();
        logInfo("Configuração resetada para o padrão");
    }
    
    /**
     * Adiciona listener
     */
    public void addListener(ConfigChangeListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove listener
     */
    public void removeListener(ConfigChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyConfigChanged(String key, Object oldValue, Object newValue) {
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onConfigChanged(key, oldValue, newValue);
            } catch (Exception e) {
                logError("Erro ao notificar listener", e);
            }
        }
    }
    
    private void notifyActionAdded(ActionConfig action) {
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onActionAdded(action);
            } catch (Exception e) {
                logError("Erro ao notificar listener", e);
            }
        }
    }
    
    private void notifyActionRemoved(String actionName) {
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onActionRemoved(actionName);
            } catch (Exception e) {
                logError("Erro ao notificar listener", e);
            }
        }
    }
    
    private void notifyActionUpdated(String actionName, ActionConfig oldAction, ActionConfig newAction) {
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onActionUpdated(actionName, oldAction, newAction);
            } catch (Exception e) {
                logError("Erro ao notificar listener", e);
            }
        }
    }
    
    // ============================================================================
    // UTILITÁRIOS JSON
    // ============================================================================
    
    private String toJson(Object obj, boolean pretty) {
        // Implementação simplificada - em produção usar biblioteca como Jackson
        if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj, pretty ? 0 : -1);
        }
        return obj != null ? obj.toString() : "null";
    }
    
    @SuppressWarnings("unchecked")
    private String mapToJson(Map<?, ?> map, int indent) {
        StringBuilder sb = new StringBuilder();
        String indentStr = indent >= 0 ? "  ".repeat(indent) : "";
        String newLine = indent >= 0 ? "\n" : "";
        
        sb.append("{").append(newLine);
        
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                sb.append(",").append(newLine);
            }
            first = false;
            
            if (indent >= 0) {
                sb.append("  ".repeat(indent + 1));
            }
            
            sb.append("\"").append(entry.getKey()).append("\": ");
            
            Object value = entry.getValue();
            if (value instanceof Map) {
                sb.append(mapToJson((Map<?, ?>) value, indent >= 0 ? indent + 1 : -1));
            } else if (value instanceof List) {
                sb.append(listToJson((List<?>) value, indent >= 0 ? indent + 1 : -1));
            } else if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else {
                sb.append("\"").append(value).append("\"");
            }
        }
        
        if (indent >= 0) {
            sb.append(newLine).append("  ".repeat(indent));
        }
        sb.append("}");
        
        return sb.toString();
    }
    
    private String listToJson(List<?> list, int indent) {
        StringBuilder sb = new StringBuilder();
        String indentStr = indent >= 0 ? "  ".repeat(indent) : "";
        String newLine = indent >= 0 ? "\n" : "";
        
        sb.append("[").append(newLine);
        
        boolean first = true;
        for (Object item : list) {
            if (!first) {
                sb.append(",").append(newLine);
            }
            first = false;
            
            if (indent >= 0) {
                sb.append("  ".repeat(indent));
            }
            
            if (item instanceof Map) {
                sb.append(mapToJson((Map<?, ?>) item, indent >= 0 ? indent + 1 : -1));
            } else if (item instanceof List) {
                sb.append(listToJson((List<?>) item, indent >= 0 ? indent + 1 : -1));
            } else if (item instanceof String) {
                sb.append("\"").append(item).append("\"");
            } else {
                sb.append(item);
            }
        }
        
        if (indent >= 0) {
            sb.append(newLine);
            if (indent > 0) {
                sb.append("  ".repeat(indent - 1));
            }
        }
        sb.append("]");
        
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        // Implementação simplificada - em produção usar biblioteca como Jackson
        Map<String, Object> result = new LinkedHashMap<>();
        
        // Remover espaços e quebras de linha básicos
        json = json.replaceAll("\\s", "");
        
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].replaceAll("^\"|\"$", "");
                    String value = keyValue[1];
                    
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        result.put(key, value.substring(1, value.length() - 1));
                    } else if (value.equals("true") || value.equals("false")) {
                        result.put(key, Boolean.parseBoolean(value));
                    } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                        if (value.contains(".")) {
                            result.put(key, Double.parseDouble(value));
                        } else {
                            result.put(key, Long.parseLong(value));
                        }
                    } else if (value.startsWith("{")) {
                        result.put(key, parseJson(value));
                    }
                }
            }
        }
        
        return result;
    }
    
    // ============================================================================
    // LOGGING
    // ============================================================================
    
    private void logInfo(String message) {
        System.out.println("[INFO] [ConfigManager] " + message);
    }
    
    private void logWarning(String message) {
        System.out.println("[WARNING] [ConfigManager] " + message);
    }
    
    private void logError(String message, Exception e) {
        System.err.println("[ERROR] [ConfigManager] " + message + ": " + e.getMessage());
    }
}

// ============================================================================
// EXEMPLO DE USO
// ============================================================================

class AutonomousConfigDemo {
    
    public static void main(String[] args) {
        System.out.println("⚙️ LEXTRADER-IAG 4.0 - Módulo de Configuração Autônoma");
        System.out.println("=".repeat(60));
        
        // Criar gerenciador de configuração
        ConfigManager configManager = new ConfigManager("autonomous_config_test.json");
        
        // Adicionar listener para monitorar mudanças
        configManager.addListener(new ConfigManager.ConfigChangeListener() {
            @Override
            public void onConfigChanged(String key, Object oldValue, Object newValue) {
                System.out.println("📝 Configuração alterada: " + key + 
                    " = " + newValue + " (anterior: " + oldValue + ")");
            }
            
            @Override
            public void onActionAdded(ActionConfig action) {
                System.out.println("➕ Ação adicionada: " + action.getName());
            }
            
            @Override
            public void onActionRemoved(String actionName) {
                System.out.println("➖ Ação removida: " + actionName);
            }
            
            @Override
            public void onActionUpdated(String actionName, ActionConfig oldAction, ActionConfig newAction) {
                System.out.println("✏️ Ação atualizada: " + actionName);
            }
        });
        
        // Carregar configuração
        System.out.println("\n📂 Carregando configuração...");
        ModuleConfig config = configManager.getConfig();
        
        // Exibir configuração atual
        System.out.println("\n📋 Configuração atual:");
        System.out.println("  Módulo: " + config.getModuleName());
        System.out.println("  Versão: " + config.getVersion());
        System.out.println("  Ambiente: " + config.getEnvironment());
        System.out.println("  Ações configuradas: " + config.getActions().size());
        
        // Listar ações
        System.out.println("\n🎯 Ações disponíveis:");
        for (ActionConfig action : config.getActions()) {
            System.out.println("  - " + action.getName() + 
                " [" + action.getActionType() + "] " +
                "(" + action.getFrequency() + "s) " +
                (action.isEnabled() ? "✅" : "❌"));
        }
        
        // Testar atualização
        System.out.println("\n🔄 Testando atualização de configuração...");
        Map<String, Object> updates = new HashMap<>();
        updates.put("environment", Environment.STAGING.getValue());
        updates.put("max_concurrent_actions", 15);
        configManager.updateConfig(updates);
        
        // Adicionar nova ação
        System.out.println("\n➕ Adicionando nova ação...");
        ActionConfig newAction = new ActionConfig(
            "backup_database",
            "Backup automático do banco de dados",
            "MAINTENANCE",
            "LOW",
            ExecutionFrequency.EVERY_DAY.getValue(),
            true,
            3600,
            3,
            new ArrayList<>(),
            new HashMap<>()
        );
        configManager.addAction(newAction);
        
        // Exportar configuração
        System.out.println("\n📤 Exportando configuração...");
        String exported = configManager.exportConfig();
        System.out.println(exported.substring(0, Math.min(300, exported.length())) + "...");
        
        // Estatísticas
        System.out.println("\n📊 Estatísticas:");
        ModuleConfig finalConfig = configManager.getConfig();
        long enabledCount = finalConfig.getActions().stream()
            .filter(ActionConfig::isEnabled)
            .count();
        long disabledCount = finalConfig.getActions().size() - enabledCount;
        
        System.out.println("  Ações totais: " + finalConfig.getActions().size());
        System.out.println("  Ações ativas: " + enabledCount);
        System.out.println("  Ações inativas: " + disabledCount);
        
        // Reset para padrão
        System.out.println("\n🔄 Resetando para configuração padrão...");
        configManager.resetToDefault();
        
        System.out.println("\n✅ Demonstração concluída!");
    }
}