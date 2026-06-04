package modulo_emocional.Motor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controle de equilíbrio e postura
 */
public class Postura {
    
    private double sensibilidade;
    private String estadoPostural;
    private List<Map<String, Object>> ajustesAtivos;
    private List<Map<String, Object>> historicoEstabilidade;
    
    public Postura(double sensibilidade) {
        this.sensibilidade = sensibilidade;
        this.estadoPostural = "estável";
        this.ajustesAtivos = new ArrayList<>();
        this.historicoEstabilidade = new ArrayList<>();
    }
    
    public Postura() {
        this(0.8); // Sensibilidade padrão
    }
    
    /**
     * Avalia o equilíbrio atual baseado em dados sensoriais
     */
    public Map<String, Object> avaliarEquilibrio(Map<String, Object> dadosSensoriais) {
        if (dadosSensoriais == null || dadosSensoriais.isEmpty()) {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("estado", "indeterminado");
            resultado.put("confianca", 0);
            return resultado;
        }
        
        // Calcula métricas de estabilidade
        double estabilidade = calcularEstabilidade(dadosSensoriais);
        List<Map<String, Object>> desvios = detectarDesvios(dadosSensoriais);
        
        // Classifica estado
        String estado;
        if (estabilidade > 0.8) {
            estado = "estável";
        } else if (estabilidade > 0.5) {
            estado = "moderado";
        } else {
            estado = "instável";
        }
        
        Map<String, Object> diagnostico = new HashMap<>();
        diagnostico.put("estado", estado);
        diagnostico.put("estabilidade", estabilidade);
        diagnostico.put("desvios", desvios);
        diagnostico.put("ajustes_necessarios", !desvios.isEmpty());
        diagnostico.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        this.estadoPostural = estado;
        historicoEstabilidade.add(diagnostico);
        
        return diagnostico;
    }
    
    /**
     * Aplica correções posturais baseadas no diagnóstico
     */
    public List<Map<String, Object>> aplicarCorrecao(Map<String, Object> diagnostico) {
        List<Map<String, Object>> ajustes = new ArrayList<>();
        
        if ((Boolean) diagnostico.get("ajustes_necessarios")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> desvios = (List<Map<String, Object>>) diagnostico.get("desvios");
            
            for (Map<String, Object> desvio : desvios) {
                Map<String, Object> ajuste = gerarAjuste(desvio);
                if (ajuste != null) {
                    ajustes.add(ajuste);
                    ajustesAtivos.add(ajuste);
                }
            }
        }
        
        return ajustes;
    }
    
    /**
     * Calcula métrica de estabilidade (0 a 1)
     */
    private double calcularEstabilidade(Map<String, Object> dados) {
        double estabilidade;
        
        if (dados.containsKey("aceleracao")) {
            // Quanto menor aceleração, mais estável
            double aceleracao = Math.abs((Double) dados.get("aceleracao"));
            estabilidade = Math.max(0, 1 - aceleracao * sensibilidade);
        } else {
            estabilidade = 0.7; // Valor padrão
        }
        
        return Math.min(1.0, Math.max(0.0, estabilidade));
    }
    
    /**
     * Detecta desvios posturais
     */
    private List<Map<String, Object>> detectarDesvios(Map<String, Object> dados) {
        List<Map<String, Object>> desvios = new ArrayList<>();
        
        // Simulação de detecção
        if (dados.containsKey("inclinacao")) {
            double inclinacao = (Double) dados.get("inclinacao");
            if (Math.abs(inclinacao) > 0.2) {
                Map<String, Object> desvio = new HashMap<>();
                desvio.put("tipo", "inclinacao_lateral");
                desvio.put("magnitude", inclinacao);
                desvio.put("correcao", "contra_inclinacao");
                desvios.add(desvio);
            }
        }
        
        if (dados.containsKey("rotacao")) {
            double rotacao = (Double) dados.get("rotacao");
            if (Math.abs(rotacao) > 0.3) {
                Map<String, Object> desvio = new HashMap<>();
                desvio.put("tipo", "rotacao_excessiva");
                desvio.put("magnitude", rotacao);
                desvio.put("correcao", "estabilizacao_angular");
                desvios.add(desvio);
            }
        }
        
        return desvios;
    }
    
    /**
     * Gera comando de ajuste para um desvio específico
     */
    private Map<String, Object> gerarAjuste(Map<String, Object> desvio) {
        String tipo = (String) desvio.get("tipo");
        double magnitude = (Double) desvio.get("magnitude");
        
        Map<String, Object> correcoes = new HashMap<>();
        
        // Inclinação lateral
        Map<String, Object> correcaoInclinacao = new HashMap<>();
        correcaoInclinacao.put("comando", "ajustar_torcao_lateral");
        correcaoInclinacao.put("intensidade", Math.min(1.0, Math.abs(magnitude) * 2));
        correcaoInclinacao.put("direcao", magnitude > 0 ? "contraria" : "favoravel");
        correcoes.put("inclinacao_lateral", correcaoInclinacao);
        
        // Rotação excessiva
        Map<String, Object> correcaoRotacao = new HashMap<>();
        correcaoRotacao.put("comando", "compensar_rotacao");
        correcaoRotacao.put("intensidade", Math.min(1.0, Math.abs(magnitude) * 1.5));
        correcaoRotacao.put("direcao", "oposta");
        correcoes.put("rotacao_excessiva", correcaoRotacao);
        
        return correcoes.getOrDefault(tipo, null);
    }
    
    // Getters e Setters
    public String getEstadoPostural() {
        return estadoPostural;
    }
    
    public List<Map<String, Object>> getAjustesAtivos() {
        return new ArrayList<>(ajustesAtivos);
    }
    
    public List<Map<String, Object>> getHistoricoEstabilidade() {
        return new ArrayList<>(historicoEstabilidade);
    }
    
    public void setSensibilidade(double sensibilidade) {
        this.sensibilidade = sensibilidade;
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Controle de Postura ===");
        
        Postura controle = new Postura(0.8);
        
        // Dados sensoriais de teste
        Map<String, Object> dadosSensoriais = new HashMap<>();
        dadosSensoriais.put("aceleracao", 0.1);
        dadosSensoriais.put("inclinacao", 0.25);
        dadosSensoriais.put("rotacao", 0.35);
        
        // Avaliar equilíbrio
        System.out.println("Avaliando equilíbrio...");
        Map<String, Object> diagnostico = controle.avaliarEquilibrio(dadosSensoriais);
        
        System.out.println("Estado: " + diagnostico.get("estado"));
        System.out.println("Estabilidade: " + String.format("%.2f", (Double) diagnostico.get("estabilidade")));
        System.out.println("Ajustes necessários: " + diagnostico.get("ajustes_necessarios"));
        
        // Aplicar correções
        if ((Boolean) diagnostico.get("ajustes_necessarios")) {
            System.out.println("\nAplicando correções...");
            List<Map<String, Object>> correcoes = controle.aplicarCorrecao(diagnostico);
            
            for (Map<String, Object> correcao : correcoes) {
                System.out.println("  Correção: " + correcao.get("comando"));
                System.out.println("    Intensidade: " + String.format("%.2f", (Double) correcao.get("intensidade")));
                System.out.println("    Direção: " + correcao.get("direcao"));
            }
        }
        
        // Estatísticas
        System.out.println("\nEstatísticas:");
        System.out.println("  Estado postural: " + controle.getEstadoPostural());
        System.out.println("  Ajustes ativos: " + controle.getAjustesAtivos().size());
        System.out.println("  Histórico: " + controle.getHistoricoEstabilidade().size() + " avaliações");
    }
}
