package AMSuperette;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import Exception.ConnexionException;

public class Connexion 
{
	Connection conn = null;
	
	
    public void connect() throws SQLException, ConnexionException
    {
    	//tentative de connexion 
		Properties props = new Properties();
		
		props.setProperty("user", "######");
		props.setProperty("password", "#####");
		this.conn = DriverManager.getConnection("jdbc:postgresql:/######", props);
		
		System.out.println("Connexion réussi ! ");
   
    }
    
    public void disconnect() throws SQLException, ConnexionException 
    {
    	this.conn.close();
		System.out.println("Connexion fermée."); 
    	
    }
}
