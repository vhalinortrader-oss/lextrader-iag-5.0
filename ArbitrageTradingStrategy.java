package modulo_emocional.Decisao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Estratégia Avançada de Arbitragem - LEXTRADER-IAG 4.0
 * ==========================================================
 * Sistema de detecção e execução de oportunidades de arbitragem
 * Integrado com a Camada de Decisão (Layer 04) do LEXTRADER-IAG 4.0
 */

// Enums
enum ArbitrageType {
    SPATIAL,      // Entre exchanges
    TRIANGULAR,   // Triangular (3 ativos)
    STATISTICAL,  // Baseado em correlação
    CROSS_MARKET  // Entre mercados (crypto/forex)
}

// Classes de Dados
class ExchangePrice {
    String name;
    double price;
    double volume;
    
    public ExchangePrice(String name, double price, double volume) {
        this.name = name;
        this.price = price;
        this.volume = volume;
    }
    
    @Override
    public String toString() {
        return String.format("ExchangePrice{name='%s', price=%.6f, volume=%.2f}", name, price, volume);
    }
}

class ArbitrageOpportunity {
    ArbitrageType type;
    String asset;
    String buyExchange;
    String sellExchange;
    double buyPrice;
    double sellPrice;
    double profitPct;
    double maxVolume;
    LocalDateTime timestamp;
    
    // Campos específicos para diferentes tipos
    List<String> pairs;           // Para triangular
    List<String> currencies;       // Para triangular
    String direction;            // Para triangular
    double crossRate;            // Para triangular
    double directRate;           // Para triangular
    String asset1;               // Para estatística
    String asset2;               // Para estatística
    double correlation;           // Para estatística
    double zScore;                // Para estatística
    String action;                // Para estatística
    double expectedProfitPct;     // Para estatística
    String cryptoPair;            // Para cross-market
    String forexPair;            // Para cross-market
    String syntheticPair;         // Para cross-market
    double syntheticPrice;        // Para cross-market
    
    public ArbitrageOpportunity(ArbitrageType type, String asset, String buyExchange, String sellExchange,
                              double buyPrice, double sellPrice, double profitPct, double maxVolume) {
        this.type = type;
        this.asset = asset;
        this.buyExchange = buyExchange;
        this.sellExchange = sellExchange;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.profitPct = profitPct;
        this.maxVolume = maxVolume;
        this.timestamp = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("ArbitrageOpportunity{type=%s, profitPct=%.3f%%}", type, profitPct);
    }
}

class ArbitrageExecution {
    ArbitrageOpportunity opportunity;
    double volume;
    double expectedProfit;
    LocalDateTime executionTime;
    String status;
    
    public ArbitrageExecution(ArbitrageOpportunity opportunity, double volume, double expectedProfit) {
        this.opportunity = opportunity;
        this.volume = volume;
        this.expectedProfit = expectedProfit;
        this.executionTime = LocalDateTime.now();
        this.status = "EXECUTED";
    }
    
    @Override
    public String toString() {
        return String.format("ArbitrageExecution{volume=%.4f, expectedProfit=%.2f, status=%s}", 
                               volume, expectedProfit, status);
    }
}

// Classe Principal
class ArbitrageTradingStrategy {
    private double minProfitThreshold;
    private double maxExecutionTime;
    private List<ArbitrageOpportunity> opportunities;
    private List<ArbitrageExecution> executedTrades;
    private String cryptoDataPath;
    private String forexDataPath;
    private Random random;
    
    private static final Logger logger = Logger.getLogger(ArbitrageTradingStrategy.class.getName());
    
    public ArbitrageTradingStrategy(double minProfitThreshold, double maxExecutionTime) {
        this.minProfitThreshold = minProfitThreshold;
        this.maxExecutionTime = maxExecutionTime;
        this.opportunities = new ArrayList<>();
        this.executedTrades = new ArrayList<>();
        this.cryptoDataPath = "neural_layers/01_sensorial/crypto_data";
        this.forexDataPath = "neural_layers/01_sensorial/forex_data";
        this.random = new Random();
        
        logger.info("ArbitrageTradingStrategy inicializado");
    }
    
