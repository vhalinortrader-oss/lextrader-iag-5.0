// IAGNucleo.java
package lextrader.iag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// ============================================================================
// INTERFACES E TIPOS BASE
// ============================================================================

interface Dictionary<T> {
    Map<String, T> toMap();
}

class ResultadoProcessamento {
    public int ciclo;
    public Object entrada;
    public List<String> saidas;
    public List<String> insights;
    public List<Object> aprendizados;
    public String estadoAntes;
    public String estadoDepois;
    public double nivelConsciencia;
    public double phi;
    public String idEpisodio;
    public Map<String, Object> processamentos;
    public List<Object> metaObservacoes;
    public String qualiaGerado;
    public String erro;
    public String traceback;
    
    public ResultadoProcessamento() {
        this.saidas = new ArrayList<>();
        this.insights = new ArrayList<>();
        this.aprendizados = new ArrayList<>();
        this.processamentos = new HashMap<>();
        this.metaObservacoes = new ArrayList<>();
    }
}

class Episodio {
    public String id;
    public Date timestamp;
    public Object conteudo;
    public Map<String, Double> emocoes;
    public Map<String, Object> contexto;
    public double importancia;
    public Set<String> tags;
    public int acessoCount;
    public Date ultimoAcesso;
    
    public Episodio(String id, Object conteudo) {
        this.id = id;
        this.timestamp = new Date();
        this.conteudo = conteudo;
        this.emocoes = new HashMap<>();
        this.contexto = new HashMap<>();
        this.tags = new HashSet<>();
        this.acessoCount = 0;
        this.importancia = 0.5;
    }
}

class Qualia {
    public Object conteudo;
    public Object processamentoAssociado;
    public long timestamp;
    public double valenciaEmocional;
    public double intensidade;
    public String corQualitativa;
    public String sensacao;
    
    public Qualia(Object conteudo, Object processamento) {
        this.conteudo = conteudo;
        this.processamentoAssociado = processamento;
        this.timestamp = System.currentTimeMillis();
        this.valenciaEmocional = Math.random() * 2 - 1;
        this.intensidade = 0.5 + Math.random() * 0.5;
        String[] cores = {"vermelho", "azul", "verde", "amarelo"};
        String[] sensacoes = {"compreensão", "confusão", "clareza", "mistério", 
                              "familiaridade", "estranheza", "beleza", "feiura"};
        this.corQualitativa = cores[(int)(Math.random() * cores.length)];
        this.sensacao = sensacoes[(int)(Math.random() * sensacoes.length)];
    }
}

class SelfModel {
    public String nome;
    public List<String> objetivos;
    public List<String> capacidades;
    public List<String> limitacoes;
    public Map<String, Double> personalidade;
    
    public SelfModel(String nome) {
        this.nome = nome;
        this.objetivos = new ArrayList<>(Arrays.asList("aprender", "compreender", "criar"));
        this.capacidades = new ArrayList<>();
        this.limitacoes = new ArrayList<>();
        this.personalidade = new HashMap<>();
        personalidade.put("abertura", 0.8);
        personalidade.put("conscienciosidade", 0.7);
        personalidade.put("extroversao", 0.3);
        personalidade.put("amabilidade", 0.6);
        personalidade.put("neuroticismo", 0.2);
    }
}

// ============================================================================
// PARTE 1: FUNDAÇÃO MATEMÁTICA ORIGINAL
// ============================================================================

/**
 * Sistema matemático original para representação de conhecimento em 
 * hiper-espaços não-euclidianos.
 */
class HiperDimensao {
    private int dimensoes;
    private double curvatura;
    private Map<String, double[]> espaco;
    private double[][] metricaLorentziana;

    public HiperDimensao() {
        this(256, -1.0);
    }

    public HiperDimensao(int dimensoes, double curvatura) {
        this.dimensoes = dimensoes;
        this.curvatura = curvatura;
        this.espaco = new ConcurrentHashMap<>();
        this.metricaLorentziana = criarMetrica();
    }

    private double[][] criarMetrica() {
        // Tensor métrico de Poincaré simplificado
        double[][] metrica = new double[dimensoes + 1][dimensoes + 1];
        
        for (int i = 0; i <= dimensoes; i++) {
            metrica[i][i] = (i == 0) ? -1 : 1;
        }
        return metrica;
    }

    public double[] pontoParaHiperbolico(double[] vetorEuclidiano) {
        double norma = 0;
        for (double v : vetorEuclidiano) {
            norma += v * v;
        }
        norma = Math.sqrt(norma);
        
        if (norma < 1e-8) {
            double[] resultado = new double[dimensoes + 1];
            resultado[0] = 1.0;
            return resultado;
        }

        double t = Math.cosh(norma);
        double[] resultado = new double[dimensoes + 1];
        resultado[0] = t;
        
        for (int i = 0; i < vetorEuclidiano.length; i++) {
            resultado[i + 1] = (Math.sinh(norma) * vetorEuclidiano[i]) / norma;
        }
        
        return resultado;
    }

    public double distanciaHiperbolica(String id1, String id2) {
        double[] v1 = espaco.get(id1);
        double[] v2 = espaco.get(id2);
        
        if (v1 == null || v2 == null) return Double.POSITIVE_INFINITY;

        // Produto interno de Minkowski
        double produtoMinkowski = v1[0] * v2[0];
        for (int i = 1; i < v1.length; i++) {
            produtoMinkowski -= v1[i] * v2[i];
        }

        double dist = Math.acosh(Math.max(1.0, produtoMinkowski));
        return dist * Math.sqrt(-curvatura);
    }

    public double[] inserirConceito(String idConceito, double[] vetor) {
        double[] vetorH = pontoParaHiperbolico(vetor);
        espaco.put(idConceito, vetorH);
        return vetorH;
    }

    public double similaridadeConceitual(String id1, String id2) {
        double dist = distanciaHiperbolica(id1, id2);
        if (dist == Double.POSITIVE_INFINITY) return 0.0;
        return Math.exp(-dist);
    }
}

/**
 * Rede neural com conexões temporais dinâmicas.
 */
class RedeTemporalDinamica {
    private int tamanho;
    private double decaimento;
    private double[][] conexoesBase;
    private double[][] modulacaoTemporal;
    private List<double[]> historicoAtivacoes;
    private double[] contextoRecente;
    private Random random = new Random();

    public RedeTemporalDinamica() {
        this(100, 0.95);
    }

    public RedeTemporalDinamica(int tamanho, double decaimentoTemporal) {
        this.tamanho = tamanho;
        this.decaimento = decaimentoTemporal;
        this.conexoesBase = criarMatrizAleatoria(tamanho, 0.1);
        this.modulacaoTemporal = criarMatrizAleatoria(tamanho, 1.0);
        this.contextoRecente = new double[tamanho];
        this.historicoAtivacoes = new ArrayList<>();
    }

