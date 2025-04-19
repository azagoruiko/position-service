package zaico.loader;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.BinanceBalanceService;
import zaico.client.binance.BinanceCommissionService;
import zaico.client.binance.BinanceTradeService;
import zaico.client.binance.FuturesType;
import zaico.exchange.Platform;
import zaico.exchange.service.CommissionService;
import zaico.exchange.service.MarketRegistry;
import zaico.exchange.service.OrderService;
import zaico.math.Pair;
import zaico.model.MarketType;
import zaico.model.Order;
import zaico.model.Trade;
import zaico.model.WalletBalance;
import zaico.service.DataRepository;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Singleton
public class InitialBalanceLoader implements ApplicationEventListener<ServerStartupEvent> {

    @Inject BinanceBalanceService balanceService;
    @Inject BinanceTradeService tradeService;
    @Inject
    OrderService orderService;
    @Inject DataRepository repository;
    @Inject MarketRegistry marketRegistry;
    @Inject
    CommissionService binanceCommissionService;

    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        loadAll();
    }

    @PostConstruct
    public void loadOnStartup() {
        System.out.println("⏳ Loading initial data...");
        loadAll();
        System.out.println("✅ Initial data loaded.");
    }

    private void loadAllSequential() {
        System.out.println("🚶 Sequential loading started...");
        loadAllTrades(false);  // false → sequential
        System.out.println("✅ Sequential loading finished.");
    }

    private void loadAllInParallel() {
        System.out.println("🚀 Parallel loading started...");
        loadAllTrades(true);  // true → parallel
        System.out.println("✅ Parallel loading finished.");
    }

    private void loadAll() {
        loadAllInParallel();
    }

    private void loadAllTrades(boolean parallel) {
        Platform platform = Platform.BINANCE;
        Map<MarketType, Set<String>> assetsByMarket = new HashMap<>();

        // Load wallets
        for (MarketType type : MarketType.values()) {
            List<WalletBalance> balances = switch (type) {
                case SPOT -> balanceService.getSpotBalances(true);
                case FUTURES_USDT -> balanceService.getFuturesBalances(FuturesType.USDT, true);
                case FUTURES_COIN -> balanceService.getFuturesBalances(FuturesType.COIN, true);
                case EARN -> balanceService.getEarnBalances(true);
            };

            balances.forEach(w -> repository.setWallet(platform, type, w));



            assetsByMarket
                    .computeIfAbsent(type == MarketType.EARN ? MarketType.SPOT : type, k -> new HashSet<>())
                    .addAll(balances.stream().map(WalletBalance::asset).collect(Collectors.toSet()));
        }

        List<Callable<Void>> tasks = new ArrayList<>();

        for (Map.Entry<MarketType, Set<String>> entry : assetsByMarket.entrySet()) {
            MarketType type = entry.getKey();
            Set<String> assets = entry.getValue();

            Set<Pair> pairs = marketRegistry.getRelevantPairs(type, assets, binanceCommissionService);

            for (Pair pair : pairs) {
                Runnable job = () -> {
                    try {
                        // Load orders
                        List<Order> orders = orderService.getOrders(pair);
                        repository.getOrCreate(platform, type, pair).addOrder(orders);
                        System.out.printf("📥 Loaded %d orders for %s%n", orders.size(), pair);

                        List<Trade> trades = tradeService.getTrades(pair);
                        repository.getOrCreate(platform, type, pair).addTrades(trades);
                        System.out.printf("📥 Loaded %d trades for %s%n", trades.size(), pair);
                    } catch (Exception e) {
                        System.err.printf("❌ Failed to load trades for %s: %s%n", pair, e.getMessage());
                    }
                };

                if (parallel) {
                    tasks.add(Executors.callable(job, null));
                } else {
                    job.run(); // выполнять прямо тут
                }
            }
        }

        if (parallel) {
            try {
                executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                throw new RuntimeException("Trade loading interrupted", e);
            }
        }

        System.out.printf("✅ Trade data loaded for %d pairs (%s)%n", tasks.size(), parallel ? "parallel" : "sequential");
    }


    @Scheduled(fixedDelay = "1m")
    public void updatePeriodically() {
        System.out.println("🔄 Scheduled update...");
        // TODO: реализовать позже
    }
}
