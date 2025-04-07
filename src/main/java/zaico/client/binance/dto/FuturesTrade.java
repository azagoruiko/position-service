package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record FuturesTrade(
        String symbol,
        long id,
        long orderId,
        String side,
        BigDecimal price,
        BigDecimal qty,
        BigDecimal realizedPnl,
        BigDecimal quoteQty,
        BigDecimal commission,
        String commissionAsset,
        long time,
        String positionSide,
        boolean maker,
        boolean buyer
) {}
