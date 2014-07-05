package org.j8583.simulator.generator.types;

import com.solab.iso8583.IsoType;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Mauri
 */
public class LongGeneratorNGTest {
    
    public LongGeneratorNGTest() {
    }

    /**
     * Test of getNextRandomValue method, of class LongGenerator.
     */
    @Test
    public void testLongGenerator() {        
        LongGenerator instance = new LongGenerator(1l,3l,10, IsoType.NUMERIC);      
        long result = instance.getNextRandomValue(1);
        assertTrue(result >= 1);
        assertTrue(result <= 3);
    }
}