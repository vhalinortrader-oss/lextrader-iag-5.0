
class OHLCVDataService {
  private static instance: OHLCVDataService;
  public static getInstance(): OHLCVDataService {
    if (!OHLCVDataService.instance) OHLCVDataService.instance = new OHLCVDataService();
    return OHLCVDataService.instance;
  }

  public getCandleData(timeframe: string = '1m') {
    return {
      timeframe,
      integrity: 0.9999,
      gapFilling: 'Auto-Interpolate',
      lastSync: Date.now()
    };
  }
}
export const ohlcvDataService = OHLCVDataService.getInstance();
