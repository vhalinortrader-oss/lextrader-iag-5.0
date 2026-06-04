// VhalinorBrain5.java
package vhalinor.iag;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// ──────────────────────────────────────────────────────────────────────────────
// ENUMS E TIPOS
// ──────────────────────────────────────────────────────────────────────────────

enum NeuronType {
    SENSORY,
    PROCESSING,
    MEMORY,
    DECISION,
    OUTPUT,
    EMOTION,
    QUANTUM,
    META
}

enum PerformanceMode {
    ECO("Eco"),
    BALANCED("Balanced"),
    TURBO("Turbo");
    
    private final String value;
    
    PerformanceMode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// LOGGING
// ──────────────────────────────────────────────────────────────────────────────

class VhalinorLogger {
    private String name;
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public VhalinorLogger(String name) {
        this.name = name;
    }
    
    public void info(String message) {
        log("INFO", message);
    }
    
    public void debug(String message) {
        log("DEBUG", message);
    }
    
    public void warning(String message) {
        log("WARNING", message);
    }
    
    public void error(String message) {
        log("ERROR", message);
    }
    
    public void error(String message, Exception e) {
        log("ERROR", message + ": " + e.getMessage());
        e.printStackTrace();
    }
    
    public void critical(String message) {
        log("CRITICAL", message);
    }
    
    public void critical(String message, Exception e) {
        log("CRITICAL", message + ": " + e.getMessage());
        e.printStackTrace();
    }
    
    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        System.out.printf("%s | %-7s | %s%n", timestamp, level, message);
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// NEURON CLASS
// ──────────────────────────────────────────────────────────────────────────────

class Neuron implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String filePath;
    private NeuronType type;
    private double activation;
    private double threshold;
    private double importance;
    private double energyCost;
    private LocalDateTime lastFire;
    private int fireCount;
    private Set<String> connectionsOut;
    private Set<String> connectionsIn;
    private List<String> tags;
    private Deque<Double> activationHistory;
    
    private static final int HISTORY_SIZE = 120;
    private static final Random random = new Random();
    
    public Neuron(String id, String filePath, NeuronType type) {
        this.id = id;
        this.filePath = filePath;
        this.type = type;
        this.activation = 0.0;
        this.threshold = 0.48 + random.nextDouble() * 0.2;
        this.importance = 0.7 + random.nextDouble() * 0.7;
        this.energyCost = 0.8 + random.nextDouble() * 1.4;
        this.fireCount = 0;
        this.connectionsOut = new HashSet<>();
        this.connectionsIn = new HashSet<>();
        this.tags = new ArrayList<>();
        this.activationHistory = new ArrayDeque<>(HISTORY_SIZE);
    }
    
    public double stimulate(double strength, double energyAvailable) {
        double effective = Math.min(strength, energyAvailable * 0.8);
        activation += effective * (1.0 + (random.nextDouble() * 0.16 - 0.08));
        activation = Math.min(1.8, Math.max(0.0, activation));
        
        if (activation >= threshold) {
            double fired = fire();
            activation *= 0.12; // Vazamento forte após disparo
            return fired;
        }
        return 0.0;
    }
    
    private double fire() {
        fireCount++;
        lastFire = LocalDateTime.now();
        double output = Math.min(1.0, activation * 0.92);
        activationHistory.add(output);
        if (activationHistory.size() > HISTORY_SIZE) {
            activationHistory.pollFirst();
        }
        return output;
    }
    
