package zaico.exchange.service;

import zaico.math.Pair;
import zaico.model.MarketType;

public interface MarketRegistry {
    Pair getPair(String asset, String quote, MarketType type);

    boolean supports(MarketType type, String symbol);

    boolean supports(String symbol);
}
