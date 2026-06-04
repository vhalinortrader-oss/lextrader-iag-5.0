# 🧠 VHALINOR.IAG 4.5

**Virtual Hybrid Advanced Learning Intelligence Neural Optimized Reasoning**

Sistema de Inteligência Artificial Avançado com Processamento Quântico, Machine Learning e Interface Gráfica Integrada.

## 🎯 Visão Geral

VHALINOR.IAG é um sistema de IA de próxima geração que combina:

- 🧠 **Rede Neural Artificial** com milhares de neurônios interconectados
- 🔬 **Processamento Quântico** simulado para computação avançada
- 🤖 **Machine Learning** integrado com detecção de anomalias
- 💾 **Sistema de Memória** hierárquico e otimizado
- 🖥️ **Interface Gráfica** em tempo real para monitoramento
- ⚡ **Performance Otimizada** com processamento assíncrono

## 🚀 Instalação Rápida

### 1. Instalar Dependências
```bash
python install_dependencies_complete.py
```

### 2. Executar Sistema
```bash
python start_vhalinor_iag.py
```

### 3. Ou Usar Versão Específica
```bash
# Versão com PyTorch (se disponível)
python ai_core/ação/CLAI_fallback.py

# Versão sem PyTorch
python ai_core/ação/CLAI_no_pytorch.py
```

## 📋 Requisitos

### Mínimos
- Python 3.7+
- 4 GB RAM
- 1 GB espaço em disco

### Recomendados
- Python 3.9+
- 8 GB RAM
- 2 GB espaço em disco
- GPU NVIDIA (opcional, para PyTorch)

### Dependências Principais
- `numpy` - Computação numérica
- `pandas` - Manipulação de dados
- `scikit-learn` - Machine Learning
- `matplotlib` - Visualização
- `networkx` - Análise de redes
- `tkinter` - Interface gráfica
- `asyncio` - Programação assíncrona

### Dependências Opcionais
- `torch` - PyTorch para IA avançada
- `transformers` - Modelos de linguagem
- `openai` - API OpenAI

## 🔧 Configuração

### Modos de Operação

| Modo | Descrição | Recursos | Uso |
|------|-----------|----------|-----|
| **dev** | Desenvolvimento | Limitados, logs detalhados | Desenvolvimento |
| **prod** | Produção | Máximos, logs mínimos | Produção |
| **test** | Teste | Mínimos, sem GUI | Testes automatizados |
| **demo** | Demonstração | Balanceados | Apresentações |
| **default** | Padrão | Equilibrados | Uso geral |

### Configuração Personalizada

```python
from VHALINOR_IAG_CONFIG import VhalinorConfig

config = VhalinorConfig(
    max_neurons=10000,
    memory_capacity_mb=100,
    quantum_enabled=True,
    performance_mode="balanced"
)
```

## 🎮 Como Usar

### Interface Gráfica
1. Execute `python start_vhalinor_iag.py`
2. Selecione modo de operação
3. Monitore métricas em tempo real
4. Use botões para executar funções

### Modo Console
```bash
python start_vhalinor_iag.py --no-gui
```

Comandos disponíveis:
- `status` - Status do sistema
- `metrics` - Métricas detalhadas
- `quantum` - Simulação quântica
- `train` - Treinamento ML
- `quit` - Encerrar

### Programático
```python
from ai_core.ação.CLAI_fallback import ContinuousLearningAI
import numpy as np

# Criar instância
clai = ContinuousLearningAI()

# Iniciar sistema
clai.start()

# Fazer predição
input_data = np.random.random(256)
prediction = clai.predict(input_data)

# Verificar status
status = clai.get_status()
print(f"Backend: {status['backend']}")
```

## 🧪 Funcionalidades

### 🧠 Sistema Neural
- **Neurônios Especializados**: Sensorial, Processamento, Memória, Decisão, Saída, Quântico
- **Conexões Dinâmicas**: Rede adaptativa com pesos ajustáveis
- **Ativação Inteligente**: Thresholds adaptativos por tipo
- **Energia Simulada**: Sistema de energia com regeneração

### 🔬 Processamento Quântico
- **Simulação de Qubits**: Até 10 qubits simultâneos
- **Portas Quânticas**: Hadamard, Pauli-X/Y/Z, CNOT
- **Entrelaçamento**: Correlações quânticas entre neurônios
- **Medição**: Colapso de estado com ativação neural

### 🤖 Machine Learning
- **Detecção de Anomalias**: Isolation Forest
- **Clustering**: K-Means adaptativo
- **Redução de Dimensionalidade**: PCA
- **Treinamento Contínuo**: Aprendizado incremental

### 💾 Sistema de Memória
- **Memória Hierárquica**: Curto prazo, longo prazo, cache
- **Tipos Especializados**: Episódica, Semântica, Procedural, Emocional
- **Busca Inteligente**: Indexação por palavras-chave
- **Compressão**: Otimização automática de espaço

### 📊 Monitoramento
- **Métricas em Tempo Real**: Ativação, energia, memória
- **Gráficos Dinâmicos**: Visualização de performance
- **Logs Estruturados**: Sistema de logging avançado
- **Profiling**: Análise de performance detalhada

