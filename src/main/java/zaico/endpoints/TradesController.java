package zaico.endpoints;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import zaico.client.binance.BinanceTradeService;
import zaico.math.Pair;
import zaico.model.Trade;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller("/facts")
public class TradesController {

    @Inject
    BinanceTradeService binanceTradeService;

    @Get("/trades")
    public List<Trade> getTrades(
            @QueryValue Optional<String> asset,
            @QueryValue Optional<String> quote,
            @QueryValue Optional<BigDecimal> commission,
            @QueryValue Optional<String> from // ISO8601: 2024-12-31T23:59:59Z
    ) {
        if (asset.isPresent() && quote.isPresent()) {
            Pair pair = new Pair(
                    asset.get(),
                    quote.get(),
                    commission.orElse(BigDecimal.ZERO)
            );

            return from
                    .map(Instant::parse)
                    .map(since -> binanceTradeService.getTrades(pair, since))
                    .orElseGet(() -> binanceTradeService.getTrades(pair));
        }

        // Пока заглушка — возвращаем пустой список
        // В будущем можно будет грузить все пары из портфеля
        return Collections.emptyList();
    }
}
