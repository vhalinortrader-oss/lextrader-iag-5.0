

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * LEXTRADER-IAG 4.0 - Sistema Principal
 * ======================================
 * Sistema de Inteligência Artificial Avançada para Trading e Análise de Mercados
 * 
 * Autor: LEXTRADER Team
 * Versão: 4.0.0
 * Data: Janeiro 2026
 */
public class Main {
    
    private String version = "4.0.0";
    private String name = "LEXTRADER-IAG";
    private boolean initialized = false;
    
    // Analisadores
    private AdvancedCryptoAnalyzer cryptoAnalyzer;
    private AdvancedForexAnalyzer forexAnalyzer;
    private AdvancedArbitrageAnalyzer arbitrageAnalyzer;
    private UnifiedMarketAnalyzer unifiedAnalyzer;
    
    // Sistemas avançados
    private AdvancedIntegrationManager integrationManager;
    private InteligenciaArtificialCentral iaCentral;
    
    public Main() {
        System.out.println("🚀 " + name + " v" + version + " inicializado");
    }
    
    /**
     * Inicializa todos os sistemas
     */
    public void initialize() {
        System.out.println("================================================================================");
        System.out.println("🚀 Inicializando " + name + " v" + version);
        System.out.println("================================================================================");
        
        // Inicializar sistemas de análise de mercados (simulados)
        System.out.println("📊 Inicializando sistemas de análise de mercados...");
        try {
            this.cryptoAnalyzer = new AdvancedCryptoAnalyzer();
            System.out.println("  ✅ Crypto Analyzer inicializado");
        } catch (Exception e) {
            System.err.println("  ❌ Erro ao inicializar Crypto Analyzer: " + e.getMessage());
        }
        
        try {
            this.forexAnalyzer = new AdvancedForexAnalyzer();
            System.out.println("  ✅ Forex Analyzer inicializado");
        } catch (Exception e) {
            System.err.println("  ❌ Erro ao inicializar Forex Analyzer: " + e.getMessage());
        }
        
        try {
            this.arbitrageAnalyzer = new AdvancedArbitrageAnalyzer();
            System.out.println("  ✅ Arbitrage Analyzer inicializado");
        } catch (Exception e) {
            System.err.println("  ❌ Erro ao inicializar Arbitrage Analyzer: " + e.getMessage());
        }
        
        try {
            this.unifiedAnalyzer = new UnifiedMarketAnalyzer();
            System.out.println("  ✅ Unified Analyzer inicializado");
        } catch (Exception e) {
            System.err.println("  ❌ Erro ao inicializar Unified Analyzer: " + e.getMessage());
        }
        
        // Inicializar sistemas avançados (simulados)
        System.out.println("🔧 Inicializando Advanced Integration Manager...");
        try {
            this.integrationManager = new AdvancedIntegrationManager();
            Map<String, Object> status = this.integrationManager.getSystemsStatus();
            System.out.println("  ✅ Integration Manager inicializado");
            System.out.println("  📊 Sistemas ativos: " + status.get("active_systems"));
            System.out.println("  ⚠️ Sistemas inativos: " + status.get("inactive_systems"));
        } catch (Exception e) {
            System.err.println("  ❌ Erro ao inicializar Integration Manager: " + e.getMessage());
        }
        
        try {
            this.iaCentral = new InteligenciaArtificialCentral();
            System.out.println("  ✅ IA Central inicializada");
        } catch (Exception e) {
            System.err.println("  ❌ Erro ao inicializar IA Central: " + e.getMessage());
        }
        
        this.initialized = true;
        System.out.println("================================================================================");
        System.out.println("✅ Sistema inicializado com sucesso!");
        System.out.println("================================================================================");
    }
    
