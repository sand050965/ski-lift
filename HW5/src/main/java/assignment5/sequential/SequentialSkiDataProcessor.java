package assignment5.sequential;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processes ski lift data sequentially and produces CSV summary reports.
 */
public class SequentialSkiDataProcessor {

    private static final int MIN_HOUR = 1;
    private static final int MAX_HOUR = 6;
    private static final int MIN_TIMESTAMP = 0;
    private static final int MAX_TIMESTAMP = 360;

    /** Number of minutes in an hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** Offset used to convert timestamp boundaries into hour indices. */
    private static final int TIMESTAMP_OFFSET = 1;

    /** Offset added to the computed hour index. */
    private static final int HOUR_OFFSET = 1;

    /** Maximum number of skiers written to Skiers.csv. */
    private static final int TOP_SKIERS_COUNT = 100;

    /** Maximum number of lifts written for each hour in Hours.csv. */
    private static final int TOP_LIFTS_PER_HOUR = 10;

    private final Map<String, SkierStats> skierStatsMap;
    private final Map<Integer, LiftStats> liftStatsMap;
    private final Map<Integer, HourlyLiftStats> hourlyStatsMap;

    /**
     * Creates a new sequential data processor instance.
     */
    public SequentialSkiDataProcessor() {
        this.skierStatsMap = new HashMap<>();
        this.liftStatsMap = new HashMap<>();
        this.hourlyStatsMap = new HashMap<>();

        for (int hour = MIN_HOUR; hour <= MAX_HOUR; hour++) {
            this.hourlyStatsMap.put(hour, new HourlyLiftStats(hour));
        }
        for (int liftId = LiftVerticalMapping.MIN_LIFT_ID;
             liftId <= LiftVerticalMapping.MAX_LIFT_ID; liftId++) {
            this.liftStatsMap.put(liftId, new LiftStats(liftId));
        }
    }

    /**
     * Processes the input file and writes the three CSV output files.
     *
     * @param inputFilePath path to the raw RFID data file
     * @param skiersCsvPath path to the Skiers.csv file
     * @param liftsCsvPath  path to the Lifts.csv file
     * @param hoursCsvPath  path to the Hours.csv file
     * @throws IOException if an I/O error occurs
     */
    public void process(String inputFilePath,
                        String skiersCsvPath,
                        String liftsCsvPath,
                        String hoursCsvPath) throws IOException {

        readAndAggregate(inputFilePath);

        writeSkiersCsv(skiersCsvPath, TOP_SKIERS_COUNT);
        writeLiftsCsv(liftsCsvPath);
        writeHoursCsv(hoursCsvPath, TOP_LIFTS_PER_HOUR);
    }

