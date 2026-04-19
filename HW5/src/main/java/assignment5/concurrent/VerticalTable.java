package assignment5.concurrent;

/**
 * Utility class for getting vertical rise (meters) for ski lifts.
 * Lifts 1-10: 200m, 11-20: 300m, 21-30: 400m, 31-40: 500m
 */
public final class VerticalTable {
    /**
     * Vertical meters for 200m lifts.
     */
    public static final int VERTICAL_200 = 200;

    /**
     * Minimum lift ID: 1 for 200m vertical lifts.
     */
    public static final int VERTICAL_200_MIN_LIFT_ID = 1;

    /**
     * Maximum lift ID: 10 for 200m vertical lifts.
     */
    public static final int VERTICAL_200_MAX_LIFT_ID = 10;

    /**
     * Vertical meters for 300m lifts.
     */
    public static final int VERTICAL_300 = 300;

    /**
     * Minimum lift ID: 11 for 300m vertical lifts.
     */
    public static final int VERTICAL_300_MIN_LIFT_ID = 11;

    /**
     * Maximum lift ID: 20 for 300m vertical lifts.
     */
    public static final int VERTICAL_300_MAX_LIFT_ID = 20;

    /**
     * Vertical meters for 400m lifts.
     */
    public static final int VERTICAL_400 = 400;

    /**
     * Minimum lift ID: 21 for 400m vertical lifts.
     */
    public static final int VERTICAL_400_MIN_LIFT_ID = 21;

    /**
     * Maximum lift ID: 30 for 400m vertical lifts.
     */
    public static final int VERTICAL_400_MAX_LIFT_ID = 30;

    /**
     * Vertical meters for 500m lifts.
     */
    public static final int VERTICAL_500 = 500;

    /**
     * Minimum lift ID: 31 for 500m vertical lifts.
     */
    public static final int VERTICAL_500_MIN_LIFT_ID = 31;

    /**
     * Maximum lift ID: 40 for 500m vertical lifts.
     */
    public static final int VERTICAL_500_MAX_LIFT_ID = 40;


    private VerticalTable() {
    }

    /**
     * Returns the vertical rise in meters for the given lift.
     *
     * @param liftId the lift ID (1-40)
     * @return vertical rise in meters
     * @throws IllegalArgumentException if liftId is not in range 1-40
     */
    public static int getVertical(int liftId) {
        if (liftId >= VERTICAL_200_MIN_LIFT_ID && liftId <= VERTICAL_200_MAX_LIFT_ID)
            return VERTICAL_200;
        if (liftId >= VERTICAL_300_MIN_LIFT_ID && liftId <= VERTICAL_300_MAX_LIFT_ID)
            return VERTICAL_300;
        if (liftId >= VERTICAL_400_MIN_LIFT_ID && liftId <= VERTICAL_400_MAX_LIFT_ID)
            return VERTICAL_400;
        if (liftId >= VERTICAL_500_MIN_LIFT_ID && liftId <= VERTICAL_500_MAX_LIFT_ID)
            return VERTICAL_500;
        throw new IllegalArgumentException("Invalid liftId: " + liftId);
    }
}
