package assignment5.concurrent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Utility class for writing CSV files.
 */
public class CsvWriter {
    /**
     * File name for storing skiers' data in CSV format.
     */
    public static final String SKIERS_CSV_FILE_NAME = "Skiers.csv";

    /**
     * File name for storing lifts' data in CSV format.
     */
    public static final String LIFTS_CSV_FILE_NAME = "Lifts.csv";

    /**
     * File name for storing hours' data in CSV format.
     */
    public static final String HOURS_CSV_FILE_NAME = "Hours.csv";

    /**
     * Minimum number of skiers' data entries.
     */
    public static final int MIN_SKIERS_DATA_SIZE = 0;

    /**
     * Maximum number of skiers' data entries.
     */
    public static final int MAX_SKIERS_DATA_SIZE = 100;

    /**
     * Minimum lift ID.
     */
    public static final int MIN_LIFT_ID = 1;

    /**
     * Maximum lift ID.
     */
    public static final int MAX_LIFT_ID = 40;

    /**
     * Minimum hour value.
     */
    public static final int MIN_HOUR = 1;

    /**
     * Maximum hour value.
     */
    public static final int MAX_HOUR = 6;

    /**
     * Maximum number of data entries allowed for each hour.
     */
    public static final int MAX_DATA_SIZE_FOR_EACH_HOUR = 10;

    /**
     * Column name for skier ID in CSV files.
     */
    public static final String SKIER_ID_COLUMN_NAME = "SkierID";

    /**
     * Column name for vertical meters in CSV files.
     */
    public static final String VERTICAL_COLUMN_NAME = "Vertical";

    /**
     * Column name for lift ID in CSV files.
     */
    public static final String LIFT_ID_COLUMN_NAME = "LiftID";

    /**
     * Column name for number of rides in CSV files.
     */
    public static final String NUMBER_OF_RIDES_COLUMN_NAME = "Number of Rides";

    /**
     * Column name for hour in CSV files.
     */
    public static final String HOUR_COLUMN_NAME = "Hour #";


    /**
     * Writes top 100 skiers by vertical meters to Skiers.csv.
     *
     * @param skierVerticals map of skierID to total vertical meters
     * @param outputDir      output directory
     * @throws IOException if file cannot be written
     */
    public static void writeSkiersCSV(Map<String, Integer> skierVerticals, String outputDir) throws IOException {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                (a, b) -> {
                    int cmp = Integer.compare(b.getValue(), a.getValue());
                    if (cmp != 0) return cmp;
                    return a.getKey().compareTo(b.getKey());
                });
        pq.addAll(skierVerticals.entrySet());
        String outputPath = outputDir + File.separator + SKIERS_CSV_FILE_NAME;
        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(SKIER_ID_COLUMN_NAME + "," + VERTICAL_COLUMN_NAME + "\n");
            for (int i = MIN_SKIERS_DATA_SIZE; i < MAX_SKIERS_DATA_SIZE && !pq.isEmpty(); i++) {
                Map.Entry<String, Integer> entry = pq.poll();
                fw.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }

    /**
     * Writes total rides per lift to Lifts.csv.
     *
     * @param liftRides map of liftID to ride count
     * @param outputDir output directory
     * @throws IOException if file cannot be written
     */
    public static void writeLiftsCSV(Map<Integer, Integer> liftRides, String outputDir) throws IOException {
        String outputPath = outputDir + File.separator + LIFTS_CSV_FILE_NAME;
        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(LIFT_ID_COLUMN_NAME + "," + NUMBER_OF_RIDES_COLUMN_NAME + "\n");
            for (int i = MIN_LIFT_ID; i <= MAX_LIFT_ID; i++) {
                fw.write(i + "," + liftRides.getOrDefault(i, 0) + "\n");
            }
        }
    }

    /**
     * Writes top 10 busiest lifts per hour to Hours.csv.
     *
     * @param hourLiftRides nested map: hour -> (liftID -> ride count)
     * @param outputDir     output directory
     * @throws IOException if file cannot be written
     */
    public static void writeHoursCSV(Map<Integer, Map<Integer, Integer>> hourLiftRides, String outputDir) throws IOException {
        String outputPath = outputDir + File.separator + HOURS_CSV_FILE_NAME;
        try (FileWriter fw = new FileWriter(outputPath)) {
            for (int h = MIN_HOUR; h <= MAX_HOUR; h++) {
                fw.write(HOUR_COLUMN_NAME + h + "\n");
                fw.write(LIFT_ID_COLUMN_NAME + "," + NUMBER_OF_RIDES_COLUMN_NAME + "\n");
                Map<Integer, Integer> liftMap = hourLiftRides.getOrDefault(h, new HashMap<>());
                liftMap.entrySet().stream()
                        .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                        .limit(MAX_DATA_SIZE_FOR_EACH_HOUR)
                        .forEach(entry -> {
                            try {
                                fw.write(entry.getKey() + "," + entry.getValue() + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                fw.write("\n");
            }
        }
    }
}
