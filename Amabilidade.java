package modulo_emocional.Emocional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sistema de Amabilidade e Comportamento Social Positivo
 * ==================================================
 * Versão Java do sistema de gerenciamento de comportamentos
 * amáveis e cortesia com seleção contextual, avaliação de
 * respostas e aprendizado adaptativo
 */

class ComportamentoAmabilidade {
    public String nome;
    public double intensidade;
    public List<String> contextos;
    public List<String> exemplos;
    
    public ComportamentoAmabilidade(String nome, double intensidade, 
                                  List<String> contextos, List<String> exemplos) {
        this.nome = nome;
        this.intensidade = intensidade;
        this.contextos = new ArrayList<>(contextos);
        this.exemplos = new ArrayList<>(exemplos);
    }
    
    @Override
    public String toString() {
        return String.format("Comportamento{nome='%s', intensidade=%.2f, contextos=%s}", 
            nome, intensidade, contextos);
    }
}

class ComportamentoSelecionado {
    public String tipo;
    public String comportamento;
    public double intensidade;
    public double adequacao;
    public Map<String, Object> contexto;
    public String motivacao;
    public LocalDateTime timestamp;
    
    public ComportamentoSelecionado(String tipo, String comportamento, double intensidade,
                                   double adequacao, Map<String, Object> contexto, String motivacao) {
        this.tipo = tipo;
        this.comportamento = comportamento;
        this.intensidade = intensidade;
        this.adequacao = adequacao;
        this.contexto = new HashMap<>(contexto);
        this.motivacao = motivacao;
        this.timestamp = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("Selecionado{tipo='%s', comportamento='%s', adequacao=%.3f}", 
            tipo, comportamento, adequacao);
    }
}

class RegistroInteracao {
    public LocalDateTime timestamp;
    public ComportamentoSelecionado comportamentoEmitido;
    public Map<String, Object> respostaRecebida;
    public double qualidadeResposta;
    public double eficacia;
    public double nivelAmabilidadeApos;
    
    public RegistroInteracao(ComportamentoSelecionado comportamentoEmitido,
                            Map<String, Object> respostaRecebida, double qualidadeResposta,
                            double eficacia, double nivelAmabilidadeApos) {
        this.timestamp = LocalDateTime.now();
        this.comportamentoEmitido = comportamentoEmitido;
        this.respostaRecebida = new HashMap<>(respostaRecebida);
        this.qualidadeResposta = qualidadeResposta;
        this.eficacia = eficacia;
        this.nivelAmabilidadeApos = nivelAmabilidadeApos;
    }
    
    @Override
    public String toString() {
        return String.format("Interacao{eficacia=%.3f, qualidade=%.3f}", eficacia, qualidadeResposta);
    }
}

class AvaliacaoInteracao {
    public Map<String, Object> avaliacao;
    public List<String> aprendizados;
    public double nivelAmabilidadeAtual;
    public String tendencia;
    
    public AvaliacaoInteracao(Map<String, Object> avaliacao, List<String> aprendizados,
                             double nivelAmabilidadeAtual, String tendencia) {
        this.avaliacao = new HashMap<>(avaliacao);
        this.aprendizados = new ArrayList<>(aprendizados);
        this.nivelAmabilidadeAtual = nivelAmabilidadeAtual;
        this.tendencia = tendencia;
    }
    
    @Override
    public String toString() {
        return String.format("Avaliacao{adequado=%s, positivo=%s, tendencia='%s'}", 
            avaliacao.get("comportamento_adequado"), avaliacao.get("resposta_positiva"), tendencia);
    }
}

class AjusteAmabilidade {
    public boolean ajusteRealizado;
    public double nivelAnterior;
    public double nivelAtual;
    public double variacao;
    public String recomendacao;
    
    public AjusteAmabilidade(boolean ajusteRealizado, double nivelAnterior, 
                           double nivelAtual, double variacao, String recomendacao) {
        this.ajusteRealizado = ajusteRealizado;
        this.nivelAnterior = nivelAnterior;
        this.nivelAtual = nivelAtual;
        this.variacao = variacao;
        this.recomendacao = recomendacao;
    }
    
    @Override
    public String toString() {
        return String.format("Ajuste{anterior=%.2f, atual=%.2f, variacao=%.3f}", 
            nivelAnterior, nivelAtual, variacao);
    }
}

