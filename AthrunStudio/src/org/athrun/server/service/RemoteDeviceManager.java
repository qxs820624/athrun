/**
 * 
 */
package org.athrun.server.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.athrun.server.struts.Device;
import org.athrun.server.utils.PropertiesUtil;

/**
 * @author taichan
 * 
 */
public class RemoteDeviceManager {

	private static String RemoteRegisterUrl = "http://t-taichan3.taobao.ali.com:8080/AthrunStudio/RemoteRegister";

	static Map<String, Map<String, Device>> remoteDeviceMap = new HashMap<String, Map<String, Device>>();

	public static List<Device> getDevices(){
		ArrayList<Device> deviceList = new ArrayList<Device>();
		for (String ip : remoteDeviceMap.keySet()) {
			for(String serialNumber : remoteDeviceMap.get(ip).keySet())
			{
				deviceList.add(remoteDeviceMap.get(ip).get(serialNumber));
			}
		}
		return deviceList;
	}
	
	/**
	 * @param remoteAddr
	 * @param sn
	 * @param url
	 */
	public static void add(String remoteAddr, String sn, Device device) {
		synchronized (remoteDeviceMap) {
			Map<String, Device> list = remoteDeviceMap.get(remoteAddr);
			if (list == null) {
				list = new HashMap<String, Device>();
			}
			if (!list.containsKey(sn)) {
				list.put(sn, device);
				remoteDeviceMap.put(remoteAddr, list);
			} else {
				maintain(remoteAddr, sn);
			}
		}
	}

	/**
	 * @param remoteAddr
	 * @param sn
	 */
	private static void maintain(String remoteAddr, String sn) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param remoteAddr
	 * @param sn
	 */
	public static void remove(String remoteAddr, String sn) {
		synchronized (remoteDeviceMap) {
			Map<String, Device> list = remoteDeviceMap.get(remoteAddr);
			if (list != null) {
				list.remove(sn);
			}
		}
	}

	private static String getShortUrl() {
		if (shortUrl == null) {
			shortUrl = PropertiesUtil.getHost() + ":"
					+ PropertiesUtil.getPort() + "/"
					+ PropertiesUtil.getContextPath() + "/";
		}
		return shortUrl;
	}

	private static String shortUrl;

	/*
	 * @see RemoteRegister#getDeviceFromRequest
	 */
	public static void register(Device device)
			throws UnsupportedEncodingException {
		device.setRemoteUrl(getShortUrl());

		StringBuilder uri = new StringBuilder();
		uri.append("?").append("type=").append("add");
		uri.append("&").append("cpuAbi=").append(device.getCpuAbi());
		uri.append("&").append("device=").append(device.getDevice());
		uri.append("&").append("ipAddress=").append(device.getIpAddress());
		uri.append("&").append("manufacturer=")
				.append(device.getManufacturer());
		uri.append("&").append("model=").append(encode(device.getModel()));
		uri.append("&").append("url=")
				.append(encode(device.getRemoteUrl()));
		uri.append("&").append("sdk=").append(device.getSdk());
		uri.append("&").append("sn=").append(device.getSerialNumber());

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(RemoteRegisterUrl + uri.toString());
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			httpClient.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String encode(String content) {
		try {
			return URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void unRegister(Device device) {
		StringBuilder uri = new StringBuilder();
		uri.append("?").append("type=").append("remove");
		uri.append("&").append("sn=").append(device.getSerialNumber());

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(RemoteRegisterUrl + uri.toString());
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			httpClient.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
