package modulo_emocional.Decisao;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * AGI CLI - Interface de Linha de Comando para Automação de Trading
 * ==============================================================
 * Versão Java do CLI para controlar e monitorar a automação AGI via terminal
 * Suporta iniciar/parar, configurar, visualizar status e análises
 */

// Classe para cores do terminal
class TerminalColors {
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String GREEN = "\033[92m";
    public static final String RED = "\033[91m";
    public static final String YELLOW = "\033[93m";
    public static final String BLUE = "\033[94m";
    public static final String CYAN = "\033[96m";
    public static final String WHITE = "\033[97m";
    
    public static void printHeader(String title) {
        System.out.println("\n" + BOLD + CYAN + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60) + RESET + "\n");
    }
    
    public static void printSuccess(String message) {
        System.out.println(GREEN + "✅ " + message + RESET);
    }
    
    public static void printError(String message) {
        System.out.println(RED + "❌ " + message + RESET);
    }
    
    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠️  " + message + RESET);
    }
    
    public static void printInfo(String message) {
        System.out.println(BLUE + "ℹ️  " + message + RESET);
    }
}

// Classes de Dados
class AGIConfig {
    String analysisFrequency;  // FAST, NORMAL, SLOW
    int maxConcurrentTrades;
    int maxDailyTrades;
    double riskPerTrade;
    double maxDailyRisk;
    boolean enableLiveTrading;
    boolean enablePaperTrading;
    List<String> symbols;
    
    public AGIConfig() {
        this.analysisFrequency = "NORMAL";
        this.maxConcurrentTrades = 5;
        this.maxDailyTrades = 50;
        this.riskPerTrade = 0.02;
        this.maxDailyRisk = 0.05;
        this.enableLiveTrading = false;
        this.enablePaperTrading = true;
        this.symbols = Arrays.asList("EURUSD", "GBPUSD", "BTC", "ETH");
    }
    
    @Override
    public String toString() {
        return String.format(
            "AGIConfig{analysisFrequency='%s', maxConcurrentTrades=%d, maxDailyTrades=%d, " +
            "riskPerTrade=%.3f, maxDailyRisk=%.3f, enableLiveTrading=%s, enablePaperTrading=%s, symbols=%s}",
            analysisFrequency, maxConcurrentTrades, maxDailyTrades, riskPerTrade, maxDailyRisk,
            enableLiveTrading, enablePaperTrading, symbols
        );
    }
}

class StatusData {
    String state;
    String uptime;
    Map<String, Object> analyses;
    Map<String, Object> decisions;
    Map<String, Object> trades;
    Map<String, Object> portfolio;
    
    public StatusData() {
        this.state = "RUNNING";
        this.uptime = "2h 34m 12s";
        this.analyses = new HashMap<>();
        this.decisions = new HashMap<>();
        this.trades = new HashMap<>();
        this.portfolio = new HashMap<>();
        
        // Dados simulados
        analyses.put("total", 156);
        analyses.put("last", "2 minutos atrás");
        analyses.put("symbols_active", 10);
        
        decisions.put("total", 23);
        decisions.put("buy_signals", 8);
        decisions.put("sell_signals", 7);
        decisions.put("hold", 8);
        
        trades.put("open", 3);
        trades.put("today", 5);
        trades.put("successful", 4);
        trades.put("failed", 1);
        
        portfolio.put("capital", "$100,000");
        portfolio.put("used", "$45,230");
        portfolio.put("available", "$54,770");
        portfolio.put("pnl_today", "+$1,230.50");
        portfolio.put("pnl_pct", "+1.23%");
    }
}

class AnalysisData {
    String symbol;
    String timestamp;
    String action;
    double confidence;
    List<String> signals;
    
    public AnalysisData(String symbol, String timestamp, String action, double confidence, List<String> signals) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.action = action;
        this.confidence = confidence;
        this.signals = signals;
    }
}

class TradeData {
    String symbol;
    String action;
    String entry;
    String current;
    String pnl;
    String pnlPct;
    String opened;
    
