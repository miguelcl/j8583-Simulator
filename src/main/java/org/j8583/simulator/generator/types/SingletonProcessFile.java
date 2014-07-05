package org.j8583.simulator.generator.types;

import com.google.common.base.Charsets;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mauri
 */
class SingletonProcessFile {

    private static Logger logger = LoggerFactory.getLogger(SingletonProcessFile.class);
   
    private static String DEFAULT_SEPARATOR = ";";
    private static SingletonProcessFile instance = null;    
    private Map<String,List<String[]>> fileProcessed;
    
    private SingletonProcessFile(){
        fileProcessed = new HashMap<String,List<String[]>>();
    }
    
    static synchronized SingletonProcessFile getInstance() {
        if(instance==null){
            instance = new SingletonProcessFile();
        }
        return instance;
    }

    public synchronized void parse(String path, String separator) throws IOException {
        checkNotNull(path,"File path can not be null");
        if(separator ==null || separator.equals("")){
            separator = DEFAULT_SEPARATOR;
        }
        if(!fileProcessed.containsKey(path)){
            logger.info("j8583-Simulator Parsing data from file:" + path);
            List<String[]> dataArray = new ArrayList<String[]>();
            File f = new File(path);
            List<String> lines = Files.readLines(f, Charsets.UTF_8);
            for(String ln:lines){
                String[] data = ln.split(separator);
                dataArray.add(data);
            }
            fileProcessed.put(path, dataArray);
        }
    }
    
    public synchronized List<String[]> getLstValues(String path){
        if(fileProcessed.containsKey(path)){
            return fileProcessed.get(path);
        }else{
            throw new NullPointerException("File not found");
        }
    }
    
}
