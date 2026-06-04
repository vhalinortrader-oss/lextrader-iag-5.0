import streamlit as st
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import time
import json
import random
from typing import List, Dict, Any, Optional
from enum import Enum
import hashlib

# Configuração da página
st.set_page_config(
    page_title="Gestor de Credenciais",
    page_icon="🔐",
    layout="wide",
    initial_sidebar_state="collapsed"
)

# Enums e Classes de Tipos
class PlatformType(str, Enum):
    BINANCE = "BINANCE"
    CTRADER = "CTRADER"
    B3_DMA = "B3_DMA"
    BJF_TRADING_GROUP = "BJF_TRADING_GROUP"

class ConnectionStatus(str, Enum):
    CONNECTED = "CONNECTED"
    DISCONNECTED = "DISCONNECTED"
    CONNECTING = "CONNECTING"

class PlatformCredentials:
    def __init__(self, id: str, platform_name: PlatformType, api_key: str = "", 
                 api_secret: str = "", is_demo: bool = False, is_connected: bool = False):
        self.id = id
        self.platform_name = platform_name
        self.api_key = api_key
        self.api_secret = api_secret
        self.is_demo = is_demo
        self.is_connected = is_connected
        self.last_connection: Optional[datetime] = None

# Serviços simulados
class SentientCore:
    def __init__(self):
        self.thoughts = []
    
    def add_thought(self, thought: str):
        self.thoughts.append(f"{datetime.now().strftime('%H:%M:%S')} - {thought}")
        if len(self.thoughts) > 10:
            self.thoughts.pop(0)
    
    def perceive_reality(self, value: float, level: int):
        # Simulação de percepção da realidade
        pass

class ExchangeService:
    def __init__(self):
        self.credentials = {}
    
    def set_credentials(self, api_key: str, api_secret: str):
        self.credentials = {
            'api_key': api_key,
            'api_secret': api_secret,
            'timestamp': datetime.now()
        }
        return True

class CTraderService:
    def __init__(self):
        self.connected = False
    
    def connect(self, api_key: str) -> bool:
        # Simulação de conexão com cTrader
        time.sleep(1.5)
        self.connected = True
        return True
    
    def disconnect(self):
        self.connected = False
        return True

# Inicialização dos serviços
if 'sentient_core' not in st.session_state:
    st.session_state.sentient_core = SentientCore()

if 'exchange_service' not in st.session_state:
    st.session_state.exchange_service = ExchangeService()

if 'ctrader_service' not in st.session_state:
    st.session_state.ctrader_service = CTraderService()

if 'platform_credentials' not in st.session_state:
    # Credenciais iniciais
    st.session_state.platform_credentials = [
        PlatformCredentials('1', PlatformType.BINANCE, "", "", False, False),
        PlatformCredentials('2', PlatformType.CTRADER, "", "", True, False),
        PlatformCredentials('3', PlatformType.B3_DMA, "", "", True, False),
        PlatformCredentials('4', PlatformType.BJF_TRADING_GROUP, "", "", False, False)
    ]

if 'show_secrets' not in st.session_state:
    st.session_state.show_secrets = {}

if 'connecting_id' not in st.session_state:
    st.session_state.connecting_id = None

if 'active_tab' not in st.session_state:
    st.session_state.active_tab = PlatformType.BINANCE

# Funções auxiliares
def save_to_vault(credentials: List[PlatformCredentials]):
    """Salva credenciais no vault simulado"""
    vault_data = []
    for cred in credentials:
        vault_data.append({
            'platform_name': cred.platform_name.value,
            'api_key': cred.api_key,
            'api_secret': cred.api_secret,
            'is_demo': cred.is_demo
        })
    
    # Em uma aplicação real, isso seria criptografado
    st.session_state.vault_data = vault_data
    return True

def load_from_vault():
    """Carrega credenciais do vault simulado"""
    if 'vault_data' in st.session_state:
        vault_data = st.session_state.vault_data
        for cred_data in vault_data:
            for cred in st.session_state.platform_credentials:
                if cred.platform_name.value == cred_data['platform_name']:
                    cred.api_key = cred_data['api_key']
                    cred.api_secret = cred_data['api_secret']
                    cred.is_demo = cred_data['is_demo']
        return True
    return False

def update_credential_field(cred_id: str, field: str, value: Any):
    """Atualiza um campo específico de uma credencial"""
    for cred in st.session_state.platform_credentials:
        if cred.id == cred_id:
            setattr(cred, field, value)
            break
    save_to_vault(st.session_state.platform_credentials)
    return True

