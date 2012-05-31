package org.athrun.ios.instrumentdriver.test;

import org.athrun.ios.instrumentdriver.MySocket;
import org.athrun.ios.instrumentdriver.RunType;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.junit.After;
import org.junit.Before;

/**
 * @author ziyu.hch
 * 
 */
public class InstrumentDriverTestCase {

	public UIATarget target;
	public UIAApplication app;
	public UIAWindow win;

	// private String appPath
	// ="/Users/jerryding/taobao4iphone/build/Distribution-iphonesimulator/taobao4iphone.app";
	private String appPath = "/Users/athrun/Desktop/TaoTest/build/Debug-iphonesimulator/TaoTest.app";
	// private String appPath
	// ="/Users/athrun/Desktop/ios_cs/PNTB/build/Debug-iphonesimulator/PNTB.app";
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

	@Before
	public void setUp() throws Exception {
		// TODO Auto-generated method stub

		this.target = UIATarget.localTarget();
		this.app = target.frontMostApp();
		this.win = app.mainWindow();

		RunType.DEBUG = this.isDebug;

		String cmd = String
				.format("/bin/bash instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate %s -e UIASCRIPT ./JSLib/CSRunner.js -e UIARESULTSPATH /Athrun/log/",
						this.appPath);

		Runtime.getRuntime().exec(cmd);
	}

	@After
	public void tearDown() throws Exception {
		// TODO Auto-generated method stub

		MySocket.sendExit();
		Runtime.getRuntime().exec("rm -rf *.trace ");
	}
}
