<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>⚛️ LEXTRADER-IAG 3.0 - Sistema Quântico</title>
    <!-- Fontes e Ícones -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.min.css">
    <!-- Chart.js para gráficos -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        /* Variáveis CSS */
        :root {
            /* Cores do tema quântico */
            --quantum-primary: #0a0a23;
            --quantum-secondary: #1a1a2e;
            --quantum-tertiary: #2d2d44;
            --quantum-accent: #4cc9f0;
            --quantum-accent-dark: #4361ee;
            --quantum-neon: #00ff9d;
            --quantum-purple: #7209b7;
            --quantum-pink: #f72585;
            --quantum-cyan: #00bbf9;
            
            /* Cores de estado */
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
            --shadow-neon: 0 0 15px rgba(0, 255, 157, 0.5);
            
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
            text-shadow: var(--shadow-glow);
            animation: pulse-quantum 2s infinite;
        }
        
        @keyframes pulse-quantum {
            0%, 100% { 
                transform: scale(1);
                opacity: 1;
            }
            50% { 
                transform: scale(1.05);
                opacity: 0.8;
            }
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
        
        /* Tabs */
        .tabs-container {
            background: rgba(26, 26, 46, 0.8);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(76, 201, 240, 0.1);
            border-radius: var(--border-radius-lg);
            overflow: hidden;
            margin-bottom: var(--spacing-xl);
            box-shadow: var(--shadow-lg);
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
            background: rgba(76, 201, 240, 0.05);
        }
        
        .tab-btn.active::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, var(--quantum-accent), var(--quantum-neon));
            box-shadow: var(--shadow-neon);
        }
        
        .tab-content {
            padding: var(--spacing-xl);
            display: none;
            animation: fadeInUp 0.5s ease;
        }
        
        @keyframes fadeInUp {
            from { 
                opacity: 0; 
                transform: translateY(20px); 
            }
            to { 
                opacity: 1; 
                transform: translateY(0); 
            }
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
        
        /* Formulários e Controles */
        .form-group {
            margin-bottom: var(--spacing-md);
        }
        
        .form-label {
            display: block;
            margin-bottom: 0.5rem;
            color: var(--gray-300);
            font-weight: 500;
        }
        
        .form-control {
            width: 100%;
            padding: 0.75rem 1rem;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(76, 201, 240, 0.2);
            border-radius: var(--border-radius-md);
            color: var(--gray-100);
            font-family: var(--font-sans);
            font-size: 1rem;
            transition: all var(--transition-fast);
        }
        
        .form-control:focus {
            outline: none;
            border-color: var(--quantum-accent);
            box-shadow: 0 0 0 3px rgba(76, 201, 240, 0.1);
        }
        
        .form-control::placeholder {
            color: var(--gray-500);
        }
        
        /* Botões */
        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: var(--border-radius-md);
            font-family: var(--font-sans);
            font-weight: 600;
            font-size: 0.95rem;
            cursor: pointer;
            transition: all var(--transition-normal);
            position: relative;
            overflow: hidden;
        }
        
        .btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: left 0.5s ease;
        }
        
        .btn:hover::before {
            left: 100%;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, var(--quantum-accent-dark), var(--quantum-purple));
            color: white;
            box-shadow: var(--shadow-md);
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: var(--shadow-lg), var(--shadow-glow);
        }
        
        .btn-secondary {
            background: rgba(76, 201, 240, 0.1);
            border: 1px solid rgba(76, 201, 240, 0.3);
            color: var(--quantum-accent);
        }
        
        .btn-secondary:hover {
            background: rgba(76, 201, 240, 0.2);
            border-color: var(--quantum-accent);
        }
        
        .btn-success {
            background: linear-gradient(135deg, var(--success), #059669);
            color: white;
        }
        
        .btn-danger {
            background: linear-gradient(135deg, var(--danger), #dc2626);
            color: white;
        }
        
        .btn-lg {
            padding: 1rem 2rem;
            font-size: 1.1rem;
        }
        
        /* Circuito Quântico Visual */
        .circuit-visualizer {
            background: rgba(0, 0, 0, 0.3);
            border: 1px solid rgba(76, 201, 240, 0.2);
            border-radius: var(--border-radius-lg);
            padding: var(--spacing-lg);
            margin-bottom: var(--spacing-lg);
            position: relative;
            overflow: hidden;
        }
        
        .circuit-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: var(--spacing-md);
        }
        
        .qubit-line {
            display: flex;
            align-items: center;
            gap: var(--spacing-sm);
            padding: var(--spacing-sm);
            background: rgba(76, 201, 240, 0.05);
            border-radius: var(--border-radius-md);
            border: 1px solid rgba(76, 201, 240, 0.1);
        }
        
        .qubit-label {
            font-family: var(--font-mono);
            font-weight: 600;
            color: var(--quantum-accent);
            min-width: 60px;
        }
        
        .qubit-state {
            flex: 1;
            text-align: center;
            padding: 0.5rem;
            background: rgba(0, 255, 157, 0.1);
            border-radius: var(--border-radius-sm);
            font-family: var(--font-mono);
            color: var(--quantum-neon);
            border: 1px solid rgba(0, 255, 157, 0.2);
        }
        
        /* Alerta de Sistema */
        .system-alert {
            padding: var(--spacing-md);
            border-radius: var(--border-radius-md);
            margin-bottom: var(--spacing-md);
            display: flex;
            align-items: flex-start;
            gap: var(--spacing-sm);
            border: 1px solid;
            animation: slideInRight 0.5s ease;
        }
        
        @keyframes slideInRight {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        
        .alert-success {
            background: rgba(16, 185, 129, 0.1);
            border-color: rgba(16, 185, 129, 0.3);
            color: var(--success);
        }
        
        .alert-warning {
            background: rgba(245, 158, 11, 0.1);
            border-color: rgba(245, 158, 11, 0.3);
            color: var(--warning);
        }
        
        .alert-danger {
            background: rgba(239, 68, 68, 0.1);
            border-color: rgba(239, 68, 68, 0.3);
            color: var(--danger);
        }
        
        .alert-info {
            background: rgba(59, 130, 246, 0.1);
            border-color: rgba(59, 130, 246, 0.3);
            color: var(--info);
        }
        
        /* Resultados JSON */
        .json-result {
            background: rgba(0, 0, 0, 0.3);
            border: 1px solid rgba(76, 201, 240, 0.2);
            border-radius: var(--border-radius-md);
            padding: var(--spacing-md);
            margin-top: var(--spacing-md);
            overflow-x: auto;
            font-family: var(--font-mono);
            font-size: 0.9rem;
        }
        
        /* Chart Container */
        .chart-container {
            position: relative;
            height: 300px;
            width: 100%;
            margin: var(--spacing-lg) 0;
            background: rgba(0, 0, 0, 0.2);
            border-radius: var(--border-radius-lg);
            padding: var(--spacing-md);
        }
        
        /* Partículas Quânticas */
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
            animation: float-quantum 20s infinite linear;
        }
        
        @keyframes float-quantum {
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
        
        /* Animações Especiais */
        @keyframes quantum-glow {
            0%, 100% { box-shadow: 0 0 5px rgba(76, 201, 240, 0.5); }
            50% { box-shadow: 0 0 20px rgba(76, 201, 240, 0.8); }
        }
        
        .quantum-glow {
            animation: quantum-glow 2s infinite;
        }
        
        /* Loading Spinner */
        .quantum-spinner {
            display: inline-block;
            width: 50px;
            height: 50px;
            border: 3px solid rgba(76, 201, 240, 0.3);
            border-radius: 50%;
            border-top-color: var(--quantum-accent);
            animation: spin 1s ease-in-out infinite;
        }
        
        @keyframes spin {
            to { transform: rotate(360deg); }
        }
        
        /* Divider */
        .divider {
            height: 1px;
            background: linear-gradient(90deg, transparent, rgba(76, 201, 240, 0.3), transparent);
            margin: var(--spacing-lg) 0;
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
                    <div class="subtitle">SISTEMA QUÂNTICO</div>
                </div>
            </div>
        </header>
        
        <!-- Métricas Principais -->
        <div class="main-metrics" id="mainMetrics">
            <!-- Preenchido por JavaScript -->
        </div>
        
        <!-- Alertas do Sistema -->
        <div id="systemAlerts"></div>
        
        <!-- Tabs -->
        <div class="tabs-container">
            <div class="tabs-header">
                <button class="tab-btn active" data-tab="circuits">
                    <i class="fas fa-microchip"></i>
                    Circuitos
                </button>
                <button class="tab-btn" data-tab="algorithms">
                    <i class="fas fa-cogs"></i>
                    Algoritmos
                </button>
                <button class="tab-btn" data-tab="analysis">
                    <i class="fas fa-chart-line"></i>
                    Análise
                </button>
            </div>
            
            <!-- Conteúdo das Tabs -->
            <div class="tab-content active" id="circuitsTab">
                <div class="tab-header">
                    <h2><i class="fas fa-microchip"></i> Circuitos Quânticos</h2>
                </div>
                
                <div class="circuit-visualizer" id="circuitVisualizer">
                    <!-- Circuito será renderizado aqui -->
                </div>
                
                <div class="columns-2">
                    <div class="form-group">
                        <label class="form-label">Selecione um circuito</label>
                        <select class="form-control" id="circuitSelect">
                            <!-- Opções serão preenchidas -->
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Ações do Circuito</label>
                        <div style="display: flex; gap: 0.5rem; margin-top: 0.5rem;">
                            <button class="btn btn-secondary" id="resetCircuitBtn">
                                <i class="fas fa-redo"></i> Resetar
                            </button>
                            <button class="btn btn-danger" id="deleteCircuitBtn">
                                <i class="fas fa-trash"></i> Remover
                            </button>
                        </div>
                    </div>
                </div>
                
                <div class="divider"></div>
                
                <!-- Criar Novo Circuito -->
                <div class="content-card">
                    <h3 style="margin-bottom: var(--spacing-md); color: var(--quantum-accent);">
                        <i class="fas fa-plus-circle"></i> Criar Novo Circuito
                    </h3>
                    
                    <div class="columns-2">
                        <div class="form-group">
                            <label class="form-label">Nome do Circuito</label>
                            <input type="text" class="form-control" id="circuitName" placeholder="ex: Meu_Circuito_01">
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label">Número de Qubits</label>
                            <input type="number" class="form-control" id="numQubits" min="1" max="10" value="4">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Portas Quânticas Iniciais (opcional)</label>
                        <textarea class="form-control" id="initialGates" rows="3" 
                                  placeholder="ex: H(0)\nCNOT(0,1)\nX(2)"></textarea>
                        <small style="color: var(--gray-500); font-size: 0.875rem;">
                            Uma porta por linha. Formato: Porta(qubit) ou CNOT(control, target)
                        </small>
                    </div>
                    
                    <button class="btn btn-primary btn-lg" id="createCircuitBtn" style="width: 100%;">
                        <i class="fas fa-bolt"></i> Criar Circuito Quântico
                    </button>
                </div>
            </div>
            
            <div class="tab-content" id="algorithmsTab">
                <div class="tab-header">
                    <h2><i class="fas fa-cogs"></i> Algoritmos Quânticos</h2>
                </div>
                
                <div class="form-group">
                    <label class="form-label">Selecione um algoritmo</label>
                    <select class="form-control" id="algorithmSelect">
                        <option value="superposition">Superposição Quântica (Hadamard)</option>
                        <option value="entanglement">Emaranhamento Quântico (Bell State)</option>
                        <option value="teleportation">Teleporte Quântico</option>
                        <option value="grover">Algoritmo de Grover (Busca)</option>
                        <option value="shor">Algoritmo de Shor (Fatoração)</option>
                        <option value="qft">Transformada Quântica de Fourier</option>
                        <option value="vqe">Variational Quantum Eigensolver</option>
                    </select>
                </div>
                
                <div class="content-card" id="algorithmDescription">
                    <!-- Descrição será preenchida -->
                </div>
                
                <!-- Parâmetros Específicos -->
                <div id="algorithmParams" style="display: none;">
                    <div class="divider"></div>
                    <h3 style="margin-bottom: var(--spacing-md); color: var(--quantum-accent);">
                        <i class="fas fa-sliders-h"></i> Parâmetros do Algoritmo
                    </h3>
                    
                    <div id="groverParams" style="display: none;">
                        <div class="columns-2">
                            <div class="form-group">
                                <label class="form-label">Estado Alvo</label>
                                <input type="text" class="form-control" id="targetState" placeholder="ex: 101" value="101">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Número de Iterações</label>
                                <input type="number" class="form-control" id="groverIterations" min="1" max="10" value="3">
                            </div>
                        </div>
                    </div>
                    
                    <div id="shorParams" style="display: none;">
                        <div class="form-group">
                            <label class="form-label">Número para Fatorar</label>
                            <input type="number" class="form-control" id="shorNumber" min="15" max="100" value="15">
                        </div>
                    </div>
                </div>
                
                <div class="divider"></div>
                
                <button class="btn btn-primary btn-lg" id="executeAlgorithmBtn" style="width: 100%;">
                    <i class="fas fa-rocket"></i> Executar Algoritmo Quântico
                </button>
                
                <!-- Resultados -->
                <div id="algorithmResults" style="display: none; margin-top: var(--spacing-lg);">
                    <h3 style="margin-bottom: var(--spacing-md); color: var(--quantum-accent);">
                        <i class="fas fa-chart-bar"></i> Resultados
                    </h3>
                    <div class="json-result" id="resultOutput">
                        <!-- Resultados serão exibidos aqui -->
                    </div>
                    
                    <div class="chart-container">
                        <canvas id="resultsChart"></canvas>
                    </div>
                </div>
            </div>
            
            <div class="tab-content" id="analysisTab">
                <div class="tab-header">
                    <h2><i class="fas fa-chart-line"></i> Análise Quântica</h2>
                </div>
                
                <div class="columns-2">
                    <div class="content-card">
                        <div class="metric-title">
                            <i class="fas fa-play-circle"></i>
                            Total de Execuções
                        </div>
                        <div class="metric-value" id="totalExecutions">0</div>
                    </div>
                    
                    <div class="content-card">
                        <div class="metric-title">
                            <i class="fas fa-check-circle"></i>
                            Taxa de Sucesso
                        </div>
                        <div class="metric-value" id="successRate">0%</div>
                    </div>
                    
                    <div class="content-card">
                        <div class="metric-title">
                            <i class="fas fa-clock"></i>
                            Tempo Médio
                        </div>
                        <div class="metric-value" id="avgTime">0s</div>
                    </div>
                    
                    <div class="content-card">
                        <div class="metric-title">
                            <i class="fas fa-atom"></i>
                            Coerência Média
                        </div>
                        <div class="metric-value" id="coherenceRate">0%</div>
                    </div>
                </div>
                
                <div class="divider"></div>
                
                <h3 style="margin-bottom: var(--spacing-md); color: var(--quantum-accent);">
                    <i class="fas fa-chart-line"></i> Histórico de Execuções
                </h3>
                
                <div class="chart-container">
                    <canvas id="executionChart"></canvas>
                </div>
                
                <div class="divider"></div>
                
                <h3 style="margin-bottom: var(--spacing-md); color: var(--quantum-accent);">
                    <i class="fas fa-database"></i> Estatísticas Detalhadas
                </h3>
                
                <div class="columns-3">
                    <div class="content-card">
                        <div class="metric-title">
                            <i class="fas fa-bolt"></i>
                            Pico de Qubits
                        </div>
                        <div class="metric-value" style="font-size: 1.8rem;" id="peakQubits">0</div>
                    </div>
                    
                    <div class="content-card">
                        <div class="metric-title">
                            <i class="fas fa-brain"></i>
                            Entropia Média
                        </div>
                        <div class="metric-value" style="font-size: 1.8rem;" id="avgEntropy">0.00</div>
                    </div>
                    
                    <div class="content-card">
                        <div class="metric-title">
                            <i class="fas fa-wave-square"></i>
                            Interferência Média
                        </div>
                        <div class="metric-value" style="font-size: 1.8rem;" id="avgInterference">0.00</div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Modal de Loading -->
        <div id="loadingModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.8); z-index: 1000; justify-content: center; align-items: center; flex-direction: column; gap: var(--spacing-md);">
            <div class="quantum-spinner"></div>
            <div style="color: var(--quantum-accent); font-size: 1.2rem; font-weight: 600;" id="loadingMessage">
                Executando algoritmo quântico...
            </div>
        </div>
    </div>

    <script>
        // ============================================
        // SISTEMA QUÂNTICO CORE
        // ============================================
        
        class QuantumSystem {
            constructor() {
                this.status = {
                    system_status: "Ativo",
                    qubits: 32,
                    algorithms_executed: 0,
                    measurements_performed: 0,
                    circuits: {},
                    history: [],
                    quantum_state: "coerente",
                    temperature: 0.015, // Kelvin
                    last_calibration: new Date().toISOString()
                };
                
                this.circuits = this.loadCircuits();
                this.executionHistory = this.loadHistory();
                this.initialize();
            }
            
            initialize() {
                // Inicializa circuitos padrão
                if (Object.keys(this.circuits).length === 0) {
                    this.createDefaultCircuits();
                }
                
                // Inicializa partículas quânticas
                this.initQuantumParticles();
                
                // Atualiza status periodicamente
                setInterval(() => this.updateSystemStatus(), 10000);
            }
            
            createDefaultCircuits() {
                // Circuito básico de Hadamard
                this.circuits["Hadamard_Circuit"] = {
                    name: "Hadamard_Circuit",
                    qubits: 4,
                    gates: [
                        { type: "H", target: 0 },
                        { type: "H", target: 1 },
                        { type: "H", target: 2 },
                        { type: "H", target: 3 }
                    ],
                    created: new Date().toISOString(),
                    measurements: 0
                };
                
                // Circuito de emaranhamento
                this.circuits["Bell_State_Circuit"] = {
                    name: "Bell_State_Circuit",
                    qubits: 2,
                    gates: [
                        { type: "H", target: 0 },
                        { type: "CNOT", control: 0, target: 1 }
                    ],
                    created: new Date().toISOString(),
                    measurements: 0
                };
                
                // Circuito de Grover
                this.circuits["Grover_Search_Circuit"] = {
                    name: "Grover_Search_Circuit",
                    qubits: 3,
                    gates: [
                        { type: "H", target: 0 },
                        { type: "H", target: 1 },
                        { type: "H", target: 2 },
                        { type: "X", target: 0 },
                        { type: "H", target: 2 },
                        { type: "CX", control: 0, target: 1 },
                        { type: "H", target: 2 },
                        { type: "X", target: 0 }
                    ],
                    created: new Date().toISOString(),
                    measurements: 0
                };
                
                this.saveCircuits();
            }
            
            getStatus() {
                // Atualiza contagens
                this.status.qubits = this.calculateTotalQubits();
                this.status.algorithms_executed = this.executionHistory.length;
                
                return {
                    ...this.status,
                    total_circuits: Object.keys(this.circuits).length,
                    active_circuits: this.countActiveCircuits(),
                    system_uptime: this.calculateUptime()
                };
            }
            
            listCircuits() {
                return Object.keys(this.circuits);
            }
            
            createCircuit(name, numQubits, gates = []) {
                if (this.circuits[name]) {
                    throw new Error(`Circuito '${name}' já existe`);
                }
                
                this.circuits[name] = {
                    name,
                    qubits: numQubits,
                    gates: gates,
                    created: new Date().toISOString(),
                    measurements: 0,
                    last_modified: new Date().toISOString()
                };
                
                this.saveCircuits();
                return this.circuits[name];
            }
            
            resetCircuit(name) {
                if (!this.circuits[name]) {
                    throw new Error(`Circuito '${name}' não encontrado`);
                }
                
                // Remove todas as portas exceto as iniciais
                this.circuits[name].gates = [];
                this.circuits[name].measurements = 0;
                this.circuits[name].last_modified = new Date().toISOString();
                
                this.saveCircuits();
                return true;
            }
            
            deleteCircuit(name) {
                if (!this.circuits[name]) {
                    throw new Error(`Circuito '${name}' não encontrado`);
                }
                
                delete this.circuits[name];
                this.saveCircuits();
                return true;
            }
            
            runQuantumAlgorithm(algorithm, params = {}) {
                const startTime = performance.now();
                
                // Simula execução do algoritmo
                let result;
                
                switch (algorithm) {
                    case "superposition":
                        result = this.runSuperposition(params);
                        break;
                    case "entanglement":
                        result = this.runEntanglement(params);
                        break;
                    case "teleportation":
                        result = this.runTeleportation(params);
                        break;
                    case "grover":
                        result = this.runGrover(params);
                        break;
                    case "shor":
                        result = this.runShor(params);
                        break;
                    case "qft":
                        result = this.runQFT(params);
                        break;
                    case "vqe":
                        result = this.runVQE(params);
                        break;
                    default:
                        throw new Error(`Algoritmo '${algorithm}' não suportado`);
                }
                
                const executionTime = performance.now() - startTime;
                
                // Registra execução no histórico
                this.executionHistory.push({
                    algorithm,
                    params,
                    result: result.success,
                    execution_time: executionTime,
                    timestamp: new Date().toISOString(),
                    qubits_used: result.qubits || 4
                });
                
                // Limita histórico
                if (this.executionHistory.length > 100) {
                    this.executionHistory = this.executionHistory.slice(-100);
                }
                
                this.saveHistory();
                
                return {
                    ...result,
                    execution_time: executionTime.toFixed(3) + "ms",
                    algorithm,
                    timestamp: new Date().toISOString()
                };
            }
            
            runSuperposition(params) {
                const qubits = params.qubits || 4;
                const states = [];
                
                for (let i = 0; i < Math.pow(2, qubits); i++) {
                    const probability = 1 / Math.pow(2, qubits);
                    states.push({
                        state: i.toString(2).padStart(qubits, '0'),
                        probability: probability,
                        amplitude: {
                            real: Math.sqrt(probability),
                            imag: 0
                        }
                    });
                }
                
                return {
                    success: true,
                    qubits,
                    superposition_states: states,
                    description: "Todos os qubits colocados em superposição uniforme",
                    fidelity: 0.998
                };
            }
            
            runEntanglement(params) {
                const pairs = params.pairs || 2;
                const results = [];
                
                for (let i = 0; i < pairs; i++) {
                    const pair = [i * 2, i * 2 + 1];
                    results.push({
                        pair,
                        bell_state: "Φ+",
                        correlation: 0.999,
                        measurement: {
                            "00": 0.5,
                            "11": 0.5,
                            "01": 0.0,
                            "10": 0.0
                        }
                    });
                }
                
                return {
                    success: true,
                    entangled_pairs: results,
                    description: "Pares de qubits emaranhados criados (estado Bell Φ+)",
                    entanglement_entropy: 1.0
                };
            }
            
            runGrover(params) {
                const targetState = params.target_state || "101";
                const iterations = params.iterations || Math.floor(Math.PI / 4 * Math.sqrt(Math.pow(2, targetState.length)));
                
                // Simula busca de Grover
                const numStates = Math.pow(2, targetState.length);
                const successProbability = Math.pow(Math.sin((2 * iterations + 1) * Math.asin(1 / Math.sqrt(numStates))), 2);
                
                return {
                    success: true,
                    target_state: targetState,
                    iterations,
                    success_probability: successProbability,
                    amplification_factor: Math.sqrt(numStates),
                    measurements: Array.from({length: numStates}, (_, i) => {
                        const state = i.toString(2).padStart(targetState.length, '0');
                        const isTarget = state === targetState;
                        return {
                            state,
                            probability: isTarget ? successProbability : (1 - successProbability) / (numStates - 1)
                        };
                    })
                };
            }
            
            calculateTotalQubits() {
                return Object.values(this.circuits).reduce((total, circuit) => total + circuit.qubits, 0);
            }
            
            countActiveCircuits() {
                return Object.values(this.circuits).filter(circuit => 
                    circuit.gates && circuit.gates.length > 0
                ).length;
            }
            
            calculateUptime() {
                // Simula uptime desde o carregamento da página
                const startTime = performance.timing.navigationStart;
                const uptime = Date.now() - startTime;
                
                const hours = Math.floor(uptime / (1000 * 60 * 60));
                const minutes = Math.floor((uptime % (1000 * 60 * 60)) / (1000 * 60));
                const seconds = Math.floor((uptime % (1000 * 60)) / 1000);
                
                return `${hours}h ${minutes}m ${seconds}s`;
            }
            
            updateSystemStatus() {
                // Atualiza status do sistema
                const variations = {
                    temperature: (Math.random() - 0.5) * 0.001,
                    quantum_state: Math.random() > 0.98 ? "decoerente" : "coerente"
                };
                
                this.status.temperature = Math.max(0.01, this.status.temperature + variations.temperature);
                if (variations.quantum_state === "decoerente" && Math.random() > 0.7) {
                    this.status.quantum_state = "decoerente";
                    setTimeout(() => {
                        this.status.quantum_state = "coerente";
                    }, 5000);
                }
                
                // Aumenta medições
                this.status.measurements_performed += Math.floor(Math.random() * 3);
            }
            
            loadCircuits() {
                try {
                    const saved = localStorage.getItem('quantum_circuits');
                    return saved ? JSON.parse(saved) : {};
                } catch (e) {
                    console.warn('Erro ao carregar circuitos:', e);
                    return {};
                }
            }
            
            saveCircuits() {
                try {
                    localStorage.setItem('quantum_circuits', JSON.stringify(this.circuits));
                } catch (e) {
                    console.warn('Erro ao salvar circuitos:', e);
                }
            }
            
            loadHistory() {
                try {
                    const saved = localStorage.getItem('quantum_execution_history');
                    return saved ? JSON.parse(saved) : [];
                } catch (e) {
                    console.warn('Erro ao carregar histórico:', e);
                    return [];
                }
            }
            
            saveHistory() {
                try {
                    localStorage.setItem('quantum_execution_history', JSON.stringify(this.executionHistory));
                } catch (e) {
                    console.warn('Erro ao salvar histórico:', e);
                }
            }
            
            initQuantumParticles() {
                const container = document.getElementById('quantumParticles');
                if (!container) return;
                
                // Limpa partículas existentes
                container.innerHTML = '';
                
                // Cria partículas
                for (let i = 0; i < 30; i++) {
                    const particle = document.createElement('div');
                    particle.className = 'particle';
                    
                    // Tamanho aleatório
                    const size = Math.random() * 3 + 1;
                    particle.style.width = `${size}px`;
                    particle.style.height = `${size}px`;
                    
                    // Posição inicial
                    particle.style.left = `${Math.random() * 100}vw`;
                    particle.style.top = `${Math.random() * 100}vh`;
                    
                    // Cor aleatória
                    const colors = ['#4cc9f0', '#00ff9d', '#7209b7', '#f72585', '#00bbf9'];
                    particle.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
                    
                    // Opacidade e delay
                    particle.style.opacity = Math.random() * 0.5 + 0.3;
                    particle.style.animationDelay = `${Math.random() * 20}s`;
                    
                    container.appendChild(particle);
                }
            }
            
            getCircuitDetails(name) {
                return this.circuits[name] || null;
            }
            
            getExecutionStats() {
                const total = this.executionHistory.length;
                const successful = this.executionHistory.filter(e => e.result).length;
                const totalTime = this.executionHistory.reduce((sum, e) => sum + e.execution_time, 0);
                
                return {
                    total_executions: total,
                    success_rate: total > 0 ? (successful / total * 100).toFixed(1) : 0,
                    avg_execution_time: total > 0 ? (totalTime / total).toFixed(3) : 0,
                    peak_qubits: Math.max(...this.executionHistory.map(e => e.qubits_used || 0), 0),
                    recent_executions: this.executionHistory.slice(-5)
                };
            }
        }
        
        // ============================================
        // INTERFACE DO SISTEMA QUÂNTICO
        // ============================================
        
        class QuantumSystemUI {
            constructor(system) {
                this.system = system;
                this.currentTab = 'circuits';
                this.charts = {};
                this.initialize();
            }
            
            initialize() {
                this.bindEvents();
                this.render();
                this.initCharts();
                this.startAutoUpdate();
            }
            
            bindEvents() {
                // Tabs
                document.querySelectorAll('.tab-btn').forEach(btn => {
                    btn.addEventListener('click', (e) => {
                        this.switchTab(e.currentTarget.dataset.tab);
                    });
                });
                
                // Circuitos
                document.getElementById('createCircuitBtn').addEventListener('click', () => this.createCircuit());
                document.getElementById('resetCircuitBtn').addEventListener('click', () => this.resetCircuit());
                document.getElementById('deleteCircuitBtn').addEventListener('click', () => this.deleteCircuit());
                document.getElementById('circuitSelect').addEventListener('change', () => this.updateCircuitVisualizer());
                
                // Algoritmos
                document.getElementById('algorithmSelect').addEventListener('change', () => this.updateAlgorithmDescription());
                document.getElementById('executeAlgorithmBtn').addEventListener('click', () => this.executeAlgorithm());
                
                // Atalhos de teclado
                document.addEventListener('keydown', (e) => {
                    if (e.ctrlKey || e.metaKey) {
                        switch(e.key) {
                            case '1':
                                e.preventDefault();
                                this.switchTab('circuits');
                                break;
                            case '2':
                                e.preventDefault();
                                this.switchTab('algorithms');
                                break;
                            case '3':
                                e.preventDefault();
                                this.switchTab('analysis');
                                break;
                            case 'Enter':
                                if (this.currentTab === 'algorithms') {
                                    e.preventDefault();
                                    this.executeAlgorithm();
                                }
                                break;
                        }
                    }
                });
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
                
                // Executa renderização específica da tab
                switch(tabName) {
                    case 'circuits':
                        this.updateCircuitList();
                        this.updateCircuitVisualizer();
                        break;
                    case 'algorithms':
                        this.updateAlgorithmDescription();
                        break;
                    case 'analysis':
                        this.updateAnalysis();
                        this.updateExecutionChart();
                        break;
                }
                
                // Salva preferência
                localStorage.setItem('quantum_last_tab', tabName);
            }
            
            render() {
                this.updateMainMetrics();
                this.updateCircuitList();
                this.updateAlgorithmDescription();
                this.updateAnalysis();
            }
            
            updateMainMetrics() {
                const status = this.system.getStatus();
                const container = document.getElementById('mainMetrics');
                
                container.innerHTML = `
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-power-off"></i>
                            Status
                        </div>
                        <div class="metric-value" style="color: ${status.system_status === 'Ativo' ? 'var(--quantum-neon)' : 'var(--danger)'}">
                            ${status.system_status}
                        </div>
                        <div class="metric-title" style="font-size: 0.8rem; margin-top: 0.5rem;">
                            <i class="fas fa-thermometer-half"></i>
                            ${status.temperature.toFixed(3)}K
                        </div>
                    </div>
                    
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-atom"></i>
                            Qubits Ativos
                        </div>
                        <div class="metric-value">${status.qubits}</div>
                        <div class="metric-title" style="font-size: 0.8rem; margin-top: 0.5rem;">
                            <i class="fas fa-wave-square"></i>
                            ${status.quantum_state}
                        </div>
                    </div>
                    
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-cogs"></i>
                            Algoritmos Executados
                        </div>
                        <div class="metric-value">${status.algorithms_executed}</div>
                        <div class="metric-title" style="font-size: 0.8rem; margin-top: 0.5rem;">
                            <i class="fas fa-microchip"></i>
                            ${status.total_circuits} circuitos
                        </div>
                    </div>
                    
                    <div class="metric-card">
                        <div class="metric-title">
                            <i class="fas fa-ruler"></i>
                            Medições
                        </div>
                        <div class="metric-value">${status.measurements_performed}</div>
                        <div class="metric-title" style="font-size: 0.8rem; margin-top: 0.5rem;">
                            <i class="fas fa-clock"></i>
                            ${status.system_uptime}
                        </div>
                    </div>
                `;
            }
            
            updateCircuitList() {
                const circuits = this.system.listCircuits();
                const select = document.getElementById('circuitSelect');
                
                select.innerHTML = '';
                
                circuits.forEach(circuitName => {
                    const option = document.createElement('option');
                    option.value = circuitName;
                    option.textContent = circuitName;
                    select.appendChild(option);
                });
                
                if (circuits.length > 0) {
                    select.value = circuits[0];
                }
            }
            
            updateCircuitVisualizer() {
                const circuitName = document.getElementById('circuitSelect').value;
                if (!circuitName) return;
                
                const circuit = this.system.getCircuitDetails(circuitName);
                if (!circuit) return;
                
                const container = document.getElementById('circuitVisualizer');
                
                let html = `
                    <h3 style="margin-bottom: var(--spacing-md); color: var(--quantum-accent);">
                        <i class="fas fa-project-diagram"></i> ${circuit.name}
                    </h3>
                    <div class="circuit-grid">
                `;
                
                // Linhas de qubits
                for (let i = 0; i < circuit.qubits; i++) {
                    const gatesOnQubit = circuit.gates.filter(g => 
                        g.target === i || (g.control === i && g.type === 'CNOT')
                    );
                    
                    html += `
                        <div class="qubit-line">
                            <div class="qubit-label">Qubit ${i}</div>
                            <div class="qubit-state">${this.getQubitState(gatesOnQubit)}</div>
                        </div>
                    `;
                }
                
                html += '</div>';
                
                // Informações do circuito
                html += `
                    <div class="columns-3" style="margin-top: var(--spacing-md);">
                        <div class="content-card" style="padding: var(--spacing-sm);">
                            <div style="font-size: 0.8rem; color: var(--gray-400);">Portas</div>
                            <div style="font-size: 1.2rem; font-weight: 600; color: var(--quantum-accent);">
                                ${circuit.gates.length}
                            </div>
                        </div>
                        
                        <div class="content-card" style="padding: var(--spacing-sm);">
                            <div style="font-size: 0.8rem; color: var(--gray-400);">Criado em</div>
                            <div style="font-size: 0.9rem; color: var(--gray-300);">
                                ${new Date(circuit.created).toLocaleDateString()}
                            </div>
                        </div>
                        
                        <div class="content-card" style="padding: var(--spacing-sm);">
                            <div style="font-size: 0.8rem; color: var(--gray-400);">Medições</div>
                            <div style="font-size: 1.2rem; font-weight: 600; color: var(--quantum-neon);">
                                ${circuit.measurements}
                            </div>
                        </div>
                    </div>
                `;
                
                container.innerHTML = html;
            }
            
            getQubitState(gates) {
                if (gates.length === 0) return '|0⟩';
                
                const lastGate = gates[gates.length - 1];
                
                switch(lastGate.type) {
                    case 'H':
                        return '|+⟩';
                    case 'X':
                        return '|1⟩';
                    case 'Y':
                        return '|i⟩';
                    case 'Z':
                        return '|0⟩';
                    case 'CNOT':
                        return '💫';
                    default:
                        return '|ψ⟩';
                }
            }
            
            createCircuit() {
                const name = document.getElementById('circuitName').value.trim();
                const numQubits = parseInt(document.getElementById('numQubits').value);
                const gatesText = document.getElementById('initialGates').value;
                
                if (!name) {
                    this.showAlert('Por favor, insira um nome para o circuito', 'warning');
                    return;
                }
                
                // Parse das portas
                let gates = [];
                if (gatesText.trim()) {
                    const lines = gatesText.split('\n');
                    lines.forEach(line => {
                        const match = line.match(/(\w+)\((\d+)(?:,\s*(\d+))?\)/);
                        if (match) {
                            const [, type, target, control] = match;
                            if (type === 'CNOT' && control !== undefined) {
                                gates.push({ type: 'CNOT', control: parseInt(control), target: parseInt(target) });
                            } else {
                                gates.push({ type: type.toUpperCase(), target: parseInt(target) });
                            }
                        }
                    });
                }
                
                try {
                    this.system.createCircuit(name, numQubits, gates);
                    this.updateCircuitList();
                    document.getElementById('circuitSelect').value = name;
                    this.updateCircuitVisualizer();
                    
                    this.showAlert(`Circuito '${name}' criado com ${numQubits} qubits!`, 'success');
                    
                    // Limpa formulário
                    document.getElementById('circuitName').value = '';
                    document.getElementById('initialGates').value = '';
                } catch (error) {
                    this.showAlert(error.message, 'danger');
                }
            }
            
            resetCircuit() {
                const circuitName = document.getElementById('circuitSelect').value;
                if (!circuitName) return;
                
                if (confirm(`Tem certeza que deseja resetar o circuito "${circuitName}"?`)) {
                    try {
                        this.system.resetCircuit(circuitName);
                        this.updateCircuitVisualizer();
                        this.showAlert(`Circuito '${circuitName}' resetado com sucesso!`, 'success');
                    } catch (error) {
                        this.showAlert(error.message, 'danger');
                    }
                }
            }
            
            deleteCircuit() {
                const circuitName = document.getElementById('circuitSelect').value;
                if (!circuitName) return;
                
                if (confirm(`Tem certeza que deseja remover o circuito "${circuitName}"? Esta ação não pode ser desfeita.`)) {
                    try {
                        this.system.deleteCircuit(circuitName);
                        this.updateCircuitList();
                        this.updateCircuitVisualizer();
                        this.showAlert(`Circuito '${circuitName}' removido com sucesso!`, 'success');
                    } catch (error) {
                        this.showAlert(error.message, 'danger');
                    }
                }
            }
            
            updateAlgorithmDescription() {
                const algorithm = document.getElementById('algorithmSelect').value;
                const container = document.getElementById('algorithmDescription');
                
                const descriptions = {
                    superposition: {
                        title: "Superposição Quântica (Hadamard)",
                        description: "Coloca qubits em superposição de estados |0⟩ e |1⟩. O estado resultante é uma combinação linear de todos os estados possíveis com amplitudes iguais.",
                        complexity: "O(1) por qubit",
                        qubits: "1+",
                        applications: "Base para a maioria dos algoritmos quânticos, paralelismo quântico"
                    },
                    entanglement: {
                        title: "Emaranhamento Quântico (Bell State)",
                        description: "Cria pares de qubits emaranhados onde o estado de um qubit é perfeitamente correlacionado com o estado do outro, independentemente da distância.",
                        complexity: "O(1)",
                        qubits: "2+ (pares)",
                        applications: "Teleporte quântico, criptografia quântica, computação distribuída"
                    },
                    teleportation: {
                        title: "Teleporte Quântico",
                        description: "Transfere o estado quântico de um qubit para outro, utilizando um par emaranhado e comunicação clássica.",
                        complexity: "O(1)",
                        qubits: "3",
                        applications: "Comunicação quântica, redes quânticas, estados quânticos remotos"
                    },
                    grover: {
                        title: "Algoritmo de Grover (Busca)",
                        description: "Algoritmo de busca em banco de dados não ordenado que fornece aceleração quadrática em relação aos algoritmos clássicos.",
                        complexity: "O(√N)",
                        qubits: "log₂(N)",
                        applications: "Busca em banco de dados, satisfação de problemas de restrição, inversão de funções"
                    },
                    shor: {
                        title: "Algoritmo de Shor (Fatoração)",
                        description: "Algoritmo quântico para fatoração de inteiros em tempo polinomial, uma ameaça significativa à criptografia RSA.",
                        complexity: "O((log N)³)",
                        qubits: "2log₂(N)",
                        applications: "Fatoração de inteiros, quebra de criptografia RSA, teoria dos números"
                    },
                    qft: {
                        title: "Transformada Quântica de Fourier",
                        description: "Versão quântica da Transformada Discreta de Fourier, componente fundamental de muitos algoritmos quânticos.",
                        complexity: "O(n²)",
                        qubits: "n",
                        applications: "Algoritmo de Shor, estimativa de fase, processamento de sinal quântico"
                    },
                    vqe: {
                        title: "Variational Quantum Eigensolver",
                        description: "Algoritmo híbrido quântico-clássico para encontrar o estado fundamental de moléculas e materiais.",
                        complexity: "Variável",
                        qubits: "10-100",
                        applications: "Química quântica, descoberta de materiais, farmacologia"
                    }
                };
                
                const desc = descriptions[algorithm] || descriptions.superposition;
                
                container.innerHTML = `
                    <h3 style="margin-bottom: var(--spacing-sm); color: var(--quantum-accent);">
                        ${desc.title}
                    </h3>
                    <p style="margin-bottom: var(--spacing-sm); color: var(--gray-300);">
                        ${desc.description}
                    </p>
                    <div class="columns-3">
                        <div style="background: rgba(76, 201, 240, 0.1); padding: 0.75rem; border-radius: var(--border-radius-md);">
                            <div style="font-size: 0.8rem; color: var(--gray-400);">Complexidade</div>
                            <div style="font-size: 1rem; color: var(--quantum-neon); font-family: var(--font-mono);">
                                ${desc.complexity}
                            </div>
                        </div>
                        <div style="background: rgba(76, 201, 240, 0.1); padding: 0.75rem; border-radius: var(--border-radius-md);">
                            <div style="font-size: 0.8rem; color: var(--gray-400);">Qubits</div>
                            <div style="font-size: 1rem; color: var(--quantum-neon); font-family: var(--font-mono);">
                                ${desc.qubits}
                            </div>
                        </div>
                        <div style="background: rgba(76, 201, 240, 0.1); padding: 0.75rem; border-radius: var(--border-radius-md);">
                            <div style="font-size: 0.8rem; color: var(--gray-400);">Aplicações</div>
                            <div style="font-size: 0.9rem; color: var(--gray-300);">
                                ${desc.applications}
                            </div>
                        </div>
                    </div>
                `;
                
                // Mostra/oculta parâmetros específicos
                document.getElementById('algorithmParams').style.display = 
                    algorithm === 'grover' || algorithm === 'shor' ? 'block' : 'none';
                
                document.getElementById('groverParams').style.display = 
                    algorithm === 'grover' ? 'block' : 'none';
                
                document.getElementById('shorParams').style.display = 
                    algorithm === 'shor' ? 'block' : 'none';
            }
            
            async executeAlgorithm() {
                const algorithm = document.getElementById('algorithmSelect').value;
                const params = {};
                
                // Coleta parâmetros específicos
                if (algorithm === 'grover') {
                    params.target_state = document.getElementById('targetState').value;
                    params.iterations = parseInt(document.getElementById('groverIterations').value);
                } else if (algorithm === 'shor') {
                    params.number = parseInt(document.getElementById('shorNumber').value);
                }
                
                // Mostra loading
                this.showLoading('Executando algoritmo quântico...');
                
                // Simula delay de execução
                await new Promise(resolve => setTimeout(resolve, 1500));
                
                try {
                    const result = this.system.runQuantumAlgorithm(algorithm, params);
                    
                    // Atualiza interface
                    this.updateMainMetrics();
                    this.showAlgorithmResults(result);
                    
                    this.showAlert(`Algoritmo ${algorithm} executado com sucesso!`, 'success');
                } catch (error) {
                    this.showAlert(error.message, 'danger');
                } finally {
                    this.hideLoading();
                }
            }
            
            showAlgorithmResults(result) {
                const container = document.getElementById('algorithmResults');
                const output = document.getElementById('resultOutput');
                
                // Formata resultado
                output.textContent = JSON.stringify(result, null, 2);
                
                // Cria gráfico se houver dados de probabilidade
                if (result.measurements || result.superposition_states) {
                    this.createResultsChart(result);
                }
                
                container.style.display = 'block';
                
                // Scroll para resultados
                container.scrollIntoView({ behavior: 'smooth' });
            }
            
            updateAnalysis() {
                const stats = this.system.getExecutionStats();
                
                document.getElementById('totalExecutions').textContent = stats.total_executions;
                document.getElementById('successRate').textContent = `${stats.success_rate}%`;
                document.getElementById('avgTime').textContent = `${stats.avg_execution_time}ms`;
                document.getElementById('peakQubits').textContent = stats.peak_qubits;
                
                // Cálculos simulados
                const coherence = 95.2 + (Math.random() * 2 - 1);
                const entropy = 0.85 + (Math.random() * 0.3 - 0.15);
                const interference = 0.72 + (Math.random() * 0.2 - 0.1);
                
                document.getElementById('coherenceRate').textContent = `${coherence.toFixed(1)}%`;
                document.getElementById('avgEntropy').textContent = entropy.toFixed(2);
                document.getElementById('avgInterference').textContent = interference.toFixed(2);
            }
            
            initCharts() {
                // Gráfico de execuções
                const execCtx = document.getElementById('executionChart').getContext('2d');
                this.charts.executionChart = new Chart(execCtx, {
                    type: 'line',
                    data: {
                        labels: [],
                        datasets: [{
                            label: 'Execuções por Dia',
                            data: [],
                            borderColor: 'rgba(76, 201, 240, 1)',
                            backgroundColor: 'rgba(76, 201, 240, 0.1)',
                            borderWidth: 2,
                            fill: true,
                            tension: 0.4
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                labels: {
                                    color: 'rgba(255, 255, 255, 0.8)'
                                }
                            }
                        },
                        scales: {
                            x: {
                                grid: {
                                    color: 'rgba(255, 255, 255, 0.1)'
                                },
                                ticks: {
                                    color: 'rgba(255, 255, 255, 0.6)'
                                }
                            },
                            y: {
                                grid: {
                                    color: 'rgba(255, 255, 255, 0.1)'
                                },
                                ticks: {
                                    color: 'rgba(255, 255, 255, 0.6)'
                                }
                            }
                        }
                    }
                });
                
                // Gráfico de resultados
                const resultsCtx = document.getElementById('resultsChart').getContext('2d');
                this.charts.resultsChart = new Chart(resultsCtx, {
                    type: 'bar',
                    data: {
                        labels: [],
                        datasets: [{
                            label: 'Probabilidade',
                            data: [],
                            backgroundColor: 'rgba(0, 255, 157, 0.6)',
                            borderColor: 'rgba(0, 255, 157, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                labels: {
                                    color: 'rgba(255, 255, 255, 0.8)'
                                }
                            }
                        },
                        scales: {
                            x: {
                                grid: {
                                    color: 'rgba(255, 255, 255, 0.1)'
                                },
                                ticks: {
                                    color: 'rgba(255, 255, 255, 0.6)'
                                }
                            },
                            y: {
                                beginAtZero: true,
                                max: 1,
                                grid: {
                                    color: 'rgba(255, 255, 255, 0.1)'
                                },
                                ticks: {
                                    color: 'rgba(255, 255, 255, 0.6)',
                                    callback: function(value) {
                                        return (value * 100).toFixed(0) + '%';
                                    }
                                }
                            }
                        }
                    }
                });
            }
            
            updateExecutionChart() {
                // Gera dados simulados para o histórico
                const labels = [];
                const data = [];
                
                for (let i = 29; i >= 0; i--) {
                    const date = new Date();
                    date.setDate(date.getDate() - i);
                    labels.push(date.toLocaleDateString());
                    data.push(Math.floor(Math.random() * 30) + 10);
                }
                
                this.charts.executionChart.data.labels = labels;
                this.charts.executionChart.data.datasets[0].data = data;
                this.charts.executionChart.update();
            }
            
            createResultsChart(result) {
                let labels = [];
                let data = [];
                
                if (result.measurements) {
                    labels = result.measurements.map(m => m.state);
                    data = result.measurements.map(m => m.probability);
                } else if (result.superposition_states) {
                    // Limita a 8 estados para o gráfico
                    const states = result.superposition_states.slice(0, 8);
                    labels = states.map(s => s.state);
                    data = states.map(s => s.probability);
                }
                
                this.charts.resultsChart.data.labels = labels;
                this.charts.resultsChart.data.datasets[0].data = data;
                this.charts.resultsChart.update();
            }
            
            showAlert(message, type = 'info') {
                const container = document.getElementById('systemAlerts');
                
                const alert = document.createElement('div');
                alert.className = `system-alert alert-${type}`;
                alert.innerHTML = `
                    <i class="fas fa-${this.getAlertIcon(type)}"></i>
                    <div style="flex: 1;">${message}</div>
                    <button onclick="this.parentElement.remove()" style="background: none; border: none; color: inherit; cursor: pointer;">
                        <i class="fas fa-times"></i>
                    </button>
                `;
                
                container.appendChild(alert);
                
                // Remove após 5 segundos
                setTimeout(() => {
                    if (alert.parentNode) {
                        alert.style.animation = 'slideOut 0.3s ease';
                        setTimeout(() => {
                            if (alert.parentNode) {
                                alert.parentNode.removeChild(alert);
                            }
                        }, 300);
                    }
                }, 5000);
            }
            
            getAlertIcon(type) {
                switch(type) {
                    case 'success': return 'check-circle';
                    case 'warning': return 'exclamation-triangle';
                    case 'danger': return 'exclamation-circle';
                    default: return 'info-circle';
                }
            }
            
            showLoading(message = 'Processando...') {
                const modal = document.getElementById('loadingModal');
                document.getElementById('loadingMessage').textContent = message;
                modal.style.display = 'flex';
            }
            
            hideLoading() {
                const modal = document.getElementById('loadingModal');
                modal.style.display = 'none';
            }
            
            startAutoUpdate() {
                // Atualiza métricas a cada 10 segundos
                setInterval(() => {
                    this.updateMainMetrics();
                    if (this.currentTab === 'analysis') {
                        this.updateAnalysis();
                        this.updateExecutionChart();
                    }
                }, 10000);
            }
        }
        
        // ============================================
        // INICIALIZAÇÃO
        // ============================================
        
        document.addEventListener('DOMContentLoaded', () => {
            try {
                // Inicializa sistema quântico
                const quantumSystem = new QuantumSystem();
                
                // Inicializa interface
                const quantumUI = new QuantumSystemUI(quantumSystem);
                
                // Carrega última tab
                const lastTab = localStorage.getItem('quantum_last_tab');
                if (lastTab) {
                    quantumUI.switchTab(lastTab);
                }
                
                // Mostra alerta de inicialização
                setTimeout(() => {
                    quantumUI.showAlert('Sistema Quântico inicializado com sucesso. Status: Ativo.', 'success');
                }, 1000);
                
                // Expõe para debugging
                window.quantumSystem = quantumSystem;
                window.quantumUI = quantumUI;
                
            } catch (error) {
                console.error('Erro fatal na inicialização do sistema quântico:', error);
                
                const errorHTML = `
                    <div style="background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.3); 
                         border-radius: var(--border-radius-md); padding: var(--spacing-md); color: var(--danger);
                         margin: 2rem;">
                        <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.5rem;">
                            <i class="fas fa-exclamation-triangle"></i>
                            <strong>Erro fatal na inicialização do sistema quântico</strong>
                        </div>
                        <div style="font-family: var(--font-mono); font-size: 0.875rem; color: var(--gray-400);">
                            ${error.toString()}
                        </div>
                        <div style="margin-top: 1rem; display: flex; gap: 0.5rem;">
                            <button onclick="location.reload()" 
                                    style="background: var(--danger); border: none; 
                                           color: white; padding: 0.75rem 1.5rem; border-radius: var(--border-radius-sm); 
                                           cursor: pointer; font-size: 0.875rem; font-weight: 600;">
                                <i class="fas fa-redo"></i> Reiniciar Sistema
                            </button>
                            <button onclick="localStorage.clear(); location.reload()" 
                                    style="background: transparent; border: 1px solid var(--danger); 
                                           color: var(--danger); padding: 0.75rem 1.5rem; border-radius: var(--border-radius-sm); 
                                           cursor: pointer; font-size: 0.875rem; font-weight: 600;">
                                <i class="fas fa-trash"></i> Limpar Dados
                            </button>
                        </div>
                    </div>
                `;
                
                document.querySelector('.app-container').innerHTML = errorHTML;
            }
        });
    </script>
</body>
</html>