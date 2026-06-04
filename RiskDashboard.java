package modulo_emocional.Decisao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Dashboard de Risco - LEXTRADER-IAG 4.0
 * =====================================
 * Versão Java do RiskDashboard.py - Sistema avançado de gestão de risco
 * com monitoramento em tempo real, curva de patrimônio e configuração dinâmica
 */

// Enums
enum TradeStatus {
    PENDING,
    FILLED,
    CANCELLED,
    REJECTED
}

// Classes de Dados
class Trade {
    public String id;
    public LocalDateTime timestamp;
    public String symbol;
    public double quantity;
    public double price;
    public String side;  // 'BUY' or 'SELL'
    public TradeStatus status;
    public Double profit;
    
    public Trade(String id, LocalDateTime timestamp, String symbol, double quantity, 
                 double price, String side, TradeStatus status, Double profit) {
        this.id = id;
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.status = status;
        this.profit = profit;
    }
    
    public Map<String, Object> toDict() {
        Map<String, Object> dict = new HashMap<>();
        dict.put("id", id);
        dict.put("timestamp", timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        dict.put("symbol", symbol);
        dict.put("quantity", quantity);
        dict.put("price", price);
        dict.put("side", side);
        dict.put("status", status.name());
        dict.put("profit", profit);
        return dict;
    }
    
    @Override
    public String toString() {
        return String.format("Trade{id='%s', symbol='%s', side='%s', profit=%.2f}", 
                           id, symbol, side, profit != null ? profit : 0.0);
    }
}

class MarketDataPoint {
    public LocalDateTime timestamp;
    public double price;
    public double volume;
    public double volatility;
    
    public MarketDataPoint(LocalDateTime timestamp, double price, double volume, double volatility) {
        this.timestamp = timestamp;
        this.price = price;
        this.volume = volume;
        this.volatility = volatility;
    }
    
    public Map<String, Object> toDict() {
        Map<String, Object> dict = new HashMap<>();
        dict.put("timestamp", timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        dict.put("price", price);
        dict.put("volume", volume);
        dict.put("volatility", volatility);
        return dict;
    }
    
    @Override
    public String toString() {
        return String.format("MarketDataPoint{price=%.2f, volume=%.0f, volatility=%.2f}", 
                           price, volume, volatility);
    }
}

class RiskSettings {
    public double maxDrawdownLimit = 5.0;  // 5%
    public double maxPositionSize = 10.0;  // 10% do patrimônio
    public double stopLossDefault = 2.0;   // 2%
    public double volatilityThreshold = 15.0;  // 15%
    public double correlationLimit = 80.0;  // 80%
    
    public Map<String, Object> toDict() {
        Map<String, Object> dict = new HashMap<>();
        dict.put("maxDrawdownLimit", maxDrawdownLimit);
        dict.put("maxPositionSize", maxPositionSize);
        dict.put("stopLossDefault", stopLossDefault);
        dict.put("volatilityThreshold", volatilityThreshold);
        dict.put("correlationLimit", correlationLimit);
        return dict;
    }
    
    @Override
    public String toString() {
        return String.format("RiskSettings{maxDrawdown=%.1f%%, maxPosition=%.1f%%, stopLoss=%.1f%%}", 
                           maxDrawdownLimit, maxPositionSize, stopLossDefault);
    }
}

class EquityCurvePoint {
    public String tradeId;
    public double balance;
    public double drawdown;
    public String timestamp;
    
    public EquityCurvePoint(String tradeId, double balance, double drawdown, String timestamp) {
        this.tradeId = tradeId;
        this.balance = balance;
        this.drawdown = drawdown;
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("EquityCurvePoint{balance=%.2f, drawdown=%.2f%%, time='%s'}", 
                           balance, drawdown, timestamp);
    }
}

// Dashboard de Risco
class RiskDashboard {
    private List<Trade> trades;
    private List<MarketDataPoint> marketData;
    private double currentVolatility;
    private RiskSettings settings;
    private Random random;
    
    private ScheduledExecutorService scheduler;
    private AtomicBoolean running;
    
    private static final Logger logger = Logger.getLogger(RiskDashboard.class.getName());
    
