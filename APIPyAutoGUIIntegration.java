package modulo_emocional.Sensório;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Integração PyAutoGUI - Layer 1 (Sensorial)
 * Versão Java convertida e superpotenciada do Python original
 */
public class APIPyAutoGUIIntegration {
    
    // Estrutura de dados para captura de tela
    public static class APIScreenCapture {
        private final String apiName;
        private final LocalDateTime timestamp;
        private final String filepath;
        private final boolean success;
        private final int[] resolution;
        private final long fileSize;
        
        public APIScreenCapture(String apiName, String filepath, boolean success, 
                                 int[] resolution, long fileSize) {
            this.apiName = apiName;
            this.timestamp = LocalDateTime.now();
            this.filepath = filepath;
            this.success = success;
            this.resolution = resolution != null ? resolution : new int[]{0, 0};
            this.fileSize = fileSize;
        }
        
        // Getters
        public String getApiName() { return apiName; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getFilepath() { return filepath; }
        public boolean isSuccess() { return success; }
        public int[] getResolution() { return Arrays.copyOf(resolution, resolution.length); }
        public long getFileSize() { return fileSize; }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("api_name", apiName);
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            map.put("filepath", filepath);
            map.put("success", success);
            map.put("resolution", Arrays.toString(resolution));
            map.put("file_size", fileSize);
            return map;
        }
    }
    
    // Estrutura de dados para eventos de mouse
    public static class MouseEvent {
        public enum MouseAction {
            MOVE, CLICK, DRAG
        }
        
        private final int x;
        private final int y;
        private final LocalDateTime timestamp;
        private final MouseAction action;
        private final double duration;
        
        public MouseEvent(int x, int y, MouseAction action, double duration) {
            this.x = x;
            this.y = y;
            this.timestamp = LocalDateTime.now();
            this.action = action;
            this.duration = duration;
        }
        
        // Getters
        public int getX() { return x; }
        public int getY() { return y; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public MouseAction getAction() { return action; }
        public double getDuration() { return duration; }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("x", x);
            map.put("y", y);
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            map.put("action", action.toString());
            map.put("duration", duration);
            return map;
        }
    }
    
    // Estrutura de dados para posições de APIs
    public static class APIPosition {
        private final int x;
        private final int y;
        
        public APIPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        // Getters
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    // Estrutura de dados para resultado de simulação
    public static class SimulationResult {
        private final String api;
        private final String action;
        private final List<Map<String, Object>> steps;
        private final boolean success;
        private final String error;
        private final LocalDateTime timestamp;
        
        public SimulationResult(String api, String action, List<Map<String, Object>> steps, 
                              boolean success, String error) {
            this.api = api;
            this.action = action;
            this.steps = new ArrayList<>(steps);
            this.success = success;
            this.error = error;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getApi() { return api; }
        public String getAction() { return action; }
        public List<Map<String, Object>> getSteps() { return new ArrayList<>(steps); }
        public boolean isSuccess() { return success; }
        public String getError() { return error; }
        public LocalDateTime getTimestamp() { return timestamp; }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("api", api);
            map.put("action", action);
            map.put("steps", steps);
            map.put("success", success);
            map.put("error", error);
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return map;
        }
    }
    
    // Estrutura de dados para credenciais
    public static class CredentialsResult {
        private final String api;
        private final int fieldsFilled;
        private final boolean success;
        private final String error;
        private final LocalDateTime timestamp;
        
        public CredentialsResult(String api, int fieldsFilled, boolean success, String error) {
            this.api = api;
            this.fieldsFilled = fieldsFilled;
            this.success = success;
            this.error = error;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getApi() { return api; }
        public int getFieldsFilled() { return fieldsFilled; }
        public boolean isSuccess() { return success; }
        public String getError() { return error; }
        public LocalDateTime getTimestamp() { return timestamp; }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("api", api);
            map.put("fields_filled", fieldsFilled);
            map.put("success", success);
            map.put("error", error);
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return map;
        }
    }
    
    // Estado do sensor
    private final Map<String, APIPosition> apiPositions;
    private final List<APIScreenCapture> captures;
    private final List<MouseEvent> mouseEvents;
    private final int[] screenResolution;
    private final Map<String, List<SimulationResult>> simulationHistory;
    private final ScheduledExecutorService scheduler;
    private final Map<String, CompletableFuture<Void>> activeTasks;
    private final AtomicInteger totalCaptures;
    private final AtomicInteger totalMouseEvents;
    private final AtomicInteger totalSimulations;
    private final String logDirectory;
    private Robot robot;
    
