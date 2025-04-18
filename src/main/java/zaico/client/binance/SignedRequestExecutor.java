package zaico.client.binance;

import jakarta.inject.Singleton;

import java.util.concurrent.*;
import java.util.function.Supplier;

@Singleton
public class SignedRequestExecutor {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public <T> T execute(Supplier<T> supplier) {
        Future<T> future = executor.submit(supplier::get);
        try {
            return future.get(); // блокирует до результата
        } catch (Exception e) {
            throw new RuntimeException("❌ Signed request execution failed", e);
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
