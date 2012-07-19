package org.athrun.server.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.SyncException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.utils.ReservedPortExhaust;

public class CaptureManager {

	static final String remotePath = "/data/local/gsnap";
	
	private Map<String, CaptureService> captureServiceMap = new HashMap<String, CaptureService>();

	private static CaptureManager instance = new CaptureManager(); // 单态

	public static CaptureManager getInstance() {
		return instance;
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
		
		TaskResult tr = service.capture(serialNumber, output);
		
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
