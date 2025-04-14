package zaico.exchange.service;

import zaico.math.Pair;
import zaico.model.FuturesSnapshot;

import java.util.List;

public interface PositionService {
    List<FuturesSnapshot> getOpenPositions(Pair pair);
}
