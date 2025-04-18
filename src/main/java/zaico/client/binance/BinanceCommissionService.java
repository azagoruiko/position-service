package zaico.client.binance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.FuturesAccountInfo;

import java.math.BigDecimal;
import java.util.*;

@Singleton
public class BinanceCommissionService extends AbstractBinanceService implements zaico.exchange.service.CommissionService {

    @Inject ObjectMapper objectMapper;

    private BigDecimal globalSpotTakerFee = null;
    private final Map<String, Map<FuturesType, BigDecimal>> futuresTakerFees = new HashMap<>();

    public BinanceCommissionService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    @Override
    public synchronized BigDecimal getSpotTakerFee(String symbol) {
        if (globalSpotTakerFee == null) {
            fetchGlobalSpotFee(); // один раз на всё приложение
        }
        return globalSpotTakerFee;
    }

    private void fetchGlobalSpotFee() {
        try {
            String raw = signedRequest(() -> spotClient.createTrade()
                    .account(new LinkedHashMap<>()));

            JsonNode root = objectMapper.readTree(raw);
            JsonNode commissionNode = root.path("commissionRates").path("taker");

            if (commissionNode.isMissingNode()) {
                throw new RuntimeException("No taker commission found in /account response");
            }

            globalSpotTakerFee = new BigDecimal(commissionNode.asText());
            System.out.printf("✅ Global SPOT taker fee loaded: %s%n", globalSpotTakerFee);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch global SPOT taker commission", e);
        }
    }

    @Override
    public synchronized BigDecimal getFuturesTakerFee(FuturesType type, String symbol) {
        return futuresTakerFees
                .computeIfAbsent(symbol, k -> new EnumMap<>(FuturesType.class))
                .computeIfAbsent(type, t -> fetchFuturesTakerFee(type, symbol));
    }

    private BigDecimal fetchFuturesTakerFee(FuturesType type, String symbol) {
        var client = (type == FuturesType.USDT)
                ? clientProvider.getuFuturesClient()
                : clientProvider.getcFuturesClient();

        try {
            String raw = signedRequest(() -> client.account().getCommissionRate(
                    new LinkedHashMap<>(Map.of("symbol", symbol)))
            );
            FuturesAccountInfo info = objectMapper.readValue(raw, FuturesAccountInfo.class);
            return info.takerCommission();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch FUTURES commission for " + symbol + " (" + type + ")", e);
        }
    }

    public synchronized void refreshAll() {
        globalSpotTakerFee = null;
        futuresTakerFees.clear();
    }
}
