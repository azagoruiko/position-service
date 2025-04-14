package zaico.exchange.service;

import zaico.client.binance.FuturesType;

import java.math.BigDecimal;

public interface CommissionService {
    BigDecimal getSpotTakerFee(String symbol);

    BigDecimal getFuturesTakerFee(FuturesType type, String symbol);
}
