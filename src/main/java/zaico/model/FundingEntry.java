package zaico.model;

import java.math.BigDecimal;

public record FundingEntry(
        String symbol,
        BigDecimal amount,
        long time
) {}
