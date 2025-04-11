package zaico.client.binance;

import zaico.math.Pair;

public class BinanceSymbol {
    public static String getSpotSymbol(Pair pair) {
        return String.format("%s%s", pair.asset(), pair.quote());
    }

    public static String getFuturesSymbol(Pair pair, FuturesType type) {
        return switch (type) {
            case USDT -> pair.asset() + pair.quote();             // BTCUSDT
            case COIN -> pair.asset() + "USD_PERP"; // BTCUSD_PERP
        };
    }
}
