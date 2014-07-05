/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.j8583.simulator.parser;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mauri
 */
public class ConfigParserWrapper extends ConfigParser {

    private static Logger logger = LoggerFactory.getLogger(ConfigParserWrapper.class);

    /**
     * Creates a message factory from from a file.
     */
    public static <T extends IsoMessage> MessageFactory<T> createFromFile(String path) throws IOException {
        InputStream ins = new FileInputStream(path);
        MessageFactory<T> mfact = new MessageFactory<T>();
        logger.info("ISO8583 Parsing config from file " + path);
        try {
            parse(mfact, ins);
        } finally {
            ins.close();
        }
        return mfact;
    }
}
