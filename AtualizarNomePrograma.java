package modulo_emocional.Decisao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Script para atualizar o nome do programa para LEXTRADER-IAG 4.0 em todos os arquivos
 * ==============================================================================
 * Versão Java do script de atualização em massa de nomes do programa
 */

// Classe para representar resultado da atualização
class AtualizacaoResultado {
    boolean modificado;
    int substituicoes;
    String mensagem;
    
    public AtualizacaoResultado(boolean modificado, int substituicoes, String mensagem) {
        this.modificado = modificado;
        this.substituicoes = substituicoes;
        this.mensagem = mensagem;
    }
    
    @Override
    public String toString() {
        return String.format("AtualizacaoResultado{modificado=%s, substituicoes=%d, mensagem='%s'}", 
                           modificado, substituicoes, mensagem);
    }
}

// Classe principal
class AtualizadorNomePrograma {
    private Map<String, String> substituicoes;
    private Set<String> extensoes;
    private Set<String> ignorarPastas;
    private Set<String> ignorarArquivos;
    private boolean dryRun;
    private Path diretorioRaiz;
    
    private static final Logger logger = Logger.getLogger(AtualizadorNomePrograma.class.getName());
    
    public AtualizadorNomePrograma(boolean dryRun) {
        this.dryRun = dryRun;
        this.diretorioRaiz = Paths.get(".");
        inicializarConfiguracoes();
        logger.info("AtualizadorNomePrograma inicializado - modo: " + (dryRun ? "DRY-RUN" : "APLICAÇÃO"));
    }
    
    private void inicializarConfiguracoes() {
        /** Inicializa configurações de substituição e filtros */
        
        // Mapeamento de substituições
        substituicoes = new LinkedHashMap<>();
        substituicoes.put("Quantum Algorithms Trader", "LEXTRADER-IAG 4.0 - Quantum Algorithms Trader");
        substituicoes.put("QUANTUM ALGORITHMS TRADER", "LEXTRADER-IAG 4.0 - QUANTUM ALGORITHMS TRADER");
        substituicoes.put("Sistema de Trading", "LEXTRADER-IAG 4.0 - Sistema de Trading");
        substituicoes.put("Sistema Quântico", "LEXTRADER-IAG 4.0 - Sistema Quântico");
        substituicoes.put("Trading System", "LEXTRADER-IAG 4.0 - Trading System");
        substituicoes.put("Quantum System", "LEXTRADER-IAG 4.0 - Quantum System");
        
        // Evitar duplicação
        substituicoes.put("LEXTRADER-IAG 4\\.0 - LEXTRADER-IAG 4\\.0", "LEXTRADER-IAG 4.0");
        
        // Extensões a processar
        extensoes = new HashSet<>();
        extensoes.add(".py");
        extensoes.add(".md");
        extensoes.add(".txt");
        extensoes.add(".json");
        extensoes.add(".java");
        extensoes.add(".xml");
        extensoes.add(".properties");
        extensoes.add(".yml");
        extensoes.add(".yaml");
        
        // Pastas a ignorar
        ignorarPastas = new HashSet<>();
        ignorarPastas.add("__pycache__");
        ignorarPastas.add(".git");
        ignorarPastas.add("node_modules");
        ignorarPastas.add(".vscode");
        ignorarPastas.add("target");
        ignorarPastas.add("build");
        ignorarPastas.add(".idea");
        ignorarPastas.add("bin");
        ignorarPastas.add("out");
        
        // Arquivos a ignorar
        ignorarArquivos = new HashSet<>();
        ignorarArquivos.add("AtualizarNomePrograma.java");
        ignorarArquivos.add("atualizar_nome_programa.py");
    }
    
    public boolean deveProcessar(Path caminho) {
        /** Verifica se o arquivo deve ser processado */
        
        // Ignorar pastas específicas
        for (String pasta : ignorarPastas) {
            if (caminho.toString().contains(pasta)) {
                return false;
            }
        }
        
        // Ignorar arquivos específicos
        if (ignorarArquivos.contains(caminho.getFileName().toString())) {
            return false;
        }
        
        // Processar apenas extensões específicas
        String extensao = caminho.getFileName() != null ? 
            getExtension(caminho.getFileName().toString()) : "";
        
        return extensoes.contains(extensao);
    }
    
