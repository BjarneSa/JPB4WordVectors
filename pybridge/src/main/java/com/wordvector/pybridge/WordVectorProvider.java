package com.wordvector.pybridge;

import java.util.Optional;

/**
 * This interface WordVectorProvider declares methods which are ultimately
 * needed to use the python-java interoperability.
 *
 * @author bjarne
 * @version 1.0
 */
public interface WordVectorProvider {
    /**
     * Implemented, this method should provide the possibility to start the python
     * server from java side. Therefore, it must open the python file referenced in
     * the configuration file and call python.
     * 
     * @return whether the server started successfully
     */
    boolean initServer();

    /**
     * Implemented, this method should provide the possibility to request a word
     * vector with HTTP request and save the data in the database.
     * 
     * @param word for which you request a vector
     * @return vector as WordVector object (attributes: word, vector)
     */
    Optional<WordVector> getVector(String word);

    /**
     * Implemented, this method should provide the option to shutdown the python
     * server gracefully. You can use an endpoint on python side.
     * 
     * @return whether a graceful shutdown was possible
     */
    boolean shutdownServer();
}
