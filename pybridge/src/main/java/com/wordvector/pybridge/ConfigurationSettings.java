package com.wordvector.pybridge;

import java.io.IOException;
import java.util.*;
import java.util.Properties;

/**
 *  This is the class ConfigurationSettings.
 * It is structured as Singleton class.
 * It provides methods to read from configuration file.
 * 
 * @author bjarne
 * @version 1.0
 */
public class ConfigurationSettings {
    
    private Properties configFile;
    private static ConfigurationSettings instance;
    
    /**
     * Constructor of class ConfigurationSettings.
     * Trying to load config.properties file from same java project.
     */
    public ConfigurationSettings()
    {
          configFile = new java.util.Properties();
          
          try {
            configFile.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
          } catch(IOException e){
              e.printStackTrace();
          }
    }
  
    /**
     * Getting value for given key by searching in configuration file.
     * @param key as named in configuration file
     * @return value 
     */
    private String getValue(String key)
    {
         return this.configFile.getProperty(key);
    }
    
    /**
     * Getting property by checking existence of an instance of this class and then call getValue.
     * @param key as named in configuration file
     * @return value
     */
    public static String getProperty(String key) {
        if(instance == null) {
            instance = new ConfigurationSettings();
        }
        return instance.getValue(key);
    }
}
