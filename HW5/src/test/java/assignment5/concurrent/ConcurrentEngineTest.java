package assignment5.concurrent;

import assignment5.SkiDataProcessorEngine;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentEngineTest {

    @Test
    void testConcurrentPipelineEndToEnd() throws Exception {
        Path tempDir = Files.createTempDirectory("ski-test-output");
        Path input = Files.createTempFile("ski-test", ".csv");

        try (FileWriter fw = new FileWriter(input.toFile())) {
            fw.write("Resort,Day,Skier,Lift,Time\n");
            fw.write("1,10,101,5,30\n");
            fw.write("1,10,101,12,90\n");
            fw.write("1,10,202,31,120\n");
        }

        SkiDataProcessorEngine engine = new ConcurrentEngine();

        assertDoesNotThrow(() ->
                engine.process(input.toString(), tempDir.toString())
        );

        File skiers = tempDir.resolve("Skiers.csv").toFile();
        File lifts = tempDir.resolve("Lifts.csv").toFile();
        File hours = tempDir.resolve("Hours.csv").toFile();

        assertTrue(skiers.exists());
        assertTrue(lifts.exists());
        assertTrue(hours.exists());

        String skiersContent = Files.readString(skiers.toPath());
        assertTrue(skiersContent.contains("101,500"));
        assertTrue(skiersContent.contains("202,500"));

        String liftsContent = Files.readString(lifts.toPath());
        assertTrue(liftsContent.contains("5,1"));
        assertTrue(liftsContent.contains("12,1"));
        assertTrue(liftsContent.contains("31,1"));

        String hoursContent = Files.readString(hours.toPath());
        assertTrue(hoursContent.contains("Hour #1"));
        assertTrue(hoursContent.contains("Hour #2"));
        assertTrue(hoursContent.contains("Hour #3"));
        assertTrue(hoursContent.contains("5,1"));
        assertTrue(hoursContent.contains("12,1"));
        assertTrue(hoursContent.contains("31,1"));
    }
}
