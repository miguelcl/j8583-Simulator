package org.j8583.simulator.generator;

import com.solab.iso8583.IsoMessage;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Mauri
 */
public class MessageFactoryDecoratorNGTest {
    
    public MessageFactoryDecoratorNGTest() {
    }

    /**
     * Test of getNewRandomMessage method, of class MessageFactoryDecorator.
     */
    @Test
    public void testMessageFactoryDecorator() throws Exception {               
        MessageFactoryDecorator instance = new MessageFactoryDecorator<IsoMessage>("testFile/messageTest.xml");
        
       
        //check only few fields
        for (int i=1; i < 100 ; i++) {
            IsoMessage result = instance.getNewRandomMessage(0x100);
            long val2 = (Long) result.getField(2).getValue();
            assertTrue(val2 >= 1 && val2 <= 100);

            String val3 = (String) result.getField(3).getValue();
            assertTrue(val3.length() == 4 && val3.equals(val3.toLowerCase()));

            Double val4 = (Double) result.getField(4).getValue();
            assertTrue(val4 >= 1.11d && val4 <= 2.02d);
        }
    }
}