class EstatisticasAmabilidade {
    public double nivelAmabilidadeAtual;
    public int totalInteracoes;
    public double eficaciaMedia;
    public String comportamentoMaisUtilizado;
    public Map<String, Integer> distribuicaoComportamentos;
    public String tendenciaEficacia;
    public List<String> recomendacoes;
    
    public EstatisticasAmabilidade(double nivelAmabilidadeAtual, int totalInteracoes,
                                  double eficaciaMedia, String comportamentoMaisUtilizado,
                                  Map<String, Integer> distribuicaoComportamentos,
                                  String tendenciaEficacia, List<String> recomendacoes) {
        this.nivelAmabilidadeAtual = nivelAmabilidadeAtual;
        this.totalInteracoes = totalInteracoes;
        this.eficaciaMedia = eficaciaMedia;
        this.comportamentoMaisUtilizado = comportamentoMaisUtilizado;
        this.distribuicaoComportamentos = new HashMap<>(distribuicaoComportamentos);
        this.tendenciaEficacia = tendenciaEficacia;
        this.recomendacoes = new ArrayList<>(recomendacoes);
    }
    
    @Override
    public String toString() {
        return String.format("Estatisticas{interacoes=%d, eficacia=%.3f, tendencia='%s'}", 
            totalInteracoes, eficaciaMedia, tendenciaEficacia);
    }
}

class ConfigAmabilidade {
    public double nivelAmabilidadePadrao;
    public boolean debugMode;
    public int maxHistorico;
    
    public ConfigAmabilidade(double nivelAmabilidadePadrao, boolean debugMode, int maxHistorico) {
        this.nivelAmabilidadePadrao = nivelAmabilidadePadrao;
        this.debugMode = debugMode;
        this.maxHistorico = maxHistorico;
    }
    
    public static ConfigAmabilidade criarPadrao() {
        return new ConfigAmabilidade(0.6, false, 100);
    }
}

// Classe principal SistemaAmabilidade
public class Amabilidade {
    
    private ConfigAmabilidade config;
    private double nivelAmabilidade;
    private Map<String, ComportamentoAmabilidade> comportamentosAmabilidade;
    private List<RegistroInteracao> historicoInteracoes;
    
    public Amabilidade(ConfigAmabilidade config) {
        this.config = config;
        this.nivelAmabilidade = config.nivelAmabilidadePadrao;
        this.comportamentosAmabilidade = carregarComportamentos();
        this.historicoInteracoes = new ArrayList<>();
    }
    
    private Map<String, ComportamentoAmabilidade> carregarComportamentos() {
        /** Carrega comportamentos de amabilidade padrão */
        
        Map<String, ComportamentoAmabilidade> mapa = new HashMap<>();
        
        // Cumprimentos
        mapa.put("cumprimentos", new ComportamentoAmabilidade(
            "cumprimentos", 0.3,
            Arrays.asList("inicio_interacao", "reencontro", "despedida"),
            Arrays.asList("Olá", "Bom dia", "Tudo bem?", "Até logo")
        ));
        
        // Agradecimentos
        mapa.put("agradecimentos", new ComportamentoAmabilidade(
            "agradecimentos", 0.5,
            Arrays.asList("receber_ajuda", "receber_elogio", "final_interacao"),
            Arrays.asList("Obrigado", "Agradeço", "Muito gentil")
        ));
        
        // Elogios
        mapa.put("elogios", new ComportamentoAmabilidade(
            "elogios", 0.7,
            Arrays.asList("reconhecer_esforco", "destacar_habilidade", "expressar_admiracao"),
            Arrays.asList("Bom trabalho", "Excelente ideia", "Muito bem feito")
        ));
        
        // Consideração
        mapa.put("consideracao", new ComportamentoAmabilidade(
            "consideracao", 0.6,
            Arrays.asList("demonstrar_interesse", "validar_sentimentos", "oferecer_suporte"),
            Arrays.asList("Como você está?", "Entendo como se sente", "Posso ajudar?")
        ));
        
        // Generosidade
        mapa.put("generosidade", new ComportamentoAmabilidade(
            "generosidade", 0.8,
            Arrays.asList("oferecer_ajuda", "compartilhar_recursos", "ceder_prioridade"),
            Arrays.asList("Deixe-me ajudar", "Pode usar isso", "Você primeiro")
        ));
        
        return mapa;
    }
    
