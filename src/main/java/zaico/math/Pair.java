package zaico.math;

import java.math.BigDecimal;

public record Pair(String asset, String quote, BigDecimal commission) {

    public BigDecimal buy(BigDecimal amount, String of, BigDecimal price) {
        if (of.equals(asset)) {
            return Calc.size(amount, price, commission);
        } else if (of.equals(quote)) {
            return Calc.amount(amount, price, commission);
        } else throw new IllegalArgumentException(String.format("%s, is not a valid asset of %s", of, this));
    }

    public BigDecimal sell(BigDecimal amount, String of, BigDecimal price) {
        if (of.equals(asset)) {
            return buy(amount, asset, price);
        } else if (of.equals(quote)) {
            return buy(amount, quote, price);
        } else throw new IllegalArgumentException(String.format("%s, is not a valid asset of %s", of, this));
    }

    @Override
    public String toString() {
        return "Pair{" +
                "asset='" + asset + '\'' +
                ", quote='" + quote + '\'' +
                ", commission=" + commission +
                '}';
    }
}
