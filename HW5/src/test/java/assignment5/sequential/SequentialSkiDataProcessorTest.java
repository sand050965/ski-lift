package assignment5.sequential;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class SequentialSkiDataProcessorTest {

    /**
     * Integration test:
     *  - creates a small input CSV file
     *  - runs the processor
     *  - verifies that three CSV outputs are created and have the expected headers
     *  - indirectly exercises readAndAggregate, hour mapping, and CSV writers
     */
    @Test
    public void testProcessCreatesCsvOutputs() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "hw5_sequential_processor_test");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        File inputFile = new File(tempDir, "input.csv");
        writeTestInputFile(inputFile);

        File skiersCsv = new File(tempDir, "Skiers_out.csv");
        File liftsCsv = new File(tempDir, "Lifts_out.csv");
        File hoursCsv = new File(tempDir, "Hours_out.csv");

        SequentialSkiDataProcessor processor = new SequentialSkiDataProcessor();
        processor.process(
                inputFile.getAbsolutePath(),
                skiersCsv.getAbsolutePath(),
                liftsCsv.getAbsolutePath(),
                hoursCsv.getAbsolutePath()
        );

        // Files should exist
        assertTrue(skiersCsv.exists(), "Skiers CSV should exist");
        assertTrue(liftsCsv.exists(), "Lifts CSV should exist");
        assertTrue(hoursCsv.exists(), "Hours CSV should exist");

        // Check headers
        assertEquals("SkierID,Vertical", readFirstLine(skiersCsv));
        assertEquals("LiftID,Number of Rides", readFirstLine(liftsCsv));

        // Hours.csv first non-empty line should be "Hour #1"
        assertEquals("Hour #1", readFirstNonEmptyLine(hoursCsv));
    }

    /**
     * Tests computeHourFromTimestamp (private) for valid and invalid inputs using reflection.
     */
    @Test
    public void testComputeHourFromTimestampValidAndInvalid() throws Exception {
        SequentialSkiDataProcessor processor = new SequentialSkiDataProcessor();
        Method method = SequentialSkiDataProcessor.class
                .getDeclaredMethod("computeHourFromTimestamp", int.class);
        method.setAccessible(true);

        // Valid boundary values
        assertEquals(1, invokeComputeHour(method, processor, 0));
        assertEquals(1, invokeComputeHour(method, processor, 59));
        assertEquals(1, invokeComputeHour(method, processor, 60));
        assertEquals(2, invokeComputeHour(method, processor, 119));
        assertEquals(2, invokeComputeHour(method, processor, 120));
        assertEquals(3, invokeComputeHour(method, processor, 180));
        assertEquals(4, invokeComputeHour(method, processor, 240));
        assertEquals(5, invokeComputeHour(method, processor, 300));
        assertEquals(6, invokeComputeHour(method, processor, 360));

        // Invalid: < 0
        InvocationTargetException ex1 = assertThrows(InvocationTargetException.class,
                () -> method.invoke(processor, -1));
        assertTrue(ex1.getCause() instanceof IllegalArgumentException);

        // Invalid: > 360
        InvocationTargetException ex2 = assertThrows(InvocationTargetException.class,
                () -> method.invoke(processor, 361));
        assertTrue(ex2.getCause() instanceof IllegalArgumentException);
    }

    /**
     * Tests isInteger (private static) using reflection.
     */
    @Test
    public void testIsIntegerHelper() throws Exception {
        Method method = SequentialSkiDataProcessor.class
                .getDeclaredMethod("isInteger", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, "123"));
        assertTrue((Boolean) method.invoke(null, "0"));
        assertFalse((Boolean) method.invoke(null, "-1x"));
        assertFalse((Boolean) method.invoke(null, ""));
        assertFalse((Boolean) method.invoke(null, (String) null));
    }

    /**
     * Tests getTopSkiersByVertical (private) using reflection and direct manipulation
     * of the internal skierStatsMap field.
     */
    @Test
    public void testGetTopSkiersByVerticalBranches() throws Exception {
        SequentialSkiDataProcessor processor = new SequentialSkiDataProcessor();

        // Access private field skierStatsMap
        Field field = SequentialSkiDataProcessor.class.getDeclaredField("skierStatsMap");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, SkierStats> map = (Map<String, SkierStats>) field.get(processor);

        SkierStats s1 = new SkierStats("A");
        s1.addRide(100);
        s1.addRide(200); // total = 300

        SkierStats s2 = new SkierStats("B");
        s2.addRide(150); // total = 150

        SkierStats s3 = new SkierStats("C");
        s3.addRide(50);  // total = 50

        map.put("A", s1);
        map.put("B", s2);
        map.put("C", s3);

        Method method = SequentialSkiDataProcessor.class
                .getDeclaredMethod("getTopSkiersByVertical", int.class);
        method.setAccessible(true);

        // Case 1: topN < size (should return only top 2)
        @SuppressWarnings("unchecked")
        List<SkierStats> top2 = (List<SkierStats>) method.invoke(processor, 2);
        assertEquals(2, top2.size());
        assertEquals("A", top2.get(0).getSkierId());
        assertEquals("B", top2.get(1).getSkierId());

        // Case 2: topN >= size (should return all 3)
        @SuppressWarnings("unchecked")
        List<SkierStats> top5 = (List<SkierStats>) method.invoke(processor, 5);
        assertEquals(3, top5.size());
    }

    /**
     * Tests getTopLiftsForHour (private) through a constructed HourlyLiftStats
     * with more entries than topN to exercise both branches.
     */
    @Test
    public void testGetTopLiftsForHourBranches() throws Exception {
        SequentialSkiDataProcessor processor = new SequentialSkiDataProcessor();

        HourlyLiftStats hourly = new HourlyLiftStats(1);
        // Create 5 lifts with different ride counts
        // Lift 1: 3 rides
        hourly.addRideForLift(1);
        hourly.addRideForLift(1);
        hourly.addRideForLift(1);

        // Lift 2: 1 ride
        hourly.addRideForLift(2);

        // Lift 3: 2 rides
        hourly.addRideForLift(3);
        hourly.addRideForLift(3);

        // Lift 4: 4 rides
        hourly.addRideForLift(4);
        hourly.addRideForLift(4);
        hourly.addRideForLift(4);
        hourly.addRideForLift(4);

        // Lift 5: 1 ride
        hourly.addRideForLift(5);

        Method method = SequentialSkiDataProcessor.class
                .getDeclaredMethod("getTopLiftsForHour", HourlyLiftStats.class, int.class);
        method.setAccessible(true);

        // Case 1: topN larger than list size
        @SuppressWarnings("unchecked")
        List<LiftStats> all = (List<LiftStats>) method.invoke(processor, hourly, 10);
        assertEquals(5, all.size());
        // First should be lift 4 (4 rides)
        assertEquals(4, all.get(0).getLiftId());
        assertEquals(4, all.get(0).getTotalRides());

        // Case 2: topN smaller than list size (e.g., 2)
        @SuppressWarnings("unchecked")
        List<LiftStats> top2 = (List<LiftStats>) method.invoke(processor, hourly, 2);
        assertEquals(2, top2.size());
        assertEquals(4, top2.get(0).getLiftId());
        assertEquals(1, top2.get(1).getLiftId()); // second is lift 1 (3 rides)
    }

    // ---------- Helper methods for tests ----------

    private int invokeComputeHour(Method method, SequentialSkiDataProcessor processor, int timestamp)
            throws Exception {
        Object result = method.invoke(processor, timestamp);
        return (Integer) result;
    }

    private void writeTestInputFile(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // header
            writer.write("resort,day,skier,lift,time\n");

            // 101 unique skiers to exercise topN logic (top 100)
            for (int i = 0; i <= 100; i++) {
                String resort = "0";
                int day = 1;
                String skier = "Skier-" + i;
                int lift = (i % 40) + 1; // 1..40
                int time = (i % 6) * 60 + 5; // 5,65,125,185,245,305
                writer.write(resort + "," + day + "," + skier + "," + lift + "," + time + "\n");
            }

            // Extra lines to ensure at least one hour has more than 10 distinct lifts
            // All in hour 1 (time around 10)
            for (int lift = 1; lift <= 12; lift++) {
                writer.write("0,1,Extra-" + lift + "," + lift + ",10\n");
            }
        }
    }

    private String readFirstLine(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line == null ? "" : line;
        }
    }

    private String readFirstNonEmptyLine(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null && line.trim().isEmpty()) {
                line = reader.readLine();
            }
            return line == null ? "" : line;
        }
    }
}
