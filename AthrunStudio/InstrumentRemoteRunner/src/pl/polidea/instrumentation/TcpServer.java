package pl.polidea.instrumentation;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpServer  {
	
	private int port;
	private ServerHandler serverHandler;
	private Thread handlerThread;
	private TestCmdReceiver cmdReceiver;
	
	public void setCmdReceiver(TestCmdReceiver cmdReceiver) {
		this.cmdReceiver = cmdReceiver;
	}

	public TcpServer(String strPort) {
		this.port = Integer.parseInt(strPort);
	}
	
	public void start() {
		 System.out.println ("TCPServer Waiting for client on port: " + port);
		 ServerSocket serverSocket = null;
		 try {
				serverSocket = new ServerSocket (port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 serverHandler = new ServerHandler(serverSocket);  
		 if (cmdReceiver != null) {
			 serverHandler.setCmdReceiver(cmdReceiver);
		 }
		 
		 handlerThread = new Thread(serverHandler);
		 handlerThread.start();
	}
	
	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	public void stop() {
		serverHandler.stop();
	}
	
}
