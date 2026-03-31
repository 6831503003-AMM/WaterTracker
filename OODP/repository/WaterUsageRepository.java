package OODP.repository;

import OODP.model.WaterUsage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete implementation of {@link Repository} for {@link WaterUsage} records.
 * Demonstrates Collection with Generics (2.5) and Parametric Polymorphism (2.6).
 */
public class WaterUsageRepository implements Repository<WaterUsage> {

    // Collection with Generics (2.5)
    private final List<WaterUsage> store = new ArrayList<>();

    // ── Repository<WaterUsage> contract ──────────────────────────────────────

    @Override
    public void add(WaterUsage item) {
        store.add(item);
    }

    @Override
    public List<WaterUsage> findAll() {
        return Collections.unmodifiableList(store);
    }

    @Override
    public boolean remove(WaterUsage item) {
        return store.remove(item);
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public int size() {
        return store.size();
    }

    // ── Domain-specific queries ──────────────────────────────────────────────

    /** All records for a given date. */
    public List<WaterUsage> findByDate(LocalDate date) {
        return store.stream()
                .filter(u -> u.getDate().equals(date))
                .collect(Collectors.toList());
    }

    /** Remove all records for a given date. Returns number of removed records. */
    public int removeByDate(LocalDate date) {
        int before = store.size();
        store.removeIf(u -> u.getDate().equals(date));
        return before - store.size();
    }

    /** Sum of water used on a given date. */
    public double getTotalByDate(LocalDate date) {
        return store.stream()
                .filter(u -> u.getDate().equals(date))
                .mapToDouble(WaterUsage::getAmount)
                .sum();
    }

    /** Records grouped by date – Map with Generics (2.5). */
    public Map<LocalDate, List<WaterUsage>> groupByDate() {
        return store.stream()
                .collect(Collectors.groupingBy(WaterUsage::getDate));
    }

    /** Overall total across all records. */
    public double getGrandTotal() {
        return store.stream().mapToDouble(WaterUsage::getAmount).sum();
    }
}
