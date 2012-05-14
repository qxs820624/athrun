/**
 * 
 */
package org.athrun.server.utils;

import java.util.Properties;

/**
 * @author taichan
 *
 */
public class PropertiesUtil {
	public static Properties AthrunProperties; 
	
	
	public static String getHost(){
		return AthrunProperties.getProperty("host");
	}
	
	public static String getPort(){
		return AthrunProperties.getProperty("port");
	}

	/**
	 * @return
	 */
	public static String getContextPath() {
		// TODO Auto-generated method stub
		return AthrunProperties.getProperty("contextPath");
	}
}
