package com.wordvector.pybridge;

import java.util.Optional;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * This is the class App. It has method main which is the entry point of the
 * program.
 *
 * @author bjarne
 * @version 1.1
 */
public class App {

    private static Logger logger = LogManager.getLogger(App.class);

    /**
     * Main method: entry point of the program It initializes the server. Afterwards
     * the user can request word vectors from the command line. Server can be
     * shutdown by typing 'shutdown!'.
     *
     * @param args
     */
    public static void main(String[] args) {
        Configurator.setRootLevel(Level.DEBUG);

        PythonBridge pythonBridge = new PythonBridge();
        if (pythonBridge.initServer()) {
            logger.info("Server is ready for your requests. Type 'shutdown!' to shutdown the server.");
            boolean shutdownRequested = false;
            Scanner in = new Scanner(System.in);
            while (!shutdownRequested) {
                String s = in.nextLine();
                if (s.equals("shutdown!")) {
                    shutdownRequested = true;
                    break;
                }
                logger.debug("You requested word vector for: " + s);
                Optional<WordVector> optionalVector = pythonBridge.getVector(s);
                if (optionalVector.isPresent()) {
                    logger.debug(optionalVector.get().getVectorEntries());
                } else {
                    logger.debug("The given input is not included in the model.");
                }
            }

            in.close();
            pythonBridge.shutdownServer();
        } else {
            logger.error("Server could not be started.");
        }
    }
}
