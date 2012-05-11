package org.athrun.ios.test;

import junit.framework.TestCase;

import org.athrun.ios.instruments.MySocket;
import org.athrun.ios.instruments.RunType;

public class IOSTestCase extends TestCase {

	private String appPath = "/Users/jerryding/taobao4iphone/build/Distribution-iphonesimulator/taobao4iphone.app";
	private Boolean isDebug = false;

	public String getAppPath() {
		return appPath;
	}

	/**
	 * set the test app's file path.
	 * 
	 * @param appPath
	 */
	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public Boolean getIsDebug() {
		return isDebug;
	}

	public void setIsDebug(Boolean isDebug) {
		this.isDebug = isDebug;
	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		RunType.DEBUG = this.isDebug;

		Runtime.getRuntime().exec(
				"/bin/bash /Athrun/RunScript.sh " + this.appPath, null);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		MySocket.sendExit();
	}
}
