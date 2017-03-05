package net.java.sip.communicator.additionalServices;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.java.sip.communicator.common.Utils;

public class ClientServer {
	
	private int port = 6000;
	private String proxyIP = (Utils.getProperty("net.java.sip.communicator.sip.REGISTRAR_ADDRESS").split(":"))[0] ; 
	private InetAddress serverIp;
	
	public ClientServer()
	{
		try {
			this.serverIp = InetAddress.getByName(proxyIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public ClientServer(int port)
	{
		try {
			this.serverIp = InetAddress.getByName(proxyIP);
			this.port = port;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public ClientServer(String ip,int port)
	{
		try {
			this.serverIp = InetAddress.getByName(ip);
			this.port = port;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
	}
	
	public void sendUpdateQuery(String query) throws RuntimeException
	{
		try {
			Socket clientSocket = new Socket(serverIp, port);
			
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			out.writeUTF(query);
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not send the update.");
		}
	}
	
	public String sendSelectQuery(String query) throws RuntimeException
	{
		try {
			Socket clientSocket = new Socket(serverIp, port);
			
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			out.writeUTF(query);
			
			//Query gets executed on server...
			
			DataInputStream reply = new DataInputStream(clientSocket.getInputStream());
			String repl = reply.readUTF();
			clientSocket.close();
			return repl;
		} catch (IOException e) {
			
		}
		throw new RuntimeException("Could not send the query");
	}
}
