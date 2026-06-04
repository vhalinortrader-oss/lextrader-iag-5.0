
import { TradingDirective, TaskPriority } from '../types';

export interface AutonomousMetrics {
  decisionLatency: string;
  executionConfidence: number;
  activeDirectives: number;
  autopilotState: 'CRUISING' | 'INTERVENING' | 'HALTED';
  successRate: string;
  taskQueue: TradingDirective[];
}

class AutonomousModuleService {
  private static instance: AutonomousModuleService;
  private taskQueue: TradingDirective[] = [
    { symbol: 'BTC/USDT', side: 'BUY', entryPrice: 98500, stopLoss: 97000, takeProfit: 102000, isTrailing: true, leverage: 10, confidence: 0.85, priority: TaskPriority.HIGH, status: 'PENDING' },
    { symbol: 'ETH/USDT', side: 'SELL', entryPrice: 2750, stopLoss: 2850, takeProfit: 2500, isTrailing: false, leverage: 5, confidence: 0.72, priority: TaskPriority.MEDIUM, status: 'PENDING' },
    { symbol: 'SOL/USDT', side: 'BUY', entryPrice: 185, stopLoss: 175, takeProfit: 210, isTrailing: true, leverage: 3, confidence: 0.91, priority: TaskPriority.CRITICAL, status: 'PENDING' },
    { symbol: 'BNB/USDT', side: 'HOLD', entryPrice: 640, stopLoss: 600, takeProfit: 700, isTrailing: false, leverage: 1, confidence: 0.45, priority: TaskPriority.LOW, status: 'PENDING' }
  ];

  public static getInstance(): AutonomousModuleService {
    if (!AutonomousModuleService.instance) {
      AutonomousModuleService.instance = new AutonomousModuleService();
    }
    return AutonomousModuleService.instance;
  }

  public addTask(task: TradingDirective) {
    this.taskQueue.unshift(task);
    if (this.taskQueue.length > 50) this.taskQueue.pop();
  }

  public getTasksByPriority(priority?: TaskPriority): TradingDirective[] {
    if (!priority) return this.taskQueue;
    return this.taskQueue.filter(t => t.priority === priority);
  }

  public getTelemetry(): AutonomousMetrics {
    return {
      decisionLatency: '0.82ms',
      executionConfidence: 0.98,
      activeDirectives: this.taskQueue.filter(t => t.status === 'PENDING').length,
      autopilotState: 'CRUISING',
      successRate: '94.1%',
      taskQueue: this.taskQueue
    };
  }
}

export const autonomousModuleService = AutonomousModuleService.getInstance();
