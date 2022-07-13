package Util;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConectaBanco {
    
    public Statement stm;
    public ResultSet rs;
 
    private final String driver = "org.mysql.Driver";
    private final String root = "jdbc:mysql://127.0.0.1/facial_recognition";
    private final String user = "root";
    private final String pass = "";
    
    public Connection conn;
    
    public void conexao(){
        try {
            System.setProperty("jdbc.Driver", driver);
            conn = DriverManager.getConnection(root, user, pass);
            System.out.println("CONEXÃO REALIZADA COM SUCESSO");
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