    public double getAvgActivation() {
        if (activationHistory.isEmpty()) return 0.0;
        return activationHistory.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
    
    public boolean isActive() {
        return getAvgActivation() > 0.15;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getFilePath() { return filePath; }
    public NeuronType getType() { return type; }
    public double getActivation() { return activation; }
    public void setActivation(double activation) { this.activation = activation; }
    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }
    public double getImportance() { return importance; }
    public void setImportance(double importance) { this.importance = importance; }
    public double getEnergyCost() { return energyCost; }
    public LocalDateTime getLastFire() { return lastFire; }
    public int getFireCount() { return fireCount; }
    public void setFireCount(int fireCount) { this.fireCount = fireCount; }
    public Set<String> getConnectionsOut() { return connectionsOut; }
    public Set<String> getConnectionsIn() { return connectionsIn; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}

// ──────────────────────────────────────────────────────────────────────────────
// CONNECTION MANAGER
// ──────────────────────────────────────────────────────────────────────────────

class ConnectionManager {
    private Map<String, Map<String, Double>> weights;
    private int edgeCount;
    
    public ConnectionManager() {
        this.weights = new ConcurrentHashMap<>();
        this.edgeCount = 0;
    }
    
    public void add(String fromId, String toId, double weight) {
        if (fromId.equals(toId)) return;
        
        weight = Math.max(0.05, Math.min(1.8, weight));
        
        weights.computeIfAbsent(fromId, k -> new ConcurrentHashMap<>())
               .put(toId, weight);
        
        edgeCount++;
    }
    
    public void remove(String fromId, String toId) {
        Map<String, Double> fromMap = weights.get(fromId);
        if (fromMap != null && fromMap.remove(toId) != null) {
            edgeCount--;
        }
    }
    
    public double getWeight(String fromId, String toId) {
        Map<String, Double> fromMap = weights.get(fromId);
        if (fromMap == null) return 0.1;
        return fromMap.getOrDefault(toId, 0.1);
    }
    
    public void pruneWeak(double threshold) {
        int removed = 0;
        int limit = 150;
        
        List<String[]> toRemove = new ArrayList<>();
        
        for (Map.Entry<String, Map<String, Double>> fromEntry : weights.entrySet()) {
            String fromId = fromEntry.getKey();
            for (Map.Entry<String, Double> toEntry : fromEntry.getValue().entrySet()) {
                if (toEntry.getValue() < threshold) {
                    toRemove.add(new String[]{fromId, toEntry.getKey()});
                    if (toRemove.size() >= limit) break;
                }
            }
            if (toRemove.size() >= limit) break;
        }
        
        for (String[] pair : toRemove) {
            remove(pair[0], pair[1]);
            removed++;
        }
        
        if (removed > 0) {
            VhalinorLogger logger = new VhalinorLogger("Connections");
            logger.debug("Conexões podadas: " + removed + " removidas");
        }
    }
    
    public Set<String> getSuccessors(String nodeId) {
        Map<String, Double> map = weights.get(nodeId);
        return map != null ? map.keySet() : Collections.emptySet();
    }
    
    public int getEdgeCount() {
        return edgeCount;
    }
    
    public Map<String, Map<String, Double>> getAllWeights() {
        return weights;
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// MEMORY SYSTEM
// ──────────────────────────────────────────────────────────────────────────────

class MemoryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Object content;
    private List<String> tags;
    private double importance;
    private LocalDateTime created;
    private int accesses;
    private double sizeKb;
    
    public MemoryItem(Object content, List<String> tags, double importance) {
        this.content = content;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.importance = importance;
        this.created = LocalDateTime.now();
        this.accesses = 0;
        this.sizeKb = estimateSize(content) / 1024.0;
    }
    
    private int estimateSize(Object obj) {
        if (obj instanceof String) {
            return ((String) obj).length() * 2;
        }
        return 100; // Estimativa padrão
    }
    
    // Getters
    public Object getContent() { return content; }
    public List<String> getTags() { return tags; }
    public double getImportance() { return importance; }
    public LocalDateTime getCreated() { return created; }
    public int getAccesses() { return accesses; }
    public void incrementAccesses() { accesses++; }
    public double getSizeKb() { return sizeKb; }
}

class MemorySystem {
    private Map<String, MemoryItem> memories;
    private Map<String, Set<String>> index;
    private Map<String, Void> lru;
    private int maxItems;
    private AtomicLong accessCount;
    private AtomicLong hitCount;
    private static final VhalinorLogger logger = new VhalinorLogger("Memory");
    
    public MemorySystem() {
        this(2200);
    }
    
    public MemorySystem(int maxItems) {
        this.memories = new ConcurrentHashMap<>();
        this.index = new ConcurrentHashMap<>();
        this.lru = new LinkedHashMap<String, Void>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Void> eldest) {
                return size() > 600;
            }
        };
        this.maxItems = maxItems;
        this.accessCount = new AtomicLong(0);
        this.hitCount = new AtomicLong(0);
    }
    
    public String store(Object content) {
        return store(content, new ArrayList<>(), 0.6);
    }
    
    public String store(Object content, List<String> tags, double importance) {
        String memId = generateHash(content.toString());
        
        MemoryItem item = new MemoryItem(content, tags, importance);
        memories.put(memId, item);
        
        // Indexação simples
        if (content instanceof String) {
            String[] words = ((String) content).toLowerCase().split("\\s+");
            Set<String> uniqueWords = new HashSet<>();
            for (String w : words) {
                if (w.length() > 3) {
                    uniqueWords.add(w);
                }
            }
            for (String w : uniqueWords) {
                index.computeIfAbsent(w, k -> new HashSet<>()).add(memId);
            }
        }
        
        updateLru(memId);
        pruneIfNeeded();
        
        return memId;
    }
    
    private String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(String.format("%02x", digest[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(input.hashCode());
        }
    }
    
    public Object retrieve(String memId) {
        accessCount.incrementAndGet();
        MemoryItem item = memories.get(memId);
        if (item != null) {
            hitCount.incrementAndGet();
            item.incrementAccesses();
            updateLru(memId);
            return item.getContent();
        }
        return null;
    }
    
    public List<MemoryItem> search(String query, int limit) {
        if (query == null || query.isEmpty()) return new ArrayList<>();
        
        String[] qWords = query.toLowerCase().split("\\s+");
        Set<String> qWordSet = new HashSet<>();
        for (String w : qWords) {
            if (w.length() > 2) qWordSet.add(w);
        }
        
        Map<String, Double> scores = new HashMap<>();
        
        for (String word : qWordSet) {
            Set<String> memIds = index.get(word);
            if (memIds == null) continue;
            
            for (String memId : memIds) {
                MemoryItem item = memories.get(memId);
                if (item == null) continue;
                
                double score = item.getImportance() + (item.getAccesses() * 0.015);
                
                if (item.getContent() instanceof String) {
                    String content = ((String) item.getContent()).toLowerCase();
                    for (String w : qWordSet) {
                        if (content.contains(w)) {
                            score += 0.25;
                        }
                    }
                }
                
                scores.put(memId, scores.getOrDefault(memId, 0.0) + score);
            }
        }
        
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(scores.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        List<MemoryItem> results = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, sorted.size()); i++) {
            results.add(memories.get(sorted.get(i).getKey()));
        }
        
        return results;
    }
    
    private void updateLru(String memId) {
        lru.put(memId, null);
    }
    
    private void pruneIfNeeded() {
        while (memories.size() > maxItems) {
            // Remove o menos importante
            List<Map.Entry<String, MemoryItem>> candidates = new ArrayList<>(memories.entrySet());
            candidates.sort((a, b) -> {
                int cmp = Double.compare(a.getValue().getImportance(), b.getValue().getImportance());
                if (cmp != 0) return cmp;
                return Integer.compare(a.getValue().getAccesses(), b.getValue().getAccesses());
            });
            
            if (candidates.isEmpty()) break;
            
            String victimId = candidates.get(0).getKey();
            memories.remove(victimId);
            lru.remove(victimId);
            
            for (Set<String> s : index.values()) {
                s.remove(victimId);
            }
        }
    }
    
    public Map<String, Object> getStats() {
        double totalSizeKb = memories.values().stream()
            .mapToDouble(MemoryItem::getSizeKb)
            .sum();
        
        long hits = hitCount.get();
        long accesses = accessCount.get();
        double hitRate = accesses > 0 ? (double) hits / accesses : 0.0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", memories.size());
        stats.put("lru_size", lru.size());
        stats.put("hit_rate", hitRate);
        stats.put("total_size_mb", totalSizeKb / 1024.0);
        
        return stats;
    }
    
    public Map<String, MemoryItem> getMemories() {
        return memories;
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// ML BRAIN
// ──────────────────────────────────────────────────────────────────────────────

class MLBrain {
    private List<double[]> featuresHistory;
    private LocalDateTime lastTrain;
    private Random random = new Random();
    private static final VhalinorLogger logger = new VhalinorLogger("ML");
    
    // Simulações de modelos ML
    private double[] scalerMean;
    private double[] scalerStd;
    private List<double[]> predictorData;
    
    public MLBrain() {
        this.featuresHistory = new ArrayList<>();
        this.predictorData = new ArrayList<>();
    }
    
    public void train(List<double[]> featuresList) {
        if (featuresList.isEmpty()) return;
        if (featuresList.size() < 20) return;
        
        // Normalização simulada
        int featureDim = featuresList.get(0).length;
        scalerMean = new double[featureDim];
        scalerStd = new double[featureDim];
        
        for (int j = 0; j < featureDim; j++) {
            double sum = 0;
            for (double[] f : featuresList) {
                sum += f[j];
            }
            scalerMean[j] = sum / featuresList.size();
            
            double sumSq = 0;
            for (double[] f : featuresList) {
                double diff = f[j] - scalerMean[j];
                sumSq += diff * diff;
            }
            scalerStd[j] = Math.sqrt(sumSq / featuresList.size());
            if (scalerStd[j] < 1e-8) scalerStd[j] = 1.0;
        }
        
        // Preparar dados para preditor
        predictorData.clear();
        for (int i = 0; i < featuresList.size() - 1; i++) {
            predictorData.add(featuresList.get(i));
        }
        
        lastTrain = LocalDateTime.now();
        logger.debug("Treinamento ML concluído: " + featuresList.size() + " amostras");
    }
    
    public double predictNextActivation(double[] currentFeatures) {
        if (predictorData.isEmpty() || scalerMean == null) {
            return 0.4;
        }
        
        // Simulação simples de predição
        double[] normalized = normalizeFeatures(currentFeatures);
        double sum = 0;
        for (double v : normalized) {
            sum += v;
        }
        return 0.3 + (sum / normalized.length) * 0.5;
    }
    
    private double[] normalizeFeatures(double[] features) {
        if (scalerMean == null) return features;
        
        double[] normalized = new double[features.length];
        for (int i = 0; i < Math.min(features.length, scalerMean.length); i++) {
            normalized[i] = (features[i] - scalerMean[i]) / scalerStd[i];
        }
        return normalized;
    }
    
    public int[] detectAnomalies(List<double[]> features) {
        int[] results = new int[features.size()];
        for (int i = 0; i < features.size(); i++) {
            // Simulação: detecta outliers baseado em desvio padrão
            double[] norm = normalizeFeatures(features.get(i));
            double maxDev = 0;
            for (double v : norm) {
                maxDev = Math.max(maxDev, Math.abs(v));
            }
            results[i] = maxDev > 3.0 ? -1 : 1;
        }
        return results;
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// QUANTUM SIMULATOR
// ──────────────────────────────────────────────────────────────────────────────

class QuantumSimulator {
    private int nQubits;
    private int dim;
    private double[] state; // Representação simplificada (probabilidades)
    private Random random = new Random();
    
    public QuantumSimulator() {
        this(7);
    }
    
    public QuantumSimulator(int nQubits) {
        this.nQubits = nQubits;
        this.dim = 1 << nQubits;
        this.state = new double[dim];
        this.state[0] = 1.0; // Estado inicial |000...0⟩
    }
    
    public void hadamard(int target) {
        // Simulação simplificada de Hadamard
        for (int i = 0; i < dim; i++) {
            state[i] = state[i] * 0.5 + random.nextDouble() * 0.5;
        }
        normalize();
    }
    
    public void pauliX(int target) {
        // Simulação de Pauli-X
        int mask = 1 << target;
        for (int i = 0; i < dim; i++) {
            if ((i & mask) != 0) {
                int j = i ^ mask;
                double temp = state[i];
                state[i] = state[j];
                state[j] = temp;
            }
        }
    }
    
    public void cnot(int control, int target) {
        // Simulação de CNOT
        int maskControl = 1 << control;
        int maskTarget = 1 << target;
        
        for (int i = 0; i < dim; i++) {
            if ((i & maskControl) != 0) {
                int j = i ^ maskTarget;
                double temp = state[i];
                state[i] = state[j];
                state[j] = temp;
            }
        }
    }
    
    private void normalize() {
        double sum = 0;
        for (double v : state) {
            sum += v * v;
        }
        if (sum < 1e-14) return;
        
        double norm = Math.sqrt(sum);
        for (int i = 0; i < state.length; i++) {
            state[i] /= norm;
        }
    }
    
    public int measure() {
        double[] probs = new double[dim];
        double sum = 0;
        
        for (int i = 0; i < dim; i++) {
            probs[i] = state[i] * state[i];
            sum += probs[i];
        }
        
        // Normaliza
        if (sum < 1e-14) {
            Arrays.fill(state, 0);
            state[0] = 1.0;
            return 0;
        }
        
        for (int i = 0; i < dim; i++) {
            probs[i] /= sum;
        }
        
        // Escolhe resultado
        double r = random.nextDouble();
        double accum = 0;
        int outcome = dim - 1;
        
        for (int i = 0; i < dim; i++) {
            accum += probs[i];
            if (r < accum) {
                outcome = i;
                break;
            }
        }
        
        // Colapso
        Arrays.fill(state, 0);
        state[outcome] = 1.0;
        
        return outcome;
    }
    
    public double vonNeumannEntropy() {
        double[] probs = new double[dim];
        double sum = 0;
        
        for (int i = 0; i < dim; i++) {
            probs[i] = state[i] * state[i];
            sum += probs[i];
        }
        
        if (sum < 1e-14) return 0.0;
        
        double entropy = 0;
        for (int i = 0; i < dim; i++) {
            double p = probs[i] / sum;
            if (p > 1e-12) {
                entropy -= p * (Math.log(p) / Math.log(2));
            }
        }
        
        return entropy;
    }
    
    public Map<String, Object> runCycle(int depth) {
        long startTime = System.currentTimeMillis();
        
        for (int d = 0; d < depth; d++) {
            int q = random.nextInt(nQubits);
            if (random.nextDouble() < 0.6) {
                hadamard(q);
            } else {
                pauliX(q);
            }
            
            if (nQubits >= 2 && random.nextDouble() < 0.4) {
                int c = random.nextInt(nQubits - 1);
                int t = c + 1 + random.nextInt(nQubits - c - 1);
                cnot(c, t);
            }
        }
        
        int result = measure();
        String resultBin = Integer.toBinaryString(result);
        while (resultBin.length() < nQubits) {
            resultBin = "0" + resultBin;
        }
        
        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        
        // Probabilidades top 5
        double[] probs = new double[dim];
        double sum = 0;
        for (int i = 0; i < dim; i++) {
            probs[i] = state[i] * state[i];
            sum += probs[i];
        }
        
        List<Double> topProbs = new ArrayList<>();
        for (int i = 0; i < Math.min(5, dim); i++) {
            topProbs.add(probs[i] / sum);
        }
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result_bin", resultBin);
        resultMap.put("entropy", vonNeumannEntropy());
        resultMap.put("int_value", result);
        resultMap.put("simulation_time_s", duration);
        resultMap.put("qubits", nQubits);
        resultMap.put("probs_top5", topProbs);
        
        return resultMap;
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// VHALINOR BRAIN
// ──────────────────────────────────────────────────────────────────────────────

class VhalinorBrain {
    private static final VhalinorLogger logger = new VhalinorLogger("Brain");
    
    private Path basePath;
    private Map<String, Neuron> neurons;
    private ConnectionManager connections;
    private MemorySystem memory;
    private MLBrain ml;
    private QuantumSimulator quantum;
    private double energy;
    private PerformanceMode mode;
    private Path stateFile;
    
    private ScheduledExecutorService scheduler;
    private boolean running;
    
    public VhalinorBrain() {
        this("./vhalinor_iag");
    }
    
    public VhalinorBrain(String basePathStr) {
        this.basePath = Paths.get(basePathStr).toAbsolutePath().normalize();
        this.neurons = new ConcurrentHashMap<>();
        this.connections = new ConnectionManager();
        this.memory = new MemorySystem(2200);
        this.ml = new MLBrain();
        this.quantum = new QuantumSimulator(7);
        this.energy = 1200.0;
        this.mode = PerformanceMode.BALANCED;
        this.stateFile = basePath.resolve("brain_state.dat");
        this.running = true;
        
        loadOrCreateNeurons();
        loadConnections();
        startBackgroundMaintenance();
    }
    
    private void loadOrCreateNeurons() {
        logger.info("Carregando neurônios...");
        
        try {
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
                logger.info("Diretório base criado: " + basePath);
            }
            
            int count = 0;
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(basePath)) {
                for (Path file : stream) {
                    if (Files.isRegularFile(file)) {
                        String name = file.getFileName().toString().toLowerCase();
                        if (name.endsWith(".java") || name.endsWith(".txt") || 
                            name.endsWith(".json") || name.endsWith(".md")) {
                            
                            String fid = generateId(file.toString());
                            NeuronType type = guessType(file);
                            
                            Neuron neuron = new Neuron(fid, file.toString(), type);
                            neurons.put(fid, neuron);
                            count++;
                        }
                    }
                }
            }
            
            if (count == 0) {
                logger.warning("Nenhum neurônio encontrado. Criando exemplos...");
                createSampleNeurons();
            } else {
                logger.info(count + " neurônios carregados");
            }
            
        } catch (IOException e) {
            logger.error("Erro ao carregar neurônios", e);
        }
    }
    
    private String generateId(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                sb.append(String.format("%02x", digest[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(input.hashCode()).substring(0, 14);
        }
    }
    
    private NeuronType guessType(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        
        if (name.contains("quantum") || name.contains("qubit")) return NeuronType.QUANTUM;
        if (name.contains("decision") || name.contains("logic")) return NeuronType.DECISION;
        if (name.contains("emotion") || name.contains("mood")) return NeuronType.EMOTION;
        if (name.contains("output") || name.contains("response")) return NeuronType.OUTPUT;
        if (name.contains("memory") || name.contains("recall")) return NeuronType.MEMORY;
        if (name.contains("meta") || name.contains("self")) return NeuronType.META;
        if (name.contains("sensor") || name.contains("input")) return NeuronType.SENSORY;
        
        return NeuronType.PROCESSING;
    }
    
    private void createSampleNeurons() {
        String[] sampleFiles = {
            "sensor_input.java",
            "quantum_processor.java",
            "decision_logic.java",
            "memory_core.json",
            "emotional_state.md"
        };
        
        for (String filename : sampleFiles) {
            Path filePath = basePath.resolve(filename);
            try {
                Files.createFile(filePath);
                String fid = generateId(filePath.toString());
                NeuronType type = guessType(filePath);
                
                Neuron neuron = new Neuron(fid, filePath.toString(), type);
                neurons.put(fid, neuron);
                
            } catch (IOException e) {
                logger.error("Erro ao criar neurônio de exemplo", e);
            }
        }
        
        logger.info(sampleFiles.length + " neurônios de exemplo criados");
    }
    
    private void loadConnections() {
        Path connFile = basePath.resolve("connections.json");
        
        if (Files.exists(connFile)) {
            try {
                String content = new String(Files.readAllBytes(connFile));
                // Parsing simplificado de JSON
                // Em produção usar biblioteca como Jackson
                logger.info("Conexões carregadas (simulado)");
            } catch (IOException e) {
                logger.error("Erro ao carregar conexões", e);
                createInitialConnections();
            }
        } else {
            createInitialConnections();
        }
    }
    
    private void createInitialConnections() {
        List<String> neuronIds = new ArrayList<>(neurons.keySet());
        if (neuronIds.size() < 2) return;
        
        Random random = new Random();
        
        for (String nid : neuronIds) {
            int numConnections = 2 + random.nextInt(Math.min(7, neuronIds.size() - 1));
            
            List<String> targets = new ArrayList<>(neuronIds);
            targets.remove(nid);
            Collections.shuffle(targets);
            
            for (int i = 0; i < Math.min(numConnections, targets.size()); i++) {
                String target = targets.get(i);
                double weight = 0.2 + random.nextDouble() * 0.9;
                connections.add(nid, target, weight);
            }
        }
        
        logger.info("Conexões iniciais criadas: " + connections.getEdgeCount());
    }
    
    private void startBackgroundMaintenance() {
        scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (!running) return;
                
                int interval = (mode == PerformanceMode.ECO) ? 45 : 18;
                Thread.sleep(interval * 1000L);
                
                // Recarga de energia
                energy = Math.min(1800.0, energy + 40 + new Random().nextInt(81));
                
                // Poda de conexões fracas
                if (Math.random() < 0.4) {
                    connections.pruneWeak(0.12);
                }
                
                // Treinamento ML
                if (Math.random() < 0.25) {
                    trainMLCycle();
                }
                
                // Coleta de lixo
                System.gc();
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Erro na manutenção", e);
            }
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    private void trainMLCycle() {
        List<double[]> features = new ArrayList<>();
        int neuronsToSample = Math.min(400, neurons.size());
        
        List<String> sampleKeys = new ArrayList<>(neurons.keySet());
        Collections.shuffle(sampleKeys);
        
        for (int i = 0; i < Math.min(neuronsToSample, sampleKeys.size()); i++) {
            Neuron neuron = neurons.get(sampleKeys.get(i));
            double[] feat = new double[]{
                neuron.getActivation(),
                neuron.getAvgActivation(),
                neuron.getFireCount() / 100.0,
                neuron.getConnectionsOut().size(),
                neuron.getConnectionsIn().size(),
                neuron.getImportance(),
                neuron.getType() == NeuronType.QUANTUM ? 1.0 : 0.0
            };
            features.add(feat);
        }
        
        ml.train(features);
        logger.debug("Ciclo ML concluído: " + features.size() + " amostras");
    }
    
    public CompletableFuture<Double> stimulate(String neuronId, double strength) {
        return CompletableFuture.supplyAsync(() -> {
            Neuron neuron = neurons.get(neuronId);
            if (neuron == null) {
                logger.warning("Neurônio não encontrado: " + neuronId);
                return 0.0;
            }
            
            double cost = neuron.getEnergyCost() * Math.pow(strength, 1.3);
            
            if (energy < cost * 0.6) {
                return 0.0;
            }
            
            energy -= cost;
            double fired = neuron.stimulate(strength, energy);
            
            if (fired > 0) {
                // Propaga para neurônios conectados
                Set<String> successors = connections.getSuccessors(neuronId);
                for (String target : successors) {
                    double weight = connections.getWeight(neuronId, target);
                    double propStrength = fired * weight * 0.7;
                    stimulate(target, propStrength);
                }
            }
            
            return fired;
        });
    }
    
    public CompletableFuture<String> think(String stimulus) {
        return CompletableFuture.supplyAsync(() -> {
            List<Neuron> active = neurons.values().stream()
                .filter(Neuron::isActive)
                .collect(Collectors.toList());
            
            if (active.isEmpty()) {
                return "Sistema em repouso profundo...";
            }
            
            List<Neuron> topNeurons = active.stream()
                .sorted((a, b) -> Double.compare(b.getAvgActivation(), a.getAvgActivation()))
                .limit(5)
                .collect(Collectors.toList());
            
            List<String> concepts = new ArrayList<>();
            for (Neuron n : topNeurons) {
                switch (n.getType()) {
                    case EMOTION: concepts.add("emoção"); break;
                    case DECISION: concepts.add("decisão crítica"); break;
                    case QUANTUM: concepts.add("incerteza quântica"); break;
                    default: concepts.add(n.getType().name().toLowerCase());
                }
            }
            
            boolean quantumActive = topNeurons.stream()
                .anyMatch(n -> n.getType() == NeuronType.QUANTUM);
            
            String quantumResult = "";
            if (quantumActive && Math.random() < 0.3) {
                Map<String, Object> qResult = quantum.runCycle(10);
                quantumResult = String.format("Entropia quântica: %.3f. ", 
                    (double) qResult.get("entropy"));
            }
            
            String stimulusStr = (stimulus == null || stimulus.isEmpty()) ? 
                "fluxo interno" : stimulus;
            
            return String.format("Processando %s. Neurônios dominantes: %s. %sEnergia: %.0f | Ativos: %d",
                stimulusStr,
                String.join(", ", concepts),
                quantumResult,
                energy,
                active.size()
            );
        });
    }
    
    public CompletableFuture<Map<String, Object>> quantumStimulus() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> result = quantum.runCycle(10);
            int measurementInt = (int) result.get("int_value");
            
            List<Neuron> quantumNeurons = neurons.values().stream()
                .filter(n -> n.getType() == NeuronType.QUANTUM)
                .collect(Collectors.toList());
            
            int stimulatedCount = 0;
            for (int i = 0; i < Math.min(quantumNeurons.size(), 7); i++) {
                int bit = (measurementInt >> i) & 1;
                if (bit == 1) {
                    Neuron neuron = quantumNeurons.get(i);
                    double strength = 0.4 + Math.random() * 0.7;
                    stimulate(neuron.getId(), strength);
                    stimulatedCount++;
                }
            }
            
            result.put("stimulated_neurons", stimulatedCount);
            return result;
        });
    }
    
    public void saveState() {
        try {
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }
            
            Map<String, Object> state = new HashMap<>();
            state.put("energy", energy);
            state.put("mode", mode.name());
            
            Map<String, Map<String, Object>> neuronsBasic = new HashMap<>();
            for (Map.Entry<String, Neuron> entry : neurons.entrySet()) {
                Neuron n = entry.getValue();
                Map<String, Object> nData = new HashMap<>();
                nData.put("activation", n.getActivation());
                nData.put("fire_count", n.getFireCount());
                nData.put("importance", n.getImportance());
                nData.put("threshold", n.getThreshold());
                neuronsBasic.put(entry.getKey(), nData);
            }
            state.put("neurons_basic", neuronsBasic);
            
            List<Object[]> connectionsList = new ArrayList<>();
            for (Map.Entry<String, Map<String, Double>> fromEntry : 
                 connections.getAllWeights().entrySet()) {
                String fromId = fromEntry.getKey();
                for (Map.Entry<String, Double> toEntry : fromEntry.getValue().entrySet()) {
                    connectionsList.add(new Object[]{fromId, toEntry.getKey(), toEntry.getValue()});
                }
            }
            state.put("connections", connectionsList);
            
            state.put("timestamp", LocalDateTime.now().toString());
            state.put("version", "5.0");
            
            // Salvar como JSON
            Path jsonFile = basePath.resolve("brain_state.json");
            // Em produção usar biblioteca JSON
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"energy\":").append(energy).append(",");
            json.append("\"mode\":\"").append(mode).append("\",");
            json.append("\"timestamp\":\"").append(LocalDateTime.now()).append("\"");
            json.append("}");
            
            Files.write(jsonFile, json.toString().getBytes());
            
            // Salvar binário
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    Files.newOutputStream(stateFile))) {
                oos.writeObject(state);
            }
            
            logger.info("Estado salvo em " + stateFile);
            
        } catch (IOException e) {
            logger.error("Erro ao salvar estado", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void loadState() {
        if (!Files.exists(stateFile)) {
            logger.info("Nenhum estado anterior encontrado");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(stateFile))) {
            
            Map<String, Object> state = (Map<String, Object>) ois.readObject();
            
            Map<String, Map<String, Object>> neuronsBasic = 
                (Map<String, Map<String, Object>>) state.get("neurons_basic");
            
            if (neuronsBasic != null) {
                for (Map.Entry<String, Map<String, Object>> entry : neuronsBasic.entrySet()) {
                    Neuron neuron = neurons.get(entry.getKey());
                    if (neuron != null) {
                        Map<String, Object> data = entry.getValue();
                        neuron.setActivation((Double) data.getOrDefault("activation", 0.0));
                        neuron.setFireCount((Integer) data.getOrDefault("fire_count", 0));
                        neuron.setImportance((Double) data.getOrDefault("importance", 1.0));
                        neuron.setThreshold((Double) data.getOrDefault("threshold", 0.55));
                    }
                }
            }
            
            energy = (Double) state.getOrDefault("energy", 1200.0);
            String modeStr = (String) state.get("mode");
            if (modeStr != null) {
                try {
                    mode = PerformanceMode.valueOf(modeStr);
                } catch (IllegalArgumentException e) {
                    mode = PerformanceMode.BALANCED;
                }
            }
            
            logger.info("Estado carregado (versão: " + state.get("version") + ")");
            
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Erro ao carregar estado", e);
        }
    }
    
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            running = false;
            if (scheduler != null) {
                scheduler.shutdown();
                try {
                    if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                        scheduler.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    scheduler.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
            saveState();
            logger.info("Cérebro desligado com segurança");
        });
    }
    
    // Getters
    public Map<String, Neuron> getNeurons() { return neurons; }
    public ConnectionManager getConnections() { return connections; }
    public MemorySystem getMemory() { return memory; }
    public double getEnergy() { return energy; }
    public PerformanceMode getMode() { return mode; }
}

// ──────────────────────────────────────────────────────────────────────────────
// INTERFACE GRÁFICA SIMPLIFICADA (CONSOLE)
// ──────────────────────────────────────────────────────────────────────────────

class BrainConsole {
    private VhalinorBrain brain;
    private Scanner scanner;
    private boolean running;
    private static final VhalinorLogger logger = new VhalinorLogger("Console");
    
