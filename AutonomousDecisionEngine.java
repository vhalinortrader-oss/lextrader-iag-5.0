package com.lextrader.iag4.autonomous;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Motor de Decisão Autônomo
 * Versão Java convertida do Python original
 */
public class AutonomousDecisionEngine {
    
    // Enums para tipos de dados
    public enum Action {
        BUY, SELL, HOLD, TAKE_PROFIT, STOP_LOSS
    }
    
    public enum Status {
        PENDING, EXECUTED, CANCELLED
    }
    
    public enum BollingerPosition {
        UPPER, MIDDLE, LOWER
    }
    
    // Estruturas de dados equivalentes às interfaces TypeScript
    public static class TechnicalIndicators {
        private final double rsi;
        private final double macd;
        private final BollingerPosition bollinger;
        private final double volume;
        private final double volatility;
        
        public TechnicalIndicators(double rsi, double macd, BollingerPosition bollinger, 
                                 double volume, double volatility) {
            this.rsi = rsi;
            this.macd = macd;
            this.bollinger = bollinger;
            this.volume = volume;
            this.volatility = volatility;
        }
        
        // Getters
        public double getRsi() { return rsi; }
        public double getMacd() { return macd; }
        public BollingerPosition getBollinger() { return bollinger; }
        public double getVolume() { return volume; }
        public double getVolatility() { return volatility; }
    }
    
    public static class AutonomousDecision {
        private final String id;
        private final String symbol;
        private final Action action;
        private final double confidence;
        private final String reasoning;
        private final double entryPrice;
        private final double currentPrice;
        private final double takeProfitPrice;
        private final double stopLossPrice;
        private final double riskRewardRatio;
        private final double expectedReturn;
        private final LocalDateTime timestamp;
        private final Status status;
        
        public AutonomousDecision(String id, String symbol, Action action, double confidence,
                                String reasoning, double entryPrice, double currentPrice,
                                double takeProfitPrice, double stopLossPrice,
                                double riskRewardRatio, double expectedReturn) {
            this.id = id;
            this.symbol = symbol;
            this.action = action;
            this.confidence = confidence;
            this.reasoning = reasoning;
            this.entryPrice = entryPrice;
            this.currentPrice = currentPrice;
            this.takeProfitPrice = takeProfitPrice;
            this.stopLossPrice = stopLossPrice;
            this.riskRewardRatio = riskRewardRatio;
            this.expectedReturn = expectedReturn;
            this.timestamp = LocalDateTime.now();
            this.status = Status.PENDING;
        }
        
        // Getters
        public String getId() { return id; }
        public String getSymbol() { return symbol; }
        public Action getAction() { return action; }
        public double getConfidence() { return confidence; }
        public String getReasoning() { return reasoning; }
        public double getEntryPrice() { return entryPrice; }
        public double getCurrentPrice() { return currentPrice; }
        public double getTakeProfitPrice() { return takeProfitPrice; }
        public double getStopLossPrice() { return stopLossPrice; }
        public double getRiskRewardRatio() { return riskRewardRatio; }
        public double getExpectedReturn() { return expectedReturn; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Status getStatus() { return status; }
    }
    
    // Constantes do sistema
    private static final double MAX_POSITION_SIZE = 0.15; // 15% do capital
    private static final double MIN_CONFIDENCE_THRESHOLD = 0.85; // 85% mínimo
    private static final double RISK_FREE_RATE = 0.02; // 2% anual
    private static final double MAX_RISK_REWARD_RATIO = 3.0; // 1:3 mínimo
    
    // Estado do motor de decisão
    private final Map<String, AutonomousDecision> activeDecisions;
    private final List<AutonomousDecision> decisionHistory;
    private final AtomicInteger decisionCounter;
    private final Map<String, TechnicalIndicators> marketData;
    
    public AutonomousDecisionEngine() {
        this.activeDecisions = new HashMap<>();
        this.decisionHistory = new ArrayList<>();
        this.decisionCounter = new AtomicInteger(0);
        this.marketData = new HashMap<>();
    }
    
    /**
     * Processa dados de mercado e gera decisões autônomas
     */
    public List<AutonomousDecision> processMarketData(String symbol, TechnicalIndicators indicators) {
        // Atualiza dados de mercado
        marketData.put(symbol, indicators);
        
        List<AutonomousDecision> decisions = new ArrayList<>();
        
        // Análise técnica para gerar sinais
        Action signal = analyzeTechnicalIndicators(indicators);
        if (signal != Action.HOLD) {
            AutonomousDecision decision = createDecision(symbol, signal, indicators);
            if (decision != null) {
                decisions.add(decision);
                activeDecisions.put(decision.getId(), decision);
                decisionHistory.add(decision);
            }
        }
        
        return decisions;
    }
    
