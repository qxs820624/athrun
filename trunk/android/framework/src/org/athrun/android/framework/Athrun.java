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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.athrun.android.framework.special.taobaoview.SkuOptionElement;
import org.athrun.android.framework.utils.RClassUtils;
import org.athrun.android.framework.viewelement.IViewElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ToastElement;
import org.athrun.android.framework.viewelement.ViewElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;
import org.athrun.android.framework.viewelement.ViewUtils;
import org.athrun.android.framework.webview.WebViewElement;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;


/**
 * Android Test Framework Class for TmtsTestCase to include.
 * 
 * @author shidun Added in 2011-05-16.
 * @author bingyang.djj.
 */
final class Athrun {
	
	private final Logger logger = LogConfigure.getLogger(getClass());

	/**
	 * Parameter of Athrun.
	 */
	private Instrumentation inst;
	private ActivityUtils activityUtils;
	private AthrunDevice athrunDevice;

	/**
	 * Max time to find a view.
	 */
	private int maxTimeToFindView;

	/**
	 * Constructor of {@link Athrun}.
	 * 
	 * @param inst
	 *            {@link Instrumentation}.
	 * @param activity
	 *            {@link Activity}.
	 */
	Athrun(Instrumentation inst, Activity activity) {
		this.inst = inst;
		this.activityUtils = ActivityUtils.getInstance(inst, activity);
		this.athrunDevice = AthrunDevice.getInstance(inst, activity);
		logger.info("Construct instance of Athrun finished.");
	}

	AthrunDevice getDevice() {
		return athrunDevice;
	}

	<T extends ViewElement> T findElementById(String name, Class<T> returnType)
			throws Exception {
		View view = findViewById(name);
		if (null == view) {
			logger.error("findElementById(" + name + ") return null.");
			return null;
		}

		Object obj = getConstructor(returnType).newInstance(inst, view);
		return returnType.cast(obj);
	}

	<T extends ViewElement> T findElementById(int id, Class<T> returnType)
			throws Exception {
		View view = findViewById(id);
		if (null == view) {
			logger.error("findElementById(" + id + ") return null.");
			return null;
		}

		Object obj = getConstructor(returnType).newInstance(inst, view);
		return returnType.cast(obj);
	}

	private View findViewById(int id, int timeout) {
		final long startTime = System.currentTimeMillis();
		logger.info("maxTimeToFindView is " + timeout);

		ArrayList<View> matches = new ArrayList<View>();

		while (System.currentTimeMillis() < startTime + timeout) {

			ArrayList<View> all = ViewUtils.getAllViews(false);
			all = ViewUtils.removeInvisibleViews(all);

			for (View view : all) {

				if (id == view.getId()) {
					matches.add(view);
				}
			}

			if (0 == matches.size()) {
				sleep(IViewElement.RETRY_TIME);
				continue;
			}

			logger.info("find " + matches.size()
					+ " veiw that match the id " + id);

			return getMostSuitableView(matches);
		}

		logger.error("findViewById(" + id + ") return null",
				new Throwable());
		return null;
	}

	private View findViewById(int id) {
		return findViewById(id, getMaxTimeToFindView());
	}

	private View findViewById(String name, int timeout) throws Exception {
		int id = RClassUtils.getIdByName(getPackageName(), name);
		return findViewById(id, timeout);
	}

	private View findViewById(String name) throws Exception {
		return findViewById(name, getMaxTimeToFindView());
	}

	<T extends ViewElement> T findElementInTree(String idTree,
			Class<T> returnType) throws Exception {
		ViewElement viewElement = null;
		String[] ids = idTree.split(">");

		if (ids.length == 1) {
			viewElement = findElementById(ids[0], returnType);

		} else if (ids.length > 1) {

			viewElement = findElementById(ids[0], ViewGroupElement.class);
			for (int i = 1; i < ids.length; i++) {
				viewElement = ((ViewGroupElement) viewElement).findElementById(
						ids[i], returnType);
			}
		}

		return returnType.cast(viewElement);
	}

	<T extends TextViewElement> T findElementByText(String text,
			Class<T> returnType) throws Exception {
		View view = findViewByText(text);
		if (null == view) {
			logger.error("findElementByText(" + text + ") return null.");
			
			return null;
		}

		Object obj = getConstructor(returnType).newInstance(inst, view);
		return returnType.cast(obj);
	}

	/*
	 * andsun add modified by bingyang.djj, change the method name for athrun.
	 */
	<T extends TextViewElement> T findElementByTextEqual(String text,
			Class<T> returnType) throws Exception {
		View view = findViewByTextEqual(text);
		if (null == view) {
//			AthrunLog.e(LOG_TAG, "findElementByTextEqual(" + text
//					+ ") return null.");
			logger.error("findElementByTextEqual(" + text
					+ ") return null.");
			return null;
		}

		Object obj = getConstructor(returnType).newInstance(inst, view);
		return returnType.cast(obj);
	}

