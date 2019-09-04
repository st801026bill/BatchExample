package npa.projectId.util;

import java.util.ResourceBundle;

public class NPAConfig {
	private static final String DEFAULT_PROPERTY_NAME = "setting";	//預設 - 設定檔檔名
	private static ResourceBundle rb = ResourceBundle.getBundle(DEFAULT_PROPERTY_NAME);		
	
	public static ResourceBundle getProperties(String propertyName) {		
		return ResourceBundle.getBundle(propertyName);
	}
	
	public static String getString(String key) {
		String value = "";
		try {
		    value = rb.getString(key).trim();		    
		} catch (Exception e) {		    
		}
		return value;
    }

    public static int getInt(String key) throws NumberFormatException {
    	return Integer.parseInt(getString(key));
    }

    public static int getShort(String key) throws NumberFormatException {
    	return Short.parseShort(getString(key));
    }

    public static boolean getBoolean(String key) {
    	return Boolean.parseBoolean(getString(key));
    }
}
