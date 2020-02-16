package com.wordvector.pybridge;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This is the class App.
 * It has method main which is the entry point of the program.
 * 
 * @author bjarne
 * @version 1.1
 */
public class App 
{
    /**
     * Main method: entry point of the program
     * @param args
     */
    public static void main( String[] args )
    {
        PythonBridge pythonBridge = new PythonBridge();
        
        if(pythonBridge.initServer()) {
            try {
                TimeUnit.SECONDS.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WordVector returnedVector = pythonBridge.getVector("queen");
            Optional<WordVector> optionalVector = Optional.ofNullable(returnedVector);
            if(optionalVector.isPresent()) {
                System.out.println(returnedVector.getWord() + " " + returnedVector.getVectorEntry(1));
            } else {
                System.out.println("Server did nt return a valid word vector.");
            }
            pythonBridge.shutdownServer();
        }
    }
}
