package org.j8583.simulator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.LoggerFactory;

/**
 *
 * @author MauricioF1
 */
public class CommandOptions {

    public final static String HELP = "help";
    public final static String VERBOSE = "verbose";
    public final static String THREADS = "threads";
    public final static String D_START = "dStart";
    public final static String MESSAGE_FREC = "messageFrec";
    public final static String DEF_FILE = "defF";
    public final static String DELAY = "delay";
    public final static String SEND = "Send";
    public final static String RECIEVE = "Recieve";
    public final static String TIMEOUT = "timeout";
    public final static String HOST = "host";
    public final static String PORT = "port";   
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandOptions.class);
    private Options options;
    
    private CommandLine cmd;

    CommandOptions() {

        options = new Options();
        options.addOption("h", HELP, false, "Print help for this application.");
        options.addOption("v", VERBOSE, false, "Enable verbose mode.");
        options.addOption("t", THREADS, true, "Number of threads.");
        options.addOption("d", D_START, true, "Thread start delay frecuency.");
        options.addOption("m", MESSAGE_FREC, true, "Thread message frecuency.");
        Option def_file = OptionBuilder
                .isRequired()
                .hasArg()
                .withDescription("Path to definition file path.")
                .create(DEF_FILE);
        options.addOption(def_file);
        options.addOption(DELAY, true, "Delay between thread launches.");
        options.addOption("s", SEND, true, "Message type to send, example 100.");
        options.addOption("r", RECIEVE, true, "Message type to get on response , example 210.");


        Option hostDef = OptionBuilder
                .isRequired()
                .hasArg()
                .withDescription("Ip of the server to make the connection.")
                .create(HOST);
        options.addOption(hostDef);
        Option portDef = OptionBuilder
                .isRequired()
                .hasArg()
                .withDescription("Port in which the server attempt for connection.")
                .create(PORT);
        options.addOption(portDef);
        options.addOption("tout", TIMEOUT, true, "Connection timeout.");
    }

    public boolean parse(String[] args) throws ParseException {
        if (args.length < 1 || (args.length == 1 && (args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("-h")))) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("2", options, true);
            return false;
        } else {
            CommandLineParser parser = new PosixParser();
            this.cmd = parser.parse(options, args);
            return true;
        }        
    }

    public String getStringValue(String arg, String... defValue) {
        if (defValue.length > 0) {
            return cmd.getOptionValue(arg, defValue[0]);
        } else {
            return cmd.getOptionValue(arg, "");
        }
    }

    public long getLongValue(String arg, Long... defValue) {
        if (cmd.hasOption(arg)) {
            return Long.parseLong(cmd.getOptionValue(arg));
        } else {
            if (defValue.length > 0) {
                return defValue[0];
            } else {
                return 0;
            }
        }
    }

    public int getIntValue(String arg, int... defValue) {
        if (cmd.hasOption(arg)) {
            return Integer.parseInt(cmd.getOptionValue(arg));
        } else {
            if (defValue.length > 0) {
                return defValue[0];
            } else {
                return 0;
            }
        }
    }
    
    public int getHexIntValue(String arg, int... defValue) {
        if (cmd.hasOption(arg)) {
            return  Integer.valueOf(cmd.getOptionValue(arg),16);
        } else {
            if (defValue.length > 0) {
                return Integer.valueOf(String.valueOf(defValue[0]), 16);
            } else {
                return 0;
            }
        }
    }

    boolean hasOption(String v) {
        return cmd.hasOption(v);
    }
    
     
   
    
    
}
