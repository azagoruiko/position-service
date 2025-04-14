// BinanceFundingRate.java
package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable
public record BinanceFundingRate(
        String symbol,
        @JsonProperty("fundingRate") BigDecimal rate,
        long fundingTime
) {
    public Instant time() {
        return Instant.ofEpochMilli(fundingTime);
    }
}
