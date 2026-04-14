package WT_OODP;

import WT_OODP.core.WaterTracker;
import WT_OODP.util.ConsoleHelper;
import java.util.Scanner;

/**
 * Application entry point.
 * Keyboard input (2.4) is handled via {@link Scanner}.
 */
public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            WaterTracker tracker = new WaterTracker();
            
            ConsoleHelper.printBanner();
            tracker.loadData();
            
            boolean running = true;
            while (running) {
                ConsoleHelper.printMenu();
                System.out.print("Enter your choice: ");
                String input = scanner.nextLine().trim();
                
                switch (input) {
                    case "1" -> tracker.addWaterUsage(scanner);
                    case "2" -> tracker.setDailyLimit(scanner);
                    case "3" -> tracker.viewReport();
                    case "4" -> tracker.viewTodaySummary();
                    case "5" -> tracker.resetTodayData(scanner);
                    case "6" -> {
                        tracker.saveAll();
                        System.out.println("\n💾 Data saved. Goodbye! 🌊");
                        running = false;
                    }
                    default  -> System.out.println("❌ Invalid choice. Please try again.\n");
                }
            }
        }
    }
}
