package modulo_emocional.Motor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Processador central que integra todos os subsistemas do módulo motor
 */
public class ProcessadorCentral {
    
    private ConfigMotor config;
    private MovimentosVoluntarios coordenador;
    private PadroesRepetitivos geradorPadroes;
    private Postura controlePostura;
    private PredicoesConstancia sistemaPredicoes;
    private ReflexosEconomicos reflexos;
    private String estadoSistema;
    private List<Map<String, Object>> logOperacoes;
    
    public ProcessadorCentral() {
        this.config = new ConfigMotor();
        
        // Inicializa subsistemas
        this.coordenador = new MovimentosVoluntarios();
        this.geradorPadroes = new PadroesRepetitivos();
        this.controlePostura = new Postura(config.getSensibilidade());
        this.sistemaPredicoes = new PredicoesConstancia(config.getTaxaAprendizado());
        this.reflexos = new ReflexosEconomicos(config.getLimiarReflexo());
        
        this.estadoSistema = "inicializado";
        this.logOperacoes = new ArrayList<>();
    }
    
    /**
     * Executa um ciclo completo de processamento motor
     */
    public Map<String, Object> executarCiclo(Map<String, Object> dadosEntrada) {
        Map<String, Object> resultados = new HashMap<>();
        resultados.put("ciclo_id", logOperacoes.size() + 1);
        resultados.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        resultados.put("subsistemas", new HashMap<>());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> subsistemas = (Map<String, Object>) resultados.get("subsistemas");
        
        // 1. Processar reflexos automáticos (prioritário)
        if (dadosEntrada.containsKey("noticias")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> noticias = (List<Map<String, Object>>) dadosEntrada.get("noticias");
            List<Map<String, Object>> reflexosAtivos = new ArrayList<>();
            
            for (Map<String, Object> noticia : noticias) {
                List<Map<String, Object>> reflexosNoticia = reflexos.processarNoticia(noticia);
                reflexosAtivos.addAll(reflexosNoticia);
            }
            
            Map<String, Object> resultadoReflexos = new HashMap<>();
            resultadoReflexos.put("quantidade", reflexosAtivos.size());
            resultadoReflexos.put("reflexos", reflexosAtivos);
            subsistemas.put("reflexos", resultadoReflexos);
        }
        
        // 2. Avaliar equilíbrio e postura
        if (dadosEntrada.containsKey("dados_sensoriais")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> dadosSensoriais = (Map<String, Object>) dadosEntrada.get("dados_sensoriais");
            
            Map<String, Object> diagnostico = controlePostura.avaliarEquilibrio(dadosSensoriais);
            List<Map<String, Object>> correcoes = controlePostura.aplicarCorrecao(diagnostico);
            
            Map<String, Object> resultadoEquilibrio = new HashMap<>();
            resultadoEquilibrio.put("diagnostico", diagnostico);
            resultadoEquilibrio.put("correcoes", correcoes);
            subsistemas.put("equilibrio", resultadoEquilibrio);
        }
        
        // 3. Processar movimentos voluntários
        if (dadosEntrada.containsKey("objetivos")) {
            @SuppressWarnings("unchecked")
            List<String> objetivos = (List<String>) dadosEntrada.get("objetivos");
            List<Map<String, Object>> movimentos = new ArrayList<>();
            
            for (String objetivo : objetivos) {
                Map<String, Object> plano = coordenador.planejarMovimento(objetivo, null);
                Map<String, Object> resultado = coordenador.executarMovimento(plano);
                movimentos.add(resultado);
            }
            
            Map<String, Object> resultadoMovimentos = new HashMap<>();
            resultadoMovimentos.put("quantidade", movimentos.size());
            resultadoMovimentos.put("movimentos", movimentos);
            subsistemas.put("movimentos", resultadoMovimentos);
        }
        
        // 4. Atualizar predições e constância
        if (dadosEntrada.containsKey("contexto_atual")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> contextoAtual = (Map<String, Object>) dadosEntrada.get("contexto_atual");
            
            Map<String, Object> predicoes = sistemaPredicoes.preverPerturbacao(contextoAtual);
            
            Map<String, Object> constancia;
            if (dadosEntrada.containsKey("estado_desejado")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> estadoDesejado = (Map<String, Object>) dadosEntrada.get("estado_desejado");
                @SuppressWarnings("unchecked")
                Map<String, Object> estadoAtual = (Map<String, Object>) dadosEntrada.getOrDefault("estado_atual", new HashMap<>());
                
                constancia = sistemaPredicoes.manterConstancia(estadoAtual, estadoDesejado);
            } else {
                constancia = new HashMap<>();
                constancia.put("status", "nao_aplicavel");
            }
            
            Map<String, Object> resultadoPredicoes = new HashMap<>();
            resultadoPredicoes.put("perturbacoes_previstas", predicoes);
            resultadoPredicoes.put("manutencao_constancia", constancia);
            subsistemas.put("predicoes", resultadoPredicoes);
        }
        
        // 5. Identificar e otimizar padrões
        if (dadosEntrada.containsKey("sequencias")) {
            @SuppressWarnings("unchecked")
            List<List<String>> sequencias = (List<List<String>>) dadosEntrada.get("sequencias");
            List<Map<String, Object>> padroes = new ArrayList<>();
            
            for (List<String> sequencia : sequencias) {
                Map<String, Object> padrao = geradorPadroes.identificarPadrao(sequencia);
                if (padrao != null) {
                    padroes.add(padrao);
                }
            }
            
            Map<String, Object> resultadoPadroes = new HashMap<>();
            resultadoPadroes.put("identificados", padroes.size());
            resultadoPadroes.put("padroes", padroes);
            subsistemas.put("padroes", resultadoPadroes);
        }
        
        // Registra ciclo no log
        logOperacoes.add(resultados);
        estadoSistema = "operacional";
        
        return resultados;
    }
    