    /**
     * Analisa indicadores técnicos para determinar ação
     */
    private Action analyzeTechnicalIndicators(TechnicalIndicators indicators) {
        // Lógica de análise baseada em RSI
        if (indicators.getRsi() < 30 && indicators.getBollinger() == BollingerPosition.LOWER) {
            return Action.BUY;
        }
        
        if (indicators.getRsi() > 70 && indicators.getBollinger() == BollingerPosition.UPPER) {
            return Action.SELL;
        }
        
        // Análise de MACD
        if (indicators.getMacd() > 0 && indicators.getRsi() < 50) {
            return Action.BUY;
        }
        
        if (indicators.getMacd() < 0 && indicators.getRsi() > 50) {
            return Action.SELL;
        }
        
        return Action.HOLD;
    }
    
    /**
     * Cria uma decisão autônoma baseada na análise
     */
    private AutonomousDecision createDecision(String symbol, Action action, TechnicalIndicators indicators) {
        String decisionId = "DEC_" + decisionCounter.incrementAndGet() + "_" + System.currentTimeMillis();
        
        // Calcula preços e métricas
        double currentPrice = getCurrentPrice(symbol);
        double confidence = calculateConfidence(indicators, action);
        
        if (confidence < MIN_CONFIDENCE_THRESHOLD) {
            return null;
        }
        
        double entryPrice = currentPrice;
        double stopLossPrice = calculateStopLoss(entryPrice, action, indicators);
        double takeProfitPrice = calculateTakeProfit(entryPrice, action, indicators);
        double riskRewardRatio = calculateRiskRewardRatio(entryPrice, stopLossPrice, takeProfitPrice);
        
        if (riskRewardRatio < MAX_RISK_REWARD_RATIO) {
            return null;
        }
        
        double expectedReturn = calculateExpectedReturn(action, indicators, riskRewardRatio);
        String reasoning = generateReasoning(indicators, action, confidence);
        
        return new AutonomousDecision(
            decisionId, symbol, action, confidence, reasoning,
            entryPrice, currentPrice, takeProfitPrice, stopLossPrice,
            riskRewardRatio, expectedReturn
        );
    }
    
    /**
     * Calcula confiança da decisão baseada nos indicadores
     */
    private double calculateConfidence(TechnicalIndicators indicators, Action action) {
        double confidence = 0.0;
        
        // Confiança baseada no RSI
        if (action == Action.BUY && indicators.getRsi() < 30) {
            confidence += 0.4;
        } else if (action == Action.SELL && indicators.getRsi() > 70) {
            confidence += 0.4;
        }
        
        // Confiança baseada no MACD
        if ((action == Action.BUY && indicators.getMacd() > 0) ||
            (action == Action.SELL && indicators.getMacd() < 0)) {
            confidence += 0.3;
        }
        
        // Confiança baseada nas Bandas de Bollinger
        if ((action == Action.BUY && indicators.getBollinger() == BollingerPosition.LOWER) ||
            (action == Action.SELL && indicators.getBollinger() == BollingerPosition.UPPER)) {
            confidence += 0.2;
        }
        
        // Confiança baseada no volume
        if (indicators.getVolume() > 1.5) { // Volume 50% acima da média
            confidence += 0.1;
        }
        
        return Math.min(1.0, confidence);
    }
    
    /**
     * Calcula preço de stop loss
     */
    private double calculateStopLoss(double entryPrice, Action action, TechnicalIndicators indicators) {
        double volatility = indicators.getVolatility();
        double stopLossDistance = entryPrice * volatility * 0.02; // 2% da volatilidade
        
        if (action == Action.BUY) {
            return entryPrice - stopLossDistance;
        } else {
            return entryPrice + stopLossDistance;
        }
    }
    
    /**
     * Calcula preço de take profit
     */
    private double calculateTakeProfit(double entryPrice, Action action, TechnicalIndicators indicators) {
        double volatility = indicators.getVolatility();
        double takeProfitDistance = entryPrice * volatility * 0.04; // 4% da volatilidade
        
        if (action == Action.BUY) {
            return entryPrice + takeProfitDistance;
        } else {
            return entryPrice - takeProfitDistance;
        }
    }
    
    /**
     * Calcula razão risco/retorno
     */
    private double calculateRiskRewardRatio(double entryPrice, double stopLoss, double takeProfit) {
        double risk = Math.abs(entryPrice - stopLoss);
        double reward = Math.abs(takeProfit - entryPrice);
        
        return risk > 0 ? reward / risk : 0.0;
    }
    
