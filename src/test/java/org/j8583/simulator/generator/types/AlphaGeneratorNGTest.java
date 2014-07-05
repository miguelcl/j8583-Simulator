package org.j8583.simulator.generator.types;

import com.solab.iso8583.IsoType;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Mauri
 */
public class AlphaGeneratorNGTest {
    
    public AlphaGeneratorNGTest() {
    }

    /**
     * Test of getNextRandomValue method, of class AlphaGenerator.
     */
    @Test
    public void testAlphaGenerator() {        
        long randomTrace = 0L;
        AlphaGenerator instance = new AlphaGenerator(10, true, 1, IsoType.ALPHA);
        String result = instance.getNextRandomValue(randomTrace);
        assertTrue(result.length()==10);
        assertTrue(result.toLowerCase().endsWith(result));
        assertFalse(result.contains("0"));
        assertFalse(result.contains("1"));
        assertFalse(result.contains("2"));
        assertFalse(result.contains("3"));
        assertFalse(result.contains("4"));
        assertFalse(result.contains("5"));
        assertFalse(result.contains("6"));
        assertFalse(result.contains("7"));
        assertFalse(result.contains("8"));
        assertFalse(result.contains("9"));   
        
        instance = new AlphaGenerator(150, false, 2, IsoType.ALPHA);
        result = instance.getNextRandomValue(randomTrace);
        assertTrue(result.length()==150);
        assertTrue(result.toUpperCase().endsWith(result));
    }
}