    public TradeData(String symbol, String action, String entry, String current, String pnl, String pnlPct, String opened) {
        this.symbol = symbol;
        this.action = action;
        this.entry = entry;
        this.current = current;
        this.pnl = pnl;
        this.pnlPct = pnlPct;
        this.opened = opened;
    }
}

class AlertData {
    String type;
    String symbol;
    String severity;
    String message;
    
    public AlertData(String type, String symbol, String severity, String message) {
        this.type = type;
        this.symbol = symbol;
        this.severity = severity;
        this.message = message;
    }
}

class PortfolioData {
    String capitalInicial;
    String capitalAtual;
    String pnlTotal;
    String pnlPct;
    int posicoesAbertas;
    int posicoesFechadas;
    String taxaAcerto;
    String maiorLucro;
    String maiorPerda;
    String duracao;
    
    public PortfolioData() {
        this.capitalInicial = "$100,000";
        this.capitalAtual = "$101,230.50";
        this.pnlTotal = "+$1,230.50";
        this.pnlPct = "+1.23%";
        this.posicoesAbertas = 3;
        this.posicoesFechadas = 12;
        this.taxaAcerto = "75%";
        this.maiorLucro = "$810";
        this.maiorPerda = "-$325";
        this.duracao = "2h 34m";
    }
}

// Controlador Principal
class AGICLIController {
    private String configFile;
    private String stateFile;
    private AGIConfig config;
    private AtomicBoolean isRunning;
    private Random random;
    
    private static final Logger logger = Logger.getLogger(AGICLIController.class.getName());
    
    public AGICLIController() {
        this.configFile = "agi_automation_config.json";
        this.stateFile = "agi_automation_state.json";
        this.config = new AGIConfig();
        this.isRunning = new AtomicBoolean(false);
        this.random = new Random();
        
        loadConfig();
        logger.info("AGI CLI Controller inicializado");
    }
    
    // Métodos de Configuração
    public AGIConfig loadConfig() {
        /** Carrega configuração do arquivo */
        try {
            Path configPath = Paths.get(configFile);
            if (Files.exists(configPath)) {
                // Simulação de leitura de JSON
                // Em produção, usar biblioteca JSON como Jackson ou Gson
                TerminalColors.printInfo("Configuração carregada de " + configFile);
                return config;
            }
        } catch (Exception e) {
            TerminalColors.printWarning("Não foi possível carregar config: " + e.getMessage());
        }
        
        return config; // Retorna configuração padrão
    }
    
    public void saveConfig() {
        /** Salva configuração no arquivo */
        try {
            // Simulação de escrita de JSON
            // Em produção, usar biblioteca JSON
            TerminalColors.printSuccess("Configuração salva em " + configFile);
        } catch (Exception e) {
            TerminalColors.printError("Erro ao salvar config: " + e.getMessage());
        }
    }
    
    // =========================================================================
    // COMANDOS DE CONTROLE
    // =========================================================================
    
    public boolean cmdStart() {
        /** Inicia a automação */
        TerminalColors.printHeader("INICIANDO AUTOMAÇÃO AGI");
        
        TerminalColors.printInfo("Modo de análise: " + config.analysisFrequency);
        TerminalColors.printInfo("Símbolos: " + String.join(", ", config.symbols));
        TerminalColors.printInfo("Capital máximo por trade: " + (config.riskPerTrade * 100) + "%");
        
        if (config.enableLiveTrading) {
            TerminalColors.printWarning("⚠️  MODO LIVE TRADING ATIVADO!");
            Scanner scanner = new Scanner(System.in);
            System.out.print(TerminalColors.YELLOW + "Digite 'CONFIRMAR' para continuar: " + TerminalColors.RESET);
            String confirm = scanner.nextLine().trim();
            
            if (!confirm.equals("CONFIRMAR")) {
                TerminalColors.printWarning("Operação cancelada");
                return false;
            }
        } else {
            TerminalColors.printInfo("Modo paper trading ativado (sem execução real)");
        }
        
        isRunning.set(true);
        TerminalColors.printSuccess("✨ Automação AGI iniciada com sucesso!");
        TerminalColors.printInfo("A IA agora está operando automaticamente no mercado");
        TerminalColors.printInfo("Use 'java -cp . modulo_emocional.Decisao.AGICLI status' para monitorar");
        
        return true;
    }
    
