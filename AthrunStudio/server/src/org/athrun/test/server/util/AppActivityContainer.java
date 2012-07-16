package org.athrun.test.server.util;

import java.util.ArrayList;
import java.util.List;

public class AppActivityContainer {
	private String appName;
	private String appIconPath;
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppIconPath() {
		return appIconPath;
	}

	public void setAppIconPath(String appIconPath) {
		this.appIconPath = appIconPath;
	}

	private List<ActivityEntity> allActivity = new ArrayList<ActivityEntity>();
	
	public void addActivityEntity(ActivityEntity ae) {
		this.allActivity.add(ae);
	}

	public List<ActivityEntity> getAllActivity() {
		return allActivity;
	}
}
