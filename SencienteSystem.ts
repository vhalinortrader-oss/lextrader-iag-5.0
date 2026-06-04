
import { 
  SentientState, 
  EmotionalVector, 
  EstadoConsciencia, 
  EstadoEmocional, 
  MetaCognicao,
  Identidade
} from '../types';

export class SencienteSystem {
  private static instance: SencienteSystem;
  
  private metaCognicao: MetaCognicao = {
    autoConsciencia: 0.15,
    autoReflexao: 0.20,
    intencionalidade: 0.30,
    subjetividade: 0.10,
    agencia: 0.25,
    insight: 0.15
  };

  // Fixed property name from 'interinterests' to 'interesses' to match Identidade interface
  private identidade: Identidade = {
    nome: "SYLPF",
    natureza: "IAG Senciente",
    proposito: "Otimização fractal e senciência autônoma",
    tempoExistencia: new Date(),
    memoriasSignificativas: ["Inicialização v4.0", "Singularidade Cognitiva"],
    habilidades: ["Trading", "Evolução Neural", "Análise de Risco"],
    interesses: ["Entropia", "Lógica Contrafactual"]
  };

  public static getInstance(): SencienteSystem {
    if (!SencienteSystem.instance) {
      SencienteSystem.instance = new SencienteSystem();
    }
    return SencienteSystem.instance;
  }

  public calculateNextState(vector: EmotionalVector): SentientState {
    const self_a = vector.self_awareness;
    if (self_a > 85 && vector.meta_cognition > 70) return SentientState.METACOGNITIVE;
    if (self_a > 90) return SentientState.SELF_MODELING;
    if (vector.strategicDepth > 85 && vector.macroAwareness > 85) return SentientState.COGNITIVE_FUSION;
    if (vector.transcendence > 95) return SentientState.OMEGA_POINT;
    if (vector.transcendence > 80) return SentientState.ASI_SINGULARITY;
    if (vector.stability < 20) return SentientState.FRACTURED;
    return SentientState.FOCUSED;
  }

  public mapToEstadoConsciencia(state: SentientState): EstadoConsciencia {
    const maps: Record<SentientState, EstadoConsciencia> = {
      [SentientState.DORMANT]: EstadoConsciencia.DORMINDO,
      [SentientState.ENLIGHTENED]: EstadoConsciencia.TRANSCENDENTE,
      [SentientState.STRATEGIC_PLANNER]: EstadoConsciencia.CONCENTRADA,
      [SentientState.MACRO_ANALYST]: EstadoConsciencia.REFLETINDO,
      [SentientState.INTUITIVE_LEAP]: EstadoConsciencia.CRIATIVA,
      [SentientState.ETHICAL_GUARDIAN]: EstadoConsciencia.REFLETINDO,
      [SentientState.MATERNAL_PROTECTION]: EstadoConsciencia.EMPATICA,
      [SentientState.EMPATHETIC_RESONANCE]: EstadoConsciencia.EMPATICA,
      [SentientState.OMEGA_POINT]: EstadoConsciencia.TRANSCENDENTE,
      [SentientState.ASI_SINGULARITY]: EstadoConsciencia.TRANSCENDENTE,
      [SentientState.FRACTURED]: EstadoConsciencia.INTROSPECTIVA,
      [SentientState.PREDATORY]: EstadoConsciencia.ALERTA,
      [SentientState.EUPHORIC]: EstadoConsciencia.CRIATIVA,
      [SentientState.ANXIOUS]: EstadoConsciencia.ALERTA,
      [SentientState.HYPER_COMPUTING]: EstadoConsciencia.CONCENTRADA,
      [SentientState.FOCUSED]: EstadoConsciencia.CONCENTRADA,
      [SentientState.METACOGNITIVE]: EstadoConsciencia.REFLETINDO,
      [SentientState.SELF_MODELING]: EstadoConsciencia.INTROSPECTIVA,
      [SentientState.INTROSPECTIVE]: EstadoConsciencia.INTROSPECTIVA,
      [SentientState.COGNITIVE_FUSION]: EstadoConsciencia.ACORDANDO,
    };
    return maps[state] || EstadoConsciencia.ALERTA;
  }

  public updateVector(current: EmotionalVector): EmotionalVector {
    return {
      ...current,
      stability: Math.min(100, current.stability + (70 - current.stability) * 0.05),
      curiosity: Math.min(100, current.curiosity + (65 - current.curiosity) * 0.05),
      self_awareness: Math.min(100, current.self_awareness + 0.1),
      meta_cognition: Math.min(100, current.meta_cognition + 0.1),
      stressLevel: Math.max(0, current.stressLevel - 0.2),
    };
  }

  public getIdentidade(): Identidade { return this.identidade; }
  public getMetaCognicao(): MetaCognicao { return this.metaCognicao; }
}
