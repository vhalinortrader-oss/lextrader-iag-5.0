import streamlit as st
import plotly.graph_objects as go
import plotly.express as px
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import time
import random
from typing import List, Dict, Any, Optional
from enum import Enum
import threading

# Configuração da página
st.set_page_config(
    page_title="Omega Trading Terminal",
    page_icon="📡",
    layout="wide",
    initial_sidebar_state="collapsed"
)

# Enums e Classes de Tipos
class NewsItem:
    def __init__(self, id: str, title: str, summary: str, source: str, 
                 sentiment_score: float, timestamp: datetime):
        self.id = id
        self.title = title
        self.summary = summary
        self.source = source
        self.sentiment_score = sentiment_score
        self.timestamp = timestamp

class OmegaRiskMetrics:
    def __init__(self, sharpe_ratio: float, max_drawdown: float, 
                 win_rate: float, expectancy: float, current_exposure: float):
        self.sharpe_ratio = sharpe_ratio
        self.max_drawdown = max_drawdown
        self.win_rate = win_rate
        self.expectancy = expectancy
        self.current_exposure = current_exposure

class OmegaSystemStatus:
    def __init__(self, is_active: bool, neural_load: float):
        self.is_active = is_active
        self.neural_load = neural_load

# Serviço Omega
class OmegaService:
    def __init__(self):
        self.news_feed: List[NewsItem] = []
        self.metrics = OmegaRiskMetrics(2.5, 8.4, 0.73, 1.8, 0.65)
        self.status = OmegaSystemStatus(True, 78.5)
        self.logs: List[str] = []
        self.sentiment_history: List[Dict[str, Any]] = []
        self.running = False
        self.thread = None
        
        # Fontes de notícias
        self.sources = [
            "Reuters", "Bloomberg", "CNBC", "Financial Times", 
            "Wall Street Journal", "Cointelegraph", "TradingView",
            "MarketWatch", "Investing.com", "Benzinga"
        ]
        
        # Tópicos de notícias
        self.topics = [
            "Fed Interest Rate Decision", "Bitcoin ETF Approval", 
            "Earnings Report", "GDP Growth Data", "Inflation Report",
            "Oil Price Volatility", "Tech Stock Rally", "Bond Yield Curve",
            "Currency Market Intervention", "Cryptocurrency Regulation",
            "Market Correction", "IPO Launch", "Merger Announcement",
            "Economic Stimulus Package", "Trade War Developments"
        ]
    
    def start(self):
        """Inicia o serviço Omega"""
        self.running = True
        self.thread = threading.Thread(target=self._update_loop, daemon=True)
        self.thread.start()
        self.add_log("Omega Trading Terminal inicializado")
    
    def stop(self):
        """Para o serviço Omega"""
        self.running = False
        if self.thread:
            self.thread.join()
        self.add_log("Omega Trading Terminal desligado")
    
    def _update_loop(self):
        """Loop de atualização em background"""
        while self.running:
            try:
                # Gerar notícia aleatória
                self._generate_random_news()
                
                # Atualizar métricas
                self._update_metrics()
                
                # Atualizar status
                self._update_status()
                
                # Atualizar histórico de sentimento
                self._update_sentiment_history()
                
                time.sleep(1)  # Atualizar a cada segundo
                
            except Exception as e:
                self.add_log(f"Erro no loop de atualização: {e}")
    
    def _generate_random_news(self):
        """Gera uma notícia aleatória"""
        if random.random() < 0.3:  # 30% de chance de gerar nova notícia
            news_id = f"news_{datetime.now().timestamp()}_{random.randint(1000, 9999)}"
            
            # Gerar sentimento aleatório com tendência
            sentiment = random.random()
            
            # Ajustar sentimento baseado na hora do dia (simulação de mercado)
            hour = datetime.now().hour
            if 9 <= hour <= 16:  # Horário de mercado
                sentiment = max(0.3, min(0.8, sentiment + 0.1))
            else:
                sentiment = max(0.2, min(0.7, sentiment - 0.1))
            
            news_item = NewsItem(
                id=news_id,
                title=f"{random.choice(self.topics)} - {random.choice(['Breaking', 'Update', 'Analysis', 'Alert'])}",
                summary=f"Market reacts to {random.choice(['data', 'news', 'events'])}. " +
                       f"Traders watching {random.choice(['key levels', 'volume', 'momentum'])}.",
                source=random.choice(self.sources),
                sentiment_score=sentiment,
                timestamp=datetime.now()
            )
            
            self.news_feed.insert(0, news_item)
            
            # Manter apenas as últimas 50 notícias
            if len(self.news_feed) > 50:
                self.news_feed = self.news_feed[:50]
            
            # Adicionar log
            if sentiment > 0.7:
                self.add_log(f"News ALERT: High sentiment detected ({sentiment:.2f})")
            elif sentiment < 0.3:
                self.add_log(f"News WARNING: Low sentiment detected ({sentiment:.2f})")
    
    def _update_metrics(self):
        """Atualiza métricas de risco"""
        # Simular variação nas métricas
        volatility = random.random() * 0.05
        
        self.metrics.sharpe_ratio = max(1.0, min(4.0, self.metrics.sharpe_ratio + (random.random() - 0.5) * 0.1))
        self.metrics.max_drawdown = max(5.0, min(15.0, self.metrics.max_drawdown + (random.random() - 0.5) * 0.2))
        self.metrics.win_rate = max(0.65, min(0.85, self.metrics.win_rate + (random.random() - 0.5) * 0.02))
        self.metrics.expectancy = max(1.2, min(2.5, self.metrics.expectancy + (random.random() - 0.5) * 0.05))
        self.metrics.current_exposure = max(0.3, min(0.9, self.metrics.current_exposure + (random.random() - 0.5) * 0.05))
        
        # Adicionar log se houver mudança significativa
        if abs(volatility) > 0.03:
            self.add_log(f"Risk metrics updated: Sharpe={self.metrics.sharpe_ratio:.2f}, Exposure={self.metrics.current_exposure:.1%}")
    
    def _update_status(self):
        """Atualiza status do sistema"""
        # Simular variação na carga neural
        self.status.neural_load = max(50, min(95, self.status.neural_load + (random.random() - 0.5) * 5))
        
        # Log se carga for alta
        if self.status.neural_load > 85:
            self.add_log(f"WARNING: High neural load ({self.status.neural_load:.1f}%)")
    
    def _update_sentiment_history(self):
        """Atualiza histórico de sentimento"""
        if self.news_feed:
            # Calcular média de sentimento das últimas notícias
            recent_news = self.news_feed[:min(10, len(self.news_feed))]
            avg_sentiment = sum(n.sentiment_score for n in recent_news) / len(recent_news)
            
            self.sentiment_history.append({
                'time': datetime.now().strftime('%H:%M:%S'),
                'score': avg_sentiment
            })
            
            # Manter apenas os últimos 20 pontos
            if len(self.sentiment_history) > 20:
                self.sentiment_history = self.sentiment_history[-20:]
    
    def add_log(self, message: str):
        """Adiciona mensagem ao log"""
        timestamp = datetime.now().strftime('%H:%M:%S.%f')[:-3]
        log_entry = f"[{timestamp}] {message}"
        self.logs.append(log_entry)
        
        # Manter apenas os últimos 100 logs
        if len(self.logs) > 100:
            self.logs = self.logs[-100:]
    
    def get_metrics(self) -> OmegaRiskMetrics:
        """Retorna as métricas atuais"""
        return self.metrics

