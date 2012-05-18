package org.athrun.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.athrun.server.service.RemoteDeviceManager;
import org.athrun.server.struts.Device;
import org.athrun.server.utils.PropertiesUtil;

/**
 * Servlet implementation class RemoteRegister
 */
public class RemoteRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @author taichan
	 * @see HttpServlet#HttpServlet()
	 */
	public RemoteRegister() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String remoteAddr = getHost(request);
		String localAddr = request.getLocalAddr();
		if (!remoteAddr.equals(localAddr)
				|| PropertiesUtil.getIncludeLocalMachine()) {
			String type = request.getParameter("type");
			if (type.equalsIgnoreCase("add")) {
				Device device = getDeviceFromRequest(request);
				RemoteDeviceManager.add(remoteAddr, device.getSerialNumber(),
						device);
			} else {
				if (type.equalsIgnoreCase("remove")) {
					String sn = request.getParameter("sn");
					RemoteDeviceManager.remove(remoteAddr, sn, true);
				} else {
					throw new RuntimeException("Not supported type");
				}
			}
		}
	}

	/**
	 * @see RemoteDeviceManager#register
	 * @param request
	 * @return
	 */
	private Device getDeviceFromRequest(HttpServletRequest request) {
		String manufacturer = request.getParameter("manufacturer");
		String model = request.getParameter("model");
		String devicePara = request.getParameter("device");
		String sdk = request.getParameter("sdk");
		String ipAddress = request.getParameter("ipAddress");
		String cpuAbi = request.getParameter("cpuAbi");
		String serialNumber = request.getParameter("sn");

		String url = getHost(request) + ":" + request.getParameter("port")
				+ "/" + request.getParameter("cp") + "/"; // 用ip地址换掉

		Device device = new Device(manufacturer, model, devicePara, sdk,
				ipAddress, cpuAbi, serialNumber, true);
		device.setRemoteUrl(url);
		device.setRemoteAddr(request.getRemoteHost());

		return device;
	}

	private String getHost(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;

	}
}
