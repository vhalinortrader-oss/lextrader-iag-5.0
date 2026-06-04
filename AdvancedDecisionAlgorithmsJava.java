import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Advanced Decision Algorithms - Interface para Algoritmos de Decisão Avançados
 * 
 * Sistema de visualização e controle de algoritmos de decisão para trading,
 * com monitoramento em tempo real, fluxo de processamento e histórico de decisões.
 */
public class AdvancedDecisionAlgorithmsJava {

    // ==================== ENUMS ====================

    public enum AlgorithmType {
        ML("ML"),
        STATISTICAL("STATISTICAL"),
        HYBRID("HYBRID"),
        QUANTUM("QUANTUM"),
        ENSEMBLE("ENSEMBLE");

        private final String value;

        AlgorithmType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum NodeStatus {
        PROCESSING("PROCESSING"),
        COMPLETED("COMPLETED"),
        WAITING("WAITING"),
        ERROR("ERROR");

        private final String value;

        NodeStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getIcon() {
            switch (this) {
                case COMPLETED: return "✅";
                case PROCESSING: return "🔄";
                case WAITING: return "⏳";
                case ERROR: return "❌";
                default: return "⏳";
            }
        }

        public Color getColor() {
            switch (this) {
                case COMPLETED: return new Color(16, 185, 129); // #10b981
                case PROCESSING: return new Color(59, 130, 246); // #3b82f6
                case WAITING: return new Color(245, 158, 11); // #f59e0b
                case ERROR: return new Color(239, 68, 68); // #ef4444
                default: return Color.GRAY;
            }
        }
    }

    public enum Decision {
        BUY("BUY"),
        SELL("SELL"),
        HOLD("HOLD"),
        CLOSE("CLOSE");

        private final String value;

        Decision(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getIcon() {
            switch (this) {
                case BUY: return "📈";
                case SELL: return "📉";
                case HOLD: return "🎯";
                case CLOSE: return "👁";
                default: return "•";
            }
        }

        public Color getColor() {
            switch (this) {
                case BUY: return new Color(16, 185, 129); // #10b981
                case SELL: return new Color(239, 68, 68); // #ef4444
                case HOLD: return new Color(245, 158, 11); // #f59e0b
                case CLOSE: return new Color(59, 130, 246); // #3b82f6
                default: return Color.GRAY;
            }
        }
    }

    // ==================== DATA CLASSES ====================

    public static class DecisionAlgorithm {
        private String id;
        private String name;
        private AlgorithmType type;
        private String description;
        private double accuracy;
        private double speed;
        private double complexity;
        private double confidence;
        private boolean isActive;
        private int decisions;
        private double successRate;
        private double avgResponseTime;
        private List<String> specialization;

        public DecisionAlgorithm(String id, String name, AlgorithmType type, String description,
                                double accuracy, double speed, double complexity, double confidence,
                                boolean isActive, int decisions, double successRate, 
                                double avgResponseTime, List<String> specialization) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.description = description;
            this.accuracy = accuracy;
            this.speed = speed;
            this.complexity = complexity;
            this.confidence = confidence;
            this.isActive = isActive;
            this.decisions = decisions;
            this.successRate = successRate;
            this.avgResponseTime = avgResponseTime;
            this.specialization = specialization != null ? specialization : new ArrayList<>();
        }

        // Getters e Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public AlgorithmType getType() { return type; }
        public String getDescription() { return description; }
        public double getAccuracy() { return accuracy; }
        public double getSpeed() { return speed; }
        public double getComplexity() { return complexity; }
        public double getConfidence() { return confidence; }
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
        public int getDecisions() { return decisions; }
        public double getSuccessRate() { return successRate; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public List<String> getSpecialization() { return specialization; }
    }

    public static class DecisionNode {
        private String id;
        private String name;
        private String input;
        private String output;
        private double confidence;
        private double executionTime;
        private NodeStatus status;

        public DecisionNode(String id, String name, String input, String output,
                           double confidence, double executionTime, NodeStatus status) {
            this.id = id;
            this.name = name;
            this.input = input;
            this.output = output;
            this.confidence = confidence;
            this.executionTime = executionTime;
            this.status = status;
        }

        // Getters e Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getInput() { return input; }
        public String getOutput() { return output; }
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        public double getExecutionTime() { return executionTime; }
        public void setExecutionTime(double executionTime) { this.executionTime = executionTime; }
        public NodeStatus getStatus() { return status; }
        public void setStatus(NodeStatus status) { this.status = status; }
    }

