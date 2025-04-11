package zaico.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import zaico.client.binance.BinanceTradeService;
import zaico.client.binance.dto.FuturesTrade;
import zaico.math.Pair;

import java.math.BigDecimal;
import java.util.List;

@Controller("/futures")
public class TradesController {
    @Inject ObjectMapper mapper;

    @Inject
    BinanceTradeService binanceTradeService;

    @Get("/trades")
    public List<FuturesTrade> getTrades() throws JsonProcessingException {

        return binanceTradeService.getFuturesTrades(new Pair("SOL", "USDT", BigDecimal.valueOf(0.001d)));
    }
}