    public ComportamentoSelecionado selecionarComportamento(Map<String, Object> contexto) {
        /**
         * Seleciona comportamento amável apropriado para o contexto
         * 
         * @param contexto Contexto da interação
         * @return Comportamento selecionado
         */
        
        // Filtra comportamentos por contexto
        List<Map<String, Object>> comportamentosRelevantes = new ArrayList<>();
        
        String tipoInteracao = (String) contexto.getOrDefault("tipo_interacao", "");
        
        for (Map.Entry<String, ComportamentoAmabilidade> entry : comportamentosAmabilidade.entrySet()) {
            String nome = entry.getKey();
            ComportamentoAmabilidade dados = entry.getValue();
            
            for (String contextoComportamento : dados.contextos) {
                if (tipoInteracao.contains(contextoComportamento)) {
                    double adequacao = calcularAdequacao(dados, contexto);
                    
                    Map<String, Object> comportamentoRelevante = new HashMap<>();
                    comportamentoRelevante.put("nome", nome);
                    comportamentoRelevante.put("dados", dados);
                    comportamentoRelevante.put("adequacao", adequacao);
                    
                    comportamentosRelevantes.add(comportamentoRelevante);
                    break; // Evita duplicação
                }
            }
        }
        
        if (comportamentosRelevantes.isEmpty()) {
            // Comportamento padrão
            return new ComportamentoSelecionado(
                "cumprimentos", "Olá", 0.3, 0.5, contexto, "inicio_interacao_padrao"
            );
        }
        
        // Seleciona o mais adequado
        Map<String, Object> comportamentoSelecionado = comportamentosRelevantes.stream()
            .max((c1, c2) -> Double.compare((Double) c1.get("adequacao"), (Double) c2.get("adequacao")))
            .orElse(comportamentosRelevantes.get(0));
        
        // Seleciona exemplo específico
        @SuppressWarnings("unchecked")
        ComportamentoAmabilidade dados = (ComportamentoAmabilidade) comportamentoSelecionado.get("dados");
        String exemploSelecionado = dados.exemplos.get(0); // Simplificado
        
        return new ComportamentoSelecionado(
            (String) comportamentoSelecionado.get("nome"),
            exemploSelecionado,
            dados.intensidade,
            (Double) comportamentoSelecionado.get("adequacao"),
            contexto,
            gerarMotivacao((String) comportamentoSelecionado.get("nome"), contexto)
        );
    }
    
    private double calcularAdequacao(ComportamentoAmabilidade dadosComportamento, 
                                    Map<String, Object> contexto) {
        /** Calcula adequação do comportamento ao contexto */
        
        double adequacao = 0.5;
        
        // Ajusta baseado na intensidade necessária
        double intensidadeContexto = (Double) contexto.getOrDefault("intensidade_necessaria", 0.5);
        double diffIntensidade = Math.abs(dadosComportamento.intensidade - intensidadeContexto);
        adequacao -= diffIntensidade * 0.3;
        
        // Ajusta baseado na relação
        String relacao = (String) contexto.get("relacao");
        if ("proxima".equals(relacao)) {
            // Comportamentos mais intensos são mais adequados
            if (dadosComportamento.intensidade > 0.6) {
                adequacao += 0.2;
            }
        } else if ("formal".equals(relacao)) {
            // Comportamentos moderados são mais adequados
            if (dadosComportamento.intensidade >= 0.4 && dadosComportamento.intensidade <= 0.6) {
                adequacao += 0.2;
            }
        }
        
        // Ajusta por nível de amabilidade atual
        adequacao += (nivelAmabilidade - 0.5) * 0.1;
        
        return Math.max(0.0, Math.min(1.0, adequacao));
    }
    
    private String gerarMotivacao(String tipoComportamento, Map<String, Object> contexto) {
        /** Gera justificativa/motivação para o comportamento */
        
        Map<String, String> motivacoes = new HashMap<>();
        motivacoes.put("cumprimentos", "Estabelecer conexão inicial positiva");
        motivacoes.put("agradecimentos", "Reconhecer e valorizar contribuição do outro");
        motivacoes.put("elogios", "Reforçar comportamentos positivos e construir autoestima");
        motivacoes.put("consideracao", "Demonstrar interesse genuíno e validação emocional");
        motivacoes.put("generosidade", "Promover reciprocidade e fortalecer vínculos");
        
        return motivacoes.getOrDefault(tipoComportamento, "Manter interação positiva");
    }
    
