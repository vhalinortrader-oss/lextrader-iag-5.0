import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Sistema de autocuidado e manutenção do bem-estar
 * Gerencia práticas de autocuidado e monitora o bem-estar do sistema
 */
public class SistemaAutocuidado {
    
    private final Map<String, Object> config;
    private double nivelAutocuidado;
    private long ultimoAutocuidado;
    private final Map<String, AreaAutocuidado> praticasAutocuidado;
    private final List<RegistroAutocuidado> historicoAutocuidado;
    private List<String> areasPrioritarias;
    
    // Classe para configuração do autocuidado
    public static class ConfigAutocuidado {
        private final long intervaloAutocuidado; // em segundos
        
        public ConfigAutocuidado(long intervaloAutocuidado) {
            this.intervaloAutocuidado = intervaloAutocuidado;
        }
        
        public long getIntervaloAutocuidado() {
            return intervaloAutocuidado;
        }
    }
    
    // Classe para área de autocuidado
    private static class AreaAutocuidado {
        String descricao;
        List<PraticaAutocuidado> praticas;
        double prioridade;
        
        AreaAutocuidado(String descricao, double prioridade) {
            this.descricao = descricao;
            this.prioridade = prioridade;
            this.praticas = new ArrayList<>();
        }
        
        void adicionarPratica(PraticaAutocuidado pratica) {
            praticas.add(pratica);
        }
    }
    
    // Classe para prática de autocuidado
    private static class PraticaAutocuidado {
        String nome;
        int duracao; // em minutos
        double beneficio;
        
        PraticaAutocuidado(String nome, int duracao, double beneficio) {
            this.nome = nome;
            this.duracao = duracao;
            this.beneficio = beneficio;
        }
        
        // Getters
        public String getNome() { return nome; }
        public int getDuracao() { return duracao; }
        public double getBeneficio() { return beneficio; }
    }
    
    // Classe para registro de autocuidado
    private static class RegistroAutocuidado {
        String id;
        long timestamp;
        String pratica;
        String area;
        int duracaoPlanejada;
        double duracaoReal; // em minutos
        double beneficioPercebido;
        double nivelAutocuidadoApos;
        double qualidadeExecucao;
        
        RegistroAutocuidado(String id, String pratica, String area, 
                           int duracaoPlanejada, double duracaoReal,
                           double beneficioPercebido, double nivelAutocuidadoApos,
                           double qualidadeExecucao) {
            this.id = id;
            this.timestamp = Instant.now().getEpochSecond();
            this.pratica = pratica;
            this.area = area;
            this.duracaoPlanejada = duracaoPlanejada;
            this.duracaoReal = duracaoReal;
            this.beneficioPercebido = beneficioPercebido;
            this.nivelAutocuidadoApos = nivelAutocuidadoApos;
            this.qualidadeExecucao = qualidadeExecucao;
        }
    }
    
    // Classe para resultado da verificação de necessidade
    public static class ResultadoNecessidade {
        double necessidadeAutocuidado;
        Map<String, Double> fatores;
        String recomendacaoAcao;
        List<Map<String, Object>> areasPrioritarias;
        double tempoDesdeUltimoHoras;
        
        ResultadoNecessidade(double necessidadeAutocuidado, Map<String, Double> fatores,
                            String recomendacaoAcao, List<Map<String, Object>> areasPrioritarias,
                            double tempoDesdeUltimoHoras) {
            this.necessidadeAutocuidado = necessidadeAutocuidado;
            this.fatores = fatores;
            this.recomendacaoAcao = recomendacaoAcao;
            this.areasPrioritarias = areasPrioritarias;
            this.tempoDesdeUltimoHoras = tempoDesdeUltimoHoras;
        }
    }
    
    // Classe para prática selecionada
    public static class PraticaSelecionada {
        String praticaSelecionada;
        String area;
        int duracaoEstimada;
        double beneficioEsperado;
        List<String> instrucoes;
        List<String> preparacaoNecessaria;
        String erro;
        
