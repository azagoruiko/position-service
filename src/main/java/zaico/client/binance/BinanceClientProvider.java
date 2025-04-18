package zaico.client.binance;

import com.binance.connector.client.enums.DefaultUrls;
import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.binance.connector.futures.client.impl.CMFuturesClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.binance.connector.client.utils.signaturegenerator.SignatureGenerator;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.infra.TimeProvider;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Singleton
public class BinanceClientProvider {

    private final SpotClientImpl spotClient;
    private final UMFuturesClientImpl uFuturesClient;
    private final CMFuturesClientImpl cFuturesClient;
    private final SignatureGeneratorWithOffset signatureGenerator;
    private volatile long timeOffset;


    public BinanceClientProvider(@Value("${binance.api-key}") String apiKey,
                                 @Value("${binance.api-secret}") String apiSecret) {

        long offset = fetchServerTimeOffset();
        this.signatureGenerator = new SignatureGeneratorWithOffset(apiSecret, offset);

        this.spotClient = new SpotClientImpl(apiKey, signatureGenerator, DefaultUrls.PROD_URL);
        this.uFuturesClient = new UMFuturesClientImpl(apiKey, apiSecret); // пока без генератора
        this.cFuturesClient = new CMFuturesClientImpl(apiKey, apiSecret); // пока без генератора
    }

    private long fetchServerTimeOffset() {
        try {
            long localBefore = System.currentTimeMillis();
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.binance.com/api/v3/time").openConnection();
            connection.setRequestMethod("GET");

            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> response = mapper.readValue(is, new TypeReference<>() {});
                long serverTime = ((Number) response.get("serverTime")).longValue();
                long localAfter = System.currentTimeMillis();
                return serverTime - ((localBefore + localAfter) / 2);
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to fetch Binance server time offset", e);
        }
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

    public SignatureGeneratorWithOffset getSignatureGenerator() {
        return signatureGenerator;
    }

    public void resyncTimeOffset() {
        this.timeOffset = fetchServerTimeOffset();
    }
}
