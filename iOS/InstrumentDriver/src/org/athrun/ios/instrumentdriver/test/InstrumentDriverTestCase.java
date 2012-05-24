package org.athrun.ios.instrumentdriver.test;

import junit.framework.TestCase;

import org.athrun.ios.instrumentdriver.MySocket;
import org.athrun.ios.instrumentdriver.RunType;

public class InstrumentDriverTestCase extends TestCase {

	private String appPath = "/Users/jerryding/taobao4iphone/build/Distribution-iphonesimulator/taobao4iphone.app";
	private Boolean isDebug = true;

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

		String cmd = String
				.format("/bin/bash instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate  %s -e UIASCRIPT ./JSLib/CSRunner.js -e UIARESULTSPATH /Athrun/log/",
						this.appPath);
	 
		Runtime.getRuntime().exec(cmd);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		MySocket.sendExit();
		Runtime.getRuntime().exec("rm -rf *.trace ");
	}
}
