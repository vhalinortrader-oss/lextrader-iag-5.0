
import { TradingDirective } from '../types';

class AutonomousDecisionEngine {
  private static instance: AutonomousDecisionEngine;
  private currentDecision: string = "IDLE";

  public static getInstance(): AutonomousDecisionEngine {
    if (!AutonomousDecisionEngine.instance) AutonomousDecisionEngine.instance = new AutonomousDecisionEngine();
    return AutonomousDecisionEngine.instance;
  }

  /**
   * Avalia confluências de padrões, histórico e previsões para gerar o veredito final.
   */
  public async arbitrate(context: any): Promise<TradingDirective["side"]> {
    const confidence = context.confidence || 0;
    const historicalMatch = context.historicalMatch || 0;
    
    if (confidence > 0.85 && historicalMatch > 0.8) {
      this.currentDecision = "EXECUTE_BUY";
      return 'BUY';
    }
    
    if (confidence < 0.2 && historicalMatch > 0.8) {
      this.currentDecision = "EXECUTE_SELL";
      return 'SELL';
    }

    this.currentDecision = "WAITING_CONFLUENCE";
    return 'HOLD';
  }

  public getStatus() {
    return { lastVeredict: this.currentDecision };
  }
}

export const autonomousDecisionEngine = AutonomousDecisionEngine.getInstance();
