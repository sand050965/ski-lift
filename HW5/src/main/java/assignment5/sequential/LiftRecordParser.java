package assignment5.sequential;

/**
 * Parses raw input lines into {@link LiftRecord} instances.
 */
public final class LiftRecordParser {

    /** The CSV delimiter used to split fields. */
    private static final String DELIMITER = ",";

    /** Expected number of columns in each input line. */
    private static final int EXPECTED_COLUMN_COUNT = 5;

    /** Index of the resort ID field in the CSV. */
    private static final int INDEX_RESORT_ID = 0;

    /** Index of the day number field in the CSV. */
    private static final int INDEX_DAY_NUMBER = 1;

    /** Index of the skier ID field in the CSV. */
    private static final int INDEX_SKIER_ID = 2;

    /** Index of the lift ID field in the CSV. */
    private static final int INDEX_LIFT_ID = 3;

    /** Index of the timestamp field in the CSV. */
    private static final int INDEX_TIMESTAMP = 4;

    /**
     * Private constructor to prevent instantiation.
     * This class only contains static parsing methods.
     */
    private LiftRecordParser() {
        // Prevent instantiation.
    }

    /**
     * Parses a CSV line into a {@link LiftRecord}.
     *
     * @param line the line to parse; must not be null
     * @return the parsed record
     * @throws IllegalArgumentException if the line is null, empty, or incorrectly formatted
     */
    public static LiftRecord parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Line must not be null.");
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Line must not be empty.");
        }

        String[] parts = trimmed.split(DELIMITER);
        if (parts.length < EXPECTED_COLUMN_COUNT) {
            throw new IllegalArgumentException(
                    "Invalid line, expected at least " + EXPECTED_COLUMN_COUNT + " columns: " + line);
        }

        // resort,day,skier,lift,time
        String resortId = parts[INDEX_RESORT_ID].trim();
        int dayNumber = Integer.parseInt(parts[INDEX_DAY_NUMBER].trim());
        String skierId = parts[INDEX_SKIER_ID].trim();
        int liftId = Integer.parseInt(parts[INDEX_LIFT_ID].trim());
        int timestamp = Integer.parseInt(parts[INDEX_TIMESTAMP].trim());

        return new LiftRecord(resortId, dayNumber, timestamp, skierId, liftId);
    }
}
