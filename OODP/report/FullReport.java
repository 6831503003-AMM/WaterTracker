package OODP.report;

import OODP.model.WaterUsage;
import OODP.repository.WaterUsageRepository;
import OODP.util.ConsoleHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Implements {@link Report} for a full historical summary.
 * Satisfies Inheritance / Interface (2.2).
 */
public class FullReport implements Report {

    private final WaterUsageRepository repository;
    private final double               dailyLimit;
    private final String[]             categories;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

    public FullReport(WaterUsageRepository repository,
                      double dailyLimit,
                      String[] categories) {
        this.repository = repository;
        this.dailyLimit = dailyLimit;
        this.categories = categories;
    }

    @Override
    public String getTitle() {
        return "FULL WATER USAGE REPORT";
    }

    @Override
    public void display() {
        System.out.println("\n📋 " + getTitle());
        System.out.println("══════════════════════════════════════════");

        if (repository.isEmpty()) {
            System.out.println("No data recorded yet.");
            return;
        }

        Map<LocalDate, List<WaterUsage>> byDate = repository.groupByDate();
        List<LocalDate> sortedDates = new ArrayList<>(byDate.keySet());
        Collections.sort(sortedDates);

        double grandTotal = 0;

        // ── Per-day section ──────────────────────────────────────────────────
        for (LocalDate date : sortedDates) {
            List<WaterUsage> dayList = byDate.get(date);
            double dayTotal = dayList.stream().mapToDouble(WaterUsage::getAmount).sum();
            grandTotal += dayTotal;

            System.out.printf("%n📅 %s%n", date.format(DATE_FMT));
            System.out.println("   ──────────────────────────────────");

            Map<String, Double> catMap = buildCategoryMap(dayList);
            for (Map.Entry<String, Double> e : catMap.entrySet()) {
                if (e.getValue() > 0) {
                    System.out.printf("   %-10s : %6.1f L  %s%n",
                            ConsoleHelper.capitalize(e.getKey()),
                            e.getValue(),
                            ConsoleHelper.usageBar(e.getValue(), dayTotal, 10));
                }
            }

            System.out.println("   ──────────────────────────────────");
            System.out.printf ("   TOTAL      : %6.1f L  %s%n", dayTotal,
                    dayTotal > dailyLimit ? "⚠️  OVER LIMIT" : "✅ Within limit");
        }

        // ── Summary statistics ───────────────────────────────────────────────
        double avgPerDay = grandTotal / sortedDates.size();
        long overDays = sortedDates.stream()
                .filter(d -> byDate.get(d).stream().mapToDouble(WaterUsage::getAmount).sum() > dailyLimit)
                .count();

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("📊 SUMMARY STATISTICS");
        System.out.println("──────────────────────────────────────────");
        System.out.printf("  Total days tracked : %d%n",   sortedDates.size());
        System.out.printf("  Grand total usage  : %.1f L%n", grandTotal);
        System.out.printf("  Average per day    : %.1f L%n", avgPerDay);
        System.out.printf("  Daily limit set    : %.1f L%n", dailyLimit);
        System.out.printf("  Days over limit    : %d / %d%n", overDays, sortedDates.size());

        // ── Overall category breakdown ───────────────────────────────────────
        System.out.println("\n📦 OVERALL CATEGORY BREAKDOWN");
        System.out.println("──────────────────────────────────────────");

        Map<String, Double> totalCat = buildCategoryMap(repository.findAll());
        for (Map.Entry<String, Double> e : totalCat.entrySet()) {
            if (e.getValue() > 0) {
                double pct = (e.getValue() / grandTotal) * 100;
                System.out.printf("  %-10s : %7.1f L  (%5.1f%%)  %s%n",
                        ConsoleHelper.capitalize(e.getKey()),
                        e.getValue(), pct,
                        ConsoleHelper.usageBar(e.getValue(), grandTotal, 10));
            }
        }
        System.out.println("══════════════════════════════════════════");
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Map<String, Double> buildCategoryMap(List<WaterUsage> list) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (String cat : categories) map.put(cat, 0.0);
        for (WaterUsage u : list) map.merge(u.getCategory(), u.getAmount(), Double::sum);
        return map;
    }
}
