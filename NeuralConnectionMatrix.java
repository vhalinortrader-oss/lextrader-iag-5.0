import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Módulo server-side que simula clusters neurais, conexões sinápticas e padrões de atividade.
 * Use renderHtml() para obter bloco HTML com resumo.
 */
public class NeuralConnectionMatrix {
    
    // Data classes as static inner classes
    public static class SynapticConnection {
        public String id;
        public String fromNeuronId;
        public String toNeuronId;
        public double weight;
        public double strength;
        public double plasticity;
        public double frequency;
        public double latency;
        public String neurotransmitter;
        public boolean isExcitatory;
        public double lastFired;
        public double adaptationRate;
        public double refractionPeriod;
        public String connectionType;
        
        public SynapticConnection(String id, String fromNeuronId, String toNeuronId,
                                  double weight, double strength, double plasticity,
                                  double frequency, double latency, String neurotransmitter,
                                  boolean isExcitatory, double lastFired, double adaptationRate,
                                  double refractionPeriod, String connectionType) {
            this.id = id;
            this.fromNeuronId = fromNeuronId;
            this.toNeuronId = toNeuronId;
            this.weight = weight;
            this.strength = strength;
            this.plasticity = plasticity;
            this.frequency = frequency;
            this.latency = latency;
            this.neurotransmitter = neurotransmitter;
            this.isExcitatory = isExcitatory;
            this.lastFired = lastFired;
            this.adaptationRate = adaptationRate;
            this.refractionPeriod = refractionPeriod;
            this.connectionType = connectionType;
        }
    }
    
    public static class NeuralCluster {
        public String id;
        public String name;
        public String region;
        public String specialization;
        public int neurons;
        public int connections;
        public double activity;
        public double coherence;
        public Map<String, Double> networkPosition;
        public double activationThreshold;
        public double learningRate;
        public int memoryCapacity;
        public int processingSpeed;
        
        public NeuralCluster(String id, String name, String region, String specialization,
                            int neurons, int connections, double activity, double coherence,
                            Map<String, Double> networkPosition, double activationThreshold,
                            double learningRate, int memoryCapacity, int processingSpeed) {
            this.id = id;
            this.name = name;
            this.region = region;
            this.specialization = specialization;
            this.neurons = neurons;
            this.connections = connections;
            this.activity = activity;
            this.coherence = coherence;
            this.networkPosition = networkPosition != null ? networkPosition : new HashMap<>();
            this.activationThreshold = activationThreshold;
            this.learningRate = learningRate;
            this.memoryCapacity = memoryCapacity;
            this.processingSpeed = processingSpeed;
        }
    }
    
    public static class NetworkTopology {
        public double smallWorldIndex;
        public double clusteringCoefficient;
        public double pathLength;
        public double networkEfficiency;
        public double modularityScore;
        public double hubConnectivity;
        public double neuralDensity;
        public double informationFlow;
        public double synchronization;
        public double plasticity;
        
        public NetworkTopology() {
            this.smallWorldIndex = 0.0;
            this.clusteringCoefficient = 0.0;
            this.pathLength = 0.0;
            this.networkEfficiency = 0.0;
            this.modularityScore = 0.0;
            this.hubConnectivity = 0.0;
            this.neuralDensity = 0.0;
            this.informationFlow = 0.0;
            this.synchronization = 0.0;
            this.plasticity = 0.0;
        }
        
        public NetworkTopology(double smallWorldIndex, double clusteringCoefficient,
                              double pathLength, double networkEfficiency,
                              double modularityScore, double hubConnectivity,
                              double neuralDensity, double informationFlow,
                              double synchronization, double plasticity) {
            this.smallWorldIndex = smallWorldIndex;
            this.clusteringCoefficient = clusteringCoefficient;
            this.pathLength = pathLength;
            this.networkEfficiency = networkEfficiency;
            this.modularityScore = modularityScore;
            this.hubConnectivity = hubConnectivity;
            this.neuralDensity = neuralDensity;
            this.informationFlow = informationFlow;
            this.synchronization = synchronization;
            this.plasticity = plasticity;
        }
    }
    
    public static class BrainwavePattern {
        public double frequency;
        public double amplitude;
        public String type;
        public double phase;
        public double coherence;
        public double timestamp;
        
