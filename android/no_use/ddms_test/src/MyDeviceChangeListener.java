import com.android.ddmlib.Client;

import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

/**
 * 
 */

/**
 * @author taichan
 * 
 */
public class MyDeviceChangeListener implements IDeviceChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#deviceConnected
	 * (com.android.ddmlib.IDevice)
	 */
	@Override
	public void deviceConnected(IDevice device) {
		// TODO Auto-generated method stub
		System.out.println("deviceConnected " + device.getSerialNumber());
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
		System.out.println("deviceDisconnected " + device.getSerialNumber());
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
		System.out.println("deviceChanged(" + changeMask + ")"
				+ device.getSerialNumber() + "-" + Client.CHANGE_PORT
				+ "表示可以用了");
	}
}
