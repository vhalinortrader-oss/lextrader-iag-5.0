package modulo_emocional.Memoria_saida;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe que representa um engrama de memória neural
 */
public class MemoryEngram {
    private String patternName;
    private LocalDateTime timestamp;
    private String outcome;
    private double synapticStrength;
    private boolean isApex;
    
    public MemoryEngram(String patternName, LocalDateTime timestamp, String outcome, 
                     double synapticStrength, boolean isApex) {
        this.patternName = patternName;
        this.timestamp = timestamp;
        this.outcome = outcome;
        this.synapticStrength = synapticStrength;
        this.isApex = isApex;
    }
    
    // Getters e Setters
    public String getPatternName() { return patternName; }
    public void setPatternName(String patternName) { this.patternName = patternName; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    
    public double getSynapticStrength() { return synapticStrength; }
    public void setSynapticStrength(double synapticStrength) { this.synapticStrength = synapticStrength; }
    
    public boolean isApex() { return isApex; }
    public void setApex(boolean apex) { isApex = apex; }
    
    @Override
    public String toString() {
        return String.format("MemoryEngram{pattern='%s', strength=%.2f, apex=%s}", 
                          patternName, synapticStrength, isApex);
    }
}

/**
 * Sistema de núcleo de memória neural
 */
public class MemoryCore {
    
    private List<MemoryEngram> activeMemories;
    private int totalMemories;
    private Random random;
    
    public MemoryCore() {
        this.activeMemories = new ArrayList<>();
        this.totalMemories = 123456;
        this.random = new Random();
    }
    
    /**
     * Renderiza o componente Córtex de Memória Contínua (versão console)
     */
    public void renderMemoryCore() {
        System.out.println("\n=== CÓRTEX DE MEMÓRIA CONTÍNUA ===");
        System.out.println("ENGRAMAS TOTAIS: " + totalMemories);
        System.out.println();
        
        // Gerar visualização do mapa de ativação sináptica
        renderActivationMap();
        
        // Renderizar engramas ativos
        renderActiveEngrams();
    }
    
    /**
     * Renderiza o mapa de ativação neural
     */
    private void renderActivationMap() {
        System.out.println("MAPA DE ATIVAÇÃO SINÁPTICA:");
        System.out.println("Plasticidade: ALTA | Consolidação: 98%");
        
        // Grid 8x8 simplificado
        boolean[][] grid = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = random.nextDouble() > 0.8;
            }
        }
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String cell = grid[i][j] ? "█" : "░";
                System.out.print(cell);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Renderiza engramas ativos recuperados
     */
    private void renderActiveEngrams() {
        System.out.println("ENGRAMAS RECUPERADOS (CONTEXTO ATUAL):");
        
        if (activeMemories.isEmpty()) {
            System.out.println("Nenhum engrama semelhante detectado no buffer de curto prazo.");
            return;
        }
        
        for (int i = 0; i < activeMemories.size(); i++) {
            MemoryEngram mem = activeMemories.get(i);
            renderEngram(mem, i + 1);
        }
    }
    
    /**
     * Renderiza um engrama individual
     */
    private void renderEngram(MemoryEngram mem, int index) {
        String apex = mem.isApex() ? "⭐" : "";
        String patternName = mem.getPatternName().replace("[APEX] ", "");
        String dateStr = mem.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String strengthPercent = String.format("%.1f%%", mem.getSynapticStrength() * 100);
        String strengthText = mem.isApex() ? "100% EFICÁCIA" : "Força Sináptica";
        
        System.out.println();
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.printf("│ %s%s %s%n", apex, patternName, 
                         mem.getOutcome().equals("SUCCESS") ? "✅" : "❌");
        System.out.printf("│ %s 🕒 %s%n", mem.getOutcome(), dateStr);
        System.out.printf("│ %s: %s%n", strengthText, strengthPercent);
        
        // Barra de força visual
        System.out.print("│ ");
        int barLength = (int) (mem.getSynapticStrength() * 20);
        for (int i = 0; i < 20; i++) {
            if (i < barLength) {
                System.out.print(mem.isApex() ? "█" : "▓");
            } else {
                System.out.print("░");
            }
        }
        System.out.println(" " + barLength + "/20");
        System.out.println("└─────────────────────────────────────────┘");
    }
    
    /**
     * Cria memórias de exemplo para demonstração
     */
    public List<MemoryEngram> createSampleMemories(int count) {
        List<String> samplePatterns = Arrays.asList(
            "Padrão de Alta Frequência",
            "Sequência Fibonacci",
            "Resposta ao Medo",
            "Recompensa Positiva",
            "Ponto de Virada",
            "Padrão de Confiança",
            "Estabilidade Temporal",
            "Divergência de Preço",
            "Retorno à Média",
            "Momentum Inverso"
        );
        
        List<String> apexPatterns = Arrays.asList(
            "[APEX] Memória de Sucesso Total",
            "[APEX] Experiência Ótima",
            "[APEX] Pico de Performance"
        );
        
        List<String> outcomes = Arrays.asList("SUCCESS", "FAILURE", "NEUTRAL");
        List<MemoryEngram> memories = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            boolean isApex = random.nextDouble() > 0.8;
            String pattern;
            
            if (isApex && !apexPatterns.isEmpty()) {
                pattern = apexPatterns.get(random.nextInt(apexPatterns.size()));
            } else {
                pattern = samplePatterns.get(random.nextInt(samplePatterns.size()));
            }
            
            MemoryEngram memory = new MemoryEngram(
                pattern,
                LocalDateTime.now().minusDays(random.nextInt(365)),
                outcomes.get(random.nextInt(outcomes.size())),
                0.3 + random.nextDouble() * 0.7,
                isApex
            );
            
            memories.add(memory);
        }
        
        return memories;
    }
    
    /**
     * Adiciona engramas ativos
     */
    public void addActiveMemories(List<MemoryEngram> memories) {
        this.activeMemories.addAll(memories);
    }
    
    /**
     * Limpa engramas ativos
     */
    public void clearActiveMemories() {
        this.activeMemories.clear();
    }
    
    /**
     * Atualiza o total de memórias
     */
    public void setTotalMemories(int total) {
        this.totalMemories = total;
    }
    
    // Getters
    public List<MemoryEngram> getActiveMemories() {
        return new ArrayList<>(activeMemories);
    }
    
    public int getTotalMemories() {
        return totalMemories;
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Memory Core ===");
        
        MemoryCore core = new MemoryCore();
        
        // Criar memórias de exemplo
        List<MemoryEngram> sampleMemories = core.createSampleMemories(6);
        core.addActiveMemories(sampleMemories);
        
        // Renderizar o componente
        core.renderMemoryCore();
        
        // Estatísticas
        System.out.println("\nESTATÍSTICAS:");
        System.out.println("  Engramas ativos: " + core.getActiveMemories().size());
        System.out.println("  Total armazenado: " + core.getTotalMemories());
        
        // Simular atualização
        System.out.println("\nSimulando atualização de memórias...");
        core.clearActiveMemories();
        core.renderMemoryCore();
    }
}
