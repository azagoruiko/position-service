package zaico.math;

import zaico.exchange.Platform;
import zaico.model.MarketType;

import java.math.BigDecimal;
import java.util.Objects;

public record Pair(Platform platform, MarketType marketType, String asset, String quote, String symbol) {

    public Pair(Platform platform, MarketType marketType, String asset, String quote) {
        this(platform, marketType, asset, quote, String.format("%s%s", asset, quote));
    }

    public BigDecimal buy(BigDecimal amount, String of, BigDecimal price, BigDecimal commission) {
        if (of.equals(asset)) {
            return Calc.size(amount, price, commission);
        } else if (of.equals(quote)) {
            return Calc.amount(amount, price, commission);
        } else throw new IllegalArgumentException(String.format("%s, is not a valid asset of %s", of, this));
    }

    public BigDecimal sell(BigDecimal amount, String of, BigDecimal price, BigDecimal commission) {
        if (of.equals(asset)) {
            return buy(amount, asset, price, commission);
        } else if (of.equals(quote)) {
            return buy(amount, quote, price, commission);
        } else throw new IllegalArgumentException(String.format("%s, is not a valid asset of %s", of, this));
    }

    public BigDecimal buyWithQuote(BigDecimal quoteAmount, BigDecimal price, BigDecimal commission) {
        return buy(quoteAmount, quote, price, commission);
    }

    public BigDecimal sellForQuote(BigDecimal assetAmount, BigDecimal price, BigDecimal commission) {
        return sell(assetAmount, asset, price, commission);
    }

    public boolean isOf(String currency) {
        return asset.equals(currency) || quote.equals(currency);
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair p)) return false;
        return platform == p.platform &&
                marketType == p.marketType &&
                asset.equals(p.asset) &&
                quote.equals(p.quote) &&
                symbol.equals(p.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(platform, marketType, asset, quote, symbol);
    }

}
