package com.wordvector.pybridge;

import java.util.Optional;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        PythonBridge pythonBridge = new PythonBridge();
        if (pythonBridge.initServer()) {
            logger.info("Server is ready for your requests. Type 'shutdown!' to shutdown the server.");
            System.out.println("Here");
            System.out.println(logger.isDebugEnabled());
            boolean shutdownRequested = false;
            Scanner in = new Scanner(System.in);
            while (!shutdownRequested) {
                String s = in.nextLine();
                if (s.equals("shutdown!")) {
                    shutdownRequested = true;
                    break;
                }
                System.out.println("You requested word vector for: " + s);
                logger.debug("You requested word vector for: " + s);
                Optional<WordVector> optionalVector = pythonBridge.getVector(s);
                if (optionalVector.isPresent()) {
                    System.out.println(optionalVector.get().getVectorEntries());
                    logger.debug(optionalVector.get().getVectorEntries());
                } else {
                    System.out.println("The given input is not included in the model.");
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
