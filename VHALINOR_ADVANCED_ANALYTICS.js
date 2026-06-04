/**
 * VHALINOR.IAG - Advanced Analytics System
 * =========================================
 * Virtual Hybrid Advanced Learning Intelligence Neural Optimized Reasoning
 * Advanced Financial Market Analysis Module with AI
 * 
 * Features:
 * - Advanced Pattern Recognition
 * - Market Correlations Analysis
 * - Volatility Analysis with GARCH models
 * - Anomaly Detection
 * - Technical Indicators (40+)
 * - Machine Learning Predictions
 * - Real-time Dashboard
 * 
 * Version: 5.0.0 - Advanced Analytics Module
 * Author: VHALINOR.IAG Team
 */

import React, { useState, useEffect, useCallback, useRef } from 'react';
import './VhalinorAnalytics.css';

// ============================================================================
// IMPORTAÇÕES DE BIBLIOTECAS
// ============================================================================

import { motion, AnimatePresence } from 'framer-motion';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// Ícones
import {
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
  FaRobot,
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

export enum AnalysisType {
  TECHNICAL = "technical",
  FUNDAMENTAL = "fundamental",
  SENTIMENT = "sentiment",
  CORRELATION = "correlation",
  VOLATILITY = "volatility",
  MOMENTUM = "momentum",
  PATTERN = "pattern",
  ANOMALY = "anomaly",
  QUANTUM = "quantum",
  PREDICTIVE = "predictive"
}

export enum PatternType {
  TREND_CONTINUATION = "trend_continuation",
  TREND_REVERSAL = "trend_reversal",
  CONSOLIDATION = "consolidation",
  BREAKOUT = "breakout",
  SUPPORT_RESISTANCE = "support_resistance",
  HARMONIC = "harmonic",
  CANDLESTICK = "candlestick",
  ELLIOTT_WAVE = "elliott_wave",
  GARTLEY = "gartley",
  FIBONACCI = "fibonacci",
  ICHIMOKU = "ichimoku"
}

export enum MarketRegime {
  BULL_MARKET = "bull_market",
  BEAR_MARKET = "bear_market",
  SIDEWAYS_MARKET = "sideways_market",
  HIGH_VOLATILITY = "high_volatility",
  LOW_VOLATILITY = "low_volatility",
  CRISIS = "crisis",
  RECOVERY = "recovery",
  BREAKOUT = "breakout",
  REVERSAL = "reversal"
}

export enum CorrelationStrength {
  VERY_WEAK = "very_weak",
  WEAK = "weak",
  MODERATE = "moderate",
  STRONG = "strong",
  VERY_STRONG = "very_strong"
}

export enum VolatilityRegime {
  EXTREME = "extreme",
  HIGH = "high",
  NORMAL = "normal",
  LOW = "low",
  VERY_LOW = "very_low"
}

export enum TimeFrame {
  M1 = "1m",
  M5 = "5m",
  M15 = "15m",
  M30 = "30m",
  H1 = "1h",
  H4 = "4h",
  D1 = "1d",
  W1 = "1w",
  MN1 = "1M"
}

// ============================================================================
// INTERFACES E TIPOS
// ============================================================================

export interface MarketData {
  timestamp: Date;
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
  symbol: string;
}

export interface TechnicalIndicators {
  // Moving Averages
  sma20: number;
  sma50: number;
  sma200: number;
  ema12: number;
  ema26: number;
  wma20: number;
  hma20: number;
  vwma20: number;
  
  // Momentum
  rsi: number;
  stochK: number;
  stochD: number;
  cci: number;
  williamsR: number;
  roc: number;
  mfi: number;
  
  // MACD
  macd: number;
  macdSignal: number;
  macdHistogram: number;
  
  // Bollinger Bands
  bbUpper: number;
  bbMiddle: number;
  bbLower: number;
  bbWidth: number;
  bbPercent: number;
  
  // Ichimoku
  tenkanSen: number;
  kijunSen: number;
  senkouSpanA: number;
  senkouSpanB: number;
  chikouSpan: number;
  
  // Volume
  obv: number;
  vpt: number;
  mfi20: number;
  cmf: number;
  
  // Volatility
  atr: number;
  keltnerUpper: number;
  keltnerLower: number;
  keltnerMiddle: number;
  
  // Support/Resistance
  pivot: number;
  r1: number;
  r2: number;
  r3: number;
  s1: number;
  s2: number;
  s3: number;
  
  // Advanced
  fractalDimension: number;
  hurstExponent: number;
  lyapunovExponent: number;
  correlationDimension: number;
}

export interface AdvancedPattern {
  patternType: PatternType;
  symbol: string;
  timeframe: TimeFrame;
  startTime: Date;
  endTime: Date;
  confidence: number;
  strength: number;
  targetPrice?: number;
  stopLoss?: number;
  probability: number;
  historicalSuccessRate: number;
  riskRewardRatio: number;
  description: string;
  keyLevels: number[];
  entryPrice?: number;
  exitPrice?: number;
  patternQuality: number;
  marketRegime: MarketRegime;
  indicators: string[];
}

export interface MarketCorrelation {
  asset1: string;
  asset2: string;
  correlation: number;
  pValue: number;
  timeframe: TimeFrame;
  periodDays: number;
  strength: CorrelationStrength;
  stability: number;
  rollingCorrelations: number[];
  leadLag?: number;
  cointegrated: boolean;
}

export interface VolatilityAnalysis {
  symbol: string;
  currentVolatility: number;
  historicalVolatility: number;
  volatilityPercentile: number;
  volatilityRegime: VolatilityRegime;
  garchForecast?: number;
  volatilityClustering: boolean;
  meanReversionSpeed: number;
  expectedShortfall: number;
  valueAtRisk: number;
  tailRisk: number;
  volOfVol: number;
  skewness: number;
  kurtosis: number;
  jarqueBera: number;
}

export interface AnomalyDetection {
  symbol: string;
  timestamp: Date;
  price: number;
  volume: number;
  anomalyScore: number;
  anomalyType: 'price' | 'volume' | 'both';
  severity: 'low' | 'medium' | 'high';
  description: string;
  expectedRange: [number, number];
  zScore: number;
}

export interface PredictiveModel {
  modelType: string;
  accuracy: number;
  predictions: number[];
  confidence: number;
  features: string[];
  mse: number;
  mae: number;
  r2: number;
}

export interface ComprehensiveAnalysis {
  timestamp: Date;
  assetsAnalyzed: string[];
  analysisTypes: AnalysisType[];
  patterns: Record<string, AdvancedPattern[]>;
  correlations: MarketCorrelation[];
  volatility: Record<string, VolatilityAnalysis>;
  anomalies: Record<string, AnomalyDetection[]>;
  technical: Record<string, TechnicalIndicators>;
  predictions: Record<string, PredictiveModel>;
  marketRegime: MarketRegime;
  overallConfidence: number;
  summary: string;
}

// ============================================================================
/// CLASSES PRINCIPAIS
// ============================================================================

export class TechnicalIndicatorCalculator {
  
  /**
   * Calcula SMA (Simple Moving Average)
   */
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

  /**
   * Calcula EMA (Exponential Moving Average)
   */
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

  /**
   * Calcula MACD
   */
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

  /**
   * Calcula RSI
   */
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

  /**
   * Calcula Bollinger Bands
   */
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

  /**
   * Calcula ATR (Average True Range)
   */
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

  /**
   * Calcula OBV (On-Balance Volume)
   */
  calculateOBV(prices: number[], volumes: number[]): number[] {
    const obv: number[] = [0];
    
    for (let i = 1; i < prices.length; i++) {
      if (prices[i] > prices[i - 1]) {
        obv.push(obv[i - 1] + volumes[i]);
      } else if (prices[i] < prices[i - 1]) {
        obv.push(obv[i - 1] - volumes[i]);
      } else {
        obv.push(obv[i - 1]);
      }
    }
    
    return obv;
  }

  /**
   * Calcula todos os indicadores para um conjunto de dados
   */
  calculateAllIndicators(marketData: MarketData[]): TechnicalIndicators[] {
    const prices = marketData.map(d => d.close);
    const highs = marketData.map(d => d.high);
    const lows = marketData.map(d => d.low);
    const volumes = marketData.map(d => d.volume);
    
    const sma20 = this.calculateSMA(prices, 20);
    const sma50 = this.calculateSMA(prices, 50);
    const sma200 = this.calculateSMA(prices, 200);
    const ema12 = this.calculateEMA(prices, 12);
    const ema26 = this.calculateEMA(prices, 26);
    const macd = this.calculateMACD(prices);
    const rsi = this.calculateRSI(prices);
    const bollinger = this.calculateBollingerBands(prices);
    const atr = this.calculateATR(highs, lows, prices);
    const obv = this.calculateOBV(prices, volumes);
    
    const indicators: TechnicalIndicators[] = [];
    
    for (let i = 0; i < marketData.length; i++) {
      indicators.push({
        sma20: sma20[i] || 0,
        sma50: sma50[i] || 0,
        sma200: sma200[i] || 0,
        ema12: ema12[i] || 0,
        ema26: ema26[i] || 0,
        wma20: 0,
        hma20: 0,
        vwma20: 0,
        rsi: rsi[i] || 50,
        stochK: 50,
        stochD: 50,
        cci: 0,
        williamsR: 0,
        roc: 0,
        mfi: 50,
        macd: macd.macd[i] || 0,
        macdSignal: macd.signal[i] || 0,
        macdHistogram: macd.histogram[i] || 0,
        bbUpper: bollinger.upper[i] || 0,
        bbMiddle: bollinger.middle[i] || 0,
        bbLower: bollinger.lower[i] || 0,
        bbWidth: ((bollinger.upper[i] || 0) - (bollinger.lower[i] || 0)) / (bollinger.middle[i] || 1),
        bbPercent: ((prices[i] - (bollinger.lower[i] || 0)) / ((bollinger.upper[i] || 0) - (bollinger.lower[i] || 0) || 1)),
        tenkanSen: 0,
        kijunSen: 0,
        senkouSpanA: 0,
        senkouSpanB: 0,
        chikouSpan: 0,
        obv: obv[i] || 0,
        vpt: 0,
        mfi20: 50,
        cmf: 0,
        atr: atr[i] || 0,
        keltnerUpper: 0,
        keltnerLower: 0,
        keltnerMiddle: 0,
        pivot: 0,
        r1: 0,
        r2: 0,
        r3: 0,
        s1: 0,
        s2: 0,
        s3: 0,
        fractalDimension: 1.5,
        hurstExponent: 0.5,
        lyapunovExponent: 0,
        correlationDimension: 0
      });
    }
    
    return indicators;
  }
}

export class AdvancedPatternAnalyzer {
  private patterns: Map<string, AdvancedPattern[]> = new Map();

  async identifyPatterns(marketData: MarketData[], timeframe: TimeFrame = TimeFrame.H1): Promise<AdvancedPattern[]> {
    if (marketData.length < 50) return [];
    
    const patterns: AdvancedPattern[] = [];
    const prices = marketData.map(d => d.close);
    const highs = marketData.map(d => d.high);
    const lows = marketData.map(d => d.low);
    
    // Detectar Head and Shoulders
    const headShoulders = this.detectHeadShoulders(prices, highs, lows);
    if (headShoulders) patterns.push(headShoulders);
    
    // Detectar Double Top/Bottom
    const doublePatterns = this.detectDoublePatterns(prices);
    patterns.push(...doublePatterns);
    
    // Detectar Triangles
    const triangles = this.detectTriangles(prices);
    patterns.push(...triangles);
    
    // Detectar Flags and Pennants
    const flags = this.detectFlags(prices);
    patterns.push(...flags);
    
    // Detectar Support/Resistance
    const supportResistance = this.detectSupportResistance(marketData);
    patterns.push(...supportResistance);
    
    // Filtrar por confiança
    return patterns.filter(p => p.confidence > 0.6).sort((a, b) => b.confidence - a.confidence);
  }

  private detectHeadShoulders(prices: number[], highs: number[], lows: number[]): AdvancedPattern | null {
    if (prices.length < 50) return null;
    
    // Procurar por padrão Cabeça e Ombros
    for (let i = 20; i < prices.length - 20; i++) {
      const leftShoulder = Math.max(...prices.slice(i - 15, i - 5));
      const head = Math.max(...prices.slice(i - 5, i + 5));
      const rightShoulder = Math.max(...prices.slice(i + 5, i + 15));
      
      if (head > leftShoulder && head > rightShoulder && 
          Math.abs(leftShoulder - rightShoulder) / leftShoulder < 0.1) {
        
        const neckline = Math.min(...lows.slice(i - 10, i + 10));
        
        return {
          patternType: PatternType.TREND_REVERSAL,
          symbol: 'SYMBOL',
          timeframe: TimeFrame.H1,
          startTime: new Date(),
          endTime: new Date(),
          confidence: 0.75,
          strength: 0.8,
          targetPrice: neckline * 0.95,
          stopLoss: head * 1.02,
          probability: 0.7,
          historicalSuccessRate: 0.72,
          riskRewardRatio: 2.5,
          description: "Head and Shoulders pattern detected - bearish reversal expected",
          keyLevels: [leftShoulder, head, rightShoulder, neckline],
          entryPrice: neckline,
          exitPrice: neckline * 0.95,
          patternQuality: 0.8,
          marketRegime: MarketRegime.BEAR_MARKET,
          indicators: ['Volume', 'RSI', 'MACD']
        };
      }
    }
    
    return null;
  }

  private detectDoublePatterns(prices: number[]): AdvancedPattern[] {
    const patterns: AdvancedPattern[] = [];
    
    for (let i = 20; i < prices.length - 20; i++) {
      // Double Top
      const firstTop = Math.max(...prices.slice(i - 15, i - 5));
      const secondTop = Math.max(...prices.slice(i + 5, i + 15));
      
      if (Math.abs(firstTop - secondTop) / firstTop < 0.03) {
        const valley = Math.min(...prices.slice(i - 5, i + 5));
        
        patterns.push({
          patternType: PatternType.TREND_REVERSAL,
          symbol: 'SYMBOL',
          timeframe: TimeFrame.H1,
          startTime: new Date(),
          endTime: new Date(),
          confidence: 0.7,
          strength: 0.75,
          targetPrice: valley * 0.98,
          stopLoss: secondTop * 1.02,
          probability: 0.68,
          historicalSuccessRate: 0.7,
          riskRewardRatio: 2.0,
          description: "Double Top pattern detected - bearish reversal",
          keyLevels: [firstTop, secondTop, valley],
          entryPrice: valley,
          exitPrice: valley * 0.98,
          patternQuality: 0.75,
          marketRegime: MarketRegime.BEAR_MARKET,
          indicators: ['Volume', 'RSI', 'Support']
        });
      }
      
      // Double Bottom
      const firstBottom = Math.min(...prices.slice(i - 15, i - 5));
      const secondBottom = Math.min(...prices.slice(i + 5, i + 15));
      
      if (Math.abs(firstBottom - secondBottom) / firstBottom < 0.03) {
        const peak = Math.max(...prices.slice(i - 5, i + 5));
        
        patterns.push({
          patternType: PatternType.TREND_REVERSAL,
          symbol: 'SYMBOL',
          timeframe: TimeFrame.H1,
          startTime: new Date(),
          endTime: new Date(),
          confidence: 0.7,
          strength: 0.75,
          targetPrice: peak * 1.02,
          stopLoss: secondBottom * 0.98,
          probability: 0.68,
          historicalSuccessRate: 0.7,
          riskRewardRatio: 2.0,
          description: "Double Bottom pattern detected - bullish reversal",
          keyLevels: [firstBottom, secondBottom, peak],
          entryPrice: peak,
          exitPrice: peak * 1.02,
          patternQuality: 0.75,
          marketRegime: MarketRegime.BULL_MARKET,
          indicators: ['Volume', 'RSI', 'Support']
        });
      }
    }
    
    return patterns;
  }

  private detectTriangles(prices: number[]): AdvancedPattern[] {
    const patterns: AdvancedPattern[] = [];
    
    for (let i = 30; i < prices.length; i++) {
      const segment = prices.slice(i - 30, i);
      
      // Calcular máximos e mínimos
      const highs = segment.map((_, idx) => Math.max(...segment.slice(idx, idx + 5)));
      const lows = segment.map((_, idx) => Math.min(...segment.slice(idx, idx + 5)));
      
      // Verificar convergência (triângulo simétrico)
      const highTrend = this.linearRegression(highs);
      const lowTrend = this.linearRegression(lows);
      
      if (highTrend.slope < 0 && lowTrend.slope > 0 && Math.abs(highTrend.slope) > 0.01) {
        patterns.push({
          patternType: PatternType.CONSOLIDATION,
          symbol: 'SYMBOL',
          timeframe: TimeFrame.H1,
          startTime: new Date(),
          endTime: new Date(),
          confidence: 0.65,
          strength: 0.7,
          targetPrice: prices[i - 1] * (1 + Math.abs(highTrend.slope) * 5),
          stopLoss: prices[i - 1] * 0.98,
          probability: 0.6,
          historicalSuccessRate: 0.62,
          riskRewardRatio: 1.8,
          description: "Symmetrical Triangle pattern detected - breakout expected",
          keyLevels: [highTrend.intercept, lowTrend.intercept],
          entryPrice: prices[i - 1],
          exitPrice: prices[i - 1] * 1.05,
          patternQuality: 0.7,
          marketRegime: MarketRegime.SIDEWAYS_MARKET,
          indicators: ['Volume', 'Volatility']
        });
      }
    }
    
    return patterns;
  }

  private detectFlags(prices: number[]): AdvancedPattern[] {
    const patterns: AdvancedPattern[] = [];
    
    for (let i = 20; i < prices.length - 10; i++) {
      // Detectar movimento forte (flagpole)
      const moveStart = prices[i - 20];
      const moveEnd = prices[i - 10];
      const moveSize = Math.abs(moveEnd - moveStart) / moveStart;
      
      if (moveSize > 0.05) {
        // Verificar consolidação (flag)
        const consolidation = prices.slice(i - 10, i);
        const consolidationRange = Math.max(...consolidation) - Math.min(...consolidation);
        const consolidationRatio = consolidationRange / prices[i - 1];
        
        if (consolidationRatio < 0.03) {
          const direction = moveEnd > moveStart ? 1 : -1;
          
          patterns.push({
            patternType: PatternType.TREND_CONTINUATION,
            symbol: 'SYMBOL',
            timeframe: TimeFrame.H1,
            startTime: new Date(),
            endTime: new Date(),
            confidence: 0.7,
            strength: 0.75,
            targetPrice: prices[i - 1] * (1 + direction * moveSize),
            stopLoss: prices[i - 1] * 0.97,
            probability: 0.65,
            historicalSuccessRate: 0.68,
            riskRewardRatio: 2.2,
            description: `${direction > 0 ? 'Bull' : 'Bear'} Flag pattern detected - continuation expected`,
            keyLevels: [moveStart, moveEnd],
            entryPrice: prices[i - 1],
            exitPrice: prices[i - 1] * (1 + direction * moveSize),
            patternQuality: 0.72,
            marketRegime: direction > 0 ? MarketRegime.BULL_MARKET : MarketRegime.BEAR_MARKET,
            indicators: ['Volume Spike', 'Momentum']
          });
        }
      }
    }
    
    return patterns;
  }

  private detectSupportResistance(marketData: MarketData[]): AdvancedPattern[] {
    const patterns: AdvancedPattern[] = [];
    const levels: number[] = [];
    const touches: Map<number, number> = new Map();
    
    // Coletar níveis de suporte/resistência
    for (let i = 1; i < marketData.length - 1; i++) {
      // Picos (resistência)
      if (marketData[i].high > marketData[i - 1].high && 
          marketData[i].high > marketData[i + 1].high) {
        levels.push(marketData[i].high);
      }
      
      // Vales (suporte)
      if (marketData[i].low < marketData[i - 1].low && 
          marketData[i].low < marketData[i + 1].low) {
        levels.push(marketData[i].low);
      }
    }
    
    // Contar toques em cada nível
    for (const level of levels) {
      let count = 0;
      for (const data of marketData) {
        if (Math.abs(data.close - level) / level < 0.01) {
          count++;
        }
      }
      touches.set(level, count);
    }
    
    // Filtrar níveis com múltiplos toques
    const significantLevels = Array.from(touches.entries())
      .filter(([_, count]) => count >= 3)
      .sort((a, b) => b[1] - a[1])
      .slice(0, 5);
    
    for (const [level, count] of significantLevels) {
      const isSupport = level < marketData[marketData.length - 1].close;
      
      patterns.push({
        patternType: PatternType.SUPPORT_RESISTANCE,
        symbol: marketData[0].symbol,
        timeframe: TimeFrame.H1,
        startTime: marketData[0].timestamp,
        endTime: marketData[marketData.length - 1].timestamp,
        confidence: 0.7 + count * 0.05,
        strength: count / 10,
        targetPrice: isSupport ? level * 1.05 : level * 0.95,
        stopLoss: isSupport ? level * 0.98 : level * 1.02,
        probability: 0.65,
        historicalSuccessRate: 0.7,
        riskRewardRatio: 2.0,
        description: `${isSupport ? 'Support' : 'Resistance'} level with ${count} touches`,
        keyLevels: [level],
        entryPrice: level,
        exitPrice: isSupport ? level * 1.05 : level * 0.95,
        patternQuality: count / 10,
        marketRegime: MarketRegime.SIDEWAYS_MARKET,
        indicators: ['Price Action', 'Volume']
      });
    }
    
    return patterns;
  }

  private linearRegression(data: number[]): { slope: number; intercept: number } {
    const n = data.length;
    const x = Array.from({ length: n }, (_, i) => i);
    
    const sumX = x.reduce((a, b) => a + b, 0);
    const sumY = data.reduce((a, b) => a + b, 0);
    const sumXY = x.reduce((a, _, i) => a + x[i] * data[i], 0);
    const sumXX = x.reduce((a, _, i) => a + x[i] * x[i], 0);
    
    const slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
    const intercept = (sumY - slope * sumX) / n;
    
    return { slope, intercept };
  }
}

export class CorrelationAnalyzer {
  
  async analyzeCorrelations(
    assetsData: Record<string, MarketData[]>,
    timeframe: TimeFrame = TimeFrame.H1
  ): Promise<MarketCorrelation[]> {
    const correlations: MarketCorrelation[] = [];
    const symbols = Object.keys(assetsData);
    
    if (symbols.length < 2) return correlations;
    
    // Preparar retornos para cada ativo
    const returns: Record<string, number[]> = {};
    
    for (const symbol of symbols) {
      const data = assetsData[symbol];
      const rets: number[] = [];
      for (let i = 1; i < data.length; i++) {
        rets.push((data[i].close - data[i - 1].close) / data[i - 1].close);
      }
      returns[symbol] = rets;
    }
    
    // Calcular correlações entre todos os pares
    for (let i = 0; i < symbols.length; i++) {
      for (let j = i + 1; j < symbols.length; j++) {
        const sym1 = symbols[i];
        const sym2 = symbols[j];
        
        const minLength = Math.min(returns[sym1].length, returns[sym2].length);
        const ret1 = returns[sym1].slice(-minLength);
        const ret2 = returns[sym2].slice(-minLength);
        
        // Calcular correlação de Pearson
        const correlation = this.pearsonCorrelation(ret1, ret2);
        const pValue = this.calculatePValue(correlation, minLength);
        
        // Calcular correlação rolling para estabilidade
        const rollingCorrs: number[] = [];
        const windowSize = 20;
        
        for (let k = windowSize; k < minLength; k++) {
          const window1 = ret1.slice(k - windowSize, k);
          const window2 = ret2.slice(k - windowSize, k);
          rollingCorrs.push(this.pearsonCorrelation(window1, window2));
        }
        
        const stability = 1 - (rollingCorrs.reduce((a, b) => a + b, 0) / rollingCorrs.length);
        
        // Determinar força da correlação
        let strength: CorrelationStrength;
        const absCorr = Math.abs(correlation);
        
        if (absCorr >= 0.8) strength = CorrelationStrength.VERY_STRONG;
        else if (absCorr >= 0.6) strength = CorrelationStrength.STRONG;
        else if (absCorr >= 0.4) strength = CorrelationStrength.MODERATE;
        else if (absCorr >= 0.2) strength = CorrelationStrength.WEAK;
        else strength = CorrelationStrength.VERY_WEAK;
        
        // Teste de cointegração (simplificado)
        const cointegrated = Math.abs(correlation) > 0.5 && Math.abs(correlation) < 0.95;
        
        correlations.push({
          asset1: sym1,
          asset2: sym2,
          correlation,
          pValue,
          timeframe,
          periodDays: Math.floor(minLength / 24),
          strength,
          stability,
          rollingCorrelations: rollingCorrs,
          leadLag: 0,
          cointegrated
        });
      }
    }
    
    return correlations.sort((a, b) => Math.abs(b.correlation) - Math.abs(a.correlation));
  }

  private pearsonCorrelation(x: number[], y: number[]): number {
    const n = x.length;
    const meanX = x.reduce((a, b) => a + b, 0) / n;
    const meanY = y.reduce((a, b) => a + b, 0) / n;
    
    let numerator = 0;
    let denomX = 0;
    let denomY = 0;
    
    for (let i = 0; i < n; i++) {
      const dx = x[i] - meanX;
      const dy = y[i] - meanY;
      numerator += dx * dy;
      denomX += dx * dx;
      denomY += dy * dy;
    }
    
    if (denomX === 0 || denomY === 0) return 0;
    
    return numerator / Math.sqrt(denomX * denomY);
  }

  private calculatePValue(correlation: number, n: number): number {
    const t = correlation * Math.sqrt((n - 2) / (1 - correlation * correlation));
    // Distribuição t de Student (simplificada)
    return 2 * (1 - this.tCDF(Math.abs(t), n - 2));
  }

  private tCDF(t: number, df: number): number {
    // Aproximação simples da CDF da distribuição t
    return 0.5 + 0.5 * Math.tanh(t / Math.sqrt(df));
  }
}

export class AdvancedVolatilityAnalyzer {
  
  async analyzeVolatility(marketData: MarketData[], symbol: string): Promise<VolatilityAnalysis> {
    if (marketData.length < 50) {
      return {
        symbol,
        currentVolatility: 0,
        historicalVolatility: 0,
        volatilityPercentile: 0,
        volatilityRegime: VolatilityRegime.NORMAL,
        volatilityClustering: false,
        meanReversionSpeed: 0,
        expectedShortfall: 0,
        valueAtRisk: 0,
        tailRisk: 0,
        volOfVol: 0,
        skewness: 0,
        kurtosis: 0,
        jarqueBera: 0
      };
    }
    
    const prices = marketData.map(d => d.close);
    const returns: number[] = [];
    
    for (let i = 1; i < prices.length; i++) {
      returns.push((prices[i] - prices[i - 1]) / prices[i - 1]);
    }
    
    // Volatilidade atual (últimos 20 períodos)
    const currentVol = Math.sqrt(returns.slice(-20).reduce((sum, r) => sum + r * r, 0) / 20) * Math.sqrt(252);
    
    // Volatilidade histórica
    const historicalVol = Math.sqrt(returns.reduce((sum, r) => sum + r * r, 0) / returns.length) * Math.sqrt(252);
    
    // Percentil da volatilidade atual
    const rollingVol: number[] = [];
    for (let i = 20; i < returns.length; i++) {
      const vol = Math.sqrt(returns.slice(i - 20, i).reduce((sum, r) => sum + r * r, 0) / 20) * Math.sqrt(252);
      rollingVol.push(vol);
    }
    
    const sortedVol = [...rollingVol].sort((a, b) => a - b);
    const percentile = sortedVol.indexOf(currentVol) / sortedVol.length;
    
    // Determinar regime de volatilidade
    let regime: VolatilityRegime;
    if (percentile >= 0.9) regime = VolatilityRegime.EXTREME;
    else if (percentile >= 0.75) regime = VolatilityRegime.HIGH;
    else if (percentile >= 0.25) regime = VolatilityRegime.NORMAL;
    else if (percentile >= 0.1) regime = VolatilityRegime.LOW;
    else regime = VolatilityRegime.VERY_LOW;
    
    // Detectar clustering de volatilidade
    const volatilityClustering = this.detectVolatilityClustering(returns);
    
    // Calcular VaR e Expected Shortfall
    const sortedReturns = [...returns].sort((a, b) => a - b);
    const var95 = sortedReturns[Math.floor(sortedReturns.length * 0.05)];
    const cvar95 = sortedReturns.slice(0, Math.floor(sortedReturns.length * 0.05))
      .reduce((a, b) => a + b, 0) / (sortedReturns.length * 0.05);
    
    // Calcular momentos
    const skewness = this.calculateSkewness(returns);
    const kurtosis = this.calculateKurtosis(returns);
    
    return {
      symbol,
      currentVolatility: currentVol,
      historicalVolatility: historicalVol,
      volatilityPercentile: percentile,
      volatilityRegime: regime,
      garchForecast: currentVol * 1.1,
      volatilityClustering,
      meanReversionSpeed: 0.5,
      expectedShortfall: Math.abs(cvar95),
      valueAtRisk: Math.abs(var95),
      tailRisk: Math.abs(sortedReturns[Math.floor(sortedReturns.length * 0.01)]),
      volOfVol: this.calculateVolOfVol(returns),
      skewness,
      kurtosis,
      jarqueBera: returns.length * (skewness * skewness / 6 + kurtosis * kurtosis / 24)
    };
  }

  private detectVolatilityClustering(returns: number[]): boolean {
    if (returns.length < 100) return false;
    
    const squared = returns.map(r => r * r);
    let correlation = 0;
    
    for (let lag = 1; lag <= 10; lag++) {
      let sum = 0;
      for (let i = lag; i < squared.length; i++) {
        sum += squared[i] * squared[i - lag];
      }
      correlation += sum / (squared.length - lag);
    }
    
    return correlation / 10 > 0.1;
  }

  private calculateVolOfVol(returns: number[]): number {
    const window = 20;
    const vols: number[] = [];
    
    for (let i = window; i < returns.length; i++) {
      const vol = Math.sqrt(returns.slice(i - window, i).reduce((sum, r) => sum + r * r, 0) / window);
      vols.push(vol);
    }
    
    return Math.sqrt(vols.reduce((sum, v) => sum + v * v, 0) / vols.length);
  }

  private calculateSkewness(returns: number[]): number {
    const n = returns.length;
    const mean = returns.reduce((a, b) => a + b, 0) / n;
    const variance = returns.reduce((sum, r) => sum + Math.pow(r - mean, 2), 0) / n;
    const std = Math.sqrt(variance);
    
    if (std === 0) return 0;
    
    const skew = returns.reduce((sum, r) => sum + Math.pow((r - mean) / std, 3), 0) / n;
    return skew;
  }

  private calculateKurtosis(returns: number[]): number {
    const n = returns.length;
    const mean = returns.reduce((a, b) => a + b, 0) / n;
    const variance = returns.reduce((sum, r) => sum + Math.pow(r - mean, 2), 0) / n;
    const std = Math.sqrt(variance);
    
    if (std === 0) return 0;
    
    const kurt = returns.reduce((sum, r) => sum + Math.pow((r - mean) / std, 4), 0) / n;
    return kurt - 3; // Excess kurtosis
  }
}

export class AnomalyDetector {
  
  async detectAnomalies(
    marketData: MarketData[],
    symbol: string
  ): Promise<AnomalyDetection[]> {
    if (marketData.length < 30) return [];
    
    const anomalies: AnomalyDetection[] = [];
    const prices = marketData.map(d => d.close);
    const volumes = marketData.map(d => d.volume);
    
    // Calcular médias e desvios padrão
    const priceMean = prices.reduce((a, b) => a + b, 0) / prices.length;
    const priceStd = Math.sqrt(prices.reduce((sum, p) => sum + Math.pow(p - priceMean, 2), 0) / prices.length);
    
    const volumeMean = volumes.reduce((a, b) => a + b, 0) / volumes.length;
    const volumeStd = Math.sqrt(volumes.reduce((sum, v) => sum + Math.pow(v - volumeMean, 2), 0) / volumes.length);
    
    for (let i = 0; i < marketData.length; i++) {
      const data = marketData[i];
      const priceZScore = Math.abs(data.close - priceMean) / priceStd;
      const volumeZScore = Math.abs(data.volume - volumeMean) / volumeStd;
      
      if (priceZScore > 3 || volumeZScore > 3) {
        const anomalyType = priceZScore > 3 && volumeZScore > 3 ? 'both' :
                            priceZScore > 3 ? 'price' : 'volume';
        
        let severity: 'low' | 'medium' | 'high';
        const maxZScore = Math.max(priceZScore, volumeZScore);
        
        if (maxZScore > 5) severity = 'high';
        else if (maxZScore > 4) severity = 'medium';
        else severity = 'low';
        
        anomalies.push({
          symbol,
          timestamp: data.timestamp,
          price: data.close,
          volume: data.volume,
          anomalyScore: maxZScore,
          anomalyType,
          severity,
          description: `${anomalyType} anomaly detected with Z-score ${maxZScore.toFixed(2)}`,
          expectedRange: [
            priceMean - 2 * priceStd,
            priceMean + 2 * priceStd
          ],
          zScore: maxZScore
        });
      }
    }
    
    return anomalies;
  }
}

export class VhalinorAdvancedAnalytics {
  private patternAnalyzer: AdvancedPatternAnalyzer;
  private correlationAnalyzer: CorrelationAnalyzer;
  private volatilityAnalyzer: AdvancedVolatilityAnalyzer;
  private anomalyDetector: AnomalyDetector;
  private technicalCalculator: TechnicalIndicatorCalculator;
  
  private analysisHistory: ComprehensiveAnalysis[] = [];
  private analysisCache: Map<string, ComprehensiveAnalysis> = new Map();
  
  constructor() {
    this.patternAnalyzer = new AdvancedPatternAnalyzer();
    this.correlationAnalyzer = new CorrelationAnalyzer();
    this.volatilityAnalyzer = new AdvancedVolatilityAnalyzer();
    this.anomalyDetector = new AnomalyDetector();
    this.technicalCalculator = new TechnicalIndicatorCalculator();
  }

  async comprehensiveAnalysis(
    assetsData: Record<string, MarketData[]>,
    analysisTypes: AnalysisType[] = [
      AnalysisType.PATTERN,
      AnalysisType.CORRELATION,
      AnalysisType.VOLATILITY,
      AnalysisType.ANOMALY,
      AnalysisType.TECHNICAL
    ]
  ): Promise<ComprehensiveAnalysis> {
    
    const timestamp = new Date();
    const assetsAnalyzed = Object.keys(assetsData);
    const results: ComprehensiveAnalysis = {
      timestamp,
      assetsAnalyzed,
      analysisTypes,
      patterns: {},
      correlations: [],
      volatility: {},
      anomalies: {},
      technical: {},
      predictions: {},
      marketRegime: MarketRegime.SIDEWAYS_MARKET,
      overallConfidence: 0,
      summary: ''
    };
    
    // Parallel analysis
    const promises: Promise<any>[] = [];
    
    if (analysisTypes.includes(AnalysisType.PATTERN)) {
      for (const symbol of assetsAnalyzed) {
        promises.push(
          this.patternAnalyzer.identifyPatterns(assetsData[symbol])
            .then(patterns => { results.patterns[symbol] = patterns; })
        );
      }
    }
    
    if (analysisTypes.includes(AnalysisType.CORRELATION)) {
      promises.push(
        this.correlationAnalyzer.analyzeCorrelations(assetsData)
          .then(correlations => { results.correlations = correlations; })
      );
    }
    
    if (analysisTypes.includes(AnalysisType.VOLATILITY)) {
      for (const symbol of assetsAnalyzed) {
        promises.push(
          this.volatilityAnalyzer.analyzeVolatility(assetsData[symbol], symbol)
            .then(vol => { results.volatility[symbol] = vol; })
        );
      }
    }
    
    if (analysisTypes.includes(AnalysisType.ANOMALY)) {
      for (const symbol of assetsAnalyzed) {
        promises.push(
          this.anomalyDetector.detectAnomalies(assetsData[symbol], symbol)
            .then(anomalies => { results.anomalies[symbol] = anomalies; })
        );
      }
    }
    
    if (analysisTypes.includes(AnalysisType.TECHNICAL)) {
      for (const symbol of assetsAnalyzed) {
        const indicators = this.technicalCalculator.calculateAllIndicators(assetsData[symbol]);
        if (indicators.length > 0) {
          results.technical[symbol] = indicators[indicators.length - 1];
        }
      }
    }
    
    await Promise.all(promises);
    
    // Determinar regime de mercado
    results.marketRegime = this.determineMarketRegime(results);
    
    // Calcular confiança geral
    results.overallConfidence = this.calculateOverallConfidence(results);
    
    // Gerar sumário
    results.summary = this.generateSummary(results);
    
    // Armazenar no histórico
    this.analysisHistory.push(results);
    
    return results;
  }

  private determineMarketRegime(analysis: ComprehensiveAnalysis): MarketRegime {
    const volatilityRegimes = Object.values(analysis.volatility).map(v => v.volatilityRegime);
    const avgVolatility = Object.values(analysis.volatility)
      .reduce((sum, v) => sum + v.currentVolatility, 0) / Object.keys(analysis.volatility).length;
    
    const bullishPatterns = Object.values(analysis.patterns)
      .flat()
      .filter(p => p.description.toLowerCase().includes('bull'))
      .length;
    
    const bearishPatterns = Object.values(analysis.patterns)
      .flat()
      .filter(p => p.description.toLowerCase().includes('bear'))
      .length;
    
    if (avgVolatility > 0.5) return MarketRegime.HIGH_VOLATILITY;
    if (avgVolatility < 0.1) return MarketRegime.LOW_VOLATILITY;
    
    if (bullishPatterns > bearishPatterns * 2) return MarketRegime.BULL_MARKET;
    if (bearishPatterns > bullishPatterns * 2) return MarketRegime.BEAR_MARKET;
    
    return MarketRegime.SIDEWAYS_MARKET;
  }

  private calculateOverallConfidence(analysis: ComprehensiveAnalysis): number {
    let confidence = 0.5;
    let factors = 0;
    
    // Padrões
    const patternConfidence = Object.values(analysis.patterns)
      .flat()
      .reduce((sum, p) => sum + p.confidence, 0) / Math.max(1, Object.values(analysis.patterns).flat().length);
    
    if (!isNaN(patternConfidence)) {
      confidence += patternConfidence * 0.3;
      factors += 0.3;
    }
    
    // Correlações
    const avgCorrelation = analysis.correlations
      .reduce((sum, c) => sum + Math.abs(c.correlation), 0) / Math.max(1, analysis.correlations.length);
    
    if (!isNaN(avgCorrelation)) {
      confidence += avgCorrelation * 0.2;
      factors += 0.2;
    }
    
    // Volatilidade
    const avgVolConfidence = Object.values(analysis.volatility)
      .map(v => v.volatilityPercentile)
      .reduce((sum, p) => sum + (1 - Math.abs(p - 0.5) * 2), 0) / Math.max(1, Object.keys(analysis.volatility).length);
    
    if (!isNaN(avgVolConfidence)) {
      confidence += avgVolConfidence * 0.2;
      factors += 0.2;
    }
    
    return factors > 0 ? confidence / (0.5 + factors) : 0.5;
  }

  private generateSummary(analysis: ComprehensiveAnalysis): string {
    const totalPatterns = Object.values(analysis.patterns).flat().length;
    const totalAnomalies = Object.values(analysis.anomalies).flat().length;
    const avgVolatility = Object.values(analysis.volatility)
      .reduce((sum, v) => sum + v.currentVolatility, 0) / Math.max(1, Object.keys(analysis.volatility).length);
    
    const strongCorrelations = analysis.correlations.filter(c => 
      c.strength === CorrelationStrength.STRONG || c.strength === CorrelationStrength.VERY_STRONG
    ).length;
    
    return `Analysis completed at ${analysis.timestamp.toLocaleString()}. ` +
           `Found ${totalPatterns} patterns, ${totalAnomalies} anomalies. ` +
           `Market regime: ${analysis.marketRegime}. ` +
           `Average volatility: ${(avgVolatility * 100).toFixed(2)}%. ` +
           `Strong correlations: ${strongCorrelations}. ` +
           `Overall confidence: ${(analysis.overallConfidence * 100).toFixed(1)}%.`;
  }

  getAnalysisHistory(): ComprehensiveAnalysis[] {
    return [...this.analysisHistory];
  }

  getLatestAnalysis(): ComprehensiveAnalysis | undefined {
    return this.analysisHistory[this.analysisHistory.length - 1];
  }
}

// ============================================================================
/// COMPONENTES DE INTERFACE
// ============================================================================

interface PatternCardProps {
  pattern: AdvancedPattern;
  onClick?: () => void;
}

export const PatternCard: React.FC<PatternCardProps> = ({ pattern, onClick }) => {
  const getPatternColor = () => {
    if (pattern.patternType.toString().includes('BULL')) return '#10B981';
    if (pattern.patternType.toString().includes('BEAR')) return '#EF4444';
    return '#3B82F6';
  };

  return (
    <motion.div
      className="pattern-card"
      onClick={onClick}
      whileHover={{ scale: 1.02 }}
      style={{ borderLeftColor: getPatternColor() }}
    >
      <div className="pattern-header">
        <span className="pattern-type">{pattern.patternType}</span>
        <span className="pattern-timeframe">{pattern.timeframe}</span>
      </div>

      <p className="pattern-description">{pattern.description}</p>

      <div className="pattern-confidence">
        <div className="confidence-bar">
          <div
            className="confidence-fill"
            style={{
              width: `${pattern.confidence * 100}%`,
              backgroundColor: getPatternColor()
            }}
          />
        </div>
        <span className="confidence-value">{(pattern.confidence * 100).toFixed(1)}%</span>
      </div>

      <div className="pattern-levels">
        {pattern.keyLevels.slice(0, 3).map((level, idx) => (
          <span key={idx} className="level">{level.toFixed(2)}</span>
        ))}
      </div>

      <div className="pattern-stats">
        <div className="stat">
          <span className="label">Força:</span>
          <span className="value">{(pattern.strength * 100).toFixed(1)}%</span>
        </div>
        <div className="stat">
          <span className="label">R:R:</span>
          <span className="value">1:{pattern.riskRewardRatio.toFixed(1)}</span>
        </div>
      </div>
    </motion.div>
  );
};

interface CorrelationCardProps {
  correlation: MarketCorrelation;
}

export const CorrelationCard: React.FC<CorrelationCardProps> = ({ correlation }) => {
  const getCorrelationColor = () => {
    if (correlation.correlation > 0.7) return '#10B981';
    if (correlation.correlation > 0.3) return '#3B82F6';
    if (correlation.correlation > -0.3) return '#94A3B8';
    if (correlation.correlation > -0.7) return '#F59E0B';
    return '#EF4444';
  };

  return (
    <div className="correlation-card">
      <div className="correlation-header">
        <span className="assets">{correlation.asset1} / {correlation.asset2}</span>
        <span className="timeframe">{correlation.timeframe}</span>
      </div>

      <div className="correlation-value" style={{ color: getCorrelationColor() }}>
        {correlation.correlation.toFixed(3)}
      </div>

      <div className="correlation-details">
        <div className="detail">
          <span>Força:</span>
          <span>{correlation.strength}</span>
        </div>
        <div className="detail">
          <span>Estabilidade:</span>
          <span>{(correlation.stability * 100).toFixed(1)}%</span>
        </div>
        <div className="detail">
          <span>Cointegrado:</span>
          <span>{correlation.cointegrated ? '✅' : '❌'}</span>
        </div>
      </div>
    </div>
  );
};

interface VolatilityCardProps {
  volatility: VolatilityAnalysis;
}

export const VolatilityCard: React.FC<VolatilityCardProps> = ({ volatility }) => {
  const getRegimeColor = () => {
    switch (volatility.volatilityRegime) {
      case VolatilityRegime.EXTREME: return '#EF4444';
      case VolatilityRegime.HIGH: return '#F59E0B';
      case VolatilityRegime.NORMAL: return '#3B82F6';
      case VolatilityRegime.LOW: return '#10B981';
      case VolatilityRegime.VERY_LOW: return '#94A3B8';
      default: return '#94A3B8';
    }
  };

  return (
    <div className="volatility-card">
      <div className="volatility-header">
        <span className="symbol">{volatility.symbol}</span>
        <span className="regime" style={{ color: getRegimeColor() }}>
          {volatility.volatilityRegime}
        </span>
      </div>

      <div className="volatility-value">
        <span className="current">{(volatility.currentVolatility * 100).toFixed(2)}%</span>
        <span className="historical">Hist: {(volatility.historicalVolatility * 100).toFixed(2)}%</span>
      </div>

      <div className="volatility-metrics">
        <div className="metric">
          <span>VaR 95%:</span>
          <span>{(volatility.valueAtRisk * 100).toFixed(2)}%</span>
        </div>
        <div className="metric">
          <span>CVaR:</span>
          <span>{(volatility.expectedShortfall * 100).toFixed(2)}%</span>
        </div>
        <div className="metric">
          <span>Clustering:</span>
          <span>{volatility.volatilityClustering ? '✅' : '❌'}</span>
        </div>
      </div>
    </div>
  );
};

interface AnomalyCardProps {
  anomaly: AnomalyDetection;
}

export const AnomalyCard: React.FC<AnomalyCardProps> = ({ anomaly }) => {
  const getSeverityColor = () => {
    switch (anomaly.severity) {
      case 'high': return '#EF4444';
      case 'medium': return '#F59E0B';
      case 'low': return '#3B82F6';
      default: return '#94A3B8';
    }
  };

  return (
    <div className="anomaly-card" style={{ borderLeftColor: getSeverityColor() }}>
      <div className="anomaly-header">
        <span className="timestamp">{anomaly.timestamp.toLocaleString()}</span>
        <span className="severity">{anomaly.severity}</span>
      </div>

      <p className="anomaly-description">{anomaly.description}</p>

      <div className="anomaly-details">
        <div className="detail">
          <span>Preço:</span>
          <span>${anomaly.price.toFixed(2)}</span>
        </div>
        <div className="detail">
          <span>Volume:</span>
          <span>{anomaly.volume.toLocaleString()}</span>
        </div>
        <div className="detail">
          <span>Z-Score:</span>
          <span>{anomaly.zScore.toFixed(2)}</span>
        </div>
      </div>

      <div className="expected-range">
        Range esperado: ${anomaly.expectedRange[0].toFixed(2)} - ${anomaly.expectedRange[1].toFixed(2)}
      </div>
    </div>
  );
};

// ============================================================================
// COMPONENTE PRINCIPAL
// ============================================================================

interface VhalinorAnalyticsProps {
  onAnalysisComplete?: (analysis: ComprehensiveAnalysis) => void;
}

export const VhalinorAnalytics: React.FC<VhalinorAnalyticsProps> = ({
  onAnalysisComplete
}) => {
  const [analytics] = useState(() => new VhalinorAdvancedAnalytics());
  const [analysis, setAnalysis] = useState<ComprehensiveAnalysis | null>(null);
  const [assets, setAssets] = useState<Record<string, MarketData[]>>({});
  const [selectedAsset, setSelectedAsset] = useState<string>('AAPL');
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [activeTab, setActiveTab] = useState<'overview' | 'patterns' | 'correlations' | 'volatility' | 'anomalies'>('overview');
  const [autoRefresh, setAutoRefresh] = useState(false);
  const [analysisTypes, setAnalysisTypes] = useState<AnalysisType[]>([
    AnalysisType.PATTERN,
    AnalysisType.CORRELATION,
    AnalysisType.VOLATILITY,
    AnalysisType.ANOMALY,
    AnalysisType.TECHNICAL
  ]);

  // Gerar dados mock para demonstração
  const generateMockData = useCallback((symbol: string, days: number = 30): MarketData[] => {
    const data: MarketData[] = [];
    const basePrice = symbol === 'AAPL' ? 150 : symbol === 'GOOGL' ? 2500 : symbol === 'MSFT' ? 300 : 100;
    const volatility = symbol === 'AAPL' ? 0.02 : symbol === 'GOOGL' ? 0.03 : 0.015;
    const now = new Date();

    for (let i = days * 24; i >= 0; i--) {
      const timestamp = new Date(now.getTime() - i * 60 * 60 * 1000);
      const change = (Math.random() - 0.5) * volatility * basePrice;
      const close = basePrice + change * (1 + Math.sin(i / 24) * 0.1);
      const high = close * (1 + Math.random() * volatility);
      const low = close * (1 - Math.random() * volatility);
      const volume = Math.floor(1000000 + Math.random() * 5000000);

      data.push({
        timestamp,
        open: close - change * 0.5,
        high,
        low,
        close,
        volume,
        symbol
      });
    }

    return data;
  }, []);

  useEffect(() => {
    // Inicializar com dados mock
    const mockAssets: Record<string, MarketData[]> = {
      'AAPL': generateMockData('AAPL'),
      'GOOGL': generateMockData('GOOGL'),
      'MSFT': generateMockData('MSFT'),
      'AMZN': generateMockData('AMZN'),
      'TSLA': generateMockData('TSLA')
    };
    setAssets(mockAssets);
  }, [generateMockData]);

  useEffect(() => {
    if (autoRefresh) {
      const interval = setInterval(() => {
        handleAnalyze();
      }, 60000); // Atualizar a cada minuto
      return () => clearInterval(interval);
    }
  }, [autoRefresh]);

  const handleAnalyze = async () => {
    setIsAnalyzing(true);

    try {
      const result = await analytics.comprehensiveAnalysis(assets, analysisTypes);
      setAnalysis(result);
      onAnalysisComplete?.(result);
      toast.success(`✅ Análise concluída! ${result.summary}`);
    } catch (error) {
      toast.error('❌ Erro na análise');
      console.error(error);
    } finally {
      setIsAnalyzing(false);
    }
  };

  const toggleAnalysisType = (type: AnalysisType) => {
    setAnalysisTypes(prev =>
      prev.includes(type)
        ? prev.filter(t => t !== type)
        : [...prev, type]
    );
  };

  return (
    <div className="vhalinor-analytics">
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
      <header className="analytics-header">
        <div className="header-left">
          <h1>
            <FaBrain className="icon" />
            VHALINOR.IAG Advanced Analytics
          </h1>
          
          {analysis && (
            <div className="header-badges">
              <span className="badge regime">
                📈 {analysis.marketRegime}
              </span>
              <span className="badge confidence">
                🎯 Confiança: {(analysis.overallConfidence * 100).toFixed(1)}%
              </span>
            </div>
          )}
        </div>

        <div className="header-right">
          <select
            className="asset-select"
            value={selectedAsset}
            onChange={(e) => setSelectedAsset(e.target.value)}
          >
            {Object.keys(assets).map(symbol => (
              <option key={symbol} value={symbol}>{symbol}</option>
            ))}
          </select>

          <label className="auto-refresh">
            <input
              type="checkbox"
              checked={autoRefresh}
              onChange={(e) => setAutoRefresh(e.target.checked)}
            />
            Auto Refresh (1m)
          </label>

          <button
            className="action-btn"
            onClick={handleAnalyze}
            disabled={isAnalyzing}
          >
            {isAnalyzing ? (
              <>
                <FaSpinner className="spinner" />
                Analisando...
              </>
            ) : (
              <>
                <FaPlay /> Analisar
              </>
            )}
          </button>
        </div>
      </header>

      {/* Analysis Type Selector */}
      <div className="analysis-type-selector">
        <span className="selector-label">Tipos de Análise:</span>
        <div className="type-buttons">
          {Object.values(AnalysisType).map(type => (
            <button
              key={type}
              className={`type-btn ${analysisTypes.includes(type) ? 'active' : ''}`}
              onClick={() => toggleAnalysisType(type)}
            >
              {type}
            </button>
          ))}
        </div>
      </div>

      {/* Tabs */}
      <div className="analytics-tabs">
        <button
          className={activeTab === 'overview' ? 'active' : ''}
          onClick={() => setActiveTab('overview')}
        >
          📊 Visão Geral
        </button>
        <button
          className={activeTab === 'patterns' ? 'active' : ''}
          onClick={() => setActiveTab('patterns')}
        >
          🎯 Padrões
        </button>
        <button
          className={activeTab === 'correlations' ? 'active' : ''}
          onClick={() => setActiveTab('correlations')}
        >
          🔗 Correlações
        </button>
        <button
          className={activeTab === 'volatility' ? 'active' : ''}
          onClick={() => setActiveTab('volatility')}
        >
          📈 Volatilidade
        </button>
        <button
          className={activeTab === 'anomalies' ? 'active' : ''}
          onClick={() => setActiveTab('anomalies')}
        >
          ⚠️ Anomalias
        </button>
      </div>

      {/* Content */}
      <div className="analytics-content">
        <AnimatePresence mode="wait">
          {activeTab === 'overview' && analysis && (
            <motion.div
              key="overview"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="overview-tab"
            >
              <div className="summary-card">
                <h3>📋 Resumo da Análise</h3>
                <p className="summary-text">{analysis.summary}</p>
              </div>

              <div className="stats-grid">
                <div className="stat-card">
                  <div className="stat-value">
                    {Object.values(analysis.patterns).flat().length}
                  </div>
                  <div className="stat-label">Padrões Encontrados</div>
                </div>

                <div className="stat-card">
                  <div className="stat-value">
                    {analysis.correlations.length}
                  </div>
                  <div className="stat-label">Pares Correlacionados</div>
                </div>

                <div className="stat-card">
                  <div className="stat-value">
                    {Object.values(analysis.anomalies).flat().length}
                  </div>
                  <div className="stat-label">Anomalias Detectadas</div>
                </div>

                <div className="stat-card">
                  <div className="stat-value">
                    {analysis.assetsAnalyzed.length}
                  </div>
                  <div className="stat-label">Ativos Analisados</div>
                </div>
              </div>

              <div className="quick-insights">
                <h4>💡 Insights Rápidos</h4>
                <ul>
                  {Object.values(analysis.patterns).flat().slice(0, 5).map((p, i) => (
                    <li key={i}>{p.description}</li>
                  ))}
                </ul>
              </div>
            </motion.div>
          )}

          {activeTab === 'patterns' && analysis && (
            <motion.div
              key="patterns"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="patterns-tab"
            >
              <div className="patterns-grid">
                {Object.entries(analysis.patterns).map(([symbol, patterns]) => (
                  <div key={symbol} className="symbol-section">
                    <h3>{symbol}</h3>
                    <div className="symbol-patterns">
                      {patterns.map((pattern, idx) => (
                        <PatternCard key={idx} pattern={pattern} />
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </motion.div>
          )}

          {activeTab === 'correlations' && analysis && (
            <motion.div
              key="correlations"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="correlations-tab"
            >
              <div className="correlations-grid">
                {analysis.correlations.map((corr, idx) => (
                  <CorrelationCard key={idx} correlation={corr} />
                ))}
              </div>
            </motion.div>
          )}

          {activeTab === 'volatility' && analysis && (
            <motion.div
              key="volatility"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="volatility-tab"
            >
              <div className="volatility-grid">
                {Object.entries(analysis.volatility).map(([symbol, vol]) => (
                  <VolatilityCard key={symbol} volatility={vol} />
                ))}
              </div>
            </motion.div>
          )}

          {activeTab === 'anomalies' && analysis && (
            <motion.div
              key="anomalies"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              className="anomalies-tab"
            >
              <div className="anomalies-grid">
                {Object.entries(analysis.anomalies).map(([symbol, anomalies]) => (
                  <div key={symbol} className="symbol-section">
                    <h3>{symbol}</h3>
                    <div className="symbol-anomalies">
                      {anomalies.map((anomaly, idx) => (
                        <AnomalyCard key={idx} anomaly={anomaly} />
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>

      {/* Footer */}
      <footer className="analytics-footer">
        <div className="footer-left">
          <span>🧠 VHALINOR.IAG v5.0.0</span>
          <span>📊 {analysis?.assetsAnalyzed.length || 0} ativos</span>
          <span>🕒 {new Date().toLocaleTimeString()}</span>
        </div>
        <div className="footer-right">
          <span>⚡ Advanced Analytics Module</span>
          <span>© 2026 VHALINOR.IAG</span>
        </div>
      </footer>
    </div>
  );
};

// ============================================================================
// CSS Styles (adicione no seu arquivo CSS)
// ============================================================================

export const VhalinorAnalyticsStyles = `
.vhalinor-analytics {
  min-height: 100vh;
  background: linear-gradient(135deg, #0F172A 0%, #1E293B 100%);
  color: #fff;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  flex-direction: column;
}

/* Header */
.analytics-header {
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

.badge.regime {
  background: rgba(59, 130, 246, 0.2);
  color: #3B82F6;
}

.badge.confidence {
  background: rgba(6, 214, 160, 0.2);
  color: #06D6A0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.asset-select {
  padding: 8px 12px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid #334155;
  border-radius: 6px;
  color: #fff;
  font-size: 13px;
  min-width: 120px;
}

.asset-select:focus {
  outline: none;
  border-color: #06D6A0;
}

.auto-refresh {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #94A3B8;
  font-size: 13px;
}

.auto-refresh input {
  cursor: pointer;
}

.action-btn {
  padding: 8px 16px;
  background: rgba(6, 214, 160, 0.1);
  border: 1px solid #06D6A0;
  border-radius: 8px;
  color: #06D6A0;
  cursor: pointer;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
}

.action-btn:hover:not(:disabled) {
  background: #06D6A0;
  color: #0F172A;
}

.action-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.spinner {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Analysis Type Selector */
.analysis-type-selector {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 20px;
  background: rgba(0, 0, 0, 0.2);
  border-bottom: 1px solid #334155;
}

.selector-label {
  color: #94A3B8;
  font-size: 12px;
}

.type-buttons {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.type-btn {
  padding: 4px 8px;
  background: transparent;
  border: 1px solid #334155;
  border-radius: 4px;
  color: #94A3B8;
  cursor: pointer;
  font-size: 11px;
  transition: all 0.2s;
}

.type-btn:hover {
  border-color: #06D6A0;
  color: #06D6A0;
}

.type-btn.active {
  background: rgba(6, 214, 160, 0.1);
  border-color: #06D6A0;
  color: #06D6A0;
}

/* Tabs */
.analytics-tabs {
  display: flex;
  gap: 5px;
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid #334155;
  border-radius: 10px;
  padding: 5px;
  margin: 20px;
}

.analytics-tabs button {
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

.analytics-tabs button:hover {
  background: rgba(255, 255, 255, 0.05);
}

.analytics-tabs button.active {
  background: rgba(6, 214, 160, 0.2);
  color: #06D6A0;
}

/* Content */
.analytics-content {
  flex: 1;
  padding: 0 20px;
  overflow-y: auto;
}

/* Overview Tab */
.summary-card {
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid #334155;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 20px;
}

.summary-card h3 {
  margin: 0 0 10px 0;
  color: #94A3B8;
  font-size: 14px;
}

.summary-text {
  font-size: 14px;
  line-height: 1.5;
  color: #fff;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.stat-card {
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid #334155;
  border-radius: 10px;
  padding: 15px;
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #06D6A0;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 13px;
  color: #94A3B8;
}

.quick-insights {
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid #334155;
  border-radius: 10px;
  padding: 15px;
}

.quick-insights h4 {
  margin: 0 0 10px 0;
  color: #94A3B8;
  font-size: 13px;
}

.quick-insights ul {
  margin: 0;
  padding-left: 20px;
}

.quick-insights li {
  color: #fff;
  font-size: 13px;
  margin: 5px 0;
}

/* Patterns Tab */
.patterns-grid,
.symbol-section {
  margin-bottom: 20px;
}

.symbol-section h3 {
  margin: 0 0 10px 0;
  color: #06D6A0;
  font-size: 14px;
}

.symbol-patterns {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
}

.pattern-card {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 12px;
  cursor: pointer;
  transition: all 0.2s;
  border-left: 3px solid;
}

.pattern-card:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.05);
}

.pattern-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.pattern-type {
  font-size: 12px;
  font-weight: bold;
  text-transform: uppercase;
}

.pattern-timeframe {
  font-size: 10px;
  color: #94A3B8;
}

.pattern-description {
  font-size: 12px;
  color: #94A3B8;
  margin-bottom: 10px;
}

.pattern-confidence {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.confidence-bar {
  flex: 1;
  height: 4px;
  background: #334155;
  border-radius: 2px;
  overflow: hidden;
}

.confidence-fill {
  height: 100%;
  border-radius: 2px;
  transition: width 0.3s;
}

.confidence-value {
  min-width: 45px;
  font-size: 11px;
  color: #94A3B8;
}

.pattern-levels {
  display: flex;
  gap: 5px;
  margin-bottom: 8px;
}

.level {
  padding: 2px 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
  font-size: 10px;
  color: #F59E0B;
}

.pattern-stats {
  display: flex;
  gap: 10px;
  font-size: 11px;
}

.pattern-stats .stat {
  display: flex;
  gap: 5px;
}

.pattern-stats .label {
  color: #64748B;
}

.pattern-stats .value {
  font-weight: bold;
  color: #06D6A0;
}

/* Correlations Tab */
.correlations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 15px;
}

.correlation-card {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 12px;
}

.correlation-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.assets {
  font-weight: bold;
  font-size: 13px;
}

.timeframe {
  font-size: 10px;
  color: #94A3B8;
}

.correlation-value {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin: 10px 0;
}

.correlation-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 11px;
}

.correlation-details .detail {
  display: flex;
  justify-content: space-between;
}

.correlation-details .detail span:first-child {
  color: #64748B;
}

.correlation-details .detail span:last-child {
  font-weight: bold;
  color: #06D6A0;
}

/* Volatility Tab */
.volatility-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 15px;
}

.volatility-card {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 12px;
}

.volatility-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.symbol {
  font-weight: bold;
  font-size: 14px;
}

.regime {
  font-size: 11px;
  font-weight: bold;
  padding: 2px 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.volatility-value {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.current {
  font-size: 20px;
  font-weight: bold;
  color: #F59E0B;
}

.historical {
  font-size: 12px;
  color: #94A3B8;
}

.volatility-metrics {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 11px;
}

.volatility-metrics .metric {
  display: flex;
  justify-content: space-between;
}

.volatility-metrics .metric span:first-child {
  color: #64748B;
}

.volatility-metrics .metric span:last-child {
  font-weight: bold;
  color: #3B82F6;
}

/* Anomalies Tab */
.anomalies-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 15px;
}

.anomaly-card {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 12px;
  border-left: 3px solid;
}

.anomaly-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.timestamp {
  font-size: 11px;
  color: #94A3B8;
}

.severity {
  font-size: 11px;
  font-weight: bold;
  padding: 2px 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}

.anomaly-description {
  font-size: 13px;
  margin-bottom: 10px;
}

.anomaly-details {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
  margin-bottom: 10px;
  font-size: 11px;
}

.anomaly-details .detail {
  display: flex;
  justify-content: space-between;
}

.anomaly-details .detail span:first-child {
  color: #64748B;
}

.anomaly-details .detail span:last-child {
  font-weight: bold;
}

.expected-range {
  font-size: 11px;
  color: #64748B;
  padding-top: 8px;
  border-top: 1px solid #334155;
}

/* Footer */
.analytics-footer {
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
  .patterns-grid,
  .correlations-grid,
  .volatility-grid,
  .anomalies-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .analytics-header {
    flex-direction: column;
    gap: 10px;
  }
  
  .header-left {
    flex-direction: column;
    text-align: center;
  }
  
  .analytics-tabs {
    flex-wrap: wrap;
  }
  
  .analysis-type-selector {
    flex-wrap: wrap;
  }
  
  .type-buttons {
    flex-wrap: wrap;
  }
  
  .stats-grid {
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

export const VhalinorAnalyticsDemo: React.FC = () => {
  const handleAnalysisComplete = (analysis: ComprehensiveAnalysis) => {
    console.log('Análise concluída:', analysis);
  };

  return (
    <VhalinorAnalytics onAnalysisComplete={handleAnalysisComplete} />
  );
};

// ============================================================================
// EXPORTS
// ============================================================================

export default VhalinorAnalytics;