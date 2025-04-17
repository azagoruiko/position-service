package zaico.service;

import zaico.exchange.Platform;
import zaico.math.Pair;
import zaico.model.FundingEntry;
import zaico.model.Order;
import zaico.model.Trade;
import zaico.model.TreeUpdatableList;

public class PairData {
    private final Pair pair;

    private final Platform platform;
    private final TreeUpdatableList<Trade> trades = new TreeUpdatableList<>();
    private final TreeUpdatableList<FundingEntry> fundingHistory = new TreeUpdatableList<>();
    private final TreeUpdatableList<Order> orders = new TreeUpdatableList<>();

    public PairData(Pair pair, Platform platform) {
        this.pair = pair;
        this.platform = platform;
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