    public AvaliacaoInteracao avaliarResposta(ComportamentoSelecionado comportamentoEmitido, 
                                             Map<String, Object> respostaRecebida) {
        /**
         * Avalia resposta recebida a um comportamento amável
         * 
         * @param comportamentoEmitido Comportamento que foi emitido
         * @param respostaRecebida Resposta recebida
         * @return Avaliação da interação
         */
        
        // Analisa qualidade da resposta
        double qualidadeResposta = analisarQualidadeResposta(respostaRecebida);
        
        // Calcula eficácia do comportamento
        double eficacia = calcularEficacia(comportamentoEmitido, qualidadeResposta);
        
        // Atualiza nível de amabilidade
        double nivelAnterior = nivelAmabilidade;
        atualizarAmabilidade(eficacia);
        
        // Registra interação
        RegistroInteracao registro = new RegistroInteracao(
            comportamentoEmitido, respostaRecebida, qualidadeResposta, eficacia, nivelAmabilidade
        );
        
        historicoInteracoes.add(registro);
        
        // Mantém histórico limitado
        if (historicoInteracoes.size() > config.maxHistorico) {
            historicoInteracoes.remove(0);
        }
        
        // Extrai aprendizados
        List<String> aprendizados = extrairAprendizados(registro);
        
        // Determina tendência
        String tendencia;
        if (eficacia > 0.7) {
            tendencia = "melhorando";
        } else if (eficacia > 0.4) {
            tendencia = "estavel";
        } else {
            tendencia = "piorando";
        }
        
        Map<String, Object> avaliacaoMap = new HashMap<>();
        avaliacaoMap.put("comportamento_adequado", eficacia > 0.6);
        avaliacaoMap.put("resposta_positiva", qualidadeResposta > 0.6);
        avaliacaoMap.put("eficacia_geral", eficacia);
        
        return new AvaliacaoInteracao(avaliacaoMap, aprendizados, nivelAmabilidade, tendencia);
    }
    
    private double analisarQualidadeResposta(Map<String, Object> resposta) {
        /** Analisa qualidade da resposta recebida */
        
        double qualidade = 0.5;
        
        // Fatores positivos
        if ("positivo".equals(resposta.get("tom"))) {
            qualidade += 0.3;
        }
        
        if (Boolean.TRUE.equals(resposta.get("reciprocidade"))) {
            qualidade += 0.2;
        }
        
        if ("alto".equals(resposta.get("engajamento"))) {
            qualidade += 0.2;
        }
        
        // Fatores negativos
        if ("negativo".equals(resposta.get("tom"))) {
            qualidade -= 0.4;
        }
        
        if (Boolean.TRUE.equals(resposta.get("ignorar"))) {
            qualidade -= 0.3;
        }
        
        return Math.max(0.0, Math.min(1.0, qualidade));
    }
    
    private double calcularEficacia(ComportamentoSelecionado comportamento, double qualidadeResposta) {
        /** Calcula eficácia do comportamento amável */
        
        // Eficácia baseada na adequação e resposta
        double adequacao = comportamento.adequacao;
        double intensidade = comportamento.intensidade;
        
        double eficacia = adequacao * 0.4 + qualidadeResposta * 0.4 + intensidade * 0.2;
        
        // Ajusta pelo nível de amabilidade
        eficacia *= (0.8 + nivelAmabilidade * 0.4);
        
        return Math.max(0.0, Math.min(1.0, eficacia));
    }
    
    private void atualizarAmabilidade(double eficacia) {
        /** Atualiza nível de amabilidade baseado na eficácia */
        
        if (eficacia > 0.7) {
            // Reforça amabilidade
            double incremento = Math.min(0.1, (eficacia - 0.7) * 0.2);
            nivelAmabilidade = Math.min(1.0, nivelAmabilidade + incremento);
        } else if (eficacia < 0.4) {
            // Reduz amabilidade
            double decremento = Math.min(0.05, (0.4 - eficacia) * 0.1);
            nivelAmabilidade = Math.max(0.1, nivelAmabilidade - decremento);
        }
    }
    
