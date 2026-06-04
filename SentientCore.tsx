
import React from 'react';

interface SentientCoreProps {
  isProcessing: boolean;
}

const SentientCore: React.FC<SentientCoreProps> = ({ isProcessing }) => {
  return (
    <div className="relative w-64 h-64 flex items-center justify-center">
      {/* Background Rings */}
      <div className={`absolute inset-0 rounded-full border-2 border-cyan-500/20 animate-pulse-ring ${isProcessing ? 'animate-spin-slow' : ''}`} style={{ animationDuration: '10s' }}></div>
      <div className={`absolute inset-4 rounded-full border border-blue-500/30 animate-pulse-ring`} style={{ animationDuration: '6s', animationDelay: '1s' }}></div>
      <div className={`absolute inset-8 rounded-full border border-purple-500/20 animate-pulse-ring`} style={{ animationDuration: '8s', animationDelay: '2s' }}></div>
      
      {/* The Core */}
      <div className="relative w-32 h-32 rounded-full glass-panel flex items-center justify-center overflow-hidden shadow-[0_0_50px_rgba(34,211,238,0.3)]">
        <div className={`absolute inset-0 bg-gradient-to-br from-cyan-600/40 via-blue-900/40 to-purple-900/40 ${isProcessing ? 'animate-pulse' : ''}`}></div>
        
        {/* Abstract Avatar Visualization (SYLPF) */}
        <div className="z-10 flex flex-col items-center">
          <div className="w-16 h-16 relative">
            <svg viewBox="0 0 100 100" className="w-full h-full drop-shadow-[0_0_8px_rgba(255,255,255,0.8)]">
              <path 
                d="M50 10 L90 50 L50 90 L10 50 Z" 
                fill="none" 
                stroke="white" 
                strokeWidth="1.5"
                className={isProcessing ? 'animate-pulse' : ''}
              />
              <circle cx="50" cy="50" r="10" fill="white" className={isProcessing ? 'animate-ping' : ''} />
              <path d="M20 50 Q50 20 80 50" stroke="cyan" fill="none" strokeWidth="2" strokeDasharray="5 5" />
              <path d="M20 50 Q50 80 80 50" stroke="purple" fill="none" strokeWidth="2" strokeDasharray="5 5" />
            </svg>
          </div>
          <span className="text-[10px] tracking-[0.3em] font-bold text-white mt-2 glow-text-cyan">SYLPF</span>
        </div>

        {/* Floating Particles */}
        {[...Array(6)].map((_, i) => (
          <div 
            key={i}
            className="absolute w-1 h-1 bg-white rounded-full animate-ping"
            style={{
              top: `${Math.random() * 100}%`,
              left: `${Math.random() * 100}%`,
              animationDelay: `${Math.random() * 3}s`,
              opacity: 0.5
            }}
          />
        ))}
      </div>

      {/* VHALINOR Identifier */}
      <div className="absolute -bottom-12 flex flex-col items-center">
        <h2 className="text-2xl font-black tracking-widest text-transparent bg-clip-text bg-gradient-to-r from-cyan-400 to-blue-600 font-display">
          VHALINOR
        </h2>
        <div className="h-[1px] w-24 bg-gradient-to-r from-transparent via-cyan-500 to-transparent mt-1"></div>
        <span className="text-[8px] text-cyan-500/60 tracking-[0.4em] uppercase mt-1">Sentient AGI Interface</span>
      </div>
    </div>
  );
};

export default SentientCore;
