package org.athrun.server.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.athrun.client.AthrunMsg;
import org.athrun.client.AthrunSlaveClient;
import org.athrun.server.struts.Device;

public class MsgProcessor {
	
	public static void notifyOnlineDevices(List<Device> onlineDevices) {
		if (onlineDevices != null) {
			for(Device device : onlineDevices) {
				AthrunMsg msg = new AthrunMsg();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("key", "device");
				jsonObject.put("manufacturer", device.getManufacturer());
				jsonObject.put("model", device.getModel());
				jsonObject.put("device", device.getDevice());
				jsonObject.put("sdk", device.getSdk());
				jsonObject.put("ipAddress", device.getIpAddress());
				jsonObject.put("cpuAbi", device.getCpuAbi());
				jsonObject.put("serialNumber", device.getSerialNumber());
				jsonObject.put("remoteAddr", device.getRemoteAddr());
				jsonObject.put("solution", device.getSolution());
	
				msg.setInfo(jsonObject.toString());
				
				AthrunSlaveClient.sendAthrunMsg(msg);
			}
		}
	}
	
}