    public BrainConsole(VhalinorBrain brain) {
        this.brain = brain;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void run() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   VHALINOR.IAG 5.0 - Console de Controle");
        System.out.println("=".repeat(60));
        
        printHelp();
        
        while (running) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.isEmpty()) continue;
            
            if (input.equals("exit") || input.equals("quit")) {
                running = false;
                break;
            }
            
            processCommand(input);
        }
    }
    
    private void processCommand(String input) {
        String[] parts = input.split("\\s+");
        String cmd = parts[0];
        
        switch (cmd) {
            case "status":
            case "stats":
                printStatus();
                break;
                
            case "think":
                String stimulus = parts.length > 1 ? 
                    String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)) : "";
                
                brain.think(stimulus).thenAccept(thought -> {
                    System.out.println("\n[PENSAMENTO] " + thought);
                });
                break;
                
            case "stimulate":
                if (parts.length >= 3) {
                    String nid = parts[1];
                    try {
                        double strength = Double.parseDouble(parts[2]);
                        brain.stimulate(nid, strength).thenAccept(result -> {
                            System.out.printf("Estímulo aplicado: resultado = %.3f%n", result);
                        });
                    } catch (NumberFormatException e) {
                        System.out.println("Força inválida");
                    }
                } else {
                    // Estimular aleatório
                    List<String> ids = new ArrayList<>(brain.getNeurons().keySet());
                    if (!ids.isEmpty()) {
                        String nid = ids.get(new Random().nextInt(ids.size()));
                        double strength = 0.3 + Math.random() * 0.9;
                        brain.stimulate(nid, strength).thenAccept(result -> {
                            Neuron n = brain.getNeurons().get(nid);
                            System.out.printf("Neurônio %s estimulado: força=%.2f, resultado=%.3f%n",
                                n.getType().name(), strength, result);
                        });
                    }
                }
                break;
                
