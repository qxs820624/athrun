package org.athrun.server.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.Callable;

import org.athrun.server.utils.InOutStructure;
import org.athrun.server.utils.ReservedPortExhaust;

public class InfoTask implements Callable<TaskResult> {

	private String serialNumber;
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Override
	public TaskResult call() throws Exception {
		TaskResult tr = new TaskResult();
		
		InOutStructure inOutStructure;
		try {
			inOutStructure = InOutStructure
					.getInOutStructure(serialNumber);

			inOutStructure.GetOut().println("info");
			inOutStructure.GetOut().flush();

			byte[] b = new byte[50];
			DataInputStream in = inOutStructure.getIn();

			int len = in.read(b);
			String result = new String(b, 0, len).trim();
			tr.setResult(result);
			
		} catch (ReservedPortExhaust e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return tr;
	}

}
