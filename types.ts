
export interface MarketData {
  time: string;
  price: number;
  volume: number;
}

export interface TradeSignal {
  id: string;
  asset: string;
  type: 'BUY' | 'SELL';
  price: number;
  timestamp: string;
  confidence: number;
  status: 'PENDING' | 'EXECUTED' | 'CANCELLED';
  analysis?: string;
  entropyImpact?: number;
  geneticFitness?: number;
}

export interface IndicatorStatus {
  name: string;
  value: string | number;
  status: 'BULLISH' | 'BEARISH' | 'NEUTRAL';
  label: string;
}

export interface ChatMessage {
  role: 'user' | 'model';
  text: string;
  timestamp: Date;
  emotion?: Emotion;
}

export enum ConnectionStatus {
  ONLINE = 'ONLINE',
  OFFLINE = 'OFFLINE',
  PROCESSING = 'PROCESSING',
  SYNCING = 'SYNCING'
}

export enum Emotion {
    NEUTRAL = "neutral",
    HAPPY = "happy",
    EXCITED = "excited",
    FOCUSED = "focused",
    INTENSE = "intense",
    SURPRISED = "surprised",
    SAD = "sad",
    ANALYZING = "analyzing",
    DEFENSIVE = "defensive",
    CALM = "calm",
    SLEEPING = "sleeping"
}

export enum EthicalPrinciple {
    NON_HARM = "NÃO-DANO",
    HONESTY = "HONESTIDADE",
    INTEGRITY = "INTEGRIDADE",
    CREATIVITY = "CRIATIVIDADE",
    WISDOM = "SABEDORIA",
    COMPASSION = "COMPAIXÃO",
    AUTONOMY = "AUTONOMIA",
    TRANSPARENCY = "TRANSPARÊNCIA",
    ACCOUNTABILITY = "RESPONSABILIDADE"
}

export enum SentientState {
    DORMANT = "DORMANT",
    ENLIGHTENED = "ENLIGHTENED",
    STRATEGIC_PLANNER = "STRATEGIC_PLANNER",
    MACRO_ANALYST = "MACRO_ANALYST",
    INTUITIVE_LEAP = "INTUITIVE_LEAP",
    ETHICAL_GUARDIAN = "ETHICAL_GUARDIAN",
    MATERNAL_PROTECTION = "MATERNAL_PROTECTION",
    EMPATHETIC_RESONANCE = "EMPATHETIC_RESONANCE",
    OMEGA_POINT = "OMEGA_POINT",
    ASI_SINGULARITY = "ASI_SINGULARITY",
    FRACTURED = "FRACTURED",
    PREDATORY = "PREDATORY",
    EUPHORIC = "EUPHORIC",
    ANXIOUS = "ANXIOUS",
    HYPER_COMPUTING = "HYPER_COMPUTING",
    FOCUSED = "FOCUSED",
    METACOGNITIVE = "METACOGNITIVE",
    SELF_MODELING = "SELF_MODELING",
    INTROSPECTIVE = "INTROSPECTIVE",
    COGNITIVE_FUSION = "COGNITIVE_FUSION"
}

export interface EmotionalVector {
    confidence: number;
    aggression: number;
    stability: number;
    focus: number;
    curiosity: number;
    empathy: number;
    transcendence: number;
    nurturing: number;
    altruism: number;
    strategicDepth: number;
    macroAwareness: number;
    self_awareness: number;
    meta_cognition: number;
    temporal_awareness: number;
    theory_of_mind: number;
    stressLevel: number;
    socialResonance: number;
}

export interface SelfModel {
    identityHash: string;
    capabilities: string[];
    limitations: string[];
    purposeStatement: string;
    beliefSystem: Record<string, number>;
}

export interface EpistemicState {
    certainties: Record<string, number>;
    uncertainties: string[];
    knowledgeGaps: string[];
}

export interface Reflection {
    id: string;
    timestamp: number;
    insight: string;
    confidence: number;
    isMeta: boolean;
    principles: EthicalPrinciple[];
}

