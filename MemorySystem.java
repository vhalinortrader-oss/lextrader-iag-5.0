package modulo_emocional.Memoria_saida;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Sistema de memória neural avançado
 */
public class MemorySystem {
    
    private Map<String, MemoryEngram> memoryStore;
    private List<String> activePatterns;
    private double synapticPlasticity;
    private Random random;
    
    public MemorySystem() {
        this.memoryStore = new HashMap<>();
        this.activePatterns = new ArrayList<>();
        this.synapticPlasticity = 0.85;
        this.random = new Random();
    }
    
    /**
     * Armazena um novo engrama de memória
     */
    public void storeMemory(String key, MemoryEngram engram) {
        memoryStore.put(key, engram);
        System.out.println("💾 Memória armazenada: " + key + " -> " + engram.getPatternName());
    }
    
    /**
     * Recupera um engrama de memória
     */
    public MemoryEngram retrieveMemory(String key) {
        MemoryEngram engram = memoryStore.get(key);
        if (engram != null) {
            System.out.println("🧠 Memória recuperada: " + key);
            // Aumenta força sináptica pelo acesso
            engram.setSynapticStrength(Math.min(1.0, engram.getSynapticStrength() + 0.01));
        }
        return engram;
    }
    
    /**
     * Busca padrões similares baseado em contexto
     */
    public List<MemoryEngram> searchSimilarPatterns(String context) {
        List<MemoryEngram> similarPatterns = new ArrayList<>();
        
        for (MemoryEngram engram : memoryStore.values()) {
            if (isPatternSimilar(engram.getPatternName(), context)) {
                similarPatterns.add(engram);
            }
        }
        
        // Ordena por força sináptica
        similarPatterns.sort((a, b) -> Double.compare(b.getSynapticStrength(), a.getSynapticStrength()));
        
        return similarPatterns;
    }
    
    /**
     * Verifica similaridade entre padrões
     */
    private boolean isPatternSimilar(String pattern1, String pattern2) {
        String[] words1 = pattern1.toLowerCase().split("\\s+");
        String[] words2 = pattern2.toLowerCase().split("\\s+");
        
        int commonWords = 0;
        for (String word : words1) {
            for (String w : words2) {
                if (word.equals(w)) {
                    commonWords++;
                    break;
                }
            }
        }
        
        double similarity = (double) commonWords / Math.max(words1.length, words2.length);
        return similarity > 0.3; // 30% de similaridade
    }
    
    /**
     * Realiza consolidação de memória (long-term)
     */
    public void consolidateMemory() {
        System.out.println("🔄 Iniciando consolidação de memória...");
        
        List<MemoryEngram> toConsolidate = new ArrayList<>();
        
        for (MemoryEngram engram : memoryStore.values()) {
            // Engramas com alta força e acesso recente são consolidados
            if (engram.getSynapticStrength() > 0.7 && !engram.isApex()) {
                engram.setApex(true);
                toConsolidate.add(engram);
            }
        }
        
        System.out.println("✅ " + toConsolidate.size() + " engramas consolidados como APEX");
    }
    
    /**
     * Limpa memórias fracas ou de baixa relevância
     */
    public void cleanupWeakMemories() {
        System.out.println("🧹 Limpando memórias fracas...");
        
        Iterator<Map.Entry<String, MemoryEngram>> iterator = memoryStore.entrySet().iterator();
        int removed = 0;
        
        while (iterator.hasNext()) {
            Map.Entry<String, MemoryEngram> entry = iterator.next();
            MemoryEngram engram = entry.getValue();
            
            // Remove memórias com força baixa e resultado negativo
            if (engram.getSynapticStrength() < 0.2 && "FAILURE".equals(engram.getOutcome())) {
                iterator.remove();
                removed++;
            }
        }
        
        System.out.println("✅ " + removed + " memórias fracas removidas");
    }
    
    /**
     * Gera estatísticas do sistema de memória
     */
    public Map<String, Object> generateStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        int totalMemories = memoryStore.size();
        int apexMemories = 0;
        double totalSynapticStrength = 0;
        Map<String, Integer> outcomeCounts = new HashMap<>();
        
