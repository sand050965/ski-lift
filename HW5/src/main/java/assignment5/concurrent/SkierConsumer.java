package assignment5.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Consumer thread that processes skier records.
 * Calculates total vertical meters skied for each skier.
 */
class SkierConsumer implements Runnable {
    private final BlockingQueue<Records.SkierRecord> skierQueue;
    private final Map<String, Integer> skierVerticals;

    /**
     * Constructs a SkierConsumer.
     *
     * @param skierQueue the queue to consume SkierRecord objects from
     */
    public SkierConsumer(BlockingQueue<Records.SkierRecord> skierQueue) {
        this.skierQueue = skierQueue;
        this.skierVerticals = new HashMap<>();
    }

    /**
     * Processes records from the queue until poison pill is received.
     * Accumulates vertical meters for each skier based on lift vertical rise.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Records.SkierRecord record = skierQueue.take();
                if (record.equals(Records.SkierRecord.POISON)) break;

                int vertical = VerticalTable.getVertical(record.liftId());
                skierVerticals.merge(record.skierId(), vertical, Integer::sum);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the aggregated vertical statistics for all skiers.
     *
     * @return map of skierID to total vertical meters
     */
    public Map<String, Integer> getSkierVerticals() {
        return skierVerticals;
    }
}
