// ActionController.java
package vhalinor.autonomous;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// ============================================================================
// ENUMS E TIPOS
// ============================================================================

enum ControlMode {
    AUTONOMOUS("autonomous"),
    SUPERVISED("supervised"),
    MANUAL("manual"),
    SAFE("safe");
    
    private final String value;
    
    ControlMode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}

enum ActionPriority {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum ActionStatus {
    PENDING, RUNNING, PAUSED, COMPLETED, FAILED, REJECTED
}

enum ActionType {
    ANALYSIS, DECISION, EXECUTION, MONITORING, LEARNING
}

// ============================================================================
// CLASSES DE DADOS
// ============================================================================

/**
 * Controle de execução de ações
 */
class ExecutionControl {
    private String actionId;
    private int maxExecutionTime; // segundos
    private double cpuLimit;      // porcentagem
    private double memoryLimit;   // MB
    private double networkLimit;  // MB/s
    private boolean canBePaused;
    private boolean canBeCancelled;
    private boolean requiresApproval;
    private int approvalTimeout;  // segundos
    
    public ExecutionControl(String actionId, int maxExecutionTime) {
        this.actionId = actionId;
        this.maxExecutionTime = maxExecutionTime;
        this.cpuLimit = 80.0;
        this.memoryLimit = 1024.0;
        this.networkLimit = 100.0;
        this.canBePaused = true;
        this.canBeCancelled = true;
        this.requiresApproval = false;
        this.approvalTimeout = 300;
    }
    
    // Getters and Setters
    public String getActionId() { return actionId; }
    public int getMaxExecutionTime() { return maxExecutionTime; }
    public void setMaxExecutionTime(int maxExecutionTime) { this.maxExecutionTime = maxExecutionTime; }
    public double getCpuLimit() { return cpuLimit; }
    public void setCpuLimit(double cpuLimit) { this.cpuLimit = cpuLimit; }
    public double getMemoryLimit() { return memoryLimit; }
    public void setMemoryLimit(double memoryLimit) { this.memoryLimit = memoryLimit; }
    public double getNetworkLimit() { return networkLimit; }
    public void setNetworkLimit(double networkLimit) { this.networkLimit = networkLimit; }
    public boolean isCanBePaused() { return canBePaused; }
    public void setCanBePaused(boolean canBePaused) { this.canBePaused = canBePaused; }
    public boolean isCanBeCancelled() { return canBeCancelled; }
    public void setCanBeCancelled(boolean canBeCancelled) { this.canBeCancelled = canBeCancelled; }
    public boolean isRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(boolean requiresApproval) { this.requiresApproval = requiresApproval; }
    public int getApprovalTimeout() { return approvalTimeout; }
    public void setApprovalTimeout(int approvalTimeout) { this.approvalTimeout = approvalTimeout; }
}

/**
 * Monitor de recursos
 */
class ResourceMonitor {
    private String actionId;
    private double cpuUsage;
    private double memoryUsage;
    private double networkUsage;
    private double executionTime;
    private Instant timestamp;
    
    public ResourceMonitor(String actionId) {
        this.actionId = actionId;
        this.cpuUsage = 0.0;
        this.memoryUsage = 0.0;
        this.networkUsage = 0.0;
        this.executionTime = 0.0;
        this.timestamp = Instant.now();
    }
    
    /**
     * Verifica se excedeu os limites
     */
    public boolean isOverLimit(ExecutionControl limits) {
        return cpuUsage > limits.getCpuLimit() ||
               memoryUsage > limits.getMemoryLimit() ||
               networkUsage > limits.getNetworkLimit() ||
               executionTime > limits.getMaxExecutionTime();
    }
    
