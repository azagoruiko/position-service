package zaico.exchange.service;

import zaico.math.Pair;
import zaico.model.MarketType;

import java.util.Set;

public interface MarketRegistry {
    Pair getPair(String asset, String quote, MarketType type, CommissionService commissionService);

    boolean supports(MarketType type, String symbol);

    boolean supports(String symbol);


    Set<Pair> getRelevantPairs(MarketType type, Set<String> assets, CommissionService commissionService);
}
