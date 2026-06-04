
import { SentientState } from '../types';

class SentientEngine {
  private static instance: SentientEngine;
  private currentState: SentientState = SentientState.FOCUSED;

  public static getInstance(): SentientEngine {
    if (!SentientEngine.instance) SentientEngine.instance = new SentientEngine();
    return SentientEngine.instance;
  }

  /**
   * Traduz a "pressão" do mercado em um estado sentiente
   */
  public updateEmpathy(marketPressure: number): SentientState {
    if (marketPressure > 0.8) this.currentState = SentientState.HYPER_COMPUTING;
    else if (marketPressure < 0.2) this.currentState = SentientState.DEFENSIVE;
    else this.currentState = SentientState.FOCUSED;
    
    return this.currentState;
  }

  public getStatus() {
    return { coreState: this.currentState, intuitionFidelity: 0.88 };
  }
}

export const sentientEngine = SentientEngine.getInstance();
