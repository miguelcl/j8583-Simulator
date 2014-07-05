package org.j8583.simulator.generator.types;

import com.solab.iso8583.IsoType;
import java.io.IOException;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Mauri
 */
public class FromFileGeneratorNGTest {
    
    public FromFileGeneratorNGTest() {
    }

    /**
     * Test of getNextRandomValue method, of class FromFileGenerator.
     */
    @Test
    public void testFromFileGenerator() throws IOException {        
        long randomTrace = 0L;
        FromFileGenerator instanceC0 = new FromFileGenerator("testFile/dataTest.txt", ";", 0, 10, IsoType.ALPHA);
        FromFileGenerator instanceC1 = new FromFileGenerator("testFile/dataTest.txt", ";", 1, 5, IsoType.NUMERIC);
        
        String resultC0 = instanceC0.getNextRandomValue(randomTrace);
        String resultC1 = instanceC1.getNextRandomValue(randomTrace);        
        randomTrace++;
        assertEquals(resultC0, "testC0-1");
        assertEquals(resultC1, "1");
                
        resultC0 = instanceC0.getNextRandomValue(randomTrace);
        resultC1 = instanceC1.getNextRandomValue(randomTrace);
        randomTrace++;        
        assertEquals(resultC0, "testC0-2");
        assertEquals(resultC1, "2");
        
        
        resultC0 = instanceC0.getNextRandomValue(randomTrace);
        resultC1 = instanceC1.getNextRandomValue(randomTrace);
        randomTrace++;
        assertEquals(resultC0, "testC0-3");
        assertEquals(resultC1, "3");
                
        //VOLVIO AL COMIENZO
        resultC0 = instanceC0.getNextRandomValue(randomTrace);
        resultC1 = instanceC1.getNextRandomValue(randomTrace);
        randomTrace++;        
        assertEquals(resultC0, "testC0-1");
        assertEquals(resultC1, "1");
        
    }
}