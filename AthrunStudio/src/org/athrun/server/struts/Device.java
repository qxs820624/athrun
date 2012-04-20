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
		this.ipaddress = iDevice.getProperty("dhcp.eth0.ipaddress");
		this.cpu_abi = iDevice.getProperty("ro.product.cpu.abi");
	}

	private String manufacturer;
	private String model;
	private String device;
	private String sdk;
	private String ipaddress;
	private String cpu_abi;

}
