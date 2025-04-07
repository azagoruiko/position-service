package zaico.client.binance.parser;

import org.junit.jupiter.api.Test;
import zaico.client.binance.dto.FuturesTrade;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinanceTradeParserTest {

    @Test
    void testParseFuturesTrades() {
        String json = "[{\"symbol\":\"SOLUSDT\",\"id\":2196535277,\"orderId\":109336343754,\"side\":\"BUY\",\"price\":\"128.7900\",\"qty\":\"1\",\"realizedPnl\":\"8.88000000\",\"quoteQty\":\"128.7900\",\"commission\":\"0.06439499\",\"commissionAsset\":\"USDT\",\"time\":1743495626019,\"positionSide\":\"BOTH\",\"maker\":false,\"buyer\":true}]";
        List<FuturesTrade> trades = BinanceTradeParser.parseFuturesTrades(json);

        assertEquals(1, trades.size());
        FuturesTrade t = trades.get(0);
        assertEquals("SOLUSDT", t.symbol());
        assertEquals("BUY", t.side());
        assertEquals(new BigDecimal("128.7900"), t.price());
    }

}