    // Getters and Setters
    public String getActionId() { return actionId; }
    public double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
    public double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(double memoryUsage) { this.memoryUsage = memoryUsage; }
    public double getNetworkUsage() { return networkUsage; }
    public void setNetworkUsage(double networkUsage) { this.networkUsage = networkUsage; }
    public double getExecutionTime() { return executionTime; }
    public void setExecutionTime(double executionTime) { this.executionTime = executionTime; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

/**
 * Representação simplificada de uma ação
 */
class AutonomousAction {
    private String id;
    private String name;
    private ActionType type;
    private ActionPriority priority;
    private String description;
    private ActionStatus status;
    private boolean isRunning;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public AutonomousAction(String id, String name, ActionType type, 
                           ActionPriority priority, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.description = description;
        this.status = ActionStatus.PENDING;
        this.isRunning = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public ActionType getType() { return type; }
    public ActionPriority getPriority() { return priority; }
    public String getDescription() { return description; }
    public ActionStatus getStatus() { return status; }
    public void setStatus(ActionStatus status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    public boolean isRunning() { return isRunning; }
    public void setRunning(boolean running) { isRunning = running; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

/**
 * Representação simplificada do módulo autônomo
 */
class AutonomousModule {
    private Map<String, AutonomousAction> actions;
    private java.util.logging.Logger logger;
    
    public AutonomousModule() {
        this.actions = new ConcurrentHashMap<>();
        this.logger = java.util.logging.Logger.getLogger("AutonomousModule");
    }
    
    public Map<String, AutonomousAction> getActions() { return actions; }
    public java.util.logging.Logger getLogger() { return logger; }
    
    public CompletableFuture<Boolean> pauseAction(String actionId) {
        return CompletableFuture.supplyAsync(() -> {
            AutonomousAction action = actions.get(actionId);
            if (action != null && action.isRunning()) {
                action.setRunning(false);
                action.setStatus(ActionStatus.PAUSED);
                return true;
            }
            return false;
        });
    }
    
    public void addAction(AutonomousAction action) {
        actions.put(action.getId(), action);
    }
}

// ============================================================================
// CONTROLADOR DE AÇÕES PRINCIPAL
// ============================================================================

/**
 * Controlador de Ações Autônomas
 * Gerencia execução, priorização e controle de ações
 */
public class ActionController {
    
    private final AutonomousModule module;
    private ControlMode controlMode;
    
    private final Map<String, ExecutionControl> executionControls;
    private final Map<String, ResourceMonitor> resourceMonitors;
    private final Queue<String> approvalQueue;
    private final Set<String> pausedActions;
    
    private final Map<String, SafetyThreshold> safetyThresholds;
    private final List<ActionListener> listeners;
    
    private final ScheduledExecutorService scheduler;
    private final ExecutorService workerPool;
    private final AtomicBoolean monitoringActive;
    
    // Estatísticas
    private final AtomicLong totalActionsProcessed;
    private final AtomicLong totalApprovalsRequested;
    private final AtomicLong totalResourcesExceeded;
    
    /**
     * Thresholds de segurança
     */
    public static class SafetyThreshold {
        private double maxTotalCpu;
        private double maxTotalMemory;
        private int maxConcurrentCritical;
        private double maxFailureRate;
        
        public SafetyThreshold(double maxTotalCpu, double maxTotalMemory, 
                               int maxConcurrentCritical, double maxFailureRate) {
            this.maxTotalCpu = maxTotalCpu;
            this.maxTotalMemory = maxTotalMemory;
            this.maxConcurrentCritical = maxConcurrentCritical;
            this.maxFailureRate = maxFailureRate;
        }
        
        // Getters and Setters
        public double getMaxTotalCpu() { return maxTotalCpu; }
        public void setMaxTotalCpu(double maxTotalCpu) { this.maxTotalCpu = maxTotalCpu; }
        public double getMaxTotalMemory() { return maxTotalMemory; }
        public void setMaxTotalMemory(double maxTotalMemory) { this.maxTotalMemory = maxTotalMemory; }
        public int getMaxConcurrentCritical() { return maxConcurrentCritical; }
        public void setMaxConcurrentCritical(int maxConcurrentCritical) { this.maxConcurrentCritical = maxConcurrentCritical; }
        public double getMaxFailureRate() { return maxFailureRate; }
        public void setMaxFailureRate(double maxFailureRate) { this.maxFailureRate = maxFailureRate; }
    }
    
    /**
     * Interface para listeners de ações
     */
    public interface ActionListener {
        void onActionPaused(String actionId);
        void onActionResumed(String actionId);
        void onActionCompleted(String actionId, boolean success);
        void onResourceExceeded(String actionId, ResourceMonitor monitor);
        void onApprovalRequested(String actionId);
        void onControlModeChanged(ControlMode oldMode, ControlMode newMode);
    }
    
    /**
     * Construtor
     */
    public ActionController(AutonomousModule module) {
        this.module = module;
        this.controlMode = ControlMode.AUTONOMOUS;
        
        this.executionControls = new ConcurrentHashMap<>();
        this.resourceMonitors = new ConcurrentHashMap<>();
        this.approvalQueue = new ConcurrentLinkedQueue<>();
        this.pausedActions = ConcurrentHashMap.newKeySet();
        
        this.safetyThresholds = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.workerPool = Executors.newFixedThreadPool(4);
        this.monitoringActive = new AtomicBoolean(true);
        
        this.totalActionsProcessed = new AtomicLong(0);
        this.totalApprovalsRequested = new AtomicLong(0);
        this.totalResourcesExceeded = new AtomicLong(0);
        
        initializeDefaultThresholds();
        startMonitoring();
    }
    
    /**
     * Inicializa thresholds padrão
     */
    private void initializeDefaultThresholds() {
        safetyThresholds.put("default", new SafetyThreshold(90.0, 4096.0, 3, 0.3));
        safetyThresholds.put("safe", new SafetyThreshold(50.0, 2048.0, 1, 0.1));
        safetyThresholds.put("performance", new SafetyThreshold(95.0, 8192.0, 5, 0.4));
    }
    
    /**
     * Inicia monitoramento de recursos
     */
    private void startMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            if (monitoringActive.get()) {
                monitorResources().join();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
    
    /**
     * Define modo de controle
     */
    public void setControlMode(ControlMode mode) {
        ControlMode oldMode = this.controlMode;
        this.controlMode = mode;
        
        if (mode == ControlMode.SAFE) {
            enterSafeMode();
        }
        
        // Notificar listeners
        for (ActionListener listener : listeners) {
            try {
                listener.onControlModeChanged(oldMode, mode);
            } catch (Exception e) {
                logError("Erro ao notificar mudança de modo", e);
            }
        }
    }
    
    /**
     * Entra em modo de segurança
     */
    private void enterSafeMode() {
        // Pausar ações críticas
        for (Map.Entry<String, AutonomousAction> entry : module.getActions().entrySet()) {
            AutonomousAction action = entry.getValue();
            if (action.getPriority() == ActionPriority.CRITICAL && action.isRunning()) {
                CompletableFuture.runAsync(() -> {
                    boolean paused = pauseAction(entry.getKey()).join();
                    if (paused) {
                        logInfo("Ação crítica pausada em modo seguro: " + entry.getKey());
                    }
                }, workerPool);
            }
        }
        
        // Ajustar thresholds
        SafetyThreshold safeThreshold = safetyThresholds.get("safe");
        if (safeThreshold != null) {
            safetyThresholds.put("default", safeThreshold);
        }
    }
    
    /**
     * Registra controle para uma ação
     */
    public void registerControl(String actionId, ExecutionControl control) {
        executionControls.put(actionId, control);
        
        if (control.isRequiresApproval()) {
            approvalQueue.offer(actionId);
            totalApprovalsRequested.incrementAndGet();
            
            // Notificar listeners
            for (ActionListener listener : listeners) {
                try {
                    listener.onApprovalRequested(actionId);
                } catch (Exception e) {
                    logError("Erro ao notificar solicitação de aprovação", e);
                }
            }
        }
    }
    
    /**
     * Solicita aprovação para execução
     */
    public CompletableFuture<Boolean> requestApproval(String actionId) {
        return CompletableFuture.supplyAsync(() -> {
            ExecutionControl control = executionControls.get(actionId);
            if (control == null || !control.isRequiresApproval()) {
                return true;
            }
            
            // Adicionar à fila de aprovação se não estiver
            if (!approvalQueue.contains(actionId)) {
                approvalQueue.offer(actionId);
            }
            
            // Aguardar aprovação ou timeout
            try {
                return waitForApproval(actionId, control.getApprovalTimeout());
            } catch (Exception e) {
                logError("Erro ao aguardar aprovação para " + actionId, e);
                return false;
            }
        }, workerPool);
    }
    
    /**
     * Aguarda aprovação com timeout
     */
    private boolean waitForApproval(String actionId, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000L;
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (!approvalQueue.contains(actionId)) {
                return true; // Aprovado (removido da fila)
            }
            
            try {
                Thread.sleep(100); // Verificar a cada 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        return false; // Timeout
    }
    
    /**
     * Aprova uma ação manualmente
     */
    public boolean approveAction(String actionId) {
        if (approvalQueue.remove(actionId)) {
            logInfo("Ação aprovada: " + actionId);
            return true;
        }
        return false;
    }
    
    /**
     * Rejeita uma ação
     */
    public boolean rejectAction(String actionId) {
        if (approvalQueue.remove(actionId)) {
            // Marcar ação como rejeitada
            AutonomousAction action = module.getActions().get(actionId);
            if (action != null) {
                action.setStatus(ActionStatus.REJECTED);
                logInfo("Ação rejeitada: " + actionId);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Monitora uso de recursos das ações
     */
    public CompletableFuture<Void> monitorResources() {
        return CompletableFuture.runAsync(() -> {
            // Coletar métricas simuladas
            for (String actionId : executionControls.keySet()) {
                ResourceMonitor monitor = resourceMonitors.computeIfAbsent(
                    actionId, ResourceMonitor::new);
                
                // Simular métricas (em produção, usar psutil ou similar)
                monitor.setCpuUsage(Math.random() * 30 + 10); // 10-40%
                monitor.setMemoryUsage(Math.random() * 200 + 50); // 50-250 MB
                monitor.setNetworkUsage(Math.random() * 20); // 0-20 MB/s
                monitor.setExecutionTime(monitor.getExecutionTime() + 5); // +5 segundos
                monitor.setTimestamp(Instant.now());
                
                // Verificar limites
                ExecutionControl control = executionControls.get(actionId);
                if (control != null && monitor.isOverLimit(control)) {
                    handleResourceExceeded(actionId, monitor);
                }
            }
        }, workerPool);
    }
    
    /**
     * Lida com excedente de recursos
     */
    private void handleResourceExceeded(String actionId, ResourceMonitor monitor) {
        totalResourcesExceeded.incrementAndGet();
        
        AutonomousAction action = module.getActions().get(actionId);
        if (action != null && action.isRunning()) {
            logWarning(String.format(
                "Ação %s excedeu limites de recursos: CPU=%.1f%%, Mem=%.1fMB, Time=%.1fs",
                actionId, monitor.getCpuUsage(), monitor.getMemoryUsage(), 
                monitor.getExecutionTime()
            ));
            
            // Pausar ação se permitido
            ExecutionControl control = executionControls.get(actionId);
            if (control != null && control.isCanBePaused()) {
                CompletableFuture.runAsync(() -> {
                    boolean paused = pauseAction(actionId).join();
                    if (paused) {
                        logInfo("Ação pausada por excesso de recursos: " + actionId);
                    }
                }, workerPool);
            }
            
            // Notificar listeners
            for (ActionListener listener : listeners) {
                try {
                    listener.onResourceExceeded(actionId, monitor);
                } catch (Exception e) {
                    logError("Erro ao notificar excesso de recursos", e);
                }
            }
        }
    }
    
    /**
     * Pausa uma ação
     */
    public CompletableFuture<Boolean> pauseAction(String actionId) {
        return CompletableFuture.supplyAsync(() -> {
            AutonomousAction action = module.getActions().get(actionId);
            ExecutionControl control = executionControls.get(actionId);
            
            if (action != null && action.isRunning() && 
                control != null && control.isCanBePaused()) {
                
                action.setRunning(false);
                action.setStatus(ActionStatus.PAUSED);
                pausedActions.add(actionId);
                
                // Notificar listeners
                for (ActionListener listener : listeners) {
                    try {
                        listener.onActionPaused(actionId);
                    } catch (Exception e) {
                        logError("Erro ao notificar pausa", e);
                    }
                }
                
                logInfo("Ação pausada: " + actionId);
                return true;
            }
            return false;
        }, workerPool);
    }
    
    /**
     * Retoma uma ação pausada
     */
    public CompletableFuture<Boolean> resumeAction(String actionId) {
        return CompletableFuture.supplyAsync(() -> {
            AutonomousAction action = module.getActions().get(actionId);
            
            if (action != null && pausedActions.contains(actionId)) {
                action.setRunning(true);
                action.setStatus(ActionStatus.RUNNING);
                pausedActions.remove(actionId);
                
                // Notificar listeners
                for (ActionListener listener : listeners) {
                    try {
                        listener.onActionResumed(actionId);
                    } catch (Exception e) {
                        logError("Erro ao notificar retomada", e);
                    }
                }
                
                logInfo("Ação retomada: " + actionId);
                return true;
            }
            return false;
        }, workerPool);
    }
    
    /**
     * Cancela uma ação
     */
    public CompletableFuture<Boolean> cancelAction(String actionId) {
        return CompletableFuture.supplyAsync(() -> {
            AutonomousAction action = module.getActions().get(actionId);
            ExecutionControl control = executionControls.get(actionId);
            
            if (action != null && control != null && control.isCanBeCancelled()) {
                action.setRunning(false);
                action.setStatus(ActionStatus.COMPLETED);
                pausedActions.remove(actionId);
                
                logInfo("Ação cancelada: " + actionId);
                return true;
            }
            return false;
        }, workerPool);
    }
    
    /**
     * Obtém lista de ações pendentes de aprovação
     */
    public List<Map<String, Object>> getPendingApprovals() {
        List<Map<String, Object>> approvals = new ArrayList<>();
        
        for (String actionId : approvalQueue) {
            AutonomousAction action = module.getActions().get(actionId);
            if (action != null) {
                Map<String, Object> approval = new LinkedHashMap<>();
                approval.put("id", actionId);
                approval.put("name", action.getName());
                approval.put("type", action.getType().name());
                approval.put("priority", action.getPriority().name());
                approval.put("description", action.getDescription());
                approvals.add(approval);
            }
        }
        
        return approvals;
    }
    
    /**
     * Obtém saúde do sistema de controle
     */
    public Map<String, Object> getSystemHealth() {
        int totalActions = module.getActions().size();
        int controlledActions = executionControls.size();
        int pendingApprovals = approvalQueue.size();
        int paused = pausedActions.size();
        
        // Calcular uso de recursos
        double totalCpu = resourceMonitors.values().stream()
            .mapToDouble(ResourceMonitor::getCpuUsage)
            .sum();
        double totalMemory = resourceMonitors.values().stream()
            .mapToDouble(ResourceMonitor::getMemoryUsage)
            .sum();
        
        SafetyThreshold thresholds = safetyThresholds.getOrDefault(
            controlMode == ControlMode.SAFE ? "safe" : "default", 
            safetyThresholds.get("default")
        );
        
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("control_mode", controlMode.getValue());
        health.put("total_actions", totalActions);
        health.put("controlled_actions", controlledActions);
        health.put("pending_approvals", pendingApprovals);
        health.put("paused_actions", paused);
        
        Map<String, Object> resourceUsage = new LinkedHashMap<>();
        resourceUsage.put("total_cpu", totalCpu);
        resourceUsage.put("total_memory", totalMemory);
        
        Map<String, Object> thresholdMap = new LinkedHashMap<>();
        thresholdMap.put("max_total_cpu", thresholds.getMaxTotalCpu());
        thresholdMap.put("max_total_memory", thresholds.getMaxTotalMemory());
        thresholdMap.put("max_concurrent_critical", thresholds.getMaxConcurrentCritical());
        thresholdMap.put("max_failure_rate", thresholds.getMaxFailureRate());
        resourceUsage.put("thresholds", thresholdMap);
        
        health.put("resource_usage", resourceUsage);
        
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total_actions_processed", totalActionsProcessed.get());
        stats.put("total_approvals_requested", totalApprovalsRequested.get());
        stats.put("total_resources_exceeded", totalResourcesExceeded.get());
        health.put("statistics", stats);
        
        return health;
    }
    
    /**
     * Adiciona listener para eventos
     */
    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove listener
     */
    public void removeListener(ActionListener listener) {
        listeners.remove(listener);
    }
    
    // ============================================================================
    // UTILITÁRIOS DE LOGGING
    // ============================================================================
    
    private void logInfo(String message) {
        module.getLogger().info("[ActionController] " + message);
    }
    
    private void logWarning(String message) {
        module.getLogger().warning("[ActionController] " + message);
    }
    
    private void logError(String message, Exception e) {
        module.getLogger().severe("[ActionController] " + message + ": " + e.getMessage());
    }
    
    // ============================================================================
    // GETTERS
    // ============================================================================
    
    public ControlMode getControlMode() { return controlMode; }
    public Map<String, ExecutionControl> getExecutionControls() { return executionControls; }
    public Map<String, ResourceMonitor> getResourceMonitors() { return resourceMonitors; }
    public Set<String> getPausedActions() { return pausedActions; }
    public Map<String, SafetyThreshold> getSafetyThresholds() { return safetyThresholds; }
    
    /**
     * Encerra o controlador
     */
    public void shutdown() {
        monitoringActive.set(false);
        
        scheduler.shutdown();
        workerPool.shutdown();
        
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!workerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                workerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            workerPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        logInfo("ActionController encerrado");
    }
}

// ============================================================================
// EXEMPLO DE USO
// ============================================================================

class ActionControllerDemo {
    
    public static void main(String[] args) throws Exception {
        System.out.println("🤖 CONTROLADOR DE AÇÕES AUTÔNOMAS VHALINOR");
        System.out.println("=".repeat(60));
        
        // Criar módulo autônomo
        AutonomousModule module = new AutonomousModule();
        
        // Adicionar algumas ações de exemplo
        module.addAction(new AutonomousAction(
            "act_001", "Análise de Mercado", 
            ActionType.ANALYSIS, ActionPriority.HIGH, 
            "Analisar tendências de mercado em tempo real"
        ));
        module.addAction(new AutonomousAction(
            "act_002", "Execução de Trade", 
            ActionType.EXECUTION, ActionPriority.CRITICAL, 
            "Executar ordens de compra/venda"
        ));
        module.addAction(new AutonomousAction(
            "act_003", "Monitoramento de Risco", 
            ActionType.MONITORING, ActionPriority.MEDIUM, 
            "Calcular métricas de risco do portfólio"
        ));
        
        // Criar controlador
        ActionController controller = new ActionController(module);
        
        // Adicionar listener para eventos
        controller.addListener(new ActionController.ActionListener() {
            @Override
            public void onActionPaused(String actionId) {
                System.out.println("⏸️ Ação pausada: " + actionId);
            }
            
            @Override
            public void onActionResumed(String actionId) {
                System.out.println("▶️ Ação retomada: " + actionId);
            }
            
            @Override
            public void onActionCompleted(String actionId, boolean success) {
                System.out.println("✅ Ação concluída: " + actionId + " (sucesso=" + success + ")");
            }
            
            @Override
            public void onResourceExceeded(String actionId, ResourceMonitor monitor) {
                System.out.printf("⚠️ Recursos excedidos em %s: CPU=%.1f%%, Mem=%.1fMB%n",
                    actionId, monitor.getCpuUsage(), monitor.getMemoryUsage());
            }
            
            @Override
            public void onApprovalRequested(String actionId) {
                System.out.println("📋 Aprovação solicitada: " + actionId);
            }
            
            @Override
            public void onControlModeChanged(ControlMode oldMode, ControlMode newMode) {
                System.out.println("🔄 Modo alterado: " + oldMode + " → " + newMode);
            }
        });
        
        // Registrar controles
        ExecutionControl control1 = new ExecutionControl("act_001", 30);
        control1.setRequiresApproval(true);
        control1.setApprovalTimeout(10);
        controller.registerControl("act_001", control1);
        
        ExecutionControl control2 = new ExecutionControl("act_002", 60);
        control2.setCpuLimit(50.0);
        control2.setCanBePaused(false);
        controller.registerControl("act_002", control2);
        
        // Simular solicitações de aprovação
        System.out.println("\n📋 Solicitando aprovações...");
        controller.requestApproval("act_001").thenAccept(approved -> {
            System.out.println("✅ Aprovação para act_001: " + (approved ? "aprovada" : "rejeitada"));
        });
        
        // Aguardar um pouco
        Thread.sleep(2000);
        
        // Aprovar ação
        controller.approveAction("act_001");
        
        // Simular monitoramento
        System.out.println("\n📊 Monitorando recursos...");
        Thread.sleep(6000);
        
        // Verificar saúde do sistema
        System.out.println("\n🏥 Saúde do Sistema:");
        Map<String, Object> health = controller.getSystemHealth();
        health.forEach((key, value) -> {
            if (value instanceof Map) {
                System.out.println("  " + key + ":");
                ((Map<?, ?>) value).forEach((k, v) -> 
                    System.out.println("    " + k + ": " + v));
            } else {
                System.out.println("  " + key + ": " + value);
            }
        });
        
        // Testar modo seguro
        System.out.println("\n🛡️ Ativando modo seguro...");
        controller.setControlMode(ControlMode.SAFE);
        
        Thread.sleep(2000);
        
        // Verificar ações pausadas
        System.out.println("⏸️ Ações pausadas: " + controller.getPausedActions());
        
        // Encerrar
        controller.shutdown();
        
        System.out.println("\n✅ Demonstração concluída");
    }
}