    private List<String> extrairAprendizados(RegistroInteracao registro) {
        /** Extrai aprendizados da interação */
        
        List<String> aprendizados = new ArrayList<>();
        
        double eficacia = registro.eficacia;
        String tipoComportamento = registro.comportamentoEmitido.tipo;
        
        if (eficacia > 0.8) {
            aprendizados.add(String.format(
                "Comportamento '%s' foi muito eficaz neste contexto", tipoComportamento
            ));
        } else if (eficacia < 0.3) {
            aprendizados.add(String.format(
                "Reconsiderar uso de '%s' em contextos similares", tipoComportamento
            ));
        }
        
        // Aprendizado sobre intensidade
        double intensidade = registro.comportamentoEmitido.intensidade;
        double qualidadeResposta = registro.qualidadeResposta;
        
        if (intensidade > 0.7 && qualidadeResposta < 0.4) {
            aprendizados.add("Intensidade alta pode não ser adequada em contextos formais");
        }
        
        return aprendizados;
    }
    
    public AjusteAmabilidade ajustarAmabilidade(double novoNivel) {
        /**
         * Ajusta manualmente o nível de amabilidade
         * 
         * @param novoNivel Novo nível desejado (0 a 1)
         * @return Resultado do ajuste
         */
        
        double nivelAntigo = nivelAmabilidade;
        nivelAmabilidade = Math.max(0.0, Math.min(1.0, novoNivel));
        
        return new AjusteAmabilidade(
            true, nivelAntigo, nivelAmabilidade, nivelAmabilidade - nivelAntigo,
            gerarRecomendacaoAjuste()
        );
    }
    
    private String gerarRecomendacaoAjuste() {
        /** Gera recomendação baseada no nível atual de amabilidade */
        
        if (nivelAmabilidade > 0.8) {
            return "Nível alto - considerar balancear com assertividade quando necessário";
        } else if (nivelAmabilidade > 0.6) {
            return "Nível adequado - manter comportamentos positivos consistentes";
        } else if (nivelAmabilidade > 0.4) {
            return "Nível moderado - oportunidade para aumentar engajamento social";
        } else {
            return "Nível baixo - considerar desenvolver mais comportamentos sociais positivos";
        }
    }
    
    public EstatisticasAmabilidade obterEstatisticasAmabilidade() {
        /** Retorna estatísticas do sistema de amabilidade */
        
        if (historicoInteracoes.isEmpty()) {
            return new EstatisticasAmabilidade(
                nivelAmabilidade, 0, 0, "nenhum", new HashMap<>(), 
                "insuficientes_dados", Arrays.asList("Nenhuma interação registrada ainda")
            );
        }
        
        // Calcula métricas
        List<Double> eficacias = historicoInteracoes.stream()
            .map(i -> i.eficacia)
            .collect(Collectors.toList());
        
        double eficaciaMedia = eficacias.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        
        // Distribuição de comportamentos
        Map<String, Integer> comportamentosUtilizados = new HashMap<>();
        for (RegistroInteracao interacao : historicoInteracoes) {
            String comportamento = interacao.comportamentoEmitido.tipo;
            comportamentosUtilizados.put(comportamento, 
                comportamentosUtilizados.getOrDefault(comportamento, 0) + 1);
        }
        
        // Encontra comportamento mais utilizado
        String comportamentoMaisUtilizado = comportamentosUtilizados.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("nenhum");
        
        return new EstatisticasAmabilidade(
            nivelAmabilidade,
            historicoInteracoes.size(),
            eficaciaMedia,
            comportamentoMaisUtilizado,
            comportamentosUtilizados,
            calcularTendenciaEficacia(),
            gerarRecomendacoesAmabilidade()
        );
    }
    
    private String calcularTendenciaEficacia() {
        /** Calcula tendência de eficácia recente */
        
        if (historicoInteracoes.size() < 5) {
            return "insuficientes_dados";
        }
        
        // Compara primeiras e últimas interações
        int meio = historicoInteracoes.size() / 2;
        List<RegistroInteracao> primeiras = historicoInteracoes.subList(0, meio);
        List<RegistroInteracao> ultimas = historicoInteracoes.subList(meio, historicoInteracoes.size());
        
        double eficaciaPrimeiras = primeiras.stream()
            .mapToDouble(i -> i.eficacia)
            .average().orElse(0.5);
        
        double eficaciaUltimas = ultimas.stream()
            .mapToDouble(i -> i.eficacia)
            .average().orElse(0.5);
        
        if (eficaciaUltimas > eficaciaPrimeiras * 1.1) {
            return "melhorando";
        } else if (eficaciaUltimas < eficaciaPrimeiras * 0.9) {
            return "piorando";
        } else {
            return "estavel";
        }
    }
    
