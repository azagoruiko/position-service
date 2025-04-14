package zaico.client.binance.dto.mapper;

import zaico.client.binance.dto.RawFuturesPosition;
import zaico.model.FuturesSnapshot;
import zaico.model.MarketType;

import java.math.BigDecimal;
import java.time.Instant;

public class BinancePositionMapper {
    public static FuturesSnapshot fromFutures(RawFuturesPosition raw, MarketType type) {
        return new FuturesSnapshot(
                raw.symbol(),
                type,
                new BigDecimal(raw.positionAmt()),
                new BigDecimal(raw.entryPrice()),
                new BigDecimal(raw.markPrice()),
                new BigDecimal(raw.unRealizedProfit()),
                Integer.parseInt(raw.leverage()),
                raw.positionSide(),
                Instant.ofEpochMilli(raw.updateTime())
        );
    }
}

