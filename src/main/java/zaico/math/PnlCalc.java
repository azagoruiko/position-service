package zaico.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PnlCalc {

    public static final int SCALE = Calc.SCALE;

    public static BigDecimal basicPnl(BigDecimal assetAmount, BigDecimal entryPrice, BigDecimal price) {
        return price.subtract(entryPrice)
                .multiply(assetAmount)
                .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal spotPnl(BigDecimal assetAmount, BigDecimal entryPrice, BigDecimal price, BigDecimal tradeCommission) {
        return basicPnl(assetAmount, entryPrice, price)
                .subtract(tradeCommission)
                .setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal futurePnl(BigDecimal assetAmount, BigDecimal entryPrice, BigDecimal price, BigDecimal tradeCommission, BigDecimal fundingSum) {
        return spotPnl(assetAmount, entryPrice, price, tradeCommission)
                .subtract(fundingSum)
                .setScale(SCALE, RoundingMode.HALF_UP);
    }
}
