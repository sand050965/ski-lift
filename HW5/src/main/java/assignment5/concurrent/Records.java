package assignment5.concurrent;

/**
 * Contains all record types used in the concurrent ski resort solution.
 */
public final class Records {

    /**
     * Private constructor to prevent instantiation.
     */
    private Records() {
    }

    /**
     * Represents a skier ride record for calculating skier vertical totals.
     *
     * @param skierId ID of the skier
     * @param liftId  ID of the lift
     */
    public record SkierRecord(String skierId, int liftId) {
        public static final SkierRecord POISON = new SkierRecord(null, -1);
    }

    /**
     * Represents a lift ride record for calculating total rides per lift.
     *
     * @param liftId ID of the lift
     */
    public record LiftRecord(int liftId) {

        public static final LiftRecord POISON = new LiftRecord(-1);
    }

    /**
     * Represents a ride record per hour for calculating hourly lift popularity.
     *
     * @param hour   Hour number (1-6)
     * @param liftId ID of the lift
     */
    public record HourRecord(int hour, int liftId) {
        public static final HourRecord POISON = new HourRecord(-1, -1);
    }
}
