package assignment5.sequential;

/**
 * Represents aggregate statistics for a single skier.
 */
public class SkierStats {

    private final String skierId;
    private int totalRides;
    private int totalVertical;

    /**
     * Creates a new instance for the given skier.
     *
     * @param skierId the skier identifier; must not be null or empty
     * @throws IllegalArgumentException if skierId is null or empty
     */
    public SkierStats(String skierId) {
        if (skierId == null || skierId.isEmpty()) {
            throw new IllegalArgumentException("Skier ID must not be null or empty.");
        }
        this.skierId = skierId;
        this.totalRides = 0;
        this.totalVertical = 0;
    }

    /**
     * Records a single lift ride for this skier.
     *
     * @param vertical the vertical meters gained from this ride; must be non-negative
     * @throws IllegalArgumentException if vertical is negative
     */
    public void addRide(int vertical) {
        if (vertical < 0) {
            throw new IllegalArgumentException("Vertical must be non-negative.");
        }
        this.totalRides++;
        this.totalVertical += vertical;
    }

    /**
     * Returns the skier ID.
     *
     * @return the skier ID
     */
    public String getSkierId() {
        return this.skierId;
    }

    /**
     * Returns the total number of rides.
     *
     * @return the total number of rides
     */
    public int getTotalRides() {
        return this.totalRides;
    }

    /**
     * Returns the total vertical skied in meters.
     *
     * @return the total vertical
     */
    public int getTotalVertical() {
        return this.totalVertical;
    }
}
