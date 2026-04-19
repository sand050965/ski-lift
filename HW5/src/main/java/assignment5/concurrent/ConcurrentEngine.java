package assignment5.concurrent;

import assignment5.SkiDataProcessorEngine;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Concurrent engine that processes ski lift CSV data using a producer thread and
 * multiple consumer threads for skier, lift, and hour aggregation.
 * <p>
 * The producer reads input records into three blocking queues. Each group of
 * consumers processes its queue independently. After the producer finishes, the
 * engine inserts poison-pill records, waits for termination, merges all partial
 * results, and writes final CSV output files.
 */
public class ConcurrentEngine implements SkiDataProcessorEngine {

    private static final int SKIER_CONSUMER_COUNT =
            Integer.getInteger("skierThreads", 1);
    private static final int LIFT_CONSUMER_COUNT =
            Integer.getInteger("liftThreads", 1);
    private static final int HOUR_CONSUMER_COUNT =
            Integer.getInteger("hourThreads", 1);

    private static final int QUEUE_CAPACITY = 10_000;

    @Override
    public void process(String inputFile, String outputDir) {
        BlockingQueue<Records.SkierRecord> skierQueue =
                new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<Records.LiftRecord> liftQueue =
                new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<Records.HourRecord> hourQueue =
                new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        Thread producerThread = new Thread(
                new Producer(inputFile, skierQueue, liftQueue, hourQueue)
        );

        List<SkierConsumer> skierConsumers = new ArrayList<>(SKIER_CONSUMER_COUNT);
        List<Thread> skierThreads = new ArrayList<>(SKIER_CONSUMER_COUNT);
        for (int i = 0; i < SKIER_CONSUMER_COUNT; i++) {
            SkierConsumer c = new SkierConsumer(skierQueue);
            skierConsumers.add(c);
            skierThreads.add(new Thread(c));
        }

        List<LiftConsumer> liftConsumers = new ArrayList<>(LIFT_CONSUMER_COUNT);
        List<Thread> liftThreads = new ArrayList<>(LIFT_CONSUMER_COUNT);
        for (int i = 0; i < LIFT_CONSUMER_COUNT; i++) {
            LiftConsumer c = new LiftConsumer(liftQueue);
            liftConsumers.add(c);
            liftThreads.add(new Thread(c));
        }

        List<HourConsumer> hourConsumers = new ArrayList<>(HOUR_CONSUMER_COUNT);
        List<Thread> hourThreads = new ArrayList<>(HOUR_CONSUMER_COUNT);
        for (int i = 0; i < HOUR_CONSUMER_COUNT; i++) {
            HourConsumer c = new HourConsumer(hourQueue);
            hourConsumers.add(c);
            hourThreads.add(new Thread(c));
        }

        producerThread.start();
        skierThreads.forEach(Thread::start);
        liftThreads.forEach(Thread::start);
        hourThreads.forEach(Thread::start);

        try {
            producerThread.join();

            for (int i = 0; i < SKIER_CONSUMER_COUNT; i++) {
                skierQueue.put(Records.SkierRecord.POISON);
            }
            for (int i = 0; i < LIFT_CONSUMER_COUNT; i++) {
                liftQueue.put(Records.LiftRecord.POISON);
            }
            for (int i = 0; i < HOUR_CONSUMER_COUNT; i++) {
                hourQueue.put(Records.HourRecord.POISON);
            }

            for (Thread t : skierThreads) t.join();
            for (Thread t : liftThreads) t.join();
            for (Thread t : hourThreads) t.join();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        Map<String, Integer> mergedSkiers = mergeSkiers(skierConsumers);
        Map<Integer, Integer> mergedLifts = mergeLifts(liftConsumers);
        Map<Integer, Map<Integer, Integer>> mergedHours = mergeHours(hourConsumers);

        File outDirFile = new File(outputDir);

        try {
            CsvWriter.writeSkiersCSV(mergedSkiers, outDirFile.getAbsolutePath());
            CsvWriter.writeLiftsCSV(mergedLifts, outDirFile.getAbsolutePath());
            CsvWriter.writeHoursCSV(mergedHours, outDirFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Merges skier vertical totals from all skier consumers.
     *
     * @param consumers list of skier consumers
     * @return merged map of skierId → totalVertical
     */
    private Map<String, Integer> mergeSkiers(List<SkierConsumer> consumers) {
        Map<String, Integer> result = new HashMap<>();
        for (SkierConsumer c : consumers) {
            for (var e : c.getSkierVerticals().entrySet()) {
                result.merge(e.getKey(), e.getValue(), Integer::sum);
            }
        }
        return result;
    }

    /**
     * Merges total lift ride counts from all lift consumers.
     *
     * @param consumers list of lift consumers
     * @return merged map of liftId → rideCount
     */
    private Map<Integer, Integer> mergeLifts(List<LiftConsumer> consumers) {
        Map<Integer, Integer> result = new HashMap<>();
        for (LiftConsumer c : consumers) {
            for (var e : c.getLiftRides().entrySet()) {
                result.merge(e.getKey(), e.getValue(), Integer::sum);
            }
        }
        return result;
    }

    /**
     * Merges hourly lift ride counts from all hour consumers.
     *
     * @param consumers list of hour consumers
     * @return merged map of hour → (liftId → rideCount)
     */
    private Map<Integer, Map<Integer, Integer>> mergeHours(List<HourConsumer> consumers) {
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
        for (HourConsumer c : consumers) {
            for (var hourEntry : c.getHourLiftRides().entrySet()) {
                int hour = hourEntry.getKey();
                Map<Integer, Integer> lifts = hourEntry.getValue();
                result.computeIfAbsent(hour, h -> new HashMap<>());
                for (var liftEntry : lifts.entrySet()) {
                    result.get(hour).merge(liftEntry.getKey(), liftEntry.getValue(), Integer::sum);
                }
            }
        }
        return result;
    }
}
