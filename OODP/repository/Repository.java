package OODP.repository;

import java.util.List;

/**
 * Generic repository contract – satisfies the Parametric Polymorphism
 * requirement (2.6).  Any entity type {@code T} can be stored, retrieved
 * and managed through this interface.
 *
 * @param <T> the type of entity managed by this repository
 */
public interface Repository<T> {

    /** Persist a new item. */
    void add(T item);

    /** Return an unmodifiable snapshot of all stored items. */
    List<T> findAll();

    /** Remove a specific item (uses equals). */
    boolean remove(T item);

    /** Remove all items. */
    void clear();

    /** Return the number of stored items. */
    int size();

    /** Return {@code true} if no items are stored. */
    default boolean isEmpty() {
        return size() == 0;
    }
}
