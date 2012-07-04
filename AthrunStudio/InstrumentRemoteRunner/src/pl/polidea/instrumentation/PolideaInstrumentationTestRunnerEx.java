package pl.polidea.instrumentation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class PolideaInstrumentationTestRunnerEx extends
		PolideaInstrumentationTestRunner implements TestCmdReceiver {
	
	// for test
	private static String packageName = "com.taobao.taobao";
	
	// for test
	private static String activityName = "com.taobao.tao.Welcome";
	
	private static String port = "1234";
	
	private static TcpServer testServer;
	
	private static boolean testScriptReady = false;
	
	private static boolean snapShotOver = false;
	
	private static List<String> testCmdList;

	public static String getPort() {
		return port;
	}

	public static String getPackageName() {
		return packageName;
	}

	public static String getActivityName() {
		return activityName;
	}
	
	public static boolean isTestScriptReady() {
		return testScriptReady;
	}

	public static void setTestScriptReady(boolean testScriptReady) {
		PolideaInstrumentationTestRunnerEx.testScriptReady = testScriptReady;
	}

	@Override
	public void onCreate(Bundle arguments) {
		if (arguments != null) {
			String port = arguments.getString("port");
			String packageName = arguments.getString("packageName");
			String activityName = arguments.getString("activityName");
			if (port != null) {
				PolideaInstrumentationTestRunnerEx.port = port;
			}
			if (packageName != null) {
				PolideaInstrumentationTestRunnerEx.packageName = packageName;
			}
			if (activityName != null) {
				PolideaInstrumentationTestRunnerEx.activityName = activityName;
			}
        }
		
		startTestServer();
		
		super.onCreate(arguments);
	}
	
	private void startTestServer() {
		if (port != null) {
			testServer = new TcpServer(port);
			testServer.setCmdReceiver(this);
			testServer.start();
		}
	}

	@Override
	public void onDestroy() {
		if (testServer != null) {
			testServer.stop();
		}
		
		super.onDestroy();
	}

	@Override
	public void receiveCmd(String cmd) {
		if (testCmdList == null) {
			testCmdList = new ArrayList<String>();
		}
		
		testCmdList.add(cmd);
	}
	
	public static String getTestCmds() {
		StringBuilder sb = new StringBuilder();
		for(String cmd : testCmdList) {
			sb.append(cmd);
			sb.append("\n");
		}
		
		testScriptReady = false;
		return sb.toString();
	}

	@Override
	public void receiveOver() {
		receiveCmd("quit");
		testScriptReady = true;
	}

	@Override
	public void snapShotOver() {
		setSnapShotOver(true);
	}

	public static boolean isSnapShotOver() {
		return snapShotOver;
	}

	public static void setSnapShotOver(boolean snapShotOver) {
		PolideaInstrumentationTestRunnerEx.snapShotOver = snapShotOver;
	}

	public static TcpServer getTestServer() {
		return testServer;
	}
	
}
