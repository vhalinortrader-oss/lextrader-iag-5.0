"""
Gerenciamento de objetivos e metas
"""

import time
from typing import Dict, List, Optional

class GerenciadorObjetivos:
    """Gerencia objetivos e metas para manutenção da motivação"""
    
    def __init__(self, config):
        self.config = config
        self.objetivos_ativos = []
        self.objetivos_concluidos = []
        self.historico_progresso = []
        
    def definir_objetivo(self, titulo: str, descricao: str, 
                         prazo: Optional[float] = None,
                         prioridade: str = "media",
                         metricas: Optional[Dict] = None) -> Dict:
        """
        Define um novo objetivo
        
        Args:
            titulo: Título do objetivo
            descricao: Descrição detalhada
            prazo: Timestamp do prazo (opcional)
            prioridade: alta/media/baixa
            metricas: Métricas para medir progresso
        
        Returns:
            Dict: Objetivo criado
        """
        objetivo = {
            "id": f"obj_{int(time.time())}_{len(self.objetivos_ativos)}",
            "titulo": titulo,
            "descricao": descricao,
            "prazo": prazo,
            "prioridade": prioridade,
            "metricas": metricas or {},
            "progresso": 0.0,
            "status": "ativo",
            "data_criacao": time.time(),
            "submetas": [],
            "milestones": [],
            "dificuldade_estimada": 0.5,
            "valor_motivacional": self._calcular_valor_motivacional(titulo, descricao, prioridade)
        }
        
        self.objetivos_ativos.append(objetivo)
        
        # Inicializa histórico de progresso
        self.historico_progresso.append({
            "timestamp": time.time(),
            "objetivo_id": objetivo["id"],
            "progresso": 0.0,
            "evento": "criacao"
        })
        
        return objetivo
    
    def _calcular_valor_motivacional(self, titulo: str, descricao: str, 
                                    prioridade: str) -> float:
        """Calcula valor motivacional de um objetivo"""
        valor = 0.5
        
        # Ajustes baseados em características
        palavras_chave_motivacionais = [
            "aprender", "crescer", "melhorar", "desafio", 
            "superar", "criar", "inov", "ajudar"
        ]
        
        texto = f"{titulo} {descricao}".lower()
        for palavra in palavras_chave_motivacionais:
            if palavra in texto:
                valor += 0.1
        
        # Ajuste por prioridade
        ajustes_prioridade = {
            "alta": 0.3,
            "media": 0.1,
            "baixa": 0.0
        }
        
        valor += ajustes_prioridade.get(prioridade, 0.1)
        
        return min(1.0, max(0.1, valor))
    
    def atualizar_progresso(self, objetivo_id: str, progresso: float,
                           observacoes: Optional[str] = None) -> Dict:
        """
        Atualiza progresso de um objetivo
        
        Args:
            objetivo_id: ID do objetivo
            progresso: Novo valor de progresso (0 a 1)
            observacoes: Observações sobre o progresso
        
        Returns:
            Dict: Resultado da atualização
        """
        objetivo = self._encontrar_objetivo_por_id(objetivo_id)
        
        if not objetivo:
            return {"erro": "Objetivo não encontrado"}
        
        progresso_antigo = objetivo["progresso"]
        objetivo["progresso"] = max(0.0, min(1.0, progresso))
        
        # Registra no histórico
        self.historico_progresso.append({
            "timestamp": time.time(),
            "objetivo_id": objetivo_id,
            "progresso_antigo": progresso_antigo,
            "progresso_novo": objetivo["progresso"],
            "delta": objetivo["progresso"] - progresso_antigo,
            "observacoes": observacoes,
            "evento": "atualizacao_progresso"
        })
        
        # Verifica se objetivo foi concluído
        if objetivo["progresso"] >= 0.99:
            return self.concluir_objetivo(objetivo_id)
        
        return {
            "sucesso": True,
            "objetivo_id": objetivo_id,
            "progresso_atual": objetivo["progresso"],
            "progresso_anterior": progresso_antigo,
            "delta": objetivo["progresso"] - progresso_antigo
        }
    
    def _encontrar_objetivo_por_id(self, objetivo_id: str) -> Optional[Dict]:
        """Encontra objetivo pelo ID"""
        for objetivo in self.objetivos_ativos:
            if objetivo["id"] == objetivo_id:
                return objetivo
        return None
    
    def concluir_objetivo(self, objetivo_id: str) -> Dict:
        """
        Marca objetivo como concluído
        
        Args:
            objetivo_id: ID do objetivo
        
        Returns:
            Dict: Resultado da conclusão
        """
        objetivo = self._encontrar_objetivo_por_id(objetivo_id)
        
        if not objetivo:
            return {"erro": "Objetivo não encontrado"}
        
        # Atualiza status
        objetivo["status"] = "concluido"
        objetivo["progresso"] = 1.0
        objetivo["data_conclusao"] = time.time()
        
        # Calcula tempo para conclusão
        tempo_total = objetivo["data_conclusao"] - objetivo["data_criacao"]
        
        # Move para lista de concluídos
        self.objetivos_concluidos.append(objetivo)
        self.objetivos_ativos.remove(objetivo)
        
        # Registra conclusão
        self.historico_progresso.append({
            "timestamp": time.time(),
            "objetivo_id": objetivo_id,
            "progresso": 1.0,
            "evento": "conclusao",
            "tempo_total_segundos": tempo_total
        })
        
        # Calcula recompensa por conclusão
        recompensa_conclusao = self._calcular_recompensa_conclusao(objetivo)
        
        return {
            "sucesso": True,
            "objetivo": objetivo["titulo"],
            "tempo_total_dias": tempo_total / 86400,
            "recompensa_conclusao": recompensa_conclusao,
            "mensagem": f"Objetivo '{objetivo['titulo']}' concluído com sucesso!"
        }
    
    def _calcular_recompensa_conclusao(self, objetivo: Dict) -> Dict:
        """Calcula recompensa por conclusão de objetivo"""
        base_recompensa = 0.7
        
        # Ajustes
        if objetivo.get("dificuldade_estimada", 0.5) > 0.7:
            base_recompensa += 0.2
        
        if objetivo.get("prioridade") == "alta":
            base_recompensa += 0.1
        
        # Verifica se concluiu antes do prazo
        if objetivo.get("prazo"):
            tempo_restante = objetivo["prazo"] - time.time()
            if tempo_restante > 0:
                base_recompensa += 0.15  # Bônus por antecipação
        
        return {
            "valor": min(1.0, base_recompensa),
            "tipo": "progresso",
            "fatores": {
                "dificuldade": objetivo.get("dificuldade_estimada", 0.5),
                "prioridade": objetivo.get("prioridade"),
                "valor_motivacional": objetivo.get("valor_motivacional", 0.5)
            }
        }
    
    def adicionar_submeta(self, objetivo_id: str, submeta: Dict) -> Dict:
        """
        Adiciona uma submeta a um objetivo
        
        Args:
            objetivo_id: ID do objetivo
            submeta: Dicionário com informações da submeta
        
        Returns:
            Dict: Resultado da operação
        """
        objetivo = self._encontrar_objetivo_por_id(objetivo_id)
        
        if not objetivo:
            return {"erro": "Objetivo não encontrado"}
        
        submeta_completa = {
            "id": f"sub_{int(time.time())}_{len(objetivo['submetas'])}",
            "descricao": submeta.get("descricao", ""),
            "concluida": False,
            "peso": submeta.get("peso", 0.1),  # Peso no progresso total
            "data_criacao": time.time()
        }
        
        objetivo["submetas"].append(submeta_completa)
        
        return {
            "sucesso": True,
            "objetivo_id": objetivo_id,
            "submeta": submeta_completa
        }
    
    def concluir_submeta(self, objetivo_id: str, submeta_id: str) -> Dict:
        """
        Marca uma submeta como concluída
        
        Args:
            objetivo_id: ID do objetivo
            submeta_id: ID da submeta
        
        Returns:
            Dict: Resultado da operação
        """
        objetivo = self._encontrar_objetivo_por_id(objetivo_id)
        
        if not objetivo:
            return {"erro": "Objetivo não encontrado"}
        
        # Encontra submeta
        submeta_encontrada = None
        for submeta in objetivo["submetas"]:
            if submeta["id"] == submeta_id:
                submeta_encontrada = submeta
                break
        
        if not submeta_encontrada:
            return {"erro": "Submeta não encontrada"}
        
        # Atualiza submeta
        submeta_encontrada["concluida"] = True
        submeta_encontrada["data_conclusao"] = time.time()
        
        # Atualiza progresso do objetivo
        peso = submeta_encontrada["peso"]
        progresso_atual = objetivo["progresso"]
        objetivo["progresso"] = min(1.0, progresso_atual + peso)
        
        return {
            "sucesso": True,
            "objetivo_id": objetivo_id,
            "submeta_id": submeta_id,
            "progresso_atualizado": objetivo["progresso"],
            "incremento": peso
        }
    
    def definir_milestone(self, objetivo_id: str, milestone: Dict) -> Dict:
        """
        Define um milestone para um objetivo
        
        Args:
            objetivo_id: ID do objetivo
            milestone: Dicionário com informações do milestone
        
        Returns:
            Dict: Milestone criado
        """
        objetivo = self._encontrar_objetivo_por_id(objetivo_id)
        
        if not objetivo:
            return {"erro": "Objetivo não encontrado"}
        
        milestone_completo = {
            "id": f"milestone_{int(time.time())}_{len(objetivo['milestones'])}",
            "descricao": milestone.get("descricao", ""),
            "progresso_esperado": milestone.get("progresso_esperado", 0.5),
            "alcançado": False,
            "recompensa_associada": milestone.get("recompensa", 0.3)
        }
        
        objetivo["milestones"].append(milestone_completo)
        
        return {
            "sucesso": True,
            "objetivo_id": objetivo_id,
            "milestone": milestone_completo
        }
    
    def obter_objetivos_prioritarios(self, limite: int = 5) -> List[Dict]:
        """
        Retorna objetivos mais prioritários
        
        Args:
            limite: Número máximo de objetivos a retornar
        
        Returns:
            List: Objetivos prioritários
        """
        # Ordena por prioridade e valor motivacional
        objetivos_ordenados = sorted(
            self.objetivos_ativos,
            key=lambda x: (
                3 if x["prioridade"] == "alta" else 
                2 if x["prioridade"] == "media" else 1,
                x["valor_motivacional"]
            ),
            reverse=True
        )
        
        return objetivos_ordenados[:limite]
    
    def obter_estatisticas_objetivos(self) -> Dict:
        """Retorna estatísticas dos objetivos"""
        total_ativos = len(self.objetivos_ativos)
        total_concluidos = len(self.objetivos_concluidos)
        total_geral = total_ativos + total_concluidos
        
        if total_geral == 0:
            return {"mensagem": "Nenhum objetivo registrado"}
        
        # Progresso médio dos ativos
        if total_ativos > 0:
            progresso_medio = sum(o["progresso"] for o in self.objetivos_ativos) / total_ativos
        else:
            progresso_medio = 0
        
        # Taxa de conclusão
        taxa_conclusao = total_concluidos / total_geral if total_geral > 0 else 0
        
        # Tempo médio para conclusão
        tempos_conclusao = []
        for objetivo in self.objetivos_concluidos:
            if "data_conclusao" in objetivo and "data_criacao" in objetivo:
                tempo = objetivo["data_conclusao"] - objetivo["data_criacao"]
                tempos_conclusao.append(tempo)
        
        tempo_medio_conclusao = sum(tempos_conclusao) / len(tempos_conclusao) if tempos_conclusao else 0
        
        return {
            "total_objetivos_ativos": total_ativos,
            "total_objetivos_concluidos": total_concluidos,
            "progresso_medio_ativos": progresso_medio,
            "taxa_conclusao_geral": taxa_conclusao,
            "tempo_medio_conclusao_dias": tempo_medio_conclusao / 86400 if tempo_medio_conclusao > 0 else 0,
            "distribuicao_prioridades": self._calcular_distribuicao_prioridades(),
            "recomendacoes": self._gerar_recomendacoes_objetivos()
        }
    
    def _calcular_distribuicao_prioridades(self) -> Dict:
        """Calcula distribuição de prioridades"""
        distribuicao = {"alta": 0, "media": 0, "baixa": 0}
        
        for objetivo in self.objetivos_ativos:
            prioridade = objetivo.get("prioridade", "media")
            if prioridade in distribuicao:
                distribuicao[prioridade] += 1
        
        return distribuicao
    
    def _gerar_recomendacoes_objetivos(self) -> List[str]:
        """Gera recomendações para gestão de objetivos"""
        recomendacoes = []
        
        total_ativos = len(self.objetivos_ativos)
        
        if total_ativos > 10:
            recomendacoes.append("Considerar consolidar ou priorizar objetivos para evitar sobrecarga")
        
        if total_ativos > 0:
            # Verifica objetivos parados
            objetivos_parados = []
            for objetivo in self.objetivos_ativos:
                # Verifica último progresso
                progressos = [h for h in self.historico_progresso 
                            if h["objetivo_id"] == objetivo["id"]]
                
                if progressos:
                    ultimo_progresso = max(progressos, key=lambda x: x["timestamp"])
                    tempo_desde_progresso = time.time() - ultimo_progresso["timestamp"]
                    
                    if tempo_desde_progresso > 7 * 86400:  # 7 dias
                        objetivos_parados.append(objetivo["titulo"])
            
            if objetivos_parados:
                recomendacoes.append(
                    f"Revisar objetivos parados: {', '.join(objetivos_parados[:2])}"
                )
        
        # Verifica balanceamento de prioridades
        distribuicao = self._calcular_distribuicao_prioridades()
        if distribuicao.get("alta", 0) > distribuicao.get("media", 0) * 2:
            recomendacoes.append("Balancear prioridades para evitar sobrecarga de objetivos de alta prioridade")
        
        return recomendacoes