    /**
     * Gera relatório completo do estado do sistema
     */
    public Map<String, Object> obterRelatorio() {
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("estado_geral", estadoSistema);
        
        Map<String, Object> estatisticas = new HashMap<>();
        estatisticas.put("ciclos_executados", logOperacoes.size());
        estatisticas.put("reflexos_ativos", reflexos.getReflexosConfigurados().size());
        estatisticas.put("padroes_aprendidos", geradorPadroes.getPadroesAprendidos().size());
        estatisticas.put("contextos_historico", sistemaPredicoes.getHistoricoContextos().size());
        relatorio.put("estatisticas", estatisticas);
        
        Map<String, Object> subsistemas = new HashMap<>();
        
        // Coordenação
        Map<String, Object> coords = new HashMap<>();
        coords.put("movimentos_ativos", coordenador.getMovimentosAtivos().size());
        coords.put("historico", coordenador.getHistoricoMovimentos().size());
        subsistemas.put("coordenacao", coords);
        
        // Equilíbrio
        Map<String, Object> equil = new HashMap<>();
        equil.put("estado_postural", controlePostura.getEstadoPostural());
        equil.put("ajustes_ativos", controlePostura.getAjustesAtivos().size());
        subsistemas.put("equilibrio", equil);
        
        // Predições
        Map<String, Object> pred = new HashMap<>();
        pred.put("modelo_tamanho", sistemaPredicoes.getModeloPredicoes().size());
        pred.put("constancia_alvo", sistemaPredicoes.getConstanciaAlvo());
        subsistemas.put("predicoes", pred);
        
        // Reflexos
        Map<String, Object> refl = new HashMap<>();
        refl.put("categorias_monitoradas", reflexos.getCategoriasNoticias().size());
        refl.put("historico_ativacoes", reflexos.getHistoricoAtivacoes().size());
        subsistemas.put("reflexos", refl);
        
        relatorio.put("subsistemas", subsistemas);
        
        // Configurações
        Map<String, Object> configs = new HashMap<>();
        configs.put("sensibilidade", config.getSensibilidade());
        configs.put("limiar_reflexo", config.getLimiarReflexo());
        configs.put("taxa_aprendizado", config.getTaxaAprendizado());
        relatorio.put("configuracoes", configs);
        
        return relatorio;
    }
    
