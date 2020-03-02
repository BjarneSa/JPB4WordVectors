package com.wordvector.pybridge;

import java.util.List;

/**
 * This is the class WordVector. A word vector has a natural word as identifier
 * and an array of double values (commonly 300).
 *
 * @author bjarne
 * @version 1.0
 */
public class WordVector {
    private String word;
    private List<Double> vector;

    public WordVector(String word) {
        this.word = word;
    }

    public void setVector(List<Double> vectorData) {
        vector = vectorData;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Double> getVectorEntries() {
        return vector;
    }

    /**
     * Getting a specific vector entry at the given index.
     *
     * @param index of array
     * @return value as double
     */
    public double getVectorEntry(int index) {
        return vector.get(index);
    }

    /**
     * Length of the word vector is number of entries.
     *
     * @return
     */
    public int getLength() {
        return vector.size();
    }
}
