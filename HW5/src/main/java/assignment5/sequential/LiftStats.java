package assignment5.sequential;

/**
 * Represents aggregate statistics for a single lift.
 */
public class LiftStats {

    private final int liftId;
    private int totalRides;

    /**
     * Creates a new instance for the given lift.
     *
     * @param liftId the lift ID
     */
    public LiftStats(int liftId) {
        this.liftId = liftId;
        this.totalRides = 0;
    }

    /**
     * Increments the ride count by one.
     */
    public void incrementRides() {
        this.totalRides++;
    }

    /**
     * Returns the lift ID.
     *
     * @return the lift ID
     */
    public int getLiftId() {
        return this.liftId;
    }

    /**
     * Returns the total number of rides for this lift.
     *
     * @return the total number of rides
     */
    public int getTotalRides() {
        return this.totalRides;
    }
}
