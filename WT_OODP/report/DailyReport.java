package WT_OODP.report;

import WT_OODP.model.WaterUsage;
import WT_OODP.repository.WaterUsageRepository;
import WT_OODP.util.ConsoleHelper;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements {@link Report} for a single day's water usage.
 * Satisfies Inheritance / Interface (2.2).
 */
public class DailyReport implements Report {

    private final WaterUsageRepository repository;
    private final double               dailyLimit;
    private final LocalDate            date;
    private final String[]             categories;

    public DailyReport(WaterUsageRepository repository,
                       double dailyLimit,
                       LocalDate date,
                       String[] categories) {
        this.repository = repository;
        this.dailyLimit = dailyLimit;
        this.date       = date;
        this.categories = categories;
    }

    @Override
    public String getTitle() {
        return "TODAY'S SUMMARY — " + date;
    }

    @Override
    public void display() {
        System.out.println("\n🌤️  " + getTitle());
        System.out.println("──────────────────────────────────────────");

        List<WaterUsage> todayList = repository.findByDate(date);

        if (todayList.isEmpty()) {
            System.out.println("  No usage recorded for today yet.");
            System.out.printf ("  Daily limit: %.1f L%n", dailyLimit);
            return;
        }

        double total     = todayList.stream().mapToDouble(WaterUsage::getAmount).sum();
        double remaining = dailyLimit - total;

        System.out.printf("  Daily limit : %.1f L%n", dailyLimit);
        System.out.printf("  Used today  : %.1f L%n", total);

        if (remaining >= 0) {
            System.out.printf("  Remaining   : %.1f L%n", remaining);
            System.out.printf("  Usage       : %.1f%%%n", (total / dailyLimit) * 100);
            System.out.println("\n  " + ConsoleHelper.progressBar(total, dailyLimit, 30));
            System.out.println("  ✅ Within daily limit.");
        } else {
            System.out.printf("  Over limit  : %.1f L%n", -remaining);
            System.out.println("\n  " + ConsoleHelper.progressBar(total, dailyLimit, 30));
            System.out.println("  ⚠️  WARNING: Daily limit exceeded!");
        }

        System.out.println("\n  Usage by category:");
        Map<String, Double> catMap = buildCategoryMap(todayList);
        for (Map.Entry<String, Double> e : catMap.entrySet()) {
            if (e.getValue() > 0)
                System.out.printf("    %-10s : %.1f L%n",
                        ConsoleHelper.capitalize(e.getKey()), e.getValue());
        }
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Map<String, Double> buildCategoryMap(List<WaterUsage> list) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (String cat : categories) map.put(cat, 0.0);
        for (WaterUsage u : list) map.merge(u.getCategory(), u.getAmount(), Double::sum);
        return map;
    }
}