def toggle_secret(cred_id: str):
    """Alterna a visibilidade do segredo"""
    if cred_id in st.session_state.show_secrets:
        st.session_state.show_secrets[cred_id] = not st.session_state.show_secrets[cred_id]
    else:
        st.session_state.show_secrets[cred_id] = True

def handle_connect(cred: PlatformCredentials):
    """Manipula a conexão com a plataforma"""
    if not cred.api_key or (not cred.api_secret and cred.platform_name != PlatformType.CTRADER):
        st.error("Credenciais incompletas.")
        return False
    
    st.session_state.connecting_id = cred.id
    
    try:
        success = False
        
        # Lógica específica da plataforma
        if cred.platform_name == PlatformType.BINANCE:
            # Serviço de exchange
            success = st.session_state.exchange_service.set_credentials(cred.api_key, cred.api_secret)
            time.sleep(1.5)
        elif cred.platform_name == PlatformType.CTRADER:
            success = st.session_state.ctrader_service.connect(cred.api_key)
        elif cred.platform_name == PlatformType.BJF_TRADING_GROUP:
            # Simulação de conexão BJF
            time.sleep(2.5)
            success = True
            st.session_state.sentient_core.add_thought("Módulo BJF HFT Ativado. Latency Arbitrage Engines online.")
        else:
            # B3 / Genérico
            time.sleep(2.0)
            success = True
        
        if success:
            update_credential_field(cred.id, 'is_connected', True)
            update_credential_field(cred.id, 'last_connection', datetime.now())
            st.session_state.sentient_core.add_thought(
                f"Uplink estabelecido com {cred.platform_name.value}. Canais de dados abertos."
            )
            st.success(f"Conectado com sucesso a {cred.platform_name.value}!")
        else:
            raise Exception("Handshake falhou.")
            
    except Exception as e:
        st.error(f"Erro ao conectar com {cred.platform_name.value}: {str(e)}")
        st.session_state.sentient_core.add_thought(
            f"Falha de conexão com {cred.platform_name.value}. Verifique credenciais."
        )
        success = False
    finally:
        st.session_state.connecting_id = None
    
    return success

def handle_disconnect(cred: PlatformCredentials):
    """Manipula a desconexão da plataforma"""
    if cred.platform_name == PlatformType.CTRADER:
        st.session_state.ctrader_service.disconnect()
    
    update_credential_field(cred.id, 'is_connected', False)
    st.session_state.sentient_core.add_thought(
        f"Desconectado de {cred.platform_name.value}. Operações suspensas."
    )
    st.info(f"Desconectado de {cred.platform_name.value}")

