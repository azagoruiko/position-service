package zaico.client.binance;

import com.binance.connector.client.utils.signaturegenerator.SignatureGenerator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

public class SignatureGeneratorWithOffset implements SignatureGenerator {

    private final String secretKey;
    private final long offset;
    private final AtomicLong lastTimestamp = new AtomicLong(0);

    public SignatureGeneratorWithOffset(String secretKey, long offset) {
        this.secretKey = secretKey;
        this.offset = offset;
    }

    public long getTimestamp() {
        long now = System.currentTimeMillis() + offset;
        return lastTimestamp.updateAndGet(prev -> Math.max(prev + 1, now));
    }

    @Override
    public String getSignature(String queryString) {
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] hash = hmacSHA256.doFinal(queryString.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign message", e);
        }
    }
}
