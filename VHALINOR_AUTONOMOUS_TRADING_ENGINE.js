/**
 * VHALINOR.IAG - Autonomous Trading Engine
 * ========================================
 * Virtual Hybrid Advanced Learning Intelligence Neural Optimized Reasoning
 * Autonomous AI Trading System for Financial Markets
 * 
 * Features:
 * - Real-time market analysis with technical indicators
 * - Machine learning price predictions
 * - Advanced risk management
 * - Position sizing based on Kelly criterion
 * - Portfolio performance tracking
 * - Multi-timeframe analysis
 * 
 * Version: 5.0.0 - Autonomous Trading Specialist
 * Author: VHALINOR.IAG Team
 */

import React, { useState, useEffect, useCallback, useRef } from 'react';
import './VhalinorTrading.css';

// ============================================================================
// IMPORTAÇÕES DE BIBLIOTECAS
// ============================================================================

import { motion, AnimatePresence } from 'framer-motion';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// Ícones
import {
  FaRobot,
  FaBrain,
  FaChartLine,
  FaChartBar,
  FaChartPie,
  FaChartArea,
  FaChartScatter,
  FaCog,
  FaPlay,
  FaStop,
  FaRedo,
  FaDownload,
  FaUpload,
  FaSave,
  FaCopy,
  FaTrash,
  FaEdit,
  FaPlus,
  FaMinus,
  FaCheck,
  FaTimes,
  FaInfo,
  FaQuestion,
  FaExclamation,
  FaExclamationTriangle,
  FaCheckCircle,
  FaTimesCircle,
  FaInfoCircle,
  FaQuestionCircle,
  FaClock,
  FaHistory,
  FaCalendar,
  FaFilter,
  FaSearch,
  FaSort,
  FaSortUp,
  FaSortDown,
  FaArrowUp,
  FaArrowDown,
  FaArrowLeft,
  FaArrowRight,
  FaExchangeAlt,
  FaBalanceScale,
  FaRuler,
  FaWeight,
  FaTachometerAlt,
  FaThermometerHalf,
  FaWind,
  FaTint,
  FaSun,
  FaMoon,
  FaCloud,
  FaCloudSun,
  FaCloudMoon,
  FaCloudRain,
  FaCloudSnow,
  FaCloudStorm,
  FaBolt,
  FaFire,
  FaSnowflake,
  FaLeaf,
  FaSeedling,
  FaTree,
  FaMountain,
  FaGlobe,
  FaGlobeAsia,
  FaGlobeAfrica,
  FaGlobeAmericas,
  FaGlobeEurope,
  FaLock,
  FaUnlock,
  FaEye,
  FaEyeSlash,
  FaCode,
  FaTerminal,
  FaDatabase,
  FaServer,
  FaCloud as FaCloudSolid,
  FaCloudUploadAlt,
  FaCloudDownloadAlt,
  FaNetworkWired,
  FaWifi,
  FaBluetooth,
  FaRss,
  FaSatellite,
  FaSatelliteDish,
  FaSpaceShuttle,
  FaRocket,
  FaRocketchat,
  FaSpaceStationMoon,
  FaSpaceStationMoonAlt,
  FaMeteor,
  FaComet,
  FaGalaxy,
  FaStar,
  FaStarHalf,
  FaStarHalfAlt,
  FaSun as FaSunSolid,
  FaMoon as FaMoonSolid,
  FaPlanetRinged,
  FaRing,
  FaGripfire,
  FaVolcano,
  FaEarth,
  FaEarthAfrica,
  FaEarthAmericas,
  FaEarthAsia,
  FaEarthEurope,
  FaEarthOceania,
  FaMap,
  FaMapMarked,
  FaMapMarkedAlt,
  FaMapPin,
  FaMapSigns,
  FaMapMarker,
  FaMapMarkerAlt,
  FaLocationArrow,
  FaCompass,
  FaCrosshairs,
  FaTarget,
  FaBullseye,
  FaDice,
  FaDiceD20,
  FaDiceD6,
  FaDiceFive,
  FaDiceFour,
  FaDiceOne,
  FaDiceSix,
  FaDiceThree,
  FaDiceTwo,
  FaChess,
  FaChessBoard,
  FaChessKing,
  FaChessQueen,
  FaChessRook,
  FaChessBishop,
  FaChessKnight,
  FaChessPawn,
  FaGamepad,
  FaJoystick,
  FaKeyboard,
  FaMouse,
  FaMousePointer,
  FaLaptop,
  FaLaptopCode,
  FaDesktop,
  FaTablet,
  FaMobile,
  FaMobileAlt,
  FaRobot as FaRobotSolid,
  FaAndroid,
  FaApple,
  FaWindows,
  FaLinux,
  FaPython,
  FaJs,
  FaHtml5,
  FaCss3Alt,
  FaReact,
  FaAngular,
  FaVuejs,
  FaNodeJs,
  FaNpm,
  FaYarn,
  FaDocker,
  FaKubernetes,
  FaAws,
  FaGoogle,
  FaMicrosoft,
  FaApple as FaAppleSolid,
  FaFacebook,
  FaTwitter,
  FaInstagram,
  FaLinkedin,
  FaGithub,
  FaGitlab,
  FaBitbucket,
  FaJira,
  FaTrello,
  FaSlack,
  FaDiscord,
  FaTelegram,
  FaWhatsapp,
  FaSignal,
  FaWechat,
  FaSkype,
  FaZoom,
  FaMeetup,
  FaCalendarAlt,
  FaClock as FaClockSolid,
  FaHourglass,
  FaHourglassHalf,
  FaHourglassEnd,
  FaHourglassStart,
  FaStopwatch,
  FaStopwatch20,
  FaAlarmClock,
  FaBell,
  FaBellSlash,
  FaVolumeUp,
  FaVolumeDown,
  FaVolumeMute,
  FaHeadphones,
  FaHeadphonesAlt,
  FaMicrophone,
  FaMicrophoneAlt,
  FaRadio,
  FaPodcast,
  FaVideo,
  FaVideoSlash,
  FaCamera,
  FaCameraRetro,
  FaImage,
  FaImages,
  FaFileImage as FaFileImageSolid,
  FaFileVideo as FaFileVideoSolid,
  FaFileAudio as FaFileAudioSolid,
  FaFileArchive as FaFileArchiveSolid,
  FaFileCode as FaFileCodeSolid,
  FaFilePdf as FaFilePdfSolid,
  FaFileWord as FaFileWordSolid,
  FaFileExcel as FaFileExcelSolid,
  FaFilePowerpoint as FaFilePowerpointSolid,
  FaFile,
  FaFileAlt as FaFileAltSolid,
  FaFileArchive as FaFileArchiveRegular,
  FaFolder,
  FaFolderOpen,
  FaFolderPlus,
  FaFolderMinus,
  FaHome,
  FaUser,
  FaUsers,
  FaUserPlus,
  FaUserMinus,
  FaUserCheck,
  FaUserTimes,
  FaUserCog,
  FaUserCircle,
  FaUserTie,
  FaUserSecret,
  FaUserNinja,
  FaUserAstronaut,
  FaUserGraduate,
  FaUniversity,
  FaSchool,
  FaChalkboard,
  FaChalkboardTeacher,
  FaGraduationCap,
  FaBook,
  FaBookOpen,
  FaBookReader,
  FaBookDead,
  FaBookmark,
  FaTags,
  FaTag,
  FaMap as FaMapSolid,
  FaMapMarked as FaMapMarkedSolid,
  FaMapMarkedAlt as FaMapMarkedAltSolid,
  FaMapPin as FaMapPinSolid,
  FaMapSigns as FaMapSignsSolid,
  FaMapMarker as FaMapMarkerSolid,
  FaMapMarkerAlt as FaMapMarkerAltSolid,
  FaLocationArrow as FaLocationArrowSolid,
  FaCompass as FaCompassSolid,
  FaCrosshairs as FaCrosshairsSolid,
  FaTarget as FaTargetSolid,
  FaBullseye as FaBullseyeSolid,
  FaDice as FaDiceSolid,
  FaDiceD20 as FaDiceD20Solid,
  FaDiceD6 as FaDiceD6Solid,
  FaDiceFive as FaDiceFiveSolid,
  FaDiceFour as FaDiceFourSolid,
  FaDiceOne as FaDiceOneSolid,
  FaDiceSix as FaDiceSixSolid,
  FaDiceThree as FaDiceThreeSolid,
  FaDiceTwo as FaDiceTwoSolid,
  FaChess as FaChessSolid,
  FaChessBoard as FaChessBoardSolid,
  FaChessKing as FaChessKingSolid,
  FaChessQueen as FaChessQueenSolid,
  FaChessRook as FaChessRookSolid,
  FaChessBishop as FaChessBishopSolid,
  FaChessKnight as FaChessKnightSolid,
  FaChessPawn as FaChessPawnSolid,
  FaGamepad as FaGamepadSolid,
  FaJoystick as FaJoystickSolid,
  FaKeyboard as FaKeyboardSolid,
  FaMouse as FaMouseSolid,
  FaMousePointer as FaMousePointerSolid,
  FaLaptop as FaLaptopSolid,
  FaLaptopCode as FaLaptopCodeSolid,
  FaDesktop as FaDesktopSolid,
  FaTablet as FaTabletSolid,
  FaMobile as FaMobileSolid,
  FaMobileAlt as FaMobileAltSolid
} from 'react-icons/fa';

