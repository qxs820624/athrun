package org.athrun.ios;

import org.athrun.instruments.MySocket;

import junit.framework.TestCase;

public class IOSTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();

	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		MySocket.sendExit();

	}

}