    public void cmdStop() {
        /** Para a automação */
        TerminalColors.printHeader("PARANDO AUTOMAÇÃO AGI");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print(TerminalColors.YELLOW + "Tem certeza? (s/n): " + TerminalColors.RESET);
        String confirm = scanner.nextLine().trim();
        
        if (confirm.toLowerCase().equals("s")) {
            isRunning.set(false);
            TerminalColors.printSuccess("Automação parada");
        } else {
            TerminalColors.printWarning("Operação cancelada");
        }
    }
    
    public void cmdPause() {
        /** Pausa a automação */
        TerminalColors.printInfo("Automação pausada (posições abertas continuam monitoradas)");
    }
    
    public void cmdResume() {
        /** Retoma a automação */
        isRunning.set(true);
        TerminalColors.printInfo("Automação retomada");
    }
    
    public void cmdStatus() {
        /** Mostra status da automação */
        TerminalColors.printHeader("STATUS DA AUTOMAÇÃO AGI");
        
        StatusData status = new StatusData();
        
        System.out.println(TerminalColors.GREEN + "Estado: " + status.state + TerminalColors.RESET);
        System.out.println("Uptime: " + status.uptime);
        
        System.out.println("\n" + TerminalColors.BOLD + "📊 Análises:" + TerminalColors.RESET);
        System.out.println("  Total: " + status.analyses.get("total"));
        System.out.println("  Última: " + status.analyses.get("last"));
        System.out.println("  Símbolos ativos: " + status.analyses.get("symbols_active"));
        
        System.out.println("\n" + TerminalColors.BOLD + "🎯 Decisões:" + TerminalColors.RESET);
        System.out.println("  Total: " + status.decisions.get("total"));
        System.out.println("  BUY: " + status.decisions.get("buy_signals"));
        System.out.println("  SELL: " + status.decisions.get("sell_signals"));
        System.out.println("  HOLD: " + status.decisions.get("hold"));
        
        System.out.println("\n" + TerminalColors.BOLD + "💹 Trades:" + TerminalColors.RESET);
        System.out.println("  Abertos: " + status.trades.get("open"));
        System.out.println("  Hoje: " + status.trades.get("today"));
        System.out.println("  Sucessos: " + status.trades.get("successful"));
        System.out.println("  Falhas: " + status.trades.get("failed"));
        
        System.out.println("\n" + TerminalColors.BOLD + "💰 Portfólio:" + TerminalColors.RESET);
        System.out.println("  Capital total: " + status.portfolio.get("capital"));
        System.out.println("  Utilizado: " + status.portfolio.get("used"));
        System.out.println("  Disponível: " + status.portfolio.get("available"));
        System.out.println("  " + TerminalColors.GREEN + "P&L hoje: " + status.portfolio.get("pnl_today") + 
                          " (" + status.portfolio.get("pnl_pct") + ")" + TerminalColors.RESET);
    }
    
    public void cmdConfig(String subcommand, String key, String value, String action, String symbol) {
        /** Gerencia configuração */
        TerminalColors.printHeader("CONFIGURAÇÃO DE AUTOMAÇÃO");
        
        if ("show".equals(subcommand)) {
            System.out.println(config.toString());
        } else if ("set".equals(subcommand)) {
            if (key != null && value != null) {
                // Atualizar configuração
                updateConfigValue(key, value);
                saveConfig();
            } else {
                TerminalColors.printError("Forneça --key e --value");
            }
        } else if ("symbols".equals(subcommand)) {
            if ("add".equals(action) && symbol != null) {
                if (!config.symbols.contains(symbol)) {
                    config.symbols.add(symbol);
                    saveConfig();
                    TerminalColors.printSuccess("Símbolo " + symbol + " adicionado");
                } else {
                    TerminalColors.printWarning("Símbolo " + symbol + " já existe");
                }
            } else if ("remove".equals(action) && symbol != null) {
                if (config.symbols.remove(symbol)) {
                    saveConfig();
                    TerminalColors.printSuccess("Símbolo " + symbol + " removido");
                } else {
                    TerminalColors.printWarning("Símbolo " + symbol + " não encontrado");
                }
            } else if ("list".equals(action)) {
                System.out.println("\nSímbolos: " + String.join(", ", config.symbols) + "\n");
            }
        }
    }
    
