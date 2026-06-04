package modulo_emocional.Motor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Geração e reconhecimento de padrões repetitivos
 */
public class PadroesRepetitivos {
    
    private Map<String, Map<String, Object>> padroesAprendidos;
    private List<List<String>> sequenciasAtivas;
    
    public PadroesRepetitivos() {
        this.padroesAprendidos = new HashMap<>();
        this.sequenciasAtivas = new ArrayList<>();
    }
    
    /**
     * Identifica padrões em sequências de movimentos
     */
    public Map<String, Object> identificarPadrao(List<String> sequencia) {
        if (sequencia == null || sequencia.isEmpty()) {
            return null;
        }
        
        // Converte sequência para string hash para comparação
        String seqHash = gerarHashSequencia(sequencia);
        
        // Verifica se padrão já existe
        for (Map.Entry<String, Map<String, Object>> entry : padroesAprendidos.entrySet()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> padrao = entry.getValue();
            
            if (padrao.get("hash").equals(seqHash)) {
                padrao.put("frequencia", (Integer) padrao.get("frequencia") + 1);
                
                Map<String, Object> resultado = new HashMap<>();
                resultado.put("nome", entry.getKey());
                resultado.put("padrao", padrao);
                
                return resultado;
            }
        }
        
        // Se não encontrado, cria novo padrão
        String novoNome = "padrao_" + (padroesAprendidos.size() + 1);
        
        Map<String, Object> novoPadrao = new HashMap<>();
        novoPadrao.put("sequencia", new ArrayList<>(sequencia));
        novoPadrao.put("hash", seqHash);
        novoPadrao.put("frequencia", 1);
        novoPadrao.put("contexto", "geral");
        
        padroesAprendidos.put(novoNome, novoPadrao);
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("nome", novoNome);
        resultado.put("padrao", novoPadrao);
        
        return resultado;
    }
    
    /**
     * Otimiza uma sequência baseada em padrões aprendidos
     */
    public List<String> otimizarSequencia(List<String> sequencia) {
        Map<String, Object> padraoResult = identificarPadrao(sequencia);
        
        if (padraoResult != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> padrao = (Map<String, Object>) padraoResult.get("padrao");
            
            if ((Integer) padrao.get("frequencia") > 3) {
                // Se padrão frequente, pode aplicar otimizações
                List<String> sequenciaOtimizada = new ArrayList<>(sequencia);
                
                // Simplificação hipotética
                if (sequencia.size() > 5) {
                    // Remove etapas redundantes para sequências longas
                    sequenciaOtimizada = removerRedundancias(sequenciaOtimizada);
                }
                
                return sequenciaOtimizada;
            }
        }
        
        return sequencia;
    }
    
    /**
     * Gera hash único para sequência
     */
    private String gerarHashSequencia(List<String> sequencia) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String seqStr = sequencia.toString();
            byte[] hashBytes = md.digest(seqStr.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString().substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            // Fallback para hash simples
            return String.valueOf(sequencia.hashCode());
        }
    }
    
    /**
     * Remove elementos redundantes da sequência
     */
    private List<String> removerRedundancias(List<String> sequencia) {
        if (sequencia.size() <= 1) {
            return sequencia;
        }
        
        List<String> sequenciaSimplificada = new ArrayList<>();
        sequenciaSimplificada.add(sequencia.get(0));
        
        for (int i = 1; i < sequencia.size(); i++) {
            String item = sequencia.get(i);
            if (!item.equals(sequenciaSimplificada.get(sequenciaSimplificada.size() - 1))) {
                sequenciaSimplificada.add(item);
            }
        }
        
        return sequenciaSimplificada;
    }
    
    // Getters
    public Map<String, Map<String, Object>> getPadroesAprendidos() {
        return new HashMap<>(padroesAprendidos);
    }
    
    public List<List<String>> getSequenciasAtivas() {
        return new ArrayList<>(sequenciasAtivas);
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Padrões Repetitivos ===");
        
        PadroesRepetitivos gerador = new PadroesRepetitivos();
        
        // Criar sequências de teste
        List<String> sequencia1 = Arrays.asList("analise", "decisao", "execucao", "avaliacao");
        List<String> sequencia2 = Arrays.asList("analise", "decisao", "execucao", "avaliacao");
        List<String> sequencia3 = Arrays.asList("coleta", "processamento", "decisao", "execucao", "avaliacao");
        
        // Identificar padrões
        System.out.println("Identificando padrões...");
        Map<String, Object> padrao1 = gerador.identificarPadrao(sequencia1);
        System.out.println("Padrão 1: " + padrao1.get("nome"));
        
        Map<String, Object> padrao2 = gerador.identificarPadrao(sequencia2);
        System.out.println("Padrão 2: " + padrao2.get("nome"));
        
        Map<String, Object> padrao3 = gerador.identificarPadrao(sequencia3);
        System.out.println("Padrão 3: " + padrao3.get("nome"));
        
        // Otimizar sequência longa
        List<String> sequenciaLonga = Arrays.asList(
            "analise", "analise", "decisao", "decisao", 
            "execucao", "execucao", "avaliacao", "avaliacao"
        );
        
        System.out.println("\nSequência original: " + sequenciaLonga);
        List<String> sequenciaOtimizada = gerador.otimizarSequencia(sequenciaLonga);
        System.out.println("Sequência otimizada: " + sequenciaOtimizada);
        
        // Estatísticas
        System.out.println("\nEstatísticas:");
        System.out.println("  Padrões aprendidos: " + gerador.getPadroesAprendidos().size());
        
        for (Map.Entry<String, Map<String, Object>> entry : gerador.getPadroesAprendidos().entrySet()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> padrao = entry.getValue();
            System.out.println("    " + entry.getKey() + ": frequência " + padrao.get("frequencia"));
        }
    }
}