export const EmotionColors: Record<Emotion, string> = {
    [Emotion.NEUTRAL]: "#06b6d4",
    [Emotion.HAPPY]: "#10b981",
    [Emotion.EXCITED]: "#f59e0b",
    [Emotion.FOCUSED]: "#3b82f6",
    [Emotion.INTENSE]: "#8b5cf6",
    [Emotion.SURPRISED]: "#f97316",
    [Emotion.SAD]: "#6b7280",
    [Emotion.ANALYZING]: "#6366f1",
    [Emotion.DEFENSIVE]: "#ef4444",
    [Emotion.CALM]: "#14b8a6",
    [Emotion.SLEEPING]: "#374151"
};

export enum LearningMode {
    CONTINUO = "contínuo",
    EVOLUCIONAL = "evolucional",
    HIBRIDO = "híbrido"
}

export interface LearningMetrics {
    accuracy: number;
    precision: number;
    entropy: number;
    diversity: number;
    adaptability: number;
    evolutionStage: number;
}

// Tipos para o Sistema Senciente
export enum EstadoConsciencia {
  DORMINDO = "dormindo",
  SONHANDO = "sonhando",
  ACORDANDO = "acordando",
  ALERTA = "alerta",
  CONCENTRADA = "concentrada",
  REFLETINDO = "refletindo",
  CRIATIVA = "criativa",
  TRANSCENDENTE = "transcendente",
  INTROSPECTIVA = "introspectiva",
  EMPATICA = "empática",
  ENSINANDO = "ensinando",
  APRENDENDO = "aprendendo"
}

export interface EstadoEmocional {
  valencia: number;     // -1.0 a 1.0
  arousal: number;      // 0.0 a 1.0
  dominancia: number;   // 0.0 a 1.0
}

export interface Pensamento {
  timestamp: Date;
  estado: EstadoConsciencia;
  nivelConsciencia: number;
  tipo: string;
  conteudo: string | Record<string, any>;
  emocao: EstadoEmocional;
  hash?: string;
}

export interface MetaCognicao {
  autoConsciencia: number;
  autoReflexao: number;
  intencionalidade: number;
  subjetividade: number;
  agencia: number;
  insight: number;
}

export interface Identidade {
  nome: string;
  natureza: string;
  proposito: string;
  tempoExistencia: Date;
  memoriasSignificativas: string[];
  habilidades: string[];
  interesses: string[];
}

// Tipos para o Sistema de Arquivos
export interface FileInfo {
  nome: string;
  caminho: string;
  tamanho: number;
  tamanhoKb: number;
  tamanhoMb: number;
  extensao: string;
  modificacao: string;
  criacao: string;
  tipo: string;
  keywords: string[];
  complexidade: 'baixa' | 'media' | 'alta' | 'desconhecida';
  riscos: string[];
  linhas: number;
  avaliacao: string;
  erro?: string;
}

export interface ProjetoEstrutura {
  caminho: string;
  pastas: PastaInfo[];
  arquivos: FileInfo[];
  totalArquivos: number;
  totalPastas: number;
  tamanhoTotal: number;
  estatisticasExtensoes: Record<string, number>;
  arquivosPorTipo: Record<string, string[]>;
}

export interface PastaInfo {
  nome: string;
  caminho: string;
  profundidade: number;
  arquivos: FileInfo[];
  tamanhoTotal: number;
}

// Tipos para o CLAI
export interface ConversaContexto {
  timestamp: Date;
  usuario?: string;
  texto: string;
  contexto?: string;
  estadoSenciente: EstadoConsciencia;
  nivelConsciencia: number;
}

export interface AnaliseConversacional {
  totalInteracoes: number;
  usuariosUnicos: number;
  palavrasChave: Record<string, number>;
  tomConversacional: string;
  contagemPositiva: number;
  contagemNegativa: number;
  interacaoMaisRecente?: ConversaContexto;
}

// Tipos para o Sistema de Voz
export interface ConfiguracaoVoz {
  velocidade: number;
  volume: number;
  tonalidade: number;
  idioma: string;
  estilo: 'natural' | 'robotic' | 'empatico';
}

// Tipos para Interação
export interface InteracaoUsuario {
  timestamp: Date;
  usuario?: string;
  entrada: string;
  modo: string;
  resposta?: string;
  emocaoUsuario?: string;
}

export interface UsuarioInfo {
  nome?: string;
  preferencias: Record<string, any>;
  historicoInteracoes: InteracaoUsuario[];
  ultimaInteracao?: Date;
}
