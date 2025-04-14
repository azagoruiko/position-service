package zaico.model;

import java.math.BigDecimal;
import java.time.Instant;

public record WalletTransaction(
        String asset,
        BigDecimal amount,
        WalletTransactionType type,
        String status,
        Instant time
) {}