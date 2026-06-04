"""
Sistema de monitoramento contínuo da saúde emocional
"""

import time
from typing import Dict, List, Optional
from datetime import datetime, timedelta

class MonitorSaudeEmocional:
    """Monitora continuamente a saúde emocional e gera alertas"""
    
    def __init__(self, config):
        self.config = config
        self.metricas_saude = {
            "equilibrio_emocional": 0.7,
            "resiliencia": 0.6,
            "satisfacao_vida": 0.5,
            "qualidade_sono": 0.8,
            "energia_vital": 0.7
        }
        self.historico_metricas = []
        self.tendencia_saude = "estavel"
        self.alertas_saude = []
        self.checkpoints = []
        
        # Limiares para alertas
        self.limiares_alerta = {
            "critico": 0.3,
            "alerta": 0.4,
            "atenção": 0.5
        }
    
    def atualizar_metrica(self, nome_metrica: str, valor: float, 
                          contexto: Optional[Dict] = None) -> Dict:
        """
        Atualiza uma métrica de saúde emocional
        
        Args:
            nome_metrica: Nome da métrica
            valor: Novo valor (0 a 1)
            contexto: Contexto da atualização (opcional)
        
        Returns:
            Dict: Resultado da atualização
        """
        if nome_metrica not in self.metricas_saude:
            return {"erro": f"Métrica '{nome_metrica}' não reconhecida"}
        
        valor_antigo = self.metricas_saude[nome_metrica]
        valor_normalizado = max(0.0, min(1.0, valor))
        
        self.metricas_saude[nome_metrica] = valor_normalizado
        
        # Registra no histórico
        registro = {
            "timestamp": time.time(),
            "metrica": nome_metrica,
            "valor_anterior": valor_antigo,
            "valor_novo": valor_normalizado,
            "variacao": valor_normalizado - valor_antigo,
            "contexto": contexto
        }
        
        self.historico_metricas.append(registro)
        
        # Verifica alertas
        alerta = self._verificar_alerta_metrica(nome_metrica, valor_normalizado, valor_antigo)
        
        # Atualiza tendência geral
        self._atualizar_tendencia()
        
        return {
            "metrica_atualizada": nome_metrica,
            "valor_anterior": valor_antigo,
            "valor_atual": valor_normalizado,
            "variacao": valor_normalizado - valor_antigo,
            "classificacao": self._classificar_valor_metrica(valor_normalizado),
            "alerta_gerado": alerta if alerta else None,
            "tendencia_atual": self.tendencia_saude
        }
    
    def _classificar_valor_metrica(self, valor: float) -> str:
        """Classifica valor da métrica"""
        if valor > 0.7:
            return "ótimo"
        elif valor > 0.5:
            return "bom"
        elif valor > 0.3:
            return "razoável"
        else:
            return "preocupante"
    
    def _verificar_alerta_metrica(self, metrica: str, valor_atual: float, 
                                 valor_anterior: float) -> Optional[Dict]:
        """Verifica necessidade de alerta para uma métrica"""
        alerta = None
        
        # Verifica limiares
        if valor_atual < self.limiares_alerta["critico"]:
            nivel = "critico"
            mensagem = f"Métrica '{metrica}' em nível crítico: {valor_atual:.2f}"
        elif valor_atual < self.limiares_alerta["alerta"]:
            nivel = "alerta"
            mensagem = f"Métrica '{metrica}' em nível de alerta: {valor_atual:.2f}"
        elif valor_atual < self.limiares_alerta["atenção"]:
            nivel = "atenção"
            mensagem = f"Métrica '{metrica}' requer atenção: {valor_atual:.2f}"
        else:
            return None
        
        # Verifica se houve queda significativa
        queda_significativa = valor_anterior - valor_atual > 0.2
        
        if queda_significativa:
            mensagem += f" (queda significativa de {valor_anterior:.2f})"
        
        alerta = {
            "id": f"alerta_{metrica}_{int(time.time())}",
            "timestamp": time.time(),
            "metrica": metrica,
            "valor": valor_atual,
            "nivel": nivel,
            "mensagem": mensagem,
            "queda_significativa": queda_significativa,
            "acao_recomendada": self._gerar_acao_recomendada(metrica, nivel)
        }
        
        self.alertas_saude.append(alerta)
        
        return alerta
    
    def _gerar_acao_recomendada(self, metrica: str, nivel: str) -> str:
        """Gera ação recomendada baseada na métrica e nível"""
        acoes = {
            "equilibrio_emocional": {
                "critico": "Buscar suporte profissional imediatamente",
                "alerta": "Praticar técnicas de regulação emocional",
                "atenção": "Monitorar emoções e praticar mindfulness"
            },
            "resiliencia": {
                "critico": "Focar em autocuidado básico e suporte social",
                "alerta": "Desenvolver estratégias de enfrentamento",
                "atenção": "Praticar resolução de problemas gradual"
            },
            "satisfacao_vida": {
                "critico": "Identificar fontes de insatisfação e buscar ajuda",
                "alerta": "Explorar atividades significativas",
                "atenção": "Praticar gratidão e reconhecer conquistas"
            }
        }
        
        acoes_metrica = acoes.get(metrica, {
            "critico": "Buscar avaliação profissional",
            "alerta": "Monitorar e praticar autocuidado",
            "atenção": "Observar padrões e fazer ajustes"
        })
        
        return acoes_metrica.get(nivel, "Monitorar e fazer ajustes conforme necessário")
    
    def _atualizar_tendencia(self):
        """Atualiza tendência geral da saúde emocional"""
        if len(self.historico_metricas) < 5:
            self.tendencia_saude = "dados_insuficientes"
            return
        
        # Analisa últimas métricas gerais
        metricas_recentes = self._obter_metricas_recentes(7)  # Últimos 7 dias
        
        if not metricas_recentes or len(metricas_recentes) < 2:
            self.tendencia_saude = "estavel"
            return
        
        # Calcula tendência
        valores_iniciais = [m["valor"] for m in metricas_recentes[:len(metricas_recentes)//2]]
        valores_finais = [m["valor"] for m in metricas_recentes[len(metricas_recentes)//2:]]
        
        media_inicial = sum(valores_iniciais) / len(valores_iniciais)
        media_final = sum(valores_finais) / len(valores_finais)
        
        if media_final > media_inicial * 1.1:
            self.tendencia_saude = "melhorando"
        elif media_final < media_inicial * 0.9:
            self.tendencia_saude = "piorando"
        else:
            self.tendencia_saude = "estavel"
    
    def _obter_metricas_recentes(self, dias: int) -> List[Dict]:
        """Obtém métricas recentes"""
        limite_tempo = time.time() - (dias * 24 * 3600)
        
        metricas_recentes = []
        for registro in self.historico_metricas:
            if registro["timestamp"] > limite_tempo:
                metricas_recentes.append({
                    "timestamp": registro["timestamp"],
                    "valor": registro["valor_novo"],
                    "metrica": registro["metrica"]
                })
        
        return metricas_recentes
    
    def executar_checkpoint_saude(self) -> Dict:
        """
        Executa checkpoint completo de saúde emocional
        
        Returns:
            Dict: Resultado do checkpoint
        """
        checkpoint_id = f"checkpoint_{int(time.time())}"
        
        # Coleta métricas atuais
        metricas_atuais = self.metricas_saude.copy()
        
        # Calcula índice geral de saúde
        indice_saude = sum(metricas_atuais.values()) / len(metricas_atuais)
        
        # Identifica áreas críticas
        areas_criticas = [
            {"metrica": m, "valor": v}
            for m, v in metricas_atuais.items()
            if v < self.limiares_alerta["atenção"]
        ]
        
        # Gera recomendações
        recomendacoes = self._gerar_recomendacoes_checkpoint(areas_criticas, indice_saude)
        
        checkpoint = {
            "id": checkpoint_id,
            "timestamp": time.time(),
            "indice_saude_geral": indice_saude,
            "classificacao_geral": self._classificar_indice_saude(indice_saude),
            "metricas_detalhadas": metricas_atuais,
            "areas_criticas": areas_criticas,
            "tendencia_atual": self.tendencia_saude,
            "recomendacoes": recomendacoes,
            "alertas_ativos": [a for a in self.alertas_saude 
                              if time.time() - a["timestamp"] < 7 * 24 * 3600]  # Últimos 7 dias
        }
        
        self.checkpoints.append(checkpoint)
        
        return checkpoint
    
    def _classificar_indice_saude(self, indice: float) -> str:
        """Classifica índice geral de saúde"""
        if indice > 0.7:
            return "excelente"
        elif indice > 0.6:
            return "bom"
        elif indice > 0.5:
            return "razoável"
        elif indice > 0.4:
            return "atenção"
        else:
            return "preocupante"
    
    def _gerar_recomendacoes_checkpoint(self, areas_criticas: List[Dict], 
                                        indice_saude: float) -> List[str]:
        """Gera recomendações baseadas no checkpoint"""
        recomendacoes = []
        
        # Recomendações baseadas no índice geral
        if indice_saude < 0.5:
            recomendacoes.append("Considerar avaliação profissional da saúde emocional")
        
        if indice_saude < 0.6:
            recomendacoes.append("Aumentar práticas de autocuidado e regulação emocional")
        
        # Recomendações específicas para áreas críticas
        for area in areas_criticas:
            recomendacoes.append(
                f"Focar em melhorar {area['metrica']} (atual: {area['valor']:.2f})"
            )
        
        # Recomendações baseadas na tendência
        if self.tendencia_saude == "piorando":
            recomendacoes.append("Identificar fatores contribuintes para tendência negativa")
        elif self.tendencia_saude == "melhorando":
            recomendacoes.append("Manter práticas que estão contribuindo para melhora")
        
        return recomendacoes[:5]  # Limita a 5 recomendações
    
    def monitorar_padrao(self, periodo_dias: int = 30) -> Dict:
        """
        Monitora padrões na saúde emocional
        
        Args:
            periodo_dias: Período para análise em dias
        
        Returns:
            Dict: Padrões identificados
        """
        # Coleta dados do período
        limite_tempo = time.time() - (periodo_dias * 24 * 3600)
        dados_periodo = [
            r for r in self.historico_metricas 
            if r["timestamp"] > limite_tempo
        ]
        
        if not dados_periodo:
            return {"mensagem": f"Nenhum dado disponível nos últimos {periodo_dias} dias"}
        
        # Agrupa por métrica
        metricas_agrupadas = {}
        for registro in dados_periodo:
            metrica = registro["metrica"]
            if metrica not in metricas_agrupadas:
                metricas_agrupadas[metrica] = []
            metricas_agrupadas[metrica].append(registro["valor_novo"])
        
        # Identifica padrões
        padroes = []
        for metrica, valores in metricas_agrupadas.items():
            if len(valores) >= 5:  # Mínimo para análise
                padrao = self._analisar_padrao_metrica(metrica, valores)
                if padrao:
                    padroes.append(padrao)
        
        # Identifica correlações entre métricas
        correlacoes = self._identificar_correlacoes(metricas_agrupadas)
        
        return {
            "periodo_analisado_dias": periodo_dias,
            "total_registros_analisados": len(dados_periodo),
            "padroes_identificados": padroes,
            "correlacoes_significativas": correlacoes,
            "metricas_mais_volateis": self._identificar_metricas_volateis(metricas_agrupadas),
            "insights": self._gerar_insights_padroes(padroes, correlacoes)
        }
    
    def _analisar_padrao_metrica(self, metrica: str, valores: List[float]) -> Optional[Dict]:
        """Analisa padrão em uma métrica específica"""
        if len(valores) < 5:
            return None
        
        # Calcula tendência
        media_inicial = sum(valores[:len(valores)//2]) / (len(valores)//2)
        media_final = sum(valores[len(valores)//2:]) / (len(valores)//2)
        
        # Calcula volatilidade
        media = sum(valores) / len(valores)
        variancia = sum((v - media) ** 2 for v in valores) / len(valores)
        desvio_padrao = variancia ** 0.5
        volatilidade = desvio_padrao / media if media > 0 else 0
        
        # Determina padrão
        if media_final > media_inicial * 1.15:
            tendencia = "forte_melhora"
        elif media_final > media_inicial * 1.05:
            tendencia = "melhora"
        elif media_final < media_inicial * 0.85:
            tendencia = "forte_piora"
        elif media_final < media_inicial * 0.95:
            tendencia = "piora"
        else:
            tendencia = "estavel"
        
        return {
            "metrica": metrica,
            "tendencia": tendencia,
            "media_geral": media,
            "volatilidade": volatilidade,
            "valor_minimo": min(valores),
            "valor_maximo": max(valores),
            "amostras_analisadas": len(valores)
        }
    
    def _identificar_correlacoes(self, metricas_agrupadas: Dict) -> List[Dict]:
        """Identifica correlações entre métricas"""
        if len(metricas_agrupadas) < 2:
            return []
        
        correlacoes = []
        metricas_list = list(metricas_agrupadas.items())
        
        for i in range(len(metricas_list)):
            for j in range(i + 1, len(metricas_list)):
                metrica1, valores1 = metricas_list[i]
                metrica2, valores2 = metricas_list[j]
                
                # Alinha tamanho das listas
                tamanho_min = min(len(valores1), len(valores2))
                if tamanho_min < 5:
                    continue
                
                valores1_alinhados = valores1[:tamanho_min]
                valores2_alinhados = valores2[:tamanho_min]
                
                # Calcula correlação simples
                correlacao = self._calcular_correlacao_simples(
                    valores1_alinhados, valores2_alinhados
                )
                
                if abs(correlacao) > 0.7:  # Correlação forte
                    correlacoes.append({
                        "metrica1": metrica1,
                        "metrica2": metrica2,
                        "correlacao": correlacao,
                        "tipo": "positiva" if correlacao > 0 else "negativa",
                        "forca": "forte" if abs(correlacao) > 0.8 else "moderada",
                        "amostras": tamanho_min
                    })
        
        return correlacoes
    
    def _calcular_correlacao_simples(self, valores1: List[float], valores2: List[float]) -> float:
        """Calcula correlação simples entre duas listas"""
        if len(valores1) != len(valores2) or len(valores1) < 2:
            return 0.0
        
        media1 = sum(valores1) / len(valores1)
        media2 = sum(valores2) / len(valores2)
        
        numerador = sum((valores1[i] - media1) * (valores2[i] - media2) 
                       for i in range(len(valores1)))
        
        var1 = sum((v - media1) ** 2 for v in valores1)
        var2 = sum((v - media2) ** 2 for v in valores2)
        
        if var1 == 0 or var2 == 0:
            return 0.0
        
        return numerador / ((var1 * var2) ** 0.5)
    
    def _identificar_metricas_volateis(self, metricas_agrupadas: Dict) -> List[Dict]:
        """Identifica métricas mais voláteis"""
        volatilidades = []
        
        for metrica, valores in metricas_agrupadas.items():
            if len(valores) >= 5:
                media = sum(valores) / len(valores)
                variancia = sum((v - media) ** 2 for v in valores) / len(valores)
                desvio_padrao = variancia ** 0.5
                volatilidade = desvio_padrao / media if media > 0 else 0
                
                volatilidades.append({
                    "metrica": metrica,
                    "volatilidade": volatilidade,
                    "desvio_padrao": desvio_padrao,
                    "amostras": len(valores)
                })
        
        # Ordena por volatilidade
        volatilidades.sort(key=lambda x: x["volatilidade"], reverse=True)
        return volatilidades[:3]  # Top 3 mais voláteis
    
    def _gerar_insights_padroes(self, padroes: List[Dict], correlacoes: List[Dict]) -> List[str]:
        """Gera insights baseados nos padrões identificados"""
        insights = []
        
        # Insights de tendência
        for padrao in padroes:
            if padrao["tendencia"] in ["forte_melhora", "melhora"]:
                insights.append(
                    f"{padrao['metrica']} mostra tendência positiva (média: {padrao['media_geral']:.2f})"
                )
            elif padrao["tendencia"] in ["forte_piora", "piora"]:
                insights.append(
                    f"{padrao['metrica']} requer atenção (tendência negativa detectada)"
                )
        
        # Insights de correlação
        for correlacao in correlacoes:
            if correlacao["tipo"] == "positiva":
                insights.append(
                    f"{correlacao['metrica1']} e {correlacao['metrica2']} tendem a variar juntas"
                )
            else:
                insights.append(
                    f"{correlacao['metrica1']} e {correlacao['metrica2']} têm relação inversa"
                )
        
        # Insights de volatilidade
        if padroes:
            padrao_mais_volatil = max(padroes, key=lambda x: x.get("volatilidade", 0))
            if padrao_mais_volatil.get("volatilidade", 0) > 0.3:
                insights.append(
                    f"{padrao_mais_volatil['metrica']} é a métrica mais volátil"
                )
        
        return insights[:5]  # Limita a 5 insights
    
    def configurar_alerta_personalizado(self, metrica: str, condicao: str, 
                                        valor_limite: float) -> Dict:
        """
        Configura alerta personalizado para uma métrica
        
        Args:
            metrica: Métrica a monitorar
            condicao: Condição (abaixo_de, acima_de)
            valor_limite: Valor limite para alerta
        
        Returns:
            Dict: Alerta configurado
        """
        if metrica not in self.metricas_saude:
            return {"erro": f"Métrica '{metrica}' não reconhecida"}
        
        alerta_id = f"alerta_personalizado_{int(time.time())}"
        
        alerta = {
            "id": alerta_id,
            "metrica": metrica,
            "condicao": condicao,
            "valor_limite": valor_limite,
            "timestamp_configuracao": time.time(),
            "ativo": True,
            "mensagem_personalizada": self._gerar_mensagem_alerta_personalizado(
                metrica, condicao, valor_limite
            )
        }
        
        # Adiciona aos alertas ativos
        self.alertas_saude.append(alerta)
        
        return {
            "alerta_configurado": alerta_id,
            "detalhes": alerta,
            "status": "ativo",
            "verificacao_proxima": self._calcular_proxima_verificacao()
        }
    
    def _gerar_mensagem_alerta_personalizado(self, metrica: str, condicao: str, 
                                            valor_limite: float) -> str:
        """Gera mensagem personalizada para alerta"""
        if condicao == "abaixo_de":
            return f"{metrica} caiu abaixo de {valor_limite:.2f}"
        else:  # acima_de
            return f"{metrica} subiu acima de {valor_limite:.2f}"
    
    def _calcular_proxima_verificacao(self) -> str:
        """Calcula quando será a próxima verificação de alertas"""
        # Próxima verificação em 1 hora
        proxima = time.time() + 3600
        return datetime.fromtimestamp(proxima).strftime("%Y-%m-%d %H:%M:%S")
    
    def obter_relatorio_saude(self, periodo_dias: int = 7) -> Dict:
        """Obtém relatório completo de saúde emocional"""
        # Executa checkpoint atual
        checkpoint_atual = self.executar_checkpoint_saude()
        
        # Analisa padrões recentes
        padroes_recentes = self.monitorar_padrao(periodo_dias)
        
        # Coleta alertas ativos
        alertas_ativos_recentes = [
            a for a in self.alertas_saude
            if a.get("ativo", False) and 
            time.time() - a.get("timestamp", 0) < periodo_dias * 24 * 3600
        ]
        
        return {
            "periodo_analisado_dias": periodo_dias,
            "checkpoint_atual": checkpoint_atual,
            "indice_saude_geral": checkpoint_atual["indice_saude_geral"],
            "tendencia_geral": self.tendencia_saude,
            "metricas_detalhadas": self.metricas_saude,
            "alertas_ativos": alertas_ativos_recentes,
            "padroes_recentes": padroes_recentes.get("padroes_identificados", []),
            "insights_principais": padroes_recentes.get("insights", []),
            "recomendacoes_prioritarias": self._gerar_recomendacoes_prioritarias(
                checkpoint_atual, padroes_recentes
            )
        }
    
    def _gerar_recomendacoes_prioritarias(self, checkpoint: Dict, 
                                          padroes: Dict) -> List[str]:
        """Gera recomendações prioritárias baseadas no relatório"""
        recomendacoes = []
        
        # Baseado no índice geral
        indice = checkpoint.get("indice_saude_geral", 0.5)
        if indice < 0.5:
            recomendacoes.append("Focar em práticas básicas de autocuidado e regulação emocional")
        
        # Baseado em áreas críticas
        areas_criticas = checkpoint.get("areas_criticas", [])
        if areas_criticas:
            for area in areas_criticas[:2]:  # Foca em até 2 áreas
                recomendacoes.append(
                    f"Desenvolver estratégias específicas para melhorar {area['metrica']}"
                )
        
        # Baseado na tendência
        if self.tendencia_saude == "piorando":
            recomendacoes.append("Identificar e abordar fatores causais da tendência negativa")
        
        # Baseado em padrões
        padroes_identificados = padroes.get("padroes_identificados", [])
        for padrao in padroes_identificados:
            if padrao.get("tendencia") in ["forte_piora", "piora"]:
                recomendacoes.append(
                    f"Intervir na tendência negativa de {padrao['metrica']}"
                )
        
        return recomendacoes[:3]  # Limita a 3 recomendações prioritárias