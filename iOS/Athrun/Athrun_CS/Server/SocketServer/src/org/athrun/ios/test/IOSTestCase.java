package org.athrun.ios.test;

import junit.framework.TestCase;

import org.athrun.ios.instruments.MySocket;
import org.athrun.ios.instruments.RunType;

public class IOSTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		RunType.DEBUG = false;

		Runtime.getRuntime().exec(
				"/bin/bash /Athrun/RunScript.sh /Athrun/CSRunner.js", null);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		MySocket.sendExit();
	}
}
