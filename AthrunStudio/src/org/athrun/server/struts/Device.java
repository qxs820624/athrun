/**
 * 
 */
package org.athrun.server.struts;

import org.athrun.ddmlib.IDevice;

/**
 * @author taichan
 * 
 */
public class Device {
	/**
	 * @author taichan
	 * @param iDevice
	 */
	public Device(IDevice iDevice, boolean isRemote) {
		// TODO Auto-generated constructor stub
		this.manufacturer = iDevice.getProperty("ro.product.manufacturer");
		this.model = iDevice.getProperty("ro.product.model");
		this.device = iDevice.getProperty("ro.product.device");
		this.sdk = iDevice.getProperty("ro.build.version.sdk");
		this.ipAddress = iDevice.getProperty("dhcp.eth0.ipaddress");
		this.cpuAbi = iDevice.getProperty("ro.product.cpu.abi");
		this.serialNumber = iDevice.getSerialNumber();
		this.isRemote = isRemote;		
	}

	/**
	 * @param manufacturer
	 * @param model
	 * @param device
	 * @param sdk
	 * @param ipAddress
	 * @param cpuAbi
	 * @param serialNumber
	 */
	public Device(String manufacturer, String model, String device, String sdk,
			String ipAddress, String cpuAbi, String serialNumber,
			boolean isRemote) {
		this.manufacturer = manufacturer;
		this.model = model;
		this.device = device;
		this.sdk = sdk;
		this.ipAddress = ipAddress;
		this.cpuAbi = cpuAbi;
		this.serialNumber = serialNumber;
		this.isRemote = isRemote;
	}

	/**
	 * @return the isRemote
	 */
	public boolean isRemote() {
		return isRemote;
	}

	private String remoteUrl;
	private boolean isRemote;
	private String manufacturer;
	private String model;
	private String device;
	private String sdk;
	private String ipAddress;
	private String cpuAbi;
	private String serialNumber;
	private String RemoteAddr;

	/**
	 * @return the remoteAddr
	 */
	public String getRemoteAddr() {
		return RemoteAddr;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @return the sdk
	 */
	public String getSdk() {
		return sdk;
	}

	/**
	 * @return the ipaddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return the cpu_abi
	 */
	public String getCpuAbi() {
		return cpuAbi;
	}

	/**
	 * @return the remoteUrl
	 * @example t-taichan3.taobao.ali.com:8080/AthrunStudio/
	 */
	public String getRemoteUrl() {
		return remoteUrl;
	}

	/**
	 * @param remoteUrl
	 *            the remoteUrl to set
	 */
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	
	public void setRemoteAddr(String remoteAddr){
		this.RemoteAddr = remoteAddr;
	}

	public String getHref() {
		String shortPath = "remote.jsp?serialNumber=" + getSerialNumber();
		if (isRemote) {
			return "http://" + getRemoteUrl() + shortPath;
		} else {
			return shortPath;
		}
	}

	public String getJpg() {
		String shortPath = "JpgGen.jpg?ts=0&serialNumber="
				+ getSerialNumber();
		if (isRemote) {
			return "http://" + getRemoteUrl() + shortPath;
		} else {
			return shortPath;
		}
	}
}
