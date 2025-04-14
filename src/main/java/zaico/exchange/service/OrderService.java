package zaico.exchange.service;

import zaico.math.Pair;
import zaico.model.Order;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getOrders(Pair pair);

    List<Order> getOrders(Pair pair, Instant startTime);

    List<Order> getOrders(Pair pair, Optional<Instant> startTime);
}