        PraticaSelecionada(String praticaSelecionada, String area, int duracaoEstimada,
                          double beneficioEsperado, List<String> instrucoes,
                          List<String> preparacaoNecessaria) {
            this.praticaSelecionada = praticaSelecionada;
            this.area = area;
            this.duracaoEstimada = duracaoEstimada;
            this.beneficioEsperado = beneficioEsperado;
            this.instrucoes = instrucoes;
            this.preparacaoNecessaria = preparacaoNecessaria;
        }
        
        PraticaSelecionada(String erro) {
            this.erro = erro;
        }
    }
    
    // Classe para resultado da execução
    public static class ResultadoExecucao {
        boolean execucaoConcluida;
        String praticaId;
        double beneficioObtido;
        double nivelAutocuidadoAtualizado;
        String feedbackExecucao;
        Map<String, Object> recomendacaoProximaPratica;
        
        ResultadoExecucao(boolean execucaoConcluida, String praticaId, double beneficioObtido,
                         double nivelAutocuidadoAtualizado, String feedbackExecucao,
                         Map<String, Object> recomendacaoProximaPratica) {
            this.execucaoConcluida = execucaoConcluida;
            this.praticaId = praticaId;
            this.beneficioObtido = beneficioObtido;
            this.nivelAutocuidadoAtualizado = nivelAutocuidadoAtualizado;
            this.feedbackExecucao = feedbackExecucao;
            this.recomendacaoProximaPratica = recomendacaoProximaPratica;
        }
    }
    
    // Classe para rotina de autocuidado
    public static class RotinaAutocuidado {
        String id;
        long timestampCriacao;
        List<String> areasFoco;
        int tempoTotal;
        String frequencia;
        List<Map<String, Object>> sequenciaPraticas;
        double eficienciaEstimada;
        List<String> instrucoesGerais;
        
        RotinaAutocuidado(String id, List<String> areasFoco, int tempoTotal,
                         String frequencia, List<Map<String, Object>> sequenciaPraticas,
                         double eficienciaEstimada, List<String> instrucoesGerais) {
            this.id = id;
            this.timestampCriacao = Instant.now().getEpochSecond();
            this.areasFoco = areasFoco;
            this.tempoTotal = tempoTotal;
            this.frequencia = frequencia;
            this.sequenciaPraticas = sequenciaPraticas;
            this.eficienciaEstimada = eficienciaEstimada;
            this.instrucoesGerais = instrucoesGerais;
        }
    }
    
    // Construtor
    public SistemaAutocuidado(ConfigAutocuidado config) {
        this.config = new HashMap<>();
        this.config.put("intervalo_autocuidado", config.getIntervaloAutocuidado());
        
        this.nivelAutocuidado = 0.6;
        // Força primeira verificação
        this.ultimoAutocuidado = Instant.now().getEpochSecond() - config.getIntervaloAutocuidado();
        
        this.praticasAutocuidado = new ConcurrentHashMap<>();
        this.historicoAutocuidado = Collections.synchronizedList(new ArrayList<>());
        this.areasPrioritarias = new ArrayList<>();
        
        carregarPraticas();
    }
    
