// AutonomousModuleMain.java
package lextrader.autonomous;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.*;

/**
 * Classe principal de execução do Módulo Autônomo LEXTRADER-IAG 4.0
 * 
 * Melhorias implementadas:
 * ✅ Sistema de logging avançado com múltiplos handlers
 * ✅ Tratamento de sinais para shutdown gracioso
 * ✅ Monitoramento de saúde em tempo real
 * ✅ Relatórios periódicos de performance
 * ✅ Gerenciamento de configuração integrado
 * ✅ Sistema de recuperação de erros
 * ✅ Métricas de execução detalhadas
 * ✅ Suporte a múltiplos formatos de configuração (JSON/YAML)
 * ✅ Backup automático de configuração
 * ✅ Validação de configuração
 */
public class AutonomousModuleMain {
    
    private static final String VERSION = "4.0.0";
    private static final String MODULE_NAME = "LEXTRADER-IAG Autonomous Module";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter DATE_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    private static Logger logger;
    private static AutonomousModule module;
    private static ConfigManager configManager;
    private static AtomicBoolean running;
    private static ScheduledExecutorService healthScheduler;
    private static ExecutorService workerPool;
    
    /**
     * Configura o sistema de logging avançado
     */
    private static Logger setupLogging() {
        Logger logger = Logger.getLogger("AutonomousModule");
        logger.setUseParentHandlers(false);
        
        // Remover handlers existentes
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        
        try {
            // Formato de log personalizado
            Formatter formatter = new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("%s - %s - %s - %s%n",
                        LocalDateTime.now().format(TIMESTAMP_FORMAT),
                        record.getLevel(),
                        record.getLoggerName(),
                        record.getMessage()
                    );
                }
            };
            
