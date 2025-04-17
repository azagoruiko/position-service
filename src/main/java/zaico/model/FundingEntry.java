package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;

@Serdeable
public record FundingEntry(
        String symbol,
        BigDecimal amount,
        String asset,
        Instant time,
        String info,
        long tranId
) implements HistoryItem {

    @Override
    public long closedAt() {
        return time.toEpochMilli();
    }

    @Override
    public long openedAt() {
        return time.toEpochMilli(); // funding начисляется сразу
    }
}
