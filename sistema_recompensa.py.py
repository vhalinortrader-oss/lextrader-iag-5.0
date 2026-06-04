"""
Sistema de motivação e recompensa
"""

import time
from typing import Dict, List, Optional

class SistemaRecompensa:
    """Gerencia sistema de recompensa e motivação"""
    
    def __init__(self, config):
        self.config = config
        self.recompensas_registradas = []
        self.nivel_motivacao = 0.6
        self.satisfacao_atual = 0.5
        
        # Tipos de recompensa
        self.tipos_recompensa = [
            "intrinseca",  # Satisfação interna
            "extrinseca",  # Recompensa externa
            "social",      # Reconhecimento social
            "progresso",   # Sensação de progresso
            "aprendizado"  # Novo conhecimento/habilidade
        ]
    
    def avaliar_acao(self, acao: Dict, resultado: Dict) -> Dict:
        """
        Avalia uma ação e atribui recompensa
        
        Args:
            acao: Dicionário com informações da ação
            resultado: Resultado da ação
        
        Returns:
            Dict: Avaliação e recompensa calculada
        """
        # Calcula recompensa baseada em múltiplos fatores
        recompensa_total = 0
        fatores = {}
        
        # 1. Resultado objetivo
        if resultado.get("sucesso"):
            fator_resultado = 0.6
        elif resultado.get("parcial"):
            fator_resultado = 0.3
        else:
            fator_resultado = -0.2
        
        recompensa_total += fator_resultado
        fatores["resultado"] = fator_resultado
        
        # 2. Esforço aplicado
        esforco = acao.get("esforco", 0.5)
        fator_esforco = esforco * 0.3
        recompensa_total += fator_esforco
        fatores["esforco"] = fator_esforco
        
        # 3. Dificuldade
        dificuldade = acao.get("dificuldade", 0.5)
        if resultado.get("sucesso") and dificuldade > 0.7:
            # Bônus por superar desafios difíceis
            fator_dificuldade = 0.4
            recompensa_total += fator_dificuldade
            fatores["dificuldade_superada"] = fator_dificuldade
        
        # 4. Impacto social/coletivo
        if resultado.get("impacto_social"):
            fator_social = min(0.5, resultado["impacto_social"] * 0.3)
            recompensa_total += fator_social
            fatores["impacto_social"] = fator_social
        
        # 5. Aprendizado obtido
        if resultado.get("aprendizado"):
            fator_aprendizado = min(0.4, resultado["aprendizado"] * 0.4)
            recompensa_total += fator_aprendizado
            fatores["aprendizado"] = fator_aprendizado
        
        # Normaliza recompensa
        recompensa_total = max(-1.0, min(1.0, recompensa_total))
        
        # Tipo de recompensa predominante
        tipo_principal = self._determinar_tipo_recompensa(fatores)
        
        # Registra recompensa
        registro = {
            "timestamp": time.time(),
            "acao": acao.get("descricao", "acao_nao_especificada"),
            "recompensa_total": recompensa_total,
            "fatores": fatores,
            "tipo_principal": tipo_principal,
            "resultado": resultado
        }
        
        self.recompensas_registradas.append(registro)
        
        # Atualiza motivação e satisfação
        self._atualizar_estado_interno(recompensa_total, tipo_principal)
        
        return {
            "recompensa_calculada": recompensa_total,
            "analise_detalhada": fatores,
            "tipo_recompensa": tipo_principal,
            "impacto_motivacional": self._calcular_impacto_motivacional(recompensa_total),
            "registro": registro
        }
    
    def _determinar_tipo_recompensa(self, fatores: Dict) -> str:
        """Determina tipo de recompensa predominante"""
        mapeamento = {
            "aprendizado": "aprendizado",
            "impacto_social": "social",
            "resultado": "extrinseca",
            "esforco": "intrinseca",
            "dificuldade_superada": "progresso"
        }
        
        # Encontra fator com maior valor positivo
        fatores_positivos = {k: v for k, v in fatores.items() if v > 0}
        
        if not fatores_positivos:
            return "intrinseca"  # Default
        
        fator_principal = max(fatores_positivos.items(), key=lambda x: x[1])[0]
        
        return mapeamento.get(fator_principal, "intrinseca")
    
    def _atualizar_estado_interno(self, recompensa: float, tipo: str):
        """Atualiza estado motivacional interno"""
        # Atualiza satisfação
        ajuste_satisfacao = recompensa * 0.2
        self.satisfacao_atual = max(0.0, min(1.0, 
            self.satisfacao_atual + ajuste_satisfacao))
        
        # Atualiza motivação (com efeito diferente baseado no tipo)
        fatores_tipo = {
            "intrinseca": 1.0,
            "extrinseca": 0.7,
            "social": 0.8,
            "progresso": 1.2,  # Progresso é altamente motivador
            "aprendizado": 0.9
        }
        
        ajuste_motivacao = recompensa * 0.15 * fatores_tipo.get(tipo, 1.0)
        self.nivel_motivacao = max(0.0, min(1.0,
            self.nivel_motivacao + ajuste_motivacao))
    
    def _calcular_impacto_motivacional(self, recompensa: float) -> float:
        """Calcula impacto na motivação futura"""
        if recompensa > 0.5:
            return 0.8  # Alto impacto positivo
        elif recompensa > 0:
            return 0.4  # Moderado positivo
        elif recompensa > -0.3:
            return -0.2  # Leve negativo
        else:
            return -0.6  # Significativamente negativo
    
    def gerar_recompensa_antecipada(self, acao_planejada: Dict) -> Dict:
        """
        Gera recompensa antecipada para planejamento
        
        Args:
            acao_planejada: Ação sendo planejada
        
        Returns:
            Dict: Previsão de recompensa
        """
        # Baseado em histórico similar
        acoes_similares = self._encontrar_acoes_similares(acao_planejada)
        
        if acoes_similares:
            # Média de recompensas de ações similares
            recompensas = [r["recompensa_total"] for r in acoes_similares]
            recompensa_estimada = sum(recompensas) / len(recompensas)
            confianca = min(0.9, len(acoes_similares) * 0.1)
        else:
            # Estimativa baseada em características
            recompensa_estimada = self._estimar_recompensa_base(acao_planejada)
            confianca = 0.3
        
        return {
            "recompensa_estimada": recompensa_estimada,
            "confianca_estimativa": confianca,
            "tipo_esperado": self._prever_tipo_recompensa(acao_planejada),
            "acoes_similares_encontradas": len(acoes_similares)
        }
    
    def _encontrar_acoes_similares(self, acao: Dict) -> List[Dict]:
        """Encontra ações similares no histórico"""
        similares = []
        
        for registro in self.recompensas_registradas[-50:]:  # Últimas 50
            similaridade = self._calcular_similaridade_acoes(
                acao, registro.get("acao", {})
            )
            
            if similaridade > 0.6:
                similares.append(registro)
        
        return similares
    
    def _calcular_similaridade_acoes(self, acao1: Dict, acao2: Dict) -> float:
        """Calcula similaridade entre duas ações"""
        # Implementação simplificada
        if not isinstance(acao1, dict) or not isinstance(acao2, dict):
            return 0
        
        # Verifica descrição
        desc1 = str(acao1.get("descricao", "")).lower()
        desc2 = str(acao2.get("descricao", "")).lower()
        
        palavras1 = set(desc1.split())
        palavras2 = set(desc2.split())
        
        if palavras1 and palavras2:
            similaridade_desc = len(palavras1 & palavras2) / len(palavras1 | palavras2)
        else:
            similaridade_desc = 0
        
        # Verifica outras características
        similaridade_caracteristicas = 0
        for chave in ["dificuldade", "esforco", "tipo"]:
            if chave in acao1 and chave in acao2:
                if acao1[chave] == acao2[chave]:
                    similaridade_caracteristicas += 0.1
        
        return min(1.0, similaridade_desc + similaridade_caracteristicas)
    
    def _estimar_recompensa_base(self, acao: Dict) -> float:
        """Estima recompensa baseada em características da ação"""
        estimativa = 0.5  # Neutro
        
        # Ajustes baseados em características
        if acao.get("dificuldade", 0.5) > 0.7:
            estimativa += 0.2  # Desafios oferecem maior recompensa potencial
        
        if acao.get("impacto_potencial", 0) > 0.6:
            estimativa += 0.3
        
        if acao.get("novidade", 0) > 0.5:
            estimativa += 0.1  # Novidade pode aumentar recompensa
        
        return min(1.0, max(-1.0, estimativa))
    
    def _prever_tipo_recompensa(self, acao: Dict) -> str:
        """Prevê tipo de recompensa baseado na ação"""
        caracteristicas = acao.get("caracteristicas", [])
        
        if "aprendizado" in caracteristicas:
            return "aprendizado"
        elif "social" in caracteristicas:
            return "social"
        elif "desafio" in caracteristicas:
            return "progresso"
        elif "rotina" in caracteristicas:
            return "extrinseca"
        else:
            return "intrinseca"
    
    def obter_estatisticas_motivacao(self) -> Dict:
        """Retorna estatísticas do sistema de recompensa"""
        if not self.recompensas_registradas:
            return {"mensagem": "Nenhuma recompensa registrada ainda"}
        
        # Análise de tendências
        recentes = self.recompensas_registradas[-20:] if len(self.recompensas_registradas) > 20 else self.recompensas_registradas
        
        recompensas_valores = [r["recompensa_total"] for r in recentes]
        tipos_recompensa = [r["tipo_principal"] for r in recentes]
        
        from collections import Counter
        contagem_tipos = Counter(tipos_recompensa)
        
        return {
            "nivel_motivacao_atual": self.nivel_motivacao,
            "satisfacao_atual": self.satisfacao_atual,
            "total_acoes_avaliadas": len(self.recompensas_registradas),
            "recompensa_media_recente": sum(recompensas_valores) / len(recompensas_valores),
            "distribuicao_tipos_recompensa": dict(contagem_tipos),
            "tendencia_motivacional": self._analisar_tendencia_motivacional(),
            "recomendacoes": self._gerar_recomendacoes_motivacao()
        }
    
    def _analisar_tendencia_motivacional(self) -> str:
        """Analisa tendência motivacional"""
        if len(self.recompensas_registradas) < 5:
            return "estavel"
        
        # Analisa últimas 10 recompensas
        recentes = self.recompensas_registradas[-10:] if len(self.recompensas_registradas) >= 10 else self.recompensas_registradas
        recompensas = [r["recompensa_total"] for r in recentes]
        
        media_primeira_metade = sum(recompensas[:len(recompensas)//2]) / (len(recompensas)//2)
        media_segunda_metade = sum(recompensas[len(recompensas)//2:]) / (len(recompensas)//2)
        
        if media_segunda_metade > media_primeira_metade * 1.2:
            return "ascendente"
        elif media_segunda_metade < media_primeira_metade * 0.8:
            return "descendente"
        else:
            return "estavel"
    
    def _gerar_recomendacoes_motivacao(self) -> List[str]:
        """Gera recomendações para manutenção da motivação"""
        recomendacoes = []
        
        if self.nivel_motivacao < 0.4:
            recomendacoes.append("Buscar atividades com alta probabilidade de recompensa intrínseca")
        
        if self.satisfacao_atual < 0.4:
            recomendacoes.append("Estabelecer metas menores e mais alcançáveis")
        
        # Analisa tipos de recompensa
        if len(self.recompensas_registradas) > 10:
            tipos = [r["tipo_principal"] for r in self.recompensas_registradas[-20:]]
            from collections import Counter
            contagem = Counter(tipos)
            
            if contagem.get("extrinseca", 0) > contagem.get("intrinseca", 0) * 2:
                recomendacoes.append("Balancear recompensas extrínsecas com intrínsecas")
        
        return recomendacoes