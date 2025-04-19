package zaico.client.binance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import zaico.client.binance.dto.*;
import zaico.client.binance.dto.mapper.BinanceBalanceMapper;
import zaico.model.MarketType;
import zaico.model.WalletBalance;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;


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

    public List<WalletBalance> getSpotBalances(boolean nonZeroOnly) {
        var balances = getSpotBalances();
        return nonZeroOnly
                ? balances.stream().filter(b -> b.total().signum() > 0).toList()
                : balances;
    }

    public List<WalletBalance> getFuturesBalances(FuturesType type) {
        try {
            String raw = signedRequest(() ->
                    (type == FuturesType.USDT)
                    ? uFuturesClient.account().accountInformation(new LinkedHashMap<>(Map.of()))
                    : cFuturesClient.account().accountInformation(new LinkedHashMap<>(Map.of()))
            );

            JsonNode node = objectMapper.readTree(raw);
            List<BinanceFuturesBalance> assets = objectMapper.readValue(
                    node.get("assets").toString(),
                    new TypeReference<>() {}
            );

            MarketType marketType = type.toMarketType();
            return assets.stream()
                    .map(dto -> BinanceBalanceMapper.fromFutures(dto, marketType))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch futures balances", e);
        }
    }

    public List<WalletBalance> getFuturesBalances(FuturesType type, boolean nonZeroOnly) {
        var balances = getFuturesBalances(type);
        return nonZeroOnly
                ? balances.stream().filter(b -> b.total().signum() > 0).toList()
                : balances;
    }

    public List<WalletBalance> getEarnBalances() {
        try {
            // Получаем позиции Earn: заблокированные и гибкие
            var lockedJson = spotClient.createSimpleEarn().lockedProductPosition(new LinkedHashMap<>());
            var flexibleJson = spotClient.createSimpleEarn().flexibleProductPosition(new LinkedHashMap<>());

            // Парсим обе части
            var locked = objectMapper.readValue(lockedJson, BinanceEarnPositionResponse.class).rows();
            var flexible = objectMapper.readValue(flexibleJson, BinanceEarnPositionResponse.class).rows();

            // Объединяем и мапим в WalletBalance
            return Stream.concat(locked.stream(), flexible.stream())
                    .map(BinanceBalanceMapper::fromEarn)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to fetch Earn balances", e);
        }
    }


    public List<WalletBalance> getEarnBalances(boolean nonZeroOnly) {
        var balances = getEarnBalances();
        return nonZeroOnly
                ? balances.stream().filter(b -> b.total().signum() > 0).toList()
                : balances;
    }
}
