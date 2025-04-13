package zaico.client.binance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotOrder(
        String symbol,
        long orderId,
        long orderListId,
        String clientOrderId,
        BigDecimal price,
        BigDecimal origQty,
        BigDecimal executedQty,
        BigDecimal cummulativeQuoteQty,
        String status,
        String timeInForce,
        String type,
        String side,
        BigDecimal stopPrice,
        BigDecimal icebergQty,
        long time,
        long updateTime,
        boolean isWorking
) {}
