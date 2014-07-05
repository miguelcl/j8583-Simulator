package org.j8583.simulator;

import org.j8583.simulator.test.utils.EchoServer;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Mauri
 */
public class CallsExecutorNGTest {
    
    public CallsExecutorNGTest() {
    }

    /**
     * Test of getExecutorService method, of class CallsExecutor.
     */
    @Test
    public void testGetExecutorService() throws InterruptedException {
        System.out.println("testSendMessages");
        
        //start the server
        int port = 50120;        
        new EchoServer(port).run();
        CallsExecutor ce = new CallsExecutor(false,5,0,"testFile/messageTest.xml",0x200,0x200);
        ce.execute(10,"127.0.0.1",port,10000);
        
        while(ce.getTotalMessage()<1000){
            Thread.sleep(100);
        }
               
        assertEquals(ce.getTotalErrorMessage(), 0);
    }
  
}