package org.athrun.android.kelude;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.athrun.android.result.JunitKeludeLogConverter;

public final class KeludeRunner {
	private static final String RUNNER = "pl.polidea.instrumentation.PolideaInstrumentationTestRunner";
	private static final String DEFAULT_RESULT_FILE = "./res/default_result.xml";

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

				.append(" -e junitOutputDirectory ").append(REPORT_FILE_DIR)
				.append(" -e junitSplitLevel ").append("none")

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

	private void clean() throws Exception {
		ShellCommandRunner.run("adb shell rm " + REPORT_FILE_DIR + "/" + REPORT_FILE_NAME);
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

			System.out.println("Delete old result files...");
			runner.clean();

			System.out.println("Run command: " + runner.getInstCommand());
			String testInfo = ShellCommandRunner.run(runner.getInstCommand());
			System.out.println("Test run finished.");
			System.out.println(testInfo);

			if (!testInfo.contains("Time")) {
				FileUtils.writeStringToFile(new File(resultFile
						+ "/exception.log"), testInfo, "UTF-8");
			} 

			TestResultCollector resultCollector = new TestResultCollector(
					runner.device, runner.resultPath);
			String result = resultCollector.getJunitReport(runner.device);

			if (result.contains("KB/s")) {
				System.out.println("Pull file result: ");
				System.out.println(result);

			} else {
				System.out.println("Exception");
				System.out.println("Copy default file to " + resultFile
						+ "\\default_result.xml");
				FileUtils.copyFile(new File(DEFAULT_RESULT_FILE), new File(runner.resultPath));
			}

			convertJunitToKeludeReport(runner.getLocalReportPath());			
			System.exit(0);
		}
	}

	/**
	 * @param localReportPath
	 * @throws IOException
	 */
	private static void convertJunitToKeludeReport(String localReportPath)
			throws IOException {
		// TODO Auto-generated method stub
		File tmpFile = new File("c:/AthrunLog/tmp.xml");
		File local = new File(localReportPath);
		InputStream is = new FileInputStream(local);
		FileOutputStream fo = new FileOutputStream(tmpFile);
		JunitKeludeLogConverter.convert(is, fo);
		is.close();
		fo.close();
		FileUtils.copyFile(tmpFile, local);		
	}
}