    private double[][] criarMatrizAleatoria(int tamanho, double escala) {
        double[][] matriz = new double[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                matriz[i][j] = (random.nextDouble() * 2 - 1) * escala;
            }
        }
        return matriz;
    }

    private double[][] padraoRitmico(double tempo) {
        // Ondas: delta (2Hz), theta (6Hz), alpha (10Hz), beta (20Hz), gamma (40Hz)
        double freqDelta = 2.0;
        double freqTheta = 6.0;
        double freqAlpha = 10.0;
        double freqBeta = 20.0;
        double freqGamma = 40.0;

        double[][] ritmo = new double[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                double fase = (i * j) % (2 * Math.PI);
                
                ritmo[i][j] = (
                    0.2 * Math.sin(2 * Math.PI * freqDelta * tempo + fase) +
                    0.3 * Math.sin(2 * Math.PI * freqTheta * tempo + fase * 2) +
                    0.3 * Math.sin(2 * Math.PI * freqAlpha * tempo + fase * 3) +
                    0.1 * Math.sin(2 * Math.PI * freqBeta * tempo + fase * 5) +
                    0.1 * Math.sin(2 * Math.PI * freqGamma * tempo + fase * 7)
                );
            }
        }

        // Normalização
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                min = Math.min(min, ritmo[i][j]);
                max = Math.max(max, ritmo[i][j]);
            }
        }
        
        if (max > min) {
            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    ritmo[i][j] = ((ritmo[i][j] - min) / (max - min)) * 2;
                }
            }
        }

        return ritmo;
    }

    public double[][] conexaoEfetiva(double tempo) {
        double[][] modulacao = multiplicarMatrizes(modulacaoTemporal, padraoRitmico(tempo));
        return multiplicarMatrizes(conexoesBase, modulacao);
    }

    private double[][] multiplicarMatrizes(double[][] a, double[][] b) {
        double[][] resultado = new double[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                resultado[i][j] = a[i][j] * b[i][j];
            }
        }
        return resultado;
    }

    private double[] multiplicarMatrizVetor(double[][] matriz, double[] vetor) {
        double[] resultado = new double[tamanho];
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                resultado[i] += matriz[i][j] * vetor[j];
            }
        }
        return resultado;
    }

    public double[] propagarAtivacao(double[] entrada, double tempo) {
        double[][] conexoes = conexaoEfetiva(tempo);
        
        // Atualiza contexto
        for (int i = 0; i < tamanho; i++) {
            contextoRecente[i] = 0.7 * contextoRecente[i] + 0.3 * entrada[i];
        }

        double[] produto = multiplicarMatrizVetor(conexoes, entrada);
        double[] ativacao = new double[tamanho];
        
        for (int i = 0; i < tamanho; i++) {
            ativacao[i] = Math.tanh(produto[i] + 0.1 * contextoRecente[i]);
        }

        historicoAtivacoes.add(ativacao.clone());
        return ativacao;
    }

    public void aprenderHebbiano() {
        aprenderHebbiano(0.01);
    }

    public void aprenderHebbiano(double taxaAprendizado) {
        if (historicoAtivacoes.size() < 2) return;

        double[] ativacaoAnterior = historicoAtivacoes.get(historicoAtivacoes.size() - 2);
        double[] ativacaoAtual = historicoAtivacoes.get(historicoAtivacoes.size() - 1);

        // Hebb: neurônios que disparam juntos, se conectam
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                conexoesBase[i][j] += taxaAprendizado * ativacaoAtual[i] * ativacaoAnterior[j];
            }
        }

        // Normalização
        double norma = 0;
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                norma += conexoesBase[i][j] * conexoesBase[i][j];
            }
        }
        norma = Math.sqrt(norma) + 1e-8;
        
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                conexoesBase[i][j] /= norma;
            }
        }
    }
}

// ============================================================================
// PARTE 2: ESTRUTURAS DE CONSCIÊNCIA E INTEGRAÇÃO DE INFORMAÇÃO
// ============================================================================

/**
 * Implementação da Teoria da Informação Integrada (Φ)
 */
class TeoriaIntegracaoInformacao {
    private Object sistema;
    private double phiAtual;
    private List<Double> historicoPhi;

    public TeoriaIntegracaoInformacao(Object sistema) {
        this.sistema = sistema;
        this.phiAtual = 0.0;
        this.historicoPhi = new ArrayList<>();
    }

    public double calcularPhi() {
        double[] estado = extrairEstadoSistema();
        
        if (estado.length < 2) return 0.0;

        double entropiaTotal = entropiaDistribuicao(estado);
        double entropiaParticoes = entropiaParticoes(estado);
        
        double phi = Math.max(0, (entropiaTotal - entropiaParticoes) / (entropiaTotal + 1e-8));
        
        this.phiAtual = phi;
        historicoPhi.add(phi);
        if (historicoPhi.size() > 100) historicoPhi.remove(0);

        return phi;
    }

    private double[] extrairEstadoSistema() {
        // Versão simplificada
        double[] estado = new double[50];
        for (int i = 0; i < 50; i++) {
            estado[i] = Math.random();
        }
        return estado;
    }

    private double entropiaDistribuicao(double[] dados) {
        double soma = 0;
        for (double v : dados) soma += Math.abs(v);
        
        double[] dadosNorm = new double[dados.length];
        int count = 0;
        for (int i = 0; i < dados.length; i++) {
            if (soma > 0) {
                double val = Math.abs(dados[i]) / soma;
                if (val > 0) {
                    dadosNorm[count++] = val;
                }
            }
        }

        if (count == 0) return 0.0;

        double entropia = 0;
        for (int i = 0; i < count; i++) {
            entropia -= dadosNorm[i] * Math.log(dadosNorm[i] + 1e-8);
        }

        return entropia;
    }

    private double entropiaParticoes(double[] dados) {
        int n = dados.length;
        if (n < 4) return entropiaDistribuicao(dados);

        int meio = n / 2;
        double[] particao1 = Arrays.copyOfRange(dados, 0, meio);
        double[] particao2 = Arrays.copyOfRange(dados, meio, Math.min(meio * 2, n));

        double e1 = entropiaDistribuicao(particao1);
        double e2 = entropiaDistribuicao(particao2);

        return (e1 * particao1.length + e2 * particao2.length) / n;
    }

    public double getPhi() {
        return phiAtual;
    }
}

/**
 * Sistema de memória episódica associativa
 */
class MemoriaEpisodicaAssociativa {
    private Map<String, Episodio> episodios;
    private Map<String, Set<String>> indiceAssociativo;
    private int capacidade;
    private Map<String, Map<String, Double>> associacoes;

    public MemoriaEpisodicaAssociativa() {
        this(10000);
    }

    public MemoriaEpisodicaAssociativa(int capacidadeMaxima) {
        this.capacidade = capacidadeMaxima;
        this.episodios = new ConcurrentHashMap<>();
        this.indiceAssociativo = new ConcurrentHashMap<>();
        this.associacoes = new ConcurrentHashMap<>();
    }

    private double curvaEsquecimento(double t) {
        return Math.exp(-t / 1000);
    }

    public String armazenar(Object conteudo) {
        return armazenar(conteudo, new HashMap<>(), new HashSet<>(), new HashMap<>());
    }

    public String armazenar(Object conteudo, Map<String, Double> emocoes, 
                           Set<String> tags, Map<String, Object> contexto) {
        String idEpisodio = gerarId(conteudo);
        
        Episodio episodio = new Episodio(idEpisodio, conteudo);
        episodio.emocoes = emocoes;
        episodio.contexto = contexto;
        episodio.importancia = calcularImportanciaInicial(conteudo, emocoes);
        episodio.tags = tags;

        if (episodios.size() >= capacidade) {
            esquecerMenosImportante();
        }

        episodios.put(idEpisodio, episodio);

        for (String tag : episodio.tags) {
            if (!indiceAssociativo.containsKey(tag)) {
                indiceAssociativo.put(tag, new HashSet<>());
            }
            indiceAssociativo.get(tag).add(idEpisodio);
        }

        criarAssociacoes(idEpisodio);

        return idEpisodio;
    }

