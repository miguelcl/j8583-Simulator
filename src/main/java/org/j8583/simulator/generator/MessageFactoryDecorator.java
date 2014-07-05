package org.j8583.simulator.generator;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import static com.solab.iso8583.IsoType.ALPHA;
import static com.solab.iso8583.IsoType.DATE10;
import static com.solab.iso8583.IsoType.DATE_EXP;
import com.solab.iso8583.MessageFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.j8583.simulator.generator.types.AlphaGenerator;
import org.j8583.simulator.generator.types.AmountGenerator;
import org.j8583.simulator.generator.types.DateGenerator;
import org.j8583.simulator.generator.types.FromFileGenerator;
import org.j8583.simulator.generator.types.LongGenerator;
import org.j8583.simulator.parser.ConfigParserWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *
 * @author Mauri
 */
public class MessageFactoryDecorator <T extends IsoMessage> {
    
    private static Logger logger = LoggerFactory.getLogger(MessageFactoryDecorator.class);
   
    private MessageFactory<T> mf;
    private Map<Integer, MessageFieldGen> randomFields;
    private long randomID = 0;
    private long MAX_RANDOM_ID = Long.MAX_VALUE;
    
    private final Calendar MIN_CALENDAR;
    private final Calendar MAX_CALENDAR;   
         
    public MessageFactoryDecorator(String templatePath) throws IOException {
        this.mf = ConfigParserWrapper.createFromFile(templatePath);
        
        MIN_CALENDAR = new GregorianCalendar();
        MAX_CALENDAR = new GregorianCalendar();
        MAX_CALENDAR.add(Calendar.YEAR, 1);
        
        //parse random file definition
        this.createFromFile(templatePath);
    }
   
    public T getNewRandomMessage(int type) throws Exception {
      T message = this.mf.getMessageTemplate(type);
      
      if(randomFields.containsKey(type)){
          MessageFieldGen ldg = randomFields.get(type);
          for(Integer key : ldg.getGenList().keySet()){
              DataGenerator dg = ldg.getGenList().get(key);
              message.setValue(key, dg.getNextRandomValue(randomID), dg.getType(), dg.getLength());
          }
      }
      if(randomID==MAX_RANDOM_ID){
          randomID = 1;
      }else{
          randomID = randomID + 1;
      }
      return message;
    }

    public MessageFactory<T> getMf() {
        return mf;
    }   
    
