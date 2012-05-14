/**
 * 
 */
package org.athrun.server.struts;

import java.util.ArrayList;
import java.util.List;

import org.athrun.ddmlib.IDevice;
import org.athrun.server.service.CaptureManager;
import org.athrun.server.service.DeviceManager;
import org.athrun.server.service.RemoteDeviceManager;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author taichan
 * 
 */
public class Devices {
	List<Device> devices = new ArrayList<Device>();
	public void add(Device device){
		this.devices.add(device);
	}
	/**
	 * @return the devices
	 */
	public List<Device> getDevices() {
		return devices;
	}
	
	public static Devices getCurrent(){
		Devices devices = new Devices();

		for (String serialNumber : DeviceManager.getDeviceList().keySet()) {
			IDevice device = DeviceManager.getDeviceList()
					.get(serialNumber);
			Device deviceStru = new Device(device, false);			
			devices.add(deviceStru);
		}
		
		for (Device de : RemoteDeviceManager.getDevices()) {
			devices.add(de);
		}		
				
		return devices;		
	}
}
