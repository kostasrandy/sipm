package gov.nist.sip.proxy.additionalServices;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ProxySQL implements Runnable {

	protected Socket clientSocket = null;
	protected DBConnection c = null;
	
	public ProxySQL(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
		this.c = new DBConnection();
	}
	
	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			
			String input = in.readUTF();
			String queryKind = input.split(" ")[0].toLowerCase();
			
			String output = "";
			if(queryKind.equals("select"))
			{
				try {
					PreparedStatement q = c.conn.prepareStatement(input);
					ResultSet results = q.executeQuery();
					java.sql.ResultSetMetaData meta = results.getMetaData();
					int columnCount = meta.getColumnCount();
					while(results.next())
					{
						for (int i = 1; i <= columnCount; i++)
					        output += results.getString(meta.getColumnLabel(i)) + " ";
						output += ";";
					}
					out.writeUTF(output.trim());
				} 
				catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
			else if(queryKind.equals("delete") || queryKind.equals("insert") || queryKind.equals("update") )
			{
				try {
					PreparedStatement q = c.conn.prepareStatement(input);
					q.executeUpdate();
				} 
				catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}

			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
