package org.athrun.test.server.adb;

import org.athrun.ddmlib.AndroidDebugBridge;
import org.athrun.server.adb.AthrunDebugBridgeChanged;
import org.athrun.server.adb.AthrunDeviceChanged;
import org.junit.Test;

/**
 * 
 */

/**
 * @author taichan
 * 
 */
public class AndroidDebugBridgeTest {

	@Test
	public void test() throws InterruptedException {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge("adb.exe", false);
		System.out.println(adb.toString());
		AndroidDebugBridge.addDeviceChangeListener(new AthrunDeviceChanged());		
		AndroidDebugBridge.addDebugBridgeChangeListener(new AthrunDebugBridgeChanged());		
		
		while (true) {
			Thread.sleep(1000);
		}

	}
}
