package org.j8583.simulator.generator.types;

import static com.google.common.base.Preconditions.checkArgument;
import com.solab.iso8583.IsoType;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.j8583.simulator.generator.DataGenerator;

/**
 *
 * @author Mauri
 */
public class DateGenerator extends DataGenerator {

    private Calendar dmin;
    private Calendar dmax;
    
    public DateGenerator(Calendar min , Calendar max ,IsoType type) {
        super(0,type);
        checkArgument(min.before(max) , "Min value (%s) must be less to max (%s) value",min.getTime(),max.getTime());
        this.dmin = min;
        this.dmax = max;       
        this.type = type;
    }

    @Override
    public Date getNextRandomValue(long randomTrace) {
        
        int year = getRandomInt(1900, 2040);
        int month = getRandomInt(1,12);
        int day = getRandomInt(1,28);
        int hh =  getRandomInt(0,24);
        int mm =  getRandomInt(0,60);
        int ss =  getRandomInt(0,60);
        
        GregorianCalendar gc = new GregorianCalendar(year,month,day,hh,mm,ss);
        long minM = dmin.getTimeInMillis();
        long maxM = dmax.getTimeInMillis();
        long genMInRange = getRandomLong(minM,maxM);
       
        GregorianCalendar res = new GregorianCalendar();
        res.setTimeInMillis(genMInRange);
        return res.getTime();
    }
    private int getRandomInt(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
    
    private long getRandomLong( long min , long max){
        return  min + (long)(Math.random() * ((max - min) + 1));
    }
}
