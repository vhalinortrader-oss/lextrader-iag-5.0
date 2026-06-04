
class TickDataService {
  private static instance: TickDataService;
  public static getInstance(): TickDataService {
    if (!TickDataService.instance) TickDataService.instance = new TickDataService();
    return TickDataService.instance;
  }

  public getTickFeed() {
    return {
      updateFrequency: 'ms',
      precision: 8,
      noiseFiltering: 'Kalman-Active',
      status: 'STREAMING'
    };
  }
}
export const tickDataService = TickDataService.getInstance();