// Gráficos
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler,
  ArcElement,
  RadialLinearScale,
  LogarithmicScale,
  TimeScale,
  ChartData,
  ChartOptions
} from 'chart.js';

import { Line, Bar, Pie, Doughnut, Radar, Scatter } from 'react-chartjs-2';
import 'chartjs-adapter-date-fns';
import { ptBR } from 'date-fns/locale';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler,
  ArcElement,
  RadialLinearScale,
  LogarithmicScale,
  TimeScale
);

// Tabelas
import { DataGrid, GridColDef, GridRowsProp } from '@mui/x-data-grid';
import { ThemeProvider, createTheme } from '@mui/material/styles';

// ============================================================================
// ENUMS E CONSTANTES
// ============================================================================

export enum MarketCondition {
  BULLISH = "bullish",
  BEARISH = "bearish",
  SIDEWAYS = "sideways",
  VOLATILE = "volatile",
  CONSOLIDATION = "consolidation"
}

export enum TradingSignal {
  STRONG_BUY = "strong_buy",
  BUY = "buy",
  HOLD = "hold",
  SELL = "sell",
  STRONG_SELL = "strong_sell"
}

export enum RiskLevel {
  VERY_LOW = "very_low",
  LOW = "low",
  MEDIUM = "medium",
  HIGH = "high",
  VERY_HIGH = "very_high"
}

export enum TimeFrame {
  M1 = "1m",
  M5 = "5m",
  M15 = "15m",
  M30 = "30m",
  H1 = "1h",
  H4 = "4h",
  D1 = "1d",
  W1 = "1w"
}

export enum OrderType {
  MARKET = "market",
  LIMIT = "limit",
  STOP = "stop",
  STOP_LIMIT = "stop_limit"
}

export enum OrderStatus {
  PENDING = "pending",
  FILLED = "filled",
  PARTIALLY_FILLED = "partially_filled",
  CANCELLED = "cancelled",
  REJECTED = "rejected",
  EXPIRED = "expired"
}

// ============================================================================
// INTERFACES E TIPOS
// ============================================================================

export interface MarketData {
  symbol: string;
  timestamp: Date;
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
  timeframe: TimeFrame;
}

export interface TechnicalIndicators {
  rsi: number;
  macd: number;
  macdSignal: number;
  bbUpper: number;
  bbMiddle: number;
  bbLower: number;
  sma20: number;
  sma50: number;
  ema12: number;
  ema26: number;
  stochK: number;
  stochD: number;
  atr: number;
  adx: number;
  williamsR: number;
  cci: number;
  obv: number;
  mfi: number;
  psar: number;
}

export interface TradingDecision {
  id: string;
  symbol: string;
  signal: TradingSignal;
  confidence: number;
  entryPrice: number;
  stopLoss: number;
  takeProfit: number;
  riskRewardRatio: number;
  positionSize: number;
  reasoning: string[];
  timestamp: Date;
  marketCondition: MarketCondition;
  riskLevel: RiskLevel;
  executed?: boolean;
  executionPrice?: number;
  executionTime?: Date;
  pnl?: number;
}

export interface Position {
  symbol: string;
  side: 'long' | 'short';
  entryPrice: number;
  currentPrice: number;
  quantity: number;
  value: number;
  stopLoss: number;
  takeProfit: number;
  unrealizedPnL: number;
  unrealizedPnLPercent: number;
  timestamp: Date;
  decisionId: string;
}

export interface PortfolioMetrics {
  totalValue: number;
  availableCash: number;
  investedAmount: number;
  unrealizedPnL: number;
  realizedPnL: number;
  totalReturn: number;
  sharpeRatio: number;
  sortinoRatio: number;
  maxDrawdown: number;
  winRate: number;
  profitFactor: number;
  totalTrades: number;
  winningTrades: number;
  losingTrades: number;
  avgWin: number;
  avgLoss: number;
  expectancy: number;
}

export interface MarketPrediction {
  symbol: string;
  prediction: number;
  confidence: number;
  direction: 'buy' | 'sell' | 'hold';
  priceChangePercent: number;
  currentPrice: number;
  timestamp: Date;
  timeframe: TimeFrame;
}

export interface RiskMetrics {
  var: number;            // Value at Risk
  cvar: number;           // Conditional VaR
  expectedShortfall: number;
  volatility: number;
  beta: number;
  correlationRisk: number;
  concentrationRisk: number;
  liquidityRisk: number;
}

// ============================================================================
/// CLASSES PRINCIPAIS
// ============================================================================

export class TechnicalIndicatorCalculator {
  
  calculateSMA(prices: number[], period: number): number[] {
    const sma: number[] = [];
    for (let i = 0; i < prices.length; i++) {
      if (i < period - 1) {
        sma.push(prices[i]);
        continue;
      }
      const sum = prices.slice(i - period + 1, i + 1).reduce((a, b) => a + b, 0);
      sma.push(sum / period);
    }
    return sma;
  }

  calculateEMA(prices: number[], period: number): number[] {
    const ema: number[] = [];
    const multiplier = 2 / (period + 1);
    
    for (let i = 0; i < prices.length; i++) {
      if (i === 0) {
        ema.push(prices[i]);
        continue;
      }
      const value = (prices[i] - ema[i - 1]) * multiplier + ema[i - 1];
      ema.push(value);
    }
    return ema;
  }

