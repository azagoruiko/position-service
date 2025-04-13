package zaico.client.binance;

import zaico.model.MarketType;

public enum FuturesType {
    USDT,
    COIN;
    public MarketType toMarketType() {
        return switch (this) {
            case USDT -> MarketType.FUTURES_USDT;
            case COIN -> MarketType.FUTURES_COIN;
        };
    }
}

