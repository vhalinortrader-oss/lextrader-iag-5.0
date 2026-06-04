package modulo_emocional.Emocional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Sistema de regulação emocional
 */
public class ReguladorEmocional {
    
    private Map<String, Object> config;
    private List<EstadoEmocional> historicoEmocoes;
    private Map<String, Double> estadoAtual;
    private EstrategiasRegulacao estrategias;
    private MapeadorEmocional mapeador;
    private double limiarRegulacao;
    private Random random;
    
    public ReguladorEmocional() {
        this.config = new HashMap<>();
        this.historicoEmocoes = new ArrayList<>();
        this.estadoAtual = new HashMap<>();
        this.estrategias = new EstrategiasRegulacao();
        this.mapeador = new MapeadorEmocional();
        this.limiarRegulacao = 0.7;
        this.random = new Random();
        
        inicializarEstadoPadrao();
    }
    
    /**
     * Inicializa estado emocional padrão
     */
    private void inicializarEstadoPadrao() {
        estadoAtual.put("felicidade", 0.6);
        estadoAtual.put("tristeza", 0.2);
        estadoAtual.put("raiva", 0.1);
        estadoAtual.put("medo", 0.3);
        estadoAtual.put("surpresa", 0.2);
        estadoAtual.put("medo", 0.4);
        estadoAtual.put("confianca", 0.7);
        estadoAtual.put("ansiedade", 0.3);
        estadoAtual.put("calma", 0.5);
    }
    
    /**
     * Regula uma emoção específica
     */
    public Map<String, Object> regularEmocao(String emocao, double intensidade, String contexto) {
        System.out.println("🎭 Regulando emoção: " + emocao + " (intensidade: " + 
                         String.format("%.2f", intensidade) + ")");
        
        // Mapear emoção para valores normalizados
        double[] vetorEmocional = mapeador.mapearEmocao(emocao);
        
        // Verificar se regulação é necessária
        double nivelEmocional = calcularNivelEmocional(vetorEmocional);
        if (nivelEmocional < limiarRegulacao) {
            System.out.println("✅ Emoção dentro dos limites aceitáveis");
            return gerarRespostaRegulacao(emocao, "ACEITAVEL", vetorEmocional);
        }
        
        // Aplicar estratégia de regulação
        EstrategiaRegulacao estrategia = estrategias.selecionarEstrategia(emocao, vetorEmocional, contexto);
        Map<String, Object> resultado = estrategia.aplicar(vetorEmocional);
        
        // Atualizar estado emocional
        atualizarEstadoEmocional(vetorEmocional, resultado);
        
        // Registrar no histórico
        registrarHistoricoEmocional(emocao, intensidade, contexto, resultado);
        
        return resultado;
    }
    
    /**
     * Calcula o nível geral da emoção
     */
    private double calcularNivelEmocional(double[] vetorEmocional) {
        double soma = 0;
        for (double valor : vetorEmocional) {
            soma += valor;
        }
        return soma / vetorEmocional.length;
    }
    
    /**
     * Gera resposta de regulação
     */
    private Map<String, Object> gerarRespostaRegulacao(String emocao, String status, double[] vetorEmocional) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("emocao", emocao);
        resposta.put("status", status);
        resposta.put("vetor_emocional", vetorEmocional);
        resposta.put("nivel_geral", calcularNivelEmocional(vetorEmocional));
        resposta.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        resposta.put("estrategia_utilizada", status.equals("REGULADO") ? "sim" : "não");
        
