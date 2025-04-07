package zaico.client.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.futures.client.impl.CMFuturesClientImpl;
import com.binance.connector.futures.client.impl.FuturesClientImpl;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import jakarta.inject.Singleton;
import io.micronaut.context.annotation.Value;

@Singleton
public class BinanceClientProvider {

    private final SpotClientImpl spotClient;
    private final UMFuturesClientImpl uFuturesClient;
    private final CMFuturesClientImpl cFuturesClient;

    public BinanceClientProvider(@Value("${binance.api-key}") String apiKey,
                                 @Value("${binance.api-secret}") String apiSecret) {
        this.spotClient = new SpotClientImpl(apiKey, apiSecret);
        this.uFuturesClient = new UMFuturesClientImpl(apiKey, apiSecret);
        this.cFuturesClient = new CMFuturesClientImpl(apiKey, apiSecret);
    }

    public SpotClientImpl getSpotClient() {
        return spotClient;
    }

    public UMFuturesClientImpl getuFuturesClient() {
        return uFuturesClient;
    }

    public CMFuturesClientImpl getcFuturesClient() {
        return cFuturesClient;
    }
}

