package modulo_emocional.Emocional;

import java.util.*;

/**
 * Mapeador de estados emocionais
 */
public class MapeadorEmocional {
    
    private Map<String, double[]> emocoesMap;
    private Random random;
    
    public MapeadorEmocional() {
        this.emocoesMap = new HashMap<>();
        this.random = new Random();
        inicializarEmocoes();
    }
    
    /**
     * Inicializa o mapeamento de emoções
     */
    private void inicializarEmocoes() {
        // Felicidade
        emocoesMap.put("felicidade", new double[]{0.8, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0});
        
        // Tristeza
        emocoesMap.put("tristeza", new double[]{0.7, 0.2, 0.1, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.4});
        
        // Raiva
        emocoesMap.put("raiva", new double[]{0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0});
        
        // Medo
        emocoesMap.put("medo", new double[]{0.5, 0.4, 0.3, 0.2, 0.1, 0.0, -0.1, -0.2, -0.3, -0.4});
        
        // Surpresa
        emocoesMap.put("surpresa", new double[]{0.6, 0.8, 0.9, 0.7, 0.5, 0.3, 0.1, 0.0, -0.1});
        
        // Confiança
        emocoesMap.put("confianca", new double[]{0.7, 0.8, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2});
        
        // Ansiedade
        emocoesMap.put("ansiedade", new double[]{0.8, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0});
        
        // Calma
        emocoesMap.put("calma", new double[]{0.4, 0.3, 0.2, 0.1, 0.0, -0.1, -0.2, -0.3, -0.4, -0.5});
        
        // Medo (segunda instância para variação)
        emocoesMap.put("medo_alternativo", new double[]{0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0, -0.1, -0.2, -0.3});
    }
    
    /**
     * Mapeia uma emoção para vetor numérico
     */
    public double[] mapearEmocao(String emocao) {
        double[] vetor = emocoesMap.get(emocao);
        
        if (vetor == null) {
            // Emoção não reconhecida - retorna vetor neutro
            return new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
        }
        
        // Adicionar pequena variação aleatória para simular nuances
        double[] vetorComVariacao = new double[vetor.length];
        System.arraycopy(vetor, 0, vetorComVariacao, vetor.length);
        
        for (int i = 0; i < vetor.length; i++) {
            double variacao = (random.nextDouble() - 0.5) * 0.1; // ±5% variação
            vetorComVariacao[i] = Math.max(0.0, Math.min(1.0, vetor[i] + variacao));
        }
        
        return vetorComVariacao;
    }
    
    /**
     * Obtém todas as emoções disponíveis
     */
    public Set<String> getEmocoesDisponiveis() {
        return emocoesMap.keySet();
    }
    
    /**
     * Obtém o vetor para uma emoção específica
     */
    public double[] getVetorEmocao(String emocao) {
        return emocoesMap.get(emocao);
    }
    
    /**
     * Calcula distância entre dois vetores emocionais
     */
    public double calcularDistanciaEmocional(double[] vetor1, double[] vetor2) {
        if (vetor1 == null || vetor2 == null || 
            vetor1.length != vetor2.length) {
            return Double.MAX_VALUE;
        }
        
        double somaDiferencas = 0;
        for (int i = 0; i < vetor1.length; i++) {
            double diff = Math.abs(vetor1[i] - vetor2[i]);
            somaDiferencas += diff;
        }
        
        return somaDiferencas / vetor1.length;
    }
    
    /**
     * Encontra a emoção mais próxima de um vetor
     */
    public String encontrarEmocaoProxima(double[] vetorAlvo) {
        String emocaoProxima = null;
        double menorDistancia = Double.MAX_VALUE;
        
        for (Map.Entry<String, double[]> entry : emocoesMap.entrySet()) {
            double[] vetorEmocao = entry.getValue();
            double distancia = calcularDistanciaEmocional(vetorAlvo, vetorEmocao);
            
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                emocaoProxima = entry.getKey();
            }
        }
        
