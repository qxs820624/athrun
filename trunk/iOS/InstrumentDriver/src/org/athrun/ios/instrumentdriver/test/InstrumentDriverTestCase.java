package org.athrun.ios.instrumentdriver.test;

import org.athrun.ios.instrumentdriver.MySocket;
import org.athrun.ios.instrumentdriver.RunType;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.athrun.ios.instrumentdriver.config.Config;
import org.athrun.ios.instrumentdriver.config.DriverUtil;
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
	private String appPath = DriverUtil.getApp();
	// private String appPath
	// ="/Users/athrun/Desktop/ios_cs/PNTB/build/Debug-iphonesimulator/PNTB.app";
	private Boolean isDebug = DriverUtil.isDebug();

	@Before
	public void setUp() throws Exception {
		// TODO Auto-generated method stub

		this.target = UIATarget.localTarget();
		this.app = target.frontMostApp();
		this.win = app.mainWindow();

		RunType.DEBUG = this.isDebug;

		String shellCmd = String.format("./JSLib/runTests.sh %s", appPath);
		String[] cmd = { "/bin/sh", "-c", shellCmd };
		Runtime.getRuntime().exec(cmd);
	}

	@After
	public void tearDown() throws Exception {
		// TODO Auto-generated method stub

		MySocket.sendExit();
		String[] cmd = { "/bin/sh", "-c", "rm -rf *.trace " };
		Runtime.getRuntime().exec(cmd);

	}
}