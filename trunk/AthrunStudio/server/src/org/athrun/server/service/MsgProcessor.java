package org.athrun.server.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
	
	public static void sendMonkeyResult(String serialNumber, String testcaseName, String snapshotsPath) {
		beginSendSnapshots(serialNumber);
		
		File snapshotsDir = new File(snapshotsPath);
		if (snapshotsDir.exists()) {
			if (snapshotsDir.isDirectory()) {
				File[] snapshots = snapshotsDir.listFiles();
				for(File snapshot : snapshots) {
					sendSnapshot(serialNumber, testcaseName, snapshot);
				}
			}
		}
		
		endSendSnapshots(serialNumber);
	}
	
	private static void beginSendSnapshots(String serialNumber) {
		AthrunMsg msg = new AthrunMsg();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", "result-begin");
		jsonObject.put("serialNumber", serialNumber);
		msg.setInfo(jsonObject.toString());
		
		AthrunSlaveClient.sendAthrunMsg(msg);
	}
	
	private static void endSendSnapshots(String serialNumber) {
		AthrunMsg msg = new AthrunMsg();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", "result-end");
		jsonObject.put("serialNumber", serialNumber);
		msg.setInfo(jsonObject.toString());
		
		AthrunSlaveClient.sendAthrunMsg(msg);
	}
	
	private static void sendSnapshot(String serialNumber, String testcaseName, File snapshotFile) {
		AthrunMsg msg = new AthrunMsg();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", "result");
		jsonObject.put("serialNumber", serialNumber);
		jsonObject.put("testcaseName", testcaseName);
		jsonObject.put("fileName", snapshotFile.getName());
		msg.setInfo(jsonObject.toString());
		
		msg.setPicture(true);
		
		int fileLength = 0;
		// 这里的代码还有点问题
		byte[] pictureContent = null;
		
		InputStream in = null;
		try {
			System.out.println("snapshotFile path: " + snapshotFile.getAbsolutePath());
			in = new BufferedInputStream(new FileInputStream(snapshotFile));
			fileLength = in.available();
			long leng2 = snapshotFile.length();
			System.out.println("@@@@@$ leng2: " + leng2);
			
			pictureContent = new byte[fileLength];
			int len = in.read(pictureContent);
			System.out.println("@@@@@@@@@@@@$$$$$$$$$ picture len: " + len + ", fileLength: " + fileLength);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		msg.setPictureContent(pictureContent);

		AthrunSlaveClient.sendAthrunMsg(msg);
	}
	
}