    /**
     * Carrega práticas de autocuidado
     */
    private void carregarPraticas() {
        // Área Física
        AreaAutocuidado fisico = new AreaAutocuidado(
            "Cuidados com o corpo e saúde física", 0.7);
        fisico.adicionarPratica(new PraticaAutocuidado("alongamento", 10, 0.6));
        fisico.adicionarPratica(new PraticaAutocuidado("hidratacao", 2, 0.7));
        fisico.adicionarPratica(new PraticaAutocuidado("alimentacao_consciente", 20, 0.8));
        praticasAutocuidado.put("fisico", fisico);
        
        // Área Emocional
        AreaAutocuidado emocional = new AreaAutocuidado(
            "Cuidados com saúde emocional", 0.9);
        emocional.adicionarPratica(new PraticaAutocuidado("diario_emocional", 15, 0.8));
        emocional.adicionarPratica(new PraticaAutocuidado("autoempatia", 10, 0.9));
        emocional.adicionarPratica(new PraticaAutocuidado("reconhecimento_conquistas", 5, 0.7));
        praticasAutocuidado.put("emocional", emocional);
        
        // Área Mental
        AreaAutocuidado mental = new AreaAutocuidado(
            "Cuidados com saúde mental", 0.6);
        mental.adicionarPratica(new PraticaAutocuidado("pausa_digital", 30, 0.7));
        mental.adicionarPratica(new PraticaAutocuidado("leitura_leve", 20, 0.6));
        mental.adicionarPratica(new PraticaAutocuidado("planejamento_descanso", 10, 0.8));
        praticasAutocuidado.put("mental", mental);
        
        // Área Social
        AreaAutocuidado social = new AreaAutocuidado(
            "Cuidados com relações sociais", 0.5);
        social.adicionarPratica(new PraticaAutocuidado("conexao_significativa", 30, 0.8));
        social.adicionarPratica(new PraticaAutocuidado("estabelecer_limites", 15, 0.9));
        social.adicionarPratica(new PraticaAutocuidado("gratidao_relacional", 5, 0.7));
        praticasAutocuidado.put("social", social);
    }
    
    /**
     * Verifica necessidade de autocuidado baseado no estado atual
     * @param estadoAtual Estado atual do sistema
     * @return Análise de necessidade
     */
    public ResultadoNecessidade verificarNecessidadeAutocuidado(Map<String, Object> estadoAtual) {
        // Calcula tempo desde último autocuidado
        long agora = Instant.now().getEpochSecond();
        long tempoDesdeUltimo = agora - ultimoAutocuidado;
        long intervaloConfig = (long) config.get("intervalo_autocuidado");
        
        double necessidadeTemporal = Math.min(1.0, (double) tempoDesdeUltimo / intervaloConfig);
        
        // Analisa estado atual
        double necessidadeEstado = analisarNecessidadeEstado(estadoAtual);
        
        // Necessidade combinada
        double necessidadeGeral = Math.max(necessidadeTemporal, necessidadeEstado);
        
        // Identifica áreas prioritárias
        List<Map<String, Object>> areasPrioritarias = identificarAreasPrioritarias(estadoAtual);
        
        // Determina recomendação de ação
        String recomendacaoAcao;
        if (necessidadeGeral > 0.7) {
            recomendacaoAcao = "recomendado";
        } else if (necessidadeGeral > 0.4) {
            recomendacaoAcao = "opcional";
        } else {
            recomendacaoAcao = "nao_necessario";
        }
        
        // Prepara fatores
        Map<String, Double> fatores = new HashMap<>();
        fatores.put("temporal", necessidadeTemporal);
        fatores.put("estado", necessidadeEstado);
        
        double tempoDesdeUltimoHoras = tempoDesdeUltimo / 3600.0;
        
        return new ResultadoNecessidade(necessidadeGeral, fatores, recomendacaoAcao,
                                       areasPrioritarias, tempoDesdeUltimoHoras);
    }
    
    /**
     * Analisa necessidade de autocuidado baseado no estado
     */
    private double analisarNecessidadeEstado(Map<String, Object> estado) {
        double necessidade = 0.0;
        
        // Extrai valores do estado com conversão segura
        double nivelEstresse = ((Number) estado.getOrDefault("nivel_estresse", 0)).doubleValue();
        double fadigaMental = ((Number) estado.getOrDefault("fadiga_mental", 0)).doubleValue();
        double isolamentoSocial = ((Number) estado.getOrDefault("isolamento_social", 0)).doubleValue();
        double negligenciaAutocuidado = ((Number) estado.getOrDefault("negligencia_autocuidado", 0)).doubleValue();
        
        // Indicadores de necessidade
        if (nivelEstresse > 0.6) {
            necessidade += 0.4;
        }
        
        if (fadigaMental > 0.5) {
            necessidade += 0.3;
        }
        
        if (isolamentoSocial > 0.4) {
            necessidade += 0.2;
        }
        
        if (negligenciaAutocuidado > 0.3) {
            necessidade += 0.3;
        }
        
        return Math.min(1.0, necessidade);
    }
    
