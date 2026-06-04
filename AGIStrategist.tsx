
import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, Grid, Chip, Button, Switch, LinearProgress } from '@mui/material';
import { ShieldCheck, Activity, Lock, Play, Pause, Zap, ListChecks, Radio, Terminal } from 'lucide-react';
import { motion as m, AnimatePresence } from 'framer-motion';
import { getAutonomousDirective } from '../services/gemini';
import { AccountType, TradingDirective, SecurityFund, TaskPriority, LogEntry } from '../types';
import { autonomousModuleService } from '../services/AutonomousModuleService';

const motion = m as any;

export const AGIStrategist: React.FC<{ nexusSnapshot: any }> = ({ nexusSnapshot }) => {
  const [accountType, setAccountType] = useState<AccountType>(AccountType.DEMO);
  const [isAutoPilot, setIsAutoPilot] = useState(false);
  const [securityFund, setSecurityFund] = useState<SecurityFund>({
    totalReserved: 12450.32,
    dailyGains: 842.15,
    lastTransfer: new Date().toISOString(),
    status: 'PROTECTED',
    reserveProgress: 85
  });
  const [activeDirective, setActiveDirective] = useState<TradingDirective | null>(null);
  const [isGenerating, setIsGenerating] = useState(false);
  const [executionLogs, setExecutionLogs] = useState<LogEntry[]>([]);

  const addLog = (msg: string) => {
    setExecutionLogs(prev => [{ timestamp: new Date().toLocaleTimeString(), message: msg }, ...prev.slice(0, 19)]);
  };

  const triggerAutonomousScan = async () => {
    setIsGenerating(true);
    addLog("🌀 AGI processando análise multidimensional...");
    
    const directive = await getAutonomousDirective("BTC/USDT", 98500, nexusSnapshot);
    if (directive) {
      setActiveDirective(directive);
      autonomousModuleService.addTask(directive);
      addLog(`DIRETRIZ GERADA: ${directive.side} @ $${directive.entryPrice.toLocaleString()}`);
      
      if (isAutoPilot) {
        addLog(`PAS: Executando Ordem em conta ${accountType}`);
        console.log(`PAS: Executando Ordem prioridade ${directive.priority} em conta ${accountType}`, directive);
      }
    }
    setIsGenerating(false);
  };

  return (
    <Box sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 4, height: '100%', bgcolor: '#020617', overflowY: 'auto' }}>
      <header className="flex justify-between items-end border-b border-white/10 pb-6">
        <div>
          <Typography variant="h4" sx={{ fontWeight: 900, color: 'white', letterSpacing: -1 }}>
            AGI <span className="text-indigo-500 font-black">OMNI-STRATEGIC</span>
          </Typography>
          <Typography variant="caption" sx={{ color: '#6366f1', fontWeight: 'black', letterSpacing: 4, display: 'flex', alignItems: 'center', gap: 1 }}>
            <span className="w-2 h-2 bg-indigo-500 rounded-full animate-pulse" />
            PROTOCOLO DE AUTOMAÇÃO SOBERANA (PAS)
          </Typography>
        </div>
        
        <div className="flex gap-4">
           <Paper sx={{ p: 1.5, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 4, display: 'flex', gap: 2 }}>
              <Button 
                onClick={() => setAccountType(AccountType.DEMO)}
                sx={{ 
                  px: 3, borderRadius: 2, fontSize: '10px', fontWeight: 'black',
                  bgcolor: accountType === AccountType.DEMO ? 'rgba(99, 102, 241, 0.1)' : 'transparent',
                  color: accountType === AccountType.DEMO ? '#818cf8' : '#475569'
                }}
              >DEMO</Button>
              <Button 
                onClick={() => setAccountType(AccountType.REAL)}
                sx={{ 
                  px: 3, borderRadius: 2, fontSize: '10px', fontWeight: 'black',
                  bgcolor: accountType === AccountType.REAL ? 'rgba(239, 68, 68, 0.1)' : 'transparent',
                  color: accountType === AccountType.REAL ? '#ef4444' : '#475569'
                }}
              >REAL</Button>
           </Paper>
        </div>
      </header>

      <Grid container spacing={4}>
        <Grid item xs={12} lg={4}>
          <div className="space-y-6">
            <Paper sx={{ p: 4, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 8 }}>
              <div className="flex justify-between items-center mb-6">
                <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
                  <ShieldCheck size={18} className="text-indigo-400" /> AUTOPILOT STATUS
                </Typography>
                <Switch checked={isAutoPilot} onChange={(e) => setIsAutoPilot(e.target.checked)} color="primary" />
              </div>
              
              <div className="space-y-4 mb-6">
                <StatusItem label="Sync Engine" status="CONNECTED" color="#10b981" />
                <StatusItem label="Latency" status="42ms" color="#6366f1" />
                <StatusItem label="Mode" status={isAutoPilot ? "FULL AUTO" : "PILOTED"} color={isAutoPilot ? "#10b981" : "#f59e0b"} />
              </div>

              <Button 
                fullWidth 
                variant="contained" 
                onClick={triggerAutonomousScan}
                disabled={isGenerating}
                sx={{ py: 2, borderRadius: 3, bgcolor: '#6366f1', fontWeight: 'black' }}
                startIcon={isGenerating ? <Activity className="animate-spin" size={16}/> : (isAutoPilot ? <Pause size={16}/> : <Play size={16}/>)}
              >
                {isGenerating ? 'ANALYZING...' : (isAutoPilot ? 'PAUSE AUTOMATION' : 'START AGI CYCLE')}
              </Button>
            </Paper>

            <Paper sx={{ p: 4, bgcolor: 'linear-gradient(135deg, #111827 0%, #020617 100%)', border: '1px solid #10b98130', borderRadius: 8 }}>
               <div className="flex justify-between items-start mb-4">
                 <div>
                   <Typography variant="caption" sx={{ color: '#10b981', fontWeight: 'black', tracking: 2 }}>SECURITY FUND</Typography>
                   <Typography variant="h4" sx={{ color: 'white', fontWeight: 'black', mt: 1 }}>${securityFund.totalReserved.toLocaleString()}</Typography>
                 </div>
                 <Lock size={24} className="text-emerald-500" />
               </div>
               <LinearProgress variant="determinate" value={securityFund.reserveProgress} sx={{ height: 4, borderRadius: 2, bgcolor: '#1e293b', '& .MuiLinearProgress-bar': { bgcolor: '#10b981' } }} />
               <div className="flex justify-between mt-3">
                 <span className="text-[10px] font-bold text-slate-500 uppercase">Daily Allocation</span>
                 <span className="text-[10px] font-black text-emerald-400">+${securityFund.dailyGains}</span>
               </div>
            </Paper>
          </div>
        </Grid>

        <Grid item xs={12} lg={8}>
          <Paper sx={{ p: 4, bgcolor: '#020617', border: '1px solid #1e293b', borderRadius: 8, minHeight: '500px', display: 'flex', flexDirection: 'column' }}>
            <div className="flex items-center justify-between mb-8 border-b border-white/5 pb-4">
              <Typography variant="h6" sx={{ color: 'white', fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
                <Zap size={20} className="text-yellow-400" /> DIRETRIZ DE OPERAÇÃO ATIVA
              </Typography>
              <Chip label={accountType} size="small" color={accountType === AccountType.REAL ? "error" : "primary"} sx={{ fontWeight: 'black' }} />
            </div>

            <Box sx={{ flex: 1 }}>
              {!activeDirective ? (
                <div className="flex flex-col items-center justify-center h-full text-slate-700 opacity-30 gap-4">
                  <Typography variant="h1" sx={{ fontSize: '5rem' }}>📊</Typography>
                  <Typography variant="h6" fontWeight="900" sx={{ letterSpacing: 5 }}>AGUARDANDO SINAL IAG</Typography>
                </div>
              ) : (
                <motion.div initial={{ opacity: 0, scale: 0.98 }} animate={{ opacity: 1, scale: 1 }}>
                   <div className="grid grid-cols-2 gap-8 mb-8">
                      <DirectiveCard label="AÇÃO" value={activeDirective.side} color={activeDirective.side === 'BUY' ? '#10b981' : '#ef4444'} />
                      <DirectiveCard label="CONFIANÇA" value={`${(activeDirective.confidence * 100).toFixed(1)}%`} color="#6366f1" />
                   </div>

                   <div className="grid grid-cols-3 gap-4 mb-8">
                      <PriceBox label="ENTRADA" value={activeDirective.entryPrice} />
                      <PriceBox label="STOP-LOSS" value={activeDirective.stopLoss} isDanger />
                      <PriceBox label="TAKE-PROFIT" value={activeDirective.takeProfit} isSuccess />
                   </div>

                   {activeDirective.reasoning && (
                     <Box sx={{ mb: 4, p: 2, bgcolor: 'rgba(99, 102, 241, 0.05)', border: '1px solid rgba(99, 102, 241, 0.2)', borderRadius: 2 }}>
                        <Typography variant="caption" sx={{ color: '#818cf8', fontWeight: 'black', display: 'block', mb: 1, textTransform: 'uppercase' }}>Raciocínio AGI</Typography>
                        <Typography variant="body2" sx={{ color: '#94a3b8', fontStyle: 'italic' }}>{activeDirective.reasoning}</Typography>
                     </Box>
                   )}

                   <Paper sx={{ p: 3, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 4 }}>
                      <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', mb: 2, display: 'block', letterSpacing: 2 }}>LOG DE EXECUÇÃO PAS</Typography>
                      <div className="space-y-2 max-h-[150px] overflow-y-auto scrollbar-hide">
                        {executionLogs.length > 0 ? (
                          executionLogs.map((log, i) => (
                            <LogLine key={i} time={log.timestamp} msg={log.message} />
                          ))
                        ) : (
                          <>
                            <LogLine time={new Date().toLocaleTimeString()} msg="Sincronizando com Binance API..." />
                            <LogLine time={new Date().toLocaleTimeString()} msg={`Verificando conta ${accountType}... OK`} />
                            <LogLine time={new Date().toLocaleTimeString()} msg="Calculando Stop-Loss Dinâmico via Gemini..." />
                            <LogLine time={new Date().toLocaleTimeString()} msg="Aguardando trigger de preço no Nexus..." />
                          </>
                        )}
                      </div>
                   </Paper>
                </motion.div>
              )}
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

const getPriorityHex = (p: TaskPriority) => {
    switch(p) {
        case TaskPriority.LOW: return '#94a3b8';
        case TaskPriority.MEDIUM: return '#60a5fa';
        case TaskPriority.HIGH: return '#fb923c';
        case TaskPriority.CRITICAL: return '#ef4444';
        default: return '#94a3b8';
    }
};

const StatusItem = ({ label, status, color }: any) => (
  <div className="flex justify-between items-center border-b border-white/5 pb-2">
    <span className="text-[10px] font-black text-slate-500 uppercase">{label}</span>
    <span style={{ color, fontSize: '10px', fontWeight: 'black' }}>{status}</span>
  </div>
);

const DirectiveCard = ({ label, value, color }: any) => (
  <Box sx={{ p: 4, bgcolor: 'rgba(255,255,255,0.02)', border: `1px solid ${color}30`, borderRadius: 6, textAlign: 'center' }}>
    <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', tracking: 2, display: 'block', mb: 1, textTransform: 'uppercase' }}>{label}</Typography>
    <Typography variant="h3" sx={{ color, fontWeight: 'black' }}>{value}</Typography>
  </Box>
);

const PriceBox = ({ label, value, isDanger, isSuccess }: any) => (
  <Box sx={{ p: 2, bgcolor: '#0a0a0a', border: '1px solid #1e293b', borderRadius: 4, textAlign: 'center' }}>
    <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'bold' }}>{label}</Typography>
    <Typography sx={{ 
      color: isDanger ? '#ef4444' : isSuccess ? '#10b981' : 'white', 
      fontWeight: 'black', fontSize: '1rem', fontFamily: 'monospace' 
    }}>${value.toLocaleString()}</Typography>
  </Box>
);

const LogLine = ({ time, msg }: any) => (
  <div className="flex gap-4 font-mono text-[9px]">
    <span className="text-indigo-400">{time}</span>
    <span className="text-slate-400 uppercase">{msg}</span>
  </div>
);
