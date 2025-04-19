package zaico.model;

import io.micronaut.serde.annotation.Serdeable;

import java.util.*;

@Serdeable
public final class OrderExecution implements HistoryItem {
    private Order order;
    private final Set<Trade> trades = new HashSet<>();

    public OrderExecution(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public Order setOrder(Order order) {
        if (!Objects.equals(order.getId(), this.order.getId())) {
            throw new IllegalArgumentException("Replacisng existing order is not allowed");
        }
        this.order = order;
        return order;
    }

    public Set<Trade> getTrades() {
        return Collections.unmodifiableSet(trades);
    }

    public void addTrade(Trade trade) {
        if (!Objects.equals(trade.sourceOrderId(), String.valueOf(order.orderId()))) {
            throw new IllegalArgumentException("Trade does not belong to this order");
        }
        trades.add(trade);
    }

    public void addTrades(Collection<Trade> tradeList) {
        tradeList.forEach(this::addTrade);
    }

    @Override
    public long closedAt() {
        return order.updateTime().toEpochMilli();
    }

    @Override
    public long openedAt() {
        return order.updateTime().toEpochMilli();
    }

    @Override
    public String toString() {
        return "OrderWithTrades{" +
                "order=" + order +
                ", trades=" + trades.size() +
                '}';
    }
}
