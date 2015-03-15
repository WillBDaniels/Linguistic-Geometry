/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.structures.Pair;
import edu.wdaniels.lg.structures.Triple;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wdaniels
 */
public class GrammarGzTest {
    
    public GrammarGzTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of produceZone method, of class GrammarGz.
     */
    @Test
    public void testProduceZone() {
        System.out.println("produceZone");
        GrammarGz instance = null;
        List<Pair<List<Triple<Integer, Integer, Integer>>, Integer>> expResult = null;
        List<Pair<List<Triple<Integer, Integer, Integer>>, Integer>> result = instance.produceZone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
