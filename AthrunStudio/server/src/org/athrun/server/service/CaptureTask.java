package org.athrun.server.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import org.athrun.server.utils.InOutStructure;

public class CaptureTask implements Callable<TaskResult> {

	private String serialNumber;
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	@Override
	public TaskResult call() throws Exception {
		TaskResult tr = new TaskResult();
		
		long start = System.currentTimeMillis();
		
		CaptureCache cc = CaptureCache.get();
		long lastCaptureTime = cc.getLastCaptureTime();
		long snapTimeConsume = cc.getSnapTimeConsume();
		
		if ((start - lastCaptureTime) < snapTimeConsume) {
			tr.setCached(true);
			tr.setResult("screenshot complete!");
			return tr;
		}
		
		InOutStructure inOutStructure = InOutStructure
				.getInOutStructure(serialNumber);

		int length = 0;
		byte[] captureBuffer = cc.getCaptureBuffer();
		
		ByteBuffer bb = ByteBuffer.wrap(captureBuffer);
		
		try {
			inOutStructure.GetOut().println("snap");
			inOutStructure.GetOut().flush();
			
			byte[] buffer = new byte[1024];
			
			while(true) {
				int read = inOutStructure.getIn().read(buffer);
				length += read;
				bb.put(buffer, 0, read);
				
				if (read != 1024) {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		lastCaptureTime = System.currentTimeMillis();
		snapTimeConsume = lastCaptureTime - start;
		
		cc.setLastCaptureTime(lastCaptureTime);
		cc.setSnapTimeConsume(snapTimeConsume);
		
		// 将截屏结果缓存起来
		TaskResult.cacheCaptureBuffer(serialNumber, captureBuffer, length);
		
		tr.setCached(true);
		tr.setResult("2) screenshot complete!");
		return tr;
	}

}
