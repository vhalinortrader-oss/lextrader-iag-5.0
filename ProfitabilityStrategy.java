package modulo_emocional.Decisao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 📈 ESTRATÉGIAS DE LUCRATIVIDADE
 * ================================
 * 
 * Sistema de gerenciamento de estratégias de trading com otimização
 * Adaptado do Streamlit para Java com console interface
 */

// Enums
enum StrategyType {
    MOMENTUM, MEAN_REVERSION, BREAKOUT, SCALPING, SWING
}

enum StrategyStatus {
    OPTIMIZING, ACTIVE, LEARNING, PAUSED
}

enum ImpactLevel {
    HIGH, MEDIUM, LOW
}

enum RiskLevel {
    LOW, MEDIUM, HIGH
}

enum SystemStatus {
    ACTIVE, WARNING, CRITICAL
}

// Classes de Dados
class Strategy {
    String id;
    String name;
    StrategyType type;
    double profitability;
    double winRate;
    double avgProfit;
    double maxDrawdown;
    double sharpeRatio;
    int trades;
    double adaptationLevel;
    boolean isActive;
    StrategyStatus status;
    LocalDateTime createdAt;
    LocalDateTime lastTrade;
    List<Double> profitHistory;
    
    public Strategy(String id, String name, StrategyType type, double profitability, 
                   double winRate, double avgProfit, double maxDrawdown, double sharpeRatio, 
                   int trades, double adaptationLevel, boolean isActive, StrategyStatus status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.profitability = profitability;
        this.winRate = winRate;
        this.avgProfit = avgProfit;
        this.maxDrawdown = maxDrawdown;
        this.sharpeRatio = sharpeRatio;
        this.trades = trades;
        this.adaptationLevel = adaptationLevel;
        this.isActive = isActive;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.lastTrade = LocalDateTime.now();
        this.profitHistory = new ArrayList<>();
    }
}

class OptimizationMetric {
    String parameter;
    double current;
    double optimal;
    double improvement;
    ImpactLevel impact;
    double minValue;
    double maxValue;
    double[] targetRange;
    
    public OptimizationMetric(String parameter, double current, double optimal, double improvement, 
                           ImpactLevel impact, double minValue, double maxValue, double[] targetRange) {
        this.parameter = parameter;
        this.current = current;
        this.optimal = optimal;
        this.improvement = improvement;
        this.impact = impact;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.targetRange = targetRange;
    }
}

class ProfitOpportunity {
    String asset;
    String strategy;
    double expectedProfit;
    double confidence;
    String timeframe;
    RiskLevel riskLevel;
    String reasoning;
    double entryPrice;
    double targetPrice;
    double stopLoss;
    boolean volumeConfirmation;
    List<String> technicalSignals;
    
    public ProfitOpportunity(String asset, String strategy, double expectedProfit, double confidence,
                          String timeframe, RiskLevel riskLevel, String reasoning, double entryPrice,
                          double targetPrice, double stopLoss, boolean volumeConfirmation,
                          List<String> technicalSignals) {
        this.asset = asset;
        this.strategy = strategy;
        this.expectedProfit = expectedProfit;
        this.confidence = confidence;
        this.timeframe = timeframe;
        this.riskLevel = riskLevel;
        this.reasoning = reasoning;
        this.entryPrice = entryPrice;
        this.targetPrice = targetPrice;
        this.stopLoss = stopLoss;
        this.volumeConfirmation = volumeConfirmation;
        this.technicalSignals = technicalSignals;
    }
}

class AnalyticsData {
    int totalTradesToday = 0;
    double avgProfitPerTrade = 0.0;
    String bestStrategy = "";
    int optimizationCycles = 0;
}

class PortfolioMetrics {
    double totalProfit;
    double avgSharpe;
    double maxDrawdown;
    int totalTrades;
    int activeCount;
    
    public PortfolioMetrics(double totalProfit, double avgSharpe, double maxDrawdown, 
                          int totalTrades, int activeCount) {
        this.totalProfit = totalProfit;
        this.avgSharpe = avgSharpe;
        this.maxDrawdown = maxDrawdown;
        this.totalTrades = totalTrades;
        this.activeCount = activeCount;
    }
}

