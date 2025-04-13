package zaico.endpoints;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import zaico.client.binance.BinanceCommissionService;
import zaico.client.binance.BinanceOrderService;
import zaico.client.binance.BinanceSymbol;
import zaico.client.binance.MarketRegistry;
import zaico.math.Pair;
import zaico.model.MarketType;
import zaico.model.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller("/facts")
public class OrdersController {

    @Inject
    BinanceOrderService binanceOrderService;

    @Inject
    BinanceCommissionService binanceCommissionService;

    @Inject
    MarketRegistry marketRegistry;

    @Get("/orders")
    public List<Order> getOrders(
            @QueryValue Optional<String> asset,
            @QueryValue Optional<String> quote,
            @QueryValue Optional<String> from // ISO8601
    ) {

        if (asset.isPresent() && quote.isPresent()) {
            Pair pair = marketRegistry.getPair(
                    asset.get(),
                    quote.get(), MarketType.SPOT);
            Pair pairFuturesM = marketRegistry.getPair(
                    asset.get(),
                    quote.get(), MarketType.FUTURES_USDT);
            Pair pairFuturesC = marketRegistry.getPair(
                    asset.get(),
                    quote.get(), MarketType.FUTURES_COIN);

            return from
                    .map(Instant::parse)
                    .map(since -> binanceOrderService.getOrders(pair, since))
                    .orElseGet(() -> binanceOrderService.getOrders(pair));
        }

        return Collections.emptyList();
    }
}
