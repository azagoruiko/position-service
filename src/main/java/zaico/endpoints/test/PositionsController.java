package zaico.endpoints.test;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import zaico.client.binance.BinancePositionService;
import zaico.exchange.service.MarketRegistry;
import zaico.model.FuturesSnapshot;
import zaico.math.Pair;
import zaico.model.MarketType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller("/facts")
public class PositionsController {

    @Inject
    BinancePositionService positionService;

    @Inject
    MarketRegistry marketRegistry;

    @Get("/positions")
    public List<FuturesSnapshot> getPositions(
            @QueryValue Optional<String> asset,
            @QueryValue Optional<String> quote
    ) {
        if (asset.isPresent() && quote.isPresent()) {
            Pair pairUSDT = marketRegistry.getPair(asset.get(), quote.get(), MarketType.FUTURES_USDT);

            List<FuturesSnapshot> result = positionService.getOpenPositions(pairUSDT);
            return result;
        }

        return Collections.emptyList();
    }
}
