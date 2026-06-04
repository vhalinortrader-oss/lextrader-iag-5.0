import React, { useState, useEffect } from 'react';
import { APIMonitoringPanel } from './components/monitoring';
import { TradingEngine, TradingConfig } from './core/engines/TradingEngine';
import { NeuralNetwork, NeuralNetworkConfig } from './core/neural/NeuralNetwork';
import { BinanceService, BinanceConfig } from './services/trading/BinanceService';
import './App.css';

// ─── Configurações ───
const TRADING_CONFIG: TradingConfig = {
  apiKey: process.env.REACT_APP_BINANCE_API_KEY || '',
  apiSecret: process.env.REACT_APP_BINANCE_API_SECRET || '',
  sandbox: true,
  maxRiskPerTrade: 0.02,
  maxDailyLoss: 1000,
  symbols: ['BTCUSDT', 'ETHUSDT', 'BNBUSDT']
};

const NEURAL_CONFIG: NeuralNetworkConfig = {
  inputSize: 10,
  hiddenLayers: [64, 32, 16],
  outputSize: 3,
  learningRate: 0.001,
  activationFunction: 'relu'
};

const BINANCE_CONFIG: BinanceConfig = {
  apiKey: process.env.REACT_APP_BINANCE_API_KEY || '',
  apiSecret: process.env.REACT_APP_BINANCE_API_SECRET || '',
  sandbox: true,
  testnet: true
};

// ─── Interfaces ───
interface AppState {
  tradingEngine: TradingEngine | null;
  neuralNetwork: NeuralNetwork | null;
  binanceService: BinanceService | null;
  isInitialized: boolean;
  activeTab: 'monitoring' | 'trading' | 'neural' | 'analytics';
  systemStatus: 'online' | 'offline' | 'error';
}

interface SystemMetrics {
  totalOrders: number;
  activeOrders: number;
  portfolioValue: number;
  dailyPnL: number;
  neuralAccuracy: number;
  apiHealth: 'healthy' | 'degraded' | 'unhealthy';
}

