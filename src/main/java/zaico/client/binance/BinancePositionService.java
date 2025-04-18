package zaico.client.binance;

import jakarta.inject.Singleton;
import zaico.client.binance.dto.mapper.BinancePositionMapper;
import zaico.math.Pair;
import zaico.model.FuturesSnapshot;
import zaico.model.MarketType;

import java.util.*;

import static zaico.client.binance.parser.BinancePositionParser.parseFuturesPositions;

@Singleton
public class BinancePositionService extends AbstractBinanceService implements zaico.exchange.service.PositionService {

    public BinancePositionService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    private List<FuturesSnapshot> fetchFuturesPositions(FuturesType type) {
        String raw = signedRequest( () ->
                (type == FuturesType.USDT)
                ? uFuturesClient.account().positionInformation(new LinkedHashMap<>())
                : cFuturesClient.account().positionInformation(new LinkedHashMap<>())
        );

        return parseFuturesPositions(raw).stream()
                .map(p -> BinancePositionMapper.fromFutures(p, type.toMarketType()))
                .filter(p -> p.size().signum() != 0) // только открытые позиции
                .toList();
    }

    @Override
    public List<FuturesSnapshot> getOpenPositions(Pair pair) {
        List<FuturesSnapshot> all = new ArrayList<>();

        if (marketRegistry.supports(MarketType.FUTURES_USDT, BinanceSymbol.getFuturesSymbol(pair, FuturesType.USDT))) {
            all.addAll(fetchFuturesPositions(FuturesType.USDT).stream()
                    .filter(p -> p.symbol().equalsIgnoreCase(BinanceSymbol.getFuturesSymbol(pair, FuturesType.USDT)))
                    .toList());
        }

        if (marketRegistry.supports(MarketType.FUTURES_COIN, BinanceSymbol.getFuturesSymbol(pair, FuturesType.COIN))) {
            all.addAll(fetchFuturesPositions(FuturesType.COIN).stream()
                    .filter(p -> p.symbol().equalsIgnoreCase(BinanceSymbol.getFuturesSymbol(pair, FuturesType.COIN)))
                    .toList());
        }

        return all;
    }
}
