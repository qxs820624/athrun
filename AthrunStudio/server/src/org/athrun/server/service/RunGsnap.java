package org.athrun.server.service;

import java.io.IOException;
import java.io.OutputStream;

import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.IShellOutputReceiver;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.utils.DeviceConfig;

public class RunGsnap {

	private IDevice device;

	// 表示是否在运行状态
	private boolean running;

	public IDevice getDevice() {
		return device;
	}

	public void setDevice(IDevice device) {
		this.device = device;
	}

	public boolean isRunning() {
		return running;
	}

	class RunGnaspThread extends Thread {
		public void run() {
			while (true) {
				boolean deviceNotFound = false;
				long sleepTime = 2000;
				
				try {
					String cmdString = CaptureManager.remotePath
							+ " /sdcard/test/1.jpg /dev/graphics/fb0 50 2";
					if (DeviceConfig.needAdjust(device.getSerialNumber())) {
						cmdString += " 0 8 16";
					}

					// 重置失败标识
					running = true;

					device.executeShellCommand(cmdString,
							new OutputStreamShellOutputReceiver(System.out));

					running = false;

				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AdbCommandRejectedException e) {
					deviceNotFound = true;
					sleepTime = 10000;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ShellCommandUnresponsiveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	Thread runThread;
	
	public void run() {
		if (running) {
			return;
		}

		if (runThread != null) {
			return;
		}
		
		runThread = new RunGnaspThread();
		runThread.start();
	}

}
