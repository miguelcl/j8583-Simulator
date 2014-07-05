package org.j8583.simulator.generator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mauri
 */
public class MessageFieldGen {
    
    private Map<Integer, DataGenerator> genList = new HashMap<Integer,DataGenerator>();

    public MessageFieldGen() {
    }
    
    public Map<Integer, DataGenerator> getGenList() {
        return genList;
    }

    public void setGenList(Map<Integer, DataGenerator> genList) {
        this.genList = genList;
    }

    void addRandomField(int i, DataGenerator dg) {
        this.genList.put(i, dg);
    }
}
