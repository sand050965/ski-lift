package assignment5.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


/**
 * Consumer thread that processes hourly lift ride records.
 * Aggregates ride counts per lift for each hour (1-6).
 */
class HourConsumer implements Runnable {
    private final BlockingQueue<Records.HourRecord> hourQueue;
    private final Map<Integer, Map<Integer, Integer>> hourLiftRides;

    /**
     * Constructs an HourConsumer.
     *
     * @param hourQueue the queue to consume HourRecord objects from
     */
    public HourConsumer(BlockingQueue<Records.HourRecord> hourQueue) {
        this.hourQueue = hourQueue;
        this.hourLiftRides = new HashMap<>();
    }

    /**
     * Processes records from the queue until poison pill is received.
     * Increments ride count for each hour and lift combination.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Records.HourRecord record = hourQueue.take();
                if (record.equals(Records.HourRecord.POISON)) break;

                hourLiftRides
                        .computeIfAbsent(record.hour(), k -> new HashMap<>())
                        .merge(record.liftId(), 1, Integer::sum);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the aggregated hourly lift ride statistics.
     *
     * @return nested map of hour to (liftID to ride count)
     */
    public Map<Integer, Map<Integer, Integer>> getHourLiftRides() {
        return hourLiftRides;
    }
}