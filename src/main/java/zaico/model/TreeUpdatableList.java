package zaico.model;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class TreeUpdatableList<T extends HistoryItem> implements UpdatableList<T> {

    private final NavigableSet<T> items;

    public TreeUpdatableList() {
        this.items = new ConcurrentSkipListSet<>(Comparator
                .comparingLong(HistoryItem::closedAt)
                .thenComparing(System::identityHashCode)); // для уникальности
    }

    @Override
    public void addAll(Collection<T> newItems) {
        items.addAll(newItems);
    }

    @Override
    public void add(T item) {
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
}
