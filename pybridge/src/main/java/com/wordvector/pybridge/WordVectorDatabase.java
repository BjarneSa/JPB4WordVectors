package com.wordvector.pybridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is the class WordVectorDatabase. In this database, requested word
 * vectors will be saved and can be requested again without a server request.
 *
 * @author bjarne
 * @version 1.0
 */
public class WordVectorDatabase {
    private List<WordVector> requestedVectors;

    /**
     * Constructor of WordVectorDatabase. Initializes an empty ArrayList.
     */
    public WordVectorDatabase() {
        requestedVectors = new ArrayList<>();
    }

    /**
     * Adding a vector to the database by putting it into the ArrayList.
     *
     * @param wordVector to be saved
     */
    public void addVector(WordVector wordVector) {
        requestedVectors.add(wordVector);
    }

    /**
     * Requesting the database. If the vector is in the database because it was
     * requested during the same runtime, the word vector will be returned.
     *
     * @param word for which you request a vector
     * @return word vector
     */
    public Optional<WordVector> getVectorToWord(String word) {
        Optional<WordVector> optionalVector = Optional.empty();
        for (WordVector w : requestedVectors) {
            if (w.getWord().equals(word)) {
                optionalVector = Optional.of(w);
            }
        }
        return optionalVector;
    }

    /**
     * Clearing array to reset database. Empty array requestedVectors continues to
     * exist.
     */
    public void clearDatabase() {
        requestedVectors.clear();
    }
}
