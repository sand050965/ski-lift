package assignment5.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LiftConsumerTest {

    @Test
    public void testLiftConsumerCountsRides() throws Exception {
        BlockingQueue<Records.LiftRecord> queue = new ArrayBlockingQueue<>(10);

        queue.put(new Records.LiftRecord(5));
        queue.put(new Records.LiftRecord(5));
        queue.put(new Records.LiftRecord(8));
        queue.put(Records.LiftRecord.POISON);  // 結束 signal

        LiftConsumer consumer = new LiftConsumer(queue);
        consumer.run();

        Map<Integer, Integer> result = consumer.getLiftRides();

        assertEquals(2, result.get(5));
        assertEquals(1, result.get(8));
    }

    @Test
    public void testStopsOnPoison() throws Exception {
        BlockingQueue<Records.LiftRecord> queue = new ArrayBlockingQueue<>(5);

        queue.put(Records.LiftRecord.POISON);

        LiftConsumer consumer = new LiftConsumer(queue);
        consumer.run();

        assertTrue(consumer.getLiftRides().isEmpty());
    }
}