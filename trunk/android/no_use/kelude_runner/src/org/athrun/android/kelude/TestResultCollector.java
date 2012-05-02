package org.athrun.android.kelude;

import java.io.IOException;

public class TestResultCollector {
	private String device;
	private String resultPath;

	private static final String DEVICE_FILE_PATH = KeludeRunner.REPORT_FILE_DIR
			+ "/" + KeludeRunner.REPORT_FILE_NAME;

	public TestResultCollector(String device, String resultPath) {
		this.device = device;
		this.resultPath = resultPath;
	}

	String getJunitReport(String device) {

		try {
			String result = ShellCommandRunner
					.run(getReportCommand(this.resultPath));

			return result;

		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getReportPath() {
		StringBuilder filePath = new StringBuilder();
		filePath.append("adb");
		if (null == this.device) {
			filePath.append(" pull ").append(DEVICE_FILE_PATH);

		} else {
			filePath.append(" -s ").append(device).append(" pull ")
					.append(DEVICE_FILE_PATH);
		}

		return filePath.toString();
	}

	private String getReportCommand(String localPath) {
		StringBuilder command = new StringBuilder();
		command.append(getReportPath()).append(" ").append(localPath);

		return command.toString();
	}
}
