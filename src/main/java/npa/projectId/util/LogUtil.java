
package npa.projectId.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author YuChieh
 */
public class LogUtil {
    private FileOutputStream fosLog = null;
    
    public LogUtil() {
    	fosLog = null;
    }
    
    public LogUtil(String batchNo){
//		ResourceBundle resourceBundle = ResourceBundle.getBundle("setting");
//		String logPath = resourceBundle.getString("BATCH_LOG_PATH");
		String logPath = NPAConfig.getString("BATCH_LOG_PATH");
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		File logFile = new File(logPath + "/" + batchNo + "/" + today.substring(0, 6) + "/" + batchNo + "_" + today + ".txt");
		try {
		    logFile.getParentFile().mkdirs();
		    if (logFile.exists() == false || logFile.isFile() == false){
		    	logFile.createNewFile();
		    }
		    fosLog = new FileOutputStream(logFile, true);
		} catch (IOException e) {
		    e.printStackTrace();
		    fosLog = null;
		}
    }
    
    public LogUtil(String batchNo, String unitCd){
//		ResourceBundle resourceBundle = ResourceBundle.getBundle("setting");
//		String logPath = resourceBundle.getString("BATCH_LOG_PATH");
		String logPath = NPAConfig.getString("BATCH_LOG_PATH");
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		File logFile = new File(logPath + "/" + batchNo + "/" + unitCd + "/" + today.substring(0, 6) + "/" + batchNo + "_" + today + ".txt");
		try {
		    logFile.getParentFile().mkdirs();
		    if (logFile.exists() == false || logFile.isFile() == false){
		    	logFile.createNewFile();
		    }
		    fosLog = new FileOutputStream(logFile, true);
		} catch (IOException e) {
		    e.printStackTrace();
		    fosLog = null;
		}
    }
    
    public boolean isOk(){
    	return fosLog != null;		
    }
    
    public void logStart(){
	log("--------------------------------------------------------------------------------");
	log(String.format("Start At：%1$tY/%1$tm/%1$td %1$tT", new Object[]{Calendar.getInstance()}));
    }
    
    public void logEnd(){
	log(String.format("Stop At：%1$tY/%1$tm/%1$td %1$tT", new Object[]{Calendar.getInstance()}));
	log("--------------------------------------------------------------------------------");
    }
    
    public void log(String logMsg){
		try {
		    logMsg = logMsg + "\r\n";
		    fosLog.write(logMsg.getBytes());
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
    }
    
    public void logStatusEnd(String processNm){
    	log("Process Status："+processNm+" done!");
    }
    
    public void logClose(){
        try{
            if(fosLog != null)
                fosLog.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
