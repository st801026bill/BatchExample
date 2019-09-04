/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package npa.projectId.util;

import com.sybase.jdbc4.jdbc.SybDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;



/**
 * Database Utility class with a convenient method to get Session Factory object.
 *
 * @author Ben
 */
public class DatabaseUtil {
    private static Connection conn = null;

    public static Connection getConnection() throws Exception {
        if(conn == null){
            ResourceBundle resource = ResourceBundle.getBundle("database");
            SybDriver sybDriver = (SybDriver)
            Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance();
            sybDriver.setVersion(com.sybase.jdbcx.SybDriver.VERSION_7);
            DriverManager.registerDriver(sybDriver);
            conn = DriverManager.getConnection(resource.getString("URL"),resource.getString("USER"),resource.getString("PASSWORD"));
        }
        return conn;
    }
    
    public static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }   
}