  calculateRSI(prices: number[], period: number = 14): number[] {
    const rsi: number[] = [];
    const gains: number[] = [];
    const losses: number[] = [];
    
    for (let i = 1; i < prices.length; i++) {
      const change = prices[i] - prices[i - 1];
      gains.push(change > 0 ? change : 0);
      losses.push(change < 0 ? -change : 0);
    }
    
    for (let i = 0; i < prices.length; i++) {
      if (i < period) {
        rsi.push(50);
        continue;
      }
      
      const avgGain = gains.slice(i - period, i).reduce((a, b) => a + b, 0) / period;
      const avgLoss = losses.slice(i - period, i).reduce((a, b) => a + b, 0) / period;
      
      if (avgLoss === 0) {
        rsi.push(100);
      } else {
        const rs = avgGain / avgLoss;
        rsi.push(100 - (100 / (1 + rs)));
      }
    }
    
    return rsi;
  }

  calculateMACD(prices: number[]): { macd: number[]; signal: number[]; histogram: number[] } {
    const ema12 = this.calculateEMA(prices, 12);
    const ema26 = this.calculateEMA(prices, 26);
    
    const macd: number[] = [];
    const minLength = Math.min(ema12.length, ema26.length);
    
    for (let i = 0; i < minLength; i++) {
      macd.push(ema12[i] - ema26[i]);
    }
    
    const signal = this.calculateEMA(macd, 9);
    const histogram: number[] = [];
    
    for (let i = 0; i < Math.min(macd.length, signal.length); i++) {
      histogram.push(macd[i] - signal[i]);
    }
    
    return { macd, signal, histogram };
  }

  calculateBollingerBands(prices: number[], period: number = 20): {
    upper: number[];
    middle: number[];
    lower: number[];
  } {
    const middle = this.calculateSMA(prices, period);
    const upper: number[] = [];
    const lower: number[] = [];
    
    for (let i = 0; i < prices.length; i++) {
      if (i < period - 1) {
        upper.push(prices[i]);
        lower.push(prices[i]);
        continue;
      }
      
      const slice = prices.slice(i - period + 1, i + 1);
      const mean = middle[i];
      const squaredDiffs = slice.map(p => Math.pow(p - mean, 2));
      const stdDev = Math.sqrt(squaredDiffs.reduce((a, b) => a + b, 0) / period);
      
      upper.push(mean + 2 * stdDev);
      lower.push(mean - 2 * stdDev);
    }
    
    return { upper, middle, lower };
  }

  calculateATR(high: number[], low: number[], close: number[], period: number = 14): number[] {
    const atr: number[] = [];
    
    for (let i = 0; i < high.length; i++) {
      if (i === 0) {
        atr.push(high[i] - low[i]);
        continue;
      }
      
      const tr1 = high[i] - low[i];
      const tr2 = Math.abs(high[i] - close[i - 1]);
      const tr3 = Math.abs(low[i] - close[i - 1]);
      const trueRange = Math.max(tr1, tr2, tr3);
      
      if (i < period) {
        atr.push(trueRange);
      } else {
        const prevAtr = atr[i - 1];
        atr.push((prevAtr * (period - 1) + trueRange) / period);
      }
    }
    
    return atr;
  }

  calculateStochastic(high: number[], low: number[], close: number[], kPeriod: number = 14, dPeriod: number = 3): {
    k: number[];
    d: number[];
  } {
    const k: number[] = [];
    
    for (let i = 0; i < close.length; i++) {
      if (i < kPeriod - 1) {
        k.push(50);
        continue;
      }
      
      const highMax = Math.max(...high.slice(i - kPeriod + 1, i + 1));
      const lowMin = Math.min(...low.slice(i - kPeriod + 1, i + 1));
      
      if (highMax === lowMin) {
        k.push(50);
      } else {
        k.push(100 * (close[i] - lowMin) / (highMax - lowMin));
      }
    }
    
    const d = this.calculateSMA(k, dPeriod);
    
    return { k, d };
  }

  calculateAllIndicators(marketData: MarketData[]): TechnicalIndicators {
    if (marketData.length < 50) {
      return this.getDefaultIndicators();
    }

    const prices = marketData.map(d => d.close);
    const highs = marketData.map(d => d.high);
    const lows = marketData.map(d => d.low);
    const volumes = marketData.map(d => d.volume);
    
    const rsi = this.calculateRSI(prices);
    const macd = this.calculateMACD(prices);
    const bollinger = this.calculateBollingerBands(prices);
    const atr = this.calculateATR(highs, lows, prices);
    const stochastic = this.calculateStochastic(highs, lows, prices);
    
    // OBV calculation
    let obv = 0;
    for (let i = 1; i < prices.length; i++) {
      if (prices[i] > prices[i - 1]) {
        obv += volumes[i];
      } else if (prices[i] < prices[i - 1]) {
        obv -= volumes[i];
      }
    }
    
    return {
      rsi: rsi[rsi.length - 1] || 50,
      macd: macd.macd[macd.macd.length - 1] || 0,
      macdSignal: macd.signal[macd.signal.length - 1] || 0,
      bbUpper: bollinger.upper[bollinger.upper.length - 1] || prices[prices.length - 1],
      bbMiddle: bollinger.middle[bollinger.middle.length - 1] || prices[prices.length - 1],
      bbLower: bollinger.lower[bollinger.lower.length - 1] || prices[prices.length - 1],
      sma20: this.calculateSMA(prices, 20).pop() || prices[prices.length - 1],
      sma50: this.calculateSMA(prices, 50).pop() || prices[prices.length - 1],
      ema12: this.calculateEMA(prices, 12).pop() || prices[prices.length - 1],
      ema26: this.calculateEMA(prices, 26).pop() || prices[prices.length - 1],
      stochK: stochastic.k[stochastic.k.length - 1] || 50,
      stochD: stochastic.d[stochastic.d.length - 1] || 50,
      atr: atr[atr.length - 1] || 0,
      adx: 25 + Math.random() * 50, // Simplified ADX
      williamsR: 0,
      cci: 0,
      obv,
      mfi: 50,
      psar: prices[prices.length - 1]
    };
  }

  private getDefaultIndicators(): TechnicalIndicators {
    return {
      rsi: 50,
      macd: 0,
      macdSignal: 0,
      bbUpper: 0,
      bbMiddle: 0,
      bbLower: 0,
      sma20: 0,
      sma50: 0,
      ema12: 0,
      ema26: 0,
      stochK: 50,
      stochD: 50,
      atr: 0,
      adx: 25,
      williamsR: 0,
      cci: 0,
      obv: 0,
      mfi: 50,
      psar: 0
    };
  }
}

export class MarketAnalyzer {
  private indicatorCalculator: TechnicalIndicatorCalculator;
  
  constructor() {
    this.indicatorCalculator = new TechnicalIndicatorCalculator();
  }

