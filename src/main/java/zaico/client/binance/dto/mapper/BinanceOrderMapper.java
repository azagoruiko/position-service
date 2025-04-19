package zaico.client.binance.dto.mapper;

import zaico.client.binance.dto.FuturesOrder;
import zaico.client.binance.dto.SpotOrder;
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
                o.price(),
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
                o.activatePrice(),
                o.priceRate(),
                o.origQty(),
                o.executedQty(),
                o.cumBase(),
                Instant.ofEpochMilli(o.updateTime()),
                marketType
        );
    }
}
