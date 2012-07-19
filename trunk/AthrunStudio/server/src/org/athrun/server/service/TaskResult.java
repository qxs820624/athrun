package org.athrun.server.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.ClientAbortException;

public class TaskResult {
	
	static class MobileCaptureBuffer {
		private final byte[] captureBuffer = new byte[500000];
		private int captureLength = 0;
		
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
	}
	
	private String result;
	
	private static Map<String, MobileCaptureBuffer> captureBufferMap = new HashMap<String, MobileCaptureBuffer>();
	
	private boolean isCached = false;

	public String getResult() {
		return result;
	}
	
	public boolean isCached() {
		return isCached;
	}
	
	public void setCached(boolean isCached) {
		this.isCached = isCached;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public static void cacheCaptureBuffer(String serialNumber, byte[] buffer, int length) {
		synchronized(TaskResult.class) {
			MobileCaptureBuffer captureBuffer = captureBufferMap.get(serialNumber);
			if (captureBuffer != null) {
				captureBuffer.cacheCaptureBuffer(buffer, length);
			} else {
				captureBuffer = new MobileCaptureBuffer();
				captureBuffer.cacheCaptureBuffer(buffer, length);
				captureBufferMap.put(serialNumber, captureBuffer);
			}
		}
	}
	
	public static void writeToOutput(String serialNumber, OutputStream out) {
		synchronized(TaskResult.class) {
			MobileCaptureBuffer captureBuffer = captureBufferMap.get(serialNumber);
			if (captureBuffer != null) {
				captureBuffer.writeToOutput(out);
			}
		}
	}
}
