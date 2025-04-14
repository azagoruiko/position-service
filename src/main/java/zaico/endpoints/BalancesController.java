package zaico.endpoints;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import zaico.client.binance.BinanceBalanceService;
import zaico.client.binance.FuturesType;
import zaico.model.WalletBalance;

import java.util.List;

@Controller("/wallets")
public class BalancesController {

    @Inject BinanceBalanceService balanceService;

    @Get("/spot")
    public List<WalletBalance> spot() {
        return balanceService.getSpotBalances();
    }

    @Get("/futures/usdt")
    public List<WalletBalance> usdtFutures() {
        return balanceService.getFuturesBalances(FuturesType.USDT);
    }


    @Get("/futures/coin")
    public List<WalletBalance> coinFutures() {
        return balanceService.getFuturesBalances(FuturesType.COIN);
    }
    @Get("/earn/simple")
    public List<WalletBalance> simpleEarn() {
        return balanceService.getEarnBalances();
    }

}
