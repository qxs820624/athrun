package org.athrun.ios.instrumentdriver.test;

import java.io.File;

import org.athrun.ios.instrumentdriver.MySocket;
import org.athrun.ios.instrumentdriver.RunType;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.athrun.ios.instrumentdriver.config.DriverUtil;
import org.athrun.ios.instrumentdriver.config.ResourceManager;
import org.junit.After;
import org.junit.Before;

/**
 * @author ziyu.hch
 * @author taichan
 */
public class InstrumentDriverTestCase {

	public UIATarget target;
	public UIAApplication app;
	public UIAWindow win;


	private String appPath = DriverUtil.getApp();

	private Boolean isDebug = DriverUtil.isDebug();

	@Before
	public void setUp() throws Exception {

		this.target = UIATarget.localTarget();
		this.app = target.frontMostApp();
		this.win = app.mainWindow();

		RunType.DEBUG = this.isDebug;
		
		ResourceManager.updateResource();
		
		String[] cmdrun = {"chmod", "777", String.format("%s", ResourceManager.getRunShell())};
		Runtime.getRuntime().exec(cmdrun);
		String[] cmdtcp = {"chmod", "777", String.format("%s", ResourceManager.getTcpShell())};
		Runtime.getRuntime().exec(cmdtcp);

		String shellCmd = String.format("%s %s %s", ResourceManager.getRunShell(), appPath, ResourceManager.getInstrumentRoot());

		String[] cmd = { "/bin/sh", "-c", shellCmd};
		Runtime.getRuntime().exec(cmd);
	}

	@After
	public void tearDown() throws Exception {

		MySocket.sendExit();
		String[] cmd = { "/bin/sh", "-c", "rm -rf *.trace " };
		Runtime.getRuntime().exec(cmd);

	}
}
