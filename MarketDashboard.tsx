
import React from 'react';
import { 
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, 
} from 'recharts';
import { MarketData, TradeSignal } from '../types';
import { TrendingUp, TrendingDown, Zap, Activity, Brain, ShieldCheck } from 'lucide-react';

interface MarketDashboardProps {
  data: MarketData[];
  signals: TradeSignal[];
}

const MarketDashboard: React.FC<MarketDashboardProps> = ({ data, signals }) => {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-full">
      {/* Main Chart Section */}
      <div className="lg:col-span-2 glass-panel p-6 rounded-2xl flex flex-col border border-white/10 relative overflow-hidden">
        <div className="flex justify-between items-center mb-6 relative z-10">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-cyan-500/10 rounded-lg text-cyan-400">
              <Activity size={20} />
            </div>
            <div>
              <h3 className="font-bold text-lg leading-tight">Terminal de Fluxo Fractal</h3>
              <p className="text-xs text-slate-400">Par: BTC/USD | TF: 1m | Agregado VHALINOR 5.0</p>
            </div>
          </div>
          <div className="text-right">
            <p className="text-2xl font-black text-cyan-400 glow-text-cyan tabular-nums">
              ${data[data.length - 1]?.price.toLocaleString(undefined, { minimumFractionDigits: 2 })}
            </p>
            <p className="text-[10px] text-green-400 flex items-center justify-end gap-1">
              <TrendingUp size={10} /> +2.41% HOJE
            </p>
          </div>
        </div>

        <div className="flex-1 min-h-[350px] w-full relative z-10">
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={data}>
              <defs>
                <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#06b6d4" stopOpacity={0.3}/>
                  <stop offset="95%" stopColor="#06b6d4" stopOpacity={0}/>
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" vertical={false} />
              <XAxis 
                dataKey="time" 
                stroke="rgba(255,255,255,0.3)" 
                fontSize={10} 
                tickLine={false}
                axisLine={false}
              />
              <YAxis 
                stroke="rgba(255,255,255,0.3)" 
                fontSize={10} 
                tickLine={false}
                axisLine={false}
                domain={['auto', 'auto']}
                tickFormatter={(val) => `$${val.toLocaleString()}`}
              />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: '#0f172a', 
                  borderColor: 'rgba(255,255,255,0.1)', 
                  color: '#fff',
                  borderRadius: '12px',
                  fontSize: '12px'
                }} 
              />
              <Area 
                type="monotone" 
                dataKey="price" 
                stroke="#06b6d4" 
                fillOpacity={1} 
                fill="url(#colorPrice)" 
                strokeWidth={2}
                animationDuration={500}
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Signals & Operations Section */}
      <div className="lg:col-span-1 glass-panel p-6 rounded-2xl flex flex-col border border-white/10 overflow-hidden">
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-purple-500/10 rounded-lg text-purple-400">
              <Zap size={20} />
            </div>
            <h3 className="font-bold text-lg uppercase tracking-tight">Decisões SYLPF</h3>
          </div>
          <div className="flex items-center gap-1 text-[8px] font-bold text-cyan-500 bg-cyan-500/10 px-2 py-0.5 rounded border border-cyan-500/20">
            <Brain size={10} /> SENC_MOD ACTIVE
          </div>
        </div>

        <div className="flex-1 overflow-y-auto space-y-4 pr-2 scrollbar-hide">
          {signals.map((signal) => (
            <div key={signal.id} className="p-4 rounded-xl bg-white/5 border border-white/5 hover:border-cyan-500/30 transition-all group relative overflow-hidden">
              <div className="absolute top-0 right-0 w-16 h-16 bg-gradient-to-br from-cyan-500/5 to-transparent pointer-events-none"></div>
              
              <div className="flex justify-between items-start mb-2 relative z-10">
                <span className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                  signal.type === 'BUY' ? 'bg-green-500/20 text-green-400 border border-green-500/30' : 'bg-red-500/20 text-red-400 border border-red-500/30'
                }`}>
                  {signal.type} ENTRY
                </span>
                <span className="text-[10px] text-slate-500 font-mono">{signal.timestamp}</span>
              </div>
              
              <div className="flex justify-between items-center mb-3 relative z-10">
                <div>
                  <p className="font-bold text-sm text-slate-100">{signal.asset}</p>
                  <p className="text-xs text-slate-400 font-mono">${signal.price.toLocaleString()}</p>
                </div>
                <div className="text-right">
                  <div className="flex items-center gap-1 justify-end">
                    <ShieldCheck size={12} className="text-cyan-400" />
                    <p className="text-xs font-bold text-cyan-400">{Math.round(signal.confidence * 100)}%</p>
                  </div>
                  <div className="w-16 h-1 bg-white/10 rounded-full mt-1 overflow-hidden ml-auto">
                    <div className="h-full bg-cyan-500" style={{ width: `${signal.confidence * 100}%` }}></div>
                  </div>
                </div>
              </div>

              {signal.analysis && (
                <div className="mt-3 pt-3 border-t border-white/10">
                  <div className="flex items-center gap-1.5 mb-1.5">
                    <Brain size={10} className="text-purple-400" />
                    <span className="text-[8px] font-bold text-slate-500 uppercase tracking-widest">Análise VHALINOR</span>
                  </div>
                  <p className="text-[10px] text-slate-300 italic leading-relaxed">
                    "{signal.analysis}"
                  </p>
                  <div className="flex gap-3 mt-2">
                    <div className="text-[8px] font-mono text-cyan-500/70">ENTROPIA: {(signal.entropyImpact || 0).toFixed(3)}</div>
                    <div className="text-[8px] font-mono text-purple-500/70">FITNESS: {((signal.geneticFitness || 0) * 100).toFixed(1)}%</div>
                  </div>
                </div>
              )}
            </div>
          ))}
          
          {signals.length === 0 && (
            <div className="h-full flex flex-col items-center justify-center opacity-40 py-10">
              <Activity className="animate-pulse mb-4 text-cyan-500" size={48} />
              <p className="text-sm font-display tracking-widest uppercase">Escaneando Divergências Fractals...</p>
            </div>
          )}
        </div>

        <div className="mt-6 pt-4 border-t border-white/10">
          <div className="flex justify-between items-center mb-2">
            <span className="text-xs text-slate-400 uppercase tracking-tighter">Lucro Acumulado VHALINOR</span>
            <span className="text-xs font-black text-green-400 glow-text-cyan">+$1,240.50</span>
          </div>
          <div className="flex justify-between items-center">
            <span className="text-xs text-slate-400 uppercase tracking-tighter">Probabilidade Preditiva SYLPF</span>
            <span className="text-xs font-black text-cyan-400">88.4%</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MarketDashboard;
