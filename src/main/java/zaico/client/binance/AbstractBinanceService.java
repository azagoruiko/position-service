package zaico.client.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.futures.client.impl.FuturesClientImpl;
import jakarta.inject.Inject;
import zaico.model.MarketType;

public class AbstractBinanceService {
    protected final SpotClientImpl spotClient;
    protected final FuturesClientImpl uFuturesClient;
    protected final FuturesClientImpl cFuturesClient;

    @Inject
    MarketRegistry marketRegistry;

// потом в коде:
    protected boolean symbolExists(String symbol) {
        return  marketRegistry.supports("BTCUSDT");
    }
    public AbstractBinanceService(BinanceClientProvider clientProvider) {
        this.spotClient = clientProvider.getSpotClient();
        this.uFuturesClient = clientProvider.getuFuturesClient();
        this.cFuturesClient = clientProvider.getcFuturesClient();
    }
}
