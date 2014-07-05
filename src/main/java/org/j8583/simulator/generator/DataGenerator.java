package org.j8583.simulator.generator;

import static com.google.common.base.Preconditions.*;
import com.solab.iso8583.IsoType;

/**
 *
 * @author Mauri
 */
public abstract class DataGenerator {
    
    protected IsoType type;
    protected int length;
    
    public DataGenerator(int length,IsoType type){
       this.type = checkNotNull(type);       
       checkArgument(length >= 0 ,"length must be greater than zero (%s)",length);
       this.length = length;
    }
        
    public Object getNextRandomValue(long randomTrace){
        return new Object();
    }
    public IsoType getType(){
        return type;
    }
    public int getLength(){
        return  length;
    }
}