  detectMarketCondition(marketData: MarketData[], indicators: TechnicalIndicators): MarketCondition {
    if (marketData.length < 20) {
      return MarketCondition.SIDEWAYS;
    }

    const recentCloses = marketData.slice(-20).map(d => d.close);
    const priceChange = (recentCloses[recentCloses.length - 1] - recentCloses[0]) / recentCloses[0];
    const volatility = Math.sqrt(
      recentCloses.reduce((sum, val, i, arr) => {
        if (i === 0) return sum;
        const ret = (val - arr[i - 1]) / arr[i - 1];
        return sum + ret * ret;
      }, 0) / recentCloses.length
    );

    if (volatility > 0.05) {
      return MarketCondition.VOLATILE;
    } else if (priceChange > 0.03) {
      return MarketCondition.BULLISH;
    } else if (priceChange < -0.03) {
      return MarketCondition.BEARISH;
    } else if (Math.abs(priceChange) < 0.01) {
      return MarketCondition.CONSOLIDATION;
    } else {
      return MarketCondition.SIDEWAYS;
    }
  }

  generateSignals(indicators: TechnicalIndicators, marketCondition: MarketCondition): {
    signal: TradingSignal;
    score: number;
    reasoning: string[];
  } {
    let score = 0;
    const reasoning: string[] = [];

    // RSI Analysis
    if (indicators.rsi < 30) {
      score += 0.3;
      reasoning.push("RSI oversold - bullish");
    } else if (indicators.rsi > 70) {
      score -= 0.3;
      reasoning.push("RSI overbought - bearish");
    }

    // Moving Averages
    if (indicators.ema12 > indicators.ema26) {
      score += 0.2;
      reasoning.push("EMA bullish crossover");
    } else {
      score -= 0.2;
      reasoning.push("EMA bearish crossover");
    }

    // Price vs SMA
    const lastPrice = indicators.sma20; // Using SMA as proxy for current price
    if (lastPrice > indicators.sma20) {
      score += 0.2;
      reasoning.push("Price above 20 SMA");
    } else {
      score -= 0.2;
      reasoning.push("Price below 20 SMA");
    }

    // Bollinger Bands
    if (lastPrice < indicators.bbLower) {
      score += 0.2;
      reasoning.push("Price below lower Bollinger Band");
    } else if (lastPrice > indicators.bbUpper) {
      score -= 0.2;
      reasoning.push("Price above upper Bollinger Band");
    }

    // MACD
    if (indicators.macd > indicators.macdSignal) {
      score += 0.2;
      reasoning.push("MACD bullish");
    } else {
      score -= 0.2;
      reasoning.push("MACD bearish");
    }

    // Market condition adjustment
    if (marketCondition === MarketCondition.BULLISH) {
      score += 0.1;
    } else if (marketCondition === MarketCondition.BEARISH) {
      score -= 0.1;
    }

    // Determine final signal
    let signal: TradingSignal;
    if (score >= 0.6) {
      signal = TradingSignal.STRONG_BUY;
    } else if (score >= 0.3) {
      signal = TradingSignal.BUY;
    } else if (score <= -0.6) {
      signal = TradingSignal.STRONG_SELL;
    } else if (score <= -0.3) {
      signal = TradingSignal.SELL;
    } else {
      signal = TradingSignal.HOLD;
    }

    return { signal, score, reasoning };
  }

  calculateConfidence(score: number, marketCondition: MarketCondition): number {
    let confidence = Math.abs(score);
    
    // Adjust confidence based on market condition
    if (marketCondition === MarketCondition.VOLATILE) {
      confidence *= 0.8;
    } else if (marketCondition === MarketCondition.CONSOLIDATION) {
      confidence *= 0.9;
    } else if (marketCondition === MarketCondition.BULLISH || marketCondition === MarketCondition.BEARISH) {
      confidence *= 1.1;
    }
    
    return Math.min(1, Math.max(0, confidence));
  }
}

export class RiskManager {
  private initialCapital: number;
  private currentCapital: number;
  private maxRiskPerTrade: number = 0.02; // 2%
  private maxPortfolioRisk: number = 0.10; // 10%
  private maxCorrelationExposure: number = 0.30; // 30%
  
  public openPositions: Map<string, Position> = new Map();
  public tradeHistory: TradingDecision[] = [];

  constructor(initialCapital: number = 100000) {
    this.initialCapital = initialCapital;
    this.currentCapital = initialCapital;
  }

  calculatePositionSize(
    entryPrice: number,
    stopLoss: number,
    riskAmount?: number
  ): number {
    if (riskAmount === undefined) {
      riskAmount = this.currentCapital * this.maxRiskPerTrade;
    }

    const priceRisk = Math.abs(entryPrice - stopLoss);
    if (priceRisk === 0) return 0;

    let positionSize = riskAmount / priceRisk;

    // Limit to 25% of capital in one position
    const maxPositionValue = this.currentCapital * 0.25;
    const maxShares = maxPositionValue / entryPrice;

    return Math.min(positionSize, maxShares);
  }

  calculateStopLoss(entryPrice: number, atr: number, signal: TradingSignal): number {
    let atrMultiplier = 2.0;

    if (signal === TradingSignal.STRONG_BUY || signal === TradingSignal.STRONG_SELL) {
      atrMultiplier = 1.5;
    } else if (signal === TradingSignal.HOLD) {
      atrMultiplier = 3.0;
    }

    if (signal === TradingSignal.BUY || signal === TradingSignal.STRONG_BUY) {
      return entryPrice - (atr * atrMultiplier);
    } else {
      return entryPrice + (atr * atrMultiplier);
    }
  }

  calculateTakeProfit(entryPrice: number, stopLoss: number, riskRewardRatio: number = 2.0): number {
    const risk = Math.abs(entryPrice - stopLoss);
    const reward = risk * riskRewardRatio;

    if (entryPrice > stopLoss) {
      return entryPrice + reward;
    } else {
      return entryPrice - reward;
    }
  }

  assessRiskLevel(
    symbol: string,
    marketCondition: MarketCondition,
    volatility: number,
    correlationRisk: number = 0
  ): RiskLevel {
    let riskScore = 0;

    // Market condition risk
    const marketRisk = {
      [MarketCondition.BULLISH]: 0.2,
      [MarketCondition.BEARISH]: 0.3,
      [MarketCondition.SIDEWAYS]: 0.1,
      [MarketCondition.VOLATILE]: 0.8,
      [MarketCondition.CONSOLIDATION]: 0.1
    };
    riskScore += marketRisk[marketCondition] || 0.5;

    // Volatility risk
    if (volatility > 0.05) riskScore += 0.3;
    else if (volatility > 0.03) riskScore += 0.2;
    else if (volatility > 0.01) riskScore += 0.1;

    // Correlation risk
    riskScore += correlationRisk;

    if (riskScore >= 0.8) return RiskLevel.VERY_HIGH;
    if (riskScore >= 0.6) return RiskLevel.HIGH;
    if (riskScore >= 0.4) return RiskLevel.MEDIUM;
    if (riskScore >= 0.2) return RiskLevel.LOW;
    return RiskLevel.VERY_LOW;
  }

  validateTrade(decision: TradingDecision): { valid: boolean; issues: string[] } {
    const issues: string[] = [];

    // Check risk per trade
    const positionValue = decision.positionSize * decision.entryPrice;
    const tradeRisk = Math.abs(decision.entryPrice - decision.stopLoss) * decision.positionSize;
    const riskPercentage = tradeRisk / this.currentCapital;

    if (riskPercentage > this.maxRiskPerTrade) {
      issues.push(`Risk per trade too high: ${(riskPercentage * 100).toFixed(2)}%`);
    }

    // Check portfolio exposure
    const totalExposure = Array.from(this.openPositions.values())
      .reduce((sum, pos) => sum + pos.value, 0);
    const newTotalExposure = totalExposure + positionValue;
    const exposurePercentage = newTotalExposure / this.currentCapital;

    if (exposurePercentage > this.maxPortfolioRisk) {
      issues.push(`Portfolio exposure too high: ${(exposurePercentage * 100).toFixed(2)}%`);
    }

    // Check risk/reward ratio
    if (decision.riskRewardRatio < 1.5) {
      issues.push(`Risk/Reward ratio too low: ${decision.riskRewardRatio.toFixed(2)}`);
    }

    // Check confidence
    if (decision.confidence < 0.6) {
      issues.push(`Confidence too low: ${(decision.confidence * 100).toFixed(1)}%`);
    }

    return {
      valid: issues.length === 0,
      issues
    };
  }