        public BrainwavePattern(double frequency, double amplitude, String type,
                               double phase, double coherence, double timestamp) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.type = type;
            this.phase = phase;
            this.coherence = coherence;
            this.timestamp = timestamp;
        }
    }
    
    // Main class fields
    private List<SynapticConnection> connections;
    private List<NeuralCluster> clusters;
    private NetworkTopology topology;
    private List<BrainwavePattern> brainwaves;
    
    private boolean isActive;
    private boolean learningActive;
    private double plasticityLevel;
    private double synchronizationRate;
    private double neuralDensity;
    private String currentView; // MATRIX | TOPOLOGY | BRAINWAVES | 3D
    
    private final double activityInterval;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> activityFuture;
    
    private static final List<String> NEUROTRANSMITTERS = 
        Arrays.asList("DOPAMINE", "SEROTONIN", "ACETYLCHOLINE", "GABA", "GLUTAMATE");
    private static final List<String> NT_INTER_CLUSTER = 
        Arrays.asList("DOPAMINE", "ACETYLCHOLINE", "GLUTAMATE");
    private static final List<String> NT_FEEDBACK = 
        Arrays.asList("GABA", "SEROTONIN");
    
    /**
     * Constructor
     */
    public NeuralConnectionMatrix() {
        this(0.1);
    }
    
    public NeuralConnectionMatrix(double activityInterval) {
        this.connections = new CopyOnWriteArrayList<>();
        this.clusters = new CopyOnWriteArrayList<>();
        this.topology = new NetworkTopology();
        this.brainwaves = new CopyOnWriteArrayList<>();
        
        this.isActive = true;
        this.learningActive = false;
        this.plasticityLevel = 0.8;
        this.synchronizationRate = 0.75;
        this.neuralDensity = 0.6;
        this.currentView = "MATRIX";
        
        this.activityInterval = activityInterval;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        
        // Initialize
        initializeNeuralClusters();
        generateSynapticConnections();
        startActivityLoop();
    }
    
    /**
     * Initialize neural clusters
     */
    public List<NeuralCluster> initializeNeuralClusters() {
        List<NeuralCluster> newClusters = new ArrayList<>();
        
        Map<String, Double> pos1 = new HashMap<>();
        pos1.put("x", 0.2); pos1.put("y", 0.8); pos1.put("z", 0.9);
        newClusters.add(new NeuralCluster(
            "prefrontal_analysis", "Córtex Pré-frontal Analítico", "PREFRONTAL", "ANALYSIS",
            15000, 450000, 85.2, 92.1, pos1, 0.65, 0.012, 2048, 150
        ));
        
        Map<String, Double> pos2 = new HashMap<>();
        pos2.put("x", 0.7); pos2.put("y", 0.4); pos2.put("z", 0.6);
        newClusters.add(new NeuralCluster(
            "temporal_pattern", "Lobo Temporal de Padrões", "TEMPORAL", "PATTERN_RECOGNITION",
            22000, 880000, 91.7, 87.3, pos2, 0.58, 0.015, 4096, 200
        ));
        
        Map<String, Double> pos3 = new HashMap<>();
        pos3.put("x", 0.5); pos3.put("y", 0.9); pos3.put("z", 0.7);
        newClusters.add(new NeuralCluster(
            "parietal_decision", "Córtex Parietal Decisório", "PARIETAL", "DECISION_MAKING",
            18500, 740000, 88.9, 94.6, pos3, 0.72, 0.009, 1536, 180
        ));
        
        Map<String, Double> pos4 = new HashMap<>();
        pos4.put("x", 0.1); pos4.put("y", 0.1); pos4.put("z", 0.5);
        newClusters.add(new NeuralCluster(
            "occipital_prediction", "Córtex Occipital Preditivo", "OCCIPITAL", "PREDICTION",
            12800, 384000, 82.4, 89.1, pos4, 0.61, 0.018, 3072, 220
        ));
        
        Map<String, Double> pos5 = new HashMap<>();
        pos5.put("x", 0.5); pos5.put("y", 0.5); pos5.put("z", 0.3);
        newClusters.add(new NeuralCluster(
            "limbic_emotion", "Sistema Límbico Emocional", "LIMBIC", "EMOTION",
            8200, 246000, 95.3, 78.2, pos5, 0.45, 0.025, 512, 300
        ));
        
        Map<String, Double> pos6 = new HashMap<>();
        pos6.put("x", 0.8); pos6.put("y", 0.2); pos6.put("z", 0.4);
        newClusters.add(new NeuralCluster(
            "cerebellum_memory", "Cerebelo de Memória", "CEREBELLUM", "MEMORY",
            35000, 1750000, 76.8, 96.7, pos6, 0.55, 0.007, 8192, 120
        ));
        
        this.clusters = new CopyOnWriteArrayList<>(newClusters);
        return newClusters;
    }
    
    /**
     * Generate synaptic connections
     */
    public List<SynapticConnection> generateSynapticConnections() {
        List<SynapticConnection> newConnections = new ArrayList<>();
        Random rand = new Random();
        int cid = 0;
        
        // Intra-cluster connections
        for (NeuralCluster cluster : clusters) {
            int intra = (int)(cluster.neurons * 0.3);
            for (int i = 0; i < intra; i++) {
                String fromNeuron = String.format("%s_neuron_%d", cluster.id, rand.nextInt(cluster.neurons));
                String toNeuron = String.format("%s_neuron_%d", cluster.id, rand.nextInt(cluster.neurons));
                
                if (fromNeuron.equals(toNeuron)) continue;
                
                SynapticConnection conn = new SynapticConnection(
                    String.format("conn_%d", cid++),
                    fromNeuron, toNeuron,
                    (rand.nextDouble() - 0.5) * 2,
                    rand.nextDouble() * 0.8 + 0.2,
                    plasticityLevel + (rand.nextDouble() - 0.5) * 0.2,
                    rand.nextDouble() * 100 + 10,
                    rand.nextDouble() * 5 + 1,
                    NEUROTRANSMITTERS.get(rand.nextInt(NEUROTRANSMITTERS.size())),
                    rand.nextDouble() > 0.2,
                    0.0,
                    rand.nextDouble() * 0.1 + 0.05,
                    rand.nextDouble() * 3 + 2,
                    "LATERAL"
                );
                newConnections.add(conn);
            }
        }
        
        // Inter-cluster connections
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                NeuralCluster c1 = clusters.get(i);
                NeuralCluster c2 = clusters.get(j);
                
                double dist = Math.sqrt(
                    Math.pow(c1.networkPosition.get("x") - c2.networkPosition.get("x"), 2) +
                    Math.pow(c1.networkPosition.get("y") - c2.networkPosition.get("y"), 2) +
                    Math.pow(c1.networkPosition.get("z") - c2.networkPosition.get("z"), 2)
                );
                
                double compat = 1.0 / (1.0 + dist);
                int count = (int)(compat * 500 * neuralDensity);
                
                for (int k = 0; k < count; k++) {
                    String fromNeuron = String.format("%s_neuron_%d", c1.id, rand.nextInt(c1.neurons));
                    String toNeuron = String.format("%s_neuron_%d", c2.id, rand.nextInt(c2.neurons));
                    
                    SynapticConnection conn = new SynapticConnection(
                        String.format("conn_%d", cid++),
                        fromNeuron, toNeuron,
                        (rand.nextDouble() - 0.5) * 1.5 * compat,
                        rand.nextDouble() * 0.6 + 0.1,
                        plasticityLevel * compat,
                        rand.nextDouble() * 80 + 5,
                        rand.nextDouble() * 10 + dist * 5,
                        NT_INTER_CLUSTER.get(rand.nextInt(NT_INTER_CLUSTER.size())),
                        rand.nextDouble() > 0.15,
                        0.0,
                        rand.nextDouble() * 0.05 + 0.02,
                        rand.nextDouble() * 4 + 3,
                        rand.nextDouble() > 0.5 ? "SKIP" : "FEEDFORWARD"
                    );
                    newConnections.add(conn);
                }
                
                // Feedback connections
                int feedback = (int)(count * 0.4);
                for (int k = 0; k < feedback; k++) {
                    String fromNeuron = String.format("%s_neuron_%d", c2.id, rand.nextInt(c2.neurons));
                    String toNeuron = String.format("%s_neuron_%d", c1.id, rand.nextInt(c1.neurons));
                    
                    SynapticConnection conn = new SynapticConnection(
                        String.format("conn_%d", cid++),
                        fromNeuron, toNeuron,
                        (rand.nextDouble() - 0.5) * 1.2 * compat,
                        rand.nextDouble() * 0.4 + 0.1,
                        plasticityLevel * 0.8,
                        rand.nextDouble() * 60 + 5,
                        rand.nextDouble() * 12 + dist * 6,
                        NT_FEEDBACK.get(rand.nextInt(NT_FEEDBACK.size())),
                        rand.nextDouble() > 0.6,
                        0.0,
                        rand.nextDouble() * 0.03 + 0.01,
                        rand.nextDouble() * 5 + 4,
                        "FEEDBACK"
                    );
                    newConnections.add(conn);
                }
            }
        }
        
        // Recurrent connections for memory specializations
        for (NeuralCluster cluster : clusters) {
            if (cluster.specialization.equals("MEMORY") || cluster.specialization.equals("DECISION_MAKING")) {
                int recurrent = (int)(cluster.neurons * 0.15);
                for (int i = 0; i < recurrent; i++) {
                    String nid = String.format("%s_neuron_%d", cluster.id, rand.nextInt(cluster.neurons));
                    
                    SynapticConnection conn = new SynapticConnection(
                        String.format("conn_%d", cid++),
                        nid, nid,
                        rand.nextDouble() * 0.8 + 0.2,
                        rand.nextDouble() * 0.9 + 0.1,
                        plasticityLevel * 1.2,
                        rand.nextDouble() * 40 + 30,
                        0.5,
                        "GLUTAMATE",
                        true,
                        0.0,
                        rand.nextDouble() * 0.08 + 0.03,
                        1.0,
                        "RECURRENT"
                    );
                    newConnections.add(conn);
                }
            }
        }
        
        this.connections = new CopyOnWriteArrayList<>(newConnections);
        
        // Compute topology metrics
        int totalConn = newConnections.size();
        int totalNeurons = clusters.stream().mapToInt(c -> c.neurons).sum();
        
        this.topology = new NetworkTopology(
            0.68 + rand.nextDouble() * 0.2,
            0.45 + rand.nextDouble() * 0.25,
            2.1 + rand.nextDouble() * 0.8,
            0.78 + rand.nextDouble() * 0.15,
            0.52 + rand.nextDouble() * 0.18,
            totalNeurons > 0 ? (double)totalConn / totalNeurons * 100 : 0.0,
            neuralDensity * 100,
            synchronizationRate * 85 + rand.nextDouble() * 15,
            synchronizationRate * 100,
            plasticityLevel * 100
        );
        
        System.out.printf("Matriz neural inicializada: %,d conexões sinápticas%n", newConnections.size());
        return newConnections;
    }
    
    /**
     * Simulate activity step
     */
    private void simulateActivityStep() {
        if (!isActive) return;
        
        Random rand = new Random();
        double now = System.currentTimeMillis() / 1000.0;
        
        // Update clusters activity/coherence
        List<NeuralCluster> updatedClusters = new ArrayList<>();
        for (NeuralCluster cluster : clusters) {
            double delta = (rand.nextDouble() - 0.5) * 5 * synchronizationRate;
            cluster.activity = Math.max(0.0, Math.min(100.0, cluster.activity + delta));
            cluster.coherence = Math.max(50.0, Math.min(100.0, cluster.coherence + (rand.nextDouble() - 0.5) * 3));
            updatedClusters.add(cluster);
        }
        clusters = new CopyOnWriteArrayList<>(updatedClusters);
        
        // Propagate through connections
        List<SynapticConnection> updatedConns = new ArrayList<>();
        for (SynapticConnection conn : connections) {
            double timeSince = conn.lastFired > 0 ? now - conn.lastFired : Double.MAX_VALUE;
            double newStrength = conn.strength * 0.995;
            double newWeight = conn.weight;
            
            if (learningActive && timeSince > conn.refractionPeriod) {
                if (rand.nextDouble() < conn.plasticity) {
                    if (conn.isExcitatory) {
                        newWeight += conn.adaptationRate * plasticityLevel;
                        newStrength = Math.min(1.0, newStrength + 0.01);
                    } else {
                        newWeight -= conn.adaptationRate * plasticityLevel * 0.5;
                        newStrength = Math.max(0.1, newStrength - 0.005);
                    }
                    if (rand.nextDouble() < 0.1) {
                        conn.lastFired = now;
                    }
                }
            }
            
            conn.strength = Math.max(0.05, newStrength);
            conn.weight = Math.max(-2.0, Math.min(2.0, newWeight));
            conn.frequency = Math.max(5.0, conn.frequency * 0.998);
            updatedConns.add(conn);
        }
        connections = new CopyOnWriteArrayList<>(updatedConns);
        
        // Generate brainwaves snapshot
        double t = now;
        List<BrainwavePattern> newBrainwaves = new ArrayList<>();
        
        newBrainwaves.add(new BrainwavePattern(
            3 + Math.sin(t * 0.001) * 1,
            rand.nextDouble() * 0.3 + 0.1,
            "DELTA",
            (t * 0.003) % (2 * Math.PI),
            rand.nextDouble() * 0.4 + 0.3,
            t
        ));
        
        newBrainwaves.add(new BrainwavePattern(
            6 + Math.sin(t * 0.002) * 2,
            rand.nextDouble() * 0.5 + 0.2,
            "THETA",
            (t * 0.006) % (2 * Math.PI),
            rand.nextDouble() * 0.5 + 0.4,
            t
        ));
        
        newBrainwaves.add(new BrainwavePattern(
            10 + Math.sin(t * 0.0015) * 3,
            rand.nextDouble() * 0.7 + 0.3,
            "ALPHA",
            (t * 0.01) % (2 * Math.PI),
            rand.nextDouble() * 0.6 + 0.5,
            t
        ));
        
        newBrainwaves.add(new BrainwavePattern(
            20 + Math.sin(t * 0.003) * 10,
            rand.nextDouble() * 0.6 + 0.4,
            "BETA",
            (t * 0.02) % (2 * Math.PI),
            rand.nextDouble() * 0.7 + 0.6,
            t
        ));
        
        newBrainwaves.add(new BrainwavePattern(
            50 + Math.sin(t * 0.004) * 30,
            rand.nextDouble() * 0.4 + 0.2,
            "GAMMA",
            (t * 0.05) % (2 * Math.PI),
            rand.nextDouble() * 0.8 + 0.7,
            t
        ));
        
        this.brainwaves = new CopyOnWriteArrayList<>(newBrainwaves);
    }
    
    /**
     * Start activity loop
     */
    public void startActivityLoop() {
        if (activityFuture != null && !activityFuture.isDone()) {
            return;
        }
        
        activityFuture = executor.scheduleAtFixedRate(
            this::simulateActivityStep,
            0,
            (long)(activityInterval * 1000),
            TimeUnit.MILLISECONDS
        );
    }
    
    /**
     * Stop the activity loop
     */
    public void stop() {
        if (activityFuture != null) {
            activityFuture.cancel(false);
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Reset the network
     */
    public void resetNetwork() {
        initializeNeuralClusters();
        generateSynapticConnections();
        System.out.println("Matriz neural reinicializada");
    }
    
    /**
     * HTML escape utility
     */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    
    /**
     * Render HTML summary
     */
    public String renderHtml() {
        int totalConnections = connections.size();
        int excitatory = (int) connections.stream().filter(c -> c.isExcitatory).count();
        int inhibitory = totalConnections - excitatory;
        double avgStrength = totalConnections > 0 ? 
            connections.stream().mapToDouble(c -> c.strength).average().orElse(0.0) * 100 : 0.0;
        
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"card trading-card\">\n");
        html.append("  <div class=\"card-header\"><h3>🧠 Matriz de Conexões Neurais Amplificada</h3></div>\n");
        html.append("  <div class=\"card-content\">\n\n");
        
        // Summary cards
        html.append("    <div class=\"grid grid-cols-2 md:grid-cols-4 gap-4\">\n");
        html.append(String.format("      <div class=\"p-3 text-center\"><div class=\"font-bold\">%,d</div><div class=\"text-xs text-muted-foreground\">Conexões Totais</div></div>\n", totalConnections));
        html.append(String.format("      <div class=\"p-3 text-center\"><div class=\"font-bold\">%,d</div><div class=\"text-xs text-muted-foreground\">Excitatórias</div></div>\n", excitatory));
        html.append(String.format("      <div class=\"p-3 text-center\"><div class=\"font-bold\">%,d</div><div class=\"text-xs text-muted-foreground\">Inibitórias</div></div>\n", inhibitory));
        html.append(String.format("      <div class=\"p-3 text-center\"><div class=\"font-bold\">%.0f%%</div><div class=\"text-xs text-muted-foreground\">Força Média</div></div>\n", avgStrength));
        html.append("    </div>\n\n");
        
        // Clusters summary
        html.append("    <h4 class=\"font-semibold mt-4\">Clusters Neurais</h4>\n");
        html.append("    <div class=\"grid gap-4\">\n");
        
        for (NeuralCluster cl : clusters) {
            html.append("      <div class=\"border p-3 rounded\">\n");
            html.append(String.format("        <div class=\"flex justify-between\"><div><b>%s</b> <small class=\"text-muted\">(%s)</small></div><div><small>%s</small></div></div>\n",
                escape(cl.name), escape(cl.specialization), escape(cl.region)));
            html.append(String.format("        <div class=\"text-xs mt-2\">Neurônios: %,d • Conexões: %,d • Velocidade: %d Hz</div>\n",
                cl.neurons, cl.connections, cl.processingSpeed));
            html.append(String.format("        <div class=\"mt-2\"><div class=\"text-xs\">Atividade: %.1f%%</div></div>\n", cl.activity));
            html.append("      </div>\n");
        }
        
        html.append("    </div>\n\n");
        
        // Topology summary
        NetworkTopology t = topology;
        html.append("    <h4 class=\"font-semibold mt-4\">Topologia da Rede</h4>\n");
        html.append("    <div class=\"grid grid-cols-2 gap-4\">\n");
        html.append(String.format("      <div class=\"p-3\"><div>Small-World: <b>%.2f</b></div></div>\n", t.smallWorldIndex));
        html.append(String.format("      <div class=\"p-3\"><div>Clustering: <b>%.2f</b></div></div>\n", t.clusteringCoefficient));
        html.append(String.format("      <div class=\"p-3\"><div>Path Length: <b>%.2f</b></div></div>\n", t.pathLength));
        html.append(String.format("      <div class=\"p-3\"><div>Eficiência: <b>%.1f%%</b></div></div>\n", t.networkEfficiency * 100));
        html.append("    </div>\n\n");
        
        // Brainwaves snapshot
        html.append("    <h4 class=\"font-semibold mt-4\">Padrões de Ondas Cerebrais</h4>\n");
        html.append("    <div class=\"grid gap-3\">\n");
        
        for (BrainwavePattern w : brainwaves) {
            html.append("      <div class=\"border p-3 rounded\">\n");
            html.append(String.format("        <div class=\"flex justify-between\"><div><b>%s</b> - %.1f Hz</div><div class=\"text-xs text-muted-foreground\">Coerência: %.0f%%</div></div>\n",
                escape(w.type), w.frequency, w.coherence * 100));
            html.append("      </div>\n");
        }
        
        html.append("    </div>\n\n");
        html.append("  </div>\n");
        html.append("</div>\n");
        
        return html.toString();
    }
    
    // Getters and setters
    public List<SynapticConnection> getConnections() {
        return Collections.unmodifiableList(connections);
    }
    
    public List<NeuralCluster> getClusters() {
        return Collections.unmodifiableList(clusters);
    }
    
    public NetworkTopology getTopology() {
        return topology;
    }
    
    public List<BrainwavePattern> getBrainwaves() {
        return Collections.unmodifiableList(brainwaves);
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public boolean isLearningActive() {
        return learningActive;
    }
    
    public void setLearningActive(boolean learningActive) {
        this.learningActive = learningActive;
    }
    
    public double getPlasticityLevel() {
        return plasticityLevel;
    }
    
    public void setPlasticityLevel(double plasticityLevel) {
        this.plasticityLevel = Math.max(0.0, Math.min(1.0, plasticityLevel));
    }
    
    public double getSynchronizationRate() {
        return synchronizationRate;
    }
    
    public void setSynchronizationRate(double synchronizationRate) {
        this.synchronizationRate = Math.max(0.0, Math.min(1.0, synchronizationRate));
    }
    
    public double getNeuralDensity() {
        return neuralDensity;
    }
    
    public void setNeuralDensity(double neuralDensity) {
        this.neuralDensity = Math.max(0.0, Math.min(1.0, neuralDensity));
    }
    
    public String getCurrentView() {
        return currentView;
    }
    
    public void setCurrentView(String currentView) {
        this.currentView = currentView;
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        NeuralConnectionMatrix ncm = new NeuralConnectionMatrix();
        try {
            Thread.sleep(1000);
            System.out.println(ncm.renderHtml().substring(0, Math.min(800, ncm.renderHtml().length())));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            ncm.stop();
        }
    }
}