package assignment5.sequential;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks the number of rides per lift for a single hour.
 */
public class HourlyLiftStats {

    /** The initial ride count for a lift in this hour. */
    private static final int INITIAL_RIDE_COUNT = 0;

    /** The increment added when recording one ride. */
    private static final int RIDE_INCREMENT = 1;

    private final int hourNumber;
    private final Map<Integer, Integer> ridesPerLift;

    /**
     * Creates a new instance for the given hour.
     *
     * @param hourNumber the hour number (1–6)
     */
    public HourlyLiftStats(int hourNumber) {
        this.hourNumber = hourNumber;
        this.ridesPerLift = new HashMap<>();
    }

    /**
     * Records a ride for the given lift ID.
     *
     * @param liftId the lift ID
     */
    public void addRideForLift(int liftId) {
        int current = this.ridesPerLift.getOrDefault(liftId, INITIAL_RIDE_COUNT);
        this.ridesPerLift.put(liftId, current + RIDE_INCREMENT);
    }

    /**
     * Returns the hour number represented by this stats object.
     *
     * @return the hour number
     */
    public int getHourNumber() {
        return this.hourNumber;
    }

    /**
     * Returns a copy of the rides-per-lift mapping.
     * This prevents external modification of the internal map.
     *
     * @return a defensive copy of the rides-per-lift map
     */
    public Map<Integer, Integer> getRidesPerLift() {
        return new HashMap<>(this.ridesPerLift);
    }
}
