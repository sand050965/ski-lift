package assignment5.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SkierConsumerTest {

    @Test
    public void testSkierVerticalAccumulation() throws Exception {
        BlockingQueue<Records.SkierRecord> queue = new ArrayBlockingQueue<>(10);

        // To Skier 101:
        // lift 5 → vertical = 200m
        // lift 12 → vertical = 300m
        queue.put(new Records.SkierRecord("101", 5));
        queue.put(new Records.SkierRecord("101", 12));

        // To Skier 999:
        // lift 25 → vertical = 400m
        queue.put(new Records.SkierRecord("999", 25));

        queue.put(Records.SkierRecord.POISON);

        SkierConsumer consumer = new SkierConsumer(queue);
        consumer.run();

        Map<String, Integer> result = consumer.getSkierVerticals();

        assertEquals(500, result.get("101")); // skier 101 vertical sum
        assertEquals(400, result.get("999")); // skier 999 vertical
    }

    @Test
    public void testStopsOnPoison() throws Exception {
        BlockingQueue<Records.SkierRecord> queue = new ArrayBlockingQueue<>(5);

        queue.put(Records.SkierRecord.POISON); // no real data

        SkierConsumer consumer = new SkierConsumer(queue);
        consumer.run();

        assertTrue(consumer.getSkierVerticals().isEmpty());
    }
}
