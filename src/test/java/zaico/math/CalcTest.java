package zaico.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
class CalcTest {

    @Test
    void testSizeWithoutCommission() {
        BigDecimal amount = new BigDecimal("2");
        BigDecimal price = new BigDecimal("10000");
        BigDecimal expected = new BigDecimal("20000.00");

        BigDecimal actual = Calc.size(amount, price);

        assertEquals(0, expected.compareTo(actual));
    }

    @Test
    void testSizeWithCommission() {
        BigDecimal amount = new BigDecimal("2");
        BigDecimal price = new BigDecimal("10000");
        BigDecimal commission = new BigDecimal("0.001"); // 0.1%

        BigDecimal expected = new BigDecimal("19980.00");
        BigDecimal actual = Calc.size(amount, price, commission);

        assertEquals(0, expected.compareTo(actual));
    }

    @Test
    void testAmountWithoutCommission() {
        BigDecimal size = new BigDecimal("20000");
        BigDecimal price = new BigDecimal("10000");

        BigDecimal expected = new BigDecimal("2.00000000");
        BigDecimal actual = Calc.amount(size, price);

        assertEquals(0, expected.compareTo(actual.setScale(8, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    void testAmountWithCommission() {
        BigDecimal size = new BigDecimal("20000");
        BigDecimal price = new BigDecimal("10000");
        BigDecimal commission = new BigDecimal("0.001");

        BigDecimal expected = new BigDecimal("1.998000"); // = 2 * (1 - 0.001)
        BigDecimal actual = Calc.amount(size, price, commission);

        assertEquals(0, expected.compareTo(actual.setScale(6, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    void testChange() {
        BigDecimal from = new BigDecimal("10000");
        BigDecimal to = new BigDecimal("11000");

        BigDecimal expected = new BigDecimal("0.1"); // 10% рост
        BigDecimal actual = Calc.change(from, to);

        assertEquals(0, expected.compareTo(actual.setScale(1, BigDecimal.ROUND_HALF_UP)));
    }

}
