import React from 'react';
// Added missing Chip import from @mui/material
import { Box, Typography, Paper, Grid, Divider, alpha, Tooltip, Chip } from '@mui/material';
import { 
  Activity, TrendingUp, TrendingDown, BarChart3, 
  Gauge, Zap, Waves, Target, Compass, Layers, 
  Shield, Atom, Cloud, Info, Volume2, MoveUp, MoveDown
} from 'lucide-react';
import { TechnicalIndicators } from '../types';

export const TechnicalIndicatorsPanel: React.FC<{ indicators: TechnicalIndicators }> = ({ indicators }) => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      {/* Header do Painel */}
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <div className="p-2 bg-indigo-500/10 rounded-lg border border-indigo-500/20">
          <Layers size={18} className="text-indigo-400" />
        </div>
        <div>
          <Typography variant="caption" sx={{ color: 'white', fontWeight: 'black', letterSpacing: 2, display: 'block' }}>
            NEXUS TECHNICAL CORE v4.8
          </Typography>
          <Typography variant="caption" sx={{ color: '#475569', fontSize: '9px' }}>
            15+ INDICATORS • REAL-TIME FEED
          </Typography>
        </div>
      </Box>

      {/* CATEGORIA 1: MOMENTUM OSCILLATORS */}
      <Box>
        <SectionHeader icon={<Zap size={12}/>} label="Momentum & Oscillation" />
        <Grid container spacing={1.5}>
          <Grid item xs={6}>
            <IndicatorCard 
              label="RSI (14)" 
              value={indicators.rsi.toFixed(1)} 
              status={indicators.rsi > 70 ? 'OVERBOUGHT' : indicators.rsi < 30 ? 'OVERSOLD' : 'NEUTRAL'}
              color={indicators.rsi > 70 ? '#ef4444' : indicators.rsi < 30 ? '#10b981' : '#6366f1'}
            />
          </Grid>
          <Grid item xs={6}>
            <IndicatorCard 
              label="CCI (20)" 
              value={indicators.cci.toFixed(0)} 
              status={indicators.cci > 100 ? 'BULLISH' : indicators.cci < -100 ? 'BEARISH' : 'STABLE'}
              color={indicators.cci > 100 ? '#10b981' : indicators.cci < -100 ? '#ef4444' : '#64748b'}
            />
          </Grid>
          <Grid item xs={6}>
            <IndicatorCard 
              label="Stoch %K" 
              value={indicators.stochastic.k.toFixed(1)} 
              status={indicators.stochastic.k > 80 ? 'HIGH' : 'LOW'}
              color="#f59e0b"
            />
          </Grid>
          <Grid item xs={6}>
            <IndicatorCard 
              label="Will %R" 
              value={indicators.williamsR.toFixed(1)} 
              status={indicators.williamsR > -20 ? 'BULL' : 'BEAR'}
              color="#8b5cf6"
            />
          </Grid>
          <Grid item xs={6}>
            <IndicatorCard 
              label="MFI (14)" 
              value={indicators.mfi.toFixed(0)} 
              status={indicators.mfi > 80 ? 'PRESSURE' : 'NORMAL'}
              color="#ec4899"
            />
          </Grid>
          <Grid item xs={6}>
            <IndicatorCard 
              label="Awesome Osc" 
              value={indicators.awesomeOscillator.toFixed(2)} 
              status={indicators.awesomeOscillator > 0 ? 'BULLISH' : 'BEARISH'}
              color={indicators.awesomeOscillator > 0 ? '#10b981' : '#ef4444'}
            />
          </Grid>
        </Grid>
      </Box>

      {/* CATEGORIA 2: TREND & CONFLUENCE */}
      <Box>
        <SectionHeader icon={<TrendingUp size={12}/>} label="Trend & Confluence" />
        <Paper sx={{ p: 2, bgcolor: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)', borderRadius: 3 }}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            {/* MACD Display */}
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
               <Box>
                  <Typography sx={{ color: '#475569', fontSize: '8px', fontWeight: 900 }}>MACD (12,26,9)</Typography>
                  <Typography sx={{ color: 'white', fontSize: '12px', fontWeight: 'bold', fontFamily: 'monospace' }}>
                    {indicators.macd.value.toFixed(2)} / {indicators.macd.signal.toFixed(2)}
                  </Typography>
               </Box>
               <Chip 
                  label={indicators.macd.histogram > 0 ? 'BULL CROSS' : 'BEAR CROSS'} 
                  size="small" 
                  sx={{ height: 16, fontSize: '7px', fontWeight: 900, bgcolor: indicators.macd.histogram > 0 ? '#10b98120' : '#ef444420', color: indicators.macd.histogram > 0 ? '#10b981' : '#ef4444' }} 
               />
            </Box>
            
            {/* ADX & SAR */}
            <Grid container spacing={1}>
              <Grid item xs={6}>
                <Box sx={{ p: 1, bgcolor: 'rgba(0,0,0,0.2)', borderRadius: 2 }}>
                  <Typography sx={{ color: '#475569', fontSize: '7px', fontWeight: 900 }}>ADX (TREND STRENGTH)</Typography>
                  <Typography sx={{ color: indicators.adx > 25 ? '#10b981' : '#f59e0b', fontSize: '11px', fontWeight: 'black' }}>{indicators.adx.toFixed(1)}</Typography>
                </Box>
              </Grid>
              <Grid item xs={6}>
                <Box sx={{ p: 1, bgcolor: 'rgba(0,0,0,0.2)', borderRadius: 2 }}>
                  <Typography sx={{ color: '#475569', fontSize: '7px', fontWeight: 900 }}>PARABOLIC SAR</Typography>
                  <Typography sx={{ color: 'white', fontSize: '11px', fontWeight: 'black' }}>{indicators.parabolicSar.toFixed(2)}</Typography>
                </Box>
              </Grid>
            </Grid>
          </Box>
        </Paper>
      </Box>

      {/* CATEGORIA 3: ICHIMOKU CLOUD (FULL) */}
      <Box>
        <SectionHeader icon={<Cloud size={12}/>} label="Ichimoku Kinko Hyo" />
        <Box sx={{ p: 2, bgcolor: 'rgba(99, 102, 241, 0.05)', borderRadius: 3, border: '1px solid rgba(99, 102, 241, 0.1)' }}>
          <Grid container spacing={2}>
            <CloudItem label="Tenkan" value={indicators.ichimoku.tenkan.toFixed(2)} />
            <CloudItem label="Kijun" value={indicators.ichimoku.kijun.toFixed(2)} />
            <CloudItem label="Senkou A" value={indicators.ichimoku.senkouA.toFixed(2)} />
            <CloudItem label="Senkou B" value={indicators.ichimoku.senkouB.toFixed(2)} />
          </Grid>
        </Box>
      </Box>

      {/* CATEGORIA 4: VOLUMETRIC & LIQUIDITY */}
      <Box>
        <SectionHeader icon={<Volume2 size={12}/>} label="Volume & Liquidity" />
        <Grid container spacing={1.5}>
          <Grid item xs={6}>
            <IndicatorCard 
              label="OBV" 
              value={(indicators.obv / 1000).toFixed(1) + 'K'} 
              status={indicators.obv > 0 ? 'ACCUM' : 'DISTR'}
              color="#34d399"
            />
          </Grid>
          <Grid item xs={6}>
            <IndicatorCard 
              label="CMF (20)" 
              value={indicators.chaikinMoneyFlow.toFixed(2)} 
              status={indicators.chaikinMoneyFlow > 0 ? 'INFLOW' : 'OUTFLOW'}
              color={indicators.chaikinMoneyFlow > 0 ? '#10b981' : '#ef4444'}
            />
          </Grid>
          <Grid item xs={12}>
             <Paper sx={{ p: 1.5, bgcolor: 'rgba(255,255,255,0.02)', border: '1px solid rgba(255,255,255,0.05)', borderRadius: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography sx={{ color: '#475569', fontSize: '8px', fontWeight: 900 }}>VWAP (SESSÃO)</Typography>
                <Typography sx={{ color: '#818cf8', fontSize: '11px', fontWeight: 'black', fontFamily: 'monospace' }}>${indicators.vwap.toLocaleString()}</Typography>
             </Paper>
          </Grid>
        </Grid>
      </Box>

      {/* CATEGORIA 5: RISK & VOLATILITY */}
      <Box>
        <SectionHeader icon={<Shield size={12}/>} label="Risk & Volatility" />
        <Grid container spacing={1.5}>
          <Grid item xs={12}>
             <IndicatorCard 
              label="Bollinger Bands (20,2)" 
              value={`W: ${((indicators.bollinger.upper - indicators.bollinger.lower)/indicators.bollinger.mid * 100).toFixed(2)}%`} 
              status="STABLE"
              color="#6366f1"
            />
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
};

const SectionHeader = ({ icon, label }: any) => (
  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1.5, opacity: 0.6 }}>
    <span className="text-slate-400">{icon}</span>
    <Typography sx={{ color: 'white', fontSize: '9px', fontWeight: 'black', letterSpacing: 1, textTransform: 'uppercase' }}>{label}</Typography>
    <Divider sx={{ flex: 1, bgcolor: 'white/5' }} />
  </Box>
);

