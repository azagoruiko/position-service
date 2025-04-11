package zaico.client.binance.dto;

import zaico.model.MarketType;
import zaico.model.Trade;
import zaico.model.TradeSide;

public class BinanceTradeMapper {
    public static Trade fromSpot(SpotTrade t) {
        return new Trade(
                t.symbol(),
                MarketType.SPOT,
                t.isBuyer() ? TradeSide.BUY : TradeSide.SELL,
                t.qty(),
                t.price(),
                t.quoteQty(),
                t.commission(),
                t.commissionAsset(),
                t.time(),
                String.valueOf(t.orderId())
        );
    }

    public static Trade fromFutures(FuturesTrade t) {
        return new Trade(
                t.symbol(),
                MarketType.FUTURES,
                "BUY".equalsIgnoreCase(t.side()) ? TradeSide.BUY : TradeSide.SELL,
                t.qty(),
                t.price(),
                t.quoteQty(),
                t.commission(),
                t.commissionAsset(),
                t.time(),
                String.valueOf(t.orderId())
        );
    }
}

