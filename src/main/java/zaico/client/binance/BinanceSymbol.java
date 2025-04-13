package zaico.client.binance;

import zaico.math.Pair;

public class BinanceSymbol {
    public static String getSpotSymbol(Pair pair) {
        return getSpotSymbol(pair.asset(), pair.quote());
    }

    public static String getSpotSymbol(String asset, String quote) {
        return String.format("%s%s", asset, quote);
    }

    public static String getFuturesSymbol(String asset, String quote, FuturesType type) {
        return switch (type) {
            case USDT -> asset + quote;             // BTCUSDT
            case COIN -> asset + "USD_PERP"; // BTCUSD_PERP
        };
    }
    public static String getFuturesSymbol(Pair pair, FuturesType type) {
        return getFuturesSymbol(pair.asset(), pair.quote(), type);
    }

}
