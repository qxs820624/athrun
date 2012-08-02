package org.athrun.server.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.AndroidDebugBridge;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.AthrunDebugBridgeChanged;
import org.athrun.server.adb.AthrunDeviceChanged;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.log.Log;
import org.athrun.server.struts.Device;
import org.athrun.server.utils.DeviceConfig;
import org.athrun.server.utils.ForwardPortManager;
import org.athrun.server.utils.OneParameterRunnable;
import org.athrun.server.utils.ReservedPortExhaust;

/**
 * 负责开启截图和事件服务
 * 
 * @author taichan
 */
public class DeviceManager {
	private static AthrunDeviceChanged deviceChangedInstance = new AthrunDeviceChanged();
	private static AthrunDebugBridgeChanged debugBridgeChangedInstance = new AthrunDebugBridgeChanged();

	public static void CreateAdb() {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge.createBridge("adb", false);
		AndroidDebugBridge.addDeviceChangeListener(deviceChangedInstance);
		AndroidDebugBridge
				.addDebugBridgeChangeListener(debugBridgeChangedInstance);
		startUpdateDevicesToRemote();
	}

	private static boolean updateToRemote = false;

	/**
	 * @author taichan
	 */
	private static void startUpdateDevicesToRemote() {
		if (!updateToRemote) {

			new Thread(new OneParameterRunnable(deviceList) {

				@Override
				public void run() {
					Map<String, IDevice> deviceList = (Map<String, IDevice>) getParameter();
					while (true) {
						try {
							Thread.sleep(RemoteDeviceManager.UpdateStatusTime); // 等待
																				// 2
																				// 分钟
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						for (IDevice iDevice : deviceList.values()) {
							RemoteDeviceManager.register(new Device(iDevice,
									true));
						}
					}
				}
			}, "UpdateDeviceToRemote").start();

			updateToRemote = true;
		}
	}

	public static void RemoveAdb() {
		AndroidDebugBridge.removeDeviceChangeListener(deviceChangedInstance);
		AndroidDebugBridge
				.removeDebugBridgeChangeListener(debugBridgeChangedInstance);
		AndroidDebugBridge.terminate();
	}

	private static Map<String, IDevice> deviceList = new HashMap<String, IDevice>();

	public static Map<String, IDevice> getDeviceList() {
		return deviceList;
	}
	
	// add by wuhe
	public static IDevice getDeviceBySerialNumber(String serialNumber) {
		if (serialNumber == null) {
			return null;
		}
		
		return deviceList.get(serialNumber);
	}

	// 说明device已经连接
	public static void add(IDevice device) {
		String serialNumber = device.getSerialNumber();
		
		
		synchronized (deviceList) {
			if (deviceList.containsKey(serialNumber)) {
				// 什么都不做
			} else {
				checkServices(serialNumber, device);
				System.out.println("deviceList add");
				deviceList.put(serialNumber, device);
			}
		}

		
		CaptureManager.getInstance().add(serialNumber);
		RemoteDeviceManager.register(new Device(device, true));
	}

	/**
	 * @param serialNumber
	 * @param device
	 */
	private static void checkServices(String serialNumber, IDevice device) {
		checkCaptureService(serialNumber, device);
		EventManager.checkEventService(serialNumber, device);
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
					device.createForward(
							ForwardPortManager.getCapturePort(serialNumber),
							5678);
				} catch (AdbCommandRejectedException e) {
					System.out.println("create forward失败：" + serialNumber);
					System.out.println("正在处理 create forward 失败的情况。");
					Thread.sleep(5000);
					CommandRunner.createForward(device,
							ForwardPortManager.getCapturePort(serialNumber),
							5678);
					// e.printStackTrace();
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
				CaptureManager.uploadCaptureEvent(device);
				device.executeShellCommand("chmod 777 "
						+ CaptureManager.remotePath,
						new OutputStreamShellOutputReceiver(System.out));
				/*
				// & 代表将shell命令放入后台工作，但验证不可行，尝试启线程方式运行
				new Thread(new OneParameterRunnable(device) {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						IDevice device = (IDevice) getParameter();
						try {
							
							OutputStream os = new ByteArrayOutputStream();

							String cmdString = CaptureManager.remotePath
									+ " /sdcard/test/1.jpg /dev/graphics/fb0 50 2";
							if (DeviceConfig.needAdjust(device.getSerialNumber())) {
								cmdString += " 0 8 16";
							}
							device.executeShellCommand(cmdString,
									new OutputStreamShellOutputReceiver(os));
							String content = os.toString();
							os.flush();

							// server: Address already in use
							// server bind to the address failed!
							// at exist
							if (content.length() < 100) {
								if (content
										.contains("server bind to the address failed")) {
									System.out.println("请处理端口被占用的情况！");
									try {
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									device.executeShellCommand(
											CaptureManager.remotePath
													+ " /sdcard/test/1.jpg /dev/graphics/fb0 50 2",
											new OutputStreamShellOutputReceiver(
													os));
									System.out
											.println("第二次输出：" + os.toString());
								}
							}
							
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
				}, "gsnap-sync").start();
				*/
				//Thread.sleep(2000);
				RunGsnap runGsnap = new RunGsnap();
				runGsnap.setDevice(device);
				runGsnap.run();
				
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
			// 这里要读10个字节，逻辑不够严密
			int number = in.read(b);
			//assert (number == 6); // 返回 finish
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
		System.out.println("移除" + serialNumber + "设备的同步功能");
		synchronized (deviceList) {
			System.out.println("deviceList remove");
			deviceList.remove(serialNumber);
		}
		CaptureManager.getInstance().remove(serialNumber);
		RemoteDeviceManager.unRegister(new Device(device, true));
	}

	static Object closeAllLock = new Object();

	public static void closeAllConnection() {
		synchronized (closeAllLock) {

			// 防止 ConcurrentModificationException
			List<IDevice> devicelist = new ArrayList<IDevice>();
			for (IDevice device : deviceList.values()) {
				devicelist.add(device);
			}

			for (IDevice iDevice : devicelist) {
				DeviceManager.remove(iDevice);
				try {
					EventManager.killRunningAgent(ForwardPortManager
							.getEventPort(iDevice.getSerialNumber()));
				} catch (ReservedPortExhaust e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
