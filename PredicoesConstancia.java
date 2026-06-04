package modulo_emocional.Motor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manutenção da constância e predições
 */
public class PredicoesConstancia {
    
    private double taxaAprendizado;
    private Map<String, Map<String, Object>> modeloPredicoes;
    private List<Map<String, Object>> historicoContextos;
    private double constanciaAlvo; // Meta de constância (0 a 1)
    
    public PredicoesConstancia(double taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
        this.modeloPredicoes = new HashMap<>();
        this.historicoContextos = new ArrayList<>();
        this.constanciaAlvo = 0.9;
    }
    
    public PredicoesConstancia() {
        this(0.1); // Taxa padrão
    }
    
    /**
     * Prevê possíveis perturbações baseadas no contexto
     */
    public Map<String, Object> preverPerturbacao(Map<String, Object> contextoAtual) {
        if (modeloPredicoes.isEmpty()) {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("predicoes", new ArrayList<>());
            resultado.put("confianca", 0);
            return resultado;
        }
        
        List<Map<String, Object>> predicoes = new ArrayList<>();
        double confiancaTotal = 0;
        
        // Compara com histórico de contextos similares
        int limite = Math.min(10, historicoContextos.size());
        for (int i = historicoContextos.size() - limite; i < historicoContextos.size(); i++) {
            Map<String, Object> contextoHistorico = historicoContextos.get(i);
            @SuppressWarnings("unchecked")
            Map<String, Object> contexto = (Map<String, Object>) contextoHistorico.get("contexto");
            
            double similaridade = calcularSimilaridade(contextoAtual, contexto);
            
            if (similaridade > 0.7) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> perturbacoes = (List<Map<String, Object>>) contextoHistorico.get("perturbacoes");
                
                for (Map<String, Object> perturbacao : perturbacoes) {
                    Map<String, Object> predicao = new HashMap<>();
                    predicao.put("tipo", perturbacao.get("tipo"));
                    predicao.put("probabilidade", (Double) perturbacao.get("probabilidade") * similaridade);
                    predicao.put("intensidade_esperada", perturbacao.get("intensidade"));
                    predicao.put("acao_recomendada", 
                        perturbacao.containsKey("acao_recomendada") ? perturbacao.get("acao_recomendada") : "monitorar");
                    
                    predicoes.add(predicao);
                    confiancaTotal += similaridade;
                }
            }
        }
        
        // Calcula confiança média
        double confianca = predicoes.isEmpty() ? 0 : confiancaTotal / predicoes.size();
        
        // Ordena predições por probabilidade
        predicoes.sort((a, b) -> Double.compare(
            (Double) b.get("probabilidade"), 
            (Double) a.get("probabilidade")
        ));
        
        // Limita às 3 melhores predições
        List<Map<String, Object>> topPredicoes = predicoes.size() > 3 ? 
            predicoes.subList(0, 3) : predicoes;
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("predicoes", topPredicoes);
        resultado.put("confianca", confianca);
        resultado.put("contexto_analisado", contextoAtual);
        
