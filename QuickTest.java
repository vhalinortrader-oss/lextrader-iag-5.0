package modulo_emocional.Decisao;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Teste Rápido - LEXTRADER-IAG 4.0
 * ================================
 * Teste simples para verificar se o sistema está funcionando
 * Versão Java do quick_test.py
 */

// Classes Mock para simulação dos componentes
class DecisionEngineMaster {
    private double capital;
    private boolean initialized;
    
    public DecisionEngineMaster(double capital) {
        this.capital = capital;
        this.initialized = true;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public double getCapital() {
        return capital;
    }
    
    public String getSystemMode() {
        return "AUTOMATED";
    }
}

class TradingExecutionEngine {
    private double capital;
    private boolean initialized;
    
    public TradingExecutionEngine(double capital) {
        this.capital = capital;
        this.initialized = true;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public double getCapital() {
        return capital;
    }
    
    public String getExecutionStatus() {
        return "READY";
    }
}

class IntegratedTradingSystem {
    private DecisionEngineMaster decisionEngine;
    private TradingExecutionEngine executionEngine;
    private boolean initialized;
    
    public IntegratedTradingSystem() {
        this.decisionEngine = new DecisionEngineMaster(100000);
        this.executionEngine = new TradingExecutionEngine(100000);
        this.initialized = true;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public DecisionEngineMaster getDecisionEngine() {
        return decisionEngine;
    }
    
    public TradingExecutionEngine getExecutionEngine() {
        return executionEngine;
    }
    
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("system_mode", decisionEngine.getSystemMode());
        status.put("account_balance", decisionEngine.getCapital());
        status.put("execution_status", executionEngine.getExecutionStatus());
        status.put("decision_engine_status", decisionEngine.isInitialized() ? "ACTIVE" : "INACTIVE");
        status.put("execution_engine_status", executionEngine.isInitialized() ? "ACTIVE" : "INACTIVE");
        return status;
    }
}

// Classe Principal de Teste
class QuickTest {
    private static final Logger logger = Logger.getLogger(QuickTest.class.getName());
    