	private TextView findViewByText(String text, int timeout) {
		final long startTime = System.currentTimeMillis();
		logger.info("maxTimeToFindView is " + timeout);

		ArrayList<TextView> matches = new ArrayList<TextView>();

		while (System.currentTimeMillis() < startTime + timeout) {

//			updateActivities();
			ArrayList<TextView> textViews = getAllTextView();
			textViews = ViewUtils.removeInvisibleViews(textViews);

			for (TextView textView : textViews) {

				String current = textView.getText().toString();
				if (current.equals(text)) {
					matches.add(textView);
				}
			}

			if (0 == matches.size()) {
				sleep(IViewElement.RETRY_TIME);
				continue;
			}

			logger.info("found " + matches.size()
					+ " TextView that contains the text " + text + ".");
			return getMostSuitableView(matches);
		}

		logger.error("findViewByText(" + text + ") return null",
				new Throwable());
		return null;
	}

	private <T extends View> T getMostSuitableView(ArrayList<T> matches) {
		int matchedCounts = matches.size();
		T mostSuitable = null;

		if (1 == matchedCounts) {
			ViewUtils.scrollInToScreenIfNeeded(inst, matches.get(0));
			mostSuitable = matches.get(0);

		} else if (matchedCounts > 1) {

			ArrayList<T> shown = ViewUtils.removeUnshownViews(matches);
			logger.info("There are " + shown.size() + " matched views is shown.");
			ArrayList<T> inScreen = new ArrayList<T>();

			for (T view : matches) {
				if (!ViewUtils.isViewOutOfScreen(view)) {
					inScreen.add(view);
				}
			}
			logger.info("There are " + inScreen.size() + " matched views in screen.");

			ArrayList<T> inScreenShown = ViewUtils.removeUnshownViews(inScreen);
			logger.info("There are " + inScreenShown.size() + " matched views is shown & in screen.");
			
			if (0 != inScreenShown.size()) {
				mostSuitable = inScreenShown.get(0);

			} else {

				if (0 != shown.size()) {
					mostSuitable = shown.get(0);

				} else {

					if (0 != inScreen.size()) {
						mostSuitable = inScreen.get(0);

					} else {
						mostSuitable = matches.get(0);
					}
				}
			}
		}

		return mostSuitable;
	}

	// andsun add
	private TextView findViewByTextEqual(String text, int timeout) {
		final long startTime = System.currentTimeMillis();
		logger.info("maxTimeToFindView is " + timeout);

		while (System.currentTimeMillis() < startTime + timeout) {

//			updateActivities();

			ArrayList<TextView> textViews = getAllTextView();

			for (TextView textView : textViews) {
				String current = textView.getText().toString();
				if (current.equals(text)) {
					logger.info("findViewByText(" + text + ") succeed. In: "
							+ current);
					ViewUtils.scrollInToScreenIfNeeded(inst, textView);
					return textView;
				}
			}
		}
		logger.error("findViewByText(" + text + ") return null",
				new Throwable());
		return null;
	}

	private ArrayList<TextView> getAllTextView() {
		ArrayList<View> all = ViewUtils.getAllViews(false);
		ArrayList<TextView> textViews = ViewUtils.filterViews(TextView.class,
				all);
		return textViews;
	}

	private TextView findViewByText(String text) {
		return findViewByText(text, getMaxTimeToFindView());
	}

	// andsun add
	private TextView findViewByTextEqual(String text) {
		return findViewByTextEqual(text, getMaxTimeToFindView());
	}

	WebViewElement findWebViewById(String id) throws Exception {
		View view = findViewById(id);

		if (null == view) {
			logger.error("findWebViewById(" + id + ") return null");
			return null;
		}

		ViewUtils.scrollInToScreenIfNeeded(inst, view);
		WebView webView = WebView.class.cast(view);

		Object obj = getConstructor(WebViewElement.class).newInstance(inst,
				webView);
		return WebViewElement.class.cast(obj);
	}

	ToastElement findToastElement(String message) throws Exception {
		final long startTime = System.currentTimeMillis();
		int id = RClassUtils.getIdByName("com.android.internal", "message");

		while (System.currentTimeMillis() < startTime + getMaxTimeToFindView()) {
			ArrayList<TextView> textViews = getAllTextView();
			for (TextView textView : textViews) {
				String content = textView.getText().toString();
				logger.info("Text content is " + content);
				if (id == textView.getId() && content.contains(message)) {
					return ToastElement.class.cast(getConstructor(
							ToastElement.class).newInstance(content));
				}
			}
		}
		return null;
	}

	// <T extends AbsListViewElement> T findListByIndex(int index, Class<T>
	// returnType)
	// throws Exception {
	// return findViewByIndex(index, AbsListView.class, returnType);
	// }