    private void updateConfigValue(String key, String value) {
        /** Atualiza valor específico da configuração */
        switch (key) {
            case "analysis_frequency":
                config.analysisFrequency = value;
                break;
            case "max_concurrent_trades":
                config.maxConcurrentTrades = Integer.parseInt(value);
                break;
            case "max_daily_trades":
                config.maxDailyTrades = Integer.parseInt(value);
                break;
            case "risk_per_trade":
                config.riskPerTrade = Double.parseDouble(value);
                break;
            case "max_daily_risk":
                config.maxDailyRisk = Double.parseDouble(value);
                break;
            case "enable_live_trading":
                config.enableLiveTrading = Boolean.parseBoolean(value);
                break;
            case "enable_paper_trading":
                config.enablePaperTrading = Boolean.parseBoolean(value);
                break;
            default:
                TerminalColors.printError("Chave de configuração desconhecida: " + key);
        }
    }
    
    public void cmdAnalysis() {
        /** Mostra análises recentes */
        TerminalColors.printHeader("ANÁLISES RECENTES");
        
        List<AnalysisData> analyses = Arrays.asList(
            new AnalysisData("EURUSD", "2 min atrás", "BUY", 0.87, Arrays.asList("Breakout", "Volume")),
            new AnalysisData("GBPUSD", "5 min atrás", "HOLD", 0.65, Arrays.asList("Ranging")),
            new AnalysisData("BTC", "3 min atrás", "SELL", 0.92, Arrays.asList("Divergência", "Resistência"))
        );
        
        for (AnalysisData analysis : analyses) {
            String color = "BUY".equals(analysis.action) ? TerminalColors.GREEN : 
                           "SELL".equals(analysis.action) ? TerminalColors.RED : TerminalColors.YELLOW;
            
            System.out.println(color + String.format("%-8s %-6s", analysis.symbol, analysis.action) + 
                              TerminalColors.RESET + " | " +
                              "Confiança: " + String.format("%.2f", analysis.confidence) + " | " +
                              "Sinais: " + String.join(", ", analysis.signals) + " | " +
                              analysis.timestamp);
        }
    }
    
    public void cmdTrades() {
        /** Mostra trades abertos */
        TerminalColors.printHeader("TRADES ABERTOS");
        
        List<TradeData> trades = Arrays.asList(
            new TradeData("EURUSD", "BUY", "1.0850", "1.0892", "+$420", "+1.23%", "1h 20m atrás"),
            new TradeData("BTC", "BUY", "42300", "42850", "+$810", "+0.82%", "45m atrás"),
            new TradeData("ETH", "BUY", "2280", "2215", "-$325", "-1.43%", "2h 10m atrás")
        );
        
        System.out.println(TerminalColors.BOLD + String.format("%-8s %-6s %-12s %-12s %-12s %-15s", 
            "Symbol", "Action", "Entry", "Current", "P&L", "Aberto") + TerminalColors.RESET);
        System.out.println("-".repeat(75));
        
        for (TradeData trade : trades) {
            String pnlColor = trade.pnl.startsWith("+") ? TerminalColors.GREEN : TerminalColors.RED;
            System.out.println(String.format("%-8s %-6s %-12s %-12s %s%-12s%s %-15s",
                trade.symbol, trade.action, trade.entry, trade.current,
                pnlColor, trade.pnl, TerminalColors.RESET, trade.opened));
        }
    }
    
