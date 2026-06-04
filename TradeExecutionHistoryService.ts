
class TradeExecutionHistoryService {
  private static instance: TradeExecutionHistoryService;
  public static getInstance(): TradeExecutionHistoryService {
    if (!TradeExecutionHistoryService.instance) TradeExecutionHistoryService.instance = new TradeExecutionHistoryService();
    return TradeExecutionHistoryService.instance;
  }

  public getExecutionFlow() {
    return {
      tapeReading: 'Active',
      largeTradeAlert: true,
      buySellPressure: 0.62,
      lastTradeTimestamp: Date.now()
    };
  }
}
export const tradeExecutionHistoryService = TradeExecutionHistoryService.getInstance();