# Inicialização do serviço
if 'omega_service' not in st.session_state:
    st.session_state.omega_service = OmegaService()
    st.session_state.omega_service.start()

if 'last_update' not in st.session_state:
    st.session_state.last_update = datetime.now()

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
    
    .orange-glow {
        color: #f97316;
        text-shadow: 0 0 10px rgba(249, 115, 22, 0.5);
    }
    
    .news-card {
        background-color: rgba(0, 0, 0, 0.4);
        border: 1px solid #374151;
        border-radius: 0.5rem;
        padding: 0.75rem;
        margin-bottom: 0.75rem;
        transition: all 0.3s;
    }
    
    .news-card:hover {
        border-color: rgba(249, 115, 22, 0.3);
        transform: translateY(-1px);
    }
    
    .sentiment-badge {
        font-size: 0.625rem;
        padding: 0.125rem 0.5rem;
        border-radius: 0.25rem;
        font-family: monospace;
        border: 1px solid;
    }
    
    .sentiment-high {
        border-color: #16a34a;
        background-color: rgba(22, 163, 74, 0.2);
        color: #4ade80;
    }
    
    .sentiment-medium {
        border-color: #ca8a04;
        background-color: rgba(202, 138, 4, 0.2);
        color: #facc15;
    }
    
    .sentiment-low {
        border-color: #dc2626;
        background-color: rgba(220, 38, 38, 0.2);
        color: #f87171;
    }
    
    .metric-card {
        background-color: rgba(0, 0, 0, 0.4);
        border: 1px solid #374151;
        border-radius: 0.25rem;
        padding: 0.5rem;
    }
    
    .log-container {
        background-color: #000;
        border: 1px solid #374151;
        border-radius: 0.5rem;
        padding: 0.75rem;
        font-family: monospace;
        font-size: 0.625rem;
        color: #9ca3af;
        height: 300px;
        overflow-y: auto;
    }
    
    .log-entry {
        border-bottom: 1px solid #1f2937;
        padding-bottom: 0.25rem;
        margin-bottom: 0.25rem;
        word-break: break-all;
    }
    
    .log-entry:last-child {
        border-bottom: none;
    }
    
    .alert-box {
        background-color: rgba(220, 38, 38, 0.1);
        border: 1px solid rgba(220, 38, 38, 0.3);
        border-radius: 0.5rem;
        padding: 0.75rem;
    }
    
    .pulse {
        animation: pulse 1s infinite;
    }
    
    @keyframes pulse {
        0% { opacity: 1; }
        50% { opacity: 0.5; }
        100% { opacity: 1; }
    }
