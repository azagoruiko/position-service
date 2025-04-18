package zaico.service;

import jakarta.inject.Singleton;
import zaico.math.Pair;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class CommissionCache {
    private final Map<Pair, BigDecimal> commissionMap = new ConcurrentHashMap<>();

    public void put(Pair pair, BigDecimal commission) {
        commissionMap.put(pair, commission);
    }

    public BigDecimal get(Pair pair) {
        return commissionMap.getOrDefault(pair, BigDecimal.ZERO); // если комиссии нет, будет 0
    }

    public boolean has(Pair pair) {
        return commissionMap.containsKey(pair);
    }

    public void clear() {
        commissionMap.clear();
    }

    public Map<Pair, BigDecimal> all() {
        return commissionMap;
    }
}

