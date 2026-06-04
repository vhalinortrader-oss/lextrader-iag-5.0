package modulo_emocional.Decisao;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Risk Calculator - Calcula métricas de risco
 * =====================================
 * Versão Java do risk_calculator.py - Sistema para cálculo de métricas de risco de portfolio
 * com validação de limites, cálculo de tamanho de posição e análise de risco completa
 */

// Classe de Métricas de Risco
class RiskMetrics {
    public double portfolioValue;
    public double dailyLossLimit;
    public double positionRiskPercent;
    public double leverage;
    public double var95;  // Value at Risk 95%
    
    public RiskMetrics(double portfolioValue, double dailyLossLimit, double positionRiskPercent, 
                      double leverage, double var95) {
        this.portfolioValue = portfolioValue;
        this.dailyLossLimit = dailyLossLimit;
        this.positionRiskPercent = positionRiskPercent;
        this.leverage = leverage;
        this.var95 = var95;
    }
    
    @Override
    public String toString() {
        return String.format(
            "RiskMetrics{portfolio=%.2f, dailyLossLimit=%.2f, positionRisk=%.1f%%, " +
            "leverage=%.1f, var95=%.2f}", 
            portfolioValue, dailyLossLimit, positionRiskPercent, leverage, var95
        );
    }
    
    public Map<String, Object> toDict() {
        Map<String, Object> dict = new HashMap<>();
        dict.put("portfolioValue", portfolioValue);
        dict.put("dailyLossLimit", dailyLossLimit);
        dict.put("positionRiskPercent", positionRiskPercent);
        dict.put("leverage", leverage);
        dict.put("var95", var95);
        return dict;
    }
}

// Classe Principal
class RiskCalculator {
    private static final Logger logger = Logger.getLogger(RiskCalculator.class.getName());
    
    // Constantes
    public static final double DAILY_LOSS_LIMIT = 0.05;  // 5% máximo de perda diária
    public static final double MAX_LEVERAGE = 2.0;
    public static final double MAX_POSITION_SIZE = 0.10;  // 10% do portfolio por posição
    
    private double portfolioValue;
    
    public RiskCalculator(double portfolioValue) {
        this.portfolioValue = portfolioValue;
        logger.info("RiskCalculator inicializado com portfolio de " + portfolioValue);
    }
    
    /**
     * Calcula tamanho máximo de posição
     * @param riskPercent Percentual de risco desejado (ex: 2.0 para 2%)
     * @return Tamanho máximo da posição em valor monetário
     */
    public double calculatePositionSize(double riskPercent) {
        double maxSize = portfolioValue * MAX_POSITION_SIZE;
        double riskSize = portfolioValue * (riskPercent / 100);
        double positionSize = Math.min(maxSize, riskSize);
        
        logger.info(String.format("Posição calculada: %.2f (máximo: %.2f, risco: %.2f%%)", 
            positionSize, maxSize, riskPercent));
        
        return positionSize;
    }
    
    /**
     * Verifica se ultrapassou limite de perda diária
     * @param currentLoss Perda atual em valor monetário
     * @return true se dentro do limite, false se ultrapassou
     */
    public boolean checkDailyLossLimit(double currentLoss) {
        double maxLoss = portfolioValue * DAILY_LOSS_LIMIT;
        boolean withinLimit = currentLoss <= maxLoss;
        
        if (!withinLimit) {
            logger.warning(String.format("Limite de perda diária ultrapassado: %.2f > %.2f", 
                currentLoss, maxLoss));
        }
        
        return withinLimit;
    }
    
    /**
     * Calcula o percentual de risco atual
     * @param currentLoss Perda atual em valor monetário
     * @return Percentual de risco (0-100%)
     */
    public double calculateRiskPercentage(double currentLoss) {
        return (currentLoss / portfolioValue) * 100;
    }
    
    /**
     * Verifica se o tamanho da posição é seguro
     * @param positionSize Tamanho da posição em valor monetário
     * @return true se seguro, false se muito grande
     */
    public boolean isPositionSizeSafe(double positionSize) {
        double maxAllowed = portfolioValue * MAX_POSITION_SIZE;
        boolean isSafe = positionSize <= maxAllowed;
        
        if (!isSafe) {
            logger.warning(String.format("Tamanho de posição inseguro: %.2f > %.2f", 
                positionSize, maxAllowed));
        }
        
        return isSafe;
    }
    
