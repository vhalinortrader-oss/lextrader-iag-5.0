package modulo_emocional.Sensório;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Oracle Service - Sistema de Dados e Sinais de Trading
 * Versão Java convertida e superpotenciada do Python original
 */
public class OracleService {
    
    // Enums para tipos de oráculos e sinais
    public enum OracleSourceType {
        TRADING_VIEW("TRADINGVIEW"),
        YFINANCE("YFINANCE"),
        GLASSNODE("GLASSNODE"),
        SANTIMENT("SANTIMENT"),
        COINGLASS("COINGLASS"),
        INTOTHEBLOCK("INTOTHEBLOCK"),
        MESSARI("MESSARI"),
        CRYPTOCOMPARE("CRYPTOCOMPARE"),
        COINMETRICS("COINMETRICS"),
        DEFIPULSE("DEFIPULSE"),
        BYBIT("BYBIT"),
        COINBASE("COINBASE"),
        INFINITY_GRID("INFINITY_GRID"),
        MARTINGALE("MARTINGALE")
    }
    
    public enum SignalType {
        BUY("BUY"),
        SELL("SELL"),
        NEUTRAL("NEUTRAL"),
        STRONG_BUY("STRONG_BUY"),
        STRONG_SELL("STRONG_SELL")
    }
    
    public enum SourceReliability {
        HIGH("HIGH"),
        MEDIUM("MEDIUM"),
        LOW("LOW"),
        UNKNOWN("UNKNOWN")
    }
    
    // Estrutura de dados para sinal de oráculo
    public static class OracleSignal {
        private final String id;
        private final OracleSourceType sourceType;
        private final String sourceName;
        private final SignalType signalType;
        private final String symbol;
        private final double confidence;
        private final double targetPrice;
        private final double stopLoss;
        private final double takeProfit;
        private final LocalDateTime timestamp;
        private final SourceReliability reliability;
        private final Map<String, Object> metadata;
        
        public OracleSignal(String id, OracleSourceType sourceType, String sourceName, SignalType signalType,
                       String symbol, double confidence, double targetPrice,
                       double stopLoss, double takeProfit, SourceReliability reliability,
                       Map<String, Object> metadata) {
            this.id = id;
            this.sourceType = sourceType;
            this.sourceName = sourceName;
            this.signalType = signalType;
            this.symbol = symbol;
            this.confidence = Math.max(0.0, Math.min(1.0, confidence));
            this.targetPrice = targetPrice;
            this.stopLoss = stopLoss;
            this.takeProfit = takeProfit;
            this.timestamp = LocalDateTime.now();
            this.reliability = reliability;
            this.metadata = new HashMap<>(metadata != null ? metadata : Collections.emptyMap());
        }
        
        // Getters
        public String getId() { return id; }
        public OracleSourceType getSourceType() { return sourceType; }
        public String getSourceName() { return sourceName; }
        public SignalType getSignalType() { return signalType; }
        public String getSymbol() { return symbol; }
        public double getConfidence() { return confidence; }
        public double getTargetPrice() { return targetPrice; }
        public double getStopLoss() { return stopLoss; }
        public double getTakeProfit() { return takeProfit; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public SourceReliability getReliability() { return reliability; }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("sourceType", sourceType.toString());
            map.put("sourceName", sourceName);
            map.put("signalType", signalType.toString());
            map.put("symbol", symbol);
            map.put("confidence", confidence);
            map.put("targetPrice", targetPrice);
            map.put("stopLoss", stopLoss);
            map.put("takeProfit", takeProfit);
            map.put("timestamp", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            map.put("reliability", reliability.toString());
            map.put("metadata", metadata);
            return map;
        }
    }
    
    // Estrutura de dados para fonte de dados
    public static class DataSource {
        private final String name;
        private final OracleSourceType type;
        private final String url;
        private final String apiKey;
        private final boolean isActive;
        private final Map<String, Object> config;
        private final Map<String, Object> lastUpdate;
        private final List<OracleSignal> recentSignals;
        private final AtomicInteger totalSignals;
        private final AtomicInteger successfulSignals;
        
        public DataSource(String name, OracleSourceType type, String url, String apiKey, 
                      Map<String, Object> config) {
            this.name = name;
            this.type = type;
            this.url = url;
            this.apiKey = apiKey;
            this.isActive = false;
            this.config = new HashMap<>(config != null ? config : Collections.emptyMap());
            this.lastUpdate = LocalDateTime.now();
            this.recentSignals = new ArrayList<>();
            this.totalSignals = new AtomicInteger(0);
            this.successfulSignals = new AtomicInteger(0);
        }
        
