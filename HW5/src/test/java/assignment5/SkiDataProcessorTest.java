package assignment5;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class SkiDataProcessorTest {

    @Test
    public void testTooFewArguments() {
        String[] args = {"onlyOne"};
        assertThrows(IllegalArgumentException.class,
                () -> SkiDataProcessor.run(args));
    }

    @Test
    public void testMissingInputFile() {
        String[] args = {"no_such_file_888.csv", "outDir"};
        assertThrows(IllegalArgumentException.class,
                () -> SkiDataProcessor.run(args));
    }

    @Test
    public void testValidSequentialDefaultMode() throws Exception {
        File tempDir = Files.createTempDirectory("ski-test").toFile();
        File input = new File(tempDir, "input.csv");
        writeSmallInput(input);

        String[] args = {
                input.getAbsolutePath(),
                tempDir.getAbsolutePath()
        };

        assertDoesNotThrow(() -> SkiDataProcessor.run(args));

        assertTrue(new File(tempDir, "Skiers.csv").exists());
        assertTrue(new File(tempDir, "Lifts.csv").exists());
        assertTrue(new File(tempDir, "Hours.csv").exists());
    }

    private void writeSmallInput(File file) throws Exception {
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("resort,day,skier,lift,time");
            pw.println("0,1,Skier-1,5,10");
            pw.println("0,1,Skier-2,10,120");
        }
    }
}
