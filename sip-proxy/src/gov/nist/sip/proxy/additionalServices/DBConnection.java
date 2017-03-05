package gov.nist.sip.proxy.additionalServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import gov.nist.sip.db.ParseXMLCredentials;

public class DBConnection {
	public Connection conn;
	private ParseXMLCredentials dbCred;
	
	public DBConnection()
	{		
		try {
			dbCred = new ParseXMLCredentials();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbCred.getUrl(),
					dbCred.getUsername(), dbCred.getPassword());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					e.getMessage(),
					"Database connection failed!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public Statement createStatement()
	{
		try {
			return conn.createStatement();
		} catch (SQLException e) {

		}
		return null;
	}
	
	public void Shutdown()
	{
		try {
			if(conn != null)
			{
				conn.close();
				conn = null;
			}
		}
		catch(SQLException e)
		{
			
		}
	}
}