    // ==================== ARBITRAGEM ESPACIAL ====================
    
    public Optional<ArbitrageOpportunity> detectSpatialArbitrage(String asset, List<ExchangePrice> exchanges) {
        /** Detecta arbitragem espacial (mesmo ativo em diferentes exchanges) */
        if (exchanges == null || exchanges.size() < 2) {
            return Optional.empty();
        }
        
        // Ordenar exchanges por preço
        List<ExchangePrice> sortedExchanges = exchanges.stream()
            .sorted(Comparator.comparingDouble(ExchangePrice::getPrice))
            .collect(Collectors.toList());
        
        // Menor preço (compra) e maior preço (venda)
        ExchangePrice buyExchange = sortedExchanges.get(0);
        ExchangePrice sellExchange = sortedExchanges.get(sortedExchanges.size() - 1);
        
        // Calcular lucro potencial
        double profitPct = ((sellExchange.price - buyExchange.price) / buyExchange.price) * 100;
        
        // Considerar taxas (estimativa: 0.1% por transação)
        double fees = 0.2; // 0.1% compra + 0.1% venda
        double netProfitPct = profitPct - fees;
        
        if (netProfitPct >= minProfitThreshold) {
            ArbitrageOpportunity opportunity = new ArbitrageOpportunity(
                ArbitrageType.SPATIAL,
                asset,
                buyExchange.name,
                sellExchange.name,
                buyExchange.price,
                sellExchange.price,
                Math.round(netProfitPct * 1000) / 1000.0,
                Math.min(buyExchange.volume, sellExchange.volume)
            );
            
            return Optional.of(opportunity);
        }
        
        return Optional.empty();
    }
    
    // ==================== ARBITRAGEM TRIANGULAR ====================
    