    public RiskDashboard() {
        this.trades = new ArrayList<>();
        this.marketData = new ArrayList<>();
        this.currentVolatility = 2.5;
        this.settings = new RiskSettings();
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.running = new AtomicBoolean(true);
        this.random = new Random();
        
        initializeSampleData();
        startRealTimeUpdates();
        
        logger.info("RiskDashboard inicializado com sucesso");
    }
    
    private void initializeSampleData() {
        /** Inicializa dados de exemplo */
        LocalDateTime baseTime = LocalDateTime.now().minusHours(2);
        String[] symbols = {"BTC/USD", "ETH/USD", "SOL/USD", "ADA/USD"};
        
        // Trades de exemplo
        for (int i = 0; i < 15; i++) {
            Trade trade = new Trade(
                String.format("trade_%03d", i),
                baseTime.plusMinutes(i * 8),
                symbols[random.nextInt(symbols.length)],
                0.1 + random.nextDouble() * 4.9,
                100 + random.nextDouble() * 49900,
                random.nextBoolean() ? "BUY" : "SELL",
                TradeStatus.FILLED,
                -500 + random.nextDouble() * 1500
            );
            trades.add(trade);
        }
        
        // Dados de mercado de exemplo
        for (int i = 0; i < 50; i++) {
            MarketDataPoint point = new MarketDataPoint(
                baseTime.plusMinutes(i * 3),
                45000 + random.nextDouble() * 7000,
                1000 + random.nextDouble() * 4000,
                1.5 + random.nextDouble() * 2.0
            );
            marketData.add(point);
        }
        
        // Atualizar volatilidade atual
        if (!marketData.isEmpty()) {
            currentVolatility = marketData.get(marketData.size() - 1).volatility;
        }
    }
    
