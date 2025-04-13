package zaico.client.binance.dto;

import zaico.model.MarketType;
import zaico.model.WalletBalance;

public class BinanceBalanceMapper {

    public static WalletBalance fromSpot(BinanceSpotBalance dto) {
        return new WalletBalance(
                dto.asset(),
                dto.free().add(dto.locked()),
                dto.free(),
                MarketType.SPOT
        );
    }

    public static WalletBalance fromFutures(BinanceFuturesBalance dto, MarketType type) {
        return new WalletBalance(
                dto.asset(),
                dto.walletBalance(),
                dto.availableBalance(),
                type
        );
    }
}
