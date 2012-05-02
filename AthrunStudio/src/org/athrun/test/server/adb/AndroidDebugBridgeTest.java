package org.athrun.test.server.adb;

import java.io.IOException;
import java.net.URISyntaxException;

import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.AndroidDebugBridge;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.SyncException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.AthrunDebugBridgeChanged;
import org.athrun.server.adb.AthrunDeviceChanged;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.service.CaptureManager;
import org.athrun.server.utils.ForwardPortManager;
import org.athrun.server.utils.OneParameterRunnable;
import org.athrun.server.utils.ReservedPortExhaust;
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
	public void test() throws InterruptedException, TimeoutException,
			AdbCommandRejectedException, IOException,
			ShellCommandUnresponsiveException, ReservedPortExhaust,
			SyncException, URISyntaxException {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge("adb.exe",
				false);
		System.out.println(adb.toString());
		// AndroidDebugBridge.addDeviceChangeListener(new
		// AthrunDeviceChanged());
		// AndroidDebugBridge
		// .addDebugBridgeChangeListener(new AthrunDebugBridgeChanged());

		Thread.sleep(5000);
		System.out.println(adb.getDevices().length);

		for (IDevice device : adb.getDevices()) {
			System.out.println(device.getSerialNumber());
			System.out.println(ForwardPortManager.getCapturePort(device
					.getSerialNumber()) + ": ");

			device.createForward(
					ForwardPortManager.getCapturePort(device.getSerialNumber()),
					5678);

			CaptureManager.uploadCaptureEvent(device);
			device.executeShellCommand("chmod 777 " + "/data/local/gsnap",
					new OutputStreamShellOutputReceiver(System.out));

			new Thread(new OneParameterRunnable(device) {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					IDevice device = (IDevice)	getParameter();
					try {
						device.executeShellCommand("/data/local/gsnap"
								+ " /sdcard/test/1.jpg /dev/graphics/fb0 50 2",
								new OutputStreamShellOutputReceiver(System.out));
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
			}).start();
			
		}

		while (true) {
			Thread.sleep(1000);
			System.out.println(adb.getDevices().length);
		}

	}
}
