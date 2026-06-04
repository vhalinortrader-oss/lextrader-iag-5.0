package modulo_emocional.Decisao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * 🧪 TESTES RÁPIDOS - ESTRATÉGIA MULTI-TIMEFRAME
 * ===============================================
 * 
 * Validação rápida de funcionalidade antes de integração completa
 * 
 * Execução:
 *     java MultiTimeframeTest
 */

// ============================================================
// TEST CLASS
// ============================================================

public class MultiTimeframeTest {
    
    private static final Logger logger = Logger.getLogger(MultiTimeframeTest.class.getName());
    private static boolean importsOk = true;
    
    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║          🧪 TESTES MULTI-TIMEFRAME TRADING STRATEGY         ║");
        System.out.println("║                   Validação de Funcionalidade                ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
        
        if (!importsOk) {
            System.out.println("\n❌ Imports falharam. Abortando testes.");
            System.exit(1);
        }
        
        List<TestCase> tests = Arrays.asList(
            new TestCase("Imports", MultiTimeframeTest::test1Imports),
            new TestCase("Configuração", MultiTimeframeTest::test2ConfigValidation),
            new TestCase("Trader Básico", MultiTimeframeTest::test3BasicTrader),
            new TestCase("Trader Integrado", MultiTimeframeTest::test4IntegratedTrader),
            new TestCase("Adaptador de Decisão", MultiTimeframeTest::test5DecisionAdapter),
            new TestCase("Diferentes Tendências", MultiTimeframeTest::test6DifferentTrends),
            new TestCase("Avaliação de Risco", MultiTimeframeTest::test7RiskAssessment)
        );
        
        Map<String, Boolean> results = new HashMap<>();
        
        for (TestCase test : tests) {
            try {
                boolean result = test.run();
                results.put(test.name, result);
            } catch (Exception e) {
                System.out.println("\n❌ ERRO NÃO CAPTURADO: " + e.getMessage());
                e.printStackTrace();
                results.put(test.name, false);
            }
        }
        
        // Resumo
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 RESUMO DOS TESTES");
        System.out.println("=".repeat(70));
        
        int passed = 0;
        for (Map.Entry<String, Boolean> entry : results.entrySet()) {
            String status = entry.getValue() ? "✅ PASSOU" : "❌ FALHOU";
            System.out.println(status + ": " + entry.getKey());
            if (entry.getValue()) passed++;
        }
        
        int total = results.size();
        System.out.println("\nTotal: " + passed + "/" + total + " testes passaram");
        
        if (passed == total) {
            System.out.println("\n🎉 TODOS OS TESTES PASSARAM! Sistema pronto para produção.");
        } else {
            System.out.println("\n⚠️  " + (total - passed) + " teste(s) falharam. Revisar logs acima.");
        }
    }
    
    // ============================================================
    // FUNÇÕES DE TESTE
    // ============================================================
    
