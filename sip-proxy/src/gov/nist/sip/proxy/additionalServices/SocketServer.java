package gov.nist.sip.proxy.additionalServices;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer extends Thread {

	protected int port;
    protected ServerSocket serverSocket;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
    public SocketServer(int port) throws IOException
    {
        this.port = port;
    }
    
    @Override
    public void run()
    {
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        
        openServerSocket();
        
        while (!isStopped()) {            
                Socket clientSocket = null;
                try {
                    clientSocket = this.serverSocket.accept();
                    
                } catch (IOException e) {
                    if(isStopped())
                    {
                        System.out.println("Server Stopped!");
                        return;
                    }
                    throw new RuntimeException("Error client connection",e);
                }
                this.threadPool.execute(new ProxySQL(clientSocket));
                
        }
        this.threadPool.shutdown();
        System.out.println("Server stopped");
    }
    
    private synchronized boolean isStopped()
    {
        return this.isStopped;
    }
    
    public synchronized void stopp()
    {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openServerSocket()
    {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
