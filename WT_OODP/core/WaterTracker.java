package WT_OODP.core;

import WT_OODP.exception.DataFileException;
import WT_OODP.exception.InvalidAmountException;
import WT_OODP.exception.InvalidCategoryException;
import WT_OODP.io.CsvFileManager;
import WT_OODP.model.WaterUsage;
import WT_OODP.report.DailyReport;
import WT_OODP.report.FullReport;
import WT_OODP.report.Report;
import WT_OODP.repository.WaterUsageRepository;
import WT_OODP.util.ConsoleHelper;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Application controller.
 * Wires together the repository, file manager and reports; handles all
 * keyboard input and orchestrates user-facing operations.
 */
public class WaterTracker {

    // Constants
    public static final String[] CATEGORIES =
            {"shower", "kitchen", "laundry", "garden", "other"};
    private static final double DEFAULT_LIMIT = 150.0;

    // ── Dependencies
    private final WaterUsageRepository repository = new WaterUsageRepository();
    private final CsvFileManager       fileManager = new CsvFileManager();

    private double dailyLimit = DEFAULT_LIMIT;


    //  1. ADD WATER USAGE
    public void addWaterUsage(Scanner scanner) {
        System.out.println("\n💧 ADD WATER USAGE");
        System.out.println("──────────────────");

        // --- amount input with custom exception (2.3) -----------------------
        double amount = 0;
        while (amount <= 0) {
            System.out.print("Enter amount used (liters): ");
            try {
                double input = Double.parseDouble(scanner.nextLine().trim()); // removing whitespace from both ends of a string.
                if (input <= 0) throw new InvalidAmountException(input);
                amount = input;
            } catch (InvalidAmountException e) {
                System.out.println("❌ " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please enter a numeric value.");
            }
        }

        // --- category input with custom exception (2.3) ---------------------
        System.out.println("Select category:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, ConsoleHelper.capitalize(CATEGORIES[i]));
        }

        String category = null;
        while (category == null) {
            System.out.print("Enter category number: ");
            try {
                String raw = scanner.nextLine().trim();
                int choice = Integer.parseInt(raw);
                if (choice < 1 || choice > CATEGORIES.length)
                    throw new InvalidCategoryException(raw);
                category = CATEGORIES[choice - 1];
            } catch (InvalidCategoryException e) {
                System.out.println("❌ " + e.getMessage() +
                        " Choose 1–" + CATEGORIES.length + ".");
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a number.");
            }
        }

        // --- record and feedback --------------------------------------------
        LocalDate  today = LocalDate.now();
        WaterUsage usage = new WaterUsage(today, amount, category);
        repository.add(usage);

        System.out.printf("%n✅ Recorded: %.1f L for %s on %s%n",
                amount, ConsoleHelper.capitalize(category), today);

        double todayTotal = repository.getTotalByDate(today);
        System.out.printf("📊 Today's total: %.1f L  |  Limit: %.1f L%n",
                todayTotal, dailyLimit);

        if (todayTotal > dailyLimit) {
            double over = todayTotal - dailyLimit;
            System.out.println("\n⚠️  ════════════════════════════════════════");
            System.out.printf ("⚠️  WARNING: You have exceeded your daily limit!%n");
            System.out.printf ("⚠️  You are %.1f L over your %.0f L limit.%n", over, dailyLimit);
            System.out.println("⚠️  ════════════════════════════════════════");
            System.out.println("💡 Tip: Turn off the tap while brushing teeth.");
        } else {
            System.out.printf("✅ You have %.1f L remaining for today.%n",
                    dailyLimit - todayTotal);
        }

        saveAll();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  2. SET DAILY LIMIT
    // ─────────────────────────────────────────────────────────────────────────
    public void setDailyLimit(Scanner scanner) {
        System.out.println("\n⚙️  SET DAILY LIMIT");
        System.out.println("───────────────────");
        System.out.printf("Current daily limit: %.1f liters%n", dailyLimit);
        System.out.println("(Recommended: 150 liters per person per day)");

        double newLimit = 0;
        while (newLimit <= 0) {
            System.out.print("Enter new daily limit (liters): ");
            try {
                double input = Double.parseDouble(scanner.nextLine().trim());
                if (input <= 0) throw new InvalidAmountException(input);
                newLimit = input;
            } catch (InvalidAmountException e) {
                System.out.println("❌ " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number.");
            }
        }

        dailyLimit = newLimit;
        System.out.printf("%n✅ Daily limit set to %.1f liters.%n", dailyLimit);
        saveAll();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  3. VIEW FULL REPORT
    // ─────────────────────────────────────────────────────────────────────────
    public void viewReport() {
        Report report = new FullReport(repository, dailyLimit, CATEGORIES);
        report.display();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  4. TODAY'S SUMMARY
    // ─────────────────────────────────────────────────────────────────────────
    public void viewTodaySummary() {
        // Using Report interface polymorphically (2.2)
        Report report = new DailyReport(
                repository, dailyLimit, LocalDate.now(), CATEGORIES);
        report.display();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  5. RESET TODAY'S DATA
    // ─────────────────────────────────────────────────────────────────────────
    public void resetTodayData(Scanner scanner) {
        System.out.println("\n🗑️  RESET TODAY'S DATA");
        System.out.println("──────────────────────────────────────────");
        System.out.print("Are you sure you want to clear today's records? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            int removed = repository.removeByDate(LocalDate.now());
            System.out.printf("✅ Cleared %d record(s) for today.%n", removed);
            saveAll();
        } else {
            System.out.println("❌ Reset cancelled.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SAVE & LOAD
    // ─────────────────────────────────────────────────────────────────────────

    /** Persist both usage data and config, catching DataFileException (2.3). */
    public void saveAll() {
        try {
            fileManager.saveUsageData(repository);
            fileManager.saveConfig(dailyLimit);
        } catch (DataFileException e) {
            System.out.println("⚠️  Save error: " + e.getMessage());
        }
    }

    /** Load config and records from disk; safe to call at startup. */
    public void loadData() {
        try {
            dailyLimit = fileManager.loadConfig(DEFAULT_LIMIT);
            int count  = fileManager.loadUsageData(repository);

            if (count > 0) {
                System.out.printf("✅ Loaded %d record(s). Daily limit: %.0f L%n%n",
                        count, dailyLimit);
            } else {
                System.out.println("📁 No previous data found. Starting fresh.\n");
            }
        } catch (DataFileException e) {
            System.out.println("⚠️  Could not load data: " + e.getMessage());
        }
    }
}
