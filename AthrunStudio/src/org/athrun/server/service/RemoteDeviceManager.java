/**
 * 
 */
package org.athrun.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.athrun.server.log.Log;
import org.athrun.server.servlet.RemoteRegister;
import org.athrun.server.struts.Device;
import org.athrun.server.utils.OneParameterRunnable;
import org.athrun.server.utils.PropertiesUtil;

/**
 * @author taichan
 * 
 */
public class RemoteDeviceManager {

	static final int UpdateStatusTime = 2 * 60 * 1000;
	static final int CheckStatusTime = 5 * 60 * 1000;

	private static String RemoteRegisterUrl = "http://kelude.taobao.net/athrun/RemoteRegister";

	static Map<String, Map<String, Device>> remoteDeviceMap = new HashMap<String, Map<String, Device>>();

	public static List<Device> getDevices() {
		ArrayList<Device> deviceList = new ArrayList<Device>();
		for (String ip : remoteDeviceMap.keySet()) {
			for (String serialNumber : remoteDeviceMap.get(ip).keySet()) {
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
				maintain(remoteAddr, sn, list.get(sn));
			}
		}
	}

	/**
	 * @param remoteAddr
	 * @param sn
	 * @param device
	 *            一个已有的Device的引用，不然没有效果。
	 */
	private static void maintain(String remoteAddr, String sn, Device device) {
		RemoteDeviceManager.getInstance().maintain(device);
	}

	/**
	 * @param remoteAddr
	 * @param sn
	 * @param needMaintain
	 *            内部轮询检查不用maintain，外部请求需要maintain
	 */
	public static void remove(String remoteAddr, String sn, boolean needMaintain) {

		Map<String, Device> list = remoteDeviceMap.get(remoteAddr);
		if (list != null) {
			Device device = list.remove(sn);
			if (needMaintain) {
				if (device != null) {
					maintain(remoteAddr, sn, device);
				}
			}
		}

	}

	/**
	 * 内部轮询检查，要求维护remoteDeviceMap
	 * 
	 * @param device
	 */
	private void remove(Device device) {
		remove(device.getRemoteAddr(), device.getSerialNumber(), false);
	}

	/**
	 * @see RemoteRegister#getDeviceFromRequest
	 * 
	 */
	public static void register(Device device) {

		StringBuilder uri = new StringBuilder();
		uri.append("?").append("type=").append("add");
		uri.append("&").append("cpuAbi=").append(device.getCpuAbi());
		uri.append("&").append("device=").append(encode(device.getDevice()));
		uri.append("&").append("ipAddress=").append(device.getIpAddress());
		uri.append("&").append("manufacturer=")
				.append(encode(device.getManufacturer()));
		uri.append("&").append("model=").append(encode(device.getModel()));
		uri.append("&").append("sdk=").append(device.getSdk());
		uri.append("&").append("sn=").append(device.getSerialNumber());
		uri.append("&").append("port=").append(PropertiesUtil.getPort());
		uri.append("&").append("cp=").append(PropertiesUtil.getContextPath());
		uri.append("&")
				.append("sl=")
				.append(encode(CaptureManager.getCaptureInfo(
						device.getSerialNumber()).getSolution()));

		httpGet(RemoteRegisterUrl + uri.toString());

	}

	public static boolean httpGet(String uri) {
		StringBuilder sb = new StringBuilder();
		BufferedReader bin = null;
		try {
			URL url = new URL(uri);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			InputStream in = connection.getInputStream();
			bin = new BufferedReader(new InputStreamReader(in));
			String s = null;
			while ((s = bin.readLine()) != null) {
				sb.append(s);
			}
			if (sb.length() == 0) {
				return false;
			}
		} catch (IOException e) {
			Log.w("RemoteDeviceRegister",
					"Can't reach the host: " + e.getMessage());
			return false;
		} finally {
			try {
				bin.close();
			} catch (Exception e) {
			}
		}
		return true;
	}

	private static String encode(String content) {
		if (content == null) {
			return null;
		}
		try {
			return URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static void unRegister(Device device) {
		StringBuilder uri = new StringBuilder();
		uri.append("?").append("type=").append("remove");
		uri.append("&").append("sn=").append(device.getSerialNumber());

		httpGet(RemoteRegisterUrl + uri.toString());
	}

	private void RemoteDeviceManager() {

	}

	private void startLoop() {
		new Thread(new OneParameterRunnable(this) {
			@Override
			public void run() {
				RemoteDeviceManager r = (RemoteDeviceManager) getParameter();
				while (true) {
					r.addAll();
					try {
						Thread.sleep(RemoteDeviceManager.CheckStatusTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					r.checkAll();
				}

			}
		}, "check-devices").start();
	}

	/**
	 * 
	 */
	protected void addAll() {
		synchronized (checklist) {
			checklist.addAll(getDevices());
		}
	}

	/**
	 * 只要收到 add 或 remote，就把列表清掉
	 * 
	 * @param device
	 */
	protected void maintain(Device device) {
		synchronized (checklist) {
			checklist.remove(device);
		}
	}

	private List<Device> checklist = new ArrayList<Device>();
	private static RemoteDeviceManager instance;
	private static Object lockobj = new Object();

	public static RemoteDeviceManager getInstance() {
		synchronized (lockobj) {
			if (instance == null) {
				instance = new RemoteDeviceManager();
				instance.startLoop();
			}
		}
		return instance;
	}

	protected void checkAll() {
		synchronized (checklist) {
			for (Device device : checklist) {
				String jpgUrl = device.getJpg();

				if (!httpGet(jpgUrl)) {
					remove(device);
				}
			}
			checklist.clear();
		}
	}
}
