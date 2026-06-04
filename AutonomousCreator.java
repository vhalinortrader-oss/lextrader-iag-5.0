import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Autonomous Creator - Interface Gráfica para Criação Autônoma de Estratégias
 * Sistema de inteligência de enxame para criação de técnicas de trading
 */
public class AutonomousCreator extends JFrame {
    
    // Cores do tema
    private static final Color BG_PRIMARY = new Color(10, 10, 10);
    private static final Color BG_SECONDARY = new Color(26, 26, 46);
    private static final Color BG_TERTIARY = new Color(30, 58, 95);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(204, 204, 204);
    private static final Color TEXT_MUTED = new Color(102, 102, 102);
    private static final Color ACCENT_BLUE = new Color(96, 165, 250);
    private static final Color ACCENT_GREEN = new Color(52, 211, 153);
    private static final Color ACCENT_YELLOW = new Color(251, 191, 36);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    
    // Componentes da UI
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel tabPanel;
    private JPanel contentPanel;
    private JButton createButton;
    private JProgressBar creationProgressBar;
    private JLabel creationStageLabel;
    private JLabel creationDescriptionLabel;
    
    // Estado da aplicação
    private String activeTab = "swarm";
    private AtomicBoolean isCreating = new AtomicBoolean(false);
    private CreationProcess currentCreation;
    private double autonomyLevel = 94.2;
    private double creativityIndex = 89.7;
    private MarketRegime regime = MarketRegime.ACCUMULATION;
    
    // Dados
    private List<SwarmAgent> agents;
    private List<NeuralTechnique> techniques;
    private List<MemoryEngram> apexItems;
    private NeuralEvolution evolution;
    private List<CreationProcess> creationStages;
    private List<Map<String, Object>> techniqueTemplates;
    
    // Thread de atualização
    private Timer updateTimer;
    
