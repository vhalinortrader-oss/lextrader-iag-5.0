
import React from 'react';
import { Fingerprint, Target, AlertTriangle, Lightbulb, BookOpen, Fingerprint as IdIcon } from 'lucide-react';
import { SelfModel, EpistemicState } from '../types';

interface SelfModelMonitorProps {
  model: SelfModel;
  epistemic: EpistemicState;
}

const SelfModelMonitor: React.FC<SelfModelMonitorProps> = ({ model, epistemic }) => {
  return (
    <div className="glass-panel rounded-2xl p-5 border border-white/10 flex flex-col gap-5">
      <div className="flex items-center justify-between mb-1">
        <h4 className="text-[10px] font-bold uppercase text-slate-500 tracking-widest flex items-center gap-2">
          <IdIcon size={12} className="text-blue-400" /> MODELO DE IDENTIDADE (SELF)
        </h4>
        <span className="text-[8px] font-mono text-slate-600">HASH: {model.identityHash.substring(0, 16)}...</span>
      </div>

      <div className="space-y-4">
        <div className="p-3 rounded-xl bg-white/5 border border-white/5">
          <div className="flex items-center gap-2 mb-2 text-blue-400">
            <Target size={12} />
            <span className="text-[9px] font-bold uppercase tracking-tight">Propósito Fundamental</span>
          </div>
          <p className="text-[10px] text-slate-300 italic leading-relaxed">
            "{model.purposeStatement}"
          </p>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-2">
            <span className="text-[9px] font-bold text-slate-500 uppercase flex items-center gap-1">
              <Lightbulb size={10} className="text-emerald-400" /> Capacidades
            </span>
            <div className="flex flex-wrap gap-1">
              {model.capabilities.map((c, i) => (
                <span key={i} className="px-1.5 py-0.5 rounded bg-emerald-500/10 border border-emerald-500/20 text-[8px] text-emerald-400 uppercase font-mono">
                  {c.replace('_', ' ')}
                </span>
              ))}
            </div>
          </div>
          <div className="space-y-2">
            <span className="text-[9px] font-bold text-slate-500 uppercase flex items-center gap-1">
              <AlertTriangle size={10} className="text-rose-400" /> Limitações
            </span>
            <div className="flex flex-wrap gap-1">
              {model.limitations.map((l, i) => (
                <span key={i} className="px-1.5 py-0.5 rounded bg-rose-500/10 border border-rose-500/20 text-[8px] text-rose-400 uppercase font-mono">
                  {l.replace('_', ' ')}
                </span>
              ))}
            </div>
          </div>
        </div>

        <div className="border-t border-white/5 pt-4">
          <div className="flex items-center justify-between mb-3">
            <span className="text-[9px] font-bold text-slate-500 uppercase flex items-center gap-1">
              <BookOpen size={10} className="text-amber-400" /> Estado Epistêmico
            </span>
            <span className="text-[8px] text-slate-600 font-mono">INCERTEZAS: {epistemic.uncertainties.length}</span>
          </div>
          <div className="space-y-2">
             {epistemic.knowledgeGaps.slice(-3).map((gap, i) => (
               <div key={i} className="flex gap-2 items-center p-2 rounded-lg bg-amber-500/5 border border-amber-500/10 text-[9px] text-amber-200/80">
                 <div className="w-1 h-1 rounded-full bg-amber-500"></div>
                 {gap}
               </div>
             ))}
             {epistemic.knowledgeGaps.length === 0 && <span className="text-[9px] text-slate-600 italic">Nenhuma lacuna crítica detectada no momento.</span>}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SelfModelMonitor;
