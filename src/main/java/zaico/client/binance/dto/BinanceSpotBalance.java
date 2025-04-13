package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record BinanceSpotBalance(
        String asset,
        BigDecimal free,
        BigDecimal locked
) {}
