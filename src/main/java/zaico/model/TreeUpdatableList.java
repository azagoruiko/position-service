package zaico.model;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class TreeUpdatableList<T extends HistoryItem> implements UpdatableList<T> {

    private final NavigableSet<T> items;
    private long lastUpdatedAt;

    public TreeUpdatableList() {
        this.items = new ConcurrentSkipListSet<>(Comparator
                .comparingLong(HistoryItem::closedAt)
                .thenComparing(System::identityHashCode)); // для уникальности
    }

    public long getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    @Override
    public synchronized void addAll(Collection<T> newItems) {
        newItems.stream()
                .mapToLong(HistoryItem::closedAt)
                .max()
                .ifPresent(mu -> lastUpdatedAt = Math.max(lastUpdatedAt, mu));
        items.addAll(newItems);
    }

    @Override
    public synchronized void add(T item) {
        if (item.closedAt() > lastUpdatedAt) {
            lastUpdatedAt = item.closedAt();
        }
        items.add(item);
    }

    @Override
    public List<T> get(long since) {
        // "Фейковый" объект-заглушка с нужным timestamp
        T dummy = (T) new HistoryItem() {
            @Override
            public long closedAt() {
                return since;
            }

            @Override
            public long openedAt() {
                return since;
            }
        };

        return items.tailSet(dummy, false)
                .stream()
                .toList();
    }

    @Override
    public String toString() {
        return "TreeUpdatableList{size=" + items.size() + ", lastUpdated=" + lastUpdatedAt + "}";
    }
}
