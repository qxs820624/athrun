/**
 * 
 */
package org.athrun.server.struts;

import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author taichan
 * 
 */
public class Devices {
	List<Device> devices = new ArrayList<Device>();
	public void add(Device device){
		this.devices.add(device);
	}
}
