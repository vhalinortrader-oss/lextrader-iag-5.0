
import React from 'react';
import { Box, Grid, Paper, Typography, Button, IconButton, Chip } from '@mui/material';
// Fix: changed Robot to Bot
import { Package, Play, Settings, Trash2, ExternalLink, Bot, Puzzle } from 'lucide-react';

interface Plugin {
  id: string;
  name: string;
  author: string;
  version: string;
  status: 'RUNNING' | 'PAUSED' | 'IDLE';
  type: 'INDICATOR' | 'BOT' | 'UI';
}

export const PluginHub: React.FC = () => {
  const plugins: Plugin[] = [
    { id: '1', name: 'RSI-Adaptive Neuro', author: 'Lextrader Labs', version: '2.1', status: 'RUNNING', type: 'INDICATOR' },
    { id: '2', name: 'Arbitrage L0 Bridge', author: 'QuantumDev', version: '1.0', status: 'IDLE', type: 'BOT' },
    { id: '3', name: 'Order Book Visualizer', author: 'FinUI', version: '4.7', status: 'RUNNING', type: 'UI' },
  ];

  return (
    <Box sx={{ p: 0 }}>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 'black', color: 'white' }}>PLUGINS & ROBOTS</Typography>
          <Typography variant="caption" sx={{ color: '#818cf8', fontWeight: 'bold', letterSpacing: 2 }}>EXTENDA AS CAPACIDADES DO KERNEL v4.7.2</Typography>
        </Box>
        <Button variant="contained" startIcon={<Package size={18} />} sx={{ bgcolor: '#6366f1', borderRadius: 4, px: 3, fontWeight: 'black' }}>
          IMPORT PLUGIN
        </Button>
      </Box>

      <Grid container spacing={3}>
        {plugins.map(p => (
          <Grid item xs={12} md={4} key={p.id}>
            <Paper sx={{ 
              p: 3, bgcolor: '#0a0a0a', border: '1px solid #1e293b', borderRadius: 6,
              transition: 'all 0.3s', '&:hover': { borderColor: '#6366f1', bgcolor: '#0f172a' }
            }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
                  <Box sx={{ p: 1.5, bgcolor: '#1e293b', borderRadius: 3 }}>
                    {/* Fix: changed Robot to Bot */}
                    {p.type === 'BOT' ? <Bot size={20} className="text-yellow-400" /> : <Puzzle size={20} className="text-indigo-400" />}
                  </Box>
                  <Box>
                    <Typography variant="subtitle1" sx={{ color: 'white', fontWeight: 'black' }}>{p.name}</Typography>
                    <Typography variant="caption" sx={{ color: '#64748b' }}>v{p.version} por {p.author}</Typography>
                  </Box>
                </Box>
                <Chip 
                  label={p.status} 
                  size="small" 
                  sx={{ 
                    height: 20, fontSize: '0.6rem', fontWeight: 'black',
                    bgcolor: p.status === 'RUNNING' ? 'rgba(16, 185, 129, 0.1)' : 'rgba(100, 116, 139, 0.1)',
                    color: p.status === 'RUNNING' ? '#10b981' : '#64748b'
                  }} 
                />
              </Box>

              <Box sx={{ display: 'flex', gap: 1, mt: 3 }}>
                <Button fullWidth variant="contained" size="small" sx={{ bgcolor: '#6366f1', fontWeight: 'black', borderRadius: 2 }}>
                  {p.status === 'RUNNING' ? 'STOP' : 'LAUNCH'}
                </Button>
                <IconButton size="small" sx={{ color: '#64748b' }}><Settings size={18}/></IconButton>
                <IconButton size="small" sx={{ color: '#ef4444' }}><Trash2 size={18}/></IconButton>
              </Box>
            </Paper>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};
