package npa.projectId.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	private FTPClient ftp = new FTPClient();
	
	private String ftpServer;
	private String ftpPort;
	private String ftpUser;
	private String ftpPwd;
	private String ftpFolder;
	
	public FtpUtil() {
		this(null);
	}
	
	public FtpUtil(String propertyName) {		
		ResourceBundle resourceBundle = NPAConfig.getProperties(propertyName);		
		
		this.ftpServer 	= resourceBundle.getString("FTP_SERVER");
		this.ftpPort 	= resourceBundle.getString("FTP_PORT");
		this.ftpUser 	= resourceBundle.getString("FTP_USER");
		this.ftpPwd	   	= resourceBundle.getString("FTP_PWD");
		this.ftpFolder 	= resourceBundle.getString("FTP_FOLDER");				
	}
	
	//切換目錄
	public boolean changeDirectory() throws IOException {			
		return ftp.changeWorkingDirectory(this.ftpFolder);        
	}
	
	//傳送資料
	public Boolean sendFiles(File[] listFiles) throws Exception {
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
	            	System.out.println("FTP send file:"+file.getName()+" Fail !! error Code:"+ftp.getReplyCode());
	            	continue;
	            }	            
	            System.out.println("send file:"+file.getName()+" success !!");	            
	        }
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	if(fis != null) fis.close();
        	ftp.logout();
            ftp.disconnect();
        }
        return true;
	}
	
	//取得資料
	public void receiveFile(FTPFile[] listFiles, String inputFolder) throws Exception {
		FileOutputStream fout = null;
		
		try {			
	        ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        ftp.enterLocalPassiveMode();
	        
	        //開始接收
	        for(FTPFile file : listFiles){
                fout = new FileOutputStream(inputFolder+ File.separator +file.getName());                
                //接收失敗
                if(!ftp.retrieveFile(file.getName(), fout)){
                	System.out.println("file:"+file.getName()+" get fail..!");
                	return;
                }
                System.out.println("file:"+file.getName()+" get done!");
	        }
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	if(fout != null) fout.close();
        	ftp.logout();
            ftp.disconnect();
        }		
	}
	
	//取得Ftp Server檔案
	public FTPFile[] getFTPFiles(final String startName, final String endName) throws IOException {
		FTPFile[] files = ftp.listFiles("./",new FTPFileFilter(){
            public boolean accept(FTPFile ftpfile) {
                return ftpfile.getName().startsWith(startName) && ftpfile.getName().endsWith(endName);
            }
        });
		return files;
	}
	
	//建立ftp連線
	public void connectFtp () throws NumberFormatException, SocketException, IOException {
		ftp.connect(this.ftpServer, Integer.parseInt(this.ftpPort));
		ftp.login(this.ftpUser, this.ftpPwd);
	}
}