    public void cmdAlerts() {
        /** Mostra alertas do mercado */
        TerminalColors.printHeader("ALERTAS DO MERCADO");
        
        List<AlertData> alerts = Arrays.asList(
            new AlertData("PRICE_JUMP", "GBPUSD", "WARNING", "Salto de 0.45%"),
            new AlertData("VOLUME", "BTC", "INFO", "Volume 180% acima da média"),
            new AlertData("VOLATILITY", "EURUSD", "CRITICAL", "Volatilidade extrema detectada")
        );
        
        for (AlertData alert : alerts) {
            String color = "CRITICAL".equals(alert.severity) ? TerminalColors.RED :
                           "WARNING".equals(alert.severity) ? TerminalColors.YELLOW : TerminalColors.BLUE;
            
            System.out.println(color + "[" + alert.severity + "] " + 
                              String.format("%-8s - %s", alert.symbol, alert.message) + TerminalColors.RESET);
        }
    }
    
    public void cmdPortfolio() {
        /** Mostra estado do portfólio */
        TerminalColors.printHeader("ESTADO DO PORTFÓLIO");
        
        PortfolioData portfolio = new PortfolioData();
        
        System.out.println(TerminalColors.BOLD + "Resumo do Portfólio:" + TerminalColors.RESET);
        System.out.println("  Capital inicial: " + portfolio.capitalInicial);
        System.out.println("  Capital atual: " + portfolio.capitalAtual);
        System.out.println("  " + TerminalColors.GREEN + "P&L total: " + portfolio.pnlTotal + 
                          " (" + portfolio.pnlPct + ")" + TerminalColors.RESET);
        
        System.out.println("\n" + TerminalColors.BOLD + "Estatísticas:" + TerminalColors.RESET);
        System.out.println("  Posições abertas: " + portfolio.posicoesAbertas);
        System.out.println("  Posições fechadas: " + portfolio.posicoesFechadas);
        System.out.println("  Taxa de acerto: " + portfolio.taxaAcerto);
        System.out.println("  " + TerminalColors.GREEN + "Maior lucro: " + portfolio.maiorLucro + TerminalColors.RESET);
        System.out.println("  " + TerminalColors.RED + "Maior perda: " + portfolio.maiorPerda + TerminalColors.RESET);
        System.out.println("  Duração: " + portfolio.duracao);
    }
    
    // Getters
    public boolean isRunning() {
        return isRunning.get();
    }
    
    public AGIConfig getConfig() {
        return config;
    }
    
    public void shutdown() {
        /** Encerra o controlador */
        isRunning.set(false);
        logger.info("AGI CLI Controller encerrado");
    }
}

// Classe Principal
public class AGICLI {
    
