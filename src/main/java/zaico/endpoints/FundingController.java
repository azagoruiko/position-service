package zaico.endpoints;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import zaico.client.binance.BinanceFundingService;
import zaico.client.binance.MarketRegistry;
import zaico.math.Pair;
import zaico.model.FundingEntry;
import zaico.model.MarketType;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller("/facts")
public class FundingController {

    @Inject
    BinanceFundingService fundingService;

    @Inject
    MarketRegistry marketRegistry;

    @Get("/funding")
    public List<FundingEntry> getFunding(
            @QueryValue Optional<String> asset,
            @QueryValue Optional<String> quote,
            @QueryValue Optional<String> from // ISO-8601
    ) {
        if (asset.isPresent() && quote.isPresent()) {
            Instant fromDate = from.map(Instant::parse).orElse(Instant.now().minusSeconds(365 * 30 * 86400)); // по умолчанию — за 30 дней

            Pair pairUSDT = marketRegistry.getPair(asset.get(), quote.get(), MarketType.FUTURES_USDT);
            Pair pairCOIN = marketRegistry.getPair(asset.get(), quote.get(), MarketType.FUTURES_COIN);

            List<FundingEntry> entries = fundingService.getFunding(pairUSDT, fromDate);
            entries.addAll(fundingService.getFunding(pairCOIN, fromDate));
            return entries;
        }

        return Collections.emptyList();
    }
}
