package org.parallelcomp;

/**
 * Immutable data transfer object that represents a processed line with
 * both original and stemmed content. This class is used to pass data
 * between the processing consumers and the writer consumer.
 *
 * <p>Instances of this class are immutable and thread-safe. The class
 * also provides a special poison pill instance for signaling the end
 * of processing to the writer consumer.
 *
 * @see ProcessingConsumer
 * @see WritingConsumer
 */
public class ProcessedLine {
    /**
     * Special poison pill instance used to signal the end of processing
     * to the writer consumer. When this instance is encountered in the
     * output queue, the writer knows to terminate.
     */
    public static final ProcessedLine POISON_PILL = new ProcessedLine(null, null);

    private final String originalLine;
    private final String stemmedLine;

    /**
     * Constructs a new ProcessedLine with the specified original and stemmed content.
     *
     * @param originalLine The original text line before processing.
     * @param stemmedLine  The stemmed text line after processing.
     */
    public ProcessedLine(String originalLine, String stemmedLine) {
        this.originalLine = originalLine;
        this.stemmedLine = stemmedLine;
    }

    /**
     * Returns the original text line before stemming.
     *
     * @return The original text line, or null if this is a poison pill
     */
    public String getOriginalLine() {
        return originalLine;
    }

    /**
     * Returns the stemmed text line after processing.
     *
     * @return The stemmed text line, or null if this is a poison pill
     */
    public String getStemmedLine() {
        return stemmedLine;
    }

    /**
     * Checks if this instance is the poison pill used for signaling
     * termination to the writer consumer.
     *
     * @return true if this is the poison pill instance, false otherwise
     */
    public boolean isPoisonPill() {
        return this == POISON_PILL;
    }
}