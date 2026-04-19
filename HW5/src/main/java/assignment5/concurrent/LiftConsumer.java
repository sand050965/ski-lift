package assignment5.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Consumer thread that processes lift ride records.
 * Counts the total number of rides for each lift.
 */
class LiftConsumer implements Runnable {
    private final BlockingQueue<Records.LiftRecord> liftQueue;
    private final Map<Integer, Integer> liftRides;

    /**
     * Constructs a LiftConsumer.
     *
     * @param liftQueue the queue to consume LiftRecord objects from
     */
    public LiftConsumer(BlockingQueue<Records.LiftRecord> liftQueue) {
        this.liftQueue = liftQueue;
        this.liftRides = new HashMap<>();
    }

    /**
     * Processes records from the queue until poison pill is received.
     * Increments ride count for each lift.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Records.LiftRecord record = liftQueue.take();
                if (record.equals(Records.LiftRecord.POISON)) break;

                liftRides.merge(record.liftId(), 1, Integer::sum);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the aggregated lift ride statistics.
     *
     * @return map of liftID to total ride count
     */
    public Map<Integer, Integer> getLiftRides() {
        return liftRides;
    }
}
