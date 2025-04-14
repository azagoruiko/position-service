package zaico.exchange.service;

import zaico.math.Pair;
import zaico.model.FundingEntry;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface FundingService {
    List<FundingEntry> getFunding(Pair pair);

    List<FundingEntry> getFunding(Pair pair, Instant from);

    List<FundingEntry> getFunding(Pair pair, Optional<Instant> from);

}
