package zaico.client.binance.dto;

import java.util.List;

public record BinanceEarnPositionResponse(
        int total,
        List<BinanceEarnBalance> rows
) {}

