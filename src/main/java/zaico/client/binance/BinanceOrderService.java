package zaico.client.binance;

import jakarta.inject.Singleton;
import zaico.client.binance.dto.BinanceOrderMapper;
import zaico.math.Pair;
import zaico.model.MarketType;
import zaico.model.Order;

import java.time.Instant;
import java.util.*;

import static zaico.client.binance.dto.BinanceOrderMapper.*;
import static zaico.client.binance.parser.BinanceOrderParser.*;

@Singleton
public class BinanceOrderService extends AbstractBinanceService {

    public BinanceOrderService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    private List<Order> fetchFuturesOrders(Pair pair, FuturesType type, Optional<Instant> startTime) {
        String symbol = BinanceSymbol.getFuturesSymbol(pair, type);
        if (!marketRegistry.supports(symbol)) {
            return List.of();
        }
        LinkedHashMap<String, Object> params = new LinkedHashMap<>(Map.of("symbol", symbol));
        startTime.ifPresent(t -> params.put("startTime", t.toEpochMilli()));

        String raw = (type == FuturesType.USDT)
                ? uFuturesClient.account().allOrders(params)
                : cFuturesClient.account().allOrders(params);

        return parseFuturesOrders(raw).stream()
                .map(o -> fromFutures(o, type.toMarketType()))
                .toList();
    }

    private List<Order> fetchSpotOrders(Pair pair, Optional<Instant> startTime) {
        String symbol = BinanceSymbol.getSpotSymbol(pair);
        if (!marketRegistry.supports(MarketType.SPOT, symbol)) {
            return List.of();
        }
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        startTime.ifPresent(t -> params.put("startTime", t.toEpochMilli()));

        String raw = spotClient.createTrade().getOrders(params);

        return parseSpotOrders(raw).stream()
                .map(BinanceOrderMapper::fromSpot)
                .toList();
    }

    public List<Order> getOrders(Pair pair) {
        return getOrders(pair, Optional.empty());
    }

    public List<Order> getOrders(Pair pair, Instant startTime) {
        return getOrders(pair, Optional.of(startTime));
    }

    private List<Order> getOrders(Pair pair, Optional<Instant> startTime) {
        List<Order> allOrders = new ArrayList<>();

        allOrders.addAll(fetchFuturesOrders(pair, FuturesType.USDT, startTime));
        allOrders.addAll(fetchFuturesOrders(pair, FuturesType.COIN, startTime));
        allOrders.addAll(fetchSpotOrders(pair, startTime));

        return allOrders;
    }
}
