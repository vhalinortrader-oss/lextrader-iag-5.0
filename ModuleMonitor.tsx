
import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, LinearProgress, Chip } from '@mui/material';
import { Activity, Cpu, Database, ShieldCheck, Zap } from 'lucide-react';
import { motion } from 'framer-motion';

interface SubModule {
  id: string;
  name: string;
  load: number;
  status: 'ONLINE' | 'STANDBY' | 'OPTIMIZING';
}

export const ModuleMonitor: React.FC = () => {
  const [modules, setModules] = useState<SubModule[]>([
    { id: 'nc', name: 'Neural Core', load: 42, status: 'ONLINE' },
    { id: 'qp', name: 'Quantum P.', load: 15, status: 'ONLINE' },
    { id: 'ra', name: 'Risk AGI', load: 8, status: 'STANDBY' },
    { id: 'sm', name: 'Sensory', load: 94, status: 'OPTIMIZING' }
  ]);

  useEffect(() => {
    const interval = setInterval(() => {
      setModules(prev => prev.map(m => ({
        ...m,
        load: Math.max(5, Math.min(99, m.load + (Math.random() - 0.5) * 5))
      })));
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="p-6 bg-[#010409]/60 backdrop-blur-xl rounded-3xl border border-white/5 shadow-2xl">
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center gap-3">
          <div className="p-2 bg-indigo-500/10 rounded-xl border border-indigo-500/20">
            <Cpu size={18} className="text-indigo-400" />
          </div>
          <div>
            <h2 className="text-[11px] font-black text-white uppercase tracking-[0.2em] leading-none">Resource Telemetry</h2>
            <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest mt-1 block">Sub-Module Load Distribution</span>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <Activity size={14} className="text-emerald-500 animate-pulse" />
          <span className="text-[9px] font-black text-emerald-500 uppercase tracking-widest">System Ready</span>
        </div>
      </div>

      <div className="space-y-6">
        {modules.map(mod => (
          <div key={mod.id} className="relative">
            <div className="flex justify-between items-end mb-2">
              <div className="flex items-center gap-2">
                <span className="text-[9px] font-black text-slate-400 uppercase tracking-widest">{mod.name}</span>
                <span className="text-[7px] font-black text-slate-600 uppercase px-1.5 py-0.5 bg-white/5 rounded border border-white/5">{mod.status}</span>
              </div>
              <span className={`text-[10px] font-mono font-black ${mod.load > 80 ? 'text-rose-400' : 'text-indigo-400'}`}>
                {mod.load.toFixed(1)}%
              </span>
            </div>
            <div className="h-1.5 bg-white/5 rounded-full overflow-hidden">
              <motion.div 
                className={`h-full ${mod.load > 80 ? 'bg-rose-500' : 'bg-indigo-500'}`}
                animate={{ width: `${mod.load}%` }}
                transition={{ type: "spring", stiffness: 50 }}
              />
            </div>
          </div>
        ))}
      </div>
      
      <div className="mt-6 pt-4 border-t border-white/5 flex justify-between items-center">
        <div className="flex items-center gap-2">
          <Database size={12} className="text-slate-600" />
          <span className="text-[8px] font-black text-slate-600 uppercase tracking-widest">Memory: 4.2 TB Free</span>
        </div>
        <div className="flex gap-1">
          {[1,2,3,4].map(i => (
            <div key={i} className={`w-1 h-1 rounded-full ${i <= 3 ? 'bg-indigo-500' : 'bg-slate-800'}`} />
          ))}
        </div>
      </div>
    </div>
  );
};