    /**
     * Calcula retorno esperado
     */
    private double calculateExpectedReturn(Action action, TechnicalIndicators indicators, double riskRewardRatio) {
        double baseReturn = 0.02; // 2% base
        
        // Ajuste baseado no RSI
        if (action == Action.BUY && indicators.getRsi() < 30) {
            baseReturn += 0.03;
        } else if (action == Action.SELL && indicators.getRsi() > 70) {
            baseReturn += 0.03;
        }
        
        // Ajuste baseado no MACD
        double macdStrength = Math.abs(indicators.getMacd());
        baseReturn += macdStrength * 0.01;
        
        // Ajuste baseado na razão risco/retorno
        baseReturn *= Math.min(2.0, riskRewardRatio / MAX_RISK_REWARD_RATIO);
        
        return baseReturn;
    }
    
    /**
     * Gera justificativa para a decisão
     */
    private String generateReasoning(TechnicalIndicators indicators, Action action, double confidence) {
        StringBuilder reasoning = new StringBuilder();
        
        reasoning.append("Decisão ").append(action.toString())
                 .append(String.format(" com %.1f%% confiança. ", confidence * 100));
        
        if (indicators.getRsi() < 30) {
            reasoning.append("RSI sobrevendido (").append(String.format("%.1f", indicators.getRsi())).append("). ");
        } else if (indicators.getRsi() > 70) {
            reasoning.append("RSI sobrecomprado (").append(String.format("%.1f", indicators.getRsi())).append("). ");
        }
        
        if (indicators.getBollinger() == BollingerPosition.LOWER) {
            reasoning.append("Preço na banda inferior de Bollinger. ");
        } else if (indicators.getBollinger() == BollingerPosition.UPPER) {
            reasoning.append("Preço na banda superior de Bollinger. ");
        }
        
        if (indicators.getMacd() > 0) {
            reasoning.append("MACD positivo. ");
        } else {
            reasoning.append("MACD negativo. ");
        }
        
        if (indicators.getVolume() > 1.5) {
            reasoning.append("Alto volume confirmado. ");
        }
        
        return reasoning.toString();
    }
    
    /**
     * Obtém preço atual do ativo (simulado)
     */
    private double getCurrentPrice(String symbol) {
        // Em implementação real, buscaria da API
        return 100.0 + ThreadLocalRandom.current().nextDouble(-5, 5);
    }
    
    /**
     * Atualiza status de uma decisão
     */
    public void updateDecisionStatus(String decisionId, Status status) {
        AutonomousDecision decision = activeDecisions.get(decisionId);
        if (decision != null) {
            // Em Java, precisaríamos de uma classe mutável ou usar padrão builder
            // Por simplicidade, apenas removemos das decisões ativas
            if (status == Status.EXECUTED || status == Status.CANCELLED) {
                activeDecisions.remove(decisionId);
            }
        }
    }
    
    /**
     * Obtém decisões ativas
     */
    public Map<String, AutonomousDecision> getActiveDecisions() {
        return new HashMap<>(activeDecisions);
    }
    
    /**
     * Obtém histórico de decisões
     */
    public List<AutonomousDecision> getDecisionHistory() {
        return new ArrayList<>(decisionHistory);
    }
    
    /**
     * Calcula estatísticas de performance
     */
    public Map<String, Object> getPerformanceStats() {
        Map<String, Object> stats = new HashMap<>();
        
        int totalDecisions = decisionHistory.size();
        long buyDecisions = decisionHistory.stream()
                .filter(d -> d.getAction() == Action.BUY)
                .count();
        long sellDecisions = decisionHistory.stream()
                .filter(d -> d.getAction() == Action.SELL)
                .count();
        
        double avgConfidence = decisionHistory.stream()
                .mapToDouble(AutonomousDecision::getConfidence)
                .average()
                .orElse(0.0);
        
        double avgRiskReward = decisionHistory.stream()
                .mapToDouble(AutonomousDecision::getRiskRewardRatio)
                .average()
                .orElse(0.0);
        
        stats.put("totalDecisions", totalDecisions);
        stats.put("activeDecisions", activeDecisions.size());
        stats.put("buyDecisions", buyDecisions);
        stats.put("sellDecisions", sellDecisions);
        stats.put("avgConfidence", avgConfidence);
        stats.put("avgRiskRewardRatio", avgRiskReward);
        
        return stats;
    }
    
    /**
     * Limpa decisões antigas
     */
    public void cleanupOldDecisions(int maxAgeHours) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(maxAgeHours);
        
        decisionHistory.removeIf(decision -> decision.getTimestamp().isBefore(cutoff));
        
        activeDecisions.entrySet().removeIf(entry -> 
            entry.getValue().getTimestamp().isBefore(cutoff)
        );
    }
}