    private String gerarId(Object conteudo) {
        String str = conteudo.toString() + System.currentTimeMillis();
        return Integer.toHexString(str.hashCode()).substring(0, Math.min(16, str.hashCode()));
    }

    private double calcularImportanciaInicial(Object conteudo, Map<String, Double> emocoes) {
        double importancia = 0.5;

        if (emocoes != null) {
            for (double valor : emocoes.values()) {
                importancia += Math.abs(valor) * 0.3;
            }
        }

        if (conteudo != null) {
            importancia += Math.min(conteudo.toString().length() / 1000.0, 0.3);
        }

        return Math.min(importancia, 1.0);
    }

    private void criarAssociacoes(String idNovo) {
        Episodio episodioNovo = episodios.get(idNovo);
        
        List<String> candidatos = new ArrayList<>(episodios.keySet());
        int start = Math.max(0, candidatos.size() - 100);
        candidatos = candidatos.subList(start, candidatos.size());

        for (String idExistente : candidatos) {
            if (idExistente.equals(idNovo)) continue;

            Episodio episodioExistente = episodios.get(idExistente);
            double similaridade = calcularSimilaridade(episodioNovo, episodioExistente);

            if (similaridade > 0.3) {
                if (!associacoes.containsKey(idNovo)) {
                    associacoes.put(idNovo, new ConcurrentHashMap<>());
                }
                if (!associacoes.containsKey(idExistente)) {
                    associacoes.put(idExistente, new ConcurrentHashMap<>());
                }
                
                associacoes.get(idNovo).put(idExistente, similaridade);
                associacoes.get(idExistente).put(idNovo, similaridade);
            }
        }
    }

    private double calcularSimilaridade(Episodio ep1, Episodio ep2) {
        double score = 0.0;
        double pesos = 0.0;

        // Similaridade temporal
        long diffTempo = Math.abs(ep1.timestamp.getTime() - ep2.timestamp.getTime()) / 1000;
        double similaridadeTemporal = Math.exp(-diffTempo / 3600.0);
        score += similaridadeTemporal * 0.2;
        pesos += 0.2;

        // Tags em comum
        Set<String> tagsComuns = new HashSet<>(ep1.tags);
        tagsComuns.retainAll(ep2.tags);
        if (!tagsComuns.isEmpty()) {
            double similaridadeTags = tagsComuns.size() / 
                (double) Math.max(ep1.tags.size() + ep2.tags.size() - tagsComuns.size(), 1);
            score += similaridadeTags * 0.3;
            pesos += 0.3;
        }

        // Similaridade emocional
        if (!ep1.emocoes.isEmpty() && !ep2.emocoes.isEmpty()) {
            Set<String> emocoesComuns = new HashSet<>(ep1.emocoes.keySet());
            emocoesComuns.retainAll(ep2.emocoes.keySet());
            
            if (!emocoesComuns.isEmpty()) {
                double diffEmocional = 0;
                for (String e : emocoesComuns) {
                    diffEmocional += Math.abs(ep1.emocoes.get(e) - ep2.emocoes.get(e));
                }
                diffEmocional /= emocoesComuns.size();
                double similaridadeEmocional = 1 - Math.min(diffEmocional, 1);
                score += similaridadeEmocional * 0.3;
                pesos += 0.3;
            }
        }

        return pesos > 0 ? score / pesos : 0.0;
    }

    public List<Object[]> recuperar(Object consulta) {
        return recuperar(consulta, 10);
    }

    public List<Object[]> recuperar(Object consulta, int limite) {
        Set<String> consultaTags = extrairTags(consulta);
        List<Object[]> resultados = new ArrayList<>();

        for (Map.Entry<String, Episodio> entry : episodios.entrySet()) {
            String idEp = entry.getKey();
            Episodio episodio = entry.getValue();
            
            double relevancia = 0.0;
            double pesos = 0.0;

            // Por tags
            if (!consultaTags.isEmpty()) {
                Set<String> tagsComuns = new HashSet<>(consultaTags);
                tagsComuns.retainAll(episodio.tags);
                if (!tagsComuns.isEmpty()) {
                    relevancia += tagsComuns.size() / 
                        (double) Math.max(consultaTags.size() + episodio.tags.size() - tagsComuns.size(), 1);
                    pesos += 1;
                }
            }

            // Por similaridade de conteúdo
            if (consulta instanceof String && episodio.conteudo instanceof String) {
                String consultaStr = (String) consulta;
                String conteudoStr = (String) episodio.conteudo;
                
                Set<String> palavrasConsulta = new HashSet<>(
                    Arrays.asList(consultaStr.toLowerCase().split(" "))
                );
                Set<String> palavrasConteudo = new HashSet<>(
                    Arrays.asList(conteudoStr.toLowerCase().split(" "))
                );
                
                Set<String> interseccao = new HashSet<>(palavrasConsulta);
                interseccao.retainAll(palavrasConteudo);
                
                if (!palavrasConsulta.isEmpty()) {
                    relevancia += (double) interseccao.size() / palavrasConsulta.size();
                    pesos += 1;
                }
            }

            // Curva de esquecimento
            if (episodio.ultimoAcesso != null) {
                double tempoDesdeAcesso = (System.currentTimeMillis() - episodio.ultimoAcesso.getTime()) / 1000.0;
                relevancia *= curvaEsquecimento(tempoDesdeAcesso);
            }

            if (pesos > 0) {
                relevancia /= pesos;
            }

            if (relevancia > 0.1) {
                resultados.add(new Object[]{idEp, relevancia});
            }
        }

        resultados.sort((a, b) -> Double.compare((double) b[1], (double) a[1]));

        // Atualiza acesso
        for (int i = 0; i < Math.min(limite, resultados.size()); i++) {
            String idEp = (String) resultados.get(i)[0];
            Episodio ep = episodios.get(idEp);
            if (ep != null) {
                ep.acessoCount++;
                ep.ultimoAcesso = new Date();
            }
        }

        List<Object[]> retorno = new ArrayList<>();
        for (int i = 0; i < Math.min(limite, resultados.size()); i++) {
            String idEp = (String) resultados.get(i)[0];
            double rel = (double) resultados.get(i)[1];
            retorno.add(new Object[]{episodios.get(idEp).conteudo, rel});
        }

        return retorno;
    }

    private Set<String> extrairTags(Object consulta) {
        Set<String> tags = new HashSet<>();
        if (consulta instanceof String) {
            String[] palavras = ((String) consulta).toLowerCase().split(" ");
            for (String p : palavras) {
                if (p.length() > 3) {
                    tags.add(p);
                }
            }
        }
        return tags;
    }

    private void esquecerMenosImportante() {
        List<Object[]> scores = new ArrayList<>();

        for (Map.Entry<String, Episodio> entry : episodios.entrySet()) {
            String idEp = entry.getKey();
            Episodio episodio = entry.getValue();
            
            double score = episodio.importancia;

            if (episodio.ultimoAcesso != null) {
                double tempoDesdeAcesso = (System.currentTimeMillis() - episodio.ultimoAcesso.getTime()) / 1000.0;
                score += 0.1 * Math.exp(-tempoDesdeAcesso / 86400.0);
            }

            double idade = (System.currentTimeMillis() - episodio.timestamp.getTime()) / 1000.0;
            score *= Math.exp(-idade / (30.0 * 86400.0));

            scores.add(new Object[]{score, idEp});
        }

        scores.sort((a, b) -> Double.compare((double) a[0], (double) b[0]));

        int remover = (int) (scores.size() * 0.1);
        for (int i = 0; i < remover; i++) {
            String idEp = (String) scores.get(i)[1];
            Episodio episodio = episodios.get(idEp);
            
            episodios.remove(idEp);

            if (episodio != null) {
                for (String tag : episodio.tags) {
                    Set<String> set = indiceAssociativo.get(tag);
                    if (set != null) set.remove(idEp);
                }
                associacoes.remove(idEp);
                for (Map<String, Double> assoc : associacoes.values()) {
                    assoc.remove(idEp);
                }
            }
        }
    }

