/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.j8583.simulator.utils;

import com.solab.iso8583.IsoMessage;

/**
 *
 * @author Mauri
 */
public class Utils {
    
    public static <T extends IsoMessage> String printFormatedMessage(T m) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("TYPE: %04x\n", m.getType()));
        for (int i = 2; i < 128; i++) {
            if (m.hasField(i)) {
                sb.append(String.format("F %3d(%s): %s -> '%s'\n", i, m.getField(i)
                        .getType(), m.getObjectValue(i), m.getField(i)));
            }
        }
        return sb.toString();
    }
    
    
}
