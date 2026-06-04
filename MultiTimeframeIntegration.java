package modulo_emocional.Decisao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * 🔄 INTEGRAÇÃO MULTI-TIMEFRAME COM LEXTRADER-IAG
 * =================================================
 * 
 * Integração da estratégia multi-timeframe com o sistema existente
 * Conecta com learningService e DecisionEngine
 * 
 * Uso:
 *     IntegratedMultiTimeframeTrader trader = new IntegratedMultiTimeframeTrader();
 *     TradingDecision decision = trader.processMarketData(marketDataMap);
 */

// ============================================================
// CANDLE DATA CLASS
// ============================================================

class CandleData {
    private LocalDateTime timestamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    
    public CandleData(LocalDateTime timestamp, double open, double high, double low, double close, double volume) {
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
    
    // Getters and setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public double getOpen() { return open; }
    public void setOpen(double open) { this.open = open; }
    
    public double getHigh() { return high; }
    public void setHigh(double high) { this.high = high; }
    
    public double getLow() { return low; }
    public void setLow(double low) { this.low = low; }
    
    public double getClose() { return close; }
    public void setClose(double close) { this.close = close; }
    
    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }
    
    @Override
    public String toString() {
        return String.format("CandleData{timestamp=%s, open=%.2f, high=%.2f, low=%.2f, close=%.2f, volume=%.2f}",
            timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), open, high, low, close, volume);
    }
}

// ============================================================
// MULTI-TIMEFRAME TRADER (Simplified version)
// ============================================================

class MultiTimeframeTrader {
    private String symbol;
    
    public MultiTimeframeTrader(String symbol) {
        this.symbol = symbol;
    }
    
    public MultiTimeframeSignal analyzeAllTimeframes(List<CandleData> candles1m, 
                                               List<CandleData> candles3m, 
                                               List<CandleData> candles5m) {
        // Simplified implementation - in real scenario would analyze each timeframe
        MultiTimeframeSignal signal = new MultiTimeframeSignal(symbol);
        
        // Create mock signals for demonstration
        TimeframeSignal signal1m = new TimeframeSignal(TradeSignal.BUY, Trend.UPTREND, 0.8, 65.0);
        TimeframeSignal signal3m = new TimeframeSignal(TradeSignal.BUY, Trend.UPTREND, 0.75, 62.0);
        TimeframeSignal signal5m = new TimeframeSignal(TradeSignal.WEAK_BUY, Trend.SIDEWAYS, 0.6, 58.0);
        
        signal.setSignal1m(signal1m);
        signal.setSignal3m(signal3m);
        signal.setSignal5m(signal5m);
        
        signal.setFinalSignal(TradeSignal.BUY);
        signal.setOverallConfidence(0.78);
        signal.setRiskLevel("MEDIUM");
        signal.setSuggestedStopLoss(44000.0);
        signal.setSuggestedTakeProfit(46000.0);
        signal.setSuggestedRiskReward(1.5);
        signal.setReason("Análise multi-timeframe indica tendência de compra");
        
        return signal;
    }
}

// ============================================================
// TRADING DECISION CLASS
// ============================================================

class TradingDecision {
    /** Decisão de trading consolidada */
    
    private LocalDateTime timestamp;
    private String symbol;
    
    // Sinal
    private String signal;  // BUY, SELL, HOLD
    private double confidence;
    private String riskLevel;
    
    // Detalhes
    private MultiTimeframeSignal multiTimeframeAnalysis;
    
    // Execução
    private double entryPrice;
    private double stopLoss;
    private double takeProfit;
    private double riskRewardRatio;
    
    // Metadata
    private String analysisType;
    private String version;
    
    public TradingDecision(LocalDateTime timestamp, String symbol, String signal, double confidence, String riskLevel) {
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.signal = signal;
        this.confidence = confidence;
        this.riskLevel = riskLevel;
        this.analysisType = "MULTI_TIMEFRAME_1m_3m_5m";
        this.version = "1.0";
        this.entryPrice = 0.0;
        this.stopLoss = 0.0;
        this.takeProfit = 0.0;
        this.riskRewardRatio = 1.0;
    }
    
