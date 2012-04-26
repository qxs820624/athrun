package org.athrun.android.kelude;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class KeludeRunner {
	private static final String RUNNER = "pl.polidea.instrumentation.PolideaInstrumentationTestRunner";

	static final String REPORT_FILE_DIR = "/sdcard/kelude";
	static final String REPORT_FILE_NAME = "junitReport.xml";

	private String[] commands;
	private Map<String, String> commandMap = new HashMap<String, String>();

	private String testClassName;
	private String testMethodName;

	private String device;
	private String testPackageName;
	private String resultPath;

	KeludeRunner(String[] commands) {
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
				.append(" shell am instrument -w")
				.append(" -e junitOutpDirectory ").append(REPORT_FILE_DIR)
				.append(" -e junitSplitLevel none")
				.append(" -e junitSingleFileName ").append(REPORT_FILE_NAME)
				.append(" -e class ").append(getTestClassName()).append("#")
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

	private String getTestRunnerName() {
		return RUNNER;
	}

	private String getLocalReportPath() {
		this.resultPath = this.commandMap.get("results_file");
		return this.resultPath;
	}

	private String getFileRoot(String path) {
		String[] dirStrings = path.split("/");

		String linuxPath = path.replace(dirStrings[dirStrings.length - 1], "");
		String windowsPath = linuxPath.replace("/", "\\");
		return windowsPath;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2 || args.length % 2 != 0) {
			System.out.println("Incorrect args.");
			System.out.println("Usage:");
			System.out
					.println("- device [device] -method [testMethodName] -testPackageName [testPackageName] -results_file [localpath]");

		} else {
			KeludeRunner runner = new KeludeRunner(args);
			System.out.println("Check if need to mkdirs()");
			File resultFile = new File(runner.getFileRoot(runner
					.getLocalReportPath()));
			if (!resultFile.exists()) {
				resultFile.mkdirs();
			}

			System.out.println("Run command: " + runner.getInstCommand());
			String testInfo = ShellCommandRunner.run(runner.getInstCommand());
			System.out.println("Test run finished.");

			if (!testInfo.contains("Time")) {
				throw new RuntimeException(testInfo);

			} else {
				System.out.println(testInfo);
			}

			TestResultCollector resultCollector = new TestResultCollector(
					runner.device, runner.resultPath);
			String result = resultCollector.getJunitReport(runner.device);
			System.out.println(result);
			System.exit(0);
		}
	}
}
