package assignment5.concurrent;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProducerTest {

    @Test
    public void testProducerReadsAndDistributesRecords() throws Exception {

        Path tempFile = Files.createTempFile("ski-test", ".csv");

        try (FileWriter fw = new FileWriter(tempFile.toFile())) {
            fw.write("Resort,Day,Skier,Lift,Time\n");   // header
            fw.write("1,10,101,5,30\n");               // hour = 0 → hour 1
            fw.write("1,10,102,7,150\n");              // hour = 2 → hour 3
        }

        BlockingQueue<Records.SkierRecord> skierQ =
                new ArrayBlockingQueue<>(10);
        BlockingQueue<Records.LiftRecord> liftQ =
                new ArrayBlockingQueue<>(10);
        BlockingQueue<Records.HourRecord> hourQ =
                new ArrayBlockingQueue<>(10);

        Producer producer = new Producer(
                tempFile.toString(), skierQ, liftQ, hourQ
        );

        producer.run();

        // Skier Records
        Records.SkierRecord s1 = skierQ.take();
        Records.SkierRecord s2 = skierQ.take();
        Records.SkierRecord poisonS = skierQ.take();

        assertEquals("101", s1.skierId());
        assertEquals("102", s2.skierId());
        assertEquals(Records.SkierRecord.POISON, poisonS);

        // Lift Records
        Records.LiftRecord l1 = liftQ.take();
        Records.LiftRecord l2 = liftQ.take();
        Records.LiftRecord poisonL = liftQ.take();

        assertEquals(5, l1.liftId());
        assertEquals(7, l2.liftId());
        assertEquals(Records.LiftRecord.POISON, poisonL);

        // Hour Records
        Records.HourRecord h1 = hourQ.take();
        Records.HourRecord h2 = hourQ.take();
        Records.HourRecord poisonH = hourQ.take();

        // hour = timestamp / 60  → 30/60 = 0 + 1 = hour 1
        assertEquals(1, h1.hour());
        assertEquals(5, h1.liftId());

        // timestamp = 150 → hour = 150/60 = 2 + 1 = hour 3
        assertEquals(3, h2.hour());
        assertEquals(7, h2.liftId());

        assertEquals(Records.HourRecord.POISON, poisonH);
    }
}