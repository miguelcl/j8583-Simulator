package org.j8583.simulator;

import org.j8583.simulator.threads.SendIsoMessageService;
import com.solab.iso8583.IsoMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.j8583.simulator.generator.MessageFactoryDecorator;
import org.j8583.simulator.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mauri
 */
public class CallsExecutor {

    private static Logger logger = LoggerFactory.getLogger(CallsExecutor.class);
    private ExecutorService executorService = null;
    private long delayBetweenMessage;
    private int threadCount;
    private boolean verbose;
    private List<SendIsoMessageService> threads;
    private MessageFactoryDecorator<IsoMessage> mfd;
    private int typeToSend;
    private int typeToRecieve;
   
    public CallsExecutor(boolean verbose, int threadCount, long delayBetweenMessage, String templatePath, int typeToSend, int typeToRecieve) {
        try {
            this.verbose = verbose;
            this.delayBetweenMessage = delayBetweenMessage;
            this.threadCount = threadCount;
            this.threads = new ArrayList<SendIsoMessageService>();
            this.executorService = Executors.newFixedThreadPool(threadCount);
            this.typeToSend = typeToSend;
            this.typeToRecieve = typeToRecieve;            
            mfd = new MessageFactoryDecorator<IsoMessage>(templatePath);
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    void execute(long delayThread, String host, int port, int timeout) throws InterruptedException {
        logger.info("Creating one connections per thread and starting the thread.");
        for (int i = 0; i < threadCount; i++) {
            SendIsoMessageService ms = new SendIsoMessageService(i, host, port, timeout, this);
            threads.add(ms);
            executorService.submit(ms);
            if (delayThread > 0) {
                Thread.sleep(delayThread);
            }
        }
        logger.info("Threads started");
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public long getDelayBetweenMessage() {
        return delayBetweenMessage;
    }  

    public IsoMessage getMessateToSend() throws Exception {
        IsoMessage m = mfd.getNewRandomMessage(typeToSend);
        if(verbose){
             logger.info("m--->" + Utils.printFormatedMessage(m));
        }
        return m;
    }

    public MessageFactoryDecorator<IsoMessage> getMfd() {
        return mfd;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public long getTotalMessage() {
        long messageCount = 0;
        for (SendIsoMessageService t : threads) {
            messageCount = messageCount + t.getTotalMessage();
        }
        return messageCount;
    }

    public long getTotalErrorMessage() {
        long messageCount = 0;
        for (SendIsoMessageService t : threads) {
            messageCount = messageCount + t.getMessageErrorCount();
        }
        return messageCount;
    }
}
