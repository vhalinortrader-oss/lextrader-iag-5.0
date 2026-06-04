
import React from 'react';
// Adicionado Sparkles ao import do lucide-react
import { Eye, Radio, Activity, Database, Mic, Camera, Sparkles } from 'lucide-react';
import { Box, Typography, Paper, Grid, alpha } from '@mui/material';

export const SensoryCortex: React.FC = () => {
  return (
    <div className="p-6 bg-[#010409]/60 backdrop-blur-xl rounded-3xl border border-white/5 shadow-2xl">
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 bg-amber-500/10 rounded-xl border border-amber-500/20">
          <Eye size={18} className="text-amber-400" />
        </div>
        <div>
          <h2 className="text-[11px] font-black text-white uppercase tracking-[0.2em] leading-none">Sensory Cortex</h2>
          <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest mt-1 block">Multimodal Ingestion v5.0</span>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-3">
        <SensorItem icon={<Radio size={14}/>} label="Market Feed" status="LIVE" value="450ms" />
        <SensorItem icon={<Mic size={14}/>} label="Audio Driver" status="READY" value="24kHz" />
        <SensorItem icon={<Camera size={14}/>} label="Vision Node" status="SCANNING" value="12 FPS" />
        <SensorItem icon={<Database size={14}/>} label="DEX Oracles" status="STABLE" value="Sync 100%" />
      </div>
      
      <div className="mt-6 p-3 bg-amber-500/5 rounded-2xl border border-dashed border-amber-500/20">
        <div className="flex items-center gap-2">
          <Sparkles size={12} className="text-amber-400" />
          <span className="text-[8px] font-black text-amber-400 uppercase tracking-widest">Perception Bias: Neutral (0.012 Entropy)</span>
        </div>
      </div>
    </div>
  );
};

const SensorItem = ({ icon, label, status, value }: any) => (
  <div className="p-3 bg-black/40 rounded-2xl border border-white/5 group hover:border-amber-500/30 transition-colors">
    <div className="flex items-center gap-2 mb-2">
      <span className="text-slate-500 group-hover:text-amber-400 transition-colors">{icon}</span>
      <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest">{label}</span>
    </div>
    <div className="flex justify-between items-end">
      <span className="text-[10px] font-black text-white font-mono tracking-tighter">{value}</span>
      <span className="text-[7px] font-black text-emerald-500 uppercase tracking-widest">{status}</span>
    </div>
  </div>
);
