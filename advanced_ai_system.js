/*
  LexTrader-IAG - Advanced AI Prediction / Central Orchestrator (Node)
  =====================================================================
  Objetivo: melhorar a “inteligência artificial central” exposta via Node.
  - remove predições/recursos fixos
  - adiciona pontuação baseada em sinais
  - cria um pipeline mínimo de decisão
  - adiciona memória local e eventos (hooks) para integração futura com TS/Java

  OBS: este arquivo não depende obrigatoriamente de tfjs-node.
       Se @tensorflow/tfjs-node estiver presente, pode ser usado futuramente.
*/

let tf;
try {
  // Optional dependency: pode não existir no ambiente do usuário.
  // eslint-disable-next-line import/no-extraneous-dependencies
  tf = require('@tensorflow/tfjs-node');
} catch (_) {
  tf = null;
}

const { EventEmitter } = require('events');

function clamp(n, min, max) {
  return Math.max(min, Math.min(max, n));
}

function safeNumber(x, fallback = 0) {
  const n = typeof x === 'number' ? x : Number(x);
  return Number.isFinite(n) ? n : fallback;
}

function normalizeVector(v) {
  if (!Array.isArray(v) || v.length === 0) return [];
  const arr = v.map((x) => safeNumber(x, 0));
  const norm = Math.sqrt(arr.reduce((s, x) => s + x * x, 0)) || 1;
  return arr.map((x) => x / norm);
}

function sigmoid(z) {
  // Estável o suficiente para inputs típicos.
  if (z >= 0) {
    const ez = Math.exp(-z);
    return 1 / (1 + ez);
  }
  const ez = Math.exp(z);
  return ez / (1 + ez);
}

class EnsemblePredictor {
  constructor() {
    this.weights = { xgboost: 0.35, lightgbm: 0.3, rf: 0.35 };
  }

  // Permite mudar pesos sem precisar recriar o ensemble
  setWeights(newWeights = {}) {
    this.weights = {
      xgboost: safeNumber(newWeights.xgboost, this.weights.xgboost),
      lightgbm: safeNumber(newWeights.lightgbm, this.weights.lightgbm),
      rf: safeNumber(newWeights.rf, this.weights.rf),
    };

    // Normaliza para soma 1 (evita viés acidental)
    const sum = this.weights.xgboost + this.weights.lightgbm + this.weights.rf;
    if (sum > 0) {
      this.weights.xgboost /= sum;
      this.weights.lightgbm /= sum;
      this.weights.rf /= sum;
    }

    return this;
  }

  /**
   * X pode ser:
   * - array numérica: [f1,f2,...]
   * - objeto de features: { featureA: value, ... }
   * Para o stub, criamos um “score” determinístico a partir do input.
   *
   * Retorna:
   *  { prediction: [p], confidence: c } com p em [0,1].
   */
  async predict(X) {
    const features = Array.isArray(X)
      ? X
      : X && typeof X === 'object'
        ? Object.values(X)
        : [];

    const v = normalizeVector(features);
    if (v.length === 0) {
      return { prediction: [0.5], confidence: 0.0 };
    }

    // “Base models” como scorers determinísticos (placeholder com melhor qualidade do que constante).
    // Cada model aplica um peso + projeção simples.
    const proj = v.reduce((s, x, i) => s + x * (i % 7 === 0 ? 1.2 : i % 5 === 0 ? -0.7 : 0.9), 0);

    const xgboostScore = sigmoid(proj * 1.15 + 0.1);
    const lightgbmScore = sigmoid(proj * 0.95 - 0.05);
    const rfScore = sigmoid(proj * 1.05 + 0.02);

    const w = this.weights;
    const weighted =
      w.xgboost * xgboostScore + w.lightgbm * lightgbmScore + w.rf * rfScore;

    const prediction = clamp(weighted, 0, 1);

    // Confidence: quanto mais perto de 0 ou 1 (mais separação), maior.
    // Também usa consistência entre os modelos.
    const mean = (xgboostScore + lightgbmScore + rfScore) / 3;
    const variance =
      ((xgboostScore - mean) ** 2 + (lightgbmScore - mean) ** 2 + (rfScore - mean) ** 2) / 3;
    const disagreementPenalty = clamp(variance / 0.08, 0, 1); // heurística

    const separation = Math.abs(prediction - 0.5) * 2; // 0..1
    const confidence = clamp(0.15 + 0.75 * separation * (1 - disagreementPenalty), 0, 1);

    return { prediction: [prediction], confidence };
  }
}

