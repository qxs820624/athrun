/* Athrun - Android automation testing Framework.
 Copyright (C) 2010-2012 TaoBao UI AutoMan Team

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., HuaXing road, Hangzhou,China. 
 Email:taichan@taobao.com,shidun@taobao.com,bingyang.djj@taobao.com
 */
package org.athrun.android.framework.client;

import org.apache.log4j.Logger;

public class Athrun {
	private final Logger logger = Logger.getLogger(getClass());

	private static final String USAGE = "USAGE:";

	private String testProjectRoot;
	private String deviceSerial;
	private String testClassName;
	private String testInfoFilesPath;

	private String[] args;
	
	private static final String DEFAULT_TESTINFO_FILES_PATH = "c:\\AthrunTestInfo\\";

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println(USAGE);

		} else {
			new Athrun().run(args);
		}
	}

	private void run(String...args) {
		this.args = args;
		
		logger.info("Athrun begin.");

		if (this.args[0].equalsIgnoreCase("debug")) {
			logger.info("Debug mode, only the communication function is available. Press Ctrl + C to exit.");
			AthrunClientThread.start(null);

		} else {
			initArgs();
			logger.info("Init finished.");
			runTests();
		}
	}
	
	private void initArgs() {
		this.testProjectRoot = args[0];
		this.deviceSerial = args[1];
		this.testClassName = args[2];
		this.testInfoFilesPath = args.length > 3 ? args[3] : DEFAULT_TESTINFO_FILES_PATH;
	}
	
	private void runTests() {
		this.logger.info("Start to run tests.");
		
		this.logger.info("Run tests finished.");
	}
}
