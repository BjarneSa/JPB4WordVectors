package com.wordvector.pybridge;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * This is the class PythonBridge. 
 * It provides main methods to interact with the python server 
 * and you can get and save vectors by using methods from this class.
 * 
 * @author bjarne
 * @version 1.2
 */
public class PythonBridge implements WordVectorProvider 
{
    private static String serverDomain;
    private static String pythonFilePath;
    private static String pythonFileName;
    private static int loadingPercentage;
    
    private WordVectorDatabase db; 
    private Process processServerRunning;
    
    /**
     * Constructor of class PythonBridge.
     * It initializes an empty database for requested word vectors 
     * and calls method updateServerDomain which loads proposed server domain from configuration file.
     */
    public PythonBridge() {
        db = new WordVectorDatabase();
        PythonBridge.updateServerDomain();
    }
    
    /**
     * Updating server domain by reading from configuration file.
     * For this, configuration file must include attribute 'serverURLName'.
     */
    public static void updateServerDomain() {
        serverDomain = ConfigurationSettings.getProperty("serverURLName");
    }
    
    /**
     * Updating python file path by reading from configuration file.
     * For this, configuration file must include attribute 'pythonFilePath'.
     */
    public static void updatePythonFilePath() {
        pythonFilePath = ConfigurationSettings.getProperty("pythonFilePath");
    }
    
    /**
     * Updating python file name by reading from configuration file.
     * For this, configuration file must include attribute 'pythonFileName'.
     */
    public static void updatePythonFileName() {
        pythonFileName = ConfigurationSettings.getProperty("pythonFileName");
    }
    
    /**
     * Updating loading percentage by reading from configuration file.
     * Loading percentage defines how many vectors should be loaded from the model. 
     * Smaller loading percentages can speed up the loading process.
     * For this, configuration file must include attribute 'serverURLName'.
     * Loading percentage must be in [1;100]. Otherwise default percentage of 10% is used.
     */
    public static void updateLoadingPercentage() {
        try { 
            if(Integer.parseInt(ConfigurationSettings.getProperty("loadingPercentage")) > 0 && Integer.parseInt(ConfigurationSettings.getProperty("loadingPercentage")) <= 100) {
                loadingPercentage = Integer.parseInt(ConfigurationSettings.getProperty("loadingPercentage"));
            } else {
                loadingPercentage = 10;
                System.out.println("Loading percentage must be in [1;100]. Now using default loading percentage: 10%.");
            }
        } catch (NumberFormatException e) {
            loadingPercentage = 10;
            System.out.println("Could not parse an integer from configuration file. Using default loading percentage: 10%.");
        }
    }
    
    /* (non-Javadoc)
     * @see com.wordvector.pybridge.WordVectorProvider#initServer()
     */
    public boolean initServer() {
           PythonBridge.updatePythonFilePath();
           PythonBridge.updatePythonFileName();
           PythonBridge.updateLoadingPercentage();
           return this.executeInitializingServer(pythonFilePath, "python " + pythonFileName + " " + loadingPercentage);
    }
    
    /**
     * Executing the dosCommand and running it on the given file path.
     * Waiting for response until the server was successfully started.
     * @param filePath where the python file is saved
     * @param dosCommand to call python from console
     * @return whether the server is running
     * @throws IOException if response cannot be executed
     * @throws InterruptedException if process has been interrupted
     */
    public boolean executeInitializingServer(String filePath, String dosCommand) {
        try {
            if(this.isServerUp()) {
                System.out.println("Server is already running.");
                return true;
            }
            else {
                ArrayList<String> dosCommands = new ArrayList<String>();
                dosCommands.add(dosCommand);
                File dir = new File(System.getProperty("user.dir"));
                
                for (int i=0; i< dosCommands.size();i++){
                    System.out.println(
                            processServerRunning = Runtime.getRuntime().exec(dosCommands.get(i), new String[0], dir));
                    System.out.println("Processing...");
                    
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(processServerRunning.getInputStream()));
                    String response = responseReader.readLine();
                    while(response != null) { 
                        System.out.println(response);
                        
                        if(response.contains("Debug mode")) { //Windows response after starting the server
                            System.out.println("Server successfully started. Restart with stat possible.");
                            return true;
                        }
                        response = responseReader.readLine(); 
                    }
                }
            }
            return false;
        }
        catch(IOException e) {
            e.printStackTrace();    
            System.out.println("Process execution caused an IOException. Server could not be started.");
                return false;
        }
        
    }
    
    /**
     * Proofing whether the server is already running by sending a test request (getVector/test).
     * @return whether server is already up
     * @throws IOException from method getVector
     */
    public boolean isServerUp() {
        WordVector vector = this.getVector("test");
        Optional<WordVector> optionalVector = Optional.ofNullable(vector);
        if(optionalVector.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public Process getProcessServerRunning() {
        return processServerRunning;
    }

    /**
     * Destroying the java process which started the server.
     * For shutting down the server, use shutdownServer.
     * Python server PID is not equal due to subprocesses.
     */
    public void destroyProcessServerRunning() {
        if(processServerRunning != null) {
            System.out.println(processServerRunning);
            processServerRunning.destroy();
            System.out.println("Exiting");
        }
    }
    
    /**
     * Shutting down the python server by using an endpoint.
     * Route: /shutdown
     * @throws IOException during interaction with HTTP requests
     */
    public boolean shutdownServer() {
        try{
            URL url = new URL(serverDomain + "shutdown");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            if(connection.getResponseCode() == 200) {
                System.out.println("Server shutting down...");
                return true;
            } 
            return false;
        } catch(IOException e) {
            System.out.println("Connection refused. No graceful shutdown possible.");
            return false;
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.wordvector.pybridge.WordVectorProvider#getVector(java.lang.String)
     */
    public WordVector getVector(String word) {
        if(db.getVectorToWord(word) != null) {
            return db.getVectorToWord(word); //Runtime improvement by using local database instead of requesting same vector repeatedly
        } else {
            try {
                URL url = new URL(serverDomain + "getVector/" + word);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                int status = connection.getResponseCode();
                if(status == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                          content.append(inputLine);
                      }
                    in.close();
                    
                    //Converting returned content (json) to array of float values
                    String contentSubstringValues = content.substring(content.indexOf("[") +1, content.indexOf("]"));
                    String[] parts = contentSubstringValues.split(",");
                    
                    WordVector returnedVector = new WordVector(word);
                    ArrayList<Double> vectorData = new ArrayList<Double>();
                    for(int i=0; i<parts.length;i++) {
                        vectorData.add(Double.parseDouble(parts[i]));
                    }
                    returnedVector.setVector(vectorData);
                    db.addVector(returnedVector);
                    
                    return returnedVector;
                } else {
                    return null;
                }
            }
            catch(IOException e) {
                return null;
            }
            }      
    }
}
