package assignment5;

import java.io.IOException;

public interface SkiDataProcessorEngine {
    /**
     * Process the input data and produce Skiers.csv, Lifts.csv, and Hours.csv.
     *
     * @param inputFile path to raw input CSV
     * @param outputDir path to output folder
     */
    void process(String inputFile, String outputDir) throws IOException;
}
