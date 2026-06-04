
class OrderBookDepthService {
  private static instance: OrderBookDepthService;
  public static getInstance(): OrderBookDepthService {
    if (!OrderBookDepthService.instance) OrderBookDepthService.instance = new OrderBookDepthService();
    return OrderBookDepthService.instance;
  }

  public getBookDepth() {
    return {
      levels: 100,
      imbalanceRatio: 0.15,
      spoofingDetection: 'Enabled',
      wallResistance: 'Detected'
    };
  }
}
export const orderBookDepthService = OrderBookDepthService.getInstance();
