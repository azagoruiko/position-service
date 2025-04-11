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

    public BigDecimal buyWithQuote(BigDecimal quoteAmount, BigDecimal price) {
        return buy(quoteAmount, quote, price);
    }

    public BigDecimal sellForQuote(BigDecimal assetAmount, BigDecimal price) {
        return sell(assetAmount, asset, price);
    }


    public String name() {
        return String.format("%s_%s", asset, quote);
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Pair p) && asset.equals(p.asset) && quote.equals(p.quote);
    }

    @Override
    public int hashCode() {
        return 31 * asset.hashCode() + quote.hashCode();
    }
}
