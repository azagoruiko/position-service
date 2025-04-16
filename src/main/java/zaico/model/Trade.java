package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable
public record Trade(
        String symbol,
        MarketType marketType,         // SPOT / FUTURES
        TradeSide side,               // BUY / SELL
        BigDecimal qty,
        BigDecimal price,
        BigDecimal quoteQty,
        BigDecimal commission,
        String commissionAsset,
        long time,
        String sourceOrderId
) implements HistoryItem {

    @Override
    public long closedAt() {
        return time;
    }

    @Override
    public long openedAt() {
        return time;
    }
}
