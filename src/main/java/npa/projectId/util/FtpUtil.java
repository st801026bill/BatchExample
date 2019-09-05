package npa.projectId.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.SocketException;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	private FTPClient ftp;
	private LogUtil log = null;
	
	private String ftpServer;
	private String ftpPort;
	private String ftpUser;
	private String ftpPwd;
	private String ftpFolder;	
	
	public FtpUtil() {}
	
	public FtpUtil(String propertyName) {
		ResourceBundle resourceBundle = NPAConfig.getProperties(propertyName);		
		
		this.ftpServer 	= resourceBundle.getString("FTP_SERVER");
		this.ftpPort 	= resourceBundle.getString("FTP_PORT");
		this.ftpUser 	= resourceBundle.getString("FTP_USER");
		this.ftpPwd	   	= resourceBundle.getString("FTP_PWD");
		this.ftpFolder 	= resourceBundle.getString("FTP_FOLDER");
	}
	
	public void setLog(LogUtil log) {
		this.log = log;
	}
	
	public void connect() throws SocketException, IOException {
		ftp = new FTPClient();
		ftp.connect(this.ftpServer, Integer.parseInt(this.ftpPort));
		ftp.login(this.ftpUser, this.ftpPwd);
	}	
	
	public void disConnect() throws IOException {
		if(ftp != null) {
			ftp.logout();
            ftp.disconnect();
		}		
	}
	
	public Boolean uploadFiles(String outputFolder) throws Exception {		
		return uploadFiles(this.ftpFolder, outputFolder, "", "");
	}	
	public Boolean uploadFiles(String outputFolder, String startName, String endName) throws Exception {		
		return uploadFiles(this.ftpFolder, outputFolder, startName, endName);
	}
	public Boolean uploadFiles(String ftpFolder, String outputFolder, final String startName, final String endName) throws Exception {
		File[] listFiles = new File(outputFolder).listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.startsWith(startName) && fileName.endsWith(endName);
            }
        });
		//無資料不處理
        if(listFiles == null || listFiles.length == 0) {
        	log.log("***Ftp send error : no Files need to send...");        	
        	System.out.println("***Ftp send error : no Files need to send...");
        	return false;
		}
        //切換目錄
        if(!changeDirectory()) {
        	log.log("***Ftp send error : change Ftp working directory Fail...");
        	System.out.println("***Ftp send error : change Ftp working directory Fail...");
        	return false;
        }
        
        // 開啟服務器對UTF-8的支持，如果服務器支持就用UTF-8編碼，否則就使用本地編碼（big5）.
 		String LOCAL_CHARSET = FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))? "UTF-8" : "big5";        
		String SERVER_CHARSET = "ISO-8859-1";                           
		FileInputStream fis = null;
		 
		try {	
			ftp.setControlEncoding(LOCAL_CHARSET);
		    ftp.setFileType(FTP.BINARY_FILE_TYPE);
		    ftp.enterLocalPassiveMode();             
		
		    //開始傳送        
			for(File file : listFiles) {
				fis = new FileInputStream(file);        	
			    String fileName = new String(file.getName().getBytes(LOCAL_CHARSET),SERVER_CHARSET);
			    //傳檔失敗
				if(!ftp.storeFile(fileName, fis)) {
					log.log("***FTP send file:"+file.getName()+" Fail !! error Code:"+ftp.getReplyCode());
					System.out.println("***FTP send file:"+file.getName()+" Fail !! error Code:"+ftp.getReplyCode());
					continue;
				}
				log.log("send file:"+file.getName()+" success !!");
				System.out.println("send file:"+file.getName()+" success !!");	            
			}
			return true;
		} catch (Exception ex) {
		 	throw ex;
		}
	}
	
	public Boolean downloadFiles(String inputFolder) throws Exception {		
		return downloadFiles(this.ftpFolder, inputFolder, "", "");
	}	
	public Boolean downloadFiles(String inputFolder, String startName, String endName) throws Exception {		
		return downloadFiles(this.ftpFolder, inputFolder, startName, endName);
	}
	public Boolean downloadFiles(String ftpFolder, String inputFolder, final String startName, final String endName) throws Exception {
		//切換目錄
        if(!changeDirectory()) {
        	log.log("***Ftp get error : change Ftp working directory Fail...");
        	System.out.println("***Ftp get error : change Ftp working directory Fail...");
        	return false;
        }        
        FTPFile[] listFiles = ftp.listFiles("./",new FTPFileFilter(){
            public boolean accept(FTPFile ftpfile) {
                return ftpfile.getName().startsWith(startName) && ftpfile.getName().endsWith(endName);
            }
        });
        //無資料不處理
        if(listFiles == null || listFiles.length == 0) {
        	log.log("***Ftp get error : no Files need to get...");
        	System.out.println("***Ftp get error : no Files need to get...");
        	return false;
		}
        
        FileOutputStream fout = null;		
		try {			
	        ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        ftp.enterLocalPassiveMode();
	        
	        //開始接收
	        for(FTPFile file : listFiles){
                fout = new FileOutputStream(inputFolder+ File.separator +file.getName());                
                //接收失敗
                if(!ftp.retrieveFile(file.getName(), fout)){
                	log.log("***file:"+file.getName()+" get fail..!");
                	System.out.println("***file:"+file.getName()+" get fail..!");
                	continue;
                }
                log.log("file:"+file.getName()+" get done!");
                System.out.println("file:"+file.getName()+" get done!");
	        }
	        return true;
        } catch (Exception ex) {
        	throw ex;
        }
	}
	
	//切換目錄
	public boolean changeDirectory() throws IOException {		
		return ftp.changeWorkingDirectory(this.ftpFolder);
	}
}
