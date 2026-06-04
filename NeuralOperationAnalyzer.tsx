
import React, { useState, useEffect } from 'react';
import { Box, Grid, Paper, Typography, LinearProgress, Chip } from '@mui/material';
import { Activity, Zap, TrendingUp, BarChart3, Clock, Target, ShieldCheck } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import CountUp from 'react-countup';

export const NeuralOperationAnalyzer: React.FC = () => {
  const [metrics, setMetrics] = useState({
    accuracy: 94.2,
    confidence: 88.7,
    pnl: 12847.30,
    profitFactor: 2.15,
    avgHoldTime: '12m 45s'
  });

  const [data, setData] = useState<any[]>([]);

  useEffect(() => {
    const interval = setInterval(() => {
      setMetrics(prev => ({
        ...prev,
        accuracy: Math.min(99.9, prev.accuracy + (Math.random() - 0.5)),
        pnl: prev.pnl + (Math.random() - 0.4) * 10,
        profitFactor: 2.15 + (Math.random() - 0.5) * 0.05
      }));
      
      setData(prev => [...prev.slice(-29), { 
        time: new Date().toLocaleTimeString(), 
        val: 80 + Math.random() * 20 
      }]);
    }, 2000);
    return () => clearInterval(interval);
  }, []);

  return (
    <Box sx={{ p: 0, spaceY: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 6 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 'black', color: 'white' }}>IAG ANALYZE</Typography>
          <Typography variant="caption" sx={{ color: '#10b981', fontWeight: 'bold', letterSpacing: 2 }}>CORE PERFORMANCE & EFFICIENCY TELEMETRY</Typography>
        </Box>
        <Chip label="LIVE AUDIT ACTIVE" color="success" sx={{ borderRadius: 2, fontWeight: 'black' }} />
      </Box>

      <Grid container spacing={3}>
        {/* Fix: use item xs/md instead of size prop for Grid v1 */}
        <Grid item xs={12} md={3}>
          <MetricBox label="Success Accuracy" value={metrics.accuracy} suffix="%" color="#10b981" icon={<Target size={14}/>} />
        </Grid>
        {/* Fix: use item xs/md instead of size prop for Grid v1 */}
        <Grid item xs={12} md={3}>
          <MetricBox label="Profit Factor" value={metrics.profitFactor} color="#6366f1" icon={<TrendingUp size={14}/>} />
        </Grid>
        {/* Fix: use item xs/md instead of size prop for Grid v1 */}
        <Grid item xs={12} md={3}>
          <MetricBox label="Avg. Hold Time" value={0} customValue={metrics.avgHoldTime} color="#f59e0b" icon={<Clock size={14}/>} />
        </Grid>
        {/* Fix: use item xs/md instead of size prop for Grid v1 */}
        <Grid item xs={12} md={3}>
          <MetricBox label="Risk Stability" value={98.2} suffix="%" color="#ef4444" icon={<ShieldCheck size={14}/>} />
        </Grid>

        {/* Fix: use item xs instead of size prop for Grid v1 */}
        <Grid item xs={12}>
          <Paper sx={{ p: 4, bgcolor: '#0a0a0a', border: '1px solid #1e293b', borderRadius: 8 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 6 }}>
               <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: 'black', display: 'flex', alignItems: 'center', gap: 2 }}>
                 <BarChart3 size={18} className="text-indigo-400" /> CUMULATIVE EQUITY CURVE (NEURAL NODES)
               </Typography>
               <Box sx={{ display: 'flex', gap: 2 }}>
                  <Chip label="MAX DRAWDOWN: 4.2%" size="small" sx={{ bgcolor: '#ef444420', color: '#ef4444', fontWeight: 'black' }} />
                  <Chip label="SHARPE: 3.12" size="small" sx={{ bgcolor: '#6366f120', color: '#6366f1', fontWeight: 'black' }} />
               </Box>
            </Box>
            
            <Box sx={{ width: '100%', height: 400, position: 'relative' }}>
              <ResponsiveContainer width="100%" height="100%" minWidth={0}>
                <AreaChart data={data}>
                  <defs>
                    <linearGradient id="analyzeGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#10b981" stopOpacity={0.3}/>
                      <stop offset="95%" stopColor="#10b981" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" vertical={false} />
                  <XAxis dataKey="time" hide />
                  <YAxis hide />
                  <Tooltip contentStyle={{ backgroundColor: '#0f172a', border: 'none', borderRadius: 12 }} />
                  <Area type="monotone" dataKey="val" stroke="#10b981" fillOpacity={1} fill="url(#analyzeGrad)" strokeWidth={4} />
                </AreaChart>
              </ResponsiveContainer>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

const MetricBox: React.FC<{ label: string, value: number, suffix?: string, prefix?: string, customValue?: string, color: string, icon: React.ReactNode }> = ({ label, value, suffix, prefix, customValue, color, icon }) => (
  <Paper sx={{ p: 3, bgcolor: '#0a0a0a', border: '1px solid #1e293b', borderRadius: 4 }}>
    <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: 1 }}>{icon} {label}</Typography>
    <Typography variant="h4" sx={{ color: 'white', fontWeight: '900', mt: 1 }}>
      {customValue || <>{prefix}<CountUp end={value} decimals={value < 10 ? 2 : 1} duration={1} />{suffix}</>}
    </Typography>
    <LinearProgress variant="determinate" value={customValue ? 100 : Math.min(100, value)} sx={{ mt: 2, height: 4, borderRadius: 2, bgcolor: '#1e293b', '& .MuiLinearProgress-bar': { bgcolor: color } }} />
  </Paper>
);