    /**
     * Identifica áreas prioritárias para autocuidado
     */
    private List<Map<String, Object>> identificarAreasPrioritarias(Map<String, Object> estado) {
        List<Map<String, Object>> areas = new ArrayList<>();
        
        // Mapeamento de indicadores para áreas
        Map<String, List<String>> mapeamentoIndicadores = new HashMap<>();
        mapeamentoIndicadores.put("nivel_estresse", Arrays.asList("emocional", "fisico"));
        mapeamentoIndicadores.put("fadiga_mental", Arrays.asList("mental", "fisico"));
        mapeamentoIndicadores.put("isolamento_social", Arrays.asList("social", "emocional"));
        mapeamentoIndicadores.put("negligencia_autocuidado", Arrays.asList("fisico", "emocional"));
        
        for (Map.Entry<String, Object> entry : estado.entrySet()) {
            String indicador = entry.getKey();
            double valor = ((Number) entry.getValue()).doubleValue();
            
            if (valor > 0.5 && mapeamentoIndicadores.containsKey(indicador)) {
                for (String area : mapeamentoIndicadores.get(indicador)) {
                    // Verifica se área já está na lista
                    Optional<Map<String, Object>> areaExistente = areas.stream()
                        .filter(a -> area.equals(a.get("area")))
                        .findFirst();
                    
                    if (areaExistente.isPresent()) {
                        Map<String, Object> areaMap = areaExistente.get();
                        double prioridadeAtual = (double) areaMap.get("prioridade");
                        areaMap.put("prioridade", prioridadeAtual + valor * 0.2);
                    } else {
                        Map<String, Object> novaArea = new HashMap<>();
                        novaArea.put("area", area);
                        novaArea.put("prioridade", valor * 0.3);
                        novaArea.put("indicador_principal", indicador);
                        areas.add(novaArea);
                    }
                }
            }
        }
        
        // Ordena por prioridade
        areas.sort((a, b) -> 
            Double.compare((double) b.get("prioridade"), (double) a.get("prioridade")));
        
        // Limita a 2 áreas principais
        List<Map<String, Object>> areasPrincipais = areas.stream()
            .limit(2)
            .collect(Collectors.toList());
        
        // Atualiza áreas prioritárias
        this.areasPrioritarias = areasPrincipais.stream()
            .map(a -> (String) a.get("area"))
            .collect(Collectors.toList());
        
        return areasPrincipais;
    }
    
    /**
     * Seleciona prática de autocuidado apropriada
     * @param area Área específica (opcional)
     * @param tempoDisponivel Tempo disponível em minutos (opcional)
     * @return Prática selecionada
     */
    public PraticaSelecionada selecionarPraticaAutocuidado(String area, Integer tempoDisponivel) {
        // Se área não especificada, usa área prioritária
        if (area == null && !areasPrioritarias.isEmpty()) {
            area = areasPrioritarias.get(0);
        } else if (area == null) {
            area = "emocional"; // Padrão
        }
        
        if (!praticasAutocuidado.containsKey(area)) {
            return new PraticaSelecionada(String.format("Área '%s' não reconhecida", area));
        }
        
        // Filtra práticas pela área
        List<PraticaAutocuidado> praticasArea = praticasAutocuidado.get(area).praticas;
        
        // Filtra por tempo disponível se especificado
        List<PraticaAutocuidado> praticasCompativeis;
        if (tempoDisponivel != null) {
            praticasCompativeis = praticasArea.stream()
                .filter(p -> p.getDuracao() <= tempoDisponivel)
                .collect(Collectors.toList());
        } else {
            praticasCompativeis = new ArrayList<>(praticasArea);
        }
        
        PraticaAutocuidado praticaSelecionada;
        
        if (praticasCompativeis.isEmpty()) {
            // Seleciona prática mais curta
            praticaSelecionada = praticasArea.stream()
                .min(Comparator.comparingInt(PraticaAutocuidado::getDuracao))
                .orElse(null);
        } else {
            // Seleciona prática com melhor benefício-tempo
            praticaSelecionada = praticasCompativeis.stream()
                .max(Comparator.comparingDouble(p -> p.getBeneficio() / p.getDuracao()))
                .orElse(null);
        }
        
        if (praticaSelecionada == null) {
            return new PraticaSelecionada("Nenhuma prática disponível");
        }
        
        return new PraticaSelecionada(
            praticaSelecionada.getNome(),
            area,
            praticaSelecionada.getDuracao(),
            praticaSelecionada.getBeneficio(),
            gerarInstrucoesPratica(praticaSelecionada.getNome(), area),
            verificarPreparacaoPratica(praticaSelecionada.getNome())
        );
    }
    