    private void createFromFile(String path) throws IOException {
        randomFields = new HashMap<Integer, MessageFieldGen>();
        Document doc = getDocParse(path);
        final Element root = doc.getDocumentElement();

        //Read the message templates
        NodeList nodes = root.getElementsByTagName("template");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            int type = parseType(elem.getAttribute("type"));
            if (type == -1) {
                throw new IOException("Invalid ISO8583 type for template: " + elem.getAttribute("type"));
            }
            NodeList fields = elem.getElementsByTagName("random-field");
            MessageFieldGen mfg = new MessageFieldGen();
            for (int j = 0; j < fields.getLength(); j++) {
                Element f = (Element) fields.item(j);
                int num = Integer.parseInt(f.getAttribute("num"));
                IsoType itype = IsoType.valueOf(f.getAttribute("type"));
                int length = (int) getSafeLongFromString(f.getAttribute("length"), 0);
                                
                DataGenerator dg;
                String filePath = f.getAttribute("filePath");
                if(filePath.length()==0){
                    boolean onlyAlpha = getSafeBooleanFromString(f.getAttribute("onlyAlpha"),false);
                    int caseOpt = (int) getSafeLongFromString(f.getAttribute("caseOpt"), 0); 
                    String min = f.getAttribute("min");
                    String max = f.getAttribute("max");
                    dg = getIsoTypeDataGenerator(length,min,max,onlyAlpha,caseOpt,itype);
                }else{
                    int colID = (int) getSafeLongFromString(f.getAttribute("colID"), 0);
                    String colSep = f.getAttribute("colSep");
                    dg = new FromFileGenerator(filePath,colSep,colID,length,itype);
                }
                mfg.addRandomField(num,dg);
            }
            randomFields.put(type, mfg);
        }
    }
    
    private Document getDocParse(String path){

        InputStream ins = null;
        Document doc = null;
        try {
            ins = new FileInputStream(path);            
            logger.info("j8583-Simulator Parsing random config from file " + path);
                final DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();                
                DocumentBuilder docb = docfact.newDocumentBuilder();
                docb.setEntityResolver(new EntityResolver() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        if (systemId.indexOf("j8583-simulator.dtd") >= 0) {
                            URL dtd = getClass().getResource("j8583.dtd");
                            if (dtd == null) {
                                logger.warn("Cannot find j8583-simulator.dtd in classpath. j8583-randomGen config files will not be validated.");
                            } else {
                                return new InputSource(dtd.toString());
                            }
                        }
                        return null;
                    }
                });
                doc = docb.parse(ins);            
        } catch (Exception ex) {
            logger.error("j8583-Simulator Cannot parse XML configuration", ex);              
        } finally {
            if(ins!=null){
                try {
                    ins.close();
                } catch (IOException ex) {
                   logger.error("j8583-Simulator Parsing XML configuration", ex);
                }
            }            
        }
        return doc;
    }
    
    
    /** Parses a message type expressed as a hex string and returns the integer number.
	 * For example, "0200" or "200" return the number 512 (0x200) */
	private static int parseType(String type) throws IOException {
		if (type.length() % 2 == 1) {
			type = "0" + type;
		}
		if (type.length() != 4) {
			return -1;
		}
		return ((type.charAt(0) - 48) << 12) | ((type.charAt(1) - 48) << 8)
			| ((type.charAt(2) - 48) << 4) | (type.charAt(3) - 48);
	}

    private DataGenerator getIsoTypeDataGenerator(int length, String min, String max, boolean onlyAlpha,int caseOpt, IsoType itype) {
        
        DataGenerator dg;
        switch(itype){
            case NUMERIC:
                dg = new LongGenerator(getSafeLongFromString(min,Long.MIN_VALUE),getSafeLongFromString(max,Long.MAX_VALUE),length,itype);
                break;
            case ALPHA:
                dg = new AlphaGenerator(length,onlyAlpha,caseOpt,itype);
                break;
            case AMOUNT:
                dg = new AmountGenerator(getSafeDoubleFromString(min,Double.MIN_VALUE),getSafeDoubleFromString(max,Double.MAX_VALUE));
                break;
            case DATE10:
                dg = new DateGenerator(getSafeCalendarFromString(min,MIN_CALENDAR),getSafeCalendarFromString(max,MAX_CALENDAR),itype);
                break;
            case DATE4:
                dg = new DateGenerator(getSafeCalendarFromString(min,MIN_CALENDAR),getSafeCalendarFromString(max,MAX_CALENDAR),itype);
                break;
            case DATE_EXP:
                dg = new DateGenerator(getSafeCalendarFromString(min,MIN_CALENDAR),getSafeCalendarFromString(max,MAX_CALENDAR),itype);
                break;
            case TIME:
                dg = new DateGenerator(getSafeCalendarFromString(min,MIN_CALENDAR),getSafeCalendarFromString(max,MAX_CALENDAR),itype);
                break;
            default:
                dg = new AlphaGenerator(length,onlyAlpha,caseOpt,itype);
                break;
        }
        return dg;
   }
    
    
   private long getSafeLongFromString(String value , long defaultValue){
       long ret = defaultValue;
       
       if("".equals(value)){
           return defaultValue;
       }
       
       try{
           ret = Long.parseLong(value);
       }catch(Exception ex){
           logger.error(ExceptionUtils.getRootCauseMessage(ex));           
           logger.error("Error parsing {} to Long, default value {} will be taken",value,defaultValue);           
       }
       return ret;      
   }
   
   private Double getSafeDoubleFromString(String value , Double defaultValue){
       Double ret = defaultValue;
       
       if("".equals(value)){
           return defaultValue;
       }
       
       try{
           ret = Double.parseDouble(value);
       }catch(Exception ex){
           logger.error(ExceptionUtils.getRootCauseMessage(ex));
           logger.error("Error parsing {} to Double, default value {} will be taken",value,defaultValue);
       }
       return ret;
   }
   
   private Calendar getSafeCalendarFromString(String value , Calendar defaultValue){
       Calendar ret = defaultValue;
       
       if("".equals(value)){
           return defaultValue;
       }
       
       try{
           SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");           
           ret.setTime(df.parse(value));
       }catch(Exception ex){
           logger.error(ExceptionUtils.getRootCauseMessage(ex));
           logger.error("Error parsing {} to Calendar, default value {} will be taken",value,defaultValue.getTime());
       }
       return ret;   
   }

    private boolean getSafeBooleanFromString(String value, boolean defaultValue) {
       boolean ret = defaultValue;
        
       if("".equals(value)){
           return defaultValue;
       }
        
       try{
           ret = Boolean.parseBoolean(value);
       }catch(Exception ex){
           logger.error(ExceptionUtils.getRootCauseMessage(ex));
           logger.error("Error parsing {} to Boolean, default value {} will be taken",value,defaultValue);
       }
       return ret;
    }
    
    
    
}
