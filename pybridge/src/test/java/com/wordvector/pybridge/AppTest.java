package com.wordvector.pybridge;

import java.io.IOException;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    private PythonBridge pythonBridge;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
        pythonBridge = new PythonBridge();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testInitServer()
    {
        try {
            assertTrue(pythonBridge.initServer());
        } catch(IOException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void testGetSomeVectors() {
        try {
            assertEquals(0.10819999873638153, pythonBridge.getVector("king").getVectorEntry(0));
            assertEquals(-0.0820000022649765, pythonBridge.getVector("man").getVectorEntry(1));
            assertEquals(-0.08169999718666077, pythonBridge.getVector("!").getVectorEntry(2));
            assertEquals(0.052799999713897705, pythonBridge.getVector("queen").getVectorEntry(3));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void testSpecialCharacters() {
        try {
            assertEquals(0.05000000074505806, pythonBridge.getVector("...").getVectorEntry(16));
            assertEquals(-0.07689999788999557, pythonBridge.getVector("*").getVectorEntry(184));
            assertEquals(0.010700000450015068, pythonBridge.getVector("]").getVectorEntry(279));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void testVectorToNumbers() {
        try {
            assertEquals(-0.13740000128746033, pythonBridge.getVector("0").getVectorEntry(0));
            assertEquals(0.0010000000474974513, pythonBridge.getVector("100").getVectorEntry(1));
            assertEquals(-0.02930000051856041, pythonBridge.getVector("10000").getVectorEntry(2));
            assertEquals(0.05920000001788139, pythonBridge.getVector("7.5").getVectorEntry(3));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void testGetVectorToWordTwice() {
        try {
            WordVector returnedVector = pythonBridge.getVector("queen");
            assertEquals(-0.19709999859333038, returnedVector.getVectorEntry(20));
            assertEquals(returnedVector, pythonBridge.getVector("queen"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void testgetVectorOnLength() {
        try {
            assertEquals(300, pythonBridge.getVector("king").getLength());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void testGetVectorToUnknownWord() {
        //Assert.assertThrows();
    }
}
