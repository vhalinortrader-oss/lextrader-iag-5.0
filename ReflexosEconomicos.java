package modulo_emocional.Motor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Sistema de reflexos automáticos baseados em notícias econômicas
 */
public class ReflexosEconomicos {
    
    private double limiarAtivacao;
    private Map<String, Map<String, Object>> reflexosConfigurados;
    private List<Map<String, Object>> historicoAtivacoes;
    private List<String> categoriasNoticias;
    
    public ReflexosEconomicos(double limiarAtivacao) {
        this.limiarAtivacao = limiarAtivacao;
        this.reflexosConfigurados = new HashMap<>();
        this.historicoAtivacoes = new ArrayList<>();
        this.categoriasNoticias = Arrays.asList(
            "indicadores_macroeconomicos",
            "politica_monetaria",
            "mercado_financeiro",
            "comercio_exterior",
            "emprego_renda",
            "inflacao",
            "setor_produtivo"
        );
        
        inicializarReflexosPadrao();
    }
    
    public ReflexosEconomicos() {
        this(0.6); // Limiar padrão
    }
    
    /**
     * Processa uma notícia econômica e ativa reflexos correspondentes
     */
    public List<Map<String, Object>> processarNoticia(Map<String, Object> noticia) {
        if (noticia == null || noticia.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Map<String, Object>> reflexosAtivados = new ArrayList<>();
        
        // Analisa impacto da notícia
        double impacto = analisarImpacto(noticia);
        String categoria = noticia.containsKey("categoria") ? (String) noticia.get("categoria") : "geral";
        
        // Verifica reflexos configurados para esta categoria
        for (Map.Entry<String, Map<String, Object>> entry : reflexosConfigurados.entrySet()) {
            String nomeReflexo = entry.getKey();
            @SuppressWarnings("unchecked")
            Map<String, Object> config = entry.getValue();
            
            @SuppressWarnings("unchecked")
            List<String> categorias = (List<String>) config.get("categorias");
            
            if (categorias.contains(categoria)) {
                double limiarEspecifico = config.containsKey("limiar_especifico") ? 
                    (Double) config.get("limiar_especifico") : limiarAtivacao;
                
                if (impacto >= limiarEspecifico) {
                    // Ativa reflexo
                    Map<String, Object> resposta = ativarReflexo(nomeReflexo, noticia, impacto);
                    reflexosAtivados.add(resposta);
                }
            }
        }
        
        // Registra ativação
        if (!reflexosAtivados.isEmpty()) {
            Map<String, Object> registro = new HashMap<>();
            registro.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            registro.put("noticia", noticia.containsKey("titulo") ? noticia.get("titulo") : "sem_titulo");
            registro.put("impacto", impacto);
            
            List<String> nomesReflexos = new ArrayList<>();
            for (Map<String, Object> reflexo : reflexosAtivados) {
                nomesReflexos.add((String) reflexo.get("nome"));
            }
            registro.put("reflexos_ativados", nomesReflexos);
            
            historicoAtivacoes.add(registro);
        }
        
        return reflexosAtivados;
    }
    
    /**
     * Configura um novo reflexo automático
     */
    public Map<String, Object> configurarReflexo(String nome, List<String> categorias, String acao, Double limiar) {
        Map<String, Object> config = new HashMap<>();
        config.put("categorias", categorias);
        config.put("acao", acao);
        config.put("limiar_especifico", limiar != null ? limiar : limiarAtivacao);
        config.put("ativo", true);
        
        reflexosConfigurados.put(nome, config);
        return config;
    }
    
    /**
     * Inicializa reflexos econômicos padrão
     */
    private void inicializarReflexosPadrao() {
        Map<String, Map<String, Object>> reflexosPadrao = new HashMap<>();
        
        // Resposta inflação
        Map<String, Object> respostaInflacao = new HashMap<>();
        respostaInflacao.put("categorias", Arrays.asList("inflacao", "politica_monetaria"));
        respostaInflacao.put("acao", "ajustar_expectativas_inflacionarias");
        respostaInflacao.put("descricao", "Ajusta projeções baseado em dados de inflação");
        reflexosPadrao.put("resposta_inflacao", respostaInflacao);
        
        // Reação indicadores
        Map<String, Object> reacaoIndicadores = new HashMap<>();
        reacaoIndicadores.put("categorias", Arrays.asList("indicadores_macroeconomicos", "emprego_renda"));
        reacaoIndicadores.put("acao", "recalcular_previsoes_crescimento");
        reacaoIndicadores.put("descricao", "Recalcula previsões com novos indicadores");
        reflexosPadrao.put("reacao_indicadores", reacaoIndicadores);
        
        // Defesa cambial
        Map<String, Object> defesaCambial = new HashMap<>();
        defesaCambial.put("categorias", Arrays.asList("comercio_exterior", "mercado_financeiro"));
        defesaCambial.put("acao", "proteger_ativos_cambio");
        defesaCambial.put("descricao", "Ativa proteções para volatilidade cambial");
        reflexosPadrao.put("defesa_cambial", defesaCambial);
        
        // Adaptação juros
        Map<String, Object> adaptacaoJuros = new HashMap<>();
        adaptacaoJuros.put("categorias", Arrays.asList("politica_monetaria", "mercado_financeiro"));
        adaptacaoJuros.put("acao", "revisar_alocacao_ativos");
        adaptacaoJuros.put("descricao", "Revisa alocação baseado em política de juros");
        reflexosPadrao.put("adaptacao_juros", adaptacaoJuros);
        
        // Configurar todos os reflexos padrão
        for (Map.Entry<String, Map<String, Object>> entry : reflexosPadrao.entrySet()) {
            String nome = entry.getKey();
            @SuppressWarnings("unchecked")
            Map<String, Object> config = entry.getValue();
            
            configurarReflexo(
                nome,
                (List<String>) config.get("categorias"),
                (String) config.get("acao"),
                0.5 // Limiar mais baixo para reflexos padrão
            );
        }
    }
    
    /**
     * Analisa o impacto potencial de uma notícia (0 a 1)
     */
    private double analisarImpacto(Map<String, Object> noticia) {
        double impacto = 0.5; // Base
        
        // Fatores que aumentam impacto
        Map<String, Double> fatores = new HashMap<>();
        fatores.put("urgente", 0.3);
        fatores.put("dados_oficiais", 0.2);
        fatores.put("mudanca_politica", 0.4);
        fatores.put("crise", 0.5);
        fatores.put("surpresa_mercado", 0.3);
        
        String titulo = noticia.containsKey("titulo") ? ((String) noticia.get("titulo")).toLowerCase() : "";
        String conteudo = noticia.containsKey("conteudo") ? ((String) noticia.get("conteudo")).toLowerCase() : "";
        
        for (Map.Entry<String, Double> entry : fatores.entrySet()) {
            String fator = entry.getKey();
            Double valor = entry.getValue();
            
            if (titulo.contains(fator) || conteudo.contains(fator)) {
                impacto += valor;
            }
        }
        
        // Limita entre 0 e 1
        return Math.min(1.0, Math.max(0.0, impacto));
    }
    
    /**
     * Ativa um reflexo específico
     */
    private Map<String, Object> ativarReflexo(String nomeReflexo, Map<String, Object> noticia, double impacto) {
        Map<String, Object> config = reflexosConfigurados.get(nomeReflexo);
        
        if (config == null || !(Boolean) config.get("ativo")) {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("nome", nomeReflexo);
            resultado.put("status", "inativo");
            return resultado;
        }
        
        // Simula execução da ação
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("nome", nomeReflexo);
        resposta.put("status", "ativado");
        resposta.put("intensidade", impacto);
        resposta.put("acao_executada", config.get("acao"));
        resposta.put("noticia_trigger", noticia.containsKey("titulo") ? noticia.get("titulo") : "sem_titulo");
        resposta.put("categoria", noticia.containsKey("categoria") ? noticia.get("categoria") : "geral");
        resposta.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // Adiciona resultado simulado
        String acao = (String) config.get("acao");
        if (acao.contains("ajustar")) {
            resposta.put("resultado", String.format("Ajuste aplicado com fator %.2f", impacto));
        } else if (acao.contains("recalcular")) {
            resposta.put("resultado", "Previsões atualizadas");
        } else if (acao.contains("proteger")) {
            resposta.put("resultado", "Mecanismos de defesa ativados");
        }
        
        return resposta;
    }
    
    // Getters e Setters
    public double getLimiarAtivacao() {
        return limiarAtivacao;
    }
    
    public void setLimiarAtivacao(double limiarAtivacao) {
        this.limiarAtivacao = limiarAtivacao;
    }
    
    public Map<String, Map<String, Object>> getReflexosConfigurados() {
        return new HashMap<>(reflexosConfigurados);
    }
    
    public List<Map<String, Object>> getHistoricoAtivacoes() {
        return new ArrayList<>(historicoAtivacoes);
    }
    
    public List<String> getCategoriasNoticias() {
        return new ArrayList<>(categoriasNoticias);
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Reflexos Econômicos ===");
        
        ReflexosEconomicos reflexos = new ReflexosEconomicos(0.6);
        
        // Notícia de teste
        Map<String, Object> noticia = new HashMap<>();
        noticia.put("titulo", "BC anuncia aumento da taxa básica de juros");
        noticia.put("categoria", "politica_monetaria");
        noticia.put("conteudo", "Banco Central elevou a Selic em 0,5 pontos percentuais");
        noticia.put("urgente", true);
        
        // Processar notícia
        System.out.println("Processando notícia econômica...");
        List<Map<String, Object>> reflexosAtivados = reflexos.processarNoticia(noticia);
        
        System.out.println("Reflexos ativados: " + reflexosAtivados.size());
        for (Map<String, Object> reflexo : reflexosAtivados) {
            System.out.println("  " + reflexo.get("nome") + ": " + reflexo.get("acao_executada"));
            System.out.println("    Intensidade: " + String.format("%.2f", (Double) reflexo.get("intensidade")));
            System.out.println("    Resultado: " + reflexo.get("resultado"));
        }
        
        // Estatísticas
        System.out.println("\nEstatísticas:");
        System.out.println("  Reflexos configurados: " + reflexos.getReflexosConfigurados().size());
        System.out.println("  Categorias monitoradas: " + reflexos.getCategoriasNoticias().size());
        System.out.println("  Histórico de ativações: " + reflexos.getHistoricoAtivacoes().size());
    }
}
