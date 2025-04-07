package zaico.math;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    Pair pair = new Pair("BTC", "USDT", new BigDecimal("0.001")); // комиссия 0.1%

    @Test
    void testBuyWithQuoteCurrency() {
        BigDecimal usdt = new BigDecimal("10000");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal btcReceived = pair.buy(usdt, "USDT", price);
        BigDecimal expected = usdt.divide(price, Calc.SCALE, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.ONE.subtract(pair.commission()));

        assertEquals(0, expected.compareTo(btcReceived));
    }

    @Test
    void testBuyWithAssetCurrency() {
        BigDecimal btc = new BigDecimal("0.5");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal usdtPaid = pair.buy(btc, "BTC", price);
        BigDecimal expected = btc.multiply(price).multiply(BigDecimal.ONE.subtract(pair.commission()));

        assertEquals(0, expected.compareTo(usdtPaid));
    }

    @Test
    void testSellWithQuoteCurrency() {
        BigDecimal usdt = new BigDecimal("10000");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal btcReceived = pair.sell(usdt, "USDT", price); // эквивалентен buy(btc, "BTC", price)
        BigDecimal expected = usdt.divide(price, Calc.SCALE, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.ONE.subtract(pair.commission()));

        assertEquals(0, expected.compareTo(btcReceived));
    }

    @Test
    void testSellWithAssetCurrency() {
        BigDecimal btc = new BigDecimal("0.5");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal usdtReceived = pair.sell(btc, "BTC", price); // эквивалентен buy(usdt, "USDT", price)
        BigDecimal expected = btc.multiply(price).multiply(BigDecimal.ONE.subtract(pair.commission()));

        assertEquals(0, expected.compareTo(usdtReceived));
    }

    @Test
    void testInvalidCurrency() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                pair.buy(new BigDecimal("1"), "ETH", new BigDecimal("1000"))
        );
        assertTrue(ex.getMessage().contains("is not a valid asset"));
    }

    @Test
    void testBuyWithQuoteAlias() {
        BigDecimal usdt = new BigDecimal("10000");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal expectedBtc = Calc.amount(usdt, price, pair.commission());
        BigDecimal actualBtc = pair.buyWithQuote(usdt, price);

        assertEquals(0, expectedBtc.compareTo(actualBtc));
    }

    @Test
    void testSellForQuoteAlias() {
        BigDecimal btc = new BigDecimal("0.5");
        BigDecimal price = new BigDecimal("20000");

        BigDecimal expectedUsdt = Calc.size(btc, price, pair.commission());
        BigDecimal actualUsdt = pair.sellForQuote(btc, price);

        assertEquals(0, expectedUsdt.compareTo(actualUsdt));
    }
}