    private String getExtension(String fileName) {
        /** Obtém extensão do arquivo */
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
    
    public AtualizacaoResultado atualizarArquivo(Path caminho) {
        /** Atualiza um arquivo com as substituições */
        
        try {
            // Ler conteúdo do arquivo
            String conteudo = lerArquivo(caminho);
            if (conteudo == null) {
                return new AtualizacaoResultado(false, 0, "Não foi possível ler o arquivo");
            }
            
            String conteudoOriginal = conteudo;
            int totalSubstituicoes = 0;
            Map<String, Integer> substituicoesPorPadrao = new HashMap<>();
            
            // Aplicar substituições
            for (Map.Entry<String, String> entry : substituicoes.entrySet()) {
                String padrao = entry.getKey();
                String substituicao = entry.getValue();
                
                try {
                    Pattern pattern = Pattern.compile(Pattern.quote(padrao));
                    Matcher matcher = pattern.matcher(conteudo);
                    
                    int matches = 0;
                    while (matcher.find()) {
                        matches++;
                    }
                    
                    if (matches > 0) {
                        conteudo = matcher.replaceAll(Matcher.quoteReplacement(substituicao));
                        totalSubstituicoes += matches;
                        substituicoesPorPadrao.put(padrao, matches);
                    }
                    
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Erro no padrão '" + padrao + "': " + e.getMessage());
                }
            }
            
            // Verificar se houve mudanças
            if (!conteudo.equals(conteudoOriginal)) {
                if (!dryRun) {
                    boolean sucesso = escreverArquivo(caminho, conteudo);
                    if (sucesso) {
                        String detalhes = formatarDetalhesSubstituicoes(substituicoesPorPadrao);
                        return new AtualizacaoResultado(true, totalSubstituicoes, 
                            "Arquivo atualizado: " + caminho + detalhes);
                    } else {
                        return new AtualizacaoResultado(false, 0, "Erro ao escrever arquivo");
                    }
                } else {
                    String detalhes = formatarDetalhesSubstituicoes(substituicoesPorPadrao);
                    return new AtualizacaoResultado(true, totalSubstituicoes, 
                        "Seria atualizado: " + caminho + detalhes);
                }
            }
            
            return new AtualizacaoResultado(false, 0, "Sem alterações");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao processar arquivo: " + caminho, e);
            return new AtualizacaoResultado(false, 0, "Erro: " + e.getMessage());
        }
    }
    
    private String lerArquivo(Path caminho) {
        /** Lê conteúdo do arquivo com múltiplas codificações */
        
        // Tentar UTF-8 primeiro
        try {
            byte[] bytes = Files.readAllBytes(caminho);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Tentar ISO-8859-1
            try {
                byte[] bytes = Files.readAllBytes(caminho);
                return new String(bytes, StandardCharsets.ISO_8859_1);
            } catch (IOException e2) {
                // Tentar Windows-1252
                try {
                    byte[] bytes = Files.readAllBytes(caminho);
                    return new String(bytes, Charset.forName("Windows-1252"));
                } catch (Exception e3) {
                    logger.log(Level.WARNING, "Não foi possível ler arquivo: " + caminho);
                    return null;
                }
            }
        }
    }
    
    private boolean escreverArquivo(Path caminho, String conteudo) {
        /** Escreve conteúdo no arquivo */
        try {
            Files.write(caminho, conteudo.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao escrever arquivo: " + caminho, e);
            return false;
        }
    }
    
    private String formatarDetalhesSubstituicoes(Map<String, Integer> substituicoesPorPadrao) {
        /** Formata detalhes das substituições realizadas */
        if (substituicoesPorPadrao.isEmpty()) {
            return "";
        }
        
        StringBuilder detalhes = new StringBuilder(" (");
        boolean primeiro = true;
        
        for (Map.Entry<String, Integer> entry : substituicoesPorPadrao.entrySet()) {
            if (!primeiro) {
                detalhes.append(", ");
            }
            detalhes.append(entry.getKey()).append(": ").append(entry.getValue());
            primeiro = false;
        }
        
        detalhes.append(")");
        return detalhes.toString();
    }
    
    public RelatorioProcessamento processarTodosArquivos() {
        /** Processa todos os arquivos no diretório raiz e subdiretórios */
        
        System.out.println("=".repeat(70));
        System.out.println("🔄 ATUALIZAÇÃO DO NOME DO PROGRAMA PARA LEXTRADER-IAG 4.0");
        System.out.println("=".repeat(70));
        
        if (dryRun) {
            System.out.println("\n⚠️  MODO DRY-RUN (visualização)");
            System.out.println("   Use --apply para aplicar as mudanças");
        } else {
            System.out.println("\n✅ MODO APLICAÇÃO (mudanças serão salvas)");
        }
        
        System.out.println("\n📁 Processando arquivos...");
        System.out.println("-".repeat(70));
        
        int arquivosProcessados = 0;
        int arquivosModificados = 0;
        int totalSubstituicoes = 0;
        List<String> erros = new ArrayList<>();
        List<String> modificacoes = new ArrayList<>();
        
        try {
            // Processar todos os arquivos recursivamente
            Files.walk(diretorioRaiz)
                .filter(Files::isRegularFile)
                .filter(this::deveProcessar)
                .forEach(caminho -> {
                    arquivosProcessados++;
                    
                    AtualizacaoResultado resultado = atualizarArquivo(caminho);
                    
                    if (resultado.modificado) {
                        arquivosModificados++;
                        totalSubstituicoes += resultado.substituicoes;
                        modificacoes.add(resultado.mensagem);
                        
                        if (dryRun) {
                            System.out.println("🔍 " + resultado.mensagem);
                        } else {
                            System.out.println("✅ " + resultado.mensagem);
                        }
                    }
                });
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao percorrer diretórios", e);
            erros.add("Erro ao percorrer diretórios: " + e.getMessage());
        }
        
        return new RelatorioProcessamento(
            arquivosProcessados,
            arquivosModificados,
            totalSubstituicoes,
            erros,
            modificacoes,
            dryRun
        );
    }
    
    // Getters e Setters
    public boolean isDryRun() {
        return dryRun;
    }
    
    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }
    
    public Path getDiretorioRaiz() {
        return diretorioRaiz;
    }
    
    public void setDiretorioRaiz(Path diretorioRaiz) {
        this.diretorioRaiz = diretorioRaiz;
    }
    
    public Map<String, String> getSubstituicoes() {
        return new HashMap<>(substituicoes);
    }
    
    public void adicionarSubstituicao(String padrao, String substituicao) {
        substituicoes.put(padrao, substituicao);
    }
    
    public void removerSubstituicao(String padrao) {
        substituicoes.remove(padrao);
    }
}

// Classe para relatório final
class RelatorioProcessamento {
    int arquivosProcessados;
    int arquivosModificados;
    int totalSubstituicoes;
    List<String> erros;
    List<String> modificacoes;
    boolean dryRun;
    
