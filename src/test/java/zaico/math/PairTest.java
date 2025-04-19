package zaico.math;

import org.junit.jupiter.api.Test;
import zaico.exchange.Platform;
import zaico.model.MarketType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {
    private BigDecimal commission = new BigDecimal("0.001");
    Pair pair = new Pair(Platform.BINANCE, MarketType.SPOT, "BTC", "USDT"); // комиссия 0.1%

    @Test
    void testBuyWithQuoteCurrency() {
        BigDecimal usdt = new BigDecimal("10000");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal btcReceived = pair.buy(usdt, "USDT", price, commission);
        BigDecimal expected = usdt.divide(price, Calc.SCALE, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.ONE.subtract(commission));

        assertEquals(0, expected.compareTo(btcReceived));
    }

    @Test
    void testBuyWithAssetCurrency() {
        BigDecimal btc = new BigDecimal("0.5");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal usdtPaid = pair.buy(btc, "BTC", price, commission);
        BigDecimal expected = btc.multiply(price).multiply(BigDecimal.ONE.subtract(commission));

        assertEquals(0, expected.compareTo(usdtPaid));
    }

    @Test
    void testSellWithQuoteCurrency() {
        BigDecimal usdt = new BigDecimal("10000");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal btcReceived = pair.sell(usdt, "USDT", price, commission); // эквивалентен buy(btc, "BTC", price)
        BigDecimal expected = usdt.divide(price, Calc.SCALE, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.ONE.subtract(commission));

        assertEquals(0, expected.compareTo(btcReceived));
    }

    @Test
    void testSellWithAssetCurrency() {
        BigDecimal btc = new BigDecimal("0.5");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal usdtReceived = pair.sell(btc, "BTC", price, commission); // эквивалентен buy(usdt, "USDT", price)
        BigDecimal expected = btc.multiply(price).multiply(BigDecimal.ONE.subtract(commission));

        assertEquals(0, expected.compareTo(usdtReceived));
    }

    @Test
    void testInvalidCurrency() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                pair.buy(new BigDecimal("1"), "ETH", new BigDecimal("1000"), commission)
        );
        assertTrue(ex.getMessage().contains("is not a valid asset"));
    }

    @Test
    void testBuyWithQuoteAlias() {
        BigDecimal usdt = new BigDecimal("10000");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal expectedBtc = Calc.amount(usdt, price, commission);
        BigDecimal actualBtc = pair.buyWithQuote(usdt, price, commission);

        assertEquals(0, expectedBtc.compareTo(actualBtc));
    }

    @Test
    void testSellForQuoteAlias() {
        BigDecimal btc = new BigDecimal("0.5");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal expectedUsdt = Calc.size(btc, price, commission);
        BigDecimal actualUsdt = pair.sellForQuote(btc, price, commission);

        assertEquals(0, expectedUsdt.compareTo(actualUsdt));
    }
}