class AdvancedAIPredictionSystem {
  constructor({ memorySize = 200 } = {}) {
    this.ensemble = new EnsemblePredictor();
    this.events = new EventEmitter();

    this.memory = {
      lastPredictions: [], // { ts, featuresHash, prediction, confidence }
      lastResource: null,
      lastDecision: null,
      memorySize,
      systemState: {
        // Estado interno mínimo para dar “cara” de IA central
        mode: 'BALANCED',
        stability: 0.7,
        lastCycleAt: Date.now(),
      },
    };
  }

  on(eventName, handler) {
    this.events.on(eventName, handler);
    return this;
  }

  _pushMemory(item) {
    this.memory.lastPredictions.push(item);
    if (this.memory.lastPredictions.length > this.memory.memorySize) {
      this.memory.lastPredictions.shift();
    }
  }

  _hashFeatures(X) {
    // hash simples (não criptográfico) para memória.
    const s = Array.isArray(X)
      ? X.join(',')
      : X && typeof X === 'object'
        ? Object.entries(X)
            .map(([k, v]) => `${k}:${safeNumber(v, 0)}`)
            .join('|')
        : String(X ?? '');

    let h = 0;
    for (let i = 0; i < s.length; i++) {
      h = (h * 31 + s.charCodeAt(i)) >>> 0;
    }
    return h.toString(16);
  }

  /**
   * Estima CPU/Mem com base na complexidade do input.
   * (continua heurístico, mas deixa de ser constante.)
   */
  async predictResourceUsage(historicalData) {
    const len = Array.isArray(historicalData)
      ? historicalData.length
      : Array.isArray(historicalData?.data)
        ? historicalData.data.length
        : 10;

    const featureCount =
      (historicalData && typeof historicalData === 'object' && historicalData.features && Array.isArray(historicalData.features))
        ? historicalData.features.length
        : 20;

    const cpu = clamp(25 + len * 0.8 + featureCount * 0.05, 5, 95);
    const memory = clamp(20 + len * 0.45 + featureCount * 0.08, 5, 98);

    const resource = {
      cpu: Math.round(cpu),
      memory: Math.round(memory),
      backend: tf ? 'tfjs-node-present' : 'no-tfjs',
    };

    this.memory.lastResource = resource;
    this.events.emit('resource_usage', resource);
    return resource;
  }

  /**
   * Pipeline principal para “IA central”:
   * - calcula predição e confiança
   * - estima risco (heurística baseada no próprio sinal)
   * - decide ação (BUY/SELL/WAIT)
   */
  async ingestMarketData({ features, historicalData } = {}) {
    const featuresSafe = features ?? {};
    const predictionResult = await this.ensemble.predict(featuresSafe);

    const { prediction, confidence } = predictionResult;
    const p = Array.isArray(prediction) ? prediction[0] : safeNumber(prediction, 0.5);

    const riskScore = this._estimateRisk({ p, confidence, features: featuresSafe });

    const decision = this.decide({ p, confidence, riskScore });

    const item = {
      ts: Date.now(),
      featuresHash: this._hashFeatures(featuresSafe),
      prediction: p,
      confidence,
      riskScore,
      decision,
    };
    this._pushMemory(item);
    this.memory.lastDecision = decision;

    this._updateCentralState({ confidence, riskScore, decision });

    this.events.emit('prediction', {
      p,
      confidence,
      riskScore,
      decision,
    });

    if (historicalData !== undefined) {
      await this.predictResourceUsage(historicalData);
    }

    return { ...item };
  }