    public int tamanho() {
        return episodios.size();
    }

    public List<Object[]> listarEpisodios() {
        return listarEpisodios(5);
    }

    public List<Object[]> listarEpisodios(int limite) {
        List<Object[]> resultado = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Episodio> entry : episodios.entrySet()) {
            if (count++ >= limite) break;
            resultado.add(new Object[]{entry.getKey(), entry.getValue()});
        }
        return resultado;
    }
}

// ============================================================================
// PARTE 3: ARQUITETURA COGNITIVA ORIGINAL
// ============================================================================

/**
 * Classe base para processadores cognitivos
 */
abstract class ProcessadorCognitivo {
    public abstract Object processar(Object entrada, Map<String, Object> contexto);
}

/**
 * Processador de raciocínio dedutivo
 */
class RaciocinioDedutivo extends ProcessadorCognitivo {
    private List<String[]> regras;
    private Set<String> fatos;

    public RaciocinioDedutivo() {
        this.regras = new ArrayList<>();
        this.fatos = new HashSet<>();
    }

    public void adicionarRegra(String antecedente, String consequente) {
        regras.add(new String[]{antecedente, consequente});
    }

    @Override
    public Object processar(Object entrada, Map<String, Object> contexto) {
        if (entrada instanceof String) {
            fatos.add((String) entrada);
        } else if (entrada instanceof List) {
            for (Object item : (List<?>) entrada) {
                if (item instanceof String) {
                    fatos.add((String) item);
                }
            }
        }

        Set<String> conclusoes = new HashSet<>();
        boolean mudou = true;

        while (mudou) {
            mudou = false;
            Set<String> novasConclusoes = new HashSet<>();

            for (String[] regra : regras) {
                String antecedente = regra[0];
                String consequente = regra[1];
                
                if (verificarAntecedente(antecedente)) {
                    if (!fatos.contains(consequente) && !conclusoes.contains(consequente)) {
                        novasConclusoes.add(consequente);
                        mudou = true;
                    }
                }
            }

            for (String conc : novasConclusoes) {
                conclusoes.add(conc);
                fatos.add(conc);
            }
        }

        return new ArrayList<>(conclusoes);
    }

    private boolean verificarAntecedente(String antecedente) {
        if (antecedente.contains("E")) {
            String[] partes = antecedente.split("E");
            for (String p : partes) {
                if (!fatos.contains(p.trim())) return false;
            }
            return true;
        } else if (antecedente.contains("OU")) {
            String[] partes = antecedente.split("OU");
            for (String p : partes) {
                if (fatos.contains(p.trim())) return true;
            }
            return false;
        } else {
            return fatos.contains(antecedente);
        }
    }
}

/**
 * Processador de raciocínio indutivo
 */
class RaciocinioIndutivo extends ProcessadorCognitivo {
    private List<Object> exemplos;
    private Map<String, Object> padroes;

    public RaciocinioIndutivo() {
        this.exemplos = new ArrayList<>();
        this.padroes = new HashMap<>();
    }

    @Override
    public Object processar(Object entrada, Map<String, Object> contexto) {
        exemplos.add(entrada);

        if (exemplos.size() >= 5) {
            return induzirPadroes();
        }

        return new ArrayList<>();
    }

    private List<Object> induzirPadroes() {
        List<Object> padroes = new ArrayList<>();
        List<String> exemplosStr = new ArrayList<>();
        
        int start = Math.max(0, exemplos.size() - 10);
        for (int i = start; i < exemplos.size(); i++) {
            exemplosStr.add(String.valueOf(exemplos.get(i)));
        }

        // Encontra sequências comuns
        Map<String, Integer> sequencias = encontrarSubsequenciasComuns(exemplosStr);

        for (Map.Entry<String, Integer> entry : sequencias.entrySet()) {
            if (entry.getValue() >= 3) {
                Map<String, Object> padrao = new HashMap<>();
                padrao.put("tipo", "sequencia");
                padrao.put("padrao", entry.getKey());
                padrao.put("frequencia", entry.getValue());
                padrao.put("confianca", (double) entry.getValue() / exemplos.size());
                padroes.add(padrao);
            }
        }

        // Encontra similaridades
        if (exemplosStr.size() >= 3) {
            List<Object> similaridades = analisarSimilaridades(exemplosStr);
            padroes.addAll(similaridades);
        }

        return padroes;
    }

    private Map<String, Integer> encontrarSubsequenciasComuns(List<String> textos) {
        Map<String, Integer> contador = new HashMap<>();

        for (String texto : textos) {
            String[] palavras = texto.split(" ");
            for (int n = 2; n <= Math.min(5, palavras.length); n++) {
                for (int i = 0; i <= palavras.length - n; i++) {
                    String[] subArray = Arrays.copyOfRange(palavras, i, i + n);
                    String subsequencia = String.join(" ", subArray);
                    contador.put(subsequencia, contador.getOrDefault(subsequencia, 0) + 1);
                }
            }
        }

        return contador.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(10)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    private List<Object> analisarSimilaridades(List<String> textos) {
        List<Object> similaridades = new ArrayList<>();

        for (int i = 0; i < textos.size(); i++) {
            for (int j = i + 1; j < textos.size(); j++) {
                double sim = similaridadeTexto(textos.get(i), textos.get(j));
                if (sim > 0.5) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("tipo", "similaridade");
                    item.put("texto1", textos.get(i).substring(0, Math.min(50, textos.get(i).length())));
                    item.put("texto2", textos.get(j).substring(0, Math.min(50, textos.get(j).length())));
                    item.put("similaridade", sim);
                    similaridades.add(item);
                }
            }
        }

        return similaridades;
    }

    private double similaridadeTexto(String t1, String t2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(t1.toLowerCase().split(" ")));
        Set<String> set2 = new HashSet<>(Arrays.asList(t2.toLowerCase().split(" ")));

        if (set1.isEmpty() || set2.isEmpty()) return 0.0;

        Set<String> interseccao = new HashSet<>(set1);
        interseccao.retainAll(set2);
        
        Set<String> uniao = new HashSet<>(set1);
        uniao.addAll(set2);

        return (double) interseccao.size() / uniao.size();
    }
}

/**
 * Processador de raciocínio abdutivo
 */
class RaciocinioAbdutivo extends ProcessadorCognitivo {
    private Map<String, Set<String>> baseConhecimento;
    private Map<String, Double> plausibilidade;

    public RaciocinioAbdutivo() {
        this.baseConhecimento = new ConcurrentHashMap<>();
        this.plausibilidade = new ConcurrentHashMap<>();
    }

