
import React, { useState, useEffect, useMemo } from 'react';
import { Box, Typography, Paper, Grid, Chip, Button, CircularProgress, LinearProgress, IconButton, Divider, Select, MenuItem, FormControl } from '@mui/material';
import { motion as m, AnimatePresence } from 'framer-motion';
import { 
  Brain, Zap, Shield, Radio, Activity, 
  Network, Binary, ShieldCheck, Shapes, Layers, 
  Terminal, Cpu as CpuIcon, Share2, Info, Rocket,
  ListChecks, AlertCircle, Atom, Sparkles, GitBranch, GraduationCap,
  Eye
} from 'lucide-react';
import { NeuralNode, TaskPriority, TradingDirective } from '../types';
import { getNeuralSystemAudit } from '../services/gemini';
import { autonomousModuleService, AutonomousMetrics } from '../services/AutonomousModuleService';

// Injeção de dados dos serviços
import { cpuLibrariesService } from '../services/CPULibrariesService';
import { neuroplasticityService } from '../services/NeuroplasticityService';
import { evolutionCortexService } from '../services/EvolutionCortexService';
import { onicientService } from '../services/OnicientService';

const motion = m as any;
const MotionLine = motion.line;
const MotionCircle = motion.circle;
const MotionDiv = motion.div;

