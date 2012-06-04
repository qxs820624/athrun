package org.athrun.server.listener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;

import org.athrun.server.utils.DeviceConfig;
import org.athrun.server.utils.PropertiesUtil;

import org.xml.sax.SAXException;

/**
 * Application Lifecycle Listener implementation class WebConfigListener
 * 
 */
public class WebConfigListener implements ServletContextListener {

	/**
	 * Default constructor.
	 * 
	 * @author taichan
	 */
	public WebConfigListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		PropertiesUtil.AthrunProperties = new Properties();

		try {
			String path = getClass().getClassLoader()
					.getResource("config.properties").toURI().getPath();
			FileInputStream fis = new FileInputStream(path);
			PropertiesUtil.AthrunProperties.load(fis);

			String xmlcfgfile = arg0.getServletContext().getRealPath(
					"WEB-INF/DeviceConfig.xml");
			DeviceConfig.load(new FileInputStream(xmlcfgfile));

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}
