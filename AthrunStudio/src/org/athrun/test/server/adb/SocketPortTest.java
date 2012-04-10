/**
 * 
 */
package org.athrun.test.server.adb;

import org.athrun.server.service.DeviceManager;
import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class SocketPortTest {

	@Test
	public void test() throws InterruptedException {
		DeviceManager.CreateAdb();    	
		Thread.sleep(10000);
	}

}