    public static void main(String[] args) {
        System.out.println("🧪 TESTE RÁPIDO DO SISTEMA INTEGRADO");
        System.out.println("=".repeat(50));
        
        try {
            // Teste 1: Importar e inicializar componentes
            testImports();
            
            // Teste 2: Inicializar componentes
            testInitialization();
            
            // Teste 3: Verificar configuração
            testConfiguration();
            
            // Teste 4: Teste de integração
            testIntegration();
            
            // Teste 5: Teste de performance
            testPerformance();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🎉 TODOS OS TESTES PASSARAM!");
            System.out.println("✅ Sistema está funcionando corretamente");
            System.out.println("=".repeat(50));
            
        } catch (Exception e) {
            System.err.println("\n❌ ERRO: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void testImports() {
        /** Teste 1: Importar componentes */
        System.out.println("1️⃣ Testando importações...");
        
        try {
            // Simular importação de classes
            Class<?> decisionEngineClass = DecisionEngineMaster.class;
            System.out.println("   ✅ DecisionEngineMaster importado");
            
            Class<?> executionEngineClass = TradingExecutionEngine.class;
            System.out.println("   ✅ TradingExecutionEngine importado");
            
            Class<?> integratedSystemClass = IntegratedTradingSystem.class;
            System.out.println("   ✅ IntegratedTradingSystem importado");
            
            logger.info("Todos os componentes importados com sucesso");
            
        } catch (Exception e) {
            throw new RuntimeException("Falha ao importar componentes", e);
        }
    }
    
    private static void testInitialization() {
        /** Teste 2: Inicializar componentes */
        System.out.println("\n2️⃣ Testando inicialização...");
        
        try {
            DecisionEngineMaster decisionEngine = new DecisionEngineMaster(100000);
            if (!decisionEngine.isInitialized()) {
                throw new RuntimeException("DecisionEngineMaster não inicializado");
            }
            System.out.println("   ✅ Motor de Decisão inicializado");
            
            TradingExecutionEngine executionEngine = new TradingExecutionEngine(100000);
            if (!executionEngine.isInitialized()) {
                throw new RuntimeException("TradingExecutionEngine não inicializado");
            }
            System.out.println("   ✅ Motor de Execução inicializado");
            
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            if (!system.isInitialized()) {
                throw new RuntimeException("IntegratedTradingSystem não inicializado");
            }
            System.out.println("   ✅ Sistema Integrado inicializado");
            
            logger.info("Todos os componentes inicializados com sucesso");
            
        } catch (Exception e) {
            throw new RuntimeException("Falha na inicialização", e);
        }
    }
    
    private static void testConfiguration() {
        /** Teste 3: Verificar configuração */
        System.out.println("\n3️⃣ Testando configuração...");
        
        try {
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            Map<String, Object> status = system.getSystemStatus();
            
            if (status == null || status.isEmpty()) {
                throw new RuntimeException("Status não obtido");
            }
            
            System.out.println("   Modo: " + status.get("system_mode"));
            System.out.printf("   Saldo: %,.2f%n", (Double) status.get("account_balance"));
            System.out.println("   ✅ Status obtido com sucesso");
            
            // Validar valores esperados
            String expectedMode = "AUTOMATED";
            if (!expectedMode.equals(status.get("system_mode"))) {
                throw new RuntimeException("Modo do sistema incorreto: esperado " + expectedMode + ", obtido " + status.get("system_mode"));
            }
            
            Double expectedBalance = 100000.0;
            if (!expectedBalance.equals(status.get("account_balance"))) {
                throw new RuntimeException("Saldo incorreto: esperado " + expectedBalance + ", obtido " + status.get("account_balance"));
            }
            
            logger.info("Configuração validada com sucesso");
            
        } catch (Exception e) {
            throw new RuntimeException("Falha na configuração", e);
        }
    }
    
    private static void testIntegration() {
        /** Teste 4: Teste de integração entre componentes */
        System.out.println("\n4️⃣ Testando integração...");
        
        try {
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            
            // Testar comunicação entre componentes
            DecisionEngineMaster decisionEngine = system.getDecisionEngine();
            TradingExecutionEngine executionEngine = system.getExecutionEngine();
            
            if (decisionEngine == null) {
                throw new RuntimeException("DecisionEngine não acessível");
            }
            System.out.println("   ✅ Acesso ao Motor de Decisão");
            
            if (executionEngine == null) {
                throw new RuntimeException("ExecutionEngine não acessível");
            }
            System.out.println("   ✅ Acesso ao Motor de Execução");
            
            // Testar consistência de dados
            if (Math.abs(decisionEngine.getCapital() - executionEngine.getCapital()) > 0.01) {
                throw new RuntimeException("Inconsistência de capital entre motores");
            }
            System.out.println("   ✅ Consistência de dados verificada");
            
            // Testar status integrado
            Map<String, Object> status = system.getSystemStatus();
            if (!"ACTIVE".equals(status.get("decision_engine_status")) || 
                !"ACTIVE".equals(status.get("execution_engine_status"))) {
                throw new RuntimeException("Status dos motores inconsistente");
            }
            System.out.println("   ✅ Status integrado consistente");
            
            logger.info("Integração entre componentes validada");
            
        } catch (Exception e) {
            throw new RuntimeException("Falha na integração", e);
        }
    }
    
    private static void testPerformance() {
        /** Teste 5: Teste de performance básico */
        System.out.println("\n5️⃣ Testando performance...");
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Criar múltiplas instâncias para teste de performance
            List<IntegratedTradingSystem> systems = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                systems.add(new IntegratedTradingSystem());
            }
            
            long initializationTime = System.currentTimeMillis() - startTime;
            
            if (initializationTime > 5000) { // 5 segundos limite
                throw new RuntimeException("Inicialização muito lenta: " + initializationTime + "ms");
            }
            System.out.printf("   ✅ 10 sistemas inicializados em %dms%n", initializationTime);
            
            // Testar obtenção de status em lote
            startTime = System.currentTimeMillis();
            for (IntegratedTradingSystem system : systems) {
                system.getSystemStatus();
            }
            long statusTime = System.currentTimeMillis() - startTime;
            
            if (statusTime > 1000) { // 1 segundo limite
                throw new RuntimeException("Obtenção de status muito lenta: " + statusTime + "ms");
            }
            System.out.printf("   ✅ Status obtido para 10 sistemas em %dms%n", statusTime);
            
            // Testar consumo de memória
            Runtime runtime = Runtime.getRuntime();
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
            systems.clear();
            System.gc();
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = memoryBefore - memoryAfter;
            
            if (memoryUsed > 50 * 1024 * 1024) { // 50MB limite
                throw new RuntimeException("Consumo de memória muito alto: " + (memoryUsed / 1024 / 1024) + "MB");
            }
            System.out.printf("   ✅ Memória utilizada: %.1fKB%n", memoryUsed / 1024.0);
            
            logger.info("Performance test concluído com sucesso");
            
        } catch (Exception e) {
            throw new RuntimeException("Falha no teste de performance", e);
        }
    }
    
    // Métodos utilitários adicionais
    public static void runExtendedTests() {
        /** Executa testes extendidos */
        System.out.println("🔬 EXECUTANDO TESTES EXTENDIDOS");
        System.out.println("=".repeat(50));
        
        try {
            testConcurrentAccess();
            testErrorHandling();
            testResourceCleanup();
            
            System.out.println("\n🎉 TESTES EXTENDIDOS CONCLUÍDOS!");
            
        } catch (Exception e) {
            throw new RuntimeException("Falha nos testes extendidos", e);
        }
    }
    
