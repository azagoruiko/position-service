package zaico.exchange.service;

import zaico.client.binance.FuturesType;
import zaico.client.binance.dto.BinanceFundingRate;

import java.math.BigDecimal;

public interface MarketDataService {
    BigDecimal getSpotTickerPrice(String symbol);

    BigDecimal getFuturesTickerPrice(FuturesType type, String symbol);

    BinanceFundingRate getFundingRate(FuturesType type, String symbol);
}
