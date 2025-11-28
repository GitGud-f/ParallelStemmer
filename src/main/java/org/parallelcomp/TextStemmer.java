package org.parallelcomp;

import org.tartarus.snowball.ext.EnglishStemmer;

/**
 * Text stemming utility that uses the Snowball stemmer for English text.
 * This class provides a thread-safe interface for stemming individual
 * words or entire text lines.
 *
 * <p>Each instance maintains its own Snowball stemmer, making it safe
 * for use in multiple concurrent threads. The stemming process converts
 * words to their root form (e.g., "running" → "run", "beautifully" → "beauti").
 *
 * @see ProcessingConsumer
 */
public class TextStemmer {

    private final EnglishStemmer stemmer;

    /**
     * Constructs a new TextStemmer instance.
     * Each instance maintains its own Snowball stemmer for thread safety.
     */
    public TextStemmer() {
        this.stemmer = new EnglishStemmer();
    }

    /**
     * Stems all words in the given text line.
     *
     * <p>This method:
     * <ul>
     *   <li>Splits the input text into individual words</li>
     *   <li>Converts each word to lowercase for consistent stemming</li>
     *   <li>Applies the Snowball stemmer to each word</li>
     *   <li>Reassembles the stemmed words into a single string</li>
     * </ul>
     *
     * @param text The input text to stem, may be null or empty
     * @return The stemmed text, or the original text if input is null/empty
     * @see EnglishStemmer#setCurrent(String)
     * @see EnglishStemmer#stem()
     * @see EnglishStemmer#getCurrent()
     */
    public String stemText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        String[] words = text.split("\\s+");
        StringBuilder stemmedText = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                stemmer.setCurrent(word.toLowerCase());
                if (stemmer.stem()) {
                    stemmedText.append(stemmer.getCurrent());
                } else {
                    stemmedText.append(word);
                }
                stemmedText.append(" ");
            }
        }

        return stemmedText.toString().trim();

    }

}
