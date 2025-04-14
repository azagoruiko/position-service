package zaico.client.binance.dto.mapper;

import zaico.client.binance.dto.FuturesTrade;
import zaico.client.binance.dto.SpotTrade;
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

    public static Trade fromFutures(FuturesTrade t, MarketType mType) {
        return new Trade(
                t.symbol(),
                mType,
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