        for (MemoryEngram engram : memoryStore.values()) {
            if (engram.isApex()) {
                apexMemories++;
            }
            totalSynapticStrength += engram.getSynapticStrength();
            
            String outcome = engram.getOutcome();
            outcomeCounts.put(outcome, outcomeCounts.getOrDefault(outcome, 0) + 1);
        }
        
        stats.put("total_memories", totalMemories);
        stats.put("apex_memories", apexMemories);
        stats.put("average_synaptic_strength", totalMemories > 0 ? totalSynapticStrength / totalMemories : 0);
        stats.put("synaptic_plasticity", synapticPlasticity);
        stats.put("outcome_distribution", outcomeCounts);
        stats.put("active_patterns", activePatterns.size());
        stats.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return stats;
    }
    
    /**
     * Ativa um padrão para processamento
     */
    public void activatePattern(String pattern) {
        if (!activePatterns.contains(pattern)) {
            activePatterns.add(pattern);
            System.out.println("⚡ Padrão ativado: " + pattern);
        }
    }
    
    /**
     * Desativa um padrão
     */
    public void deactivatePattern(String pattern) {
        if (activePatterns.remove(pattern)) {
            System.out.println("⭕ Padrão desativado: " + pattern);
        }
    }
    
    /**
     * Simula esquecimento natural
     */
    public void simulateForgetting() {
        System.out.println("🌊 Simulando esquecimento natural...");
        
        for (MemoryEngram engram : memoryStore.values()) {
            // Degradação baseada no tempo e uso
            double decay = 0.001; // Taxa de esquecimento
            if (!activePatterns.contains(engram.getPatternName())) {
                decay *= 2; // Degrada mais rápida se não for usado
            }
            
            double newStrength = Math.max(0.1, engram.getSynapticStrength() - decay);
            engram.setSynapticStrength(newStrength);
        }
    }
    
    /**
     * Otimiza o sistema de memória
     */
    public void optimizeMemory() {
        System.out.println("⚡ Otimizando sistema de memória...");
        
        // Reorganiza memórias por relevância
        List<MemoryEngram> sortedMemories = new ArrayList<>(memoryStore.values());
        sortedMemories.sort((a, b) -> Double.compare(b.getSynapticStrength(), a.getSynapticStrength()));
        
        // Mantém apenas as memórias mais relevantes (top 80%)
        int keepCount = (int) (sortedMemories.size() * 0.8);
        List<MemoryEngram> optimizedMemories = sortedMemories.subList(0, keepCount);
        
        memoryStore.clear();
        for (int i = 0; i < optimizedMemories.size(); i++) {
            MemoryEngram engram = optimizedMemories.get(i);
            memoryStore.put("mem_" + i, engram);
        }
        
        System.out.println("✅ Sistema otimizado: " + memoryStore.size() + " memórias mantidas");
    }
    
    // Getters
    public Map<String, MemoryEngram> getMemoryStore() {
        return new HashMap<>(memoryStore);
    }
    
    public List<String> getActivePatterns() {
        return new ArrayList<>(activePatterns);
    }
    
    public double getSynapticPlasticity() {
        return synapticPlasticity;
    }
    
    public void setSynapticPlasticity(double synapticPlasticity) {
        this.synapticPlasticity = Math.max(0.1, Math.min(1.0, synapticPlasticity));
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Memory System ===");
        
        MemorySystem memorySystem = new MemorySystem();
        
        // Criar memórias de exemplo
        MemoryCore core = new MemoryCore();
        List<MemoryEngram> sampleMemories = core.createSampleMemories(5);
        
        // Armazenar memórias
        for (int i = 0; i < sampleMemories.size(); i++) {
            MemoryEngram mem = sampleMemories.get(i);
            memorySystem.storeMemory("mem_" + i, mem);
        }
        
        // Buscar padrões similares
        System.out.println("\nBuscando padrões similares...");
        List<MemoryEngram> similar = memorySystem.searchSimilarPatterns("sucesso");
        System.out.println("Encontrados: " + similar.size() + " padrões similares");
        
        // Consolidação
        memorySystem.consolidateMemory();
        
        // Estatísticas
        Map<String, Object> stats = memorySystem.generateStatistics();
        System.out.println("\nEstatísticas:");
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        
        // Otimização
        memorySystem.optimizeMemory();
        
        // Simulação de esquecimento
        memorySystem.simulateForgetting();
        
        System.out.println("\n✅ Demonstração concluída com sucesso!");
    }
}
