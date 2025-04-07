package zaico.model;

public enum TradeType {
    BUY, SELL;

    public TradeType opposite() {
        return this == BUY ? SELL : BUY;
    }
}
