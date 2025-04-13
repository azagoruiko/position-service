package zaico.client.binance;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.BinanceFundingMapper;
import zaico.client.binance.dto.BinanceFundingParser;
import zaico.client.binance.dto.FuturesFundingEntry;
import zaico.math.Pair;
import zaico.model.FundingEntry;

import java.time.Instant;
import java.util.*;

@Singleton
public class BinanceFundingService extends AbstractBinanceService {

    private final ObjectMapper objectMapper;

    private List<FundingEntry> fetchFuturesFunding(Pair pair, FuturesType type, Optional<Instant> startTime) {
        String symbol = BinanceSymbol.getFuturesSymbol(pair, type);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>(Map.of(
                "symbol", symbol,
                "incomeType", "FUNDING_FEE"
        ));
        startTime.ifPresent(t -> params.put("startTime", t.toEpochMilli()));

        String raw = (type == FuturesType.USDT)
                ? uFuturesClient.account().getIncomeHistory(params)
                : cFuturesClient.account().getIncomeHistory(params);

        return BinanceFundingParser.parseFundingEntries(raw, objectMapper).stream()
                .map(BinanceFundingMapper::fromDto)
                .toList();
    }

    @Inject
    public BinanceFundingService(BinanceClientProvider clientProvider, ObjectMapper objectMapper) {
        super(clientProvider);
        this.objectMapper = objectMapper;
    }



    public List<FundingEntry> getFunding(Pair pair) {
        return getFunding(pair, Optional.empty());
    }

    public List<FundingEntry> getFunding(Pair pair, Instant from) {
        return getFunding(pair, Optional.of(from));
    }

    private List<FundingEntry> getFunding(Pair pair, Optional<Instant> from) {
        List<FundingEntry> all = new ArrayList<>();
        all.addAll(fetchFuturesFunding(pair, FuturesType.USDT, from));
        all.addAll(fetchFuturesFunding(pair, FuturesType.COIN, from));
        return all;
    }


}
