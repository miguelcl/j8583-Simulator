package org.j8583.simulator.generator.types;

import static com.google.common.base.Preconditions.checkArgument;
import com.solab.iso8583.IsoType;
import java.util.Random;
import org.j8583.simulator.generator.DataGenerator;

/**
 *
 * @author Mauri
 */
public class AmountGenerator extends DataGenerator{
    
    private double min;
    private double max;
    
    public AmountGenerator(double min, double max){
        super(12, IsoType.AMOUNT);
        checkArgument(min <= max , "Min value (%s) must be less or equal to max (%s) value",min,max);
        this.min = min;
        this.max = max;
    }
     
    @Override
    public Double getNextRandomValue(long randomTrace) {
        return getRandomDouble(min, max);
    }

    private Double getRandomDouble(double min, double max){
        double random = new Random().nextDouble();
        return  min + (random * (max - min));
    }
    
}
