
import React, { useState, useEffect, useMemo } from 'react';
import { 
  TrendingUp, TrendingDown, Target, Activity, 
  ArrowRightLeft, Info, Clock, ShieldCheck, Sparkles, Wand2,
  BrainCircuit, LayoutDashboard, RefreshCcw, Search, Filter,
  Maximize2, ChevronRight, BarChart2, Zap
} from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { GoogleGenAI } from "@google/genai";
import { motion, AnimatePresence } from 'framer-motion';
import { Trade, MarketRegime } from '../types';

const ASSET_BASES: Record<string, number> = {
  'BTC/USDT': 98542.20,
  'ETH/USDT': 2745.50,
  'SOL/USDT': 184.32,
  'BNB/USDT': 642.15
};

export const OmegaTradingTerminal: React.FC = () => {
  const [selectedSymbol, setSelectedSymbol] = useState('BTC/USDT');
  const [currentPrice, setCurrentPrice] = useState(ASSET_BASES['BTC/USDT']);
  const [priceHistory, setPriceHistory] = useState<any[]>([]);
  const [isAutonomous, setIsAutonomous] = useState(true);
  const [marketRegime, setMarketRegime] = useState<MarketRegime>(MarketRegime.SIDEWAYS);
  
  const [indicators, setIndicators] = useState<any>({
    rsi: 48.5,
    neuralSentiment: 52,
    atr: 845.30,
    volatility: 0.15
  });

  const [selectedTradeId, setSelectedTradeId] = useState<string | null>(null);
  const [aiInsight, setAiInsight] = useState<string | null>(null);
  const [isAnalyzing, setIsAnalyzing] = useState(false);

  const [trades] = useState<Trade[]>([
    { id: 'TX-9402', symbol: 'BTC/USDT', side: 'BUY', entryPrice: 94200.50, currentPrice: 98542.20, pnlAbs: 4341.70, pnlPct: 4.61, strategy: 'Quantum Momentum', timestamp: '2026-01-16T10:30:00Z', status: 'OPEN' },
    { id: 'TX-9405', symbol: 'ETH/USDT', side: 'SELL', entryPrice: 2850.20, currentPrice: 2745.50, pnlAbs: 104.70, pnlPct: 3.67, strategy: 'Neural Scalp', timestamp: '2026-01-16T11:15:00Z', status: 'OPEN' }
  ] as any);

  const activeFocus = useMemo(() => trades.find(t => t.id === selectedTradeId), [trades, selectedTradeId]);

  const runTradeAnalysis = async () => {
    if (!activeFocus) return;
    setIsAnalyzing(true);
    setAiInsight(null);
    try {
      const ai = new GoogleGenAI({ apiKey: process.env.API_KEY });
      const prompt = `Analise tecnicamente: ${activeFocus.symbol} (${activeFocus.side}) P/L: ${activeFocus.pnlPct}%. Contexto: ${marketRegime}. Responda como Luthien ASI em 1 frase curta e impactante.`;
      const response = await ai.models.generateContent({
        model: 'gemini-3-flash-preview',
        contents: [{ role: 'user', parts: [{ text: prompt }] }],
      });
      setAiInsight(response.text || "Erro na síntese neural.");
    } catch (err) {
      setAiInsight("Falha de conexão ASI.");
    } finally {
      setIsAnalyzing(false);
    }
  };

  useEffect(() => {
    let base = ASSET_BASES[selectedSymbol];
    const initialHistory = Array.from({ length: 60 }, (_, i) => {
      base += (Math.random() - 0.495) * (base * 0.0012);
      return { 
        time: new Date(Date.now() - (60 - i) * 5000).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }), 
        price: parseFloat(base.toFixed(2))
      };
    });
    setPriceHistory(initialHistory);
    setCurrentPrice(base);

    const interval = setInterval(() => {
      setPriceHistory(prev => {
        const lastItem = prev[prev.length - 1];
        const drift = (Math.random() - 0.49) * (lastItem.price * 0.001);
        const nextPrice = parseFloat((lastItem.price + drift).toFixed(2));
        setCurrentPrice(nextPrice);
        
        setIndicators((curr: any) => {
          const nextRsi = Math.max(10, Math.min(90, curr.rsi + (Math.random() - 0.5) * 4));
          if (nextRsi > 65) setMarketRegime(MarketRegime.BULL);
          else if (nextRsi < 35) setMarketRegime(MarketRegime.BEAR);
          else setMarketRegime(MarketRegime.SIDEWAYS);
          return { ...curr, rsi: nextRsi, neuralSentiment: Math.max(0, Math.min(100, curr.neuralSentiment + (Math.random() - 0.5) * 5)) };
        });

        const newPoint = {
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }),
          price: nextPrice
        };
        return [...prev.slice(-59), newPoint];
      });
    }, 5000);

    return () => clearInterval(interval);
  }, [selectedSymbol]);

  const regimeColor = marketRegime === MarketRegime.BULL ? '#10b981' : marketRegime === MarketRegime.BEAR ? '#ef4444' : '#6366f1';

  return (
    <div className="flex flex-col gap-4 h-full">
      {/* Terminal Header */}
      <div className="flex flex-wrap items-center justify-between gap-4 p-4 bg-[#010409]/60 backdrop-blur-xl border border-white/5 rounded-3xl">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-indigo-500/10 rounded-2xl border border-indigo-500/20 shadow-[0_0_20px_rgba(79,70,229,0.1)]">
            <LayoutDashboard size={24} style={{ color: regimeColor }} />
          </div>
          <div>
            <h1 className="text-sm font-black text-white uppercase tracking-[0.2em] flex items-center gap-2">
              Omega Terminal <span className="text-[10px] text-indigo-500 font-mono">v4.7.2</span>
            </h1>
            <div className="flex items-center gap-2 mt-1">
              <div className={`w-1.5 h-1.5 rounded-full ${isAutonomous ? 'bg-emerald-500 animate-pulse' : 'bg-slate-600'}`} />
              <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest">
                {isAutonomous ? 'Autonomous Active' : 'Manual Oversight'} • {marketRegime} REGIME
              </span>
            </div>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <div className="flex items-center gap-2 px-3 py-1.5 bg-black/40 rounded-xl border border-white/5">
            <Search size={12} className="text-slate-500" />
            <select 
              value={selectedSymbol} 
              onChange={(e) => setSelectedSymbol(e.target.value)}
              className="bg-transparent text-[10px] font-black text-white uppercase tracking-widest outline-none cursor-pointer"
            >
              {Object.keys(ASSET_BASES).map(sym => <option key={sym} value={sym} className="bg-[#0f172a]">{sym}</option>)}
            </select>
          </div>
          
          <button 
            onClick={() => setIsAutonomous(!isAutonomous)}
            className={`flex items-center gap-2 px-4 py-1.5 rounded-xl border transition-all ${
              isAutonomous 
              ? 'bg-indigo-500/10 border-indigo-500/30 text-indigo-400' 
              : 'bg-slate-800/50 border-white/5 text-slate-500'
            }`}
          >
            <BrainCircuit size={14} />
            <span className="text-[9px] font-black uppercase tracking-widest">AGI Protocol</span>
          </button>
        </div>
      </div>

      {/* Main Chart Section */}
      <div className="grid grid-cols-1 xl:grid-cols-4 gap-4 flex-1 min-h-0">
        <div className="xl:col-span-3 flex flex-col bg-[#010409]/60 backdrop-blur-xl border border-white/5 rounded-3xl overflow-hidden group">
          <div className="p-6 flex justify-between items-start border-b border-white/5">
            <div>
              <div className="flex items-center gap-3">
                <h2 className="text-[11px] font-black text-indigo-400 uppercase tracking-[0.3em]">{selectedSymbol} Quantum Feed</h2>
                <div className="px-2 py-0.5 bg-rose-500/10 rounded text-[8px] font-black text-rose-500 uppercase tracking-widest border border-rose-500/20">Real-Time</div>
              </div>
              <div className="flex gap-4 mt-2">
                <div className="flex items-center gap-1.5">
                  <BarChart2 size={10} className="text-slate-600" />
                  <span className="text-[9px] font-black text-slate-500 uppercase tracking-widest">Vol: {indicators.atr.toFixed(2)}</span>
                </div>
                <div className="flex items-center gap-1.5">
                  <Zap size={10} className="text-slate-600" />
                  <span className="text-[9px] font-black text-slate-500 uppercase tracking-widest">Drift: +0.02%</span>
                </div>
              </div>
            </div>
            <div className="text-right">
              <div className="text-3xl font-black text-white font-mono tracking-tighter">
                ${currentPrice.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </div>
              <div className="flex items-center justify-end gap-1.5 mt-1">
                <Activity size={12} className="text-emerald-400 animate-pulse" />
                <span className="text-[9px] font-black text-emerald-400 uppercase tracking-widest">Live Sensor Data</span>
              </div>
            </div>
          </div>

          <div className="flex-1 p-4 min-h-[300px]">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={priceHistory}>
                <defs>
                  <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor={regimeColor} stopOpacity={0.2}/>
                    <stop offset="95%" stopColor={regimeColor} stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.02)" vertical={false} />
                <XAxis dataKey="time" hide />
                <YAxis domain={['auto', 'auto']} hide />
                <Tooltip 
                  contentStyle={{ backgroundColor: '#020617', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '16px', padding: '12px', backdropFilter: 'blur(10px)' }}
                  itemStyle={{ color: '#fff', fontWeight: '900', fontFamily: 'JetBrains Mono', fontSize: '12px' }}
                  labelStyle={{ color: '#64748b', marginBottom: '4px', fontSize: '9px', fontWeight: '900', textTransform: 'uppercase', letterSpacing: '1px' }}
                  formatter={(value: number) => [`$${value.toLocaleString()}`, "Price"]}
                />
                <Area 
                  type="monotone" 
                  dataKey="price" 
                  stroke={regimeColor} 
                  strokeWidth={3} 
                  fill="url(#colorPrice)" 
                  isAnimationActive={false}
                  dot={false}
                />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Sidebar Metrics */}
        <div className="flex flex-col gap-4">
          <div className="p-6 bg-[#010409]/60 backdrop-blur-xl border border-white/5 rounded-3xl flex-1">
            <h3 className="text-[9px] font-black text-slate-600 uppercase tracking-[0.3em] mb-6">Neural Metrics</h3>
            <div className="space-y-6">
              <div className="space-y-2">
                <div className="flex justify-between items-end">
                  <span className="text-[9px] font-black text-slate-400 uppercase tracking-widest">RSI (14)</span>
                  <span className="text-[10px] font-black text-white font-mono">{indicators.rsi.toFixed(1)}</span>
                </div>
                <div className="h-1 bg-white/5 rounded-full overflow-hidden">
                  <motion.div 
                    className="h-full bg-indigo-500"
                    animate={{ width: `${indicators.rsi}%` }}
                    transition={{ type: "spring", stiffness: 50 }}
                  />
                </div>
              </div>
              <div className="space-y-2">
                <div className="flex justify-between items-end">
                  <span className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Sentiment</span>
                  <span className="text-[10px] font-black text-white font-mono">{indicators.neuralSentiment.toFixed(1)}%</span>
                </div>
                <div className="h-1 bg-white/5 rounded-full overflow-hidden">
                  <motion.div 
                    className="h-full bg-amber-500"
                    animate={{ width: `${indicators.neuralSentiment}%` }}
                    transition={{ type: "spring", stiffness: 50 }}
                  />
                </div>
              </div>
            </div>

            <div className="mt-10 pt-6 border-t border-white/5">
              <h3 className="text-[9px] font-black text-slate-600 uppercase tracking-[0.3em] mb-4">Trade Inspector</h3>
              {activeFocus ? (
                <div className="space-y-4">
                  <div className="p-4 bg-black/40 rounded-2xl border border-white/5">
                    <div className="flex justify-between items-center mb-1">
                      <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest">Current P/L</span>
                      <span className={`text-xs font-black font-mono ${activeFocus.pnlAbs >= 0 ? 'text-emerald-400' : 'text-rose-400'}`}>
                        {activeFocus.pnlAbs >= 0 ? '+' : ''}${Math.abs(activeFocus.pnlAbs).toLocaleString()}
                      </span>
                    </div>
                  </div>
                  <button 
                    onClick={runTradeAnalysis} 
                    disabled={isAnalyzing}
                    className="w-full py-3 bg-indigo-600/10 hover:bg-indigo-600/20 border border-indigo-500/30 rounded-2xl flex items-center justify-center gap-2 transition-all group"
                  >
                    {isAnalyzing ? <RefreshCcw className="animate-spin text-indigo-400" size={14}/> : <Sparkles className="text-indigo-400 group-hover:scale-110 transition-transform" size={14}/>}
                    <span className="text-[9px] font-black text-indigo-400 uppercase tracking-widest">{isAnalyzing ? 'Syncing' : 'Luthien Insight'}</span>
                  </button>
                  <AnimatePresence>
                    {aiInsight && (
                      <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="p-4 bg-indigo-950/30 rounded-2xl border border-indigo-500/20">
                        <p className="text-[10px] text-indigo-200 font-medium italic leading-relaxed">"{aiInsight}"</p>
                      </motion.div>
                    )}
                  </AnimatePresence>
                </div>
              ) : (
                <div className="flex flex-col items-center justify-center py-10 opacity-20">
                  <Info size={24} className="text-slate-600 mb-2" />
                  <span className="text-[8px] font-black text-slate-600 uppercase tracking-widest">Select position to audit</span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Positions Table */}
      <div className="bg-[#010409]/60 backdrop-blur-xl border border-white/5 rounded-3xl overflow-hidden">
        <div className="px-6 py-4 border-b border-white/5 flex justify-between items-center">
          <div className="flex items-center gap-3">
            <ArrowRightLeft size={16} className="text-indigo-400" />
            <h3 className="text-[10px] font-black text-white uppercase tracking-[0.2em]">Active Nexus Positions</h3>
          </div>
          <div className="flex items-center gap-2">
            <Filter size={12} className="text-slate-600" />
            <span className="text-[8px] font-black text-slate-600 uppercase tracking-widest">Filter: All</span>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="border-b border-white/5">
                {['ID Trade', 'Asset', 'Side', 'Entry', 'Mark', 'P/L %'].map(h => (
                  <th key={h} className="px-6 py-4 text-[9px] font-black text-slate-500 uppercase tracking-widest">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {trades.map(t => {
                const isSelected = selectedTradeId === t.id;
                return (
                  <tr 
                    key={t.id} 
                    onClick={() => { setSelectedTradeId(isSelected ? null : t.id); setAiInsight(null); }}
                    className={`group cursor-pointer transition-colors hover:bg-white/5 ${isSelected ? 'bg-indigo-600/5' : ''}`}
                  >
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        {isSelected && <div className="w-1 h-4 bg-indigo-500 rounded-full" />}
                        <span className={`text-[10px] font-mono font-black ${isSelected ? 'text-indigo-400' : 'text-slate-500'}`}>{t.id}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-[10px] font-black text-white uppercase tracking-widest">{t.symbol}</td>
                    <td className="px-6 py-4">
                      <span className={`px-2 py-0.5 rounded text-[8px] font-black uppercase tracking-widest ${t.side === 'BUY' ? 'bg-emerald-500/10 text-emerald-500 border border-emerald-500/20' : 'bg-rose-500/10 text-rose-500 border border-rose-500/20'}`}>
                        {t.side}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-[10px] font-mono font-black text-slate-400">${t.entryPrice.toLocaleString()}</td>
                    <td className="px-6 py-4 text-[10px] font-mono font-black text-indigo-400">${t.currentPrice.toLocaleString()}</td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <span className={`text-[10px] font-mono font-black ${t.pnlPct >= 0 ? 'text-emerald-400' : 'text-rose-400'}`}>
                          {t.pnlPct > 0 ? '+' : ''}{t.pnlPct}%
                        </span>
                        {isSelected && <ChevronRight size={12} className="text-indigo-500 animate-bounce-x" />}
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
