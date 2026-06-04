package modulo_emocional.Emocional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

/**
 * Módulo de Aprendizado Avançado - VHALINOR.IAG 5.0
 * ================================================
 * Sistemas de aprendizado contínuo e evolucional para rede neural artificial.
 * 
 * Este módulo implementa:
 * - Aprendizado Contínuo: Adaptação em tempo real baseada em fluxo de dados
 * - Aprendizado Evolucional: Otimização através de algoritmos genéticos e neuroevolução
 * 
 * Versão: 2.0.0
 * Autor: Sistema VHALINOR
 * Data: Fevereiro 2026
 */

class ConfiguracaoModulo {
    public String versao;
    public String autor;
    public String licenca;
    public String copyright;
    public boolean dependenciasOk;
    public Map<String, Boolean> modulosDisponiveis;
    public Map<String, String> pacotesRequeridos;
    public String caminhoModulo;
    
    public ConfiguracaoModulo(String versao, String autor, String licenca, String copyright) {
        this.versao = versao;
        this.autor = autor;
        this.licenca = licenca;
        this.copyright = copyright;
        this.modulosDisponiveis = new HashMap<>();
        this.pacotesRequeridos = new HashMap<>();
    }
    
    @Override
    public String toString() {
        return String.format("ConfigModulo{versao='%s', dependencias=%s}", 
            versao, dependenciasOk ? "OK" : "Faltando");
    }
}

class SistemaAprendizado {
    public String nome;
    public String tipo;
    public LocalDateTime dataCriacao;
    public boolean ativo;
    public Map<String, Object> configuracao;
    
    public SistemaAprendizado(String nome, String tipo, Map<String, Object> configuracao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.configuracao = new HashMap<>(configuracao);
    }
    
    @Override
    public String toString() {
        return String.format("Sistema{nome='%s', tipo='%s', ativo=%s}", 
            nome, tipo, ativo);
    }
}

class ResultadoProcessamento {
    public Map<String, Object> resultados;
    public LocalDateTime timestamp;
    public double tempoExecucao;
    public String modo;
    
    public ResultadoProcessamento(Map<String, Object> resultados, String modo, double tempoExecucao) {
        this.resultados = new HashMap<>(resultados);
        this.timestamp = LocalDateTime.now();
        this.tempoExecucao = tempoExecucao;
        this.modo = modo;
    }
    
    @Override
    public String toString() {
        return String.format("Resultado{modo='%s', tempo=%.3fs}", modo, tempoExecucao);
    }
}

class StatusModulo {
    public String versao;
    public boolean dependenciasOk;
    public Map<String, Boolean> modulosDisponiveis;
    public Map<String, String> pacotesRequeridos;
    public String caminhoModulo;
    public Map<String, String> versoesModulos;
    
    public StatusModulo(String versao, boolean dependenciasOk) {
        this.versao = versao;
        this.dependenciasOk = dependenciasOk;
        this.modulosDisponiveis = new HashMap<>();
        this.pacotesRequeridos = new HashMap<>();
        this.versoesModulos = new HashMap<>();
    }
    
    @Override
    public String toString() {
        return String.format("Status{versao='%s', modulos=%d}", 
            versao, modulosDisponiveis.size());
    }
}

// Classes de constantes
final class ModosAprendizado {
    public static final String CONTINUO = "continuo";
    public static final String EVOLUCIONAL = "evolucional";
    public static final String HIBRIDO = "hibrido";
    public static final String BATCH = "batch"; // Processamento em lote
    public static final String ONLINE = "online"; // Processamento online/streaming
    public static final String REFORCO = "reforco"; // Aprendizado por reforço
    
    private ModosAprendizado() {} // Impedir instanciação
}

final class MetricasAvaliacao {
    public static final String ACURACIA = "acuracia";
    public static final String PRECISAO = "precisao";
    public static final String RECALL = "recall";
    public static final String F1_SCORE = "f1_score";
    public static final String LOSS = "loss";
    public static final String ENTROPIA = "entropia";
    public static final String DIVERSIDADE = "diversidade";
    public static final String ADAPTABILIDADE = "adaptabilidade";
    
    private MetricasAvaliacao() {} // Impedir instanciação
}

// Classe principal do módulo
public class Aprendizado {
    
