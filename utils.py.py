"""
Utilitários para o módulo emocional
"""

import time
from datetime import datetime
import json
from typing import Dict, List, Any, Optional

class LoggerEmocional:
    """Sistema de logging para eventos emocionais"""
    
    def __init__(self, nivel_log="INFO"):
        self.nivel_log = nivel_log
        self.logs = []
        self.niveis = {"DEBUG": 1, "INFO": 2, "WARN": 3, "ERROR": 4}
    
    def registrar(self, mensagem: str, nivel: str = "INFO", 
                  contexto: Optional[Dict] = None):
        """Registra um evento emocional"""
        if self.niveis.get(nivel, 2) >= self.niveis.get(self.nivel_log, 2):
            log_entry = {
                "timestamp": datetime.now().isoformat(),
                "nivel": nivel,
                "mensagem": mensagem,
                "contexto": contexto or {}
            }
            self.logs.append(log_entry)
            
            # Log para console (opcional)
            print(f"[{nivel}] {mensagem}")
            
            return log_entry
    
    def exportar_logs(self, limite: int = 50):
        """Exporta logs recentes"""
        return self.logs[-limite:] if self.logs else []

class MetricasEmocionais:
    """Calcula métricas emocionais"""
    
    @staticmethod
    def calcular_intensidade_media(estados: List[Dict]) -> float:
        """Calcula intensidade emocional média"""
        if not estados:
            return 0.5
        
        intensidades = [e.get("intensidade", 0.5) for e in estados]
        return sum(intensidades) / len(intensidades)
    
    @staticmethod
    def detectar_tendencia(historico: List[float], janela: int = 5) -> str:
        """Detecta tendência emocional"""
        if len(historico) < janela:
            return "estavel"
        
        recente = historico[-janela:]
        antigo = historico[-janela*2:-janela] if len(historico) >= janela*2 else recente
        
        media_recente = sum(recente) / len(recente)
        media_antiga = sum(antigo) / len(antigo)
        
        if media_recente > media_antiga * 1.1:
            return "ascendente"
        elif media_recente < media_antiga * 0.9:
            return "descendente"
        else:
            return "estavel"
    
    @staticmethod
    def normalizar_valor(valor: float, min_val: float = 0.0, 
                         max_val: float = 1.0) -> float:
        """Normaliza valor entre 0 e 1"""
        return max(min_val, min(max_val, valor))