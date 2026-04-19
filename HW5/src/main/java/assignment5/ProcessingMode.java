package assignment5;

public enum ProcessingMode {
    SEQUENTIAL,
    CONCURRENT;

    /**
     * Convert a CLI flag into a ProcessingMode.
     */
    public static ProcessingMode fromFlag(String flag) {
        return switch (flag) {
            case "-s", "--sequential" -> SEQUENTIAL;
            case "-c", "--concurrent" -> CONCURRENT;
            default -> throw new IllegalArgumentException(
                    "Unknown mode: " + flag +
                            " (expected --sequential, -s, --concurrent, or -c)"
            );
        };
    }
}