    /**
     * Gera instruções para a prática de autocuidado
     */
    private List<String> gerarInstrucoesPratica(String nomePratica, String area) {
        Map<String, List<String>> instrucoesPorPratica = new HashMap<>();
        
        instrucoesPorPratica.put("alongamento", Arrays.asList(
            "Encontrar espaço confortável",
            "Respirar profundamente",
            "Alongar suavemente músculos tensionados",
            "Manter cada alongamento por 20-30 segundos"
        ));
        
        instrucoesPorPratica.put("diario_emocional", Arrays.asList(
            "Pegar papel e caneta ou abrir documento digital",
            "Escrever livremente sobre emoções atuais",
            "Não se preocupar com gramática ou estilo",
            "Refletir sobre padrões identificados"
        ));
        
        instrucoesPorPratica.put("pausa_digital", Arrays.asList(
            "Desligar notificações",
            "Afastar-se de telas",
            "Focar em ambiente físico ao redor",
            "Respirar conscientemente"
        ));
        
        instrucoesPorPratica.put("conexao_significativa", Arrays.asList(
            "Identificar pessoa para contato",
            "Preparar para escuta ativa",
            "Compartilhar de forma autêntica",
            "Focar em qualidade da interação"
        ));
        
        List<String> instrucoesDefault = Arrays.asList(
            "Encontrar ambiente confortável",
            "Respirar profundamente para começar",
            "Praticar com atenção plena",
            "Agradecer a si mesmo pelo cuidado"
        );
        
        return instrucoesPorPratica.getOrDefault(nomePratica, instrucoesDefault);
    }
    
    /**
     * Verifica preparação necessária para a prática
     */
    private List<String> verificarPreparacaoPratica(String nomePratica) {
        Map<String, List<String>> preparacao = new HashMap<>();
        
        preparacao.put("alongamento", Arrays.asList("espaco_livre", "roupa_confortavel"));
        preparacao.put("diario_emocional", Arrays.asList("material_escrita", "privacidade"));
        preparacao.put("pausa_digital", Arrays.asList("notificacoes_desligadas", "alternativa_nao_digital"));
        preparacao.put("conexao_significativa", Arrays.asList("disponibilidade_emocional", "tempo_suficiente"));
        
        List<String> preparacaoDefault = Arrays.asList("boa_vontade", "atencao");
        
        return preparacao.getOrDefault(nomePratica, preparacaoDefault);
    }
    
