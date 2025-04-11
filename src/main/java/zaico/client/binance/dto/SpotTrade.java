package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record SpotTrade(
        String symbol,
        long id,
        long orderId,
        long orderListId,
        BigDecimal price,
        BigDecimal qty,
        BigDecimal quoteQty,
        BigDecimal commission,
        String commissionAsset,
        long time,
        boolean isBuyer,
        boolean isMaker,
        boolean isBestMatch
) {}