	// ScrollViewElement findScrollViewByIndex(int index) throws Exception {
	// return findViewByIndex(index, ScrollView.class, ScrollViewElement.class);
	// }

	<T extends ViewElement> ArrayList<T> findElementsByType(
			Class<? extends View> view, Class<T> returnType) throws Exception {
		inst.waitForIdleSync();
		ArrayList<View> all = ViewUtils.getAllViews(false);
		ArrayList<View> allVisable = ViewUtils.removeInvisibleViews(all);
		ArrayList<? extends View> allWanted = ViewUtils.filterViews(view,
				allVisable);
		ArrayList<T> returnList = new ArrayList<T>();
		Constructor<?> constructor = getConstructor(returnType);
		for (View specifiedView : allWanted) {
			Object obj = constructor.newInstance(inst, specifiedView);
			returnList.add(returnType.cast(obj));
		}
		return returnList;
	}

	private <T extends View> ArrayList<T> findViewByIndex(Class<T> view) {
		inst.waitForIdleSync();
		try {
			Thread.sleep(getMaxTimeToFindView());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<View> all = ViewUtils.getAllViews(false);
		all = ViewUtils.removeInvisibleViews(all);
		ArrayList<T> lists = ViewUtils.filterViews(view, all);
		
		logger.info("Find " + lists.size() + " " + view.getClass().getName());
		
		ArrayList<T> list = ViewUtils.removeInvisibleViews(lists);
		ArrayList<T> listReturn = ViewUtils.removeUnshownViews(list);
		return listReturn;
	}

	<T extends ViewElement> T findElementByIndex(int index,
			Class<? extends View> view, Class<T> returnType) throws Exception {
		ArrayList<? extends View> viewList = findViewByIndex(view);

		if (0 == viewList.size()) {
			logger.error("findViewByIndex(" + index + ") return null.");
			return null;
		}

		View listItem = viewList.get(index);
		ViewUtils.scrollInToScreenIfNeeded(inst, listItem);

		Object obj = getConstructor(returnType).newInstance(inst, listItem);
		return returnType.cast(obj);
	}

	private String getPackageName() {
		return inst.getTargetContext().getPackageName();
	}

	/**
	 * Set max time to find view, default time is 5000ms. If a view can not be
	 * found in this time, the test will fail.
	 * 
	 * @param maxTime
	 *            Time in milliseconds.
	 */
	void setMaxTimeToFindView(int maxTime) {
		this.maxTimeToFindView = maxTime;
	}

	private int getMaxTimeToFindView() {
		return maxTimeToFindView;
	}

	private Resources getResource() {
		return inst.getTargetContext().getResources();
	}

	String getStringById(String name) throws Exception {
		int id = RClassUtils.getRFieldByName(getPackageName(), "string", name);
		return getResource().getString(id);
	}

	XmlResourceParser getLayoutById(String name) throws Exception {
		int id = RClassUtils.getRFieldByName(getPackageName(), "layout", name);
		return getResource().getLayout(id);
	}

	void finishAllActivities() {
		try {
			activityUtils.finishOpenedActivities();
			activityUtils.finishInactiveActivities();
			activityUtils.finalize();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private Constructor<?> getConstructor(Class<?> returnType) {
		Constructor<?> constructor = returnType.getDeclaredConstructors()[0];
		constructor.setAccessible(true);
		return constructor;
	}

	private void sleep(int time) {
		try {
			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	boolean waitForActivity(String name, int timeout) {
		return activityUtils.waitForActivity(name, timeout);
	}
	
	boolean waitForText(String text, int timeout) {
		TextView textView = findViewByText(text, timeout);
		return (null != textView) ? true : false;
	}
	
	//taobao skuview=============================================
	SkuOptionElement findSkuOptionByText(String text) throws Exception {
		Class<?> skuSelectOption = Class.forName("com.taobao.tao.component.skunative.SkuSelectOption");
		logger.info(skuSelectOption.toString());
		
		final long startTime = System.currentTimeMillis();
		logger.info("maxTimeToFindView is " + getMaxTimeToFindView());

		while (System.currentTimeMillis() < startTime + getMaxTimeToFindView()) {

			ArrayList<View> all = ViewUtils.getAllViews(false);
			all = ViewUtils.removeInvisibleViews(all);

			for (View view : all) {

				if (skuSelectOption.isAssignableFrom(view.getClass())) {
					Method getText = view.getClass().getDeclaredMethod("getText");
					
					if (((String)getText.invoke(view)).equalsIgnoreCase(text)) {
						Object obj = getConstructor(SkuOptionElement.class).newInstance(inst, view);
						return SkuOptionElement.class.cast(obj);
					}
				}
			}
		}

		logger.error("findSkuSelectOptionByText(" + text + ") return null",
				new Throwable());
		return null;
	}
}
