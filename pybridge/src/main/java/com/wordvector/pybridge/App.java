package com.wordvector.pybridge;

import java.io.IOException;
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
        
        try {
            if(pythonBridge.initServer()) {
                TimeUnit.SECONDS.sleep(30);
                WordVector returnedVector = pythonBridge.getVector("queen");
                System.out.println(returnedVector.getWord() + " " + returnedVector.getVectorEntry(1));
                pythonBridge.shutdownServer();
            }
        } catch (java.net.ConnectException e) {
            System.out.println("Server is not reachable.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }   
    }
}
