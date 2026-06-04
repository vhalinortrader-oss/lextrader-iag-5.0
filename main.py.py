"""
Execução Principal do Módulo Autônomo LEXTRADER-IAG 4.0
"""

import asyncio
import sys
import signal
from typing import Dict, Any
import json
import logging


def setup_logging():
    """Configura sistema de logging"""
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        handlers=[
            logging.StreamHandler(),
            logging.FileHandler('autonomous_module.log')
        ]
    )
    return logging.getLogger("Main")


async def main():
    """Função principal"""
    logger = setup_logging()
    
    try:
        from autonomous_module import AutonomousModule, AutonomousModuleFactory
        from autonomous_config import ConfigManager
        
        logger.info("Iniciando Módulo Autônomo LEXTRADER-IAG 4.0")
        logger.info("=" * 50)
        
        # Carregar configuração
        config_manager = ConfigManager()
        config = config_manager.load_config()
        
        logger.info(f"Configuração carregada: {config.module_name} v{config.version}")
        
        # Criar módulo autônomo
        module = AutonomousModuleFactory.create_module(config.__dict__)
        
        # Configurar handlers de sinal
        def signal_handler(signum, frame):
            logger.info(f"Sinal {signum} recebido, iniciando shutdown...")
            asyncio.create_task(module.shutdown())
        
        signal.signal(signal.SIGINT, signal_handler)
        signal.signal(signal.SIGTERM, signal_handler)
        
        # Inicializar módulo
        await module.initialize()
        
        logger.info("Módulo autônomo inicializado com sucesso")
        logger.info(f"Total de ações registradas: {len(module.actions)}")
        
        # Manter execução
        try:
            while module.is_running:
                await asyncio.sleep(1)
                
                # Log periódico de saúde
                health = module.get_health()
                if health.total_actions % 10 == 0:
                    logger.info(f"Status: {health.completed_actions} completadas, "
                              f"{health.failed_actions} falhas, "
                              f"Sucesso: {health.success_rate:.1%}")
        
        except KeyboardInterrupt:
            logger.info("Interrupção por teclado recebida")
        
        finally:
            await module.shutdown()
        
        logger.info("Módulo autônomo finalizado com sucesso")
        
    except Exception as e:
        logger.error(f"Erro fatal: {e}", exc_info=True)
        return 1
    
    return 0


if __name__ == "__main__":
    exit_code = asyncio.run(main())
    sys.exit(exit_code)