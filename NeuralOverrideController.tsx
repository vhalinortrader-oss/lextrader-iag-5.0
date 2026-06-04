
import React, { useState } from 'react';
import { Box, Grid, Paper, Typography, Button, Switch, FormControlLabel } from '@mui/material';
import { ShieldAlert, Zap, Lock, Unlock } from 'lucide-react';
import { format } from 'date-fns';

export const NeuralOverrideController: React.FC = () => {
  const [isLocked, setIsLocked] = useState(false);

  return (
    <Box sx={{ p: 4, bgcolor: '#0a0a0a', borderRadius: 8, border: '1px solid #ef444430' }}>
      <div className="flex justify-between items-center mb-8">
        <div className="flex items-center gap-3">
          <ShieldAlert className="text-red-500" size={24} />
          <Typography variant="h5" sx={{ fontWeight: 'black', color: 'white' }}>NEURAL OVERRIDE CONTROL</Typography>
        </div>
        <FormControlLabel
          control={<Switch checked={!isLocked} onChange={() => setIsLocked(!isLocked)} color="error" />}
          label={<span className="text-[10px] font-black text-slate-400 uppercase">System Status</span>}
        />
      </div>

      <Grid container spacing={4}>
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 4, bgcolor: isLocked ? '#450a0a20' : '#0f172a', borderRadius: 6, border: '1px solid #ef444420' }}>
            <div className="flex justify-between items-center mb-6">
              <Typography variant="h6" sx={{ color: 'white' }}>Protocolos de Emergência</Typography>
              {isLocked ? <Lock className="text-red-500" size={20} /> : <Unlock className="text-emerald-500" size={20} />}
            </div>
            
            <div className="space-y-4">
              <OverrideAction label="Liquidação Total Instantânea" type="danger" disabled={isLocked} />
              <OverrideAction label="Halt em Todas as Execuções" type="warning" disabled={isLocked} />
              <OverrideAction label="Rollback de Último Bloco" type="info" disabled={isLocked} />
            </div>
          </Paper>
        </Grid>

        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 4, bgcolor: '#0f172a', borderRadius: 6, border: '1px solid #1e293b' }}>
            <Typography variant="subtitle2" sx={{ color: '#94a3b8', mb: 3 }}>Últimas Intervenções</Typography>
            <div className="space-y-4">
              <LogEntry time="14:25:01" msg="Auto-Hedge ativado por volatilidade" />
              <LogEntry time="12:10:44" msg="Ajuste de alavancagem via Nexus" />
              <LogEntry time="09:15:22" msg="Resync de Layer 05 solicitado" />
            </div>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

const OverrideAction: React.FC<{ label: string, type: 'danger' | 'warning' | 'info', disabled?: boolean }> = ({ label, type, disabled }) => (
  <Button 
    fullWidth 
    variant="contained" 
    disabled={disabled}
    sx={{ 
      py: 2, 
      borderRadius: 4,
      bgcolor: type === 'danger' ? '#dc262620' : type === 'warning' ? '#f59e0b20' : '#3b82f620',
      color: type === 'danger' ? '#ef4444' : type === 'warning' ? '#f59e0b' : '#3b82f6',
      border: `1px solid ${type === 'danger' ? '#ef444440' : type === 'warning' ? '#f59e0b40' : '#3b82f640'}`,
      '&:hover': { bgcolor: type === 'danger' ? '#dc262640' : type === 'warning' ? '#f59e0b40' : '#3b82f640' }
    }}
  >
    <span className="font-black text-[10px] tracking-widest">{label}</span>
  </Button>
);

const LogEntry: React.FC<{ time: string, msg: string }> = ({ time, msg }) => (
  <div className="flex gap-4 items-start border-l border-white/5 pl-4 py-1">
    <span className="text-[9px] font-mono text-indigo-400">{time}</span>
    <span className="text-[10px] text-slate-400 uppercase leading-tight">{msg}</span>
  </div>
);
