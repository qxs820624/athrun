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
		Thread.sleep(1000 * 5);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					// TODO Auto-generated method stub
					System.out.println(DeviceManager.getDeviceList().size());
					try {
						Thread.sleep(1000 * 2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();						
					}
				}
			}
		}).start();
		Thread.sleep(1000 * 60 * 5);
	}

}
