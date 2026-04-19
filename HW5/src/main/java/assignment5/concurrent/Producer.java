package assignment5.concurrent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Producer thread that reads CSV file and distributes records to three queues.
 * Sends poison pills after reading all data.
 */
public class Producer implements Runnable {
    public static final int MINUTES_PER_HOUR = 60;

    private final String filePath;
    private final BlockingQueue<Records.SkierRecord> skierQueue;
    private final BlockingQueue<Records.LiftRecord> liftQueue;
    private final BlockingQueue<Records.HourRecord> hourQueue;

    /**
     * Constructs a Producer.
     *
     * @param filePath   path to the CSV input file
     * @param skierQueue queue for skier records
     * @param liftQueue  queue for lift records
     * @param hourQueue  queue for hourly records
     */
    public Producer(String filePath,
                    BlockingQueue<Records.SkierRecord> skierQueue,
                    BlockingQueue<Records.LiftRecord> liftQueue,
                    BlockingQueue<Records.HourRecord> hourQueue) {
        this.filePath = filePath;
        this.skierQueue = skierQueue;
        this.liftQueue = liftQueue;
        this.hourQueue = hourQueue;
    }

    /**
     * Reads CSV file and distributes records to queues.
     * Sends poison pills when done to signal consumers to terminate.
     */
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String skierId = tokens[2].trim();
                int liftId = Integer.parseInt(tokens[3].trim());
                int timestamp = Integer.parseInt(tokens[4].trim());

                int hour = (timestamp - 1) / MINUTES_PER_HOUR + 1;
                skierQueue.put(new Records.SkierRecord(skierId, liftId));
                liftQueue.put(new Records.LiftRecord(liftId));
                hourQueue.put(new Records.HourRecord(hour, liftId));
            }

            skierQueue.put(Records.SkierRecord.POISON);
            liftQueue.put(Records.LiftRecord.POISON);
            hourQueue.put(Records.HourRecord.POISON);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}