    public List<ArbitrageOpportunity> detectTriangularArbitrage(Map<String, Double> pairsData) {
        /** Detecta arbitragem triangular (3 pares de moedas) */
        List<ArbitrageOpportunity> opportunities = new ArrayList<>();
        
        // Definir triângulos possíveis
        List<TriangularConfig> triangles = Arrays.asList(
            // Majors
            new TriangularConfig(
                Arrays.asList("EUR/USD", "USD/JPY", "EUR/JPY"), 
                Arrays.asList("EUR", "USD", "JPY")
            ),
            new TriangularConfig(
                Arrays.asList("GBP/USD", "USD/JPY", "GBP/JPY"), 
                Arrays.asList("GBP", "USD", "JPY")
            ),
            new TriangularConfig(
                Arrays.asList("EUR/USD", "USD/CHF", "EUR/CHF"), 
                Arrays.asList("EUR", "USD", "CHF")
            ),
            new TriangularConfig(
                Arrays.asList("GBP/USD", "USD/CHF", "GBP/CHF"), 
                Arrays.asList("GBP", "USD", "CHF")
            ),
            new TriangularConfig(
                Arrays.asList("AUD/USD", "USD/JPY", "AUD/JPY"), 
                Arrays.asList("AUD", "USD", "JPY")
            ),
            new TriangularConfig(
                Arrays.asList("EUR/GBP", "GBP/USD", "EUR/USD"), 
                Arrays.asList("EUR", "GBP", "USD")
            ),
            // Cross pairs
            new TriangularConfig(
                Arrays.asList("EUR/GBP", "GBP/JPY", "EUR/JPY"), 
                Arrays.asList("EUR", "GBP", "JPY")
            ),
            new TriangularConfig(
                Arrays.asList("EUR/AUD", "AUD/JPY", "EUR/JPY"), 
                Arrays.asList("EUR", "AUD", "JPY")
            ),
            new TriangularConfig(
                Arrays.asList("GBP/AUD", "AUD/JPY", "GBP/JPY"), 
                Arrays.asList("GBP", "AUD", "JPY")
            )
        );
        
        for (TriangularConfig config : triangles) {
            try {
                // Verificar se todos os pares estão disponíveis
                boolean allPairsAvailable = config.pairs.stream()
                    .all(pair -> pairsData.containsKey(pair.replace("/", "_")));
                
                if (!allPairsAvailable) {
                    continue;
                }
                
                // Obter preços
                double price1 = pairsData.get(config.pairs.get(0).replace("/", "_"));
                double price2 = pairsData.get(config.pairs.get(1).replace("/", "_"));
                double price3 = pairsData.get(config.pairs.get(2).replace("/", "_"));
                
                // Calcular taxa cruzada
                double crossRate = price1 * price2;
                double directRate = price3;
                
                // Calcular diferença
                double diffPct = ((crossRate - directRate) / directRate) * 100;
                
                // Considerar taxas (3 transações)
                double fees = 0.3; // 0.1% por transação × 3
                double netProfitPct = Math.abs(diffPct) - fees;
                
                if (netProfitPct >= minProfitThreshold) {
                    // Determinar direção
                    String direction;
                    if (crossRate > directRate) {
                        direction = config.currencies.get(0) + " → " + config.currencies.get(1) + " → " + 
                                     config.currencies.get(2) + " → " + config.currencies.get(0);
                    } else {
                        direction = config.currencies.get(0) + " → " + config.currencies.get(2) + " → " + 
                                     config.currencies.get(1) + " → " + config.currencies.get(0);
                    }
                    
                    ArbitrageOpportunity opportunity = new ArbitrageOpportunity(
                        ArbitrageType.TRIANGULAR,
                        "TRIANGULAR",
                        config.pairs.get(0),
                        config.pairs.get(1),
                        price1,
                        price3,
                        Math.round(netProfitPct * 1000) / 1000.0,
                        0.0 // maxVolume não aplicável para triangular
                    );
                    
                    // Adicionar campos específicos
                    opportunity.pairs = config.pairs;
                    opportunity.currencies = config.currencies;
                    opportunity.direction = direction;
                    opportunity.crossRate = Math.round(crossRate * 100000) / 100000.0;
                    opportunity.directRate = Math.round(directRate * 100000) / 100000.0;
                    
                    opportunities.add(opportunity);
                }
                
            } catch (Exception e) {
                logger.log(Level.WARNING, "Erro ao calcular triângulo " + config.pairs + ": " + e.getMessage());
            }
        }
        
        return opportunities;
    }
    
    // ==================== ARBITRAGEM ESTATÍSTICA ====================
    