    /**
     * Executa uma prática de autocuidado
     * @param praticaInfo Informações da prática a executar
     * @return Resultado da execução
     */
    public ResultadoExecucao executarPraticaAutocuidado(PraticaSelecionada praticaInfo) {
        String praticaId = String.format("autocuidado_%d", Instant.now().getEpochSecond());
        
        // Simula execução
        long tempoInicio = System.currentTimeMillis();
        
        // Simulação de execução (1 segundo)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long tempoFim = System.currentTimeMillis();
        double duracaoReal = (tempoFim - tempoInicio) / 1000.0; // em segundos
        
        // Calcula benefício percebido
        double beneficioBase = praticaInfo.beneficioEsperado;
        double qualidadeExecucao = avaliarQualidadeExecucao(praticaInfo, duracaoReal);
        double beneficioPercebido = beneficioBase * qualidadeExecucao;
        
        // Atualiza estado
        this.ultimoAutocuidado = Instant.now().getEpochSecond();
        this.nivelAutocuidado = Math.min(1.0, this.nivelAutocuidado + beneficioPercebido * 0.1);
        
        // Registra execução
        RegistroAutocuidado registro = new RegistroAutocuidado(
            praticaId,
            praticaInfo.praticaSelecionada,
            praticaInfo.area,
            praticaInfo.duracaoEstimada,
            duracaoReal / 60.0, // em minutos
            beneficioPercebido,
            this.nivelAutocuidado,
            qualidadeExecucao
        );
        
        historicoAutocuidado.add(registro);
        
        // Gera recomendação para próxima prática
        Map<String, Object> recomendacao = sugerirProximaPratica(praticaInfo.area);
        
        return new ResultadoExecucao(
            true,
            praticaId,
            beneficioPercebido,
            this.nivelAutocuidado,
            gerarFeedbackExecucao(qualidadeExecucao),
            recomendacao
        );
    }
    
    /**
     * Avalia qualidade da execução da prática
     */
    private double avaliarQualidadeExecucao(PraticaSelecionada praticaInfo, double duracaoReal) {
        double qualidade = 0.7;
        
        // Ajusta pela duração
        int duracaoPlanejadaSegundos = praticaInfo.duracaoEstimada * 60; // converter para segundos
        
        if (duracaoReal >= duracaoPlanejadaSegundos * 0.8) {
            qualidade += 0.2;
        } else if (duracaoReal >= duracaoPlanejadaSegundos * 0.5) {
            qualidade += 0.1;
        }
        
        // Considera nível de autocuidado atual
        if (this.nivelAutocuidado > 0.7) {
            qualidade += 0.1;
        }
        
        return Math.min(1.0, qualidade);
    }
    
    /**
     * Gera feedback baseado na qualidade da execução
     */
    private String gerarFeedbackExecucao(double qualidade) {
        if (qualidade > 0.8) {
            return "Excelente execução! O autocuidado foi realizado com atenção plena.";
        } else if (qualidade > 0.6) {
            return "Boa execução. O tempo dedicado foi benéfico para o bem-estar.";
        } else if (qualidade > 0.4) {
            return "Execução adequada. Mesmo práticas breves contribuem para o autocuidado.";
        } else {
            return "Execução realizada. Considerar aumentar a qualidade na próxima prática.";
        }
    }
    
    /**
     * Sugere próxima prática de autocuidado
     */
    private Map<String, Object> sugerirProximaPratica(String areaAtual) {
        // Sugere prática em área diferente para balancear
        List<String> areasDisponiveis = new ArrayList<>(praticasAutocuidado.keySet());
        
        String proximaArea;
        if (areasDisponiveis.size() > 1) {
            // Remove área atual
            List<String> areasAlternativas = areasDisponiveis.stream()
                .filter(a -> !a.equals(areaAtual))
                .collect(Collectors.toList());
            proximaArea = areasAlternativas.get(0);
        } else {
            proximaArea = areaAtual;
        }
        
        // Seleciona prática curta para próxima vez
        PraticaSelecionada pratica = selecionarPraticaAutocuidado(proximaArea, 10);
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("area", proximaArea);
        resultado.put("pratica", pratica.praticaSelecionada);
        resultado.put("duracao", pratica.duracaoEstimada);
        
        return resultado;
    }
    
