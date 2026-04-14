package WT_OODP.io;

import WT_OODP.exception.DataFileException;
import WT_OODP.model.WaterUsage;
import WT_OODP.repository.WaterUsageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file I/O for the Water Tracker application.
 *
 * <ul>
 *   <li>Usage records  → {@code water_data.csv}</li>
 *   <li>Configuration  → {@code water_config.txt}</li>
 * </ul>
 *
 * Satisfies Read/Write from File (2.1) and Exception / Custom Exception (2.3).
 */
public class CsvFileManager {

    private static final String DATA_FILE   = "water_data.csv";
    private static final String CONFIG_FILE = "water_config.txt";

    // ── Save ─────────────────────────────────────────────────────────────────

    /**
     * Persist all usage records to the CSV data file.
     *
     * @param repository source of records to write
     * @throws DataFileException (runtime) if writing fails
     */
    public void saveUsageData(WaterUsageRepository repository) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (WaterUsage u : repository.findAll()) {
                pw.println(u.toCsv());
            }
        } catch (IOException e) {
            throw new DataFileException(DATA_FILE, "Could not write usage data.", e);
        }
    }

    /**
     * Persist the current daily-limit setting.
     *
     * @param dailyLimit the limit value to save
     * @throws DataFileException (runtime) if writing fails
     */
    public void saveConfig(double dailyLimit) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            pw.println("dailyLimit=" + dailyLimit);
        } catch (IOException e) {
            throw new DataFileException(CONFIG_FILE, "Could not write config.", e);
        }
    }

    // ── Load ─────────────────────────────────────────────────────────────────

    /**
     * Read all usage records from the CSV data file into the repository.
     * Corrupt lines are silently skipped.
     *
     * @param repository the target to populate
     * @return number of records successfully loaded
     */
    public int loadUsageData(WaterUsageRepository repository) {
        File file = new File(DATA_FILE);
        if (!file.exists()) return 0;

        int loaded = 0;
        List<String> badLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                WaterUsage u = WaterUsage.fromCsv(line);
                if (u != null) {
                    repository.add(u);
                    loaded++;
                } else {
                    badLines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DataFileException(DATA_FILE, "Could not read usage data.", e);
        }

        if (!badLines.isEmpty()) {
            System.out.printf("⚠️  Skipped %d corrupt line(s) in %s.%n",
                    badLines.size(), DATA_FILE);
        }
        return loaded;
    }

    /**
     * Read the daily-limit setting from the config file.
     *
     * @param defaultLimit value returned when the file does not exist or is unreadable
     * @return the stored limit, or {@code defaultLimit} on any error
     */
    public double loadConfig(double defaultLimit) {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return defaultLimit;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("dailyLimit=")) {
                    return Double.parseDouble(line.split("=")[1].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️  Could not read config – using default limit.");
        }
        return defaultLimit;
    }
}