# CSS Customizado
st.markdown("""
<style>
    .stApp {
        background-color: #0a0a0a;
        color: #d1d5db;
        font-family: 'Segoe UI', sans-serif;
    }
    
    .matrix-panel {
        background-color: #111827;
        border: 1px solid #374151;
        border-radius: 0.5rem;
        padding: 1rem;
    }
    
    .matrix-border {
        border-color: #374151;
    }
    
    .status-indicator {
        position: absolute;
        top: 0;
        left: 0;
        width: 0.25rem;
        height: 100%;
    }
    
    .status-connected {
        background-color: #10b981;
        box-shadow: 0 0 10px #22c55e;
    }
    
    .status-disconnected {
        background-color: #ef4444;
    }
    
    .platform-icon {
        padding: 0.75rem;
        border-radius: 0.25rem;
        border: 1px solid;
    }
    
    .binance-connected { background-color: rgba(16, 185, 129, 0.2); border-color: rgba(16, 185, 129, 0.5); color: #10b981; }
    .binance-disconnected { background-color: #1f2937; border-color: #374151; color: #6b7280; }
    
    .ctrader-connected { background-color: rgba(59, 130, 246, 0.2); border-color: rgba(59, 130, 246, 0.5); color: #3b82f6; }
    .ctrader-disconnected { background-color: #1f2937; border-color: #374151; color: #6b7280; }
    
    .b3-connected { background-color: rgba(245, 158, 11, 0.2); border-color: rgba(245, 158, 11, 0.5); color: #f59e0b; }
    .b3-disconnected { background-color: #1f2937; border-color: #374151; color: #6b7280; }
    
    .bjf-connected { background-color: rgba(168, 85, 247, 0.2); border-color: rgba(168, 85, 247, 0.5); color: #a855f7; }
    .bjf-disconnected { background-color: #1f2937; border-color: #374151; color: #6b7280; }
    
    .connect-button {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.5rem 1rem;
        border-radius: 0.25rem;
        border: 1px solid;
        font-size: 0.75rem;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.3s;
    }
    
    .connect-button:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }
    
    .connect-green {
        background-color: rgba(16, 185, 129, 0.2);
        border-color: #10b981;
        color: #10b981;
    }
    
    .connect-green:hover:not(:disabled) {
        background-color: rgba(16, 185, 129, 0.4);
    }
    
    .connect-red {
        background-color: rgba(239, 68, 68, 0.2);
        border-color: #ef4444;
        color: #ef4444;
    }
    
    .connect-red:hover:not(:disabled) {
        background-color: rgba(239, 68, 68, 0.4);
    }
    
    .security-alert {
        background-color: rgba(239, 68, 68, 0.1);
        border: 1px solid rgba(239, 68, 68, 0.3);
        border-radius: 0.5rem;
        padding: 1rem;
    }
    
    .input-field {
        width: 100%;
        background-color: #000;
        border: 1px solid #374151;
        border-radius: 0.25rem;
        padding: 0.5rem 0.75rem;
        color: white;
        font-family: monospace;
        font-size: 0.75rem;
    }
    
    .input-field:focus {
        outline: none;
        border-color: #0ea5e9;
    }
    
    .input-field:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }
    
    .toggle-switch {
        width: 2rem;
        height: 1rem;
        border-radius: 0.5rem;
        padding: 0.125rem;
        cursor: pointer;
        transition: background-color 0.3s;
    }
    
    .toggle-switch-demo {
        background-color: #d97706;
    }
    
    .toggle-switch-live {
        background-color: #10b981;
    }
    
    .toggle-knob {
        width: 0.75rem;
        height: 0.75rem;
        background-color: white;
        border-radius: 50%;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
        transition: transform 0.3s;
    }
    
    .latency-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0.75rem;
        background-color: rgba(0, 0, 0, 0.4);
        border: 1px solid #374151;
        border-radius: 0.25rem;
        margin-bottom: 0.5rem;
    }
</style>
""", unsafe_allow_html=True)

# Carregar do vault na inicialização
if 'vault_loaded' not in st.session_state:
    load_from_vault()
    st.session_state.vault_loaded = True

# Header
st.markdown("""
<div style="padding: 1rem; border-bottom: 1px solid #374151; background-color: #111827; display: flex; align-items: center; gap: 0.75rem; margin-bottom: 1rem;">
    <div style="padding: 0.5rem; background-color: rgba(100, 116, 139, 0.2); border: 1px solid rgba(100, 116, 139, 0.5); border-radius: 0.25rem;">
        <span style="color: #94a3b8; font-size: 1.25rem;">🔐</span>
    </div>
    <div>
        <h1 style="color: white; font-weight: bold; letter-spacing: 0.1em; margin: 0;">GESTOR DE CREDENCIAIS</h1>
        <div style="color: #94a3b8; font-size: 0.625rem; font-family: monospace; margin-top: 0.25rem;">
            SECURE VAULT • API AUTHENTICATION
        </div>
    </div>
</div>
""", unsafe_allow_html=True)

# Layout principal
col1, col2 = st.columns([2, 1])