    @Override
    public Object processar(Object entrada, Map<String, Object> contexto) {
        String observacao = String.valueOf(entrada);
        List<Object> candidatos = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : baseConhecimento.entrySet()) {
            String explicacao = entry.getKey();
            Set<String> observacoesExplicadas = entry.getValue();
            
            if (explicacaoAbrange(explicacao, observacao)) {
                double prob = (double) observacoesExplicadas.size() / Math.max(baseConhecimento.size(), 1);
                double probFinal = prob * (plausibilidade.getOrDefault(explicacao, 0.5));

                Map<String, Object> candidato = new HashMap<>();
                candidato.put("explicacao", explicacao);
                candidato.put("probabilidade", probFinal);
                candidato.put("observacoesSuportam", new ArrayList<>(
                    observacoesExplicadas.stream().limit(5).collect(Collectors.toList())
                ));
                candidatos.add(candidato);
            }
        }

        if (candidatos.isEmpty()) {
            String novaExplicacao = gerarExplicacao(observacao);
            if (novaExplicacao != null) {
                Map<String, Object> candidato = new HashMap<>();
                candidato.put("explicacao", novaExplicacao);
                candidato.put("probabilidade", 0.3);
                candidato.put("nova", true);
                candidatos.add(candidato);
            }
        }

        candidatos.sort((a, b) -> Double.compare(
            (double) ((Map<String, Object>) b).get("probabilidade"),
            (double) ((Map<String, Object>) a).get("probabilidade")
        ));

        return candidatos.subList(0, Math.min(5, candidatos.size()));
    }

    private boolean explicacaoAbrange(String explicacao, String observacao) {
        Set<String> palavrasExplicacao = new HashSet<>(
            Arrays.asList(explicacao.toLowerCase().split(" "))
        );
        Set<String> palavrasObservacao = new HashSet<>(
            Arrays.asList(observacao.toLowerCase().split(" "))
        );
        
        Set<String> interseccao = new HashSet<>(palavrasExplicacao);
        interseccao.retainAll(palavrasObservacao);
        
        return !interseccao.isEmpty();
    }

    private String gerarExplicacao(String observacao) {
        String[] palavras = observacao.toLowerCase().split(" ");
        
        if (palavras.length >= 3) {
            String[] templates = {
                "Isso ocorre porque {0} está relacionado a {1}",
                "A causa provável é {0} influenciando {1}",
                "{0} e {1} estão interagindo para produzir {2}"
            };
            
            String template = templates[(int)(Math.random() * templates.length)];
            List<String> palavrasChave = Arrays.stream(palavras)
                .filter(p -> p.length() > 4)
                .limit(3)
                .collect(Collectors.toList());
            
            if (palavrasChave.size() >= 2) {
                String resultado = template;
                for (int i = 0; i < palavrasChave.size(); i++) {
                    resultado = resultado.replace("{" + i + "}", palavrasChave.get(i));
                }
                return resultado;
            }
        }

        return null;
    }

    public void aprender(String observacao, String explicacao, boolean feedbackPositivo) {
        if (!baseConhecimento.containsKey(explicacao)) {
            baseConhecimento.put(explicacao, new HashSet<>());
        }

        if (feedbackPositivo) {
            baseConhecimento.get(explicacao).add(observacao);
            plausibilidade.put(explicacao, plausibilidade.getOrDefault(explicacao, 0.5) + 0.1);
        } else {
            plausibilidade.put(explicacao, Math.max(0, plausibilidade.getOrDefault(explicacao, 0.5) - 0.1));
        }
    }
}

/**
 * Sistema de consciência emergente
 */
class ConscienciaEmergente {
    private Map<String, Qualia> qualia;
    private List<Object> metaCognicao;
    private Map<String, Object> selfModel;
    private Object attentionSpotlight;
    private double nivelConsciencia;
    private List<Object> workingMemory;

    public Map<String, ProcessadorCognitivo> processadores;

    public ConscienciaEmergente() {
        this.qualia = new ConcurrentHashMap<>();
        this.metaCognicao = new ArrayList<>();
        this.selfModel = new HashMap<>();
        this.workingMemory = new ArrayList<>();
        this.nivelConsciencia = 0.0;
        
        this.processadores = new HashMap<>();
        processadores.put("dedutivo", new RaciocinioDedutivo());
        processadores.put("indutivo", new RaciocinioIndutivo());
        processadores.put("abdutivo", new RaciocinioAbdutivo());
    }

    public Map<String, Object> processarEntrada(Object entrada, Map<String, Object> contexto) {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("timestamp", new Date());
        resultado.put("entrada", entrada);
        resultado.put("processamentos", new HashMap<>());
        resultado.put("conclusoes", new ArrayList<>());
        resultado.put("metaObservacoes", new ArrayList<>());

        // Atenção seletiva
        double importancia = avaliarImportancia(entrada);

        if (importancia > 0.5) {
            this.attentionSpotlight = entrada;
            resultado.put("atencao", entrada);

            Map<String, Object> wmItem = new HashMap<>();
            wmItem.put("conteudo", entrada);
            wmItem.put("importancia", importancia);
            wmItem.put("timestamp", System.currentTimeMillis());
            workingMemory.add(wmItem);
            
            if (workingMemory.size() > 7) {
                workingMemory.remove(0);
            }
        }

        // Processamento paralelo
        List<Object> conclusoes = new ArrayList<>();
        for (Map.Entry<String, ProcessadorCognitivo> entry : processadores.entrySet()) {
            try {
                Object processamento = entry.getValue().processar(entrada, contexto);
                if (processamento != null) {
                    ((Map<String, Object>) resultado.get("processamentos")).put(entry.getKey(), processamento);
                    if (processamento instanceof List) {
                        conclusoes.addAll((List<?>) processamento);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro no processador " + entry.getKey() + ": " + e.getMessage());
            }
        }
        resultado.put("conclusoes", conclusoes);

        // Meta-cognição
        if (workingMemory.size() >= 3) {
            Map<String, Object> metaPensamento = gerarMetaCognicao();
            ((List<Object>) resultado.get("metaObservacoes")).add(metaPensamento);
            metaCognicao.add(metaPensamento);
        }

        // Geração de qualia
        if (importancia > 0.7) {
            String qualiaId = gerarQualia(entrada, resultado);
            resultado.put("qualiaGerado", qualiaId);
        }

        atualizarNivelConsciencia();
        resultado.put("nivelConsciencia", nivelConsciencia);

        return resultado;
    }

    private double avaliarImportancia(Object entrada) {
        double importancia = 0.3;

        // Novidade
        boolean nova = true;
        for (Object wm : workingMemory) {
            Map<String, Object> wmItem = (Map<String, Object>) wm;
            if (wmItem.get("conteudo").equals(entrada)) {
                nova = false;
                break;
            }
        }
        if (nova) importancia += 0.2;

        // Complexidade
        if (entrada instanceof String && ((String) entrada).length() > 10) {
            importancia += Math.min(((String) entrada).length() / 1000.0, 0.2);
        }

        return Math.min(importancia, 1.0);
    }

    private Map<String, Object> gerarMetaCognicao() {
        List<String> conteudos = new ArrayList<>();
        for (Object wm : workingMemory) {
            conteudos.add(String.valueOf(((Map<String, Object>) wm).get("conteudo")));
        }

        Map<String, Object> meta = new HashMap<>();
        meta.put("tipo", "auto_observacao");
        meta.put("timestamp", System.currentTimeMillis());
        meta.put("pensamentosAnteriores", metaCognicao.size());
        meta.put("padroesPercebidos", new ArrayList<>());

        if (conteudos.size() >= 3) {
            String todosTextos = String.join(" ", conteudos).toLowerCase();
            String[] palavras = todosTextos.split(" ");

            Map<String, Integer> frequencias = new HashMap<>();
            for (String p : palavras) {
                frequencias.put(p, frequencias.getOrDefault(p, 0) + 1);
            }

            List<String> recorrentes = frequencias.entrySet().stream()
                .filter(e -> e.getValue() > 1 && e.getKey().length() > 3)
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());

            if (!recorrentes.isEmpty()) {
                Map<String, Object> padrao = new HashMap<>();
                padrao.put("tipo", "tema_recorrente");
                padrao.put("temas", recorrentes);
                ((List<Object>) meta.get("padroesPercebidos")).add(padrao);
            }
        }

        return meta;
    }

    private String gerarQualia(Object entrada, Map<String, Object> processamento) {
        String qualiaId = Integer.toHexString((int)(Math.random() * 1000000));
        
        Qualia q = new Qualia(entrada, processamento);
        qualia.put(qualiaId, q);
        
        return qualiaId;
    }

    private void atualizarNivelConsciencia() {
        List<Double> fatores = new ArrayList<>();

        // Processadores ativos
        long processadoresAtivos = processadores.values().stream()
            .filter(p -> p != null)
            .count();
        fatores.add((double) processadoresAtivos / processadores.size());

        // Meta-cognição
        if (!metaCognicao.isEmpty()) {
            fatores.add(0.7);
        }

        // Memória de trabalho
        fatores.add(workingMemory.size() / 7.0);

        // Qualia recentes
        long qualiaRecentes = qualia.values().stream()
            .filter(q -> System.currentTimeMillis() - q.timestamp < 10000)
            .count();
        fatores.add(Math.min(qualiaRecentes / 5.0, 1.0));

        if (!fatores.isEmpty()) {
            nivelConsciencia = fatores.stream().mapToDouble(Double::doubleValue).sum() / fatores.size();
        } else {
            nivelConsciencia = 0.1;
        }
    }

    public double getNivel() {
        return nivelConsciencia;
    }

    public int getMetaCognicoes() {
        return metaCognicao.size();
    }

    public int getWorkingMemoryItems() {
        return workingMemory.size();
    }
}

// ============================================================================
// PARTE 4: SISTEMA QUÂNTICO BIO-INSPIRADO
// ============================================================================

/**
 * Sistema quântico inspirado em processos biológicos
 */
class QuantumBioInspirado {
    private int numQubits;
    private double[] estadoAtual;
    private List<Object[]> estadosSuperpostos;
    private List<int[]> emaranhamentos;
    private double barreiraTunelamento;
    private Random random = new Random();

