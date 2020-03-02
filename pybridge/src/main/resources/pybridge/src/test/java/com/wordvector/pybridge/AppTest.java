package com.wordvector.pybridge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static PythonBridge pythonBridge = new PythonBridge();
    private static double delta = 1e-14;

    public AppTest() {
    }

    /**
     * Before you can run tests on requesting a word vector, the server has to start
     * and build a connection. After a 30 second timeout (server restarts with
     * stat), the tests can be run.
     */
    @BeforeAll
    public static void init() {
        pythonBridge.initServer();
    }

    /**
     * After all tests ran, the server is shut down again. This prevents an open url
     * and port beyond the test cases.
     */
    @AfterAll
    public static void finish() {
        pythonBridge.shutdownServer();
    }

    @Test
    public void testVector() {
        assertEquals(0.08969999849796295, pythonBridge.getVector("the").get().getVectorEntry(0), delta);
    }

//    /**
//     * Test on one single vector to word. Comparison on entry with index 0.
//     */
//    @Test
//    public void testOneVectorFirstIndex() {
//        assertEquals(-0.19200000166893005, pythonBridge.getVector("hello").get().getVectorEntry(0), delta);
//    }
//
//    /**
//     * Test on one single vector to word. Comparison on entry with irregular index.
//     */
//    @Test
//    public void testOneVectorSomeIndex() {
//        assertEquals(-0.11010000109672546, pythonBridge.getVector("dog").get().getVectorEntry(191), delta);
//    }
//
//    /**
//     * Test on one single vector to word. Comparison on entry with index 299.
//     */
//    @Test
//    public void testOneVectorLastIndex() {
//        assertEquals(0.048500001430511475, pythonBridge.getVector("cat").get().getVectorEntry(299), delta);
//    }
//
//    /**
//     * Test multiple requests.
//     */
//    @Test
//    public void testGetSomeVectors() {
//        assertEquals(0.10819999873638153, pythonBridge.getVector("king").get().getVectorEntry(0), delta);
//        assertEquals(-0.0820000022649765, pythonBridge.getVector("man").get().getVectorEntry(1), delta);
//        assertEquals(-0.08169999718666077, pythonBridge.getVector("!").get().getVectorEntry(2), delta);
//        assertEquals(-0.16670000553131104, pythonBridge.getVector("boy").get().getVectorEntry(3), delta);
//    }
//
//    /**
//     * Test requests of special characters.
//     */
//    @Test
//    public void testSpecialCharacters() {
//        assertEquals(0.05000000074505806, pythonBridge.getVector("...").get().getVectorEntry(16), delta);
//        assertEquals(-0.07689999788999557, pythonBridge.getVector("*").get().getVectorEntry(184), delta);
//        assertEquals(0.010700000450015068, pythonBridge.getVector("]").get().getVectorEntry(279), delta);
//    }
//
//    /**
//     * Test requests of numbers.
//     */
//    @Test
//    public void testVectorToNumbers() {
//        assertEquals(-0.13740000128746033, pythonBridge.getVector("0").get().getVectorEntry(0), delta);
//        assertEquals(0.0010000000474974513, pythonBridge.getVector("100").get().getVectorEntry(1), delta);
//        assertEquals(-0.02930000051856041, pythonBridge.getVector("10000").get().getVectorEntry(2), delta);
//        assertEquals(0.05920000001788139, pythonBridge.getVector("7.5").get().getVectorEntry(3), delta);
//    }
//
//    /**
//     * Test same requests twice and returned vector should be same object because of
//     * link to database.
//     */
//    @Test
//    public void testGetVectorToWordTwice() {
//        Optional<WordVector> returnedVector = pythonBridge.getVector("queen");
//        assertEquals(-0.19709999859333038, returnedVector.get().getVectorEntry(20), delta);
//        assertEquals(returnedVector, pythonBridge.getVector("queen"));
//    }
//
//    /**
//     * Test vector length == 300
//     */
//    @Test
//    public void testGetVectorOnLength() {
//        assertEquals(300, pythonBridge.getVector("tree").get().getLength());
//    }
//
//    /**
//     * Test whether vector to not English word is null.
//     */
//    @Test
//    public void testGetVectorToUnknownWord() {
//        assertTrue(pythonBridge.getVector("qwert").isEmpty());
//    }
//
//    /**
//     * Test index that is out of bounds for vector of length 300.
//     */
//    @Test
//    public void testIndexOutOfBound() {
//        assertThrows(IndexOutOfBoundsException.class, () -> {
//            pythonBridge.getVector("index").get().getVectorEntry(300);
//        });
//    }
}
