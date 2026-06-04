
import { TVBot } from '../types';

class TradingViewBotService {
  private static instance: TradingViewBotService;
  private bots: TVBot[] = [
    { id: 'tv_01', name: 'Pine Script Alpha', pineScriptId: 'MACD_RSI_STRAT', status: 'SYNCHRONIZED', lastSignal: 'BUY', accuracy: 0.72, pair: 'BTCUSDT', strategyType: 'TREND' },
    { id: 'tv_02', name: 'LuxAlgo Emulation', pineScriptId: 'EXTERNAL_LUX_04', status: 'AWAITING_SIGNAL', lastSignal: 'SELL', accuracy: 0.68, pair: 'ETHUSDT', strategyType: 'SCALP' },
    { id: 'tv_03', name: 'OrderBlock Finder', pineScriptId: 'OB_SMART_MONEY', status: 'SYNCHRONIZED', lastSignal: 'NEUTRAL', accuracy: 0.81, pair: 'SOLUSDT', strategyType: 'REVERSAL' }
  ];

  public static getInstance(): TradingViewBotService {
    if (!TradingViewBotService.instance) {
      TradingViewBotService.instance = new TradingViewBotService();
    }
    return TradingViewBotService.instance;
  }

  public getImportedBots(): TVBot[] {
    return this.bots.map(b => ({
      ...b,
      // Simulação de atividade de rede
      status: Math.random() > 0.95 ? 'DISCONNECTED' : b.status
    }));
  }

  /**
   * Simula a recepção de um Webhook JSON do TradingView
   */
  public simulateWebhookSignal(botId: string) {
    const bot = this.bots.find(b => b.id === botId);
    if (!bot) return null;
    
    const signals: Array<'BUY' | 'SELL'> = ['BUY', 'SELL'];
    const newSignal = signals[Math.floor(Math.random() * signals.length)];
    
    bot.lastSignal = newSignal;
    bot.status = 'SYNCHRONIZED';
    
    return {
      webhookId: `WH-${Math.random().toString(36).substr(2, 9).toUpperCase()}`,
      botName: bot.name,
      symbol: bot.pair,
      signal: newSignal,
      timestamp: new Date().toISOString()
    };
  }
}

export const tvBotService = TradingViewBotService.getInstance();