    public AutonomousCreator() {
        super("Criador Autônomo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initializeData();
        setupUI();
        startBackgroundUpdates();
    }
    
    private void initializeData() {
        // Inicializa agentes
        agents = new CopyOnWriteArrayList<>();
        agents.add(new SwarmAgent("1", "Alpha Hunter", "MOMENTUM", "HUNTING", 0.87));
        agents.add(new SwarmAgent("2", "Quantum Arbitrage", "ARBITRAGE", "EXECUTING", 0.92));
        agents.add(new SwarmAgent("3", "Mean Reversion Bot", "MEAN_REVERSION", "LEARNING", 0.76));
        agents.add(new SwarmAgent("4", "Sentiment Analyzer", "SENTIMENT", "IDLE", 0.68));
        
        // Inicializa técnicas
        techniques = new CopyOnWriteArrayList<>();
        techniques.add(new NeuralTechnique(
            "1",
            "Fusão HiperMomento v1.2",
            "Combina momento multi-timeframe com análise fractal.",
            87.3,
            92.1,
            18.7,
            23.4,
            TechniqueStatus.EM_USO,
            LocalDateTime.now(),
            Arrays.asList("Momento Adaptativo", "Fractais", "Detecção de Regime"),
            new Performance(73.2, 2.4, 8.1, 1.85)
        ));
        techniques.add(new NeuralTechnique(
            "2",
            "Motor de Arbitragem Quântica",
            "Usa princípios quânticos para arbitragem de alta frequência.",
            94.1,
            87.9,
            22.3,
            19.8,
            TechniqueStatus.TESTANDO,
            LocalDateTime.now(),
            Arrays.asList("Estados Quânticos", "Superposição", "Multi-Ativo"),
            new Performance(68.9, 3.1, 6.7, 2.12)
        ));
        
        // Inicializa itens Apex
        apexItems = new CopyOnWriteArrayList<>();
        
        // Inicializa evolução
        evolution = new NeuralEvolution(127, 50, 0.847, 0.623, 0.15, 0.75);
        
        // Templates de técnicas
        techniqueTemplates = new CopyOnWriteArrayList<>();
        
        Map<String, Object> template1 = new HashMap<>();
        template1.put("name", "Quantum Micro-Scalper (Curto Prazo)");
        template1.put("description", "Estratégia de alta frequência focada em explorar micro-volatilidade e divergências de RSI em janelas de segundos.");
        template1.put("components", Arrays.asList("Flash Order Execution", "RSI Extremo", "Micro-Tendência", "Fluxo de Ordens L2"));
        template1.put("innovation_level", 92);
        techniqueTemplates.add(template1);
        
        Map<String, Object> template2 = new HashMap<>();
        template2.put("name", "Neural Trend Surfer (Médio Prazo)");
        template2.put("description", "Swing trade clássico aprimorado por redes neurais para capturar tendências de dias ou semanas.");
        template2.put("components", Arrays.asList("Cruzamento MA Adaptativo", "Filtro de Ruído Quântico", "Sentimento Social", "Ondas de Elliott"));
        template2.put("innovation_level", 88);
        techniqueTemplates.add(template2);
        
        Map<String, Object> template3 = new HashMap<>();
        template3.put("name", "Deep Value Accumulator (Longo Prazo)");
        template3.put("description", "Algoritmo de position trading baseado em dados fundamentais on-chain e ciclos de halving.");
        template3.put("components", Arrays.asList("Análise On-Chain Glassnode", "Múltiplo de Mayer", "Reserva de Valor", "Ciclos Macro"));
        template3.put("innovation_level", 95);
        techniqueTemplates.add(template3);
        
        // Estágios de criação
        creationStages = new CopyOnWriteArrayList<>();
        creationStages.add(new CreationProcess("Análise de Padrão", 0, "Identificando novos padrões em dados históricos", 3000));
        creationStages.add(new CreationProcess("Síntese Neural", 0, "Combinando elementos de diferentes estratégias", 4000));
        creationStages.add(new CreationProcess("Otimização Genética", 0, "Aplicando algoritmos genéticos para refinar a técnica", 5000));
        creationStages.add(new CreationProcess("Simulação Monte Carlo", 0, "Testando robustez em múltiplos cenários", 6000));
        creationStages.add(new CreationProcess("Validação Cruzada", 0, "Verificando consistência em diferentes timeframes", 4000));
        creationStages.add(new CreationProcess("Implementação", 0, "Preparando técnica para implantação operacional", 2000));
    }
    
    private void setupUI() {
        // Configuração principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_PRIMARY);
        setContentPane(mainPanel);
        
        setupHeader();
        setupTabs();
        
        // Área de conteúdo
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_PRIMARY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Mostrar conteúdo inicial
        showTabContent();
    }
    
    private void setupHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(26, 26, 46));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Título
        JLabel titleLabel = new JLabel("CRIADOR AUTÔNOMO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Estatísticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        statsPanel.setBackground(new Color(26, 26, 46));
        
        // Autonomia
        JPanel autonomyPanel = createStatsPanel(
            String.format("Autonomia: %.1f%%", autonomyLevel),
            ACCENT_BLUE,
            new Color(30, 58, 95)
        );
        statsPanel.add(autonomyPanel);
        
        // Criatividade
        JPanel creativityPanel = createStatsPanel(
            String.format("Criatividade: %.1f%%", creativityIndex),
            ACCENT_GREEN,
            new Color(6, 78, 59)
        );
        statsPanel.add(creativityPanel);
        
        headerPanel.add(statsPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private JPanel createStatsPanel(String text, Color fgColor, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Courier", Font.PLAIN, 10));
        label.setForeground(fgColor);
        panel.add(label, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupTabs() {
        tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabPanel.setBackground(BG_PRIMARY);
        
        String[][] tabs = {
            {"swarm", "Inteligência de Enxame"},
            {"creation", "Criação Ativa"},
            {"techniques", "Biblioteca de Técnicas"},
            {"evolution", "Evolução Neural"},
            {"apex", "Cofre Apex (100%)"}
        };
        
        for (String[] tab : tabs) {
            JButton tabButton = new JButton(tab[1]);
            tabButton.setActionCommand(tab[0]);
            tabButton.setFont(new Font("Arial", Font.PLAIN, 10));
            tabButton.setForeground(activeTab.equals(tab[0]) ? ACCENT_BLUE : TEXT_SECONDARY);
            tabButton.setBackground(activeTab.equals(tab[0]) ? BG_TERTIARY : new Color(26, 26, 26));
            tabButton.setBorderPainted(false);
            tabButton.setFocusPainted(false);
            tabButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tabButton.setMargin(new Insets(10, 20, 10, 20));
            
            tabButton.addActionListener(e -> {
                activeTab = e.getActionCommand();
                showTabContent();
                
                // Atualizar cores dos botões
                for (Component comp : tabPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        JButton btn = (JButton) comp;
                        btn.setForeground(
                            btn.getActionCommand().equals(activeTab) ? 
                            ACCENT_BLUE : TEXT_SECONDARY
                        );
                        btn.setBackground(
                            btn.getActionCommand().equals(activeTab) ? 
                            BG_TERTIARY : new Color(26, 26, 26)
                        );
                    }
                }
            });
            
            tabPanel.add(tabButton);
        }
        
        mainPanel.add(tabPanel, BorderLayout.NORTH);
    }
    
    private void showTabContent() {
        contentPanel.removeAll();
        
        switch (activeTab) {
            case "swarm":
                showSwarmTab();
                break;
            case "creation":
                showCreationTab();
                break;
            case "techniques":
                showTechniquesTab();
                break;
            case "evolution":
                showEvolutionTab();
                break;
            case "apex":
                showApexTab();
                break;
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showSwarmTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_PRIMARY);
        
        // Regime de mercado
        Map<String, Object> regimeStyle = getRegimeStyle(regime.name());
        
        JPanel regimePanel = new JPanel(new BorderLayout());
        regimePanel.setBackground((Color) regimeStyle.get("bg"));
        regimePanel.setBorder(BorderFactory.createLineBorder(
            (Color) regimeStyle.get("border"), 2
        ));
        regimePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel regimeLabel = new JLabel("Regime de Mercado Detectado");
        regimeLabel.setFont(new Font("Courier", Font.PLAIN, 9));
        regimeLabel.setForeground(TEXT_MUTED);
        regimePanel.add(regimeLabel, BorderLayout.NORTH);
        
        JLabel regimeValue = new JLabel(regime.name().replace("_", " "));
        regimeValue.setFont(new Font("Arial", Font.BOLD, 24));
        regimeValue.setForeground((Color) regimeStyle.get("color"));
        regimePanel.add(regimeValue, BorderLayout.CENTER);
        
        mainPanel.add(regimePanel, BorderLayout.NORTH);
        
        // Agentes
        JPanel agentsPanel = new JPanel();
        agentsPanel.setLayout(new BoxLayout(agentsPanel, BoxLayout.Y_AXIS));
        agentsPanel.setBackground(BG_PRIMARY);
        agentsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        for (SwarmAgent agent : agents) {
            agentsPanel.add(createAgentPanel(agent));
            agentsPanel.add(Box.createVerticalStrut(5));
        }
        
        JScrollPane scrollPane = new JScrollPane(agentsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_PRIMARY);
        scrollPane.getViewport().setBackground(BG_PRIMARY);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(mainPanel);
    }
    
    private JPanel createAgentPanel(SwarmAgent agent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BG_SECONDARY.brighter(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_SECONDARY);
        
        // Tipo
        Map<String, Color[]> typeColors = getTypeColors(agent.getType());
        
        JLabel typeLabel = new JLabel(agent.getType().substring(0, 1));
        typeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        typeLabel.setForeground(typeColors.get("fg")[0]);
        typeLabel.setBackground(typeColors.get("bg")[0]);
        typeLabel.setOpaque(true);
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        typeLabel.setPreferredSize(new Dimension(30, 30));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        headerPanel.add(typeLabel, BorderLayout.WEST);
        
        // Nome e ID
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(BG_SECONDARY);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JLabel nameLabel = new JLabel(agent.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 11));
        nameLabel.setForeground(TEXT_PRIMARY);
        infoPanel.add(nameLabel);
        
        JLabel idLabel = new JLabel(agent.getId());
        idLabel.setFont(new Font("Courier", Font.PLAIN, 8));
        idLabel.setForeground(TEXT_MUTED);
        infoPanel.add(idLabel);
        
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Status
        Map<String, Color[]> statusColors = getStatusColors(agent.getStatus());
        
        JLabel statusLabel = new JLabel(agent.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 8));
        statusLabel.setForeground(statusColors.get("fg")[0]);
        statusLabel.setBackground(statusColors.get("bg")[0]);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Barra de confiança
        JPanel confidencePanel = new JPanel(new BorderLayout());
        confidencePanel.setBackground(BG_SECONDARY);
        confidencePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel confidenceLabel = new JLabel("Confiança");
        confidenceLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        confidenceLabel.setForeground(TEXT_SECONDARY);
        confidencePanel.add(confidenceLabel, BorderLayout.WEST);
        
        JLabel confidenceValue = new JLabel(String.format("%.0f%%", agent.getConfidence() * 100));
        confidenceValue.setFont(new Font("Courier", Font.PLAIN, 9));
        confidenceValue.setForeground(TEXT_PRIMARY);
        confidencePanel.add(confidenceValue, BorderLayout.EAST);
        
        panel.add(confidencePanel, BorderLayout.CENTER);
        
        // Barra de progresso
        JPanel progressBarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Fundo
                g2d.setColor(new Color(55, 65, 81));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Progresso
                g2d.setColor(ACCENT_BLUE);
                int width = (int) (getWidth() * agent.getConfidence());
                g2d.fillRect(0, 0, width, getHeight());
            }
        };
        progressBarPanel.setPreferredSize(new Dimension(100, 6));
        progressBarPanel.setBackground(new Color(55, 65, 81));
        
