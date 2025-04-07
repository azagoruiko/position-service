package zaico.endpoints;

import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import zaico.client.binance.BinanceClientProvider;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import zaico.model.FuturesSnapshot;

@Controller("/futures")
public class PositionsController {
    @Inject ObjectMapper mapper;

    @Inject
    BinanceClientProvider binanceClientProvider;

    @Get("/positions")
    public List<FuturesSnapshot> getPositions() throws JsonProcessingException {
        UMFuturesClientImpl client = binanceClientProvider.getuFuturesClient();
        String rawJson = client
                .account().positionInformation(new LinkedHashMap<>());
        List<FuturesSnapshot> allPositions = mapper.readValue(
                rawJson, new TypeReference<>() {}
        );

        List<FuturesSnapshot> positions = allPositions.stream()
                .filter(p -> p.positionAmt().compareTo(BigDecimal.ZERO) != 0)
                .toList();

        return positions;
    }
}

