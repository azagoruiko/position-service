package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable

public record FuturesSnapshot(
        String symbol,
        MarketType marketType,
        BigDecimal positionAmt,       // Объём позиции (может быть отрицательным для short)
        BigDecimal entryPrice,        // Цена входа
        BigDecimal markPrice,         // Текущая (рыночная) цена
        BigDecimal unrealizedPnl,     // Нереализованный PnL
        int leverage,                 // Плечо
        String positionSide,          // "LONG", "SHORT" или "BOTH"
        Instant updateTime            // Последнее обновление (по сути — последнее изменение позиции)
) {
    public boolean isLong() {
        return positionAmt.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isShort() {
        return positionAmt.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isFlat() {
        return positionAmt.compareTo(BigDecimal.ZERO) == 0;
    }

    public BigDecimal absSize() {
        return positionAmt.abs();
    }
    public BigDecimal size() {
        return positionAmt;
    }

}