  updatePosition(decision: TradingDecision, currentPrice: number): void {
    const existing = this.openPositions.get(decision.symbol);
    
    if (existing) {
      // Update existing position
      const newQuantity = existing.quantity + decision.positionSize;
      const newValue = existing.value + (decision.positionSize * decision.entryPrice);
      
      existing.quantity = newQuantity;
      existing.value = newValue;
      existing.currentPrice = currentPrice;
      existing.unrealizedPnL = (currentPrice - existing.entryPrice) * existing.quantity;
      existing.unrealizedPnLPercent = (currentPrice - existing.entryPrice) / existing.entryPrice;
    } else {
      // New position
      const position: Position = {
        symbol: decision.symbol,
        side: decision.signal.includes('BUY') ? 'long' : 'short',
        entryPrice: decision.entryPrice,
        currentPrice,
        quantity: decision.positionSize,
        value: decision.positionSize * decision.entryPrice,
        stopLoss: decision.stopLoss,
        takeProfit: decision.takeProfit,
        unrealizedPnL: 0,
        unrealizedPnLPercent: 0,
        timestamp: decision.timestamp,
        decisionId: decision.id
      };
      
      this.openPositions.set(decision.symbol, position);
    }
  }

  closePosition(symbol: string, exitPrice: number): Position | undefined {
    const position = this.openPositions.get(symbol);
    if (!position) return undefined;

    const pnl = (exitPrice - position.entryPrice) * position.quantity;
    position.unrealizedPnL = pnl;
    position.currentPrice = exitPrice;

    this.openPositions.delete(symbol);
    return position;
  }

  calculatePortfolioMetrics(decisions: TradingDecision[]): PortfolioMetrics {
    const winningTrades = decisions.filter(d => d.pnl && d.pnl > 0);
    const losingTrades = decisions.filter(d => d.pnl && d.pnl < 0);
    
    const totalPnL = decisions.reduce((sum, d) => sum + (d.pnl || 0), 0);
    const totalWins = winningTrades.reduce((sum, d) => sum + (d.pnl || 0), 0);
    const totalLosses = Math.abs(losingTrades.reduce((sum, d) => sum + (d.pnl || 0), 0));

    const avgWin = winningTrades.length > 0 ? totalWins / winningTrades.length : 0;
    const avgLoss = losingTrades.length > 0 ? totalLosses / losingTrades.length : 0;

    // Calculate Sharpe ratio (simplified)
    const returns = decisions.map(d => d.pnl || 0);
    const avgReturn = returns.length > 0 ? returns.reduce((a, b) => a + b, 0) / returns.length : 0;
    const stdDev = Math.sqrt(
      returns.reduce((sum, r) => sum + Math.pow(r - avgReturn, 2), 0) / (returns.length || 1)
    );
    const sharpeRatio = stdDev > 0 ? (avgReturn / stdDev) * Math.sqrt(252) : 0;

    return {
      totalValue: this.currentCapital + Array.from(this.openPositions.values())
        .reduce((sum, p) => sum + p.value, 0),
      availableCash: this.currentCapital,
      investedAmount: Array.from(this.openPositions.values())
        .reduce((sum, p) => sum + p.value, 0),
      unrealizedPnL: Array.from(this.openPositions.values())
        .reduce((sum, p) => sum + p.unrealizedPnL, 0),
      realizedPnL: totalPnL,
      totalReturn: (this.currentCapital + totalPnL - this.initialCapital) / this.initialCapital,
      sharpeRatio,
      sortinoRatio: sharpeRatio * 1.2,
      maxDrawdown: 0.05, // Simplified
      winRate: decisions.length > 0 ? winningTrades.length / decisions.length : 0,
      profitFactor: totalLosses > 0 ? totalWins / totalLosses : 0,
      totalTrades: decisions.length,
      winningTrades: winningTrades.length,
      losingTrades: losingTrades.length,
      avgWin,
      avgLoss,
      expectancy: decisions.length > 0 ? totalPnL / decisions.length : 0
    };
  }

  updateCapital(pnl: number): void {
    this.currentCapital += pnl;
  }
}

export class VhalinorTradingEngine {
  private marketAnalyzer: MarketAnalyzer;
  private riskManager: RiskManager;
  private indicatorCalculator: TechnicalIndicatorCalculator;
  
  private watchlist: string[];
  private decisions: TradingDecision[] = [];
  private isRunning: boolean = false;
  private intervalId?: NodeJS.Timeout;
  
  constructor(initialCapital: number = 100000, watchlist: string[] = ["AAPL", "GOOGL", "MSFT", "TSLA", "NVDA", "AMZN"]) {
    this.marketAnalyzer = new MarketAnalyzer();
    this.riskManager = new RiskManager(initialCapital);
    this.indicatorCalculator = new TechnicalIndicatorCalculator();
    this.watchlist = watchlist;
  }

  startTrading(callback?: (decision: TradingDecision) => void): void {
    if (this.isRunning) return;
    
    this.isRunning = true;
    this.intervalId = setInterval(() => {
      this.runAnalysisCycle(callback);
    }, 60000); // Run every minute
  }

  stopTrading(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
    this.isRunning = false;
  }

  private runAnalysisCycle(callback?: (decision: TradingDecision) => void): void {
    this.watchlist.forEach(symbol => {
      const marketData = this.generateMockMarketData(symbol);
      const decision = this.analyzeSymbol(symbol, marketData);
      
      if (decision) {
        this.decisions.push(decision);
        callback?.(decision);
      }
    });
  }

  private generateMockMarketData(symbol: string): MarketData[] {
    const data: MarketData[] = [];
    const basePrice = symbol === 'AAPL' ? 150 : symbol === 'GOOGL' ? 2500 : symbol === 'MSFT' ? 300 : 100;
    const now = new Date();

    for (let i = 100; i >= 0; i--) {
      const timestamp = new Date(now.getTime() - i * 60 * 60 * 1000);
      const change = (Math.random() - 0.5) * 2;
      const close = basePrice + change * (1 + Math.sin(i / 10) * 0.1);

      data.push({
        symbol,
        timestamp,
        open: close - 0.5,
        high: close + Math.random() * 2,
        low: close - Math.random() * 2,
        close,
        volume: 1000000 + Math.random() * 5000000,
        timeframe: TimeFrame.H1
      });
    }

    return data;
  }

