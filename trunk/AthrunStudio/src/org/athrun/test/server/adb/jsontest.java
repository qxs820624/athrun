/**
 * 
 */
package org.athrun.test.server.adb;

import static org.junit.Assert.*;

import net.sf.json.JSONObject;

import org.athrun.ddmlib.IDevice;
import org.athrun.server.service.DeviceManager;
import org.athrun.server.struts.Device;
import org.athrun.server.struts.Devices;
import org.athrun.server.struts.HelloWorld;
import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class jsontest {

	@Test
	public void test() throws InterruptedException {
		
		DeviceManager.CreateAdb();
		Thread.sleep(1000 * 5);
		
		Devices devices = new Devices();

		for (String serialNumber : DeviceManager.getDeviceList().keySet()) {
			IDevice device = DeviceManager.getDeviceList().get(serialNumber);
			devices.add(new Device(device));
		}

		JSONObject fromObject = JSONObject.fromObject(devices);

		System.out.println(fromObject.toString(1));
	}

}