            // Handler para console
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);
            
            // Handler para arquivo com rotação
            String logFile = "autonomous_module.log";
            FileHandler fileHandler = new FileHandler(logFile, 10 * 1024 * 1024, 5, true);
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
            
            // Handler para arquivo de erros
            String errorLogFile = "autonomous_module_error.log";
            FileHandler errorHandler = new FileHandler(errorLogFile, 10 * 1024 * 1024, 3, true);
            errorHandler.setFormatter(formatter);
            errorHandler.setLevel(Level.WARNING);
            logger.addHandler(errorHandler);
            
            logger.setLevel(Level.INFO);
            
        } catch (IOException e) {
            System.err.println("Erro ao configurar logging: " + e.getMessage());
            // Fallback para console simples
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);
        }
        
        return logger;
    }
    
    /**
     * Handler para sinais de shutdown
     */
    private static class ShutdownHandler {
        private final Logger logger;
        private final AtomicBoolean shuttingDown;
        
        public ShutdownHandler(Logger logger) {
            this.logger = logger;
            this.shuttingDown = new AtomicBoolean(false);
        }
        
        public void handle(String signal) {
            if (shuttingDown.compareAndSet(false, true)) {
                logger.info("Sinal " + signal + " recebido, iniciando shutdown gracioso...");
                
                // Executar shutdown em thread separada para não bloquear
                CompletableFuture.runAsync(() -> {
                    try {
                        shutdown();
                    } catch (Exception e) {
                        logger.severe("Erro durante shutdown: " + e.getMessage());
                        System.exit(1);
                    }
                });
            }
        }
    }
    
    /**
     * Executa shutdown gracioso do sistema
     */
    private static void shutdown() {
        logger.info("🛑 Iniciando procedimento de shutdown...");
        
        try {
            // Parar monitoramento de saúde
            if (healthScheduler != null) {
                healthScheduler.shutdown();
                if (!healthScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    healthScheduler.shutdownNow();
                }
            }
            
            // Desligar módulo autônomo
            if (module != null) {
                module.shutdown().get(30, TimeUnit.SECONDS);
            }
            
            // Salvar configuração atual
            if (configManager != null) {
                configManager.saveConfig();
                logger.info("Configuração salva com sucesso");
            }
            
            // Parar worker pool
            if (workerPool != null) {
                workerPool.shutdown();
                if (!workerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    workerPool.shutdownNow();
                }
            }
            
            logger.info("✅ Shutdown concluído com sucesso");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Shutdown interrompido: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Erro durante shutdown: " + e.getMessage());
        } finally {
            // Forçar saída após timeout
            System.exit(0);
        }
    }
    
    /**
     * Monitor de saúde do sistema
     */
    private static class HealthMonitor implements Runnable {
        private final Logger logger;
        private final AutonomousModule module;
        private final AtomicBoolean running;
        private int reportCounter;
        
        public HealthMonitor(Logger logger, AutonomousModule module, AtomicBoolean running) {
            this.logger = logger;
            this.module = module;
            this.running = running;
            this.reportCounter = 0;
        }
        
        @Override
        public void run() {
            if (!running.get()) return;
            
            try {
                ModuleHealth health = module.getHealth();
                reportCounter++;
                
                // Log periódico de saúde
                if (reportCounter % 5 == 0) { // A cada 5 execuções (~30 segundos)
                    logHealthStatus(health);
                }
                
                // Verificar alertas
                if (!health.getAlerts().isEmpty()) {
                    logAlerts(health.getAlerts());
                }
                
                // Gerar relatório completo periodicamente
                if (reportCounter % 60 == 0) { // A cada 60 execuções (~5 minutos)
                    generateHealthReport(health);
                }
                
            } catch (Exception e) {
                logger.warning("Erro no monitor de saúde: " + e.getMessage());
            }
        }
        
        private void logHealthStatus(ModuleHealth health) {
            logger.info(String.format(
                "📊 Status: Total=%d, Completadas=%d, Falhas=%d, Em execução=%d, Taxa sucesso=%.1f%%",
                health.getTotalActions(),
                health.getCompletedActions(),
                health.getFailedActions(),
                health.getRunningActions(),
                health.getSuccessRate() * 100
            ));
        }
        
        private void logAlerts(List<String> alerts) {
            for (String alert : alerts) {
                logger.warning("🚨 " + alert);
            }
        }
        
        private void generateHealthReport(ModuleHealth health) {
            try {
                Map<String, Object> report = new LinkedHashMap<>();
                report.put("timestamp", LocalDateTime.now().toString());
                report.put("health", health);
                
                Map<String, Object> performance = module.getPerformanceReport();
                report.put("performance", performance);
                
                String filename = String.format("health_report_%s.json", 
                    LocalDateTime.now().format(DATE_FORMAT));
                
                Files.write(Paths.get(filename), 
                    JsonUtils.toPrettyJson(report).getBytes());
                
                logger.info("📈 Relatório de saúde salvo: " + filename);
                
            } catch (Exception e) {
                logger.warning("Erro ao gerar relatório de saúde: " + e.getMessage());
            }
        }
    }
    
    /**
     * Utilitário JSON simplificado
     */
    private static class JsonUtils {
        
        public static String toPrettyJson(Map<String, Object> map) {
            return toJson(map, true);
        }
        
        @SuppressWarnings("unchecked")
        private static String toJson(Object obj, boolean pretty) {
            StringBuilder sb = new StringBuilder();
            String indent = pretty ? "  " : "";
            String newline = pretty ? "\n" : "";
            
            if (obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                sb.append("{").append(newline);
                
                boolean first = true;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (!first) {
                        sb.append(",").append(newline);
                    }
                    first = false;
                    
                    if (pretty) sb.append(indent);
                    sb.append("\"").append(entry.getKey()).append("\": ");
                    
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        sb.append("\"").append(value).append("\"");
                    } else if (value instanceof Number || value instanceof Boolean) {
                        sb.append(value);
                    } else if (value instanceof Map || value instanceof List) {
                        sb.append(toJson(value, pretty));
                    } else {
                        sb.append("\"").append(value).append("\"");
                    }
                }
                
                sb.append(newline).append("}");
                
            } else if (obj instanceof List) {
                List<?> list = (List<?>) obj;
                sb.append("[").append(newline);
                
                boolean first = true;
                for (Object item : list) {
                    if (!first) {
                        sb.append(",").append(newline);
                    }
                    first = false;
                    
                    if (pretty) sb.append(indent);
                    sb.append(toJson(item, pretty));
                }
                
                sb.append(newline).append("]");
                
            } else {
                sb.append(obj);
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Exibe banner de inicialização
     */
    private static void printBanner() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║     LEXTRADER-IAG 4.0 - Autonomous Module               ║");
        System.out.println("║                                                          ║");
        System.out.println("║     Sistema Autônomo de Inteligência Artificial         ║");
        System.out.println("║     para Trading e Análise de Mercado                    ║");
        System.out.println("║                                                          ║");
        System.out.println("║     Versão: " + VERSION + "                                      ║");
        System.out.println("║     Iniciado em: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "      ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Exibe ajuda
     */
    private static void printHelp() {
        System.out.println();
        System.out.println("Uso: java lextrader.autonomous.AutonomousModuleMain [opções]");
        System.out.println();
        System.out.println("Opções:");
        System.out.println("  -h, --help                 Exibe esta ajuda");
        System.out.println("  -c, --config <arquivo>     Arquivo de configuração (padrão: autonomous_config.json)");
        System.out.println("  -l, --log-level <level>    Nível de log (DEBUG, INFO, WARNING, ERROR)");
        System.out.println("  -d, --daemon               Executar como daemon");
        System.out.println("  -r, --reset                Reset para configuração padrão");
        System.out.println("  -e, --export <arquivo>     Exportar configuração atual");
        System.out.println();
    }
    
    /**
     * Processa argumentos da linha de comando
     */
    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> options = new HashMap<>();
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                case "--help":
                    options.put("help", "true");
                    break;
                case "-c":
                case "--config":
                    if (i + 1 < args.length) {
                        options.put("config", args[++i]);
                    }
                    break;
                case "-l":
                case "--log-level":
                    if (i + 1 < args.length) {
                        options.put("loglevel", args[++i]);
                    }
                    break;
                case "-d":
                case "--daemon":
                    options.put("daemon", "true");
                    break;
                case "-r":
                case "--reset":
                    options.put("reset", "true");
                    break;
                case "-e":
                case "--export":
                    if (i + 1 < args.length) {
                        options.put("export", args[++i]);
                    }
                    break;
            }
        }
        
        return options;
    }
    
    /**
     * Função principal
     */
    public static void main(String[] args) {
        // Processar argumentos
        Map<String, String> options = parseArgs(args);
        
        // Exibir ajuda
        if (options.containsKey("help")) {
            printHelp();
            System.exit(0);
        }
        
        // Configurar logging
        logger = setupLogging();
        
        // Exibir banner
        printBanner();
        
        // Inicializar componentes
        running = new AtomicBoolean(true);
        healthScheduler = Executors.newScheduledThreadPool(1);
        workerPool = Executors.newCachedThreadPool();
        
        // Configurar handlers de shutdown
        ShutdownHandler shutdownHandler = new ShutdownHandler(logger);
        
        // Registrar handlers para sinais (simulado - Java não tem sinais nativos)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownHandler.handle("SHUTDOWN_HOOK");
        }));
        
        // Executar em thread separada
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                return run(options);
            } catch (Exception e) {
                logger.severe("Erro fatal: " + e.getMessage());
                e.printStackTrace();
                return 1;
            }
        }, workerPool);
        
        try {
            int exitCode = future.get();
            System.exit(exitCode);
        } catch (Exception e) {
            logger.severe("Erro na execução principal: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Execução principal do módulo
     */
    private static int run(Map<String, String> options) {
        try {
            logger.info("=".repeat(60));
            logger.info("Iniciando Módulo Autônomo LEXTRADER-IAG " + VERSION);
            logger.info("=".repeat(60));
            
            // Definir arquivo de configuração
            String configFile = options.getOrDefault("config", "autonomous_config.json");
            
            // Ajustar nível de log
            if (options.containsKey("loglevel")) {
                String level = options.get("loglevel");
                switch (level.toUpperCase()) {
                    case "DEBUG":
                        logger.setLevel(Level.FINE);
                        break;
                    case "INFO":
                        logger.setLevel(Level.INFO);
                        break;
                    case "WARNING":
                        logger.setLevel(Level.WARNING);
                        break;
                    case "ERROR":
                        logger.setLevel(Level.SEVERE);
                        break;
                }
                logger.info("Nível de log ajustado para: " + level);
            }
            
            // Reset para configuração padrão
            if (options.containsKey("reset")) {
                logger.info("Resetando para configuração padrão...");
                ModuleConfig defaultConfig = ModuleConfig.getDefaultConfig();
                ModuleConfigAdapter adapter = new ModuleConfigAdapter();
                Map<String, Object> configMap = adapter.toMap(defaultConfig);
                Files.write(Paths.get(configFile), 
                    JsonUtils.toPrettyJson(configMap).getBytes());
                logger.info("Configuração padrão salva em: " + configFile);
            }
            
            // Exportar configuração
            if (options.containsKey("export")) {
                String exportFile = options.get("export");
                if (Files.exists(Paths.get(configFile))) {
                    Files.copy(Paths.get(configFile), Paths.get(exportFile), 
                        StandardCopyOption.REPLACE_EXISTING);
                    logger.info("Configuração exportada para: " + exportFile);
                } else {
                    logger.warning("Arquivo de configuração não encontrado: " + configFile);
                }
            }
            
            // Carregar configuração
            configManager = new ConfigManager(configFile);
            ModuleConfig config = configManager.getConfig();
            
            logger.info("📋 Configuração carregada: " + config.getModuleName() + 
                " v" + config.getVersion());
            logger.info("   Ambiente: " + config.getEnvironment());
            logger.info("   Ações configuradas: " + config.getActions().size());
            
            // Converter para mapa de configuração
            Map<String, Object> configMap = new HashMap<>();
            configMap.put("health_check_interval", config.getHealthCheckInterval());
            configMap.put("max_concurrent_actions", config.getMaxConcurrentActions());
            configMap.put("log_level", config.getLogLevel());
            configMap.put("alert_channels", config.getAlertChannels());
            configMap.put("enable_resource_monitoring", config.isEnableResourceMonitoring());
            configMap.put("auto_recovery", config.isAutoRecovery());
            
            // Criar módulo autônomo
            module = AutonomousModuleFactory.createModule(configMap);
            
            // Registrar ações da configuração
            for (ActionConfig actionConfig : config.getActions()) {
                if (actionConfig.isEnabled()) {
                    AutonomousAction action = new AutonomousAction(
                        actionConfig.getName(),
                        ActionType.valueOf(actionConfig.getActionType()),
                        actionConfig.getDescription(),
                        ActionPriority.valueOf(actionConfig.getPriority()),
                        ExecutionMode.SCHEDULED,
                        actionConfig.getFrequency(),
                        actionConfig.getTimeout()
                    );
                    action.setRetryCount(actionConfig.getRetryCount());
                    action.setDependencies(new ArrayList<>(actionConfig.getDependencies()));
                    
                    module.registerAction(action);
                    logger.fine("Ação registrada: " + actionConfig.getName());
                }
            }
            
            // Inicializar módulo
            module.initialize().get(30, TimeUnit.SECONDS);
            
            logger.info("✅ Módulo autônomo inicializado com sucesso");
            logger.info("📊 Total de ações registradas: " + module.getActions().size());
            
            // Iniciar monitor de saúde
            HealthMonitor healthMonitor = new HealthMonitor(logger, module, running);
            healthScheduler.scheduleAtFixedRate(healthMonitor, 5, 6, TimeUnit.SECONDS);
            
            logger.info("📈 Monitor de saúde iniciado");
            
            // Modo daemon
            boolean daemon = options.containsKey("daemon");
            if (daemon) {
                logger.info("🔄 Executando em modo daemon");
                
                // Em modo daemon, manter execução até sinal de shutdown
                while (running.get()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } else {
                // Modo interativo - manter por tempo determinado para demonstração
                logger.info("⌛ Executando por 60 segundos em modo demonstrativo...");
                
                for (int i = 0; i < 60 && running.get(); i++) {
                    try {
                        Thread.sleep(1000);
                        
                        if (i % 10 == 0) {
                            ModuleHealth health = module.getHealth();
                            logger.info(String.format(
                                "[%ds] Saúde: Completadas=%d, Taxa=%.1f%%",
                                60 - i,
                                health.getCompletedActions(),
                                health.getSuccessRate() * 100
                            ));
                        }
                        
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            
            logger.info("Encerrando execução normal...");
            return 0;
            
        } catch (Exception e) {
            logger.severe("Erro fatal: " + e.getMessage());
            logger.severe(stackTraceToString(e));
            return 1;
        }
    }
    
    /**
     * Converte stack trace para string
     */
    private static String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    
    // ============================================================================
    // ADAPTADOR PARA CONVERSÃO ENTRE ModuleConfig E AutonomousModule
    // ============================================================================
    
    /**
     * Adaptador para converter ModuleConfig para o formato esperado pelo AutonomousModule
     */
    static class ModuleConfigAdapter {
        
        public Map<String, Object> toMap(ModuleConfig config) {
            Map<String, Object> map = new HashMap<>();
            map.put("module_name", config.getModuleName());
            map.put("version", config.getVersion());
            map.put("environment", config.getEnvironment());
            map.put("max_concurrent_actions", config.getMaxConcurrentActions());
            map.put("health_check_interval", config.getHealthCheckInterval());
            map.put("performance_log_interval", config.getPerformanceLogInterval());
            map.put("log_level", config.getLogLevel());
            map.put("log_file", config.getLogFile());
            map.put("enable_resource_monitoring", config.isEnableResourceMonitoring());
            map.put("cpu_threshold", config.getCpuThreshold());
            map.put("memory_threshold", config.getMemoryThreshold());
            map.put("disk_threshold", config.getDiskThreshold());
            map.put("auto_recovery", config.isAutoRecovery());
            map.put("max_failures_before_disable", config.getMaxFailuresBeforeDisable());
            map.put("failure_cooldown", config.getFailureCooldown());
            map.put("alert_channels", config.getAlertChannels());
            return map;
        }
        
        public ModuleConfig fromMap(Map<String, Object> map) {
            ModuleConfig config = new ModuleConfig();
            
            if (map.containsKey("module_name"))
                config.setModuleName((String) map.get("module_name"));
            if (map.containsKey("version"))
                config.setVersion((String) map.get("version"));
            if (map.containsKey("environment"))
                config.setEnvironment((String) map.get("environment"));
            if (map.containsKey("max_concurrent_actions"))
                config.setMaxConcurrentActions(((Number) map.get("max_concurrent_actions")).intValue());
            if (map.containsKey("health_check_interval"))
                config.setHealthCheckInterval(((Number) map.get("health_check_interval")).intValue());
            if (map.containsKey("performance_log_interval"))
                config.setPerformanceLogInterval(((Number) map.get("performance_log_interval")).intValue());
            if (map.containsKey("log_level"))
                config.setLogLevel((String) map.get("log_level"));
            if (map.containsKey("log_file"))
                config.setLogFile((String) map.get("log_file"));
            if (map.containsKey("enable_resource_monitoring"))
                config.setEnableResourceMonitoring((Boolean) map.get("enable_resource_monitoring"));
            if (map.containsKey("cpu_threshold"))
                config.setCpuThreshold(((Number) map.get("cpu_threshold")).doubleValue());
            if (map.containsKey("memory_threshold"))
                config.setMemoryThreshold(((Number) map.get("memory_threshold")).doubleValue());
            if (map.containsKey("disk_threshold"))
                config.setDiskThreshold(((Number) map.get("disk_threshold")).doubleValue());
            if (map.containsKey("auto_recovery"))
                config.setAutoRecovery((Boolean) map.get("auto_recovery"));
            if (map.containsKey("max_failures_before_disable"))
                config.setMaxFailuresBeforeDisable(((Number) map.get("max_failures_before_disable")).intValue());
            if (map.containsKey("failure_cooldown"))
                config.setFailureCooldown(((Number) map.get("failure_cooldown")).intValue());
            if (map.containsKey("alert_channels"))
                config.setAlertChannels((List<String>) map.get("alert_channels"));
            
            return config;
        }
    }
}

// ============================================================================
// ARQUIVO DE CONFIGURAÇÃO EXTERNO (se não existir, será criado)
// ============================================================================

/**
 * Configuração padrão em JSON que será criada se não existir
 */
class DefaultConfigTemplate {
    
    public static String getDefaultConfigJson() {
        return """
        {
          "module_name": "LEXTRADER-IAG Autonomous Module",
          "version": "4.0.0",
          "environment": "development",
          "max_concurrent_actions": 10,
          "health_check_interval": 60,
          "performance_log_interval": 300,
          "log_level": "INFO",
          "log_file": "autonomous_module.log",
          "log_max_size": 10485760,
          "log_backup_count": 5,
          "alert_channels": ["console"],
          "email_alerts": false,
          "email_recipients": [],
          "enable_resource_monitoring": true,
          "cpu_threshold": 80.0,
          "memory_threshold": 85.0,
          "disk_threshold": 90.0,
          "auto_recovery": true,
          "max_failures_before_disable": 5,
          "failure_cooldown": 300,
          "persist_action_history": true,
          "history_retention_days": 30,
          "backup_interval": 86400,
          "actions": [
            {
              "name": "market_analysis",
              "description": "Análise contínua de mercado",
              "action_type": "MARKET_ANALYSIS",
              "priority": "HIGH",
              "frequency": "300",
              "enabled": true,
              "timeout": 180,
              "retry_count": 3
            },
            {
              "name": "risk_assessment",
              "description": "Avaliação de risco",
              "action_type": "RISK_ASSESSMENT",
              "priority": "CRITICAL",
              "frequency": "60",
              "enabled": true,
              "timeout": 120,
              "retry_count": 5
            },
            {
              "name": "data_collection",
              "description": "Coleta de dados",
              "action_type": "DATA_COLLECTION",
              "priority": "MEDIUM",
              "frequency": "3600",
              "enabled": true,
              "timeout": 900,
              "retry_count": 4
            }
          ]
        }
        """;
    }
}