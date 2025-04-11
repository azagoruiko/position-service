package zaico.client.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.futures.client.impl.FuturesClientImpl;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.FuturesTrade;
import zaico.client.binance.parser.BinanceTradeParser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BinanceTradeService extends AbstractBinanceService {

    public BinanceTradeService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    public String getSpotTrades(String symbol) {
        Map<String, Object> params = Map.of("symbol", symbol);
        return spotClient.createTrade().myTrades(params);
    }

    public List<FuturesTrade> getFuturesTrades(String symbol) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>(Map.of("symbol", symbol));
        var rawTrades = uFuturesClient.account().accountTradeList(params);
        List<FuturesTrade> trades = BinanceTradeParser.parseFuturesTrades(rawTrades);
        params = new LinkedHashMap<>(Map.of("symbol", "ETHUSD_PERP"));
        rawTrades = cFuturesClient.account().accountTradeList(params);
        trades.addAll(BinanceTradeParser.parseFuturesTrades(rawTrades));
        return trades;
    }
}
