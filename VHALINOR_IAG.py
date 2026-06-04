"""
VHALINOR.IAG 5.0 - Sistema Cerebral Artificial Avançado Ultra-Otimizado
=======================================================================
Versão 5.0 - Expansão máxima em um único arquivo
Data: Fevereiro 2026
"""

import os
import sys
import asyncio
import threading
import time
import random
import json
import pickle
import hashlib
import logging
import warnings
import gc
from datetime import datetime
from dataclasses import dataclass, field
from typing import Dict, List, Any, Optional, Set, Tuple, DefaultDict, Deque
from collections import deque, defaultdict, OrderedDict
from enum import Enum, auto
from pathlib import Path

import numpy as np
import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure

import tkinter as tk
from tkinter import ttk, scrolledtext, messagebox

# Bibliotecas de ML
from sklearn.ensemble import IsolationForest, RandomForestRegressor
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
from sklearn.cluster import MiniBatchKMeans

warnings.filterwarnings('ignore')

# ──────────────────────────────────────────────────────────────────────────────
# CONFIGURAÇÃO GLOBAL E LOGGING
# ──────────────────────────────────────────────────────────────────────────────

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s | %(levelname)-7s | %(message)s',
    datefmt='%H:%M:%S',
    handlers=[
        logging.FileHandler("vhalinor_brain.log", encoding='utf-8'),
        logging.StreamHandler(sys.stdout)
    ]
)
logger = logging.getLogger("VHALINOR.IAG")

# ──────────────────────────────────────────────────────────────────────────────
# ENUMS E TIPOS
# ──────────────────────────────────────────────────────────────────────────────


class NeuronType(Enum):
    SENSORY = auto()  # Entrada de dados externos
    PROCESSING = auto()  # Cálculos gerais
    MEMORY = auto()  # Armazenamento
    DECISION = auto()  # Tomada de decisão
    OUTPUT = auto()  # Ações / respostas
    EMOTION = auto()  # Simulação emocional
    QUANTUM = auto()  # Processamento "quântico" simulado
    META = auto()  # Auto-observação e otimização


class PerformanceMode(Enum):
    ECO = "Eco"
    BALANCED = "Balanced"
    TURBO = "Turbo"

# ──────────────────────────────────────────────────────────────────────────────
# DATACLASSES OTIMIZADAS
# ──────────────────────────────────────────────────────────────────────────────


@dataclass
class Neuron:
    id: str
    file_path: str
    type: NeuronType
    activation: float = 0.0
    threshold: float = 0.55
    importance: float = 1.0
    energy_cost: float = 1.2
    last_fire: Optional[datetime] = None
    fire_count: int = 0
    connections_out: Set[str] = field(default_factory=set)
    connections_in: Set[str] = field(default_factory=set)
    tags: List[str] = field(default_factory=list)
    activation_history: Deque[float] = field(default_factory=lambda: deque(maxlen=120))

    def stimulate(self, strength: float, energy_available: float) -> float:
        effective = min(strength, energy_available * 0.8)
        self.activation += effective * (1.0 + random.uniform(-0.08, 0.08))
        self.activation = min(1.8, max(0.0, self.activation))

        if self.activation >= self.threshold:
            fired = self._fire()
            self.activation *= 0.12  # Vazamento forte após disparo
            return fired
        return 0.0

    def _fire(self) -> float:
        self.fire_count += 1
        self.last_fire = datetime.now()
        output = min(1.0, self.activation * 0.92)
        self.activation_history.append(output)
        return output

    @property
    def avg_activation(self) -> float:
        return np.mean(self.activation_history) if self.activation_history else 0.0

    @property
    def is_active(self) -> bool:
        return self.avg_activation > 0.15

# ──────────────────────────────────────────────────────────────────────────────
# SISTEMA DE CONEXÕES
# ──────────────────────────────────────────────────────────────────────────────


class ConnectionManager:

    def __init__(self):
        self.weights: Dict[Tuple[str, str], float] = {}
        self.graph = nx.DiGraph()

    def add(self, from_id: str, to_id: str, weight: float=0.5):
        if from_id == to_id:
            return
        self.weights[(from_id, to_id)] = max(0.05, min(1.8, weight))
        self.graph.add_edge(from_id, to_id, weight=weight)

    def remove(self, from_id: str, to_id: str):
        self.weights.pop((from_id, to_id), None)
        self.graph.remove_edge(from_id, to_id)

    def get_weight(self, from_id: str, to_id: str) -> float:
        return self.weights.get((from_id, to_id), 0.1)

    def prune_weak(self, threshold: float=0.12):
        to_remove = [(u, v) for (u, v), w in self.weights.items() if w < threshold]
        for u, v in to_remove[:150]:  # Limite por ciclo
            self.remove(u, v)

# ──────────────────────────────────────────────────────────────────────────────
# MEMÓRIA AVANÇADA (com busca semântica simulada)
# ──────────────────────────────────────────────────────────────────────────────


