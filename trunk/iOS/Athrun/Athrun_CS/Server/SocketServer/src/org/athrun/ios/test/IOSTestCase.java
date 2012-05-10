package org.athrun.ios.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.athrun.ios.instruments.MySocket;
import org.athrun.ios.instruments.RunType;

public class IOSTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		RunType.DEBUG = false;
		
		try {
			Runtime.getRuntime()
					.exec("/bin/bash /Athrun/RunScript.sh /Athrun/CSRunner.js",
							null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		MySocket.sendExit();
	}
}
