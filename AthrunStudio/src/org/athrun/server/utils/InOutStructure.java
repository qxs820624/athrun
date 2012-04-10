/**
 * 
 */
package org.athrun.server.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author taichan
 *
 */
public class InOutStructure {
	private DataInputStream in;
	private PrintWriter out;

	public InOutStructure(int port) {
		try {
			Socket server = new Socket("127.0.0.1", port);
			in = new DataInputStream(new BufferedInputStream(
					server.getInputStream(), 1024));
			out = new PrintWriter(server.getOutputStream());
		} catch (UnknownHostException e) {			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
}