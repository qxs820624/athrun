package org.athrun.server.service;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.catalina.connector.ClientAbortException;

public class MobileCaptureBuffer {
	private final byte[] captureBuffer = new byte[500000];
	private int captureLength = 0;
	
	public byte[] getCaptureBuffer() {
		return captureBuffer;
	}

	public int getCaptureLength() {
		return captureLength;
	}

	public void cacheCaptureBuffer(byte[] buffer, int length) {
		System.arraycopy(buffer, 0, captureBuffer, 0, length);
		captureLength = length;
	}
	
	public void writeToOutput(OutputStream out) {
		try {
			out.write(captureBuffer, 0, captureLength);
			out.flush();
			out.close();
		} catch(ClientAbortException cae) {
			cae.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void clone(MobileCaptureBuffer captureBuffer) {
		System.arraycopy(captureBuffer.captureBuffer, 0, this.captureBuffer, 0, captureBuffer.captureLength);
		this.captureLength = captureBuffer.captureLength;
	}
}