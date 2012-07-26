package org.athrun.server.listener;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.struts2.ServletActionContext;
import org.athrun.client.AthrunSlaveClient;
import org.athrun.server.service.CaptureManager;
import org.athrun.server.service.DeviceManager;
import org.athrun.server.service.EventServiceManager;
import org.athrun.server.service.RemoteDeviceManager;

/**
 * Application Lifecycle Listener implementation class DeviceSetupListener
 * 
 */
public class DeviceSetupListener implements ServletContextListener {

	private AthrunSlaveClient client;
	
	/**
	 * Default constructor.
	 * 
	 * @author taichan
	 */
	public DeviceSetupListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		CaptureManager.getInstance();
		DeviceManager.CreateAdb();
		RemoteDeviceManager.getInstance();
		EventServiceManager.getInstance();
		
		new Thread() {
			public void run() {
				String host="10.13.47.33";
				int port = 53024;
				
				client = new AthrunSlaveClient(host, port);
				try {
					client.run();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		String clsesDir = DeviceSetupListener.class.getResource("/").getPath();
		int lastWebInfoPos = clsesDir.lastIndexOf("WEB-INF");
		String webRootDir = clsesDir.substring(0, lastWebInfoPos);
		String apksDir = webRootDir + "apks";
		String snapshotsDir = webRootDir + "snapshots";
		
		System.out.println("apksDir: " + apksDir);
		System.out.println("snapshotsDir: " + snapshotsDir);
		/*
		File file = new File(apksDir);
		if (file.exists()) {
			System.out.println("file exist!");
			System.out.println("file is directory: " +  file.isDirectory());
		}
		*/
		
		AthrunSlaveClient.setApksDir(apksDir);
		AthrunSlaveClient.setSnapshotsDir(snapshotsDir);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		DeviceManager.RemoveAdb();
		
		if (client != null) {
			client.shutdown();
		}
	}

}
