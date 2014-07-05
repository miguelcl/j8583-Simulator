/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.j8583.simulator.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.solab.iso8583.IsoMessage;
import org.j8583.simulator.generator.MessageFactoryDecorator;
import org.j8583.simulator.utils.Utils;

/**
 *
 * @author MauricioF1
 */
public class Sender<T extends IsoMessage> {

    private static Logger logger = LoggerFactory.getLogger(Sender.class);
    private MessageFactoryDecorator<T> mfact;
    private String server_ip_address;
    private int server_port;
    private int time_out;
    private boolean debugMessages;
    Socket socket = null;

    public Sender(String server_ip_address, int server_port, int timeOut, MessageFactoryDecorator<T> mfact) {
        this.mfact = mfact;
        this.server_ip_address = server_ip_address != null ? server_ip_address.trim() : server_ip_address;
        this.server_port = server_port;
        this.time_out = timeOut;
        this.debugMessages = false;
    }

    public void openConnection() throws IOException {
        socket = new Socket(server_ip_address, server_port);
        socket.setSoTimeout(time_out);
    }
    
    public void closeConnection() throws IOException {
        if(socket != null) {
          socket.close();
        }
    }

    public T sendIsoMessage(T msg) throws Exception {

        if (debugMessages) {
            logger.info("DEBUG MODE - REQUEST");
            this.writeMessageData(msg);
        }

        T resp = null;
        byte[] buf;
        try {
            if (socket == null || !socket.isConnected()) {
                this.openConnection();
            }

            if (socket != null && socket.isConnected()) {
                // escribo el mensaje Iso a enviar
                msg.write(socket.getOutputStream(), 2);
                // leo los dos primeros bytes de la respuesta.
                byte[] lenbuf = new byte[2];
                socket.getInputStream().read(lenbuf);
                int size = ((lenbuf[0] & 0xff) << 8) | (lenbuf[1] & 0xff);
                if (size > 0) {
                    buf = new byte[size];
                    if (socket.getInputStream().read(buf) == size) {
                        resp = mfact.getMf().parseMessage(buf, 0);
                        if (resp == null) {
                            //es null no lo parsio bien.
                            logger.info("Error parsing the response");
                            throw new Exception("Error parsing the response");
                        }
                        if (debugMessages) {
                            logger.info("DEBUG MODE - FTM RESPONSE");
                            this.writeMessageData(resp);
                        }
                    } else {
                        logger.info("The message size not match -> size:" + size);
                        throw new Exception("The message size not match -> size:" + size);
                    }
                } else {
                    logger.info("Error getting the response ->Readed large is:" + size);
                    this.writeBuffer(lenbuf);
                    throw new Exception("Error getting the response ->Readed large is:" + size);
                }
            } else {
                logger.info("Imposible to connect -> ip:" + this.server_ip_address + " port:" + this.server_port);
                throw new Exception("Imposible to connect -> ip:" + this.server_ip_address + " port:" + this.server_port);
            }
        } catch (ParseException ex) {
            logger.info("Error parsing message");
            throw ex;
        } catch (UnknownHostException e) {
            logger.info("Imposible to connect -> ip:" + this.server_ip_address + " port:" + this.server_port);
            throw e;
        } catch (IOException e) {
            logger.info("Error reading the buffer -> ip:" + this.server_ip_address + " port:" + this.server_port);
            throw e;
        }
        return resp;
    }

    private void writeBuffer(byte[] lenbuf) {
        try {
            String strData2 = new String(lenbuf, "UTF-8");
            logger.info("The buffer have:" + strData2);
        } catch (UnsupportedEncodingException e) {
            logger.info("Error writing the buffer");
        }
    }

    private void writeMessageData(T m) {
        try {
            if (m != null) {
                String strData2 = new String(m.writeData(), "UTF-8");
                logger.info("The raw message is:" + strData2);
                logger.info(Utils.printFormatedMessage(m));
            } else {
                logger.info("The parsed message is null");
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error("Error writing the message");
        }
    }
}
