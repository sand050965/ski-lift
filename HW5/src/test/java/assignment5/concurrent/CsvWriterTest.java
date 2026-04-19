package assignment5.concurrent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {

    @TempDir
    File tempDir;

    @Test
    void testWriteSkiersCSVNormal() throws IOException {
        Map<String, Integer> skierMap = new HashMap<>();
        skierMap.put("1", 100);
        skierMap.put("2", 200);

        CsvWriter.writeSkiersCSV(skierMap, tempDir.getAbsolutePath());

        File file = new File(tempDir, "Skiers.csv");
        assertTrue(file.exists());

        String content = Files.readString(file.toPath());
        assertTrue(content.contains("SkierID,Vertical"));
        assertTrue(content.contains("1,100"));
        assertTrue(content.contains("2,200"));
    }

    @Test
    void testWriteLiftsCSVNormal() throws IOException {
        Map<Integer, Integer> liftMap = new HashMap<>();
        liftMap.put(1, 50);
        liftMap.put(2, 70);

        CsvWriter.writeLiftsCSV(liftMap, tempDir.getAbsolutePath());

        File file = new File(tempDir, "Lifts.csv");
        assertTrue(file.exists());

        String content = Files.readString(file.toPath());
        assertTrue(content.contains("LiftID,Number of Rides"));
        assertTrue(content.contains("1,50"));
        assertTrue(content.contains("2,70"));
    }

    @Test
    void testWriteHoursCSVNormal() throws IOException {
        Map<Integer, Map<Integer, Integer>> hourMap = new HashMap<>();
        Map<Integer, Integer> liftMap = new HashMap<>();
        liftMap.put(1, 10);
        liftMap.put(2, 20);
        hourMap.put(1, liftMap);

        CsvWriter.writeHoursCSV(hourMap, tempDir.getAbsolutePath());

        File file = new File(tempDir, "Hours.csv");
        assertTrue(file.exists());

        String content = Files.readString(file.toPath());
        assertTrue(content.contains("Hour #1"));
        assertTrue(content.contains("LiftID,Number of Rides"));
        assertTrue(content.contains("1,10"));
        assertTrue(content.contains("2,20"));
    }

    @Test
    void testWriteSkiersCSVIOException() {
        Map<String, Integer> skierMap = new HashMap<>();
        skierMap.put("1", 100);

        String invalidDir = "/invalid_path_for_test";

        IOException thrown = assertThrows(IOException.class, () ->
                CsvWriter.writeSkiersCSV(skierMap, invalidDir)
        );

        assertTrue(thrown.getMessage().contains("invalid_path_for_test"));
    }

    @Test
    void testWriteLiftsCSVIOException() {
        Map<Integer, Integer> liftMap = new HashMap<>();
        liftMap.put(1, 50);

        String invalidDir = "/invalid_path_for_test";

        IOException thrown = assertThrows(IOException.class, () ->
                CsvWriter.writeLiftsCSV(liftMap, invalidDir)
        );

        assertTrue(thrown.getMessage().contains("invalid_path_for_test"));
    }

    @Test
    void testWriteHoursCSVIOException() {
        Map<Integer, Map<Integer, Integer>> hourMap = new HashMap<>();
        Map<Integer, Integer> liftMap = new HashMap<>();
        liftMap.put(1, 10);
        hourMap.put(1, liftMap);

        String invalidDir = "/invalid_path_for_test";

        IOException thrown = assertThrows(IOException.class, () ->
                CsvWriter.writeHoursCSV(hourMap, invalidDir)
        );

        assertTrue(thrown.getMessage().contains("invalid_path_for_test"));
    }
}