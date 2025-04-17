package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable
public record Order(
        String symbol,
        long orderId,
        String side,          // BUY / SELL
        String type,          // LIMIT / MARKET / STOP / ...
        String status,        // NEW / FILLED / PARTIALLY_FILLED / CANCELED ...
        BigDecimal price,
        BigDecimal origQty,
        BigDecimal executedQty,
        BigDecimal quoteQty,
        Instant updateTime,
        MarketType marketType // SPOT / FUTURES_USDT / FUTURES_COIN
)  implements HistoryItem {

    @Override
    public long closedAt() {
        return updateTime.toEpochMilli();
    }

    @Override
    public long openedAt() {
        return updateTime.toEpochMilli();
    }
}
