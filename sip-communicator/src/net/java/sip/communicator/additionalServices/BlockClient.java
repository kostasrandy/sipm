package net.java.sip.communicator.additionalServices;

public class BlockClient {
	
	ClientServer s = new ClientServer();
	
	public BlockClient() {}
	
	public String getBlocks(String username) {
		String rep="";
		String stmt = "SELECT blocked FROM blocking where blockedFrom ='"+username+"'";
		String[] reply = s.sendSelectQuery(stmt).split(";");
		if (reply[0].equals(""))return "No users blocked\n\t ...yet";
		else{ 
			for (int i = 0; i< reply.length; i++ ){
				rep = rep+reply[i];
			}
			return rep;
		}	
	}
	/** 
	 * @param fromUser
	 * @param toUser
	 * @throws NoSuchElementException in case no such user found
	 * @throws RuntimeException in case of a circle forwarding
	 */
	public void blockUser(String fromUser, String toUser){
		String stmt;
		// first check if there is such user
		try {
			stmt = "select username from users where username='" + toUser + "'";
			String[] reply = s.sendSelectQuery(stmt).split(" ");
			if(reply[0].equals("")) return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * If the toUser is already blocked from the fromUser, nothing happens (return)
		 * else we update the blocking table
		 */
		try {
			stmt = "SELECT blocked FROM blocking where blocked ='"+toUser+"' AND blockedFrom ='"+fromUser+"'";
			String[] reply = s.sendSelectQuery(stmt).split(" ");
			if (!reply[0].equals(""))return;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try{
			stmt =	"INSERT into blocking set blockedFrom='"+fromUser+"', blocked='"+toUser+"'";
			s.sendUpdateQuery(stmt);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unblockUser(String fromUser, String toUser) {
		String stmt="";
		try{
			stmt = "DELETE from blocking where blocked='"+toUser+"' and blockedFrom='"+fromUser+"'";
			s.sendUpdateQuery(stmt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
