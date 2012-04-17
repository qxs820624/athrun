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
package org.athrun.android.framework;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.athrun.android.framework.special.taobaoview.SkuOptionElement;
import org.athrun.android.framework.utils.AthrunConnectorThread;
import org.athrun.android.framework.viewelememt.IViewElement;
import org.athrun.android.framework.viewelememt.TextViewElement;
import org.athrun.android.framework.viewelememt.ToastElement;
import org.athrun.android.framework.viewelememt.ViewElement;
import org.athrun.android.framework.webview.WebViewElement;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;


/**
 * Extends this class to use Athrun. If your app overrides onDestroy() method of
 * the main Activity, and adds code like this:
 * <p>
 * android.os.Process.killProcess(android.os.Process.myPid()); </br>
 * <p>
 * You will encounter a process crash error when you run your test, to resolve
 * this, override the tearDown() method in your test class which extends form
 * TmtsTestCase, just make it to be an empty method and run test methods one by
 * one.
 * 
 * @author bingyang.djj
 * 
 */
@SuppressWarnings("rawtypes")
public class AthrunTestCase extends ActivityInstrumentationTestCase2 {

	/**
	 * Log tag.
	 */
	private static final String LOG_TAG = "AthrunTestCase";
	
	private final Logger logger = Logger.getLogger(AthrunTestCase.class);

	/**
	 * {@link Athrun}.
	 */
	private Athrun athrun;
	
	/**
	 * {@link Instrumentation}.
	 */
	private Instrumentation inst;

	private static int maxTimeToFindView = IViewElement.ANR_TIME;
	
	// andsun add
	private Activity ac ;
	private String activityName;
	private ActivityManager activityManager ;
	
	/**
	 * Constructor of {@link AthrunTestCase}.
	 * 
	 * @param pkg
	 *            Package name of the app under test.
	 * @param activityClass
	 *            First {@link Activity} to start.
	 * @throws Exception
	 *             Exception.
	 */
	@SuppressWarnings("unchecked")
	public AthrunTestCase(String pkg, String activityClass) throws Exception {
		super(pkg, Class.forName(activityClass));
	}

	/**
	 * Return an instance of {@link Athrun}.
	 * 
	 * @return An instance of {@link Athrun}.
	 */
	private Athrun getAthrun() {
		if (athrun == null) {
			athrun = new Athrun(getInstrumentation(), getActivity());
		}
		return athrun;
	}

