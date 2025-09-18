
package project;

import HOTELL.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import static javax.swing.DropMode.ON;


public class ConnectionProvider {
    public static Connection getCon()
    {
       try
       {
    Class.forName("oracle.jdbc.OracleDriver");
           
    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","FLORY","FLORY");
      
    
    
    if (Session.currentUserEmail != null) {
                PreparedStatement ps = con.prepareStatement("BEGIN DBMS_SESSION.SET_IDENTIFIER(?); END;");
                ps.setString(1, Session.currentUserEmail);
                ps.execute();
            }
    
    
    
    
       
       return con;
       }
       catch(Exception e)
       {
       return null;
       }
        
    }}    
    
    
    

