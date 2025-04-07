package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record BinanceFuturesPosition(
        String symbol,
        BigDecimal positionAmt,
        BigDecimal entryPrice,
        BigDecimal breakEvenPrice,
        BigDecimal markPrice,
        BigDecimal unRealizedProfit,
        BigDecimal liquidationPrice,
        int leverage,
        BigDecimal maxNotionalValue,
        String marginType,
        BigDecimal isolatedMargin,
        boolean isAutoAddMargin,
        String positionSide,
        BigDecimal notional,
        BigDecimal isolatedWallet,
        long updateTime,
        boolean isolated,
        int adlQuantile
) {}
