package org.athrun.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.AndroidDebugBridge;
import org.athrun.ddmlib.IDevice;
import org.athrun.server.adb.AthrunDebugBridgeChanged;
import org.athrun.server.adb.AthrunDeviceChanged;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.log.Log;
import org.athrun.server.utils.ForwardPortManager;

/**
 * @author taichan 负责开启截图和事件服务
 */
public class DeviceManager {
	private static AthrunDeviceChanged deviceChangedInstance = new AthrunDeviceChanged();
	private static AthrunDebugBridgeChanged debugBridgeChangedInstance = new AthrunDebugBridgeChanged();

	public static void CreateAdb() {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge.createBridge("adb.exe", false);
		AndroidDebugBridge.addDeviceChangeListener(deviceChangedInstance);
		AndroidDebugBridge
				.addDebugBridgeChangeListener(debugBridgeChangedInstance);
	}

	public static void RemoveAdb() {
		AndroidDebugBridge.removeDeviceChangeListener(deviceChangedInstance);
		AndroidDebugBridge
				.removeDebugBridgeChangeListener(debugBridgeChangedInstance);
		AndroidDebugBridge.terminate();
	}

	private static Map<String, IDevice> deviceList = new HashMap<String, IDevice>();

	
	public static Map<String, IDevice> getDeviceList(){
		return deviceList;
	}
	
	// 说明device已经连接
	public static void add(IDevice device) {
		String serialNumber = device.getSerialNumber();
		synchronized (deviceList) {
			if (deviceList.containsKey(serialNumber)) {
				// 什么都不做
			} else {
				checkServices(serialNumber, device);
				deviceList.put(serialNumber, device);
			}
		}
		CaptureManager.getInstance().add(serialNumber);
	}

	/**
	 * @param serialNumber
	 * @param device
	 */
	private static void checkServices(String serialNumber, IDevice device) {
		checkCaptureService(serialNumber, device);
		// checkEventService(device);
	}

	/**
	 * @param device
	 */
	private static void checkEventService(IDevice device) {

		try {
			device.createForward(1324, 1324);
			device.executeShellCommand(
					"export CLASSPATH=/data/local/tmp/InjectAgent.jar; exec app_process /system/bin net.srcz.android.screencast.client.Main 1324",
					new OutputStreamShellOutputReceiver(System.out));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param serialNumber
	 * @param device
	 */
	public static void checkCaptureService(String serialNumber, IDevice device) {
		try {
			// 看一下5678端口是不是可以访问
			switch (isSocketPortAvailable(ForwardPortManager
					.getCapturePort(serialNumber))) {
			case OK:
				// 如果可以访问，直接用就可以了，退出check
				break;
			case ForwardError:
				try {
					device.createForward(5678,
							ForwardPortManager.getCapturePort(serialNumber));
				} catch (AdbCommandRejectedException e) {
					e.printStackTrace();
					device.removeForward(5678,
							ForwardPortManager.getCapturePort(serialNumber));
					device.createForward(5678,
							ForwardPortManager.getCapturePort(serialNumber));
				}
				checkCaptureService(serialNumber, device);
				Log.d("DeviceManager",
						"创建forward"
								+ ForwardPortManager
										.getCapturePort(serialNumber));
				break;
			case SoftwareError:
				// TODO 检查gsnap
				// 如果有gsnap，直接createForward
				// 如果没有gsnap，启动进程，再createForward
				device.executeShellCommand("chmod 777 /data/local/gsnap",
						new OutputStreamShellOutputReceiver(System.out));
				device.executeShellCommand(
						"/data/local/gsnap /sdcard/test/1.jpg /dev/graphics/fb0 50 2",
						new OutputStreamShellOutputReceiver(System.out));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param port
	 * @return
	 */
	private static SocketStatus isSocketPortAvailable(int port) {
		Socket server;
		byte[] b = new byte[10];// 存入finish
		try {
			server = new Socket("127.0.0.1", port);
			OutputStream out = server.getOutputStream();
			out.write('t');
			out.flush();
			InputStream in = server.getInputStream();
			int number = in.read(b);
			assert (number == 6); // 返回 finish
			in.close();
			out.close();
			server.close();
			return SocketStatus.OK;
		} catch (ConnectException e) {
			Log.d("DeviceManager", "端口未开放，或设备未插入");
			return SocketStatus.ForwardError;
		} catch (SocketException e) {
			Log.d("DeviceManager", "端口开放，但无法建立连接");
			return SocketStatus.SoftwareError;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SocketStatus.Invalid;
	}

	enum SocketStatus {
		OK, ForwardError, SoftwareError, Invalid
	}

	// 说明device已经断开
	public static void remove(IDevice device) {
		String serialNumber = device.getSerialNumber();
		synchronized (deviceList) {
			deviceList.remove(serialNumber);
		}
		CaptureManager.getInstance().remove(serialNumber);
	}
}
