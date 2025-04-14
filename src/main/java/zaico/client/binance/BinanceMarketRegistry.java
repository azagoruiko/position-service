package zaico.client.binance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.ExchangeInfo;
import zaico.exchange.service.MarketRegistry;
import zaico.math.Pair;
import zaico.model.MarketType;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class BinanceMarketRegistry implements MarketRegistry {
    @Inject
    BinanceCommissionService binanceCommissionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Set<String> spotSymbols = new HashSet<>();
    private final Set<String> usdtFuturesSymbols = new HashSet<>();
    private final Set<String> coinFuturesSymbols = new HashSet<>();

    private boolean loaded = false;

    public synchronized void initIfNeeded() {
        if (loaded) return;

        spotSymbols.addAll(loadSymbols("https://api.binance.com/api/v3/exchangeInfo"));
        usdtFuturesSymbols.addAll(loadSymbols("https://fapi.binance.com/fapi/v1/exchangeInfo"));
        coinFuturesSymbols.addAll(loadSymbols("https://dapi.binance.com/dapi/v1/exchangeInfo"));

        loaded = true;
    }

    @Override
    public Pair getPair(String asset, String quote, MarketType type) {
        String symbol = switch (type) {
            case SPOT -> BinanceSymbol.getSpotSymbol(asset, quote);
            case FUTURES_USDT -> BinanceSymbol.getFuturesSymbol(asset, quote, FuturesType.USDT);
            case FUTURES_COIN -> BinanceSymbol.getFuturesSymbol(asset, quote, FuturesType.COIN);
            case EARN -> null;
        };

        BigDecimal commission = switch (type) {
            case SPOT -> binanceCommissionService.getSpotTakerFee(symbol);
            case FUTURES_USDT -> binanceCommissionService.getFuturesTakerFee(FuturesType.USDT, symbol);
            case FUTURES_COIN -> binanceCommissionService.getFuturesTakerFee(FuturesType.COIN, symbol);
            case EARN -> BigDecimal.ZERO;
        };

        return new Pair(asset, quote, commission);
    }


    @Override
    public boolean supports(MarketType type, String symbol) {
        initIfNeeded();
        return switch (type) {
            case SPOT -> spotSymbols.contains(symbol);
            case FUTURES_USDT -> usdtFuturesSymbols.contains(symbol);
            case FUTURES_COIN -> coinFuturesSymbols.contains(symbol);
            case EARN -> true;
        };
    }

    @Override
    public boolean supports(String symbol) {
        initIfNeeded();
        return supports(MarketType.SPOT, symbol)
                || supports(MarketType.FUTURES_USDT, symbol)
                || supports(MarketType.FUTURES_COIN, symbol);
    }

    private Set<String> loadSymbols(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream input = connection.getInputStream()) {
                ExchangeInfo info = objectMapper.readValue(input, new TypeReference<>() {});
                Set<String> symbols = new HashSet<>();
                for (ExchangeInfo.Symbol s : info.symbols()) {
                    symbols.add(s.symbol());
                }
                return symbols;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load exchange info from " + urlString, e);
        }
    }
}
