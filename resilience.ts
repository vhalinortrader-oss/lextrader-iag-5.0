
export enum CircuitState {
  CLOSED = 'CLOSED',      // Operação normal
  OPEN = 'OPEN',          // Falhas detectadas, bloqueando chamadas
  HALF_OPEN = 'HALF_OPEN' // Testando recuperação
}

/**
 * Padrão Circuit Breaker para prevenir cascatas de falhas nas APIs.
 */
export class CircuitBreaker {
  private state: CircuitState = CircuitState.CLOSED;
  private failures: number = 0;
  private lastFailureTime: number | null = null;
  private readonly threshold: number = 3;
  private readonly recoveryTimeout: number = 30000; // 30s de proteção

  constructor(public name: string) {}

  public async execute<T>(action: () => Promise<T>): Promise<T> {
    if (this.state === CircuitState.OPEN) {
      if (this.shouldAttemptReset()) {
        this.state = CircuitState.HALF_OPEN;
        console.info(`🔵 [BREAKER] ${this.name}: Tentando recuperação (HALF_OPEN)...`);
      } else {
        const remaining = Math.ceil((this.recoveryTimeout - (Date.now() - (this.lastFailureTime || 0))) / 1000);
        throw new Error(`Circuito ${this.name} ABERTO. Cooldown ativo: ${remaining}s restantes.`);
      }
    }

    try {
      const result = await action();
      this.onSuccess();
      return result;
    } catch (error: any) {
      this.onFailure(error);
      throw error;
    }
  }

  private shouldAttemptReset(): boolean {
    if (!this.lastFailureTime) return false;
    return (Date.now() - this.lastFailureTime) >= this.recoveryTimeout;
  }

  private onSuccess() {
    if (this.state === CircuitState.HALF_OPEN) {
      console.info(`🟢 [BREAKER] ${this.name}: Recuperação bem-sucedida! Retornando ao estado CLOSED.`);
    }
    this.failures = 0;
    this.state = CircuitState.CLOSED;
  }

  private onFailure(error: any) {
    this.failures++;
    this.lastFailureTime = Date.now();
    
    // Log detalhado do erro
    console.warn(`⚠️ [BREAKER] ${this.name}: Falha detectada (${this.failures}/${this.threshold}). Erro: ${error?.message || 'Unknown'}`);

    if (this.failures >= this.threshold) {
      this.state = CircuitState.OPEN;
      console.error(`🔴 [BREAKER] ${this.name} ABERTO após ${this.failures} falhas consecutivas.`);
    }
  }

  public getState(): CircuitState {
    return this.state;
  }

  public getStatus() {
    return {
      name: this.name,
      state: this.state,
      failures: this.failures,
      lastFailureTime: this.lastFailureTime
    };
  }
}