    public RelatorioProcessamento(int arquivosProcessados, int arquivosModificados, int totalSubstituicoes,
                                 List<String> erros, List<String> modificacoes, boolean dryRun) {
        this.arquivosProcessados = arquivosProcessados;
        this.arquivosModificados = arquivosModificados;
        this.totalSubstituicoes = totalSubstituicoes;
        this.erros = erros;
        this.modificacoes = modificacoes;
        this.dryRun = dryRun;
    }
    
    public void imprimirRelatorio() {
        /** Imprime relatório final do processamento */
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 RESUMO");
        System.out.println("=".repeat(70));
        System.out.println("Arquivos processados: " + arquivosProcessados);
        System.out.println("Arquivos modificados: " + arquivosModificados);
        System.out.println("Total de substituições: " + totalSubstituicoes);
        
        if (!erros.isEmpty()) {
            System.out.println("\n❌ Erros encontrados:");
            for (String erro : erros) {
                System.out.println("   " + erro);
            }
        }
        
        if (dryRun && arquivosModificados > 0) {
            System.out.println("\n💡 Para aplicar as mudanças, execute:");
            System.out.println("   java -cp . modulo_emocional.Decisao.AtualizarNomePrograma --apply");
        } else if (!dryRun && arquivosModificados > 0) {
            System.out.println("\n✅ Mudanças aplicadas com sucesso!");
        } else {
            System.out.println("\n✅ Nenhuma mudança necessária");
        }
        
        System.out.println("=".repeat(70));
    }
    