// ─── Componente Principal ───
const App: React.FC = () => {
  const [appState, setAppState] = useState<AppState>({
    tradingEngine: null,
    neuralNetwork: null,
    binanceService: null,
    isInitialized: false,
    activeTab: 'monitoring',
    systemStatus: 'offline'
  });

  const [metrics, setMetrics] = useState<SystemMetrics>({
    totalOrders: 0,
    activeOrders: 0,
    portfolioValue: 0,
    dailyPnL: 0,
    neuralAccuracy: 0,
    apiHealth: 'healthy'
  });

  // ─── Inicialização ───
  useEffect(() => {
    initializeSystem();
  }, []);

  const initializeSystem = async () => {
    try {
      setAppState(prev => ({ ...prev, systemStatus: 'offline' }));

      // Inicializar serviços
      const tradingEngine = new TradingEngine(TRADING_CONFIG);
      const neuralNetwork = new NeuralNetwork(NEURAL_CONFIG);
      const binanceService = new BinanceService(BINANCE_CONFIG);

      // Verificar saúde das APIs
      await binanceService.healthCheck();

      // Carregar modelo neural treinado se existir
      // await loadNeuralModel(neuralNetwork);

      setAppState({
        tradingEngine,
        neuralNetwork,
        binanceService,
        isInitialized: true,
        activeTab: 'monitoring',
        systemStatus: 'online'
      });

      console.log('✅ Sistema Lextrader-IAG inicializado com sucesso');
    } catch (error) {
      console.error('❌ Falha na inicialização:', error);
      setAppState(prev => ({ ...prev, systemStatus: 'error' }));
    }
  };

  // ─── Atualização de Métricas ───
  useEffect(() => {
    if (!appState.isInitialized) return;

    const interval = setInterval(() => {
      updateMetrics();
    }, 5000); // Atualizar a cada 5 segundos

    return () => clearInterval(interval);
  }, [appState.isInitialized]);

  const updateMetrics = async () => {
    if (!appState.tradingEngine || !appState.binanceService) return;

    try {
      const engineStats = appState.tradingEngine.getEngineStats();
      const binanceHealth = await appState.binanceService.healthCheck();

      setMetrics({
        totalOrders: engineStats.totalOrders,
        activeOrders: engineStats.activeOrders,
        portfolioValue: engineStats.portfolioValue,
        dailyPnL: engineStats.dailyPnL,
        neuralAccuracy: appState.neuralNetwork ? 0.85 : 0, // Simulado
        apiHealth: binanceHealth.status === 'healthy' ? 'healthy' : 
                  binanceHealth.status === 'degraded' ? 'degraded' : 'unhealthy'
      });
    } catch (error) {
      console.error('Erro ao atualizar métricas:', error);
    }
  };

  // ─── Handlers ───
  const handleTabChange = (tab: AppState['activeTab']) => {
    setAppState(prev => ({ ...prev, activeTab: tab }));
  };

  const handleRestart = () => {
    initializeSystem();
  };

  // ─── Renderização ───
  const renderHeader = () => (
    <header className="app-header">
      <div className="header-content">
        <div className="logo-section">
          <h1>🚀 Lextrader-IAG 4.0</h1>
          <p>Sistema Autônomo de Trading com IA Quântica</p>
        </div>
        
        <div className="status-section">
          <div className={`status-indicator ${appState.systemStatus}`}>
            <span className="status-dot"></span>
            {appState.systemStatus === 'online' ? 'Online' : 
             appState.systemStatus === 'offline' ? 'Offline' : 'Error'}
          </div>
          
          {appState.systemStatus === 'error' && (
            <button onClick={handleRestart} className="restart-btn">
              🔄 Reiniciar
            </button>
          )}
        </div>
      </div>

      <nav className="app-nav">
        <button 
          className={`nav-btn ${appState.activeTab === 'monitoring' ? 'active' : ''}`}
          onClick={() => handleTabChange('monitoring')}
        >
          📊 Monitoramento
        </button>
        <button 
          className={`nav-btn ${appState.activeTab === 'trading' ? 'active' : ''}`}
          onClick={() => handleTabChange('trading')}
        >
          💱 Trading
        </button>
        <button 
          className={`nav-btn ${appState.activeTab === 'neural' ? 'active' : ''}`}
          onClick={() => handleTabChange('neural')}
        >
          🧠 IA Neural
        </button>
        <button 
          className={`nav-btn ${appState.activeTab === 'analytics' ? 'active' : ''}`}
          onClick={() => handleTabChange('analytics')}
        >
          📈 Análise
        </button>
      </nav>
    </header>
  );

  const renderMetricsBar = () => (
    <div className="metrics-bar">
      <div className="metric-item">
        <span className="metric-label">Ordens Totais</span>
        <span className="metric-value">{metrics.totalOrders}</span>
      </div>
      <div className="metric-item">
        <span className="metric-label">Ordens Ativas</span>
        <span className="metric-value">{metrics.activeOrders}</span>
      </div>
      <div className="metric-item">
        <span className="metric-label">Portfolio</span>
        <span className="metric-value">${metrics.portfolioValue.toFixed(2)}</span>
      </div>
      <div className="metric-item">
        <span className="metric-label">PnL Diário</span>
        <span className={`metric-value ${metrics.dailyPnL >= 0 ? 'positive' : 'negative'}`}>
          ${metrics.dailyPnL.toFixed(2)}
        </span>
      </div>
      <div className="metric-item">
        <span className="metric-label">Precisão IA</span>
        <span className="metric-value">{(metrics.neuralAccuracy * 100).toFixed(1)}%</span>
      </div>
      <div className="metric-item">
        <span className="metric-label">API Health</span>
        <span className={`metric-value api-${metrics.apiHealth}`}>
          {metrics.apiHealth}
        </span>
      </div>
    </div>
  );

  const renderContent = () => {
    if (!appState.isInitialized) {
      return (
        <div className="loading-screen">
          <div className="loading-spinner"></div>
          <h2>Inicializando Sistema Lextrader-IAG...</h2>
          <p>Preparando engines de trading, redes neurais e conexões API</p>
        </div>
      );
    }

    switch (appState.activeTab) {
      case 'monitoring':
        return <APIMonitoringPanel />;
      
      case 'trading':
        return (
          <div className="trading-panel">
            <h2>🎯 Painel de Trading</h2>
            <div className="trading-controls">
              <button className="trade-btn buy">Comprar BTC</button>
              <button className="trade-btn sell">Vender BTC</button>
              <button className="trade-btn stop">Stop Loss</button>
            </div>
            <div className="order-book">
              <h3>Order Book</h3>
              {/* Order book content here */}
            </div>
          </div>
        );
      
      case 'neural':
        return (
          <div className="neural-panel">
            <h2>🧠 Rede Neural</h2>
            <div className="neural-stats">
              <div className="stat-card">
                <h4>Precisão do Modelo</h4>
                <span className="stat-value">85.3%</span>
              </div>
              <div className="stat-card">
                <h4>Epocas de Treinamento</h4>
                <span className="stat-value">1,247</span>
              </div>
              <div className="stat-card">
                <h4>Previsões/Hora</h4>
                <span className="stat-value">3,847</span>
              </div>
            </div>
            <div className="neural-visualization">
              {/* Neural network visualization here */}
            </div>
          </div>
        );
      
      case 'analytics':
        return (
          <div className="analytics-panel">
            <h2>📈 Análise Avançada</h2>
            <div className="analytics-grid">
              <div className="chart-container">
                <h3>Performance Diária</h3>
                {/* Chart here */}
              </div>
              <div className="chart-container">
                <h3>Distribuição de Ativos</h3>
                {/* Chart here */}
              </div>
              <div className="chart-container">
                <h3>Risco vs Retorno</h3>
                {/* Chart here */}
              </div>
            </div>
          </div>
        );
      
      default:
        return <APIMonitoringPanel />;
    }
  };

  return (
    <div className="app">
      {renderHeader()}
      {appState.isInitialized && renderMetricsBar()}
      <main className="app-main">
        {renderContent()}
      </main>
      <footer className="app-footer">
        <p>© 2024 Lextrader-IAG 4.0 | Powered by Quantum AI</p>
      </footer>
    </div>
  );
};

export default App;
