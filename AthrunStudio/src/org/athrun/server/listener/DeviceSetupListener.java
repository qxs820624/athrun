package org.athrun.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.athrun.server.service.CaptureManager;
import org.athrun.server.service.DeviceManager;
import org.athrun.server.service.EventServiceManager;
import org.athrun.server.service.RemoteDeviceManager;

/**
 * Application Lifecycle Listener implementation class DeviceSetupListener
 * 
 */
public class DeviceSetupListener implements ServletContextListener {

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
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		DeviceManager.RemoveAdb();
	}

}
