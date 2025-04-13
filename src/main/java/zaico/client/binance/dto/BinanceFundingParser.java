package zaico.client.binance.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class BinanceFundingParser {
    public static List<FuturesFundingEntry> parseFundingEntries(String rawJson, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(rawJson, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse funding entries", e);
        }
    }
}