        return resultado;
    }
    
    /**
     * Calcula ajustes para manter constância
     */
    public Map<String, Object> manterConstancia(Map<String, Object> estadoAtual, Map<String, Object> estadoDesejado) {
        Map<String, Object> diferenca = calcularDiferenca(estadoAtual, estadoDesejado);
        double constanciaAtual = 1.0 - Math.min(1.0, (Double) diferenca.get("magnitude"));
        
        List<Map<String, Object>> ajustes = new ArrayList<>();
        
        if (constanciaAtual < constanciaAlvo) {
            // Calcula ajustes necessários
            @SuppressWarnings("unchecked")
            Map<String, Double> componentes = (Map<String, Double>) diferenca.get("componentes");
            
            for (Map.Entry<String, Double> entry : componentes.entrySet()) {
                Map<String, Object> ajuste = new HashMap<>();
                ajuste.put("componente", entry.getKey());
                ajuste.put("correcao", -entry.getValue() * taxaAprendizado);
                ajuste.put("prioridade", Math.abs(entry.getValue()));
                ajustes.add(ajuste);
            }
        }
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("constancia_atual", constanciaAtual);
        resultado.put("meta_constancia", constanciaAlvo);
        resultado.put("diferenca", diferenca);
        resultado.put("ajustes_recomendados", ajustes);
        resultado.put("necessita_ajuste", !ajustes.isEmpty());
        
        return resultado;
    }
    
    /**
     * Atualiza modelo com novas observações
     */
    public void atualizarModelo(Map<String, Object> contexto, List<Map<String, Object>> perturbacoesReais) {
        Map<String, Object> registro = new HashMap<>();
        registro.put("contexto", contexto);
        registro.put("perturbacoes", perturbacoesReais);
        registro.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        historicoContextos.add(registro);
        
        // Atualiza modelo preditivo
        for (Map<String, Object> perturbacao : perturbacoesReais) {
            String tipo = (String) perturbacao.get("tipo");
            String chave = (contexto.containsKey("tipo") ? (String) contexto.get("tipo") : "geral") + "_" + tipo;
            
            if (!modeloPredicoes.containsKey(chave)) {
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("contagem", 1);
                modelo.put("probabilidade_acumulada", 
                    perturbacao.containsKey("probabilidade") ? perturbacao.get("probabilidade") : 0.5);
                modeloPredicoes.put(chave, modelo);
            } else {
                Map<String, Object> modelo = modeloPredicoes.get(chave);
                modelo.put("contagem", (Integer) modelo.get("contagem") + 1);
                
                // Atualiza com média ponderada
                double antiga = (Double) modelo.get("probabilidade_acumulada");
                double nova = perturbacao.containsKey("probabilidade") ? (Double) perturbacao.get("probabilidade") : 0.5;
                modelo.put("probabilidade_acumulada", antiga * 0.7 + nova * 0.3);
            }
        }
    }
    
    /**
     * Calcula similaridade entre dois contextos (0 a 1)
     */
    private double calcularSimilaridade(Map<String, Object> contexto1, Map<String, Object> contexto2) {
        if (contexto1 == null || contexto2 == null || contexto1.isEmpty() || contexto2.isEmpty()) {
            return 0;
        }
        
        Set<String> chavesComuns = new HashSet<>(contexto1.keySet());
        chavesComuns.retainAll(contexto2.keySet());
        
        if (chavesComuns.isEmpty()) {
            return 0;
        }
        
        double similaridade = 0;
        for (String chave : chavesComuns) {
            Object valor1 = contexto1.get(chave);
            Object valor2 = contexto2.get(chave);
            
            if (valor1.equals(valor2)) {
                similaridade += 1;
            } else if (valor1 instanceof Number && valor2 instanceof Number) {
                // Para valores numéricos, calcula proximidade
                double diff = Math.abs(((Number) valor1).doubleValue() - ((Number) valor2).doubleValue());
                double maxVal = Math.max(Math.abs(((Number) valor1).doubleValue()), 
                                     Math.abs(((Number) valor2).doubleValue()));
                if (maxVal > 0) {
                    similaridade += 1 - (diff / maxVal);
                }
            }
        }
        
        return similaridade / chavesComuns.size();
    }
    
    /**
     * Calcula diferença entre estados
     */
    private Map<String, Object> calcularDiferenca(Map<String, Object> estadoAtual, Map<String, Object> estadoDesejado) {
        Map<String, Double> componentes = new HashMap<>();
        double magnitudeTotal = 0;
        
        Set<String> todasChaves = new HashSet<>();
        todasChaves.addAll(estadoAtual.keySet());
        todasChaves.addAll(estadoDesejado.keySet());
        
        for (String chave : todasChaves) {
            double atual = estadoAtual.containsKey(chave) ? ((Number) estadoAtual.get(chave)).doubleValue() : 0;
            double desejado = estadoDesejado.containsKey(chave) ? ((Number) estadoDesejado.get(chave)).doubleValue() : 0;
            
            double diff = desejado - atual;
            componentes.put(chave, diff);
            magnitudeTotal += diff * diff;
        }
        
        double magnitude = magnitudeTotal > 0 ? Math.sqrt(magnitudeTotal) : 0;
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("magnitude", magnitude);
        resultado.put("componentes", componentes);
        
        return resultado;
    }
    
    // Getters e Setters
    public double getTaxaAprendizado() {
        return taxaAprendizado;
    }
    
    public void setTaxaAprendizado(double taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
    }
    
    public double getConstanciaAlvo() {
        return constanciaAlvo;
    }
    
    public void setConstanciaAlvo(double constanciaAlvo) {
        this.constanciaAlvo = constanciaAlvo;
    }
    
    public Map<String, Map<String, Object>> getModeloPredicoes() {
        return new HashMap<>(modeloPredicoes);
    }
    
    public List<Map<String, Object>> getHistoricoContextos() {
        return new ArrayList<>(historicoContextos);
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Predições e Constância ===");
        
        PredicoesConstancia sistema = new PredicoesConstancia(0.1);
        
        // Contexto atual
        Map<String, Object> contextoAtual = new HashMap<>();
        contextoAtual.put("mercado", "volatil");
        contextoAtual.put("juros", "alta");
        contextoAtual.put("inflacao", "controlada");
        
        // Estado desejado
        Map<String, Object> estadoDesejado = new HashMap<>();
        estadoDesejado.put("estabilidade", 0.9);
        estadoDesejado.put("liquidez", 0.7);
        estadoDesejado.put("retorno", 0.6);
        
        // Estado atual (simulado)
        Map<String, Object> estadoAtual = new HashMap<>();
        estadoAtual.put("estabilidade", 0.7);
        estadoAtual.put("liquidez", 0.5);
        estadoAtual.put("retorno", 0.4);
        
        // Prever perturbações
        System.out.println("Prevendo perturbações...");
        Map<String, Object> predicoes = sistema.preverPerturbacao(contextoAtual);
        System.out.println("Confiança: " + String.format("%.2f", (Double) predicoes.get("confianca")));
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> listaPredicoes = (List<Map<String, Object>>) predicoes.get("predicoes");
        for (Map<String, Object> pred : listaPredicoes) {
            System.out.println("  Predição: " + pred.get("tipo") + 
                             " (prob: " + String.format("%.2f", (Double) pred.get("probabilidade")) + ")");
        }
        
        // Manter constância
        System.out.println("\nAnalisando constância...");
        Map<String, Object> constancia = sistema.manterConstancia(estadoAtual, estadoDesejado);
        System.out.println("Constância atual: " + String.format("%.2f", (Double) constancia.get("constancia_atual")));
        System.out.println("Meta: " + String.format("%.2f", (Double) constancia.get("meta_constancia")));
        System.out.println("Necessita ajuste: " + constancia.get("necessita_ajuste"));
        
        // Atualizar modelo com perturbações reais
        List<Map<String, Object>> perturbacoesReais = new ArrayList<>();
        Map<String, Object> pert1 = new HashMap<>();
        pert1.put("tipo", "volatilidade_mercado");
        pert1.put("probabilidade", 0.8);
        pert1.put("intensidade", 0.6);
        perturbacoesReais.add(pert1);
        
        sistema.atualizarModelo(contextoAtual, perturbacoesReais);
        
        // Estatísticas
        System.out.println("\nEstatísticas:");
        System.out.println("  Contextos no histórico: " + sistema.getHistoricoContextos().size());
        System.out.println("  Modelos de predição: " + sistema.getModeloPredicoes().size());
    }
}