    public Optional<ArbitrageOpportunity> detectStatisticalArbitrage(List<Double> asset1Data, List<Double> asset2Data,
                                                              String asset1Name, String asset2Name) {
        /** Detecta arbitragem estatística baseada em correlação e mean reversion */
        if (asset1Data == null || asset2Data == null || asset1Data.isEmpty() || asset2Data.isEmpty()) {
            return Optional.empty();
        }
        
        // Garantir que os dados têm o mesmo tamanho
        int minLen = Math.min(asset1Data.size(), asset2Data.size());
        List<Double> asset1Prices = asset1Data.subList(asset1Data.size() - minLen, asset1Data.size());
        List<Double> asset2Prices = asset2Data.subList(asset2Data.size() - minLen, asset2Data.size());
        
        // Calcular médias e desvios padrão
        double asset1Mean = asset1Prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double asset2Mean = asset2Prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double asset1Std = Math.sqrt(asset1Prices.stream()
            .mapToDouble(x -> Math.pow(x - asset1Mean, 2))
            .average().orElse(0.0));
        double asset2Std = Math.sqrt(asset2Prices.stream()
            .mapToDouble(x -> Math.pow(x - asset2Mean, 2))
            .average().orElse(0.0));
        
        // Normalizar preços
        List<Double> asset1Norm = asset1Prices.stream()
            .map(price -> asset1Std > 0 ? (price - asset1Mean) / asset1Std : 0)
            .collect(Collectors.toList());
        List<Double> asset2Norm = asset2Prices.stream()
            .map(price -> asset2Std > 0 ? (price - asset2Mean) / asset2Std : 0)
            .collect(Collectors.toList());
        
        // Calcular spread
        List<Double> spread = new ArrayList<>();
        for (int i = 0; i < asset1Norm.size(); i++) {
            spread.add(asset1Norm.get(i) - asset2Norm.get(i));
        }
        
        // Calcular estatísticas do spread
        double spreadMean = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double spreadStd = Math.sqrt(spread.stream()
            .mapToDouble(x -> Math.pow(x - spreadMean, 2))
            .average().orElse(0.0));
        double currentSpread = spread.isEmpty() ? 0.0 : spread.get(spread.size() - 1);
        double zScore = spreadStd > 0 ? (currentSpread - spreadMean) / spreadStd : 0.0;
        
        // Calcular correlação
        double correlation = calculateCorrelation(asset1Prices, asset2Prices);
        
        // Oportunidade se z-score alto e correlação forte
        if (Math.abs(zScore) >= 2.0 && Math.abs(correlation) >= 0.7) {
            // Determinar ação
            String action;
            double expectedProfit;
            
            if (zScore > 2.0) {
                action = "SELL " + asset1Name + ", BUY " + asset2Name;
                expectedProfit = Math.abs(zScore) * 0.5; // Estimativa
            } else {
                action = "BUY " + asset1Name + ", SELL " + asset2Name;
                expectedProfit = Math.abs(zScore) * 0.5;
            }
            
            ArbitrageOpportunity opportunity = new ArbitrageOpportunity(
                ArbitrageType.STATISTICAL,
                asset1Name + "/" + asset2Name,
                "", "", 0, 0, 0, 0
            );
            
            // Adicionar campos específicos
            opportunity.asset1 = asset1Name;
            opportunity.asset2 = asset2Name;
            opportunity.correlation = Math.round(correlation * 1000) / 1000.0;
            opportunity.zScore = Math.round(zScore * 1000) / 1000.0;
            opportunity.action = action;
            opportunity.expectedProfitPct = Math.round(expectedProfit * 1000) / 1000.0;
            
            return Optional.of(opportunity);
        }
        
        return Optional.empty();
    }
    
    // ==================== ARBITRAGEM CROSS-MARKET ====================
    
    public List<ArbitrageOpportunity> detectCrossMarketArbitrage(Map<String, Double> cryptoPairs, Map<String, Double> forexPairs) {
        /** Detecta arbitragem entre mercados crypto e forex */
        List<ArbitrageOpportunity> opportunities = new ArrayList<>();
        
        // Pares relacionados
        List<CrossMarketConfig> crossPairs = Arrays.asList(
            new CrossMarketConfig("BTC_USDT", "EUR_USD", "BTC/EUR"),
            new CrossMarketConfig("ETH_USDT", "EUR_USD", "ETH/EUR"),
            new CrossMarketConfig("BTC_USDT", "GBP_USD", "BTC/GBP"),
            new CrossMarketConfig("ETH_USDT", "GBP_USD", "ETH/GBP")
        );
        
        for (CrossMarketConfig config : crossPairs) {
            try {
                if (!cryptoPairs.containsKey(config.cryptoPair) || !forexPairs.containsKey(config.forexPair)) {
                    continue;
                }
                
                double cryptoPrice = cryptoPairs.get(config.cryptoPair);
                double forexRate = forexPairs.get(config.forexPair);
                
                // Calcular preço sintético
                double syntheticPrice = cryptoPrice * forexRate;
                
                ArbitrageOpportunity opportunity = new ArbitrageOpportunity(
                    ArbitrageType.CROSS_MARKET,
                    "CROSS-MARKET",
                    config.cryptoPair,
                    config.forexPair,
                    0, 0, 0, 0
                );
                
                // Adicionar campos específicos
                opportunity.cryptoPair = config.cryptoPair;
                opportunity.forexPair = config.forexPair;
                opportunity.syntheticPair = config.syntheticPair;
                opportunity.syntheticPrice = Math.round(syntheticPrice * 100) / 100.0;
                
                opportunities.add(opportunity);
                
            } catch (Exception e) {
                logger.log(Level.WARNING, "Erro ao calcular cross-market " + config.cryptoPair + "/" + config.forexPair + ": " + e.getMessage());
            }
        }
        
        return opportunities;
    }
    
