from setuptools import setup, find_packages

setup(
    name="modulo_motor",
    version="1.0.0",
    packages=find_packages(),
    description="Sistema de controle motor com coordenação, equilíbrio e reflexos econômicos",
    author="Seu Nome",
    python_requires=">=3.8",
    install_requires=[
        # Dependências se houver
    ],
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
    ],
)
# Exemplo de uso
from modulo_motor import ProcessadorMotor

# Inicializa o processador
processador = ProcessadorMotor()

# Dados de entrada simulados
dados_entrada = {
    "noticias": [
        {
            "titulo": "BC anuncia aumento da taxa básica de juros",
            "categoria": "politica_monetaria",
            "conteudo": "Banco Central elevou a Selic em 0,5 pontos percentuais",
            "urgente": True
        }
    ],
    "dados_sensoriais": {
        "aceleracao": 0.1,
        "inclinacao": 0.15,
        "rotacao": 0.2
    },
    "objetivos": ["estabilizar_carteira_investimentos", "revisar_projecoes"],
    "contexto_atual": {
        "mercado": "volatil",
        "juros": "alta",
        "inflacao": "controlada"
    },
    "estado_desejado": {
        "estabilidade": 0.9,
        "liquidez": 0.7,
        "retorno": 0.6
    },
    "sequencias": [
        ["analise", "decisao", "execucao", "avaliacao"],
        ["coleta", "processamento", "decisao"]
    ]
}

# Executa um ciclo de processamento
resultado = processador.executar_ciclo(dados_entrada)
print(f"Ciclo executado: {resultado['ciclo_id']}")

# Obtém relatório
relatorio = processador.obter_relatorio()
print(f"Estado do sistema: {relatorio['estado_geral']}")