    private List<String> gerarRecomendacoesAmabilidade() {
        /** Gera recomendações para melhorar amabilidade */
        
        List<String> recomendacoes = new ArrayList<>();
        
        // Análise de eficácia
        if (historicoInteracoes.size() >= 10) {
            List<Double> eficaciasRecentes = historicoInteracoes.stream()
                .skip(historicoInteracoes.size() - 10)
                .map(i -> i.eficacia)
                .collect(Collectors.toList());
            
            double eficaciaMediaRecente = eficaciasRecentes.stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);
            
            if (eficaciaMediaRecente < 0.5) {
                recomendacoes.add("Experimentar diferentes tipos de comportamentos amáveis");
            }
        }
        
        // Diversidade de comportamentos
        Set<String> comportamentosUtilizados = new HashSet<>();
        int limiteRecentes = Math.min(20, historicoInteracoes.size());
        
        for (int i = historicoInteracoes.size() - limiteRecentes; i < historicoInteracoes.size(); i++) {
            String comportamento = historicoInteracoes.get(i).comportamentoEmitido.tipo;
            if (comportamento != null) {
                comportamentosUtilizados.add(comportamento);
            }
        }
        
        if (comportamentosUtilizados.size() < 3 && historicoInteracoes.size() > 5) {
            recomendacoes.add("Diversificar repertório de comportamentos amáveis");
        }
        
        // Nível de amabilidade
        if (nivelAmabilidade < 0.4) {
            recomendacoes.add("Praticar comportamentos sociais básicos regularmente");
        } else if (nivelAmabilidade > 0.9) {
            recomendacoes.add("Garantir que amabilidade não comprometa necessidades próprias");
        }
        
