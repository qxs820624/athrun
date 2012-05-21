/**
 * 
 */
package org.athrun.server.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
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
	 * @param needMaintain 内部轮询检查不用maintain，外部请求需要maintain
	 */
	public static void remove(String remoteAddr, String sn, boolean needMaintain) {
		synchronized (remoteDeviceMap) {
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
	}

	/**
	 * 内部轮询检查，要求维护remoteDeviceMap
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
		uri.append("&").append("device=").append(device.getDevice());
		uri.append("&").append("ipAddress=").append(device.getIpAddress());
		uri.append("&").append("manufacturer=")
				.append(device.getManufacturer());
		uri.append("&").append("model=").append(encode(device.getModel()));
		uri.append("&").append("sdk=").append(device.getSdk());
		uri.append("&").append("sn=").append(device.getSerialNumber());
		uri.append("&").append("port=").append(PropertiesUtil.getPort());
		uri.append("&").append("cp=").append(PropertiesUtil.getContextPath());

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(RemoteRegisterUrl + uri.toString());
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			httpClient.execute(httpget, responseHandler);
		} catch (IOException e) {
			Log.w("RemoteDeviceRegister",
					"Can't reach the host: " + e.getMessage());
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
		} catch (UnknownHostException e) {
			Log.w("remoteRegister", "Can't reach the host: " + e.getMessage());
		} catch (HttpHostConnectException e) {
			Log.w("RemoteDeviceRegister",
					"Can't reach the host: " + e.getMessage());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
						Thread.sleep(5 * 60 * 1000); // 等 5 分钟
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					r.checkAll();
				}

			}
		},"check-devices").start();
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
				try {
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
					HttpConnectionParams.setSoTimeout(httpParams, 3000);
					DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParams);
					HttpGet httpget = new HttpGet(jpgUrl);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					httpClient.execute(httpget, responseHandler);
				} catch (SocketTimeoutException e) {
					Log.i("RemoteDeviceManager",
							"Read jpg timeout, remove the remote device.");
					remove(device);
				} catch (ConnectTimeoutException e) {
					Log.i("RemoteDeviceManager", e.getMessage()
							+ ", remove the remote device.");
					remove(device);
				} catch (IOException e) {
					e.printStackTrace();
					remove(device);
				}
			}
			checklist.clear();
		}
	}
}