            case "quantum":
                brain.quantumStimulus().thenAccept(result -> {
                    System.out.println("Ciclo quântico concluído:");
                    System.out.println("  Resultado: " + result.get("result_bin"));
                    System.out.println("  Entropia: " + String.format("%.3f", result.get("entropy")));
                    System.out.println("  Neurônios estimulados: " + result.get("stimulated_neurons"));
                });
                break;
                
            case "save":
                brain.saveState();
                System.out.println("Estado salvo.");
                break;
                
            case "load":
                brain.loadState();
                System.out.println("Estado carregado.");
                break;
                
            case "neurons":
                listNeurons();
                break;
                
            case "help":
                printHelp();
                break;
                
            default:
                System.out.println("Comando desconhecido: " + cmd);
                System.out.println("Digite 'help' para ajuda");
        }
    }
    
    private void printStatus() {
        long active = brain.getNeurons().values().stream()
            .filter(Neuron::isActive)
            .count();
        
        System.out.println("\n📊 STATUS DO SISTEMA");
        System.out.println("   Energia: " + String.format("%.1f", brain.getEnergy()));
        System.out.println("   Neurônios: " + brain.getNeurons().size());
        System.out.println("   Ativos: " + active);
        System.out.println("   Conexões: " + brain.getConnections().getEdgeCount());
        System.out.println("   Modo: " + brain.getMode().getValue());
        
        Map<String, Object> memStats = brain.getMemory().getStats();
        System.out.println("   Memória: " + memStats.get("total") + " itens");
        System.out.println("   Taxa de acerto: " + String.format("%.1f%%", 
            (double) memStats.get("hit_rate") * 100));
    }
    
    private void listNeurons() {
        System.out.println("\n📋 NEURÔNIOS (amostra):");
        
        List<Neuron> neuronList = new ArrayList<>(brain.getNeurons().values());
        Collections.shuffle(neuronList);
        
        int count = 0;
        for (Neuron n : neuronList) {
            if (count++ >= 20) break;
            
            System.out.printf("  [%s] %s | ativ=%.3f | fires=%d | imp=%.2f | cons=%d%n",
                n.getType().name().substring(0, 3),
                n.getId().substring(0, 8),
                n.getAvgActivation(),
                n.getFireCount(),
                n.getImportance(),
                n.getConnectionsOut().size() + n.getConnectionsIn().size()
            );
        }
    }
    
    private void printHelp() {
        System.out.println("\n📚 COMANDOS DISPONÍVEIS:");
        System.out.println("  status               - Mostra status do sistema");
        System.out.println("  think [mensagem]     - Gera um pensamento");
        System.out.println("  stimulate [id força] - Estimula um neurônio");
        System.out.println("  quantum              - Executa ciclo quântico");
        System.out.println("  neurons              - Lista neurônios");
        System.out.println("  save                 - Salva estado");
        System.out.println("  load                 - Carrega estado");
        System.out.println("  help                 - Mostra esta ajuda");
        System.out.println("  exit                 - Sai do programa");
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// CLASSE PRINCIPAL
// ──────────────────────────────────────────────────────────────────────────────

public class VhalinorBrain5 {
    private static final VhalinorLogger logger = new VhalinorLogger("Main");
    
    public static void main(String[] args) {
        System.out.println("""
            ╔═══════════════════════════════════════════════╗
            ║      VHALINOR.IAG 5.0  -  2026                ║
            ║   Sistema Cerebral Artificial Ultra-Otimizado ║
            ╚═══════════════════════════════════════════════╝
            """);
        
        VhalinorBrain brain = new VhalinorBrain();
        brain.loadState();
        
        // Estímulos iniciais
        logger.info("Aplicando estímulos iniciais...");
        List<String> neuronIds = new ArrayList<>(brain.getNeurons().keySet());
        Random random = new Random();
        
        for (int i = 0; i < 15 && !neuronIds.isEmpty(); i++) {
            String nid = neuronIds.get(random.nextInt(neuronIds.size()));
            double strength = 0.4 + random.nextDouble() * 0.9;
            brain.stimulate(nid, strength);
        }
        
        // Iniciar console
        BrainConsole console = new BrainConsole(brain);
        
        // Thread para processamento assíncrono
        ScheduledExecutorService cycleScheduler = Executors.newScheduledThreadPool(1);
        cycleScheduler.scheduleAtFixedRate(() -> {
            try {
                if (!neuronIds.isEmpty() && random.nextDouble() < 0.55) {
                    String nid = neuronIds.get(random.nextInt(neuronIds.size()));
                    double strength = 0.2 + random.nextDouble() * 0.7;
                    brain.stimulate(nid, strength);
                }
                
                if (random.nextDouble() < 0.18) {
                    brain.think("ciclo automático").thenAccept(thought -> {
                        logger.info("Ciclo automático: " + thought);
                    });
                }
                
                if (random.nextDouble() < 0.12) {
                    brain.quantumStimulus();
                }
                
            } catch (Exception e) {
                logger.error("Erro no ciclo automático", e);
            }
        }, 12, 12, TimeUnit.SECONDS);
        
        // Executar console
        try {
            console.run();
        } finally {
            // Shutdown
            cycleScheduler.shutdown();
            try {
                if (!cycleScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    cycleScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                cycleScheduler.shutdownNow();
            }
            
            brain.shutdown().join();
            logger.info("Sistema encerrado.");
        }
    }
}