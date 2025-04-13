package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record WalletBalance(
        String asset,
        BigDecimal total,
        BigDecimal available,
        MarketType marketType
) {}
