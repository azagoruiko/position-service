package zaico.client.binance.dto;

import zaico.model.FundingEntry;

import java.time.Instant;

public class BinanceFundingMapper {

    public static FundingEntry fromDto(FuturesFundingEntry dto) {
        return new FundingEntry(
                dto.symbol(),
                dto.income(),
                dto.asset(),
                Instant.ofEpochMilli(dto.time()),
                dto.info(),
                dto.tranId()
        );
    }
}
