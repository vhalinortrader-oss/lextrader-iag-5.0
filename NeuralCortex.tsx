
import React, { useState, useEffect } from 'react';
import { 
  Box, Typography, Paper, Grid, Button, LinearProgress, Chip, 
  alpha, Divider, Tooltip 
} from '@mui/material';
import { 
  Brain, Database, History, Sparkles, Zap, Binary, BookOpen, 
  RefreshCcw, Atom, TrendingUp, TrendingDown, Activity, Globe,
  ShieldCheck, Terminal, HardDrive, Cpu, DownloadCloud
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { NeuralMemoryEngram, STMEngram, StockData, IntelLibraryStatus } from '../types';
import { consolidateNeuralMemories } from '../services/gemini';
import { synapticSTM } from '../services/SynapticSTM';
import { yFinance } from '../services/YFinanceService';
import { externalLibBridge } from '../services/ExternalLibraryBridge';
/* Fix: Added ResponsiveContainer to the imported symbols from recharts to resolve compilation errors */
import { Radar, RadarChart, PolarGrid, PolarAngleAxis, Radar as RadarArea, ResponsiveContainer } from 'recharts';

export const NeuralCortex: React.FC = () => {
  const [engrams, setEngrams] = useState<NeuralMemoryEngram[]>([]);
  const [stmBuffer, setStmBuffer] = useState<STMEngram[]>([]);
  const [stocks, setStocks] = useState<StockData[]>([]);
  const [libs, setLibs] = useState<IntelLibraryStatus[]>([]);
  const [isConsolidating, setIsConsolidating] = useState(false);
  const [iqLevel, setIqLevel] = useState(145);

  useEffect(() => {
    const interval = setInterval(async () => {
      // Atualiza Buffer STM
      const recent = synapticSTM.getRecent();
      setStmBuffer(recent.slice(0, 10));

      // Atualiza YFinance
      const stockUpdates = await yFinance.fetchQuotes();
      setStocks(stockUpdates);

      // Atualiza Status das Bibliotecas (Pandas, CCXT, etc)
      const libUpdates = externalLibBridge.getLibraryStatus();
      setLibs(libUpdates);

      // Ingestão cruzada: Adiciona engrama se lib ativa
      const activeLib = libUpdates.find(l => l.active);
      if (activeLib) {
        synapticSTM.push({
          price: 98000 + Math.random() * 500,
          indicators: { rsi: 40 + Math.random() * 20, volatility: Math.random() * 0.05, volume: 1000 + Math.random() * 500 },
          significance: Math.random(),
          timestamp: Date.now(),
          source: activeLib.name
        });
      }
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const runConsolidation = async () => {
    setIsConsolidating(true);
    try {
      await consolidateNeuralMemories(engrams, []);
      setIqLevel(prev => prev + 1);
    } catch (err) {
      console.error("Erro na consolidação:", err);
    } finally {
      setIsConsolidating(false);
    }
  };

  return (
    <Box sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', borderBottom: '1px solid rgba(255,255,255,0.05)', pb: 4 }}>
        <Box>
          <Typography variant="h3" sx={{ fontWeight: 950, color: 'white', letterSpacing: -2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Brain size={42} className="text-indigo-500" /> NEURAL CORTEX <span className="text-indigo-600">v5.0</span>
          </Typography>
          <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', letterSpacing: 5, textTransform: 'uppercase' }}>
            SYNAPTIC CORE & AGI DATA INGESTION
          </Typography>
        </Box>
        <Box sx={{ textAlign: 'right' }}>
           <Typography variant="h2" sx={{ fontWeight: 'black', color: '#10b981', fontFamily: 'JetBrains Mono', lineHeight: 1 }}>
             {iqLevel}<span className="text-xs text-slate-500 ml-2">IQ</span>
           </Typography>
           <Typography variant="caption" sx={{ color: '#6366f1', fontWeight: 'bold' }}>COGNITIVE CAPACITY</Typography>
        </Box>
      </Box>

      <Grid container spacing={4}>
        {/* Lado Esquerdo: AGI DATA LIBRARIES (CCXT, Pandas, etc) */}
        <Grid item xs={12} lg={4}>
           <Paper sx={{ p: 4, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 8, height: '100%' }}>
              <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
                  <DownloadCloud size={18} className="text-cyan-400" /> INTEL BRIDGES
                </Typography>
                <Chip label="ONLINE" size="small" color="success" sx={{ height: 16, fontSize: '8px', fontWeight: 900 }} />
              </Box>
              
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
                {libs.map(lib => (
                  <Box key={lib.id} sx={{ 
                    p: 2, bgcolor: alpha(lib.active ? '#6366f1' : '#475569', 0.05), 
                    borderRadius: 3, border: `1px solid ${lib.active ? 'rgba(99, 102, 241, 0.2)' : 'rgba(255,255,255,0.05)'}`,
                    transition: 'all 0.3s'
                  }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                      <Typography sx={{ color: lib.active ? 'white' : '#475569', fontWeight: 900, fontSize: '11px' }}>{lib.name}</Typography>
                      <Typography sx={{ color: '#6366f1', fontSize: '9px', fontWeight: 'black' }}>{lib.category}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <Typography sx={{ color: '#475569', fontSize: '9px' }}>{lib.provider}</Typography>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <div className={`w-1.5 h-1.5 rounded-full ${lib.active ? 'bg-emerald-500 animate-pulse' : 'bg-red-500'}`} />
                        <span className="text-[9px] font-mono text-slate-500">{lib.throughput}</span>
                      </Box>
                    </Box>
                  </Box>
                ))}
              </Box>

              <Box sx={{ mt: 4, p: 2, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 4, border: '1px dashed #1e293b' }}>
                 <Typography sx={{ color: '#475569', fontSize: '10px', fontStyle: 'italic', lineHeight: 1.5 }}>
                   "Os drivers Quandl e CCXT estão enviando pacotes macro e de liquidez diretamente para o Arquicórtex para filtragem de ruído."
                 </Typography>
              </Box>
           </Paper>
        </Grid>

        {/* Centro: Synaptic STM Monitor com Fonte */}
        <Grid item xs={12} lg={4}>
          <Paper sx={{ p: 4, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 8, height: '650px', display: 'flex', flexDirection: 'column' }}>
            <Box sx={{ mb: 3 }}>
               <Typography variant="subtitle2" sx={{ color: 'white', mb: 2, fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
                <Activity size={18} className="text-indigo-400" /> SYNAPTIC STM FEED
              </Typography>
              <Typography variant="caption" sx={{ color: '#475569', display: 'block', mb: 2 }}>Multi-Source Input Buffer</Typography>
            </Box>
            
            <Box sx={{ flex: 1, overflowY: 'auto', pr: 1 }} className="scrollbar-hide">
              <AnimatePresence>
                {stmBuffer.map((e) => (
                  <motion.div key={e.id} initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }} layout>
                    <Paper sx={{ 
                      p: 2, mb: 1.5, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 3, 
                      border: `1px solid ${alpha('#6366f1', e.significance * 0.5)}`,
                      position: 'relative'
                    }}>
                      <div className="flex justify-between items-center mb-1">
                        <span className="text-[9px] font-mono text-slate-500">{new Date(e.timestamp).toLocaleTimeString()}</span>
                        <Chip label={e.source || 'INTERNAL'} size="small" sx={{ height: 12, fontSize: '6px', fontWeight: 900, bgcolor: 'indigo.900' }} />
                      </div>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <Typography sx={{ color: 'white', fontSize: '11px', fontWeight: '900' }}>${e.price.toFixed(2)}</Typography>
                        <Box sx={{ display: 'flex', gap: 1 }}>
                           <span className="text-[8px] font-black text-indigo-400">SIG: {(e.significance * 100).toFixed(0)}%</span>
                        </Box>
                      </Box>
                      <Box sx={{ position: 'absolute', left: 0, bottom: 0, height: '2px', width: `${e.significance * 100}%`, bgcolor: '#6366f1' }} />
                    </Paper>
                  </motion.div>
                ))}
              </AnimatePresence>
            </Box>
          </Paper>
        </Grid>

        {/* Lado Direito: Radar de Inteligência e YFinance */}
        <Grid item xs={12} lg={4}>
           <Paper sx={{ p: 4, bgcolor: '#020617', border: '1px solid #1e293b', borderRadius: 10, height: '100%', display: 'flex', flexDirection: 'column' }}>
              <Box sx={{ mb: 4 }}>
                <Typography variant="h6" sx={{ color: 'white', fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
                  <Sparkles size={20} className="text-yellow-400" /> INTEL CONVERGENCE
                </Typography>
              </Box>

              <Box sx={{ bgcolor: alpha('#6366f1', 0.03), p: 4, borderRadius: 6, border: '1px solid rgba(99, 102, 241, 0.1)', mb: 4 }}>
                {/* Fix: Added ResponsiveContainer to the recharts components to resolve compilation errors on lines 177 and 189 */}
                <ResponsiveContainer width="100%" height={250}>
                  <RadarChart data={[
                    { subject: 'CCXT Liquidity', A: 92 },
                    { subject: 'Alpha Vantage Tech', A: 85 },
                    { subject: 'Quandl Macro', A: 78 },
                    { subject: 'YFinance Sentiment', A: 65 },
                    { subject: 'Internal Logic', A: 98 },
                  ]}>
                    <PolarGrid stroke="rgba(255,255,255,0.1)" />
                    <PolarAngleAxis dataKey="subject" tick={{ fill: '#94a3b8', fontSize: 10 }} />
                    <RadarArea name="AGI" dataKey="A" stroke="#6366f1" fill="#6366f1" fillOpacity={0.5} />
                  </RadarChart>
                </ResponsiveContainer>
              </Box>

              <Typography variant="subtitle2" sx={{ color: 'white', mb: 2, fontWeight: 900, fontSize: '11px' }}>GLOBAL ASSETS SYNC</Typography>
              <Box sx={{ flex: 1, overflowY: 'auto' }} className="scrollbar-hide">
                <Grid container spacing={2}>
                  {stocks.slice(0, 6).map(stock => (
                    <Grid item xs={6} key={stock.symbol}>
                      <Box sx={{ p: 1.5, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 2, border: '1px solid rgba(255,255,255,0.05)' }}>
                        <Typography sx={{ color: 'white', fontWeight: 900, fontSize: '10px' }}>{stock.symbol}</Typography>
                        <Typography sx={{ color: stock.change >= 0 ? '#10b981' : '#ef4444', fontWeight: 'bold', fontSize: '10px', fontFamily: 'monospace' }}>
                          {stock.change >= 0 ? '+' : ''}{stock.change.toFixed(2)}%
                        </Typography>
                      </Box>
                    </Grid>
                  ))}
                </Grid>
              </Box>

              <Button 
                fullWidth 
                variant="contained" 
                onClick={runConsolidation}
                disabled={isConsolidating}
                sx={{ mt: 3, bgcolor: '#6366f1', borderRadius: 4, fontWeight: 'black', py: 1.5 }}
              >
                {isConsolidating ? 'CONSOLIDATING...' : 'CONSOLIDATE KERNEL'}
              </Button>
           </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};