with col1:
    st.markdown("#### 📊 Plataformas de Conexão")
    
    for cred in st.session_state.platform_credentials:
        # Determinar classes CSS baseadas no status
        status_class = "status-connected" if cred.is_connected else "status-disconnected"
        
        platform_icon_class = ""
        if cred.platform_name == PlatformType.BINANCE:
            platform_icon_class = "binance-connected" if cred.is_connected else "binance-disconnected"
        elif cred.platform_name == PlatformType.CTRADER:
            platform_icon_class = "ctrader-connected" if cred.is_connected else "ctrader-disconnected"
        elif cred.platform_name == PlatformType.B3_DMA:
            platform_icon_class = "b3-connected" if cred.is_connected else "b3-disconnected"
        else:  # BJF_TRADING_GROUP
            platform_icon_class = "bjf-connected" if cred.is_connected else "bjf-disconnected"
        
        # Determinar ícone
        platform_icon = "🌐" if cred.platform_name == PlatformType.BINANCE else "🖥️" if cred.platform_name == PlatformType.CTRADER else "🏢" if cred.platform_name == PlatformType.B3_DMA else "⚡"
        
        st.markdown(f"""
        <div style="background-color: #111827; border: 1px solid #374151; border-radius: 0.5rem; padding: 1.5rem; margin-bottom: 1rem; position: relative; overflow: hidden;">
            <div class="{status_class}"></div>
            
            <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 1rem;">
                <div style="display: flex; align-items: center; gap: 0.75rem;">
                    <div class="platform-icon {platform_icon_class}">
                        <span style="font-size: 1.5rem;">{platform_icon}</span>
                    </div>
                    <div>
                        <h3 style="font-weight: bold; color: white; margin: 0; font-size: 1.125rem;">
                            {cred.platform_name.value.replace('_', ' ')}
                        </h3>
                        <div style="font-size: 0.75rem; font-family: monospace; color: #9ca3af; display: flex; align-items: center; gap: 0.5rem; margin-top: 0.25rem;">
                            <span style="width: 0.5rem; height: 0.5rem; border-radius: 50%; background-color: {'#10b981' if cred.is_connected else '#ef4444'}; {'animation: pulse 1s infinite;' if cred.is_connected else ''}"></span>
                            {'CONECTADO' if cred.is_connected else 'DESCONECTADO'}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        """, unsafe_allow_html=True)
        
        # Campos de entrada
        col_input1, col_input2 = st.columns(2)
        
        with col_input1:
            label = "License Key / ID" if cred.platform_name == PlatformType.BJF_TRADING_GROUP else "API Key (Chave de Acesso)"
            api_key = st.text_input(
                label,
                value=cred.api_key,
                key=f"api_key_{cred.id}",
                disabled=cred.is_connected,
                placeholder="Insira sua chave pública...",
                help="Chave pública da API"
            )
            if api_key != cred.api_key:
                update_credential_field(cred.id, 'api_key', api_key)
        
        with col_input2:
            label = "Machine ID / Secret" if cred.platform_name == PlatformType.BJF_TRADING_GROUP else "API Secret (Chave Privada)"
            
            # Botão para mostrar/esconder senha
            show_password = st.session_state.show_secrets.get(cred.id, False)
            
            if show_password:
                api_secret = st.text_input(
                    label,
                    value=cred.api_secret,
                    key=f"api_secret_{cred.id}_show",
                    disabled=cred.is_connected,
                    placeholder="Insira sua chave secreta...",
                    help="Chave secreta da API"
                )
            else:
                api_secret = st.text_input(
                    label,
                    value=cred.api_secret,
                    type="password",
                    key=f"api_secret_{cred.id}_hide",
                    disabled=cred.is_connected,
                    placeholder="Insira sua chave secreta...",
                    help="Chave secreta da API"
                )
            
            if api_secret != cred.api_secret:
                update_credential_field(cred.id, 'api_secret', api_secret)
            
            # Botão para mostrar/esconder
            col_toggle1, col_toggle2 = st.columns([3, 1])
            with col_toggle2:
                eye_icon = "🙈" if show_password else "👁️"
                if st.button(eye_icon, key=f"toggle_{cred.id}", help="Mostrar/Esconder senha"):
                    toggle_secret(cred.id)
                    st.rerun()
        
        # Controle de ambiente Demo/Live
        col_env, col_status = st.columns(2)
        
        with col_env:
            st.markdown("**Ambiente:**")
            demo_status = st.radio(
                "",
                ["DEMO / SANDBOX", "LIVE / PRODUÇÃO"],
                index=0 if cred.is_demo else 1,
                key=f"env_{cred.id}",
                horizontal=True,
                disabled=cred.is_connected,
                label_visibility="collapsed"
            )
            if demo_status == "DEMO / SANDBOX":
                update_credential_field(cred.id, 'is_demo', True)
            else:
                update_credential_field(cred.id, 'is_demo', False)
        
        with col_status:
            if cred.last_connection:
                st.markdown(f"**Última conexão:** {cred.last_connection.strftime('%H:%M:%S')}")
        
        # Botões de conexão
        col_btn1, col_btn2 = st.columns(2)
        
        with col_btn1:
            if cred.is_connected:
                if st.button("🔌 Desconectar", key=f"disconnect_{cred.id}", use_container_width=True):
                    handle_disconnect(cred)
                    st.rerun()
            else:
                connecting = st.session_state.connecting_id == cred.id
                btn_text = "🔄 Conectando..." if connecting else "🔗 Conectar"
                if st.button(btn_text, key=f"connect_{cred.id}", use_container_width=True, disabled=connecting):
                    handle_connect(cred)
                    st.rerun()
        
        with col_btn2:
            if cred.is_connected:
                st.success("✅ Conectado")
            else:
                st.info("⏸️ Desconectado")
        
        st.divider()

