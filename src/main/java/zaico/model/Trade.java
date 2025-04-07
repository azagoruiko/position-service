package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record Trade(
        String symbol,
        TradeType type,                 // BUY / SELL
        BigDecimal price,
        BigDecimal qty,
        BigDecimal quoteQty,
        BigDecimal commission,
        String commissionAsset,
        long time,
        PositionSide side,             // LONG / SHORT / BOTH — для фьючей
        String source,                 // "SPOT", "FUTURES", "MANUAL", "BYBIT" и т.д.
        String externalId              // orderId или tradeId с биржи
) {}
