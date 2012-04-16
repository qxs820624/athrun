/**
 * 
 */
package org.athrun.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author taichan
 * 
 */
public class ForwardPortManager {
	static int capturePortMin = 5678;
	static int capturePortMax = 5778;

	static int eventPortMin = 5324;
	static int eventPortMax = 5424;

	static Map<String, PortBean> portMap = new HashMap<String, PortBean>();

	/**
	 * ����Ѿ����ˣ�����ʵ�ʶ˿ڡ� ��������ˣ�������ö˿ڣ���ռ�á�
	 * 
	 * @param serialNumber
	 * @return
	 * @throws ReservedPortExhaust
	 */
	public static int getCapturePort(String serialNumber)
			throws ReservedPortExhaust {
		if (portMap.containsKey(serialNumber)) {
			return portMap.get(serialNumber).getCapturePort();
		} else {
			return findMinAvailableValueFormMap(serialNumber).getCapturePort();
		}
	}

	public static int getEventPort(String serialNumber)
			throws ReservedPortExhaust {
		if (portMap.containsKey(serialNumber)) {
			return portMap.get(serialNumber).getEventPort();
		} else {
			return findMinAvailableValueFormMap(serialNumber).getEventPort();
		}
	}

	/**
	 * ������ö˿ڣ���ռ�á�
	 * 
	 * @param intMap
	 * @param min
	 * @param max
	 * @param serialNumber
	 * @throws ReservedPortExhaust
	 */
	private static PortBean findMinAvailableValueFormMap(String serialNumber)
			throws ReservedPortExhaust {
		int actualCapturePort = 0;
		int actualEventPort = 0;
		synchronized (portMap) {
			if (!portMap.containsKey(serialNumber)) {
				for (int i = capturePortMin; i <= capturePortMax; i++) {
					if (!portMap.containsKey(i)) {
						actualCapturePort = i;
						break;
					}
				}
				if (actualCapturePort == 0) {
					throw new ReservedPortExhaust();
				}
				for (int i = eventPortMin; i <= eventPortMax; i++) {
					if (!portMap.containsKey(i)) {
						actualEventPort = i;
						break;
					}
				}
				if (actualEventPort == 0) {
					throw new ReservedPortExhaust();
				}
				portMap.put(serialNumber, new PortBean(actualEventPort,
						actualCapturePort, serialNumber));
			}
		}
		return portMap.get(serialNumber);
	}

	public static void remorePort(String serialNumber) {
		synchronized (portMap) {
			portMap.remove(serialNumber);
		}
	}
}
