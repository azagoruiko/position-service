package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record FuturesAccountInfo(
        @JsonProperty("takerCommission") BigDecimal takerCommission,
        @JsonProperty("makerCommission") BigDecimal makerCommission
) {}