    // Getters and setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public String getSignal() { return signal; }
    public void setSignal(String signal) { this.signal = signal; }
    
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public MultiTimeframeSignal getMultiTimeframeAnalysis() { return multiTimeframeAnalysis; }
    public void setMultiTimeframeAnalysis(MultiTimeframeSignal multiTimeframeAnalysis) { this.multiTimeframeAnalysis = multiTimeframeAnalysis; }
    
    public double getEntryPrice() { return entryPrice; }
    public void setEntryPrice(double entryPrice) { this.entryPrice = entryPrice; }
    
    public double getStopLoss() { return stopLoss; }
    public void setStopLoss(double stopLoss) { this.stopLoss = stopLoss; }
    
    public double getTakeProfit() { return takeProfit; }
    public void setTakeProfit(double takeProfit) { this.takeProfit = takeProfit; }
    
    public double getRiskRewardRatio() { return riskRewardRatio; }
    public void setRiskRewardRatio(double riskRewardRatio) { this.riskRewardRatio = riskRewardRatio; }
    
    public String getAnalysisType() { return analysisType; }
    public void setAnalysisType(String analysisType) { this.analysisType = analysisType; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}

// ============================================================
// INTEGRATED MULTI-TIMEFRAME TRADER
// ============================================================

class IntegratedMultiTimeframeTrader {
    /** Trader integrado com sistema LEXTRADER */
    
    private static final Logger logger = Logger.getLogger(IntegratedMultiTimeframeTrader.class.getName());
    
    private List<String> symbols;
    private Map<String, MultiTimeframeTrader> traders;
    
    // Histórico de decisões
    private List<TradingDecision> decisionHistory;
    private int maxHistory;
    
    private ExecutorService executorService;
    
    public IntegratedMultiTimeframeTrader(List<String> symbols) {
        /**
         * Inicializa trader integrado
         * 
         * Args:
         *     symbols: Lista de símbolos a monitorar (ex: ["BTC/USDT", "ETH/USDT"])
         */
        this.symbols = symbols != null ? new ArrayList<>(symbols) : 
            Arrays.asList("BTC/USDT", "ETH/USDT", "BNB/USDT");
        
        this.traders = new HashMap<>();
        for (String symbol : this.symbols) {
            traders.put(symbol, new MultiTimeframeTrader(symbol));
        }
        
        this.decisionHistory = new ArrayList<>();
        this.maxHistory = 100;
        this.executorService = Executors.newFixedThreadPool(4);
        
        logger.info("Trader integrado inicializado para " + this.symbols.size() + " símbolos");
    }
    
    public IntegratedMultiTimeframeTrader() {
        this(null);
    }
    
