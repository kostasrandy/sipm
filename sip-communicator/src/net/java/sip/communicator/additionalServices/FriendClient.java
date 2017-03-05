package net.java.sip.communicator.additionalServices;

import java.util.NoSuchElementException;

public class FriendClient {

	ClientServer s = new ClientServer();
	
	public FriendClient() {
		
	}
	
	public void enableFriend(){
		
	}
	public String getFriends(String username) {
		
		String friendlist = "";
		try {
		String stmt = "SELECT relation, touser FROM friendlist where fromuser = '"+username+"'";		
		String[] reply = s.sendSelectQuery(stmt).split(";");
		if (!reply[0].equals("")){
		for (int i=0; i<reply.length; i++){
			String[] temp = reply[i].split(" ");
			friendlist = friendlist + temp[0] + ": " + temp[1] + "\n";
		}
		}
		if (friendlist.equals("")) {
			friendlist = "You have no contacts in your friendlist.";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return friendlist;
		
	}

	public void friendUser(String fromUser, String toUser, String relation) throws NoSuchElementException, RuntimeException{
		String stmt;
		// first check if there is such user
		try {
			stmt = "select username from users where username='" + toUser + "'";
			String[] reply = s.sendSelectQuery(stmt).split(" ");
			if(reply[0].equals("")) return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If they are already friends return
		try {
			stmt = "SELECT * FROM friendlist where fromuser ='"+fromUser+"' AND touser = '"+toUser+"' AND relation = '"+relation+"'";
			String[] reply = s.sendSelectQuery(stmt).split(" ");
			if(!reply[0].equals("")) return;
		
			stmt = "insert into friendlist set fromuser='"+fromUser+"', touser='"+toUser+"', relation='"+relation+"'";
			s.sendUpdateQuery(stmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unfriendUser(String fromUser, String toUser) {
		
		try {
			String stmt = "delete from friendlist where touser='"+toUser+"' and fromuser='"+fromUser+"' ";
			s.sendUpdateQuery(stmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

