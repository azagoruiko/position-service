package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public record BinanceEarnBalance(
        BigDecimal totalAmountInBTC,
        BigDecimal totalAmountInUSDT,
        BigDecimal totalFlexibleAmountInBTC,
        BigDecimal totalFlexibleAmountInUSDT,
        BigDecimal totalLockedInBTC,
        BigDecimal totalLockedInUSDT
) {}

