package org.athrun.server.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.athrun.server.service.CaptureManager;
import org.athrun.server.service.DeviceManager;
import org.athrun.server.service.RemoteDeviceManager;
import org.athrun.server.utils.PropertiesUtil;

/**
 * Application Lifecycle Listener implementation class DeviceSetupListener
 *
 */
public class DeviceSetupListener implements ServletContextListener {

    /**
     * Default constructor. 
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
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	DeviceManager.RemoveAdb();
    }
	
}
