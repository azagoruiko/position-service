package zaico.client.binance;

import com.binance.connector.client.exceptions.BinanceClientException;
import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.futures.client.impl.CMFuturesClientImpl;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import jakarta.inject.Inject;
import zaico.exchange.service.MarketRegistry;

import java.util.function.Supplier;

public abstract class AbstractBinanceService {

    protected final SpotClientImpl spotClient;
    protected final UMFuturesClientImpl uFuturesClient;
    protected final CMFuturesClientImpl cFuturesClient;

    @Inject MarketRegistry marketRegistry;

    @Inject BinanceClientProvider clientProvider;

    public AbstractBinanceService(BinanceClientProvider clientProvider) {
        this.spotClient = clientProvider.getSpotClient();
        this.uFuturesClient = clientProvider.getuFuturesClient();
        this.cFuturesClient = clientProvider.getcFuturesClient();
    }

    protected synchronized String signedRequest(Supplier<String> request) {
        final int maxRetries = 8;
        int attempts = 0;

        while (true) {
            try {
                return request.get();
            } catch (BinanceClientException ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("\"code\":-1021") && attempts < maxRetries) {
                    System.err.println("⏱ Desync detected (-1021). Resyncing time...");
                    attempts++;
                    clientProvider.resyncTimeOffset(); // <- обновим offset
                    sleepBackoff(attempts);            // <- небольшая задержка
                } else {
                    throw ex;
                }
            } catch (RuntimeException ex) {
                throw ex;
            }
        }
    }

    private void sleepBackoff(int attempts) {
        try {
            Thread.sleep(300L * attempts); // плавный бэкофф
        } catch (InterruptedException ignored) {}
    }
}