    /**
     * Analisa criptomoeda
     */
    public CompletableFuture<AnalysisResult> analyzeCrypto(String symbol, String timeframe) {
        if (cryptoAnalyzer == null) {
            System.err.println("❌ Crypto Analyzer não disponível");
            return CompletableFuture.completedFuture(null);
        }
        
        System.out.println("🪙 Analisando " + symbol + "...");
        return CompletableFuture.supplyAsync(() -> {
            try {
                AnalysisResult result = cryptoAnalyzer.analyze(symbol, timeframe);
                
                System.out.println("  Preço: $" + String.format("%.2f", result.getPrice()));
                System.out.println("  Sinal: " + result.getSignal());
                System.out.println("  Confiança: " + String.format("%.1f%%", result.getConfidence()));
                System.out.println("  Score Técnico: " + String.format("%.1f/100", result.getTechnicalScore()));
                System.out.println("  Risco: " + String.format("%.1f/100", result.getRiskScore()));
                System.out.println("  Previsão 24h: $" + String.format("%.2f", result.getPredictedPrice24h()));
                
                return result;
            } catch (Exception e) {
                System.err.println("❌ Erro na análise de crypto: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Analisa par de forex
     */
    public CompletableFuture<AnalysisResult> analyzeForex(String pair, String timeframe) {
        if (forexAnalyzer == null) {
            System.err.println("❌ Forex Analyzer não disponível");
            return CompletableFuture.completedFuture(null);
        }
        
        System.out.println("💱 Analisando " + pair + "...");
        return CompletableFuture.supplyAsync(() -> {
            try {
                AnalysisResult result = forexAnalyzer.analyze(pair, timeframe);
                
                System.out.println("  Bid/Ask: " + String.format("%.5f / %.5f", result.getBid(), result.getAsk()));
                System.out.println("  Sinal: " + result.getSignal());
                System.out.println("  Confiança: " + String.format("%.1f%%", result.getConfidence()));
                System.out.println("  Sessão: " + result.getSession());
                System.out.println("  Tendência: " + result.getTrendStrength());
                
                return result;
            } catch (Exception e) {
                System.err.println("❌ Erro na análise de forex: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Escaneia oportunidades de arbitragem
     */
    public CompletableFuture<ArbitrageResult> scanArbitrage(List<String> assets, List<String> exchanges) {
        if (arbitrageAnalyzer == null) {
            System.err.println("❌ Arbitrage Analyzer não disponível");
            return CompletableFuture.completedFuture(null);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                ArbitrageResult result = arbitrageAnalyzer.scanAllOpportunities(assets, exchanges);

                System.out.println("  Oportunidades: " + result.getOpportunitiesFound());
                System.out.println("  Lucro Total: " + String.format("%.2f%%", result.getTotalProfitPotential()));

                if (result.getBestOpportunity() != null) {
                    ArbitrageOpportunity best = result.getBestOpportunity();
                    System.out.println("  Melhor: " + best.getType() + " - " + best.getAsset());
                    System.out.println("  Lucro Líquido: " + String.format("%.2f%%", best.getNetProfit()));
                    System.out.println("  Risco: " + best.getRiskScore() + "/100");
                }

                return result;
            } catch (Exception e) {
                System.err.println("❌ Erro no scan de arbitragem: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Análise unificada de todos os mercados
     */
    public CompletableFuture<UnifiedAnalysisResult> analyzeUnified(List<String> cryptoSymbols, 
                                                          List<String> forexPairs,
                                                          List<String> arbitrageAssets, 
                                                          List<String> exchanges) {
        if (unifiedAnalyzer == null) {
            System.err.println("❌ Unified Analyzer não disponível");
            return CompletableFuture.completedFuture(null);
        }
        
        if (cryptoSymbols == null) {
            cryptoSymbols = Arrays.asList("BTC/USDT");
        }
        if (forexPairs == null) {
            forexPairs = Arrays.asList("EUR/USD");
        }
        if (arbitrageAssets == null) {
            arbitrageAssets = Arrays.asList("BTC/USDT", "ETH/USDT");
        }
        if (exchanges == null) {
            exchanges = Arrays.asList("binance", "coinbase");
        }

        final List<String> cryptoSymbolsFinal = cryptoSymbols;
        final List<String> forexPairsFinal = forexPairs;
        final List<String> arbitrageAssetsFinal = arbitrageAssets;
        final List<String> exchangesFinal = exchanges;
        
        System.out.println("🌐 Análise unificada de mercados...");
        return CompletableFuture.supplyAsync(() -> {
            try {
                UnifiedAnalysisResult result = unifiedAnalyzer.analyzeAllMarkets(
                    cryptoSymbolsFinal, forexPairsFinal, arbitrageAssetsFinal, exchangesFinal);
                
                System.out.println("  Sinal Geral: " + result.getOverallSignal());
                System.out.println("  Confiança: " + String.format("%.1f%%", result.getConfidence()));
                System.out.println("  Risco Geral: " + String.format("%.1f/100", result.getRiskAssessment().get("overall_risk")));
                System.out.println("  Mercados: " + result.getMetadata().get("markets_analyzed"));
                
                return result;
            } catch (Exception e) {
                System.err.println("❌ Erro na análise unificada: " + e.getMessage());
                return null;
            }
        });
    }
    
    /**
     * Executa análise completa de todos os mercados
     */
    public CompletableFuture<Map<String, Object>> runFullAnalysis() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 ANÁLISE COMPLETA DE MERCADOS");
        System.out.println("=".repeat(80));
        
        Map<String, Object> results = new HashMap<>();
        
        // Análise de Crypto
        CompletableFuture<AnalysisResult> cryptoFuture = analyzeCrypto("BTC/USDT", "1h");
        
        // Análise de Forex
        CompletableFuture<AnalysisResult> forexFuture = analyzeForex("EUR/USD", "1h");
        
        // Análise de Arbitragem
        CompletableFuture<ArbitrageResult> arbFuture = scanArbitrage(null, null);
        
        // Análise Unificada
        CompletableFuture<UnifiedAnalysisResult> unifiedFuture = analyzeUnified(null, null, null, null);
        
        return CompletableFuture.allOf(cryptoFuture, forexFuture, arbFuture, unifiedFuture)
            .thenApply(v -> {
                try {
                    AnalysisResult cryptoResult = cryptoFuture.get();
                    if (cryptoResult != null) {
                        results.put("crypto", cryptoResult);
                    }
                    
                    AnalysisResult forexResult = forexFuture.get();
                    if (forexResult != null) {
                        results.put("forex", forexResult);
                    }
                    
                    ArbitrageResult arbResult = arbFuture.get();
                    if (arbResult != null) {
                        results.put("arbitrage", arbResult);
                    }
                    
                    UnifiedAnalysisResult unifiedResult = unifiedFuture.get();
                    if (unifiedResult != null) {
                        results.put("unified", unifiedResult);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao compilar resultados: " + e.getMessage());
                }
                
                System.out.println("\n" + "=".repeat(80));
                System.out.println("✅ ANÁLISE COMPLETA CONCLUÍDA");
                System.out.println("=".repeat(80));
                
                return results;
            });
    }
    
    /**
     * Retorna status do sistema
     */
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("version", version);
        status.put("initialized", initialized);
        status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        Map<String, Boolean> systems = new HashMap<>();
        systems.put("crypto_analyzer", cryptoAnalyzer != null);
        systems.put("forex_analyzer", forexAnalyzer != null);
        systems.put("arbitrage_analyzer", arbitrageAnalyzer != null);
        systems.put("unified_analyzer", unifiedAnalyzer != null);
        systems.put("integration_manager", integrationManager != null);
        systems.put("ia_central", iaCentral != null);
        status.put("systems", systems);
        
        if (integrationManager != null) {
            status.put("advanced_systems", integrationManager.getSystemsStatus());
        }
        
        return status;
    }
    
    /**
     * Imprime banner do sistema
     */
    public void printBanner() {
        String banner = 
            "╔══════════════════════════════════════════════════════════════════════════╗\n" +
            "║                                                                              ║\n" +
            "║                        LEXTRADER-IAG 4.0                                     ║\n" +
            "║                                                                              ║\n" +
            "║           Sistema de IA Avançada para Trading e Análise de Mercados         ║\n" +
            "║                                                                              ║\n" +
            "║  🪙 Análise de Criptomoedas  |  💱 Análise de Forex                         ║\n" +
            "║  ⚡ Análise de Arbitragem    |  🌐 Análise Unificada                        ║\n" +
            "║  🧠 IA Central               |  🔧 Sistemas Avançados                        ║\n" +
            "║                                                                              ║\n" +
            "║  Versão: " + version + "                                                         ║\n" +
            "║  Status: " + (initialized ? "🟢 OPERACIONAL" : "🟡 INICIALIZANDO") + "                                                    ║\n" +
            "║  Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "                                              ║\n" +
            "║                                                                              ║\n" +
            "╚══════════════════════════════════════════════════════════════════════╝";
        
        System.out.println(banner);
    }
    
    /**
     * Menu interativo
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("MENU PRINCIPAL");
            System.out.println("=".repeat(80));
            System.out.println("1. Analisar Criptomoeda (BTC/USDT)");
            System.out.println("2. Analisar Forex (EUR/USD)");
            System.out.println("3. Escanear Arbitragem");
            System.out.println("4. Análise Unificada");
            System.out.println("5. Análise Completa (Todos os Mercados)");
            System.out.println("6. Ver Status do Sistema");
            System.out.println("7. Executar Testes");
            System.out.println("0. Sair");
            System.out.println("=".repeat(80));
            
            try {
                System.out.print("\nEscolha uma opção: ");
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1":
                        System.out.print("Símbolo (padrão BTC/USDT): ");
                        String symbol = scanner.nextLine().trim();
                        if (symbol.isEmpty()) symbol = "BTC/USDT";
                        
                        analyzeCrypto(symbol, "1h")
                            .thenAccept(result -> {
                                if (result != null) {
                                    System.out.println("Análise concluída com sucesso!");
                                }
                            });
                        break;
                        
                    case "2":
                        System.out.print("Par (padrão EUR/USD): ");
                        String pair = scanner.nextLine().trim();
                        if (pair.isEmpty()) pair = "EUR/USD";
                        
                        analyzeForex(pair, "1h")
                            .thenAccept(result -> {
                                if (result != null) {
                                    System.out.println("Análise concluída com sucesso!");
                                }
                            });
                        break;
                        
                    case "3":
                        scanArbitrage(null, null)
                            .thenAccept(result -> {
                                if (result != null) {
                                    System.out.println("Scan concluído com sucesso!");
                                }
                            });
                        break;
                        
                    case "4":
                        analyzeUnified(null, null, null, null)
                            .thenAccept(result -> {
                                if (result != null) {
                                    System.out.println("Análise unificada concluída com sucesso!");
                                }
                            });
                        break;
                        
                    case "5":
                        runFullAnalysis()
                            .thenAccept(results -> {
                                System.out.println("Análise completa concluída!");
                            });
                        break;
                        
                    case "6":
                        Map<String, Object> status = getStatus();
                        System.out.println("\n📊 STATUS DO SISTEMA:");
                        System.out.println("  Versão: " + status.get("version"));
                        System.out.println("  Inicializado: " + (status.get("initialized").equals(true) ? "✅" : "❌"));
                        System.out.println("  Timestamp: " + status.get("timestamp"));
                        System.out.println("\n  Sistemas:");
                        
                        @SuppressWarnings("unchecked")
                        Map<String, Boolean> systems = (Map<String, Boolean>) status.get("systems");
                        for (Map.Entry<String, Boolean> entry : systems.entrySet()) {
                            System.out.println("    " + entry.getKey() + ": " + (entry.getValue() ? "✅" : "❌"));
                        }
                        break;
                        
                    case "7":
                        System.out.println("\n🧪 Executando testes...");
                        if (iaCentral == null) {
                            System.out.println("IA Central não inicializada.");
                        } else {
                            Map<String, Object> input = new HashMap<>();
                            input.put("features", Arrays.asList(0.12, 0.34, 0.56, 0.78, 1.2, -0.4, 0.9));


                            Map<String, Object> historicalData = new HashMap<>();
                            historicalData.put("data", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
                            input.put("historicalData", historicalData);

                            Map<String, Object> res = iaCentral.predictDecision(input);
                            System.out.println("Resultado IA Central (Node): " + res);
                            System.out.println("Testes concluídos.");
                        }

                        break;
                        
                    case "0":
                        System.out.println("\n👋 Encerrando LEXTRADER-IAG 4.0...");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("❌ Opção inválida!");
                        break;
                }
                
            } catch (Exception e) {
                System.err.println("❌ Erro: " + e.getMessage());
            }
        }
    }
    
    /**
     * Método principal
     */
    public static void main(String[] args) {
        try {
            Main system = new Main();
            system.printBanner();
            
            // Inicializar
            system.initialize();
            
            // Menu interativo
            system.showMenu();
            
        } catch (Exception e) {
            System.err.println("💥 Erro crítico: " + e.getMessage());
            System.exit(1);
        }
    }
}

// Classes de resultado simuladas para compatibilidade
class AnalysisResult {
    private double price;
    private String signal;
    private double confidence;
    private double technicalScore;
    private double riskScore;
    private double predictedPrice24h;
    private double bid;
    private double ask;
    private String session;
    private String trendStrength;
    
    // Constructors e getters/setters
    public AnalysisResult() {}
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getSignal() { return signal; }
    public void setSignal(String signal) { this.signal = signal; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public double getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(double technicalScore) { this.technicalScore = technicalScore; }
    public double getRiskScore() { return riskScore; }
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    public double getPredictedPrice24h() { return predictedPrice24h; }
    public void setPredictedPrice24h(double predictedPrice24h) { this.predictedPrice24h = predictedPrice24h; }
    public double getBid() { return bid; }
    public void setBid(double bid) { this.bid = bid; }
    public double getAsk() { return ask; }
    public void setAsk(double ask) { this.ask = ask; }
    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }
    public String getTrendStrength() { return trendStrength; }
    public void setTrendStrength(String trendStrength) { this.trendStrength = trendStrength; }
}

class ArbitrageResult {
    private int opportunitiesFound;
    private double totalProfitPotential;
    private ArbitrageOpportunity bestOpportunity;
    
    public ArbitrageResult() {}
    
    public int getOpportunitiesFound() { return opportunitiesFound; }
    public void setOpportunitiesFound(int opportunitiesFound) { this.opportunitiesFound = opportunitiesFound; }
    public double getTotalProfitPotential() { return totalProfitPotential; }
    public void setTotalProfitPotential(double totalProfitPotential) { this.totalProfitPotential = totalProfitPotential; }
    public ArbitrageOpportunity getBestOpportunity() { return bestOpportunity; }
    public void setBestOpportunity(ArbitrageOpportunity bestOpportunity) { this.bestOpportunity = bestOpportunity; }
}

class ArbitrageOpportunity {
    private String type;
    private String asset;
    private double netProfit;
    private int riskScore;
    
    public ArbitrageOpportunity() {}
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }
    public double getNetProfit() { return netProfit; }
    public void setNetProfit(double netProfit) { this.netProfit = netProfit; }
    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
}

class UnifiedAnalysisResult {
    private String overallSignal;
    private double confidence;
    private Map<String, Object> riskAssessment;
    private Map<String, Object> metadata;
    
    public UnifiedAnalysisResult() {}
    
    public String getOverallSignal() { return overallSignal; }
    public void setOverallSignal(String overallSignal) { this.overallSignal = overallSignal; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public Map<String, Object> getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(Map<String, Object> riskAssessment) { this.riskAssessment = riskAssessment; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}

// Classes simuladas para dependências
class AdvancedCryptoAnalyzer {
    public AnalysisResult analyze(String symbol, String timeframe) {
        AnalysisResult result = new AnalysisResult();
        result.setPrice(45000 + Math.random() * 5000);
        result.setSignal(Math.random() > 0.5 ? "BUY" : "SELL");
        result.setConfidence(70 + Math.random() * 30);
        result.setTechnicalScore(Math.random() * 100);
        result.setRiskScore((int)(Math.random() * 100));
        result.setPredictedPrice24h(result.getPrice() * (0.95 + Math.random() * 0.1));
        return result;
    }
}

class AdvancedForexAnalyzer {
    public AnalysisResult analyze(String pair, String timeframe) {
        AnalysisResult result = new AnalysisResult();
        result.setBid(1.0850 + Math.random() * 0.01);
        result.setAsk(1.0855 + Math.random() * 0.01);
        result.setSignal(Math.random() > 0.5 ? "BUY" : "SELL");
        result.setConfidence(65 + Math.random() * 35);
        result.setSession(Math.random() > 0.5 ? "LONDON" : "NEW_YORK");
        result.setTrendStrength(Math.random() > 0.5 ? "STRONG_UP" : "STRONG_DOWN");
        return result;
    }
}

class AdvancedArbitrageAnalyzer {
    public ArbitrageResult scanAllOpportunities(List<String> assets, List<String> exchanges) {
        ArbitrageResult result = new ArbitrageResult();
        result.setOpportunitiesFound((int)(Math.random() * 10));
        result.setTotalProfitPotential(Math.random() * 5);
        
        if (Math.random() > 0.7) {
            ArbitrageOpportunity best = new ArbitrageOpportunity();
            best.setType("TRIANGULAR");
            best.setAsset("BTC/USDT");
            best.setNetProfit(Math.random() * 2);
            best.setRiskScore((int)(Math.random() * 50));
            result.setBestOpportunity(best);
        }
        
        return result;
    }
}

class UnifiedMarketAnalyzer {
    public UnifiedAnalysisResult analyzeAllMarkets(List<String> cryptoSymbols, 
                                               List<String> forexPairs,
                                               List<String> arbitrageAssets, 
                                               List<String> exchanges) {
        UnifiedAnalysisResult result = new UnifiedAnalysisResult();
        result.setOverallSignal(Math.random() > 0.5 ? "HOLD" : "BUY");
        result.setConfidence(75 + Math.random() * 25);
        
        Map<String, Object> risk = new HashMap<>();
        risk.put("overall_risk", Math.random() * 100);
        result.setRiskAssessment(risk);
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("markets_analyzed", "crypto,forex,arbitrage");
        result.setMetadata(meta);
        
        return result;
    }
}

class AdvancedIntegrationManager {
    public Map<String, Object> getSystemsStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("active_systems", Arrays.asList("crypto_analyzer", "forex_analyzer"));
        status.put("inactive_systems", Arrays.asList("portfolio_manager", "risk_manager"));
        return status;
    }
}

class InteligenciaArtificialCentral {
    // Tenta binários comuns do Node (robusto para ambientes diferentes)
    private final String[] nodeCmds = new String[] {"nodejs", "node"};
    private final String scriptPath = "advanced_ai_system.js";


    public InteligenciaArtificialCentral() {
        // Integração Java -> Node (advanced_ai_system.js)
    }

    public Map<String, Object> predictDecision(Map<String, Object> input) {
        try {
            // Monta JSON de entrada para o JS
            String featuresJson = toJson(input.get("features"));
            String historicalJson = toJson(input.get("historicalData"));

            String jsonArg = "{" +
                    "\"features\":" + featuresJson + "," +
                    "\"historicalData\":" + historicalJson +
                    "}";

            // Escolhe o primeiro binário de node disponível no sistema
            String chosenNode = null;
            for (String cmd : nodeCmds) {
                try {
                    // "which" depende do PATH e ajuda a validar disponibilidade
                    Process check = new ProcessBuilder("sh", "-c", "command -v " + cmd).start();
                    int code = check.waitFor();
                    if (code == 0) {
                        chosenNode = cmd;
                        break;
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    // continua tentando
                }
            }

            if (chosenNode == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Node.js não encontrado no PATH (nem node nem nodejs). Instale Node.js LTS.");
                return err;
            }

            ProcessBuilder pb = new ProcessBuilder(chosenNode, scriptPath, jsonArg);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // Lê stdout+stderr unificados
            String output = new String(p.getInputStream().readAllBytes());
            p.waitFor(10, TimeUnit.SECONDS);

            // A saída deve ser JSON único.
            Map<String, Object> parsed = fromJson(output);
            return parsed;
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", String.valueOf(e.getMessage()));
            return err;
        }
    }

    // Helpers mínimos (sem libs externas)
    private String toJson(Object v) {
        if (v == null) return "null";
        if (v instanceof String) return "\"" + escapeJson((String) v) + "\"";
        if (v instanceof Number || v instanceof Boolean) return String.valueOf(v);
        if (v instanceof java.util.List) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : (java.util.List<?>) v) {
                if (!first) sb.append(',');
                first = false;
                sb.append(toJson(item));
            }
            sb.append(']');
            return sb.toString();
        }
        if (v instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> m = (java.util.Map<String, Object>) v;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (java.util.Map.Entry<String, Object> e : m.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append("\"").append(escapeJson(e.getKey())).append("\"").append(":").append(toJson(e.getValue()));
            }
            sb.append('}');
            return sb.toString();
        }
        // fallback
        return "\"" + escapeJson(String.valueOf(v)) + "\"";
    }

    private Map<String, Object> fromJson(String s) {
        // Parser ultra simples para o formato específico do CLI (não genérico).
        // Esperado: {"decision":{...},"p":0.5,"confidence":0.7,"riskScore":0.2,...}
        // Para robustez real, troque por uma lib (Gson/Jackson). Aqui mantemos zero-deps.
        Map<String, Object> out = new HashMap<>();
        if (s == null) return out;
        String t = s.trim();
        if (t.startsWith("{") && t.endsWith("}")) {
            // Extrai chaves numéricas simples e o objeto decision como string JSON.
            // p/confidence/riskScore
            putIfNumber(out, t, "p");
            putIfNumber(out, t, "confidence");
            putIfNumber(out, t, "riskScore");
            putIfStringObject(out, t, "decision");
            putIfNumber(out, t, "timestamp");
            putIfString(out, t, "error");
        }
        return out;
    }

    private void putIfNumber(Map<String, Object> out, String json, String key) {
        String pattern = "\\\"" + key + "\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (m.find()) {
            String val = m.group(1);
            if (val.contains(".")) out.put(key, Double.parseDouble(val));
            else out.put(key, Long.parseLong(val));
        }
    }

    private void putIfString(Map<String, Object> out, String json, String key) {
        String pattern = "\\\"" + key + "\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (m.find()) out.put(key, m.group(1));
    }

    private void putIfStringObject(Map<String, Object> out, String json, String key) {
        // Captura o objeto {...} associado a "decision"
        int idx = json.indexOf("\"" + key + "\"");
        if (idx < 0) return;
        int colon = json.indexOf(':', idx);
        if (colon < 0) return;
        // procura '{' após o colon
        int start = json.indexOf('{', colon);
        if (start < 0) return;
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) {
                    out.put(key, json.substring(start, i + 1));
                    return;
                }
            }
        }
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
}

