package zaico.client.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.futures.client.impl.FuturesClientImpl;

public class AbstractBinanceService {
    protected final SpotClientImpl spotClient;
    protected final FuturesClientImpl uFuturesClient;
    protected final FuturesClientImpl cFuturesClient;

    public AbstractBinanceService(BinanceClientProvider clientProvider) {
        this.spotClient = clientProvider.getSpotClient();
        this.uFuturesClient = clientProvider.getuFuturesClient();
        this.cFuturesClient = clientProvider.getcFuturesClient();
    }
}
