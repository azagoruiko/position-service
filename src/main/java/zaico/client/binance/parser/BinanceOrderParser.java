package zaico.client.binance.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import zaico.client.binance.dto.FuturesOrder;
import zaico.client.binance.dto.SpotOrder;

import java.io.IOException;
import java.util.List;

public class BinanceOrderParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<SpotOrder> parseSpotOrders(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse spot orders", e);
        }
    }

    public static List<FuturesOrder> parseFuturesOrders(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse futures orders", e);
        }
    }
}

