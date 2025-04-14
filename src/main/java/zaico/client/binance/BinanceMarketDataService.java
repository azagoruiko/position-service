package zaico.client.binance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.BinanceBookTicker;
import zaico.client.binance.dto.BinanceFundingRate;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BinanceMarketDataService extends AbstractBinanceService implements zaico.exchange.service.MarketDataService {

    @Inject ObjectMapper objectMapper;

    @Inject
    public BinanceMarketDataService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    @Override
    public BigDecimal getSpotTickerPrice(String symbol) {
        String raw = spotClient.createMarket().bookTicker(Map.of("symbol", symbol));
        try {
            BinanceBookTicker dto = objectMapper.readValue(raw, BinanceBookTicker.class);
            return dto.midPrice(); // или dto.bidPrice() / dto.askPrice() — на выбор
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch bookTicker for " + symbol, e);
        }
    }

    @Override
    public BigDecimal getFuturesTickerPrice(FuturesType type, String symbol) {
        String raw = (type == FuturesType.USDT)
                ? uFuturesClient.market().bookTicker(new LinkedHashMap<>(Map.of("symbol", symbol)))
                : cFuturesClient.market().bookTicker(new LinkedHashMap<>(Map.of("symbol", symbol)));
        try {
            BinanceBookTicker dto = objectMapper.readValue(raw, BinanceBookTicker.class);
            return dto.midPrice();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch futures ticker price for " + symbol, e);
        }
    }

    @Override
    public BinanceFundingRate getFundingRate(FuturesType type, String symbol) {
        String raw = (type == FuturesType.USDT)
                ? uFuturesClient.market().fundingRate(new LinkedHashMap<>(Map.of("symbol", symbol)))
                : cFuturesClient.market().fundingRate(new LinkedHashMap<>(Map.of("symbol", symbol)));
        try {
            List<BinanceFundingRate> list = objectMapper.readValue(raw, new TypeReference<>() {});
            return list.get(0); // берём ближайший
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch funding rate for " + symbol, e);
        }
    }
}