    public static class MarketDecision {
        private String timestamp;
        private String algorithm;
        private Decision decision;
        private double confidence;
        private String reasoning;
        private List<String> factors;
        private double risk;
        private double expectedReturn;
        private String timeframe;

        public MarketDecision(String timestamp, String algorithm, Decision decision,
                             double confidence, String reasoning, List<String> factors,
                             double risk, double expectedReturn, String timeframe) {
            this.timestamp = timestamp;
            this.algorithm = algorithm;
            this.decision = decision;
            this.confidence = confidence;
            this.reasoning = reasoning;
            this.factors = factors != null ? factors : new ArrayList<>();
            this.risk = risk;
            this.expectedReturn = expectedReturn;
            this.timeframe = timeframe;
        }

        // Getters
        public String getTimestamp() { return timestamp; }
        public String getAlgorithm() { return algorithm; }
        public Decision getDecision() { return decision; }
        public double getConfidence() { return confidence; }
        public String getReasoning() { return reasoning; }
        public List<String> getFactors() { return factors; }
        public double getRisk() { return risk; }
        public double getExpectedReturn() { return expectedReturn; }
        public String getTimeframe() { return timeframe; }
    }

    // ==================== CARD PANEL PERSONALIZADO ====================

    public static class CardPanel extends JPanel {
        private Color borderColor = new Color(229, 231, 235); // #e5e7eb

        public CardPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        }

