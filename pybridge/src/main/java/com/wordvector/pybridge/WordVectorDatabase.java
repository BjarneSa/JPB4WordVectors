package com.wordvector.pybridge;

import java.util.ArrayList;

/**
 * This is the class WordVectorDatabase.
 * In this database, requested word vectors will be saved and can be requested again without a server request.
 * 
 * @author bjarne
 * @version 1.0
 */
public class WordVectorDatabase {
    private ArrayList<WordVector> requestedVectors;
    
    /**
     * Constructor of WordVectorDatabase.
     * Initializes an empty ArrayList.
     */
    public WordVectorDatabase() {
        requestedVectors = new ArrayList<WordVector>();
    }
    
    /**
     * Adding a vector to the database by putting it into the ArrayList.
     * @param wordVector to be saved
     */
    public void addVector(WordVector wordVector) {
        requestedVectors.add(wordVector);
    }
    
    /**
     * Requesting the database.
     * If the vector is in the database because it was requested during the same runtime, 
     * the word vector will be returned.
     * @param word for which you request a vector
     * @return word vector
     */
    public WordVector getVectorToWord(String word) {
        for(WordVector w: requestedVectors) {
            if(w.getWord().equals(word)) {
                return w;
            }
        } 
        return null;   
    }
    
    /**
     * Clearing array to reset database. Empty array requestedVectors continues to exist.
     */
    public void clearDatabase() {
        requestedVectors.clear();
    }
}