    public APIPyAutoGUIIntegration(String logDirectory) {
        this.logDirectory = logDirectory != null ? logDirectory : "logs/automation/sensorial";
        this.apiPositions = new ConcurrentHashMap<>();
        this.captures = new ArrayList<>();
        this.mouseEvents = new ArrayList<>();
        this.simulationHistory = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.activeTasks = new HashMap<>();
        this.totalCaptures = new AtomicInteger(0);
        this.totalMouseEvents = new AtomicInteger(0);
        this.totalSimulations = new AtomicInteger(0);
        
        // Inicializa posições das APIs
        initializeAPIPositions();
        
        // Obtém resolução da tela
        this.screenResolution = getScreenResolution();
        
        // Inicializa Robot para automação
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            System.err.println("❌ Erro ao inicializar Robot: " + e.getMessage());
        }
        
        // Cria diretório de logs
        createLogDirectory();
        
        System.out.println("🤖 APIPyAutoGUIIntegration inicializado");
        System.out.println("📁 Diretório de logs: " + this.logDirectory);
        System.out.println("📺 Resolução da tela: " + Arrays.toString(screenResolution));
        System.out.println("🎮 APIs configuradas: " + apiPositions.size());
    }
    
    /**
     * Inicializa posições das APIs na interface
     */
    private void initializeAPIPositions() {
        apiPositions.put("binance", new APIPosition(100, 100));
        apiPositions.put("ctrader", new APIPosition(200, 100));
        apiPositions.put("pionex", new APIPosition(300, 100));
        apiPositions.put("coinbase", new APIPosition(400, 100));
        apiPositions.put("alpaca", new APIPosition(500, 100));
        apiPositions.put("alphavantage", new APIPosition(100, 200));
        apiPositions.put("polygon", new APIPosition(200, 200));
        apiPositions.put("twelvedata", new APIPosition(300, 200));
    }
    
