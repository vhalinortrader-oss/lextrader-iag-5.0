package modulo_emocional.Motor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Coordenação de movimentos voluntários
 */
public class MovimentosVoluntarios {
    
    private List<Map<String, Object>> movimentosAtivos;
    private List<Map<String, Object>> historicoMovimentos;
    private Map<String, Integer> prioridades;
    
    public MovimentosVoluntarios() {
        this.movimentosAtivos = new ArrayList<>();
        this.historicoMovimentos = new ArrayList<>();
        this.prioridades = new HashMap<>();
        
        // Inicializar prioridades base
        prioridades.put("emergencial", 10);
        prioridades.put("corretivo", 7);
        prioridades.put("preventivo", 5);
        prioridades.put("rotina", 3);
        prioridades.put("monitoramento", 1);
    }
    
    /**
     * Planeja um movimento voluntário baseado em objetivo
     */
    public Map<String, Object> planejarMovimento(String objetivo, Map<String, Object> parametros) {
        Map<String, Object> plano = new HashMap<>();
        plano.put("objetivo", objetivo);
        plano.put("parametros", parametros != null ? parametros : new HashMap<>());
        plano.put("estagios", new ArrayList<>());
        plano.put("prioridade", calcularPrioridade(objetivo));
        
        // Simulação de decomposição do movimento
        if (objetivo.toLowerCase().contains("econômico")) {
            List<String> estagios = Arrays.asList(
                "análise_contexto",
                "calculo_trajetoria", 
                "execução_adaptativa",
                "avaliação_resultado"
            );
            plano.put("estagios", estagios);
        }
        
        movimentosAtivos.add(plano);
        return plano;
    }
    
    /**
     * Executa um movimento planejado
     */
    public Map<String, Object> executarMovimento(Map<String, Object> plano) {
        System.out.println("Executando movimento: " + plano.get("objetivo"));
        
        @SuppressWarnings("unchecked")
        List<String> estagios = (List<String>) plano.get("estagios");
        
        // Simulação de execução
        for (String estagio : estagios) {
            System.out.println("  → Estágio: " + estagio);
        }
        
        registrarHistorico(plano);
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("status", "concluído");
        resultado.put("plano", plano);
        
        return resultado;
    }
    
    /**
     * Calcula prioridade baseada no objetivo
     */
    private int calcularPrioridade(String objetivo) {
        String objetivoLower = objetivo.toLowerCase();
        
        for (Map.Entry<String, Integer> entry : prioridades.entrySet()) {
            if (objetivoLower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return 2; // Prioridade padrão
    }
    
    /**
     * Registra movimento no histórico
     */
    private void registrarHistorico(Map<String, Object> plano) {
        Map<String, Object> registro = new HashMap<>();
        registro.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        registro.put("plano", plano);
        registro.put("resultado", "executado");
        
        historicoMovimentos.add(registro);
    }
    
    // Getters
    public List<Map<String, Object>> getMovimentosAtivos() {
        return new ArrayList<>(movimentosAtivos);
    }
    
    public List<Map<String, Object>> getHistoricoMovimentos() {
        return new ArrayList<>(historicoMovimentos);
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Movimentos Voluntários ===");
        
        MovimentosVoluntarios coordenador = new MovimentosVoluntarios();
        
        // Planejar movimento econômico
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("intensidade", 0.8);
        parametros.put("duracao", 30);
        
        Map<String, Object> plano = coordenador.planejarMovimento(
            "ajuste_econômico_emergencial", 
            parametros
        );
        
        System.out.println("Plano criado: " + plano.get("objetivo"));
        System.out.println("Prioridade: " + plano.get("prioridade"));
        System.out.println("Estágios: " + plano.get("estagios"));
        
        // Executar movimento
        Map<String, Object> resultado = coordenador.executarMovimento(plano);
        System.out.println("Resultado: " + resultado.get("status"));
        
        // Estatísticas
        System.out.println("\nEstatísticas:");
        System.out.println("  Movimentos ativos: " + coordenador.getMovimentosAtivos().size());
        System.out.println("  Histórico: " + coordenador.getHistoricoMovimentos().size());
    }
}
