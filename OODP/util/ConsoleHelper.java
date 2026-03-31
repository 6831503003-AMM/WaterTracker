package OODP.util;

/**
 * Stateless helper methods shared by the tracker and reports.
 * Keeps presentation logic in one place so it can evolve independently.
 */
public final class ConsoleHelper {

    // Prevent instantiation
    private ConsoleHelper() {}

    // ── String helpers ────────────────────────────────────────────────────────

    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // ── Bar helpers ───────────────────────────────────────────────────────────

    /**
     * Proportional bar showing {@code value} as a fraction of {@code total}.
     *
     * @param value  current value
     * @param total  maximum value (100 %)
     * @param width  total number of bar segments
     * @return       a string of filled (█) and empty (░) characters
     */
    public static String usageBar(double value, double total, int width) {
        int filled = total <= 0 ? 0 : (int) Math.round((value / total) * width);
        filled = Math.max(0, Math.min(width, filled));
        return "█".repeat(filled) + "░".repeat(width - filled);
    }

    /**
     * Full-width progress bar with a percentage label appended.
     *
     * @param used   amount used
     * @param limit  daily limit
     * @param width  bar segment count
     * @return       {@code [████░░░░░] 45%} style string
     */
    public static String progressBar(double used, double limit, int width) {
        double pct    = limit <= 0 ? 100 : Math.min(100, (used / limit) * 100);
        int    filled = (int) Math.min(width, Math.round((used / limit) * width));
        String bar    = "█".repeat(filled) + "░".repeat(width - filled);
        return "[" + bar + "] " + String.format("%.0f%%", pct);
    }

    // ── Menu helpers ──────────────────────────────────────────────────────────

    public static void printBanner() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   💧 SMART WATER USAGE TRACKING SYSTEM   ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    public static void printMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│          MAIN MENU          │");
        System.out.println("├─────────────────────────────┤");
        System.out.println("│  1. Add Water Usage         │");
        System.out.println("│  2. Set Daily Limit         │");
        System.out.println("│  3. View Full Report        │");
        System.out.println("│  4. View Today's Summary    │");
        System.out.println("│  5. Reset Today's Data      │");
        System.out.println("│  6. Save & Exit             │");
        System.out.println("└─────────────────────────────┘");
    }
}