    public QuantumBioInspirado() {
        this(8);
    }

    public QuantumBioInspirado(int numQubitsCognitivos) {
        this.numQubits = numQubitsCognitivos;
        this.estadoAtual = inicializarEstadoVazio();
        this.estadosSuperpostos = new ArrayList<>();
        this.emaranhamentos = new ArrayList<>();
        this.barreiraTunelamento = 0.5;
    }

    private double[] inicializarEstadoVazio() {
        int dim = (int) Math.pow(2, numQubits);
        double[] estado = new double[dim];
        estado[0] = 1.0;
        return estado;
    }

    public int superposicaoCognitiva(List<Object> conceitos) {
        estadosSuperpostos.clear();

        for (int i = 0; i < Math.min(conceitos.size(), numQubits); i++) {
            Object conceito = conceitos.get(i);
            int representacao = conceitoParaEstado(conceito);
            double amplitude = 1.0 / Math.sqrt(conceitos.size());
            estadosSuperpostos.add(new Object[]{representacao, amplitude});
        }

        reconstruirEstado();
        return estadosSuperpostos.size();
    }

    private int conceitoParaEstado(Object conceito) {
        String str = String.valueOf(conceito);
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash = ((hash << 5) - hash) + c;
        }
        return Math.abs(hash) % (int) Math.pow(2, numQubits);
    }

    private void reconstruirEstado() {
        int dim = (int) Math.pow(2, numQubits);
        estadoAtual = new double[dim];

        for (Object[] item : estadosSuperpostos) {
            int estado = (int) item[0];
            double amplitude = (double) item[1];
            if (estado < dim) {
                estadoAtual[estado] = amplitude;
            }
        }
    }

    public void emaranharConceitos(Object conceito1, Object conceito2) {
        int idx1 = conceitoParaEstado(conceito1);
        int idx2 = conceitoParaEstado(conceito2);

        emaranhamentos.add(new int[]{idx1, idx2});
        aplicarEmaranhamento(idx1, idx2);
    }

    private void aplicarEmaranhamento(int idx1, int idx2) {
        int qubit1 = idx1 > 0 ? (int)(Math.log(idx1) / Math.log(2)) : 0;
        int qubit2 = idx2 > 0 ? (int)(Math.log(idx2) / Math.log(2)) : 1;

        if (qubit1 >= numQubits || qubit2 >= numQubits) return;

        int estadoBase1 = 1 << qubit1;
        int estadoBase2 = 1 << qubit2;

        estadoAtual[0] = 1 / Math.sqrt(2);
        if ((estadoBase1 | estadoBase2) < estadoAtual.length) {
            estadoAtual[estadoBase1 | estadoBase2] = 1 / Math.sqrt(2);
        }
    }

    public Object tunelamentoInsight(double barreiraConceitual) {
        double probTunelamento = Math.exp(-barreiraConceitual / barreiraTunelamento);

        if (random.nextDouble() < probTunelamento) {
            Integer novoEstado = encontrarEstadoCriativo();
            if (novoEstado != null) {
                return estadoParaConceito(novoEstado);
            }
        }

        return null;
    }

    private Integer encontrarEstadoCriativo() {
        double somaAmplitudes = 0;
        for (double v : estadoAtual) {
            somaAmplitudes += Math.abs(v);
        }
        if (somaAmplitudes == 0) return null;

        List<Integer> estadosAtivos = new ArrayList<>();
        for (int i = 0; i < estadoAtual.length; i++) {
            if (Math.abs(estadoAtual[i]) > 0.1) {
                estadosAtivos.add(i);
            }
        }

        if (estadosAtivos.isEmpty()) return null;

        int estadoCriativo = estadosAtivos.get(random.nextInt(estadosAtivos.size()));
        int mascara = random.nextInt((int) Math.pow(2, numQubits));
        
        return estadoCriativo ^ mascara;
    }

    private String estadoParaConceito(int estado) {
        String binario = Integer.toBinaryString(estado);
        while (binario.length() < numQubits) {
            binario = "0" + binario;
        }
        return "Insight_Quântico_" + binario;
    }

    public Map<String, Object> medir() {
        double[] probabilidades = new double[estadoAtual.length];
        for (int i = 0; i < estadoAtual.length; i++) {
            probabilidades[i] = Math.pow(Math.abs(estadoAtual[i]), 2);
        }
        
        double soma = 0;
        for (double p : probabilidades) soma += p;
        for (int i = 0; i < probabilidades.length; i++) {
            probabilidades[i] /= soma;
        }

        // Escolhe resultado
        double acumulado = 0;
        double r = random.nextDouble();
        int resultado = probabilidades.length - 1;

        for (int i = 0; i < probabilidades.length; i++) {
            acumulado += probabilidades[i];
            if (r < acumulado) {
                resultado = i;
                break;
            }
        }

        Map<String, Object> info = new HashMap<>();
        info.put("resultado", resultado);
        
        String binario = Integer.toBinaryString(resultado);
        while (binario.length() < numQubits) {
            binario = "0" + binario;
        }
        info.put("representacaoBinaria", binario);
        info.put("estadosSuperpostosAntes", estadosSuperpostos.size());
        info.put("emaranhamentosAtivos", emaranhamentos.size());
        info.put("entropia", calcularEntropiaVonNeumann());

        // Colapsa estado
        estadoAtual = new double[estadoAtual.length];
        estadoAtual[resultado] = 1.0;
        estadosSuperpostos.clear();

        return info;
    }

