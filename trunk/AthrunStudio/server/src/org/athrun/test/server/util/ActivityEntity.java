package org.athrun.test.server.util;

public class ActivityEntity {
	private String name;
	private String launchMode;
	public ActivityEntity(String name, String launchMode) {
		super();
		this.name = name;
		this.launchMode = launchMode;
	}
	public String getName() {
		return name;
	}
	public String getLaunchMode() {
		return launchMode;
	}
}
