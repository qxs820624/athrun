package org.athrun.server.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.Callable;

import org.apache.catalina.connector.ClientAbortException;
import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.service.CaptureManager;
import org.athrun.server.service.DeviceManager;
import org.athrun.server.service.TaskResult;
import org.athrun.server.utils.InOutStructure;
import org.athrun.test.server.util.AthrunTestRunner;

public class MonkeyTestTask implements Callable<TaskResult> {
	private String packageName;
	
	private String activityName;

	private int testCount;//执行的monkey次数
	
	private boolean isRunning = true;
	
	private String serialNumber;
	
	private String deviceSnapshotsPath;
	
	public MonkeyTestTask(String packageName, String activityName,
			int testCount) {
		this.packageName = packageName;
		this.activityName = activityName;
		this.testCount = testCount;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setDeviceSnapshotsPath(String deviceSnapshotsPath) {
		this.deviceSnapshotsPath = deviceSnapshotsPath;
	}
	
	private class RunTestMonkeyThread extends Thread {
		private IDevice device;
				
		public RunTestMonkeyThread(IDevice device) {
			this.device = device;
		}

		public void run() {
		/**
		 * start activity
		 * @param adb shell am start -n com.taobao.taobao/com.taobao.tao.Welcome
		 * @author renxia.sd
		 */
			StringBuilder sb = new StringBuilder();
			sb.append("am start -n ").append(packageName).append("/").append(activityName);
			String runTestcaseCmd = sb.toString();
			try {
				device.executeShellCommand(runTestcaseCmd, new OutputStreamShellOutputReceiver(System.out));
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AdbCommandRejectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ShellCommandUnresponsiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// 执行monkey测试
	public void execute() {
		// 执行monkey测试
        IDevice device = DeviceManager.getDeviceBySerialNumber(serialNumber);
		if (device != null) {
			new RunTestMonkeyThread(device).start();
			AthrunTestRunner.start(device);
			new Thread(){
				public void run(){
					while(isRunning){
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						snapshot(deviceSnapshotsPath);
					}
				}
			}.start();
			try {
				device.executeShellCommand(monkeyCmd(packageName, testCount), new OutputStreamShellOutputReceiver(System.out));
				isRunning = false;
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AdbCommandRejectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ShellCommandUnresponsiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行monkey命令
	 * @param adb -s serialNumber shell monkey -p packageName -v counts
	 * @return
	 */
	public String monkeyCmd(String packageName, int count){
		StringBuilder sb = new StringBuilder();
		sb.append("monkey -p ").append(packageName).append(" -v ").append(count);
		return sb.toString();
	}
	
	public void snapshot(String deviceSnapshotsPath){
		String fileName = new Date().getTime() + ".jpg";
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File(deviceSnapshotsPath + File.separator + fileName));
			//CaptureManager.getInstance().register(serialNumber, os);
			
			CaptureCache cc = CaptureCache.get();						
			InOutStructure inOutStructure = InOutStructure
					.getInOutStructure(serialNumber);

			int length = 0;
			byte[] captureBuffer = cc.getCaptureBuffer();
			
			ByteBuffer bb = ByteBuffer.wrap(captureBuffer);
			
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
			
			// 将截屏结果写入文件		
			out.write(captureBuffer, 0, length);
			out.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}

	@Override
	public TaskResult call() throws Exception {
		execute();
		
		return null;
	}
}
