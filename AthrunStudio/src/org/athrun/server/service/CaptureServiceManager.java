/**
 * 
 */
package org.athrun.server.service;

/**
 * @author taichan 负责开启截图和事件服务
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
