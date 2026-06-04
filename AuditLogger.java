package modulo_emocional.Seguranca;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Logger de Auditoria - Sistema de Rastreamento de Ações
 * Versão Java convertida e melhorada do Python original
 */
public class AuditLogger {
    
    // Tipos de eventos de auditoria
    public enum AuditEventType {
        TRADE("TRADE", "Execução de Trade"),
        API_ACCESS("API_ACCESS", "Acesso à API"),
        LOGIN("LOGIN", "Login de Usuário"),
        LOGOUT("LOGOUT", "Logout de Usuário"),
        CONFIG_CHANGE("CONFIG_CHANGE", "Alteração de Configuração"),
        ERROR("ERROR", "Erro do Sistema"),
        SECURITY("SECURITY", "Evento de Segurança"),
        SYSTEM("SYSTEM", "Evento do Sistema"),
        DATA_ACCESS("DATA_ACCESS", "Acesso a Dados"),
        FILE_OPERATION("FILE_OPERATION", "Operação de Arquivo");
        
        private final String code;
        private final String description;
        
        AuditEventType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() { return code; }
        public String getDescription() { return description; }
    }
    
    // Estrutura de dados para evento de auditoria
    public static class AuditEvent {
        private final String id;
        private final LocalDateTime timestamp;
        private final AuditEventType eventType;
        private final String user;
        private final String action;
        private final Map<String, Object> details;
        private final String sessionId;
        private final String ipAddress;
        private final String userAgent;
        private final boolean success;
        
        public AuditEvent(AuditEventType eventType, String user, String action, 
                           Map<String, Object> details, String sessionId, 
                           String ipAddress, String userAgent, boolean success) {
            this.id = generateEventId();
            this.timestamp = LocalDateTime.now();
            this.eventType = eventType;
            this.user = user;
            this.action = action;
            this.details = new HashMap<>(details != null ? details : Collections.emptyMap());
            this.sessionId = sessionId != null ? sessionId : "N/A";
            this.ipAddress = ipAddress != null ? ipAddress : "N/A";
            this.userAgent = userAgent != null ? userAgent : "N/A";
            this.success = success;
        }
        
        private String generateEventId() {
            return "AUDIT_" + System.currentTimeMillis() + "_" + 
                   Thread.currentThread().getId();
        }
        
        // Getters
        public String getId() { return id; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public AuditEventType getEventType() { return eventType; }
        public String getUser() { return user; }
        public String getAction() { return action; }
        public Map<String, Object> getDetails() { return new HashMap<>(details); }
        public String getSessionId() { return sessionId; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public boolean isSuccess() { return success; }
        
        // Formatação
        public String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"id\":\"").append(id).append("\",");
            json.append("\"timestamp\":\"").append(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\",");
            json.append("\"eventType\":\"").append(eventType.getCode()).append("\",");
            json.append("\"eventDescription\":\"").append(eventType.getDescription()).append("\",");
            json.append("\"user\":\"").append(user).append("\",");
            json.append("\"action\":\"").append(action).append("\",");
            json.append("\"sessionId\":\"").append(sessionId).append("\",");
            json.append("\"ipAddress\":\"").append(ipAddress).append("\",");
            json.append("\"userAgent\":\"").append(userAgent).append("\",");
            json.append("\"success\":").append(success);
            
            if (!details.isEmpty()) {
                json.append(",\"details\":{");
                boolean first = true;
                for (Map.Entry<String, Object> entry : details.entrySet()) {
                    if (!first) json.append(",");
                    json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
                    first = false;
                }
                json.append("}");
            }
            
            json.append("}");
            return json.toString();
        }
    }
    
    // Estrutura de dados para trade
    public static class TradeAuditDetails {
        private final String symbol;
        private final String action;
        private final double quantity;
        private final double price;
        private final String orderType;
        private final String exchange;
        private final String strategy;
        
        public TradeAuditDetails(String symbol, String action, double quantity, 
                               double price, String orderType, String exchange, String strategy) {
            this.symbol = symbol;
            this.action = action;
            this.quantity = quantity;
            this.price = price;
            this.orderType = orderType;
            this.exchange = exchange;
            this.strategy = strategy;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("symbol", symbol);
            map.put("action", action);
            map.put("quantity", quantity);
            map.put("price", price);
            map.put("orderType", orderType);
            map.put("exchange", exchange);
            map.put("strategy", strategy);
            return map;
        }
    }
    
