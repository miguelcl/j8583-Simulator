package org.j8583.simulator.generator.types;

import com.solab.iso8583.IsoType;
import java.io.IOException;
import java.util.List;
import org.j8583.simulator.generator.DataGenerator;

/**
 *
 * @author Mauri
 */
public class FromFileGenerator extends DataGenerator{
    
    private int colID;
    private String path;
        
    public FromFileGenerator(String path,String separator,int colID,int legth,IsoType type) throws IOException{
        super(legth, type);
        SingletonProcessFile.getInstance().parse(path,separator);
        this.path = path;
        this.colID = colID;
    }   
 
    @Override
    public String getNextRandomValue(long randomTrace){
       List dataList = SingletonProcessFile.getInstance().getLstValues(this.path);
       int rowID = (int) (randomTrace  % dataList.size());
       String[] data = (String[]) dataList.get(rowID);
       if(colID > data.length-1){
            throw new IndexOutOfBoundsException("Max columns in file:" + data.length + " the index is:" + colID);
       }
       return data[colID];
    }                   
}