class MemorySystem:

    def __init__(self, max_items: int=1800):
        self.memories: Dict[str, Dict] = {}
        self.index: DefaultDict[str, Set[str]] = defaultdict(set)
        self.lru: OrderedDict = OrderedDict()
        self.max_items = max_items
        self.access_count = 0
        self.hit_count = 0

    def store(self, content: Any, tags: List[str]=None, importance: float=0.6) -> str:
        mem_id = hashlib.sha256(str(content).encode()).hexdigest()[:16]
        size = sys.getsizeof(content)

        self.memories[mem_id] = {
            'content': content,
            'tags': tags or [],
            'importance': importance,
            'created': datetime.now(),
            'accesses': 0,
            'size_kb': size / 1024
        }

        # Indexação simples + "palavras-chave" simuladas
        if isinstance(content, str):
            words = {w.lower() for w in content.split() if len(w) > 3}
            for w in words:
                self.index[w].add(mem_id)

        self._update_lru(mem_id)
        self._prune_if_needed()
        return mem_id

    def retrieve(self, mem_id: str) -> Optional[Any]:
        self.access_count += 1
        if mem_id in self.memories:
            self.hit_count += 1
            item = self.memories[mem_id]
            item['accesses'] += 1
            self._update_lru(mem_id)
            return item['content']
        return None

    def search(self, query: str, limit: int=8) -> List[Dict]:
        if not query:
            return []
        q_words = {w.lower() for w in query.split() if len(w) > 2}
        scores = []

        for word in q_words:
            for mem_id in self.index.get(word, set()):
                if mem_id not in self.memories:
                    continue
                item = self.memories[mem_id]
                score = item['importance'] + (item['accesses'] * 0.015)
                if isinstance(item['content'], str):
                    overlap = len(q_words & set(item['content'].lower().split()))
                    score += overlap * 0.25
                scores.append((score, mem_id))

        scores.sort(reverse=True)
        return [self.memories[mem_id] for _, mem_id in scores[:limit]]

    def _update_lru(self, mem_id: str):
        if mem_id in self.lru:
            self.lru.move_to_end(mem_id)
        else:
            self.lru[mem_id] = None
            if len(self.lru) > 600:
                self.lru.popitem(last=False)

    def _prune_if_needed(self):
        while len(self.memories) > self.max_items:
            # Remove o menos importante/acessado
            candidates = sorted(
                self.memories.items(),
                key=lambda x: (x[1]['importance'], x[1]['accesses'])
            )
            victim_id = candidates[0][0]
            del self.memories[victim_id]
            self.lru.pop(victim_id, None)
            for s in self.index.values():
                s.discard(victim_id)

    def stats(self):
        return {
            "total": len(self.memories),
            "lru_size": len(self.lru),
            "hit_rate": self.hit_count / max(1, self.access_count),
            "total_size_mb": sum(m['size_kb'] for m in self.memories.values()) / 1024
        }

# ──────────────────────────────────────────────────────────────────────────────
# MÓDULO DE MACHINE LEARNING
# ──────────────────────────────────────────────────────────────────────────────


class MLBrain:

    def __init__(self):
        self.scaler = StandardScaler()
        self.anomaly_detector = IsolationForest(contamination=0.08, random_state=42)
        self.predictor = RandomForestRegressor(n_estimators=60, max_depth=8, random_state=42)
        self.clusterer = MiniBatchKMeans(n_clusters=12, batch_size=120)
        self.pca = PCA(n_components=8)
        self.last_train = None

    async def train(self, features_list: List[np.ndarray]):
        if not features_list:
            return
        X = np.vstack(features_list)
        if len(X) < 20:
            return

        X_scaled = self.scaler.fit_transform(X)
        self.anomaly_detector.fit(X_scaled)
        self.pca.fit(X_scaled)
        self.clusterer.partial_fit(X_scaled)

        # Preparar dados para previsão de ativação futura
        if len(X) > 40:
            y = X[1:, 0]  # próxima ativação como target
            X_train = X[:-1]
            self.predictor.fit(self.scaler.transform(X_train), y)

        self.last_train = datetime.now()

    def predict_next_activation(self, current_features: np.ndarray) -> float:
        if not hasattr(self.predictor, 'estimators_'):
            return 0.4
        scaled = self.scaler.transform(current_features.reshape(1, -1))
        return float(self.predictor.predict(scaled)[0])

    def detect_anomalies(self, features: np.ndarray) -> np.ndarray:
        if len(features) == 0:
            return np.array([])
        scaled = self.scaler.transform(features)
        return self.anomaly_detector.predict(scaled)

# ──────────────────────────────────────────────────────────────────────────────
# SIMULAÇÃO QUÂNTICA MELHORADA (IMPLEMENTAÇÃO CORRIGIDA)
# ──────────────────────────────────────────────────────────────────────────────


class QuantumSimulator:
    """Simulador quântico realista baseado em vetor de estado (statevector)"""
    
    def __init__(self, n_qubits: int=6):
        self.n_qubits = n_qubits
        self.dim = 1 << n_qubits  # 2^n_qubits
        self.state = np.zeros(self.dim, dtype=complex)
        self.state[0] = 1.0 + 0j  # Estado inicial |000...0⟩
        self._init_gates()

    def _init_gates(self):
        """Inicializa matrizes das portas quânticas de 1 qubit"""
        # Hadamard
        self.H = np.array([[1, 1], [1, -1]], dtype=complex) / np.sqrt(2)
        # Pauli-X (NOT quântico)
        self.X = np.array([[0, 1], [1, 0]], dtype=complex)
        # Pauli-Y
        self.Y = np.array([[0, -1j], [1j, 0]], dtype=complex)
        # Pauli-Z
        self.Z = np.array([[1, 0], [0, -1]], dtype=complex)
        # Fase S (π/2)
        self.S = np.array([[1, 0], [0, 1j]], dtype=complex)
        # Fase T (π/4)
        self.T = np.array([[1, 0], [0, np.exp(1j * np.pi / 4)]], dtype=complex)
        # Identidade
        self.I = np.eye(2, dtype=complex)

    def _apply_single_qubit_gate(self, gate: np.ndarray, target: int):
        """Aplica uma porta de 1 qubit em um qubit específico"""
        if target < 0 or target >= self.n_qubits:
            raise ValueError(f"Qubit inválido: {target}")

        # Matriz completa: I ⊗ I ⊗ ... ⊗ gate ⊗ ... ⊗ I
        full_gate = np.eye(1, dtype=complex)
        for q in range(self.n_qubits):
            if q == target:
                full_gate = np.kron(full_gate, gate)
            else:
                full_gate = np.kron(full_gate, self.I)
        
        # Aplica a transformação unitária
        self.state = full_gate @ self.state

    def hadamard(self, target: int):
        """Aplica porta Hadamard em um qubit"""
        self._apply_single_qubit_gate(self.H, target)

    def pauli_x(self, target: int):
        """Pauli-X (bit flip)"""
        self._apply_single_qubit_gate(self.X, target)

    def pauli_y(self, target: int):
        """Pauli-Y"""
        self._apply_single_qubit_gate(self.Y, target)

    def pauli_z(self, target: int):
        """Pauli-Z (phase flip)"""
        self._apply_single_qubit_gate(self.Z, target)

    def cnot(self, control: int, target: int):
        """Aplica CNOT (Controlled-NOT)"""
        if control == target:
            raise ValueError("Control e target não podem ser o mesmo qubit")
        if control < 0 or control >= self.n_qubits or target < 0 or target >= self.n_qubits:
            raise ValueError("Qubit fora do intervalo")

        mask_control = 1 << control
        mask_target = 1 << target
        new_state = np.zeros_like(self.state, dtype=complex)

        for i in range(self.dim):
            if (i & mask_control) == 0:
                # control = |0⟩ → não faz nada
                new_state[i] = self.state[i]
            else:
                # control = |1⟩ → aplica X no target
                j = i ^ mask_target
                new_state[j] = self.state[i]

        self.state = new_state

    def measure(self) -> int:
        """Realiza medição em todos os qubits (colapso do estado)"""
        probs = np.abs(self.state) ** 2
        probs /= np.sum(probs) + 1e-14  # normaliza
        
        # Escolhe resultado com probabilidade correta
        outcome = np.random.choice(self.dim, p=probs)
        
        # Colapso: mantém apenas o componente medido
        self.state = np.zeros(self.dim, dtype=complex)
        self.state[outcome] = 1.0 + 0j
        
        return outcome

    def get_probabilities(self) -> np.ndarray:
        """Retorna as probabilidades de cada base computacional"""
        probs = np.abs(self.state) ** 2
        return probs / np.sum(probs)

    def von_neumann_entropy(self) -> float:
        """Entropia de von Neumann do estado quântico"""
        probs = np.abs(self.state) ** 2
        probs = probs[probs > 1e-12]
        if len(probs) <= 1:
            return 0.0
        return -np.sum(probs * np.log2(probs))

    async def run_cycle(self, depth: int=4) -> Dict[str, Any]:
        """Executa um circuito quântico aleatório"""
        start_time = time.time()

        for _ in range(depth):
            q = random.randint(0, self.n_qubits - 1)
            if random.random() < 0.6:
                self.hadamard(q)
            else:
                self.pauli_x(q)
            
            if self.n_qubits >= 2 and random.random() < 0.4:
                c = random.randint(0, self.n_qubits - 2)
                t = random.randint(c + 1, self.n_qubits - 1)
                self.cnot(c, t)

        result = self.measure()
        result_bin = bin(result)[2:].zfill(self.n_qubits)
        duration = time.time() - start_time

        return {
            "result_bin": result_bin,
            "entropy": self.von_neumann_entropy(),
            "int_value": result,
            "simulation_time_s": duration,
            "qubits": self.n_qubits,
            "probs_top5": sorted(self.get_probabilities(), reverse=True)[:5].tolist()
        }

