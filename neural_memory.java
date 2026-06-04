package lextrader.iag.memory;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sistema de memória neural para aprendizado contínuo
 * Inspired by biological neural networks and cognitive architectures
 */
public class NeuralMemory {
    
    private static final Logger logger = LoggerFactory.getLogger(NeuralMemory.class);
    
    // ========== ENUMS ==========
    
    public enum MemoryType {
        TRADING_EXPERIENCE("trading_experience"),
        MARKET_PATTERN("market_pattern"),
        STRATEGY_PERFORMANCE("strategy_performance"),
        RISK_EVENT("risk_event"),
        SYSTEM_LEARNING("system_learning");
        
        private final String value;
        
        MemoryType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static MemoryType fromValue(String value) {
            for (MemoryType type : MemoryType.values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            return TRADING_EXPERIENCE; // Default
        }
    }
    
    // ========== INNER CLASSES ==========
    
    /**
     * Representa uma memória individual no sistema
     */
    public static class Memory {
        private String id;
        private String type;
        private Map<String, Object> data;
        private LocalDateTime timestamp;
        private double importance;
        private List<String> tags;
        private int accessCount;
        private LocalDateTime lastAccessed;
        private List<String> associations;
        private float[] embedding;
        
        public Memory(String id, String type, Map<String, Object> data, double importance, 
                     List<String> tags, float[] embedding) {
            this.id = id;
            this.type = type;
            this.data = new HashMap<>(data);
            this.timestamp = LocalDateTime.now();
            this.importance = importance;
            this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
            this.accessCount = 0;
            this.lastAccessed = LocalDateTime.now();
            this.associations = new ArrayList<>();
            this.embedding = embedding != null ? embedding.clone() : new float[0];
        }
        
        // Getters and setters
        public String getId() { return id; }
        public String getType() { return type; }
        public Map<String, Object> getData() { return Collections.unmodifiableMap(data); }
        public LocalDateTime getTimestamp() { return timestamp; }
        public double getImportance() { return importance; }
        public void setImportance(double importance) { this.importance = importance; }
        public List<String> getTags() { return Collections.unmodifiableList(tags); }
        public int getAccessCount() { return accessCount; }
        public void incrementAccessCount() { 
            this.accessCount++; 
            this.lastAccessed = LocalDateTime.now();
        }
        public LocalDateTime getLastAccessed() { return lastAccessed; }
        public List<String> getAssociations() { return Collections.unmodifiableList(associations); }
        public void addAssociation(String memoryId) {
            if (!associations.contains(memoryId)) {
                associations.add(memoryId);
            }
        }
        public float[] getEmbedding() { return embedding != null ? embedding.clone() : null; }
        public void setEmbedding(float[] embedding) { this.embedding = embedding != null ? embedding.clone() : null; }
    }
    
    /**
     * Resultado de recuperação de memória com pontuação de relevância
     */
    public static class MemoryResult {
        private Memory memory;
        private double relevanceScore;
        private List<Memory> associations;
        
        public MemoryResult(Memory memory, double relevanceScore, List<Memory> associations) {
            this.memory = memory;
            this.relevanceScore = relevanceScore;
            this.associations = new ArrayList<>(associations);
        }
        
        public Memory getMemory() { return memory; }
        public double getRelevanceScore() { return relevanceScore; }
        public List<Memory> getAssociations() { return Collections.unmodifiableList(associations); }
    }
    
    /**
     * Estatísticas do sistema de memória
     */
    public static class MemoryStats {
        private int totalMemories;
        private Map<String, Integer> memoryTypes;
        private double averageImportance;
        private int totalAccesses;
        private int indexSize;
        
        public MemoryStats(int totalMemories, Map<String, Integer> memoryTypes,
                          double averageImportance, int totalAccesses, int indexSize) {
            this.totalMemories = totalMemories;
            this.memoryTypes = new HashMap<>(memoryTypes);
            this.averageImportance = averageImportance;
            this.totalAccesses = totalAccesses;
            this.indexSize = indexSize;
        }
        
        public int getTotalMemories() { return totalMemories; }
        public Map<String, Integer> getMemoryTypes() { return Collections.unmodifiableMap(memoryTypes); }
        public double getAverageImportance() { return averageImportance; }
        public int getTotalAccesses() { return totalAccesses; }
        public int getIndexSize() { return indexSize; }
        
        @Override
        public String toString() {
            return String.format(
                "MemoryStats{totalMemories=%d, memoryTypes=%s, avgImportance=%.2f, totalAccesses=%d, indexSize=%d}",
                totalMemories, memoryTypes, averageImportance, totalAccesses, indexSize
            );
        }
    }
    
