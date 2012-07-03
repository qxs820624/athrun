package pl.polidea.instrumentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler implements Runnable {

	private ServerSocket serverSocket;
	private boolean bRunning = true;
	private TestCmdReceiver cmdReceiver;
	private PrintWriter outToClient;

	public void setCmdReceiver(TestCmdReceiver cmdReceiver) {
		this.cmdReceiver = cmdReceiver;
	}

	public ServerHandler(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	// 譬如发送截屏指令run::snapshot-req，退出指令：run::over-req
	public void sendClientCmd(String cmd) {
		if (outToClient != null) {
			outToClient.println(cmd);
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket connected = serverSocket.accept();
				System.out.println(" THE CLIENT" + " "
						+ connected.getInetAddress() + ":"
						+ connected.getPort() + " IS CONNECTED ");

				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connected.getInputStream(), "gb2312"));
				
				//BufferedReader inFromClient = new BufferedReader(
				//		new InputStreamReader(connected.getInputStream()));
				
				//InputStream is = connected.getInputStream();
				

				outToClient = new PrintWriter(
						connected.getOutputStream(), true);

				while (true) {

					System.out.println("SEND(Type Q or q to Quit):");

					// 如果运行指示为false，服务器退出
					if (!bRunning) {
						outToClient.println("run::over-req");
						connected.close();
						break;
					}

					/*
					int len = 0;
					while(true) {
						byte[] buffer = new byte[50];
						len = is.read(buffer);
						if (len > 0) {
							for(int i=0; i<len; i++) {
								System.out.println("buffer[" + i + "]: " + Integer.toHexString(buffer[i]));
							}
							break;
						}
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					*/
					
					String fromclient = inFromClient.readLine();
					System.out.println("received: " + fromclient);
										
					if (fromclient != null) {
						// 收到客户端的退出指示
						if (fromclient.equals("quit")) {
							// 应答ok
							outToClient.println("OK");
							outToClient.flush();
							
							//connected.close();
							
							// 指示接收完毕
							if (cmdReceiver != null) {
								cmdReceiver.receiveOver();
							}
							//break;
						} else if (fromclient.equals("run::over-ack")) {
							connected.close();
							break;
						} else if (fromclient.startsWith("run::")) {
							if (fromclient.equals("run::snapshot-ack")) {
								if (cmdReceiver != null) {
									cmdReceiver.snapShotOver();
								}
							}
						} else {
							System.out.println("RECIEVED:" + fromclient);
							if (cmdReceiver != null) {
								cmdReceiver.receiveCmd(fromclient);
								// 应答ok
								outToClient.println("OK");
								outToClient.flush();
							}
						}
					} else {
						System.out.println("else connected.close()");
						connected.close();
						break;
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public void stop() {
		bRunning = false;
	}
}