package com.wordvector.pybridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the class PythonBridge. It provides main methods to interact with the
 * python server and you can get and save vectors by using methods from this
 * class.
 *
 * @author bjarne
 * @version 1.1
 */
public class PythonBridge implements WordVectorProvider {
    private String serverDomain;
    private String pythonFileName;
    private int loadingPercentage;

    private WordVectorDatabase db;
    private Process processServerRunning;
    private int defaultLoadingPercentage = 10;

    final static Logger logger = LogManager.getLogger(PythonBridge.class);

    /**
     * Constructor of class PythonBridge. It initializes an empty database for
     * requested word vectors and calls method configureServerDomain which loads
     * proposed server domain from configuration file.
     */
    public PythonBridge() {
        db = new WordVectorDatabase();
        configureServerDomain();
    }

    /**
     * Updating server domain by reading from configuration file. For this,
     * configuration file must include attribute 'serverURLName'.
     */
    public void configureServerDomain() {
        serverDomain = ConfigurationSettings.getProperty("serverURLName");
    }

    /**
     * Updating python file name by reading from configuration file. For this,
     * configuration file must include attribute 'pythonFileName'.
     */
    public void configurePythonFileName() {
        pythonFileName = ConfigurationSettings.getProperty("pythonFileName");
    }

    /**
     * Updating loading percentage by reading from configuration file. Loading
     * percentage defines how many vectors should be loaded from the model. Smaller
     * loading percentages can speed up the loading process. For this, configuration
     * file must include attribute 'serverURLName'. Loading percentage must be in
     * [1;100]. Otherwise default percentage is used.
     */
    public void configureLoadingPercentage() {
        try {
            if ((Integer.parseInt(ConfigurationSettings.getProperty("loadingPercentage")) > 0)
                    && (Integer.parseInt(ConfigurationSettings.getProperty("loadingPercentage")) <= 100)) {
                loadingPercentage = Integer.parseInt(ConfigurationSettings.getProperty("loadingPercentage"));
            } else {
                loadingPercentage = defaultLoadingPercentage;
                logger.warn("Loading percentage must be in [1;100]. Now using default loading percentage: "
                        + defaultLoadingPercentage + "%.");
            }
        } catch (NumberFormatException e) {
            loadingPercentage = defaultLoadingPercentage;
            logger.warn("Could not parse an integer from configuration file. Using default loading percentage: "
                    + defaultLoadingPercentage + "%.");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.wordvector.pybridge.WordVectorProvider#initServer()
     */
    @Override
    public boolean initServer() {
        configurePythonFileName();
        configureLoadingPercentage();
        return executeInitializingServer("python " + pythonFileName + " " + loadingPercentage);
    }

    /**
     * Executing the dosCommand and running it on the given file path. Waiting for
     * response until the server was successfully started.
     *
     * @param filePath   where the python file is saved
     * @param dosCommand to call python from console
     * @return whether the server is running
     * @throws IOException          if response cannot be executed
     * @throws InterruptedException if process has been interrupted
     */
    public boolean executeInitializingServer(String dosCommand) {
        try {
            if (isServerUp()) {
                logger.info("Server is already running.");
                return true;
            }
            ArrayList<String> dosCommands = new ArrayList<>();
            dosCommands.add(dosCommand);
            Path path = Paths.get(Paths.get(System.getProperty("user.dir")).toString(), "src", "main", "resources");
            System.out.println(path);
            File dir = path.toFile();
            System.out.println(dir);

            for (String command : dosCommands) {
                System.out.println(command);
                System.out.println(processServerRunning = Runtime.getRuntime().exec(command, new String[0], dir));
                System.out.println("Processing...");

                BufferedReader responseReader = new BufferedReader(
                        new InputStreamReader(processServerRunning.getInputStream()));
                String response = responseReader.readLine();
                while (response != null) {
                    System.out.println(response);

                    if (response.contains("Debug mode")) { // Windows response after starting the server
                        System.out.println("Server successfully started. Restart with stat possible.");
                        return true;
                    }
                    response = responseReader.readLine();

                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Process execution caused an IOException. Server could not be started.");
            return false;
        }

    }

    /**
     * Proofing whether the server is already running by sending a test request
     * (getVector/test).
     *
     * @return whether server is already up
     * @throws IOException from method getVector
     */
    public boolean isServerUp() {
        Optional<WordVector> optionalVector = getVector("test");
        if (optionalVector.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public Process getProcessServerRunning() {
        return processServerRunning;
    }

    /**
     * Destroying the java process which started the server. For shutting down the
     * server, use shutdownServer. Python server PID is not equal due to
     * subprocesses.
     */
    public void destroyProcessServerRunning() {
        if (processServerRunning != null) {
            logger.debug(processServerRunning);
            processServerRunning.destroy();
            logger.debug("Exiting");
        }
    }

    /**
     * Shutting down the python server by using an endpoint. Route: /shutdown
     *
     * @throws IOException during interaction with HTTP requests
     */
    @Override
    public boolean shutdownServer() {
        try {
            URL url = new URL(serverDomain + "shutdown");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                logger.info("Server shutting down...");
                return true;
            }
            return false;
        } catch (IOException e) {
            logger.error("Connection refused. No graceful shutdown possible.");
            return false;
        }

    }

    /*
     * Implementation of getVector. Either the vector is present in the database or
     * it can be requested from the server with HTTP request and the input stream
     * converted to WordVector object.
     *
     * @see com.wordvector.pybridge.WordVectorProvider#getVector(java.lang.String)
     */
    @Override
    public Optional<WordVector> getVector(String word) {
        if (db.getVectorToWord(word).isPresent()) {
            return db.getVectorToWord(word); // Runtime improvement by using local database instead of requesting same
                                             // vector repeatedly
        }

        Optional<WordVector> optionalVector = Optional.empty();
        try {
            URL url = new URL(serverDomain + "getVector/" + word);
            StringBuffer content = getContentFromHTTPConnection(url, "GET");
            WordVector returnedVector = convertContentToWordVector(word, content);
            db.addVector(returnedVector);
            optionalVector = Optional.of(returnedVector);
            return optionalVector;
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * The content as an instance of StringBuffer is converted to a word vector by
     * generating a new WordVector. The name of the word vector is set to parameter
     * name and the values are generated by splitting the content into parts which
     * represent the values.
     *
     * @param word    of natural language and tag of returned vector
     * @param content must include float values in square brackets and divided by
     *                comma
     * @return a new word vector object
     */
    private WordVector convertContentToWordVector(String word, StringBuffer content) {
        String contentSubstringValues = content.substring(content.indexOf("[") + 1, content.indexOf("]"));
        String[] parts = contentSubstringValues.split(",");

        WordVector wordVector = new WordVector(word);

        List<Double> vectorData = new ArrayList<>();
        for (String part : parts) {
            vectorData.add(Double.parseDouble(part));
        }
        wordVector.setVector(vectorData);

        return wordVector;
    }

    /**
     * The method opens a HTTP connection with the given URL and HTTP request
     * method. If the connection returns a valid response code (200), the input
     * stream will be read and all input lines will be appended to content object.
     *
     * @param url           of word vector server
     * @param requestMethod is an HTTP request method, usually GET
     * @return content from input stream
     * @throws IOException if connection has been refused
     */
    private StringBuffer getContentFromHTTPConnection(URL url, String requestMethod) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        int status = connection.getResponseCode();
        if (status == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content;
        } else {
            throw new IOException();
        }
    }
}
