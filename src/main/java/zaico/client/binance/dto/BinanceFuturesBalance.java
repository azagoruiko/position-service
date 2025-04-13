package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record BinanceFuturesBalance(
        String asset,
        BigDecimal walletBalance,
        BigDecimal crossUnPnl,
        BigDecimal availableBalance
) {}

