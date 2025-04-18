// BinanceMarketRegistry.java
package zaico.client.binance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.ExchangeInfo;
import zaico.exchange.service.CommissionService;
import zaico.exchange.service.MarketRegistry;
import zaico.math.Pair;
import zaico.model.MarketType;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class BinanceMarketRegistry implements MarketRegistry {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Set<String> tradingQuotes = Set.of("USDT", "BTC", "BNB", "USDC", "ETH", "USD");

    private final Map<MarketType, List<ExchangeInfo.Symbol>> symbolsByType = new EnumMap<>(MarketType.class);
    private boolean loaded = false;

    public synchronized void initIfNeeded() {
        if (loaded) return;

        symbolsByType.put(MarketType.SPOT, loadExchangeInfo("https://api.binance.com/api/v3/exchangeInfo"));
        symbolsByType.put(MarketType.FUTURES_USDT, loadExchangeInfo("https://fapi.binance.com/fapi/v1/exchangeInfo"));
        symbolsByType.put(MarketType.FUTURES_COIN, loadExchangeInfo("https://dapi.binance.com/dapi/v1/exchangeInfo"));

        loaded = true;
    }

    private List<ExchangeInfo.Symbol> loadExchangeInfo(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream input = connection.getInputStream()) {
                ExchangeInfo info = objectMapper.readValue(input, new TypeReference<>() {});
                return info.symbols();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load exchange info from " + urlString, e);
        }
    }

    @Override
    public Pair getPair(String asset, String quote, MarketType type, CommissionService commissionService) {
        String symbol = switch (type) {
            case SPOT -> BinanceSymbol.getSpotSymbol(asset, quote);
            case FUTURES_USDT -> BinanceSymbol.getFuturesSymbol(asset, quote, FuturesType.USDT);
            case FUTURES_COIN -> BinanceSymbol.getFuturesSymbol(asset, quote, FuturesType.COIN);
            case EARN -> null;
        };

        BigDecimal commission = switch (type) {
            case SPOT -> commissionService.getSpotTakerFee(symbol);
            case FUTURES_USDT -> commissionService.getFuturesTakerFee(FuturesType.USDT, symbol);
            case FUTURES_COIN -> commissionService.getFuturesTakerFee(FuturesType.COIN, symbol);
            case EARN -> BigDecimal.ZERO;
        };

        return new Pair(asset, quote, commission, symbol);
    }

    @Override
    public boolean supports(MarketType type, String symbol) {
        initIfNeeded();
        return symbolsByType.getOrDefault(type, List.of()).stream()
                .anyMatch(s -> s.symbol().equals(symbol));
    }

    @Override
    public boolean supports(String symbol) {
        initIfNeeded();
        return Arrays.stream(MarketType.values())
                .anyMatch(type -> supports(type, symbol));
    }

    @Override
    public Set<Pair> getRelevantPairs(MarketType type, Set<String> assets, CommissionService commissionService) {
        initIfNeeded();

        return symbolsByType.getOrDefault(type, List.of()).stream()
                .filter(s -> assets.contains(s.baseAsset()) && tradingQuotes.contains(s.quoteAsset()))
                .map(s -> new Pair(
                        s.baseAsset(),
                        s.quoteAsset(),
                        getCommissionForSymbol(type, s.symbol(), commissionService),
                        s.symbol()
                ))
                .collect(Collectors.toSet());
    }

    private BigDecimal getCommissionForSymbol(MarketType type, String symbol, CommissionService commissionService) {
        return switch (type) {
            case SPOT -> commissionService.getSpotTakerFee(symbol);
            case FUTURES_USDT -> commissionService.getFuturesTakerFee(FuturesType.USDT, symbol);
            case FUTURES_COIN -> commissionService.getFuturesTakerFee(FuturesType.COIN, symbol);
            case EARN -> BigDecimal.ZERO;
        };
    }
}
