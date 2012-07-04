package org.athrun.remoterunner;

import android.util.Log;
import com.stuffwithstuff.Jasic;
import pl.polidea.instrumentation.PolideaInstrumentationTestRunnerEx;
import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;


public class RemoteTestRunner extends AthrunTestCase {

	/**
	 * Log tag.
	 */
	private static final String LOG_TAG = "RemoteTestRunner";
	
	private Jasic jasic;

	@Override
	protected void setUp() throws Exception {
		Log.i(LOG_TAG, "setUp()");
		super.setUp();
		
		jasic = new Jasic();
		jasic.setTestCase(this);

		Log.i(LOG_TAG, "setUp() finished");
	}

	@Override
	protected void tearDown() throws Exception {
		Log.i(LOG_TAG, "tearDown()");
		super.tearDown();
	}

	private static String getPkgName() {
		String packageName = PolideaInstrumentationTestRunnerEx.getPackageName();
		Log.i(LOG_TAG, "getPkgName: " + packageName);
		return packageName;
	}

	private static String getActivityClassStr() throws ClassNotFoundException {
		String activityName = PolideaInstrumentationTestRunnerEx.getActivityName();
		Log.i(LOG_TAG, "IN getActivityClassStr: " + activityName);
		return activityName;
	}

	public RemoteTestRunner() throws Exception {
		super(getPkgName(), getActivityClassStr());
	}

	@Test
	public void test() throws Exception {
		
		Log.i(LOG_TAG, "IN RemoteTestRunner::test()");
		
		// 获取命令序列，最多等待10秒
		String testScript = null;
		int TIMEOUT = 1000 * 10;
		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < startTime + TIMEOUT) {
			if (PolideaInstrumentationTestRunnerEx.isTestScriptReady()) {
				testScript = PolideaInstrumentationTestRunnerEx.getTestCmds();
				break;
			}
			
			// 每隔50毫秒检查是否已经获得定位信息
			try {
				Thread.sleep(50);
				continue;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (testScript != null) {
			// 解释执行传进来的测试脚本！
			Log.i(LOG_TAG, "testScript: " + testScript.replace("\n", ""));
			jasic.interpret(testScript);
		}
	}
}
