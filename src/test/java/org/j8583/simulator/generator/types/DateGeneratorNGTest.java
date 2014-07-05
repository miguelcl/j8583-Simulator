package org.j8583.simulator.generator.types;

import com.solab.iso8583.IsoType;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Mauri
 */
public class DateGeneratorNGTest {
    
    public DateGeneratorNGTest() {
    }

    /**
     * Test of getNextRandomValue method, of class DateGenerator.
     */
    @Test
    public void testDateGenerator() {        
        long randomTrace = 0L;
        GregorianCalendar min  = new GregorianCalendar();
        GregorianCalendar max  = new GregorianCalendar();       
        max.add(Calendar.DAY_OF_WEEK, 1);       
        DateGenerator instance = new DateGenerator(min, max, IsoType.DATE10);
       
        Date result = instance.getNextRandomValue(randomTrace);
        GregorianCalendar res = new GregorianCalendar();
        res.setTime(result);
        
        assertTrue(res.before(max));
        assertTrue(res.after(min));       
    }
}