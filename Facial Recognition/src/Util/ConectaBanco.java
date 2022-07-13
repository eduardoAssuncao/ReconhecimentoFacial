package Util;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConectaBanco {
    
    public Statement stm;
    public ResultSet rs;
 
    private String driver;
    private String root;
    private String user;
    private String pass;
    
    public Connection conn;
    
    public void conecta(){
        try {
            System.setProperty("jdbc.Driver", driver);
            conn = DriverManager.getConnection(root, user, pass);
            System.out.println("OK");
        } catch (Exception e) {
            System.out.print("Error: " + e);
        }
    }
    
    public void desconectar(){
        try {
            conn.close();
        } catch (Exception e) {
            System.out.print("Error: " + e);
        }
    }
    
    public void executeSQL(String SQL){
        try {
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(SQL);
        } catch (Exception e) {
            System.out.print("Error: " + e);
        }
    }
}