    public static void main(String[] args) {
        System.out.println("🚀 AGI CLI - Controlador de Automação de Trading");
        
        AGICLIController controller = new AGICLIController();
        
        // Adicionar shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n⏹️ Encerrando AGI CLI...");
            controller.shutdown();
        }));
        
        try {
            // Se não houver argumentos, mostrar ajuda
            if (args.length == 0) {
                printHelp();
                return;
            }
            
            String command = args[0].toLowerCase();
            
            switch (command) {
                case "start":
                    controller.cmdStart();
                    break;
                    
                case "stop":
                    controller.cmdStop();
                    break;
                    
                case "pause":
                    controller.cmdPause();
                    break;
                    
                case "resume":
                    controller.cmdResume();
                    break;
                    
                case "status":
                    controller.cmdStatus();
                    break;
                    
                case "config":
                    handleConfigCommand(controller, args);
                    break;
                    
                case "analysis":
                    controller.cmdAnalysis();
                    break;
                    
                case "trades":
                    controller.cmdTrades();
                    break;
                    
                case "alerts":
                    controller.cmdAlerts();
                    break;
                    
                case "portfolio":
                    controller.cmdPortfolio();
                    break;
                    
                case "help":
                case "--help":
                case "-h":
                    printHelp();
                    break;
                    
                default:
                    TerminalColors.printError("Comando desconhecido: " + command);
                    printHelp();
            }
            
        } catch (Exception e) {
            TerminalColors.printError("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void handleConfigCommand(AGICLIController controller, String[] args) {
        /** Processa comandos de configuração */
        if (args.length < 2) {
            TerminalColors.printError("Uso: config <subcommand> [opções]");
            return;
        }
        
        String subcommand = args[1].toLowerCase();
        String key = null;
        String value = null;
        String action = null;
        String symbol = null;
        
        // Parse argumentos adicionais
        for (int i = 2; i < args.length; i++) {
            if (args[i].startsWith("--key=")) {
                key = args[i].substring(7);
            } else if (args[i].startsWith("--value=")) {
                value = args[i].substring(8);
            } else if (args[i].startsWith("--symbol=")) {
                symbol = args[i].substring(9);
            } else if (!args[i].startsWith("--")) {
                if (action == null) {
                    action = args[i];
                } else if (symbol == null) {
                    symbol = args[i];
                }
            }
        }
        
        controller.cmdConfig(subcommand, key, value, action, symbol);
    }
    
    private static void printHelp() {
        /** Imprime ajuda do CLI */
        TerminalColors.printHeader("AGI CLI - AJUDA");
        
        System.out.println("Uso: java -cp . modulo_emocional.Decisao.AGICLI <comando> [opções]");
        System.out.println();
        
        System.out.println("Comandos disponíveis:");
        System.out.println("  start                    Inicia automação AGI");
        System.out.println("  stop                     Para automação AGI");
        System.out.println("  pause                    Pausa automação AGI");
        System.out.println("  resume                   Retoma automação AGI");
        System.out.println("  status                   Mostra status da automação");
        System.out.println("  config                   Gerencia configuração");
        System.out.println("  analysis                 Últimas análises");
        System.out.println("  trades                   Trades abertos");
        System.out.println("  alerts                   Alertas do mercado");
        System.out.println("  portfolio                Estado do portfólio");
        System.out.println("  help                     Mostra esta ajuda");
        System.out.println();
        
        System.out.println("Comandos de configuração:");
        System.out.println("  config show              Mostra configuração atual");
        System.out.println("  config set --key=<chave> --value=<valor>");
        System.out.println("  config symbols add --symbol=<símbolo>");
        System.out.println("  config symbols remove --symbol=<símbolo>");
        System.out.println("  config symbols list");
        System.out.println();
        
        System.out.println("Exemplos de uso:");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI start");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI status");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI config show");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI config set --key enable_live_trading --value true");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI config symbols add --symbol BTCUSD");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI analysis");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI trades");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI alerts");
        System.out.println("  java -cp . modulo_emocional.Decisao.AGICLI portfolio");
        System.out.println();
        
        System.out.println("Chaves de configuração disponíveis:");
        System.out.println("  analysis_frequency     - FAST, NORMAL, SLOW");
        System.out.println("  max_concurrent_trades  - Número máximo de trades simultâneos");
        System.out.println("  max_daily_trades       - Número máximo de trades diários");
        System.out.println("  risk_per_trade         - Risco por trade (ex: 0.02 para 2%)");
        System.out.println("  max_daily_risk         - Risco máximo diário (ex: 0.05 para 5%)");
        System.out.println("  enable_live_trading    - true/false para trading real");
        System.out.println("  enable_paper_trading   - true/false para paper trading");
        System.out.println();
        
        TerminalColors.printInfo("Para mais informações, consulte a documentação do LEXTRADER-IAG 4.0");
    }
    
    /**
     * Método utilitário para uso programático
     */
    public static AGICLIController createController() {
        return new AGICLIController();
    }
    
    /**
     * Método utilitário para demonstração automática
     */
    public static void runDemo() {
        TerminalColors.printHeader("DEMONSTRAÇÃO AUTOMÁTICA DO AGI CLI");
        
        AGICLIController controller = new AGICLIController();
        
        try {
            // Simular sequência de comandos
            System.out.println("🤖 Iniciando demonstração automática...\n");
            
            Thread.sleep(1000);
            controller.cmdStatus();
            
            Thread.sleep(2000);
            controller.cmdAnalysis();
            
            Thread.sleep(2000);
            controller.cmdTrades();
            
            Thread.sleep(2000);
            controller.cmdPortfolio();
            
            Thread.sleep(2000);
            controller.cmdAlerts();
            
            TerminalColors.printSuccess("✨ Demonstração concluída com sucesso!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TerminalColors.printWarning("Demonstração interrompida");
        } finally {
            controller.shutdown();
        }
    }
}
