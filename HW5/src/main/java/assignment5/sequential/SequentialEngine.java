package assignment5.sequential;

import assignment5.SkiDataProcessorEngine;

import java.io.IOException;

/**
 * Sequential implementation of the {@link SkiDataProcessorEngine}.
 *
 * <p>This engine delegates all work to {@link SequentialSkiDataProcessor},
 * generating the three required output files:
 * <ul>
 *     <li>Skiers.csv</li>
 *     <li>Lifts.csv</li>
 *     <li>Hours.csv</li>
 * </ul>
 * The entire input file is processed on a single thread.</p>
 */
public class SequentialEngine implements SkiDataProcessorEngine {

    private final SequentialSkiDataProcessor processor = new SequentialSkiDataProcessor();

    /**
     * Processes the input CSV file sequentially and writes the output CSV files
     * into the specified directory.
     *
     * @param inputFile path to the input CSV file
     * @param outputDir directory where the output CSV files will be created
     * @throws IOException if any file read/write error occurs
     */
    @Override
    public void process(String inputFile, String outputDir) throws IOException {
        String skiersCsv = outputDir + "/Skiers.csv";
        String liftsCsv = outputDir + "/Lifts.csv";
        String hoursCsv = outputDir + "/Hours.csv";

        processor.process(inputFile, skiersCsv, liftsCsv, hoursCsv);
    }
}
