package zaico.client.binance;

import jakarta.inject.Singleton;
import zaico.client.binance.dto.mapper.BinanceTradeMapper;
import zaico.math.Pair;
import zaico.model.Trade;

import java.time.Instant;
import java.util.*;

import static zaico.client.binance.dto.mapper.BinanceTradeMapper.*;
import static zaico.client.binance.parser.BinanceTradeParser.*;

@Singleton
public class BinanceTradeService extends AbstractBinanceService {

    public BinanceTradeService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    private List<Trade> fetchFuturesTrades(Pair pair, FuturesType type, Optional<Instant> startTime) {
        String symbol = BinanceSymbol.getFuturesSymbol(pair, type);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>(Map.of("symbol", symbol));
        startTime.ifPresent(t -> params.put("startTime", t.toEpochMilli()));

        String raw = (type == FuturesType.USDT)
                ? uFuturesClient.account().accountTradeList(params)
                : cFuturesClient.account().accountTradeList(params);

        return parseFuturesTrades(raw).stream()
                .map(t -> fromFutures(t, type.toMarketType()))
                .toList();
    }
    private List<Trade> fetchSpotTrades(Pair pair, Optional<Instant> startTime) {
        String symbol = BinanceSymbol.getSpotSymbol(pair);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        startTime.ifPresent(t -> params.put("startTime", t.toEpochMilli()));

        String raw = spotClient.createTrade().myTrades(params);

        return parseSpotTrades(raw).stream()
                .map(BinanceTradeMapper::fromSpot)
                .toList();
    }



    public List<Trade> getTrades(Pair pair) {
        return getTrades(pair, Optional.empty());
    }

    public List<Trade> getTrades(Pair pair, Instant startTime) {
        return getTrades(pair, Optional.of(startTime));
    }

    private List<Trade> getTrades(Pair pair, Optional<Instant> startTime) {
        List<Trade> allTrades = new ArrayList<>();

        allTrades.addAll(fetchFuturesTrades(pair, FuturesType.USDT, startTime));
        allTrades.addAll(fetchFuturesTrades(pair, FuturesType.COIN, startTime));
        allTrades.addAll(fetchSpotTrades(pair, startTime));

        return allTrades;
    }
}
