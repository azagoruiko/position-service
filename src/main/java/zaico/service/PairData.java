package zaico.service;

import zaico.exchange.Platform;
import zaico.math.Pair;
import zaico.model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PairData {
    private final Pair pair;

    private final Platform platform;
    private final TreeUpdatableList<Trade> trades = new TreeUpdatableList<>();
    private final TreeUpdatableList<FundingEntry> fundingHistory = new TreeUpdatableList<>();
    private final TreeUpdatableList<Order> orders = new TreeUpdatableList<>();

    private final Map<String, OrderExecution> orderIndex = new HashMap<>();

    private WalletBalance walletBalance;

    public PairData(Pair pair, Platform platform) {
        this.pair = pair;
        this.platform = platform;
    }

    public void addOrder(Order o) {
        if (orderIndex.containsKey(o.getId())) {
            throw new IllegalArgumentException(String.format("Order %s exists", o.getId()));
        }

        orderIndex.put(o.getId(), new OrderExecution(o));
        orders.add(o);
    }

    public void updateOrder(Order o) {
        if (!orderIndex.containsKey(o.getId())) {
            throw new IllegalArgumentException(String.format("Order %s does not exists", o.getId()));
        }

        orderIndex.get(o.getId()).setOrder(o);
    }

    public void addOrder(Collection<Order> orders) {
        for (Order o : orders) {
            addOrder(o);
        }
    }

    public void addTrade(Trade t) {
        if (!orderIndex.containsKey(t.sourceOrderId())) {
            throw new IllegalArgumentException(String.format("Order %s does not exist, what is this trade coming from?", t.sourceOrderId()));
        }

        orderIndex.get(t.sourceOrderId()).addTrade(t);
        trades.add(t);
    }

    public void addTrades(Collection<Trade> tradeList) {
        for (Trade t : tradeList) {
            addTrade(t);
        }
    }

    public Pair getPair() {
        return pair;
    }

    public Platform getPlatform() {
        return platform;
    }

    public TreeUpdatableList<Trade> getTrades() {
        return trades;
    }

    public TreeUpdatableList<FundingEntry> getFundingHistory() {
        return fundingHistory;
    }

    public TreeUpdatableList<Order> getOrders() {
        return orders;
    }

    public WalletBalance getWallet() {
        return walletBalance;
    }

    public synchronized void setWalletBalance(WalletBalance walletBalance) {
        this.walletBalance = walletBalance;
    }

    @Override
    public String toString() {
        return "PairData{" +
                "pair=" + pair +
                ", platform=" + platform +
                ", trades=" + trades +
                ", funding=" + fundingHistory +
                ", orders=" + orders +
                '}';
    }

}