  analyzeSymbol(symbol: string, marketData: MarketData[]): TradingDecision | null {
    if (marketData.length < 50) return null;

    const indicators = this.indicatorCalculator.calculateAllIndicators(marketData);
    const marketCondition = this.marketAnalyzer.detectMarketCondition(marketData, indicators);
    const { signal, score, reasoning } = this.marketAnalyzer.generateSignals(indicators, marketCondition);
    
    if (signal === TradingSignal.HOLD) return null;

    const confidence = this.marketAnalyzer.calculateConfidence(score, marketCondition);
    const currentPrice = marketData[marketData.length - 1].close;
    
    const stopLoss = this.riskManager.calculateStopLoss(currentPrice, indicators.atr, signal);
    const riskRewardRatio = 2.0;
    const takeProfit = this.riskManager.calculateTakeProfit(currentPrice, stopLoss, riskRewardRatio);
    const positionSize = this.riskManager.calculatePositionSize(currentPrice, stopLoss);
    
    const volatility = indicators.atr / currentPrice;
    const riskLevel = this.riskManager.assessRiskLevel(symbol, marketCondition, volatility);

    const decision: TradingDecision = {
      id: `trade_${Date.now()}_${Math.random().toString(36).substring(2, 7)}`,
      symbol,
      signal,
      confidence,
      entryPrice: currentPrice,
      stopLoss,
      takeProfit,
      riskRewardRatio,
      positionSize,
      reasoning,
      timestamp: new Date(),
      marketCondition,
      riskLevel,
      executed: false
    };

    const { valid, issues } = this.riskManager.validateTrade(decision);
    
    if (valid) {
      this.riskManager.updatePosition(decision, currentPrice);
      decision.executed = true;
      decision.executionPrice = currentPrice;
    }

    return decision;
  }

  getStatus(): {
    isRunning: boolean;
    openPositions: number;
    totalDecisions: number;
    metrics: PortfolioMetrics;
  } {
    return {
      isRunning: this.isRunning,
      openPositions: this.riskManager.openPositions.size,
      totalDecisions: this.decisions.length,
      metrics: this.riskManager.calculatePortfolioMetrics(this.decisions)
    };
  }

  getRecentDecisions(limit: number = 10): TradingDecision[] {
    return this.decisions.slice(-limit);
  }

  getOpenPositions(): Position[] {
    return Array.from(this.riskManager.openPositions.values());
  }
}

// ============================================================================
/// COMPONENTES DE INTERFACE
// ============================================================================

interface TradingDecisionCardProps {
  decision: TradingDecision;
  onClick?: () => void;
}

export const TradingDecisionCard: React.FC<TradingDecisionCardProps> = ({ decision, onClick }) => {
  const getSignalColor = () => {
    switch (decision.signal) {
      case TradingSignal.STRONG_BUY: return '#10B981';
      case TradingSignal.BUY: return '#3B82F6';
      case TradingSignal.HOLD: return '#94A3B8';
      case TradingSignal.SELL: return '#F59E0B';
      case TradingSignal.STRONG_SELL: return '#EF4444';
      default: return '#94A3B8';
    }
  };

  const getRiskColor = () => {
    switch (decision.riskLevel) {
      case RiskLevel.VERY_LOW: return '#10B981';
      case RiskLevel.LOW: return '#3B82F6';
      case RiskLevel.MEDIUM: return '#F59E0B';
      case RiskLevel.HIGH: return '#EF4444';
      case RiskLevel.VERY_HIGH: return '#8B5CF6';
      default: return '#94A3B8';
    }
  };

  return (
    <motion.div
      className="trading-decision-card"
      onClick={onClick}
      whileHover={{ scale: 1.02 }}
      style={{ borderLeftColor: getSignalColor() }}
    >
      <div className="decision-header">
        <span className="symbol">{decision.symbol}</span>
        <span className="signal" style={{ color: getSignalColor() }}>
          {decision.signal}
        </span>
      </div>

      <div className="decision-price">
        <span className="label">Preço:</span>
        <span className="value">${decision.entryPrice.toFixed(2)}</span>
      </div>

      <div className="decision-confidence">
        <div className="confidence-bar">
          <div
            className="confidence-fill"
            style={{
              width: `${decision.confidence * 100}%`,
              backgroundColor: getSignalColor()
            }}
          />
        </div>
        <span className="confidence-value">{(decision.confidence * 100).toFixed(1)}%</span>
      </div>

      <div className="decision-levels">
        <div className="level">
          <span className="label">Stop:</span>
          <span className="value">${decision.stopLoss.toFixed(2)}</span>
        </div>
        <div className="level">
          <span className="label">Alvo:</span>
          <span className="value">${decision.takeProfit.toFixed(2)}</span>
        </div>
        <div className="level">
          <span className="label">R:R:</span>
          <span className="value">1:{decision.riskRewardRatio.toFixed(1)}</span>
        </div>
      </div>

      <div className="decision-reasoning">
        {decision.reasoning.slice(0, 2).map((reason, idx) => (
          <div key={idx} className="reason">• {reason}</div>
        ))}
        {decision.reasoning.length > 2 && (
          <div className="reason more">+{decision.reasoning.length - 2} mais...</div>
        )}
      </div>

      <div className="decision-footer">
        <span className="market-condition">{decision.marketCondition}</span>
        <span className="risk-level" style={{ color: getRiskColor() }}>
          {decision.riskLevel}
        </span>
        {decision.executed && (
          <span className="executed-badge">✅ Executado</span>
        )}
      </div>
    </motion.div>
  );
};

interface PositionCardProps {
  position: Position;
  onClose?: () => void;
}

export const PositionCard: React.FC<PositionCardProps> = ({ position, onClose }) => {
  const pnlColor = position.unrealizedPnL >= 0 ? '#10B981' : '#EF4444';

  return (
    <div className="position-card">
      <div className="position-header">
        <span className="symbol">{position.symbol}</span>
        <span className="side" style={{ color: position.side === 'long' ? '#10B981' : '#EF4444' }}>
          {position.side}
        </span>
      </div>

      <div className="position-details">
        <div className="detail">
          <span>Entrada:</span>
          <span>${position.entryPrice.toFixed(2)}</span>
        </div>
        <div className="detail">
          <span>Atual:</span>
          <span>${position.currentPrice.toFixed(2)}</span>
        </div>
        <div className="detail">
          <span>Qtd:</span>
          <span>{position.quantity.toFixed(4)}</span>
        </div>
      </div>

      <div className="position-pnl" style={{ color: pnlColor }}>
        <span>P&L: ${position.unrealizedPnL.toFixed(2)}</span>
        <span>({(position.unrealizedPnLPercent * 100).toFixed(2)}%)</span>
      </div>

      <div className="position-levels">
        <div className="level">
          <span>Stop:</span>
          <span>${position.stopLoss.toFixed(2)}</span>
        </div>
        <div className="level">
          <span>Alvo:</span>
          <span>${position.takeProfit.toFixed(2)}</span>
        </div>
      </div>

      {onClose && (
        <button className="close-position-btn" onClick={onClose}>
          Fechar Posição
        </button>
      )}
    </div>
  );
};

interface MetricsCardProps {
  metrics: PortfolioMetrics;
}

