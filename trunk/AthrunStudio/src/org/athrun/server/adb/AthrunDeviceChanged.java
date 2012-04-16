/**
 * 
 */
package org.athrun.server.adb;

import java.util.ArrayList;
import java.util.List;

import org.athrun.ddmlib.Client;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import org.athrun.server.service.DeviceManager;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class AthrunDeviceChanged implements IDeviceChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#deviceConnected
	 * (com.android.ddmlib.IDevice)
	 */
	@Override
	public void deviceConnected(IDevice device) {
		System.out.println("deviceConnected: " + device.getSerialNumber());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#
	 * deviceDisconnected(com.android.ddmlib.IDevice)
	 */
	@Override
	public void deviceDisconnected(IDevice device) {
		// TODO Auto-generated method stub
		System.out.println("deviceDisconnected: " + device.getSerialNumber());
		System.out.println("说明设备" + device.getSerialNumber() + "已经拔掉了");
		CancelAdd(device.getSerialNumber());
		DeviceManager.remove(device);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#deviceChanged
	 * (com.android.ddmlib.IDevice, int)
	 */
	@Override
	public void deviceChanged(IDevice device, int changeMask) {
		// TODO Auto-generated method stub
		System.out.println("deviceChanged: (" + changeMask + ")"
				+ device.getSerialNumber());
		if (changeMask == Client.CHANGE_PORT) {
			System.out.println("说明设备" + device.getSerialNumber() + "已经可以用了");
			DelayAdd(device.getSerialNumber(), device);
		}
	}

	// 延迟添加设备，以防事件变化太快，造成不必要的事件响应
	private void DelayAdd(String serialNumber, IDevice device) {
		boolean canAdd = serialList.add(serialNumber);
		if (canAdd) {
			System.out.println("设备" + serialNumber + "：加入Delay列表");
		} else {
			System.out.println("[Debug]设备" + serialNumber + "：不需要再加入Delay列表");
		}

		// TODO 传参不规范，要改
		currentSerialNumber = serialNumber;
		currentDevice = device;

		new Thread(new Runnable() {
			@Override
			public void run() {
				String serialNumber1 = currentSerialNumber;
				IDevice device1 = currentDevice;
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated method stub
				if (serialList.contains(serialNumber1)) {
					serialList.remove(serialNumber1);
					System.out.println("设备" + serialNumber1 + "：从Delay列表中处理完毕");
					DeviceManager.add(device1);
				} else {
					System.out.println("[Debug]设备" + serialNumber1 + "：已经被删除");
				}
			}
		}, serialNumber).start();
	}

	private static IDevice currentDevice;
	private static String currentSerialNumber;

	private static List<String> serialList = new ArrayList<String>();

	// 取消添加设备
	private void CancelAdd(String serialNumber) {
		boolean canRemove = serialList.remove(serialNumber);
		if (canRemove) {
			System.out.println("设备" + serialNumber + "：从Delay列表删除");
		} else {
			System.out.println("[Debug]设备" + serialNumber
					+ "：不存在，不需要从Delay列表删除");
		}
	}

}