#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Security Manager - Gerencia segurança global do sistema"""

from typing import Dict, Any, List
from dataclasses import dataclass
from datetime import datetime
import logging

logger = logging.getLogger(__name__)

@dataclass
class SecurityPolicy:
    """Política de segurança"""
    name: str
    enabled: bool
    description: str
    rules: Dict[str, Any]

class SecurityManager:
    """Gerenciador central de segurança"""
    
    def __init__(self):
        self.policies: Dict[str, SecurityPolicy] = {}
        self.security_events: List[Dict] = []
        self.init_default_policies()
    
    def init_default_policies(self):
        """Inicializa políticas de segurança padrão"""
        self.policies = {
            "api_rate_limiting": SecurityPolicy(
                name="API Rate Limiting",
                enabled=True,
                description="Limita requisições por IP/usuário",
                rules={"max_requests_per_minute": 60, "max_requests_per_hour": 1000}
            ),
            "brute_force_protection": SecurityPolicy(
                name="Brute Force Protection",
                enabled=True,
                description="Protege contra tentativas de força bruta",
                rules={"max_failed_attempts": 5, "lockout_duration_minutes": 30}
            ),
            "encryption": SecurityPolicy(
                name="Data Encryption",
                enabled=True,
                description="Criptografa dados sensíveis",
                rules={"algorithm": "AES-256", "key_rotation_days": 90}
            ),
            "audit_logging": SecurityPolicy(
                name="Audit Logging",
                enabled=True,
                description="Registra todas as ações sensíveis",
                rules={"log_level": "INFO", "retention_days": 365}
            )
        }
    
    def validate_request(self, request_data: Dict) -> bool:
        """Valida requisição contra políticas de segurança"""
        
        # Verifica se há IP suspeito
        if not self.check_ip_whitelist(request_data.get("ip")):
            logger.warning(f"IP suspeito bloqueado: {request_data.get('ip')}")
            self.log_security_event("SUSPICIOUS_IP", request_data)
            return False
        
        # Verifica rate limiting
        if not self.check_rate_limit(request_data.get("user_id")):
            logger.warning(f"Rate limit excedido para usuário: {request_data.get('user_id')}")
            self.log_security_event("RATE_LIMIT_EXCEEDED", request_data)
            return False
        
        # Verifica autenticação
        if not request_data.get("authenticated"):
            logger.warning("Requisição não autenticada recebida")
            self.log_security_event("UNAUTHENTICATED_REQUEST", request_data)
            return False
        
        return True
    
    def check_ip_whitelist(self, ip: str) -> bool:
        """Verifica se IP está na whitelist"""
        # Implementação simplificada
        return ip and not ip.startswith("127.0.0.2")  # Bloqueia apenas 127.0.0.2
    
    def check_rate_limit(self, user_id: str) -> bool:
        """Verifica rate limiting do usuário"""
        # Implementação simplificada
        return True
    
    def log_security_event(self, event_type: str, data: Dict = None):
        """Registra evento de segurança"""
        event = {
            "timestamp": datetime.now().isoformat(),
            "type": event_type,
            "data": data or {},
            "severity": self.get_event_severity(event_type)
        }
        self.security_events.append(event)
        logger.info(f"Evento de segurança registrado: {event_type}")
    
    def get_event_severity(self, event_type: str) -> str:
        """Determina severidade do evento"""
        severity_map = {
            "SUSPICIOUS_IP": "HIGH",
            "RATE_LIMIT_EXCEEDED": "MEDIUM",
            "UNAUTHENTICATED_REQUEST": "HIGH",
            "API_KEY_COMPROMISED": "CRITICAL",
            "UNAUTHORIZED_ACCESS": "HIGH"
        }
        return severity_map.get(event_type, "LOW")
    
    def get_security_status(self) -> Dict[str, Any]:
        """Retorna status de segurança do sistema"""
        critical_events = len([e for e in self.security_events if e["severity"] == "CRITICAL"])
        high_events = len([e for e in self.security_events if e["severity"] == "HIGH"])
        
        status = "SECURE"
        if critical_events > 0:
            status = "COMPROMISED"
        elif high_events > 3:
            status = "AT_RISK"
        
        return {
            "overall_status": status,
            "total_events": len(self.security_events),
            "critical_events": critical_events,
            "high_events": high_events,
            "policies_enabled": sum(1 for p in self.policies.values() if p.enabled),
            "last_event": self.security_events[-1] if self.security_events else None
        }
    
    def enable_policy(self, policy_name: str) -> bool:
        """Ativa uma política de segurança"""
        if policy_name in self.policies:
            self.policies[policy_name].enabled = True
            self.log_security_event("POLICY_ENABLED", {"policy": policy_name})
            return True
        return False
    
    def disable_policy(self, policy_name: str) -> bool:
        """Desativa uma política de segurança"""
        if policy_name in self.policies:
            self.policies[policy_name].enabled = False
            self.log_security_event("POLICY_DISABLED", {"policy": policy_name})
            return True
        return False

if __name__ == "__main__":
    manager = SecurityManager()
    print("SecurityManager initialized")
    status = manager.get_security_status()
    print(f"Security Status: {status['overall_status']}")
