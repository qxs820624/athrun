package org.athrun.ios.instrumentdriver.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.athrun.ios.instrumentdriver.MySocket;
import org.athrun.ios.instrumentdriver.RunType;
import org.athrun.ios.instrumentdriver.UIAApplication;
import org.athrun.ios.instrumentdriver.UIATarget;
import org.athrun.ios.instrumentdriver.UIAWindow;
import org.athrun.ios.instrumentdriver.config.DriverUtil;
import org.athrun.ios.instrumentdriver.config.ResourceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

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
	private String[] cmd = {};
	Process proc = null;

	@BeforeClass
	public static void prepareResource() throws Exception {
		ResourceManager.updateResource();

		String[] cmdrun = { "chmod", "777",
				String.format("%s", ResourceManager.getRunShell()) };
		Runtime.getRuntime().exec(cmdrun);
		String[] cmdtcp = { "chmod", "777",
				String.format("%s", ResourceManager.getTcpShell()) };
		Runtime.getRuntime().exec(cmdtcp);
	}

	@Before
	public void setUp() throws Exception {

		this.target = UIATarget.localTarget();
		this.app = target.frontMostApp();
		this.win = app.mainWindow();

		RunType.DEBUG = this.isDebug;

		String shellCmd = String.format("%s %s %s",
				ResourceManager.getRunShell(), appPath,
				ResourceManager.getInstrumentRoot());

		this.cmd = new String[] { "/bin/sh", "-c", shellCmd };

		System.out.println("shellCmd:\t" + shellCmd);

		proc = Runtime.getRuntime().exec(cmd);
		Thread instrument = new Thread(new StartInstruments(), "instrument");
		instrument.setPriority(6);
		instrument.start();

	}

	@After
	public void tearDown() throws Exception {

		MySocket.sendExit();

		proc.waitFor();
		proc.destroy();

		String[] cmd = { "/bin/sh", "-c", "rm -rf *.trace " };
		Process pro = Runtime.getRuntime().exec(cmd);
		pro.waitFor();
		pro.destroy();

	}

	class StartInstruments implements Runnable {

		@Override
		public void run() {

			try {
				InputStream input = proc.getInputStream();
				InputStreamReader inputReader;

				inputReader = new InputStreamReader(input, "UTF-8");

				BufferedReader bufferR = new BufferedReader(inputReader);

				String str = null;

				while ((str = bufferR.readLine()) != null) {

					System.err.println("Instruments:\t" + str);
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