        return recomendacoes;
    }
    
    // Getters
    public double getNivelAmabilidade() {
        return nivelAmabilidade;
    }
    
    public Map<String, ComportamentoAmabilidade> getComportamentosAmabilidade() {
        return new HashMap<>(comportamentosAmabilidade);
    }
    
    public List<RegistroInteracao> getHistoricoInteracoes() {
        return new ArrayList<>(historicoInteracoes);
    }
    
    public ConfigAmabilidade getConfig() {
        return config;
    }
    
    /**
     * Método utilitário para criar instância com configuração padrão
     */
    public static Amabilidade criarComConfigPadrao() {
        return new Amabilidade(ConfigAmabilidade.criarPadrao());
    }
    
    /**
     * Método utilitário para demonstração
     */
    public static void demonstracao() {
        System.out.println("🤗 DEMONSTRAÇÃO DO SISTEMA DE AMABILIDADE");
        System.out.println("=".repeat(50));
        
        Amabilidade sistema = new Amabilidade(ConfigAmabilidade.criarPadrao());
        
        // 1. Mostrar comportamentos disponíveis
        System.out.println("\n📋 Comportamentos de Amabilidade Disponíveis:");
        for (ComportamentoAmabilidade comportamento : sistema.getComportamentosAmabilidade().values()) {
            System.out.println("   " + comportamento.toString());
            System.out.printf("      Exemplos: %s%n", String.join(", ", comportamento.exemplos));
        }
        
        // 2. Testar seleção de comportamentos
        System.out.println("\n🎯 Teste de Seleção de Comportamentos:");
        
        // Contexto 1: Início de interação
        Map<String, Object> contexto1 = Map.of(
            "tipo_interacao", "inicio_interacao",
            "relacao", "proxima",
            "intensidade_necessaria", 0.3
        );
        
        ComportamentoSelecionado selecionado1 = sistema.selecionarComportamento(contexto1);
        System.out.println("   Contexto 1 - Início de interação:");
        System.out.println("   " + selecionado1.toString());
        System.out.printf("   Motivação: %s%n", selecionado1.motivacao);
        
        // Contexto 2: Receber ajuda
        Map<String, Object> contexto2 = Map.of(
            "tipo_interacao", "receber_ajuda",
            "relacao", "formal",
            "intensidade_necessaria", 0.5
        );
        
        ComportamentoSelecionado selecionado2 = sistema.selecionarComportamento(contexto2);
        System.out.println("\n   Contexto 2 - Receber ajuda:");
        System.out.println("   " + selecionado2.toString());
        System.out.printf("   Motivação: %s%n", selecionado2.motivacao);
        
        // Contexto 3: Oferecer ajuda
        Map<String, Object> contexto3 = Map.of(
            "tipo_interacao", "oferecer_ajuda",
            "relacao", "proxima",
            "intensidade_necessaria", 0.7
        );
        
        ComportamentoSelecionado selecionado3 = sistema.selecionarComportamento(contexto3);
        System.out.println("\n   Contexto 3 - Oferecer ajuda:");
        System.out.println("   " + selecionado3.toString());
        System.out.printf("   Motivação: %s%n", selecionado3.motivacao);
        
        // 3. Simular interações completas
        System.out.println("\n💬 Simulação de Interações Completas:");
        
        // Interação 1: Resposta positiva
        Map<String, Object> respostaPositiva = Map.of(
            "tom", "positivo",
            "reciprocidade", true,
            "engajamento", "alto"
        );
        
        AvaliacaoInteracao avaliacao1 = sistema.avaliarResposta(selecionado1, respostaPositiva);
        System.out.println("   Interação 1 - Resposta Positiva:");
        System.out.println("   " + avaliacao1.toString());
        System.out.printf("   Aprendizados: %s%n", String.join(", ", avaliacao1.aprendizados));
        
        // Interação 2: Resposta neutra
        Map<String, Object> respostaNeutra = Map.of(
            "tom", "neutro",
            "reciprocidade", false,
            "engajamento", "baixo"
        );
        
        AvaliacaoInteracao avaliacao2 = sistema.avaliarResposta(selecionado2, respostaNeutra);
        System.out.println("\n   Interação 2 - Resposta Neutra:");
        System.out.println("   " + avaliacao2.toString());
        System.out.printf("   Aprendizados: %s%n", String.join(", ", avaliacao2.aprendizados));
        
        // Interação 3: Resposta negativa
        Map<String, Object> respostaNegativa = Map.of(
            "tom", "negativo",
            "reciprocidade", false,
            "ignorar", true
        );
        
        AvaliacaoInteracao avaliacao3 = sistema.avaliarResposta(selecionado3, respostaNegativa);
        System.out.println("\n   Interação 3 - Resposta Negativa:");
        System.out.println("   " + avaliacao3.toString());
        System.out.printf("   Aprendizados: %s%n", String.join(", ", avaliacao3.aprendizados));
        
        // 4. Ajustar nível de amabilidade
        System.out.println("\n🔧 Ajuste de Nível de Amabilidade:");
        
        AjusteAmabilidade ajuste = sistema.ajustarAmabilidade(0.8);
        System.out.println("   " + ajuste.toString());
        System.out.printf("   Recomendação: %s%n", ajuste.recomendacao);
        
        // 5. Estatísticas finais
        System.out.println("\n📊 Estatísticas Finais:");
        
        EstatisticasAmabilidade estatisticas = sistema.obterEstatisticasAmabilidade();
        System.out.println("   " + estatisticas.toString());
        System.out.printf("   Nível atual: %.3f%n", estatisticas.nivelAmabilidadeAtual);
        System.out.printf("   Comportamento mais usado: %s%n", estatisticas.comportamentoMaisUtilizado);
        System.out.printf("   Tendência de eficácia: %s%n", estatisticas.tendenciaEficacia);
        
        if (!estatisticas.recomendacoes.isEmpty()) {
            System.out.println("   Recomendações:");
            for (String rec : estatisticas.recomendacoes) {
                System.out.println("      • " + rec);
            }
        }
        
        // 6. Histórico de interações
        System.out.println("\n📋 Histórico de Interações:");
        for (int i = 0; i < sistema.getHistoricoInteracoes().size(); i++) {
            RegistroInteracao registro = sistema.getHistoricoInteracoes().get(i);
            System.out.printf("   %d. %s - %s%n", 
                i + 1, registro.comportamentoEmitido.tipo, registro.toString());
        }
        
        System.out.println("\n✅ Demonstração concluída!");
    }
    
    /**
     * Método principal para execução
     */
    public static void main(String[] args) {
        demonstracao();
    }
}
