package zaico.client.binance;

import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class BinanceHistoryService extends AbstractBinanceService {


    public BinanceHistoryService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    public String getFuturesOrders(String symbol) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        return uFuturesClient.account().allOrders(params);
    }

    public String getFuturesTrades(String symbol) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        return uFuturesClient.account().accountTradeList(params);
    }
}
