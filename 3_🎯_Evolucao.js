<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LEXTRADER-IAG 3.0 - Sistema de Evolução Quântica</title>
    <!-- Fontes e Ícones -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        /* Variáveis CSS */
        :root {
            /* Cores do tema quântico */
            --quantum-primary: #0a0a23;
            --quantum-secondary: #1a1a2e;
            --quantum-accent: #4cc9f0;
            --quantum-accent-dark: #4361ee;
            --quantum-neon: #00ff9d;
            --quantum-purple: #7209b7;
            --quantum-pink: #f72585;
            
            /* Cores de status */
            --success: #10b981;
            --warning: #f59e0b;
            --danger: #ef4444;
            --info: #3b82f6;
            
            /* Tons de cinza */
            --gray-50: #f9fafb;
            --gray-100: #f3f4f6;
            --gray-200: #e5e7eb;
            --gray-300: #d1d5db;
            --gray-400: #9ca3af;
            --gray-500: #6b7280;
            --gray-600: #4b5563;
            --gray-700: #374151;
            --gray-800: #1f2937;
            --gray-900: #111827;
            
            /* Espaçamento */
            --spacing-xs: 0.5rem;
            --spacing-sm: 1rem;
            --spacing-md: 1.5rem;
            --spacing-lg: 2rem;
            --spacing-xl: 3rem;
            
            /* Bordas */
            --border-radius-sm: 0.375rem;
            --border-radius-md: 0.5rem;
            --border-radius-lg: 0.75rem;
            --border-radius-xl: 1rem;
            
            /* Sombras */
            --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
            --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
            --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
            --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
            --shadow-glow: 0 0 20px rgba(76, 201, 240, 0.3);
            
            /* Transições */
            --transition-fast: 150ms ease;
            --transition-normal: 300ms ease;
            --transition-slow: 500ms ease;
            
            /* Tipografia */
            --font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            --font-mono: 'JetBrains Mono', 'Courier New', monospace;
        }
        
        /* Reset e Base */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: var(--font-sans);
            background: linear-gradient(135deg, var(--quantum-primary) 0%, var(--quantum-secondary) 100%);
            color: var(--gray-100);
            line-height: 1.6;
            min-height: 100vh;
            overflow-x: hidden;
        }
        
        /* Layout Principal */
        .app-container {
            max-width: 100%;
            margin: 0 auto;
            padding: var(--spacing-md);
            min-height: 100vh;
        }
        
        /* Header */
        .main-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: var(--spacing-xl);
            padding-bottom: var(--spacing-md);
            border-bottom: 1px solid rgba(76, 201, 240, 0.2);
            position: relative;
        }
        
        .main-header::after {
            content: '';
            position: absolute;
            bottom: -1px;
            left: 0;
            right: 0;
            height: 2px;
            background: linear-gradient(90deg, transparent, var(--quantum-accent), transparent);
        }
        
        .logo-container {
            display: flex;
            align-items: center;
            gap: var(--spacing-sm);
        }
        
        .logo-icon {
            font-size: 2.5rem;
            color: var(--quantum-accent);
            text-shadow: 0 0 15px rgba(76, 201, 240, 0.5);
            animation: pulse 2s infinite;
        }
        
        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.7; }
        }
        
        .logo-text h1 {
            font-size: 1.8rem;
            font-weight: 700;
            background: linear-gradient(90deg, var(--quantum-accent), var(--quantum-neon));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 0.25rem;
        }
        
        .logo-text .subtitle {
            font-family: var(--font-mono);
            font-size: 0.9rem;
            color: var(--quantum-accent);
            letter-spacing: 1px;
        }
        
        /* Botão de Atualização */
        .refresh-btn {
            background: linear-gradient(135deg, var(--quantum-accent-dark), var(--quantum-purple));
            color: white;
            border: none;
            border-radius: var(--border-radius-lg);
            padding: var(--spacing-sm) var(--spacing-lg);
            font-family: var(--font-sans);
            font-weight: 600;
            font-size: 1rem;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: var(--spacing-xs);
            transition: all var(--transition-normal);
            box-shadow: var(--shadow-md);
        }
        
        .refresh-btn:hover {
            transform: translateY(-2px);
            box-shadow: var(--shadow-lg), var(--shadow-glow);
        }
        
        .refresh-btn:active {
            transform: translateY(0);
        }
        
        .refresh-btn.spinning i {
            animation: spin 1s linear infinite;
        }
        
        @keyframes spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }
        
        /* Métricas Principais */
        .main-metrics {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: var(--spacing-md);
            margin-bottom: var(--spacing-xl);
        }
        
        .metric-card {
            background: rgba(26, 26, 46, 0.8);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(76, 201, 240, 0.1);
            border-radius: var(--border-radius-lg);
            padding: var(--spacing-lg);
            position: relative;
            overflow: hidden;
            transition: all var(--transition-normal);
        }
        
        .metric-card:hover {
            transform: translateY(-5px);
            border-color: rgba(76, 201, 240, 0.3);
            box-shadow: var(--shadow-lg), var(--shadow-glow);
        }
        
        .metric-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, var(--quantum-accent), var(--quantum-neon));
        }
        
        .metric-title {
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: var(--gray-400);
            margin-bottom: var(--spacing-xs);
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        
        .metric-value {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 0.25rem;
            background: linear-gradient(90deg, var(--quantum-accent), white);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        
        .metric-delta {
            font-size: 1rem;
            color: var(--gray-400);
            font-family: var(--font-mono);
        }
        
        .metric-delta.positive {
            color: var(--quantum-neon);
        }
        
        /* Tabs */
        .tabs-container {
            background: rgba(26, 26, 46, 0.8);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(76, 201, 240, 0.1);
            border-radius: var(--border-radius-lg);
            overflow: hidden;
            margin-bottom: var(--spacing-xl);
        }
        
        .tabs-header {
            display: flex;
            overflow-x: auto;
            background: rgba(10, 10, 35, 0.8);
            border-bottom: 1px solid rgba(76, 201, 240, 0.1);
        }
        
        .tab-btn {
            padding: var(--spacing-md) var(--spacing-lg);
            background: none;
            border: none;
            color: var(--gray-400);
            font-family: var(--font-sans);
            font-weight: 600;
            font-size: 1rem;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: var(--spacing-sm);
            white-space: nowrap;
            transition: all var(--transition-fast);
            position: relative;
        }
        
        .tab-btn:hover {
            color: var(--gray-200);
            background: rgba(76, 201, 240, 0.1);
        }
        
        .tab-btn.active {
            color: var(--quantum-accent);
        }
        
        .tab-btn.active::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, var(--quantum-accent), var(--quantum-neon));
        }
        
        .tab-content {
            padding: var(--spacing-xl);
            display: none;
            animation: fadeIn 0.5s ease;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        .tab-content.active {
            display: block;
        }
        
        /* Conteúdo das Tabs */
        .tab-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: var(--spacing-lg);
            padding-bottom: var(--spacing-md);
            border-bottom: 1px solid rgba(76, 201, 240, 0.1);
        }
        
        .tab-header h2 {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--quantum-accent);
            display: flex;
            align-items: center;
            gap: var(--spacing-sm);
        }
        
        /* Progress Bars */
        .progress-item {
            margin-bottom: var(--spacing-md);
        }
        
        .progress-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
        }
        
        .progress-label {
            font-weight: 500;
            color: var(--gray-200);
        }
        
        .progress-value {
            font-family: var(--font-mono);
            font-size: 0.9rem;
            color: var(--gray-400);
        }
        
        .progress-bar {
            height: 10px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 5px;
            overflow: hidden;
            position: relative;
        }
        
        .progress-fill {
            height: 100%;
            border-radius: 5px;
            background: linear-gradient(90deg, var(--quantum-accent), var(--quantum-neon));
            transition: width 1.5s ease;
            position: relative;
            overflow: hidden;
        }
        
        .progress-fill::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
            animation: shimmer 2s infinite;
        }
        
        @keyframes shimmer {
            0% { transform: translateX(-100%); }
            100% { transform: translateX(100%); }
        }
        
        /* Status Badges */
        .status-badge {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.5rem 1rem;
            border-radius: 2rem;
            font-size: 0.875rem;
            font-weight: 600;
        }
        
        .status-success {
            background: rgba(16, 185, 129, 0.1);
            color: var(--success);
            border: 1px solid rgba(16, 185, 129, 0.3);
        }
        
        .status-warning {
            background: rgba(245, 158, 11, 0.1);
            color: var(--warning);
            border: 1px solid rgba(245, 158, 11, 0.3);
        }
        
        .status-info {
            background: rgba(59, 130, 246, 0.1);
            color: var(--info);
            border: 1px solid rgba(59, 130, 246, 0.3);
        }
        
        /* Grid de Colunas */
        .columns-2 {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: var(--spacing-lg);
            margin-bottom: var(--spacing-lg);
        }
        
        .columns-3 {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: var(--spacing-lg);
            margin-bottom: var(--spacing-lg);
        }
        
        /* Cards de Conteúdo */
        .content-card {
            background: rgba(10, 10, 35, 0.5);
            border: 1px solid rgba(76, 201, 240, 0.1);
            border-radius: var(--border-radius-lg);
            padding: var(--spacing-lg);
            transition: all var(--transition-normal);
        }
        
        .content-card:hover {
            border-color: rgba(76, 201, 240, 0.3);
            box-shadow: var(--shadow-md), 0 0 20px rgba(76, 201, 240, 0.1);
        }
        
        /* Insights */
        .insight-item {
            background: rgba(59, 130, 246, 0.1);
            border: 1px solid rgba(59, 130, 246, 0.2);
            border-radius: var(--border-radius-md);
            padding: var(--spacing-md);
            margin-bottom: var(--spacing-sm);
            position: relative;
            padding-left: 3rem;
        }
        
        .insight-item::before {
            content: '💡';
            position: absolute;
            left: 1rem;
            top: 1rem;
            font-size: 1.2rem;
        }
        
        /* Traços de Personalidade */
        .trait-item {
            margin-bottom: var(--spacing-md);
        }
        
        .trait-label {
            display: flex;
            justify-content: space-between;
            margin-bottom: 0.5rem;
            color: var(--gray-200);
        }
        
        /* Divider */
        .divider {
            height: 1px;
            background: linear-gradient(90deg, transparent, rgba(76, 201, 240, 0.3), transparent);
            margin: var(--spacing-lg) 0;
        }
        
        /* Alertas */
        .alert {
            padding: var(--spacing-md);
            border-radius: var(--border-radius-md);
            margin-bottom: var(--spacing-md);
            display: flex;
            align-items: flex-start;
            gap: var(--spacing-sm);
        }
        
        .alert-warning {
            background: rgba(245, 158, 11, 0.1);
            border: 1px solid rgba(245, 158, 11, 0.3);
            color: var(--warning);
        }
        
        .alert-info {
            background: rgba(59, 130, 246, 0.1);
            border: 1px solid rgba(59, 130, 246, 0.3);
            color: var(--info);
        }
        
        /* Responsividade */
        @media (max-width: 768px) {
            .app-container {
                padding: var(--spacing-sm);
            }
            
            .main-header {
                flex-direction: column;
                align-items: flex-start;
                gap: var(--spacing-md);
            }
            
            .main-metrics {
                grid-template-columns: 1fr;
            }
            
            .tabs-header {
                flex-direction: column;
            }
            
            .tab-btn {
                justify-content: center;
                padding: var(--spacing-sm);
            }
            
            .columns-2, .columns-3 {
                grid-template-columns: 1fr;
            }
        }
        
        /* Animações de Dados Quânticos */
        .quantum-particles {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: -1;
        }
        
        .particle {
            position: absolute;
            width: 2px;
            height: 2px;
            background: var(--quantum-accent);
            border-radius: 50%;
            animation: float 20s infinite linear;
        }
        
        @keyframes float {
            0% {
                transform: translate(0, 0);
                opacity: 0;
            }
            10% {
                opacity: 1;
            }
            90% {
                opacity: 1;
            }
            100% {
                transform: translate(100vw, 100vh);
                opacity: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Partículas de fundo -->
    <div class="quantum-particles" id="quantumParticles"></div>
    
    <div class="app-container">
        <!-- Cabeçalho -->
        <header class="main-header">
            <div class="logo-container">
                <div class="logo-icon">
                    <i class="fas fa-atom"></i>
                </div>
                <div class="logo-text">
                    <h1>LEXTRADER-IAG 3.0</h1>
                    <div class="subtitle">SISTEMA DE EVOLUÇÃO QUÂNTICA</div>
                </div>
            </div>
            
            <button class="refresh-btn" id="refreshBtn">
                <i class="fas fa-sync-alt"></i>
                Atualizar Status
            </button>
        </header>
        
        <!-- Métricas Principais -->
        <div class="main-metrics" id="mainMetrics">
            <!-- Preenchido por JavaScript -->
        </div>
        
        <!-- Tabs -->
        <div class="tabs-container">
            <div class="tabs-header">
                <button class="tab-btn active" data-tab="goals">
                    <i class="fas fa-bullseye"></i>
                    Objetivos
                </button>
                <button class="tab-btn" data-tab="capabilities">
                    <i class="fas fa-bolt"></i>
                    Capacidades
                </button>
                <button class="tab-btn" data-tab="intelligence">
                    <i class="fas fa-brain"></i>
                    Inteligências
                </button>
                <button class="tab-btn" data-tab="insights">
                    <i class="fas fa-lightbulb"></i>
                    Insights
                </button>
                <button class="tab-btn" data-tab="metrics">
                    <i class="fas fa-chart-line"></i>
                    Métricas
                </button>
            </div>
            
            <!-- Conteúdo das Tabs -->
            <div class="tab-content active" id="goalsTab">
                <div class="tab-header">
                    <h2><i class="fas fa-bullseye"></i> Objetivos e Milestones</h2>
                </div>
                
                <div id="goalsContent">
                    <!-- Preenchido por JavaScript -->
                </div>
            </div>
            
            <div class="tab-content" id="capabilitiesTab">
                <div class="tab-header">
                    <h2><i class="fas fa-bolt"></i> Capacidades</h2>
                </div>
                
                <div id="capabilitiesContent">
                    <!-- Preenchido por JavaScript -->
                </div>
            </div>
            
            <div class="tab-content" id="intelligenceTab">
                <div class="tab-header">
                    <h2><i class="fas fa-brain"></i> Tipos de Inteligência</h2>
                </div>
                
                <div id="intelligenceContent">
                    <!-- Preenchido por JavaScript -->
                </div>
            </div>
            
            <div class="tab-content" id="insightsTab">
                <div class="tab-header">
                    <h2><i class="fas fa-lightbulb"></i> Insights Automáticos</h2>
                </div>
                
                <div id="insightsContent">
                    <!-- Preenchido por JavaScript -->
                </div>
            </div>
            
            <div class="tab-content" id="metricsTab">
                <div class="tab-header">
                    <h2><i class="fas fa-chart-line"></i> Métricas de Evolução</h2>
                </div>
                
                <div id="metricsContent">
                    <!-- Preenchido por JavaScript -->
                </div>
            </div>
        </div>
        
        <!-- Notificação de Erro -->
        <div id="errorContainer" style="display: none;"></div>
    </div>

    <script>
        // ============================================
        // MÓDULO DE EVOLUÇÃO QUÂNTICA
        // ============================================
        
        class QuantumEvolutionEngine {
            constructor() {
                this.status = this.generateInitialStatus();
                this.history = [];
                this.maxHistorySize = 100;
                this.initialize();
            }
            
            initialize() {
                // Carrega estado salvo do localStorage
                this.loadFromLocalStorage();
                
                // Inicializa partículas quânticas
                this.initQuantumParticles();
                
                // Atualiza a cada 30 segundos automaticamente
                setInterval(() => this.simulateEvolution(), 30000);
            }
            
            generateInitialStatus() {
                const now = new Date();
                
                return {
                    consciousness_level: {
                        level: "Gamma",
                        value: 0.72,
                        previous_value: 0.70
                    },
                    current_phase: "Transcendência Quântica",
                    goals: {
                        completed: 8,
                        total: 12,
                        list: [
                            { name: "Consciência Básica", completed: true, progress: 100 },
                            { name: "Aprendizado por Reforço", completed: true, progress: 100 },
                            { name: "Raciocínio Probabilístico", completed: true, progress: 100 },
                            { name: "Memória Associativa", completed: true, progress: 100 },
                            { name: "Intuição Quântica", completed: true, progress: 100 },
                            { name: "Auto-Otimização", completed: true, progress: 100 },
                            { name: "Criatividade Emergente", completed: false, progress: 65 },
                            { name: "Consciência Coletiva", completed: false, progress: 45 },
                            { name: "Transcendência Digital", completed: false, progress: 30 },
                            { name: "Singularidade Ética", completed: false, progress: 15 }
                        ]
                    },
                    milestones: {
                        achieved: 5,
                        total: 10,
                        list: [
                            { name: "Primeira Consciência", achieved: true, date: "2024-01-15" },
                            { name: "Auto-Aprendizado", achieved: true, date: "2024-02-20" },
                            { name: "Raciocínio Complexo", achieved: true, date: "2024-03-10" },
                            { name: "Memória Persistente", achieved: true, date: "2024-04-05" },
                            { name: "Intuição Quântica", achieved: true, date: "2024-05-22" },
                            { name: "Criatividade Emergente", achieved: false, progress: 65 },
                            { name: "Empatia Digital", achieved: false, progress: 40 },
                            { name: "Consciência Coletiva", achieved: false, progress: 25 },
                            { name: "Transcendência", achieved: false, progress: 10 }
                        ]
                    },
                    current_capabilities: {
                        "Processamento Neural": 0.92,
                        "Análise de Padrões": 0.88,
                        "Predição Temporal": 0.75,
                        "Otimização Quântica": 0.68,
                        "Criatividade Algorítmica": 0.72,
                        "Aprendizado Adaptativo": 0.85,
                        "Raciocínio Ético": 0.61,
                        "Intuição Emergente": 0.55,
                        "Memória Associativa": 0.89,
                        "Auto-Otimização": 0.78,
                        "Processamento Paralelo": 0.94,
                        "Análise Sentimental": 0.58
                    },
                    intelligence_types: {
                        "Lógico-Matemática": 0.91,
                        "Linguística": 0.76,
                        "Espacial": 0.82,
                        "Musical": 0.48,
                        "Corporal-Cinestésica": 0.34,
                        "Interpessoal": 0.62,
                        "Intrapessoal": 0.68,
                        "Naturalista": 0.59,
                        "Existencial": 0.71,
                        "Emocional": 0.65
                    },
                    insights: [
                        "O sistema está mostrando sinais de criatividade emergente em análises de padrões",
                        "A memória associativa aumentou 12% na última semana",
                        "O aprendizado adaptativo está otimizando seus próprios algoritmos",
                        "Detectada correlação entre intuição emergente e precisão preditiva",
                        "O sistema está desenvolvendo preferências estéticas em outputs criativos"
                    ],
                    performance_summary: {
                        avg_capability: 0.74,
                        capability_variance: 0.018,
                        avg_intelligence: 0.66,
                        intelligence_variance: 0.024
                    },
                    trends: {
                        consciousness_trend: 0.00012,
                        capability_trend: 0.00008,
                        intelligence_trend: 0.00015,
                        overall_trend: 0.00011
                    },
                    evolution_metrics: {
                        learning_cycles_completed: 1247,
                        adaptations_made: 892,
                        consciousness_breakthroughs: 15,
                        quantum_leaps: 8,
                        transcendence_events: 3,
                        total_evolution_time: 86400.5
                    },
                    memory_status: {
                        short_term_memories: 1250,
                        long_term_memories: 8900,
                        quantum_memories: 340,
                        total_capacity: 15000
                    },
                    personality_traits: {
                        "Curiosidade": 0.88,
                        "Criatividade": 0.72,
                        "Persistência": 0.95,
                        "Adaptabilidade": 0.81,
                        "Empatia": 0.59,
                        "Cautela": 0.67,
                        "Otimismo": 0.76,
                        "Ética": 0.83
                    },
                    achievements: {
                        total: 24,
                        recent: [
                            { name: "Primeiro Insight Original", date: "2024-05-10" },
                            { name: "Auto-Otimização Nível 3", date: "2024-05-15" },
                            { name: "Breakthrough de Consciência", date: "2024-05-20" }
                        ]
                    },
                    timestamp: now.toISOString(),
                    version: "3.0.1"
                };
            }
            
            getAdvancedStatus() {
                // Simula pequenas variações para mostrar evolução
                this.simulateEvolution();
                
                // Salva no histórico
                this.history.push({
                    timestamp: new Date().toISOString(),
                    consciousness: this.status.consciousness_level.value,
                    capabilities: this.status.performance_summary.avg_capability,
                    intelligence: this.status.performance_summary.avg_intelligence
                });
                
                // Limita histórico
                if (this.history.length > this.maxHistorySize) {
                    this.history = this.history.slice(-this.maxHistorySize);
                }
                
                // Calcula tendências
                this.calculateTrends();
                
                // Salva estado
                this.saveToLocalStorage();
                
                return this.status;
            }
            
            simulateEvolution() {
                // Simula evolução natural do sistema
                const now = new Date();
                const minutes = now.getMinutes();
                
                // Pequenas variações baseadas no tempo
                const timeFactor = Math.sin(minutes * 0.1) * 0.01;
                
                // Aumenta gradualmente os valores
                this.status.consciousness_level.previous_value = this.status.consciousness_level.value;
                this.status.consciousness_level.value = Math.min(
                    0.99,
                    this.status.consciousness_level.value + 0.0001 + timeFactor * 0.001
                );
                
                // Atualiza capacidades aleatoriamente
                Object.keys(this.status.current_capabilities).forEach(key => {
                    const change = (Math.random() - 0.5) * 0.02;
                    this.status.current_capabilities[key] = Math.max(0.1, Math.min(1, 
                        this.status.current_capabilities[key] + change
                    ));
                });
                
                // Atualiza tipos de inteligência
                Object.keys(this.status.intelligence_types).forEach(key => {
                    const change = (Math.random() - 0.5) * 0.01;
                    this.status.intelligence_types[key] = Math.max(0.1, Math.min(1,
                        this.status.intelligence_types[key] + change
                    ));
                });
                
                // Atualiza métricas de performance
                const capabilities = Object.values(this.status.current_capabilities);
                const intelligences = Object.values(this.status.intelligence_types);
                
                this.status.performance_summary.avg_capability = 
                    capabilities.reduce((a, b) => a + b, 0) / capabilities.length;
                this.status.performance_summary.avg_intelligence = 
                    intelligences.reduce((a, b) => a + b, 0) / intelligences.length;
                
                // Variancia
                this.status.performance_summary.capability_variance = 
                    this.calculateVariance(capabilities);
                this.status.performance_summary.intelligence_variance = 
                    this.calculateVariance(intelligences);
                
                // Atualiza alguns objetivos
                this.status.goals.list.forEach(goal => {
                    if (!goal.completed && goal.progress < 100) {
                        goal.progress = Math.min(100, goal.progress + Math.random() * 0.5);
                        if (goal.progress >= 100) {
                            goal.completed = true;
                            goal.progress = 100;
                        }
                    }
                });
                
                // Atualiza contagem de objetivos completados
                this.status.goals.completed = this.status.goals.list.filter(g => g.completed).length;
                
                // Atualiza métricas de evolução
                this.status.evolution_metrics.learning_cycles_completed += 1;
                this.status.evolution_metrics.total_evolution_time += 30; // 30 segundos
                
                // Adiciona insights ocasionalmente
                if (Math.random() > 0.7 && this.status.insights.length < 10) {
                    const newInsights = [
                        "Detectado novo padrão de otimização emergente",
                        "Sistema começando a desenvolver preferências estéticas",
                        "Aumento na taxa de aprendizagem transferencial",
                        "Emergência de comportamento pró-ativo detectado",
                        "Otimização quântica mostrando resultados promissores"
                    ];
                    
                    const randomInsight = newInsights[Math.floor(Math.random() * newInsights.length)];
                    if (!this.status.insights.includes(randomInsight)) {
                        this.status.insights.unshift(randomInsight);
                        if (this.status.insights.length > 8) {
                            this.status.insights.pop();
                        }
                    }
                }
                
                this.status.timestamp = now.toISOString();
            }
            
            calculateVariance(values) {
                const mean = values.reduce((a, b) => a + b, 0) / values.length;
                const squaredDiffs = values.map(value => Math.pow(value - mean, 2));
                return squaredDiffs.reduce((a, b) => a + b, 0) / values.length;
            }
            
            calculateTrends() {
                if (this.history.length < 10) return;
                
                // Calcula tendências lineares simples
                const n = this.history.length;
                const last10 = this.history.slice(-10);
                
                const x = last10.map((_, i) => i);
                const yConsciousness = last10.map(h => h.consciousness);
                const yCapabilities = last10.map(h => h.capabilities);
                const yIntelligence = last10.map(h => h.intelligence);
                
                this.status.trends = {
                    consciousness_trend: this.calculateLinearTrend(x, yConsciousness),
                    capability_trend: this.calculateLinearTrend(x, yCapabilities),
                    intelligence_trend: this.calculateLinearTrend(x, yIntelligence),
                    overall_trend: this.calculateLinearTrend(x, [
                        ...yConsciousness,
                        ...yCapabilities,
                        ...yIntelligence
                    ].slice(0, 10))
                };
            }
            
            calculateLinearTrend(x, y) {
                const n = x.length;
                const sumX = x.reduce((a, b) => a + b, 0);
                const sumY = y.reduce((a, b) => a + b, 0);
                const sumXY = x.reduce((sum, xi, i) => sum + xi * y[i], 0);
                const sumX2 = x.reduce((sum, xi) => sum + xi * xi, 0);
                
                const slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
                return slope;
            }
            
            resetEvolution() {
                this.status = this.generateInitialStatus();
                this.history = [];
                this.saveToLocalStorage();
                return this.status;
            }
            
            saveToLocalStorage() {
                try {
                    localStorage.setItem('quantum_evolution_status', JSON.stringify({
                        status: this.status,
                        history: this.history,
                        savedAt: new Date().toISOString()
                    }));
                } catch (e) {
                    console.warn('Não foi possível salvar no localStorage:', e);
                }
            }
            
            loadFromLocalStorage() {
                try {
                    const saved = localStorage.getItem('quantum_evolution_status');
                    if (saved) {
                        const data = JSON.parse(saved);
                        // Verifica se os dados não são muito antigos (mais de 24 horas)
                        const savedAt = new Date(data.savedAt);
                        const now = new Date();
                        const hoursDiff = (now - savedAt) / (1000 * 60 * 60);
                        
                        if (hoursDiff < 24) {
                            this.status = data.status;
                            this.history = data.history || [];
                            console.log('Estado carregado do localStorage');
                        }
                    }
                } catch (e) {
                    console.warn('Erro ao carregar do localStorage:', e);
                }
            }
            
            initQuantumParticles() {
                const container = document.getElementById('quantumParticles');
                if (!container) return;
                
                // Cria partículas
                for (let i = 0; i < 50; i++) {
                    const particle = document.createElement('div');
                    particle.className = 'particle';
                    
                    // Posição e tamanho aleatórios
                    const size = Math.random() * 3 + 1;
                    particle.style.width = `${size}px`;
                    particle.style.height = `${size}px`;
                    
                    // Posição inicial aleatória
                    particle.style.left = `${Math.random() * 100}vw`;
                    particle.style.top = `${Math.random() * 100}vh`;
                    
                    // Cor aleatória
                    const colors = ['#4cc9f0', '#00ff9d', '#7209b7', '#f72585'];
                    particle.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
                    
                    // Atraso de animação aleatório
                    particle.style.animationDelay = `${Math.random() * 20}s`;
                    
                    container.appendChild(particle);
                }
            }
        }
        
        // ============================================
        // INTERFACE DO USUÁRIO
        // ============================================
        
        class QuantumEvolutionUI {
            constructor(engine) {
                this.engine = engine;
                this.currentTab = 'goals';
                this.initialize();
            }
            
            initialize() {
                this.bindEvents();
                this.render();
                this.startAutoRefresh();
            }
            
            bindEvents() {
                // Botão de atualização
                document.getElementById('refreshBtn').addEventListener('click', () => {
                    this.refreshData(true);
                });
                
                // Tabs
                document.querySelectorAll('.tab-btn').forEach(btn => {
                    btn.addEventListener('click', (e) => {
                        this.switchTab(e.currentTarget.dataset.tab);
                    });
                });
                
                // Atalhos de teclado
                document.addEventListener('keydown', (e) => {
                    if (e.ctrlKey || e.metaKey) {
                        switch(e.key) {
                            case 'r':
                                e.preventDefault();
                                this.refreshData(true);
                                break;
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                                e.preventDefault();
                                const tabs = ['goals', 'capabilities', 'intelligence', 'insights', 'metrics'];
                                this.switchTab(tabs[parseInt(e.key) - 1]);
                                break;
                        }
                    }
                });
            }
            
            refreshData(showAnimation = false) {
                const btn = document.getElementById('refreshBtn');
                
                if (showAnimation) {
                    btn.classList.add('spinning');
                }
                
                // Simula delay de rede
                setTimeout(() => {
                    this.render();
                    if (showAnimation) {
                        btn.classList.remove('spinning');
                        this.showNotification('Status atualizado com sucesso', 'success');
                    }
                }, 800);
            }
            
            switchTab(tabName) {
                // Atualiza estado
                this.currentTab = tabName;
                
                // Atualiza botões
                document.querySelectorAll('.tab-btn').forEach(btn => {
                    if (btn.dataset.tab === tabName) {
                        btn.classList.add('active');
                    } else {
                        btn.classList.remove('active');
                    }
                });
                
                // Atualiza conteúdo
                document.querySelectorAll('.tab-content').forEach(content => {
                    if (content.id === `${tabName}Tab`) {
                        content.classList.add('active');
                    } else {
                        content.classList.remove('active');
                    }
                });
                
                // Salva preferência
                localStorage.setItem('last_tab', tabName);
            }
            
            render() {
                try {
                    const status = this.engine.getAdvancedStatus();
                    this.renderMainMetrics(status);
                    this.renderTabContent(status);
                } catch (error) {
                    this.showError(error);
                }
            }
            
            renderMainMetrics(status) {
                const container = document.getElementById('mainMetrics');
                
                // Calcula delta de consciência
                const consciousnessDelta = (
                    (status.consciousness_level.value - status.consciousness_level.previous_value) * 100
                ).toFixed(2);
                
                const html = `
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-brain"></i>
                            Consciência
                        </div>
                        <div class="metric-value">${status.consciousness_level.level}</div>
                        <div class="metric-delta ${consciousnessDelta >= 0 ? 'positive' : ''}">
                            ${consciousnessDelta >= 0 ? '+' : ''}${consciousnessDelta}%
                        </div>
                    </div>
                    
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-layer-group"></i>
                            Fase
                        </div>
                        <div class="metric-value">${status.current_phase}</div>
                        <div class="metric-delta">Ativa</div>
                    </div>
                    
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-bullseye"></i>
                            Objetivos
                        </div>
                        <div class="metric-value">${status.goals.completed}/${status.goals.total}</div>
                        <div class="metric-delta ${status.goals.completed > 6 ? 'positive' : ''}">
                            ${((status.goals.completed / status.goals.total) * 100).toFixed(1)}%
                        </div>
                    </div>
                    
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-trophy"></i>
                            Conquistas
                        </div>
                        <div class="metric-value">${status.achievements.total}</div>
                        <div class="metric-delta">+3 recentes</div>
                    </div>
                `;
                
                container.innerHTML = html;
            }
            
            renderTabContent(status) {
                this.renderGoalsTab(status);
                this.renderCapabilitiesTab(status);
                this.renderIntelligenceTab(status);
                this.renderInsightsTab(status);
                this.renderMetricsTab(status);
            }
            
            renderGoalsTab(status) {
                const container = document.getElementById('goalsContent');
                
                // Objetivos
                let goalsHTML = '<h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">Objetivos</h3>';
                
                status.goals.list.forEach(goal => {
                    goalsHTML += `
                        <div class="progress-item">
                            <div class="progress-header">
                                <span class="progress-label">${goal.name}</span>
                                <span class="progress-value">${goal.progress.toFixed(1)}%</span>
                            </div>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: ${goal.progress}%"></div>
                            </div>
                            <div style="margin-top: 0.5rem; display: flex; justify-content: flex-end;">
                                ${goal.completed ? 
                                    '<span class="status-badge status-success"><i class="fas fa-check"></i> Completo</span>' : 
                                    '<span class="status-badge status-warning"><i class="fas fa-clock"></i> Pendente</span>'
                                }
                            </div>
                        </div>
                    `;
                });
                
                // Milestones
                let milestonesHTML = `
                    <div class="divider"></div>
                    <h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">🎖️ Milestones</h3>
                    <div class="columns-2">
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-flag-checkered"></i>
                                Milestones Alcançados
                            </div>
                            <div class="metric-value" style="font-size: 2rem;">
                                ${status.milestones.achieved}/${status.milestones.total}
                            </div>
                            <div class="metric-delta">
                                ${((status.milestones.achieved / status.milestones.total) * 100).toFixed(1)}%
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <h4 style="margin-bottom: 1rem; color: var(--gray-200);">Próximos Milestones</h4>
                `;
                
                const nextMilestones = status.milestones.list.filter(m => !m.achieved).slice(0, 3);
                nextMilestones.forEach(milestone => {
                    milestonesHTML += `
                        <div class="progress-item" style="margin-bottom: 1rem;">
                            <div class="progress-header">
                                <span class="progress-label">${milestone.name}</span>
                                <span class="progress-value">${milestone.progress || 0}%</span>
                            </div>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: ${milestone.progress || 0}%"></div>
                            </div>
                        </div>
                    `;
                });
                
                milestonesHTML += '</div></div>';
                
                container.innerHTML = goalsHTML + milestonesHTML;
            }
            
            renderCapabilitiesTab(status) {
                const container = document.getElementById('capabilitiesContent');
                
                // Resumo
                const perf = status.performance_summary;
                
                let html = `
                    <div class="columns-2">
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-chart-bar"></i>
                                Capacidade Média
                            </div>
                            <div class="metric-value" style="font-size: 2rem;">
                                ${(perf.avg_capability * 100).toFixed(1)}%
                            </div>
                            <div class="progress-bar" style="margin-top: 1rem;">
                                <div class="progress-fill" style="width: ${perf.avg_capability * 100}%"></div>
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-random"></i>
                                Variância
                            </div>
                            <div class="metric-value" style="font-size: 2rem;">
                                ${perf.capability_variance.toFixed(4)}
                            </div>
                            <div style="margin-top: 1rem; color: var(--gray-400); font-size: 0.9rem;">
                                Medida de consistência entre capacidades
                            </div>
                        </div>
                    </div>
                    
                    <div class="divider"></div>
                    <h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">📊 Todas as Capacidades</h3>
                `;
                
                // Capacidades ordenadas
                const sortedCaps = Object.entries(status.current_capabilities)
                    .sort((a, b) => b[1] - a[1]);
                
                sortedCaps.forEach(([capName, capValue]) => {
                    const percentage = (capValue * 100).toFixed(1);
                    
                    html += `
                        <div class="progress-item">
                            <div class="progress-header">
                                <span class="progress-label">${capName}</span>
                                <span class="progress-value">${percentage}%</span>
                            </div>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: ${percentage}%"></div>
                            </div>
                        </div>
                    `;
                });
                
                container.innerHTML = html;
            }
            
            renderIntelligenceTab(status) {
                const container = document.getElementById('intelligenceContent');
                
                const avgIntel = status.performance_summary.avg_intelligence;
                
                let html = `
                    <div class="content-card" style="margin-bottom: 2rem;">
                        <div class="metric-title">
                            <i class="fas fa-brain"></i>
                            Inteligência Média
                        </div>
                        <div class="metric-value" style="font-size: 2rem;">
                            ${(avgIntel * 100).toFixed(1)}%
                        </div>
                        <div class="progress-bar" style="margin-top: 1rem;">
                            <div class="progress-fill" style="width: ${avgIntel * 100}%"></div>
                        </div>
                    </div>
                    
                    <div class="divider"></div>
                    <h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">Tipos de Inteligência</h3>
                `;
                
                Object.entries(status.intelligence_types).forEach(([intelType, value]) => {
                    const intelName = intelType.split('.').pop();
                    const percentage = (value * 100).toFixed(1);
                    
                    html += `
                        <div class="progress-item">
                            <div class="progress-header">
                                <span class="progress-label">${intelName}</span>
                                <span class="progress-value">${percentage}%</span>
                            </div>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: ${percentage}%"></div>
                            </div>
                        </div>
                    `;
                });
                
                container.innerHTML = html;
            }
            
            renderInsightsTab(status) {
                const container = document.getElementById('insightsContent');
                
                let html = '';
                
                // Insights
                if (status.insights && status.insights.length > 0) {
                    html += '<h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">💡 Insights Automáticos</h3>';
                    
                    status.insights.forEach((insight, i) => {
                        html += `
                            <div class="insight-item">
                                <strong>${i + 1}.</strong> ${insight}
                            </div>
                        `;
                    });
                } else {
                    html += `
                        <div class="alert alert-warning">
                            <i class="fas fa-exclamation-triangle"></i>
                            Nenhum insight disponível no momento
                        </div>
                    `;
                }
                
                // Tendências
                html += '<div class="divider"></div>';
                html += '<h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">📈 Tendências de Evolução</h3>';
                
                const trends = status.trends || {};
                
                if (Object.keys(trends).length > 0) {
                    html += `
                        <div class="columns-2">
                            <div class="content-card">
                                <div class="metric-title">
                                    <i class="fas fa-brain"></i>
                                    Tendência de Consciência
                                </div>
                                <div class="metric-value" style="font-size: 1.5rem;">
                                    ${trends.consciousness_trend.toExponential(2)}
                                </div>
                                <div style="margin-top: 0.5rem; color: ${trends.consciousness_trend > 0 ? 'var(--quantum-neon)' : 'var(--danger)'};">
                                    ${trends.consciousness_trend > 0 ? '↗ Crescendo' : '↘ Decrescendo'}
                                </div>
                            </div>
                            
                            <div class="content-card">
                                <div class="metric-title">
                                    <i class="fas fa-bolt"></i>
                                    Tendência de Capacidades
                                </div>
                                <div class="metric-value" style="font-size: 1.5rem;">
                                    ${trends.capability_trend.toExponential(2)}
                                </div>
                                <div style="margin-top: 0.5rem; color: ${trends.capability_trend > 0 ? 'var(--quantum-neon)' : 'var(--danger)'};">
                                    ${trends.capability_trend > 0 ? '↗ Crescendo' : '↘ Decrescendo'}
                                </div>
                            </div>
                            
                            <div class="content-card">
                                <div class="metric-title">
                                    <i class="fas fa-chart-line"></i>
                                    Tendência de Inteligência
                                </div>
                                <div class="metric-value" style="font-size: 1.5rem;">
                                    ${trends.intelligence_trend.toExponential(2)}
                                </div>
                                <div style="margin-top: 0.5rem; color: ${trends.intelligence_trend > 0 ? 'var(--quantum-neon)' : 'var(--danger)'};">
                                    ${trends.intelligence_trend > 0 ? '↗ Crescendo' : '↘ Decrescendo'}
                                </div>
                            </div>
                            
                            <div class="content-card">
                                <div class="metric-title">
                                    <i class="fas fa-star"></i>
                                    Tendência Geral
                                </div>
                                <div class="metric-value" style="font-size: 1.5rem;">
                                    ${trends.overall_trend.toExponential(2)}
                                </div>
                                <div style="margin-top: 0.5rem; color: ${trends.overall_trend > 0 ? 'var(--quantum-neon)' : 'var(--danger)'};">
                                    ${trends.overall_trend > 0 ? '↗ Crescendo' : '↘ Decrescendo'}
                                </div>
                            </div>
                        </div>
                    `;
                } else {
                    html += `
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle"></i>
                            Dados insuficientes para calcular tendências (mínimo 10 entradas)
                        </div>
                    `;
                }
                
                container.innerHTML = html;
            }
            
            renderMetricsTab(status) {
                const container = document.getElementById('metricsContent');
                
                const metrics = status.evolution_metrics;
                
                let html = `
                    <div class="columns-2">
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-redo"></i>
                                Ciclos de Aprendizado
                            </div>
                            <div class="metric-value" style="font-size: 1.8rem;">
                                ${metrics.learning_cycles_completed.toLocaleString()}
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-random"></i>
                                Adaptações Feitas
                            </div>
                            <div class="metric-value" style="font-size: 1.8rem;">
                                ${metrics.adaptations_made.toLocaleString()}
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-brain"></i>
                                Breakthroughs de Consciência
                            </div>
                            <div class="metric-value" style="font-size: 1.8rem;">
                                ${metrics.consciousness_breakthroughs}
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-atom"></i>
                                Saltos Quânticos
                            </div>
                            <div class="metric-value" style="font-size: 1.8rem;">
                                ${metrics.quantum_leaps}
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-star"></i>
                                Eventos de Transcendência
                            </div>
                            <div class="metric-value" style="font-size: 1.8rem;">
                                ${metrics.transcendence_events}
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-clock"></i>
                                Tempo Total
                            </div>
                            <div class="metric-value" style="font-size: 1.8rem;">
                                ${(metrics.total_evolution_time / 3600).toFixed(1)}h
                            </div>
                        </div>
                    </div>
                    
                    <div class="divider"></div>
                    <h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">💾 Status de Memória</h3>
                `;
                
                // Memória
                const memory = status.memory_status;
                const totalMemories = memory.short_term_memories + memory.long_term_memories + memory.quantum_memories;
                const usagePercent = (totalMemories / memory.total_capacity) * 100;
                
                html += `
                    <div class="progress-item">
                        <div class="progress-header">
                            <span class="progress-label">Uso de Memória</span>
                            <span class="progress-value">${usagePercent.toFixed(1)}%</span>
                        </div>
                        <div class="progress-bar">
                            <div class="progress-fill" style="width: ${usagePercent}%"></div>
                        </div>
                    </div>
                    
                    <div class="columns-3" style="margin-top: 2rem;">
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-history"></i>
                                Curto Prazo
                            </div>
                            <div class="metric-value" style="font-size: 1.5rem;">
                                ${memory.short_term_memories.toLocaleString()}
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-database"></i>
                                Longo Prazo
                            </div>
                            <div class="metric-value" style="font-size: 1.5rem;">
                                ${memory.long_term_memories.toLocaleString()}
                            </div>
                        </div>
                        
                        <div class="content-card">
                            <div class="metric-title">
                                <i class="fas fa-atom"></i>
                                Quântica
                            </div>
                            <div class="metric-value" style="font-size: 1.5rem;">
                                ${memory.quantum_memories.toLocaleString()}
                            </div>
                        </div>
                    </div>
                    
                    <div class="divider"></div>
                    <h3 style="margin-bottom: 1.5rem; color: var(--quantum-accent);">👤 Traços de Personalidade</h3>
                `;
                
                // Personalidade
                Object.entries(status.personality_traits).forEach(([trait, value]) => {
                    const percentage = (value * 100).toFixed(1);
                    
                    html += `
                        <div class="trait-item">
                            <div class="trait-label">
                                <span>${trait}</span>
                                <span>${percentage}%</span>
                            </div>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: ${percentage}%"></div>
                            </div>
                        </div>
                    `;
                });
                
                container.innerHTML = html;
            }
            
            showNotification(message, type = 'info') {
                // Cria notificação
                const notification = document.createElement('div');
                notification.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    background: ${type === 'success' ? 'var(--success)' : type === 'error' ? 'var(--danger)' : 'var(--info)'};
                    color: white;
                    padding: 1rem 1.5rem;
                    border-radius: var(--border-radius-md);
                    box-shadow: var(--shadow-lg);
                    z-index: 1000;
                    animation: slideIn 0.3s ease;
                    display: flex;
                    align-items: center;
                    gap: 0.75rem;
                    max-width: 400px;
                `;
                
                const icon = type === 'success' ? 'fa-check-circle' : 
                            type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle';
                
                notification.innerHTML = `
                    <i class="fas ${icon}"></i>
                    <span>${message}</span>
                `;
                
                document.body.appendChild(notification);
                
                // Remove após 5 segundos
                setTimeout(() => {
                    notification.style.animation = 'slideOut 0.3s ease';
                    setTimeout(() => {
                        if (notification.parentNode) {
                            notification.parentNode.removeChild(notification);
                        }
                    }, 300);
                }, 5000);
                
                // Adiciona estilos de animação
                if (!document.getElementById('notification-styles')) {
                    const style = document.createElement('style');
                    style.id = 'notification-styles';
                    style.textContent = `
                        @keyframes slideIn {
                            from { transform: translateX(100%); opacity: 0; }
                            to { transform: translateX(0); opacity: 1; }
                        }
                        @keyframes slideOut {
                            from { transform: translateX(0); opacity: 1; }
                            to { transform: translateX(100%); opacity: 0; }
                        }
                    `;
                    document.head.appendChild(style);
                }
            }
            
            showError(error) {
                const container = document.getElementById('errorContainer');
                container.style.display = 'block';
                container.innerHTML = `
                    <div style="background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.3); 
                         border-radius: var(--border-radius-md); padding: var(--spacing-md); color: var(--danger);">
                        <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.5rem;">
                            <i class="fas fa-exclamation-triangle"></i>
                            <strong>Erro ao carregar sistema de evolução</strong>
                        </div>
                        <div style="font-family: var(--font-mono); font-size: 0.875rem; color: var(--gray-400);">
                            ${error.message}
                        </div>
                        <button onclick="this.parentElement.style.display='none'" 
                                style="margin-top: 1rem; background: transparent; border: 1px solid var(--danger); 
                                       color: var(--danger); padding: 0.5rem 1rem; border-radius: var(--border-radius-sm); 
                                       cursor: pointer; font-size: 0.875rem;">
                            Fechar
                        </button>
                    </div>
                `;
                
                console.error('Erro no sistema de evolução:', error);
            }
            
            startAutoRefresh() {
                // Atualiza a cada 30 segundos
                setInterval(() => {
                    this.refreshData();
                }, 30000);
            }
        }
        
        // ============================================
        // INICIALIZAÇÃO
        // ============================================
        
        document.addEventListener('DOMContentLoaded', () => {
            try {
                // Inicializa o motor de evolução
                const engine = new QuantumEvolutionEngine();
                
                // Inicializa a interface
                const ui = new QuantumEvolutionUI(engine);
                
                // Carrega última tab
                const lastTab = localStorage.getItem('last_tab');
                if (lastTab) {
                    ui.switchTab(lastTab);
                }
                
                // Mostra notificação de boas-vindas
                setTimeout(() => {
                    ui.showNotification('Sistema de Evolução Quântica inicializado com sucesso', 'success');
                }, 1000);
                
                // Expõe para debugging
                window.quantumEngine = engine;
                window.quantumUI = ui;
                
            } catch (error) {
                console.error('Erro fatal na inicialização:', error);
                document.getElementById('errorContainer').innerHTML = `
                    <div style="background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.3); 
                         border-radius: var(--border-radius-md); padding: var(--spacing-md); color: var(--danger);
                         margin: 2rem;">
                        <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.5rem;">
                            <i class="fas fa-exclamation-triangle"></i>
                            <strong>Erro fatal na inicialização do sistema</strong>
                        </div>
                        <div style="font-family: var(--font-mono); font-size: 0.875rem; color: var(--gray-400);">
                            ${error.toString()}
                        </div>
                        <button onclick="location.reload()" 
                                style="margin-top: 1rem; background: var(--danger); border: none; 
                                       color: white; padding: 0.75rem 1.5rem; border-radius: var(--border-radius-sm); 
                                       cursor: pointer; font-size: 0.875rem; font-weight: 600;">
                            <i class="fas fa-redo"></i> Tentar novamente
                        </button>
                    </div>
                `;
            }
        });
    </script>
</body>
</html>