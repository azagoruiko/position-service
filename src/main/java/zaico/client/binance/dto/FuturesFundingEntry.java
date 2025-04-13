package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record FuturesFundingEntry(
        String symbol,
        String incomeType,
        BigDecimal income,
        String asset,
        long time,
        String info,
        long tranId
) {}
