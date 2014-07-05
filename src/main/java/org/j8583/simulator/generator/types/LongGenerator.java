package org.j8583.simulator.generator.types;

import static com.google.common.base.Preconditions.*;
import com.solab.iso8583.IsoType;
import org.j8583.simulator.generator.DataGenerator;

/**
 *
 * @author Mauri
 */
public class LongGenerator extends DataGenerator{
    
    private long min;
    private long max;
        
    public LongGenerator(long min, long max,int legth,IsoType type){
        super(legth, type);
        checkArgument(min <= max , "Min value (%s) must be less or equal to max (%s) value",min,max);
        this.min =  min;
        this.max = max;
    }   
    
    @Override
    public Long getNextRandomValue(long randomTrace){
        return getRandomLong(min,max);
    }
            
            
    private long getRandomLong(long min, long max){
        return  min + (long)(Math.random() * ((max - min) + 1));
    }
    
      
}
