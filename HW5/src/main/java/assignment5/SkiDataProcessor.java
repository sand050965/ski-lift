package assignment5;

import assignment5.concurrent.ConcurrentEngine;
import assignment5.sequential.SequentialEngine;

import java.io.File;

/**
 * Main entry point for the ski data processing program.
 *
 * <p>This class handles command-line argument parsing, validates the input and output
 * paths, selects a processing mode (sequential or concurrent), and delegates the work
 * to the corresponding {@link SkiDataProcessorEngine} implementation.</p>
 *
 * <p>Usage:</p>
 * <pre>
 *   SkiDataProcessor (--concurrent | --sequential | -c | -s) &lt;inputFile&gt; &lt;outputDir&gt;
 *   SkiDataProcessor &lt;inputFile&gt; &lt;outputDir&gt;   // defaults to sequential
 * </pre>
 */
public class SkiDataProcessor {
    /**
     * Number of required command-line arguments.
     */
    public static final int REQUIRED_ARGS_NUM = 2;

    /**
     * Parses arguments, validates input/output paths, chooses the processing mode,
     * and runs the appropriate engine.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Core logic extracted from main so it is cleanly testable.
     */
    public static void run(String[] args) throws Exception {
        if (args.length < REQUIRED_ARGS_NUM) {
            throw new IllegalArgumentException(
                    "Usage: SkiDataProcessor (--concurrent | --sequential | -c | -s) <inputFile> <outputDir>"
            );
        }

        ProcessingMode mode;
        String inputFile;
        String outputDir;

        if (args.length == REQUIRED_ARGS_NUM) {
            mode = ProcessingMode.SEQUENTIAL; // default
            inputFile = args[0];
            outputDir = args[1];
        } else {
            mode = ProcessingMode.fromFlag(args[0]);
            inputFile = args[1];
            outputDir = args[2];
        }

        validateInputFile(inputFile);
        ensureOutputDir(outputDir);

        long start = System.currentTimeMillis();

        SkiDataProcessorEngine engine =
                (mode == ProcessingMode.CONCURRENT)
                        ? new ConcurrentEngine()
                        : new SequentialEngine();

        engine.process(inputFile, outputDir);

        long end = System.currentTimeMillis();
        System.out.println("Total time: " + (end - start) + " ms");
    }

    /**
     * Ensures that the input file exists and is readable.
     *
     * @param path input file path
     */
    private static void validateInputFile(String path) {
        File file = new File(path);

        if (!file.exists()) {
            throw new IllegalArgumentException("Error: Input file does not exist: " + path);
        }

        if (!file.isFile()) {
            throw new IllegalArgumentException("Error: Input path is not a file: " + path);
        }

        if (!file.canRead()) {
            throw new IllegalArgumentException("Error: Cannot read input file: " + path);
        }
    }

    /**
     * Ensures that the output directory exists or can be created.
     *
     * @param path output directory path
     */
    private static void ensureOutputDir(String path) {
        File dir = new File(path);

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalArgumentException("Error: Could not create output directory: " + path);
        }

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Error: Output path is not a directory: " + path);
        }
    }
}
