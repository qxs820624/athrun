/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.athrun.server.struts;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.InstallException;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.service.DeviceManager;
import org.athrun.test.server.util.AthrunTestRunner;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @author wuhe
 */
public class ExecTestCase extends ActionSupport {

	private static final long serialVersionUID = -1359518557933912505L;
	
	private String serialNumber;
	
	private String packageName;
	
	private String activityName;
	
	private String testCmds;
	
	private class RunTestcaseThread extends Thread {
		private IDevice device;
				
		public RunTestcaseThread(IDevice device) {
			this.device = device;
		}

		public void run() {
			/*
			 * C:\Documents and Settings\wuhe.jyh>adb -s SH14MTJ01900 shell am instrument -w  -
	e port 1234  -e class com.taobao.android.client.test.PromotionTest2#testjust2 co
	m.taobao.android.client.test/pl.polidea.instrumentation.PolideaInstrumentationTe
	stRunnerEx
			 */
			StringBuilder sb = new StringBuilder();
			sb.append("am instrument -w ")
				.append("-e port 1234 ")
				.append("-e packageName ").append(packageName).append(" ")
				.append("-e activityName ").append(activityName).append(" ")
				.append("-e class org.athrun.remoterunner.RemoteTestRunner#test ")
				.append("org.athrun.remoterunner/pl.polidea.instrumentation.PolideaInstrumentationTestRunnerEx");
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
		File snapshotsFolder = new File(snapshotsPath);
		if (!snapshotsFolder.exists()) {
			snapshotsFolder.mkdir();
		}
		
		String deviceSnapshotsPath = snapshotsPath + File.separator + serialNumber;
		File deviceSnapshotsFolder = new File(deviceSnapshotsPath);
		if (!deviceSnapshotsFolder.exists()) {
			deviceSnapshotsFolder.mkdir();
		}
		
        // 执行测试脚本
        IDevice device = DeviceManager.getDeviceBySerialNumber(serialNumber);
		if (device != null) {
			new RunTestcaseThread(device).start();
			try {
				Thread.sleep(2000);
				
				System.out.println("testCmds: " + testCmds);
				AthrunTestRunner.start(device);
				AthrunTestRunner.runTestScript(testCmds);
				AthrunTestRunner.listenTcRunning(deviceSnapshotsPath, serialNumber);
				AthrunTestRunner.stop();
				//Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "SUCCESS";
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

	public String getTestCmds() {
		return testCmds;
	}

	public void setTestCmds(String testCmds) {
		this.testCmds = testCmds;
	}
}