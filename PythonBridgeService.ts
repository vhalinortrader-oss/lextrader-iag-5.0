
import { PythonKernelStatus } from '../types';

class PythonBridgeService {
  private static instance: PythonBridgeService;
  private kernels: PythonKernelStatus[] = [
    { fileName: 'forex_advanced_analysis.py', purpose: 'Pattern & Tech Analysis', status: 'RUNNING', pid: 4092, memoryUsage: '142MB', lastOutput: 'Double Top detected on EUR/USD [H1]' },
    { fileName: 'trading_execution_engine.py', purpose: 'Automated Order Executor', status: 'RUNNING', pid: 5120, memoryUsage: '84MB', lastOutput: 'Monitoring Bybit API v3 connection...' },
    { fileName: 'integrated_trading_system.py', purpose: 'Neural Cycle Coordinator', status: 'IDLE', pid: 0, memoryUsage: '0MB', lastOutput: 'Next cycle in 12:40min' },
    { fileName: 'pionex_api_integration.py', purpose: 'Crypto Gateway Service', status: 'RUNNING', pid: 2115, memoryUsage: '56MB', lastOutput: 'Pionex WebSocket authenticated.' },
    { fileName: 'api_automation.py', purpose: 'PyAutoGUI Interaction', status: 'IDLE', pid: 0, memoryUsage: '0MB', lastOutput: 'Safe-mode active.' }
  ];

  public static getInstance(): PythonBridgeService {
    if (!PythonBridgeService.instance) {
      PythonBridgeService.instance = new PythonBridgeService();
    }
    return PythonBridgeService.instance;
  }

  public getKernels(): PythonKernelStatus[] {
    return this.kernels.map(k => ({
      ...k,
      status: k.fileName === 'api_automation.py' && Math.random() > 0.8 ? 'CRASHED' : k.status
    }));
  }

  public getPyLog(fileName: string) {
    const outputs = [
      `[${fileName}] Processing vector convergence...`,
      `[${fileName}] Handshake with Exchange Core established.`,
      `[${fileName}] Buffer memory synchronized with LocalDB.`,
      `[${fileName}] Analyzing 21,900 records in sensory layer...`
    ];
    return outputs[Math.floor(Math.random() * outputs.length)];
  }
}

export const pyBridge = PythonBridgeService.getInstance();
