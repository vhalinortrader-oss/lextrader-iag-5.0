
import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, Grid, Button, Chip, LinearProgress, alpha, IconButton, Tooltip } from '@mui/material';
import { Share2, Zap, Radio, Settings, Trash2, Power, Code, ExternalLink, RefreshCcw, Activity } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { tvBotService } from '../services/TradingViewBotService';
import { TVBot } from '../types';

export const TradingViewBotHub: React.FC = () => {
  const [bots, setBots] = useState<TVBot[]>([]);
  const [isSyncing, setIsSyncing] = useState(false);

  const refreshBots = () => {
    setIsSyncing(true);
    setTimeout(() => {
      setBots(tvBotService.getImportedBots());
      setIsSyncing(false);
    }, 1000);
  };

  useEffect(() => {
    refreshBots();
  }, []);

  const handleTestWebhook = (id: string) => {
    const signal = tvBotService.simulateWebhookSignal(id);
    if (signal) {
      setBots(tvBotService.getImportedBots());
    }
  };

  return (
    <Box sx={{ p: 0, display: 'flex', flexDirection: 'column', gap: 4 }}>
      <header className="flex justify-between items-end border-b border-white/10 pb-6">
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 950, color: 'white', letterSpacing: -1, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Share2 size={32} className="text-blue-500" /> TV-WEBHOOK <span className="text-blue-600">BRIDGE</span>
          </Typography>
          <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', letterSpacing: 4, textTransform: 'uppercase' }}>
            IMPORT & SYNC PINE SCRIPT BOTS
          </Typography>
        </Box>
        <div className="flex gap-3">
          <Button 
            variant="outlined" 
            onClick={refreshBots}
            disabled={isSyncing}
            startIcon={<RefreshCcw size={16} className={isSyncing ? "animate-spin" : ""} />}
            sx={{ borderColor: 'rgba(99, 102, 241, 0.2)', color: '#818cf8', fontWeight: 'black', borderRadius: 3 }}
          >
            REFRESH BRIDGE
          </Button>
          <Button 
            variant="contained" 
            startIcon={<Code size={16} />}
            sx={{ bgcolor: '#2563eb', fontWeight: 'black', borderRadius: 3, px: 4 }}
          >
            NEW HOOK
          </Button>
        </div>
      </header>

      <Grid container spacing={3}>
        <AnimatePresence>
          {bots.map((bot) => (
            <Grid item xs={12} md={6} lg={4} key={bot.id}>
              <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, scale: 0.95 }}>
                <Paper sx={{ 
                  p: 3, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 6,
                  position: 'relative', overflow: 'hidden',
                  transition: 'all 0.3s',
                  '&:hover': { borderColor: '#2563eb', transform: 'translateY(-5px)' }
                }}>
                  <div className="absolute top-0 right-0 p-3 opacity-[0.03]">
                     <Radio size={80} />
                  </div>

                  <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={3}>
                    <Box>
                      <Typography variant="h6" sx={{ color: 'white', fontWeight: 900, fontSize: '15px' }}>{bot.name}</Typography>
                      <Typography variant="caption" sx={{ color: '#2563eb', fontWeight: 'black', fontFamily: 'monospace' }}>{bot.pineScriptId}</Typography>
                    </Box>
                    <Chip 
                      label={bot.status} 
                      size="small" 
                      sx={{ 
                        height: 18, fontSize: '7px', fontWeight: 'black',
                        bgcolor: bot.status === 'SYNCHRONIZED' ? 'rgba(16, 185, 129, 0.1)' : 'rgba(239, 68, 68, 0.1)',
                        color: bot.status === 'SYNCHRONIZED' ? '#10b981' : '#ef4444'
                      }} 
                    />
                  </Box>

                  <div className="grid grid-cols-2 gap-4 mb-4">
                    <Box sx={{ p: 1.5, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 2, border: '1px solid rgba(255,255,255,0.05)' }}>
                       <span className="text-[8px] text-slate-500 font-black uppercase block">Target Pair</span>
                       <span className="text-xs text-white font-bold">{bot.pair}</span>
                    </Box>
                    <Box sx={{ p: 1.5, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 2, border: '1px solid rgba(255,255,255,0.05)' }}>
                       <span className="text-[8px] text-slate-500 font-black uppercase block">Bot Accuracy</span>
                       <span className="text-xs text-emerald-400 font-bold">{(bot.accuracy * 100).toFixed(1)}%</span>
                    </Box>
                  </div>

                  <Box sx={{ mb: 4 }}>
                    <div className="flex justify-between items-center mb-1">
                      <span className="text-[9px] font-black text-slate-500 uppercase">Last Signal Trace</span>
                      <Chip 
                        label={bot.lastSignal} 
                        size="small" 
                        sx={{ height: 16, fontSize: '8px', fontWeight: 900, bgcolor: bot.lastSignal === 'BUY' ? '#10b981' : bot.lastSignal === 'SELL' ? '#ef4444' : '#475569', color: 'white' }} 
                      />
                    </div>
                  </Box>

                  <Box display="flex" gap={1}>
                    <Button 
                      fullWidth 
                      variant="contained" 
                      size="small"
                      onClick={() => handleTestWebhook(bot.id)}
                      startIcon={<Zap size={12}/>}
                      sx={{ bgcolor: '#2563eb', fontWeight: 'black', fontSize: '9px' }}
                    >
                      TEST HOOK
                    </Button>
                    <Tooltip title="Bot Settings">
                      <IconButton size="small" sx={{ bgcolor: 'rgba(255,255,255,0.05)', color: '#475569' }}><Settings size={14}/></IconButton>
                    </Tooltip>
                    <Tooltip title="View Pine Code">
                      <IconButton size="small" sx={{ bgcolor: 'rgba(255,255,255,0.05)', color: '#475569' }}><Code size={14}/></IconButton>
                    </Tooltip>
                    <Tooltip title="Remove Bot">
                      <IconButton size="small" sx={{ bgcolor: 'rgba(255,255,255,0.05)', color: '#ef444430' }}><Trash2 size={14} className="text-red-400" /></IconButton>
                    </Tooltip>
                  </Box>
                </Paper>
              </motion.div>
            </Grid>
          ))}
        </AnimatePresence>

        {/* Empty State / Add New Card */}
        <Grid item xs={12} md={6} lg={4}>
           <Paper sx={{ 
              p: 3, height: '100%', minHeight: '220px', bgcolor: 'transparent', 
              border: '2px dashed rgba(37, 99, 235, 0.2)', borderRadius: 6,
              display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
              cursor: 'pointer', transition: 'all 0.3s',
              '&:hover': { bgcolor: alpha('#2563eb', 0.05), borderColor: '#2563eb' }
           }}>
              <Box sx={{ p: 2, bgcolor: alpha('#2563eb', 0.1), borderRadius: '50%', mb: 2 }}>
                <Share2 size={32} className="text-blue-500" />
              </Box>
              <Typography sx={{ color: 'white', fontWeight: 900, fontSize: '13px' }}>IMPORT TRADINGVIEW BOT</Typography>
              <Typography sx={{ color: '#475569', fontSize: '10px', mt: 1 }}>Configure a Webhook URL no TradingView</Typography>
           </Paper>
        </Grid>
      </Grid>

      <Paper sx={{ p: 4, bgcolor: alpha('#2563eb', 0.03), border: '1px solid rgba(37, 99, 235, 0.2)', borderRadius: 6 }}>
        <Box display="flex" gap={3} alignItems="center">
           <div className="p-3 bg-blue-500/20 rounded-full">
              <Activity size={24} className="text-blue-400" />
           </div>
           <Box>
              <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: 900 }}>BRIDGE STATUS: OPERATIONAL</Typography>
              <Typography variant="body2" sx={{ color: '#94a3b8', fontSize: '11px', mt: 0.5 }}>
                A ponte LEXTRADER-TV está escutando em <code className="text-blue-400 font-mono">https://api.lextrader.com/v4/webhook/pine</code>. Sinais recebidos são auditados pela Camada 04 antes de qualquer ação.
              </Typography>
           </Box>
        </Box>
      </Paper>
    </Box>
  );
};