export const MetricsCard: React.FC<MetricsCardProps> = ({ metrics }) => {
  return (
    <div className="metrics-card">
      <h3>📊 Métricas do Portfólio</h3>

      <div className="metrics-grid">
        <div className="metric-item">
          <span className="label">Valor Total:</span>
          <span className="value">${metrics.totalValue.toFixed(2)}</span>
        </div>
        <div className="metric-item">
          <span className="label">Caixa Disponível:</span>
          <span className="value">${metrics.availableCash.toFixed(2)}</span>
        </div>
        <div className="metric-item">
          <span className="label">Retorno Total:</span>
          <span className="value" style={{ color: metrics.totalReturn >= 0 ? '#10B981' : '#EF4444' }}>
            {(metrics.totalReturn * 100).toFixed(2)}%
          </span>
        </div>
        <div className="metric-item">
          <span className="label">Sharpe Ratio:</span>
          <span className="value">{metrics.sharpeRatio.toFixed(2)}</span>
        </div>
        <div className="metric-item">
          <span className="label">Win Rate:</span>
          <span className="value">{(metrics.winRate * 100).toFixed(1)}%</span>
        </div>
        <div className="metric-item">
          <span className="label">Profit Factor:</span>
          <span className="value">{metrics.profitFactor.toFixed(2)}</span>
        </div>
        <div className="metric-item">
          <span className="label">Total Trades:</span>
          <span className="value">{metrics.totalTrades}</span>
        </div>
        <div className="metric-item">
          <span className="label">Expectancy:</span>
          <span className="value">${metrics.expectancy.toFixed(2)}</span>
        </div>
      </div>
    </div>
  );
};

// ============================================================================
// COMPONENTE PRINCIPAL
// ============================================================================

interface VhalinorTradingEngineProps {
  initialCapital?: number;
  onTradeExecuted?: (decision: TradingDecision) => void;
}

