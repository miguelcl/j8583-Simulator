package org.j8583.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final int THREADS_DEFAULT = 1;
    private static final long THREAD_DELAY_DEFAULT = 100;
    private static final long MESSAGE_FREC_DEFAULT = 1;
    private static final int TIMEOUT_DEFAULT = 20000;
    private static final int SENDMESSAGETYPE_DEFAULT = 100;
    private static final int RECMESSAGETYPE_DEFAULT = 110;
    private static final long INFO_MESSAGE_LOG = 3000;

    public static void main(String[] args) {
        try {

            CommandOptions cmOptions = new CommandOptions();

            if (cmOptions.parse(args)) {
                int th = cmOptions.getIntValue(CommandOptions.THREADS, THREADS_DEFAULT);
                long mfrec = cmOptions.getLongValue(CommandOptions.MESSAGE_FREC, MESSAGE_FREC_DEFAULT);
                long delayThread = cmOptions.getLongValue(CommandOptions.DELAY, THREAD_DELAY_DEFAULT);
                boolean verbose = cmOptions.hasOption(CommandOptions.VERBOSE);
                String templatePath = cmOptions.getStringValue(CommandOptions.DEF_FILE);
                int typeToSend = cmOptions.getHexIntValue(CommandOptions.SEND, SENDMESSAGETYPE_DEFAULT);
                int typeToRecieve = cmOptions.getHexIntValue(CommandOptions.RECIEVE, RECMESSAGETYPE_DEFAULT);
                int timeout = cmOptions.getIntValue(CommandOptions.TIMEOUT, TIMEOUT_DEFAULT);
                String host = cmOptions.getStringValue(CommandOptions.HOST);
                int port = cmOptions.getIntValue(CommandOptions.PORT);
                CallsExecutor ce = new CallsExecutor(verbose, th, mfrec, templatePath, typeToSend, typeToRecieve);
                ce.execute(delayThread, host, port, timeout);

                long hist = 0;
                while (true) {
                    //solo es informativo, para hacerlo preciso buscar otra solucion.
                    long total = ce.getTotalMessage();
                    long errorTotal = ce.getTotalErrorMessage();
                    logger.info("==================================================");
                    logger.info("Total messages sent:{}", total);
                    logger.info("Total messages with error:{}", errorTotal);
                    logger.info("Frecuency {}/sec", (total - hist) / (INFO_MESSAGE_LOG / 100));
                    hist = total;
                    logger.info("==================================================");
                    Thread.sleep(INFO_MESSAGE_LOG);
                }
            }

        } catch (Exception ex) {
            logger.error("Error::", ex);
        }
    }
}
