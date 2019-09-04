/*
 * ExceptionLogger.java
 *
 * Created on 2007?~5??23??, ?U?? 3:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package npa.projectId.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author YuChieh
 */
public class ExceptionLogger extends OutputStream {
    private String strException = null;
    /** Creates a new instance of ExceptionLogger */
    public ExceptionLogger() {
	strException = "";
    }
    
    public void write(int b) throws IOException{
	strException += String.valueOf(b);
    }
    
    public void write(byte[] b) throws IOException{
	strException += new String(b);
    }
    
    public void write(byte[] b, int off, int len) throws IOException{
	strException += new String(b, off, len);
    }
    
    public String getExceptionString(){
	return strException;
    }
}