    // ========== FIELDS ==========
    
    private final String systemType;
    private final int maxMemories;
    private final List<Memory> memories;
    private final Map<String, List<Integer>> memoryIndex;
    private final Map<String, Set<String>> associations;
    private double learningRate;
    private double retentionRate;
    private final ReentrantReadWriteLock lock;
    
    // Constantes
    private static final int EMBEDDING_DIMENSIONS = 16;
    private static final double ASSOCIATION_THRESHOLD = 0.7;
    private static final double FORGET_IMPORTANCE_WEIGHT = 0.4;
    private static final double FORGET_ACCESS_WEIGHT = 0.3;
    private static final double FORGET_AGE_WEIGHT = 0.3;
    
    // ========== CONSTRUCTOR ==========
    
    public NeuralMemory(String systemType, int maxMemories) {
        this.systemType = systemType;
        this.maxMemories = maxMemories;
        this.memories = Collections.synchronizedList(new ArrayList<>());
        this.memoryIndex = new ConcurrentHashMap<>();
        this.associations = new ConcurrentHashMap<>();
        this.learningRate = 0.1;
        this.retentionRate = 0.95;
        this.lock = new ReentrantReadWriteLock();
    }
    
    public NeuralMemory(String systemType) {
        this(systemType, 10000);
    }
    
    // ========== PUBLIC METHODS ==========
    
    /**
     * Inicializa o sistema de memória neural
     */
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            logger.info("🧠 Inicializando Memória Neural...");
            
            // Carregar memórias persistentes se existirem
            loadPersistentMemories();
            
            // Inicializar estruturas de indexação
            initializeMemoryIndex();
            
