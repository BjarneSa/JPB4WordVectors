package com.wordvector.pybridge;

import java.util.Scanner;

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
            System.out.println("Server is ready for your requests. Type 'shutdown!' to shutdown the server.");
            boolean shutdownRequested = false;
            Scanner in = new Scanner(System.in);
            while(!shutdownRequested) {
                String s = in.nextLine();
                if(s.equals("shutdown!")) {
                    shutdownRequested = true;
                    break;
                }
                System.out.println("You requested word vector for: " + s);
                WordVector returnedVector = pythonBridge.getVector(s);
                if(returnedVector != null) {
                    System.out.println(pythonBridge.getVector(s).getVector());
                } else {
                System.out.println("The given input is not included in the model.");
                }
            }
            
            in.close();
            pythonBridge.shutdownServer();
        } else {
            System.out.println("Server could not be started.");
        }
    }
}
