package modulo_emocional.Motor;

/**
 * Classe de demonstração para o módulo Motor completo
 */
public class MotorDemo {
    
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("    MÓDULO MOTOR - DEMONSTRAÇÃO");
        System.out.println("=====================================");
        
        // Executar demonstrações de cada componente
        System.out.println("\n1. ConfigMotor:");
        ConfigMotor.demonstracao();
        
        System.out.println("\n2. Movimentos Voluntários:");
        MovimentosVoluntarios.demonstracao();
        
        System.out.println("\n3. Padrões Repetitivos:");
        PadroesRepetitivos.demonstracao();
        
        System.out.println("\n4. Controle de Postura:");
        Postura.demonstracao();
        
        System.out.println("\n5. Predições e Constância:");
        PredicoesConstancia.demonstracao();
        
        System.out.println("\n6. Reflexos Econômicos:");
        ReflexosEconomicos.demonstracao();
        
        System.out.println("\n7. Processador Central (Integração Completa):");
        ProcessadorCentral.demonstracao();
        
        System.out.println("\n=====================================");
        System.out.println("    DEMONSTRAÇÃO CONCLUÍDA");
        System.out.println("=====================================");
    }
}
