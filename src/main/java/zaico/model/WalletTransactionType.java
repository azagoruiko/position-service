package zaico.model;

public enum WalletTransactionType {
    SPOT_TO_FUTURES,
    FUTURES_TO_SPOT,
    SPOT_TO_MARGIN,
    MARGIN_TO_SPOT,
    SPOT_TO_EARN,
    EARN_TO_SPOT,
    UNKNOWN;

    public static WalletTransactionType from(String binanceType) {
        return switch (binanceType) {
            case "MAIN_UMFUTURE" -> SPOT_TO_FUTURES;
            case "UMFUTURE_MAIN" -> FUTURES_TO_SPOT;
            case "MAIN_MARGIN" -> SPOT_TO_MARGIN;
            case "MARGIN_MAIN" -> MARGIN_TO_SPOT;
            case "MAIN_SIMPLE_EARN" -> SPOT_TO_EARN;
            case "SIMPLE_EARN_MAIN" -> EARN_TO_SPOT;
            default -> UNKNOWN;
        };
    }
}