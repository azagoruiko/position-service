package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;

@Serdeable
public record BinanceAssetTransfer(
        long tranId,
        @JsonProperty("asset") String asset,
        @JsonProperty("amount") String amount,
        @JsonProperty("type") String type,
        @JsonProperty("status") String status,
        @JsonProperty("timestamp") long timestamp
) {
    public Instant time() {
        return Instant.ofEpochMilli(timestamp);
    }
}