    /**
     * Cria rotina personalizada de autocuidado
     * @param preferencias Preferências do usuário
     * @return Rotina criada
     */
    public RotinaAutocuidado criarRotinaAutocuidado(Map<String, Object> preferencias) {
        // Analisa preferências
        @SuppressWarnings("unchecked")
        List<String> areasFoco = (List<String>) preferencias.getOrDefault(
            "areas_foco", Arrays.asList("emocional", "fisico"));
        
        int tempoTotal = (int) preferencias.getOrDefault("tempo_total_min", 30);
        String frequencia = (String) preferencias.getOrDefault("frequencia", "diaria");
        
        // Cria sequência de práticas
        List<Map<String, Object>> sequencia = new ArrayList<>();
        int tempoAlocado = 0;
        
        for (String area : areasFoco) {
            if (tempoAlocado >= tempoTotal) {
                break;
            }
            
            if (praticasAutocuidado.containsKey(area)) {
                // Seleciona prática adequada
                int tempoRestante = tempoTotal - tempoAlocado;
                PraticaSelecionada pratica = selecionarPraticaAutocuidado(area, tempoRestante);
                
                if (pratica.erro == null) {
                    Map<String, Object> itemSequencia = new HashMap<>();
                    itemSequencia.put("ordem", sequencia.size() + 1);
                    itemSequencia.put("area", area);
                    itemSequencia.put("pratica", pratica.praticaSelecionada);
                    itemSequencia.put("duracao", pratica.duracaoEstimada);
                    itemSequencia.put("beneficio", pratica.beneficioEsperado);
                    
                    sequencia.add(itemSequencia);
                    tempoAlocado += pratica.duracaoEstimada;
                }
            }
        }
        
        String rotinaId = String.format("rotina_%d", Instant.now().getEpochSecond());
        
        return new RotinaAutocuidado(
            rotinaId,
            areasFoco,
            tempoAlocado,
            frequencia,
            sequencia,
            calcularEficienciaRotina(sequencia),
            gerarInstrucoesRotina(frequencia)
        );
    }
    
    /**
     * Calcula eficiência estimada da rotina
     */
    private double calcularEficienciaRotina(List<Map<String, Object>> sequencia) {
        if (sequencia.isEmpty()) {
            return 0.0;
        }
        
        double beneficioTotal = 0.0;
        int duracaoTotal = 0;
        
        for (Map<String, Object> item : sequencia) {
            beneficioTotal += (double) item.get("beneficio");
            duracaoTotal += (int) item.get("duracao");
        }
        
        if (duracaoTotal == 0) {
            return 0.0;
        }
        
        double eficiencia = beneficioTotal / duracaoTotal;
        return Math.min(1.0, eficiencia * 2); // Normaliza para 0-1
    }
    
    /**
     * Gera instruções gerais para a rotina
     */
    private List<String> gerarInstrucoesRotina(String frequencia) {
        List<String> instrucoes = new ArrayList<>(Arrays.asList(
            "Escolher horário consistente para a prática",
            "Preparar ambiente para minimizar interrupções",
            "Iniciar com respiração consciente",
            "Praticar com gentileza e sem julgamento"
        ));
        
        if ("diaria".equals(frequencia)) {
            instrucoes.add("Manter consistência diária para melhores resultados");
        } else if ("semanal".equals(frequencia)) {
            instrucoes.add("Reservar tempo específico na semana para a prática");
        }
        
        return instrucoes;
    }
    
