
import React, { useState, useEffect } from 'react';
import { Box, Grid, Paper, Typography, LinearProgress, alpha } from '@mui/material';
import { Brain, Zap, Activity, Radio, Sparkles } from 'lucide-react';
import { AreaChart, Area, ResponsiveContainer, XAxis, YAxis, Tooltip } from 'recharts';
import { brainOrchestrator } from '../services/AdvancedBrainService';

export const NeuralBrainCore: React.FC = () => {
  const [metrics, setMetrics] = useState(brainOrchestrator.getPerformanceMetrics());
  const [history, setHistory] = useState<any[]>([]);

  useEffect(() => {
    const handle = (data: any) => {
      setMetrics(data);
      setHistory(prev => [...prev.slice(-19), { time: new Date().toLocaleTimeString(), val: data.activeNeurons }]);
    };
    brainOrchestrator.on('cycle_complete', handle);
    
    const interval = setInterval(() => {
        const neurons = Array.from(brainOrchestrator.neurons.keys());
        const stimuli: Record<string, number> = {};
        for(let i=0; i<5; i++) stimuli[neurons[Math.floor(Math.random()*neurons.length)]] = Math.random();
        brainOrchestrator.processStimulusBatch(stimuli);
    }, 2000);

    return () => {
        brainOrchestrator.off('cycle_complete', handle);
        clearInterval(interval);
    };
  }, []);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
      <header className="flex justify-between items-center bg-[#0d1117] p-8 rounded-[3rem] border border-indigo-500/20 shadow-2xl">
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 3 }}>
          <div className="p-3 bg-indigo-500/10 rounded-2xl border border-indigo-500/20">
             <Brain size={32} className="text-indigo-400" />
          </div>
          <div>
            <Typography variant="h4" sx={{ fontWeight: 950, color: 'white', letterSpacing: -1 }}>
              IAG <span className="text-indigo-500">BRAIN CORE</span> <span className="text-xs text-slate-500">v4.5</span>
            </Typography>
            <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', letterSpacing: 4 }}>
                ARTIFICIAL CEREBRAL NETWORK OPTIMIZED
            </Typography>
          </div>
        </Box>
      </header>

      <Grid container spacing={4}>
        <Grid item xs={12} md={3}>
           <div className="space-y-4">
              <Paper sx={{ p: 3, bgcolor: '#010409', border: '1px solid #1e293b', borderRadius: 4 }}>
                 <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Activity size={14} className="text-indigo-400" /> NEURÔNIOS ATIVOS
                 </Typography>
                 <Typography variant="h4" sx={{ color: 'white', fontWeight: 900 }}>{metrics.activeNeurons}</Typography>
              </Paper>
              <Paper sx={{ p: 3, bgcolor: '#010409', border: '1px solid #1e293b', borderRadius: 4 }}>
                 <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Zap size={14} className="text-yellow-500" /> ENERGIA DISPONÍVEL
                 </Typography>
                 <Typography variant="h4" sx={{ color: 'white', fontWeight: 900 }}>{metrics.systemEnergy.toFixed(1)}</Typography>
                 <LinearProgress variant="determinate" value={metrics.systemEnergy / 10} sx={{ mt: 1, height: 4, borderRadius: 2, bgcolor: 'white/5', '& .MuiLinearProgress-bar': { bgcolor: '#f59e0b' } }} />
              </Paper>
           </div>
        </Grid>

        <Grid item xs={12} md={9}>
           <Paper sx={{ p: 4, bgcolor: '#010409', border: '1px solid #1e293b', borderRadius: 10, height: '400px' }}>
              <Typography variant="subtitle2" sx={{ color: 'white', mb: 4, fontWeight: 900, display: 'flex', alignItems: 'center', gap: 2 }}>
                 <Radio size={18} className="text-indigo-400" /> ATIVAÇÃO NEURAL EM TEMPO REAL
              </Typography>
              <Box sx={{ width: '100%', height: '80%' }}>
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={history}>
                    <defs>
                      <linearGradient id="brainGrad" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#6366f1" stopOpacity={0.3}/>
                        <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <XAxis dataKey="time" hide />
                    <YAxis hide domain={['auto', 'auto']} />
                    <Tooltip contentStyle={{ backgroundColor: '#0d1117', border: 'none' }} />
                    <Area type="monotone" dataKey="val" stroke="#6366f1" fill="url(#brainGrad)" strokeWidth={4} isAnimationActive={false} />
                  </AreaChart>
                </ResponsiveContainer>
              </Box>
           </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};
