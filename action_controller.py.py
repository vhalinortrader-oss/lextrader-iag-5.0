"""
Controlador de Ações Autônomas
Gerencia execução, priorização e controle de ações
"""

import asyncio
from typing import Dict, List, Optional, Set, Any
from datetime import datetime, timezone
from dataclasses import dataclass, field
import heapq
from enum import Enum


class ControlMode(Enum):
    """Modos de controle"""
    AUTONOMOUS = "autonomous"      # Totalmente autônomo
    SUPERVISED = "supervised"      # Com supervisão humana
    MANUAL = "manual"              # Controle manual
    SAFE = "safe"                  # Modo de segurança


@dataclass
class ExecutionControl:
    """Controle de execução de ações"""
    action_id: str
    max_execution_time: int
    cpu_limit: float = 80.0
    memory_limit: float = 1024  # MB
    network_limit: float = 100  # MB/s
    can_be_paused: bool = True
    can_be_cancelled: bool = True
    requires_approval: bool = False
    approval_timeout: int = 300  # segundos


@dataclass
class ResourceMonitor:
    """Monitor de recursos"""
    action_id: str
    cpu_usage: float = 0.0
    memory_usage: float = 0.0
    network_usage: float = 0.0
    execution_time: float = 0.0
    timestamp: datetime = field(default_factory=lambda: datetime.now(timezone.utc))
    
    @property
    def is_over_limit(self, limits: ExecutionControl) -> bool:
        """Verifica se excedeu os limites"""
        return (
            self.cpu_usage > limits.cpu_limit or
            self.memory_usage > limits.memory_limit or
            self.network_usage > limits.network_limit or
            self.execution_time > limits.max_execution_time
        )


class ActionController:
    """Controlador de ações"""
    
    def __init__(self, autonomous_module):
        self.module = autonomous_module
        self.control_mode = ControlMode.AUTONOMOUS
        self.execution_controls: Dict[str, ExecutionControl] = {}
        self.resource_monitors: Dict[str, ResourceMonitor] = {}
        self.approval_queue: List[str] = []  # Ações aguardando aprovação
        self.paused_actions: Set[str] = set()
        
        # Thresholds de segurança
        self.safety_thresholds = {
            "max_total_cpu": 90.0,
            "max_total_memory": 4096,  # 4GB
            "max_concurrent_critical": 3,
            "max_failure_rate": 0.3,  # 30%
        }
    
    def set_control_mode(self, mode: ControlMode):
        """Define modo de controle"""
        self.control_mode = mode
        
        if mode == ControlMode.SAFE:
            # Em modo seguro, pausa todas as ações críticas
            self._enter_safe_mode()
    
    def _enter_safe_mode(self):
        """Entra em modo de segurança"""
        # Pausar ações críticas
        for action_id, action in self.module.actions.items():
            if action.priority == "CRITICAL" and action.is_running:
                self.module.pause_action(action_id)
        
        # Limitar recursos
        self.safety_thresholds["max_total_cpu"] = 50.0
        self.safety_thresholds["max_total_memory"] = 2048  # 2GB
    
    def register_control(self, action_id: str, control: ExecutionControl):
        """Registra controle para uma ação"""
        self.execution_controls[action_id] = control
        
        if control.requires_approval:
            self.approval_queue.append(action_id)
    
    async def request_approval(self, action_id: str) -> bool:
        """Solicita aprovação para execução"""
        if action_id not in self.execution_controls:
            return True
        
        control = self.execution_controls[action_id]
        if not control.requires_approval:
            return True
        
        # Adicionar à fila de aprovação
        if action_id not in self.approval_queue:
            self.approval_queue.append(action_id)
        
        # Aguardar aprovação ou timeout
        try:
            approved = await self._wait_for_approval(action_id, control.approval_timeout)
            return approved
        except asyncio.TimeoutError:
            return False
    
    async def _wait_for_approval(self, action_id: str, timeout: int) -> bool:
        """Aguarda aprovação"""
        # Em uma implementação real, isso se integraria com uma interface de aprovação
        # Por enquanto, simula aprovação automática após 2 segundos
        await asyncio.sleep(2)
        return True
    
    def approve_action(self, action_id: str) -> bool:
        """Aprova uma ação manualmente"""
        if action_id in self.approval_queue:
            self.approval_queue.remove(action_id)
            return True
        return False
    
    def reject_action(self, action_id: str) -> bool:
        """Rejeita uma ação"""
        if action_id in self.approval_queue:
            self.approval_queue.remove(action_id)
            
            # Marcar ação como rejeitada
            if action_id in self.module.actions:
                action = self.module.actions[action_id]
                action.status = "REJECTED"
                action.updated_at = datetime.now(timezone.utc)
            
            return True
        return False
    
    async def monitor_resources(self):
        """Monitora uso de recursos das ações"""
        while True:
            # Coletar métricas de recursos
            # Em uma implementação real, isso usaria psutil ou similar
            await asyncio.sleep(5)
            
            # Verificar limites
            for action_id, monitor in list(self.resource_monitors.items()):
                if action_id in self.execution_controls:
                    control = self.execution_controls[action_id]
                    if monitor.is_over_limit(control):
                        await self._handle_resource_exceeded(action_id, monitor)
    
    async def _handle_resource_exceeded(self, action_id: str, monitor: ResourceMonitor):
        """Lida com excedente de recursos"""
        action = self.module.actions.get(action_id)
        if action and action.is_running:
            self.module.logger.warning(
                f"Ação {action_id} excedeu limites de recursos: "
                f"CPU={monitor.cpu_usage}%, Mem={monitor.memory_usage}MB"
            )
            
            # Pausar ação se permitido
            control = self.execution_controls.get(action_id)
            if control and control.can_be_paused:
                await self.module.pause_action(action_id)
    
    def get_pending_approvals(self) -> List[Dict[str, Any]]:
        """Obtém lista de ações pendentes de aprovação"""
        approvals = []
        for action_id in self.approval_queue:
            if action_id in self.module.actions:
                action = self.module.actions[action_id]
                approvals.append({
                    "id": action_id,
                    "name": action.name,
                    "type": action.type.name,
                    "priority": action.priority.name,
                    "description": action.description
                })
        return approvals
    
    def get_system_health(self) -> Dict[str, Any]:
        """Obtém saúde do sistema de controle"""
        total_actions = len(self.module.actions)
        controlled_actions = len(self.execution_controls)
        pending_approvals = len(self.approval_queue)
        paused_actions = len(self.paused_actions)
        
        # Calcular uso de recursos
        total_cpu = sum(m.cpu_usage for m in self.resource_monitors.values())
        total_memory = sum(m.memory_usage for m in self.resource_monitors.values())
        
        return {
            "control_mode": self.control_mode.value,
            "total_actions": total_actions,
            "controlled_actions": controlled_actions,
            "pending_approvals": pending_approvals,
            "paused_actions": paused_actions,
            "resource_usage": {
                "total_cpu": total_cpu,
                "total_memory": total_memory,
                "thresholds": self.safety_thresholds
            }
        }