	@Override
	protected void runTest() {
		try {
			super.runTest();

		} catch (Throwable e) {
			logger.error("runTest(): ", e);
//			AthrunLog.e(LOG_TAG, "", e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void setUp() throws Exception {
		Log.i(LOG_TAG, "setUp()");
		super.setUp();
		this.inst = getInstrumentation();
		this.athrun = getAthrun();
		this.athrun.setMaxTimeToFindView(maxTimeToFindView);
		ViewElement.setMaxTimeToFindView(maxTimeToFindView);
		AthrunConnectorThread.start();
		Log.i(LOG_TAG, "setUp() finished");
	}

	@Override
	protected void tearDown() throws Exception {
		Log.i(LOG_TAG, "tearDown()");

		try {
			athrun.finishAllActivities();
			AthrunConnectorThread.stop();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		super.tearDown();
		Log.i(LOG_TAG, "tearDown() finished");
	}

	/**
	 * Return an instance of {@code AthrunDevice}.
	 * 
	 * @return An instance of {@code AthrunDevice}.
	 */
	public AthrunDevice getDevice() {
		return athrun.getDevice();
	}

	/**
	 * Return an instance of {@code ViewElement} or its subclass by the given name
	 * and return type.
	 * 
	 * @param <T>
	 *            The {@code ViewElement} or its subclass.
	 * @param name
	 *            String name of view id, the string after @+id/ defined in
	 *            layout files.
	 * @param returnType
	 *            The {@code ViewElement} or its subclass, this parameter
	 *            determines the return type of this method.
	 * @return The {@code ViewElement} or its subclass.
	 * @throws Exception
	 */
	public <T extends ViewElement> T findElementById(String name,
			Class<T> returnType) throws Exception {
		return athrun.findElementById(name, returnType);
	}

	/**
	 * Return an instance of {@code ViewElement} or its subclass by the given id
	 * and return type. It is strongly recommended you to use this API in such
	 * way:
	 * <p>
	 * 1.Add a new package in test project's src folder, this package must has
	 * the same package name as the app's R.java has.</br>
	 * <p>
	 * 2.Copy the R.java from app's gen folder into the package founded in step
	 * 1.</br>
	 * <p>
	 * 3.When the App has been changed, do not forget to update R.java in test
	 * project.</br>
	 * 
	 * <p>
	 * Now you can use this API in this way: findElementById(R.id.yourId,
	 * ViewElement.class).</br>
	 * 
	 * @param <T>
	 *            The {@code ViewElement} or its subclass.
	 * @param id
	 *            Int value of view's id.
	 * @param returnType
	 *            The {@code ViewElement} or its subclass, this parameter
	 *            determines the return type of this method.
	 * @return The {@code ViewElement} or its subclass.
	 * @throws Exception
	 */
	public <T extends ViewElement> T findElementById(int id, Class<T> returnType)
			throws Exception {
		return athrun.findElementById(id, returnType);
	}

	/**
	 * Return an instance of {@code ViewElement} by the given id.
	 * 
	 * @param id
	 *            Int value of view's id.
	 * @return The {@code ViewElement}.
	 * @throws Exception
	 */
	public ViewElement findElementById(int id) throws Exception {
		return athrun.findElementById(id, ViewElement.class);
	}

	/**
	 * Return an instance of {@code ViewElement} by the given name.
	 * 
	 * @param name
	 *            String name of view id, the string after @+id/ defined in
	 *            layout files.
	 * @return The {@code ViewElement}.
	 * @throws Exception
	 */
	public ViewElement findElementById(String name) throws Exception {
		return athrun.findElementById(name, ViewElement.class);
	}

	/**
	 * Return an instance of {@code ViewElement} or its subclass by the id tree and
	 * return type. This method is used to find a view that is contained in
	 * another view.
	 * 
	 * @param <T>
	 *            The {@code ViewElement} or its subclass.
	 * @param idTree
	 *            Parent id plus child id with ">", but without whitespace.
	 *            Example: parentId>childId.
	 * @param returnType
	 *            The {@code ViewElement} or its subclass, this parameter
	 *            determines the return type of this method.
	 * @return The {@code ViewElement} or its subclass.
	 * @throws Exception
	 */
	public <T extends ViewElement> T findElementInTree(String idTree,
			Class<T> returnType) throws Exception {
		return athrun.findElementInTree(idTree, returnType);
	}

	/**
	 * Return an instance of {@code TextViewElement} by the given text.
	 * 
	 * @param text
	 *            Text to be found.
	 * @return The {@code TextViewElement}.
	 * @throws Exception
	 */
	public TextViewElement findElementByText(String text) throws Exception {
		return athrun.findElementByText(text, TextViewElement.class);
	}

	/**
	 * Find all specified type of Views in current Activity and return them in
	 * specified athrun type.
	 * 
	 * @deprecated This method has poor performance.
	 * @param <T>
	 *            Subclass of {@code ViewElement}.
	 * @param view
	 *            Subclass of {@code View}
	 * @param returnType
	 *            Specified athrun type.
	 * @return An arraylist that contains the specified athrun viewelements.
	 * @throws Exception
	 */
	public <T extends ViewElement> ArrayList<T> findElementsByType(
			Class<? extends View> view, Class<T> returnType) throws Exception {
		return athrun.findElementsByType(view, returnType);
	}

	/**
	 * @author shidun Return an instance of {@code TmtsWebView} subclass by the
	 *         given name.
	 * 
	 * @param <T>
	 *            The {@code WebViewElement} subclass.
	 * @param name
	 *            String name of webview id, the string after @+id/ defined in
	 *            layout files.
	 * @return The {@code WebViewElement} subclass.
	 * @throws Exception
	 */
	public WebViewElement findWebViewById(String name) throws Exception {
		return athrun.findWebViewById(name);
	}

	/**
	 * Return an instance of {@code ViewElement} or its subclass by index.
	 * 
	 * @param <T>
	 * @param index
	 * @param view
	 *            Original type of view you want to get, it should be
	 *            {@code View} or its subclass, this param must be exact.
	 * @param returnType
	 *            The {@code ViewElement} or its subclass, this parameter
	 *            determines the return type of this method.
	 * @return Instance of {@code ViewElement} or its subclass.
	 * @throws Exception
	 */
	public <T extends ViewElement> T findElementByIndex(int index,
			Class<? extends View> view, Class<T> returnType) throws Exception {
		return athrun.findElementByIndex(index, view, returnType);
	}

	/**
	 * Return an instance of {@code ToastElement} which contains the specified
	 * message.
	 * 
	 * @param message
	 *            String help to identify the Toast, no need to pass exact
	 *            content, you can even pass "" to it, but this is not
	 *            recommended.
	 * @return Instance of {@code ToastElement}.
	 * @throws Exception
	 */
	public ToastElement findToastElement(String message) throws Exception {
		return athrun.findToastElement(message);
	}

	/**
	 * Return an instance of {@code ToastElement} which contains the specified
	 * message.
	 */
	public ToastElement findToastElement() throws Exception {
		return athrun.findToastElement("");
	}
	
	/**
	 * This method is used for taobao android client only.
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public SkuOptionElement findSkuOptionByText(String text) throws Exception {
		return athrun.findSkuOptionByText(text);
	}

	/**
	 * Set max time to find view, default time is 5000ms. If a view can not be
	 * found in this time, the test will fail.
	 * 
	 * @param maxTime
	 *            Time in milliseconds.
	 */
	public static void setMaxTimeToFindView(int maxTime) {
		maxTimeToFindView = maxTime;
	}

	/**
	 * Get string value by id from res/values/strings.xml of the app under test.
	 * 
	 * @param name
	 *            Id of the string resource, the name attribute of string node
	 *            defined in android xml files.
	 * @return String value of the string resource.
	 * @throws Exception
	 */
	public String getStringById(String name) throws Exception {
		return athrun.getStringById(name);
	}

	/**
	 * Return an instance of ViewOperation.
	 * 
	 * @return An instance of ViewOperation.
	 */
	public ViewOperation getViewOperation() {
		return ViewOperation.getInstance(inst);
	}

	/**
	 * when we meet some case that need us to wait ,use this method   avoid drop-dead halt
	 * 2011/12/08
	 * Modified by bingyang.djj 2011-12-26, correct compile error.
	 * @author andsun sunzhaoliang31@163.com
	 * @param whichtowait  that text or activity appear when end the wait
	 * @param type  when 0: text  ;  when 1: activity ;
	 * @return
	 * @throws Exception (this method may be lead to loop forever)
	 */
	public boolean doWaitForEnd(String whichtowait, int type) throws Exception{
		int counter = 0;
		switch(type){
		case 0:
			while(!athrun.waitForText(whichtowait, 2000)){
				Thread.sleep(2000);
				++counter;
				if(counter >= 30){					
					return false;
				}
			}
			break;
		case 1:
				while(!athrun.waitForActivity(whichtowait, 2000)){
					Thread.sleep(2000);
					++counter;
					if(counter >= 30)
						return false;
				}
			break;
		default:
				break;
		}
		return true;
	}
	
	/**
	 * get the activity name that display in the front screen
	 * 2011/12/08
	 * @author andsun sunzhaoliang31@163.com
	 * @return activityName   see as this type "com.android.browser.BrowserActivity"
	 */
	public String getCurrentActivityName() throws Exception{
		ac = getActivity();
		activityManager = (ActivityManager)ac.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE); 
		List<RunningTaskInfo> forGroundActivity = activityManager.getRunningTasks(1); 
		RunningTaskInfo currentActivity; 
		currentActivity = forGroundActivity.get(0); 
		activityName = currentActivity.topActivity.getClassName(); 
		return activityName;
	}
	
	public void closeCurrentActivity(String ActivityName) throws Exception{
		Intent mintent= new Intent(Intent.ACTION_MAIN);
		mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		mintent.addCategory(Intent.CATEGORY_HOME);
		ac.setIntent(mintent);
		ac.startActivityIfNeeded(mintent, 0);
		activityManager.killBackgroundProcesses(ActivityName); 
	}

	/**
	 * this method looks like  findTmtsTextViewByText(String text),the difference between this two methods
	 * is my method find the exact view, that find all views that contains that text.
	 * 2011/12/22
	 * @author andsun sunzhaoliang31@163.com
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public TextViewElement findElementByTextEqual(String text) throws Exception {
		return athrun.findElementByTextEqual(text, TextViewElement.class);
	}
}
