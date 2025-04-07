package zaico.model;

import zaico.math.Pair;

import java.math.BigDecimal;
import java.util.List;

public record AggregatedPosition(
        Pair pair,
        PositionSide side,                 // LONG / SHORT / null
        BigDecimal entryPrice,
        BigDecimal markPrice,
        BigDecimal size,
        BigDecimal realizedPnl,
        BigDecimal unrealizedPnl,
        BigDecimal fundingPaid,
        BigDecimal totalCommission,
        int leverage,
        boolean isHedged,
        List<Trade> openTrades,
        List<Trade> closedTrades,
        List<Trade> allTrades
) {}
