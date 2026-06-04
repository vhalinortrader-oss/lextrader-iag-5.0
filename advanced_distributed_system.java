import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

// Configuração de logging (simplificado)
class Logger {
    private String name;
    
    public Logger(String name) {
        this.name = name;
    }
    
    public void info(String message) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + 
            " - " + name + " - INFO - " + message);
    }
    
    public void warning(String message) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + 
            " - " + name + " - WARNING - " + message);
    }
    
    public void error(String message) {
        System.err.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + 
            " - " + name + " - ERROR - " + message);
    }
    
    public void error(String message, Exception e) {
        error(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}

// Enumerações
enum NodeStatus {
    ONLINE("online"),
    OFFLINE("offline"),
    DEGRADED("degraded"),
    BOOTSTRAPPING("bootstrapping"),
    MAINTENANCE("maintenance");
    
    private final String value;
    
    NodeStatus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static NodeStatus fromValue(String value) {
        for (NodeStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return OFFLINE;
    }
}

enum MessageType {
    HEARTBEAT("heartbeat"),
    DATA_SYNC("data_sync"),
    CONSENSUS("consensus"),
    TRANSACTION("transaction"),
    QUERY("query"),
    REPLICATION("replication"),
    LEADER_ELECTION("leader_election"),
    GOSSIP("gossip");
    
    private final String value;
    
    MessageType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}

enum ConsistencyLevel {
    STRONG("strong"),
    EVENTUAL("eventual"),
    CAUSAL("causal"),
    SEQUENTIAL("sequential");
    
    private final String value;
    
    ConsistencyLevel(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}

// Classes de dados
class Node {
    private String id;
    private String address;
    private int port;
    private NodeStatus status;
    private LocalDateTime lastHeartbeat;
    private List<String> capabilities;
    private double load;
    private String version;
    
    public Node(String id, String address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;
        this.status = NodeStatus.OFFLINE;
        this.capabilities = new ArrayList<>();
        this.load = 0.0;
        this.version = "1.0.0";
    }
    
    public Node(String id, String address, int port, NodeStatus status, 
                LocalDateTime lastHeartbeat, List<String> capabilities, 
                double load, String version) {
        this.id = id;
        this.address = address;
        this.port = port;
        this.status = status;
        this.lastHeartbeat = lastHeartbeat;
        this.capabilities = capabilities;
        this.load = load;
        this.version = version;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public NodeStatus getStatus() { return status; }
    public void setStatus(NodeStatus status) { this.status = status; }
    
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    
    public double getLoad() { return load; }
    public void setLoad(double load) { this.load = load; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}

class DistributedMessage {
    private String id;
    private MessageType type;
    private String source;
    private String destination;
    private Map<String, Object> payload;
    private LocalDateTime timestamp;
    private int ttl;
    private ConsistencyLevel consistency;
    private String signature;
    
    public DistributedMessage(String id, MessageType type, String source, 
                              String destination, Map<String, Object> payload) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.destination = destination;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
        this.ttl = 300;
        this.consistency = ConsistencyLevel.EVENTUAL;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public int getTtl() { return ttl; }
    public void setTtl(int ttl) { this.ttl = ttl; }
    
    public ConsistencyLevel getConsistency() { return consistency; }
    public void setConsistency(ConsistencyLevel consistency) { this.consistency = consistency; }
    
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}

class Partition {
    private String id;
    private String name;
    private List<String> nodes;
    private int replicationFactor;
    private ConsistencyLevel consistencyLevel;
    
    public Partition(String id, String name, List<String> nodes) {
        this.id = id;
        this.name = name;
        this.nodes = nodes;
        this.replicationFactor = 3;
        this.consistencyLevel = ConsistencyLevel.EVENTUAL;
    }
    
    public Partition(String id, String name, List<String> nodes, 
                     int replicationFactor, ConsistencyLevel consistencyLevel) {
        this.id = id;
        this.name = name;
        this.nodes = nodes;
        this.replicationFactor = replicationFactor;
        this.consistencyLevel = consistencyLevel;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<String> getNodes() { return nodes; }
    public void setNodes(List<String> nodes) { this.nodes = nodes; }
    
    public int getReplicationFactor() { return replicationFactor; }
    public void setReplicationFactor(int replicationFactor) { this.replicationFactor = replicationFactor; }
    
    public ConsistencyLevel getConsistencyLevel() { return consistencyLevel; }
    public void setConsistencyLevel(ConsistencyLevel consistencyLevel) { this.consistencyLevel = consistencyLevel; }
}

class ConsensusResult {
    private boolean success;
    private Object value;
    private int quorumSize;
    private List<String> participants;
    private LocalDateTime timestamp;
    
    public ConsensusResult(boolean success, Object value, int quorumSize, 
                           List<String> participants, LocalDateTime timestamp) {
        this.success = success;
        this.value = value;
        this.quorumSize = quorumSize;
        this.participants = participants;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
    
    public int getQuorumSize() { return quorumSize; }
    public void setQuorumSize(int quorumSize) { this.quorumSize = quorumSize; }
    
    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

// Classe principal
public class AdvancedDistributedSystem {
    private static final Logger logger = new Logger("AdvancedDistributedSystem");
    
    // Estado do sistema
    private String nodeId;
    private String host;
    private int port;
    private String address;
    
    private Map<String, Node> nodes;
    private Map<String, Partition> partitions;
    private Map<String, Object> dataStore;
    private BlockingQueue<DistributedMessage> messageQueue;
    private String leaderId;
    private int term;
    private String votedFor;
    
    // Configurações
    private int heartbeatInterval = 5;
    private int[] electionTimeoutRange = {150, 300};
    private int replicationFactor = 3;
    private int quorumSize = 2;
    
    // Criptografia (simplificada)
    private SecretKey encryptionKey;
    
    // Estatísticas e monitoramento
    private Map<String, Object> metrics;
    private AtomicLong messagesSent;
    private AtomicLong messagesReceived;
    private AtomicLong consensusOperations;
    private AtomicLong failedOperations;
    private double averageLatency;
    
    // Thread pools e execução assíncrona
    private ExecutorService threadPool;
    private List<Future<?>> backgroundTasks;
    
    // Sistema de gossip
    private Map<String, Object> gossipState;
    private List<String> gossipPeers;
    
    // Cache distribuído
    private Map<String, Object> cache;
    private Map<String, LocalDateTime> cacheTtl;
    
    // Locks para operações concorrentes
    private final ReentrantLock lock = new ReentrantLock();
    private final Semaphore consensusLock = new Semaphore(1);
    
    // Scheduler para tarefas periódicas
    private ScheduledExecutorService scheduler;
    
    // Random para timeouts
    private Random random = new Random();
    
    public AdvancedDistributedSystem() {
        this(UUID.randomUUID().toString(), "localhost", 8000);
    }
    
    public AdvancedDistributedSystem(String nodeId, String host, int port) {
        this.nodeId = nodeId;
        this.host = host;
        this.port = port;
        this.address = host + ":" + port;
        
        // Inicializar estruturas de dados
        this.nodes = new ConcurrentHashMap<>();
        this.partitions = new ConcurrentHashMap<>();
        this.dataStore = new ConcurrentHashMap<>();
        this.messageQueue = new LinkedBlockingQueue<>();
        
        // Inicializar métricas
        this.metrics = new ConcurrentHashMap<>();
        this.messagesSent = new AtomicLong(0);
        this.messagesReceived = new AtomicLong(0);
        this.consensusOperations = new AtomicLong(0);
        this.failedOperations = new AtomicLong(0);
        this.averageLatency = 0.0;
        
        // Inicializar thread pools
        this.threadPool = Executors.newFixedThreadPool(10);
        this.scheduler = Executors.newScheduledThreadPool(5);
        this.backgroundTasks = new ArrayList<>();
        
        // Inicializar gossip
        this.gossipState = new ConcurrentHashMap<>();
        this.gossipPeers = new CopyOnWriteArrayList<>();
        
        // Inicializar cache
        this.cache = new ConcurrentHashMap<>();
        this.cacheTtl = new ConcurrentHashMap<>();
        
        // Inicializar criptografia (simplificada)
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            this.encryptionKey = keyGen.generateKey();
        } catch (Exception e) {
            logger.error("Erro ao gerar chave de criptografia", e);
        }
        
        logger.info("Sistema Distribuído inicializado - Nó: " + nodeId);
    }
    
    public void initialize(List<String> bootstrapNodes) throws Exception {
        try {
            // Registrar próprio nó
            registerSelf();
            
            // Iniciar tarefas periódicas
            startBackgroundTasks();
            
            // Bootstrap com outros nós
            if (bootstrapNodes != null && !bootstrapNodes.isEmpty()) {
                bootstrap(bootstrapNodes);
            }
            
            // Aguardar um pouco e iniciar eleição se necessário
            Thread.sleep(random.nextInt(electionTimeoutRange[1] - electionTimeoutRange[0]) + 
                        electionTimeoutRange[0]);
            
            if (leaderId == null) {
                startElection();
            }
            
            logger.info("Sistema Distribuído inicializado com sucesso - Líder: " + leaderId);
            
        } catch (Exception e) {
            logger.error("Erro na inicialização", e);
            throw e;
        }
    }
    
    private void registerSelf() {
        Node selfNode = new Node(
            nodeId,
            host,
            port
        );
        selfNode.setStatus(NodeStatus.ONLINE);
        selfNode.setLastHeartbeat(LocalDateTime.now());
        selfNode.setCapabilities(Arrays.asList("storage", "compute", "routing"));
        selfNode.setLoad(0.0);
        
        nodes.put(nodeId, selfNode);
    }
    
    private void startBackgroundTasks() {
        // Heartbeat loop
        scheduler.scheduleAtFixedRate(
            this::heartbeatLoop,
            0,
            heartbeatInterval,
            TimeUnit.SECONDS
        );
        
        // Message processor
        backgroundTasks.add(threadPool.submit(this::messageProcessor));
        
        // Gossip loop
        scheduler.scheduleAtFixedRate(
            this::gossipLoop,
            10,
            10,
            TimeUnit.SECONDS
        );
        
        // Cache cleanup loop
        scheduler.scheduleAtFixedRate(
            this::cacheCleanupLoop,
            1,
            1,
            TimeUnit.MINUTES
        );
        
        // Monitoring loop
        scheduler.scheduleAtFixedRate(
            this::monitoringLoop,
            30,
            30,
            TimeUnit.SECONDS
        );
    }
    
    private void heartbeatLoop() {
        try {
            // Atualizar próprio heartbeat
            nodes.get(nodeId).setLastHeartbeat(LocalDateTime.now());
            nodes.get(nodeId).setLoad(calculateCurrentLoad());
            
            // Enviar heartbeats para outros nós
            Map<String, Object> payload = new HashMap<>();
            payload.put("node_id", nodeId);
            payload.put("status", nodes.get(nodeId).getStatus().getValue());
            payload.put("load", nodes.get(nodeId).getLoad());
            payload.put("timestamp", LocalDateTime.now().toString());
            
            DistributedMessage heartbeatMsg = new DistributedMessage(
                UUID.randomUUID().toString(),
                MessageType.HEARTBEAT,
                nodeId,
                "broadcast",
                payload
            );
            
            broadcastMessage(heartbeatMsg);
            
            // Verificar nós inativos
            checkInactiveNodes();
            
        } catch (Exception e) {
            logger.error("Erro no heartbeat loop", e);
        }
    }
    
    private void messageProcessor() {
        while (true) {
            try {
                DistributedMessage message = messageQueue.poll(1, TimeUnit.SECONDS);
                if (message != null) {
                    handleMessage(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.error("Erro no processamento de mensagem", e);
            }
        }
    }
    
    private void handleMessage(DistributedMessage message) {
        messagesReceived.incrementAndGet();
        
        try {
            switch (message.getType()) {
                case HEARTBEAT:
                    handleHeartbeat(message);
                    break;
                case DATA_SYNC:
                    handleDataSync(message);
                    break;
                case CONSENSUS:
                    handleConsensus(message);
                    break;
                case LEADER_ELECTION:
                    handleLeaderElection(message);
                    break;
                case GOSSIP:
                    handleGossip(message);
                    break;
                default:
                    logger.warning("Tipo de mensagem não reconhecido: " + message.getType());
            }
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem " + message.getId(), e);
            failedOperations.incrementAndGet();
        }
    }
    
    private void handleHeartbeat(DistributedMessage message) {
        String nodeId = (String) message.getPayload().get("node_id");
        
        lock.lock();
        try {
            if (nodes.containsKey(nodeId)) {
                Node node = nodes.get(nodeId);
                node.setLastHeartbeat(LocalDateTime.now());
                node.setLoad((double) message.getPayload().get("load"));
                node.setStatus(NodeStatus.fromValue((String) message.getPayload().get("status")));
            } else {
                // Novo nó descoberto
                Node newNode = new Node(
                    nodeId,
                    (String) message.getPayload().getOrDefault("address", "unknown"),
                    0,
                    NodeStatus.fromValue((String) message.getPayload().get("status")),
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    (double) message.getPayload().get("load"),
                    "1.0.0"
                );
                nodes.put(nodeId, newNode);
                logger.info("Novo nó descoberto: " + nodeId);
            }
        } finally {
            lock.unlock();
        }
    }
    
    private void handleDataSync(DistributedMessage message) {
        String operation = (String) message.getPayload().get("operation");
        String key = (String) message.getPayload().get("key");
        byte[] value = (byte[]) message.getPayload().get("value");
        
        if ("store".equals(operation)) {
            dataStore.put(key, value);
        }
    }
    
    private void handleLeaderElection(DistributedMessage message) {
        String type = (String) message.getPayload().get("type");
        
        if ("election".equals(type)) {
            int messageTerm = (int) message.getPayload().get("term");
            
            lock.lock();
            try {
                if (votedFor == null || messageTerm > term) {
                    term = messageTerm;
                    votedFor = message.getSource();
                    
                    // Enviar voto
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("type", "vote");
                    payload.put("term", term);
                    payload.put("voter_id", nodeId);
                    
                    DistributedMessage voteMsg = new DistributedMessage(
                        UUID.randomUUID().toString(),
                        MessageType.LEADER_ELECTION,
                        nodeId,
                        message.getSource(),
                        payload
                    );
                    
                    sendMessage(voteMsg);
                }
            } finally {
                lock.unlock();
            }
            
        } else if ("victory".equals(type)) {
            leaderId = message.getSource();
            term = (int) message.getPayload().get("term");
            logger.info("Novo líder eleito: " + leaderId);
        }
    }
    
    private void handleConsensus(DistributedMessage message) {
        // Implementação simplificada de consenso
        String phase = (String) message.getPayload().get("phase");
        logger.info("Recebida mensagem de consenso - Fase: " + phase);
    }
    
    private void handleGossip(DistributedMessage message) {
        try {
            Map<String, Object> receivedState = (Map<String, Object>) message.getPayload().get("state");
            LocalDateTime receivedTimestamp = LocalDateTime.parse(
                (String) message.getPayload().get("timestamp"));
            
            // Mesclar estados (simplificado)
            gossipState.putAll(receivedState);
            
        } catch (Exception e) {
            logger.error("Erro ao processar gossip", e);
        }
    }
    
    private void startElection() {
        lock.lock();
        try {
            term++;
            votedFor = nodeId;
        } finally {
            lock.unlock();
        }
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "election");
        payload.put("term", term);
        payload.put("candidate_id", nodeId);
        
        DistributedMessage electionMsg = new DistributedMessage(
            UUID.randomUUID().toString(),
            MessageType.LEADER_ELECTION,
            nodeId,
            "broadcast",
            payload
        );
        
        int votesReceived = 1; // Vota em si mesmo
        int votesNeeded = (nodes.size() / 2) + 1;
        
        // Enviar pedidos de votação
        broadcastMessage(electionMsg);
        
        // Aguardar votos (simulação)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        if (votesReceived >= votesNeeded) {
            // Tornar-se líder
            leaderId = nodeId;
            
            Map<String, Object> victoryPayload = new HashMap<>();
            victoryPayload.put("type", "victory");
            victoryPayload.put("term", term);
            victoryPayload.put("leader_id", nodeId);
            
            DistributedMessage victoryMsg = new DistributedMessage(
                UUID.randomUUID().toString(),
                MessageType.LEADER_ELECTION,
                nodeId,
                "broadcast",
                victoryPayload
            );
            
            broadcastMessage(victoryMsg);
            logger.info("Eleito como líder para o termo " + term);
        }
    }
    
    private void checkInactiveNodes() {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration inactiveThreshold = Duration.ofSeconds(heartbeatInterval * 3);
        
        lock.lock();
        try {
            List<String> nodesToRemove = new ArrayList<>();
            
            for (Map.Entry<String, Node> entry : nodes.entrySet()) {
                Node node = entry.getValue();
                if (node.getLastHeartbeat() != null && 
                    !nodeId.equals(entry.getKey()) &&
                    Duration.between(node.getLastHeartbeat(), currentTime).compareTo(inactiveThreshold) > 0) {
                    
                    node.setStatus(NodeStatus.OFFLINE);
                    nodesToRemove.add(entry.getKey());
                    logger.warning("Nó " + entry.getKey() + " marcado como inativo");
                }
            }
            
            // Se o líder está inativo, iniciar nova eleição
            if (leaderId != null && nodesToRemove.contains(leaderId)) {
                logger.warning("Líder " + leaderId + " está inativo. Iniciando nova eleição...");
                leaderId = null;
                
                // Iniciar eleição em thread separada
                threadPool.submit(this::startElection);
            }
        } finally {
            lock.unlock();
        }
    }
    
    private void bootstrap(List<String> bootstrapNodes) {
        HttpClient client = HttpClient.newHttpClient();
        
        for (String nodeAddr : bootstrapNodes) {
            try {
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("node_id", nodeId);
                requestBody.put("address", address);
                requestBody.put("capabilities", Arrays.asList("storage", "compute", "routing"));
                
                String jsonBody = mapToJson(requestBody);
                
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + nodeAddr + "/cluster/join"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(5))
                    .build();
                
                HttpResponse<String> response = client.send(request, 
                    HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    Map<String, Object> data = jsonToMap(response.body());
                    List<Map<String, Object>> nodesInfo = (List<Map<String, Object>>) data.get("nodes");
                    
                    if (nodesInfo != null) {
                        for (Map<String, Object> nodeInfo : nodesInfo) {
                            Node node = mapToNode(nodeInfo);
                            nodes.put(node.getId(), node);
                        }
                    }
                    
                    logger.info("Bootstrap bem-sucedido com " + nodeAddr);
                    break;
                }
                
            } catch (Exception e) {
                logger.warning("Falha no bootstrap com " + nodeAddr + ": " + e.getMessage());
            }
        }
    }
    
    public CompletableFuture<Boolean> storeData(String key, Object value, ConsistencyLevel consistency) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Serializar e comprimir dados
                byte[] serializedData = serializeObject(value);
                byte[] compressedData = compressData(serializedData);
                
                // Determinar partição
                String partitionId = getPartitionForKey(key);
                
                if (consistency == ConsistencyLevel.STRONG) {
                    // Consenso forte - requer quorum
                    ConsensusResult result = strongConsensusOperation(
                        "store", key, compressedData, partitionId);
                    return result.isSuccess();
                } else {
                    // Consenso eventual - replica para nós da partição
                    Partition partition = partitions.get(partitionId);
                    int successCount = 0;
                    
                    for (String nodeId : partition.getNodes()) {
                        Node node = nodes.get(nodeId);
                        if (node != null && node.getStatus() == NodeStatus.ONLINE) {
                            Map<String, Object> payload = new HashMap<>();
                            payload.put("operation", "store");
                            payload.put("key", key);
                            payload.put("value", compressedData);
                            payload.put("partition", partitionId);
                            
                            DistributedMessage storeMsg = new DistributedMessage(
                                UUID.randomUUID().toString(),
                                MessageType.DATA_SYNC,
                                this.nodeId,
                                nodeId,
                                payload
                            );
                            
                            sendMessage(storeMsg);
                            successCount++;
                        }
                    }
                    
                    return successCount >= quorumSize;
                }
                
            } catch (Exception e) {
                logger.error("Erro ao armazenar dados para chave " + key, e);
                return false;
            }
        }, threadPool);
    }
    
    public CompletableFuture<Object> retrieveData(String key, ConsistencyLevel consistency) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String partitionId = getPartitionForKey(key);
                Partition partition = partitions.get(partitionId);
                
                if (consistency == ConsistencyLevel.STRONG) {
                    // Ler do líder ou com quorum
                    if (leaderId != null && partition.getNodes().contains(leaderId)) {
                        // Tentar ler do líder primeiro
                        Object leaderData = retrieveFromNode(key, leaderId);
                        if (leaderData != null) {
                            return decompressData((byte[]) leaderData);
                        }
                    }
                    
                    // Ler com quorum
                    List<byte[]> results = new ArrayList<>();
                    int quorumCount = Math.min(quorumSize, partition.getNodes().size());
                    
                    for (int i = 0; i < quorumCount; i++) {
                        String nodeId = partition.getNodes().get(i);
                        Node node = nodes.get(nodeId);
                        if (node != null && node.getStatus() == NodeStatus.ONLINE) {
                            byte[] data = (byte[]) retrieveFromNode(key, nodeId);
                            if (data != null) {
                                results.add(data);
                            }
                        }
                    }
                    
                    if (!results.isEmpty()) {
                        return decompressData(results.get(0));
                    }
                    
                } else {
                    // Ler de qualquer nó disponível
                    for (String nodeId : partition.getNodes()) {
                        Node node = nodes.get(nodeId);
                        if (node != null && node.getStatus() == NodeStatus.ONLINE) {
                            byte[] data = (byte[]) retrieveFromNode(key, nodeId);
                            if (data != null) {
                                return decompressData(data);
                            }
                        }
                    }
                }
                
                return null;
                
            } catch (Exception e) {
                logger.error("Erro ao recuperar dados para chave " + key, e);
                return null;
            }
        }, threadPool);
    }
    
    private Object retrieveFromNode(String key, String nodeId) {
        if (nodeId.equals(this.nodeId)) {
            return dataStore.get(key);
        }
        
        // Para outros nós, enviar mensagem de consulta (simplificado)
        Map<String, Object> payload = new HashMap<>();
        payload.put("key", key);
        payload.put("operation", "retrieve");
        
        DistributedMessage queryMsg = new DistributedMessage(
            UUID.randomUUID().toString(),
            MessageType.QUERY,
            this.nodeId,
            nodeId,
            payload
        );
        
        sendMessage(queryMsg);
        
        // Em implementação real, aguardaria resposta
        return null;
    }
    
    private String getPartitionForKey(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes());
            String keyHash = bytesToHex(digest);
            
            List<String> partitionKeys = new ArrayList<>(partitions.keySet());
            Collections.sort(partitionKeys);
            
            if (partitionKeys.isEmpty()) {
                // Criar partição padrão
                Partition defaultPartition = new Partition(
                    "default",
                    "Default Partition",
                    Arrays.asList(nodeId),
                    replicationFactor,
                    ConsistencyLevel.EVENTUAL
                );
                partitions.put("default", defaultPartition);
                return "default";
            }
            
            // Encontrar a partição apropriada
            for (String partitionId : partitionKeys) {
                if (keyHash.compareTo(partitionId) <= 0) {
                    return partitionId;
                }
            }
            
            // Circular - retornar a primeira partição
            return partitionKeys.get(0);
            
        } catch (Exception e) {
            logger.error("Erro ao determinar partição para chave " + key, e);
            return "default";
        }
    }
    
    private ConsensusResult strongConsensusOperation(String operation, String key, 
                                                      byte[] value, String partitionId) {
        try {
            consensusLock.acquire();
            
            String proposalId = nodeId + "-" + System.currentTimeMillis();
            List<String> participants = new ArrayList<>(nodes.keySet())
                .subList(0, Math.min(quorumSize, nodes.size()));
            
            // Fase 1: Prepare (simulado)
            List<Boolean> prepareResponses = new ArrayList<>();
            for (String nodeId : participants) {
                if (!nodeId.equals(this.nodeId) && 
                    nodes.get(nodeId).getStatus() == NodeStatus.ONLINE) {
                    
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("phase", "prepare");
                    payload.put("proposal_id", proposalId);
                    payload.put("operation", operation);
                    payload.put("key", key);
                    payload.put("value", value);
                    payload.put("partition", partitionId);
                    
                    DistributedMessage prepareMsg = new DistributedMessage(
                        UUID.randomUUID().toString(),
                        MessageType.CONSENSUS,
                        this.nodeId,
                        nodeId,
                        payload
                    );
                    
                    sendMessage(prepareMsg);
                    prepareResponses.add(true);
                }
            }
            
            // Fase 2: Accept
            if (prepareResponses.size() >= (quorumSize - 1)) {
                List<Boolean> acceptResponses = new ArrayList<>();
                
                for (String nodeId : participants) {
                    if (!nodeId.equals(this.nodeId) && 
                        nodes.get(nodeId).getStatus() == NodeStatus.ONLINE) {
                        
                        Map<String, Object> payload = new HashMap<>();
                        payload.put("phase", "accept");
                        payload.put("proposal_id", proposalId);
                        payload.put("operation", operation);
                        payload.put("key", key);
                        payload.put("value", value);
                        payload.put("partition", partitionId);
                        
                        DistributedMessage acceptMsg = new DistributedMessage(
                            UUID.randomUUID().toString(),
                            MessageType.CONSENSUS,
                            this.nodeId,
                            nodeId,
                            payload
                        );
                        
                        sendMessage(acceptMsg);
                        acceptResponses.add(true);
                    }
                }
                
                if (acceptResponses.size() >= (quorumSize - 1)) {
                    consensusOperations.incrementAndGet();
                    return new ConsensusResult(
                        true,
                        null,
                        quorumSize,
                        participants,
                        LocalDateTime.now()
                    );
                }
            }
            
            return new ConsensusResult(
                false,
                null,
                quorumSize,
                participants,
                LocalDateTime.now()
            );
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ConsensusResult(false, null, quorumSize, new ArrayList<>(), LocalDateTime.now());
        } finally {
            consensusLock.release();
        }
    }
    
    private void broadcastMessage(DistributedMessage message) {
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            String nodeId = entry.getKey();
            Node node = entry.getValue();
            
            if (!nodeId.equals(this.nodeId) && node.getStatus() == NodeStatus.ONLINE) {
                message.setDestination(nodeId);
                sendMessage(message);
            }
        }
    }
    
    private void sendMessage(DistributedMessage message) {
        try {
            if (nodes.containsKey(message.getDestination())) {
                // Simular latência de rede
                Thread.sleep(random.nextInt(10));
                
                messagesSent.incrementAndGet();
                
                // Adicionar à fila do nó destino (simulado)
                double latency = Duration.between(message.getTimestamp(), LocalDateTime.now()).toMillis() / 1000.0;
                averageLatency = averageLatency * 0.9 + latency * 0.1;
                
                // Em implementação real, enviaria pela rede
                // Aqui apenas colocamos na própria fila para processamento
                if (message.getDestination().equals(this.nodeId)) {
                    messageQueue.offer(message);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem " + message.getId(), e);
        }
    }
    
    private void gossipLoop() {
        try {
            if (gossipPeers.isEmpty()) {
                return;
            }
            
            // Selecionar peer aleatório para gossip
            String peerId = gossipPeers.get(random.nextInt(gossipPeers.size()));
            Node peer = nodes.get(peerId);
            
            if (peer != null && peer.getStatus() == NodeStatus.ONLINE) {
                
                Map<String, Object> payload = new HashMap<>();
                payload.put("state", new HashMap<>(gossipState));
                payload.put("timestamp", LocalDateTime.now().toString());
                
                DistributedMessage gossipMsg = new DistributedMessage(
                    UUID.randomUUID().toString(),
                    MessageType.GOSSIP,
                    nodeId,
                    peerId,
                    payload
                );
                
                sendMessage(gossipMsg);
            }
            
        } catch (Exception e) {
            logger.error("Erro no gossip loop", e);
        }
    }
    
    private void cacheCleanupLoop() {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            List<String> expiredKeys = new ArrayList<>();
            
            for (Map.Entry<String, LocalDateTime> entry : cacheTtl.entrySet()) {
                if (entry.getValue().isBefore(currentTime)) {
                    expiredKeys.add(entry.getKey());
                }
            }
            
            for (String key : expiredKeys) {
                cache.remove(key);
                cacheTtl.remove(key);
            }
            
        } catch (Exception e) {
            logger.error("Erro na limpeza de cache", e);
        }
    }
    
    private void monitoringLoop() {
        try {
            // Coletar métricas do sistema
            Map<String, Object> systemMetrics = new HashMap<>();
            systemMetrics.put("total_nodes", nodes.size());
            systemMetrics.put("online_nodes", 
                nodes.values().stream().filter(n -> n.getStatus() == NodeStatus.ONLINE).count());
            systemMetrics.put("system_load", calculateSystemLoad());
            systemMetrics.put("message_throughput", messagesSent.get() / 30.0);
            
            long totalOps = consensusOperations.get() + failedOperations.get() + 1;
            double successRate = (double) consensusOperations.get() / totalOps;
            systemMetrics.put("consensus_success_rate", successRate);
            
            // Atualizar estado de gossip com métricas
            gossipState.put("system_metrics", systemMetrics);
            
            logger.info("Métricas do sistema: " + systemMetrics);
            
        } catch (Exception e) {
            logger.error("Erro no loop de monitoramento", e);
        }
    }
    
    private double calculateCurrentLoad() {
        // Simulação - em implementação real usaria métricas reais
        return random.nextDouble();
    }
    
    private double calculateSystemLoad() {
        if (nodes.isEmpty()) {
            return 0.0;
        }
        
        return nodes.values().stream()
            .mapToDouble(Node::getLoad)
            .average()
            .orElse(0.0);
    }
    
    private byte[] serializeObject(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }
    
    private Object deserializeObject(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }
    
    private byte[] compressData(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        
        byte[] buffer = new byte[data.length];
        int compressedSize = deflater.deflate(buffer);
        
        byte[] compressed = new byte[compressedSize];
        System.arraycopy(buffer, 0, compressed, 0, compressedSize);
        
        return compressed;
    }
    
    private Object decompressData(byte[] compressedData) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(compressedData);
            
            byte[] buffer = new byte[compressedData.length * 2];
            int decompressedSize = inflater.inflate(buffer);
            
            byte[] decompressed = new byte[decompressedSize];
            System.arraycopy(buffer, 0, decompressed, 0, decompressedSize);
            
            return deserializeObject(decompressed);
            
        } catch (Exception e) {
            logger.error("Erro ao descomprimir dados", e);
            return null;
        }
    }
    
    public Map<String, Object> getSystemStatus() {
        lock.lock();
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("node_id", nodeId);
            status.put("leader_id", leaderId);
            status.put("term", term);
            status.put("total_nodes", nodes.size());
            
            List<String> onlineNodes = new ArrayList<>();
            for (Node node : nodes.values()) {
                if (node.getStatus() == NodeStatus.ONLINE) {
                    onlineNodes.add(node.getId());
                }
            }
            status.put("online_nodes", onlineNodes);
            
            Map<String, Integer> partitionsInfo = new HashMap<>();
            for (Map.Entry<String, Partition> entry : partitions.entrySet()) {
                partitionsInfo.put(entry.getKey(), entry.getValue().getNodes().size());
            }
            status.put("partitions", partitionsInfo);
            
            Map<String, Object> metricsCopy = new HashMap<>();
            metricsCopy.put("messages_sent", messagesSent.get());
            metricsCopy.put("messages_received", messagesReceived.get());
            metricsCopy.put("consensus_operations", consensusOperations.get());
            metricsCopy.put("failed_operations", failedOperations.get());
            metricsCopy.put("average_latency", averageLatency);
            status.put("metrics", metricsCopy);
            
            status.put("system_load", calculateSystemLoad());
            
            return status;
            
        } finally {
            lock.unlock();
        }
    }
    
    public void shutdown() {
        logger.info("Iniciando desligamento gracioso...");
        
        // Parar tarefas agendadas
        scheduler.shutdown();
        
        // Parar thread pool
        threadPool.shutdown();
        
        try {
            // Aguardar término
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
            
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        logger.info("Sistema distribuído desligado com sucesso");
    }
    
    // Métodos utilitários para conversão JSON (simplificados)
    private String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            
            sb.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value instanceof List) {
                sb.append(listToJson((List<?>) value));
            } else if (value instanceof Map) {
                sb.append(mapToJson((Map<String, Object>) value));
            } else {
                sb.append("\"").append(value).append("\"");
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    private String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        
        for (Object item : list) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            
            if (item instanceof String) {
                sb.append("\"").append(item).append("\"");
            } else if (item instanceof Number || item instanceof Boolean) {
                sb.append(item);
            } else if (item instanceof Map) {
                sb.append(mapToJson((Map<String, Object>) item));
            } else if (item instanceof List) {
                sb.append(listToJson((List<?>) item));
            } else {
                sb.append("\"").append(item).append("\"");
            }
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    private Map<String, Object> jsonToMap(String json) {
        // Implementação simplificada - em produção usar biblioteca como Jackson
        Map<String, Object> map = new HashMap<>();
        
        // Remover espaços e quebras de linha
        json = json.replaceAll("\\s", "");
        
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].replaceAll("\"", "");
                    String value = keyValue[1];
                    
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        map.put(key, value.substring(1, value.length() - 1));
                    } else if (value.equals("true") || value.equals("false")) {
                        map.put(key, Boolean.parseBoolean(value));
                    } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                        if (value.contains(".")) {
                            map.put(key, Double.parseDouble(value));
                        } else {
                            map.put(key, Long.parseLong(value));
                        }
                    }
                }
            }
        }
        
        return map;
    }
    
    private Node mapToNode(Map<String, Object> map) {
        String id = (String) map.get("id");
        String address = (String) map.get("address");
        int port = ((Number) map.get("port")).intValue();
        NodeStatus status = NodeStatus.fromValue((String) map.get("status"));
        LocalDateTime lastHeartbeat = map.containsKey("last_heartbeat") ? 
            LocalDateTime.parse((String) map.get("last_heartbeat")) : null;
        List<String> capabilities = (List<String>) map.get("capabilities");
        double load = ((Number) map.get("load")).doubleValue();
        String version = (String) map.get("version");
        
        return new Node(id, address, port, status, lastHeartbeat, capabilities, load, version);
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    // Classe principal para demonstração
    public static void main(String[] args) {
        AdvancedDistributedSystem system = new AdvancedDistributedSystem("node1", "localhost", 8000);
        
        try {
            // Inicializar
            system.initialize(Arrays.asList("localhost:8001", "localhost:8002"));
            
            // Aguardar estabilização
            Thread.sleep(2000);
            
            // Armazenar alguns dados
            System.out.println("Armazenando dados no sistema distribuído...");
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", "João Silva");
            userData.put("email", "joao@email.com");
            userData.put("age", 30);
            
            CompletableFuture<Boolean> storeFuture = system.storeData(
                "user:123",
                userData,
                ConsistencyLevel.EVENTUAL
            );
            
            storeFuture.thenAccept(success -> {
                System.out.println("Dados armazenados: " + success);
                
                // Recuperar dados
                System.out.println("Recuperando dados...");
                CompletableFuture<Object> retrieveFuture = system.retrieveData("user:123", ConsistencyLevel.EVENTUAL);
                
                retrieveFuture.thenAccept(retrievedData -> {
                    System.out.println("Dados recuperados: " + retrievedData);
                    
                    // Mostrar status do sistema
                    Map<String, Object> status = system.getSystemStatus();
                    System.out.println("Status do sistema: " + status);
                });
            });
            
            // Manter sistema rodando por um tempo
            Thread.sleep(10000);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            system.shutdown();
        }
    }
}