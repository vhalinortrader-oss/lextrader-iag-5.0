
import { TradingDirective, AccountType, SecurityFund } from '../types';

class AutonomousTradingEngine {
  private activeTrade: any = null;
  private fund: SecurityFund = {
    totalReserved: 0,
    dailyGains: 0,
    lastTransfer: '',
    status: 'PROTECTED',
    // Fix: Added missing required property
    reserveProgress: 0
  };

  /**
   * Simula ou executa entrada baseada na diretriz da IAG
   */
  async executeDirective(directive: TradingDirective, account: AccountType) {
    console.log(`[ENGINE] Iniciando Ordem ${account}: ${directive.side} ${directive.symbol}`);
    this.activeTrade = { ...directive, startTime: Date.now() };
    
    // Inicia loop de ajuste de Stop-Loss dinâmico
    this.startDynamicAdjustment();
  }

  /**
   * Monitoramento em tempo real do Stop-Loss e Take-Profit
   */
  private startDynamicAdjustment() {
    const monitor = setInterval(() => {
      if (!this.activeTrade) {
        clearInterval(monitor);
        return;
      }

      // Simulação: Se o preço subir 1%, o SL sobe 0.5% (Trailing Stop dinâmico)
      const mockPriceGain = 0.001; 
      if (this.activeTrade.side === 'BUY') {
        this.activeTrade.stopLoss += this.activeTrade.stopLoss * (mockPriceGain * 0.5);
        console.log(`[ENGINE] SL Ajustado dinamicamente: $${this.activeTrade.stopLoss.toFixed(2)}`);
      }
    }, 5000);
  }

  /**
   * Finaliza operação e gerencia lucros para o Fundo de Segurança
   */
  async finalizeTrade(profit: number) {
    if (profit > 0) {
      const securityCut = profit * 0.20; // 20% para o fundo
      this.fund.totalReserved += securityCut;
      this.fund.dailyGains += (profit - securityCut);
      this.fund.lastTransfer = new Date().toISOString();
      console.log(`[ENGINE] Lucro Processado. Fundo Seguranca: +$${securityCut.toFixed(2)}`);
    }
    this.activeTrade = null;
  }
}

export const autonomousEngine = new AutonomousTradingEngine();