        panel.add(progressBarPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void showCreationTab() {
        JPanel mainPanel = new JPanel(null); // Layout absoluto
        mainPanel.setBackground(BG_PRIMARY);
        
        // Botão central
        createButton = new JButton(
            isCreating.get() ? "SINTETIZANDO CAMINHOS NEURAIS..." : "LIBERAR CRIATIVIDADE NEURAL"
        );
        createButton.setFont(new Font("Arial", Font.BOLD, 12));
        createButton.setForeground(TEXT_PRIMARY);
        createButton.setBackground(isCreating.get() ? new Color(55, 65, 81) : new Color(14, 165, 233));
        createButton.setBorderPainted(false);
        createButton.setFocusPainted(false);
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.setEnabled(!isCreating.get());
        createButton.setSize(createButton.getPreferredSize());
        
        createButton.addActionListener(e -> handleCreateTechnique());
        
        mainPanel.add(createButton);
        
        // Progresso da criação
        if (isCreating.get() && currentCreation != null) {
            JPanel progressPanel = new JPanel();
            progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
            progressPanel.setBackground(BG_SECONDARY);
            progressPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BG_SECONDARY.brighter(), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            // Estágio
            creationStageLabel = new JLabel(currentCreation.getStage());
            creationStageLabel.setFont(new Font("Arial", Font.BOLD, 11));
            creationStageLabel.setForeground(ACCENT_BLUE);
            creationStageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            progressPanel.add(creationStageLabel);
            
            progressPanel.add(Box.createVerticalStrut(10));
            
            // Barra de progresso
            creationProgressBar = new JProgressBar(0, 100);
            creationProgressBar.setValue(currentCreation.getProgress());
            creationProgressBar.setForeground(new Color(14, 165, 233));
            creationProgressBar.setBackground(new Color(55, 65, 81));
            creationProgressBar.setStringPainted(true);
            creationProgressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
            progressPanel.add(creationProgressBar);
            
            progressPanel.add(Box.createVerticalStrut(5));
            
            // Descrição
            creationDescriptionLabel = new JLabel(currentCreation.getDescription());
            creationDescriptionLabel.setFont(new Font("Courier", Font.ITALIC, 9));
            creationDescriptionLabel.setForeground(TEXT_MUTED);
            creationDescriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            progressPanel.add(creationDescriptionLabel);
            
            progressPanel.setSize(600, 150);
            mainPanel.add(progressPanel);
        }
        
        // Posicionamento após adicionar componentes
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                createButton.setLocation(
                    (mainPanel.getWidth() - createButton.getWidth()) / 2,
                    (mainPanel.getHeight() - createButton.getHeight()) / 2
                );
                
                if (isCreating.get() && currentCreation != null) {
                    Component[] comps = mainPanel.getComponents();
                    for (Component comp : comps) {
                        if (comp instanceof JPanel && comp != createButton) {
                            comp.setLocation(
                                (mainPanel.getWidth() - comp.getWidth()) / 2,
                                (mainPanel.getHeight() - comp.getHeight()) / 2 + 100
                            );
                        }
                    }
                }
            }
        });
        
        contentPanel.add(mainPanel);
    }
    
    private void showTechniquesTab() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_PRIMARY);
        
        for (NeuralTechnique tech : techniques) {
            mainPanel.add(createTechniquePanel(tech));
            mainPanel.add(Box.createVerticalStrut(5));
        }
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_PRIMARY);
        scrollPane.getViewport().setBackground(BG_PRIMARY);
        
        contentPanel.add(scrollPane);
    }
    
    private JPanel createTechniquePanel(NeuralTechnique tech) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BG_SECONDARY.brighter(), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_SECONDARY);
        
        JLabel nameLabel = new JLabel(tech.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setForeground(new Color(229, 229, 229));
        headerPanel.add(nameLabel, BorderLayout.WEST);
        
        // Status
        Map<String, Color[]> statusColors = getTechniqueStatusColors(tech.getStatus());
        
        JLabel statusLabel = new JLabel(tech.getStatus().getValue());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 8));
        statusLabel.setForeground(statusColors.get("fg")[0]);
        statusLabel.setBackground(statusColors.get("bg")[0]);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Descrição
        JLabel descLabel = new JLabel("<html>" + tech.getDescription() + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        descLabel.setForeground(TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(descLabel);
        
        panel.add(Box.createVerticalStrut(15));
        
        // Métricas
        JPanel metricsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        metricsPanel.setBackground(BG_SECONDARY);
        
        // Inovação
        metricsPanel.add(createMetricLabel(
            String.format("Inovação: %.1f%%", tech.getInnovationLevel()),
            ACCENT_YELLOW
        ));
        
        // Lucratividade
        metricsPanel.add(createMetricLabel(
            String.format("Lucratividade: %.1f%%", tech.getProfitability()),
            ACCENT_GREEN
        ));
        
        // Risco
        metricsPanel.add(createMetricLabel(
            String.format("Risco: %.1f%%", tech.getRiskLevel()),
            ACCENT_RED
        ));
        
        panel.add(metricsPanel);
        
        return panel;
    }
    
    private JLabel createMetricLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Courier", Font.PLAIN, 9));
        label.setForeground(color);
        return label;
    }
    
    private void showEvolutionTab() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(BG_PRIMARY);
        
        // Progresso Genético
        JPanel geneticPanel = createEvolutionPanel(
            "Progresso Genético",
            new String[][]{
                {"Geração Atual", String.valueOf(evolution.getGeneration())},
                {"Melhor Fitness", String.format("%.2f%%", evolution.getBestFitness() * 100)},
                {"Fitness Médio", String.format("%.2f%%", evolution.getAvgFitness() * 100)}
            }
        );
        mainPanel.add(geneticPanel);
        
        // Parâmetros Evolucionários
        JPanel paramsPanel = createEvolutionPanel(
            "Parâmetros Evolucionários",
            new String[][]{
                {"Taxa de Mutação", String.format("%.0f%%", evolution.getMutationRate() * 100)},
                {"Taxa de Crossover", String.format("%.0f%%", evolution.getCrossoverRate() * 100)},
                {"Tamanho da População", String.valueOf(evolution.getPopulationSize())}
            }
        );
        
        // Adicionar barra de mutação
        JPanel mutationBarPanel = new JPanel(new BorderLayout());
        mutationBarPanel.setBackground(BG_SECONDARY);
        mutationBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel barPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                g2d.setColor(new Color(55, 65, 81));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(ACCENT_PURPLE);
                int width = (int) (getWidth() * evolution.getMutationRate());
                g2d.fillRect(0, 0, width, getHeight());
            }
        };
        barPanel.setPreferredSize(new Dimension(100, 8));
        barPanel.setBackground(new Color(55, 65, 81));
        
        mutationBarPanel.add(barPanel, BorderLayout.CENTER);
        
        paramsPanel.add(mutationBarPanel);
        
        mainPanel.add(paramsPanel);
        
        contentPanel.add(mainPanel);
    }
    
    private JPanel createEvolutionPanel(String title, String[][] stats) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BG_SECONDARY.brighter(), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(20));
        
        for (String[] stat : stats) {
            JPanel statPanel = new JPanel(new BorderLayout());
            statPanel.setBackground(BG_SECONDARY);
            
            JLabel nameLabel = new JLabel(stat[0]);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            nameLabel.setForeground(TEXT_MUTED);
            statPanel.add(nameLabel, BorderLayout.WEST);
            
            JLabel valueLabel = new JLabel(stat[1]);
            valueLabel.setFont(new Font("Courier", Font.BOLD, 16));
            valueLabel.setForeground("Geração Atual".equals(stat[0]) ? TEXT_PRIMARY :
                                    "Melhor Fitness".equals(stat[0]) ? ACCENT_GREEN : TEXT_SECONDARY);
            statPanel.add(valueLabel, BorderLayout.EAST);
            
            panel.add(statPanel);
            panel.add(Box.createVerticalStrut(10));
        }
        
        return panel;
    }
    
    private void showApexTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_PRIMARY);
        
        // Cabeçalho Apex
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(69, 26, 3));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_YELLOW, 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel apexTitle = new JLabel("COFRE APEX: ESTRATÉGIAS 100% EFICAZES");
        apexTitle.setFont(new Font("Arial", Font.BOLD, 16));
        apexTitle.setForeground(ACCENT_YELLOW);
        apexTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(apexTitle, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        if (apexItems.isEmpty()) {
            // Mensagem quando não há itens
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(BG_PRIMARY);
            emptyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 81), 2),
                BorderFactory.createEmptyBorder(50, 50, 50, 50)
            ));
            
            JLabel emptyLabel = new JLabel("NENHUM PROTOCOLO APEX ENCONTRADO");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyLabel.setForeground(TEXT_MUTED);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(emptyLabel);
            
            emptyPanel.add(Box.createVerticalStrut(10));
            
            JLabel emptySubLabel = new JLabel(
                "<html><center>A Rede Neural ainda está aprendendo.<br>Continue operando para gerar perfeição.</center></html>"
            );
            emptySubLabel.setFont(new Font("Courier", Font.PLAIN, 10));
            emptySubLabel.setForeground(new Color(68, 68, 68));
            emptySubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(emptySubLabel);
            
            emptyPanel.add(Box.createVerticalStrut(20));
            
            JButton simulateButton = new JButton("Simular Descoberta de Padrão Mestre");
            simulateButton.setFont(new Font("Arial", Font.PLAIN, 10));
            simulateButton.setForeground(ACCENT_YELLOW);
            simulateButton.setBackground(new Color(69, 26, 3));
            simulateButton.setBorderPainted(false);
            simulateButton.setFocusPainted(false);
            simulateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            simulateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            simulateButton.setMargin(new Insets(10, 20, 10, 20));
            
            simulateButton.addActionListener(e -> handleSimulateApex());
            
            emptyPanel.add(simulateButton);
            
            mainPanel.add(emptyPanel, BorderLayout.CENTER);
        } else {
            // Lista de itens Apex
            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBackground(BG_PRIMARY);
            itemsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            
            for (MemoryEngram apex : apexItems) {
                JPanel apexPanel = new JPanel();
                apexPanel.setLayout(new BoxLayout(apexPanel, BoxLayout.Y_AXIS));
                apexPanel.setBackground(BG_SECONDARY);
                apexPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_YELLOW, 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
                
                JLabel verifiedLabel = new JLabel("PROTOCOLO VERIFICADO");
                verifiedLabel.setFont(new Font("Courier", Font.BOLD, 9));
                verifiedLabel.setForeground(ACCENT_YELLOW);
                verifiedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                apexPanel.add(verifiedLabel);
                
                apexPanel.add(Box.createVerticalStrut(5));
                
                JLabel nameLabel = new JLabel(apex.getPatternName().replace("[APEX] ", ""));
                nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
                nameLabel.setForeground(TEXT_PRIMARY);
                nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                apexPanel.add(nameLabel);
                
                itemsPanel.add(apexPanel);
                itemsPanel.add(Box.createVerticalStrut(5));
            }
            
            JScrollPane scrollPane = new JScrollPane(itemsPanel);
            scrollPane.setBorder(null);
            scrollPane.setBackground(BG_PRIMARY);
            scrollPane.getViewport().setBackground(BG_PRIMARY);
            
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        contentPanel.add(mainPanel);
    }
    
    private void handleCreateTechnique() {
        if (isCreating.get()) return;
        
        isCreating.set(true);
        if (createButton != null) {
            createButton.setText("SINTETIZANDO CAMINHOS NEURAIS...");
            createButton.setEnabled(false);
            createButton.setBackground(new Color(55, 65, 81));
        }
        
        // Executar em thread separada
        new Thread(this::createTechniqueThread).start();
    }
    
    private void createTechniqueThread() {
        Random random = new Random();
        Map<String, Object> template = techniqueTemplates.get(random.nextInt(techniqueTemplates.size()));
        
        for (CreationProcess stage : creationStages) {
            currentCreation = new CreationProcess(
                stage.getStage(),
                0,
                stage.getDescription(),
                stage.getDuration()
            );
            
            // Atualizar UI
            SwingUtilities.invokeLater(this::showTabContent);
            
            int steps = 20;
            long stepDuration = stage.getDuration() / steps;
            
            for (int i = 0; i <= steps; i++) {
                try {
                    Thread.sleep(stepDuration);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                if (currentCreation != null) {
                    currentCreation.setProgress(i * 5);
                    
                    // Atualizar barra de progresso
                    SwingUtilities.invokeLater(() -> {
                        if (creationProgressBar != null) {
                            creationProgressBar.setValue(currentCreation.getProgress());
                        }
                        if (creationStageLabel != null) {
                            creationStageLabel.setText(currentCreation.getStage());
                        }
                        if (creationDescriptionLabel != null) {
                            creationDescriptionLabel.setText(currentCreation.getDescription());
                        }
                    });
                }
            }
        }
        
        // Criar nova técnica
        NeuralTechnique newTechnique = new NeuralTechnique(
            String.valueOf(System.currentTimeMillis()),
            String.format("%s v%d.%d", 
                template.get("name"),
                random.nextInt(9) + 1,
                random.nextInt(10)
            ),
            (String) template.get("description"),
            ((Number) template.get("innovation_level")).doubleValue() + (random.nextDouble() - 0.5) * 10,
            70 + random.nextDouble() * 25,
            10 + random.nextDouble() * 20,
            15 + random.nextDouble() * 25,
            TechniqueStatus.APROVADA,
            LocalDateTime.now(),
            (List<String>) template.get("components"),
            new Performance(
                55 + random.nextDouble() * 25,
                1 + random.nextDouble() * 3,
                3 + random.nextDouble() * 12,
                1 + random.nextDouble() * 1.5
            )
        );
        
        techniques.add(0, newTechnique);
        isCreating.set(false);
        currentCreation = null;
        
        // Atualizar UI
        SwingUtilities.invokeLater(() -> {
            if (createButton != null) {
                createButton.setText("LIBERAR CRIATIVIDADE NEURAL");
                createButton.setEnabled(true);
                createButton.setBackground(new Color(14, 165, 233));
            }
            showTabContent();
        });
    }
    
    private void handleSimulateApex() {
        Random random = new Random();
        
        MemoryEngram newApex = new MemoryEngram(
            String.format("[APEX] Padrão de Eficiência %d", 100 + random.nextInt(900)),
            0.95 + random.nextDouble() * 0.05,
            LocalDateTime.now()
        );
        
        apexItems.add(newApex);
        showTabContent();
        
        JOptionPane.showMessageDialog(
            this,
            "Padrão Apex descoberto com sucesso!",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private Map<String, Object> getRegimeStyle(String regime) {
        Map<String, Object> style = new HashMap<>();
        
        if (regime.contains("BULL")) {
            style.put("color", ACCENT_GREEN);
            style.put("border", ACCENT_GREEN);
            style.put("bg", new Color(22, 101, 52));
        } else if (regime.contains("BEAR")) {
            style.put("color", ACCENT_RED);
            style.put("border", ACCENT_RED);
            style.put("bg", new Color(153, 27, 27));
        } else if (regime.contains("VOLATILITY")) {
            style.put("color", ACCENT_PURPLE);
            style.put("border", ACCENT_PURPLE);
            style.put("bg", new Color(76, 29, 149));
        } else {
            style.put("color", new Color(156, 163, 175));
            style.put("border", new Color(107, 114, 128));
            style.put("bg", new Color(55, 65, 81));
        }
        
        return style;
    }
    
    private Map<String, Color[]> getTypeColors(String type) {
        Map<String, Color[]> colors = new HashMap<>();
        
        switch (type) {
            case "MOMENTUM":
                colors.put("bg", new Color[]{new Color(22, 101, 52)});
                colors.put("fg", new Color[]{ACCENT_GREEN});
                break;
            case "ARBITRAGE":
                colors.put("bg", new Color[]{new Color(76, 29, 149)});
                colors.put("fg", new Color[]{ACCENT_PURPLE});
                break;
            case "MEAN_REVERSION":
                colors.put("bg", new Color[]{new Color(133, 77, 14)});
                colors.put("fg", new Color[]{ACCENT_YELLOW});
                break;
            default:
                colors.put("bg", new Color[]{new Color(55, 65, 81)});
                colors.put("fg", new Color[]{new Color(156, 163, 175)});
        }
        
        return colors;
    }
    
    private Map<String, Color[]> getStatusColors(String status) {
        Map<String, Color[]> colors = new HashMap<>();
        
        switch (status) {
            case "EXECUTING":
                colors.put("bg", new Color[]{new Color(22, 101, 52)});
                colors.put("fg", new Color[]{ACCENT_GREEN});
                break;
            case "HUNTING":
                colors.put("bg", new Color[]{new Color(133, 77, 14)});
                colors.put("fg", new Color[]{ACCENT_YELLOW});
                break;
            case "LEARNING":
                colors.put("bg", new Color[]{new Color(30, 64, 175)});
                colors.put("fg", new Color[]{ACCENT_BLUE});
                break;
            default:
                colors.put("bg", new Color[]{new Color(55, 65, 81)});
                colors.put("fg", new Color[]{new Color(156, 163, 175)});
        }
        
        return colors;
    }
    
    private Map<String, Color[]> getTechniqueStatusColors(TechniqueStatus status) {
        Map<String, Color[]> colors = new HashMap<>();
        
        switch (status) {
            case EM_USO:
                colors.put("bg", new Color[]{new Color(22, 101, 52)});
                colors.put("fg", new Color[]{ACCENT_GREEN});
                break;
            case TESTANDO:
                colors.put("bg", new Color[]{new Color(30, 58, 138)});
                colors.put("fg", new Color[]{ACCENT_BLUE});
                break;
            case APROVADA:
                colors.put("bg", new Color[]{new Color(6, 95, 70)});
                colors.put("fg", new Color[]{new Color(16, 185, 129)});
                break;
            case CRIANDO:
                colors.put("bg", new Color[]{new Color(133, 77, 14)});
                colors.put("fg", new Color[]{ACCENT_YELLOW});
                break;
            case DESCARTADA:
                colors.put("bg", new Color[]{new Color(153, 27, 27)});
                colors.put("fg", new Color[]{ACCENT_RED});
                break;
            default:
                colors.put("bg", new Color[]{new Color(55, 65, 81)});
                colors.put("fg", new Color[]{new Color(156, 163, 175)});
        }
        
        return colors;
    }
    
    private void startBackgroundUpdates() {
        updateTimer = new Timer(8000, e -> {
            // Atualizar evolução
            evolution.setGeneration(evolution.getGeneration() + 1);
            evolution.setBestFitness(Math.min(1, evolution.getBestFitness() + (new Random().nextDouble() - 0.4) * 0.005));
            evolution.setAvgFitness(Math.min(0.9, evolution.getAvgFitness() + (new Random().nextDouble() - 0.45) * 0.003));
            
            // Atualizar níveis
            autonomyLevel = Math.max(85, Math.min(99, autonomyLevel + (new Random().nextDouble() - 0.5) * 0.5));
            creativityIndex = Math.max(80, Math.min(97, creativityIndex + (new Random().nextDouble() - 0.5) * 0.7));
            
            // Atualizar header
            if (headerPanel != null) {
                SwingUtilities.invokeLater(this::setupHeader);
            }
            
            // Atualizar UI se estiver na aba de evolução
            if ("evolution".equals(activeTab)) {
                SwingUtilities.invokeLater(this::showTabContent);
            }
        });
        
        updateTimer.start();
    }
    
    // ==================== CLASSES DE DADOS ====================
    
    enum TechniqueStatus {
        CRIANDO("CRIANDO"),
        TESTANDO("TESTANDO"),
        APROVADA("APROVADA"),
        EM_USO("EM_USO"),
        DESCARTADA("DESCARTADA");
        
        private final String value;
        
        TechniqueStatus(String value) {
            this.value = value;
        }
        
        public String getValue() { return value; }
    }
    
    enum MarketRegime {
        ACCUMULATION("ACCUMULATION"),
        BULL("BULL"),
        BEAR("BEAR"),
        VOLATILITY("VOLATILITY");
        
        private final String value;
        
        MarketRegime(String value) {
            this.value = value;
        }
        
        public String getValue() { return value; }
    }
    
    static class Performance {
        private final double winRate;
        private final double avgReturn;
        private final double maxDrawdown;
        private final double sharpeRatio;
        
        public Performance(double winRate, double avgReturn, double maxDrawdown, double sharpeRatio) {
            this.winRate = winRate;
            this.avgReturn = avgReturn;
            this.maxDrawdown = maxDrawdown;
            this.sharpeRatio = sharpeRatio;
        }
        
        public double getWinRate() { return winRate; }
        public double getAvgReturn() { return avgReturn; }
        public double getMaxDrawdown() { return maxDrawdown; }
        public double getSharpeRatio() { return sharpeRatio; }
    }
    
    static class NeuralTechnique {
        private final String id;
        private final String name;
        private final String description;
        private final double innovationLevel;
        private final double backtestScore;
        private final double profitability;
        private final double riskLevel;
        private final TechniqueStatus status;
        private final LocalDateTime createdAt;
        private final List<String> components;
        private final Performance performance;
        
        public NeuralTechnique(String id, String name, String description,
                              double innovationLevel, double backtestScore,
                              double profitability, double riskLevel,
                              TechniqueStatus status, LocalDateTime createdAt,
                              List<String> components, Performance performance) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.innovationLevel = innovationLevel;
            this.backtestScore = backtestScore;
            this.profitability = profitability;
            this.riskLevel = riskLevel;
            this.status = status;
            this.createdAt = createdAt;
            this.components = new ArrayList<>(components);
            this.performance = performance;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getInnovationLevel() { return innovationLevel; }
        public double getBacktestScore() { return backtestScore; }
        public double getProfitability() { return profitability; }
        public double getRiskLevel() { return riskLevel; }
        public TechniqueStatus getStatus() { return status; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public List<String> getComponents() { return new ArrayList<>(components); }
        public Performance getPerformance() { return performance; }
    }
    
    static class CreationProcess {
        private final String stage;
        private int progress;
        private final String description;
        private final int duration;
        
        public CreationProcess(String stage, int progress, String description, int duration) {
            this.stage = stage;
            this.progress = progress;
            this.description = description;
            this.duration = duration;
        }
        
        public String getStage() { return stage; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getDescription() { return description; }
        public int getDuration() { return duration; }
    }
    
    static class SwarmAgent {
        private final String id;
        private final String name;
        private final String type;
        private final String status;
        private final double confidence;
        
        public SwarmAgent(String id, String name, String type, String status, double confidence) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.confidence = confidence;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public double getConfidence() { return confidence; }
    }
    
    static class MemoryEngram {
        private final String patternName;
        private final double confidence;
        private final LocalDateTime createdAt;
        
        public MemoryEngram(String patternName, double confidence, LocalDateTime createdAt) {
            this.patternName = patternName;
            this.confidence = confidence;
            this.createdAt = createdAt;
        }
        
        public String getPatternName() { return patternName; }
        public double getConfidence() { return confidence; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
    
    static class NeuralEvolution {
        private int generation;
        private final int populationSize;
        private double bestFitness;
        private double avgFitness;
        private double mutationRate;
        private double crossoverRate;
        
        public NeuralEvolution(int generation, int populationSize, double bestFitness,
                              double avgFitness, double mutationRate, double crossoverRate) {
            this.generation = generation;
            this.populationSize = populationSize;
            this.bestFitness = bestFitness;
            this.avgFitness = avgFitness;
            this.mutationRate = mutationRate;
            this.crossoverRate = crossoverRate;
        }
        
        public int getGeneration() { return generation; }
        public void setGeneration(int generation) { this.generation = generation; }
        public int getPopulationSize() { return populationSize; }
        public double getBestFitness() { return bestFitness; }
        public void setBestFitness(double bestFitness) { this.bestFitness = bestFitness; }
        public double getAvgFitness() { return avgFitness; }
        public void setAvgFitness(double avgFitness) { this.avgFitness = avgFitness; }
        public double getMutationRate() { return mutationRate; }
        public void setMutationRate(double mutationRate) { this.mutationRate = mutationRate; }
        public double getCrossoverRate() { return crossoverRate; }
        public void setCrossoverRate(double crossoverRate) { this.crossoverRate = crossoverRate; }
    }
    
    // ==================== MÉTODO MAIN ====================
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            AutonomousCreator creator = new AutonomousCreator();
            creator.setVisible(true);
        });
    }
}