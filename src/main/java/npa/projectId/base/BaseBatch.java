package npa.projectId.base;

import java.sql.Connection;
import npa.projectId.util.DatabaseUtil;
import npa.projectId.util.LogUtil;

/**
 * Bill
 *
 */
public abstract class BaseBatch {
	protected static LogUtil log = null;
	protected static Connection conn = null;
	
    public BaseBatch() {}
    
    protected final void executeBatch(String batchName) {
    	try {    		
	    	if(!generateLog(batchName)) return;	//設定Log	    	
	    	if(customerNeedDBConnection()) getDBConnection();	//取得連線資訊
	    		    	
	    	log.logStart();
	    	processBatch();	//由子排程繼承實作
	    	log.logEnd();
    	} catch (Exception e) {
			e.printStackTrace();
			log.log(e.toString());
		} finally {
			log.logClose();
			closeConnections();
		}
    }
    
    //產log
    private Boolean generateLog(String batchName) {
    	System.out.println("start generateLog()...");
    	log = new LogUtil(batchName);    	
    	return log.isOk();
    }
    
    //連DB
    private void getDBConnection() throws Exception {
    	System.out.println("start getDBConnection()...");
    	conn = DatabaseUtil.getConnection();
    }    
    
    //執行排程
    protected abstract void processBatch();
    
    //關閉連線
    private void closeConnections() {
        try{
            if(conn != null)
                DatabaseUtil.closeConnection(conn);            
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    //樣板Hook(提供使用者決定是否要做DB連線)
    protected boolean customerNeedDBConnection() {    	
    	return false;
    }    
}
