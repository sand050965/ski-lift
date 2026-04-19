package assignment5.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HourConsumerTest {

    @Test
    public void testBasicProcessing() throws Exception {
        BlockingQueue<Records.HourRecord> queue = new ArrayBlockingQueue<>(10);

        queue.put(new Records.HourRecord(1, 5));
        queue.put(new Records.HourRecord(1, 5));
        queue.put(new Records.HourRecord(2, 3));
        queue.put(Records.HourRecord.POISON);

        HourConsumer consumer = new HourConsumer(queue);
        consumer.run();

        Map<Integer, Map<Integer, Integer>> result = consumer.getHourLiftRides();

        assertEquals(2, result.get(1).get(5));
        assertEquals(1, result.get(2).get(3));
    }

    @Test
    public void testStopsOnPoison() throws Exception {
        BlockingQueue<Records.HourRecord> queue = new ArrayBlockingQueue<>(5);
        queue.put(Records.HourRecord.POISON);

        HourConsumer consumer = new HourConsumer(queue);
        consumer.run();

        assertTrue(consumer.getHourLiftRides().isEmpty());
    }
}