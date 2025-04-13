package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record FuturesOrder(
        long orderId,
        String symbol,
        String pair,
        String status,
        String clientOrderId,
        BigDecimal price,
        BigDecimal avgPrice,
        BigDecimal origQty,
        BigDecimal executedQty,
        BigDecimal cumBase,
        BigDecimal cumQuote,
        long goodTillDate,
        String timeInForce,
        String type,
        boolean reduceOnly,
        boolean closePosition,
        String side,
        String positionSide,
        BigDecimal stopPrice,
        String workingType,
        boolean priceProtect,
        String priceMatch,
        String selfTradePreventionMode,
        String origType,
        long time,
        long updateTime
) {}
