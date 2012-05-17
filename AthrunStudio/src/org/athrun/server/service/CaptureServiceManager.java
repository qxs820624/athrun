/**
 * 
 */
package org.athrun.server.service;

/**
 * 负责开启截图和事件服务
 * @author taichan 
 */
public class CaptureServiceManager {
	private static CaptureServiceManager instance;

	public static CaptureServiceManager getInstance() {
		if (instance == null) {
			instance = new CaptureServiceManager();
		}
		return instance;
	}

	private CaptureServiceManager() {
	}

	public void reconnect() {

	}
}
