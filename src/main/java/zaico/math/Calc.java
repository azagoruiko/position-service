package zaico.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calc {
    public static final int SCALE = 16;
    public static BigDecimal size(BigDecimal amount, BigDecimal price, BigDecimal commission) {
        return amount.multiply(price).multiply(BigDecimal.ONE.subtract(commission));
    }

    public static BigDecimal size(BigDecimal amount, BigDecimal price) {
        return size(amount, price, BigDecimal.ZERO);
    }

    public static BigDecimal amount(BigDecimal size, BigDecimal price, BigDecimal commission) {
        return size.divide(price).multiply(BigDecimal.ONE.subtract(commission));
    }

    public static BigDecimal amount(BigDecimal size, BigDecimal price) {
        return amount(size, price, BigDecimal.ZERO);
    }

    public static BigDecimal change(BigDecimal from, BigDecimal to) {
        return to.subtract(from).divide(from, SCALE, RoundingMode.HALF_UP);
    }

}
