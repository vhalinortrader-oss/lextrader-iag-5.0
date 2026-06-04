
import React from 'react';
import { IndicatorStatus } from '../types';
import { Gauge, ArrowUp, ArrowDown, Minus } from 'lucide-react';

interface TechnicalMatrixProps {
  indicators: IndicatorStatus[];
}

const TechnicalMatrix: React.FC<TechnicalMatrixProps> = ({ indicators }) => {
  return (
    <div className="glass-panel rounded-2xl p-5 border border-white/10 flex flex-col gap-4">
      <div className="flex items-center justify-between mb-2">
        <h4 className="text-[10px] font-bold uppercase text-slate-500 tracking-widest flex items-center gap-2">
          <Gauge size={12} className="text-cyan-400" /> MATRIZ TÉCNICA TRADINGVIEW (10X)
        </h4>
        <div className="text-[8px] bg-slate-800 px-2 py-0.5 rounded text-slate-400">REAL-TIME SYNC</div>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-5 gap-3">
        {indicators.map((ind, idx) => (
          <div key={idx} className="p-3 rounded-xl bg-white/5 border border-white/5 flex flex-col gap-1 transition-all hover:bg-white/10">
            <span className="text-[8px] font-bold text-slate-500 uppercase truncate">{ind.label}</span>
            <div className="flex items-center justify-between">
              <span className={`text-xs font-black font-mono ${
                ind.status === 'BULLISH' ? 'text-green-400' : 
                ind.status === 'BEARISH' ? 'text-red-400' : 'text-cyan-400'
              }`}>
                {ind.value}
              </span>
              {ind.status === 'BULLISH' && <ArrowUp size={10} className="text-green-500" />}
              {ind.status === 'BEARISH' && <ArrowDown size={10} className="text-red-500" />}
              {ind.status === 'NEUTRAL' && <Minus size={10} className="text-slate-500" />}
            </div>
            <div className="mt-1 flex gap-0.5">
              {[...Array(5)].map((_, b) => (
                <div 
                  key={b} 
                  className={`h-0.5 flex-1 rounded-full ${
                    ind.status === 'BULLISH' && b < 4 ? 'bg-green-500/50' :
                    ind.status === 'BEARISH' && b < 4 ? 'bg-red-500/50' :
                    ind.status === 'NEUTRAL' && b < 2 ? 'bg-cyan-500/50' : 'bg-white/5'
                  }`}
                />
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TechnicalMatrix;
