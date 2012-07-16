package org.athrun.test.server.util;

import java.util.HashMap;
import java.util.Map;

public class ActivityManager {
	private Map<String, AppActivityContainer> map = new HashMap<String, AppActivityContainer>();
	
	private static final ActivityManager am = new ActivityManager();
	
	private ActivityManager() {
		
	}
	
	public void put(String serialNumber, AppActivityContainer aac) {
		map.put(serialNumber, aac);
	}
	
	public AppActivityContainer getAppActivityContainer(String serialNumber) {
		return map.get(serialNumber);
	}
	
	public static ActivityManager getInstance() {
		return am;
	}
}
