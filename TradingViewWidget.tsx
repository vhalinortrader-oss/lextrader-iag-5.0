
import React, { useEffect, useRef, memo } from 'react';

interface TradingViewWidgetProps {
  symbol: string;
  theme?: 'light' | 'dark';
  autosize?: boolean;
  type?: 'chart' | 'technical-analysis';
}

const TradingViewWidget: React.FC<TradingViewWidgetProps> = ({ 
  symbol = "BINANCE:BTCUSDT", 
  theme = "dark", 
  autosize = true,
  type = 'chart'
}) => {
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!containerRef.current) return;

    // Limpar container antes de injetar
    containerRef.current.innerHTML = '';

    const script = document.createElement('script');
    script.src = type === 'chart' 
      ? 'https://s3.tradingview.com/tv.js' 
      : 'https://s3.tradingview.com/external-embedding/embed-widget-technical-analysis.js';
    script.async = true;

    if (type === 'chart') {
      script.onload = () => {
        if ((window as any).TradingView) {
          new (window as any).TradingView.widget({
            width: "100%",
            height: "100%",
            symbol: symbol,
            interval: "D",
            timezone: "Etc/UTC",
            theme: theme,
            style: "1",
            locale: "pt",
            toolbar_bg: "#f1f3f6",
            enable_publishing: false,
            hide_side_toolbar: false,
            allow_symbol_change: true,
            container_id: containerRef.current?.id,
            backgroundColor: "#020617",
            gridColor: "rgba(30, 41, 59, 0.1)",
          });
        }
      };
      containerRef.current.appendChild(script);
    } else {
      script.innerHTML = JSON.stringify({
        interval: "1h",
        width: "100%",
        isTransparent: true,
        height: "100%",
        symbol: symbol,
        showIntervalTabs: true,
        displayMode: "single",
        locale: "pt",
        colorTheme: theme
      });
      containerRef.current.appendChild(script);
    }

    return () => {
      if (containerRef.current) containerRef.current.innerHTML = '';
    };
  }, [symbol, theme, type]);

  return (
    <div 
      id={`tv-widget-${type}-${symbol.replace(':', '-')}`} 
      ref={containerRef} 
      className="w-full h-full"
    />
  );
};

export default memo(TradingViewWidget);
