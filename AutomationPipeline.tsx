
import React from 'react';
import { Settings, Play, ShieldCheck, Cpu, Database, Send } from 'lucide-react';

const AutomationPipeline: React.FC = () => {
  const steps = [
    { name: 'Data Processor', status: 'ACTIVE', icon: <Database size={14} />, color: 'text-cyan-400' },
    { name: 'VHALINOR Brain', status: 'SYNCHRONIZED', icon: <Cpu size={14} />, color: 'text-purple-400' },
    { name: 'Decision Engine', status: 'READY', icon: <Settings size={14} />, color: 'text-blue-400' },
    { name: 'Risk Manager', status: 'SECURE', icon: <ShieldCheck size={14} />, color: 'text-emerald-400' },
    { name: 'Order Executor', status: 'STANDBY', icon: <Send size={14} />, color: 'text-amber-400' },
  ];

  return (
    <div className="glass-panel rounded-2xl p-5 border border-white/10 flex flex-col gap-4">
      <div className="flex items-center justify-between mb-2">
        <h4 className="text-[10px] font-bold uppercase text-slate-500 tracking-widest flex items-center gap-2">
          <Play size={12} className="text-cyan-400 fill-cyan-400/20" /> PIPELINE DE AUTOMAÇÃO 4.0
        </h4>
        <span className="text-[8px] font-mono text-cyan-500 bg-cyan-500/10 px-2 py-0.5 rounded border border-cyan-500/20">TRADING-AUTOMATION/MAIN.PY: LIVE</span>
      </div>

      <div className="flex flex-col gap-3">
        {steps.map((step, i) => (
          <div key={i} className="flex items-center gap-4 group">
            <div className={`p-2 rounded-lg bg-white/5 border border-white/5 group-hover:border-white/10 transition-all ${step.color}`}>
              {step.icon}
            </div>
            <div className="flex-1">
              <div className="flex justify-between items-center mb-1">
                <span className="text-[10px] font-bold text-slate-300 uppercase">{step.name}</span>
                <span className={`text-[8px] font-mono ${step.color}`}>{step.status}</span>
              </div>
              <div className="w-full h-[2px] bg-white/5 rounded-full relative">
                <div className={`absolute top-0 left-0 h-full w-full opacity-20 ${step.color.replace('text', 'bg')} animate-pulse`}></div>
              </div>
            </div>
            {i < steps.length - 1 && (
              <div className="absolute left-[38px] mt-[40px] w-[1px] h-4 bg-white/5"></div>
            )}
          </div>
        ))}
      </div>

      <div className="mt-2 text-[9px] text-slate-500 italic flex items-center gap-2">
        <div className="w-1 h-1 rounded-full bg-cyan-500 animate-ping"></div>
        Fluxo de execução em baixa latência (Binance Futures Gateway)
      </div>
    </div>
  );
};

export default AutomationPipeline;
