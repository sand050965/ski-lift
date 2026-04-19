package assignment5.sequential;

/**
 * Provides a mapping from lift IDs to their vertical rise in meters.
 */
public final class LiftVerticalMapping {

    /** Minimum lift ID. */
    public static final int MIN_LIFT_ID = 1;

    /** Maximum lift ID. */
    public static final int MAX_LIFT_ID = 40;

    /** Vertical rise for lifts 1-10. */
    private static final int VERTICAL_1_TO_10 = 200;

    /** Vertical rise for lifts 11-20. */
    private static final int VERTICAL_11_TO_20 = 300;

    /** Vertical rise for lifts 21-30. */
    private static final int VERTICAL_21_TO_30 = 400;

    /** Vertical rise for lifts 31-40. */
    private static final int VERTICAL_31_TO_40 = 500;

    private LiftVerticalMapping() {
        // Prevent instantiation.
    }

    /**
     * Returns the vertical rise for the given lift ID.
     *
     * @param liftId the lift ID
     * @return the vertical rise in meters
     * @throws IllegalArgumentException if the lift ID is outside the valid range
     */
    public static int getVerticalForLift(int liftId) {
        if (liftId < MIN_LIFT_ID || liftId > MAX_LIFT_ID) {
            throw new IllegalArgumentException("Invalid lift ID: " + liftId);
        }

        if (liftId <= 10) {
            return VERTICAL_1_TO_10;
        } else if (liftId <= 20) {
            return VERTICAL_11_TO_20;
        } else if (liftId <= 30) {
            return VERTICAL_21_TO_30;
        } else {
            return VERTICAL_31_TO_40;
        }
    }
}
