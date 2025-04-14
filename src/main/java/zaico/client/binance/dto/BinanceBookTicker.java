package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record BinanceBookTicker(
        String symbol,
        BigDecimal bidPrice,
        BigDecimal bidQty,
        BigDecimal askPrice,
        BigDecimal askQty
) {
    public BigDecimal midPrice() {
        return bidPrice.add(askPrice).divide(BigDecimal.valueOf(2), bidPrice.scale(), BigDecimal.ROUND_HALF_UP);
    }
}
