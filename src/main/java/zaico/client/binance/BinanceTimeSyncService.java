package zaico.client.binance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

@Singleton
public class BinanceTimeSyncService {
    private volatile long offset = 0;

    @Inject ObjectMapper objectMapper;

    public long getOffset() {
        return offset;
    }

    public long getSyncedTimestamp() {
        return System.currentTimeMillis() + offset;
    }

    @PostConstruct
    public void syncTime() {
        try {
            long localBefore = System.currentTimeMillis();
            URL url = new URL("https://api.binance.com/api/v3/time");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            JsonNode json = objectMapper.readTree(conn.getInputStream());
            long serverTime = json.get("serverTime").asLong();
            long localAfter = System.currentTimeMillis();

            long localAvg = (localBefore + localAfter) / 2;
            offset = serverTime - localAvg;

            System.out.println("🔧 Binance time sync: offset = " + offset + " ms (" + Instant.ofEpochMilli(serverTime) + ")");
        } catch (Exception e) {
            throw new RuntimeException("Failed to sync Binance time", e);
        }
    }
}
