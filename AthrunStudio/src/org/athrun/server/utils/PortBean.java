/**
 * 
 */
package org.athrun.server.utils;

/**
 * @author taichan
 * 
 */
public class PortBean {
	private int eventPort;
	private int capturePort;
	private String serialNumber;

	/**
	 * @param eventPort
	 * @param capturePort
	 * @param serialNumber
	 */
	public PortBean(int eventPort, int capturePort, String serialNumber) {
		super();
		this.eventPort = eventPort;
		this.capturePort = capturePort;
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the eventPort
	 */
	public int getEventPort() {
		return eventPort;
	}

	/**
	 * @param eventPort
	 *            the eventPort to set
	 */
	public void setEventPort(int eventPort) {
		this.eventPort = eventPort;
	}

	/**
	 * @return the capturePort
	 */
	public int getCapturePort() {
		return capturePort;
	}

	/**
	 * @param capturePort
	 *            the capturePort to set
	 */
	public void setCapturePort(int capturePort) {
		this.capturePort = capturePort;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *            the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