    public TradingDecision processMarketData(Map<String, Object> marketData) {
        /**
         * Processa dados de mercado e gera decisão
         * 
         * Args:
         *     marketData: Map com estrutura:
         *     {
         *         'symbol': 'BTC/USDT',
         *         'candles_1m': [...],
         *         'candles_3m': [...],
         *         'candles_5m': [...]
         *     }
         * 
         * Returns:
         *     TradingDecision com análise consolidada
         */
        
        String symbol = (String) marketData.getOrDefault("symbol", "BTC/USDT");
        
        // Converter dados para CandleData
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> candles1mRaw = (List<Map<String, Object>>) marketData.getOrDefault("candles_1m", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> candles3mRaw = (List<Map<String, Object>>) marketData.getOrDefault("candles_3m", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> candles5mRaw = (List<Map<String, Object>>) marketData.getOrDefault("candles_5m", new ArrayList<>());
        
        List<CandleData> candles1m = convertToCandleData(candles1mRaw);
        List<CandleData> candles3m = convertToCandleData(candles3mRaw);
        List<CandleData> candles5m = convertToCandleData(candles5mRaw);
        
        // Validar dados
        if (!(candles1m.size() >= 20 && candles3m.size() >= 20 && candles5m.size() >= 20)) {
            logger.warning("Dados insuficientes para " + symbol);
            return createHoldDecision(symbol, marketData);
        }
        
        // Obter trader
        MultiTimeframeTrader trader = traders.getOrDefault(symbol, new MultiTimeframeTrader(symbol));
        
        // Analisar todos os timeframes
        MultiTimeframeSignal mtSignal = trader.analyzeAllTimeframes(candles1m, candles3m, candles5m);
        
        // Criar decisão
        TradingDecision decision = new TradingDecision(
            LocalDateTime.now(),
            symbol,
            signalToAction(mtSignal.getFinalSignal()),
            mtSignal.getOverallConfidence(),
            mtSignal.getRiskLevel()
        );
        
        decision.setMultiTimeframeAnalysis(mtSignal);
        decision.setEntryPrice(candles1m.get(candles1m.size() - 1).getClose());
        decision.setStopLoss(mtSignal.getSuggestedStopLoss());
        decision.setTakeProfit(mtSignal.getSuggestedTakeProfit());
        decision.setRiskRewardRatio(mtSignal.getSuggestedRiskReward());
        
        // Armazenar no histórico
        addToHistory(decision);
        
        logger.info("Decisão gerada para " + symbol + ": " + decision.signal + 
                   " (confiança: " + String.format("%.0f%%", decision.getConfidence() * 100) + ")");
        
        return decision;
    }
    
    public List<TradingDecision> processMultipleSymbols(List<Map<String, Object>> marketDataList) {
        /**
         * Processa múltiplos símbolos
         * 
         * Args:
         *     marketDataList: Lista de maps com dados de mercado
         * 
         * Returns:
         *     Lista de TradingDecisions
         */
        List<TradingDecision> decisions = new ArrayList<>();
        
        for (Map<String, Object> marketData : marketDataList) {
            try {
                TradingDecision decision = processMarketData(marketData);
                decisions.add(decision);
            } catch (Exception e) {
                String symbol = (String) marketData.getOrDefault("symbol", "unknown");
                logger.severe("Erro ao processar " + symbol + ": " + e.getMessage());
            }
        }
        
        return decisions;
    }
    
    public CompletableFuture<TradingDecision> processMarketDataAsync(Map<String, Object> marketData) {
        /** Versão assíncrona de processMarketData */
        return CompletableFuture.supplyAsync(() -> processMarketData(marketData), executorService);
    }
    
    private List<CandleData> convertToCandleData(List<Map<String, Object>> candles) {
        /** Converte lista de maps para CandleData */
        List<CandleData> result = new ArrayList<>();
        
        for (int i = 0; i < candles.size(); i++) {
            Map<String, Object> candle = candles.get(i);
            try {
                // Suportar diferentes formatos
                result.add(new CandleData(
                    LocalDateTime.now().minusMinutes(candles.size() - i),
                    ((Number) candle.getOrDefault("open", 0)).doubleValue(),
                    ((Number) candle.getOrDefault("high", 0)).doubleValue(),
                    ((Number) candle.getOrDefault("low", 0)).doubleValue(),
                    ((Number) candle.getOrDefault("close", 0)).doubleValue(),
                    ((Number) candle.getOrDefault("volume", 0)).doubleValue()
                ));
            } catch (Exception e) {
                logger.warning("Erro ao converter candle: " + e.getMessage());
                continue;
            }
        }
        
        return result;
    }
    
    private String signalToAction(TradeSignal signal) {
        /** Converte TradeSignal para ação */
        if (signal == TradeSignal.STRONG_BUY || signal == TradeSignal.BUY || signal == TradeSignal.WEAK_BUY) {
            return "BUY";
        } else if (signal == TradeSignal.STRONG_SELL || signal == TradeSignal.SELL || signal == TradeSignal.WEAK_SELL) {
            return "SELL";
        } else {
            return "HOLD";
        }
    }
    
    private TradingDecision createHoldDecision(String symbol, Map<String, Object> marketData) {
        /** Cria decisão HOLD padrão */
        double currentPrice = ((Number) marketData.getOrDefault("current_price", 0)).doubleValue();
        
        TradingDecision decision = new TradingDecision(
            LocalDateTime.now(),
            symbol,
            "HOLD",
            0.0,
            "UNKNOWN"
        );
        
        decision.setEntryPrice(currentPrice);
        decision.setStopLoss(currentPrice * 0.98);
        decision.setTakeProfit(currentPrice * 1.02);
        decision.setRiskRewardRatio(1.0);
        
        return decision;
    }
    
    private void addToHistory(TradingDecision decision) {
        /** Adiciona decisão ao histórico */
        decisionHistory.add(decision);
        
        // Manter limite de histórico
        while (decisionHistory.size() > maxHistory) {
            decisionHistory.remove(0);
        }
    }
    
    public Map<String, Object> getDecisionSummary(String symbol) {
        /** Retorna resumo das últimas decisões */
        
        List<TradingDecision> relevant;
        if (symbol != null && !symbol.isEmpty()) {
            relevant = new ArrayList<>();
            for (TradingDecision d : decisionHistory) {
                if (d.getSymbol().equals(symbol)) {
                    relevant.add(d);
                }
            }
        } else {
            relevant = new ArrayList<>(decisionHistory);
        }
        
        if (relevant.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Sem decisões registradas");
            return result;
        }
        
        int buyCount = 0;
        int sellCount = 0;
        int holdCount = 0;
        double totalConfidence = 0;
        
        for (TradingDecision d : relevant) {
            if ("BUY".equals(d.getSignal())) {
                buyCount++;
            } else if ("SELL".equals(d.getSignal())) {
                sellCount++;
            } else {
                holdCount++;
            }
            totalConfidence += d.getConfidence();
        }
        
        double avgConfidence = totalConfidence / relevant.size();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("total_decisions", relevant.size());
        summary.put("buy_count", buyCount);
        summary.put("sell_count", sellCount);
        summary.put("hold_count", holdCount);
        summary.put("buy_percentage", buyCount * 100.0 / relevant.size());
        summary.put("sell_percentage", sellCount * 100.0 / relevant.size());
        summary.put("hold_percentage", holdCount * 100.0 / relevant.size());
        summary.put("average_confidence", avgConfidence);
        summary.put("last_decision", relevant.get(relevant.size() - 1).getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
        
        return summary;
    }
    
    public Map<String, Object> getDecisionSummary() {
        return getDecisionSummary(null);
    }
    
    public Map<String, Object> exportDecision(TradingDecision decision) {
        /** Exporta decisão para formato de mapa */
        
        Map<String, Object> export = new HashMap<>();
        export.put("timestamp", decision.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
        export.put("symbol", decision.getSymbol());
        export.put("signal", decision.getSignal());
        export.put("confidence", decision.getConfidence());
        export.put("risk_level", decision.getRiskLevel());
        export.put("entry_price", decision.getEntryPrice());
        export.put("stop_loss", decision.getStopLoss());
        export.put("take_profit", decision.getTakeProfit());
        export.put("risk_reward_ratio", decision.getRiskRewardRatio());
        export.put("analysis_type", decision.getAnalysisType());
        export.put("version", decision.getVersion());
        
        // Timeframe signals
        Map<String, Object> timeframeSignals = new HashMap<>();
        
        if (decision.getMultiTimeframeAnalysis() != null) {
            MultiTimeframeSignal analysis = decision.getMultiTimeframeAnalysis();
            
            if (analysis.getSignal1m() != null) {
                Map<String, Object> tf1m = new HashMap<>();
                tf1m.put("signal", analysis.getSignal1m().getSignal().toString());
                tf1m.put("trend", analysis.getSignal1m().getTrend().toString());
                tf1m.put("rsi", analysis.getSignal1m().getRsi());
                tf1m.put("confidence", analysis.getSignal1m().getConfidence());
                timeframeSignals.put("1m", tf1m);
            }
            
            if (analysis.getSignal3m() != null) {
                Map<String, Object> tf3m = new HashMap<>();
                tf3m.put("signal", analysis.getSignal3m().getSignal().toString());
                tf3m.put("trend", analysis.getSignal3m().getTrend().toString());
                tf3m.put("rsi", analysis.getSignal3m().getRsi());
                tf3m.put("confidence", analysis.getSignal3m().getConfidence());
                timeframeSignals.put("3m", tf3m);
            }
            
            if (analysis.getSignal5m() != null) {
                Map<String, Object> tf5m = new HashMap<>();
                tf5m.put("signal", analysis.getSignal5m().getSignal().toString());
                tf5m.put("trend", analysis.getSignal5m().getTrend().toString());
                tf5m.put("rsi", analysis.getSignal5m().getRsi());
                tf5m.put("confidence", analysis.getSignal5m().getConfidence());
                timeframeSignals.put("5m", tf5m);
            }
            
            export.put("reason", analysis.getReason() != null ? analysis.getReason() : "Dados insuficientes");
        } else {
            export.put("reason", "Dados insuficientes");
        }
        
        export.put("timeframe_signals", timeframeSignals);
        
        return export;
    }
    
    // Getters
    public List<String> getSymbols() { return new ArrayList<>(symbols); }
    public List<TradingDecision> getDecisionHistory() { return new ArrayList<>(decisionHistory); }
    public int getMaxHistory() { return maxHistory; }
    public void setMaxHistory(int maxHistory) { this.maxHistory = maxHistory; }
    
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

// ============================================================
// MAIN CLASS FOR DEMONSTRATION
// ============================================================

public class MultiTimeframeIntegration {
    
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("DECISÕES DE TRADING INTEGRADAS");
        System.out.println("=".repeat(70));
        
        // Criar trader
        IntegratedMultiTimeframeTrader trader = new IntegratedMultiTimeframeTrader(
            Arrays.asList("BTC/USDT", "ETH/USDT")
        );
        
        try {
            // Processar BTC
            Map<String, Object> btcData = createTestData("BTC/USDT", 45000.0);
            TradingDecision btcDecision = trader.processMarketData(btcData);
            
            // Processar ETH
            Map<String, Object> ethData = createTestData("ETH/USDT", 2500.0);
            TradingDecision ethDecision = trader.processMarketData(ethData);
            
            for (TradingDecision decision : Arrays.asList(btcDecision, ethDecision)) {
                System.out.println("\n🔷 " + decision.getSymbol());
                System.out.println("  Sinal: " + decision.getSignal() + 
                                 " (confiança: " + String.format("%.0f%%", decision.getConfidence() * 100) + ")");
                System.out.println("  Preço de entrada: $" + String.format("%.2f", decision.getEntryPrice()));
                System.out.println("  Stop Loss: $" + String.format("%.2f", decision.getStopLoss()));
                System.out.println("  Take Profit: $" + String.format("%.2f", decision.getTakeProfit()));
                System.out.println("  Risk/Reward: " + String.format("%.2f", decision.getRiskRewardRatio()) + ":1");
                System.out.println("  Nível de risco: " + decision.getRiskLevel());
            }
            
            System.out.println("\n📊 RESUMO GERAL:");
            System.out.println(mapToJson(trader.getDecisionSummary()));
            
            System.out.println("\n✅ Exportar decisão BTC:");
            System.out.println(mapToJson(trader.exportDecision(btcDecision)));
            
        } finally {
            trader.shutdown();
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
    }
    
    private static Map<String, Object> createTestData(String symbol, double basePrice) {
        /** Cria dados de teste */
        Random random = new Random();
        List<Map<String, Object>> candles = new ArrayList<>();
        double price = basePrice;
        
        for (int i = 0; i < 50; i++) {
            Map<String, Object> candle = new HashMap<>();
            candle.put("timestamp", LocalDateTime.now().minusMinutes(50 - i));
            candle.put("open", price);
            candle.put("high", price + random.nextDouble() * 100);
            candle.put("low", price - random.nextDouble() * 100);
            candle.put("close", price + (random.nextDouble() - 0.5) * 100);
            candle.put("volume", 1000 + random.nextDouble() * 4000);
            candles.add(candle);
            price = (Double) candle.get("close");
        }
        
        // Create 3m and 5m data by sampling
        List<Map<String, Object>> candles3m = new ArrayList<>();
        List<Map<String, Object>> candles5m = new ArrayList<>();
        
        for (int i = 0; i < candles.size(); i += 3) {
            candles3m.add(candles.get(i));
        }
        
        for (int i = 0; i < candles.size(); i += 5) {
            candles5m.add(candles.get(i));
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("symbol", symbol);
        data.put("current_price", price);
        data.put("candles_1m", candles);
        data.put("candles_3m", candles3m);
        data.put("candles_5m", candles5m);
        
        return data;
    }
    
    private static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",\n");
            }
            first = false;
            
            json.append("  \"").append(entry.getKey()).append("\": ");
            
            Object value = entry.getValue();
            if (value instanceof Map) {
                json.append(mapToJson((Map<String, Object>) value));
            } else if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Double) {
                json.append(String.format("%.4f", (Double) value));
            } else {
                json.append(value);
            }
        }
        
        json.append("\n}");
        return json.toString();
    }
}
