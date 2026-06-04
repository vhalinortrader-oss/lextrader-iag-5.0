
class ReserveFundsService {
  private static instance: ReserveFundsService;
  public static getInstance(): ReserveFundsService {
    if (!ReserveFundsService.instance) ReserveFundsService.instance = new ReserveFundsService();
    return ReserveFundsService.instance;
  }

  public getFundMetrics() {
    return {
      insuranceFund: 12450.32,
      protectionRatio: 0.985,
      liquidityStatus: 'LOCKED',
      rebalancing: 'STANDBY',
      lastInjection: Date.now()
    };
  }
}
export const reserveFundsService = ReserveFundsService.getInstance();
