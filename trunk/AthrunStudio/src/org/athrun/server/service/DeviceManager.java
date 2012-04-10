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

/**
 * @author taichan ��������ͼ���¼�����
 */
public class DeviceManager {
	public static void CreateAdb() {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge.createBridge("adb.exe", false);
		AndroidDebugBridge.addDeviceChangeListener(new AthrunDeviceChanged());
		AndroidDebugBridge
				.addDebugBridgeChangeListener(new AthrunDebugBridgeChanged());
	}

	private static Map<String, IDevice> deviceList = new HashMap<String, IDevice>();

	// ˵��device�Ѿ�����
	public static void add(IDevice device) {
		String serialNumber = device.getSerialNumber();
		synchronized (deviceList) {
			if (deviceList.containsKey(serialNumber)) {
				// ʲô������
			} else {
				checkServices(device);
				deviceList.put(serialNumber, device);
			}
		}
	}

	/**
	 * @param device
	 */
	private static void checkServices(IDevice device) {
		checkCaptureService(device);
		checkEventService(device);
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
	 * @param device
	 */
	public static void checkCaptureService(IDevice device) {
		try {
			// ��һ��5678�˿��ǲ��ǿ��Է���
			switch (isSocketPortAvailable(5678)) {
			case OK:
				// ������Է��ʣ�ֱ���þͿ����ˣ��˳�check
				break;
			case ForwardError:
				// device.removeForward(5678, 5678);
				try {
					device.createForward(5678, 5678);
				} catch (AdbCommandRejectedException e) {
					e.printStackTrace();
					device.removeForward(5678, 5678);
					device.createForward(5678, 5678);
				}
				checkCaptureService(device);
				Log.d("DeviceManager", "����forward" + 5678);
				break;
			case SoftwareError:
				// TODO ���gsnap
				// �����gsnap��ֱ��createForward
				// ���û��gsnap���������̣���createForward
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
		byte[] b = new byte[10];// ����finish
		try {
			server = new Socket("127.0.0.1", 5678);
			OutputStream out = server.getOutputStream();
			out.write('t');
			out.flush();
			InputStream in = server.getInputStream();
			int number = in.read(b);
			assert (number == 6); // ���� finish
			in.close();
			out.close();
			server.close();
			return SocketStatus.OK;
		} catch (ConnectException e) {
			Log.d("DeviceManager", "�˿�δ���ţ����豸δ����");
			return SocketStatus.ForwardError;
		} catch (SocketException e) {
			Log.d("DeviceManager", "�˿ڿ��ţ����޷���������");
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

	// ˵��device�Ѿ��Ͽ�
	public static void remove(IDevice device) {
		String serialNumber = device.getSerialNumber();
		synchronized (deviceList) {
			deviceList.remove(serialNumber);
		}
	}
}
