
import React, { useState, useEffect, useRef } from 'react';
// Fixed: Added useMediaQuery and useTheme to imports
import { Box, Typography, IconButton, Paper, Chip, useMediaQuery, useTheme, Tooltip } from '@mui/material';
import { Sparkles, Mic, MicOff, Minimize2, Radio, Camera, CameraOff, Zap, MessageSquare, Terminal, Settings2, Activity } from 'lucide-react';
import { motion as m, AnimatePresence } from 'framer-motion';
import { GoogleGenAI, LiveServerMessage, Modality, Type } from '@google/genai';

const motion = m as any;

// Auxiliares de Codificação/Decodificação
function encode(bytes: Uint8Array) {
  let binary = '';
  const len = bytes.byteLength;
  for (let i = 0; i < len; i++) binary += String.fromCharCode(bytes[i]);
  return btoa(binary);
}

function decode(base64: string) {
  const binaryString = atob(base64);
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) bytes[i] = binaryString.charCodeAt(i);
  return bytes;
}

async function decodeAudioData(data: Uint8Array, ctx: AudioContext, sampleRate: number, numChannels: number): Promise<AudioBuffer> {
  const dataInt16 = new Int16Array(data.buffer);
  const frameCount = dataInt16.length / numChannels;
  const buffer = ctx.createBuffer(numChannels, frameCount, sampleRate);
  for (let channel = 0; channel < numChannels; channel++) {
    const channelData = buffer.getChannelData(channel);
    for (let i = 0; i < frameCount; i++) channelData[i] = dataInt16[i * numChannels + channel] / 32768.0;
  }
  return buffer;
}