        return resposta;
    }
    
    /**
     * Atualiza o estado emocional atual
     */
    private void atualizarEstadoEmocional(double[] vetorEmocional, Map<String, Object> resultado) {
        String status = (String) resultado.get("status");
        
        if (status.equals("REGULADO")) {
            // Aplicar atenuação gradual
            for (int i = 0; i < vetorEmocional.length; i++) {
                String nomeEmocao = obterNomeEmocao(i);
                double valorAtual = estadoAtual.getOrDefault(nomeEmocao, 0.0);
                double valorAlvo = vetorEmocional[i];
                
                // Atualização exponencial suave
                double novoValor = valorAtual + (valorAlvo - valorAtual) * 0.1;
                estadoAtual.put(nomeEmocao, Math.max(0.0, Math.min(1.0, novoValor)));
            }
        }
    }
    
    /**
     * Obtém o nome da emoção baseado no índice
     */
    private String obterNomeEmocao(int indice) {
        String[] emocoes = {"felicidade", "tristeza", "raiva", "medo", "surpresa", 
                              "medo", "confianca", "ansiedade", "calma"};
        
        if (indice >= 0 && indice < emocoes.length) {
            return emocoes[indice];
        }
        return "desconhecida";
    }
    
    /**
     * Registra histórico de regulações emocionais
     */
    private void registrarHistoricoEmocional(String emocao, double intensidade, String contexto, Map<String, Object> resultado) {
        Map<String, Object> registro = new HashMap<>();
        registro.put("emocao", emocao);
        registro.put("intensidade", intensidade);
        registro.put("contexto", contexto);
        registro.put("resultado", resultado);
        registro.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        historicoEmocoes.add(registro);
        
        // Manter apenas os últimos 100 registros
        if (historicoEmocoes.size() > 100) {
            historicoEmocoes = historicoEmocoes.subList(historicoEmocoes.size() - 100, historicoEmocoes.size());
        }
    }
    
    /**
     * Obtém estado emocional atual
     */
    public Map<String, Double> getEstadoAtual() {
        return new HashMap<>(estadoAtual);
    }
    
    /**
     * Obtém histórico de regulações
     */
    public List<Map<String, Object>> getHistoricoEmocoes() {
        return new ArrayList<>(historicoEmocoes);
    }
    
    /**
     * Define limiar de regulação
     */
    public void setLimiarRegulacao(double limiar) {
        this.limiarRegulacao = Math.max(0.0, Math.min(1.0, limiar));
        System.out.println("⚙️ Limiar de regulação atualizado: " + String.format("%.2f", limiar));
    }
    
    /**
     * Gera relatório de regulação emocional
     */
    public Map<String, Object> gerarRelatorioRegulacao() {
        Map<String, Object> relatorio = new HashMap<>();
        
        // Estatísticas do estado atual
        relatorio.put("estado_emocional_atual", estadoAtual);
        relatorio.put("nivel_geral", calcularNivelEmocional(
            estadoAtual.values().stream().mapToDouble(Double::doubleValue).toArray(Double[]::new)));
        
        // Estatísticas do histórico
        int totalRegulacoes = historicoEmocoes.size();
        Map<String, Integer> contagemEmocoes = new HashMap<>();
        
        for (Map<String, Object> registro : historicoEmocoes) {
            String emocao = (String) registro.get("emocao");
            contagemEmocoes.put(emocao, contagemEmocoes.getOrDefault(emocao, 0) + 1);
        }
        
        relatorio.put("total_regulacoes", totalRegulacoes);
        relatorio.put("contagem_emocoes", contagemEmocoes);
        relatorio.put("limiar_regulacao", limiarRegulacao);
        relatorio.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return relatorio;
    }
    
    /**
     * Simula regulação automática baseada em estímulos
     */
    public void simularRegulacaoAutomatica() {
        System.out.println("🤖 Iniciando simulação de regulação automática...");
        
        String[] estimulos = {"evento_positivo", "evento_negativo", "evento_neutro", "estresse", "alegria"};
        
        for (String estimulo : estimulos) {
            System.out.println("\n📥 Estímulo: " + estimulo);
            
            // Gerar emoção baseada no estímulo
            String emocao;
            double intensidade;
            
            switch (estimulo) {
                case "evento_positivo":
                    emocao = "felicidade";
                    intensidade = 0.8;
                    break;
                case "evento_negativo":
                    emocao = "tristeza";
                    intensidade = 0.7;
                    break;
                case "evento_neutro":
                    emocao = "calma";
                    intensidade = 0.3;
                    break;
                case "estresse":
                    emocao = "ansiedade";
                    intensidade = 0.9;
                    break;
                case "alegria":
                    emocao = "felicidade";
                    intensidade = 0.9;
                    break;
                default:
                    emocao = "medo";
                    intensidade = 0.5;
            }
            
            // Regularizar emoção
            Map<String, Object> resultado = regularEmocao(emocao, intensidade, "simulacao_" + estimulo);
            
            System.out.println("Resultado: " + resultado.get("status"));
            System.out.println("Nível geral: " + String.format("%.2f", (Double) resultado.get("nivel_geral")));
        }
        
        System.out.println("\n✅ Simulação concluída");
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Regulador Emocional ===");
        
        ReguladorEmocional regulador = new ReguladorEmocional();
        
        // Exibir estado inicial
        System.out.println("\n1. Estado emocional inicial:");
        Map<String, Double> estadoInicial = regulador.getEstadoAtual();
        for (Map.Entry<String, Double> entry : estadoInicial.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
        }
        
        // Regularizar emoções de exemplo
        System.out.println("\n2. Regulando emoções de exemplo:");
        
        regulador.regularEmocao("felicidade", 0.9, "recebeu_elogio");
        regulador.regularEmocao("tristeza", 0.8, "perdeu_oportunidade");
        regulador.regularEmocao("raiva", 0.6, "foi_desrespeitado");
        
        // Exibir estado após regulação
        System.out.println("\n3. Estado emocional após regulação:");
        Map<String, Double> estadoAtualizado = regulador.getEstadoAtual();
        for (Map.Entry<String, Double> entry : estadoAtualizado.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
        }
        
        // Gerar relatório
        System.out.println("\n4. Relatório de regulação:");
        Map<String, Object> relatorio = regulador.gerarRelatorioRegulacao();
        System.out.println("Total de regulações: " + relatorio.get("total_regulacoes"));
        System.out.println("Contagem de emoções: " + relatorio.get("contagem_emocoes"));
        
        // Simular regulação automática
        System.out.println("\n5. Simulação de regulação automática:");
        regulador.simularRegulacaoAutomatica();
        
        System.out.println("\n✅ Demonstração concluída com sucesso!");
    }
}