    // Estrutura de dados para API access
    public static class ApiAccessDetails {
        private final String endpoint;
        private final String method;
        private final int statusCode;
        private final long responseTime;
        private final Map<String, String> headers;
        private final String requestBody;
        private final String responseBody;
        
        public ApiAccessDetails(String endpoint, String method, int statusCode, 
                                long responseTime, Map<String, String> headers, 
                                String requestBody, String responseBody) {
            this.endpoint = endpoint;
            this.method = method;
            this.statusCode = statusCode;
            this.responseTime = responseTime;
            this.headers = new HashMap<>(headers != null ? headers : Collections.emptyMap());
            this.requestBody = requestBody != null ? requestBody : "";
            this.responseBody = responseBody != null ? responseBody : "";
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("endpoint", endpoint);
            map.put("method", method);
            map.put("statusCode", statusCode);
            map.put("responseTime", responseTime);
            map.put("headers", headers);
            map.put("requestBody", requestBody);
            map.put("responseBody", responseBody.length() > 100 ? responseBody.substring(0, 100) + "..." : responseBody);
            return map;
        }
    }
    
    // Estado do logger
    private final String logFile;
    private final Queue<AuditEvent> events;
    private final AtomicInteger totalEvents;
    private final AtomicLong eventIdCounter;
    private final Map<AuditEventType, AtomicInteger> eventCounts;
    private final Map<String, AtomicInteger> userActionCounts;
    private final boolean enableFileLogging;
    private final int maxMemoryEvents;
    
    public AuditLogger(String logFile) {
        this(logFile, true, 10000);
    }
    
    public AuditLogger(String logFile, boolean enableFileLogging, int maxMemoryEvents) {
        this.logFile = logFile != null ? logFile : "audit.log";
        this.events = new ConcurrentLinkedQueue<>();
        this.totalEvents = new AtomicInteger(0);
        this.eventIdCounter = new AtomicLong(0);
        this.eventCounts = new ConcurrentHashMap<>();
        this.userActionCounts = new ConcurrentHashMap<>();
        this.enableFileLogging = enableFileLogging;
        this.maxMemoryEvents = maxMemoryEvents;
        
        // Inicializa contadores
        for (AuditEventType type : AuditEventType.values()) {
            eventCounts.put(type, new AtomicInteger(0));
        }
        
        System.out.println("🔍 AuditLogger inicializado");
        System.out.println("📁 Arquivo de log: " + this.logFile);
        System.out.println("💾 Memória máxima: " + this.maxMemoryEvents + " eventos");
        System.out.println("📝 Logging em arquivo: " + (enableFileLogging ? "ATIVADO" : "DESATIVADO"));
    }
    
    /**
     * Registra evento genérico de auditoria
     */
    public void logEvent(AuditEventType eventType, String user, String action, 
                        Map<String, Object> details) {
        logEvent(eventType, user, action, details, null, null, null, true);
    }
    
    /**
     * Registra evento completo de auditoria
     */
    public void logEvent(AuditEventType eventType, String user, String action, 
                        Map<String, Object> details, String sessionId, 
                        String ipAddress, String userAgent, boolean success) {
        
        AuditEvent event = new AuditEvent(eventType, user, action, details, 
                                        sessionId, ipAddress, userAgent, success);
        
        // Adiciona à fila
        events.offer(event);
        totalEvents.incrementAndGet();
        eventCounts.get(eventType).incrementAndGet();
        
        // Contador por usuário
        String userKey = user + "_" + action;
        userActionCounts.computeIfAbsent(userKey, k -> new AtomicInteger(0)).incrementAndGet();
        
        // Limita tamanho da memória
        while (events.size() > maxMemoryEvents) {
            events.poll(); // Remove o evento mais antigo
        }
        
        // Escreve no arquivo se habilitado
        if (enableFileLogging) {
            writeToFile(event);
        }
        
        // Log no console
        System.out.printf("[%s] %s - %s: %s%n", 
                         event.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                         eventType.getCode(),
                         user,
                         action);
    }
    
    /**
     * Registra execução de trade
     */
    public void logTrade(String user, String symbol, String action, double quantity, 
                      double price, String orderType, String exchange, String strategy) {
        
        TradeAuditDetails tradeDetails = new TradeAuditDetails(
            symbol, action, quantity, price, orderType, exchange, strategy
        );
        
        logEvent(AuditEventType.TRADE, user, "EXECUTE_TRADE", 
                 tradeDetails.toMap(), null, null, null, true);
    }
    
