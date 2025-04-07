package zaico.factory;

import jakarta.inject.Singleton;
import zaico.client.binance.dto.BinanceFuturesPosition;
import zaico.model.FuturesSnapshot;
import zaico.model.PositionSide;

@Singleton
public class FuturesSnapshotFactory {

    public FuturesSnapshot fromBinance(BinanceFuturesPosition dto) {
        return new FuturesSnapshot(
                dto.symbol(),
                dto.positionAmt(),
                dto.entryPrice(),
                dto.markPrice(),
                dto.unRealizedProfit(),
                dto.leverage(),
                dto.isolated(),
                PositionSide.valueOf(dto.positionSide().toUpperCase()),
                dto.updateTime()
        );
    }
}