    /**
     * Reads the input file line by line and aggregates all statistics
     * into skier, lift, and hourly maps.
     *
     * @param inputFilePath path to the raw RFID data file
     * @throws IOException if an I/O error occurs while reading the file
     */
    private void readAndAggregate(String inputFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line = reader.readLine();

            // skip header line if present
            if (line != null && line.toLowerCase().startsWith("resort")) {
                line = reader.readLine();
            }

            while (line != null) {
                if (!line.trim().isEmpty()) {
                    LiftRecord record = LiftRecordParser.parse(line);

                    int hour = computeHourFromTimestamp(record.getTimestamp());
                    int liftId = record.getLiftId();
                    int vertical = LiftVerticalMapping.getVerticalForLift(liftId);

                    updateSkierStats(record.getSkierId(), vertical);
                    updateLiftStats(liftId);
                    updateHourlyStats(hour, liftId);
                }

                line = reader.readLine();
            }
        }
    }

    /**
     * Computes the hour (1-6) from a timestamp in minutes (0-360).
     *
     * @param timestamp the timestamp in minutes
     * @return the hour index (1-6)
     * @throws IllegalArgumentException if the timestamp is outside the valid range
     */
    private int computeHourFromTimestamp(int timestamp) {
        if (timestamp < MIN_TIMESTAMP || timestamp > MAX_TIMESTAMP) {
            throw new IllegalArgumentException("Invalid timestamp: " + timestamp);
        }

        return ((timestamp - TIMESTAMP_OFFSET) / MINUTES_PER_HOUR) + HOUR_OFFSET;
    }

    /**
     * Updates the statistics for a single skier by adding a ride
     * with the specified vertical distance.
     *
     * @param skierId  the skier identifier
     * @param vertical the vertical distance for the ride
     */
    private void updateSkierStats(String skierId, int vertical) {
        SkierStats stats = this.skierStatsMap.get(skierId);
        if (stats == null) {
            stats = new SkierStats(skierId);
            this.skierStatsMap.put(skierId, stats);
        }
        stats.addRide(vertical);
    }

    /**
     * Updates the statistics for a single lift by incrementing its ride count.
     *
     * @param liftId the lift identifier
     */
    private void updateLiftStats(int liftId) {
        LiftStats stats = this.liftStatsMap.get(liftId);
        if (stats != null) {
            stats.incrementRides();
        }
    }

    /**
     * Updates the hourly statistics for the given hour and lift combination.
     *
     * @param hour   the hour index (1-6)
     * @param liftId the lift identifier
     */
    private void updateHourlyStats(int hour, int liftId) {
        HourlyLiftStats stats = this.hourlyStatsMap.get(hour);
        if (stats != null) {
            stats.addRideForLift(liftId);
        }
    }

    /**
     * Writes the Skiers.csv file containing the top N skiers by total vertical.
     *
     * @param skiersCsvPath path to the Skiers.csv file
     * @param topN          maximum number of skiers to include
     * @throws IOException if an I/O error occurs while writing the file
     */
    private void writeSkiersCsv(String skiersCsvPath, int topN) throws IOException {
        List<SkierStats> sortedTopSkiers = getTopSkiersByVertical(topN);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(skiersCsvPath))) {
            writer.write("SkierID,Vertical");
            writer.newLine();

            for (SkierStats stats : sortedTopSkiers) {
                writer.write(stats.getSkierId());
                writer.write(",");
                writer.write(Integer.toString(stats.getTotalVertical()));
                writer.newLine();
            }
        }
    }

    /**
     * Writes the Lifts.csv file containing the number of rides for each lift.
     *
     * @param liftsCsvPath path to the Lifts.csv file
     * @throws IOException if an I/O error occurs while writing the file
     */
    private void writeLiftsCsv(String liftsCsvPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(liftsCsvPath))) {
            writer.write("LiftID,Number of Rides");
            writer.newLine();

            for (int liftId = LiftVerticalMapping.MIN_LIFT_ID;
                 liftId <= LiftVerticalMapping.MAX_LIFT_ID; liftId++) {

                LiftStats stats = this.liftStatsMap.get(liftId);
                int rides = (stats == null) ? 0 : stats.getTotalRides();

                writer.write(Integer.toString(liftId));
                writer.write(",");
                writer.write(Integer.toString(rides));
                writer.newLine();
            }
        }
    }

    /**
     * Writes the Hours.csv file containing the top N lifts
     * for each hour, sorted by ride count in descending order.
     *
     * @param hoursCsvPath path to the Hours.csv file
     * @param topN         maximum number of lifts to include per hour
     * @throws IOException if an I/O error occurs while writing the file
     */
    private void writeHoursCsv(String hoursCsvPath, int topN) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(hoursCsvPath))) {

            for (int hour = MIN_HOUR; hour <= MAX_HOUR; hour++) {
                HourlyLiftStats stats = this.hourlyStatsMap.get(hour);

                writer.write("Hour #" + hour);
                writer.newLine();
                writer.write("LiftID,Number of Rides");
                writer.newLine();

                List<LiftStats> topLiftsForHour = getTopLiftsForHour(stats, topN);

                for (LiftStats liftStats : topLiftsForHour) {
                    writer.write(Integer.toString(liftStats.getLiftId()));
                    writer.write(",");
                    writer.write(Integer.toString(liftStats.getTotalRides()));
                    writer.newLine();
                }

                writer.newLine();
            }
        }
    }

    /**
     * Returns the top N skiers by total vertical, in descending order of vertical.
     *
     * @param topN the maximum number of skiers to return
     * @return the list of top skiers
     */
    private List<SkierStats> getTopSkiersByVertical(int topN) {
        List<SkierStats> all = new ArrayList<>(this.skierStatsMap.values());

        all.sort((a, b) -> {
            int cmp = Integer.compare(b.getTotalVertical(), a.getTotalVertical());
            if (cmp != 0) return cmp;
            return a.getSkierId().compareTo(b.getSkierId());
        });

        if (all.size() > topN) {
            return new ArrayList<>(all.subList(0, topN));
        } else {
            return all;
        }
    }

    /**
     * Returns the top N lifts for a single hour, sorted by ride count descending.
     *
     * @param hourlyStats the hourly statistics
     * @param topN        the maximum number of lifts to return
     * @return the list of top lifts for the hour
     */
    private List<LiftStats> getTopLiftsForHour(HourlyLiftStats hourlyStats, int topN) {
        List<LiftStats> list = new ArrayList<>();

        Map<Integer, Integer> ridesPerLift = hourlyStats.getRidesPerLift();

        for (Map.Entry<Integer, Integer> entry : ridesPerLift.entrySet()) {
            int liftId = entry.getKey();
            int rides = entry.getValue();

            LiftStats stats = new LiftStats(liftId);

            for (int i = 0; i < rides; i++) {
                stats.incrementRides();
            }
            list.add(stats);
        }

        list.sort((a, b) -> Integer.compare(b.getTotalRides(), a.getTotalRides()));

        if (list.size() > topN) {
            return new ArrayList<>(list.subList(0, topN));
        } else {
            return list;
        }
    }

    /**
     * Returns true if the given string can be parsed as an integer.
     *
     * @param value the string value
     * @return true if the value is an integer; false otherwise
     */
    private static boolean isInteger(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
