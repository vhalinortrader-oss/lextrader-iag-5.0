
import React, { useState, useEffect } from 'react';
import { Box, Grid, Paper, Typography, Slider, Tooltip as MuiTooltip } from '@mui/material';
import { Settings, Info, Cpu, Zap } from 'lucide-react';
import { Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, ResponsiveContainer } from 'recharts';

export const NeuralPrecisionOptimizer: React.FC = () => {
  const [params, setParams] = useState({
    learningRate: 0.001,
    batchSize: 64,
    dropout: 0.3,
    entropy: 0.12
  });

  const radarData = [
    { subject: 'Acurácia', A: 94, fullMark: 100 },
    { subject: 'Latência', A: 85, fullMark: 100 },
    { subject: 'Eficiência', A: 78, fullMark: 100 },
    { subject: 'Resiliência', A: 92, fullMark: 100 },
    { subject: 'Estabilidade', A: 88, fullMark: 100 },
  ];

  return (
    <Box sx={{ p: 4, bgcolor: '#0a0a0a', borderRadius: 8, border: '1px solid #a855f730' }}>
      <div className="flex justify-between items-center mb-8">
        <div className="flex items-center gap-3">
          <Settings className="text-purple-500" size={24} />
          <Typography variant="h5" sx={{ fontWeight: 'black', color: 'white' }}>NEURAL PRECISION OPTIMIZER</Typography>
        </div>
      </div>

      <Grid container spacing={4}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 4, bgcolor: '#0f172a', borderRadius: 6, border: '1px solid #1e293b' }}>
            <Typography variant="subtitle2" sx={{ color: 'white', mb: 4, fontWeight: 'bold' }}>Tuning de Hiperparâmetros</Typography>
            
            <div className="space-y-8">
              <ParameterSlider label="Learning Rate" value={params.learningRate} min={0.0001} max={0.01} step={0.0001} onChange={(v) => setParams(p => ({...p, learningRate: v}))} />
              <ParameterSlider label="Batch Size" value={params.batchSize} min={16} max={256} step={16} onChange={(v) => setParams(p => ({...p, batchSize: v}))} />
              <ParameterSlider label="Dropout Rate" value={params.dropout} min={0.1} max={0.5} step={0.05} onChange={(v) => setParams(p => ({...p, dropout: v}))} />
              <ParameterSlider label="Entropy Target" value={params.entropy} min={0.05} max={0.25} step={0.01} onChange={(v) => setParams(p => ({...p, entropy: v}))} />
            </div>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 4, bgcolor: '#0f172a', borderRadius: 6, border: '1px solid #1e293b', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <Typography variant="subtitle2" sx={{ color: 'white', mb: 4, fontWeight: 'bold' }}>Assinatura de Performance Multi-Fatorial</Typography>
            <Box sx={{ width: '100%', height: 350, position: 'relative' }}>
              <ResponsiveContainer width="100%" height="100%" minWidth={0}>
                <RadarChart cx="50%" cy="50%" outerRadius="80%" data={radarData}>
                  <PolarGrid stroke="#1e293b" />
                  <PolarAngleAxis dataKey="subject" tick={{ fill: '#94a3b8', fontSize: 10 }} />
                  <PolarRadiusAxis angle={30} domain={[0, 100]} tick={false} axisLine={false} />
                  <Radar
                    name="System"
                    dataKey="A"
                    stroke="#a855f7"
                    fill="#a855f7"
                    fillOpacity={0.4}
                  />
                </RadarChart>
              </ResponsiveContainer>
            </Box>
            <div className="mt-4 flex gap-6">
              <div className="flex items-center gap-2 text-[10px] font-bold text-slate-400">
                <Cpu size={14} className="text-purple-400" /> CUDA CORE OK
              </div>
              <div className="flex items-center gap-2 text-[10px] font-bold text-slate-400">
                <Zap size={14} className="text-yellow-400" /> TENSOR READY
              </div>
            </div>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

const ParameterSlider: React.FC<{ label: string, value: number, min: number, max: number, step: number, onChange: (v: number) => void }> = ({ label, value, min, max, step, onChange }) => (
  <div className="space-y-2">
    <div className="flex justify-between items-center">
      <span className="text-[10px] font-black text-slate-400 uppercase tracking-tighter">{label}</span>
      <span className="text-[12px] font-mono text-purple-400 font-bold">{value}</span>
    </div>
    <Slider
      value={value}
      min={min}
      max={max}
      step={step}
      onChange={(_, v) => onChange(v as number)}
      sx={{ color: '#a855f7' }}
    />
  </div>
);