    /**
     * Calcula o alavancagem atual
     * @param totalPositionSize Tamanho total das posições
     * @return Alavancagem atual
     */
    public double calculateCurrentLeverage(double totalPositionSize) {
        return totalPositionSize / portfolioValue;
    }
    
    /**
     * Verifica se a alavancagem é segura
     * @param currentLeverage Alavancagem atual
     * @return true se segura, false se excessiva
     */
    public boolean isLeverageSafe(double currentLeverage) {
        boolean isSafe = currentLeverage <= MAX_LEVERAGE;
        
        if (!isSafe) {
            logger.warning(String.format("Alavancagem excessiva: %.1f > %.1f", 
                currentLeverage, MAX_LEVERAGE));
        }
        
        return isSafe;
    }
    
    /**
     * Calcula o Value at Risk (VaR) 95%
     * @param volatility Volatilidade anualizada (ex: 0.2 para 20%)
     * @param timeHorizon Horizonte de tempo em dias (ex: 1 para 1 dia)
     * @return VaR 95% em valor monetário
     */
    public double calculateVaR95(double volatility, int timeHorizon) {
        // Simplificação: VaR 95% ≈ 1.645 * σ * √t * portfolio_value
        double timeFactor = Math.sqrt(timeHorizon) / Math.sqrt(252); // 252 dias úteis no ano
        double var95 = 1.645 * volatility * timeFactor * portfolioValue;
        
        logger.info(String.format("VaR 95% calculado: %.2f (volatilidade: %.2f, horizonte: %d dias)", 
            var95, volatility, timeHorizon));
        
        return var95;
    }
    
    /**
     * Retorna métricas de risco do portfolio
     * @return Objeto RiskMetrics com todas as métricas
     */
    public RiskMetrics getRiskMetrics() {
        return new RiskMetrics(
            portfolioValue,
            portfolioValue * DAILY_LOSS_LIMIT,
            MAX_POSITION_SIZE * 100,
            MAX_LEVERAGE,
            portfolioValue * 0.05  // Simplificação: 5% do portfolio como VaR 95%
        );
    }
    
    /**
     * Realiza análise completa de risco
     * @param currentLoss Perda atual
     * @param totalPositionSize Tamanho total das posições
     * @param volatility Volatilidade anualizada
     * @return Map com resultados da análise
     */
    public Map<String, Object> performRiskAnalysis(double currentLoss, double totalPositionSize, double volatility) {
        Map<String, Object> analysis = new HashMap<>();
        
        // Verificar limite de perda diária
        boolean dailyLossOk = checkDailyLossLimit(currentLoss);
        double riskPercentage = calculateRiskPercentage(currentLoss);
        
        // Verificar tamanho das posições
        boolean positionSizeOk = isPositionSizeSafe(totalPositionSize);
        
        // Verificar alavancagem
        double currentLeverage = calculateCurrentLeverage(totalPositionSize);
        boolean leverageOk = isLeverageSafe(currentLeverage);
        
        // Calcular VaR
        double var95 = calculateVaR95(volatility, 1);
        
        // Status geral
        String overallStatus = "SAFE";
        if (!dailyLossOk || !positionSizeOk || !leverageOk) {
            overallStatus = "WARNING";
        }
        if (riskPercentage > DAILY_LOSS_LIMIT * 100) {
            overallStatus = "CRITICAL";
        }
        
        analysis.put("overallStatus", overallStatus);
        analysis.put("dailyLossOk", dailyLossOk);
        analysis.put("positionSizeOk", positionSizeOk);
        analysis.put("leverageOk", leverageOk);
        analysis.put("currentLeverage", currentLeverage);
        analysis.put("riskPercentage", riskPercentage);
        analysis.put("var95", var95);
        analysis.put("metrics", getRiskMetrics());
        
        logger.info(String.format("Análise de risco concluída - Status: %s", overallStatus));
        
        return analysis;
    }
    
    /**
     * Gera relatório detalhado de risco
     * @param currentLoss Perda atual
     * @param totalPositionSize Tamanho total das posições
     * @param volatility Volatilidade anualizada
     * @return String com relatório formatado
     */
    public String generateRiskReport(double currentLoss, double totalPositionSize, double volatility) {
        Map<String, Object> analysis = performRiskAnalysis(currentLoss, totalPositionSize, volatility);
        
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(60)).append("\n");
        report.append("📊 RELATÓRIO DE ANÁLISE DE RISCO").append("\n");
        report.append("=".repeat(60)).append("\n\n");
        