# ──────────────────────────────────────────────────────────────────────────────
# ORQUESTRADOR CENTRAL (CÉREBRO)
# ──────────────────────────────────────────────────────────────────────────────


class VhalinorBrain:

    def __init__(self, base_path: str="./vhalinor_iag"):
        self.base_path = Path(base_path).expanduser().resolve()
        self.neurons: Dict[str, Neuron] = {}
        self.connections = ConnectionManager()
        self.memory = MemorySystem(max_items=2200)
        self.ml = MLBrain()
        self.quantum = QuantumSimulator(n_qubits=7)
        self.energy = 1200.0
        self.mode = PerformanceMode.BALANCED
        self.state_file = self.base_path / "brain_state.pkl"
        self._maintenance_task = None

        self._load_or_create_neurons()
        self._load_connections()
        self._start_background_maintenance()

    def _start_background_maintenance(self):
        """Inicia a tarefa de manutenção em background"""
        self._maintenance_task = asyncio.create_task(self._background_maintenance())

    def _load_or_create_neurons(self):
        logger.info("Carregando neurônios...")
        count = 0
        if not self.base_path.exists():
            self.base_path.mkdir(parents=True, exist_ok=True)
            logger.info(f"Diretório base criado: {self.base_path}")
        
        for file in self.base_path.rglob("*"):
            if file.is_file() and file.suffix in {".py", ".txt", ".json", ".md"}:
                fid = hashlib.sha256(str(file).encode()).hexdigest()[:14]
                ntype = self._guess_type(file)
                neuron = Neuron(
                    id=fid,
                    file_path=str(file),
                    type=ntype,
                    threshold=random.uniform(0.48, 0.68),
                    importance=random.uniform(0.7, 1.4),
                    energy_cost=random.uniform(0.8, 2.2)
                )
                self.neurons[fid] = neuron
                count += 1
        
        if count == 0:
            logger.warning("Nenhum neurônio encontrado. Criando exemplos...")
            self._create_sample_neurons()
        else:
            logger.info(f"{count} neurônios carregados")

    def _create_sample_neurons(self):
        """Cria neurônios de exemplo se não houver arquivos"""
        sample_files = [
            "sensor_input.py",
            "quantum_processor.py",
            "decision_logic.py",
            "memory_core.json",
            "emotional_state.md"
        ]
        
        for filename in sample_files:
            file_path = self.base_path / filename
            file_path.touch()
            fid = hashlib.sha256(str(file_path).encode()).hexdigest()[:14]
            ntype = self._guess_type(file_path)
            neuron = Neuron(
                id=fid,
                file_path=str(file_path),
                type=ntype,
                threshold=random.uniform(0.48, 0.68),
                importance=random.uniform(0.7, 1.4),
                energy_cost=random.uniform(0.8, 2.2)
            )
            self.neurons[fid] = neuron
        
        logger.info(f"{len(sample_files)} neurônios de exemplo criados")

    def _guess_type(self, path: Path) -> NeuronType:
        name = path.name.lower()
        if "quantum" in name or "qubit" in name: 
            return NeuronType.QUANTUM
        if "decision" in name or "logic" in name: 
            return NeuronType.DECISION
        if "emotion" in name or "mood" in name: 
            return NeuronType.EMOTION
        if "output" in name or "response" in name: 
            return NeuronType.OUTPUT
        if "memory" in name or "recall" in name: 
            return NeuronType.MEMORY
        if "meta" in name or "self" in name: 
            return NeuronType.META
        if "sensor" in name or "input" in name:
            return NeuronType.SENSORY
        return NeuronType.PROCESSING

    def _load_connections(self):
        """Carrega ou cria conexões entre neurônios"""
        connection_file = self.base_path / "connections.json"
        
        if connection_file.exists():
            try:
                with open(connection_file, 'r') as f:
                    connections_data = json.load(f)
                for from_id, to_id, weight in connections_data:
                    if from_id in self.neurons and to_id in self.neurons:
                        self.connections.add(from_id, to_id, weight)
                logger.info(f"Conexões carregadas: {len(connections_data)}")
            except Exception as e:
                logger.error(f"Erro ao carregar conexões: {e}")
                self._create_initial_connections()
        else:
            self._create_initial_connections()

    def _create_initial_connections(self):
        """Cria conexões iniciais aleatórias"""
        neuron_ids = list(self.neurons.keys())
        if len(neuron_ids) < 2:
            return
        
        for nid in neuron_ids:
            num_connections = random.randint(2, min(8, len(neuron_ids) - 1))
            targets = random.sample([id for id in neuron_ids if id != nid], num_connections)
            for target in targets:
                self.connections.add(nid, target, random.uniform(0.2, 1.1))
        
        logger.info(f"Conexões iniciais criadas: {self.connections.graph.number_of_edges()}")

    async def stimulate(self, neuron_id: str, strength: float) -> float:
        """Estimula um neurônio e propaga através das conexões"""
        if neuron_id not in self.neurons:
            logger.warning(f"Neurônio não encontrado: {neuron_id}")
            return 0.0
        
        neuron = self.neurons[neuron_id]
        cost = neuron.energy_cost * (strength ** 1.3)
        
        if self.energy < cost * 0.6:
            logger.debug(f"Energia insuficiente para estimular {neuron_id}")
            return 0.0
        
        self.energy -= cost
        fired = neuron.stimulate(strength, self.energy)
        
        if fired > 0:
            # Propaga para neurônios conectados
            propagation_tasks = []
            for target in self.connections.graph.successors(neuron_id):
                w = self.connections.get_weight(neuron_id, target)
                propagation_strength = fired * w * 0.7
                propagation_tasks.append(self.stimulate(target, propagation_strength))
            
            # Executa propagação em paralelo
            if propagation_tasks:
                await asyncio.gather(*propagation_tasks, return_exceptions=True)
        
        return fired

    async def think(self, stimulus: str="") -> str:
        """Gera um pensamento baseado no estado atual do cérebro"""
        active = [n for n in self.neurons.values() if n.is_active]
        if not active:
            return "Sistema em repouso profundo..."
        
        top_neurons = sorted(active, key=lambda n: n.avg_activation, reverse=True)[:5]
        concepts = []
        for n in top_neurons:
            if n.type == NeuronType.EMOTION:
                concepts.append("emoção")
            elif n.type == NeuronType.DECISION:
                concepts.append("decisão crítica")
            elif n.type == NeuronType.QUANTUM:
                concepts.append("incerteza quântica")
            else:
                concepts.append(n.type.name.lower())
        
        # Executa ciclo quântico se houver neurônios quânticos ativos
        quantum_active = any(n.type == NeuronType.QUANTUM for n in top_neurons)
        quantum_result = None
        if quantum_active and random.random() < 0.3:
            quantum_result = await self.quantum.run_cycle()
        
        thought = f"Processando {stimulus or 'fluxo interno'}. "
        thought += f"Neurônios dominantes: {', '.join(concepts)}. "
        if quantum_result:
            thought += f"Entropia quântica: {quantum_result['entropy']:.3f}. "
        thought += f"Energia: {self.energy:.0f} | Ativos: {len(active)}"
        
        return thought

    async def quantum_stimulus(self) -> Dict[str, Any]:
        """Executa um ciclo quântico e estimula neurônios baseados nos resultados"""
        result = await self.quantum.run_cycle(depth=10)
        measurement_int = result["int_value"]
        
        quantum_neurons = [n for n in self.neurons.values() 
                          if n.type == NeuronType.QUANTUM]
        
        stimulated_count = 0
        for i, neuron in enumerate(quantum_neurons[:self.quantum.n_qubits]):
            bit = (measurement_int >> i) & 1
            if bit:
                strength = 0.4 + random.random() * 0.7
                await self.stimulate(neuron.id, strength)
                stimulated_count += 1
        
        result["stimulated_neurons"] = stimulated_count
        return result

    async def _background_maintenance(self):
        """Tarefa de manutenção em background"""
        try:
            while True:
                interval = 45 if self.mode == PerformanceMode.ECO else 18
                await asyncio.sleep(interval)
                
                # Recarga de energia
                self.energy = min(1800.0, self.energy + random.uniform(40, 120))
                
                # Poda de conexões fracas
                if random.random() < 0.4:
                    old_count = self.connections.graph.number_of_edges()
                    self.connections.prune_weak()
                    new_count = self.connections.graph.number_of_edges()
                    logger.debug(f"Conexões podadas: {old_count - new_count} removidas")
                
                # Treinamento ML
                if random.random() < 0.25:
                    await self._train_ml_cycle()
                
                # Coleta de lixo
                gc.collect()
                
        except asyncio.CancelledError:
            logger.info("Manutenção em background cancelada")
        except Exception as e:
            logger.error(f"Erro na manutenção: {e}")

    async def _train_ml_cycle(self):
        """Executa um ciclo de treinamento do modelo de ML"""
        features = []
        neurons_to_sample = min(400, len(self.neurons))
        sample_keys = random.sample(list(self.neurons.keys()), neurons_to_sample)
        
        for nid in sample_keys:
            neuron = self.neurons[nid]
            feat = np.array([
                neuron.activation,
                neuron.avg_activation,
                neuron.fire_count / 100,
                len(neuron.connections_out),
                len(neuron.connections_in),
                neuron.importance,
                1.0 if neuron.type == NeuronType.QUANTUM else 0.0
            ])
            features.append(feat)
        
        await self.ml.train(features)
        logger.debug(f"Ciclo ML concluído: {len(features)} amostras processadas")

    def save_state(self):
        """Salva o estado atual do cérebro"""
        try:
            self.base_path.mkdir(parents=True, exist_ok=True)
            
            state = {
                "energy": self.energy,
                "mode": self.mode.value,
                "neurons_basic": {
                    nid: {
                        "activation": n.activation,
                        "fire_count": n.fire_count,
                        "importance": n.importance,
                        "threshold": n.threshold
                    } 
                    for nid, n in self.neurons.items()
                },
                "connections": [
                    (from_id, to_id, weight)
                    for (from_id, to_id), weight in self.connections.weights.items()
                ],
                "timestamp": datetime.now().isoformat(),
                "version": "5.0"
            }
            
            with open(self.state_file, "wb") as f:
                pickle.dump(state, f)
            
            # Salva também como JSON legível
            json_file = self.state_file.with_suffix('.json')
            with open(json_file, 'w', encoding='utf-8') as f:
                json.dump(state, f, indent=2, default=str)
            
            logger.info(f"Estado salvo em {self.state_file}")
            
        except Exception as e:
            logger.error(f"Erro ao salvar estado: {e}")

    def load_state(self):
        """Carrega o estado salvo do cérebro"""
        if not self.state_file.exists():
            logger.info("Nenhum estado anterior encontrado")
            return
        
        try:
            with open(self.state_file, "rb") as f:
                state = pickle.load(f)
            
            for nid, data in state.get("neurons_basic", {}).items():
                if nid in self.neurons:
                    self.neurons[nid].activation = data.get("activation", 0.0)
                    self.neurons[nid].fire_count = data.get("fire_count", 0)
                    self.neurons[nid].importance = data.get("importance", 1.0)
                    self.neurons[nid].threshold = data.get("threshold", 0.55)
            
            self.energy = state.get("energy", 1200.0)
            mode_str = state.get("mode", "BALANCED")
            self.mode = PerformanceMode[mode_str] if mode_str in PerformanceMode.__members__ else PerformanceMode.BALANCED
            
            logger.info(f"Estado carregado (versão: {state.get('version', 'desconhecida')})")
            
        except Exception as e:
            logger.error(f"Erro ao carregar estado: {e}")

    async def shutdown(self):
        """Desliga o cérebro de forma segura"""
        if self._maintenance_task:
            self._maintenance_task.cancel()
            try:
                await self._maintenance_task
            except asyncio.CancelledError:
                pass
        
        self.save_state()
        logger.info("Cérebro desligado com segurança")

