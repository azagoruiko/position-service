package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeInfo(
        List<Symbol> symbols
) {
    @Serdeable
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Symbol(
            String symbol,
            String status,
            String baseAsset,
            String quoteAsset,
            BigDecimal baseAssetPrecision,
            BigDecimal quoteAssetPrecision,
            BigDecimal quotePrecision
    ) {}
}
