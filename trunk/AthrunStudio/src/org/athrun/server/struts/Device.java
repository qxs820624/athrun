/**
 * 
 */
package org.athrun.server.struts;

import org.athrun.ddmlib.IDevice;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author taichan
 * 
 */
public class Device {
	/**
	 * @param iDevice
	 */
	public Device(IDevice iDevice) {
		// TODO Auto-generated constructor stub
		this.manufacturer = iDevice.getProperty("ro.product.manufacturer");
		this.model = iDevice.getProperty("ro.product.model");
		this.device = iDevice.getProperty("ro.product.device");
		this.sdk = iDevice.getProperty("ro.build.version.sdk");
		this.ipAddress = iDevice.getProperty("dhcp.eth0.ipaddress");
		this.cpuAbi = iDevice.getProperty("ro.product.cpu.abi");
		this.serialNumber = iDevice.getSerialNumber();
	}

	private String manufacturer;
	private String model;
	private String device;
	private String sdk;
	private String ipAddress;
	private String cpuAbi;
	private String serialNumber;

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

}