# ──────────────────────────────────────────────────────────────────────────────
# INTERFACE GRÁFICA AVANÇADA
# ──────────────────────────────────────────────────────────────────────────────


class BrainGUI:

    def __init__(self, brain: VhalinorBrain):
        self.brain = brain
        self.root = tk.Tk()
        self.root.title("VHALINOR.IAG 5.0 - Núcleo Cerebral")
        self.root.geometry("1280x820")
        self.root.configure(bg="#0d1117")
        
        # Configurar protocolo de fechamento
        self.root.protocol("WM_DELETE_WINDOW", self.on_closing)

        self.notebook = ttk.Notebook(self.root)
        self.notebook.pack(fill=tk.BOTH, expand=True, padx=8, pady=8)

        self._create_overview_tab()
        self._create_neurons_tab()
        self._create_graph_tab()
        self._create_console_tab()
        self._create_memory_tab()

        self.update_task()

    def on_closing(self):
        """Lida com o fechamento da janela"""
        if messagebox.askokcancel("Sair", "Deseja salvar o estado antes de sair?"):
            self.brain.save_state()
        self.root.quit()
        self.root.destroy()

    def _create_overview_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text=" Overview ")

        # Título
        title_label = ttk.Label(
            frame,
            text="VHALINOR.IAG 5.0 - Sistema Cerebral Artificial",
            font=("Helvetica", 16, "bold")
        )
        title_label.pack(pady=12)

        # Frame de métricas
        metrics_frame = ttk.LabelFrame(frame, text="Métricas do Sistema")
        metrics_frame.pack(fill=tk.X, padx=20, pady=10)

        self.status_vars = {}
        metrics = [
            ("Energia", "energy", " unidades"),
            ("Neurônios Ativos", "active_count", ""),
            ("Conexões", "connections", ""),
            ("Memória Itens", "memory_items", ""),
            ("Taxa de Acerto", "hit_rate", "%"),
            ("Modo", "mode", "")
        ]

        for i, (label, key, unit) in enumerate(metrics):
            row = ttk.Frame(metrics_frame)
            row.grid(row=i // 2, column=i % 2, sticky="w", padx=20, pady=4)
            ttk.Label(row, text=f"{label}:").pack(side=tk.LEFT)
            var = tk.StringVar(value="—")
            ttk.Label(
                row,
                textvariable=var,
                font=("Consolas", 11)
            ).pack(side=tk.LEFT, padx=12)
            self.status_vars[key] = var

        # Frame de controle
        control_frame = ttk.LabelFrame(frame, text="Controles")
        control_frame.pack(fill=tk.X, padx=20, pady=20)

        btn_frame = ttk.Frame(control_frame)
        btn_frame.pack(pady=10)

        buttons = [
            ("Otimizar Agora", self.optimize_now),
            ("Pensar", self.manual_think),
            ("Estimular Aleatório", self.random_stimulus),
            ("Ciclo Quântico", self.quantum_cycle),
            ("Salvar Estado", self.brain.save_state),
            ("Recarregar", self.reload_state)
        ]

        for text, command in buttons:
            btn = ttk.Button(
                btn_frame,
                text=text,
                command=command,
                width=15
            )
            btn.pack(side=tk.LEFT, padx=5, pady=5)

        # Status
        self.status_label = ttk.Label(
            control_frame,
            text="Sistema inicializado",
            font=("Consolas", 9)
        )
        self.status_label.pack(pady=5)

    def _create_neurons_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text=" Neurônios ")

        # Barra de ferramentas
        toolbar = ttk.Frame(frame)
        toolbar.pack(fill=tk.X, padx=5, pady=5)

        ttk.Button(toolbar, text="Atualizar", command=self._update_neuron_list).pack(side=tk.LEFT, padx=2)
        ttk.Button(toolbar, text="Exportar CSV", command=self.export_neurons_csv).pack(side=tk.LEFT, padx=2)
        
        self.filter_var = tk.StringVar(value="Todos")
        filter_combo = ttk.Combobox(
            toolbar,
            textvariable=self.filter_var,
            values=["Todos", "Ativos", "Quânticos", "Memória", "Decisão", "Emoção"],
            state="readonly",
            width=12
        )
        filter_combo.pack(side=tk.LEFT, padx=2)
        filter_combo.bind("<<ComboboxSelected>>", lambda e: self._update_neuron_list())

        # Treeview
        tree_frame = ttk.Frame(frame)
        tree_frame.pack(fill=tk.BOTH, expand=True, padx=5, pady=5)

        self.neuron_tree = ttk.Treeview(
            tree_frame,
            columns=("ID", "Tipo", "Ativação", "Fires", "Importância", "Conexões"),
            show="headings",
            height=20
        )

        columns = [
            ("ID", 120),
            ("Tipo", 100),
            ("Ativação", 80),
            ("Fires", 80),
            ("Importância", 80),
            ("Conexões", 80)
        ]

        for col, width in columns:
            self.neuron_tree.heading(col, text=col)
            self.neuron_tree.column(col, width=width)

        scrollbar = ttk.Scrollbar(tree_frame, orient="vertical", command=self.neuron_tree.yview)
        self.neuron_tree.configure(yscrollcommand=scrollbar.set)
        self.neuron_tree.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)

        # Bind de clique duplo
        self.neuron_tree.bind("<Double-1>", self.on_neuron_double_click)

    def _create_graph_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text=" Grafo Neural ")

        toolbar = ttk.Frame(frame)
        toolbar.pack(fill=tk.X, padx=5, pady=5)

        ttk.Button(toolbar, text="Redesenhar", command=self._update_graph).pack(side=tk.LEFT, padx=2)
        ttk.Button(toolbar, text="Exportar PNG", command=self.export_graph_png).pack(side=tk.LEFT, padx=2)

        self.fig = Figure(figsize=(10, 7), dpi=100)
        self.ax = self.fig.add_subplot(111)
        self.canvas = FigureCanvasTkAgg(self.fig, frame)
        self.canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True, padx=5, pady=5)

    def _create_console_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text=" Console ")

        toolbar = ttk.Frame(frame)
        toolbar.pack(fill=tk.X, padx=5, pady=5)

        ttk.Button(toolbar, text="Limpar", command=self.clear_console).pack(side=tk.LEFT, padx=2)
        ttk.Button(toolbar, text="Exportar Log", command=self.export_log).pack(side=tk.LEFT, padx=2)

        self.console = scrolledtext.ScrolledText(
            frame,
            bg="#0d1117",
            fg="#c9d1d9",
            font=("Consolas", 10),
            wrap=tk.WORD
        )
        self.console.pack(fill=tk.BOTH, expand=True, padx=5, pady=5)

        # Configurar tags para cores
        self.console.tag_config("info", foreground="#58a6ff")
        self.console.tag_config("warning", foreground="#f0883e")
        self.console.tag_config("error", foreground="#f85149")
        self.console.tag_config("success", foreground="#3fb950")

    def _create_memory_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text=" Memória ")

        # Painel de busca
        search_frame = ttk.Frame(frame)
        search_frame.pack(fill=tk.X, padx=10, pady=10)

        ttk.Label(search_frame, text="Buscar:").pack(side=tk.LEFT)
        self.search_entry = ttk.Entry(search_frame, width=40)
        self.search_entry.pack(side=tk.LEFT, padx=5)
        self.search_entry.bind("<Return>", lambda e: self.search_memory())
        
        ttk.Button(search_frame, text="Buscar", command=self.search_memory).pack(side=tk.LEFT, padx=2)
        ttk.Button(search_frame, text="Estatísticas", command=self.show_memory_stats).pack(side=tk.LEFT, padx=2)

        # Lista de memórias
        tree_frame = ttk.Frame(frame)
        tree_frame.pack(fill=tk.BOTH, expand=True, padx=10, pady=5)

        self.memory_tree = ttk.Treeview(
            tree_frame,
            columns=("ID", "Conteúdo", "Tags", "Importância", "Acessos"),
            show="headings",
            height=15
        )

        columns = [
            ("ID", 120),
            ("Conteúdo", 300),
            ("Tags", 150),
            ("Importância", 80),
            ("Acessos", 80)
        ]

        for col, width in columns:
            self.memory_tree.heading(col, text=col)
            self.memory_tree.column(col, width=width)

        scrollbar = ttk.Scrollbar(tree_frame, orient="vertical", command=self.memory_tree.yview)
        self.memory_tree.configure(yscrollcommand=scrollbar.set)
        self.memory_tree.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)

    def update_task(self):
        """Atualiza periodicamente a interface"""
        self._update_status()
        self._update_neuron_list()
        self._update_graph()
        self.root.after(3000, self.update_task)

    def _update_status(self):
        """Atualiza as métricas de status"""
        try:
            active = sum(1 for n in self.brain.neurons.values() if n.is_active)
            mem_stats = self.brain.memory.stats()
            
            self.status_vars["energy"].set(f"{self.brain.energy:.0f}")
            self.status_vars["active_count"].set(str(active))
            self.status_vars["connections"].set(str(self.brain.connections.graph.number_of_edges()))
            self.status_vars["memory_items"].set(str(len(self.brain.memory.memories)))
            self.status_vars["hit_rate"].set(f"{mem_stats['hit_rate']:.1%}")
            self.status_vars["mode"].set(self.brain.mode.value)
            
            self.status_label.config(text=f"Última atualização: {datetime.now().strftime('%H:%M:%S')}")
            
        except Exception as e:
            self.log_message(f"Erro ao atualizar status: {e}", "error")

    def _update_neuron_list(self):
        """Atualiza a lista de neurônios"""
        try:
            for item in self.neuron_tree.get_children():
                self.neuron_tree.delete(item)

            filter_type = self.filter_var.get()
            neurons_to_show = []
            
            for nid, neuron in self.brain.neurons.items():
                if filter_type == "Todos":
                    neurons_to_show.append((nid, neuron))
                elif filter_type == "Ativos" and neuron.is_active:
                    neurons_to_show.append((nid, neuron))
                elif filter_type == "Quânticos" and neuron.type == NeuronType.QUANTUM:
                    neurons_to_show.append((nid, neuron))
                elif filter_type == "Memória" and neuron.type == NeuronType.MEMORY:
                    neurons_to_show.append((nid, neuron))
                elif filter_type == "Decisão" and neuron.type == NeuronType.DECISION:
                    neurons_to_show.append((nid, neuron))
                elif filter_type == "Emoção" and neuron.type == NeuronType.EMOTION:
                    neurons_to_show.append((nid, neuron))
            
            # Limitar para não sobrecarregar a interface
            neurons_to_show = random.sample(neurons_to_show, min(120, len(neurons_to_show)))
            
            for nid, neuron in neurons_to_show:
                connections = len(neuron.connections_out) + len(neuron.connections_in)
                self.neuron_tree.insert("", "end", values=(
                    nid[:10] + "...",
                    neuron.type.name,
                    f"{neuron.avg_activation:.3f}",
                    neuron.fire_count,
                    f"{neuron.importance:.2f}",
                    connections
                ))
                
        except Exception as e:
            self.log_message(f"Erro ao atualizar lista de neurônios: {e}", "error")

    def _update_graph(self):
        """Atualiza o gráfico da rede neural"""
        try:
            self.ax.clear()
            G = self.brain.connections.graph

            if len(G) > 0:
                # Amostra para visualização
                if len(G) > 100:
                    nodes = random.sample(list(G.nodes()), 80)
                    G = G.subgraph(nodes)

                try:
                    pos = nx.spring_layout(G, iterations=15, seed=42)
                    node_colors = []
                    node_sizes = []
                    
                    for node in G.nodes():
                        if node in self.brain.neurons:
                            neuron = self.brain.neurons[node]
                            # Cor baseada no tipo
                            if neuron.type == NeuronType.QUANTUM:
                                node_colors.append("purple")
                            elif neuron.type == NeuronType.EMOTION:
                                node_colors.append("red")
                            elif neuron.type == NeuronType.DECISION:
                                node_colors.append("green")
                            elif neuron.type == NeuronType.MEMORY:
                                node_colors.append("orange")
                            else:
                                node_colors.append("lightblue")
                            
                            # Tamanho baseado na importância
                            node_sizes.append(50 + neuron.importance * 20)
                        else:
                            node_colors.append("gray")
                            node_sizes.append(30)

                    nx.draw_networkx_nodes(G, pos, node_color=node_colors,
                                          node_size=node_sizes, alpha=0.8, ax=self.ax)
                    nx.draw_networkx_edges(G, pos, alpha=0.3, arrowsize=6,
                                          edge_color="gray", ax=self.ax)
                    
                    # Labels apenas para nós importantes
                    labels = {}
                    for node in G.nodes():
                        if node in self.brain.neurons:
                            neuron = self.brain.neurons[node]
                            if neuron.importance > 1.2:
                                labels[node] = neuron.type.name[:3]
                    
                    nx.draw_networkx_labels(G, pos, labels, font_size=8, ax=self.ax)
                    
                    self.ax.set_title(f"Grafo Neural ({len(G)} nós, {G.number_of_edges()} arestas)")
                    self.ax.axis('off')
                    
                except Exception as e:
                    self.ax.text(0.5, 0.5, f"Erro ao desenhar grafo:\n{e}",
                                ha="center", va="center", fontsize=10)
            else:
                self.ax.text(0.5, 0.5, "Grafo vazio",
                            ha="center", va="center", fontsize=12)

            self.fig.tight_layout()
            self.canvas.draw()
            
        except Exception as e:
            self.log_message(f"Erro ao atualizar gráfico: {e}", "error")

    def search_memory(self):
        """Realiza busca na memória"""
        query = self.search_entry.get()
        if not query:
            return
        
        for item in self.memory_tree.get_children():
            self.memory_tree.delete(item)
        
        results = self.brain.memory.search(query, limit=20)
        
        for item in results:
            content_preview = str(item['content'])[:50] + "..." if len(str(item['content'])) > 50 else str(item['content'])
            tags_str = ", ".join(item['tags']) if item['tags'] else ""
            
            self.memory_tree.insert("", "end", values=(
                list(self.brain.memory.memories.keys())[list(self.brain.memory.memories.values()).index(item)][:10] + "...",
                content_preview,
                tags_str,
                f"{item['importance']:.2f}",
                item['accesses']
            ))

    def show_memory_stats(self):
        """Exibe estatísticas da memória"""
        stats = self.brain.memory.stats()
        message = f"""
        Estatísticas da Memória:
        
        Total de itens: {stats['total']:,}
        Tamanho total: {stats['total_size_mb']:.2f} MB
        Taxa de acerto: {stats['hit_rate']:.1%}
        Tamanho da LRU: {stats['lru_size']}
        """
        
        messagebox.showinfo("Estatísticas da Memória", message)

    def optimize_now(self):
        """Inicia otimização"""
        asyncio.create_task(self._optimize())

    async def _optimize(self):
        """Executa otimização"""
        self.log_message("→ Iniciando otimização do sistema...", "info")
        try:
            # Poda de conexões
            old_edges = self.brain.connections.graph.number_of_edges()
            self.brain.connections.prune_weak(0.10)
            new_edges = self.brain.connections.graph.number_of_edges()
            
            # Treinamento ML
            await self.brain._train_ml_cycle()
            
            # Coleta de lixo
            gc.collect()
            
            self.log_message(f"→ Otimização concluída. Conexões removidas: {old_edges - new_edges}", "success")
            
        except Exception as e:
            self.log_message(f"→ Erro na otimização: {e}", "error")

    def manual_think(self):
        """Executa um pensamento manual"""

        async def think_wrapper():
            try:
                thought = await self.brain.think("interação manual")
                self.log_message(f"[PENSAMENTO] {thought}", "info")
            except Exception as e:
                self.log_message(f"Erro ao pensar: {e}", "error")
        
        asyncio.create_task(think_wrapper())

    def random_stimulus(self):
        """Aplica estímulo aleatório"""

        async def stimulus_wrapper():
            try:
                if self.brain.neurons:
                    nid = random.choice(list(self.brain.neurons.keys()))
                    strength = random.uniform(0.3, 1.2)
                    result = await self.brain.stimulate(nid, strength)
                    neuron = self.brain.neurons[nid]
                    self.log_message(f"→ Neurônio {neuron.type.name} estimulado: força={strength:.2f}, resultado={result:.3f}", "info")
            except Exception as e:
                self.log_message(f"Erro no estímulo: {e}", "error")
        
        asyncio.create_task(stimulus_wrapper())

    def quantum_cycle(self):
        """Executa um ciclo quântico"""

        async def quantum_wrapper():
            try:
                self.log_message("→ Iniciando ciclo quântico...", "info")
                result = await self.brain.quantum_stimulus()
                self.log_message(f"→ Ciclo quântico concluído. Resultado: {result['result_bin']}, Entropia: {result['entropy']:.3f}", "success")
            except Exception as e:
                self.log_message(f"Erro no ciclo quântico: {e}", "error")
        
        asyncio.create_task(quantum_wrapper())

    def reload_state(self):
        """Recarrega o estado salvo"""
        self.brain.load_state()
        self.log_message("→ Estado recarregado do disco", "success")

    def export_neurons_csv(self):
        """Exporta neurônios para CSV"""
        try:
            filename = f"neurons_export_{datetime.now().strftime('%Y%m%d_%H%M%S')}.csv"
            data = []
            
            for nid, neuron in self.brain.neurons.items():
                data.append({
                    'id': nid,
                    'type': neuron.type.name,
                    'activation': neuron.activation,
                    'avg_activation': neuron.avg_activation,
                    'threshold': neuron.threshold,
                    'importance': neuron.importance,
                    'fire_count': neuron.fire_count,
                    'energy_cost': neuron.energy_cost,
                    'connections_out': len(neuron.connections_out),
                    'connections_in': len(neuron.connections_in)
                })
            
            df = pd.DataFrame(data)
            df.to_csv(filename, index=False)
            self.log_message(f"→ Neurônios exportados para {filename}", "success")
            
        except Exception as e:
            self.log_message(f"Erro ao exportar CSV: {e}", "error")

    def export_graph_png(self):
        """Exporta o grafo como PNG"""
        try:
            filename = f"graph_export_{datetime.now().strftime('%Y%m%d_%H%M%S')}.png"
            self.fig.savefig(filename, dpi=150, bbox_inches='tight')
            self.log_message(f"→ Grafo exportado para {filename}", "success")
            
        except Exception as e:
            self.log_message(f"Erro ao exportar PNG: {e}", "error")

    def export_log(self):
        """Exporta o console para arquivo"""
        try:
            filename = f"console_log_{datetime.now().strftime('%Y%m%d_%H%M%S')}.txt"
            content = self.console.get("1.0", tk.END)
            with open(filename, 'w', encoding='utf-8') as f:
                f.write(content)
            self.log_message(f"→ Log exportado para {filename}", "success")
            
        except Exception as e:
            self.log_message(f"Erro ao exportar log: {e}", "error")

    def clear_console(self):
        """Limpa o console"""
        self.console.delete("1.0", tk.END)

    def log_message(self, message: str, level: str="info"):
        """Adiciona mensagem ao console com formatação"""
        timestamp = datetime.now().strftime("%H:%M:%S")
        formatted = f"[{timestamp}] {message}\n"
        
        self.console.insert(tk.END, formatted, level)
        self.console.see(tk.END)

    def on_neuron_double_click(self, event):
        """Lida com clique duplo em neurônio"""
        item = self.neuron_tree.selection()[0]
        values = self.neuron_tree.item(item, 'values')
        
        if values:
            neuron_id_short = values[0]
            # Encontrar neurônio completo
            for nid, neuron in self.brain.neurons.items():
                if nid.startswith(neuron_id_short.replace("...", "")):
                    message = f"""
                    Detalhes do Neurônio:
                    
                    ID: {nid}
                    Tipo: {neuron.type.name}
                    Caminho do arquivo: {neuron.file_path}
                    Ativação atual: {neuron.activation:.3f}
                    Ativação média: {neuron.avg_activation:.3f}
                    Limiar: {neuron.threshold:.3f}
                    Importância: {neuron.importance:.2f}
                    Custo de energia: {neuron.energy_cost:.2f}
                    Disparos totais: {neuron.fire_count}
                    Último disparo: {neuron.last_fire or 'Nunca'}
                    Conexões de saída: {len(neuron.connections_out)}
                    Conexões de entrada: {len(neuron.connections_in)}
                    Tags: {', '.join(neuron.tags) if neuron.tags else 'Nenhuma'}
                    """
                    messagebox.showinfo("Detalhes do Neurônio", message)
                    break

    def run(self):
        """Executa a interface gráfica"""
        try:
            self.root.mainloop()
        except Exception as e:
            logger.error(f"Erro na GUI: {e}")
            raise

