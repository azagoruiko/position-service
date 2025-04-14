package zaico.client.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.futures.client.impl.CMFuturesClientImpl;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import jakarta.inject.Inject;
import zaico.exchange.service.MarketRegistry;
import zaico.math.Pair;
import zaico.model.FundingEntry;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class AbstractBinanceService {
    protected final SpotClientImpl spotClient;
    protected final UMFuturesClientImpl uFuturesClient;
    protected final CMFuturesClientImpl cFuturesClient;

    @Inject
    MarketRegistry marketRegistry;

    public AbstractBinanceService(BinanceClientProvider clientProvider) {
        this.spotClient = clientProvider.getSpotClient();
        this.uFuturesClient = clientProvider.getuFuturesClient();
        this.cFuturesClient = clientProvider.getcFuturesClient();
    }
}
