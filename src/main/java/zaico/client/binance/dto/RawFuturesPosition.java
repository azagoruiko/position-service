package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record RawFuturesPosition(
        String symbol,
        String positionSide,
        String positionAmt,
        String entryPrice,
        String markPrice,
        String unRealizedProfit,
        String leverage,
        long updateTime
) {}
