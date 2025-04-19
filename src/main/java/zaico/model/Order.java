package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Serdeable
public record Order(
        String symbol,
        long orderId,
        String side,          // BUY / SELL
        String type,          // LIMIT / MARKET / STOP / ...
        String status,        // NEW / FILLED / PARTIALLY_FILLED / CANCELED ...
        BigDecimal price,
        BigDecimal activatePrice,
        BigDecimal priceRate,
        BigDecimal origQty,
        BigDecimal executedQty,
        BigDecimal quoteQty,
        Instant updateTime,
        MarketType marketType, // SPOT / FUTURES_USDT / FUTURES_COIN
        Set<Trade> trades
)  implements HistoryItem {

    public Order(String symbol, long orderId, String side, String type, String status, BigDecimal price, BigDecimal activatePrice, BigDecimal priceRate, BigDecimal origQty, BigDecimal executedQty, BigDecimal quoteQty, Instant updateTime, MarketType marketType) {
        this(symbol, orderId, side, type, status, price, activatePrice, priceRate, origQty, executedQty, quoteQty, updateTime, marketType, new HashSet<>());
    }

    public String getId() {
        return String.valueOf(orderId);
    }

    public void addTrade(Trade trade) {
        if (!trade.sourceOrderId().equals(getId())) {
            throw new IllegalArgumentException(String.format("Trade %s has id %s but should be for order %s", trade, trade.sourceOrderId(), getId()));
        }
        trades.add(trade);
    }

    public void addTrades(Collection<Trade> trades) {
        for (Trade t : trades) addTrade(t);
    }

    @Override
    public long closedAt() {
        return updateTime.toEpochMilli();
    }

    @Override
    public long openedAt() {
        return updateTime.toEpochMilli();
    }
}
