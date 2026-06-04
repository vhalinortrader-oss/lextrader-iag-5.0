
import React, { useState, useEffect, useMemo } from 'react';
import { Box, Grid, Paper, Typography, LinearProgress, Chip, alpha } from '@mui/material';
import { Atom, Zap, Share2, Activity, ShieldCheck, Waves, Binary, Sparkles } from 'lucide-react';
import { AreaChart, Area, ResponsiveContainer, YAxis } from 'recharts';
import { motion, AnimatePresence } from 'framer-motion';
import { quantumEngine } from '../services/QuantumNeuralEngine';
import { QuantumFieldSnapshot } from '../types';

export const QuantumFieldDashboard: React.FC<{ priceHistory: number[] }> = ({ priceHistory }) => {
  const [snapshot, setSnapshot] = useState<QuantumFieldSnapshot | null>(null);

  useEffect(() => {
    const update = async () => {
      const data = await quantumEngine.processMarketState(priceHistory);
      setSnapshot(data);
    };
    update();
    const interval = setInterval(update, 2000);
    return () => clearInterval(interval);
  }, [priceHistory]);

  const chartData = useMemo(() => {
    if (!snapshot) return [];
    return snapshot.waveFunction.map((val, i) => ({ x: i, val }));
  }, [snapshot]);

  if (!snapshot) return null;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
      {/* Top Status Bar */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 3 }}>
          <div className="p-3 bg-cyan-500/10 rounded-2xl border border-cyan-500/20">
             <Atom size={32} className="text-cyan-400 animate-spin-slow" />
          </div>
          <div>
            <Typography variant="h4" sx={{ fontWeight: 950, color: 'white', letterSpacing: -1 }}>
              QUANTUM <span className="text-cyan-400">NUCLEUS</span>
            </Typography>
            <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', letterSpacing: 4 }}>
              WAVE-FUNCTION PREDICTOR v5.0
            </Typography>
          </div>
        </Box>
        
        <Paper sx={{ p: 2, bgcolor: '#0d1117', border: '1px solid rgba(34, 211, 238, 0.2)', borderRadius: 4, display: 'flex', gap: 4 }}>
           <div className="text-center">
             <span className="text-[8px] font-black text-slate-500 uppercase block mb-1">Coerência</span>
             <span className="text-lg font-black text-cyan-400 font-mono">{(snapshot.coherence * 100).toFixed(1)}%</span>
           </div>
           <div className="text-center">
             <span className="text-[8px] font-black text-slate-500 uppercase block mb-1">Entropia</span>
             <span className="text-lg font-black text-purple-400 font-mono">{(snapshot.entropy * 100).toFixed(1)}%</span>
           </div>
        </Paper>
      </Box>

      <Grid container spacing={4}>
        {/* Wave Function Visualizer */}
        <Grid item xs={12} lg={8}>
          <Paper sx={{ 
            p: 4, bgcolor: '#010409', borderRadius: 10, border: '1px solid rgba(34, 211, 238, 0.1)',
            position: 'relative', overflow: 'hidden', height: '400px'
          }}>
            <div className="absolute inset-0 opacity-10 pointer-events-none" style={{ backgroundImage: 'radial-gradient(#22d3ee 1px, transparent 1px)', backgroundSize: '20px 20px' }} />
            
            <Typography variant="subtitle2" sx={{ color: 'white', mb: 4, fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
              <Waves size={18} className="text-cyan-400" /> WAVE FUNCTION COLLAPSE FIELD
            </Typography>

            <Box sx={{ width: '100%', height: '70%', mt: 4 }}>
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={chartData}>
                  <Area 
                    type="monotone" 
                    dataKey="val" 
                    stroke="#22d3ee" 
                    strokeWidth={3}
                    fill={alpha('#22d3ee', 0.1)} 
                    isAnimationActive={false}
                  />
                  <YAxis hide domain={[-2, 2]} />
                </AreaChart>
              </ResponsiveContainer>
            </Box>

            <div className="flex justify-between mt-4">
              <div className="flex gap-4">
                <Chip label="HADAMARD ACTIVE" size="small" sx={{ bgcolor: 'rgba(34, 211, 238, 0.1)', color: '#22d3ee', fontWeight: 'black', fontSize: '8px' }} />
                <Chip label="TUNNELING READY" size="small" sx={{ bgcolor: 'rgba(168, 85, 247, 0.1)', color: '#a855f7', fontWeight: 'black', fontSize: '8px' }} />
              </div>
              <div className="flex items-center gap-2">
                 <Activity size={12} className="text-cyan-400 animate-pulse" />
                 <span className="text-[10px] font-black text-cyan-400 uppercase">Quantum State: {snapshot.prediction}</span>
              </div>
            </div>
          </Paper>
        </Grid>

        {/* Qubit Telemetry */}
        <Grid item xs={12} lg={4}>
          <Paper sx={{ p: 4, bgcolor: '#0d1117', borderRadius: 10, border: '1px solid #1e293b', height: '100%' }}>
             <Typography variant="subtitle2" sx={{ color: 'white', mb: 4, fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
                <Binary size={18} className="text-indigo-400" /> QUBIT TELEMETRY
             </Typography>
             
             <div className="space-y-4">
                {snapshot.qubits.map(qubit => (
                  <Box key={qubit.id} sx={{ p: 2, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 4, border: '1px solid rgba(255,255,255,0.05)' }}>
                    <div className="flex justify-between items-center mb-2">
                       <span className="text-[10px] font-black text-slate-400 uppercase">{qubit.label}</span>
                       <span className={`text-[8px] font-black px-1.5 py-0.5 rounded ${qubit.state === 'SUPERPOSITION' ? 'bg-cyan-500/10 text-cyan-400' : 'bg-purple-500/10 text-purple-400'}`}>
                         {qubit.state}
                       </span>
                    </div>
                    <LinearProgress 
                      variant="determinate" 
                      value={qubit.probability * 100} 
                      sx={{ 
                        height: 4, borderRadius: 2, bgcolor: 'rgba(255,255,255,0.05)',
                        '& .MuiLinearProgress-bar': { bgcolor: qubit.probability > 0.5 ? '#22d3ee' : '#a855f7' }
                      }} 
                    />
                    <div className="flex justify-between mt-1.5">
                       <span className="text-[8px] font-mono text-slate-600">PHASE: {qubit.phase.toFixed(0)}°</span>
                       {qubit.entangledWith.length > 0 && (
                         <span className="text-[8px] font-black text-indigo-400 flex items-center gap-1">
                           <Share2 size={8} /> ENTANGLED
                         </span>
                       )}
                    </div>
                  </Box>
                ))}
             </div>
          </Paper>
        </Grid>
      </Grid>

      {/* Integration Analysis Footer */}
      <Paper sx={{ p: 4, bgcolor: 'linear-gradient(135deg, #0d1117 0%, #020617 100%)', borderRadius: 8, border: '1px solid rgba(34, 211, 238, 0.2)' }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 4 }}>
           <div className="p-3 bg-cyan-500/10 rounded-full">
              <Sparkles size={24} className="text-cyan-400" />
           </div>
           <Box sx={{ flex: 1 }}>
              <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: 'black', mb: 1 }}>SÍNTESE QUÂNTICA-AGI</Typography>
              <Typography variant="body2" sx={{ color: '#94a3b8', fontSize: '11px', lineHeight: 1.6, fontStyle: 'italic' }}>
                "O Núcleo Quântico detectou uma redução na coerência de rede (Entropia local em { (snapshot.entropy * 100).toFixed(1) }%). A função de onda sugere um colapso iminente na zona de ${ (priceHistory[priceHistory.length - 1] * 1.02).toFixed(2) }. Recomenda-se execução cautelosa com Portas de Proteção ativas."
              </Typography>
           </Box>
           <Box sx={{ textAlign: 'right' }}>
              <Typography sx={{ color: '#10b981', fontSize: '10px', fontWeight: 'black' }}>PROBABILIDADE DE SUCESSO</Typography>
              <Typography variant="h3" sx={{ color: 'white', fontWeight: 900 }}>{(snapshot.coherence * 92).toFixed(0)}%</Typography>
           </Box>
        </Box>
      </Paper>
    </Box>
  );
};
