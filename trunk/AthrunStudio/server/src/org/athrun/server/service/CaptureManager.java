package org.athrun.server.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.athrun.client.AthrunSlaveClient;
import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.SyncException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.struts.Device;
import org.athrun.server.struts.Devices;
import org.athrun.server.utils.ReservedPortExhaust;

public class CaptureManager {

	static final String remotePath = "/data/local/gsnap";
	
	private Map<String, CaptureService> captureServiceMap = new HashMap<String, CaptureService>();

	private static CaptureManager instance = new CaptureManager(); // 单态

	public static CaptureManager getInstance() {
		return instance;
	}
	
	public void runMonkeyTest(String serialNumber, String testcaseName, String packageName, String activityName, int testCount) {
		CaptureService service = getCaptureService(serialNumber);
		if (service != null) {
			String snapshotsDir = AthrunSlaveClient.getSnapshotsDir();
			String deviceSnapshotsPath = snapshotsDir + File.separator + serialNumber + File.separator + testcaseName;
			File serialDirFile = new File(deviceSnapshotsPath);
			// 会递归创建目录
			serialDirFile.mkdirs();
			
			service.runMonkeyTest(serialNumber, deviceSnapshotsPath, packageName, activityName, testCount);
			
			/*
			//FOR DEBUG
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			MsgProcessor.sendMonkeyResult(serialNumber, testcaseName, deviceSnapshotsPath);
		}
	}

	public void processAdjustQuality(int qualityRate, String serialNumber)
			throws ReservedPortExhaust, IOException {
		assert (qualityRate >= 0);
		assert (qualityRate <= 100);

		CaptureService service = getCaptureService(serialNumber);
		if (service != null) {
			service.adjustQuality(serialNumber, qualityRate);
		}
	}

	/**
	 * use cache
	 * @param serialNumber
	 * @return
	 */
	public FramebufferInfo getCaptureInfo(String serialNumber) {

		TaskResult tr = null;
		CaptureService service = getCaptureService(serialNumber);
		if (service != null) {
			tr = service.info(serialNumber);
		}
		
		FramebufferInfo framebufferInfo = new FramebufferInfo();
		framebufferInfo.parseInfo((tr == null)? null : tr.getResult());
		return framebufferInfo;
		
	}

	public void processAdjustResize(int resizeRate, String serialNumber)
			throws ReservedPortExhaust, IOException {
		CaptureService service = getCaptureService(serialNumber);
		if (service != null) {
			service.adjustResize(serialNumber, resizeRate);
		}
	}

	public void register(String serialNumber, OutputStream output) {
		
		long start = System.currentTimeMillis();
		
		CaptureService service = getCaptureService(serialNumber);
		if (service == null) {
			return;
		}
		
		TaskResult tr = service.capture(serialNumber);
		
		long internal = System.currentTimeMillis() - start;
		
		// 太快了没有必要，休眠一会再返回
		if (internal < 40) {
			try {
				Thread.sleep(40 - internal);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		if ((tr != null) &&(tr.isCached())) {
			TaskResult.writeToOutput(serialNumber, output);
		}
	}

	/**
	 * @param serialNumber
	 */
	public void remove(String serialNumber) {
		synchronized(captureServiceMap) {
			CaptureService service = captureServiceMap.get(serialNumber);
			if (service != null) {
				service.kill(serialNumber);
				service.stop();
			}
		}
	}

	/**
	 * @param serialNumber
	 */
	public void add(String serialNumber) {
		
		remove(serialNumber);
		
		synchronized(captureServiceMap) {
			CaptureService service = new CaptureService();
			captureServiceMap.put(serialNumber, service);
		}
		
		// 通知服务端在线手机信息
		List<Device> onlineDevices = Devices.getCurrent().getDevices();	
		MsgProcessor.notifyOnlineDevices(onlineDevices);
	}
	
	public CaptureService getCaptureService(String serialNumber) {
		synchronized(captureServiceMap) {
			return captureServiceMap.get(serialNumber);
		}
	}

	public static void uploadCaptureEvent(IDevice device) throws SyncException,
			IOException, TimeoutException, AdbCommandRejectedException,
			URISyntaxException {
		File file = new File(EventManager.class.getResource("/gsnap/gsnap")
				.toURI().getPath());

		// device.getSyncService().pushFile(file.getAbsolutePath(), remotePath,
		// new NullSyncProgressMonitor());
		// 导致：ADB server didn't ACK

		CommandRunner.pushFile(device, file.getAbsolutePath(), remotePath);

		System.out.println("capture agent uploaded.");
	}

}