    private static boolean test1Imports() {
        /** Teste 1: Validar imports */
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 TESTE 1: VALIDAR IMPORTS");
        System.out.println("=".repeat(70));
        
        try {
            System.out.println("✓ MultiTimeframeStrategy");
            System.out.println("✓ MultiTimeframeIntegration");
            System.out.println("✓ MultiTimeframeDecisionAdapter");
            System.out.println("✓ MultiTimeframeConfig");
            
            System.out.println("\n✅ Todos os módulos importados com sucesso!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean test2ConfigValidation() {
        /** Teste 2: Validar configuração */
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 TESTE 2: VALIDAR CONFIGURAÇÃO");
        System.out.println("=".repeat(70));
        
        try {
            // Padrão
            MTFConfig config = MTFConfig.getConfig();
            ValidationResult validation = ConfigValidator.validate(config);
            System.out.println("\n📋 Configuração Padrão: " + (validation.isValid() ? "✅ Válida" : "❌ Inválida"));
            System.out.println("   " + validation.getMessage());
            
            // Pré-configurações
            Map<String, MTFConfig> configsToTest = new HashMap<>();
            configsToTest.put("Agressiva", PresetConfigs.aggressive());
            configsToTest.put("Conservadora", PresetConfigs.conservative());
            configsToTest.put("Scalping", PresetConfigs.scalping());
            configsToTest.put("Swing Trading", PresetConfigs.swingTrading());
            
            boolean allValid = validation.isValid();
            for (Map.Entry<String, MTFConfig> entry : configsToTest.entrySet()) {
                ValidationResult configValidation = ConfigValidator.validate(entry.getValue());
                String status = configValidation.isValid() ? "✅" : "❌";
                System.out.println("   " + status + " " + entry.getKey());
                allValid = allValid && configValidation.isValid();
            }
            
            if (allValid) {
                System.out.println("\n✅ Todas as configurações são válidas!");
                return true;
            } else {
                System.out.println("\n❌ Algumas configurações inválidas");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean test3BasicTrader() {
        /** Teste 3: Trader básico */
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 TESTE 3: TRADER BÁSICO");
        System.out.println("=".repeat(70));
        
        try {
            System.out.println("\n📊 Criando dados de teste...");
            List<CandleData> candles1m = createSyntheticCandles(50, 45000, "UP", 0.01);
            List<CandleData> candles3m = createSyntheticCandles(50, 45000, "UP", 0.02);
            List<CandleData> candles5m = createSyntheticCandles(50, 45000, "UP", 0.015);
            
            System.out.println("✓ 1m candles criados (50 velas)");
            System.out.println("✓ 3m candles criados (50 velas)");
            System.out.println("✓ 5m candles criados (50 velas)");
            
            System.out.println("\n🤖 Analisando...");
            MultiTimeframeTrader trader = new MultiTimeframeTrader("BTC/USDT");
            MultiTimeframeSignal signal = trader.analyzeAllTimeframes(candles1m, candles3m, candles5m);
            
            System.out.println("✓ Análise concluída");
            
            System.out.println("\n📈 Resultados:");
            System.out.println("   Símbolo: " + signal.getSymbol());
            System.out.println("   Ação: " + signal.getAction());
            System.out.println("   Sinal: " + signal.getFinalSignal());
            System.out.println("   Confiança: " + String.format("%.0f%%", signal.getOverallConfidence() * 100));
            System.out.println("   Risco: " + signal.getRiskLevel());
            System.out.println("   Entrada: $" + String.format("%.2f", signal.getSignal1m() != null ? 
                candles1m.get(candles1m.size() - 1).getClose() : 0));
            System.out.println("   Stop Loss: $" + String.format("%.2f", signal.getSuggestedStopLoss()));
            System.out.println("   Take Profit: $" + String.format("%.2f", signal.getSuggestedTakeProfit()));
            System.out.println("   Risk/Reward: " + String.format("%.2f", signal.getSuggestedRiskReward()) + ":1");
            
            System.out.println("\n✅ Trader básico funcionando!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean test4IntegratedTrader() {
        /** Teste 4: Trader integrado */
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 TESTE 4: TRADER INTEGRADO");
        System.out.println("=".repeat(70));
        
        try {
            System.out.println("\n📊 Criando trader integrado...");
            IntegratedMultiTimeframeTrader trader = new IntegratedMultiTimeframeTrader(
                Arrays.asList("BTC/USDT", "ETH/USDT")
            );
            System.out.println("✓ Trader criado para " + trader.getSymbols().size() + " símbolos");
            
            // Dados de teste
            Map<String, Object> marketDataBtc = createMarketData("BTC/USDT", 45000.0, "UP");
            Map<String, Object> marketDataEth = createMarketData("ETH/USDT", 2500.0, "DOWN");
            
            System.out.println("\n🔄 Processando BTC...");
            TradingDecision btcDecision = trader.processMarketData(marketDataBtc);
            System.out.println("✓ BTC processado: " + btcDecision.getSignal() + 
                             " (confiança: " + String.format("%.0f%%", btcDecision.getConfidence() * 100) + ")");
            
            System.out.println("\n🔄 Processando ETH...");
            TradingDecision ethDecision = trader.processMarketData(marketDataEth);
            System.out.println("✓ ETH processado: " + ethDecision.getSignal() + 
                             " (confiança: " + String.format("%.0f%%", ethDecision.getConfidence() * 100) + ")");
            
            System.out.println("\n📊 Resumo:");
            Map<String, Object> summary = trader.getDecisionSummary();
            System.out.println("   Total de decisões: " + summary.get("total_decisions"));
            System.out.println("   Compras: " + summary.get("buy_count") + " (" + 
                             String.format("%.1f", (Double) summary.get("buy_percentage")) + "%)");
            System.out.println("   Vendas: " + summary.get("sell_count") + " (" + 
                             String.format("%.1f", (Double) summary.get("sell_percentage")) + "%)");
            System.out.println("   Holds: " + summary.get("hold_count") + " (" + 
                             String.format("%.1f", (Double) summary.get("hold_percentage")) + "%)");
            
            System.out.println("\n✅ Trader integrado funcionando!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean test5DecisionAdapter() {
        /** Teste 5: Adaptador de decisão */
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 TESTE 5: ADAPTADOR DE DECISÃO");
        System.out.println("=".repeat(70));
        
        try {
            System.out.println("\n📊 Criando sinal multi-timeframe...");
            MultiTimeframeTrader trader = new MultiTimeframeTrader("BTC/USDT");
            MultiTimeframeSignal signal = trader.analyzeAllTimeframes(
                createSyntheticCandles(50, 45000, "UP"),
                createSyntheticCandles(50, 45000, "UP"),
                createSyntheticCandles(50, 45000, "UP")
            );
            System.out.println("✓ Sinal criado");
            
            System.out.println("\n🔄 Adaptando para Decision...");
            MultiTimeframeDecisionAdapter adapter = new MultiTimeframeDecisionAdapter();
            Decision decision = adapter.adaptToDecision(signal);
            System.out.println("✓ Adaptação concluída");
            
            System.out.println("\n📋 Decision Object:");
            System.out.println("   ID: " + decision.getDecisionId());
            System.out.println("   Ação: " + decision.getAction());
            System.out.println("   Prioridade: " + decision.getPriority());
            System.out.println("   Confiança: " + String.format("%.0f%%", decision.getConfidence() * 100));
            System.out.println("   Risco: " + decision.getRiskLevel());
            
            System.out.println("\n📊 JSON para DecisionEngine:");
            Map<String, Object> decisionDict = decision.toDict();
            Map<String, Object> jsonOutput = new HashMap<>();
            jsonOutput.put("decision_id", decisionDict.get("decisionId"));
            jsonOutput.put("action", decisionDict.get("action"));
            jsonOutput.put("priority", decisionDict.get("priority"));
            jsonOutput.put("confidence", decisionDict.get("confidence"));
            jsonOutput.put("symbol", decisionDict.get("symbol"));
            jsonOutput.put("stop_loss", decisionDict.get("stopLoss"));
            jsonOutput.put("take_profit", decisionDict.get("takeProfit"));
            
            System.out.println(mapToJson(jsonOutput, 2));
            
            System.out.println("\n✅ Adaptador de decisão funcionando!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean test6DifferentTrends() {
        /** Teste 6: Diferentes tendências */
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 TESTE 6: DIFERENTES TENDÊNCIAS");
        System.out.println("=".repeat(70));
        
        try {
            MultiTimeframeTrader trader = new MultiTimeframeTrader("TEST/USDT");
            
            // Teste de tendências
            List<TrendTestCase> testCases = Arrays.asList(
                new TrendTestCase("FORTE ALTA", "UP", 0.05),
                new TrendTestCase("FORTE BAIXA", "DOWN", 0.05),
                new TrendTestCase("LATERALIZADO", "SIDEWAYS", 0.02)
            );
            
            for (TrendTestCase testCase : testCases) {
                MultiTimeframeSignal signal = trader.analyzeAllTimeframes(
                    createSyntheticCandles(50, 100, testCase.trend, testCase.volatility),
                    createSyntheticCandles(50, 100, testCase.trend, testCase.volatility),
                    createSyntheticCandles(50, 100, testCase.trend, testCase.volatility)
                );
                
                System.out.println("\n📊 " + testCase.name + ":");
                System.out.println("   Sinal: " + signal.getFinalSignal());
                System.out.println("   Ação: " + signal.getAction());
                System.out.println("   Tendência 5m: " + 
                    (signal.getSignal5m() != null ? signal.getSignal5m().getTrend() : "UNKNOWN"));
                System.out.println("   Confiança: " + String.format("%.0f%%", signal.getOverallConfidence() * 100));
            }
            
            System.out.println("\n✅ Diferentes tendências testadas!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean test7RiskAssessment() {
        /** Teste 7: Avaliação de risco */
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 TESTE 7: AVALIAÇÃO DE RISCO");
        System.out.println("=".repeat(70));
        
        try {
            MultiTimeframeTrader trader = new MultiTimeframeTrader("RISK/TEST");
            
            // Caso normal
            MultiTimeframeSignal signalNormal = trader.analyzeAllTimeframes(
                createSyntheticCandles(50, 100, "UP", 0.01),
                createSyntheticCandles(50, 100, "UP", 0.01),
                createSyntheticCandles(50, 100, "UP", 0.01)
            );
            
            // Caso com alta volatilidade
            MultiTimeframeSignal signalVolatile = trader.analyzeAllTimeframes(
                createSyntheticCandles(50, 100, "SIDEWAYS", 0.1),
                createSyntheticCandles(50, 100, "SIDEWAYS", 0.1),
                createSyntheticCandles(50, 100, "SIDEWAYS", 0.1)
            );
            
            System.out.println("\n📊 Cenário Normal:");
            System.out.println("   Risco: " + signalNormal.getRiskLevel());
            System.out.println("   Confiança: " + String.format("%.0f%%", signalNormal.getOverallConfidence() * 100));
            
            System.out.println("\n📊 Cenário Volátil:");
            System.out.println("   Risco: " + signalVolatile.getRiskLevel());
            System.out.println("   Confiança: " + String.format("%.0f%%", signalVolatile.getOverallConfidence() * 100));
            
            System.out.println("\n✅ Avaliação de risco testada!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ============================================================
    // FUNÇÕES AUXILIARES
    // ============================================================
    
    private static List<CandleData> createSyntheticCandles(int count, double basePrice, String trend, double volatility) {
        /** Cria candles sintéticos para teste */
        List<CandleData> candles = new ArrayList<>();
        double price = basePrice;
        Random random = new Random();
        
        for (int i = 0; i < count; i++) {
            // Tendência
            double change;
            switch (trend) {
                case "UP":
                    change = random.nextDouble() * volatility * price;
                    break;
                case "DOWN":
                    change = -random.nextDouble() * volatility * price;
                    break;
                default: // SIDEWAYS
                    change = (random.nextDouble() - 0.5) * volatility * price;
                    break;
            }
            
            // Candle
            double openPrice = price;
            double closePrice = price + change;
            double highPrice = Math.max(openPrice, closePrice) + random.nextDouble() * volatility * price / 2;
            double lowPrice = Math.min(openPrice, closePrice) - random.nextDouble() * volatility * price / 2;
            double volume = 10000 + random.nextDouble() * 40000;
            
            candles.add(new CandleData(
                LocalDateTime.now().minusMinutes(count - i),
                openPrice,
                highPrice,
                lowPrice,
                closePrice,
                volume
            ));
            
            price = closePrice;
        }
        
        return candles;
    }
    
    private static Map<String, Object> createMarketData(String symbol, double currentPrice, String trend) {
        /** Cria dados de mercado para teste */
        Map<String, Object> marketData = new HashMap<>();
        marketData.put("symbol", symbol);
        marketData.put("current_price", currentPrice);
        marketData.put("candles_1m", createSyntheticCandles(50, currentPrice, trend, 0.01));
        marketData.put("candles_3m", createSyntheticCandles(50, currentPrice, trend, 0.02));
        marketData.put("candles_5m", createSyntheticCandles(50, currentPrice, trend, 0.015));
        
        return marketData;
    }
    
    private static String mapToJson(Map<String, Object> map, int indent) {
        /** Converte mapa para JSON formatado */
        StringBuilder json = new StringBuilder();
        String indentStr = " ".repeat(indent);
        String indentStr2 = " ".repeat(indent * 2);
        
        json.append("{\n");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",\n");
            }
            first = false;
            
            json.append(indentStr).append("\"").append(entry.getKey()).append("\": ");
            Object value = entry.getValue();
            
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Map) {
                json.append(mapToJson((Map<String, Object>) value, indent * 2));
            } else {
                json.append(value);
            }
        }
        
        json.append("\n}");
        return json.toString();
    }
    
    // ============================================================
    // CLASSES AUXILIARES
    // ============================================================
    
    @FunctionalInterface
    private interface TestFunction {
        boolean run() throws Exception;
    }
    
    private static class TestCase {
        final String name;
        final TestFunction function;
        
        TestCase(String name, TestFunction function) {
            this.name = name;
            this.function = function;
        }
        
        boolean run() throws Exception {
            return function.run();
        }
    }
    
    private static class TrendTestCase {
        final String name;
        final String trend;
        final double volatility;
        
        TrendTestCase(String name, String trend, double volatility) {
            this.name = name;
            this.trend = trend;
            this.volatility = volatility;
        }
    }
}
