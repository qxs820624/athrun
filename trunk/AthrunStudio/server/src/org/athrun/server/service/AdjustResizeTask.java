package org.athrun.server.service;

import java.io.IOException;
import java.util.concurrent.Callable;
import org.athrun.server.utils.InOutStructure;

public class AdjustResizeTask implements Callable<TaskResult> {

	private String serialNumber;
	
	private int resizeRate;
	
	private static final byte[] buffer = new byte[1024];
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void setResizeRate(int resizeRate) {
		this.resizeRate = resizeRate;
	}

	@Override
	public TaskResult call() throws Exception {
		TaskResult tr = new TaskResult();
		
		InOutStructure inOutStructure = InOutStructure
				.getInOutStructure(serialNumber);

		inOutStructure.GetOut().println(
				"r" + String.format("%03d", resizeRate)); // 取3位
		inOutStructure.GetOut().flush();

		try {
			inOutStructure.getIn().read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tr.setResult(buffer.toString());
		return tr;
	}
}