## 🔍 Arquitetura

```
VHALINOR.IAG
├── 🧠 Neural Core
│   ├── Neurônios Especializados
│   ├── Matriz de Conexões
│   └── Sistema de Energia
├── 🔬 Quantum Engine
│   ├── Simulador de Qubits
│   ├── Portas Quânticas
│   └── Entrelaçamento
├── 🤖 ML Module
│   ├── Detecção de Anomalias
│   ├── Clustering
│   └── Treinamento Contínuo
├── 💾 Memory System
│   ├── Cache LRU
│   ├── Indexação
│   └── Compressão
└── 🖥️ Interface
    ├── GUI Tkinter
    ├── Console
    └── API Programática
```

## 📈 Performance

### Benchmarks Típicos
- **Neurônios**: 1.000 - 50.000 simultâneos
- **Conexões**: Até 500.000 links
- **Processamento**: 1.000+ operações/segundo
- **Memória**: 50-500 MB RAM
- **Latência**: < 10ms para predições

### Otimizações
- ✅ Processamento em lote
- ✅ Cache inteligente
- ✅ Lazy loading
- ✅ Async/await
- ✅ Garbage collection otimizado

## 🛠️ Desenvolvimento

### Estrutura do Projeto
```
vhalinor_iag/
├── ai_core/
│   └── ação/
│       ├── CLAI.py              # Versão PyTorch
│       ├── CLAI_fallback.py     # Versão adaptativa
│       └── CLAI_no_pytorch.py   # Versão Scikit-learn
├── models/                      # Modelos salvos
├── logs/                        # Arquivos de log
├── data/                        # Dados do sistema
├── quantum/                     # Dados quânticos
├── VHALINOR_IAG_CONFIG.py      # Configurações
├── start_vhalinor_iag.py       # Script principal
└── Inteligencia_artificial_central.py  # Core do sistema
```

### Extensibilidade
- **Novos Tipos de Neurônio**: Herdar de `OptimizedNeuron`
- **Algoritmos ML**: Integrar em `OptimizedMLModule`
- **Interfaces**: Adicionar em `OptimizedBrainGUI`
- **Configurações**: Estender `VhalinorConfig`

## 🔧 Solução de Problemas

### Erro: "Unable to import 'torch'"
```bash
# Solução 1: Instalar PyTorch
python fix_pytorch_simple.py

# Solução 2: Usar versão sem PyTorch
python ai_core/ação/CLAI_no_pytorch.py

# Solução 3: Usar versão adaptativa
python ai_core/ação/CLAI_fallback.py
```

### Erro: Dependências faltando
```bash
python install_dependencies_complete.py
```

### Performance baixa
1. Reduzir `max_neurons` na configuração
2. Usar modo `eco` para economia de recursos
3. Desabilitar processamento quântico se não necessário
4. Aumentar `batch_size` para processamento em lote

### Problemas de memória
1. Reduzir `memory_capacity_mb`
2. Ativar limpeza automática
3. Usar modo `test` para recursos mínimos

## 📚 Exemplos

### Exemplo 1: Uso Básico
```python
from ai_core.ação.CLAI_fallback import ContinuousLearningAI

# Criar e iniciar
clai = ContinuousLearningAI()
clai.start()

# Usar por 30 segundos
import time
time.sleep(30)

# Parar
clai.stop()
```

### Exemplo 2: Configuração Personalizada
```python
from VHALINOR_IAG_CONFIG import VhalinorConfig
from ai_core.ação.CLAI_fallback import ContinuousLearningAI

# Configuração personalizada
config = VhalinorConfig(
    max_neurons=5000,
    memory_capacity_mb=50,
    quantum_enabled=False
)

# Usar configuração
clai = ContinuousLearningAI(
    model_dir=config.models_path,
    memory_capacity=config.max_neurons,
    learning_rate=config.learning_rate
)
```

### Exemplo 3: Monitoramento
```python
import asyncio
from start_vhalinor_iag import initialize_vhalinor
from VHALINOR_IAG_CONFIG import get_config

async def monitor():
    config = get_config("demo")
    orchestrator = await initialize_vhalinor(config, None)
    
    for i in range(10):
        metrics = orchestrator.get_performance_metrics()
        print(f"Ciclo {i}: {metrics['active_neurons']} neurônios ativos")
        await asyncio.sleep(5)

asyncio.run(monitor())
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo LICENSE para detalhes.

## 🆘 Suporte

- **Documentação**: Este README
- **Exemplos**: Pasta `examples/`
- **Logs**: Pasta `logs/`
- **Configuração**: `VHALINOR_IAG_CONFIG.py`

## 🎉 Agradecimentos

- Comunidade Python
- Desenvolvedores de NumPy, Pandas, Scikit-learn
- Equipe PyTorch
- Contribuidores do projeto

---

**VHALINOR.IAG** - *Virtual Hybrid Advanced Learning Intelligence Neural Optimized Reasoning*

*Sistema de IA de próxima geração para aplicações avançadas*