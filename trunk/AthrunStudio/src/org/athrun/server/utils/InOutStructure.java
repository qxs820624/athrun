/**
 * 
 */
package org.athrun.server.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author taichan
 *
 */
public class InOutStructure {
	private DataInputStream in;
	private PrintWriter out;

	public InOutStructure(int port) throws IOException {
		try {
			Socket server = new Socket("127.0.0.1", port);
			in = new DataInputStream(new BufferedInputStream(
					server.getInputStream(), 1024));
			out = new PrintWriter(server.getOutputStream());
		} catch (UnknownHostException e) {			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public DataInputStream getIn() {
		return in;
	}

	public PrintWriter GetOut() {
		return out;
	}
	
	static Map<String, InOutStructure> inoutMap = new HashMap<String, InOutStructure>();
		
	public static InOutStructure GetCaptureInOutBySerialNumber(String serialNumber) throws ReservedPortExhaust, IOException{
		synchronized (inoutMap) {
			if(!inoutMap.containsKey(serialNumber)){
				int port = ForwardPortManager.getCapturePort(serialNumber);
				InOutStructure inOutStructure = new InOutStructure(port);
				inoutMap.put(serialNumber, inOutStructure);
			}
			return inoutMap.get(serialNumber);			
		}
	}

	/**
	 * @param serialNumber
	 */
	public static void reconnectCaptureInOut(String serialNumber) {
		synchronized (inoutMap) {
			inoutMap.remove(serialNumber);
		}		
	}
}