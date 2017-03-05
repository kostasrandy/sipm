package net.java.sip.communicator.additionalServices;

import java.util.HashSet;
import java.util.NoSuchElementException;

public class ForwardClient {
	
	ClientServer s = new ClientServer();
	
	public ForwardClient() {}
	
	public String getForward(String username) {
		String stmt = "SELECT forwardTo FROM forwarding where forwardFrom = '"+username+"'";
		
		String[] reply = s.sendSelectQuery(stmt).split(" ");
		if(reply.length != 0)return reply[0];
		else return "";		
	}	
	/**
	 * @param fromUser
	 * @param toUser
	 * @throws NoSuchElementException in case no such user found
	 * @throws RuntimeException in case of a circle forwarding
	 */
	public void setForward(String fromUser, String toUser) throws NoSuchElementException, RuntimeException{
		try {
			//Check if user exists.
			ClientServer s = new ClientServer();
			String stmt;
			stmt = "select username from users where username='" + toUser + "'";
			String[] reply = s.sendSelectQuery(stmt).split(" ");
			if(reply[0].equals("")) throw new NoSuchElementException();
		
			//Check if there is a circle.
			HashSet<String> userSet = new HashSet<String>();
			userSet.add(fromUser);
			userSet.add(toUser);
			String tempFrom = toUser;
			String forwardTo;
			while (true) {
				
					stmt = "SELECT forwardTo FROM forwarding where forwardFrom = '" + tempFrom + "' ";
					reply = s.sendSelectQuery(stmt).split(" ");
					if (!reply[0].equals("")) {
						forwardTo = reply[0];
						if (!userSet.add(forwardTo)) throw new RuntimeException();
						tempFrom = forwardTo;
					} 
					else
						break;	
			}
			
			/*
			 * ready to set the new forward first delete the old if exists and then set the new
			 */
			stmt = "delete from forwarding where forwardFrom= '"+ fromUser + "' ";
			s.sendUpdateQuery(stmt);
			stmt = "insert into forwarding set forwardFrom= '" +fromUser +"' , forwardTo= '"+ toUser +"' ";
			s.sendUpdateQuery(stmt);
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void resetForward(String fromUser){
		String stmt = "delete from forwarding where forwardFrom='"+fromUser+"'";
		s.sendUpdateQuery(stmt);
	}
}
