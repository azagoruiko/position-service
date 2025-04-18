package zaico.infra;

import jakarta.inject.Singleton;

import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class TimeProvider {

    private final long offset;
    private final AtomicLong lastTimestamp = new AtomicLong(0);

    public TimeProvider(long offset) {
        this.offset = offset;
    }

    public synchronized long getTimestamp() {
        long now = System.currentTimeMillis() + offset;
        long last = lastTimestamp.get();

        // Гарантируем строго возрастающую последовательность
        if (now <= last) {
            now = last + 1;
        }

        lastTimestamp.set(now);
        return now;
    }

    public long getOffset() {
        return offset;
    }
}

