package net.java.sip.communicator.additionalServices;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class RegisterClient {
	ClientServer s = new ClientServer();
	
	public RegisterClient() {
		
	}
	public void registerToDB(String username, String passwd, String email,
			String creditCard, int plan) {
		try {
			String stmt = "INSERT INTO users set username = '"+username+"', email = '"+email+"', password = '"+passwd+"', creditCard = '"+creditCard+"', plan = '"+plan+"'";
			s.sendUpdateQuery(stmt);

		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
}
