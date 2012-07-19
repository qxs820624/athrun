package org.athrun.server.service;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.Callable;

import org.athrun.server.utils.InOutStructure;
import org.athrun.server.utils.ReservedPortExhaust;

public class ExecuteTask implements Callable<TaskResult> {

	private String serialNumber;
	
	private String command;
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public TaskResult call() throws Exception {
		TaskResult tr = new TaskResult();
		
		InOutStructure inOutStructure;
		try {
			inOutStructure = InOutStructure
					.getInOutStructure(serialNumber);

			inOutStructure.GetOut().println(command);
			inOutStructure.GetOut().flush();
		} catch (ReservedPortExhaust e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println(serialNumber + "已经断开，不用处理");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		tr.setResult("execute command complete!");
		return tr;
	}

}
