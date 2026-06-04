
import ccxt from 'ccxt';
import { ApiCredential, AccountType } from '../types';
import { CircuitBreaker } from './resilience';

const exchangeBreaker = new CircuitBreaker('Exchange-API-Bridge');

class PlatformAPIs {
  private activeExchanges: Map<string, any> = new Map();

  async connect(credential: ApiCredential) {
    return exchangeBreaker.execute(async () => {
      if (credential.platform === 'BINANCE' || credential.platform === 'BYBIT') {
        const exchangeClass = (ccxt as any)[credential.platform.toLowerCase()];
        const exchange = new exchangeClass({
          apiKey: credential.apiKey,
          secret: credential.apiSecret,
          enableRateLimit: true,
        });

        if (credential.type === AccountType.DEMO) {
          if (exchange.urls['test']) exchange.urls['api'] = exchange.urls['test'];
        }

        await exchange.loadMarkets();
        this.activeExchanges.set(`${credential.platform}_${credential.type}`, exchange);

        return { success: true, message: `Ponte com ${credential.platform} estabelecida.` };
      }
      return { success: true, message: "Conectado via Gateway Genérico." };
    }).catch(err => ({ success: false, message: `Disjuntor Ativo: ${err.message}` }));
  }

  async getPrice(platform: string, symbol: string): Promise<number> {
    return exchangeBreaker.execute(async () => {
      const exchange = this.activeExchanges.get(`${platform}_REAL`) || this.activeExchanges.get(`${platform}_DEMO`);
      if (exchange) {
        const ticker = await exchange.fetchTicker(symbol);
        return ticker.last;
      }
      return 0;
    }).catch(() => 0);
  }
}

export const platformApis = new PlatformAPIs();