        return emocaoProxima;
    }
    
    /**
     * Gera vetor emocional aleatório
     */
    public double[] gerarVetorEmocionalAleatorio() {
        List<String> emocoes = new ArrayList<>(emocoesMap.keySet());
        String emocaoAleatoria = emocoes.get(random.nextInt(emocoes.size()));
        
        return mapearEmocao(emocaoAleatoria);
    }
    
    /**
     * Normaliza um vetor emocional
     */
    public double[] normalizarVetor(double[] vetor) {
        double[] vetorNormalizado = new double[vetor.length];
        double soma = 0;
        
        // Calcular soma
        for (double valor : vetor) {
            soma += valor;
        }
        
        if (soma == 0) {
            return vetor; // Evitar divisão por zero
        }
        
        // Normalizar para soma = 1
        for (int i = 0; i < vetor.length; i++) {
            vetorNormalizado[i] = vetor[i] / soma;
        }
        
        return vetorNormalizado;
    }
    
    /**
     * Analisa a dominância emocional de um vetor
     */
    public String analisarDominanciaEmocional(double[] vetor) {
        double[] vetorNormalizado = normalizarVetor(vetor);
        
        double maxValor = 0;
        int indiceDominante = 0;
        
        for (int i = 0; i < vetorNormalizado.length; i++) {
            if (vetorNormalizado[i] > maxValor) {
                maxValor = vetorNormalizado[i];
                indiceDominante = i;
            }
        }
        
        String[] emocoes = {"felicidade", "tristeza", "raiva", "medo", "surpresa", 
                              "confianca", "ansiedade", "calma"};
        
        if (indiceDominante < emocoes.length) {
            return emocoes[indiceDominante];
        }
        
        return "neutra";
    }
    
    /**
     * Método de demonstração
     */
    public static void demonstracao() {
        System.out.println("=== Demonstração: Mapeador Emocional ===");
        
        MapeadorEmocional mapeador = new MapeadorEmocional();
        
        // Exibir emoções disponíveis
        System.out.println("\n1. Emoções disponíveis:");
        for (String emocao : mapeador.getEmocoesDisponiveis()) {
            System.out.println("  • " + emocao);
        }
        
        // Mapear emoções de exemplo
        System.out.println("\n2. Mapeando emoções de exemplo:");
        
        String[] emocoesExemplo = {"felicidade", "tristeza", "raiva", "ansiedade"};
        
        for (String emocao : emocoesExemplo) {
            System.out.println("\n" + emocao + ":");
            double[] vetor = mapeador.mapearEmocao(emocao);
            System.out.print("  Vetor: [");
            for (int i = 0; i < vetor.length; i++) {
                System.out.printf("%.2f", vetor[i]);
                if (i < vetor.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
            
            // Análise do vetor
            String dominante = mapeador.analisarDominanciaEmocional(vetor);
            System.out.println("  Emoção dominante: " + dominante);
            
            // Encontrar emoção mais próxima
            String proxima = mapeador.encontrarEmocaoProxima(vetor);
            System.out.println("  Próxima emoção: " + proxima);
        }
        
        // Calcular distâncias entre emoções
        System.out.println("\n3. Calculando distâncias emocionais:");
        
        double[] vetorFeliz = mapeador.mapearEmocao("felicidade");
        double[] vetorTriste = mapeador.mapearEmocao("tristeza");
        double[] vetorRaiva = mapeador.mapearEmocao("raiva");
        
        double distanciaFelizTriste = mapeador.calcularDistanciaEmocional(vetorFeliz, vetorTriste);
        double distanciaFelizRaiva = mapeador.calcularDistanciaEmocional(vetorFeliz, vetorRaiva);
        double distanciaTristeRaiva = mapeador.calcularDistanciaEmocional(vetorTriste, vetorRaiva);
        
        System.out.println("  Distância Felicidade-Tristeza: " + String.format("%.3f", distanciaFelizTriste));
        System.out.println("  Distância Felicidade-Raiva: " + String.format("%.3f", distanciaFelizRaiva));
        System.out.println("  Distância Tristeza-Raiva: " + String.format("%.3f", distanciaTristeRaiva));
        
        // Gerar vetor aleatório e analisar
        System.out.println("\n4. Vetor emocional aleatório:");
        double[] vetorAleatorio = mapeador.gerarVetorEmocionalAleatorio();
        System.out.print("  Vetor: [");
        for (int i = 0; i < vetorAleatorio.length; i++) {
            System.out.printf("%.2f", vetorAleatorio[i]);
            if (i < vetorAleatorio.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        String dominanteAleatorio = mapeador.analisarDominanciaEmocional(vetorAleatorio);
        System.out.println("  Emoção dominante: " + dominanteAleatorio);
        
        System.out.println("\n✅ Demonstração concluída com sucesso!");
    }
}