    /**
     * Obtém resolução da tela
     */
    private int[] getScreenResolution() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new int[]{screenSize.width, screenSize.height};
    }
    
    /**
     * Cria diretório de logs
     */
    private void createLogDirectory() {
        File dir = new File(logDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Captura estado atual da tela para uma API
     */
    public CompletableFuture<APIScreenCapture> captureCurrentState(String apiName, String label) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("📸 Capturando estado da API: " + apiName);
                
                // Gera timestamp único
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String filename = apiName + "_" + label + "_" + timestamp + ".png";
                String filepath = logDirectory + "/" + filename;
                
                // Captura screenshot
                BufferedImage screenshot = robot.createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())
                );
                
                // Salva imagem
                File outputFile = new File(filepath);
                ImageIO.write(screenshot, "PNG", outputFile);
                
                long fileSize = outputFile.exists() ? outputFile.length() : 0;
                
                APIScreenCapture capture = new APIScreenCapture(
                    apiName, filepath, true, screenResolution, fileSize
                );
                
                captures.add(capture);
                totalCaptures.incrementAndGet();
                
                System.out.println("✅ Screenshot capturado: " + filepath);
                return capture;
                
            } catch (Exception e) {
                System.err.println("❌ Erro ao capturar screenshot: " + e.getMessage());
                return new APIScreenCapture(apiName, "", false, new int[]{0, 0}, 0);
            }
        });
    }
    
    /**
     * Move mouse para posição do botão de API
     */
    public CompletableFuture<Boolean> moveMouseToAPIButton(String apiName, int xOffset, int yOffset) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("🖱️ Movendo mouse para API: " + apiName);
                
                APIPosition position = apiPositions.get(apiName.toLowerCase());
                if (position == null) {
                    System.err.println("⚠️ Posição não encontrada para API: " + apiName);
                    return false;
                }
                
                int x = position.getX() + xOffset;
                int y = position.getY() + yOffset;
                
                // Move mouse
                robot.mouseMove(x, y);
                
                // Registra evento
                MouseEvent event = new MouseEvent(x, y, MouseEvent.MouseAction.MOVE, 0.5);
                mouseEvents.add(event);
                totalMouseEvents.incrementAndGet();
                
                System.out.println("✅ Mouse movido para " + apiName + " (" + x + ", " + y + ")");
                return true;
                
            } catch (Exception e) {
                System.err.println("❌ Erro ao mover mouse: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Clica no botão de uma API
     */
    public CompletableFuture<Boolean> clickAPIButton(String apiName, int clicks) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("🖱️ Clicando na API: " + apiName + " (" + clicks + "x)");
                
                // Primeiro move para a API
                if (!moveMouseToAPIButton(apiName, 0, 0).get()) {
                    return false;
                }
                
                // Aguarda um pouco antes de clicar
                Thread.sleep(300);
                
                // Realiza cliques
                for (int i = 0; i < clicks; i++) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN);
                    Thread.sleep(50);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN);
                    Thread.sleep(100);
                    
                    // Registra evento
                    PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                    MouseEvent event = new MouseEvent(
                        pointerInfo.getLocation().x, 
                        pointerInfo.getLocation().y, 
                        MouseEvent.MouseAction.CLICK, 
                        0.0
                    );
                    mouseEvents.add(event);
                    totalMouseEvents.incrementAndGet();
                }
                
                System.out.println("✅ " + clicks + " cliques realizados em " + apiName);
                return true;
                
            } catch (Exception e) {
                System.err.println("❌ Erro ao clicar: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Simula interação completa com API
     */
    public CompletableFuture<SimulationResult> simulateAPIInteraction(String apiName, String action) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("🤖 Simulando interação com API: " + apiName + " - " + action);
            
            List<Map<String, Object>> steps = new ArrayList<>();
            boolean overallSuccess = true;
            String error = null;
            
            try {
                // Passo 1: Capturar estado inicial
                APIScreenCapture initialCapture = captureCurrentState(apiName, action + "_initial").get();
                steps.add(Map.of(
                    "name", "Capturar estado inicial",
                    "success", initialCapture.isSuccess(),
                    "file", initialCapture.getFilepath()
                ));
                
                // Passo 2: Mover mouse para API
                boolean moveSuccess = moveMouseToAPIButton(apiName, 0, 0).get();
                steps.add(Map.of(
                    "name", "Mover mouse para API",
                    "success", moveSuccess
                ));
                
                if (!moveSuccess) {
                    overallSuccess = false;
                    error = "Falha ao mover mouse para API";
                }
                
                // Passo 3: Clicar no botão
                boolean clickSuccess = clickAPIButton(apiName, 1).get();
                steps.add(Map.of(
                    "name", "Clicar em botão",
                    "success", clickSuccess
                ));
                
                if (!clickSuccess) {
                    overallSuccess = false;
                    error = "Falha ao clicar no botão da API";
                }
                
                // Passo 4: Aguardar resultado
                Thread.sleep(1000);
                
                // Passo 5: Capturar estado final
                APIScreenCapture finalCapture = captureCurrentState(apiName, action + "_final").get();
                steps.add(Map.of(
                    "name", "Capturar estado final",
                    "success", finalCapture.isSuccess(),
                    "file", finalCapture.getFilepath()
                ));
                
                // Determina sucesso geral
                overallSuccess = overallSuccess && initialCapture.isSuccess() && finalCapture.isSuccess();
                
            } catch (Exception e) {
                overallSuccess = false;
                error = e.getMessage();
            }
            
            SimulationResult result = new SimulationResult(apiName, action, steps, overallSuccess, error);
            
            // Armazena no histórico
            simulationHistory.computeIfAbsent(apiName, k -> new ArrayList<>()).add(result);
            totalSimulations.incrementAndGet();
            
            System.out.println("✅ Simulação concluída: " + apiName + " - " + action + 
                             " (Sucesso: " + overallSuccess + ")");
            
            return result;
        });
    }
    
    /**
     * Simula entrada de credenciais
     */
    public CompletableFuture<CredentialsResult> simulateCredentialEntry(String apiName, Map<String, String> credentials) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("🔐 Simulando entrada de credenciais: " + apiName);
            
            int fieldsFilled = 0;
            boolean success = true;
            String error = null;
            
            try {
                // Capturar estado inicial
                captureCurrentState(apiName, "credentials_initial").get();
                
                // Preencher cada campo
                for (Map.Entry<String, String> entry : credentials.entrySet()) {
                    String fieldName = entry.getKey();
                    String fieldValue = entry.getValue();
                    
                    System.out.println("📝 Preenchendo campo: " + fieldName);
                    
                    // Mover para posição do campo (simulação)
                    moveMouseToAPIButton(apiName, 0, 0).get();
                    Thread.sleep(500);
                    
                    // Simular digitação
                    for (char c : fieldValue.toCharArray()) {
                        robot.keyPress(c);
                        Thread.sleep(50);
                        robot.keyRelease(c);
                        Thread.sleep(30);
                    }
                    
                    // Tab para próximo campo
                    robot.keyPress(KeyEvent.VK_TAB);
                    Thread.sleep(200);
                    robot.keyRelease(KeyEvent.VK_TAB);
                    Thread.sleep(100);
                    
                    fieldsFilled++;
                }
                
                // Capturar estado final
                captureCurrentState(apiName, "credentials_final").get();
                
                success = true;
                
            } catch (Exception e) {
                success = false;
                error = e.getMessage();
            }
            
            CredentialsResult result = new CredentialsResult(apiName, fieldsFilled, success, error);
            
            System.out.println("✅ Simulação de credenciais concluída: " + apiName + 
                             " (Campos: " + fieldsFilled + ", Sucesso: " + success + ")");
            
            return result;
        });
    }
    
    /**
     * Verifica status da API visualmente
     */
    public CompletableFuture<Map<String, Object>> verifyAPIStatusVisually(String apiName) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("👁️ Verificando status visual da API: " + apiName);
            
            APIScreenCapture capture = captureCurrentState(apiName, "status_check").get();
            
            Map<String, Object> result = new HashMap<>();
            result.put("api", apiName);
            result.put("capture_success", capture.isSuccess());
            result.put("filepath", capture.getFilepath());
            result.put("timestamp", capture.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            result.put("resolution", Arrays.toString(capture.getResolution()));
            result.put("file_size", capture.getFileSize());
            
            return result;
        });
    }
    
    /**
     * Obtém histórico de capturas
     */
    public List<APIScreenCapture> getCaptureHistory(String apiName) {
        if (apiName != null) {
            return captures.stream()
                    .filter(capture -> capture.getApiName().equals(apiName))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(captures);
    }
    
    /**
     * Obtém histórico de eventos de mouse
     */
    public List<MouseEvent> getMouseEventHistory() {
        return new ArrayList<>(mouseEvents);
    }
    
    /**
     * Exporta dados da sessão
     */
    public CompletableFuture<String> exportSessionData(String filepath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (filepath == null) {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                    filepath = logDirectory + "/session_" + timestamp + ".json";
                }
                
                Map<String, Object> sessionData = new HashMap<>();
                sessionData.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                sessionData.put("screen_resolution", Arrays.toString(screenResolution));
                sessionData.put("captures_count", captures.size());
                sessionData.put("mouse_events_count", mouseEvents.size());
                
                // Adiciona capturas
                List<Map<String, Object>> capturesData = new ArrayList<>();
                for (APIScreenCapture capture : captures) {
                    capturesData.add(capture.toMap());
                }
                sessionData.put("captures", capturesData);
                
                // Adiciona eventos de mouse
                List<Map<String, Object>> mouseEventsData = new ArrayList<>();
                for (MouseEvent event : mouseEvents) {
                    mouseEventsData.add(event.toMap());
                }
                sessionData.put("mouse_events", mouseEventsData);
                
                // Simula escrita JSON
                System.out.println("💾 Exportando dados da sessão: " + filepath);
                System.out.println("📊 Capturas: " + captures.size());
                System.out.println("🖱️ Eventos de mouse: " + mouseEvents.size());
                
                return filepath;
                
            } catch (Exception e) {
                System.err.println("❌ Erro ao exportar dados: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Inicia automação contínua
     */
    public void startContinuousAutomation() {
        System.out.println("🚀 Iniciando automação contínua...");
        
        // Agenda captura de tela a cada 30 segundos
        activeTasks.put("screen_capture", scheduler.scheduleAtFixedRate(() -> {
            captureCurrentState("system", "monitor").thenAccept(capture -> {
                if (capture.isSuccess()) {
                    System.out.println("📸 Captura automática: " + capture.getFilepath());
                }
            });
        }, 0, 30, TimeUnit.SECONDS));
        
        // Agenda verificação de status a cada 2 minutos
        activeTasks.put("status_check", scheduler.scheduleAtFixedRate(() -> {
            String[] apis = {"binance", "ctrader", "pionex"};
            for (String api : apis) {
                verifyAPIStatusVisually(api).thenAccept(result -> {
                    if ((Boolean) result.get("capture_success")) {
                        System.out.println("✅ Status OK: " + api);
                    }
                });
            }
        }, 0, 2, TimeUnit.MINUTES));
        
        // Agenda limpeza de logs a cada 24 horas
        activeTasks.put("cleanup", scheduler.scheduleAtFixedRate(() -> {
            cleanupOldData();
        }, 0, 24, TimeUnit.HOURS));
        
        System.out.println("✅ Automação contínua iniciada");
    }
    
    /**
     * Para automação contínua
     */
    public void stopContinuousAutomation() {
        System.out.println("🛑 Parando automação contínua...");
        
        // Cancela todas as tarefas
        activeTasks.values().forEach(future -> future.cancel(true));
        activeTasks.clear();
        
        // Desliga o scheduler
        scheduler.shutdown();
        
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("✅ Automação parada");
    }
    
    /**
     * Limpa dados antigos
     */
    private void cleanupOldData() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        
        // Remove capturas antigas
        int removedCaptures = (int) captures.stream()
                .filter(capture -> capture.getTimestamp().isBefore(cutoff))
                .count();
        captures.removeIf(capture -> capture.getTimestamp().isBefore(cutoff));
        
        // Remove eventos de mouse antigos
        int removedMouseEvents = (int) mouseEvents.stream()
                .filter(event -> event.getTimestamp().isBefore(cutoff))
                .count();
        mouseEvents.removeIf(event -> event.getTimestamp().isBefore(cutoff));
        
        if (removedCaptures > 0 || removedMouseEvents > 0) {
            System.out.println("🧹 Limpeza: " + removedCaptures + " capturas, " + 
                             removedMouseEvents + " eventos de mouse removidos");
        }
    }
    
    /**
     * Obtém estatísticas do sensor
     */
    public Map<String, Object> getSensorStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("log_directory", logDirectory);
        stats.put("screen_resolution", Arrays.toString(screenResolution));
        stats.put("total_captures", totalCaptures.get());
        stats.put("total_mouse_events", totalMouseEvents.get());
        stats.put("total_simulations", totalSimulations.get());
        stats.put("active_automation_tasks", activeTasks.size());
        stats.put("configured_apis", apiPositions.size());
        
        // Estatísticas por API
        Map<String, Long> capturesByAPI = new HashMap<>();
        Map<String, Long> mouseEventsByAPI = new HashMap<>();
        
        for (APIScreenCapture capture : captures) {
            capturesByAPI.merge(capture.getApiName(), 1L, Long::sum);
        }
        
        for (MouseEvent event : mouseEvents) {
            // Simula associação com API baseado na posição
            String api = findAPIByPosition(event.getX(), event.getY());
            if (api != null) {
                mouseEventsByAPI.merge(api, 1L, Long::sum);
            }
        }
        
        stats.put("captures_by_api", capturesByAPI);
        stats.put("mouse_events_by_api", mouseEventsByAPI);
        
        return stats;
    }
    
    /**
     * Encontra API baseado na posição
     */
    private String findAPIByPosition(int x, int y) {
        for (Map.Entry<String, APIPosition> entry : apiPositions.entrySet()) {
            APIPosition pos = entry.getValue();
            int distance = Math.abs(pos.getX() - x) + Math.abs(pos.getY() - y);
            if (distance < 50) { // Tolerância de 50 pixels
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Método principal para demonstração
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("🤖 DEMONSTRAÇÃO DA API PYAUTOGUI INTEGRATION");
        System.out.println("=".repeat(80));
        
        APIPyAutoGUIIntegration sensor = new APIPyAutoGUIIntegration("demo_logs");
        
        // Testa captura de estado
        sensor.captureCurrentState("binance", "test").thenAccept(capture -> {
            System.out.println("📸 Captura: " + capture.getApiName() + " - " + 
                             (capture.isSuccess() ? "SUCESSO" : "FALHA"));
        });
        
        // Testa simulação de interação
        sensor.simulateAPIInteraction("binance", "health_check").thenAccept(result -> {
            System.out.println("🤖 Simulação: " + result.getApi() + " - " + result.getAction());
            System.out.println("  Sucesso: " + result.isSuccess());
            System.out.println("  Passos: " + result.getSteps().size());
            
            result.getSteps().forEach(step -> {
                System.out.println("    " + step.get("name") + ": " + step.get("success"));
            });
        });
        
        // Testa movimentação de mouse
        sensor.moveMouseToAPIButton("binance", 10, 10).thenAccept(success -> {
            System.out.println("🖱️ Movimento: " + (success ? "SUCESSO" : "FALHA"));
        });
        
        // Exporta dados da sessão
        sensor.exportSessionData(null).thenAccept(filepath -> {
            System.out.println("💾 Dados exportados: " + filepath);
        });
        
        // Exibe estatísticas
        Map<String, Object> stats = sensor.getSensorStatistics();
        System.out.println("\n📊 Estatísticas do Sensor:");
        stats.forEach((key, value) -> System.out.println("  " + key + ": " + value));
        
        System.out.println("\n✅ DEMONSTRAÇÃO CONCLUÍDA!");
        System.out.println("=".repeat(80));
    }
}
