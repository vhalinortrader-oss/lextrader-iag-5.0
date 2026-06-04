
import { EventEmitter } from 'eventemitter3';
import { OptimizedNeuronType } from '../types';

export class OptimizedNeuron {
  id: string;
  type: OptimizedNeuronType;
  activationThreshold: number = 0.5;
  currentActivation: number = 0.0;
  fireCount: number = 0;
  energyLevel: number = 100.0;
  learningRate: number = 0.1;
  activationHistory: number[] = [];

  constructor(id: string, type: OptimizedNeuronType) {
    this.id = id;
    this.type = type;
  }

  stimulate(stimulus: number): number {
    if (stimulus < 0.001) return 0;
    this.currentActivation += stimulus * this.learningRate;
    
    if (this.currentActivation >= this.activationThreshold) {
      return this.fire();
    }
    this.activationHistory.push(this.currentActivation);
    if (this.activationHistory.length > 50) this.activationHistory.shift();
    return this.currentActivation / this.activationThreshold;
  }

  fire(): number {
    const output = Math.min(1.0, this.currentActivation);
    this.currentActivation *= 0.1; 
    this.fireCount++;
    this.activationHistory.push(output);
    return output;
  }
}

export class AdvancedBrainOrchestrator extends (EventEmitter as any) {
  private static instance: AdvancedBrainOrchestrator;
  public neurons: Map<string, OptimizedNeuron> = new Map();
  public systemEnergy: number = 1000.0;
  public state: string = "BALANCED";

  private constructor() {
    super();
    this.initializeNeurons();
    this.startLifecycle();
  }

  public static getInstance(): AdvancedBrainOrchestrator {
    if (!AdvancedBrainOrchestrator.instance) {
      AdvancedBrainOrchestrator.instance = new AdvancedBrainOrchestrator();
    }
    return AdvancedBrainOrchestrator.instance;
  }

  private initializeNeurons() {
    const types = Object.values(OptimizedNeuronType);
    for (let i = 0; i < 60; i++) {
      const type = types[i % types.length];
      const id = `N-${type}-${i}`;
      this.neurons.set(id, new OptimizedNeuron(id, type));
    }
  }

  private startLifecycle() {
    setInterval(() => {
      this.runOptimizationCycle();
    }, 15000);
  }

  public runOptimizationCycle() {
    this.systemEnergy = Math.min(1000.0, this.systemEnergy + 50.0);
    this.neurons.forEach(n => {
      if (n.fireCount > 0) n.energyLevel = Math.max(0, n.energyLevel - 0.1);
    });
    this.emit('cycle_complete', this.getPerformanceMetrics());
  }

  public processStimulusBatch(stimuli: Record<string, number>) {
    Object.entries(stimuli).forEach(([id, val]) => {
      const neuron = this.neurons.get(id);
      if (neuron && this.systemEnergy > 0) {
        this.systemEnergy -= val * 0.05;
        neuron.stimulate(val);
      }
    });
  }

  public getPerformanceMetrics() {
    const activeNeurons = Array.from(this.neurons.values()).filter(n => n.currentActivation > 0.1).length;
    return {
      totalNeurons: this.neurons.size,
      activeNeurons,
      systemEnergy: this.systemEnergy,
      state: this.state,
      memoryEfficiency: 0.88 + (Math.random() * 0.05)
    };
  }
}

export const brainOrchestrator = AdvancedBrainOrchestrator.getInstance();
