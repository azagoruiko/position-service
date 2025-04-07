package zaico.model;

import io.micronaut.serde.annotation.Serdeable;
import zaico.math.Calc;

import java.math.BigDecimal;

@Serdeable
public record FuturesSnapshot(
        String symbol,                 // например, BTCUSDT
        BigDecimal size,              // позиция: >0 long, <0 short, 0 — закрыта
        BigDecimal entryPrice,        // средняя цена входа
        BigDecimal markPrice,         // текущая цена на рынке
        BigDecimal unrealizedPnl,     // нереализованная прибыль/убыток
        int leverage,                 // плечо
        boolean isolated,             // true = isolated margin, false = cross
        PositionSide side,            // LONG / SHORT / BOTH
        long updateTime               // время последнего обновления
) {

    public boolean isOpen() {
        return size != null && size.compareTo(BigDecimal.ZERO) != 0;
    }

    public boolean isLong() {
        return size.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isShort() {
        return size.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal notional() {
        return size.abs().multiply(markPrice);
    }

    public BigDecimal pnlPercent() {
        if (entryPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal change = markPrice.subtract(entryPrice)
                .divide(entryPrice, Calc.SCALE, java.math.RoundingMode.HALF_UP);

        return change.multiply(BigDecimal.valueOf(100))
                .multiply(isLong() ? BigDecimal.ONE : BigDecimal.valueOf(-1));
    }
}