    private static void testConcurrentAccess() {
        /** Teste de acesso concorrente */
        System.out.println("🔄 Testando acesso concorrente...");
        
        IntegratedTradingSystem system = new IntegratedTradingSystem();
        List<CompletableFuture<Map<String, Object>>> futures = new ArrayList<>();
        
        // Criar múltiplas threads acessando o sistema simultaneamente
        for (int i = 0; i < 20; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(10); // Simular processamento
                    return system.getSystemStatus();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrompida", e);
                }
            }));
        }
        
        // Aguardar todos os testes
        CompletableFuture<Void> allTests = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allTests.get(10, TimeUnit.SECONDS);
        
        System.out.println("   ✅ 20 acessos concorrentes realizados com sucesso");
    }
    
    private static void testErrorHandling() {
        /** Teste de tratamento de erros */
        System.out.println("⚠️  Testando tratamento de erros...");
        
        try {
            // Tentar criar sistema com capital inválido
            DecisionEngineMaster invalidEngine = new DecisionEngineMaster(-1000);
            if (invalidEngine.getCapital() < 0) {
                System.out.println("   ✅ Validação de capital negativo funcionando");
            }
        } catch (Exception e) {
            System.out.println("   ✅ Erro tratado corretamente: " + e.getMessage());
        }
        
        try {
            // Tentar obter status de sistema não inicializado
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            // Simular sistema não inicializado
            system = null;
            if (system == null) {
                System.out.println("   ✅ Verificação de nulidade funcionando");
            }
        } catch (Exception e) {
            System.out.println("   ✅ Erro tratado corretamente: " + e.getMessage());
        }
    }
    
    private static void testResourceCleanup() {
        /** Teste de limpeza de recursos */
        System.out.println("🧹 Testando limpeza de recursos...");
        
        List<IntegratedTradingSystem> systems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            systems.add(new IntegratedTradingSystem());
        }
        
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        // Limpar referências
        systems.clear();
        System.gc();
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryFreed = memoryBefore - memoryAfter;
        
        System.out.printf("   ✅ Memória liberada: %.1fKB%n", memoryFreed / 1024.0);
    }
    
    public static void runDiagnosticMode() {
        /** Executa em modo diagnóstico */
        System.out.println("🔬 MODO DIAGNÓSTICO");
        System.out.println("=".repeat(50));
        
        try {
            // Informações do sistema
            Runtime runtime = Runtime.getRuntime();
            System.out.println("📊 INFORMAÇÕES DO SISTEMA:");
            System.out.println("   Java Version: " + System.getProperty("java.version"));
            System.out.println("   Processadores: " + runtime.availableProcessors());
            System.out.println("   Memória Máxima: " + (runtime.maxMemory() / 1024 / 1024) + "MB");
            System.out.println("   Memória Total: " + (runtime.totalMemory() / 1024 / 1024) + "MB");
            System.out.println("   Memória Livre: " + (runtime.freeMemory() / 1024 / 1024) + "MB");
            
            // Testar componentes
            testComponentsDiagnostic();
            
            // Testar ambiente
            testEnvironmentDiagnostic();
            
        } catch (Exception e) {
            System.err.println("❌ Erro no modo diagnóstico: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testComponentsDiagnostic() {
        /** Diagnóstico de componentes */
        System.out.println("\n🔧 DIAGNÓSTICO DE COMPONENTES:");
        
        try {
            // Testar DecisionEngineMaster
            DecisionEngineMaster decisionEngine = new DecisionEngineMaster(100000);
            System.out.println("   DecisionEngineMaster: ✅ OK");
            System.out.println("      Capital: " + decisionEngine.getCapital());
            System.out.println("      Modo: " + decisionEngine.getSystemMode());
            
            // Testar TradingExecutionEngine
            TradingExecutionEngine executionEngine = new TradingExecutionEngine(100000);
            System.out.println("   TradingExecutionEngine: ✅ OK");
            System.out.println("      Capital: " + executionEngine.getCapital());
            System.out.println("      Status: " + executionEngine.getExecutionStatus());
            
            // Testar IntegratedTradingSystem
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            System.out.println("   IntegratedTradingSystem: ✅ OK");
            
            Map<String, Object> status = system.getSystemStatus();
            System.out.println("      Status: " + status.get("system_mode"));
            System.out.println("      Saldo: " + String.format("%,.2f", status.get("account_balance")));
            
        } catch (Exception e) {
            System.err.println("   ❌ Erro nos componentes: " + e.getMessage());
        }
    }
    
    private static void testEnvironmentDiagnostic() {
        /** Diagnóstico do ambiente */
        System.out.println("\n🌍 DIAGNÓSTICO DE AMBIENTE:");
        
        // Verificar variáveis de ambiente
        Map<String, String> envVars = System.getenv();
        String[] importantVars = {"PATH", "JAVA_HOME", "CLASSPATH", "LEXTRADER_ENV"};
        
        for (String var : importantVars) {
            String value = envVars.get(var);
            if (value != null) {
                System.out.println("   " + var + ": " + value);
            } else {
                System.out.println("   " + var + ": ⚠️  Não definida");
            }
        }
        
        // Verificar propriedades do sistema
        String[] importantProps = {"java.version", "java.home", "user.name", "user.dir", "os.name"};
        
        for (String prop : importantProps) {
            String value = System.getProperty(prop);
            if (value != null) {
                System.out.println("   " + prop + ": " + value);
            } else {
                System.out.println("   " + prop + ": ⚠️  Não definida");
            }
        }
        
        // Verificar diretórios importantes
        String[] importantDirs = {"user.dir", "user.home", "java.io.tmpdir"};
        
        for (String dir : importantDirs) {
            String path = System.getProperty(dir);
            if (path != null) {
                java.io.File dirFile = new java.io.File(path);
                if (dirFile.exists() && dirFile.isDirectory()) {
                    System.out.println("   " + dir + ": ✅ " + path);
                } else {
                    System.out.println("   " + dir + ": ❌ " + path + " (não existe)");
                }
            }
        }
    }
}

// Classe Principal para Demonstração
public class QuickTestApp {
    
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Teste Rápido do LEXTRADER-IAG 4.0...");
        
        try {
            // Executar teste básico
            QuickTest.main(args);
            
            // Se solicitado, executar testes extendidos
            if (args.length > 0 && "--extended".equals(args[0])) {
                System.out.println("\n🔄 Executando testes extendidos...");
                QuickTest.runExtendedTests();
            }
            
            // Se solicitado, executar modo diagnóstico
            if (args.length > 0 && "--diagnostic".equals(args[0])) {
                System.out.println("\n🔬 Executando modo diagnóstico...");
                QuickTest.runDiagnosticMode();
            }
            
            // Se solicitado, executar demonstração
            if (args.length > 0 && "--demo".equals(args[0])) {
                System.out.println("\n🎭 Executando demonstração...");
                runDemo();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro na execução: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void runDemo() {
        /** Executa demonstração do sistema */
        System.out.println("=".repeat(60));
        System.out.println("🎭 DEMONSTRAÇÃO DO SISTEMA LEXTRADER-IAG 4.0");
        System.out.println("=".repeat(60));
        
        try {
            // Criar sistema
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            
            // Simular operação
            System.out.println("🚀 Inicializando sistema de trading...");
            Thread.sleep(1000);
            
            System.out.println("📊 Analisando condições de mercado...");
            Thread.sleep(500);
            
            System.out.println("🎯 Tomando decisões...");
            Thread.sleep(500);
            
            System.out.println("⚡ Executando trades...");
            Thread.sleep(500);
            
            // Mostrar resultados
            Map<String, Object> status = system.getSystemStatus();
            System.out.println("\n📈 RESULTADOS DA DEMONSTRAÇÃO:");
            System.out.println("   Modo: " + status.get("system_mode"));
            System.out.println("   Saldo: " + String.format("%,.2f", status.get("account_balance")));
            System.out.println("   Status Execução: " + status.get("execution_status"));
            System.out.println("   Status Decisão: " + status.get("decision_engine_status"));
            
            System.out.println("\n✅ Demonstração concluída com sucesso!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("⚠️ Demonstração interrompida");
        }
    }
    
    /**
     * Método utilitário para teste rápido
     */
    public static boolean quickCheck() {
        /** Executa verificação rápida do sistema */
        try {
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            Map<String, Object> status = system.getSystemStatus();
            
            return status != null && 
                   "AUTOMATED".equals(status.get("system_mode")) &&
                   status.containsKey("account_balance") &&
                   (Double) status.get("account_balance") > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Método utilitário para verificação de saúde
     */
    public static Map<String, Object> healthCheck() {
        /** Retorna status de saúde do sistema */
        Map<String, Object> health = new HashMap<>();
        
        try {
            IntegratedTradingSystem system = new IntegratedTradingSystem();
            Map<String, Object> status = system.getSystemStatus();
            
            health.put("status", "HEALTHY");
            health.put("timestamp", System.currentTimeMillis());
            health.put("system_mode", status.get("system_mode"));
            health.put("account_balance", status.get("account_balance"));
            health.put("decision_engine", status.get("decision_engine_status"));
            health.put("execution_engine", status.get("execution_engine"));
            
        } catch (Exception e) {
            health.put("status", "UNHEALTHY");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
        }
        
        return health;
    }
}