    // Metadados do módulo
    public static final String VERSION = "2.0.0";
    public static final String AUTHOR = "Sistema VHALINOR";
    public static final String LICENSE = "Proprietary - VHALINOR Research";
    public static final String COPYRIGHT = "Copyright 2026, VHALINOR Systems";
    
    // Logger do módulo
    private static final Logger logger = Logger.getLogger(Aprendizado.class.getName());
    
    // Dependências requeridas
    private static final Map<String, String> REQUIRED_PACKAGES = new HashMap<>();
    static {
        REQUIRED_PACKAGES.put("numpy", "1.21.0");
        REQUIRED_PACKAGES.put("scikit-learn", "1.0.0");
        REQUIRED_PACKAGES.put("pandas", "1.3.0");
        REQUIRED_PACKAGES.put("deap", "1.3.1"); // Para algoritmos genéticos
    }
    
    // Status dos módulos
    private static boolean aprendizadoContinuoAvailable = false;
    private static boolean aprendizadoEvolucionalAvailable = false;
    private static boolean dependenciesOk = false;
    
    // Configuração do módulo
    private static ConfiguracaoModulo configuracao;
    
    static {
        // Configurar logger
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers.length == 0) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("%s | %-20s | %-7s | %s%n",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        record.getLoggerName(),
                        record.getLevel(),
                        record.getMessage()
                    );
                }
            });
            logger.addHandler(handler);
        }
        logger.setLevel(Level.INFO);
        
        // Inicializar configuração
        configuracao = new ConfiguracaoModulo(VERSION, AUTHOR, LICENSE, COPYRIGHT);
        
        // Verificar dependências
        dependenciesOk = verificarDependencias();
        
        // Verificar disponibilidade de módulos
        verificarDisponibilidadeModulos();
        
        // Mensagem de inicialização
        if (dependenciesOk && (aprendizadoContinuoAvailable || aprendizadoEvolucionalAvailable)) {
            logger.info(String.format(
                "Módulo de Aprendizado Avançado v%s carregado. " +
                "Contínuo: %s, Evolucional: %s",
                VERSION,
                aprendizadoContinuoAvailable ? "✓" : "✗",
                aprendizadoEvolucionalAvailable ? "✓" : "✗"
            ));
        } else {
            logger.warning(String.format(
                "Módulo de Aprendizado carregado com limitações. " +
                "Dependências: %s",
                dependenciesOk ? "✓" : "✗"
            ));
        }
    }
    
    private static boolean verificarDependencias() {
        /** Verifica dependências necessárias */
        
        List<String> missingPackages = new ArrayList<>();
        List<String> outdatedPackages = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : REQUIRED_PACKAGES.entrySet()) {
            String packageName = entry.getKey();
            String minVersion = entry.getValue();
            
            try {
                // Simulação de verificação de dependência
                // Em Java, verificaríamos se as classes/bibliotecas estão disponíveis
                Class.forName(packageName.toUpperCase()); // Simulação
                
                // Simulação de verificação de versão
                String currentVersion = "1.22.0"; // Simulação
                if (compararVersoes(currentVersion, minVersion) < 0) {
                    outdatedPackages.add(String.format("%s (%s < %s)", packageName, currentVersion, minVersion));
                }
            } catch (ClassNotFoundException e) {
                missingPackages.add(packageName);
            }
        }
        
        if (!missingPackages.isEmpty() || !outdatedPackages.isEmpty()) {
            StringBuilder warningMsg = new StringBuilder("Dependências do módulo de aprendizado:\n");
            if (!missingPackages.isEmpty()) {
                warningMsg.append(String.format("  Faltando: %s\n", String.join(", ", missingPackages)));
            }
            if (!outdatedPackages.isEmpty()) {
                warningMsg.append(String.format("  Desatualizadas: %s\n", String.join(", ", outdatedPackages)));
            }
            warningMsg.append("  Instale com: pip install numpy scikit-learn pandas deap");
            logger.warning(warningMsg.toString());
            return false;
        }
        
        return true;
    }
    
    private static int compararVersoes(String v1, String v2) {
        /** Compara duas versões (simplificado) */
        
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        
        int maxLength = Math.max(parts1.length, parts2.length);
        
        for (int i = 0; i < maxLength; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            
            if (num1 != num2) {
                return num1 - num2;
            }
        }
        
        return 0;
    }
    
    private static void verificarDisponibilidadeModulos() {
        /** Verifica disponibilidade dos módulos de aprendizado */
        
        try {
            // Simulação de verificação de disponibilidade
            // Em Java, verificaríamos se as classes estão disponíveis
            Class.forName("modulo_emocional.Emocional.Continuo");
            aprendizadoContinuoAvailable = true;
        } catch (ClassNotFoundException e) {
            logger.warning("Falha ao importar AprendizadoContinuo: " + e.getMessage());
            aprendizadoContinuoAvailable = false;
        }
        
        try {
            // Simulação de verificação de disponibilidade
            Class.forName("modulo_emocional.Emocional.Evolucional");
            aprendizadoEvolucionalAvailable = true;
        } catch (ClassNotFoundException e) {
            logger.warning("Falha ao importar AprendizadoEvolucional: " + e.getMessage());
            aprendizadoEvolucionalAvailable = false;
        }
    }
    
    /**
     * Fábrica para criação de sistemas de aprendizado
     */
    public static class SistemaAprendizadoFactory {
        
        public static SistemaAprendizado criarSistema(String tipo, Map<String, Object> config) {
            /**
             * Cria um sistema de aprendizado baseado no tipo especificado
             * 
             * @param tipo 'continuo' ou 'evolucional'
             * @param config Configurações opcionais para o sistema
             * @return Instância do sistema de aprendizado
             * @throws IllegalArgumentException Se o tipo for inválido
             * @throws IllegalStateException Se o módulo necessário não estiver disponível
             */
            
            if (config == null) {
                config = new HashMap<>();
            }
            
            switch (tipo.toLowerCase()) {
                case ModosAprendizado.CONTINUO:
                    if (!aprendizadoContinuoAvailable) {
                        throw new IllegalStateException("Módulo AprendizadoContinuo não disponível");
                    }
                    return new SistemaAprendizado("AprendizadoContinuo", ModosAprendizado.CONTINUO, config);
                
                case ModosAprendizado.EVOLUCIONAL:
                    if (!aprendizadoEvolucionalAvailable) {
                        throw new IllegalStateException("Módulo AprendizadoEvolucional não disponível");
                    }
                    return new SistemaAprendizado("AprendizadoEvolucional", ModosAprendizado.EVOLUCIONAL, config);
                
                default:
                    throw new IllegalArgumentException(
                        String.format("Tipo de sistema desconhecido: %s. Use 'continuo' ou 'evolucional'", tipo)
                    );
            }
        }
        
        public static SistemaAprendizadoHibrido criarSistemaHibrido(
                Map<String, Object> configContinuo, 
                Map<String, Object> configEvolucional) {
            /**
             * Cria um sistema híbrido que combina aprendizado contínuo e evolucional
             * 
             * @param configContinuo Configuração para aprendizado contínuo
             * @param configEvolucional Configuração para aprendizado evolucional
             * @return Sistema híbrido de aprendizado
             */
            
            SistemaAprendizado continuo = criarSistema(ModosAprendizado.CONTINUO, configContinuo);
            SistemaAprendizado evolucional = criarSistema(ModosAprendizado.EVOLUCIONAL, configEvolucional);
            
            return new SistemaAprendizadoHibrido(continuo, evolucional);
        }
    }
    
    /**
     * Sistema híbrido que combina aprendizado contínuo e evolucional
     */
    public static class SistemaAprendizadoHibrido {
        
        private final SistemaAprendizado continuo;
        private final SistemaAprendizado evolucional;
        private final Logger logger;
        
        public SistemaAprendizadoHibrido(SistemaAprendizado continuo, SistemaAprendizado evolucional) {
            this.continuo = continuo;
            this.evolucional = evolucional;
            this.logger = Logger.getLogger(Aprendizado.class.getName() + ".Hibrido");
        }
        
        public ResultadoProcessamento processar(Object dados, String modo) {
            /**
             * Processa dados usando os sistemas de aprendizado
             * 
             * @param dados Dados a serem processados
             * @param modo 'continuo', 'evolucional' ou 'ambos'
             * @return Resultados do processamento
             */
            
            long inicio = System.currentTimeMillis();
            Map<String, Object> resultados = new HashMap<>();
            
            if (ModosAprendizado.CONTINUO.equals(modo) || "ambos".equals(modo)) {
                // Simulação de processamento contínuo
                resultados.put("continuo", Map.of(
                    "status", "processado",
                    "timestamp", LocalDateTime.now(),
                    "resultado", "aprendizado_realizado"
                ));
            }
            
            if (ModosAprendizado.EVOLUCIONAL.equals(modo) || "ambos".equals(modo)) {
                // Simulação de processamento evolucional
                resultados.put("evolucional", Map.of(
                    "status", "evoluido",
                    "timestamp", LocalDateTime.now(),
                    "resultado", "otimizacao_aplicada"
                ));
            }
            
            long fim = System.currentTimeMillis();
            double tempoExecucao = (fim - inicio) / 1000.0;
            
            return new ResultadoProcessamento(resultados, modo, tempoExecucao);
        }
        
        public SistemaAprendizado getContinuo() {
            return continuo;
        }
        
        public SistemaAprendizado getEvolucional() {
            return evolucional;
        }
    }
    
    /**
     * Verifica a configuração do módulo de aprendizado
     * 
     * @return Dicionário com status do módulo
     */
    public static StatusModulo verificarConfiguracao() {
        StatusModulo status = new StatusModulo(VERSION, dependenciesOk);
        
        status.modulosDisponiveis.put("AprendizadoContinuo", aprendizadoContinuoAvailable);
        status.modulosDisponiveis.put("AprendizadoEvolucional", aprendizadoEvolucionalAvailable);
        
        status.pacotesRequeridos.putAll(REQUIRED_PACKAGES);
        
        status.caminhoModulo = System.getProperty("user.dir");
        
        if (aprendizadoContinuoAvailable) {
            status.versoesModulos.put("AprendizadoContinuo", "2.0.0");
        }
        
        if (aprendizadoEvolucionalAvailable) {
            status.versoesModulos.put("AprendizadoEvolucional", "2.0.0");
        }
        
        return status;
    }
    
    /**
     * Inicializa todos os sistemas de aprendizado disponíveis
     * 
     * @param config Configuração para os sistemas
     * @return Dicionário com sistemas inicializados
     */
    public static Map<String, Object> inicializarSistemaAprendizado(Map<String, Object> config) {
        if (config == null) {
            config = new HashMap<>();
        }
        
        Map<String, Object> sistemas = new HashMap<>();
        
        if (aprendizadoContinuoAvailable) {
            @SuppressWarnings("unchecked")
            Map<String, Object> configContinuo = (Map<String, Object>) config.getOrDefault("continuo", new HashMap<>());
            sistemas.put("continuo", SistemaAprendizadoFactory.criarSistema(ModosAprendizado.CONTINUO, configContinuo));
            logger.info("Sistema de Aprendizado Contínuo inicializado");
        }
        
        if (aprendizadoEvolucionalAvailable) {
            @SuppressWarnings("unchecked")
            Map<String, Object> configEvolucional = (Map<String, Object>) config.getOrDefault("evolucional", new HashMap<>());
            sistemas.put("evolucional", SistemaAprendizadoFactory.criarSistema(ModosAprendizado.EVOLUCIONAL, configEvolucional));
            logger.info("Sistema de Aprendizado Evolucional inicializado");
        }
        
        // Criar sistema híbrido se ambos estiverem disponíveis
        if (sistemas.size() == 2) {
            @SuppressWarnings("unchecked")
            Map<String, Object> configContinuo = (Map<String, Object>) config.getOrDefault("continuo", new HashMap<>());
            @SuppressWarnings("unchecked")
            Map<String, Object> configEvolucional = (Map<String, Object>) config.getOrDefault("evolucional", new HashMap<>());
            
            sistemas.put("hibrido", SistemaAprendizadoFactory.criarSistemaHibrido(configContinuo, configEvolucional));
            logger.info("Sistema de Aprendizado Híbrido inicializado");
        }
        
        return sistemas;
    }
    
    /**
     * Decorador para monitorar desempenho de funções de aprendizado
     */
    public interface FuncaoMonitorada<T, R> {
        R aplicar(T dados);
    }
    
    public static <T, R> FuncaoMonitorada<T, R> monitorarDesempenho(FuncaoMonitorada<T, R> funcao) {
        return dados -> {
            long inicio = System.currentTimeMillis();
            R resultado = funcao.aplicar(dados);
            long fim = System.currentTimeMillis();
            double duracao = (fim - inicio) / 1000.0;
            
            // Log de desempenho
            logger.debug(String.format("Função executada em %.4f segundos", duracao));
            
            return resultado;
        };
    }
    
    /**
     * Validador de dados de aprendizado
     */
    public static class ValidadorDados {
        
        public static void validar(Object dados) {
            /** Valida dados de entrada para funções de aprendizado */
            
            if (dados == null) {
                throw new IllegalArgumentException("Dados não podem ser null");
            }
            
            if (dados instanceof Collection) {
                Collection<?> colecao = (Collection<?>) dados;
                if (colecao.isEmpty()) {
                    throw new IllegalArgumentException("Dados não podem estar vazios");
                }
            }
            
            if (dados instanceof Map) {
                Map<?, ?> mapa = (Map<?, ?>) dados;
                if (mapa.isEmpty()) {
                    throw new IllegalArgumentException("Dados não podem estar vazios");
                }
            }
            
            if (dados.getClass().isArray()) {
                if (java.lang.reflect.Array.getLength(dados) == 0) {
                    throw new IllegalArgumentException("Dados não podem estar vazios");
                }
            }
        }
    }
    
    /**
     * Obtém a configuração atual do módulo
     */
    public static ConfiguracaoModulo getConfiguracao() {
        return configuracao;
    }
    
    /**
     * Verifica se as dependências estão OK
     */
    public static boolean isDependenciesOk() {
        return dependenciesOk;
    }
    
    /**
     * Verifica se o aprendizado contínuo está disponível
     */
    public static boolean isAprendizadoContinuoAvailable() {
        return aprendizadoContinuoAvailable;
    }
    
    /**
     * Verifica se o aprendizado evolucional está disponível
     */
    public static boolean isAprendizadoEvolucionalAvailable() {
        return aprendizadoEvolucionalAvailable;
    }
    
    /**
     * Obtém os pacotes requeridos
     */
    public static Map<String, String> getRequiredPackages() {
        return new HashMap<>(REQUIRED_PACKAGES);
    }
    
    /**
     * Método utilitário para demonstração
     */
    public static void demonstracao() {
        System.out.println("🧠 DEMONSTRAÇÃO DO MÓDULO DE APRENDIZADO AVANÇADO");
        System.out.println("=".repeat(60));
        System.out.printf("Versão: %s%n", VERSION);
        System.out.printf("Autor: %s%n", AUTHOR);
        System.out.printf("Licença: %s%n", LICENSE);
        
        // 1. Verificar configuração
        System.out.println("\n📋 Status da Configuração:");
        StatusModulo status = verificarConfiguracao();
        System.out.println("   " + status.toString());
        
        System.out.println("\n   Módulos Disponíveis:");
        for (Map.Entry<String, Boolean> entry : status.modulosDisponiveis.entrySet()) {
            System.out.printf("      • %s: %s%n", entry.getKey(), entry.getValue() ? "✓" : "✗");
        }
        
        System.out.println("\n   Pacotes Requeridos:");
        for (Map.Entry<String, String> entry : status.pacotesRequeridos.entrySet()) {
            System.out.printf("      • %s: %s%n", entry.getKey(), entry.getValue());
        }
        
        // 2. Inicializar sistemas
        System.out.println("\n🚀 Inicialização de Sistemas:");
        
        Map<String, Object> config = new HashMap<>();
        config.put("continuo", Map.of("taxa_aprendizado", 0.01, "epocas", 100));
        config.put("evolucional", Map.of("populacao", 50, "geracoes", 10));
        
        Map<String, Object> sistemas = inicializarSistemaAprendizado(config);
        
        System.out.println("   Sistemas inicializados:");
        for (Map.Entry<String, Object> entry : sistemas.entrySet()) {
            System.out.printf("      • %s: %s%n", entry.getKey(), entry.getValue().getClass().getSimpleName());
        }
        
        // 3. Usar fábrica
        System.out.println("\n🏭 Uso da Fábrica:");
        
        try {
            SistemaAprendizado sistemaContinuo = SistemaAprendizadoFactory.criarSistema("continuo", null);
            System.out.println("   " + sistemaContinuo.toString());
            
            SistemaAprendizado sistemaEvolucional = SistemaAprendizadoFactory.criarSistema("evolucional", null);
            System.out.println("   " + sistemaEvolucional.toString());
            
        } catch (Exception e) {
            System.out.println("   Erro ao criar sistemas: " + e.getMessage());
        }
        
        // 4. Sistema híbrido
        System.out.println("\n🔄 Sistema Híbrido:");
        
        if (sist.containsKey("hibrido")) {
            SistemaAprendizadoHibrido hibrido = (SistemaAprendizadoHibrido) sistemas.get("hibrido");
            
            // Simular processamento
            Object dados = Map.of("tipo", "treinamento", "amostras", 1000);
            ResultadoProcessamento resultado = hibrido.processar(dados, "ambos");
            
            System.out.println("   " + resultado.toString());
            System.out.println("   Resultados:");
            for (Map.Entry<String, Object> entry : resultado.resultados.entrySet()) {
                System.out.printf("      • %s: %s%n", entry.getKey(), entry.getValue());
            }
        }
        
        // 5. Validador de dados
        System.out.println("\n✅ Validação de Dados:");
        
        try {
            ValidadorDados.validar(Map.of("dados", Arrays.asList(1, 2, 3)));
            System.out.println("   ✓ Dados válidos");
            
            ValidadorDados.validar(Arrays.asList("item1", "item2", "item3"));
            System.out.println("   ✓ Lista válida");
            
            ValidadorDados.validar("texto");
            System.out.println("   ✓ Texto válido");
            
        } catch (Exception e) {
            System.out.println("   ✗ Erro de validação: " + e.getMessage());
        }
        
        // 6. Monitor de desempenho
        System.out.println("\n⏱️ Monitor de Desempenho:");
        
        FuncaoMonitorada<String, Integer> funcao = dados -> {
            // Simulação de processamento
            Thread.sleep(100); // 100ms
            return dados.length();
        };
        
        FuncaoMonitorada<String, Integer> funcaoMonitorada = monitorarDesempenho(funcao);
        Integer resultado = funcaoMonitorada.aplicar("dados de teste");
        
        System.out.printf("   Resultado: %d%n", resultado);
        
        // 7. Constantes
        System.out.println("\n📊 Constantes do Módulo:");
        
        System.out.println("   Modos de Aprendizado:");
        System.out.printf("      • CONTINUO: %s%n", ModosAprendizado.CONTINUO);
        System.out.printf("      • EVOLUCIONAL: %s%n", ModosAprendizado.EVOLUCIONAL);
        System.out.printf("      • HIBRIDO: %s%n", ModosAprendizado.HIBRIDO);
        System.out.printf("      • BATCH: %s%n", ModosAprendizado.BATCH);
        System.out.printf("      • ONLINE: %s%n", ModosAprendizado.ONLINE);
        System.out.printf("      • REFORCO: %s%n", ModosAprendizado.REFORCO);
        
        System.out.println("\n   Métricas de Avaliação:");
        System.out.printf("      • ACURACIA: %s%n", MetricasAvaliacao.ACURACIA);
        System.out.printf("      • PRECISAO: %s%n", MetricasAvaliacao.PRECISAO);
        System.out.printf("      • RECALL: %s%n", MetricasAvaliacao.RECALL);
        System.out.printf("      • F1_SCORE: %s%n", MetricasAvaliacao.F1_SCORE);
        System.out.printf("      • LOSS: %s%n", MetricasAvaliacao.LOSS);
        System.out.printf("      • ENTROPIA: %s%n", MetricasAvaliacao.ENTROPIA);
        System.out.printf("      • DIVERSIDADE: %s%n", MetricasAvaliacao.DIVERSIDADE);
        System.out.printf("      • ADAPTABILIDADE: %s%n", MetricasAvaliacao.ADAPTABILIDADE);
        
        System.out.println("\n✅ Demonstração concluída!");
    }
    
    /**
     * Método principal para execução
     */
    public static void main(String[] args) {
        demonstracao();
    }
}