with col2:
    st.markdown("#### ⚠️ Protocolo de Segurança")
    
    st.markdown("""
    <div class="security-alert">
        <h4 style="color: #ef4444; margin-bottom: 0.5rem;">PROTOCOLO DE SEGURANÇA MÁXIMA</h4>
        <p style="color: #fca5a5; font-size: 0.875rem; line-height: 1.5;">
            As chaves de API são armazenadas localmente no seu dispositivo utilizando criptografia de nível militar simulada. 
            A Inteligência Artificial Geral (IAG) tem acesso de leitura para execução de ordens, mas não pode visualizar ou exfiltrar chaves privadas.
            <br/><br/>
            <strong>IMPORTANTE:</strong> Nunca compartilhe suas chaves privadas. O sistema nunca solicitará credenciais fora deste terminal seguro.
        </p>
    </div>
    """, unsafe_allow_html=True)
    
    st.markdown("#### 📡 Status da Rede")
    
    # Simular latências
    latencies = {
        "Binance": random.randint(20, 40),
        "cTrader": random.randint(120, 180),
        "BJF HFT": random.randint(2, 8),
        "Uplink Status": "ENCRIPTADO (TLS 1.3)"
    }
    
    for platform, latency in latencies.items():
        col_latency1, col_latency2 = st.columns([2, 1])
        
        with col_latency1:
            st.markdown(f"**{platform}**")
        
        with col_latency2:
            if isinstance(latency, int):
                if platform == "Binance":
                    st.markdown(f'<span style="color: #10b981; font-family: monospace;">{latency}ms</span>', unsafe_allow_html=True)
                elif platform == "cTrader":
                    st.markdown(f'<span style="color: #f59e0b; font-family: monospace;">{latency}ms</span>', unsafe_allow_html=True)
                else:  # BJF HFT
                    st.markdown(f'<span style="color: #a855f7; font-family: monospace;">{latency}ms (Ultra-Low)</span>', unsafe_allow_html=True)
            else:
                st.markdown(f'<span style="color: #3b82f6; font-family: monospace;">{latency}</span>', unsafe_allow_html=True)
    
    st.markdown("---")
    
    # Log de pensamentos do sistema
    st.markdown("#### 💭 Log do Sistema")
    
    thoughts_container = st.container(height=200)
    with thoughts_container:
        for thought in reversed(st.session_state.sentient_core.thoughts[-5:]):
            st.markdown(f"""
            <div style="background-color: rgba(0, 0, 0, 0.2); padding: 0.5rem; border-radius: 0.25rem; margin-bottom: 0.5rem; border-left: 3px solid #0ea5e9;">
                <div style="color: #9ca3af; font-size: 0.75rem;">{thought}</div>
            </div>
            """, unsafe_allow_html=True)
    
    # Botões de ação
    col_actions1, col_actions2 = st.columns(2)
    
    with col_actions1:
        if st.button("💾 Salvar Vault", use_container_width=True):
            save_to_vault(st.session_state.platform_credentials)
            st.success("Vault salvo com sucesso!")
    
    with col_actions2:
        if st.button("🔄 Carregar Vault", use_container_width=True):
            load_from_vault()
            st.success("Vault carregado com sucesso!")
            st.rerun()

# Rodapé
st.markdown("---")
st.markdown("""
<div style="display: flex; justify-content: space-between; align-items: center; color: #9ca3af; font-size: 0.75rem;">
    <div>🔐 Sistema de Autenticação Segura • {datetime.now().strftime('%d/%m/%Y')}</div>
    <div>Plataformas conectadas: {connected_count}/{total_count}</div>
</div>
""".format(
    connected_count=sum(1 for cred in st.session_state.platform_credentials if cred.is_connected),
    total_count=len(st.session_state.platform_credentials)
), unsafe_allow_html=True)

# Animação CSS para pulsação
st.markdown("""
<style>
    @keyframes pulse {
        0% { opacity: 1; }
        50% { opacity: 0.5; }
        100% { opacity: 1; }
    }
</style>
""", unsafe_allow_html=True)