    /**
     * Reconfigura o sistema dinamicamente
     */
    public Map<String, Object> reconfigurar(Map<String, Object> novasConfigs) {
        if (novasConfigs.containsKey("sensibilidade")) {
            double novaSensibilidade = (Double) novasConfigs.get("sensibilidade");
            config.setSensibilidade(novaSensibilidade);
            controlePostura.setSensibilidade(novaSensibilidade);
        }
        
        if (novasConfigs.containsKey("limiar_reflexo")) {
            double novoLimiar = (Double) novasConfigs.get("limiar_reflexo");
            config.setLimiarReflexo(Math.max(0.1, Math.min(1.0, novoLimiar)));
            reflexos.setLimiarAtivacao(config.getLimiarReflexo());
        }
        
        if (novasConfigs.containsKey("taxa_aprendizado")) {
            double novaTaxa = (Double) novasConfigs.get("taxa_aprendizado");
            config.setTaxaAprendizado(Math.max(0.01, Math.min(0.5, novaTaxa)));
            sistemaPredicoes.setTaxaAprendizado(config.getTaxaAprendizado());
        }
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("sensibilidade", config.getSensibilidade());
        resultado.put("limiar_reflexo", config.getLimiarReflexo());
        resultado.put("taxa_aprendizado", config.getTaxaAprendizado());
        
        return resultado;
    }
    
    // Getters
    public String getEstadoSistema() {
        return estadoSistema;
    }
    
    public List<Map<String, Object>> getLogOperacoes() {
        return new ArrayList<>(logOperacoes);
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Processador Central Motor ===");
        
        ProcessadorCentral processador = new ProcessadorCentral();
        
        // Dados de entrada simulados
        Map<String, Object> dadosEntrada = new HashMap<>();
        
        // Notícias econômicas
        List<Map<String, Object>> noticias = new ArrayList<>();
        Map<String, Object> noticia = new HashMap<>();
        noticia.put("titulo", "BC anuncia aumento da taxa básica de juros");
        noticia.put("categoria", "politica_monetaria");
        noticia.put("conteudo", "Banco Central elevou a Selic em 0,5 pontos percentuais");
        noticia.put("urgente", true);
        noticias.add(noticia);
        dadosEntrada.put("noticias", noticias);
        
        // Dados sensoriais
        Map<String, Object> dadosSensoriais = new HashMap<>();
        dadosSensoriais.put("aceleracao", 0.1);
        dadosSensoriais.put("inclinacao", 0.15);
        dadosSensoriais.put("rotacao", 0.2);
        dadosEntrada.put("dados_sensoriais", dadosSensoriais);
        
        // Objetivos
        List<String> objetivos = Arrays.asList("estabilizar_carteira_investimentos", "revisar_projecoes");
        dadosEntrada.put("objetivos", objetivos);
        
        // Contexto atual
        Map<String, Object> contextoAtual = new HashMap<>();
        contextoAtual.put("mercado", "volatil");
        contextoAtual.put("juros", "alta");
        contextoAtual.put("inflacao", "controlada");
        dadosEntrada.put("contexto_atual", contextoAtual);
        
        // Estado desejado
        Map<String, Object> estadoDesejado = new HashMap<>();
        estadoDesejado.put("estabilidade", 0.9);
        estadoDesejado.put("liquidez", 0.7);
        estadoDesejado.put("retorno", 0.6);
        dadosEntrada.put("estado_desejado", estadoDesejado);
        
        // Sequências
        List<List<String>> sequencias = new ArrayList<>();
        sequencias.add(Arrays.asList("analise", "decisao", "execucao", "avaliacao"));
        sequencias.add(Arrays.asList("coleta", "processamento", "decisao"));
        dadosEntrada.put("sequencias", sequencias);
        
        // Executar ciclo
        System.out.println("Executando ciclo de processamento...");
        Map<String, Object> resultado = processador.executarCiclo(dadosEntrada);
        System.out.println("Ciclo ID: " + resultado.get("ciclo_id"));
        
        // Obter relatório
        System.out.println("\nGerando relatório...");
        Map<String, Object> relatorio = processador.obterRelatorio();
        System.out.println("Estado geral: " + relatorio.get("estado_geral"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> estatisticas = (Map<String, Object>) relatorio.get("estatisticas");
        System.out.println("Estatísticas:");
        for (Map.Entry<String, Object> entry : estatisticas.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
