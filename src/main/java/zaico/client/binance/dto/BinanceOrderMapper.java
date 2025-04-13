package zaico.client.binance.dto;

import zaico.model.MarketType;
import zaico.model.Order;

import java.time.Instant;

public class BinanceOrderMapper {

    public static Order fromSpot(SpotOrder o) {
        return new Order(
                o.symbol(),
                o.orderId(),
                o.side(),
                o.type(),
                o.status(),
                o.price(),
                o.origQty(),
                o.executedQty(),
                o.cummulativeQuoteQty(),
                Instant.ofEpochMilli(o.updateTime()),
                MarketType.SPOT
        );
    }

    public static Order fromFutures(FuturesOrder o, MarketType marketType) {
        return new Order(
                o.symbol(),
                o.orderId(),
                o.side(),
                o.type(),
                o.status(),
                o.price(),
                o.origQty(),
                o.executedQty(),
                o.cumBase(),
                Instant.ofEpochMilli(o.updateTime()),
                marketType
        );
    }
}
