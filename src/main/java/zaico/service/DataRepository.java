package zaico.service;

import zaico.exchange.Platform;
import zaico.math.Pair;
import zaico.model.MarketType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataRepository {

    private final Map<Platform, Map<MarketType, Map<Pair, PairData>>> tradeData = new ConcurrentHashMap<>();

    // Получить PairData, создавая по необходимости
    public PairData getOrCreate(Platform platform, MarketType marketType, Pair pair) {
        return tradeData
                .computeIfAbsent(platform, p -> new ConcurrentHashMap<>())
                .computeIfAbsent(marketType, mt -> new ConcurrentHashMap<>())
                .computeIfAbsent(pair, pr -> new PairData(pr, platform));
    }

    // Просто получить, если есть
    public PairData get(Platform platform, MarketType marketType, Pair pair) {
        return tradeData.getOrDefault(platform, Map.of())
                .getOrDefault(marketType, Map.of())
                .get(pair);
    }

    // Удалить
    public void remove(Platform platform, MarketType marketType, Pair pair) {
        Map<MarketType, Map<Pair, PairData>> byMarket = tradeData.get(platform);
        if (byMarket == null) return;

        Map<Pair, PairData> byPair = byMarket.get(marketType);
        if (byPair == null) return;

        byPair.remove(pair);
    }

    // Очистить всё (например, при перезапуске)
    public void clear() {
        tradeData.clear();
    }

    @Override
    public String toString() {
        return "DataRepository{platforms=" + tradeData.size() + "}";
    }
}
