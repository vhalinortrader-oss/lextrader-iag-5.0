
import React, { useState, useEffect } from 'react';
import { Box, Typography, Paper, Grid, Chip, LinearProgress, alpha, Divider, Button, CircularProgress } from '@mui/material';
import { 
  Globe, Newspaper, TrendingUp, TrendingDown, Clock, 
  Zap, AlertTriangle, Info, RefreshCw, BarChart2,
  Gauge, Timer, Radio, Binary, Sparkles
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { MarketNews, SessionInfo } from '../types';
import { getDeepMarketIntelligence, getMacroNexusReport } from '../services/gemini';
import { globalMarketData, TickerInfo } from '../services/GlobalMarketDataService';

export const MarketIntelligencePanel: React.FC<{ symbol: string }> = ({ symbol }) => {
  const [loading, setLoading] = useState(false);
  const [macroLoading, setMacroLoading] = useState(false);
  const [macroReport, setMacroReport] = useState<string | null>(null);
  const [intelligence, setIntelligence] = useState<{ news: MarketNews[], sentimentSummary: string }>({
    news: [],
    sentimentSummary: 'Aguardando atualização neural...'
  });
  const [sessions, setSessions] = useState<SessionInfo[]>([]);
  const [tickers, setTickers] = useState<TickerInfo[]>(globalMarketData.getTickers());

  const fetchIntelligence = async () => {
    setLoading(true);
    const data = await getDeepMarketIntelligence(symbol);
    setIntelligence(data);
    setLoading(false);
  };

  const fetchMacroNexus = async () => {
    setMacroLoading(true);
    const report = await getMacroNexusReport();
    setMacroReport(report);
    setMacroLoading(false);
  };

  useEffect(() => {
    globalMarketData.startStreaming();
    const handleMarketUpdate = (data: TickerInfo[]) => setTickers(data);
    // Fixed: Property 'on' does not exist on type 'GlobalMarketDataService'
    (globalMarketData as any).on('marketUpdate', handleMarketUpdate);

    const updateSessions = () => {
      const now = new Date();
      const hour = now.getUTCHours();
      const sessionData: SessionInfo[] = [
        { name: 'LONDON', status: (hour >= 8 && hour < 16) ? 'OPEN' : 'CLOSED', volatility: 'HIGH', remainingTime: '04:12:00' },
        { name: 'NEW YORK', status: (hour >= 13 && hour < 21) ? 'OPEN' : 'CLOSED', volatility: 'HIGH', remainingTime: '02:45:00' },
        { name: 'SYDNEY', status: (hour >= 22 || hour < 6) ? 'OPEN' : 'CLOSED', volatility: 'LOW', remainingTime: 'CLOSED' },
        { name: 'TOKYO', status: (hour >= 0 && hour < 9) ? 'OPEN' : 'CLOSED', volatility: 'MEDIUM', remainingTime: '01:22:00' }
      ];
      setSessions(sessionData);
    };

    updateSessions();
    fetchIntelligence();
    fetchMacroNexus();
    
    return () => {
      // Fixed: Property 'off' does not exist on type 'GlobalMarketDataService'
      (globalMarketData as any).off('marketUpdate', handleMarketUpdate);
    };
  }, [symbol]);

  const avgSentimentScore = intelligence.news.length > 0 
    ? intelligence.news.reduce((acc, n) => acc + n.score, 0) / intelligence.news.length 
    : 0;

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
      {/* Dynamic Market Ticker Tape */}
      <Paper sx={{ p: 2, bgcolor: '#010409', border: '1px solid rgba(99, 102, 241, 0.1)', overflow: 'hidden', whiteSpace: 'nowrap' }}>
        <Box sx={{ display: 'flex', gap: 6, animation: 'tickerTape 60s linear infinite' }}>
          {tickers.map(t => (
            <Box key={t.symbol} sx={{ display: 'inline-flex', alignItems: 'center', gap: 1.5 }}>
              <Typography sx={{ fontSize: '10px', fontWeight: 'black', color: '#475569' }}>{t.symbol}</Typography>
              <Typography sx={{ fontSize: '12px', fontWeight: 'bold', color: 'white', fontFamily: 'monospace' }}>
                {t.price.toLocaleString(undefined, { minimumFractionDigits: t.type === 'FOREX' ? 4 : 2 })}
              </Typography>
              <Typography sx={{ fontSize: '9px', fontWeight: 'black', color: t.change24h >= 0 ? '#10b981' : '#ef4444' }}>
                {t.change24h >= 0 ? '+' : ''}{t.change24h.toFixed(2)}%
              </Typography>
            </Box>
          ))}
        </Box>
      </Paper>

      {/* Global Session Monitor */}
      <Paper sx={{ p: 4, bgcolor: '#010409', borderRadius: 8, border: '1px solid rgba(99, 102, 241, 0.2)' }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <div className="p-2 bg-indigo-500/10 rounded-lg">
              <Clock size={20} className="text-indigo-400" />
            </div>
            <div>
              <Typography variant="h6" sx={{ color: 'white', fontWeight: 900, fontSize: '14px', letterSpacing: 2 }}>
                GLOBAL SESSIONS MONITOR
              </Typography>
              <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'bold' }}>UTC SYNCHRONIZED FEED</Typography>
            </div>
          </Box>
        </Box>
        <Grid container spacing={2}>
          {sessions.map(session => (
            <Grid item xs={6} md={3} key={session.name}>
              <Box sx={{ 
                p: 2, 
                bgcolor: alpha(session.status === 'OPEN' ? '#10b981' : '#475569', 0.05), 
                borderRadius: 4,
                border: `1px solid ${alpha(session.status === 'OPEN' ? '#10b981' : '#334155', 0.2)}`
              }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                  <Typography sx={{ color: session.status === 'OPEN' ? '#10b981' : '#94a3b8', fontSize: '10px', fontWeight: 'black' }}>{session.name}</Typography>
                  <Chip 
                    label={session.status} 
                    size="small" 
                    sx={{ 
                      height: 14, fontSize: '7px', fontWeight: 'black',
                      bgcolor: session.status === 'OPEN' ? '#10b98120' : '#47556920',
                      color: session.status === 'OPEN' ? '#10b981' : '#94a3b8'
                    }} 
                  />
                </Box>
                <Typography variant="h6" sx={{ color: 'white', fontWeight: 900, fontSize: '12px' }}>{session.remainingTime}</Typography>
                <Typography variant="caption" sx={{ color: '#475569', fontSize: '8px', fontWeight: 'bold' }}>Volatility: {session.volatility}</Typography>
              </Box>
            </Grid>
          ))}
        </Grid>
      </Paper>

      <Grid container spacing={4}>
        {/* Macro Nexus Report */}
        <Grid item xs={12} lg={7}>
          <Paper sx={{ p: 4, bgcolor: '#010409', borderRadius: 8, height: '100%', border: '1px solid rgba(99, 102, 241, 0.1)', position: 'relative', overflow: 'hidden' }}>
            <div className="absolute top-0 right-0 p-4 opacity-5">
               <Globe size={120} />
            </div>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
              <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: 900, display: 'flex', alignItems: 'center', gap: 2 }}>
                <Binary size={18} className="text-indigo-400" /> MACRO NEXUS REPORT
              </Typography>
              <Button 
                onClick={fetchMacroNexus} 
                disabled={macroLoading}
                startIcon={macroLoading ? <CircularProgress size={12} color="inherit" /> : <RefreshCw size={14} />}
                sx={{ color: '#818cf8', fontWeight: 'black', fontSize: '10px' }}
              >
                UPDATE MACRO
              </Button>
            </Box>
            
            <Box sx={{ minHeight: '300px' }}>
              {macroLoading ? (
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                   <Skeleton variant="text" sx={{ bgcolor: 'white/5' }} />
                   <Skeleton variant="rectangular" height={100} sx={{ bgcolor: 'white/5', borderRadius: 2 }} />
                   <Skeleton variant="text" sx={{ bgcolor: 'white/5' }} />
                </Box>
              ) : (
                <Typography variant="body2" sx={{ color: '#cbd5e1', lineHeight: 1.8, fontSize: '13px', whiteSpace: 'pre-line', fontStyle: 'italic' }}>
                  {macroReport || "Iniciando varredura macroeconômica global..."}
                </Typography>
              )}
            </Box>
          </Paper>
        </Grid>

        {/* Sentiment Gauge */}
        <Grid item xs={12} lg={5}>
          <Paper sx={{ p: 4, bgcolor: '#010409', borderRadius: 8, height: '100%', border: '1px solid rgba(99, 102, 241, 0.1)' }}>
            <Typography variant="subtitle2" sx={{ color: 'white', fontWeight: 900, mb: 4, display: 'flex', alignItems: 'center', gap: 2 }}>
              <Gauge size={18} className="text-indigo-400" /> NEURAL SENTIMENT GAUGE
            </Typography>
            
            <Box sx={{ textAlign: 'center', py: 4 }}>
              <Box sx={{ position: 'relative', display: 'inline-flex', mb: 4 }}>
                <CircularProgressIndicator value={((avgSentimentScore + 10) / 20) * 100} color={avgSentimentScore >= 0 ? '#10b981' : '#ef4444'} />
                <Box sx={{ top: 0, left: 0, bottom: 0, right: 0, position: 'absolute', display: 'flex', alignItems: 'center', justifyContent: 'center', flexDirection: 'column' }}>
                  <Typography variant="h3" sx={{ color: 'white', fontWeight: 900, mb: -1 }}>{avgSentimentScore.toFixed(1)}</Typography>
                  <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black' }}>/ 10</Typography>
                </Box>
              </Box>
              
              <Typography variant="h6" sx={{ color: avgSentimentScore >= 0 ? '#10b981' : '#ef4444', fontWeight: 900, mb: 1 }}>
                {avgSentimentScore >= 5 ? 'STRONG BULLISH' : avgSentimentScore > 0 ? 'MODERATE BULLISH' : avgSentimentScore > -5 ? 'MODERATE BEARISH' : 'STRONG BEARISH'}
              </Typography>
              
              <Typography variant="body2" sx={{ color: '#94a3b8', fontSize: '11px', fontStyle: 'italic', px: 2 }}>
                "{intelligence.sentimentSummary}"
              </Typography>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

const CircularProgressIndicator = ({ value, color }: { value: number, color: string }) => {
  const size = 160;
  const thickness = 5;
  const radius = (size - thickness) / 2;
  const circumference = 2 * Math.PI * radius;
  const offset = circumference - (value / 100) * circumference;

  return (
    <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
      <circle 
        cx={size / 2} cy={size / 2} r={radius} 
        stroke="rgba(255,255,255,0.05)" strokeWidth={thickness} fill="none" 
      />
      <circle 
        cx={size / 2} cy={size / 2} r={radius} 
        stroke={color} strokeWidth={thickness} fill="none" 
        strokeDasharray={circumference} strokeDashoffset={offset}
        strokeLinecap="round" transform={`rotate(-90 ${size / 2} ${size / 2})`}
        style={{ transition: 'stroke-dashoffset 0.5s ease' }}
      />
    </svg>
  );
};

const Skeleton = ({ variant, height, sx }: any) => (
  <Box sx={{ width: '100%', height: height || '20px', bgcolor: 'rgba(255,255,255,0.05)', borderRadius: 1, animation: 'pulse 1.5s infinite', ...sx }} />
);
