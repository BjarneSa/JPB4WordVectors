package com.wordvector.pybridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static PythonBridge pythonBridge = new PythonBridge();
    private static double delta = 1e-14;

    public AppTest()
    {
        }

    @BeforeAll
    public static void init() {
        pythonBridge.initServer();
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @AfterAll
    public static void finish() {
        pythonBridge.shutdownServer();
    }
    
    @Test
    public void testOneVectorFirstIndex() {
        assertEquals(-0.19200000166893005, pythonBridge.getVector("hello").getVectorEntry(0), delta);
    }
    
    @Test
    public void testOneVectorSomeIndex() {
        assertEquals(-0.11010000109672546, pythonBridge.getVector("dog").getVectorEntry(191), delta);
    }
    
    @Test
    public void testOneVectorLastIndex() {
        assertEquals(0.048500001430511475, pythonBridge.getVector("cat").getVectorEntry(299), delta);
    }
    
    @Test
    public void testGetSomeVectors() {
        assertEquals(0.10819999873638153, pythonBridge.getVector("king").getVectorEntry(0), delta);
        assertEquals(-0.0820000022649765, pythonBridge.getVector("man").getVectorEntry(1), delta);
        assertEquals(-0.08169999718666077, pythonBridge.getVector("!").getVectorEntry(2), delta);
        assertEquals(-0.16670000553131104, pythonBridge.getVector("boy").getVectorEntry(3), delta);
    }
    
    @Test
    public void testSpecialCharacters() {
        assertEquals(0.05000000074505806, pythonBridge.getVector("...").getVectorEntry(16), delta);
        assertEquals(-0.07689999788999557, pythonBridge.getVector("*").getVectorEntry(184), delta);
        assertEquals(0.010700000450015068, pythonBridge.getVector("]").getVectorEntry(279), delta);
    }
    
    @Test
    public void testVectorToNumbers() {
        assertEquals(-0.13740000128746033, pythonBridge.getVector("0").getVectorEntry(0), delta);
        assertEquals(0.0010000000474974513, pythonBridge.getVector("100").getVectorEntry(1), delta);
        assertEquals(-0.02930000051856041, pythonBridge.getVector("10000").getVectorEntry(2), delta);
        assertEquals(0.05920000001788139, pythonBridge.getVector("7.5").getVectorEntry(3), delta);
    }
    
    @Test
    public void testGetVectorToWordTwice() {
        WordVector returnedVector = pythonBridge.getVector("queen");
        assertEquals(-0.19709999859333038, returnedVector.getVectorEntry(20), delta);
        assertEquals(returnedVector, pythonBridge.getVector("queen"));
    }
    
    @Test
    public void testgetVectorOnLength() {
        assertEquals(300, pythonBridge.getVector("tree").getLength());
    }
    
    @Test
    public void testGetVectorToUnknownWord() {
        assertNull(pythonBridge.getVector("qwert"));
    }
}
