package zaico.client.binance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.FuturesAccountInfo;
//import zaico.client.binance.dto.FuturesAccountInfo;
//import zaico.client.binance.dto.SpotAccountInfo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BinanceCommissionService {

    private final BinanceClientProvider clientProvider;
    private final ObjectMapper objectMapper;

    @Inject
    public BinanceCommissionService(BinanceClientProvider clientProvider, ObjectMapper objectMapper) {
        this.clientProvider = clientProvider;
        this.objectMapper = objectMapper;
    }

    public BigDecimal getSpotTakerFee(String symbol) {
        String raw = clientProvider.getSpotClient()
                .createTrade()
                .commission(new LinkedHashMap<>(Map.of("symbol", symbol)));

        try {
            Map<String, Object> root = objectMapper.readValue(raw, new TypeReference<>() {});
            Map<String, String> standardCommission = (Map<String, String>) root.get("standardCommission");

            if (standardCommission == null || !standardCommission.containsKey("taker")) {
                throw new RuntimeException("No 'taker' fee found in standardCommission");
            }

            return new BigDecimal(standardCommission.get("taker"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse spot taker commission for " + symbol, e);
        }
    }


    public BigDecimal getFuturesTakerFee(FuturesType type, String symbol) {
        var client = (type == FuturesType.USDT)
                ? clientProvider.getuFuturesClient()
                : clientProvider.getcFuturesClient();

        String raw = client.account().getCommissionRate(new LinkedHashMap<>(Map.of("symbol", symbol)));

        try {
            FuturesAccountInfo info = objectMapper.readValue(raw, new TypeReference<>() {});
            return info.takerCommission();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse futures commission for type " + type, e);
        }
    }


}