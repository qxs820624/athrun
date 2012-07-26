package org.athrun.server.service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class TaskResult {
	
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
	
	public static MobileCaptureBuffer cloneCaptureBuffer(String serialNumber) {
		MobileCaptureBuffer clone = null;
		synchronized(TaskResult.class) {
			MobileCaptureBuffer captureBuffer = captureBufferMap.get(serialNumber);
			if (captureBuffer == null) {
				return clone;
			}
			
			clone = new MobileCaptureBuffer();
			clone.clone(captureBuffer);
			
			return clone;
		}
	}
}
