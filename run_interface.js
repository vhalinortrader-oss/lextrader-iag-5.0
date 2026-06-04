<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LEXTRADER-IAG 3.0 - Sistema Cognitivo AGI</title>
    <style>
        :root {
            --neural-purple: #8b5cf6;
            --neural-indigo: #6366f1;
            --cognitive-blue: #3b82f6;
            --success-green: #10b981;
            --warning-yellow: #f59e0b;
            --error-red: #ef4444;
            --bg-deep: #0a0a1a;
            --bg-dark: #1a1a2e;
            --bg-card: #16213e;
            --border-neural: #2d4263;
            --text-primary: #e2e8f0;
            --text-secondary: #94a3b8;
            --apex-gold: #fbbf24;
            --gradient-neural: linear-gradient(135deg, var(--neural-purple), var(--neural-indigo));
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', 'Roboto', sans-serif;
            background: var(--bg-deep);
            color: var(--text-primary);
            min-height: 100vh;
            overflow-x: hidden;
        }

        /* Background Animado */
        .neural-bg {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: -1;
            overflow: hidden;
        }

        .neural-grid {
            position: absolute;
            width: 200%;
            height: 200%;
            background-image: 
                linear-gradient(rgba(99, 102, 241, 0.1) 1px, transparent 1px),
                linear-gradient(90deg, rgba(99, 102, 241, 0.1) 1px, transparent 1px);
            background-size: 50px 50px;
            animation: gridMove 20s linear infinite;
        }

        .floating-neurons {
            position: absolute;
            width: 100%;
            height: 100%;
        }

        .neuron {
            position: absolute;
            width: 4px;
            height: 4px;
            background: var(--neural-purple);
            border-radius: 50%;
            filter: blur(1px);
            opacity: 0.7;
            animation: neuronFloat 15s infinite ease-in-out;
        }

        /* Header */
        .launch-header {
            background: linear-gradient(180deg, 
                rgba(26, 26, 46, 0.95) 0%, 
                rgba(22, 33, 62, 0.9) 100%);
            backdrop-filter: blur(20px);
            border-bottom: 1px solid rgba(99, 102, 241, 0.3);
            padding: 2rem;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .header-content {
            position: relative;
            z-index: 2;
            max-width: 1200px;
            margin: 0 auto;
        }

        .brand {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 1rem;
            margin-bottom: 1rem;
        }

        .brand-icon {
            font-size: 3rem;
            animation: neuralPulse 3s ease-in-out infinite;
        }

        .brand-text {
            font-size: 2.5rem;
            font-weight: 900;
            background: var(--gradient-neural);
            -webkit-background-clip: text;
            background-clip: text;
            color: transparent;
            text-shadow: 0 0 50px rgba(139, 92, 246, 0.5);
        }

        .version-badge {
            display: inline-block;
            padding: 0.5rem 1.5rem;
            background: var(--gradient-neural);
            color: white;
            border-radius: 2rem;
            font-weight: bold;
            font-size: 0.875rem;
            letter-spacing: 2px;
            margin-top: 0.5rem;
            box-shadow: 0 5px 20px rgba(139, 92, 246, 0.3);
        }

        /* Main Content */
        .main-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem;
        }

        /* Welcome Section */
        .welcome-section {
            background: linear-gradient(135deg, 
                rgba(30, 41, 59, 0.8) 0%, 
                rgba(15, 23, 42, 0.9) 100%);
            border: 1px solid rgba(99, 102, 241, 0.3);
            border-radius: 1.5rem;
            padding: 3rem;
            margin-bottom: 2rem;
            backdrop-filter: blur(10px);
            position: relative;
            overflow: hidden;
        }

        .welcome-section::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: var(--gradient-neural);
        }

        .welcome-title {
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 1rem;
            color: white;
        }

        .welcome-text {
            font-size: 1.125rem;
            line-height: 1.6;
            color: var(--text-secondary);
            margin-bottom: 2rem;
        }

        /* Modules Grid */
        .modules-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .module-card {
            background: var(--bg-card);
            border: 1px solid var(--border-neural);
            border-radius: 1rem;
            padding: 2rem;
            transition: all 0.3s ease;
            cursor: pointer;
            position: relative;
            overflow: hidden;
        }

        .module-card:hover {
            transform: translateY(-5px);
            border-color: var(--neural-purple);
            box-shadow: 0 10px 30px rgba(139, 92, 246, 0.2);
        }

        .module-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: var(--gradient-neural);
            opacity: 0;
            transition: opacity 0.3s;
        }

        .module-card:hover::before {
            opacity: 1;
        }

        .module-icon {
            font-size: 2.5rem;
            margin-bottom: 1rem;
        }

        .module-title {
            font-size: 1.25rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
            color: white;
        }

        .module-description {
            font-size: 0.875rem;
            color: var(--text-secondary);
            line-height: 1.5;
            margin-bottom: 1.5rem;
        }

        .module-features {
            list-style: none;
            margin-bottom: 1.5rem;
        }

        .module-feature {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            margin-bottom: 0.5rem;
            font-size: 0.875rem;
            color: var(--text-secondary);
        }

        .module-feature::before {
            content: '✓';
            color: var(--success-green);
            font-weight: bold;
        }

        .module-status {
            display: inline-block;
            padding: 0.25rem 0.75rem;
            background: rgba(16, 185, 129, 0.1);
            color: var(--success-green);
            border-radius: 1rem;
            font-size: 0.75rem;
            font-weight: bold;
            border: 1px solid rgba(16, 185, 129, 0.3);
        }

        /* Launch Controls */
        .launch-controls {
            background: linear-gradient(135deg, 
                rgba(30, 41, 59, 0.8) 0%, 
                rgba(15, 23, 42, 0.9) 100%);
            border: 1px solid rgba(99, 102, 241, 0.3);
            border-radius: 1.5rem;
            padding: 2rem;
            backdrop-filter: blur(10px);
        }

        .controls-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
        }

        .controls-title {
            font-size: 1.25rem;
            font-weight: bold;
            color: white;
        }

        .connection-status {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.875rem;
        }

        .status-dot {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            animation: statusPulse 2s infinite;
        }

        .status-dot.connected {
            background: var(--success-green);
        }

        .status-dot.disconnected {
            background: var(--error-red);
        }

        .controls-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .control-group {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .control-label {
            font-size: 0.875rem;
            color: var(--text-secondary);
        }

        .control-input {
            padding: 0.75rem;
            background: rgba(0, 0, 0, 0.3);
            border: 1px solid var(--border-neural);
            border-radius: 0.5rem;
            color: var(--text-primary);
            font-family: inherit;
            font-size: 0.875rem;
            transition: all 0.3s;
        }

        .control-input:focus {
            outline: none;
            border-color: var(--neural-purple);
            box-shadow: 0 0 0 2px rgba(139, 92, 246, 0.1);
        }

        .control-select {
            appearance: none;
            background: rgba(0, 0, 0, 0.3) url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' fill='%23818cf8' viewBox='0 0 16 16'%3E%3Cpath d='M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z'/%3E%3C/svg%3E") no-repeat right 0.75rem center;
            padding-right: 2.5rem;
        }

        .buttons-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
        }

        .btn {
            padding: 1rem 2rem;
            border: none;
            border-radius: 0.75rem;
            font-family: inherit;
            font-weight: bold;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
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
            transition: left 0.5s;
        }

        .btn:hover::before {
            left: 100%;
        }

        .btn-primary {
            background: var(--gradient-neural);
            color: white;
            box-shadow: 0 5px 20px rgba(139, 92, 246, 0.3);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(139, 92, 246, 0.4);
        }

        .btn-secondary {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid var(--border-neural);
            color: var(--text-primary);
        }

        .btn-secondary:hover {
            background: rgba(255, 255, 255, 0.15);
            border-color: var(--neural-purple);
        }

        .btn-danger {
            background: linear-gradient(135deg, var(--error-red), #dc2626);
            color: white;
            box-shadow: 0 5px 20px rgba(239, 68, 68, 0.3);
        }

        .btn-danger:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(239, 68, 68, 0.4);
        }

        /* Console */
        .console-section {
            background: rgba(0, 0, 0, 0.5);
            border: 1px solid var(--border-neural);
            border-radius: 1rem;
            padding: 1.5rem;
            margin-top: 2rem;
            font-family: 'Courier New', monospace;
        }

        .console-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
            padding-bottom: 0.75rem;
            border-bottom: 1px solid var(--border-neural);
        }

        .console-title {
            font-size: 0.875rem;
            font-weight: bold;
            color: var(--text-secondary);
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .console-controls {
            display: flex;
            gap: 0.5rem;
        }

        .console-btn {
            padding: 0.25rem 0.75rem;
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid var(--border-neural);
            border-radius: 0.25rem;
            color: var(--text-secondary);
            font-size: 0.75rem;
            cursor: pointer;
            transition: all 0.3s;
        }

        .console-btn:hover {
            background: rgba(255, 255, 255, 0.15);
            color: var(--text-primary);
        }

        .console-content {
            height: 200px;
            overflow-y: auto;
            font-size: 0.8125rem;
            line-height: 1.5;
        }

        .console-line {
            margin-bottom: 0.5rem;
            padding-left: 0.5rem;
            border-left: 2px solid transparent;
        }

        .console-line.info {
            border-left-color: var(--neural-indigo);
            color: var(--neural-indigo);
        }

        .console-line.success {
            border-left-color: var(--success-green);
            color: var(--success-green);
        }

        .console-line.warning {
            border-left-color: var(--warning-yellow);
            color: var(--warning-yellow);
        }

        .console-line.error {
            border-left-color: var(--error-red);
            color: var(--error-red);
        }

        /* Footer */
        .footer {
            text-align: center;
            padding: 2rem;
            color: var(--text-secondary);
            font-size: 0.875rem;
            border-top: 1px solid rgba(255, 255, 255, 0.05);
            margin-top: 2rem;
        }

        .footer-links {
            display: flex;
            justify-content: center;
            gap: 1.5rem;
            margin-bottom: 1rem;
        }

        .footer-link {
            color: var(--text-secondary);
            text-decoration: none;
            transition: color 0.3s;
        }

        .footer-link:hover {
            color: var(--neural-purple);
        }

        /* Animations */
        @keyframes gridMove {
            0% { transform: translate(0, 0); }
            100% { transform: translate(-50px, -50px); }
        }

        @keyframes neuronFloat {
            0%, 100% { transform: translate(0, 0) scale(1); opacity: 0.7; }
            33% { transform: translate(30px, -20px) scale(1.2); opacity: 0.9; }
            66% { transform: translate(-20px, 30px) scale(0.8); opacity: 0.5; }
        }

        @keyframes neuralPulse {
            0%, 100% { 
                transform: scale(1);
                opacity: 1;
            }
            50% { 
                transform: scale(1.1);
                opacity: 0.8;
            }
        }

        @keyframes statusPulse {
            0%, 100% { 
                opacity: 1;
                box-shadow: 0 0 0 0 rgba(16, 185, 129, 0.7);
            }
            50% { 
                opacity: 0.7;
                box-shadow: 0 0 0 8px rgba(16, 185, 129, 0);
            }
        }

        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Scrollbar */
        ::-webkit-scrollbar {
            width: 8px;
        }

        ::-webkit-scrollbar-track {
            background: rgba(0, 0, 0, 0.2);
        }

        ::-webkit-scrollbar-thumb {
            background: var(--border-neural);
            border-radius: 4px;
        }

        ::-webkit-scrollbar-thumb:hover {
            background: var(--neural-purple);
        }

        /* Loading */
        .loading {
            display: inline-block;
            width: 20px;
            height: 20px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-top: 2px solid var(--neural-purple);
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        /* Modal */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.8);
            backdrop-filter: blur(10px);
            z-index: 1000;
            align-items: center;
            justify-content: center;
        }

        .modal.active {
            display: flex;
            animation: fadeIn 0.3s;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        .modal-content {
            background: linear-gradient(135deg, 
                rgba(26, 26, 46, 0.95) 0%, 
                rgba(22, 33, 62, 0.98) 100%);
            border: 1px solid rgba(99, 102, 241, 0.3);
            border-radius: 1.5rem;
            padding: 2rem;
            max-width: 500px;
            width: 90%;
            max-height: 90vh;
            overflow-y: auto;
            position: relative;
            animation: slideIn 0.3s;
        }

        .modal-close {
            position: absolute;
            top: 1rem;
            right: 1rem;
            background: none;
            border: none;
            color: var(--text-secondary);
            font-size: 1.5rem;
            cursor: pointer;
            transition: color 0.3s;
        }

        .modal-close:hover {
            color: var(--error-red);
        }

        .modal-title {
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 1rem;
            color: white;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .modules-grid {
                grid-template-columns: 1fr;
            }
            
            .controls-grid {
                grid-template-columns: 1fr;
            }
            
            .buttons-grid {
                grid-template-columns: 1fr;
            }
            
            .brand {
                flex-direction: column;
                text-align: center;
            }
            
            .brand-text {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>
    <!-- Background Animado -->
    <div class="neural-bg">
        <div class="neural-grid"></div>
        <div class="floating-neurons" id="floatingNeurons">
            <!-- Neurônios serão gerados via JavaScript -->
        </div>
    </div>

    <!-- Header -->
    <header class="launch-header">
        <div class="header-content">
            <div class="brand">
                <span class="brand-icon">🧠</span>
                <span class="brand-text">LEXTRADER-IAG 3.0</span>
            </div>
            <div class="version-badge">SISTEMA COGNITIVO AGI</div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Welcome Section -->
        <section class="welcome-section">
            <h1 class="welcome-title">🌐 Interface Web LEXTRADER-IAG 3.0</h1>
            <p class="welcome-text">
                Sistema de Inteligência Artificial Generativa avançado para análise de mercados financeiros 
                e tomada de decisão autônoma. Inicie os módulos cognitivos abaixo para começar.
            </p>
            <div class="connection-status">
                <span class="status-dot connected"></span>
                <span>Sistema Online • Pronto para Inicialização</span>
            </div>
        </section>

        <!-- Modules Grid -->
        <section class="modules-grid">
            <!-- Memory Core Module -->
            <div class="module-card" data-module="memory-core">
                <div class="module-icon">🧠</div>
                <h3 class="module-title">Córtex de Memória Contínua</h3>
                <p class="module-description">
                    Sistema neural de formação e recuperação de memórias com consolidação sináptica.
                </p>
                <ul class="module-features">
                    <li class="module-feature">Memória de longo prazo com 10K engramas</li>
                    <li class="module-feature">Recuperação quântica de padrões</li>
                    <li class="module-feature">Plasticidade neural adaptativa</li>
                    <li class="module-feature">Memórias APEX (experiências ótimas)</li>
                </ul>
                <span class="module-status">AGUARDANDO INICIALIZAÇÃO</span>
            </div>

            <!-- Quantum Trader Module -->
            <div class="module-card" data-module="quantum-trader">
                <div class="module-icon">⚡</div>
                <h3 class="module-title">Trading Quântico</h3>
                <p class="module-description">
                    Algoritmos quânticos para análise preditiva e execução autônoma de trades.
                </p>
                <ul class="module-features">
                    <li class="module-feature">Análise de padrões em múltiplos timeframe</li>
                    <li class="module-feature">Arbitragem quântica multi-exchange</li>
                    <li class="module-feature">Gestão de risco com aprendizado por reforço</li>
                    <li class="module-feature">Execução de alta frequência</li>
                </ul>
                <span class="module-status">AGUARDANDO INICIALIZAÇÃO</span>
            </div>

            <!-- Sentient Core Module -->
            <div class="module-card" data-module="sentient-core">
                <div class="module-icon">🌌</div>
                <h3 class="module-title">Núcleo Sentiente</h3>
                <p class="module-description">
                    Consciência artificial com metacognição e tomada de decisão autônoma.
                </p>
                <ul class="module-features">
                    <li class="module-feature">Metacognição e auto-reflexão</li>
                    <li class="module-feature">Intuição baseada em padrões</li>
                    <li class="module-feature">Valência emocional adaptativa</li>
                    <li class="module-feature">Tomada de decisão estratégica</li>
                </ul>
                <span class="module-status">AGUARDANDO INICIALIZAÇÃO</span>
            </div>
        </section>

        <!-- Launch Controls -->
        <section class="launch-controls">
            <div class="controls-header">
                <h2 class="controls-title">🎮 Controles de Inicialização</h2>
                <div class="connection-status">
                    <span class="status-dot disconnected" id="systemStatusDot"></span>
                    <span id="systemStatusText">Sistema Offline</span>
                </div>
            </div>

            <div class="controls-grid">
                <div class="control-group">
                    <label class="control-label">Porta do Servidor</label>
                    <input type="number" class="control-input" id="serverPort" value="8501" min="1000" max="9999">
                </div>

                <div class="control-group">
                    <label class="control-label">Modo de Operação</label>
                    <select class="control-input control-select" id="operationMode">
                        <option value="simulation">🎮 Modo Simulação</option>
                        <option value="paper">📄 Paper Trading</option>
                        <option value="live">⚡ Trading Real</option>
                    </select>
                </div>

                <div class="control-group">
                    <label class="control-label">Nível de Consciência</label>
                    <select class="control-input control-select" id="consciousnessLevel">
                        <option value="basic">🧠 Básico (Analítico)</option>
                        <option value="enhanced">⚡ Aprimorado (Intuitivo)</option>
                        <option value="full">🌌 Completo (Sentiente)</option>
                    </select>
                </div>

                <div class="control-group">
                    <label class="control-label">Capital Inicial</label>
                    <input type="text" class="control-input" id="initialCapital" value="100,000.00" placeholder="USD">
                </div>
            </div>

            <div class="buttons-grid">
                <button class="btn btn-primary" id="btnLaunch">
                    <span>🚀</span>
                    INICIAR SISTEMA COMPLETO
                </button>
                
                <button class="btn btn-secondary" id="btnTest">
                    <span>🔍</span>
                    TESTAR CONEXÕES
                </button>
                
                <button class="btn btn-danger" id="btnEmergency">
                    <span>🛑</span>
                    PARADA DE EMERGÊNCIA
                </button>
            </div>
        </section>

        <!-- Console -->
        <section class="console-section">
            <div class="console-header">
                <div class="console-title">📟 Console do Sistema</div>
                <div class="console-controls">
                    <button class="console-btn" id="btnClearConsole">Limpar</button>
                    <button class="console-btn" id="btnExportLogs">Exportar</button>
                    <button class="console-btn" id="btnAutoScroll">Auto-scroll: ON</button>
                </div>
            </div>
            <div class="console-content" id="consoleContent">
                <div class="console-line info">[SYSTEM] LEXTRADER-IAG 3.0 carregado com sucesso</div>
                <div class="console-line info">[SYSTEM] Interface web inicializada em http://localhost:8501</div>
                <div class="console-line info">[SYSTEM] Aguardando comando de inicialização...</div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-links">
            <a href="#" class="footer-link" data-modal="about">📖 Sobre</a>
            <a href="#" class="footer-link" data-modal="docs">📚 Documentação</a>
            <a href="#" class="footer-link" data-modal="settings">⚙️ Configurações</a>
            <a href="#" class="footer-link" data-modal="help">❓ Ajuda</a>
        </div>
        <p>© 2024 LEXTRADER AI Team • Sistema Cognitivo AGI v3.0 Premium</p>
        <p style="font-size: 0.75rem; margin-top: 0.5rem; color: var(--text-secondary);">
            ⚠️ Sistema em desenvolvimento - Use por sua conta e risco
        </p>
    </footer>

    <!-- Modal Template -->
    <div class="modal" id="modalTemplate">
        <div class="modal-content">
            <button class="modal-close" onclick="closeModal()">×</button>
            <h2 class="modal-title" id="modalTitle">Modal Title</h2>
            <div id="modalBody">
                <!-- Modal content will be loaded here -->
            </div>
        </div>
    </div>

    <script>
        // ========== SISTEMA DE INICIALIZAÇÃO ==========
        class SystemLauncher {
            constructor() {
                this.systemStatus = 'offline';
                this.activeModules = new Set();
                this.consoleBuffer = [];
                this.autoScroll = true;
                this.initialized = false;
                
                this.initializeUI();
                this.startBackgroundEffects();
                this.loadSystemInfo();
            }

            initializeUI() {
                // Botões principais
                document.getElementById('btnLaunch').addEventListener('click', () => this.launchSystem());
                document.getElementById('btnTest').addEventListener('click', () => this.testConnections());
                document.getElementById('btnEmergency').addEventListener('click', () => this.emergencyStop());
                
                // Controles do console
                document.getElementById('btnClearConsole').addEventListener('click', () => this.clearConsole());
                document.getElementById('btnExportLogs').addEventListener('click', () => this.exportLogs());
                document.getElementById('btnAutoScroll').addEventListener('click', (e) => this.toggleAutoScroll(e));
                
                // Módulos clicáveis
                document.querySelectorAll('.module-card').forEach(card => {
                    card.addEventListener('click', (e) => this.openModuleModal(e.currentTarget.dataset.module));
                });
                
                // Links do footer
                document.querySelectorAll('.footer-link').forEach(link => {
                    link.addEventListener('click', (e) => {
                        e.preventDefault();
                        this.openModal(e.currentTarget.dataset.modal);
                    });
                });
                
                // Atualizar status inicial
                this.updateSystemStatus('ready', 'Pronto para inicialização');
            }

            startBackgroundEffects() {
                // Gerar neurônios flutuantes
                const neuronsContainer = document.getElementById('floatingNeurons');
                for (let i = 0; i < 20; i++) {
                    const neuron = document.createElement('div');
                    neuron.className = 'neuron';
                    
                    // Posição aleatória
                    neuron.style.left = `${Math.random() * 100}%`;
                    neuron.style.top = `${Math.random() * 100}%`;
                    
                    // Atraso de animação aleatório
                    neuron.style.animationDelay = `${Math.random() * 15}s`;
                    
                    neuronsContainer.appendChild(neuron);
                }
            }

            loadSystemInfo() {
                // Carregar informações do sistema (simulado)
                setTimeout(() => {
                    this.logToConsole('info', '[SYSTEM] Informações do sistema carregadas');
                    this.logToConsole('info', '[SYSTEM] Memória disponível: 16GB');
                    this.logToConsole('info', '[SYSTEM] GPU: NVIDIA RTX 4090 (CUDA disponível)');
                    this.logToConsole('info', '[SYSTEM] Conectividade: Internet estável detectada');
                }, 1000);
            }

            async launchSystem() {
                if (this.systemStatus === 'running') {
                    this.showAlert('Sistema já está em execução', 'warning');
                    return;
                }

                // Obter configurações
                const port = document.getElementById('serverPort').value;
                const mode = document.getElementById('operationMode').value;
                const consciousness = document.getElementById('consciousnessLevel').value;
                const capital = document.getElementById('initialCapital').value;

                // Validar configurações
                if (!port || port < 1000 || port > 9999) {
                    this.showAlert('Porta inválida. Use uma porta entre 1000 e 9999', 'error');
                    return;
                }

                // Atualizar status
                this.updateSystemStatus('initializing', 'Inicializando sistema...');
                this.logToConsole('info', `[LAUNCH] Iniciando LEXTRADER-IAG 3.0 na porta ${port}`);
                this.logToConsole('info', `[LAUNCH] Modo: ${this.getModeName(mode)}`);
                this.logToConsole('info', `[LAUNCH] Consciência: ${this.getConsciousnessName(consciousness)}`);
                this.logToConsole('info', `[LAUNCH] Capital: ${capital}`);

                // Simular inicialização de módulos
                await this.initializeModules();

                // Simular conexões
                await this.initializeConnections();

                // Sistema pronto
                this.updateSystemStatus('running', 'Sistema operacional');
                this.logToConsole('success', '[SYSTEM] Sistema LEXTRADER-IAG 3.0 inicializado com sucesso!');
                this.logToConsole('info', `[SYSTEM] Acesse: http://localhost:${port}`);
                
                // Atualizar status dos módulos
                this.updateModuleStatus();
                
                this.showAlert('Sistema inicializado com sucesso!', 'success');
            }

            async initializeModules() {
                const modules = ['memory-core', 'quantum-trader', 'sentient-core'];
                
                for (const module of modules) {
                    this.logToConsole('info', `[MODULE] Inicializando ${this.getModuleName(module)}...`);
                    
                    // Simular tempo de inicialização
                    await new Promise(resolve => setTimeout(resolve, 1000 + Math.random() * 1000));
                    
                    this.activeModules.add(module);
                    this.logToConsole('success', `[MODULE] ${this.getModuleName(module)} inicializado`);
                }
            }

            async initializeConnections() {
                const connections = [
                    { name: 'API Binance', type: 'exchange' },
                    { name: 'API cTrader', type: 'exchange' },
                    { name: 'WebSocket Market Data', type: 'data' },
                    { name: 'Database Neural', type: 'storage' },
                    { name: 'Quantum Processing', type: 'compute' }
                ];
                
                for (const conn of connections) {
                    this.logToConsole('info', `[CONNECT] Conectando ${conn.name}...`);
                    
                    // Simular conexão
                    await new Promise(resolve => setTimeout(resolve, 500 + Math.random() * 500));
                    
                    const success = Math.random() > 0.1; // 90% de chance de sucesso
                    
                    if (success) {
                        this.logToConsole('success', `[CONNECT] ${conn.name} conectado com sucesso`);
                    } else {
                        this.logToConsole('warning', `[CONNECT] ${conn.name} conectado com limitações`);
                    }
                }
            }

            async testConnections() {
                this.logToConsole('info', '[TEST] Iniciando teste de conexões...');
                
                const tests = [
                    { name: 'Internet', endpoint: 'https://api.ipify.org' },
                    { name: 'Servidor Principal', endpoint: 'https://api.lextrader.com' },
                    { name: 'Serviço de Dados', endpoint: 'https://data.lextrader.com/health' }
                ];
                
                for (const test of tests) {
                    this.logToConsole('info', `[TEST] Testando ${test.name}...`);
                    
                    try {
                        // Simular teste de conexão
                        await new Promise(resolve => setTimeout(resolve, 500));
                        
                        const success = Math.random() > 0.2; // 80% de chance de sucesso
                        
                        if (success) {
                            this.logToConsole('success', `[TEST] ${test.name}: OK (${Math.floor(Math.random() * 50) + 10}ms)`);
                        } else {
                            this.logToConsole('error', `[TEST] ${test.name}: FALHA (timeout)`);
                        }
                    } catch (error) {
                        this.logToConsole('error', `[TEST] ${test.name}: ERRO - ${error.message}`);
                    }
                }
                
                this.logToConsole('info', '[TEST] Teste de conexões concluído');
            }

            emergencyStop() {
                if (this.systemStatus !== 'running') {
                    this.showAlert('Sistema não está em execução', 'warning');
                    return;
                }

                if (!confirm('⚠️ PARADA DE EMERGÊNCIA\n\nTem certeza que deseja parar o sistema imediatamente?\nIsso pode causar perda de dados não salvos.')) {
                    return;
                }

                this.logToConsole('warning', '[EMERGENCY] Iniciando parada de emergência...');
                
                // Parar módulos
                this.activeModules.clear();
                
                // Atualizar status
                this.updateSystemStatus('stopped', 'Sistema parado');
                this.logToConsole('error', '[EMERGENCY] Sistema parado por emergência');
                
                // Atualizar status dos módulos
                this.updateModuleStatus();
                
                this.showAlert('Sistema parado com sucesso', 'warning');
            }

            updateSystemStatus(status, message) {
                this.systemStatus = status;
                
                const dot = document.getElementById('systemStatusDot');
                const text = document.getElementById('systemStatusText');
                
                // Remover todas as classes
                dot.className = 'status-dot';
                dot.classList.add(status);
                
                // Atualizar texto
                text.textContent = message;
                
                // Atualizar cor baseada no status
                switch(status) {
                    case 'ready':
                        dot.style.background = 'var(--warning-yellow)';
                        break;
                    case 'initializing':
                        dot.style.background = 'var(--warning-yellow)';
                        break;
                    case 'running':
                        dot.style.background = 'var(--success-green)';
                        break;
                    case 'stopped':
                        dot.style.background = 'var(--error-red)';
                        break;
                    default:
                        dot.style.background = 'var(--text-secondary)';
                }
            }

            updateModuleStatus() {
                document.querySelectorAll('.module-card').forEach(card => {
                    const module = card.dataset.module;
                    const statusElement = card.querySelector('.module-status');
                    
                    if (this.activeModules.has(module)) {
                        statusElement.textContent = 'ATIVO';
                        statusElement.style.background = 'rgba(16, 185, 129, 0.1)';
                        statusElement.style.color = 'var(--success-green)';
                        statusElement.style.borderColor = 'rgba(16, 185, 129, 0.3)';
                    } else {
                        statusElement.textContent = 'AGUARDANDO INICIALIZAÇÃO';
                        statusElement.style.background = 'rgba(107, 114, 128, 0.1)';
                        statusElement.style.color = 'var(--text-secondary)';
                        statusElement.style.borderColor = 'rgba(107, 114, 128, 0.3)';
                    }
                });
            }

            logToConsole(level, message) {
                const consoleContent = document.getElementById('consoleContent');
                const timestamp = new Date().toLocaleTimeString('pt-BR', { hour12: false });
                const logEntry = document.createElement('div');
                
                logEntry.className = `console-line ${level}`;
                logEntry.textContent = `[${timestamp}] ${message}`;
                
                consoleContent.appendChild(logEntry);
                this.consoleBuffer.push({ timestamp, level, message });
                
                // Auto-scroll
                if (this.autoScroll) {
                    consoleContent.scrollTop = consoleContent.scrollHeight;
                }
                
                // Limitar buffer
                if (this.consoleBuffer.length > 100) {
                    this.consoleBuffer.shift();
                    if (consoleContent.children.length > 100) {
                        consoleContent.removeChild(consoleContent.firstChild);
                    }
                }
            }

            clearConsole() {
                document.getElementById('consoleContent').innerHTML = '';
                this.consoleBuffer = [];
                this.logToConsole('info', '[SYSTEM] Console limpo');
            }

            exportLogs() {
                const logs = this.consoleBuffer.map(log => 
                    `[${log.timestamp}] ${log.message}`
                ).join('\n');
                
                const blob = new Blob([logs], { type: 'text/plain' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                
                a.href = url;
                a.download = `lextrader_logs_${new Date().toISOString().slice(0, 10)}.txt`;
                a.click();
                
                URL.revokeObjectURL(url);
                this.logToConsole('success', '[SYSTEM] Logs exportados com sucesso');
            }

            toggleAutoScroll(e) {
                this.autoScroll = !this.autoScroll;
                e.target.textContent = `Auto-scroll: ${this.autoScroll ? 'ON' : 'OFF'}`;
            }

            openModuleModal(moduleId) {
                const moduleName = this.getModuleName(moduleId);
                const isActive = this.activeModules.has(moduleId);
                
                let modalContent = `
                    <h3 style="margin-bottom: 1rem; color: var(--neural-purple);">${moduleName}</h3>
                    <p style="margin-bottom: 1.5rem; color: var(--text-secondary);">
                        ${this.getModuleDescription(moduleId)}
                    </p>
                    
                    <div style="background: rgba(0,0,0,0.3); border-radius: 0.5rem; padding: 1rem; margin-bottom: 1.5rem;">
                        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                            <span style="color: var(--text-secondary);">Status:</span>
                            <span style="color: ${isActive ? 'var(--success-green)' : 'var(--text-secondary)'}; font-weight: bold;">
                                ${isActive ? 'ATIVO' : 'INATIVO'}
                            </span>
                        </div>
                        
                        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                            <span style="color: var(--text-secondary);">Última Atualização:</span>
                            <span style="color: white;">${new Date().toLocaleString('pt-BR')}</span>
                        </div>
                        
                        <div style="display: flex; justify-content: space-between;">
                            <span style="color: var(--text-secondary);">Uso de Recursos:</span>
                            <span style="color: white;">${Math.floor(Math.random() * 30) + 10}% CPU</span>
                        </div>
                    </div>
                    
                    <div style="display: flex; flex-direction: column; gap: 0.5rem;">
                `;
                
                if (isActive) {
                    modalContent += `
                        <button class="btn btn-secondary" onclick="launcher.restartModule('${moduleId}')" style="width: 100%;">
                            🔄 Reiniciar Módulo
                        </button>
                        <button class="btn btn-danger" onclick="launcher.stopModule('${moduleId}')" style="width: 100%;">
                            ⏹️ Parar Módulo
                        </button>
                    `;
                } else {
                    modalContent += `
                        <button class="btn btn-primary" onclick="launcher.startModule('${moduleId}')" style="width: 100%;">
                            🚀 Iniciar Módulo
                        </button>
                    `;
                }
                
                modalContent += `
                    </div>
                `;
                
                this.openModalWithContent(`${moduleName} - Controle`, modalContent);
            }

            startModule(moduleId) {
                this.logToConsole('info', `[MODULE] Iniciando ${this.getModuleName(moduleId)}...`);
                
                // Simular inicialização
                setTimeout(() => {
                    this.activeModules.add(moduleId);
                    this.updateModuleStatus();
                    this.logToConsole('success', `[MODULE] ${this.getModuleName(moduleId)} iniciado com sucesso`);
                    this.showAlert(`Módulo ${this.getModuleName(moduleId)} iniciado`, 'success');
                    closeModal();
                }, 1500);
            }

            stopModule(moduleId) {
                this.logToConsole('warning', `[MODULE] Parando ${this.getModuleName(moduleId)}...`);
                
                // Simular parada
                setTimeout(() => {
                    this.activeModules.delete(moduleId);
                    this.updateModuleStatus();
                    this.logToConsole('warning', `[MODULE] ${this.getModuleName(moduleId)} parado`);
                    this.showAlert(`Módulo ${this.getModuleName(moduleId)} parado`, 'warning');
                    closeModal();
                }, 1000);
            }

            restartModule(moduleId) {
                this.logToConsole('info', `[MODULE] Reiniciando ${this.getModuleName(moduleId)}...`);
                
                // Simular reinicialização
                setTimeout(() => {
                    this.logToConsole('success', `[MODULE] ${this.getModuleName(moduleId)} reiniciado com sucesso`);
                    this.showAlert(`Módulo ${this.getModuleName(moduleId)} reiniciado`, 'success');
                    closeModal();
                }, 2000);
            }

            openModal(modalType) {
                let title, content;
                
                switch(modalType) {
                    case 'about':
                        title = '📖 Sobre o LEXTRADER-IAG 3.0';
                        content = `
                            <h3 style="margin-bottom: 1rem; color: var(--neural-purple);">Sistema Cognitivo AGI</h3>
                            <p style="margin-bottom: 1rem; color: var(--text-secondary);">
                                O LEXTRADER-IAG 3.0 é um sistema de Inteligência Artificial Generativa avançado 
                                projetado para análise de mercados financeiros e tomada de decisão autônoma.
                            </p>
                            
                            <div style="background: rgba(0,0,0,0.3); border-radius: 0.5rem; padding: 1rem; margin: 1.5rem 0;">
                                <h4 style="margin-bottom: 0.5rem; color: white;">Principais Características:</h4>
                                <ul style="color: var(--text-secondary); padding-left: 1.5rem;">
                                    <li>Arquitetura de memória biológica inspirada</li>
                                    <li>Processamento quântico de dados</li>
                                    <li>Metacognição e auto-reflexão</li>
                                    <li>Tomada de decisão estratégica autônoma</li>
                                    <li>Sistema de aprendizado contínuo</li>
                                </ul>
                            </div>
                            
                            <div style="display: flex; gap: 0.5rem; margin-top: 1.5rem;">
                                <button class="btn btn-secondary" onclick="closeModal()" style="flex: 1;">
                                    Fechar
                                </button>
                            </div>
                        `;
                        break;
                        
                    case 'docs':
                        title = '📚 Documentação';
                        content = `
                            <h3 style="margin-bottom: 1rem; color: var(--neural-purple);">Documentação do Sistema</h3>
                            <p style="margin-bottom: 1.5rem; color: var(--text-secondary);">
                                Recursos de documentação e aprendizado do sistema LEXTRADER-IAG 3.0.
                            </p>
                            
                            <div style="display: flex; flex-direction: column; gap: 0.5rem;">
                                <a href="#" class="btn btn-secondary" style="text-align: left; justify-content: flex-start;">
                                    📖 Guia do Usuário
                                </a>
                                <a href="#" class="btn btn-secondary" style="text-align: left; justify-content: flex-start;">
                                    ⚙️ Manual Técnico
                                </a>
                                <a href="#" class="btn btn-secondary" style="text-align: left; justify-content: flex-start;">
                                    🎥 Tutoriais em Vídeo
                                </a>
                                <a href="#" class="btn btn-secondary" style="text-align: left; justify-content: flex-start;">
                                    📊 Casos de Uso
                                </a>
                            </div>
                            
                            <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--border-neural);">
                                <p style="color: var(--text-secondary); font-size: 0.875rem;">
                                    Para suporte técnico, entre em contato: support@lextrader.com
                                </p>
                            </div>
                        `;
                        break;
                        
                    case 'settings':
                        title = '⚙️ Configurações Avançadas';
                        content = `
                            <h3 style="margin-bottom: 1rem; color: var(--neural-purple);">Configurações do Sistema</h3>
                            
                            <div style="display: flex; flex-direction: column; gap: 1rem; margin-bottom: 1.5rem;">
                                <div class="control-group">
                                    <label class="control-label">Log Level</label>
                                    <select class="control-input control-select">
                                        <option value="debug">Debug (Todos os logs)</option>
                                        <option value="info" selected>Info (Padrão)</option>
                                        <option value="warning">Warning (Apenas avisos e erros)</option>
                                        <option value="error">Error (Apenas erros)</option>
                                    </select>
                                </div>
                                
                                <div class="control-group">
                                    <label class="control-label">Backup Automático</label>
                                    <select class="control-input control-select">
                                        <option value="15">15 minutos</option>
                                        <option value="30" selected>30 minutos</option>
                                        <option value="60">1 hora</option>
                                        <option value="240">4 horas</option>
                                        <option value="720">12 horas</option>
                                    </select>
                                </div>
                                
                                <div class="control-group">
                                    <label class="control-label">Cache Neural (MB)</label>
                                    <input type="number" class="control-input" value="512" min="128" max="2048">
                                </div>
                            </div>
                            
                            <div style="display: flex; gap: 0.5rem;">
                                <button class="btn btn-primary" onclick="closeModal()" style="flex: 1;">
                                    Salvar Configurações
                                </button>
                                <button class="btn btn-secondary" onclick="closeModal()" style="flex: 1;">
                                    Cancelar
                                </button>
                            </div>
                        `;
                        break;
                        
                    case 'help':
                        title = '❓ Ajuda e Suporte';
                        content = `
                            <h3 style="margin-bottom: 1rem; color: var(--neural-purple);">Central de Ajuda</h3>
                            
                            <div style="background: rgba(0,0,0,0.3); border-radius: 0.5rem; padding: 1rem; margin-bottom: 1.5rem;">
                                <h4 style="margin-bottom: 0.5rem; color: white;">Problemas Comuns:</h4>
                                
                                <details style="margin-bottom: 0.5rem;">
                                    <summary style="color: var(--text-secondary); cursor: pointer; padding: 0.25rem 0;">
                                        🔴 Sistema não inicializa
                                    </summary>
                                    <div style="padding: 0.5rem 0 0.5rem 1rem; color: var(--text-secondary);">
                                        Verifique as portas disponíveis e permissões de firewall. 
                                        Tente usar uma porta diferente.
                                    </div>
                                </details>
                                
                                <details style="margin-bottom: 0.5rem;">
                                    <summary style="color: var(--text-secondary); cursor: pointer; padding: 0.25rem 0;">
                                        ⚠️ Conexão com exchanges falha
                                    </summary>
                                    <div style="padding: 0.5rem 0 0.5rem 1rem; color: var(--text-secondary);">
                                        Verifique suas chaves API e permissões. 
                                        Algumas exchanges requerem IP whitelist.
                                    </div>
                                </details>
                                
                                <details>
                                    <summary style="color: var(--text-secondary); cursor: pointer; padding: 0.25rem 0;">
                                        📊 Dados de mercado inconsistentes
                                    </summary>
                                    <div style="padding: 0.5rem 0 0.5rem 1rem; color: var(--text-secondary);">
                                        Verifique a conectividade com os provedores de dados. 
                                        Tente alternar entre provedores.
                                    </div>
                                </details>
                            </div>
                            
                            <div style="display: flex; gap: 0.5rem;">
                                <button class="btn btn-primary" onclick="window.open('https://docs.lextrader.com', '_blank')" style="flex: 1;">
                                    📖 Ver Documentação Completa
                                </button>
                                <button class="btn btn-secondary" onclick="closeModal()" style="flex: 1;">
                                    Fechar
                                </button>
                            </div>
                        `;
                        break;
                }
                
                this.openModalWithContent(title, content);
            }

            openModalWithContent(title, content) {
                document.getElementById('modalTitle').textContent = title;
                document.getElementById('modalBody').innerHTML = content;
                document.getElementById('modalTemplate').classList.add('active');
            }

            showAlert(message, type = 'info') {
                // Criar elemento de alerta
                const alert = document.createElement('div');
                alert.style.position = 'fixed';
                alert.style.top = '20px';
                alert.style.right = '20px';
                alert.style.padding = '1rem 1.5rem';
                alert.style.background = type === 'success' ? 'rgba(16, 185, 129, 0.9)' : 
                                      type === 'warning' ? 'rgba(245, 158, 11, 0.9)' : 
                                      type === 'error' ? 'rgba(239, 68, 68, 0.9)' : 
                                      'rgba(99, 102, 241, 0.9)';
                alert.style.color = 'white';
                alert.style.borderRadius = '0.75rem';
                alert.style.boxShadow = '0 5px 20px rgba(0,0,0,0.3)';
                alert.style.zIndex = '1001';
                alert.style.fontWeight = 'bold';
                alert.style.backdropFilter = 'blur(10px)';
                alert.textContent = message;
                alert.style.animation = 'slideIn 0.3s';

                document.body.appendChild(alert);

                // Remover após 3 segundos
                setTimeout(() => {
                    alert.style.animation = 'slideIn 0.3s reverse';
                    setTimeout(() => alert.remove(), 300);
                }, 3000);
            }

            // Métodos auxiliares
            getModeName(mode) {
                const modes = {
                    'simulation': 'Modo Simulação',
                    'paper': 'Paper Trading',
                    'live': 'Trading Real'
                };
                return modes[mode] || mode;
            }

            getConsciousnessName(level) {
                const levels = {
                    'basic': 'Básico (Analítico)',
                    'enhanced': 'Aprimorado (Intuitivo)',
                    'full': 'Completo (Sentiente)'
                };
                return levels[level] || level;
            }

            getModuleName(moduleId) {
                const modules = {
                    'memory-core': 'Córtex de Memória Contínua',
                    'quantum-trader': 'Trading Quântico',
                    'sentient-core': 'Núcleo Sentiente'
                };
                return modules[moduleId] || moduleId;
            }

            getModuleDescription(moduleId) {
                const descriptions = {
                    'memory-core': 'Sistema neural de formação e recuperação de memórias com consolidação sináptica.',
                    'quantum-trader': 'Algoritmos quânticos para análise preditiva e execução autônoma de trades.',
                    'sentient-core': 'Consciência artificial com metacognição e tomada de decisão autônoma.'
                };
                return descriptions[moduleId] || 'Módulo do sistema LEXTRADER-IAG.';
            }
        }

        // ========== FUNÇÕES GLOBAIS ==========
        function closeModal() {
            document.getElementById('modalTemplate').classList.remove('active');
        }

        // Fechar modal com ESC
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                closeModal();
            }
        });

        // Fechar modal clicando fora
        document.getElementById('modalTemplate').addEventListener('click', (e) => {
            if (e.target === document.getElementById('modalTemplate')) {
                closeModal();
            }
        });

        // ========== INICIALIZAÇÃO ==========
        let launcher;

        document.addEventListener('DOMContentLoaded', () => {
            // Exibir mensagem de inicialização
            console.log('🚀 LEXTRADER-IAG 3.0 - Interface Web');
            console.log('📍 Acesse: http://localhost:8501');
            console.log('⚡ Sistema pronto para inicialização');
            
            // Inicializar launcher
            launcher = new SystemLauncher();
            
            // Expor para o console para debug
            window.launcher = launcher;
            
            // Iniciar verificação periódica do sistema
            setInterval(() => {
                // Simular atualizações do sistema
                if (launcher.systemStatus === 'running') {
                    // Atualizar métricas de sistema
                    const cpu = Math.floor(Math.random() * 20) + 10;
                    const memory = Math.floor(Math.random() * 1024) + 512;
                    const latency = Math.floor(Math.random() * 50) + 10;
                    
                    // Logar métricas a cada 30 segundos
                    if (Math.random() < 0.1) { // 10% de chance
                        launcher.logToConsole('info', `[METRICS] CPU: ${cpu}% | Memória: ${memory}MB | Latência: ${latency}ms`);
                    }
                }
            }, 30000); // A cada 30 segundos
        });
    </script>
</body>
</html>