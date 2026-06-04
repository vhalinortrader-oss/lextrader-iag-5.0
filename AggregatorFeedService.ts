
class AggregatorFeedService {
  private static instance: AggregatorFeedService;
  public static getInstance(): AggregatorFeedService {
    if (!AggregatorFeedService.instance) AggregatorFeedService.instance = new AggregatorFeedService();
    return AggregatorFeedService.instance;
  }

  public getGlobalStats() {
    return {
      platforms: ['CoinMarketCap', 'CoinGecko', 'Messari'],
      globalVolume: 'High',
      dominanceUpdate: true,
      lastSync: Date.now()
    };
  }
}
export const aggregatorFeedService = AggregatorFeedService.getInstance();