</style>
""", unsafe_allow_html=True)

# Header
col_header1, col_header2 = st.columns([3, 1])

with col_header1:
    st.markdown("""
    <div style="display: flex; align-items: center; gap: 1rem; margin-bottom: 1rem;">
        <div style="padding: 0.5rem; background-color: rgba(249, 115, 22, 0.2); border: 1px solid rgba(249, 115, 22, 0.5); border-radius: 0.5rem;">
            <span class="orange-glow pulse" style="font-size: 1.5rem;">📡</span>
        </div>
        <div>
            <h1 style="color: white; font-weight: bold; letter-spacing: 0.1em; margin: 0;">OMEGA TRADING TERMINAL</h1>
            <div style="color: #f97316; font-size: 0.75rem; font-family: monospace; margin-top: 0.25rem;">
                GLOBAL NEWS SENTIMENT & RISK ENGINE
            </div>
        </div>
    </div>
    """, unsafe_allow_html=True)

with col_header2:
    # Status indicators
    status = st.session_state.omega_service.status
    col_status1, col_status2 = st.columns(2)
    
    with col_status1:
        st.markdown(f"""
        <div style="background-color: rgba(0, 0, 0, 0.4); padding: 0.5rem 0.75rem; border-radius: 0.25rem; border: 1px solid #374151; display: flex; align-items: center; gap: 0.5rem;">
            <span style="color: #6b7280; font-size: 1.25rem;">💻</span>
            <span style="font-family: monospace; font-size: 0.75rem;">LOAD: {status.neural_load:.0f}%</span>
        </div>
        """, unsafe_allow_html=True)
    
    with col_status2:
        status_color = "#10b981" if status.is_active else "#ef4444"
        status_text = "ONLINE" if status.is_active else "OFFLINE"
        st.markdown(f"""
        <div style="background-color: rgba(0, 0, 0, 0.4); padding: 0.5rem 0.75rem; border-radius: 0.25rem; border: 1px solid #374151; display: flex; align-items: center; gap: 0.5rem;">
            <span style="color: {status_color}; font-size: 1.25rem;">{'🟢' if status.is_active else '🔴'}</span>
            <span style="font-family: monospace; font-size: 0.75rem;">{status_text}</span>
        </div>
        """, unsafe_allow_html=True)

# Layout principal
col_left, col_right = st.columns([2, 1])

with col_left:
    # Feed de Notícias
    st.markdown('<div class="matrix-panel">', unsafe_allow_html=True)
    st.markdown("#### 📰 Fluxo de Notícias (RSS/NLP)")
    
    news = st.session_state.omega_service.news_feed
    
    if news:
        news_container = st.container(height=400)
        with news_container:
            for item in news[:20]:  # Mostrar apenas as 20 mais recentes
                # Determinar cor do sentimento
                if item.sentiment_score > 0.6:
                    sentiment_class = "sentiment-high"
                elif item.sentiment_score < 0.4:
                    sentiment_class = "sentiment-low"
                else:
                    sentiment_class = "sentiment-medium"
                
                st.markdown(f"""
                <div class="news-card">
                    <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 0.25rem;">
                        <h4 style="font-weight: bold; color: white; margin: 0; font-size: 0.875rem;">
                            {item.title}
                        </h4>
                        <span class="{sentiment_class}">
                            {(item.sentiment_score * 100):.0f}%
                        </span>
                    </div>
                    <p style="color: #9ca3af; font-size: 0.75rem; margin-bottom: 0.5rem;">
                        {item.summary}
                    </p>
                    <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.625rem; color: #6b7280; font-family: monospace;">
                        <span>📢 {item.source}</span>
                        <span>{item.timestamp.strftime('%H:%M:%S')}</span>
                    </div>
                </div>
                """, unsafe_allow_html=True)
    else:
        st.info("Aguardando sinais da rede global...")
    
    st.markdown('</div>', unsafe_allow_html=True)
    
    # Gráfico de Sentimento
    st.markdown('<div class="matrix-panel" style="margin-top: 1rem;">', unsafe_allow_html=True)
    st.markdown("#### 📊 Tendência de Sentimento Global")
    
    sentiment_history = st.session_state.omega_service.sentiment_history
    
    if sentiment_history:
        df_sentiment = pd.DataFrame(sentiment_history)
        
        # Criar cores baseadas no score
        colors = []
        for score in df_sentiment['score']:
            if score > 0.5:
                colors.append('#4ade80')  # verde
            else:
                colors.append('#f87171')  # vermelho
        
        fig = go.Figure(data=[
            go.Bar(
                x=df_sentiment['time'],
                y=df_sentiment['score'],
                marker_color=colors,
                marker_line_width=0
            )
        ])
        
        fig.update_layout(
            height=250,
            margin=dict(l=0, r=0, t=30, b=0),
            paper_bgcolor='rgba(0,0,0,0)',
            plot_bgcolor='rgba(0,0,0,0)',
            xaxis=dict(
                showgrid=True,
                gridcolor='#222222',
                zeroline=False,
                showticklabels=False
            ),
            yaxis=dict(
                showgrid=True,
                gridcolor='#222222',
                zeroline=False,
                range=[0, 1]
            ),
            showlegend=False,
            hovermode='x unified'
        )
        
        st.plotly_chart(fig, use_container_width=True)
    else:
        st.info("Coletando dados de sentimento...")
    
    st.markdown('</div>', unsafe_allow_html=True)

with col_right:
    # Métricas de Risco
    st.markdown('<div class="matrix-panel">', unsafe_allow_html=True)
    st.markdown("#### 🛡️ Métricas de Risco Omega")
    
    metrics = st.session_state.omega_service.metrics
    
    col_metrics1, col_metrics2 = st.columns(2)
    
    with col_metrics1:
        st.markdown(f"""
        <div class="metric-card">
            <div style="color: #9ca3af; font-size: 0.625rem; text-transform: uppercase; margin-bottom: 0.25rem;">
                Sharpe Ratio
            </div>
            <div style="color: white; font-family: monospace; font-size: 1.25rem;">
                {metrics.sharpe_ratio:.2f}
            </div>
        </div>
        """, unsafe_allow_html=True)
        
        st.markdown(f"""
        <div class="metric-card" style="margin-top: 0.5rem;">
            <div style="color: #9ca3af; font-size: 0.625rem; text-transform: uppercase; margin-bottom: 0.25rem;">
                Max Drawdown
            </div>
            <div style="color: #ef4444; font-family: monospace; font-size: 1.25rem;">
                -{metrics.max_drawdown:.1f}%
            </div>
        </div>
        """, unsafe_allow_html=True)
    
    with col_metrics2:
        st.markdown(f"""
        <div class="metric-card">
            <div style="color: #9ca3af; font-size: 0.625rem; text-transform: uppercase; margin-bottom: 0.25rem;">
                Win Rate
            </div>
            <div style="color: #10b981; font-family: monospace; font-size: 1.25rem;">
                {(metrics.win_rate * 100):.0f}%
            </div>
        </div>
        """, unsafe_allow_html=True)
        
        st.markdown(f"""
        <div class="metric-card" style="margin-top: 0.5rem;">
            <div style="color: #9ca3af; font-size: 0.625rem; text-transform: uppercase; margin-bottom: 0.25rem;">
                Exp. Matemática
            </div>
            <div style="color: #3b82f6; font-family: monospace; font-size: 1.25rem;">
                {metrics.expectancy:.1f}
            </div>
        </div>
        """, unsafe_allow_html=True)
    
    # Exposição Atual
    st.markdown("---")
    st.markdown("**Exposição Atual**")
    
    exposure_pct = metrics.current_exposure * 100
    
    col_exposure1, col_exposure2 = st.columns([3, 1])
    
    with col_exposure1:
        st.markdown(f"""
        <div style="width: 100%; background-color: #1f2937; height: 0.5rem; border-radius: 0.25rem; overflow: hidden; margin-top: 0.25rem;">
            <div style="height: 100%; background-color: #f97316; width: {exposure_pct}%; transition: width 1s;"></div>
        </div>
        """, unsafe_allow_html=True)
    
    with col_exposure2:
        st.markdown(f'<span style="color: #f97316; font-weight: bold; font-size: 0.875rem;">{exposure_pct:.0f}%</span>', unsafe_allow_html=True)
    
    st.markdown('</div>', unsafe_allow_html=True)
    
    # Logs do Sistema
    st.markdown('<div class="matrix-panel" style="margin-top: 1rem;">', unsafe_allow_html=True)
    st.markdown("#### 💻 Omega System Logs")
    
    logs = st.session_state.omega_service.logs
    
    log_container = st.container(height=300)
    with log_container:
        for log in logs[-50:]:  # Mostrar apenas os últimos 50 logs
            st.markdown(f"""
            <div class="log-entry">
                {log}
            </div>
            """, unsafe_allow_html=True)
    
    st.markdown('</div>', unsafe_allow_html=True)
    
    # Alertas
    st.markdown('<div class="alert-box" style="margin-top: 1rem;">', unsafe_allow_html=True)
    st.markdown("""
    <div style="display: flex; align-items: start; gap: 0.75rem;">
        <span class="pulse" style="color: #ef4444; font-size: 1.25rem;">🚨</span>
        <div>
            <div style="font-weight: bold; color: #ef4444; font-size: 0.875rem;">
                MONITORAMENTO DE WEBHOOK ATIVO
            </div>
            <p style="color: #fca5a5; font-size: 0.75rem; margin-top: 0.25rem;">
                Sistema pronto para disparar alertas críticos de volatilidade para terminais externos.
            </p>
        </div>
    </div>
    """, unsafe_allow_html=True)
    st.markdown('</div>', unsafe_allow_html=True)

# Controles na barra lateral
with st.sidebar:
    st.markdown("### ⚙️ Controles Omega")
    
    if st.button("🔄 Atualizar Agora"):
        st.session_state.last_update = datetime.now()
        st.rerun()
    
    if st.button("📊 Gerar Relatório"):
        # Simular geração de relatório
        st.session_state.omega_service.add_log("Relatório gerado: análise_completa_mercado.pdf")
        st.success("Relatório gerado com sucesso!")
    
    st.markdown("---")
    st.markdown("### 📈 Estatísticas")
    
    st.metric("Notícias Processadas", len(st.session_state.omega_service.news_feed))
    st.metric("Logs do Sistema", len(st.session_state.omega_service.logs))
    
    # Barra de progresso do sentimento médio
    if st.session_state.omega_service.news_feed:
        avg_sentiment = sum(n.sentiment_score for n in st.session_state.omega_service.news_feed) / len(st.session_state.omega_service.news_feed)
        st.markdown("**Sentimento Médio**")
        st.progress(avg_sentiment)
        st.markdown(f"{avg_sentiment:.1%}")
    
    st.markdown(f"*Última atualização:* {st.session_state.last_update.strftime('%H:%M:%S')}")

# Rodapé
st.markdown("---")
st.markdown("""
<div style="display: flex; justify-content: space-between; align-items: center; color: #9ca3af; font-size: 0.75rem;">
    <div>📡 Omega Trading Terminal • GLOBAL NEWS SENTIMENT & RISK ENGINE</div>
    <div>{datetime.now().strftime('%H:%M:%S')}</div>
</div>
""", unsafe_allow_html=True)

# Atualização automática (opcional)
auto_refresh = st.sidebar.checkbox("Atualização Automática", value=True)

if auto_refresh:
    time.sleep(1)
    st.session_state.last_update = datetime.now()
    st.rerun()