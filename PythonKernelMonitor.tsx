
import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, Grid, Chip, alpha } from '@mui/material';
import { Terminal, Cpu, HardDrive, Play, AlertCircle, RefreshCcw, FileText } from 'lucide-react';
import { pyBridge } from '../services/PythonBridgeService';
import { PythonKernelStatus } from '../types';

export const PythonKernelMonitor: React.FC = () => {
  const [kernels, setKernels] = useState<PythonKernelStatus[]>([]);
  const [activeLogs, setActiveLogs] = useState<string[]>([]);

  useEffect(() => {
    const interval = setInterval(() => {
      const updated = pyBridge.getKernels();
      setKernels(updated);
      
      const running = updated.filter(k => k.status === 'RUNNING');
      if (running.length > 0) {
        const randomKernel = running[Math.floor(Math.random() * running.length)];
        setActiveLogs(prev => [...prev.slice(-9), pyBridge.getPyLog(randomKernel.fileName)]);
      }
    }, 4000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="p-6 bg-[#010409]/60 backdrop-blur-xl rounded-3xl border border-white/5 shadow-2xl">
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center gap-3">
          <div className="p-2 bg-yellow-500/10 rounded-xl border border-yellow-500/20">
            <FileText size={18} className="text-yellow-400" />
          </div>
          <div>
            <h2 className="text-[11px] font-black text-white uppercase tracking-[0.2em] leading-none">Python Bridge</h2>
            <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest mt-1 block">Legacy Motors Integration</span>
          </div>
        </div>
        <div className="px-2 py-1 bg-emerald-500/10 rounded-lg border border-emerald-500/20">
          <span className="text-[8px] font-black text-emerald-500 uppercase tracking-widest">
            {kernels.filter(k => k.status === 'RUNNING').length} Active
          </span>
        </div>
      </div>

      <div className="space-y-3 mb-6">
        {kernels.map(k => (
          <div key={k.fileName} className="p-3 bg-black/40 rounded-2xl border border-white/5 group hover:border-yellow-500/30 transition-colors">
            <div className="flex justify-between items-start">
              <div>
                <h3 className="text-[10px] font-black text-white uppercase tracking-widest">{k.fileName}</h3>
                <p className="text-[8px] font-black text-slate-500 uppercase tracking-widest mt-0.5">{k.purpose}</p>
              </div>
              <div className="flex items-center gap-2">
                <span className={`text-[7px] font-black uppercase tracking-widest ${k.status === 'RUNNING' ? 'text-emerald-400' : 'text-slate-600'}`}>{k.status}</span>
                <div className={`w-1.5 h-1.5 rounded-full ${k.status === 'RUNNING' ? 'bg-emerald-500 animate-pulse' : 'bg-slate-800'}`} />
              </div>
            </div>
            {k.pid > 0 && (
              <div className="mt-2 flex items-center gap-3">
                <div className="flex items-center gap-1">
                  <Cpu size={10} className="text-slate-600" />
                  <span className="text-[7px] font-mono text-slate-600 uppercase tracking-widest">PID: {k.pid}</span>
                </div>
                <div className="flex items-center gap-1">
                  <HardDrive size={10} className="text-slate-600" />
                  <span className="text-[7px] font-mono text-slate-600 uppercase tracking-widest">MEM: {k.memoryUsage}</span>
                </div>
              </div>
            )}
          </div>
        ))}
      </div>

      <div className="p-4 bg-black rounded-2xl border border-white/5 font-mono">
        <div className="flex items-center gap-2 mb-3">
          <Terminal size={12} className="text-yellow-400" />
          <span className="text-[8px] font-black text-yellow-400 uppercase tracking-widest">Bridge Log Stream</span>
        </div>
        <div className="h-32 overflow-hidden flex flex-col gap-1">
           {activeLogs.map((log, i) => (
             <div key={i} className="text-[9px] text-slate-500 whitespace-nowrap overflow-hidden text-ellipsis">
               <span className="text-yellow-600/50 mr-2">λ</span> {log}
             </div>
           ))}
        </div>
      </div>
    </div>
  );
};
