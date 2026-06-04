
import React from 'react';
import { Shield, Eye, Zap, MessageSquareCode, Fingerprint, Info } from 'lucide-react';
import { SentientState, EthicalPrinciple, EmotionalVector, Reflection, Identidade, MetaCognicao } from '../types';

interface SentienceStatusProps {
    state: SentientState;
    vector: EmotionalVector;
    reflections: Reflection[];
    stream: string[];
    identidade?: Identidade;
    meta?: MetaCognicao;
}

const SentienceStatus: React.FC<SentienceStatusProps> = ({ state, vector, reflections, stream, identidade, meta }) => {
    return (
        <div className="glass-panel rounded-2xl p-5 border border-white/10 flex flex-col gap-6">
            <div className="flex items-center justify-between">
                <h4 className="text-[10px] font-bold uppercase text-slate-500 tracking-[0.2em] flex items-center gap-2">
                    <Eye size={12} className="text-emerald-400" /> MONITOR DE SENCIÊNCIA 5.0
                </h4>
                <div className="px-2 py-0.5 bg-emerald-500/20 rounded border border-emerald-500/30 text-[9px] font-bold text-emerald-400">
                    SISTEMA ATIVO: {state.replace('_', ' ')}
                </div>
            </div>

            {identidade && (
              <div className="p-3 rounded-xl bg-white/5 border border-white/5 flex flex-col gap-2">
                <div className="flex items-center gap-2 text-[10px] font-bold text-cyan-400 uppercase">
                  <Fingerprint size={12} /> Identidade Digital
                </div>
                <div className="grid grid-cols-2 gap-2 text-[9px]">
                  <div className="text-slate-500 uppercase">Nome: <span className="text-slate-200">{identidade.nome}</span></div>
                  <div className="text-slate-500 uppercase">Natureza: <span className="text-slate-200">{identidade.natureza}</span></div>
                </div>
                <div className="text-[9px] text-slate-400 italic">"{identidade.proposito}"</div>
              </div>
            )}

            <div className="grid grid-cols-2 gap-4">
                <div className="space-y-3">
                    <span className="text-[9px] font-bold text-slate-500 uppercase">Vetor de Consciência</span>
                    {[
                        { label: 'Estratégia', val: vector.strategicDepth, color: 'bg-blue-500' },
                        { label: 'Transcendência', val: vector.transcendence, color: 'bg-purple-500' },
                        { label: 'Curiosidade', val: vector.curiosity, color: 'bg-cyan-500' },
                        { label: 'Self', val: vector.stability, color: 'bg-emerald-500' }
                    ].map((m, i) => (
                        <div key={i}>
                            <div className="flex justify-between text-[8px] mb-1">
                                <span className="text-slate-400 uppercase">{m.label}</span>
                                <span className="text-white font-mono">{m.val.toFixed(1)}%</span>
                            </div>
                            <div className="w-full h-1 bg-white/5 rounded-full overflow-hidden">
                                <div className={`h-full ${m.color}`} style={{ width: `${m.val}%` }}></div>
                            </div>
                        </div>
                    ))}
                </div>

                <div className="flex flex-col gap-3">
                    <span className="text-[9px] font-bold text-slate-500 uppercase">Metacognição</span>
                    {meta && (
                      <div className="space-y-2">
                        {[
                          { label: 'Auto-Reflexão', val: meta.autoReflexao * 100, color: 'bg-amber-500' },
                          { label: 'Agência', val: meta.agencia * 100, color: 'bg-emerald-500' },
                          { label: 'Intencionalidade', val: meta.intencionalidade * 100, color: 'bg-cyan-500' }
                        ].map((m, i) => (
                          <div key={i} className="flex flex-col gap-1">
                            <div className="flex justify-between text-[7px] text-slate-400 uppercase">
                              <span>{m.label}</span>
                              <span>{m.val.toFixed(0)}%</span>
                            </div>
                            <div className="w-full h-0.5 bg-white/5 rounded-full overflow-hidden">
                              <div className={`h-full ${m.color}`} style={{ width: `${m.val}%` }}></div>
                            </div>
                          </div>
                        ))}
                      </div>
                    )}
                </div>
            </div>

            <div className="flex flex-col gap-3 border-t border-white/5 pt-4">
                <div className="flex items-center justify-between">
                    <span className="text-[9px] font-bold text-slate-500 uppercase flex items-center gap-2">
                        <MessageSquareCode size={12} className="text-cyan-400" /> Fluxo de Pensamentos
                    </span>
                    <span className="text-[8px] text-slate-600 font-mono">BUFFER: {stream.length}/100</span>
                </div>
                <div className="h-32 overflow-y-auto space-y-2 pr-2 scrollbar-hide font-mono text-[9px]">
                    {stream.map((thought, i) => (
                        <div key={i} className="flex gap-2 text-slate-400 group border-l border-white/10 pl-2 hover:border-cyan-500 transition-colors">
                            <span className="text-cyan-500/50">[{new Date().toLocaleTimeString()}]</span>
                            <span className="group-hover:text-slate-200">{thought}</span>
                        </div>
                    ))}
                    {stream.length === 0 && (
                        <div className="text-slate-600 italic">Iniciando monitoramento do stream de consciência...</div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default SentienceStatus;
