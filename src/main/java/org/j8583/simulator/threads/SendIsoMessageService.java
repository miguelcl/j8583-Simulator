/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.j8583.simulator.threads;

import com.solab.iso8583.IsoMessage;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.j8583.simulator.CallsExecutor;
import org.j8583.simulator.connection.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mauri
 */
public class SendIsoMessageService implements Runnable {

    private int id;
    private CallsExecutor callsExecutor;
    private long messageCount;
    private long messageErrorCount;
    private Logger logger;
    private Sender sender;

    public SendIsoMessageService(int id, String host, int port, int timeout, CallsExecutor callsExecutor) {
        try {
            this.id = id;
            this.callsExecutor = callsExecutor;
            this.messageCount = 0;
            this.messageErrorCount = 0;
            this.logger = LoggerFactory.getLogger(this.getClassName());
            this.sender = new Sender(host, port, timeout, callsExecutor.getMfd());
            this.sender.openConnection();
            logger.info("Thread number {} created.", id);
        } catch (IOException ex) {
            this.logger.error("Error when creating the connection", ExceptionUtils.getRootCause(ex));
        }
    }

    @Override
    public void run() {
        try {
            if (callsExecutor.isVerbose()) {
                logger.info("Starting thread");
            }
            while (true) {
                if (callsExecutor.isVerbose()) {
                    logger.info("Sending a message");
                }
                sendMessage();
                Thread.sleep(callsExecutor.getDelayBetweenMessage());
            }           
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getRootCauseMessage(ex));
        }finally{
            try {
                sender.closeConnection();
            } catch (IOException ex) {
                 logger.error(ExceptionUtils.getRootCauseMessage(ex));
            }
        }
    }

    public long getTotalMessage() {
        return messageCount;
    }

    public long getMessageErrorCount() {
        return messageErrorCount;
    }

    public void setMessageErrorCount(long messageErrorCount) {
        this.messageErrorCount = messageErrorCount;
    }

    private String getClassName() {
        return SendIsoMessageService.class.getSimpleName() + "[" + this.id + "]";
    }

    private void sendMessage() {
        try {
            messageCount++;
            IsoMessage m = this.callsExecutor.getMessateToSend();
            sender.sendIsoMessage(m);
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getRootCauseMessage(ex));
            messageErrorCount++;
        }
    }
}