// Serviço Principal
class ProfitabilityStrategyService {
    List<Strategy> strategies;
    List<OptimizationMetric> optimizationMetrics;
    List<ProfitOpportunity> profitOpportunities;
    AnalyticsData analyticsData;
    double overallProfitability = 25.4;
    SystemStatus systemStatus = SystemStatus.ACTIVE;
    boolean optimizationLock = false;
    private Random random = new Random();
    
    public ProfitabilityStrategyService() {
        this.strategies = initStrategies();
        this.optimizationMetrics = initOptimizationMetrics();
        this.profitOpportunities = initProfitOpportunities();
        this.analyticsData = new AnalyticsData();
    }
    
    private List<Strategy> initStrategies() {
        return Arrays.asList(
            new Strategy("momentum_pro", "Momentum Pro Max", StrategyType.MOMENTUM, 24.7, 73.2, 1.89, -8.4, 2.34, 1247, 0.87, true, StrategyStatus.ACTIVE),
            new Strategy("scalping_ai", "AI Scalping Ultra", StrategyType.SCALPING, 31.8, 68.9, 0.42, -4.2, 3.21, 5672, 0.92, true, StrategyStatus.OPTIMIZING),
            new Strategy("swing_master", "Swing Master AI", StrategyType.SWING, 19.3, 78.1, 3.67, -12.1, 1.87, 432, 0.79, true, StrategyStatus.LEARNING),
            new Strategy("breakout_hunter", "Breakout Hunter Pro", StrategyType.BREAKOUT, 28.9, 65.4, 2.81, -15.7, 2.12, 867, 0.84, false, StrategyStatus.PAUSED),
            new Strategy("mean_reversion", "Mean Reversion AI", StrategyType.MEAN_REVERSION, 22.1, 81.3, 1.23, -6.8, 2.78, 1893, 0.91, true, StrategyStatus.ACTIVE)
        );
    }
    
    private List<OptimizationMetric> initOptimizationMetrics() {
        return Arrays.asList(
            new OptimizationMetric("Position Size", 2.5, 3.2, 28.0, ImpactLevel.HIGH, 1.0, 5.0, new double[]{2.0, 4.0}),
            new OptimizationMetric("Take Profit %", 1.8, 2.4, 33.3, ImpactLevel.HIGH, 0.5, 5.0, new double[]{1.5, 3.0}),
            new OptimizationMetric("Stop Loss %", 1.2, 0.9, 25.0, ImpactLevel.MEDIUM, 0.3, 3.0, new double[]{0.8, 1.5}),
            new OptimizationMetric("Entry Timing", 0.7, 0.9, 28.6, ImpactLevel.HIGH, 0.1, 1.0, new double[]{0.6, 0.95}),
            new OptimizationMetric("Risk/Reward Ratio", 1.5, 2.7, 80.0, ImpactLevel.HIGH, 1.0, 5.0, new double[]{2.0, 4.0}),
            new OptimizationMetric("Market Filter", 0.6, 0.8, 33.3, ImpactLevel.MEDIUM, 0.1, 1.0, new double[]{0.7, 0.9})
        );
    }
    
    private List<ProfitOpportunity> initProfitOpportunities() {
        List<ProfitOpportunity> opportunities = new ArrayList<>();
        
        // PETR4
        opportunities.add(new ProfitOpportunity(
            "PETR4", "Momentum Pro Max", 3.4, 87.2, "2H", RiskLevel.MEDIUM,
            "Forte momentum ascendente com volume confirmando breakout",
            28.50, 29.47, 27.93, true,
            Arrays.asList("RSI Bullish", "Volume Spike", "Breakout Confirmed")
        ));
        
        // VALE3
        opportunities.add(new ProfitOpportunity(
            "VALE3", "AI Scalping Ultra", 1.8, 92.1, "15M", RiskLevel.LOW,
            "Padrão de reversão identificado com alta precisão",
            65.20, 66.37, 63.90, true,
            Arrays.asList("Mean Reversion", "Support Bounce", "Low Volatility")
        ));
        
        // ITUB4
        opportunities.add(new ProfitOpportunity(
            "ITUB4", "Swing Master AI", 5.2, 79.8, "1D", RiskLevel.LOW,
            "Suporte forte + divergência bullish no RSI",
            31.80, 33.45, 31.16, false,
            Arrays.asList("RSI Divergence", "Support Level", "Swing Setup")
        ));
        
        // BBAS3
        opportunities.add(new ProfitOpportunity(
            "BBAS3", "Mean Reversion AI", 2.1, 85.7, "4H", RiskLevel.LOW,
            "Oversold em múltiplos timeframes com catalysts positivos",
            42.10, 42.98, 41.26, true,
            Arrays.asList("Oversold RSI", "MACD Bullish", "Catalyst News")
        ));
        
        // MGLU3
        opportunities.add(new ProfitOpportunity(
            "MGLU3", "Breakout Hunter Pro", 4.7, 73.4, "1H", RiskLevel.HIGH,
            "Triângulo ascendente próximo ao breakout com volume",
            3.85, 4.03, 3.77, true,
            Arrays.asList("Triangle Pattern", "Volume Increase", "Resistance Test")
        ));
        
        return opportunities;
    }
    