    // ==================== ANÁLISE COMPLETA ====================
    
    public Map<String, List<ArbitrageOpportunity>> scanAllOpportunities() {
        /** Escaneia todas as oportunidades de arbitragem disponíveis */
        logger.info("=".repeat(70));
        logger.info("🔍 ESCANEANDO OPORTUNIDADES DE ARBITRAGEM");
        logger.info("=".repeat(70));
        
        Map<String, List<ArbitrageOpportunity>> allOpportunities = new HashMap<>();
        
        // Inicializar categorias
        allOpportunities.put("spatial", new ArrayList<>());
        allOpportunities.put("triangular", new ArrayList<>());
        allOpportunities.put("statistical", new ArrayList<>());
        allOpportunities.put("cross_market", new ArrayList<>());
        
        try {
            // Carregar dados de preços
            Map<String, Double> cryptoPrices = loadCryptoPrices();
            Map<String, Double> forexPrices = loadForexPrices();
            
            // 1. Arbitragem Triangular (Forex)
            logger.info("📐 Buscando arbitragem triangular...");
            List<ArbitrageOpportunity> triangularOpps = detectTriangularArbitrage(forexPrices);
            allOpportunities.put("triangular", triangularOpps);
            logger.info("   Encontradas: " + triangularOpps.size() + " oportunidades");
            
            // 2. Arbitragem Estatística (Crypto)
            logger.info("📊 Buscando arbitragem estatística...");
            List<ArbitrageOpportunity> statOpps = scanStatisticalCrypto();
            allOpportunities.put("statistical", statOpps);
            logger.info("   Encontradas: " + statOpps.size() + " oportunidades");
            
            // 3. Arbitragem Cross-Market
            logger.info("🔄 Buscando arbitragem cross-market...");
            List<ArbitrageOpportunity> crossOpps = detectCrossMarketArbitrage(cryptoPrices, forexPrices);
            allOpportunities.put("cross_market", crossOpps);
            logger.info("   Encontradas: " + crossOpps.size() + " oportunidades");
            
            // Total
            int total = allOpportunities.values().stream().mapToInt(List::size).sum();
            logger.info("\n✅ Total de oportunidades: " + total);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao escanear oportunidades", e);
        }
        
        return allOpportunities;
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private Map<String, Double> loadCryptoPrices() {
        /** Carrega preços atuais de criptomoedas */
        try {
            // Simulação de dados em tempo real
            Map<String, Double> prices = new HashMap<>();
            
            // Dados simulados para demonstração
            prices.put("BTC_USDT", 45000.0 + random.nextDouble() * 5000);
            prices.put("ETH_USDT", 3000.0 + random.nextDouble() * 500);
            prices.put("BNB_USDT", 300.0 + random.nextDouble() * 50);
            prices.put("ADA_USDT", 0.5 + random.nextDouble() * 0.3);
            prices.put("DOT_USDT", 6.0 + random.nextDouble() * 2.0);
            prices.put("SOL_USDT", 100.0 + random.nextDouble() * 20);
            prices.put("AVAX_USDT", 35.0 + random.nextDouble() * 15);
            
            return prices;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao carregar preços crypto: " + e.getMessage(), e);
            return new HashMap<>();
        }
    }
    
    private Map<String, Double> loadForexPrices() {
        /** Carrega preços atuais de forex */
        try {
            // Simulação de dados em tempo real
            Map<String, Double> prices = new HashMap<>();
            
            // Dados simulados para demonstração
            prices.put("EUR_USD", 1.0850 + random.nextDouble() * 0.02);
            prices.put("USD_JPY", 149.50 + random.nextDouble() * 2);
            prices.put("EUR_JPY", 162.30 + random.nextDouble() * 2);
            prices.put("GBP_USD", 1.2750 + random.nextDouble() * 0.02);
            prices.put("GBP_JPY", 191.80 + random.nextDouble() * 3);
            prices.put("EUR_GBP", 0.8720 + random.nextDouble() * 0.01);
            prices.put("CHF_USD", 1.0850 + random.nextDouble() * 0.02);
            prices.put("EUR_CHF", 0.9230 + random.nextDouble() * 0.01);
            prices.put("AUD_USD", 0.6850 + random.nextDouble() * 0.02);
            prices.put("AUD_JPY", 102.30 + random.nextDouble() * 2);
            
            return prices;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao carregar preços forex: " + e.getMessage(), e);
            return new HashMap<>();
        }
    }
    
    private List<ArbitrageOpportunity> scanStatisticalCrypto() {
        /** Escaneia arbitragem estatística em criptomoedas */
        List<ArbitrageOpportunity> opportunities = new ArrayList<>();
        
        // Pares correlacionados para análise
        List<String[]> correlatedPairs = Arrays.asList(
            new String[]{"BTC_USDT", "ETH_USDT"},
            new String[]{"BTC_USDT", "BNB_USDT"},
            new String[]{"ETH_USDT", "BNB_USDT"},
            new String[]{"ADA_USDT", "DOT_USDT"},
            new String[]{"SOL_USDT", "AVAX_USDT"}
        );
        
        for (String[] pair : correlatedPairs) {
            try {
                // Simular dados históricos
                List<Double> data1 = generateHistoricalData(pair[0], 1000);
                List<Double> data2 = generateHistoricalData(pair[1], 1000);
                
                // Detectar oportunidade
                Optional<ArbitrageOpportunity> opp = detectStatisticalArbitrage(data1, data2, pair[0], pair[1]);
                if (opp.isPresent()) {
                    opportunities.add(opp.get());
                }
                
            } catch (Exception e) {
                logger.log(Level.WARNING, "Erro ao analisar " + pair[0] + "/" + pair[1] + ": " + e.getMessage());
            }
        }
        
        return opportunities;
    }
    
    private List<Double> generateHistoricalData(String asset, int points) {
        /** Gera dados históricos simulados para demonstração */
        List<Double> data = new ArrayList<>();
        double basePrice = asset.contains("BTC") ? 45000.0 : 
                           asset.contains("ETH") ? 3000.0 : 
                           asset.contains("BNB") ? 300.0 : 100.0;
        
        double volatility = 0.02; // 2% volatilidade diária
        
        for (int i = 0; i < points; i++) {
            // Simulação de movimento browniano geométrico
            double randomShock = (random.nextGaussian() * volatility);
            double drift = 0.0001; // 0.01% drift diário
            
            double price = basePrice * Math.exp((drift - 0.5 * volatility * volatility) + randomShock);
            data.add(price);
            
            // Atualizar preço base para próximo período
            basePrice = price;
        }
        
        return data;
    }
    
    private double calculateCorrelation(List<Double> series1, List<Double> series2) {
        /** Calcula correlação entre duas séries de dados */
        if (series1.size() != series2.size() || series1.isEmpty()) {
            return 0.0;
        }
        
        double mean1 = series1.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double mean2 = series2.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        
        double covariance = 0.0;
        for (int i = 0; i < series1.size(); i++) {
            covariance += (series1.get(i) - mean1) * (series2.get(i) - mean2);
        }
        
        double variance1 = 0.0;
        for (double x : series1) {
            variance += Math.pow(x - mean1, 2);
        }
        double variance2 = 0.0;
        for (double x : series2) {
            variance2 += Math.pow(x - mean2, 2);
        }
        
        return variance1 > 0 && variance2 > 0 ? covariance / Math.sqrt(variance1 * variance2) : 0.0;
    }
    
    // ==================== EXECUÇÃO E GESTÃO ====================
    
    public double calculateOptimalVolume(ArbitrageOpportunity opportunity, double availableCapital) {
        /** Calcula volume ótimo para executar arbitragem */
        if (opportunity.type == ArbitrageType.SPATIAL) {
            double maxVolume = opportunity.maxVolume;
            // Usar 80% do volume disponível para evitar slippage
            return Math.min(maxVolume * 0.8, availableCapital / opportunity.buyPrice);
        } else if (opportunity.type == ArbitrageType.TRIANGULAR) {
            // Para triangular, usar capital disponível dividido por 3
            return availableCapital / 3;
        } else if (opportunity.type == ArbitrageType.STATISTICAL) {
            // Para estatística, usar 50% do capital em cada lado
            return availableCapital / 2;
        }
        
        return 0.0;
    }
    
    public Optional<ArbitrageExecution> executeArbitrage(ArbitrageOpportunity opportunity, double capital) {
        /** Simula execução de arbitragem */
        double volume = calculateOptimalVolume(opportunity, capital);
        
        if (volume <= 0) {
            return Optional.empty();
        }
        
        // Calcular lucro esperado
        double expectedProfit = (volume * opportunity.profitPct) / 100;
        
        ArbitrageExecution execution = new ArbitrageExecution(opportunity, volume, expectedProfit);
        
        executedTrades.add(execution);
        logger.info(String.format("💰 Arbitragem executada: %s | Lucro esperado: $%.2f", 
            opportunity.type, expectedProfit));
        
        return Optional.of(execution);
    }
    
    // ==================== RELATÓRIOS ====================
    
    public String generateReport(Map<String, List<ArbitrageOpportunity>> opportunities) {
        /** Gera relatório de oportunidades */
        List<String> report = new ArrayList<>();
        
        report.add("=".repeat(70));
        report.add("📊 RELATÓRIO DE ARBITRAGEM");
        report.add("=".repeat(70));
        report.add("Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        report.add("");
        
        // Resumo por tipo
        report.add("📈 OPORTUNIDADES POR TIPO:");
        report.add("-".repeat(70));
        for (Map.Entry<String, List<ArbitrageOpportunity>> entry : opportunities.entrySet()) {
            String arbType = entry.getKey();
            int count = entry.getValue().size();
            report.add(String.format("%-20s: %3d oportunidades", arbType.toUpperCase(), count));
        }
        
        int total = opportunities.values().stream().mapToInt(List::size).sum();
        report.add(String.format("%-20s: %3d oportunidades", "TOTAL", total));
        report.add("");
        
        // Top 5 oportunidades
        List<ArbitrageOpportunity> allOpps = new ArrayList<>();
        for (List<ArbitrageOpportunity> opps : opportunities.values()) {
            allOpps.addAll(opps);
        }
        
        // Ordenar por lucro
        allOpps.sort((a, b) -> Double.compare(b.profitPct, a.profitPct));
        
        if (!allOpps.isEmpty()) {
            report.add("🏆 TOP 5 OPORTUNIDADES:");
            report.add("-".repeat(70));
            
            for (int i = 0; i < Math.min(5, allOpps.size()); i++) {
                ArbitrageOpportunity opp = allOpps.get(i);
                report.add(String.format("%d. %s | Lucro: %.3f%%", 
                    i + 1, opp.type, opp.profitPct));
                
                if (opp.type == ArbitrageType.TRIANGULAR) {
                    report.add("   Pares: " + String.join(", ", opp.pairs));
                } else if (opp.type == ArbitrageType.STATISTICAL) {
                    report.add("   Ativos: " + opp.asset1 + " / " + opp.asset2);
                    report.add("   Z-Score: " + opp.zScore);
                }
            }
        }
        
        report.add("");
        report.add("=".repeat(70));
        
        return String.join("\n", report);
    }
    
    public void saveOpportunities(Map<String, List<ArbitrageOpportunity>> opportunities, String filename) {
        /** Salva oportunidades em arquivo JSON */
        if (filename == null) {
            filename = "arbitrage_opportunities_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".json";
        }
        
        String filepath = "neural_layers/04_decisao/" + filename;
        
        try {
            // Simulação de salvamento (em produção, usar biblioteca JSON)
            logger.info("💾 Oportunidades salvas: " + filepath);
            
            // Aqui você usaria uma biblioteca como Jackson ou Gson para salvar em JSON
            // Por enquanto, apenas registramos o log
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar oportunidades: " + e.getMessage(), e);
        }
    }
    
    // ==================== GETTERS ====================
    
    public double getMinProfitThreshold() {
        return minProfitThreshold;
    }
    
    public double getMaxExecutionTime() {
        return maxExecutionTime;
    }
    
    public List<ArbitrageOpportunity> getOpportunities() {
        return new ArrayList<>(opportunities);
    }
    
    public List<ArbitrageExecution> getExecutedTrades() {
        return new ArrayList<>(executedTrades);
    }
    
    public int getTotalOpportunities() {
        return opportunities.size();
    }
    
    public int getTotalExecutedTrades() {
        return executedTrades.size();
    }
    
    public double getTotalProfit() {
        return executedTrades.stream()
            .mapToDouble(ArbitrageExecution::getExpectedProfit)
            .sum();
    }
}

// Classes de Configuração
class TriangularConfig {
    List<String> pairs;
    List<String> currencies;
    
    public TriangularConfig(List<String> pairs, List<String> currencies) {
        this.pairs = pairs;
        this.currencies = currencies;
    }
}

class CrossMarketConfig {
    String cryptoPair;
    String forexPair;
    String syntheticPair;
    
    public CrossMarketConfig(String cryptoPair, String forexPair, String syntheticPair) {
        this.cryptoPair = cryptoPair;
        this.forexPair = forexPair;
        this.syntheticPair = syntheticPair;
    }
}

// Classe Principal para Demonstração
public class ArbitrageTradingStrategyApp {
    
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Estratégia de Arbitragem...");
        
        demonstrateArbitrageStrategy();
    }
    
    private static void demonstrateArbitrageStrategy() {
        /** Demonstra o funcionamento da estratégia de arbitragem */
        ArbitrageTradingStrategy strategy = new ArbitrageTradingStrategy(0.5, 5.0);
        
        try {
            // Escanear oportunidades
            Map<String, List<ArbitrageOpportunity>> opportunities = strategy.scanAllOpportunities();
            
            // Gerar relatório
            String report = strategy.generateReport(opportunities);
            System.out.println("\n" + report);
            
            // Salvar oportunidades
            strategy.saveOpportunities(opportunities, null);
            
            // Simular execução das melhores oportunidades
            System.out.println("\n💰 Executando top 3 oportunidades...");
            executeTopOpportunities(strategy, opportunities);
            
            // Estatísticas finais
            System.out.println("\n📊 ESTATÍSTICAS FINAIS:");
            System.out.println("Total de oportunidades: " + strategy.getTotalOpportunities());
            System.out.println("Execuções realizadas: " + strategy.getTotalExecutedTrades());
            System.out.println("Lucro total: $" + String.format("%.2f", strategy.getTotalProfit()));
            
            System.out.println("\n✅ Demonstração concluída com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro na demonstração: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void executeTopOpportunities(ArbitrageTradingStrategy strategy, 
                                                Map<String, List<ArbitrageOpportunity>> opportunities) {
        /** Executa as melhores oportunidades de arbitragem */
        List<ArbitrageOpportunity> allOpps = new ArrayList<>();
        for (List<ArbitrageOpportunity> opps : opportunities.values()) {
            allOpps.addAll(opps);
        }
        
        // Ordenar por lucro
        allOpps.sort((a, b) -> Double.compare(b.profitPct, a.profitPct));
        
        double capital = 10000.0; // $10,000 de capital
        
        for (int i = 0; i < Math.min(3, allOpps.size()); i++) {
            ArbitrageOpportunity opp = allOpps.get(i);
            Optional<ArbitrageExecution> execution = strategy.executeArbitrage(opp, capital / (i + 1)); // Dividir capital entre oportunidades
            execution.ifPresent(exec -> {
                System.out.println(String.format("  %d. %s executada com volume: %.4f", 
                    i + 1, opp.type, execution.volume));
            });
        }
    }
}
