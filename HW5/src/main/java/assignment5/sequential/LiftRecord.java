package assignment5.sequential;

/**
 * Represents a single input record from the ski lift data file.
 */
public class LiftRecord {

    private final String resortId;
    private final int dayNumber;
    private final int timestamp;
    private final String skierId;
    private final int liftId;

    /**
     * Creates a new record instance.
     *
     * @param resortId  the resort identifier
     * @param dayNumber the day number in the season
     * @param timestamp the number of minutes after opening (0-360)
     * @param skierId   the skier identifier
     * @param liftId    the lift ID
     */
    public LiftRecord(String resortId, int dayNumber, int timestamp,
                      String skierId, int liftId) {
        this.resortId = resortId;
        this.dayNumber = dayNumber;
        this.timestamp = timestamp;
        this.skierId = skierId;
        this.liftId = liftId;
    }

    /**
     * Returns the resort ID.
     *
     * @return the resort ID
     */
    public String getResortId() {
        return this.resortId;
    }

    /**
     * Returns the day number.
     *
     * @return the day number
     */
    public int getDayNumber() {
        return this.dayNumber;
    }

    /**
     * Returns the timestamp in minutes after opening.
     *
     * @return the timestamp
     */
    public int getTimestamp() {
        return this.timestamp;
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
     * Returns the lift ID.
     *
     * @return the lift ID
     */
    public int getLiftId() {
        return this.liftId;
    }
}
