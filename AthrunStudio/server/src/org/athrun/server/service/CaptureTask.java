package org.athrun.server.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import org.athrun.server.utils.InOutStructure;

public class CaptureTask implements Callable<TaskResult> {

	private String serialNumber;
	
	// 请求截屏时间
	private long requestTime;
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	// 实际测试发现：手机的每次截屏时间消耗很不均匀，对于配置较差的手机，最快的截屏消耗只要30ms，最慢时却要660ms
	@Override
	public TaskResult call() throws Exception {
		TaskResult tr = new TaskResult();
		
		CaptureCache cc = CaptureCache.get();
		long lastCaptureTime = cc.getLastCaptureTime();
		
		// 如果请求时间小于最后截屏时间，则直接返回缓存的截屏图片数据
		if (requestTime < lastCaptureTime) {
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
	
			cc.setLastCaptureTime(System.currentTimeMillis());
			
			// 将截屏结果缓存起来
			TaskResult.cacheCaptureBuffer(serialNumber, captureBuffer, length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tr.setCached(true);
		tr.setResult("2) screenshot complete!");
		return tr;
	}

}