  _estimateRisk({ p, confidence, features }) {
    // Heurística:
    // - baixa confiança => alto risco
    // - p perto de 0.5 => alto risco (sinal fraco)
    // - adiciona “complexidade” a partir do número de features
    const featuresCount = Array.isArray(features)
      ? features.length
      : features && typeof features === 'object'
        ? Object.keys(features).length
        : 1;

    const signalWeakness = clamp(1 - Math.abs(p - 0.5) * 2, 0, 1);
    const confRisk = clamp(1 - confidence, 0, 1);
    const complexityRisk = clamp((featuresCount - 5) / 50, 0, 1);

    // ponderações
    const risk = 0.15 + 0.55 * signalWeakness + 0.45 * confRisk + 0.2 * complexityRisk;
    return clamp(risk, 0, 1);
  }

  decide({ p, confidence, riskScore }) {
    // Thresholds ajustáveis via state no futuro.
    const BUY_T = 0.62;
    const SELL_T = 0.38;
    const CONF_T = 0.60;
    const RISK_T = 0.72;

    const isGoodConfidence = confidence >= CONF_T;
    const isLowRisk = riskScore <= RISK_T;

    if (p >= BUY_T && isGoodConfidence && isLowRisk) {
      return {
        action: 'BUY',
        reason: 'high_p_confidence_low_risk',
        strength: clamp((p - BUY_T) / (1 - BUY_T), 0, 1),
        confidence,
        riskScore,
      };
    }

    if (p <= SELL_T && isGoodConfidence && isLowRisk) {
      return {
        action: 'SELL',
        reason: 'low_p_confidence_low_risk',
        strength: clamp((SELL_T - p) / (SELL_T - 0), 0, 1),
        confidence,
        riskScore,
      };
    }

    return {
      action: 'WAIT',
      reason: 'insufficient_signal_or_confidence_or_risk',
      strength: clamp((Math.abs(p - 0.5) * 2) * confidence * (1 - riskScore), 0, 1),
      confidence,
      riskScore,
    };
  }

  _updateCentralState({ confidence, riskScore, decision }) {
    // estabilidade sobe com decisões “boas” e desce com risco.
    const delta = (decision.action === 'WAIT' ? -0.02 : 0.03) + (confidence - 0.5) * 0.04 - riskScore * 0.02;
    this.memory.systemState.stability = clamp(this.memory.systemState.stability + delta, 0, 1);

    // Mode simples
    if (riskScore > 0.75) this.memory.systemState.mode = 'RISK_AVOID';
    else if (confidence > 0.75) this.memory.systemState.mode = 'AGGRESSIVE';
    else this.memory.systemState.mode = 'BALANCED';

    this.memory.systemState.lastCycleAt = Date.now();
    this.events.emit('central_state', this.memory.systemState);
  }

  getStateSnapshot() {
    return {
      memorySize: this.memory.memorySize,
      lastDecision: this.memory.lastDecision,
      lastResource: this.memory.lastResource,
      systemState: { ...this.memory.systemState },
      predictionsCount: this.memory.lastPredictions.length,
    };
  }
}

// --- CLI mode (Java -> Node integration) ---
// Uso:
//   node advanced_ai_system.js '{"features":[...],"historicalData":{"data":[...]}}'
// Saída (stdout): JSON único com { decision, p, confidence, riskScore, ... }
if (require.main === module) {
  (async () => {
    try {
      const arg = process.argv[2];
      const input = arg ? JSON.parse(arg) : {};

      const system = new AdvancedAIPredictionSystem();
      const result = await system.ingestMarketData({
        features: input.features,
        historicalData: input.historicalData,
      });

      // Produz stdout estritamente JSON para o Java parsear.
      const out = {
        decision: result.decision,
        p: result.prediction,
        confidence: result.confidence,
        riskScore: result.riskScore,
        timestamp: result.ts,
        backend: result?.backend,
      };
      process.stdout.write(JSON.stringify(out));
    } catch (e) {
      process.stdout.write(
        JSON.stringify({ error: String(e?.message ?? e), stack: String(e?.stack ?? '') })
      );
      process.exitCode = 1;
    }
  })();
}

module.exports = {
  AdvancedAIPredictionSystem,
  EnsemblePredictor,
};



