"""
Geração e reconhecimento de padrões repetitivos
"""

class GeradorPadroes:
    def __init__(self):
        self.padroes_aprendidos = {}
        self.sequencias_ativas = []
        
    def identificar_padrao(self, sequencia):
        """
        Identifica padrões em sequências de movimentos
        
        Args:
            sequencia: Lista de movimentos ou ações
        
        Returns:
            dict: Padrão identificado ou None
        """
        if not sequencia:
            return None
            
        # Converte sequência para string hash para comparação
        seq_hash = self._gerar_hash_sequencia(sequencia)
        
        # Verifica se padrão já existe
        for nome, padrao in self.padroes_aprendidos.items():
            if padrao["hash"] == seq_hash:
                padrao["frequencia"] += 1
                return {"nome": nome, "padrao": padrao}
        
        # Se não encontrado, cria novo padrão
        novo_nome = f"padrao_{len(self.padroes_aprendidos) + 1}"
        novo_padrao = {
            "sequencia": sequencia,
            "hash": seq_hash,
            "frequencia": 1,
            "contexto": "geral"
        }
        
        self.padroes_aprendidos[novo_nome] = novo_padrao
        return {"nome": novo_nome, "padrao": novo_padrao}
    
    def otimizar_sequencia(self, sequencia):
        """
        Otimiza uma sequência baseada em padrões aprendidos
        
        Args:
            sequencia: Sequência original
        
        Returns:
            list: Sequência otimizada
        """
        padrao = self.identificar_padrao(sequencia)
        
        if padrao and padrao["padrao"]["frequencia"] > 3:
            # Se padrão frequente, pode aplicar otimizações
            sequencia_otimizada = sequencia.copy()
            
            # Simplificação hipotética
            if len(sequencia) > 5:
                # Remove etapas redundantes para sequências longas
                sequencia_otimizada = self._remover_redundancias(sequencia_otimizada)
            
            return sequencia_otimizada
        
        return sequencia
    
    def _gerar_hash_sequencia(self, sequencia):
        """Gera hash único para sequência"""
        import hashlib
        seq_str = str(sequencia).encode()
        return hashlib.md5(seq_str).hexdigest()[:8]
    
    def _remover_redundancias(self, sequencia):
        """Remove elementos redundantes da sequência"""
        if len(sequencia) <= 1:
            return sequencia
        
        sequencia_simplificada = [sequencia[0]]
        for item in sequencia[1:]:
            if item != sequencia_simplificada[-1]:
                sequencia_simplificada.append(item)
        
        return sequencia_simplificada