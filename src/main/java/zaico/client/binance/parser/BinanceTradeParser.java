package zaico.client.binance.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import zaico.client.binance.dto.FuturesTrade;

import java.io.IOException;
import java.util.List;

public class BinanceTradeParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<FuturesTrade> parseFuturesTrades(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse futures trades JSON", e);
        }
    }
}
