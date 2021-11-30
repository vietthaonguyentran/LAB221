/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author chiss
 */
public class DBContext {
    
    private final String serverName = "localhost";
    private final String dbName = "WORKER_DB";
    private final String portNumber = "1433";
    private final String userID = "sa";
    private final String password = "1234";
    
    public Connection getConnection() throws Exception{
        String url = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, userID, password);
    }
    
}
