package zaico.client.binance.dto;

import zaico.model.MarketType;
import zaico.model.WalletBalance;
import zaico.model.WalletTransaction;
import zaico.model.WalletTransactionType;

import java.math.BigDecimal;

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

    public static WalletBalance fromEarn(BinanceEarnBalance dto) {
        return new WalletBalance(
                "ALL",
                dto.totalAmountInUSDT(),
                dto.totalAmountInUSDT(),
                MarketType.EARN
        );
    }

    public static WalletTransaction fromAssetTransfer(BinanceAssetTransfer dto) {
        return new WalletTransaction(
                dto.asset(),
                new BigDecimal(dto.amount()),
                WalletTransactionType.from(dto.type()),
                dto.status(),
                dto.time()
        );
    }

}