const IndicatorCard = ({ label, value, status, color }: any) => (
  <Paper sx={{ 
    p: 1.5, 
    bgcolor: 'rgba(255,255,255,0.02)', 
    border: '1px solid rgba(255,255,255,0.05)', 
    borderRadius: 3,
    transition: 'all 0.2s',
    '&:hover': { borderColor: alpha(color, 0.4), bgcolor: alpha(color, 0.05) }
  }}>
    <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', fontSize: '7px', textTransform: 'uppercase', display: 'block', mb: 0.5 }}>{label}</Typography>
    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end' }}>
      <Typography sx={{ color: 'white', fontWeight: 'black', fontSize: '11px', fontFamily: 'monospace' }}>{value}</Typography>
      <Typography sx={{ color, fontSize: '6px', fontWeight: '900', bgcolor: alpha(color, 0.1), px: 0.5, borderRadius: '2px', border: `1px solid ${alpha(color, 0.2)}` }}>{status}</Typography>
    </Box>
  </Paper>
);

const CloudItem = ({ label, value }: { label: string, value: string }) => (
  <Grid item xs={6}>
    <Typography sx={{ color: '#475569', fontSize: '7px', fontWeight: 900, textTransform: 'uppercase' }}>{label}</Typography>
    <Typography sx={{ color: 'white', fontSize: '10px', fontWeight: 'bold', fontFamily: 'monospace' }}>{value}</Typography>
  </Grid>
);