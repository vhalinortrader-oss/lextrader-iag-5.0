
import React, { useState, useEffect } from 'react';
import { Box, Grid, Paper, Typography, Button, LinearProgress, Chip, alpha } from '@mui/material';
import { 
  PieChart, ShieldAlert, Zap, Globe, Share2, Binary, 
  TrendingUp, Activity, Lock, RefreshCw, BarChart2, ShieldCheck,
  Atom, Network, Cpu
} from 'lucide-react';
import { quantumEngine } from '../services/QuantumNeuralEngine';

export const QuantumStrategyLab: React.FC = () => {
  const [portfolio, setPortfolio] = useState<any>(null);
  const [risk, setRisk] = useState<any>(null);

  useEffect(() => {
    const run = async () => {
      const p = await quantumEngine.optimizePortfolio(['BTC', 'ETH', 'SOL', 'GOLD']);
      const r = await quantumEngine.calculateQuantumVaR(100000);
      setPortfolio(p);
      setRisk(r);
    };
    run();
  }, []);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
      <header className="flex justify-between items-center bg-[#0d1117] p-8 rounded-[3rem] border border-cyan-500/20 shadow-2xl">
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 3 }}>
          <div className="p-3 bg-cyan-500/10 rounded-2xl border border-cyan-500/20">
             <Binary size={32} className="text-cyan-400" />
          </div>
          <div>
            <Typography variant="h4" sx={{ fontWeight: 950, color: 'white', letterSpacing: -1 }}>
              QUANTUM <span className="text-cyan-400">STRATEGY LAB</span>
            </Typography>
            <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', letterSpacing: 4 }}>
              6 PILLARS OF QUANTUM FINANCE SUPREMACY
            </Typography>
          </div>
        </Box>
      </header>

      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <PillarCard title="1. Otimização de Portfólio" icon={<PieChart className="text-cyan-400" />} subtitle="QAOA Combinatorial Efficiency">
            {portfolio?.assets.map((a: any) => (
              <Box key={a.symbol} sx={{ mb: 2 }}>
                <div className="flex justify-between text-[10px] font-black text-slate-400 uppercase mb-1">
                  <span>{a.symbol}</span>
                  <span>{a.allocation.toFixed(1)}%</span>
                </div>
                <LinearProgress variant="determinate" value={a.allocation} sx={{ height: 4, borderRadius: 2, bgcolor: 'white/5', '& .MuiLinearProgress-bar': { bgcolor: '#22d3ee' } }} />
              </Box>
            ))}
          </PillarCard>
        </Grid>

        <Grid item xs={12} md={4}>
          <PillarCard title="2. Análise Preditiva" icon={<TrendingUp className="text-emerald-400" />} subtitle="Non-linear Wave Time-Series">
             <div className="space-y-3">
                <PredictItem label="Mudança de Regime" value={14} color="#f59e0b" />
                <PredictItem label="Previsão Volatilidade" value={72} color="#10b981" />
                <PredictItem label="Detecção Fractal" value={91} color="#6366f1" />
             </div>
          </PillarCard>
        </Grid>

        <Grid item xs={12} md={4}>
          <PillarCard title="3. Crypto & Blockchain" icon={<Network className="text-indigo-400" />} subtitle="Post-Quantum Cryptography Audit">
             <Box sx={{ p: 2, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 3, border: '1px solid rgba(255,255,255,0.05)' }}>
                <Typography sx={{ color: 'white', fontSize: '11px', fontWeight: 900, mb: 1 }}>REDES PROTEGIDAS</Typography>
                <div className="flex flex-wrap gap-1">
                  {['BTC-PQC', 'ETH-NIST', 'SOL-CRYSTALS'].map(n => <Chip key={n} label={n} size="small" sx={{ height: 18, fontSize: '8px', fontWeight: 'black', bgcolor: '#1e1b4b', color: '#818cf8' }} />)}
                </div>
             </Box>
          </PillarCard>
        </Grid>

        <Grid item xs={12} md={4}>
          <PillarCard title="4. Algoritmos de Trading" icon={<Cpu className="text-rose-400" />} subtitle="Quantum Machine Learning (QML)">
             <div className="p-3 bg-rose-500/5 rounded-xl border border-rose-500/10">
                <Typography sx={{ color: '#ef4444', fontSize: '10px', fontWeight: 'black', mb: 1 }}>ARBITRAGEM ATÔMICA</Typography>
                <Typography sx={{ color: '#94a3b8', fontSize: '11px' }}>Executando varredura multi-exchange em superposição (32k caminhos/sec).</Typography>
             </div>
          </PillarCard>
        </Grid>

        <Grid item xs={12} md={4}>
          <PillarCard title="5. Gestão de Risco" icon={<ShieldAlert className="text-yellow-400" />} subtitle="Quantum Monte Carlo VaR">
             <div className="text-center py-2">
                <Typography variant="h3" sx={{ color: 'white', fontWeight: 900, fontFamily: 'monospace' }}>${risk?.var95.toLocaleString()}</Typography>
                <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black' }}>VALUE-AT-RISK (95% CONFIDENCE)</Typography>
             </div>
          </PillarCard>
        </Grid>

        <Grid item xs={12} md={4}>
          <PillarCard title="6. Mercado Forex" icon={<Globe className="text-blue-400" />} subtitle="Cross-border Capital Flow">
             <div className="space-y-2">
                {['EUR/USD', 'GBP/USD', 'USD/JPY'].map(p => (
                  <div key={p} className="flex justify-between items-center p-1.5 hover:bg-white/5 rounded-lg">
                    <span className="text-xs font-bold text-white">{p}</span>
                    <span className="text-[10px] text-emerald-400 font-mono">+0.12%</span>
                  </div>
                ))}
             </div>
          </PillarCard>
        </Grid>
      </Grid>
    </Box>
  );
};

const PillarCard = ({ title, icon, subtitle, children }: any) => (
  <Paper sx={{ p: 4, bgcolor: '#010409', border: '1px solid rgba(255,255,255,0.05)', borderRadius: 8, height: '100%' }}>
    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 1 }}>
      {icon}
      <Typography variant="subtitle1" sx={{ color: 'white', fontWeight: 900 }}>{title}</Typography>
    </Box>
    <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'bold', display: 'block', mb: 3 }}>{subtitle}</Typography>
    {children}
  </Paper>
);

const PredictItem = ({ label, value, color }: any) => (
  <div>
    <div className="flex justify-between text-[9px] font-black text-slate-500 uppercase mb-1">
      <span>{label}</span>
      <span style={{ color }}>{value}%</span>
    </div>
    <LinearProgress variant="determinate" value={value} sx={{ height: 4, borderRadius: 2, bgcolor: 'white/5', '& .MuiLinearProgress-bar': { bgcolor: color } }} />
  </div>
);
