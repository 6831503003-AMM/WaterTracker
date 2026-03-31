package OODP.model;

import java.time.LocalDate;

/**
 * Immutable value object representing a single water-usage entry.
 */
public class WaterUsage {

    private final LocalDate date;
    private final double   amount;    // litres
    private final String   category;  // shower | kitchen | laundry | garden | other

    public WaterUsage(LocalDate date, double amount, String category) {
        this.date     = date;
        this.amount   = amount;
        this.category = category;
    }

    // ── CSV serialisation ────────────────────────────────────────────────────

    public String toCsv() {
        return date + "," + amount + "," + category;
    }

    /**
     * Parse a CSV line back into a {@link WaterUsage}.
     *
     * @param line comma-separated string with 3 fields
     * @return a new instance, or {@code null} if the line is malformed
     */
    public static WaterUsage fromCsv(String line) {
        if (line == null || line.isBlank()) return null;
        String[] parts = line.split(",");
        if (parts.length != 3) return null;
        try {
            LocalDate date     = LocalDate.parse(parts[0].trim());
            double    amount   = Double.parseDouble(parts[1].trim());
            String    category = parts[2].trim();
            return new WaterUsage(date, amount, category);
        } catch (Exception e) {
            return null;   // silently skip corrupt lines
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public LocalDate getDate()     { return date;     }
    public double    getAmount()   { return amount;   }
    public String    getCategory() { return category; }

    @Override
    public String toString() {
        return String.format("WaterUsage{date=%s, amount=%.1f L, category=%s}",
                date, amount, category);
    }
}
