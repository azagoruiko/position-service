package zaico.service;

import zaico.client.binance.dto.FuturesTrade;
import zaico.model.FuturesSnapshot;
import zaico.math.Pair;
import zaico.model.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PositionBuilder {

    private final Pair pair;
    private final Instant fromDate;

    private List<Trade> spotTrades = new ArrayList<>();
    private List<FuturesTrade> futuresTrades = new ArrayList<>();
    private List<FuturesSnapshot> futuresSnapshots = new ArrayList<>();
    private List<FundingEntry> fundingLog = new ArrayList<>();

    public PositionBuilder(Pair pair, Instant fromDate) {
        this.pair = pair;
        this.fromDate = fromDate;
    }

    public PositionBuilder withSpotTrades(List<Trade> spotTrades) {
        this.spotTrades = spotTrades;
        return this;
    }

    public PositionBuilder withFuturesData(List<FuturesSnapshot> positions,
                                           List<FuturesTrade> trades,
                                           List<FundingEntry> fundingLog) {
        this.futuresSnapshots = positions;
        this.futuresTrades = trades;
        this.fundingLog = fundingLog;
        return this;
    }

    public AggregatedPosition build() {
        // TODO: здесь будет мясо

        return new AggregatedPosition(
                pair,
                null,                   // TODO: определить side
                BigDecimal.ZERO,       // TODO: entry price
                BigDecimal.ZERO,       // TODO: mark price
                BigDecimal.ZERO,       // TODO: size
                BigDecimal.ZERO,       // TODO: realized PnL
                BigDecimal.ZERO,       // TODO: unrealized PnL
                BigDecimal.ZERO,       // TODO: funding
                BigDecimal.ZERO,       // TODO: commissions
                0,                     // TODO: leverage
                false,                 // TODO: isHedged
                List.of(),             // TODO: open trades
                List.of(),             // TODO: closed trades
                List.of()              // TODO: all trades
        );
    }
}