        // Status geral
        String status = (String) analysis.get("overallStatus");
        String statusIcon = status.equals("SAFE") ? "✅" : status.equals("WARNING") ? "⚠️" : "🚨";
        report.append(String.format("Status Geral: %s %s\n\n", statusIcon, status));
        
        // Métricas principais
        report.append("📈 MÉTRICAS PRINCIPAIS:\n");
        report.append(String.format("   Valor do Portfolio: R$ %,.2f\n", portfolioValue));
        report.append(String.format("   Perda Atual: R$ %,.2f (%.2f%%)\n", currentLoss, (Double) analysis.get("riskPercentage")));
        report.append(String.format("   Tamanho Total das Posições: R$ %,.2f\n", totalPositionSize));
        report.append(String.format("   Alavancagem Atual: %.1fx\n", (Double) analysis.get("currentLeverage")));
        report.append(String.format("   VaR 95% (1 dia): R$ %,.2f\n", (Double) analysis.get("var95")));
        report.append("\n");
        
        // Verificações
        report.append("🔍 VERIFICAÇÕES DE SEGURANÇA:\n");
        report.append(String.format("   Limite de Perda Diária: %s (%.2f%%)\n", 
            (Boolean) analysis.get("dailyLossOk") ? "✅ OK" : "❌ ULTRAPASSADO", DAILY_LOSS_LIMIT * 100));
        report.append(String.format("   Tamanho de Posição: %s (%.1f%%)\n", 
            (Boolean) analysis.get("positionSizeOk") ? "✅ OK" : "❌ EXCESSIVO", MAX_POSITION_SIZE * 100));
        report.append(String.format("   Alavancagem Máxima: %s (%.1fx)\n", 
            (Boolean) analysis.get("leverageOk") ? "✅ OK" : "❌ EXCESSIVA", MAX_LEVERAGE));
        report.append("\n");
        
        // Limites
        report.append("⚙️ LIMITES CONFIGURADOS:\n");
        report.append(String.format("   Limite de Perda Diária: %.1f%%\n", DAILY_LOSS_LIMIT * 100));
        report.append(String.format("   Tamanho Máximo de Posição: %.1f%%\n", MAX_POSITION_SIZE * 100));
        report.append(String.format("   Alavancagem Máxima: %.1fx\n", MAX_LEVERAGE));
        report.append("\n");
        
        // Recomendações
        report.append("💡 RECOMENDAÇÕES:\n");
        if (!(Boolean) analysis.get("dailyLossOk")) {
            report.append("   ⚠️ Reduza a exposição para controlar a perda diária\n");
        }
        if (!(Boolean) analysis.get("positionSizeOk")) {
            report.append("   ⚠️ Reduza o tamanho das posições para o limite máximo\n");
        }
        if (!(Boolean) analysis.get("leverageOk")) {
            report.append("   ⚠️ Reduza a alavancagem para o nível seguro\n");
        }
        if ((Boolean) analysis.get("dailyLossOk") && (Boolean) analysis.get("positionSizeOk") && (Boolean) analysis.get("leverageOk")) {
            report.append("   ✅ Todos os parâmetros de risco estão dentro dos limites seguros\n");
        }
        
        report.append("\n");
        report.append("=".repeat(60));
        
        return report.toString();
    }
    
    // Getters
    public double getPortfolioValue() {
        return portfolioValue;
    }
    
    public void setPortfolioValue(double portfolioValue) {
        this.portfolioValue = portfolioValue;
        logger.info("Valor do portfolio atualizado para: " + portfolioValue);
    }
    
    // Métodos estáticos para conveniência
    public static double calculatePositionSizeStatic(double portfolioValue, double riskPercent) {
        RiskCalculator calc = new RiskCalculator(portfolioValue);
        return calc.calculatePositionSize(riskPercent);
    }
    
    public static boolean checkDailyLossLimitStatic(double portfolioValue, double currentLoss) {
        RiskCalculator calc = new RiskCalculator(portfolioValue);
        return calc.checkDailyLossLimit(currentLoss);
    }
    
    public static RiskMetrics getRiskMetricsStatic(double portfolioValue) {
        RiskCalculator calc = new RiskCalculator(portfolioValue);
        return calc.getRiskMetrics();
    }
}

