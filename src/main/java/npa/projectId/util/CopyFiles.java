package npa.projectId.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFiles {
	public CopyFiles(){
		
	}
	
	public int copy(String srcDir, String destDir) throws IOException {
		File srcFile, destFile;
		int copyCount = 0;
		if (srcDir == null || destDir == null) return copyCount;
		
		srcFile = new File(srcDir);
		if (srcFile.exists() == false) return copyCount;
		destFile = new File(destDir);
		if (srcFile.isFile() == true){
			File destPath = new File(destFile.getParent());
			destPath.mkdirs();
			copyCount = 1;
			copy(srcFile, destFile);
			return copyCount;
		}
		else{
			String[] files = srcFile.list();
			for (int idx = 0; idx < files.length; idx++){
				copyCount += copy(srcDir+"/"+files[idx], destDir+"/"+files[idx]);
			}
			return copyCount;
		}
	}
	
	private void copy(File srcFile, File destFile){
		if (srcFile == null || destFile == null) return;
		try {
			InputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] buffer = new byte[(int)srcFile.length()];
			
			bis.read(buffer);
			bos.write(buffer);
			bis.close();
			bos.close();
			
		} catch (FileNotFoundException e) {
			// TODO 自動產生 catch 區塊
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動產生 catch 區塊
			e.printStackTrace();
		}
		
	}
}