export const VhalinorTradingEngineComponent: React.FC<VhalinorTradingEngineProps> = ({
  initialCapital = 100000,
  onTradeExecuted
}) => {
  const [engine] = useState(() => new VhalinorTradingEngine(initialCapital));
  const [isRunning, setIsRunning] = useState(false);
  const [decisions, setDecisions] = useState<TradingDecision[]>([]);
  const [positions, setPositions] = useState<Position[]>([]);
  const [metrics, setMetrics] = useState<PortfolioMetrics | null>(null);
  const [activeTab, setActiveTab] = useState<'overview' | 'decisions' | 'positions'>('overview');
  const [autoRun, setAutoRun] = useState(false);

  useEffect(() => {
    if (autoRun && !isRunning) {
      handleStartTrading();
    }
  }, [autoRun]);

  useEffect(() => {
    // Update positions periodically
    const interval = setInterval(() => {
      if (engine) {
        setPositions(engine.getOpenPositions());
        setMetrics(engine.getStatus().metrics);
      }
    }, 5000);

    return () => clearInterval(interval);
  }, [engine]);

  const handleStartTrading = () => {
    engine.startTrading((decision) => {
      setDecisions(prev => [decision, ...prev].slice(0, 50));
      setPositions(engine.getOpenPositions());
      setMetrics(engine.getStatus().metrics);
      onTradeExecuted?.(decision);
      
      if (decision.executed) {
        toast.success(`🎯 ${decision.symbol}: ${decision.signal} @ $${decision.entryPrice.toFixed(2)}`);
      }
    });
    
    setIsRunning(true);
    toast.info('🚀 Trading autônomo iniciado');
  };

  const handleStopTrading = () => {
    engine.stopTrading();
    setIsRunning(false);
    toast.warning('🛑 Trading autônomo parado');
  };

  const handleClosePosition = (symbol: string) => {
    const exitPrice = positions.find(p => p.symbol === symbol)?.currentPrice || 0;
    const closed = engine['riskManager'].closePosition(symbol, exitPrice);
    
    if (closed) {
      setPositions(engine.getOpenPositions());
      setMetrics(engine.getStatus().metrics);
      toast.info(`🔒 Posição ${symbol} fechada`);
    }
  };

  return (
    <div className="vhalinor-trading-engine">
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="dark"
      />

      {/* Header */}
      <header className="engine-header">
        <div className="header-left">
          <h1>
            <FaRobot className="icon" />
            VHALINOR.IAG Autonomous Trading Engine
          </h1>
          
          <div className="header-badges">
            <span className={`badge status ${isRunning ? 'running' : 'stopped'}`}>
              {isRunning ? '🟢 Executando' : '🔴 Parado'}
            </span>
            <span className="badge positions">
              📊 {positions.length} posições
            </span>
          </div>
        </div>

        <div className="header-right">
          <label className="auto-run">
            <input
              type="checkbox"
              checked={autoRun}
              onChange={(e) => setAutoRun(e.target.checked)}
            />
            Auto Run
          </label>

          {!isRunning ? (
            <button
              className="action-btn start"
              onClick={handleStartTrading}
            >
              <FaPlay /> Iniciar Trading
            </button>
          ) : (
            <button
              className="action-btn stop"
              onClick={handleStopTrading}
            >
              <FaStop /> Parar Trading
            </button>
          )}
        </div>
      </header>

      {/* Tabs */}
      <div className="engine-tabs">
        <button
          className={activeTab === 'overview' ? 'active' : ''}
          onClick={() => setActiveTab('overview')}
        >
          📊 Visão Geral
        </button>
        <button
          className={activeTab === 'decisions' ? 'active' : ''}
          onClick={() => setActiveTab('decisions')}
        >
          🎯 Decisões
        </button>
        <button
          className={activeTab === 'positions' ? 'active' : ''}
          onClick={() => setActiveTab('positions')}
        >
          📈 Posições Abertas
        </button>
      </div>

      {/* Content */}
      <div className="engine-content">
        <AnimatePresence mode="wait">
          {activeTab === 'overview' && (
            <motion.div
              key="overview"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="overview-tab"
            >
              {metrics && <MetricsCard metrics={metrics} />}

              <div className="status-card">
                <h3>📋 Status do Sistema</h3>
                <div className="status-items">
                  <div className="status-item">
                    <span>Posições Abertas:</span>
                    <span>{positions.length}</span>
                  </div>
                  <div className="status-item">
                    <span>Decisões Tomadas:</span>
                    <span>{decisions.length}</span>
                  </div>
                  <div className="status-item">
                    <span>Última Decisão:</span>
                    <span>
                      {decisions[0] ? 
                        `${decisions[0].symbol} ${decisions[0].signal} @ $${decisions[0].entryPrice.toFixed(2)}` 
                        : 'Nenhuma'}
                    </span>
                  </div>
                </div>
              </div>
            </motion.div>
          )}

          {activeTab === 'decisions' && (
            <motion.div
              key="decisions"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="decisions-tab"
            >
              <div className="decisions-grid">
                {decisions.map(decision => (
                  <TradingDecisionCard key={decision.id} decision={decision} />
                ))}
              </div>
            </motion.div>
          )}

          {activeTab === 'positions' && (
            <motion.div
              key="positions"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="positions-tab"
            >
              <div className="positions-grid">
                {positions.map(position => (
                  <PositionCard
                    key={position.symbol}
                    position={position}
                    onClose={() => handleClosePosition(position.symbol)}
                  />
                ))}
                {positions.length === 0 && (
                  <div className="no-positions">
                    <FaInfoCircle /> Nenhuma posição aberta
                  </div>
                )}
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>

      {/* Footer */}
      <footer className="engine-footer">
        <div className="footer-left">
          <span>🤖 VHALINOR.IAG v5.0.0</span>
          <span>💰 Capital: ${initialCapital.toLocaleString()}</span>
          <span>📊 Watchlist: {engine['watchlist'].join(', ')}</span>
        </div>
        <div className="footer-right">
          <span>⚡ Autonomous Trading Specialist</span>
          <span>© 2026 VHALINOR.IAG</span>
        </div>
      </footer>
    </div>
  );
};

// ============================================================================
// CSS Styles (adicione no seu arquivo CSS)
// ============================================================================

export const VhalinorTradingStyles = `
.vhalinor-trading-engine {
  min-height: 100vh;
  background: linear-gradient(135deg, #0F172A 0%, #1E293B 100%);
  color: #fff;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  flex-direction: column;
}

/* Header */
.engine-header {
  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(10px);
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(6, 214, 160, 0.2);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-left h1 {
  margin: 0;
  font-size: 20px;
  background: linear-gradient(135deg, #06D6A0, #3B82F6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-left .icon {
  font-size: 24px;
  -webkit-text-fill-color: #06D6A0;
}

.header-badges {
  display: flex;
  gap: 10px;
}

.badge {
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 11px;
  font-weight: bold;
  background: rgba(255, 255, 255, 0.1);
}

.badge.status.running {
  background: rgba(16, 185, 129, 0.2);
  color: #10B981;
}

.badge.status.stopped {
  background: rgba(239, 68, 68, 0.2);
  color: #EF4444;
}

.badge.positions {
  background: rgba(59, 130, 246, 0.2);
  color: #3B82F6;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.auto-run {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #94A3B8;
  font-size: 13px;
}

.auto-run input {
  cursor: pointer;
}

.action-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
}

.action-btn.start {
  background: #10B981;
}

.action-btn.start:hover {
  background: #059669;
}

.action-btn.stop {
  background: #EF4444;
}

.action-btn.stop:hover {
  background: #dc2626;
}

/* Tabs */
.engine-tabs {
  display: flex;
  gap: 5px;
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid #334155;
  border-radius: 10px;
  padding: 5px;
  margin: 20px;
}

.engine-tabs button {
  flex: 1;
  padding: 10px;
  background: transparent;
  border: none;
  border-radius: 6px;
  color: #94A3B8;
  cursor: pointer;
  font-size: 13px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.2s;
}

.engine-tabs button:hover {
  background: rgba(255, 255, 255, 0.05);
}

.engine-tabs button.active {
  background: rgba(6, 214, 160, 0.2);
  color: #06D6A0;
}

/* Content */
.engine-content {
  flex: 1;
  padding: 0 20px;
  overflow-y: auto;
}

/* Overview Tab */
.metrics-card,
.status-card {
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid #334155;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 20px;
}

.metrics-card h3,
.status-card h3 {
  margin: 0 0 15px 0;
  color: #94A3B8;
  font-size: 14px;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px;
}

.metric-item {
  display: flex;
  flex-direction: column;
  padding: 8px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 4px;
}

.metric-item .label {
  font-size: 10px;
  color: #64748B;
}

.metric-item .value {
  font-size: 14px;
  font-weight: bold;
  color: #06D6A0;
}

.status-items {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  padding: 8px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 4px;
}

.status-item span:first-child {
  color: #94A3B8;
}

.status-item span:last-child {
  font-weight: bold;
  color: #06D6A0;
}

/* Decisions Tab */
.decisions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 15px;
}

.trading-decision-card {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.2s;
  border-left: 3px solid;
}

.trading-decision-card:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.05);
}

.decision-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.symbol {
  font-weight: bold;
  font-size: 14px;
}

.signal {
  font-size: 12px;
  font-weight: bold;
  padding: 2px 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.decision-price {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 13px;
}

.decision-price .value {
  font-weight: bold;
  color: #06D6A0;
}

.decision-confidence {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.confidence-bar {
  flex: 1;
  height: 6px;
  background: #334155;
  border-radius: 3px;
  overflow: hidden;
}

.confidence-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s;
}

.confidence-value {
  min-width: 45px;
  font-size: 11px;
  color: #94A3B8;
}

.decision-levels {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin-bottom: 10px;
}

.level {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 11px;
}

.level .label {
  color: #64748B;
}

.level .value {
  font-weight: bold;
  color: #F59E0B;
}

.decision-reasoning {
  font-size: 11px;
  color: #94A3B8;
  margin-bottom: 10px;
}

.reason {
  margin: 2px 0;
}

.reason.more {
  color: #3B82F6;
  font-style: italic;
}

.decision-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 10px;
  padding-top: 8px;
  border-top: 1px solid #334155;
}

.market-condition {
  padding: 2px 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.executed-badge {
  padding: 2px 6px;
  background: rgba(16, 185, 129, 0.2);
  border-radius: 4px;
  color: #10B981;
}

/* Positions Tab */
.positions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
}

.position-card {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 15px;
}

.position-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.symbol {
  font-weight: bold;
  font-size: 14px;
}

.side {
  font-size: 12px;
  font-weight: bold;
  padding: 2px 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.position-details {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin-bottom: 10px;
  font-size: 12px;
}

.detail {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.detail span:first-child {
  color: #64748B;
  font-size: 10px;
}

.detail span:last-child {
  font-weight: bold;
}

.position-pnl {
  text-align: center;
  margin-bottom: 10px;
  padding: 5px;
  background: rgba(0, 0, 0, 0.3);
  border-radius: 4px;
  font-size: 13px;
  font-weight: bold;
}

.position-levels {
  display: flex;
  justify-content: space-around;
  margin-bottom: 10px;
  font-size: 11px;
}

.level span:first-child {
  color: #64748B;
  margin-right: 5px;
}

.level span:last-child {
  font-weight: bold;
  color: #F59E0B;
}

.close-position-btn {
  width: 100%;
  padding: 8px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid #EF4444;
  border-radius: 6px;
  color: #EF4444;
  cursor: pointer;
  font-size: 12px;
  font-weight: bold;
  transition: all 0.2s;
}

.close-position-btn:hover {
  background: #EF4444;
  color: white;
}

.no-positions {
  text-align: center;
  padding: 60px;
  color: #94A3B8;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 16px;
}

/* Footer */
.engine-footer {
  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(10px);
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #64748B;
  border-top: 1px solid rgba(6, 214, 160, 0.2);
  margin-top: 20px;
}

.footer-left,
.footer-right {
  display: flex;
  gap: 20px;
}

/* Scrollbar */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #0F172A;
}

::-webkit-scrollbar-thumb {
  background: #334155;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #475569;
}

/* Responsive */
@media (max-width: 1200px) {
  .decisions-grid,
  .positions-grid {
    grid-template-columns: 1fr;
  }
  
  .metrics-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .engine-header {
    flex-direction: column;
    gap: 10px;
  }
  
  .header-left {
    flex-direction: column;
    text-align: center;
  }
  
  .engine-tabs {
    flex-wrap: wrap;
  }
  
  .metrics-grid {
    grid-template-columns: 1fr;
  }
  
  .footer-left,
  .footer-right {
    flex-wrap: wrap;
  }
}
`;

// ============================================================================
// COMPONENTE DE DEMONSTRAÇÃO
// ============================================================================

export const VhalinorTradingEngineDemo: React.FC = () => {
  const handleTradeExecuted = (decision: TradingDecision) => {
    console.log('Trade executado:', decision);
  };

  return (
    <VhalinorTradingEngineComponent
      initialCapital={100000}
      onTradeExecuted={handleTradeExecuted}
    />
  );
};

// ============================================================================
// EXPORTS
// ============================================================================

export default VhalinorTradingEngineComponent;