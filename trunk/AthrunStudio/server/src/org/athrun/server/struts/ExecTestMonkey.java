/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.athrun.server.struts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.service.CaptureManager;
import org.athrun.server.service.DeviceManager;
import org.athrun.test.server.util.AthrunTestRunner;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * 调用monkey方法
 * @author renxia.sd
 */
public class ExecTestMonkey extends ActionSupport {

	private static final long serialVersionUID = -1359518557933912505L;
	
	private String serialNumber;
	
	private String packageName;
	
	private String activityName;

	private int testCount;//执行的monkey次数
	
	private boolean isRunning = true;
	
	private String deviceSnapshotsPath;
	
	/**
	 * 开启activity方法
	 * @param adb shell am start -n com.taobao.taobao/com.taobao.tao.Welcome
	 * @author renxia.sd
	 *
	 */
	
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
	
	public String execute() {
		String webRoot = ServletActionContext.getServletContext().getRealPath("/");
		String apksPath = webRoot + File.separator + "apks";
		File apksFolder = new File(apksPath);
		if (!apksFolder.exists()) {
			return "ERROR";
		}
		
		String snapshotsPath = webRoot + File.separator + "snapshots";
		System.out.println("截屏的snapshotsPath:》》》"+snapshotsPath);
		File snapshotsFolder = new File(snapshotsPath);
		if (!snapshotsFolder.exists()) {
			snapshotsFolder.mkdir();
		}
		
		deviceSnapshotsPath = snapshotsPath + File.separator + serialNumber;
		File deviceSnapshotsFolder = new File(deviceSnapshotsPath);
		if (!deviceSnapshotsFolder.exists()) {
			deviceSnapshotsFolder.mkdir();
		}
        // 执行monkey测试
        IDevice device = DeviceManager.getDeviceBySerialNumber(serialNumber);
		if (device != null) {
			new RunTestMonkeyThread(device).start();
			AthrunTestRunner.start(device);
			new Thread(){
				public void run(){
					while(isRunning){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						snapshot(deviceSnapshotsPath);
					}
				}
			}.start();
			try {
				device.executeShellCommand(monkeyCmd(packageName, 500), new OutputStreamShellOutputReceiver(System.out));
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
		return "SUCCESS";
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
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(deviceSnapshotsPath + File.separator + fileName));
			CaptureManager.getInstance().register(serialNumber, os);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getTestCount() {
		return testCount;
	}

	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}
}