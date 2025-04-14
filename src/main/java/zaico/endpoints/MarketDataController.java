package zaico.endpoints;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import zaico.client.binance.BinanceMarketDataService;

import java.math.BigDecimal;
import java.util.Optional;

@Controller("/facts")
public class MarketDataController {

    @Inject
    BinanceMarketDataService marketDataService;

    @Get("/ticker")
    public BigDecimal getSpotPrice(
            @QueryValue String asset,
            @QueryValue String quote
    ) {
        String symbol = asset + quote; // Пример: SOLUSDT
        return marketDataService.getSpotTickerPrice(symbol);
    }
}