    public PortfolioMetrics calculatePortfolioMetrics() {
        List<Strategy> activeStrategies = new ArrayList<>();
        for (Strategy s : strategies) {
            if (s.isActive) activeStrategies.add(s);
        }
        
        if (activeStrategies.isEmpty()) {
            return new PortfolioMetrics(0, 0, 0, 0, 0);
        }
        
        double totalProfit = 0;
        double avgSharpe = 0;
        double maxDrawdown = 0;
        int totalTrades = 0;
        
        for (Strategy s : activeStrategies) {
            totalProfit += s.profitability;
            avgSharpe += s.sharpeRatio;
            maxDrawdown = Math.min(maxDrawdown, s.maxDrawdown);
            totalTrades += s.trades;
        }
        
        avgSharpe /= activeStrategies.size();
        
        return new PortfolioMetrics(totalProfit, avgSharpe, maxDrawdown, totalTrades, activeStrategies.size());
    }
    
    public ProfitOpportunity getBestOpportunity() {
        if (profitOpportunities.isEmpty()) return null;
        
        ProfitOpportunity best = null;
        double bestScore = 0;
        
        for (ProfitOpportunity opp : profitOpportunities) {
            double riskMultiplier = 1.0;
            switch (opp.riskLevel) {
                case LOW: riskMultiplier = 1.0; break;
                case MEDIUM: riskMultiplier = 0.8; break;
                case HIGH: riskMultiplier = 0.6; break;
            }
            
            double score = (opp.expectedProfit * opp.confidence * riskMultiplier) / 100;
            if (score > bestScore) {
                bestScore = score;
                best = opp;
            }
        }
        
        return best;
    }
    
    public void performAdvancedOptimization() {
        if (optimizationLock) return;
        
        optimizationLock = true;
        try {
            List<OptimizationMetric> newMetrics = new ArrayList<>();
            
            for (OptimizationMetric metric : optimizationMetrics) {
                if (metric.impact == ImpactLevel.HIGH) {
                    double adjustment = (random.nextDouble() - 0.5) * 0.2;
                    double newCurrent = Math.max(metric.minValue, 
                        Math.min(metric.maxValue, metric.current + adjustment));
                    
                    double distanceToOptimal = Math.abs(metric.optimal - newCurrent);
                    double maxDistance = Math.max(Math.abs(metric.optimal - metric.minValue), 
                                               Math.abs(metric.optimal - metric.maxValue));
                    
                    double improvement = maxDistance > 0 ? (1 - distanceToOptimal / maxDistance) * 100 : 0;
                    
                    newMetrics.add(new OptimizationMetric(
                        metric.parameter, newCurrent, metric.optimal, improvement,
                        metric.impact, metric.minValue, metric.maxValue, metric.targetRange
                    ));
                } else {
                    newMetrics.add(metric);
                }
            }
            
            optimizationMetrics = newMetrics;
            analyticsData.optimizationCycles++;
            
        } catch (Exception e) {
            System.err.println("Erro durante otimização: " + e.getMessage());
        } finally {
            optimizationLock = false;
        }
    }
    
