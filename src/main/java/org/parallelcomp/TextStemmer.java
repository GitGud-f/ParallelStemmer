package org.parallelcomp;

import org.tartarus.snowball.ext.EnglishStemmer;

public class TextStemmer {

    private final EnglishStemmer stemmer;

    public TextStemmer() {
        this.stemmer = new EnglishStemmer();
    }

    /**
     * Text stemming utility using Snowball stemmer.
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