        // Getters
        public String getName() { return name; }
        public OracleSourceType getType() { return type; }
        public String getUrl() { return url; }
        public String getApiKey() { return apiKey; }
        public boolean isActive() { return isActive; }
        public Map<String, Object> getConfig() { return new HashMap<>(config); }
        public List<OracleSignal> getRecentSignals() { return new ArrayList<>(recentSignals); }
        public int getTotalSignals() { return totalSignals.get(); }
        public int getSuccessfulSignals() { return successfulSignals.get(); }
    }
        
        // Setters
        public void setActive(boolean active) { this.isActive = active; }
        public void setConfig(Map<String, Object> config) { this.config.putAll(config); }
        public void addSignal(OracleSignal signal) {
            recentSignals.add(0, signal);
            totalSignals.incrementAndGet();
            if (signal.getConfidence() > 0.7) {
                successfulSignals.incrementAndGet();
            }
        }
    }
    }
    
    // Estrutura de dados para métricas de performance
    public static class PerformanceMetrics {
        private final String sourceName;
        private final int totalSignals;
        private final int successfulSignals;
        private final double successRate;
        private final double avgConfidence;
        private final Map<String, Double> signalTypeDistribution;
        private final Map<String, Double> reliabilityDistribution;
        private final LocalDateTime lastUpdate;
        
        public PerformanceMetrics(String sourceName, int totalSignals, int successfulSignals, 
                           Map<String, Double> signalTypeDist, Map<String, Double> reliabilityDist) {
            this.sourceName = sourceName;
            this.totalSignals = totalSignals;
            this.successfulSignals = successfulSignals;
            this.successRate = totalSignals > 0 ? (double) successfulSignals / totalSignals * 100 : 0.0;
            this.avgConfidence = avgConfidence;
            this.signalTypeDistribution = new HashMap<>(signalTypeDist);
            this.reliabilityDistribution = new HashMap<>(reliabilityDist);
            this.lastUpdate = LocalDateTime.now();
        }
        
        // Getters
        public String getSourceName() { return sourceName; }
        public int getTotalSignals() { return totalSignals; }
        public int getSuccessfulSignals() { return successfulSignals; }
        public double getSuccessRate() { return successRate; }
        public double getAvgConfidence() { return avgConfidence; }
        public Map<String, Double> getSignalTypeDistribution() { return new HashMap<>(signalTypeDistribution); }
        public Map<String, Double> getReliabilityDistribution() { return new HashMap<>(reliabilityDistribution); }
    }
    }
    
    // Estado do serviço Oracle
    private final Map<String, DataSource> dataSources;
    private final Map<String, OracleSignal> allSignals;
    private final ScheduledExecutorService scheduler;
    private final Map<String, CompletableFuture<Void>> activeTasks;
    private final AtomicInteger totalSignalsGenerated;
    private final AtomicInteger totalSuccessfulSignals;
    private final Map<String, PerformanceMetrics> performanceMetrics;
    private final String logDirectory;
    
    public OracleService(String logDirectory) {
        this.logDirectory = logDirectory != null ? logDirectory : "logs/oracle";
        this.dataSources = new ConcurrentHashMap<>();
        this.allSignals = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.activeTasks = new HashMap<>();
        this.totalSignalsGenerated = new AtomicInteger(0);
        this.totalSuccessfulSignals = new AtomicInteger(0);
        this.performanceMetrics = new ConcurrentHashMap<>();
        
        // Inicializa fontes de dados padrão
        initializeDefaultDataSources();
        
        // Cria diretório de logs
        createLogDirectory();
        
        System.out.println("🔮 Oracle Service inicializado");
        System.out.println("📁 Diretório de logs: " + this.logDirectory);
        System.out.println("📊 Fontes de dados: " + this.dataSources.size());
        System.out.println("📈 Total de sinais: " + this.allSignals.size());
    }
    
    /**
     * Inicializa fontes de dados padrão
     */
    private void initializeDefaultDataSources() {
        // TradingView
        Map<String, Object> tradingViewConfig = Map.of(
            "update_interval", 30,
            "confidence_threshold", 0.6,
            "max_signals_per_source", 100
        );
        
        dataSources.put("tradingview", new DataSource(
            "TradingView", "https://api.tradingview.com", "tv_key", "tradingview_api_key", tradingViewConfig
        ));
        
        // YFinance
        Map<String, Object> yFinanceConfig = Map.of(
            "update_interval", 60,
            "confidence_threshold", 0.7,
            "max_signals_per_source", 50
        );
        
        dataSources.put("yfinance", new DataSource(
            "YFinance", "https://api.yfinance.com", "yf_api_key", "yf_api_key", yFinanceConfig
        ));
        
        // Glassnode
        Map<String, Object> glassnodeConfig = Map.of(
            "update_interval", 45,
            "confidence_threshold", 0.65,
            "max_signals_per_source", 75
        );
        
        dataSources.put("glassnode", new DataSource(
            "Glassnode", "https://api.glassnode.com", "gl_api_key", "gl_api_key", glassnodeConfig
        ));
        
        // Santiment
        Map<String, Object> sentimentConfig = Map.of(
            "update_interval", 15,
            "confidence_threshold", 0.8,
            "max_signals_per_source", 200
        );
        
        dataSources.put("santiment", new DataSource(
            "Santiment", "https://api.santiment.com", "sentiment_api_key", "sentiment_api_key", sentimentConfig
        ));
        
        // CoinGecko
        Map<String, Object> coingeckoConfig = Map.of(
            "update_interval", 120,
            "confidence_threshold", 0.75,
            "max_signals_per_source", 25
        );
        
        dataSources.put("coingecko", new DataSource(
            "CoinGecko", "https://api.coingecko.com", "cg_api_key", "cg_api_key", coingeckoConfig
        ));
        
        // CoinMetrics
        Map<String, Object> coinmetricsConfig = Map.of(
            "update_interval", 180,
            "confidence_threshold", 0.7,
            "max_signals_per_source", 30
        );
        
        dataSources.put("coinmetrics", new DataSource(
            "CoinMetrics", "https://api.coinmetrics.com", "cm_api_key", "cm_api_key", coinmetricsConfig
        ));
        
        // Messari
        Map<String, Object> messariConfig = Map.of(
            "update_interval", 10,
            "confidence_threshold", 0.85,
            "max_signals_per_source", 500
        );
        
        dataSources.put("messari", new DataSource(
            "Messari", "https://api.messari.com", "ms_api_key", "ms_api_key", messariConfig
        ));
        
        System.out.println("📊 Fontes de dados inicializadas: " + dataSources.size());
    }
    
    /**
     * Cria diretório de logs
     */
    private void createLogDirectory() {
        File dir = new File(logDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Adiciona fonte de dados
     */
    public void addDataSource(String name, OracleSourceType type, String url, String apiKey, 
                           Map<String, Object> config) {
        DataSource dataSource = new DataSource(name, type, url, apiKey, config);
        dataSources.put(name, dataSource);
        
        System.out.println("📊 Fonte de dados adicionada: " + name + " (" + type + ")");
    }
    
    /**
     * Ativa fonte de dados
     */
    public CompletableFuture<Boolean> activateDataSource(String name) {
        return CompletableFuture.supplyAsync(() -> {
            DataSource dataSource = dataSources.get(name);
            if (dataSource == null) {
                System.err.println("❌ Fonte de dados não encontrada: " + name);
                return false;
            }
            
            try {
                dataSource.setActive(true);
                
                // Agenda coleta de dados
                String taskName = "collect_" + name.toLowerCase();
                activeTasks.put(taskName, scheduler.scheduleAtFixedRate(() -> {
                    collectDataFromSource(dataSource);
                }, 0, getUpdateInterval(dataSource), TimeUnit.SECONDS));
                
                System.out.println("✅ Fonte de dados " + name + " ativada");
                return true;
                
            } catch (Exception e) {
                System.err.println("❌ Erro ao ativar fonte " + name + ": " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Desativa fonte de dados
     */
    public CompletableFuture<Boolean> deactivateDataSource(String name) {
        return CompletableFuture.supplyAsync(() -> {
            DataSource dataSource = dataSources.get(name);
            if (dataSource == null) {
                System.err.println("❌ Fonte de dados não encontrada: " + name);
                return false;
            }
            
            try {
                dataSource.setActive(false);
                
                // Cancela tarefa ativa
                String taskName = "collect_" + name.toLowerCase();
                if (activeTasks.containsKey(taskName)) {
                    activeTasks.get(taskName).cancel(true);
                }
                
                System.out.println("✅ Fonte de dados " + name + " desativada");
                return true;
                
            } catch (Exception e) {
                System.err.println("❌ Erro ao desativar fonte " + name + ": " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Coleta dados de uma fonte
     */
    private void collectDataFromSource(DataSource dataSource) {
        try {
            // Simula coleta de sinais
            List<OracleSignal> signals = generateSignalsFromSource(dataSource);
            
            // Adiciona sinais ao sistema
            for (OracleSignal signal : signals) {
                allSignals.put(signal.getId(), signal);
                dataSource.addSignal(signal);
                
                // Atualiza métricas
                updatePerformanceMetrics(dataSource.getName());
                
                // Log do sinal
                System.out.println("🔮 Sinal gerado: " + signal.getSignalType() + 
                                 " para " + signal.getSymbol() + 
                                 " (" + String.format("%.2f", signal.getConfidence() * 100) + "%) " +
                                 "em " + dataSource.getName());
            }
            
            // Limita sinais recentes
            if (dataSource.getRecentSignals().size() > 1000) {
                List<OracleSignal> recentSignals = dataSource.getRecentSignals();
                recentSignals.subList(0, 500).clear();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao coletar dados de " + dataSource.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Gera sinais simulados de uma fonte
     */
    private List<OracleSignal> generateSignalsFromSource(DataSource dataSource) {
        List<OracleSignal> signals = new ArrayList<>();
        Random random = new Random();
        
        int signalsToGenerate = Math.min(10, getMaxSignalsPerSource(dataSource));
        
        for (int i = 0; i < signalsToGenerate; i++) {
            String signalId = dataSource.getName() + "_signal_" + System.currentTimeMillis() + "_" + i;
            
            // Gera tipo de sinal baseado na fonte
            SignalType signalType = generateSignalType(dataSource.getType());
            
            // Gera símbolo aleatório
            String[] symbols = {"BTC", "ETH", "BNB", "SOL", "ADA", "DOT", "MATIC"};
            String symbol = symbols[random.nextInt(symbols.length)];
            
            // Gera confiança baseado na fonte
            double baseConfidence = getBaseConfidence(dataSource.getType());
            double confidence = baseConfidence + (random.nextDouble() * 0.3);
            confidence = Math.min(1.0, confidence);
            
            // Gera preço alvo
            double currentPrice = 50000 + (random.nextDouble() - 0.5) * 10000;
            double targetPrice = currentPrice * (1 + (random.nextDouble() - 0.3) * 0.1);
            
            // Gera stop loss e take profit
            double stopLoss = targetPrice * 0.02; // 2%
            double takeProfit = targetPrice * 0.05; // 5%
            
            // Gera dados do sinal
            Map<String, Object> metadata = Map.of(
                "source_reliability", getSourceReliability(dataSource.getType()).toString(),
                "market_condition", random.nextBoolean() ? "BULLISH" : "BEARISH",
                "volume_spike", random.nextDouble() * 1000000,
                "rsi", 30 + random.nextDouble() * 20,
                "macd", 20 + random.nextDouble() * 10
            );
            
            OracleSignal signal = new OracleSignal(
                signalId, dataSource.getType(), dataSource.getName(), signalType,
                symbol, confidence, targetPrice, stopLoss, takeProfit,
                SourceReliability.MEDIUM, metadata
            );
            
            signals.add(signal);
            totalSignalsGenerated.incrementAndGet();
            
            if (signal.getConfidence() > 0.8) {
                totalSuccessfulSignals.incrementAndGet();
            }
        }
        
        return signals;
    }
    
    /**
     * Gera tipo de sinal baseado na fonte
     */
    private SignalType generateSignalType(OracleSourceType sourceType) {
        Random random = new Random();
        
        switch (sourceType) {
            case TRADING_VIEW:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case YFINANCE:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case GLASSNODE:
                return SignalType.NEUTRAL;
            case SANTIMENT:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case COINGLASS:
                return random.nextBoolean() ? 
                    SignalType.STRONG_BUY : SignalType.STRONG_SELL;
            case INTOTHEBLOCK:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case MESSARI:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case CRYPTOCOMPARE:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case COINMETRICS:
                return SignalType.NEUTRAL;
            case DEFIPULSE:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case BYBIT:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case COINBASE:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            case INFINITY_GRID:
                return SignalType.NEUTRAL;
            case MARTINGALE:
                return random.nextBoolean() ? 
                    SignalType.BUY : SignalType.SELL;
            default:
                return SignalType.NEUTRAL;
        }
    }
    
    /**
     * Obtém confiança base para tipo de fonte
     */
    private double getBaseConfidence(OracleSourceType sourceType) {
        switch (sourceType) {
            case TRADING_VIEW:
                return 0.5;
            case YFINANCE:
                return 0.6;
            case GLASSNODE:
                return 0.4;
            case SANTIMENT:
                return 0.7;
            case COINGLASS:
                return 0.8;
            case INTOTHEBLOCK:
                return 0.3;
            case MESSARI:
                return 0.85;
            case CRYPTOCOMPARE:
                return 0.6;
            case COINMETRICS:
                return 0.5;
            case DEFIPULSE:
                return 0.4;
            case BYBIT:
                return 0.3;
            case COINBASE:
                return 0.4;
            case INFINITY_GRID:
                return 0.2;
            case MARTINGALE:
                return 0.7;
            default:
                return 0.5;
        }
    }
    
    /**
     * Obtém intervalo de atualização para fonte
     */
    private int getUpdateInterval(DataSource dataSource) {
        Map<String, Object> config = dataSource.getConfig();
        if (config.containsKey("update_interval")) {
            return (Integer) config.get("update_interval");
        }
        return 30; // Default 30 segundos
    }
    
    /**
     * Obtém número máximo de sinais por fonte
     */
    private int getMaxSignalsPerSource(DataSource dataSource) {
        Map<String, Object> config = dataSource.getConfig();
        if (config.containsKey("max_signals_per_source")) {
            return (Integer) config.get("max_signals_per_source");
        }
        return 100; // Default 100 sinais
    }
    
    /**
     * Atualiza métricas de performance
     */
    private void updatePerformanceMetrics(String sourceName) {
        DataSource dataSource = dataSources.get(sourceName);
        if (dataSource == null) return;
        
        List<OracleSignal> recentSignals = dataSource.getRecentSignals();
        if (recentSignals.isEmpty()) return;
        
        int total = dataSource.getTotalSignals();
        int successful = dataSource.getSuccessfulSignals();
        
        // Calcula distribuição por tipo de sinal
        Map<String, Double> signalTypeDist = new HashMap<>();
        Map<String, Double> reliabilityDist = new HashMap<>();
        
        for (OracleSignal signal : recentSignals) {
            String signalType = signal.getSignalType().toString();
            signalTypeDist.merge(signalType, 1.0, Double::sum);
            
            String reliability = signal.getReliability().toString();
            reliabilityDist.merge(reliability, 1.0, Double::sum);
        }
        
        // Calcula médias
        double avgConfidence = recentSignals.stream()
                .mapToDouble(OracleSignal::getConfidence)
                .average().orElse(0.0);
        
        PerformanceMetrics metrics = new PerformanceMetrics(
            sourceName, total, successful,
            total > 0 ? (double) successful / total * 100 : 0.0,
            avgConfidence, signalTypeDist, reliabilityDist
        );
        
        performanceMetrics.put(sourceName, metrics);
        
        System.out.println("📊 Métricas atualizadas: " + sourceName);
        System.out.println("  " + metrics.getTotalSignals() + " sinais gerados");
        System.out.println("  " + metrics.getSuccessfulSignals() + " bem-sucedidos");
        System.out.println("  " + String.format("%.2f%%", metrics.getSuccessRate()) + "% confiança média");
    }
    
    /**
     * Inicia coleta de todas as fontes
     */
    public void startAllDataCollection() {
        System.out.println("🚀 Iniciando coleta de dados Oracle...");
        
        // Ativa todas as fontes
        List<CompletableFuture<Boolean>> activationTasks = new ArrayList<>();
        for (String sourceName : dataSources.keySet()) {
            activationTasks.add(activateDataSource(sourceName));
        }
        
        // Aguarda todas as ativações
        CompletableFuture.allOf(activationTasks).thenRun(() -> {
            System.out.println("✅ Todas as fontes de dados estão ativas");
            System.out.println("📊 Coletando sinais de múltiplas fontes simultaneamente...");
        });
    }
    
    /**
     * Para coleta de dados de uma fonte específica
     */
    public CompletableFuture<List<OracleSignal>> collectFromSource(String sourceName) {
        return CompletableFuture.supplyAsync(() -> {
            DataSource dataSource = dataSources.get(sourceName);
            if (dataSource == null || !dataSource.isActive()) {
                return Collections.emptyList();
            }
            
            collectDataFromSource(dataSource);
            return dataSource.getRecentSignals();
        });
    }
    
    /**
     * Obtém todos os sinais gerados
     */
    public Map<String, List<OracleSignal>> getAllSignals() {
        return new HashMap<>(allSignals);
    }
    
    /**
     * Obtém sinais por tipo
     */
    public Map<String, List<OracleSignal>> getSignalsByType(SignalType signalType) {
        Map<String, List<OracleSignal>> signalsByType = new HashMap<>();
        
        for (OracleSignal signal : allSignals.values()) {
            String type = signal.getSignalType().toString();
            signalsByType.computeIfAbsent(type, k -> new ArrayList<>()).add(signal);
        }
        
        return signalsByType;
    }
    
    /**
     * Obtém sinais por fonte
     */
    public Map<String, List<OracleSignal>> getSignalsBySource(String sourceName) {
        return new HashMap<>(sourceName != null ? 
                allSignals.values().stream()
                        .filter(signal -> signal.getSourceName().equals(sourceName))
                        .collect(Collectors.toList()) : 
                Collections.emptyList());
    }
    
    /**
     * Obtém estatísticas de performance
     */
    public Map<String, PerformanceMetrics> getPerformanceMetrics() {
        return new HashMap<>(performanceMetrics);
    }
    
    /**
     * Obtém estatísticas gerais
     */
    public Map<String, Object> getOracleStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        int totalSources = dataSources.size();
        int totalSignals = allSignals.size();
        int totalSuccessful = totalSuccessfulSignals.get();
        
        stats.put("total_data_sources", totalSources);
        stats.put("total_signals_generated", totalSignalsGenerated.get());
        stats.put("total_successful_signals", totalSuccessful);
        stats.put("overall_success_rate", totalSignals > 0 ? 
                   (double) totalSuccessful / totalSignals * 100 : 0.0);
        
        // Estatísticas por fonte
        Map<String, Object> sourceStats = new HashMap<>();
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            String sourceName = entry.getKey();
            DataSource dataSource = entry.getValue();
            sourceStats.put(sourceName, Map.of(
                "is_active", dataSource.isActive(),
                "total_signals", dataSource.getTotalSignals(),
                "successful_signals", dataSource.getSuccessfulSignals(),
                "performance_metrics", performanceMetrics.get(sourceName)
            ));
        }
        
        stats.put("source_statistics", sourceStats);
        
        return stats;
    }
    
    /**
     * Método principal para demonstração
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("🔮 DEMONSTRAÇÃO DO ORACLE SERVICE");
        System.out.println("=".repeat(80));
        
        OracleService oracleService = new OracleService("oracle_logs");
        
        // Adiciona fontes de dados
        oracleService.addDataSource("tradingview", "TradingView", 
                                "https://api.tradingview.com", "tv_key", "tv_api_key");
        oracleService.addDataSource("yfinance", "YFinance", 
                                "https://api.yfinance.com", "yf_api_key");
        oracleService.addDataSource("glassnode", "Glassnode", 
                                "https://api.glassnode.com", "gl_api_key");
        oracleService.addDataSource("santiment", "Santiment", 
                                "https://api.santiment.com", "sentiment_api_key");
        
        // Inicia coleta
        oracleService.startAllDataCollection();
        
        // Simula coleta por um tempo
        try {
            Thread.sleep(5000);
            
            // Coleta de uma fonte específica
            List<OracleSignal> tradingSignals = oracleService.collectFromSource("tradingview").get();
            System.out.println("📊 Sinais TradingView: " + tradingSignals.size());
            
            List<OracleSignal> yfinanceSignals = oracleService.collectFromSource("yfinance").get();
            System.out.println("📊 Sinais YFinance: " + yfinanceSignals.size());
            
            // Exibe alguns sinais
            tradingSignals.stream().limit(5).forEach(signal -> {
                System.out.println("  " + signal.getSymbol() + ": " + 
                                 signal.getSignalType() + " - " + 
                                 String.format("%.2f", signal.getConfidence() * 100) + "%"));
            });
            
            // Exibe estatísticas
            Map<String, PerformanceMetrics> metrics = oracleService.getPerformanceMetrics();
            metrics.forEach((source, metrics) -> {
                System.out.println("📊 Fonte: " + source + " - " + 
                             metrics.getTotalSignals() + " sinais, " + 
                             String.format("%.2f%%", metrics.getSuccessRate()) + " sucesso");
            });
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n✅ DEMONSTRAÇÃO CONCLUÍDA!");
        System.out.println("=".repeat(80));
    }
}