    public void updateStrategies() {
        for (Strategy strategy : strategies) {
            if (!strategy.isActive) continue;
            
            double volatility = 0.1;
            switch (strategy.type) {
                case SCALPING: volatility = 0.1; break;
                case SWING: volatility = 0.3; break;
                case MOMENTUM: volatility = 0.2; break;
                case BREAKOUT: volatility = 0.4; break;
                case MEAN_REVERSION: volatility = 0.15; break;
            }
            
            double change = (random.nextDouble() - 0.5) * 2 * volatility;
            double newProfitability = Math.max(10, Math.min(45, strategy.profitability + change));
            
            strategy.profitHistory.add(newProfitability);
            if (strategy.profitHistory.size() > 100) {
                strategy.profitHistory.remove(0);
            }
            
            if (random.nextDouble() < 0.3) {
                strategy.trades++;
                strategy.lastTrade = LocalDateTime.now();
            }
            
            strategy.profitability = newProfitability;
        }
    }
    
    public void updateOpportunities() {
        for (ProfitOpportunity opportunity : profitOpportunities) {
            double volatility = random.nextDouble() * 0.04 + 0.01;
            double priceChange = (random.nextDouble() - 0.5) * 2 * volatility;
            
            if (opportunity.entryPrice > 0) {
                double newEntryPrice = opportunity.entryPrice * (1 + priceChange);
                opportunity.entryPrice = newEntryPrice;
                opportunity.targetPrice = newEntryPrice * (1 + opportunity.expectedProfit / 100);
                opportunity.stopLoss = newEntryPrice * 0.98;
            }
        }
    }
    
    public void updateAnalytics() {
        List<Strategy> activeStrategies = new ArrayList<>();
        for (Strategy s : strategies) {
            if (s.isActive) activeStrategies.add(s);
        }
        
        if (!activeStrategies.isEmpty()) {
            Strategy best = activeStrategies.get(0);
            for (Strategy s : activeStrategies) {
                if (s.profitability > best.profitability) best = s;
            }
            
            analyticsData.bestStrategy = best.name;
            
            double totalAvgProfit = 0;
            int totalTrades = 0;
            for (Strategy s : activeStrategies) {
                totalAvgProfit += s.avgProfit;
                totalTrades += s.trades;
            }
            
            analyticsData.avgProfitPerTrade = totalAvgProfit / activeStrategies.size();
            analyticsData.totalTradesToday = totalTrades;
        } else {
            analyticsData.bestStrategy = "";
            analyticsData.avgProfitPerTrade = 0.0;
            analyticsData.totalTradesToday = 0;
        }
    }
}

