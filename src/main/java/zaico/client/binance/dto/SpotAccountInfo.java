package zaico.client.binance.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record SpotAccountInfo(
        int makerCommission,
        int takerCommission
) {}