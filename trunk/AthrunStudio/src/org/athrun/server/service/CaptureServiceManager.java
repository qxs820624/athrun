/**
 * 
 */
package org.athrun.server.service;

/**
 * @author taichan ��������ͼ���¼�����
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