    /**
     * Registra acesso à API
     */
    public void logApiAccess(String user, String endpoint, String method, int statusCode, 
                          long responseTime, Map<String, String> headers, 
                          String requestBody, String responseBody) {
        
        ApiAccessDetails apiDetails = new ApiAccessDetails(
            endpoint, method, statusCode, responseTime, headers, requestBody, responseBody
        );
        
        boolean success = statusCode >= 200 && statusCode < 300;
        
        logEvent(AuditEventType.API_ACCESS, user, method, 
                 apiDetails.toMap(), null, null, null, success);
    }
    
    /**
     * Registra login de usuário
     */
    public void logLogin(String user, String sessionId, String ipAddress, String userAgent, boolean success) {
        Map<String, Object> details = new HashMap<>();
        details.put("loginTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        details.put("sessionId", sessionId);
        
        logEvent(AuditEventType.LOGIN, user, "USER_LOGIN", 
                 details, sessionId, ipAddress, userAgent, success);
    }
    
    /**
     * Registra logout de usuário
     */
    public void logLogout(String user, String sessionId, String ipAddress) {
        Map<String, Object> details = new HashMap<>();
        details.put("logoutTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        details.put("sessionId", sessionId);
        
        logEvent(AuditEventType.LOGOUT, user, "USER_LOGOUT", 
                 details, sessionId, ipAddress, null, true);
    }
    
    /**
     * Registra alteração de configuração
     */
    public void logConfigChange(String user, String configKey, Object oldValue, Object newValue) {
        Map<String, Object> details = new HashMap<>();
        details.put("configKey", configKey);
        details.put("oldValue", oldValue);
        details.put("newValue", newValue);
        details.put("changeTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        logEvent(AuditEventType.CONFIG_CHANGE, user, "CONFIG_UPDATE", details);
    }
    
    /**
     * Registra erro do sistema
     */
    public void logError(String user, String errorType, String errorMessage, String stackTrace) {
        Map<String, Object> details = new HashMap<>();
        details.put("errorType", errorType);
        details.put("errorMessage", errorMessage);
        details.put("stackTrace", stackTrace);
        details.put("errorTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        logEvent(AuditEventType.ERROR, user, "SYSTEM_ERROR", details);
    }
    
    /**
     * Registra evento de segurança
     */
    public void logSecurityEvent(String user, String securityType, String description, 
                               String severity, String source) {
        Map<String, Object> details = new HashMap<>();
        details.put("securityType", securityType);
        details.put("description", description);
        details.put("severity", severity);
        details.put("source", source);
        details.put("eventTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        logEvent(AuditEventType.SECURITY, user, "SECURITY_EVENT", details);
    }
    
    /**
     * Obtém eventos filtrados por tipo
     */
    public List<AuditEvent> getEvents(AuditEventType eventType) {
        return events.stream()
                .filter(event -> event.getEventType() == eventType)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtém eventos filtrados por usuário
     */
    public List<AuditEvent> getEventsByUser(String user) {
        return events.stream()
                .filter(event -> event.getUser().equals(user))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtém eventos por período
     */
    public List<AuditEvent> getEventsByPeriod(LocalDateTime start, LocalDateTime end) {
        return events.stream()
                .filter(event -> !event.getTimestamp().isBefore(start) && 
                                 !event.getTimestamp().isAfter(end))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtém todos os eventos
     */
    public List<AuditEvent> getAllEvents() {
        return new ArrayList<>(events);
    }
    
    /**
     * Obtém estatísticas de auditoria
     */
    public Map<String, Object> getAuditStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Estatísticas gerais
        stats.put("totalEvents", totalEvents.get());
        stats.put("memoryEvents", events.size());
        stats.put("maxMemoryEvents", maxMemoryEvents);
        stats.put("fileLoggingEnabled", enableFileLogging);
        stats.put("logFile", logFile);
        
        // Contagem por tipo de evento
        Map<String, Integer> eventStats = new HashMap<>();
        for (Map.Entry<AuditEventType, AtomicInteger> entry : eventCounts.entrySet()) {
            eventStats.put(entry.getKey().getCode(), entry.getValue().get());
        }
        stats.put("eventCounts", eventStats);
        
        // Top usuários por ações
        Map<String, Integer> topUsers = userActionCounts.entrySet().stream()
                .sorted(Map.Entry.<String, AtomicInteger>comparingByValue(Map.Entry::getValue))
                .limit(10)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().get()
                ));
        stats.put("topUsers", topUsers);
        
        // Eventos recentes (últimas 24 horas)
        LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
        long recentEvents = events.stream()
                .filter(event -> event.getTimestamp().isAfter(yesterday))
                .count();
        stats.put("recentEvents24h", recentEvents);
        
        // Taxa de sucesso por tipo
        Map<String, Double> successRates = new HashMap<>();
        for (AuditEventType type : AuditEventType.values()) {
            long totalType = events.stream()
                    .filter(event -> event.getEventType() == type)
                    .count();
            long successType = events.stream()
                    .filter(event -> event.getEventType() == type && event.isSuccess())
                    .count();
            
            if (totalType > 0) {
                successRates.put(type.getCode(), (double) successType / totalType * 100);
            }
        }
        stats.put("successRates", successRates);
        
        return stats;
    }
    
    /**
     * Escreve evento no arquivo
     */
    private void writeToFile(AuditEvent event) {
        try {
            // Em implementação real, usaria uma biblioteca JSON
            // Por ora, apenas escrevemos o JSON formatado
            String logEntry = event.toJson() + System.lineSeparator();
            
            // Simular escrita em arquivo
            System.out.println("📝 Escrevendo no arquivo: " + logFile);
            System.out.println("📄 Evento: " + logEntry);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }
    
    /**
     * Limpa eventos antigos
     */
    public void cleanupOldEvents(int maxAgeHours) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(maxAgeHours);
        
        int removedCount = 0;
        Iterator<AuditEvent> iterator = events.iterator();
        while (iterator.hasNext()) {
            AuditEvent event = iterator.next();
            if (event.getTimestamp().isBefore(cutoff)) {
                iterator.remove();
                removedCount++;
            }
        }
        
        System.out.println("🧹 Limpeza concluída: " + removedCount + " eventos removidos");
    }
    
    /**
     * Exporta eventos para formato estruturado
     */
    public void exportEvents(String filename, AuditEventType filterType) {
        List<AuditEvent> eventsToExport = filterType != null ? 
            getEvents(filterType) : getAllEvents();
        
        try {
            // Em implementação real, escreveria em arquivo JSON/CSV
            System.out.println("📤 Exportando " + eventsToExport.size() + 
                             " eventos para: " + filename);
            
            for (AuditEvent event : eventsToExport) {
                System.out.println(event.toJson());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao exportar eventos: " + e.getMessage());
        }
    }
    
    /**
     * Método principal para demonstração
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("🔍 DEMONSTRAÇÃO DO AUDIT LOGGER");
        System.out.println("=".repeat(80));
        
        // Criar logger
        AuditLogger logger = new AuditLogger("audit_demo.log", true, 1000);
        
        // Simular alguns eventos
        logger.logLogin("admin", "sess_123", "192.168.1.100", "Mozilla/5.0", true);
        logger.logTrade("trader1", "BTC/USDT", "BUY", 0.5, 45000.0, "LIMIT", "binance", "RSI_STRATEGY");
        logger.logApiAccess("api_user", "/api/v1/trades", "POST", 200, 150, 
                        Map.of("Authorization", "Bearer token123"), 
                        "{\"symbol\":\"BTCUSDT\",\"side\":\"BUY\"}", 
                        "{\"id\":\"12345\",\"status\":\"filled\"}");
        logger.logConfigChange("admin", "max_trades_per_day", 100, 150);
        logger.logError("system", "DATABASE_ERROR", "Connection timeout", "Stack trace...");
        logger.logSecurityEvent("admin", "BRUTE_FORCE", "Múltiplas tentativas de login", "HIGH", "IP: 192.168.1.50");
        logger.logLogout("admin", "sess_123", "192.168.1.100");
        
        // Exibir estatísticas
        System.out.println("\n📊 Estatísticas de Auditoria:");
        Map<String, Object> stats = logger.getAuditStatistics();
        stats.forEach((key, value) -> System.out.println("  " + key + ": " + value));
        
        // Exibir eventos recentes
        System.out.println("\n📋 Eventos Recentes:");
        List<AuditEvent> recentEvents = logger.getAllEvents();
        recentEvents.stream().limit(5).forEach(event -> {
            System.out.println("  " + event.getTimestamp() + " - " + 
                             event.getUser() + " - " + event.getAction());
        });
        
        System.out.println("\n✅ Demonstração concluída!");
    }
}
