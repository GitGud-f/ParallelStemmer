package org.parallelcomp;

public class ProcessedLine {
    public static final ProcessedLine POISON_PILL = new ProcessedLine(null, null);

    private final String originalLine;
    private final String stemmedLine;

    public ProcessedLine(String originalLine, String stemmedLine) {
        this.originalLine = originalLine;
        this.stemmedLine = stemmedLine;
    }

    public String getOriginalLine() {
        return originalLine;
    }

    public String getStemmedLine() {
        return stemmedLine;
    }

    public boolean isPoisonPill() {
        return this == POISON_PILL;
    }
}