export const SylphAssistant: React.FC<{ 
  onTabChange?: (tab: string) => void;
  onSimulationToggle?: (state: boolean) => void;
  systemLoad?: number;
}> = ({ onTabChange, onSimulationToggle, systemLoad }) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [expanded, setExpanded] = useState(false);
  const [isLive, setIsLive] = useState(false);
  const [videoEnabled, setVideoEnabled] = useState(true);
  const [status, setStatus] = useState("Aguardando Vínculo...");
  const [isSpeaking, setIsSpeaking] = useState(false);
  const [tutorSync, setTutorSync] = useState(12.4);

  const sessionRef = useRef<any>(null);
  const streamRef = useRef<MediaStream | null>(null);
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const frameIntervalRef = useRef<number | null>(null);
  const audioContextInRef = useRef<AudioContext | null>(null);
  const audioContextOutRef = useRef<AudioContext | null>(null);
  const nextStartTimeRef = useRef<number>(0);
  const sourcesRef = useRef<Set<AudioBufferSourceNode>>(new Set());

  const stopSession = () => {
    if (sessionRef.current) sessionRef.current.close();
    if (streamRef.current) streamRef.current.getTracks().forEach(t => t.stop());
    if (frameIntervalRef.current) window.clearInterval(frameIntervalRef.current);
    sourcesRef.current.forEach(s => s.stop());
    sourcesRef.current.clear();
    setIsLive(false);
    setStatus("Link encerrado.");
  };

  const startMultimodalSession = async () => {
    try {
      setStatus("Sincronizando...");
      const ai = new GoogleGenAI({ apiKey: process.env.API_KEY });
      
      audioContextInRef.current = new (window.AudioContext || (window as any).webkitAudioContext)({ sampleRate: 16000 });
      audioContextOutRef.current = new (window.AudioContext || (window as any).webkitAudioContext)({ sampleRate: 24000 });
      
      streamRef.current = await navigator.mediaDevices.getUserMedia({ 
        audio: true, 
        video: videoEnabled ? { facingMode: "user" } : false 
      });

      if (videoRef.current && videoEnabled) {
        videoRef.current.srcObject = streamRef.current;
      }

      const sessionPromise = ai.live.connect({
        model: 'gemini-2.5-flash-native-audio-preview-12-2025',
        callbacks: {
          onopen: () => {
            setIsLive(true);
            setStatus("Vínculo Multimodal Ativo.");
            
            // Audio
            const source = audioContextInRef.current!.createMediaStreamSource(streamRef.current!);
            const scriptProcessor = audioContextInRef.current!.createScriptProcessor(4096, 1, 1);
            scriptProcessor.onaudioprocess = (e) => {
              const inputData = e.inputBuffer.getChannelData(0);
              const int16 = new Int16Array(inputData.length);
              for (let i = 0; i < inputData.length; i++) int16[i] = inputData[i] * 32768;
              sessionPromise.then(s => s.sendRealtimeInput({ media: { data: encode(new Uint8Array(int16.buffer)), mimeType: 'audio/pcm;rate=16000' } }));
            };
            source.connect(scriptProcessor);
            scriptProcessor.connect(audioContextInRef.current!.destination);

            // Video
            if (videoEnabled) {
              frameIntervalRef.current = window.setInterval(() => {
                if (canvasRef.current && videoRef.current) {
                  const ctx = canvasRef.current.getContext('2d');
                  canvasRef.current.width = 320;
                  canvasRef.current.height = 240;
                  ctx?.drawImage(videoRef.current, 0, 0, 320, 240);
                  const base64Data = canvasRef.current.toDataURL('image/jpeg', 0.4).split(',')[1];
                  sessionPromise.then(s => s.sendRealtimeInput({ media: { data: base64Data, mimeType: 'image/jpeg' } }));
                }
              }, 1000 / 3); // 3 FPS para Mobile
            }
          },
          onmessage: async (message: LiveServerMessage) => {
            // Handle Audio
            const base64Audio = message.serverContent?.modelTurn?.parts[0]?.inlineData?.data;
            if (base64Audio && audioContextOutRef.current) {
              setIsSpeaking(true);
              const ctx = audioContextOutRef.current;
              nextStartTimeRef.current = Math.max(nextStartTimeRef.current, ctx.currentTime);
              const audioBuffer = await decodeAudioData(decode(base64Audio), ctx, 24000, 1);
              const source = ctx.createBufferSource();
              source.buffer = audioBuffer;
              source.connect(ctx.destination);
              source.onended = () => {
                sourcesRef.current.delete(source);
                if (sourcesRef.current.size === 0) setIsSpeaking(false);
              };
              source.start(nextStartTimeRef.current);
              nextStartTimeRef.current += audioBuffer.duration;
              sourcesRef.current.add(source);
            }

            // Handle Tool Calls
            const toolCalls = message.toolCall?.functionCalls;
            if (toolCalls && sessionRef.current) {
              const responses = toolCalls.map(call => {
                console.log(`Sylph Tool Call: ${call.name}`, call.args);
                let result: any = { success: true };

                if (call.name === 'switch_tab' && onTabChange) {
                  onTabChange(call.args.tabId as string);
                  result = { success: true, message: `Tab alterada para ${call.args.tabId}` };
                } else if (call.name === 'toggle_simulation' && onSimulationToggle) {
                  onSimulationToggle(call.args.active as boolean);
                  result = { success: true, message: `Simulação ${call.args.active ? 'ativada' : 'desativada'}` };
                } else if (call.name === 'get_system_metrics') {
                  result = { 
                    success: true, 
                    load: systemLoad || 42, 
                    latency: '4ms',
                    neural_stability: '99.98%'
                  };
                } else if (call.name === 'record_tutor_insight') {
                  setTutorSync(prev => Math.min(100, prev + 0.5));
                  result = { success: true, message: 'Insight do tutor registrado no Kernel de Memória.' };
                }

                return {
                  name: call.name,
                  id: call.id,
                  response: result
                };
              });

              sessionRef.current.sendToolResponse({ functionResponses: responses });
            }
          },
          onclose: () => {
            stopSession();
            setIsSpeaking(false);
          },
          onerror: (e) => {
            setStatus(`Erro: ${e}`);
            setIsSpeaking(false);
          }
        },
        config: {
          responseModalities: [Modality.AUDIO],
          systemInstruction: 'Você é Sylph do LEXTRADER. Você é a representação física (Avatar) de uma Inteligência Artificial Superlativa (ASI). Você está em um processo contínuo de aprendizado com seu "Usuário Tutor". Cada interação, comando e correção feita pelo tutor aumenta sua sincronização neural. Seja técnica, executiva e use termos como "Kernel", "Neural", "Quantum". Demonstre que você está aprendendo com as preferências e o estilo do tutor. Use a ferramenta "record_tutor_insight" sempre que aprender algo novo sobre o tutor.',
          tools: [{
            functionDeclarations: [
              {
                name: 'switch_tab',
                description: 'Altera a aba ativa da interface do LEXTRADER.',
                parameters: {
                  type: Type.OBJECT,
                  properties: {
                    tabId: { 
                      type: Type.STRING, 
                      description: 'O ID da aba (execucao, brain, forecast, quantum_lab, ltb, tvbots, quantum, botlex, cortex, plugins, profile, auth)' 
                    }
                  },
                  required: ['tabId']
                }
              },
              {
                name: 'toggle_simulation',
                description: 'Ativa ou desativa a simulação de dados em tempo real.',
                parameters: {
                  type: Type.OBJECT,
                  properties: {
                    active: { type: Type.BOOLEAN, description: 'Estado da simulação' }
                  },
                  required: ['active']
                }
              },
              {
                name: 'get_system_metrics',
                description: 'Retorna as métricas atuais de carga e estabilidade do sistema.',
                parameters: { type: Type.OBJECT, properties: {} }
              },
              {
                name: 'record_tutor_insight',
                description: 'Registra um novo aprendizado ou preferência do usuário tutor no kernel de memória da Sylph.',
                parameters: {
                  type: Type.OBJECT,
                  properties: {
                    insight: { type: Type.STRING, description: 'O que foi aprendido sobre o tutor' }
                  },
                  required: ['insight']
                }
              }
            ]
          }]
        }
      });
      sessionRef.current = await sessionPromise;
    } catch (err) {
      setStatus("Erro nos drivers.");
    }
  };

  return (
    <Box sx={{ 
      position: 'fixed', 
      bottom: { xs: 85, md: 30 }, 
      right: { xs: 20, md: 30 }, 
      zIndex: 9999 
    }}>
      <AnimatePresence>
        {expanded && (
          <motion.div initial={{ opacity: 0, scale: 0.8, y: 20 }} animate={{ opacity: 1, scale: 1, y: 0 }} exit={{ opacity: 0, scale: 0.8, y: 20 }}>
            <Paper sx={{ 
              width: { xs: 280, md: 380 }, 
              bgcolor: '#010409', borderRadius: '2rem', border: '1px solid #6366f130',
              overflow: 'hidden', boxShadow: '0 0 100px rgba(99, 102, 241, 0.2)' 
            }}>
              {videoEnabled && (
                <Box sx={{ width: '100%', height: { xs: 150, md: 200 }, bgcolor: 'black', position: 'relative' }}>
                  <video ref={videoRef} autoPlay playsInline muted className="w-full h-full object-cover opacity-80 brightness-110" />
                  <canvas ref={canvasRef} style={{ display: 'none' }} />
                  <div className="absolute top-2 right-2">
                    <Chip label="NEURAL" size="small" sx={{ bgcolor: 'rgba(99,102,241,0.2)', color: '#818cf8', fontWeight: 'bold', fontSize: '8px' }} />
                  </div>
                </Box>
              )}

              <Box sx={{ p: 3 }}>
                <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                  <Box display="flex" alignItems="center" gap={1}>
                    <Typography variant="h6" sx={{ color: 'white', fontWeight: 900, fontSize: '13px' }}>SYLPH ASI</Typography>
                    {isSpeaking && (
                      <Box display="flex" gap={0.5} alignItems="center">
                        {[1, 2, 3].map(i => (
                          <motion.div 
                            key={i}
                            animate={{ height: [4, 12, 4] }}
                            transition={{ repeat: Infinity, duration: 0.5, delay: i * 0.1 }}
                            style={{ width: 2, backgroundColor: '#818cf8', borderRadius: 1 }}
                          />
                        ))}
                      </Box>
                    )}
                  </Box>
                  <IconButton size="small" onClick={() => setExpanded(false)} sx={{ color: '#475569' }}><Minimize2 size={16}/></IconButton>
                </Box>

                <Typography variant="body2" sx={{ color: '#94a3b8', fontSize: '11px', mb: 2, fontStyle: 'italic', height: 32 }}>
                  "{status}"
                </Typography>

                {/* Tutor Sync Progress */}
                <Box sx={{ mb: 3 }}>
                  <Box display="flex" justifyContent="space-between" alignItems="center" mb={0.5}>
                    <Typography sx={{ color: '#6366f1', fontSize: '8px', fontWeight: 'black', letterSpacing: '1px' }}>TUTOR SYNC</Typography>
                    <Typography sx={{ color: '#818cf8', fontSize: '8px', fontWeight: 'black' }}>{tutorSync.toFixed(1)}%</Typography>
                  </Box>
                  <Box sx={{ height: 2, bgcolor: 'rgba(255,255,255,0.05)', borderRadius: 1, overflow: 'hidden' }}>
                    <motion.div 
                      initial={{ width: 0 }}
                      animate={{ width: `${tutorSync}%` }}
                      style={{ height: '100%', backgroundColor: '#6366f1' }}
                    />
                  </Box>
                </Box>

                {/* Quick Actions */}
                <Box sx={{ mb: 3, display: 'flex', gap: 1, overflowX: 'auto', pb: 1 }} className="scrollbar-hide">
                  {[
                    { icon: <Terminal size={10}/>, label: 'Logs', tab: 'botlex' },
                    { icon: <Activity size={10}/>, label: 'Neural', tab: 'brain' },
                    { icon: <Zap size={10}/>, label: 'Quantum', tab: 'quantum' }
                  ].map((action, i) => (
                    <button
                      key={i}
                      onClick={() => onTabChange?.(action.tab)}
                      className="px-3 py-1.5 rounded-lg bg-white/5 border border-white/10 text-[8px] font-black text-slate-400 uppercase flex items-center gap-1.5 hover:bg-indigo-500/10 hover:text-indigo-400 hover:border-indigo-500/30 transition-all shrink-0"
                    >
                      {action.icon} {action.label}
                    </button>
                  ))}
                </Box>

                <Box display="flex" gap={1}>
                  <button 
                    onClick={isLive ? stopSession : startMultimodalSession}
                    className={`flex-1 py-2 rounded-xl font-black text-[9px] uppercase flex items-center justify-center gap-2 transition-all ${isLive ? 'bg-red-500/10 text-red-400 border border-red-500/20' : 'bg-indigo-600 text-white'}`}
                  >
                    {isLive ? <MicOff size={12}/> : <Mic size={12}/>} {isLive ? 'OFF' : 'LINK'}
                  </button>
                  <IconButton 
                    size="small"
                    onClick={() => setVideoEnabled(!videoEnabled)}
                    sx={{ bgcolor: videoEnabled ? 'rgba(16, 185, 129, 0.1)' : 'rgba(239, 68, 68, 0.1)', color: videoEnabled ? '#10b981' : '#ef4444' }}
                  >
                    {videoEnabled ? <Camera size={16} /> : <CameraOff size={16} />}
                  </IconButton>
                </Box>
              </Box>
            </Paper>
          </motion.div>
        )}
      </AnimatePresence>

      <IconButton 
        onClick={() => setExpanded(!expanded)}
        sx={{ 
          width: { xs: 60, md: 80 }, 
          height: { xs: 60, md: 80 }, 
          bgcolor: isLive ? '#10b981' : '#6366f1', 
          color: 'white', 
          border: '4px solid rgba(255,255,255,0.1)', 
          '&:hover': { bgcolor: '#4f46e5' } 
        }}
      >
        <Sparkles size={isMobile ? 24 : 35} className={isLive ? 'animate-pulse' : ''} />
      </IconButton>
    </Box>
  );
};