    // Getters
    public int getArquivosProcessados() {
        return arquivosProcessados;
    }
    
    public int getArquivosModificados() {
        return arquivosModificados;
    }
    
    public int getTotalSubstituicoes() {
        return totalSubstituicoes;
    }
    
    public List<String> getErros() {
        return new ArrayList<>(erros);
    }
    
    public List<String> getModificacoes() {
        return new ArrayList<>(modificacoes);
    }
    
    public boolean isDryRun() {
        return dryRun;
    }
}

// Classe Principal
public class AtualizarNomePrograma {
    
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Atualizador de Nome do Programa...");
        
        // Verificar argumentos
        boolean dryRun = !containsArgument(args, "--apply");
        
        // Criar atualizador
        AtualizadorNomePrograma atualizador = new AtualizadorNomePrograma(dryRun);
        
        // Processar arquivos
        RelatorioProcessamento relatorio = atualizador.processarTodosArquivos();
        
        // Imprimir relatório final
        relatorio.imprimirRelatorio();
        
        // Exit code baseado no resultado
        System.exit(relatorio.getErros().isEmpty() ? 0 : 1);
    }
    
    private static boolean containsArgument(String[] args, String argument) {
        /** Verifica se um argumento está presente no array */
        for (String arg : args) {
            if (arg.equals(argument)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Método utilitário para uso programático
     */
    public static RelatorioProcessamento atualizarNomes(boolean aplicarMudancas) {
        /** Método para uso programático da atualização */
        AtualizadorNomePrograma atualizador = new AtualizadorNomePrograma(!aplicarMudancas);
        return atualizador.processarTodosArquivos();
    }
    
    /**
     * Método utilitário para atualizar apenas um diretório específico
     */
    public static RelatorioProcessamento atualizarNomes(String diretorio, boolean aplicarMudancas) {
        /** Método para atualizar apenas um diretório específico */
        AtualizadorNomePrograma atualizador = new AtualizadorNomePrograma(!aplicarMudancas);
        atualizador.setDiretorioRaiz(Paths.get(diretorio));
        return atualizador.processarTodosArquivos();
    }
    
    /**
     * Método utilitário para adicionar substituições personalizadas
     */
    public static RelatorioProcessamento atualizarNomesComSubstituicoes(
            Map<String, String> substituicoesAdicionais, boolean aplicarMudancas) {
        /** Método para usar substituições personalizadas */
        AtualizadorNomePrograma atualizador = new AtualizadorNomePrograma(!aplicarMudancas);
        
        // Adicionar substituições personalizadas
        for (Map.Entry<String, String> entry : substituicoesAdicionais.entrySet()) {
            atualizador.adicionarSubstituicao(entry.getKey(), entry.getValue());
        }
        
        return atualizador.processarTodosArquivos();
    }
    
    /**
     * Exemplo de uso avançado
     */
    public static void exemploAvancado() {
        /** Exemplo de uso avançado com configurações personalizadas */
        
        // Criar substituições personalizadas
        Map<String, String> substituicoesPersonalizadas = new HashMap<>();
        substituicoesPersonalizadas.put("Nome Antigo", "Nome Novo");
        substituicoesPersonalizadas.put("OLD_VERSION", "NEW_VERSION");
        
        // Executar atualização
        RelatorioProcessamento relatorio = atualizarNomesComSubstituicoes(substituicoesPersonalizadas, false);
        
        // Analisar resultados
        if (relatorio.getArquivosModificados() > 0) {
            System.out.println("Arquivos modificados: " + relatorio.getArquivosModificados());
            
            // Aplicar mudanças se desejar
            RelatorioProcessamento relatorioAplicacao = atualizarNomesComSubstituicoes(substituicoesPersonalizadas, true);
            relatorioAplicacao.imprimirRelatorio();
        }
    }
}
