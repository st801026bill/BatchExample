package npa.projectId.batch;

import npa.projectId.base.BaseBatch;
import npa.projectId.util.FtpUtil;
import npa.projectId.util.NPAConfig;

/**
 * 1. 修正排程名稱(MyBatch), 專案代號(projectId..(tm,op))
 * 2. 設定是否要做DB連線
 * 3. 執行executeBatch()
 * 4. override processBatch()
 */
public class MyBatch extends BaseBatch{
	private static MyBatch process;
	private static final String BATCH_NAME = " MyBatch";
	private static final boolean NEED_DB_CONNECTION = false;	//是否連DB
	
	FtpUtil ftpSend = new FtpUtil("mybatch");
	FtpUtil ftpReceive = new FtpUtil("mybatch");
	
    public static void main(String[] args) {
    	process = new MyBatch();
    	process.executeBatch(BATCH_NAME);
    }
    
    //Customer Batch
	@Override
	protected void processBatch() {
		// TODO Auto-generated method stub
		System.out.println("start processBatch()...");
		
		try {
			//Your code
			doFtpSend();
			//doFtpReceive();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.log(e.toString());
		}
	}
	
	private void doFtpSend() throws Exception {
		log.log("Start send Files 2 Ftp....");
		
		ftpSend.setLog(log);
		ftpSend.connect();
//		ftpSend.uploadFiles(NPAConfig.getString("OUTPUT_FOLDER"));
		ftpSend.uploadFiles(NPAConfig.getString("OUTPUT_FOLDER"), "D01", "jpg");		
		ftpSend.disConnect();
		
        log.log("End send Files 2 Ftp....");
	}
	
	private void doFtpReceive() throws Exception {
		log.log("Start receive Files From Ftp....");
		
		ftpReceive.setLog(log);
		ftpReceive.connect();
		ftpReceive.downloadFiles(NPAConfig.getString("INPUT_FOLDER"));		
		//ftpReceive.downloadFiles(NPAConfig.getString("INPUT_FOLDER"), "NPA_", "json");
		ftpReceive.disConnect();
        
        log.log("End receive Files From Ftp....");
	}
	
	//決定是否要做DB連線
	@Override	
    protected boolean customerNeedDBConnection() {
    	return NEED_DB_CONNECTION;
    }
}
