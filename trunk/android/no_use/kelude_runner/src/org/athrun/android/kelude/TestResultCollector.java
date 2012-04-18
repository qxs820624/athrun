package org.athrun.android.kelude;

import java.io.File;
import java.io.IOException;

import javax.management.RuntimeErrorException;

public class TestResultCollector {
	private String device;
	private String testPackage;
	private String appPackage;
	private String resultPath;

//	private static final String JUNIT_RESULT_PATH = "c:\\AthrunLog\\junit-report.xml";

	public TestResultCollector(String device, String testPackage,
			String appPackage, String resultPath) {
		this.device = device;
		this.testPackage = testPackage;
		this.appPackage = appPackage;
		this.resultPath = resultPath;
	}
	
	private String getFileRoot(String path) {
		String[] dirStrings = path.split("\\\\");
		
		return path.replace(dirStrings[dirStrings.length  - 1], "");
	}

	public String getJunitReport(String device) {
		File resultFile = new File(getFileRoot(this.resultPath));
		resultFile.mkdirs();
		
		try {
			String result = ShellCommandRunner.run(getReportCommand(
					this.appPackage, this.resultPath));
			if (result.contains("does not exist")) {
				result = ShellCommandRunner.run(getReportCommand(
						this.testPackage, this.resultPath));
			}
			
			return result;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Can not pull the file from device to local.");
	}

	private String getReportPath(String mobilePath) {
		StringBuilder filePath = new StringBuilder();
		filePath.append("adb");
		if (null == this.device) {
			filePath.append(" pull /data/data/");
			filePath.append(mobilePath);
			filePath.append("/files/junit-report.xml");

		} else {
			filePath.append(" -s ").append(device).append(" pull /data/data/")
					.append(mobilePath).append("/files/junit-report.xml");
		}

		return filePath.toString();
	}

	private String getReportCommand(String mobilePath, String localPath) {
		StringBuilder command = new StringBuilder();
		command.append(getReportPath(mobilePath)).append(" ").append(localPath);

		return command.toString();
	}
//
//	public static void main(String[] args) {
//		TestResultCollector collector = new TestResultCollector(null,
//				"org.athrun.android.test", "org.athrun.android.app");
//		collector.getJunitReport(null);
//	}
}