            logger.info("✅ Memória Neural inicializada: {} memórias carregadas", memories.size());
        });
    }
    
    /**
     * Armazena uma nova memória no sistema
     */
    public CompletableFuture<String> storeMemory(Map<String, Object> data, MemoryType memoryType,
                                                double importance, List<String> tags) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                lock.writeLock().lock();
                
                String memoryId = String.format("mem_%d_%d", 
                    Instant.now().toEpochMilli(), memories.size());
                
                float[] embedding = generateEmbedding(data);
                
                Memory memory = new Memory(
                    memoryId,
                    memoryType.getValue(),
                    data,
                    importance,
                    tags,
                    embedding
                );
                
                // Adicionar à lista de memórias
                memories.add(memory);
                
                // Atualizar índice
                updateMemoryIndex(memory, memories.size() - 1);
                
                // Estabelecer associações
                establishAssociations(memory);
                
                // Gerenciar limite de memórias
                if (memories.size() > maxMemories) {
                    forgetLeastImportant();
                }
                
                // Salvar persistentemente
                savePersistentMemories();
                
                logger.debug("💾 Memória armazenada: {} - {}", memoryType.getValue(), memoryId);
                return memoryId;
                
            } catch (Exception e) {
                logger.error("❌ Erro ao armazenar memória: {}", e.getMessage(), e);
                return "";
            } finally {
                lock.writeLock().unlock();
            }
        });
    }
    
    /**
     * Versão simplificada com valores padrão
     */
    public CompletableFuture<String> storeMemory(Map<String, Object> data, MemoryType memoryType) {
        return storeMemory(data, memoryType, 0.5, new ArrayList<>());
    }
    
    /**
     * Recupera memórias relevantes baseado na consulta
     */
    public CompletableFuture<List<MemoryResult>> retrieveMemory(Map<String, Object> query,
                                                               int maxResults,
                                                               MemoryType memoryType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                lock.readLock().lock();
                
                float[] queryEmbedding = generateEmbedding(query);
                
                // Calcular similaridade com todas as memórias
                List<SimilarityScore> similarities = new ArrayList<>();
                
                for (int i = 0; i < memories.size(); i++) {
                    Memory memory = memories.get(i);
                    
                    if (memoryType != null && !memory.getType().equals(memoryType.getValue())) {
                        continue;
                    }
                    
                    double similarity = calculateSimilarity(queryEmbedding, memory.getEmbedding());
                    
                    // Ajustar pela importância e frequência de acesso
                    double adjustedScore = 
                        similarity * 0.6 +
                        memory.getImportance() * 0.3 +
                        (Math.min(memory.getAccessCount(), 100) / 100.0) * 0.1;
                    
                    similarities.add(new SimilarityScore(i, adjustedScore));
                }
                
                // Ordenar por similaridade
                similarities.sort((a, b) -> Double.compare(b.score, a.score));
                
                // Retornar top resultados
                List<MemoryResult> results = new ArrayList<>();
                
                for (int j = 0; j < Math.min(maxResults, similarities.size()); j++) {
                    SimilarityScore score = similarities.get(j);
                    Memory memory = memories.get(score.index);
                    
                    // Atualizar contador de acesso (precisa de lock de escrita)
                    lock.readLock().unlock();
                    lock.writeLock().lock();
                    try {
                        memory.incrementAccessCount();
                    } finally {
                        lock.writeLock().unlock();
                        lock.readLock().lock();
                    }
                    
                    List<Memory> relatedMemories = getRelatedMemories(memory.getId());
                    
                    results.add(new MemoryResult(
                        copyMemory(memory),
                        score.score,
                        relatedMemories
                    ));
                }
                
                return results;
                
            } catch (Exception e) {
                logger.error("❌ Erro ao recuperar memórias: {}", e.getMessage(), e);
                return new ArrayList<>();
            } finally {
                lock.readLock().unlock();
            }
        });
    }
    
    /**
     * Recupera memórias sem filtro de tipo
     */
    public CompletableFuture<List<MemoryResult>> retrieveMemory(Map<String, Object> query, int maxResults) {
        return retrieveMemory(query, maxResults, null);
    }
    
    /**
     * Gera embedding vetorial para os dados
     */
    public float[] generateEmbedding(Map<String, Object> data) {
        float[] features = new float[EMBEDDING_DIMENSIONS];
        int idx = 0;
        
        // Extrair features numéricas
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (idx >= EMBEDDING_DIMENSIONS) break;
            
            Object value = entry.getValue();
            
            if (value instanceof Number) {
                features[idx++] = ((Number) value).floatValue();
            } else if (value instanceof String) {
                // Hash simples para strings
                features[idx++] = (Math.abs(value.hashCode()) % 1000) / 1000.0f;
            } else if (value instanceof Boolean) {
                features[idx++] = (Boolean) value ? 1.0f : 0.0f;
            } else {
                features[idx++] = 0.0f;
            }
        }
        
        // Preencher com zeros se necessário
        while (idx < EMBEDDING_DIMENSIONS) {
            features[idx++] = 0.0f;
        }
        
        // Normalizar
        float norm = 0.0f;
        for (float f : features) {
            norm += f * f;
        }
        norm = (float) Math.sqrt(norm);
        
        if (norm > 0) {
            for (int i = 0; i < features.length; i++) {
                features[i] /= norm;
            }
        }
        
        return features;
    }
    
    /**
     * Calcula similaridade entre embeddings
     */
    public double calculateSimilarity(float[] embedding1, float[] embedding2) {
        if (embedding1 == null || embedding2 == null || 
            embedding1.length == 0 || embedding2.length == 0) {
            return 0.0;
        }
        
        int minLength = Math.min(embedding1.length, embedding2.length);
        double dotProduct = 0.0;
        
        for (int i = 0; i < minLength; i++) {
            dotProduct += embedding1[i] * embedding2[i];
        }
        
        return dotProduct;
    }
    
    /**
     * Obtém memória pelo ID
     */
    public CompletableFuture<Optional<Memory>> getMemoryById(String memoryId) {
        return CompletableFuture.supplyAsync(() -> {
            lock.readLock().lock();
            try {
                return memories.stream()
                    .filter(m -> m.getId().equals(memoryId))
                    .findFirst()
                    .map(this::copyMemory);
            } finally {
                lock.readLock().unlock();
            }
        });
    }
    
    /**
     * Obtém estatísticas da memória
     */
    public CompletableFuture<MemoryStats> getMemoryStats() {
        return CompletableFuture.supplyAsync(() -> {
            lock.readLock().lock();
            try {
                if (memories.isEmpty()) {
                    return new MemoryStats(0, new HashMap<>(), 0.0, 0, memoryIndex.size());
                }
                
                Map<String, Integer> typeCounts = new HashMap<>();
                double totalImportance = 0.0;
                int totalAccesses = 0;
                
                for (Memory memory : memories) {
                    String type = memory.getType();
                    typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
                    totalImportance += memory.getImportance();
                    totalAccesses += memory.getAccessCount();
                }
                
                double averageImportance = totalImportance / memories.size();
                
                return new MemoryStats(
                    memories.size(),
                    typeCounts,
                    averageImportance,
                    totalAccesses,
                    memoryIndex.size()
                );
                
            } finally {
                lock.readLock().unlock();
            }
        });
    }
    
    /**
     * Desliga o sistema de memória
     */
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            logger.info("🔌 Desligando Memória Neural...");
            savePersistentMemories();
            logger.info("✅ Memória Neural desligada");
        });
    }
    
    // ========== PRIVATE METHODS ==========
    
    /**
     * Estabelece associações entre memórias
     */
    private void establishAssociations(Memory memory) {
        try {
            List<String> associations = new ArrayList<>();
            
            // Encontrar memórias similares
            for (Memory otherMemory : memories) {
                if (otherMemory.getId().equals(memory.getId())) {
                    continue;
                }
                
                double similarity = calculateSimilarity(
                    memory.getEmbedding(), 
                    otherMemory.getEmbedding()
                );
                
                if (similarity > ASSOCIATION_THRESHOLD) {
                    associations.add(otherMemory.getId());
                    
                    // Atualizar associação bidirecional
                    otherMemory.addAssociation(memory.getId());
                    
                    // Atualizar mapa de associações
                    this.associations.computeIfAbsent(otherMemory.getId(), k -> new HashSet<>())
                        .add(memory.getId());
                }
            }
            
            // Adicionar associações à memória atual
            for (String assocId : associations) {
                memory.addAssociation(assocId);
                this.associations.computeIfAbsent(memory.getId(), k -> new HashSet<>())
                    .add(assocId);
            }
            
        } catch (Exception e) {
            logger.error("❌ Erro ao estabelecer associações: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Obtém memórias relacionadas
     */
    private List<Memory> getRelatedMemories(String memoryId) {
        List<Memory> related = new ArrayList<>();
        
        Set<String> assocIds = associations.getOrDefault(memoryId, new HashSet<>());
        
        for (String assocId : assocIds) {
            memories.stream()
                .filter(m -> m.getId().equals(assocId))
                .findFirst()
                .ifPresent(m -> related.add(copyMemory(m)));
        }
        
        return related;
    }
    
    /**
     * Atualiza o índice de memórias
     */
    private void updateMemoryIndex(Memory memory, int index) {
        // Indexar por tipo
        memoryIndex.computeIfAbsent(memory.getType(), k -> new ArrayList<>()).add(index);
        
        // Indexar por tags
        for (String tag : memory.getTags()) {
            memoryIndex.computeIfAbsent(tag, k -> new ArrayList<>()).add(index);
        }
    }
    
    /**
     * Inicializa o índice de memórias
     */
    private void initializeMemoryIndex() {
        memoryIndex.clear();
        for (int i = 0; i < memories.size(); i++) {
            updateMemoryIndex(memories.get(i), i);
        }
    }
    
    /**
     * Esquece memórias menos importantes
     */
    private void forgetLeastImportant() {
        if (memories.size() <= maxMemories) {
            return;
        }
        
        // Calcular scores de esquecimento
        List<ForgetScore> forgetScores = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        
        for (int i = 0; i < memories.size(); i++) {
            Memory memory = memories.get(i);
            
            // Score baseado na importância, acesso e idade
            long ageDays = ChronoUnit.DAYS.between(memory.getTimestamp(), currentTime);
            double ageFactor = Math.min(ageDays / 365.0, 1.0);
            double accessFactor = 1.0 - Math.min(memory.getAccessCount() / 100.0, 1.0);
            
            double forgetScore = 
                (1.0 - memory.getImportance()) * FORGET_IMPORTANCE_WEIGHT +
                accessFactor * FORGET_ACCESS_WEIGHT +
                ageFactor * FORGET_AGE_WEIGHT;
            
            forgetScores.add(new ForgetScore(forgetScore, i));
        }
        
        // Ordenar por score de esquecimento
        forgetScores.sort((a, b) -> Double.compare(b.score, a.score));
        
        // Esquecer memórias excedentes
        int numToForget = memories.size() - maxMemories;
        
        for (int j = 0; j < numToForget; j++) {
            if (j < forgetScores.size()) {
                int indexToRemove = forgetScores.get(j).index;
                
                // Remover associações
                Memory removed = memories.get(indexToRemove);
                associations.remove(removed.getId());
                
                // Remover do índice (será reconstruído depois)
                logger.debug("🧹 Memória esquecida: {}", removed.getId());
            }
        }
        
        // Criar nova lista sem as memórias esquecidas
        List<Integer> indicesToKeep = forgetScores.stream()
            .skip(numToForget)
            .map(fs -> fs.index)
            .sorted()
            .collect(Collectors.toList());
        
        List<Memory> newMemories = new ArrayList<>();
        for (int idx : indicesToKeep) {
            newMemories.add(memories.get(idx));
        }
        
        memories.clear();
        memories.addAll(newMemories);
        
        // Reconstruir índice
        initializeMemoryIndex();
    }
    
    /**
     * Salva memórias persistentemente
     */
    private void savePersistentMemories() {
        try {
            String filename = String.format("neural_memory_%s.dat", systemType);
            
            Map<String, Object> data = new HashMap<>();
            data.put("memories", memories);
            data.put("associations", associations);
            data.put("timestamp", LocalDateTime.now().toString());
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(filename)))) {
                oos.writeObject(new ArrayList<>(memories));
                oos.writeObject(new HashMap<>(associations));
            }
            
            logger.debug("💾 Memórias salvas em: {}", filename);
            
        } catch (Exception e) {
            logger.error("❌ Erro ao salvar memórias: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Carrega memórias persistentes
     */
    @SuppressWarnings("unchecked")
    private void loadPersistentMemories() {
        String filename = String.format("neural_memory_%s.dat", systemType);
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(filename)))) {
            
            List<Memory> loadedMemories = (List<Memory>) ois.readObject();
            Map<String, Set<String>> loadedAssociations = (Map<String, Set<String>>) ois.readObject();
            
            memories.clear();
            memories.addAll(loadedMemories);
            
            associations.clear();
            associations.putAll(loadedAssociations);
            
            logger.info("📂 Memórias carregadas: {}", memories.size());
            
        } catch (FileNotFoundException e) {
            logger.info("📂 Nenhuma memória persistente encontrada");
        } catch (Exception e) {
            logger.error("❌ Erro ao carregar memórias: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Cria uma cópia superficial da memória
     */
    private Memory copyMemory(Memory original) {
        Memory copy = new Memory(
            original.getId(),
            original.getType(),
            original.getData(),
            original.getImportance(),
            original.getTags(),
            original.getEmbedding()
        );
        // Copy additional fields as needed
        return copy;
    }
    
    // ========== INNER HELPER CLASSES ==========
    
    /**
     * Helper class for similarity scores
     */
    private static class SimilarityScore {
        int index;
        double score;
        
        SimilarityScore(int index, double score) {
            this.index = index;
            this.score = score;
        }
    }
    
    /**
     * Helper class for forget scores
     */
    private static class ForgetScore {
        double score;
        int index;
        
        ForgetScore(double score, int index) {
            this.score = score;
            this.index = index;
        }
    }
    
    // ========== GETTERS AND SETTERS ==========
    
    public double getLearningRate() { return learningRate; }
    public void setLearningRate(double learningRate) { 
        this.learningRate = Math.max(0.0, Math.min(1.0, learningRate)); 
    }
    
    public double getRetentionRate() { return retentionRate; }
    public void setRetentionRate(double retentionRate) { 
        this.retentionRate = Math.max(0.0, Math.min(1.0, retentionRate)); 
    }
    
    public int getMemoryCount() { return memories.size(); }
    public String getSystemType() { return systemType; }
    
    // ========== MAIN FOR TESTING ==========
    
    public static void main(String[] args) {
        NeuralMemory neuralMemory = new NeuralMemory("test_system", 100);
        
        neuralMemory.initialize().join();
        
        // Criar dados de exemplo
        Map<String, Object> marketData = new HashMap<>();
        marketData.put("price", 50000.0);
        marketData.put("rsi", 65.5);
        marketData.put("volume", 1000);
        marketData.put("trend", "up");
        
        // Armazenar memória
        neuralMemory.storeMemory(marketData, MemoryType.TRADING_EXPERIENCE, 0.8, 
            Arrays.asList("bitcoin", "trending")).join();
        
        // Recuperar memória
        Map<String, Object> query = new HashMap<>();
        query.put("price", 50000.0);
        query.put("trend", "up");
        
        List<MemoryResult> results = neuralMemory.retrieveMemory(query, 5).join();
        
        System.out.println("Resultados encontrados: " + results.size());
        for (MemoryResult result : results) {
            System.out.println("  Relevância: " + result.getRelevanceScore());
            System.out.println("  Memória: " + result.getMemory().getData());
        }
        
        // Estatísticas
        MemoryStats stats = neuralMemory.getMemoryStats().join();
        System.out.println("Estatísticas: " + stats);
        
        neuralMemory.shutdown().join();
    }
}

// Nota: Para usar este código, você precisará adicionar a dependência SLF4J
// ou substituir o logger por System.out.println