    private void startRealTimeUpdates() {
        /** Inicia atualizações em tempo real */
        scheduler.scheduleAtFixedRate(() -> {
            if (running.get()) {
                try {
                    updateMarketData();
                    updateVolatility();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Erro nas atualizações em tempo real", e);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
    
    private void updateMarketData() {
        /** Atualiza dados de mercado */
        MarketDataPoint newPoint = new MarketDataPoint(
            LocalDateTime.now(),
            45000 + random.nextDouble() * 7000,
            1000 + random.nextDouble() * 4000,
            1.5 + random.nextDouble() * 2.0
        );
        
        marketData.add(newPoint);
        
        // Manter apenas últimos 50 pontos
        if (marketData.size() > 50) {
            marketData = marketData.subList(marketData.size() - 50, marketData.size());
        }
    }
    
    private void updateVolatility() {
        /** Atualiza volatilidade atual */
        if (!marketData.isEmpty()) {
            currentVolatility = marketData.get(marketData.size() - 1).volatility;
        }
    }
    
    public List<EquityCurvePoint> calculateEquityCurve() {
        /** Calcula curva de patrimônio e drawdown */
        double runningBalance = 10000.0;  // Patrimônio base simulado
        double peak = runningBalance;
        List<EquityCurvePoint> curve = new ArrayList<>();
        
        // Ordenar trades cronologicamente
        List<Trade> sortedTrades = new ArrayList<>(trades);
        sortedTrades.sort(Comparator.comparing(t -> t.timestamp));
        
        for (Trade trade : sortedTrades) {
            if (trade.status != TradeStatus.FILLED) {
                continue;
            }
            
            double profit = trade.profit != null ? trade.profit : 0.0;
            runningBalance += profit;
            
            if (runningBalance > peak) {
                peak = runningBalance;
            }
            
            double drawdown = peak > 0 ? ((peak - runningBalance) / peak * 100) : 0;
            
            EquityCurvePoint point = new EquityCurvePoint(
                trade.id,
                runningBalance,
                drawdown,
                trade.timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
            );
            curve.add(point);
        }
        
        // Adicionar estado atual se não houver trades
        if (curve.isEmpty()) {
            EquityCurvePoint point = new EquityCurvePoint(
                "init",
                10000,
                0,
                "Agora"
            );
            curve.add(point);
        }
        
        return curve;
    }
    
    public double calculateRiskScore(double currentDrawdown) {
        /** Calcula pontuação de risco (0-100) */
        // Soma ponderada de volatilidade e drawdown
        double riskScore = (currentVolatility * 10) + (currentDrawdown * 5);
        return Math.min(100, Math.max(0, riskScore));
    }
    
    public String getRiskColor(double score) {
        /** Retorna cor baseada no score de risco */
        if (score < 20) {
            return "#10b981";  // Verde
        } else if (score < 50) {
            return "#f59e0b";  // Amarelo
        } else if (score < 80) {
            return "#f97316";  // Laranja
        } else {
            return "#ef4444";  // Vermelho
        }
    }
    
    public String getRiskLabel(double score) {
        /** Retorna label baseado no score de risco */
        if (score < 20) {
            return "SEGURO";
        } else if (score < 50) {
            return "MODERADO";
        } else if (score < 80) {
            return "ELEVADO";
        } else {
            return "CRÍTICO";
        }
    }
    
    public void updateSettings(RiskSettings newSettings) {
        /** Atualiza configurações de risco */
        this.settings = newSettings;
        logger.info("Configurações de risco atualizadas");
    }
    
    public void addTrade(Trade trade) {
        /** Adiciona novo trade */
        trades.add(trade);
        logger.info("Novo trade adicionado: " + trade.id);
    }
    
    public void updateTradeStatus(String tradeId, TradeStatus newStatus) {
        /** Atualiza status de um trade */
        for (Trade trade : trades) {
            if (trade.id.equals(tradeId)) {
                trade.status = newStatus;
                logger.info("Status do trade " + tradeId + " atualizado para " + newStatus);
                return;
            }
        }
        logger.warning("Trade não encontrado: " + tradeId);
    }
    
    // Getters
    public List<Trade> getTrades() {
        return new ArrayList<>(trades);
    }
    
    public List<MarketDataPoint> getMarketData() {
        return new ArrayList<>(marketData);
    }
    
    public double getCurrentVolatility() {
        return currentVolatility;
    }
    
    public RiskSettings getSettings() {
        return settings;
    }
    
    public Map<String, Object> getMetrics() {
        /** Retorna métricas principais */
        List<EquityCurvePoint> equityCurve = calculateEquityCurve();
        double maxDrawdown = equityCurve.stream()
            .mapToDouble(p -> p.drawdown)
            .max()
            .orElse(0.0);
        double currentDrawdown = equityCurve.isEmpty() ? 0.0 : 
            equityCurve.get(equityCurve.size() - 1).drawdown;
        double riskScore = calculateRiskScore(currentDrawdown);
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("maxDrawdown", maxDrawdown);
        metrics.put("currentDrawdown", currentDrawdown);
        metrics.put("riskScore", riskScore);
        metrics.put("riskColor", getRiskColor(riskScore));
        metrics.put("riskLabel", getRiskLabel(riskScore));
        metrics.put("currentVolatility", currentVolatility);
        metrics.put("totalTrades", trades.size());
        metrics.put("filledTrades", trades.stream()
            .filter(t -> t.status == TradeStatus.FILLED)
            .count());
        metrics.put("totalProfit", trades.stream()
            .filter(t -> t.profit != null)
            .mapToDouble(t -> t.profit)
            .sum());
        
        return metrics;
    }
    
    public void shutdown() {
        /** Encerra o sistema de forma segura */
        running.set(false);
        
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        logger.info("RiskDashboard encerrado com sucesso");
    }
}

// Classe Principal para Demonstração
public class RiskDashboardApp {
    
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Dashboard de Risco...");
        
        RiskDashboard dashboard = new RiskDashboard();
        
        // Adicionar shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n⏹️ Encerrando dashboard...");
            dashboard.shutdown();
        }));
        
        try {
            // Demonstrar funcionalidades
            demonstrateDashboard(dashboard);
            
        } catch (Exception e) {
            System.err.println("❌ Erro na demonstração: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dashboard.shutdown();
        }
    }
    
    private static void demonstrateDashboard(RiskDashboard dashboard) {
        /** Demonstra funcionalidades do dashboard */
        
        System.out.println("🛡️ DEMONSTRAÇÃO DO DASHBOARD DE RISCO");
        System.out.println("=".repeat(60));
        
        // 1. Estado inicial
        System.out.println("\n1. ESTADO INICIAL:");
        showDashboardStatus(dashboard);
        
        // 2. Calcular curva de patrimônio
        System.out.println("\n2. CURVA DE PATRIMÔNIO:");
        showEquityCurve(dashboard);
        
        // 3. Métricas de risco
        System.out.println("\n3. MÉTRICAS DE RISCO:");
        showRiskMetrics(dashboard);
        
        // 4. Configurações
        System.out.println("\n4. CONFIGURAÇÕES ATUAIS:");
        showSettings(dashboard);
        
        // 5. Simular atualizações
        System.out.println("\n5. SIMULANDO ATUALIZAÇÕES:");
        simulateRealTimeUpdates(dashboard);
        
        // 6. Estado final
        System.out.println("\n6. ESTADO FINAL:");
        showDashboardStatus(dashboard);
        
        System.out.println("\n✅ Demonstração concluída!");
    }
    
    private static void showDashboardStatus(RiskDashboard dashboard) {
        /** Mostra status geral do dashboard */
        System.out.println("   📊 Trades Totais: " + dashboard.getTrades().size());
        System.out.println("   📈 Pontos de Mercado: " + dashboard.getMarketData().size());
        System.out.println("   🌊 Volatilidade Atual: " + String.format("%.2f", dashboard.getCurrentVolatility()));
        
        Map<String, Object> metrics = dashboard.getMetrics();
        System.out.println("   📉 Drawdown Máximo: " + String.format("%.2f%%", (Double) metrics.get("maxDrawdown")));
        System.out.println("   📉 Drawdown Atual: " + String.format("%.2f%%", (Double) metrics.get("currentDrawdown")));
        System.out.println("   🎯 Risk Score: " + String.format("%.1f/100", (Double) metrics.get("riskScore")));
        System.out.println("   🏷️  Risk Label: " + metrics.get("riskLabel"));
        System.out.println("   💰 Lucro Total: " + String.format("%.2f", (Double) metrics.get("totalProfit")));
    }
    
    private static void showEquityCurve(RiskDashboard dashboard) {
        /** Mostra curva de patrimônio */
        List<EquityCurvePoint> curve = dashboard.calculateEquityCurve();
        
        System.out.println("   📈 Pontos na Curva: " + curve.size());
        
        if (!curve.isEmpty()) {
            System.out.println("   📊 Últimos 5 pontos:");
            int start = Math.max(0, curve.size() - 5);
            
            for (int i = start; i < curve.size(); i++) {
                EquityCurvePoint point = curve.get(i);
                System.out.println(String.format("      %s - Balance: %.2f, Drawdown: %.2f%%", 
                    point.timestamp, point.balance, point.drawdown));
            }
        }
    }
    
    private static void showRiskMetrics(RiskDashboard dashboard) {
        /** Mostra métricas detalhadas de risco */
        Map<String, Object> metrics = dashboard.getMetrics();
        
        System.out.println("   🎯 Risk Score: " + String.format("%.1f/100", (Double) metrics.get("riskScore")));
        System.out.println("   🏷️  Categoria: " + metrics.get("riskLabel"));
        System.out.println("   🎨 Cor: " + metrics.get("riskColor"));
        
        double riskScore = (Double) metrics.get("riskScore");
        String riskLevel = getRiskLevelDescription(riskScore);
        System.out.println("   📋 Descrição: " + riskLevel);
        
        // Análise de trades
        long filledTrades = (Long) metrics.get("filledTrades");
        long totalTrades = (Long) metrics.get("totalTrades");
        double fillRate = totalTrades > 0 ? (double) filledTrades / totalTrades * 100 : 0;
        
        System.out.println("   📊 Taxa de Execução: " + String.format("%.1f%%", fillRate));
        System.out.println("   💰 Média de Lucro/Trade: " + String.format("%.2f", 
            filledTrades > 0 ? (Double) metrics.get("totalProfit") / filledTrades : 0));
    }
    
    private static String getRiskLevelDescription(double score) {
        /** Retorna descrição detalhada do nível de risco */
        if (score < 20) {
            return "Sistema operando em condições seguras com baixo risco";
        } else if (score < 50) {
            return "Risco moderado, monitoramento contínuo recomendado";
        } else if (score < 80) {
            return "Risco elevado, atenção necessária aos parâmetros";
        } else {
            return "Risco crítico, medidas de mitigação devem ser ativadas";
        }
    }
    
    private static void showSettings(RiskDashboard dashboard) {
        /** Mostra configurações atuais */
        RiskSettings settings = dashboard.getSettings();
        
        System.out.println("   📉 Limite Máximo Drawdown: " + String.format("%.1f%%", settings.maxDrawdownLimit));
        System.out.println("   📊 Tamanho Máximo Posição: " + String.format("%.1f%%", settings.maxPositionSize));
        System.out.println("   🛑 Stop Loss Padrão: " + String.format("%.1f%%", settings.stopLossDefault));
        System.out.println("   🌊 Limite Volatilidade: " + String.format("%.1f%%", settings.volatilityThreshold));
        System.out.println("   🔗 Limite Correlação: " + String.format("%.1f%%", settings.correlationLimit));
    }
    
    private static void simulateRealTimeUpdates(RiskDashboard dashboard) {
        /** Simula atualizações em tempo real */
        System.out.println("   🔄 Monitorando por 10 segundos...");
        
        try {
            for (int i = 0; i < 2; i++) {
                Thread.sleep(5000);
                
                // Adicionar novo trade
                Trade newTrade = new Trade(
                    String.format("trade_%03d", dashboard.getTrades().size() + 1),
                    LocalDateTime.now(),
                    "BTC/USD",
                    0.5 + Math.random() * 2.0,
                    45000 + Math.random() * 5000,
                    Math.random() > 0.5 ? "BUY" : "SELL",
                    TradeStatus.FILLED,
                    -200 + Math.random() * 800
                );
                
                dashboard.addTrade(newTrade);
                
                // Mostrar status atualizado
                Map<String, Object> metrics = dashboard.getMetrics();
                System.out.println(String.format("      [%d] Risk Score: %.1f, Volatilidade: %.2f, Trades: %d", 
                    i + 1, (Double) metrics.get("riskScore"), 
                    dashboard.getCurrentVolatility(),
                    dashboard.getTrades().size()));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Método utilitário para uso programático
     */
    public static RiskDashboard createDashboard() {
        return new RiskDashboard();
    }
    
    /**
     * Método utilitário para demonstração avançada
     */
    public static void runAdvancedDemo() {
        System.out.println("🔬 DEMONSTRAÇÃO AVANÇADA DO DASHBOARD DE RISCO");
        System.out.println("=".repeat(60));
        
        RiskDashboard dashboard = new RiskDashboard();
        
        try {
            // Testar configurações personalizadas
            System.out.println("\n⚙️ Testando Configurações Personalizadas:");
            testCustomSettings(dashboard);
            
            // Testar cenários de risco
            System.out.println("\n🌪 Testando Cenários de Risco:");
            testRiskScenarios(dashboard);
            
            // Testar análise de performance
            System.out.println("\n📊 Testando Análise de Performance:");
            testPerformanceAnalysis(dashboard);
            
        } catch (Exception e) {
            System.err.println("❌ Erro na demonstração avançada: " + e.getMessage());
        } finally {
            dashboard.shutdown();
        }
    }
    
    private static void testCustomSettings(RiskDashboard dashboard) {
        /** Testa configurações personalizadas */
        
        RiskSettings customSettings = new RiskSettings();
        customSettings.maxDrawdownLimit = 8.0;
        customSettings.maxPositionSize = 15.0;
        customSettings.stopLossDefault = 3.0;
        customSettings.volatilityThreshold = 20.0;
        customSettings.correlationLimit = 90.0;
        
        dashboard.updateSettings(customSettings);
        
        System.out.println("   ✅ Configurações personalizadas aplicadas");
        System.out.println("   📊 Novo limite de drawdown: " + String.format("%.1f%%", customSettings.maxDrawdownLimit));
        System.out.println("   📈 Novo tamanho máximo de posição: " + String.format("%.1f%%", customSettings.maxPositionSize));
    }
    
    private static void testRiskScenarios(RiskDashboard dashboard) {
        /** Testa diferentes cenários de risco */
        
        // Cenário 1: Alta volatilidade
        System.out.println("   📊 Cenário 1: Alta Volatilidade");
        for (int i = 0; i < 5; i++) {
            MarketDataPoint point = new MarketDataPoint(
                LocalDateTime.now(),
                45000 + Math.random() * 5000,
                1000 + Math.random() * 3000,
                3.0 + Math.random() * 2.0  // Alta volatilidade
            );
            dashboard.marketData.add(point);
        }
        
        dashboard.updateVolatility();
        Map<String, Object> metrics = dashboard.getMetrics();
        System.out.println("      Volatilidade: " + String.format("%.2f", dashboard.getCurrentVolatility()));
        System.out.println("      Risk Score: " + String.format("%.1f", (Double) metrics.get("riskScore")));
        
        // Cenário 2: Múltiplos trades com perdas
        System.out.println("   📉 Cenário 2: Trades com Perdas");
        for (int i = 0; i < 3; i++) {
            Trade lossTrade = new Trade(
                String.format("loss_%d", i),
                LocalDateTime.now(),
                "ETH/USD",
                1.0 + Math.random() * 2.0,
                3000 + Math.random() * 1000,
                "SELL",
                TradeStatus.FILLED,
                -300 - Math.random() * 200  // Perdas
            );
            dashboard.addTrade(lossTrade);
        }
        
        metrics = dashboard.getMetrics();
        System.out.println("      Drawdown Atual: " + String.format("%.2f%%", (Double) metrics.get("currentDrawdown")));
        System.out.println("      Lucro Total: " + String.format("%.2f", (Double) metrics.get("totalProfit")));
    }
    
    private static void testPerformanceAnalysis(RiskDashboard dashboard) {
        /** Testa análise de performance */
        
        List<Trade> trades = dashboard.getTrades();
        
        if (!trades.isEmpty()) {
            // Estatísticas básicas
            long filledTrades = trades.stream()
                .filter(t -> t.status == TradeStatus.FILLED)
                .count();
            
            double totalProfit = trades.stream()
                .filter(t -> t.profit != null)
                .mapToDouble(t -> t.profit)
                .sum();
            
            double avgProfit = filledTrades > 0 ? totalProfit / filledTrades : 0;
            
            long winningTrades = trades.stream()
                .filter(t -> t.profit != null && t.profit > 0)
                .count();
            
            double winRate = filledTrades > 0 ? (double) winningTrades / filledTrades * 100 : 0;
            
            System.out.println("   📊 Análise de Performance:");
            System.out.println("      Trades Executados: " + filledTrades);
            System.out.println("      Trades Vencedores: " + winningTrades);
            System.out.println("      Taxa de Acerto: " + String.format("%.1f%%", winRate));
            System.out.println("      Lucro Total: " + String.format("%.2f", totalProfit));
            System.out.println("      Média por Trade: " + String.format("%.2f", avgProfit));
            
            // Análise por símbolo
            System.out.println("   📈 Performance por Símbolo:");
            Map<String, List<Trade>> tradesBySymbol = new HashMap<>();
            for (Trade trade : trades) {
                tradesBySymbol.computeIfAbsent(trade.symbol, k -> new ArrayList<>()).add(trade);
            }
            
            for (Map.Entry<String, List<Trade>> entry : tradesBySymbol.entrySet()) {
                String symbol = entry.getKey();
                List<Trade> symbolTrades = entry.getValue();
                
                double symbolProfit = symbolTrades.stream()
                    .filter(t -> t.profit != null)
                    .mapToDouble(t -> t.profit)
                    .sum();
                
                System.out.println(String.format("      %s: %.2f (%d trades)", symbol, symbolProfit, symbolTrades.size()));
            }
        } else {
            System.out.println("   📊 Nenhum trade disponível para análise");
        }
    }
    
    /**
     * Método utilitário para verificação de saúde
     */
    public static Map<String, Object> healthCheck(RiskDashboard dashboard) {
        /** Retorna status de saúde do sistema */
        Map<String, Object> health = new HashMap<>();
        
        try {
            Map<String, Object> metrics = dashboard.getMetrics();
            double riskScore = (Double) metrics.get("riskScore");
            
            String status = "HEALTHY";
            if (riskScore > 80) {
                status = "CRITICAL";
            } else if (riskScore > 50) {
                status = "WARNING";
            }
            
            health.put("status", status);
            health.put("timestamp", System.currentTimeMillis());
            health.put("riskScore", riskScore);
            health.put("riskLabel", metrics.get("riskLabel"));
            health.put("currentVolatility", dashboard.getCurrentVolatility());
            health.put("totalTrades", dashboard.getTrades().size());
            health.put("marketDataPoints", dashboard.getMarketData().size());
            health.put("settings", dashboard.getSettings().toDict());
            
        } catch (Exception e) {
            health.put("status", "UNHEALTHY");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
        }
        
        return health;
    }
}
