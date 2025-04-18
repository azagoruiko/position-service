package zaico.service;

import jakarta.inject.Singleton;
import zaico.exchange.Platform;
import zaico.math.Pair;
import zaico.model.MarketType;
import zaico.model.WalletBalance;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Singleton
public class DataRepository {

    private final Map<Platform, Map<MarketType, Map<Pair, PairData>>> tradeData = new ConcurrentHashMap<>();
    private final Map<Platform, Map<MarketType, Map<String, BigDecimal>>> commissions = new ConcurrentHashMap<>();
    private final Map<Platform, Map<MarketType, Map<String, WalletBalance>>> wallets = new ConcurrentHashMap<>();

    // Получить PairData, создавая по необходимости
    public PairData getOrCreate(Platform platform, MarketType marketType, Pair pair) {
        return tradeData
                .computeIfAbsent(platform, p -> new ConcurrentHashMap<>())
                .computeIfAbsent(marketType, mt -> new ConcurrentHashMap<>())
                .computeIfAbsent(pair, pr -> new PairData(pr, platform));
    }

    // Просто получить, если есть
    public PairData get(Platform platform, MarketType marketType, Pair pair) {
        return Optional.ofNullable(
                tradeData.getOrDefault(platform, Map.of())
                        .getOrDefault(marketType, Map.of())
                        .get(pair)
        ).orElseThrow(() -> new IllegalStateException("No data for " + platform + "/" + marketType + "/" + pair));
    }

    public Collection<PairData> getAll() {
        return tradeData.values().stream()
                .flatMap(mtMap -> mtMap.values().stream())
                .flatMap(pairMap -> pairMap.values().stream())
                .toList();
    }

    public long lastUpdatedAt(PairData data) {
        long walletUpdatedAt = Optional.ofNullable(data.getWallet())
                .map(w -> getWallet(data.getPlatform(), w.marketType(), data.getPair().asset()))
                .map(WalletBalance::closedAt)
                .orElse(0L);

        return Stream.of(
                data.getOrders().getLastUpdatedAt(),
                data.getTrades().getLastUpdatedAt(),
                data.getFundingHistory().getLastUpdatedAt(),
                walletUpdatedAt
        ).max(Long::compare).orElse(0L);
    }


    public void remove(Platform platform, MarketType marketType, Pair pair) {
        Map<MarketType, Map<Pair, PairData>> byMarket = tradeData.get(platform);
        if (byMarket == null) return;
        Map<Pair, PairData> byPair = byMarket.get(marketType);
        if (byPair == null) return;
        byPair.remove(pair);
    }

    public void setCommission(Platform platform, MarketType marketType, String symbol, BigDecimal commission) {
        commissions
                .computeIfAbsent(platform, p -> new ConcurrentHashMap<>())
                .computeIfAbsent(marketType, m -> new ConcurrentHashMap<>())
                .put(symbol, commission);
    }

    public BigDecimal getCommission(Platform platform, MarketType marketType, String symbol) {
        return Optional.ofNullable(
                commissions.getOrDefault(platform, Map.of())
                        .getOrDefault(marketType, Map.of())
                        .get(symbol)
        ).orElseThrow(() -> new IllegalStateException("No commission for %s/%s/%s".formatted(platform, marketType, symbol)));
    }

    // === ✅ Кошельки ===

    public void setWallet(Platform platform, MarketType marketType, WalletBalance balance) {
        wallets
                .computeIfAbsent(platform, p -> new ConcurrentHashMap<>())
                .computeIfAbsent(marketType, m -> new ConcurrentHashMap<>())
                .put(balance.asset(), balance);
    }

    public WalletBalance getWallet(Platform platform, MarketType marketType, String asset) {
        return Optional.ofNullable(
                wallets.getOrDefault(platform, Map.of())
                        .getOrDefault(marketType, Map.of())
                        .get(asset)
        ).orElseThrow(() -> new IllegalStateException("No wallet for %s/%s/%s".formatted(platform, marketType, asset)));
    }

    public Collection<WalletBalance> getWallets(Platform platform, MarketType marketType) {
        return wallets.getOrDefault(platform, Map.of())
                .getOrDefault(marketType, Map.of())
                .values();
    }

    public void clear() {
        tradeData.clear();
        commissions.clear();
        wallets.clear();
    }

    @Override
    public String toString() {
        return "DataRepository{platforms=" + tradeData.size() + ", wallets=" + wallets.size() + "}";
    }
}