# ──────────────────────────────────────────────────────────────────────────────
# FUNÇÃO PRINCIPAL
# ──────────────────────────────────────────────────────────────────────────────


async def main_async():
    """Função principal assíncrona"""
    print("""
    ╔═══════════════════════════════════════════════╗
    ║      VHALINOR.IAG 5.0  -  2026                ║
    ║   Sistema Cerebral Artificial Ultra-Otimizado ║
    ╚═══════════════════════════════════════════════╝
    """)

    try:
        # Inicializar cérebro
        brain = VhalinorBrain()
        brain.load_state()

        # Estímulo inicial
        logger.info("Aplicando estímulos iniciais...")
        for _ in range(15):
            if brain.neurons:
                nid = random.choice(list(brain.neurons.keys()))
                await brain.stimulate(nid, random.uniform(0.4, 1.3))

        # Iniciar interface gráfica em thread separada
        def run_gui():
            try:
                gui = BrainGUI(brain)
                gui.run()
            except Exception as e:
                logger.error(f"Erro na GUI: {e}")

        gui_thread = threading.Thread(target=run_gui, daemon=True)
        gui_thread.start()

        logger.info("Sistema cerebral inicializado. GUI ativa.")

        # Loop principal
        cycle_count = 0
        try:
            while True:
                await asyncio.sleep(12)
                cycle_count += 1

                # Estímulo periódico
                if brain.neurons and random.random() < 0.55:
                    nid = random.choice(list(brain.neurons.keys()))
                    await brain.stimulate(nid, random.uniform(0.2, 0.9))

                # Pensamento consciente periódico
                if random.random() < 0.18:
                    thought = await brain.think()
                    logger.info(f"Ciclo {cycle_count}: {thought}")

                # Ciclo quântico ocasional
                if random.random() < 0.12:
                    await brain.quantum_stimulus()

                # Salvamento periódico
                if cycle_count % 30 == 0:
                    brain.save_state()

        except KeyboardInterrupt:
            logger.info("Interrompido pelo usuário")

    except Exception as e:
        logger.critical(f"Erro crítico no sistema: {e}", exc_info=True)
        raise

    finally:
        # Shutdown seguro
        logger.info("Encerrando sistema...")
        if 'brain' in locals():
            await brain.shutdown()


def main():
    """Ponto de entrada principal"""
    if sys.platform == "win32":
        asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())
    
    try:
        asyncio.run(main_async())
    except KeyboardInterrupt:
        print("\n\nSistema encerrado pelo usuário.")
    except Exception as e:
        logger.critical(f"Erro fatal: {e}", exc_info=True)
        sys.exit(1)


if __name__ == "__main__":
    main()
