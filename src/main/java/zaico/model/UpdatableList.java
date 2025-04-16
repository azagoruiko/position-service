package zaico.model;

import java.util.Collection;
import java.util.List;

public interface UpdatableList<T extends HistoryItem> {
    void addAll(Collection<T> items);
    void add(T item);

    List<T> get(long since);

    default List<T> get() {
        return get(0l);
    }
}
