package org.athrun.android.kelude;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class KeludeRunner {
	private static final String RUNNER = "com.zutubi.android.junitreport.JUnitReportTestRunner";

	private String[] commands;
	private Map<String, String> commandMap = new HashMap<String, String>();

	private String testClassName;
	private String testMethodName;
	
	private String device;
	private String appPackageName;
	private String testPackageName;
	private String resultPath;

	public KeludeRunner(String[] commands) {
		this.commands = commands;
		init();
		getTestInfo();
	}

	private void init() {
		for (int i = 0; i < commands.length; i = i + 2) {
			commandMap.put(commands[i].replace("-", ""), commands[i + 1]);
		}
	}

	private String getInstCommand() {
		StringBuilder instCommand = new StringBuilder();
		instCommand.append("adb -s ").append(getDeviceName())
				.append(" shell am instrument -w -e class ")
				.append(getTestClassName()).append("#")
				.append(getTestMethodName()).append(" ")
				.append(getTestPackageName()).append("/")
				.append(getTestRunnerName());
		return instCommand.toString();
	}

	private String getDeviceName() {
		this.device = this.commandMap.get("deviceName");
		return this.device;
	}

	private void getTestInfo() {
		String testInfo = this.commandMap.get("method");
		String[] args = testInfo.split("\\.");

		this.testMethodName = args[args.length - 1];
		this.testClassName = testInfo.replace(("." + testMethodName), "");
	}

	private String getTestClassName() {
		return this.testClassName;
	}

	private String getTestMethodName() {
		return this.testMethodName;
	}

	private String getTestPackageName() {
		this.testPackageName = this.commandMap.get("testPackageName");
		return this.testPackageName;
	}
	
	private String getPackageName() {
		this.appPackageName = this.commandMap.get("packageName");
		return this.appPackageName;
	}

	private String getTestRunnerName() {
		return RUNNER;
	}

	private String getInfo(Process process) throws Exception {
		StringBuilder output = new StringBuilder();
		BufferedReader info = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		BufferedReader error = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		process.waitFor();

		String line = null;

		while ((line = info.readLine()) != null) {
			if (line.trim().length() > 0) {
				output.append(line).append("\n");
			}
		}

		while ((line = error.readLine()) != null) {
			if (line.trim().length() > 0) {
				output.append(line).append("\n");
			}
		}

		return output.toString();
	}

	private String getLocalReportPath() {
		this.resultPath = this.commandMap.get("results_file");
		return this.resultPath;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2 || args.length % 2 != 0) {
			System.out.println("Incorrect args.");
			System.out.println("Usage:");
			System.out
					.println("- device [device] -method [testMethodName] -packageName [testPackageName] -results_file [localpath]");

		} else {
			KeludeRunner runner = new KeludeRunner(args);
			System.out.println("Run command: " + runner.getInstCommand());
			Process process = Runtime.getRuntime()
					.exec(runner.getInstCommand());
			
            String testInfo = runner.getInfo(process);
			System.out.println(testInfo);
			
			if (!testInfo.contains("Time")) {
				throw new RuntimeException(testInfo);
			} 
			
			TestResultCollector resultCollector = new TestResultCollector(runner.device, runner.testPackageName, runner.getPackageName(), runner.getLocalReportPath());
			String result = resultCollector.getJunitReport(runner.device);
			System.out.println(result);
			System.exit(0);
		}
	}
}