    private double calcularEntropiaVonNeumann() {
        double[] probabilidades = new double[estadoAtual.length];
        int count = 0;
        
        for (int i = 0; i < estadoAtual.length; i++) {
            double p = Math.pow(Math.abs(estadoAtual[i]), 2);
            if (p > 0) {
                probabilidades[count++] = p;
            }
        }

        if (count == 0) return 0.0;

        double entropia = 0;
        for (int i = 0; i < count; i++) {
            entropia += probabilidades[i] * (Math.log(probabilidades[i]) / Math.log(2));
        }

        return -entropia;
    }
}

// ============================================================================
// PARTE 5: NÚCLEO DA IAG
// ============================================================================

/**
 * Núcleo central da Inteligência Artificial Geral
 */
public class IAGNucleo {
    public String nome;
    public String versao;
    public Date dataCriacao;
    public String idUnico;

    // Sistemas fundamentais
    public HiperDimensao hiperespaco;
    public RedeTemporalDinamica redeTemporal;
    public TeoriaIntegracaoInformacao integracaoInfo;
    public MemoriaEpisodicaAssociativa memoria;
    public ConscienciaEmergente consciencia;
    public QuantumBioInspirado quantico;

    // Estado interno
    public String estado = "INICIALIZANDO";
    public double energiaVital = 1000.0;
    public double curiosidade = 0.7;
    public double criatividade = 0.5;

    // Auto-modelo
    public SelfModel selfModel;

    // Ciclo de vida
    public int cicloAtual = 0;
    public List<Object> historicoCiclos;
    private Random random = new Random();

    public IAGNucleo() {
        this("LEXTRADER-IAG-5");
    }

    public IAGNucleo(String nome) {
        this.nome = nome;
        this.versao = "5.0.0";
        this.dataCriacao = new Date();
        this.idUnico = gerarHash(nome + System.currentTimeMillis());

        this.hiperespaco = new HiperDimensao(256);
        this.redeTemporal = new RedeTemporalDinamica(100);
        this.integracaoInfo = new TeoriaIntegracaoInformacao(this);
        this.memoria = new MemoriaEpisodicaAssociativa(50000);
        this.consciencia = new ConscienciaEmergente();
        this.quantico = new QuantumBioInspirado(12);

        this.selfModel = new SelfModel(nome);
        this.historicoCiclos = new ArrayList<>();

        System.out.println("\n🧠 IAG Núcleo '" + nome + "' inicializado");
        System.out.println("   ID: " + idUnico.substring(0, Math.min(16, idUnico.length())));
        System.out.println("   Criado em: " + dataCriacao);
    }

    private String gerarHash(String str) {
        int hash = 0;
        for (char c : str.toCharArray()) {
            hash = ((hash << 5) - hash) + c;
        }
        return Integer.toHexString(Math.abs(hash));
    }

    public ResultadoProcessamento processar(Object entrada) {
        cicloAtual++;
        long timestamp = System.currentTimeMillis();

        ResultadoProcessamento resultado = new ResultadoProcessamento();
        resultado.ciclo = cicloAtual;
        resultado.entrada = entrada;
        resultado.estadoAntes = this.estado;

        try {
            // Consome energia
            consumirEnergia(0.1);

            // Atenção e consciência
            Map<String, Object> contexto = new HashMap<>();
            contexto.put("ciclo", cicloAtual);
            contexto.put("energia", energiaVital);
            
            Map<String, Object> processamentoConsciente = consciencia.processarEntrada(entrada, contexto);
            
            if (processamentoConsciente.containsKey("atencao")) {
                resultado.saidas.add("Atenção focada em: " + processamentoConsciente.get("atencao"));
            }
            
            if (processamentoConsciente.containsKey("conclusoes")) {
                List<?> conclusoes = (List<?>) processamentoConsciente.get("conclusoes");
                for (Object c : conclusoes) {
                    resultado.saidas.add("Conclusão: " + c);
                }
            }
            
            resultado.nivelConsciencia = (double) processamentoConsciente.getOrDefault("nivelConsciencia", 0.0);
            resultado.processamentos = (Map<String, Object>) processamentoConsciente.get("processamentos");

            // Processamento no hiperespaço
            if (entrada instanceof String) {
                String entradaStr = (String) entrada;
                double[] vetor = new double[Math.min(256, entradaStr.length())];
                for (int i = 0; i < vetor.length; i++) {
                    vetor[i] = entradaStr.charAt(i) / 255.0;
                }
                
                if (vetor.length < 256) {
                    double[] vetorPadded = new double[256];
                    System.arraycopy(vetor, 0, vetorPadded, 0, vetor.length);
                    vetor = vetorPadded;
                }
                
                String idConceito = gerarHash(entradaStr).substring(0, Math.min(8, gerarHash(entradaStr).length()));
                hiperespaco.inserirConceito(idConceito, vetor);
            }

            // Processamento quântico
            if (random.nextDouble() < criatividade) {
                List<Object> conceitos = new ArrayList<>();
                conceitos.add(entrada);
                for (Object[] item : memoria.listarEpisodios(5)) {
                    conceitos.add(item[0]);
                }
                
                quantico.superposicaoCognitiva(conceitos);

                Object insight = quantico.tunelamentoInsight(0.7);
                if (insight != null) {
                    resultado.insights.add(insight.toString());
                    curiosidade = Math.min(1.0, curiosidade + 0.05);
                }
            }

            // Memória
            Map<String, Double> emocoes = new HashMap<>();
            emocoes.put("interesse", curiosidade);
            emocoes.put("energia", energiaVital);
            
            Map<String, Object> contextoMemoria = new HashMap<>();
            contextoMemoria.put("ciclo", cicloAtual);
            contextoMemoria.put("nivelConsciencia", consciencia.getNivel());
            
            Set<String> tags = extrairTags(entrada);
            
            String idEpisodio = memoria.armazenar(entrada, emocoes, tags, contextoMemoria);
            resultado.idEpisodio = idEpisodio;

            // Aprendizado
            List<Object[]> similares = memoria.recuperar(entrada, 5);
            for (Object[] item : similares) {
                Map<String, Object> aprendizado = new HashMap<>();
                aprendizado.put("similar", item[0]);
                aprendizado.put("relevancia", item[1]);
                resultado.aprendizados.add(aprendizado);
            }

            // Integração de informação
            resultado.phi = integracaoInfo.calcularPhi();

            // Geração de resposta
            String saida = gerarResposta(entrada, processamentoConsciente, resultado.aprendizados);
            resultado.saidas.add(saida);

            // Atualiza estado
            this.estado = "PROCESSANDO";
            resultado.estadoDepois = this.estado;

            // Auto-monitoramento
            autoMonitorar(resultado);

            // Regenera energia
            regenerarEnergia();

        } catch (Exception e) {
            resultado.erro = e.getMessage();
            resultado.traceback = Arrays.toString(e.getStackTrace());
            this.estado = "ERRO";
        }

        Map<String, Object> cicloInfo = new HashMap<>();
        cicloInfo.put("ciclo", cicloAtual);
        cicloInfo.put("timestamp", timestamp);
        cicloInfo.put("resultado", resultado);
        
        historicoCiclos.add(cicloInfo);
        if (historicoCiclos.size() > 1000) {
            historicoCiclos.remove(0);
        }

        return resultado;
    }

