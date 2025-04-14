package zaico.client.binance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.*;
import zaico.model.MarketType;
import zaico.model.WalletBalance;
import zaico.model.WalletTransaction;

import java.util.*;

import static zaico.client.binance.dto.BinanceBalanceMapper.*;

@Singleton
public class BinanceBalanceService extends AbstractBinanceService {

    @Inject ObjectMapper objectMapper;

    @Inject
    public BinanceBalanceService(BinanceClientProvider clientProvider) {
        super(clientProvider);
    }

    public List<WalletBalance> getSpotBalances() {
        try {
            String raw = spotClient.createTrade().account(new LinkedHashMap<>(Map.of()));
            JsonNode node = objectMapper.readTree(raw);
            List<BinanceSpotBalance> balances = objectMapper.readValue(
                    node.get("balances").toString(),
                    new TypeReference<>() {}
            );
            return balances.stream().map(BinanceBalanceMapper::fromSpot).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch spot balances", e);
        }
    }

    public List<WalletBalance> getFuturesBalances(FuturesType type) {
        try {
            String raw = (type == FuturesType.USDT)
                    ? uFuturesClient.account().accountInformation(new LinkedHashMap<>(Map.of()))
                    : cFuturesClient.account().accountInformation(new LinkedHashMap<>(Map.of()));

            JsonNode node = objectMapper.readTree(raw);
            List<BinanceFuturesBalance> assets = objectMapper.readValue(
                    node.get("assets").toString(),
                    new TypeReference<>() {}
            );

            MarketType marketType = type.toMarketType();
            return assets.stream()
                    .map(dto -> fromFutures(dto, marketType))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch futures balances", e);
        }
    }

    public List<WalletBalance> getEarnBalances() {
        try {
            String raw = spotClient.createSimpleEarn().simpleAccount(new LinkedHashMap<>());

            List<BinanceEarnBalance> balances = objectMapper.readValue(
                    raw, new TypeReference<>() {}
            );

            return balances.stream()
                    .map(BinanceBalanceMapper::fromEarn)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Earn balances", e);
        }
    }
}
