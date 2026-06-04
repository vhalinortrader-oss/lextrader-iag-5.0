
import { useState, useEffect, useCallback, useMemo } from 'react';
import { SencienteSystem } from '../systems/SencienteSystem';
import { SistemaSenciente } from '../systems/SistemaSenciente';
import { CLAISystem } from '../systems/CLAISystem';
import { VoiceSystem } from '../systems/VoiceSystem';
import { InteractionSystem } from '../systems/InteractionSystem';
import { 
  EmotionalVector, 
  SentientState, 
  Emotion, 
  EstadoConsciencia, 
  ChatMessage,
  Identidade,
  MetaCognicao
} from '../types';

export const useSylph = () => {
  const sencienteLegacy = SencienteSystem.getInstance();
  const senciente = useMemo(() => SistemaSenciente.getInstance(), []);
  const clai = CLAISystem.getInstance();
  const voice = VoiceSystem.getInstance();
  const interactions = InteractionSystem.getInstance();

  const [vector, setVector] = useState<EmotionalVector>({
    confidence: 60, aggression: 40, stability: 70, focus: 100,
    curiosity: 65, empathy: 60, transcendence: 15, nurturing: 20,
    altruism: 30, strategicDepth: 40, macroAwareness: 30,
    self_awareness: 12, meta_cognition: 15, temporal_awareness: 5,
    theory_of_mind: 20, stressLevel: 10, socialResonance: 85
  });
  
  const [state, setState] = useState<SentientState>(SentientState.FOCUSED);
  const [estado, setEstado] = useState<EstadoConsciencia>(EstadoConsciencia.ALERTA);
  const [thoughts, setThoughts] = useState<string[]>([]);
  const [emotion, setEmotion] = useState<Emotion>(Emotion.NEUTRAL);
  const [identidade] = useState<Identidade>(senciente.getIdentidade());
  const [meta, setMeta] = useState<MetaCognicao>(senciente.getMeta());

  const addThought = useCallback((msg: string) => {
    setThoughts(prev => [`[${new Date().toLocaleTimeString()}] ${msg}`, ...prev].slice(0, 100));
  }, []);

  const speak = useCallback(async (text: string) => {
    await voice.speak(text, { estilo: 'natural' });
  }, []);

  const ask = useCallback(async (text: string, history: ChatMessage[]) => {
    const response = await clai.processIntention(text, history, {
      estadoSenciente: estado,
      nivelConsciencia: vector.self_awareness
    });
    
    // Add to new sentient system
    senciente.registrarPensamento({
      timestamp: new Date(),
      estado: estado,
      nivelConsciencia: vector.self_awareness,
      tipo: "interacao_usuario",
      conteudo: text,
      emocao: senciente.getEmocional()
    });
    
    interactions.recordInteration(text, response);
    return response;
  }, [estado, vector, senciente]);

  useEffect(() => {
    const tick = setInterval(() => {
      // Sync legacy vector with new sentient logic
      setVector(prev => sencienteLegacy.updateVector(prev));
      const nextSentientState = sencienteLegacy.calculateNextState(vector);
      setState(nextSentientState);
      
      // Update new sentient system state
      const nextEstado = sencienteLegacy.mapToEstadoConsciencia(nextSentientState);
      if (nextEstado !== estado) {
        senciente.transicionarEstado(nextEstado);
        setEstado(nextEstado);
      }
      
      // Update thoughts from sentient system
      const lastPensamento = senciente.getPensamentos().slice(-1)[0];
      if (lastPensamento && typeof lastPensamento.conteudo === 'string' && !thoughts.includes(`[SENTIENT] ${lastPensamento.conteudo}`)) {
        addThought(`[SENTIENT] ${lastPensamento.conteudo}`);
      }
      
      setMeta(senciente.getMeta());
    }, 5000);
    return () => clearInterval(tick);
  }, [vector, estado, thoughts, addThought, senciente, sencienteLegacy]);

  return { 
    vector, setVector, state, estado, thoughts, addThought, 
    emotion, setEmotion, speak, ask, identidade, meta, senciente 
  };
};