        public CardPanel(int padding) {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(padding, padding, padding, padding)
            ));
        }
    }

    // ==================== PROGRESS BAR PERSONALIZADA ====================

    public static class ProgressBar extends JPanel {
        private double value;
        private Color color;

        public ProgressBar(double value, Color color) {
            this.value = Math.min(100, Math.max(0, value));
            this.color = color;
            setPreferredSize(new Dimension(150, 8));
            setBackground(new Color(243, 244, 246)); // #f3f4f6
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            int width = getWidth();
            int height = getHeight();
            int progressWidth = (int) ((value / 100) * width);

            // Desenhar fundo
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, width, height);

            // Desenhar progresso
            g2d.setColor(color);
            g2d.fillRect(0, 0, progressWidth, height);

            g2d.dispose();
        }

        public void setValue(double value) {
            this.value = Math.min(100, Math.max(0, value));
            repaint();
        }
    }

    // ==================== APLICAÇÃO PRINCIPAL ====================

    public static class AdvancedDecisionAlgorithmsApp {
        private JFrame frame;
        private JTabbedPane tabbedPane;
        private JButton executeBtn;
        
        // Estado da aplicação
        private String selectedAlgorithm = "ensemble";
        private AtomicBoolean isProcessing = new AtomicBoolean(false);
        private AtomicInteger currentDecisions = new AtomicInteger(0);
        private AtomicReference<Double> systemLoad = new AtomicReference<>(67.3);
        
        // Dados
        private List<DecisionAlgorithm> algorithms;
        private List<DecisionNode> decisionFlow;
        private List<MarketDecision> recentDecisions;
        
        // Widgets para atualização dinâmica
        private Map<String, JLabel> metricsLabels = new HashMap<>();
        private Map<String, Map<String, JComponent>> algorithmWidgets = new HashMap<>();
        private List<Map<String, JComponent>> flowWidgets = new ArrayList<>();
        private JPanel decisionsPanel;
        
        // Timer para atualizações
        private ScheduledExecutorService scheduler;

        // Cores
        private Color bgColor = new Color(248, 250, 252); // #f8fafc
        private Color primaryColor = new Color(59, 130, 246); // #3b82f6
        private Color successColor = new Color(16, 185, 129); // #10b981
        private Color warningColor = new Color(245, 158, 11); // #f59e0b
        private Color errorColor = new Color(239, 68, 68); // #ef4444
        private Color infoColor = new Color(99, 102, 241); // #6366f1

        public AdvancedDecisionAlgorithmsApp() {
            initializeData();
            initializeUI();
            startUpdateThread();
        }

        private void initializeData() {
            algorithms = new ArrayList<>();
            decisionFlow = new ArrayList<>();
            recentDecisions = new ArrayList<>();

            // Algoritmos
            algorithms.add(new DecisionAlgorithm(
                "ensemble", "Ensemble Multi-Algoritmo", AlgorithmType.ENSEMBLE,
                "Combina múltiplos algoritmos usando voting e stacking para decisões mais robustas",
                94.7, 85.2, 95.8, 89.3, true, 2847, 87.4, 0.23,
                Arrays.asList("Análise Multi-Modal", "Consenso Algorítmico", "Meta-Learning")
            ));

            algorithms.add(new DecisionAlgorithm(
                "quantum_nn", "Rede Neural Quântica", AlgorithmType.QUANTUM,
                "Utiliza computação quântica simulada para processamento paralelo de cenários",
                91.3, 92.7, 98.5, 85.9, true, 1623, 83.2, 0.08,
                Arrays.asList("Superposição", "Entrelaçamento", "Túnel Quântico")
            ));

            algorithms.add(new DecisionAlgorithm(
                "adaptive_lstm", "LSTM Adaptativo Profundo", AlgorithmType.ML,
                "Rede LSTM com arquitetura adaptativa que se reconfigura baseada em condições de mercado",
                88.9, 78.4, 87.3, 82.7, true, 3241, 79.6, 0.45,
                Arrays.asList("Séries Temporais", "Memória Longa", "Adaptação Dinâmica")
            ));

            algorithms.add(new DecisionAlgorithm(
                "bayesian_optimizer", "Otimizador Bayesiano Multi-Objetivo", AlgorithmType.STATISTICAL,
                "Optimiza múltiplos objetivos simultaneamente usando inferência bayesiana",
                86.4, 71.8, 82.1, 78.9, true, 1876, 81.3, 0.67,
                Arrays.asList("Otimização", "Incerteza", "Multi-Objetivo")
            ));

            algorithms.add(new DecisionAlgorithm(
                "reinforcement_agent", "Agente de Aprendizado por Reforço", AlgorithmType.ML,
                "Agente que aprende estratégias ótimas através de interação com o ambiente de mercado",
                89.7, 83.6, 91.4, 86.1, true, 2156, 84.8, 0.31,
                Arrays.asList("Q-Learning", "Policy Gradient", "Actor-Critic")
            ));

            algorithms.add(new DecisionAlgorithm(
                "fuzzy_expert", "Sistema Especialista Fuzzy", AlgorithmType.HYBRID,
                "Combina lógica fuzzy com regras de especialistas para decisões em condições de incerteza",
                84.2, 94.7, 73.9, 76.4, false, 1432, 77.9, 0.12,
                Arrays.asList("Lógica Fuzzy", "Regras Especialistas", "Incerteza")
            ));

            algorithms.add(new DecisionAlgorithm(
                "genetic_algorithm", "Algoritmo Genético Evolutivo", AlgorithmType.HYBRID,
                "Evolui estratégias de trading através de seleção natural e mutação genética",
                87.1, 76.3, 88.7, 81.5, true, 998, 82.7, 0.89,
                Arrays.asList("Evolução", "Otimização Genética", "Seleção Natural")
            ));

            // Fluxo de decisão
            decisionFlow.add(new DecisionNode("1", "Coleta de Dados", "Market Data", "Processed Data",
                98.5, 0.02, NodeStatus.COMPLETED));
            decisionFlow.add(new DecisionNode("2", "Pré-processamento", "Raw Data", "Clean Data",
                96.7, 0.08, NodeStatus.COMPLETED));
            decisionFlow.add(new DecisionNode("3", "Feature Engineering", "Clean Data", "Features",
                94.2, 0.15, NodeStatus.COMPLETED));
            decisionFlow.add(new DecisionNode("4", "Análise Ensemble", "Features", "Predictions",
                89.3, 0.23, NodeStatus.PROCESSING));
            decisionFlow.add(new DecisionNode("5", "Validação Cruzada", "Predictions", "Validated",
                0, 0, NodeStatus.WAITING));
            decisionFlow.add(new DecisionNode("6", "Execução de Decisão", "Validated", "Action",
                0, 0, NodeStatus.WAITING));

            // Decisões recentes
            recentDecisions.add(new MarketDecision(
                "2024-01-15 15:42:23",
                "Ensemble Multi-Algoritmo",
                Decision.BUY,
                87.4,
                "Confluência de sinais: breakout técnico + momentum positivo + volume acima da média",
                Arrays.asList("RSI(14): 45.2", "MACD: Bullish Cross", "Volume: +234%", "Support: 42,100"),
                2.3,
                5.7,
                "4H"
            ));

            recentDecisions.add(new MarketDecision(
                "2024-01-15 15:38:47",
                "Rede Neural Quântica",
                Decision.SELL,
                92.1,
                "Padrão de reversão detectado com alta probabilidade baseado em análise quântica",
                Arrays.asList("Quantum State: Bearish", "Volatility: Increasing", "Resistance: 42,800", "Divergence: Confirmed"),
                1.8,
                4.2,
                "1H"
            ));

            recentDecisions.add(new MarketDecision(
                "2024-01-15 15:35:12",
                "LSTM Adaptativo",
                Decision.HOLD,
                76.8,
                "Mercado em consolidação, aguardando breakout definitivo da faixa atual",
                Arrays.asList("Trend: Sideways", "ATR: Low", "Volume: Decreasing", "Support/Resistance: Strong"),
                1.2,
                2.1,
                "2H"
            ));
        }

        private void initializeUI() {
            frame = new JFrame("🧠 Algoritmos de Decisão Avançados");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 900);
            frame.setMinimumSize(new Dimension(1000, 700));

            // Painel principal com padding
            JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
            mainPanel.setBackground(bgColor);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            frame.add(mainPanel);

            // Cabeçalho
            setupHeader(mainPanel);

            // Painel de métricas
            setupMetricsPanel(mainPanel);

            // Notebook com abas
            setupTabbedPane(mainPanel);

            // Sistema de consenso
            setupConsensusPanel(mainPanel);

            // Centralizar na tela
            frame.setLocationRelativeTo(null);
        }

        private void setupHeader(JPanel parent) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(bgColor);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            // Título
            JLabel titleLabel = new JLabel("🧠 Algoritmos de Decisão Avançados");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            headerPanel.add(titleLabel, BorderLayout.WEST);

            // Controles
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            controlPanel.setBackground(bgColor);

            // Badge de algoritmos ativos
            long activeCount = algorithms.stream().filter(DecisionAlgorithm::isActive).count();
            JLabel statusLabel = new JLabel("🔧 " + activeCount + " Ativos");
            statusLabel.setForeground(successColor);
            statusLabel.setFont(new Font("Arial", Font.BOLD, 10));
            controlPanel.add(statusLabel);

            // Botão executar
            executeBtn = new JButton("⚡ Executar Decisão");
            executeBtn.setBackground(primaryColor);
            executeBtn.setForeground(Color.WHITE);
            executeBtn.setFocusPainted(false);
            executeBtn.setBorderPainted(false);
            executeBtn.addActionListener(e -> runDecisionProcess());
            controlPanel.add(executeBtn);

            headerPanel.add(controlPanel, BorderLayout.EAST);

            parent.add(headerPanel, BorderLayout.NORTH);
        }

        private void setupMetricsPanel(JPanel parent) {
            JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
            metricsPanel.setBackground(bgColor);
            metricsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                "📊 Métricas do Sistema",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12)
            ));

            // Criar métricas
            createMetricCard(metricsPanel, "decisions_today", "Decisões Hoje", 
                String.valueOf(currentDecisions.get()), infoColor);
            createMetricCard(metricsPanel, "success_rate", "Taxa Sucesso Média", 
                calculateAvgSuccessRate() + "%", successColor);
            createMetricCard(metricsPanel, "response_time", "Tempo Resposta", 
                calculateAvgResponseTime() + "s", primaryColor);
            createMetricCard(metricsPanel, "system_load", "Carga Sistema", 
                String.format("%.1f%%", systemLoad.get()), warningColor);

            parent.add(metricsPanel, BorderLayout.CENTER);
        }

        private void createMetricCard(JPanel parent, String key, String label, String value, Color color) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
            valueLabel.setForeground(color);
            card.add(valueLabel, BorderLayout.CENTER);

            JLabel descLabel = new JLabel(label);
            descLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            card.add(descLabel, BorderLayout.SOUTH);

            parent.add(card);
            metricsLabels.put(key, valueLabel);
        }

        private void setupTabbedPane(JPanel parent) {
            tabbedPane = new JTabbedPane();
            tabbedPane.setBackground(Color.WHITE);

            // Aba Algoritmos
            JPanel algorithmsPanel = createAlgorithmsTab();
            tabbedPane.addTab("🤖 Algoritmos", algorithmsPanel);

            // Aba Fluxo
            JPanel flowPanel = createFlowTab();
            tabbedPane.addTab("🔀 Fluxo de Decisão", flowPanel);

            // Aba Decisões
            JPanel decisionsPanel = createDecisionsTab();
            tabbedPane.addTab("📈 Decisões Recentes", decisionsPanel);

            parent.add(tabbedPane, BorderLayout.CENTER);
        }

        private JPanel createAlgorithmsTab() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(bgColor);

            // Painel com scroll
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(bgColor);

            JPanel algorithmsList = new JPanel();
            algorithmsList.setLayout(new BoxLayout(algorithmsList, BoxLayout.Y_AXIS));
            algorithmsList.setBackground(bgColor);

            // Criar cards para cada algoritmo
            for (DecisionAlgorithm algo : algorithms) {
                JPanel card = createAlgorithmCard(algo);
                algorithmsList.add(card);
                algorithmsList.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            scrollPane.setViewportView(algorithmsList);
            scrollPane.getViewport().setBackground(bgColor);
            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }

        private JPanel createAlgorithmCard(DecisionAlgorithm algo) {
            JPanel card = new JPanel(new BorderLayout(10, 10));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            // Painel superior com título e status
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel(algo.getName() + " (" + algo.getType().getValue() + ")");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            topPanel.add(titleLabel, BorderLayout.WEST);

            JLabel statusLabel = new JLabel(algo.isActive() ? "● ATIVO" : "● INATIVO");
            statusLabel.setForeground(algo.isActive() ? successColor : Color.GRAY);
            statusLabel.setFont(new Font("Arial", Font.BOLD, 10));
            topPanel.add(statusLabel, BorderLayout.EAST);

            card.add(topPanel, BorderLayout.NORTH);

            // Descrição
            JTextArea descArea = new JTextArea(algo.getDescription());
            descArea.setWrapStyleWord(true);
            descArea.setLineWrap(true);
            descArea.setEditable(false);
            descArea.setBackground(Color.WHITE);
            descArea.setFont(new Font("Arial", Font.PLAIN, 11));
            descArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
            card.add(descArea, BorderLayout.CENTER);

            // Painel de métricas
            JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 20, 10));
            metricsPanel.setBackground(Color.WHITE);

            createMetricBar(metricsPanel, "Precisão", algo.getAccuracy(), successColor);
            createMetricBar(metricsPanel, "Velocidade", algo.getSpeed(), primaryColor);
            createMetricBar(metricsPanel, "Complexidade", algo.getComplexity(), warningColor);
            createMetricBar(metricsPanel, "Confiança", algo.getConfidence(), infoColor);

            card.add(metricsPanel, BorderLayout.SOUTH);

            // Especialização
            if (!algo.getSpecialization().isEmpty()) {
                JLabel specLabel = new JLabel("Especialização: " + String.join(", ", algo.getSpecialization()));
                specLabel.setFont(new Font("Arial", Font.ITALIC, 10));
                specLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                card.add(specLabel, BorderLayout.SOUTH);
            }

            // Estatísticas
            JLabel statsLabel = new JLabel(String.format("Decisões: %d | Taxa Sucesso: %.1f%% | Tempo Médio: %.2fs",
                algo.getDecisions(), algo.getSuccessRate(), algo.getAvgResponseTime()));
            statsLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            card.add(statsLabel, BorderLayout.SOUTH);

            // Salvar referências para atualização
            Map<String, JComponent> widgets = new HashMap<>();
            widgets.put("status", statusLabel);
            algorithmWidgets.put(algo.getId(), widgets);

            return card;
        }

        private void createMetricBar(JPanel parent, String label, double value, Color color) {
            JPanel panel = new JPanel(new BorderLayout(5, 2));
            panel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(label + ": " + String.format("%.1f%%", value));
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.add(nameLabel, BorderLayout.NORTH);

            ProgressBar progressBar = new ProgressBar(value, color);
            panel.add(progressBar, BorderLayout.CENTER);

            parent.add(panel);
        }

        private JPanel createFlowTab() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(bgColor);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Título
            JLabel titleLabel = new JLabel("🔀 Fluxo de Processamento de Decisão");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            panel.add(titleLabel, BorderLayout.NORTH);

            // Painel de fluxo
            JPanel flowPanel = new JPanel();
            flowPanel.setLayout(new BoxLayout(flowPanel, BoxLayout.Y_AXIS));
            flowPanel.setBackground(bgColor);

            // Criar nós do fluxo
            flowWidgets.clear();
            for (int i = 0; i < decisionFlow.size(); i++) {
                JPanel nodePanel = createFlowNode(decisionFlow.get(i), i);
                flowPanel.add(nodePanel);
                if (i < decisionFlow.size() - 1) {
                    flowPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }

            JScrollPane scrollPane = new JScrollPane(flowPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(bgColor);
            scrollPane.getViewport().setBackground(bgColor);
            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }

        private JPanel createFlowNode(DecisionNode node, int index) {
            JPanel nodePanel = new JPanel(new BorderLayout(10, 0));
            nodePanel.setBackground(Color.WHITE);
            nodePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Ícone de status
            JLabel iconLabel = new JLabel(node.getStatus().getIcon());
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            nodePanel.add(iconLabel, BorderLayout.WEST);

            // Número do nó
            JLabel numLabel = new JLabel(String.valueOf(index + 1));
            numLabel.setOpaque(true);
            numLabel.setBackground(primaryColor);
            numLabel.setForeground(Color.WHITE);
            numLabel.setFont(new Font("Arial", Font.BOLD, 10));
            numLabel.setHorizontalAlignment(SwingConstants.CENTER);
            numLabel.setPreferredSize(new Dimension(25, 25));
            numLabel.setBorder(BorderFactory.createEmptyBorder());
            nodePanel.add(numLabel, BorderLayout.WEST);

            // Informações
            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(node.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            infoPanel.add(nameLabel);

            JLabel ioLabel = new JLabel(node.getInput() + " → " + node.getOutput());
            ioLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            infoPanel.add(ioLabel);

            nodePanel.add(infoPanel, BorderLayout.CENTER);

            // Métricas
            JPanel metricsPanel = new JPanel(new GridLayout(2, 1));
            metricsPanel.setBackground(Color.WHITE);

            String confText = node.getConfidence() > 0 ? String.format("%.1f%%", node.getConfidence()) : "-";
            JLabel confLabel = new JLabel(confText);
            confLabel.setForeground(infoColor);
            confLabel.setFont(new Font("Arial", Font.BOLD, 12));
            confLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            metricsPanel.add(confLabel);

            String timeText = node.getExecutionTime() > 0 ? node.getExecutionTime() + "s" : "-";
            JLabel timeLabel = new JLabel(timeText);
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            metricsPanel.add(timeLabel);

            nodePanel.add(metricsPanel, BorderLayout.EAST);

            // Salvar referências
            Map<String, JComponent> widgets = new HashMap<>();
            widgets.put("icon", iconLabel);
            widgets.put("confidence", confLabel);
            flowWidgets.add(widgets);

            return nodePanel;
        }

        private JPanel createDecisionsTab() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(bgColor);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Título
            JLabel titleLabel = new JLabel("📈 Decisões Recentes");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            panel.add(titleLabel, BorderLayout.NORTH);

            // Painel de decisões
            decisionsPanel = new JPanel();
            decisionsPanel.setLayout(new BoxLayout(decisionsPanel, BoxLayout.Y_AXIS));
            decisionsPanel.setBackground(bgColor);

            // Criar cards de decisões
            for (MarketDecision decision : recentDecisions) {
                JPanel decisionCard = createDecisionCard(decision);
                decisionsPanel.add(decisionCard);
                decisionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            JScrollPane scrollPane = new JScrollPane(decisionsPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(bgColor);
            scrollPane.getViewport().setBackground(bgColor);
            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }

        private JPanel createDecisionCard(MarketDecision decision) {
            JPanel card = new JPanel(new BorderLayout(10, 10));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            // Título
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(Color.WHITE);

            String title = decision.getDecision().getIcon() + " " + decision.getDecision().getValue() + 
                          " - " + String.format("%.1f%% confiança", decision.getConfidence());
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            titleLabel.setForeground(decision.getDecision().getColor());
            titlePanel.add(titleLabel, BorderLayout.WEST);

            JLabel timeLabel = new JLabel(decision.getTimestamp());
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            timeLabel.setForeground(Color.GRAY);
            titlePanel.add(timeLabel, BorderLayout.EAST);

            card.add(titlePanel, BorderLayout.NORTH);

            // Algoritmo
            JLabel algoLabel = new JLabel("Algoritmo: " + decision.getAlgorithm());
            algoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            algoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            card.add(algoLabel, BorderLayout.CENTER);

            // Raciocínio
            JTextArea reasoningArea = new JTextArea(decision.getReasoning());
            reasoningArea.setWrapStyleWord(true);
            reasoningArea.setLineWrap(true);
            reasoningArea.setEditable(false);
            reasoningArea.setBackground(Color.WHITE);
            reasoningArea.setFont(new Font("Arial", Font.PLAIN, 11));
            card.add(reasoningArea, BorderLayout.CENTER);

            // Fatores
            if (!decision.getFactors().isEmpty()) {
                JLabel factorsLabel = new JLabel("Fatores: " + String.join(" | ", decision.getFactors()));
                factorsLabel.setFont(new Font("Arial", Font.ITALIC, 10));
                factorsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
                card.add(factorsLabel, BorderLayout.CENTER);
            }

            // Métricas
            JPanel metricsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            metricsPanel.setBackground(Color.WHITE);
            metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JLabel riskLabel = new JLabel("Risco: " + decision.getRisk() + "%");
            riskLabel.setForeground(errorColor);
            riskLabel.setFont(new Font("Arial", Font.BOLD, 11));
            metricsPanel.add(riskLabel);

            JLabel returnLabel = new JLabel("Retorno: " + decision.getExpectedReturn() + "%");
            returnLabel.setForeground(successColor);
            returnLabel.setFont(new Font("Arial", Font.BOLD, 11));
            metricsPanel.add(returnLabel);

            JLabel timeframeLabel = new JLabel("Timeframe: " + decision.getTimeframe());
            timeframeLabel.setForeground(infoColor);
            timeframeLabel.setFont(new Font("Arial", Font.BOLD, 11));
            metricsPanel.add(timeframeLabel);

            card.add(metricsPanel, BorderLayout.SOUTH);

            return card;
        }

        private void setupConsensusPanel(JPanel parent) {
            JPanel consensusPanel = new JPanel(new BorderLayout());
            consensusPanel.setBackground(Color.WHITE);
            consensusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(229, 231, 235)),
                    "🔗 Sistema de Consenso Algorítmico",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION,
                    new Font("Arial", Font.BOLD, 12)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            // Descrição
            JTextArea descArea = new JTextArea(
                "O sistema combina decisões de múltiplos algoritmos usando votação ponderada " +
                "e análise de consenso para produzir decisões mais robustas e confiáveis."
            );
            descArea.setWrapStyleWord(true);
            descArea.setLineWrap(true);
            descArea.setEditable(false);
            descArea.setBackground(Color.WHITE);
            descArea.setFont(new Font("Arial", Font.PLAIN, 11));
            descArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            consensusPanel.add(descArea, BorderLayout.NORTH);

            // Métricas de consenso
            JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
            metricsPanel.setBackground(Color.WHITE);

            createConsensusMetric(metricsPanel, "89.7%", "Consenso Atual", infoColor);
            long activeCount = algorithms.stream().filter(DecisionAlgorithm::isActive).count();
            createConsensusMetric(metricsPanel, String.valueOf(activeCount), "Algoritmos Ativos", successColor);
            createConsensusMetric(metricsPanel, "0.31s", "Tempo Consenso", primaryColor);

            consensusPanel.add(metricsPanel, BorderLayout.CENTER);

            parent.add(consensusPanel, BorderLayout.SOUTH);
        }

        private void createConsensusMetric(JPanel parent, String value, String label, Color color) {
            JPanel panel = new JPanel(new GridLayout(2, 1));
            panel.setBackground(Color.WHITE);

            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
            valueLabel.setForeground(color);
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(valueLabel);

            JLabel descLabel = new JLabel(label);
            descLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            descLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(descLabel);

            parent.add(panel);
        }

        private String calculateAvgSuccessRate() {
            List<DecisionAlgorithm> active = algorithms.stream()
                .filter(DecisionAlgorithm::isActive)
                .collect(Collectors.toList());
            
            if (active.isEmpty()) return "0.0";
            
            double avg = active.stream()
                .mapToDouble(DecisionAlgorithm::getSuccessRate)
                .average()
                .orElse(0.0);
            
            return String.format("%.1f", avg);
        }

        private String calculateAvgResponseTime() {
            List<DecisionAlgorithm> active = algorithms.stream()
                .filter(DecisionAlgorithm::isActive)
                .collect(Collectors.toList());
            
            if (active.isEmpty()) return "0.00";
            
            double avg = active.stream()
                .mapToDouble(DecisionAlgorithm::getAvgResponseTime)
                .average()
                .orElse(0.0);
            
            return String.format("%.2f", avg);
        }

        private void runDecisionProcess() {
            if (isProcessing.get()) return;

            isProcessing.set(true);
            executeBtn.setText("🔄 Processando...");
            executeBtn.setEnabled(false);

            // Executar em thread separada
            new Thread(this::decisionProcessWorker).start();
        }

        private void decisionProcessWorker() {
            // Simular processo de decisão
            Random random = new Random();

            for (int i = 0; i < decisionFlow.size(); i++) {
                try {
                    Thread.sleep(800); // Simular tempo de processamento

                    // Atualizar status dos nós
                    for (int j = 0; j < decisionFlow.size(); j++) {
                        DecisionNode node = decisionFlow.get(j);
                        if (j == i) {
                            node.setStatus(NodeStatus.PROCESSING);
                            node.setConfidence(Math.min(100, node.getConfidence() + random.nextDouble() * 10 + 5));
                        } else if (j < i) {
                            node.setStatus(NodeStatus.COMPLETED);
                            if (node.getConfidence() == 0) {
                                node.setConfidence(85 + random.nextDouble() * 14);
                                node.setExecutionTime(0.1 + random.nextDouble() * 0.4);
                            }
                        } else {
                            node.setStatus(NodeStatus.WAITING);
                        }
                    }

                    // Atualizar UI na thread principal
                    SwingUtilities.invokeLater(this::updateFlowDisplay);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Finalizar todos os nós
            for (DecisionNode node : decisionFlow) {
                node.setStatus(NodeStatus.COMPLETED);
                if (node.getConfidence() == 0) {
                    node.setConfidence(85 + random.nextDouble() * 14);
                    node.setExecutionTime(0.1 + random.nextDouble() * 0.4);
                }
            }

            SwingUtilities.invokeLater(this::finishDecisionProcess);
        }

        private void finishDecisionProcess() {
            isProcessing.set(false);
            executeBtn.setText("⚡ Executar Decisão");
            executeBtn.setEnabled(true);
            updateFlowDisplay();
            JOptionPane.showMessageDialog(frame, 
                "Processo de decisão executado com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        }

        private void updateFlowDisplay() {
            for (int i = 0; i < decisionFlow.size() && i < flowWidgets.size(); i++) {
                DecisionNode node = decisionFlow.get(i);
                Map<String, JComponent> widgets = flowWidgets.get(i);

                JLabel iconLabel = (JLabel) widgets.get("icon");
                if (iconLabel != null) {
                    iconLabel.setText(node.getStatus().getIcon());
                }

                JLabel confLabel = (JLabel) widgets.get("confidence");
                if (confLabel != null) {
                    String confText = node.getConfidence() > 0 ? 
                        String.format("%.1f%%", node.getConfidence()) : "-";
                    confLabel.setText(confText);
                }
            }
        }

        private void startUpdateThread() {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            
            scheduler.scheduleAtFixedRate(() -> {
                // Atualizar métricas
                currentDecisions.addAndGet(random.nextInt(4));
                systemLoad.updateAndGet(v -> Math.max(30, Math.min(95, v + (Math.random() * 6 - 3))));

                // Atualizar confiança dos nós em processamento
                for (DecisionNode node : decisionFlow) {
                    if (node.getStatus() == NodeStatus.PROCESSING) {
                        node.setConfidence(Math.min(100, node.getConfidence() + Math.random() * 2));
                    }
                }

                // Atualizar UI
                SwingUtilities.invokeLater(() -> {
                    updateMetricsDisplay();
                    updateFlowDisplay();
                });

            }, 2, 2, TimeUnit.SECONDS);
        }

        private void updateMetricsDisplay() {
            JLabel decisionsLabel = metricsLabels.get("decisions_today");
            if (decisionsLabel != null) {
                decisionsLabel.setText(String.valueOf(currentDecisions.get()));
            }

            JLabel successLabel = metricsLabels.get("success_rate");
            if (successLabel != null) {
                successLabel.setText(calculateAvgSuccessRate() + "%");
            }

            JLabel responseLabel = metricsLabels.get("response_time");
            if (responseLabel != null) {
                responseLabel.setText(calculateAvgResponseTime() + "s");
            }

            JLabel loadLabel = metricsLabels.get("system_load");
            if (loadLabel != null) {
                loadLabel.setText(String.format("%.1f%%", systemLoad.get()));
            }
        }

        private Random random = new Random();

        public void show() {
            frame.setVisible(true);
        }
    }

    // ==================== FUNÇÃO PRINCIPAL ====================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            AdvancedDecisionAlgorithmsApp app = new AdvancedDecisionAlgorithmsApp();
            app.show();
        });
    }
}