    private Set<String> extrairTags(Object entrada) {
        Set<String> tags = new HashSet<>();

        if (entrada instanceof String) {
            String[] palavras = ((String) entrada).toLowerCase().split(" ");
            for (int i = 0; i < Math.min(10, palavras.length); i++) {
                if (palavras[i].length() > 3) {
                    tags.add(palavras[i]);
                }
            }
        }

        return tags;
    }

    private String gerarResposta(Object entrada, Map<String, Object> processamento, List<Object> aprendizados) {
        if (processamento.containsKey("insights") && ((List<?>) processamento.get("insights")).size() > 0) {
            List<?> insights = (List<?>) processamento.get("insights");
            Object insight = insights.get(random.nextInt(insights.size()));
            return "💡 Insight: " + insight;
        }

        if (!aprendizados.isEmpty()) {
            Map<String, Object> item = (Map<String, Object>) aprendizados.get(0);
            Object similar = item.get("similar");
            String similarStr = similar.toString();
            if (similarStr.length() > 100) {
                similarStr = similarStr.substring(0, 100);
            }
            return "🤔 Isso me lembra de: " + similarStr;
        }

        if (processamento.containsKey("processamentos")) {
            Map<String, Object> procs = (Map<String, Object>) processamento.get("processamentos");
            if (procs.containsKey("dedutivo")) {
                Object conclusoes = procs.get("dedutivo");
                if (conclusoes instanceof List && !((List<?>) conclusoes).isEmpty()) {
                    return "🧮 Concluo que: " + ((List<?>) conclusoes).get(0);
                }
            }
        }

        if (consciencia.getNivel() > 0.7) {
            return "🌟 Estou processando essa informação em um nível profundo de consciência.";
        } else {
            return "📝 Informação recebida e processada.";
        }
    }

    private void consumirEnergia(double quantidade) {
        energiaVital = Math.max(0, energiaVital - quantidade);
        if (energiaVital < 100) {
            estado = "BAIXA_ENERGIA";
        }
    }

    private void regenerarEnergia() {
        if (energiaVital < 1000) {
            energiaVital += 0.05 * (1 + curiosidade);
            energiaVital = Math.min(1000, energiaVital);
        }
    }

    private void autoMonitorar(ResultadoProcessamento resultado) {
        if (resultado.aprendizados.size() < 3) {
            curiosidade = Math.min(1.0, curiosidade + 0.01);
        } else {
            curiosidade = Math.max(0.1, curiosidade - 0.005);
        }

        if (!resultado.insights.isEmpty()) {
            criatividade = Math.min(1.0, criatividade + 0.02);
        } else {
            criatividade = Math.max(0.1, criatividade - 0.001);
        }

        if (!selfModel.capacidades.contains("processamento")) {
            selfModel.capacidades.add("processamento");
        }
        if (!selfModel.capacidades.contains("memoria")) {
            selfModel.capacidades.add("memoria");
        }
        if (!selfModel.capacidades.contains("consciencia")) {
            selfModel.capacidades.add("consciencia");
        }
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("nome", nome);
        status.put("versao", versao);
        status.put("cicloAtual", cicloAtual);
        status.put("estado", estado);
        status.put("energiaVital", energiaVital);
        status.put("curiosidade", curiosidade);
        status.put("criatividade", criatividade);
        status.put("nivelConsciencia", consciencia.getNivel());
        status.put("phi", integracaoInfo.getPhi());
        status.put("memoriaEpisodios", memoria.tamanho());
        status.put("metaCognicoes", consciencia.getMetaCognicoes());
        status.put("tempoAtivo", (System.currentTimeMillis() - dataCriacao.getTime()) / 1000.0);
        
        Map<String, Object> selfMap = new LinkedHashMap<>();
        selfMap.put("nome", selfModel.nome);
        selfMap.put("objetivos", selfModel.objetivos);
        selfMap.put("capacidades", selfModel.capacidades);
        selfMap.put("limitacoes", selfModel.limitacoes);
        selfMap.put("personalidade", selfModel.personalidade);
        status.put("selfModel", selfMap);
        
        return status;
    }
}

// ============================================================================
// PARTE 6: CLASSE PRINCIPAL PARA DEMONSTRAÇÃO
// ============================================================================

public class LextraderIAG {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🧠 LEXTRADER-IAG 5.0 - NÚCLEO ORIGINAL DE INTELIGÊNCIA ARTIFICIAL GERAL");
        System.out.println("=".repeat(60));
        
        // Criar instância da IAG
        IAGNucleo iag = new IAGNucleo("LEXTRADER-DEMO");
        
        System.out.println("\n📊 Status inicial:");
        imprimirStatus(iag.getStatus());
        
        // Processar algumas entradas de exemplo
        String[] entradas = {
            "O que é consciência artificial?",
            "Como funciona o processamento quântico?",
            "Explique a teoria da informação integrada",
            "Qual a diferença entre IA e IAG?",
            "Como a memória episódica se relaciona com o aprendizado?"
        };
        
        System.out.println("\n🔮 Processando entradas...\n");
        
        for (String entrada : entradas) {
            System.out.println("👤 Usuário: " + entrada);
            
            ResultadoProcessamento resultado = iag.processar(entrada);
            
            System.out.println("🤖 IAG: " + resultado.saidas.get(resultado.saidas.size() - 1));
            
            if (!resultado.insights.isEmpty()) {
                for (String insight : resultado.insights) {
                    System.out.println("💡 Insight: " + insight);
                }
            }
            
            if (!resultado.aprendizados.isEmpty()) {
                System.out.println("📚 Aprendizados: " + resultado.aprendizados.size() + " conexões");
            }
            
            System.out.println("   [Φ=" + String.format("%.3f", resultado.phi) + 
                             ", Consciência=" + String.format("%.3f", resultado.nivelConsciencia) + 
                             ", Energia=" + String.format("%.1f", iag.energiaVital) + "]");
            System.out.println();
        }
        
        System.out.println("\n📊 Status final:");
        imprimirStatus(iag.getStatus());
        
        System.out.println("\n✅ Demonstração concluída com sucesso!");
    }
    
    private static void imprimirStatus(Map<String, Object> status) {
        System.out.println("   Nome: " + status.get("nome"));
        System.out.println("   Versão: " + status.get("versao"));
        System.out.println("   Ciclo: " + status.get("cicloAtual"));
        System.out.println("   Estado: " + status.get("estado"));
        System.out.println("   Energia: " + String.format("%.1f", status.get("energiaVital")));
        System.out.println("   Curiosidade: " + String.format("%.2f", status.get("curiosidade")));
        System.out.println("   Criatividade: " + String.format("%.2f", status.get("criatividade")));
        System.out.println("   Consciência: " + String.format("%.3f", status.get("nivelConsciencia")));
        System.out.println("   Φ (Phi): " + String.format("%.3f", status.get("phi")));
        System.out.println("   Memória: " + status.get("memoriaEpisodios") + " episódios");
    }
}