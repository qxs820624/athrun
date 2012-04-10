/**
 * 
 */
package org.athrun.server.adb;

import org.athrun.ddmlib.Client;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import org.athrun.server.service.DeviceManager;


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
			DeviceManager.add(device);	
		}
	}

}