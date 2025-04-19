package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record BinanceEarnBalance(
        long positionId,
        String projectId,
        String asset,
        BigDecimal amount,
        BigDecimal totalAmount,
        long purchaseTime,
        int duration,
        int accrualDays,
        String rewardAsset,
        BigDecimal rewardAmt,
        BigDecimal nextPay,
        long nextPayDate,
        int payPeriod,
        BigDecimal redeemAmountEarly,
        long rewardsEndDate,
        long deliverDate,
        int redeemPeriod,
        boolean canRedeemEarly,
        boolean autoSubscribe,
        String type,
        String status,
        boolean canReStake,
        BigDecimal totalBoostRewardAmt,
        BigDecimal apy
) {
    public BigDecimal balance() {
        return amount == null ? totalAmount : amount;
    }
}