// Interface Console
public class ProfitabilityStrategy {
    private static ProfitabilityStrategyService service = new ProfitabilityStrategyService();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static boolean running = true;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║           📈 ESTRATÉGIAS DE LUCRATIVIDADE JAVA              ║");
        System.out.println("║                   Sistema de Trading                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        
        // Iniciar atualização automática
        startAutoUpdate();
        
        // Loop principal
        while (running) {
            showMainMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    showStrategies();
                    break;
                case "2":
                    showOptimization();
                    break;
                case "3":
                    showOpportunities();
                    break;
                case "4":
                    showAnalytics();
                    break;
                case "5":
                    performOptimization();
                    break;
                case "6":
                    updateData();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        
        scheduler.shutdown();
        System.out.println("\n👋 Sistema encerrado. Até logo!");
    }
    
    private static void startAutoUpdate() {
        scheduler.scheduleAtFixedRate(() -> {
            if (running) {
                service.updateStrategies();
                service.updateOpportunities();
                service.updateAnalytics();
                
                if (Math.random() < 0.3) {
                    service.performAdvancedOptimization();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }
    
    private static void showMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 MENU PRINCIPAL");
        System.out.println("=".repeat(60));
        System.out.printf("Lucro Total: +%.1f%% • Status: %s%n", 
                         service.overallProfitability, service.systemStatus);
        System.out.println("=".repeat(60));
        System.out.println("1. 🧠 Estratégias");
        System.out.println("2. ⚙️  Otimização");
        System.out.println("3. 🎯 Oportunidades");
        System.out.println("4. 📈 Analytics");
        System.out.println("5. ⚡ Otimizar Agora");
        System.out.println("6. 🔄 Atualizar Dados");
        System.out.println("0. 🚪 Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    private static void showStrategies() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🧠 ESTRATÉGIAS ATIVAS");
        System.out.println("=".repeat(60));
        
        PortfolioMetrics metrics = service.calculatePortfolioMetrics();
        System.out.printf("Portfolio: Lucro %.1f%% | Sharpe %.2f | Trades %d%n",
                         metrics.totalProfit, metrics.avgSharpe, metrics.totalTrades);
        System.out.println("-".repeat(60));
        
        for (Strategy strategy : service.strategies) {
            String status = strategy.isActive ? "✅" : "⏸️";
            System.out.printf("%s %s%n", status, strategy.name);
            System.out.printf("   Tipo: %s | Status: %s%n", strategy.type, strategy.status);
            System.out.printf("   Lucro: +%.1f%% | Win Rate: %.1f%% | Trades: %d%n",
                             strategy.profitability, strategy.winRate, strategy.trades);
            System.out.printf("   Sharpe: %.2f | Adaptação: %.0f%%%n",
                             strategy.sharpeRatio, strategy.adaptationLevel * 100);
            System.out.println();
        }
    }
    
    private static void showOptimization() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⚙️ MÉTRICAS DE OTIMIZAÇÃO");
        System.out.println("=".repeat(60));
        
        for (OptimizationMetric metric : service.optimizationMetrics) {
            System.out.printf("%s (%s)%n", metric.parameter, metric.impact);
            System.out.printf("   Atual: %.2f | Ótimo: %.2f | Melhoria: %+.1f%%%n",
                             metric.current, metric.optimal, metric.improvement);
            System.out.printf("   Range: %.1f - %.1f%n", metric.minValue, metric.maxValue);
            System.out.println();
        }
        
        System.out.printf("Ciclos de Otimização: %d%n", service.analyticsData.optimizationCycles);
    }
    
    private static void showOpportunities() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 OPORTUNIDADES DE MERCADO");
        System.out.println("=".repeat(60));
        
        ProfitOpportunity best = service.getBestOpportunity();
        if (best != null) {
            System.out.println("🌟 MELHOR OPORTUNIDADE:");
            System.out.printf("   %s • %s%n", best.asset, best.strategy);
            System.out.printf("   Retorno: +%.1f%% | Confiança: %.1f%% | Risco: %s%n",
                             best.expectedProfit, best.confidence, best.riskLevel);
            System.out.printf("   Entrada: R$ %.2f | Alvo: R$ %.2f | Stop: R$ %.2f%n",
                             best.entryPrice, best.targetPrice, best.stopLoss);
            System.out.printf("   %s%n", best.reasoning);
            System.out.println();
        }
        
        System.out.println("TODAS AS OPORTUNIDADES:");
        for (ProfitOpportunity opp : service.profitOpportunities) {
            System.out.printf("• %s: +%.1f%% (%s) - %s%n",
                             opp.asset, opp.expectedProfit, opp.timeframe, opp.strategy);
        }
    }
    
    private static void showAnalytics() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📈 ANÁLITICS E MÉTRICAS");
        System.out.println("=".repeat(60));
        
        PortfolioMetrics metrics = service.calculatePortfolioMetrics();
        
        System.out.println("📊 MÉTRICAS DO PORTFOLIO:");
        System.out.printf("   Lucro Total: +%.1f%%%n", metrics.totalProfit);
        System.out.printf("   Sharpe Médio: %.2f%n", metrics.avgSharpe);
        System.out.printf("   Max Drawdown: %.1f%%%n", metrics.maxDrawdown);
        System.out.printf("   Trades Ativos: %d%n", metrics.activeCount);
        System.out.println();
        
        System.out.println("⚙️ MÉTRICAS AVANÇADAS:");
        System.out.printf("   Ciclos Otimização: %d%n", service.analyticsData.optimizationCycles);
        System.out.printf("   Lucro Médio: R$ %.2f%n", service.analyticsData.avgProfitPerTrade);
        System.out.printf("   Trades Hoje: %d%n", service.analyticsData.totalTradesToday);
        System.out.printf("   Melhor Estratégia: %s%n", service.analyticsData.bestStrategy);
    }
    
    private static void performOptimization() {
        System.out.println("\n⚡ Executando otimização avançada...");
        service.performAdvancedOptimization();
        System.out.println("✅ Otimização concluída!");
    }
    
    private static void updateData() {
        System.out.println("\n🔄 Atualizando dados...");
        service.updateStrategies();
        service.updateOpportunities();
        service.updateAnalytics();
        System.out.println("✅ Dados atualizados!");
    }
}
