/**
 * 
 */
package org.athrun.server.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.SyncException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.utils.ForwardPortManager;
import org.athrun.server.utils.OneParameterRunnable;

/**
 * @author taichan
 * 
 */
public class EventManager {

	private static final String remotePath = "/data/local/tmp/InjectAgent.jar";

	/**
	 * @return true if there was a client running
	 */
	static boolean killRunningAgent(int port) {
		try {
			Socket s = new Socket("127.0.0.1", port);
			OutputStream os = s.getOutputStream();
			os.write("quit\n".getBytes());
			os.flush();
			os.close();
			s.close();
			return true;
		} catch (Exception ex) {
			// ignore
		}
		return false;
	}

	/**
	 * @param serialNumber
	 * @param device
	 */
	public static void checkEventService(String serialNumber, IDevice device) {

		try {
			device.createForward(ForwardPortManager.getEventPort(serialNumber),
					1324);
			EventManager.killRunningAgent(ForwardPortManager
					.getEventPort(serialNumber));

			uploadEventAgent(device);

			Thread th = new Thread(new OneParameterRunnable(device) {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					IDevice device = (IDevice) getParameter();
					try {
						device.executeShellCommand(
								"export CLASSPATH="
										+ remotePath
										+ ";"
										+ "exec app_process /system/bin net.srcz.android.screencast.client.Main 1324",
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
			});
			th.start();
			Thread.sleep(2000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param device
	 * @throws AdbCommandRejectedException
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SyncException
	 * @throws URISyntaxException
	 * 
	 */
	private static void uploadEventAgent(IDevice device) throws SyncException,
			TimeoutException, IOException, AdbCommandRejectedException,
			URISyntaxException {
		File file = new File(EventManager.class
				.getResource("/event/InjectAgent.jar").toURI().getPath());

		// device.getSyncService().pushFile(file.getAbsolutePath(), remotePath,
		// new NullSyncProgressMonitor());
		// 导致：ADB server didn't ACK

		CommandRunner.pushFile(device, file.getAbsolutePath(), remotePath);
	
		System.out.println("event agent uploaded.");
	}
}