export const NeuralMindMap: React.FC = () => {
  const [selectedNode, setSelectedNode] = useState<string | null>('l5_onicient');
  const [hoveredNode, setHoveredNode] = useState<string | null>(null);
  const [isAuditing, setIsAuditing] = useState(false);
  const [auditText, setAuditText] = useState<string | null>(null);
  const [priorityFilter, setPriorityFilter] = useState<TaskPriority | 'ALL'>('ALL');
  const [tasks, setTasks] = useState<TradingDirective[]>([]);

  useEffect(() => {
    const interval = setInterval(() => {
      const telemetry = autonomousModuleService.getTelemetry();
      setTasks(telemetry.taskQueue);
    }, 2000);
    return () => clearInterval(interval);
  }, []);

  const nodes: NeuralNode[] = useMemo(() => [
    // TIER 1: SENSORIAL
    { id: 'l1_tick', label: 'Tick Stream', type: 'INPUT', status: 'ACTIVE', load: 94, x: 8, y: 15, description: 'Ingestão de dados em microsegundos.' },
    { id: 'l1_news', label: 'News NLP', type: 'INPUT', status: 'ACTIVE', load: 35, x: 8, y: 40, description: 'Processamento de linguagem natural macro.' },
    { id: 'l1_oracles', label: 'DeFi Oracles', type: 'INPUT', status: 'ACTIVE', load: 22, x: 8, y: 65, description: 'Sincronização on-chain.' },
    { id: 'l1_social', label: 'Social Intel', type: 'INPUT', status: 'ACTIVE', load: 15, x: 8, y: 85, description: 'Análise de sentimento social.' },

    // TIER 2: INFRAESTRUTURA
    { id: 'l2_libs', label: 'Kernels', type: 'LOGIC', status: 'ACTIVE', load: 28, x: 25, y: 30, description: 'Bibliotecas TF, CCXT e TA-Lib.' },
    { id: 'l2_cpu', label: 'Neural CPU', type: 'LOGIC', status: 'ACTIVE', load: 68, x: 25, y: 55, description: 'Processamento paralelo multi-threaded.' },
    { id: 'l2_memory', label: 'Synaptic RAM', type: 'MEMORY', status: 'ACTIVE', load: 52, x: 25, y: 80, description: 'Memória volátil de alta velocidade.' },

    // TIER 3: COGNIÇÃO
    { id: 'l4_logic', label: 'Logical Core', type: 'LOGIC', status: 'ACTIVE', load: 45, x: 50, y: 25, description: 'Raciocínio dedutivo estratégico.' },
    { id: 'l4_sentient', label: 'Sentient Hub', type: 'LOGIC', status: 'ACTIVE', load: 58, x: 50, y: 55, description: 'Núcleo de senciência e intuição.' },
    { id: 'l4_risk_eval', label: 'Risk Eval', type: 'LOGIC', status: 'ACTIVE', load: 32, x: 50, y: 85, description: 'Avaliação de risco em tempo real.' },

    // TIER 4: INTEGRAÇÃO & EVOLUÇÃO
    { id: 'l5_autonomous', label: 'Autonomous', type: 'LOGIC', status: 'ACTIVE', load: 74, x: 75, y: 15, description: 'Motor de execução e despacho autônomo.' },
    { id: 'l5_omnicortex', label: 'Omnicortex', type: 'LOGIC', status: 'ACTIVE', load: 88, x: 75, y: 35, description: 'Sincronizador global de camadas cognitivas.' },
    { id: 'l5_evolution_cortex', label: 'Evolution', type: 'LOGIC', status: 'ACTIVE', load: 42, x: 75, y: 55, description: 'Córtex de evolução de estratégias.' },
    { id: 'l5_learning', label: 'Learning', type: 'LOGIC', status: 'PROCESSING', load: 92, x: 75, y: 75, description: 'Retreinamento contínuo de modelos.' },
    { id: 'l5_plasticity', label: 'Plasticity', type: 'LOGIC', status: 'ACTIVE', load: 38, x: 75, y: 90, description: 'Ajuste dinâmico da topologia neural.' },

    // NOVO - TIER SUPREMO: ONICIENTE
    { id: 'l5_onicient', label: 'Onicient Core', type: 'LOGIC', status: 'ACTIVE', load: 12, x: 85, y: 50, description: 'Supervisor global com visão total sobre o fluxo sistêmico e entropia.' },

    // TIER 5: OUTPUT
    { id: 'l6_executor', label: 'Executor', type: 'OUTPUT', status: 'ACTIVE', load: 18, x: 92, y: 50, description: 'Interface final de execução de ordens.' }
  ], []);

  const connections = useMemo(() => [
    { from: 'l1_tick', to: 'l2_cpu' }, { from: 'l1_news', to: 'l2_libs' },
    { from: 'l1_oracles', to: 'l2_libs' }, { from: 'l1_social', to: 'l2_cpu' },
    { from: 'l2_libs', to: 'l2_cpu' }, { from: 'l2_cpu', to: 'l2_memory' },
    { from: 'l2_cpu', to: 'l4_logic' }, { from: 'l2_cpu', to: 'l4_sentient' },
    { from: 'l2_memory', to: 'l4_risk_eval' },
    { from: 'l4_logic', to: 'l5_omnicortex' }, { from: 'l4_sentient', to: 'l5_omnicortex' },
    { from: 'l4_risk_eval', to: 'l5_autonomous' },
    { from: 'l5_omnicortex', to: 'l5_onicient' },
    { from: 'l5_evolution_cortex', to: 'l5_onicient' },
    { from: 'l5_onicient', to: 'l6_executor' },
    { from: 'l5_learning', to: 'l2_cpu' },
    { from: 'l5_onicient', to: 'l5_omnicortex' } // Feedback loop
  ], []);

  const activeNode = useMemo(() => nodes.find(n => n.id === selectedNode), [selectedNode, nodes]);

  const isConnectionHighlighted = (from: string, to: string) => {
    const target = selectedNode || hoveredNode;
    if (!target) return false;
    return from === target || to === target;
  };

  const handleAudit = async () => {
    setIsAuditing(true);
    setAuditText(null);
    try {
        const result = await getNeuralSystemAudit(nodes);
        setAuditText(result || "Sistema operando em harmonia evolutiva.");
    } finally {
        setIsAuditing(false);
    }
  };

  return (
    <Box sx={{ p: 0, height: '100%', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <header className="flex justify-between items-center bg-[#0d1117] p-6 rounded-[2.5rem] border border-white/5 shadow-2xl">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-indigo-500/10 rounded-2xl border border-indigo-500/20">
             <Share2 size={28} className="text-indigo-400" />
          </div>
          <div>
            <Typography variant="h5" sx={{ fontWeight: 900, color: 'white', letterSpacing: -1 }}>
                Neural <span className="text-indigo-500">Mind Map</span> 4.8
            </Typography>
            <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', letterSpacing: 2 }}>
                ONICIENT & OMNISYNC TOPOLOGY
            </Typography>
          </div>
        </div>
        <Button 
            variant="outlined" 
            onClick={handleAudit}
            disabled={isAuditing}
            startIcon={isAuditing ? <CircularProgress size={14} color="inherit" /> : <Shield size={16}/>}
            sx={{ borderColor: 'rgba(99, 102, 241, 0.3)', color: '#818cf8', fontWeight: 'black', borderRadius: 3, px: 3 }}
        >
            System Audit
        </Button>
      </header>

      <Grid container spacing={3} sx={{ flex: 1 }}>
        <Grid item xs={12} lg={8}>
          <Paper sx={{ 
            p: 0, bgcolor: '#010409', border: '1px solid #1e293b', 
            borderRadius: 10, height: '780px', position: 'relative', overflow: 'hidden',
            boxShadow: 'inset 0 0 100px rgba(0,0,0,0.8)'
          }}>
            <div className="absolute inset-0 opacity-[0.03] pointer-events-none" 
                 style={{ backgroundImage: 'radial-gradient(#6366f1 1px, transparent 1px)', backgroundSize: '40px 40px' }} />

            <svg viewBox="0 0 100 100" style={{ width: '100%', height: '100%' }}>
              {connections.map((conn, idx) => {
                const from = nodes.find(n => n.id === conn.from)!;
                const to = nodes.find(n => n.id === conn.to)!;
                const isHighlighted = isConnectionHighlighted(conn.from, conn.to);
                
                return (
                  <g key={`conn-${idx}`}>
                    <MotionLine
                      x1={from.x} y1={from.y} x2={to.x} y2={to.y}
                      stroke={isHighlighted ? "#6366f1" : "rgba(30, 41, 59, 0.3)"}
                      strokeWidth={isHighlighted ? "0.6" : "0.2"}
                      style={{ transition: 'all 0.3s ease' }}
                    />
                    <MotionCircle
                      r={isHighlighted ? "0.4" : "0.25"}
                      fill={isHighlighted ? "#fff" : "#6366f1"}
                      animate={{ cx: [from.x, to.x], cy: [from.y, to.y], opacity: [0, 1, 1, 0] }}
                      transition={{ repeat: Infinity, duration: 4, ease: "linear", delay: Math.random() * 5 }}
                    />
                  </g>
                );
              })}

              {nodes.map(node => (
                <g 
                  key={node.id} 
                  style={{ cursor: 'pointer' }} 
                  onMouseEnter={() => setHoveredNode(node.id)}
                  onMouseLeave={() => setHoveredNode(null)}
                  onClick={() => { setSelectedNode(node.id); setAuditText(null); }}
                >
                  <MotionCircle
                    cx={node.x} cy={node.y} r="2.5"
                    fill={selectedNode === node.id ? "#6366f1" : "#0d1117"}
                    stroke={selectedNode === node.id ? '#818cf8' : (hoveredNode === node.id ? '#6366f1' : '#1e293b')}
                    strokeWidth="0.6"
                    whileHover={{ r: 3.2 }}
                  />
                  {node.id === 'l5_onicient' && (
                    <MotionCircle 
                        cx={node.x} cy={node.y} r="4" 
                        fill="none" stroke="#818cf8" strokeWidth="0.1" 
                        animate={{ opacity: [0.1, 0.4, 0.1], scale: [1, 1.2, 1] }} 
                        transition={{ repeat: Infinity, duration: 3 }} 
                    />
                  )}
                  <text 
                    x={node.x} y={node.y + 6.5} 
                    textAnchor="middle" 
                    fill={selectedNode === node.id ? "white" : "#475569"} 
                    style={{ fontSize: '1.6px', fontWeight: '900', textTransform: 'uppercase', pointerEvents: 'none', letterSpacing: '0.1px' }}
                  >
                    {node.label}
                  </text>
                </g>
              ))}
            </svg>
            
            <AnimatePresence>
              {auditText && (
                <MotionDiv 
                  initial={{ opacity: 0, y: 50 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0 }}
                  className="absolute bottom-6 left-6 right-6 p-8 bg-indigo-950/90 backdrop-blur-3xl border border-indigo-500/30 rounded-[3rem] shadow-2xl z-50"
                >
                  <div className="flex justify-between items-center mb-4">
                     <div className="flex items-center gap-3">
                        <Terminal size={20} className="text-indigo-400" />
                        <Typography variant="overline" sx={{ color: 'white', fontWeight: 900, letterSpacing: 3 }}>NEURAL AUDIT REPORT</Typography>
                     </div>
                     <IconButton size="small" onClick={() => setAuditText(null)} sx={{ color: '#94a3b8' }}><Info size={18} /></IconButton>
                  </div>
                  <Divider sx={{ mb: 3, bgcolor: 'white/10' }} />
                  <Typography variant="body2" sx={{ color: '#e2e8f0', fontStyle: 'italic', fontSize: '0.85rem', lineHeight: 1.8, fontFamily: 'JetBrains Mono' }}>
                    {auditText}
                  </Typography>
                </MotionDiv>
              )}
            </AnimatePresence>
          </Paper>
        </Grid>

        <Grid item xs={12} lg={4}>
          <Paper sx={{ p: 4, bgcolor: '#0d1117', border: '1px solid #1e293b', borderRadius: 10, height: '100%', display: 'flex', flexDirection: 'column', gap: 3 }}>
            {!activeNode ? (
              <Box className="flex flex-col items-center justify-center h-full text-center opacity-20">
                <Network size={100} className="mb-6 text-slate-500" />
                <Typography variant="h6" fontWeight="900" sx={{ textTransform: 'uppercase', letterSpacing: 2 }}>Select a Node</Typography>
                <Typography variant="caption">Para ver telemetria e tarefas em tempo real</Typography>
              </Box>
            ) : (
              <MotionDiv initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} key={activeNode.id} className="h-full flex flex-col gap-4">
                <div className="flex items-center gap-4">
                  <div className="p-4 bg-indigo-500/10 rounded-3xl border border-indigo-500/20 shadow-inner">
                    <NodeIcon id={activeNode.id} type={activeNode.type} />
                  </div>
                  <div>
                    <Typography variant="h6" sx={{ color: 'white', fontWeight: 900, fontSize: '15px', letterSpacing: -0.5 }}>{activeNode.label}</Typography>
                    <Chip label={activeNode.status} size="small" color="success" sx={{ height: 18, fontSize: '8px', fontWeight: '900', borderRadius: 1 }} />
                  </div>
                </div>

                <Box>
                  <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black', textTransform: 'uppercase', mb: 1, display: 'block', letterSpacing: 1 }}>Module Role</Typography>
                  <Typography variant="body2" sx={{ color: '#94a3b8', fontSize: '12px', lineHeight: 1.5 }}>{activeNode.description}</Typography>
                </Box>

                <Box>
                  <div className="flex justify-between mb-2">
                     <Typography variant="caption" sx={{ color: '#475569', fontWeight: 'black' }}>Operational Load</Typography>
                     <Typography variant="caption" sx={{ color: activeNode.load > 85 ? '#ef4444' : '#6366f1', fontWeight: 'black' }}>{activeNode.load}%</Typography>
                  </div>
                  <LinearProgress 
                      variant="determinate" 
                      value={activeNode.load} 
                      sx={{ height: 6, borderRadius: 3, bgcolor: 'rgba(255,255,255,0.05)', '& .MuiLinearProgress-bar': { bgcolor: activeNode.load > 85 ? '#ef4444' : '#6366f1', borderRadius: 3 } }} 
                  />
                </Box>

                <Divider sx={{ bgcolor: 'rgba(255,255,255,0.05)' }} />

                <Box sx={{ p: 2, bgcolor: 'rgba(255,255,255,0.02)', borderRadius: 4, border: '1px solid rgba(255,255,255,0.05)' }}>
                    <Typography variant="caption" sx={{ color: '#6366f1', fontWeight: 900, mb: 2, display: 'block' }}>REAL-TIME TELEMETRY</Typography>
                    <div className="space-y-3">
                        <ServiceSpecificData id={activeNode.id} />
                    </div>
                </Box>

                <Divider sx={{ bgcolor: 'rgba(255,255,255,0.05)' }} />

                <Box sx={{ flex: 1, overflowY: 'auto' }} className="scrollbar-hide">
                    <div className="flex justify-between items-center mb-3">
                        <Typography variant="caption" sx={{ color: '#475569', fontWeight: 900, textTransform: 'uppercase', letterSpacing: 1 }}>Neural Task Queue</Typography>
                        <ListChecks size={14} className="text-slate-600" />
                    </div>
                    
                    <div className="space-y-2">
                        {tasks.length === 0 ? (
                            <Typography sx={{ fontSize: '10px', color: '#475569', fontStyle: 'italic', textAlign: 'center', py: 4 }}>
                                Nenhuma diretriz ativa no momento...
                            </Typography>
                        ) : (
                            tasks.slice(0, 10).map((task, idx) => (
                                <m.div 
                                    key={`${task.symbol}-${idx}`}
                                    initial={{ opacity: 0, y: 10 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    className="p-3 bg-black/40 rounded-2xl border border-white/5 flex justify-between items-center group hover:border-indigo-500/30 transition-colors"
                                >
                                    <div className="flex flex-col">
                                        <div className="flex items-center gap-2">
                                            <span className="text-[10px] font-black text-white uppercase">{task.symbol}</span>
                                            <PriorityChip priority={task.priority || TaskPriority.LOW} />
                                        </div>
                                        <span className="text-[8px] font-black text-slate-500 uppercase tracking-widest mt-0.5">
                                            {task.side} @ ${task.entryPrice.toLocaleString()}
                                        </span>
                                    </div>
                                    <div className="text-right">
                                        <span className="text-[9px] font-black text-indigo-400 font-mono">
                                            {(task.confidence * 100).toFixed(0)}% CONF
                                        </span>
                                    </div>
                                </m.div>
                            ))
                        )}
                    </div>
                </Box>

                <div className="mt-auto pt-4 border-t border-white/5">
                   <div className="flex items-center gap-2 text-slate-600">
                      <Zap size={14} />
                      <Typography sx={{ fontSize: '10px', fontWeight: 'bold' }}>Neural Latency: 1.2ms (Ultrafast)</Typography>
                   </div>
                </div>
              </MotionDiv>
            )}
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

const PriorityChip = ({ priority }: { priority: TaskPriority }) => {
    const config = {
        [TaskPriority.LOW]: { color: '#94a3b8', label: 'BAIXA' },
        [TaskPriority.MEDIUM]: { color: '#f59e0b', label: 'MÉDIA' },
        [TaskPriority.HIGH]: { color: '#ef4444', label: 'ALTA' },
        [TaskPriority.CRITICAL]: { color: '#7c3aed', label: 'CRÍTICA' }
    };

    const { color, label } = config[priority] || config[TaskPriority.LOW];

    return (
        <span 
            style={{ 
                backgroundColor: `${color}15`, 
                color: color,
                borderColor: `${color}30`
            }}
            className="px-1.5 py-0.5 rounded text-[7px] font-black uppercase tracking-widest border"
        >
            {label}
        </span>
    );
};

const ServiceSpecificData = ({ id }: { id: string }) => {
    if (id === 'l5_onicient') {
        const data = onicientService.getGlobalStatus();
        return (
            <>
                <DataRow label="Oversight Integrity" value={`${(data.globalOversight * 100).toFixed(2)}%`} />
                <DataRow label="Systemic Entropy" value={`${(data.entropyLevel * 100).toFixed(3)}%`} />
                <DataRow label="Coherence" value={`${(data.systemicCoherence * 100).toFixed(2)}%`} />
                <DataRow label="Anomaly Risk" value={`${(data.anomalyProbability * 100).toFixed(3)}%`} />
            </>
        );
    }
    if (id === 'l2_libs') {
        const data = cpuLibrariesService.getLibraryEnvironment();
        return (
            <>
                <DataRow label="Runtime" value={data.runtime} />
                <DataRow label="Integrity" value={`${(data.integrity * 100).toFixed(1)}%`} />
                <DataRow label="Active Libs" value={data.activeLibs.length.toString()} />
            </>
        );
    }
    if (id === 'l5_evolution_cortex') {
        const data = evolutionCortexService.getTelemetry();
        return (
            <>
                <DataRow label="Mutation Rate" value={`${(data.mutationRate * 100).toFixed(1)}%`} />
                <DataRow label="Diversity" value={`${(data.populationDiversity * 100).toFixed(1)}%`} />
                <DataRow label="Gen Leap" value={data.generationalLeap} />
            </>
        );
    }
    if (id === 'l5_autonomous') {
        const data = autonomousModuleService.getTelemetry();
        return (
            <>
                <DataRow label="Decision Latency" value={data.decisionLatency} />
                <DataRow label="Confidence" value={`${(data.executionConfidence * 100).toFixed(1)}%`} />
                <DataRow label="Success Rate" value={data.successRate} />
            </>
        );
    }
    return <Typography sx={{ fontSize: '10px', color: '#475569', fontStyle: 'italic' }}>Aguardando telemetria...</Typography>;
};

const DataRow = ({ label, value }: { label: string, value: string }) => (
    <div className="flex justify-between items-center">
        <span className="text-[10px] text-slate-500 font-bold uppercase">{label}</span>
        <span className="text-[10px] text-white font-mono font-black">{value}</span>
    </div>
);

const NodeIcon = ({ id, type }: any) => {
  if (id === 'l5_onicient') return <Eye className="text-white" size={24} />;
  if (id.startsWith('l1')) return <Radio className="text-blue-400" size={24} />;
  if (id === 'l2_cpu') return <CpuIcon className="text-indigo-400" size={24} />;
  if (id === 'l2_libs') return <Layers className="text-blue-400" size={24} />;
  if (id === 'l5_autonomous') return <Rocket className="text-orange-400" size={24} />;
  if (id === 'l5_omnicortex') return <Activity className="text-indigo-400" size={24} />;
  if (id === 'l5_evolution_cortex') return <Sparkles className="text-yellow-400" size={24} />;
  if (id === 'l5_learning') return <GraduationCap className="text-emerald-400" size={24} />;
  if (id === 'l5_plasticity') return <GitBranch className="text-pink-400" size={24} />;
  
  switch (type) {
    case 'INPUT': return <Radio className="text-blue-400" size={24} />;
    case 'MEMORY': return <Shapes className="text-purple-400" size={24} />;
    case 'LOGIC': return <Binary className="text-indigo-400" size={24} />;
    case 'OUTPUT': return <ShieldCheck className="text-emerald-400" size={24} />;
    default: return <Activity className="text-slate-400" size={24} />;
  }
};
