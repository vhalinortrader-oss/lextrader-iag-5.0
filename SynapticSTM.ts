
import { STMEngram } from '../types';

class SynapticSTM {
  private static instance: SynapticSTM;
  private buffer: STMEngram[] = [];
  private maxSize = 100;

  public static getInstance(): SynapticSTM {
    if (!SynapticSTM.instance) {
      SynapticSTM.instance = new SynapticSTM();
    }
    return SynapticSTM.instance;
  }

  /**
   * Adiciona um micro-estado ao buffer circular.
   */
  public push(engram: Omit<STMEngram, 'id'>) {
    const fullEngram: STMEngram = {
      ...engram,
      id: `STM-${Date.now()}-${Math.random().toString(36).substr(2, 4)}`
    };

    this.buffer.push(fullEngram);
    if (this.buffer.length > this.maxSize) {
      this.buffer.shift();
    }
  }

  public getRecent(): STMEngram[] {
    return [...this.buffer].reverse();
  }

  /**
   * Retorna os estados que tiveram alta significância (> 0.8)
   */
  public getHighValueEngrams(): STMEngram[] {
    return this.buffer.filter(e => e.significance > 0.8);
  }

  public getMeanVolatility(): number {
    if (this.buffer.length === 0) return 0;
    return this.buffer.reduce((acc, e) => acc + e.indicators.volatility, 0) / this.buffer.length;
  }

  public clear() {
    this.buffer = [];
  }
}

export const synapticSTM = SynapticSTM.getInstance();
