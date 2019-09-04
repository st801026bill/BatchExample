package npa.projectId.batch;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.SocketException;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTPFile;

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
//			ResourceBundle rb = NPAConfig.getProperties("mybatch");
//			System.out.println(rb.getString("FTP_SERVER"));
//			System.out.println(NPAConfig.getString("OUTPUT_FORDER"));
			doFtpSend();
			doFtpReceive();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.log(e.toString());
		}
	}
	
	private void doFtpSend() throws Exception {
		log.log("Start send Files 2 Ftp....");
		
		ftpSend.connectFtp();
		
		File[] listFiles = new File( NPAConfig.getString("OUTPUT_FOLDER") ).listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                //return fileName.endsWith("json");
            	return true;
            }
        });		
		//無資料不處理
        if(listFiles == null || listFiles.length == 0) {
        	log.log("***Ftp send error : no Files need to send...");        	
        	System.out.println("***Ftp send error : no Files need to send...");
        	return;
		}
        //切換目錄
        if(!ftpSend.changeDirectory()) {
        	log.log("***Ftp send error : change Ftp working directory Fail...");
        	System.out.println("***Ftp send error : change Ftp working directory Fail...");
        	return;
        }
        ftpSend.sendFiles(listFiles);
        
        log.log("End send Files 2 Ftp....");
	}
	
	private void doFtpReceive() throws Exception {
		log.log("Start receive Files From Ftp....");
		
		ftpReceive.connectFtp();
		//切換目錄
        if(!ftpReceive.changeDirectory()) {
        	log.log("***Ftp get error : change Ftp working directory Fail...");
        	System.out.println("***Ftp get error : change Ftp working directory Fail...");
        	return;
        }        
        FTPFile[] listFiles = ftpReceive.getFTPFiles("", "");
        //無資料不處理
        if(listFiles == null || listFiles.length == 0) {
        	log.log("***Ftp get error : no Files need to get...");
        	System.out.println("***Ftp get error : no Files need to get...");
        	return;
		}        
        ftpReceive.receiveFile(listFiles, NPAConfig.getString("INPUT_FOLDER"));
        
        log.log("End receive Files From Ftp....");
	}
	
	//決定是否要做DB連線
	@Override	
    protected boolean customerNeedDBConnection() {
    	return NEED_DB_CONNECTION;
    }
}
