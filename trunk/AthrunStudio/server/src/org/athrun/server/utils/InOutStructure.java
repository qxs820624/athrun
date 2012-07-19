package org.athrun.server.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class InOutStructure {
	private DataInputStream in;
	private PrintWriter out;

	private void initialize(int port) throws Exception {
		try {
			Socket server = new Socket("127.0.0.1", port);
			in = new DataInputStream(new BufferedInputStream(
					server.getInputStream(), 1024));
			out = new PrintWriter(server.getOutputStream());
		} catch (Exception e) {
			throw e;
		} 
	}

	public DataInputStream getIn() {
		return in;
	}

	public PrintWriter GetOut() {
		return out;
	}
	
	private InOutStructure() {
		
	}
	
	private static ThreadLocal<InOutStructure> iosLocal = new ThreadLocal<InOutStructure>(){
		
		@Override
		public void set(InOutStructure value) {
			super.set(value);
		}
		
	};
	
	public static InOutStructure get() {
		return (InOutStructure)(iosLocal.get());
	}
	
	public static void set(InOutStructure ios) {
		iosLocal.set(ios);
	}
		
	public static InOutStructure getInOutStructure(String serialNumber) throws Exception{
		assert serialNumber != null;

		InOutStructure ios = InOutStructure.get();
		if (ios == null) {
			int port = ForwardPortManager.getCapturePort(serialNumber);
			System.out.println("port: " + port);
			InOutStructure inOutStructure = new InOutStructure();
			inOutStructure.initialize(port);
			InOutStructure.set(inOutStructure);
			return inOutStructure;
		}
		
		return ios;
	}
}