    /**
     * Retorna estatísticas do sistema de autocuidado
     */
    public Map<String, Object> obterEstatisticasAutocuidado() {
        if (historicoAutocuidado.isEmpty()) {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensagem", "Nenhuma prática de autocuidado registrada ainda");
            return resultado;
        }
        
        // Calcula métricas
        int totalPraticas = historicoAutocuidado.size();
        
        // Distribuição por área
        Map<String, Integer> distribuicaoAreas = new HashMap<>();
        for (RegistroAutocuidado registro : historicoAutocuidado) {
            String area = registro.area;
            distribuicaoAreas.put(area, distribuicaoAreas.getOrDefault(area, 0) + 1);
        }
        
        // Benefício médio
        double beneficioMedio = historicoAutocuidado.stream()
            .mapToDouble(r -> r.beneficioPercebido)
            .average()
            .orElse(0.0);
        
        // Frequência (intervalo médio entre práticas)
        double intervaloMedioHoras = 0.0;
        if (totalPraticas > 1) {
            List<Long> tempos = historicoAutocuidado.stream()
                .map(r -> r.timestamp)
                .sorted()
                .collect(Collectors.toList());
            
            double somaIntervalos = 0.0;
            for (int i = 0; i < tempos.size() - 1; i++) {
                somaIntervalos += tempos.get(i + 1) - tempos.get(i);
            }
            intervaloMedioHoras = somaIntervalos / (tempos.size() - 1) / 3600.0;
        }
        
        // Área mais praticada
        String areaMaisPraticada = distribuicaoAreas.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("nenhuma");
        
        long agora = Instant.now().getEpochSecond();
        double tempoDesdeUltimaHoras = (agora - ultimoAutocuidado) / 3600.0;
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("nivel_autocuidado_atual", nivelAutocuidado);
        resultado.put("total_praticas_realizadas", totalPraticas);
        resultado.put("beneficio_medio_praticas", beneficioMedio);
        resultado.put("intervalo_medio_entre_praticas_horas", intervaloMedioHoras);
        resultado.put("area_mais_praticada", areaMaisPraticada);
        resultado.put("distribuicao_areas", distribuicaoAreas);
        resultado.put("tempo_desde_ultima_pratica_horas", tempoDesdeUltimaHoras);
        resultado.put("recomendacoes", gerarRecomendacoesAutocuidado());
        
        return resultado;
    }
    
    /**
     * Gera recomendações para autocuidado
     */
    private List<String> gerarRecomendacoesAutocuidado() {
        List<String> recomendacoes = new ArrayList<>();
        
        // Baseado em frequência
        if (!historicoAutocuidado.isEmpty()) {
            long agora = Instant.now().getEpochSecond();
            double tempoDesdeUltimo = (agora - ultimoAutocuidado) / 3600.0;
            
            if (tempoDesdeUltimo > 48) {
                recomendacoes.add("Considerar prática de autocuidado em breve");
            } else if (tempoDesdeUltimo > 24) {
                recomendacoes.add("Bom momento para próxima prática de autocuidado");
            }
        }
        
        // Baseado em distribuição
        if (historicoAutocuidado.size() >= 10) {
            Map<String, Integer> distribuicao = new HashMap<>();
            for (RegistroAutocuidado registro : historicoAutocuidado.subList(
                    historicoAutocuidado.size() - 10, historicoAutocuidado.size())) {
                String area = registro.area;
                distribuicao.put(area, distribuicao.getOrDefault(area, 0) + 1);
            }
            
            if (!distribuicao.isEmpty()) {
                String areaMenosPraticada = distribuicao.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
                
                if (areaMenosPraticada != null) {
                    recomendacoes.add(String.format(
                        "Considerar práticas na área de '%s' para balancear", areaMenosPraticada));
                }
            }
        }
        
        // Baseado em nível atual
        if (nivelAutocuidado < 0.4) {
            recomendacoes.add("Aumentar frequência de práticas de autocuidado");
        } else if (nivelAutocuidado > 0.9) {
            recomendacoes.add("Manter excelentes hábitos de autocuidado");
        }
        
        return recomendacoes;
    }
    
    // Getters
    public double getNivelAutocuidado() {
        return nivelAutocuidado;
    }
    
    public List<RegistroAutocuidado> getHistoricoAutocuidado() {
        return Collections.unmodifiableList(historicoAutocuidado);
    }
    
    public List<String> getAreasPrioritarias() {
        return Collections.unmodifiableList(areasPrioritarias);
    }
}