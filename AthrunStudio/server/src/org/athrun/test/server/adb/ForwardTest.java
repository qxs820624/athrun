/**
 * 
 */
package org.athrun.test.server.adb;

import org.athrun.ddmlib.AndroidDebugBridge;
import org.athrun.ddmlib.IDevice;
import org.junit.Test;


/**
 * @author taichan
 *
 */
public class ForwardTest {

	@Test
	public void test() throws Exception {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge("adb.exe", false);
		Thread.sleep(10000);
		IDevice iDevice = adb.getDevices()[0];
		iDevice.createForward(5678, 5678);
		iDevice.createForward(5678, 5678);
		iDevice.removeForward(5678, 5678);
	}

}
