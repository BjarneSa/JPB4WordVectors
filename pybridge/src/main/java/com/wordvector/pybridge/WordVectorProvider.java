package com.wordvector.pybridge;

import java.io.IOException;

/**
 * This interface WordVectorProvider declares methods 
 * which are ultimately needed to use the python-java interoperability.
 * 
 * @author bjarne
 * @version 1.0
 */
public interface WordVectorProvider {
    /**
     * Implemented, this method should provide the possibility to start the python server from java side.
     * Therefore, it must open the python file referenced in the configuration file and call python.
     * @return whether the server started successfully
     * @throws IOException from BufferedReader
     * @throws InterruptedException if an error occurred during interaction with server
     * @throws IllegalArgumentException from Runtime.exec
     */
    boolean initServer() throws IOException, InterruptedException, IllegalArgumentException;
    
    
    /**
     * Implemented, this method should provide the possiblity to request a wotrd vector with HTTP request
     * and save the data in the database.
     * @param word for which you request a vector
     * @return vector as WordVector object (attributes: word, vector)
     * @throws IOException from HTTP requests
     */
    WordVector getVector(String word) throws IOException;
}
