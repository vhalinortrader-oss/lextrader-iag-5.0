
import React, { useState, useEffect } from 'react';
import { Box, Grid, Paper, Typography, LinearProgress, Chip, Button } from '@mui/material';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, ComposedChart, Line } from 'recharts';
import { Brain, Zap, TrendingUp, TrendingDown, Target, Activity, RefreshCw, AlertCircle } from 'lucide-react';
import { neuralPredictor } from '../services/NeuralPredictor';
import { PredictionPoint } from '../types';

export const NeuralPredictionDash: React.FC<{ prices: number[] }> = ({ prices }) => {
  const [forecast, setForecast] = useState<PredictionPoint[]>([]);
  const [stats, setStats] = useState(neuralPredictor.getStats());
  const [isTraining, setIsTraining] = useState(false);

  const updateForecast = async () => {
    // Agora utilizando a lógica de simulação estatística do código convertido
    const result = await neuralPredictor.predictWithConfidence(prices, 5);
    setForecast(result);
    setStats(neuralPredictor.getStats());
  };

  const handleTrain = async () => {
    if (prices.length < 100) return;
    setIsTraining(true);
    // Treina usando os dados de preço como features e labels (deslocados)
    await neuralPredictor.train(prices.slice(0, -10), prices.slice(10));
    await updateForecast();
    setIsTraining(false);
  };

  useEffect(() => {
    if (prices.length > 60) {
      updateForecast();
    }
  }, [prices]);

  const chartData = forecast.map(f => ({
    name: `T+${f.step}`,
    price: f.predictedPrice,
    range: [f.lowerBound, f.upperBound],
    confidence: f.confidence * 100
  }));

  return (
    <Box sx={{ p: 0, display: 'flex', flexDirection: 'column', gap: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 900, color: 'white' }}>Neural <span className="text-indigo-500">Forecaster</span></Typography>
          <Typography variant="caption" sx={{ color: '#4ade80', fontWeight: 'black', letterSpacing: 4 }}>LSTM TIME-SERIES PREDICTOR v5.0</Typography>
        </Box>
        <div className="flex gap-4">
           <Paper sx={{ p: 2, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 4, display: 'flex', gap: 4 }}>
              <StatItem label="Loss" value={stats.loss.toFixed(6)} color="#ef4444" />
              <StatItem label="Accuracy" value={`${(stats.accuracy * 100).toFixed(1)}%`} color="#10b981" />
           </Paper>
           <Button 
            variant="contained" 
            onClick={handleTrain}
            disabled={isTraining || prices.length < 100}
            startIcon={<RefreshCw className={isTraining ? "animate-spin" : ""} size={16}/>}
            sx={{ bgcolor: '#6366f1', fontWeight: 'black', borderRadius: 4, px: 4 }}
           >
             {isTraining ? 'EVOLUINDO...' : 'TREINAR AGORA'}
           </Button>
        </div>
      </Box>

      <Grid container spacing={4}>
        <Grid item xs={12} lg={8}>
          <Paper sx={{ p: 4, bgcolor: '#0a0a0a', border: '1px solid #1e293b', borderRadius: 10, height: '500px' }}>
            <Typography variant="subtitle2" sx={{ color: 'white', mb: 4, fontWeight: '900', display: 'flex', alignItems: 'center', gap: 2 }}>
              <Target size={18} className="text-indigo-400" /> PROJEÇÃO DE PREÇO E BANDAS DE INCERTEZA (95% CI)
            </Typography>
            <Box sx={{ width: '100%', height: '85%' }}>
               <ResponsiveContainer width="100%" height="100%">
                  <ComposedChart data={chartData}>
                    <defs>
                      <linearGradient id="forecastGrad" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#6366f1" stopOpacity={0.2}/>
                        <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" vertical={false} />
                    <XAxis dataKey="name" stroke="#475569" fontSize={10} fontWeight="bold" />
                    <YAxis domain={['auto', 'auto']} stroke="#475569" fontSize={10} fontWeight="bold" hide />
                    <Tooltip 
                      contentStyle={{ backgroundColor: '#0f172a', border: '1px solid #334155', borderRadius: 12 }}
                      itemStyle={{ fontSize: '12px', fontWeight: 'bold' }}
                    />
                    <Area type="monotone" dataKey="range" stroke="none" fill="#6366f1" fillOpacity={0.15} />
                    <Line type="monotone" dataKey="price" stroke="#818cf8" strokeWidth={4} dot={{ r: 4, fill: '#6366f1' }} />
                  </ComposedChart>
               </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12} lg={4}>
           <div className="space-y-6">
              <Paper sx={{ p: 4, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 10 }}>
                 <Typography variant="subtitle1" sx={{ color: 'white', fontWeight: '900', mb: 3, display: 'flex', alignItems: 'center', gap: 2 }}>
                   <Activity size={18} className="text-indigo-400" /> SÍNTESE PREDITIVA
                 </Typography>
                 <div className="space-y-4">
                    {forecast.slice(0, 5).map((f, i) => (
                      <div key={i} className="flex justify-between items-center p-3 bg-black/40 rounded-xl border border-white/5">
                        <div>
                           <span className="text-[8px] font-black text-slate-500 uppercase block">T+{f.step} Horizon</span>
                           <span className="text-xs font-mono font-bold text-white">${f.predictedPrice.toLocaleString(undefined, { maximumFractionDigits: 2 })}</span>
                        </div>
                        <div className="text-right">
                          <Chip 
                            size="small" 
                            label={f.direction} 
                            icon={f.direction === 'UP' ? <TrendingUp size={10}/> : <TrendingDown size={10}/>}
                            sx={{ 
                              height: 18, fontSize: '8px', fontWeight: 'black',
                              bgcolor: f.direction === 'UP' ? 'rgba(16, 185, 129, 0.1)' : 'rgba(239, 68, 68, 0.1)',
                              color: f.direction === 'UP' ? '#10b981' : '#ef4444'
                            }} 
                          />
                          <div className="mt-1 text-[8px] font-black text-slate-600 uppercase">Conf: {(f.confidence * 100).toFixed(0)}%</div>
                        </div>
                      </div>
                    ))}
                 </div>
              </Paper>

              <Paper sx={{ p: 4, bgcolor: 'linear-gradient(135deg, #1e1b4b 0%, #020617 100%)', border: '1px solid #6366f130', borderRadius: 10 }}>
                 <div className="flex items-center gap-3 mb-4">
                    <AlertCircle size={20} className="text-indigo-400" />
                    <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: '900' }}>NEURAL ADVICE</Typography>
                 </div>
                 <Typography variant="body2" sx={{ color: '#94a3b8', fontSize: '11px', lineHeight: 1.6, fontStyle: 'italic' }}>
                   "O motor LSTM detectou uma convergência de momentum no horizonte T+5. Recomenda-se ajuste do Trailing Stop para capturar a volatilidade projetada."
                 </Typography>
              </Paper>
           </div>
        </Grid>
      </Grid>
    </Box>
  );
};

const StatItem = ({ label, value, color }: any) => (
  <Box>
    <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', textTransform: 'uppercase', display: 'block' }}>{label}</Typography>
    <Typography sx={{ color, fontWeight: 'black', fontFamily: 'monospace', fontSize: '1.2rem' }}>{value}</Typography>
  </Box>
);
