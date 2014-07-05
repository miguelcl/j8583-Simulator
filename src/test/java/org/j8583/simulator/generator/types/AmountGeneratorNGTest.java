package org.j8583.simulator.generator.types;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Mauri
 */
public class AmountGeneratorNGTest {
    
    public AmountGeneratorNGTest() {
    }

    /**
     * Test of getNextRandomValue method, of class AmountGenerator.
     */
    @Test
    public void testAmountGenerator() {       
        long randomTrace = 0L;
        AmountGenerator instance = new AmountGenerator(0.001d,0.002d);      
        Double result = instance.getNextRandomValue(randomTrace);
        assertTrue(result >= 0.001d);
        assertTrue(result <= 0.002d);
    }
}