// Classe Principal para Demonstração
public class RiskCalculatorApp {
    
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Risk Calculator...");
        
        try {
            // Demonstrar funcionalidades
            demonstrateRiskCalculator();
            
        } catch (Exception e) {
            System.err.println("❌ Erro na demonstração: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void demonstrateRiskCalculator() {
        /** Demonstra funcionalidades do RiskCalculator */
        
        System.out.println("📊 DEMONSTRAÇÃO DO RISK CALCULATOR");
        System.out.println("=".repeat(60));
        
        // 1. Inicialização básica
        System.out.println("\n1. INICIALIZAÇÃO BÁSICA:");
        RiskCalculator calculator = new RiskCalculator(10000.0);
        System.out.println("   ✅ RiskCalculator inicializado com portfolio de R$ 10,000.00");
        
        // 2. Cálculo de tamanho de posição
        System.out.println("\n2. CÁLCULO DE TAMANHO DE POSIÇÃO:");
        double[] riskPercents = {1.0, 2.0, 5.0, 10.0};
        for (double riskPercent : riskPercents) {
            double positionSize = calculator.calculatePositionSize(riskPercent);
            System.out.println(String.format("   📊 Risco %.1f%%: Posição máxima de R$ %,.2f", riskPercent, positionSize));
        }
        
        // 3. Verificação de limites
        System.out.println("\n3. VERIFICAÇÃO DE LIMITES:");
        double[] losses = {100, 300, 600, 1000};
        for (double loss : losses) {
            boolean withinLimit = calculator.checkDailyLossLimit(loss);
            String status = withinLimit ? "✅ DENTRO DO LIMITE" : "❌ ULTRAPASSADO";
            System.out.println(String.format("   📉 Perda de R$ %,.2f: %s", loss, status));
        }
        
        // 4. Análise completa de risco
        System.out.println("\n4. ANÁLISE COMPLETA DE RISCO:");
        double currentLoss = 250.0;
        double totalPositionSize = 8000.0;
        double volatility = 0.2; // 20%
        
        Map<String, Object> analysis = calculator.performRiskAnalysis(currentLoss, totalPositionSize, volatility);
        System.out.println("   📊 Status: " + analysis.get("overallStatus"));
        System.out.println("   📈 Alavancagem: " + String.format("%.1fx", (Double) analysis.get("currentLeverage")));
        System.out.println("   📉 Percentual de Risco: " + String.format("%.2f%%", (Double) analysis.get("riskPercentage")));
        System.out.println("   🛡️ VaR 95%: R$ " + String.format("%,.2f", (Double) analysis.get("var95")));
        
        // 5. Relatório detalhado
        System.out.println("\n5. RELATÓRIO DETALHADO:");
        String report = calculator.generateRiskReport(currentLoss, totalPositionSize, volatility);
        System.out.println(report);
        
        // 6. Métricas do portfolio
        System.out.println("\n6. MÉTRICAS DO PORTFOLIO:");
        RiskMetrics metrics = calculator.getRiskMetrics();
        System.out.println("   " + metrics.toString());
        
        System.out.println("\n✅ Demonstração concluída!");
    }
    
    /**
     * Método utilitário para uso programático
     */
    public static RiskCalculator createCalculator(double portfolioValue) {
        return new RiskCalculator(portfolioValue);
    }
    
    /**
     * Método utilitário para demonstração avançada
     */
    public static void runAdvancedDemo() {
        System.out.println("🔬 DEMONSTRAÇÃO AVANÇADA DO RISK CALCULATOR");
        System.out.println("=".repeat(60));
        
        try {
            // Testar diferentes cenários de portfolio
            System.out.println("\n📊 TESTANDO DIFERENTES CENÁRIOS:");
            testDifferentPortfolios();
            
            // Testar cenários de risco extremo
            System.out.println("\n🌪 TESTANDO CENÁRIOS DE RISCO EXTREMO:");
            testExtremeRiskScenarios();
            
            // Testar cálculos de VaR
            System.out.println("\n📈 TESTANDO CÁLCULOS DE VaR:");
            testVaRCalculations();
            
        } catch (Exception e) {
            System.err.println("❌ Erro na demonstração avançada: " + e.getMessage());
        }
    }
    
    private static void testDifferentPortfolios() {
        /** Testa diferentes tamanhos de portfolio */
        
        double[] portfolioSizes = {5000, 10000, 50000, 100000, 500000};
        
        for (double portfolioSize : portfolioSizes) {
            RiskCalculator calc = new RiskCalculator(portfolioSize);
            RiskMetrics metrics = calc.getRiskMetrics();
            
            System.out.println(String.format("   💰 Portfolio R$ %,.0f:", portfolioSize));
            System.out.println(String.format("      Limite de Perda Diária: R$ %,.2f", metrics.dailyLossLimit));
            System.out.println(String.format("      Tamanho Máximo de Posição: R$ %,.2f", 
                portfolioSize * RiskCalculator.MAX_POSITION_SIZE));
            System.out.println(String.format("      VaR 95%: R$ %,.2f", metrics.var95));
            System.out.println();
        }
    }
    
    private static void testExtremeRiskScenarios() {
        /** Testa cenários de risco extremo */
        
        RiskCalculator calc = new RiskCalculator(10000.0);
        
        // Cenário 1: Perda extrema
        System.out.println("   📉 Cenário 1: Perda Extrema");
        double extremeLoss = 800.0; // 8%
        boolean withinLimit = calc.checkDailyLossLimit(extremeLoss);
        System.out.println(String.format("      Perda: R$ %,.2f (%.1f%%) - %s", 
            extremeLoss, calc.calculateRiskPercentage(extremeLoss), 
            withinLimit ? "DENTRO DO LIMITE" : "ULTRAPASSADO"));
        
        // Cenário 2: Posição excessiva
        System.out.println("   📈 Cenário 2: Posição Excessiva");
        double excessivePosition = 15000.0; // 150% do portfolio
        boolean positionSafe = calc.isPositionSizeSafe(excessivePosition);
        System.out.println(String.format("      Posição: R$ %,.2f (%.1f%%) - %s", 
            excessivePosition, (excessivePosition / 10000.0) * 100,
            positionSafe ? "SEGURA" : "EXCESSIVA"));
        
        // Cenário 3: Alavancagem excessiva
        System.out.println("   ⚖️ Cenário 3: Alavancagem Excessiva");
        double excessiveLeverage = 3.5;
        boolean leverageSafe = calc.isLeverageSafe(excessiveLeverage);
        System.out.println(String.format("      Alavancagem: %.1fx - %s", 
            excessiveLeverage, leverageSafe ? "SEGURA" : "EXCESSIVA"));
        
        // Análise combinada
        System.out.println("   🔍 Análise Combinada:");
        Map<String, Object> analysis = calc.performRiskAnalysis(extremeLoss, excessivePosition, 0.3);
        System.out.println("      Status: " + analysis.get("overallStatus"));
    }
    
    private static void testVaRCalculations() {
        /** Testa diferentes cálculos de VaR */
        
        RiskCalculator calc = new RiskCalculator(10000.0);
        
        double[] volatilities = {0.1, 0.15, 0.2, 0.3, 0.4}; // 10%, 15%, 20%, 30%, 40%
        int[] timeHorizons = {1, 5, 10, 30}; // 1, 5, 10, 30 dias
        
        System.out.println("   📊 Cálculos de VaR 95%:");
        for (double volatility : volatilities) {
            System.out.println(String.format("      Volatilidade %.1f:", volatility));
            for (int timeHorizon : timeHorizons) {
                double var95 = calc.calculateVaR95(volatility, timeHorizon);
                System.out.println(String.format("         %d dias: R$ %,.2f", timeHorizon, var95));
            }
            System.out.println();
        }
    }
    
    /**
     * Método utilitário para verificação de saúde
     */
    public static Map<String, Object> healthCheck(RiskCalculator calculator) {
        /** Retorna status de saúde do sistema */
        Map<String, Object> health = new HashMap<>();
        
        try {
            double portfolioValue = calculator.getPortfolioValue();
            RiskMetrics metrics = calculator.getRiskMetrics();
            
            health.put("status", "HEALTHY");
            health.put("timestamp", System.currentTimeMillis());
            health.put("portfolioValue", portfolioValue);
            health.put("metrics", metrics.toDict());
            health.put("dailyLossLimit", RiskCalculator.DAILY_LOSS_LIMIT);
            health.put("maxLeverage", RiskCalculator.MAX_LEVERAGE);
            health.put("maxPositionSize", RiskCalculator.MAX_POSITION_SIZE);
            
        } catch (Exception e) {
            health.put("status", "UNHEALTHY");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
        }
        
        return health;
    }
}
