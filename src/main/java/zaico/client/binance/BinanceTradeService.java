package zaico.client.binance;

import jakarta.inject.Singleton;
import zaico.client.binance.dto.FuturesTrade;
import zaico.client.binance.parser.BinanceTradeParser;
import zaico.math.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BinanceTradeService extends AbstractBinanceService {

    public BinanceTradeService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    public String getSpotTrades(String symbol) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>(Map.of("symbol", symbol));
        return spotClient.createTrade().myTrades(params);
    }

    public List<FuturesTrade> getFuturesTrades(Pair pair) {
        LinkedHashMap<String, Object> params =
                new LinkedHashMap<>(Map.of("symbol", BinanceSymbol.getFuturesSymbol(pair, FuturesType.USDT)));
        var rawTrades = uFuturesClient.account().accountTradeList(params);
        List<FuturesTrade> trades = BinanceTradeParser.parseFuturesTrades(rawTrades);

        params = new LinkedHashMap<>(Map.of("symbol", BinanceSymbol.getFuturesSymbol(pair, FuturesType.COIN)));
        rawTrades = cFuturesClient.account().accountTradeList(params);
        trades.addAll(BinanceTradeParser.parseFuturesTrades(rawTrades));

        rawTrades = getSpotTrades(BinanceSymbol.getSpotSymbol(pair));
        trades.addAll(BinanceTradeParser.parseFuturesTrades(rawTrades));

        return trades;
    }
}
