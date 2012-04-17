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
import java.util.Iterator;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import junit.framework.Assert;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;

/**
 * This class contains activity related methods. Examples are:
 * getCurrentActivity(), getActivityList(), getAllOpenedActivities().
 * 
 * @author bingyang.djj
 */
class ActivityUtils {
	private static final String LOG_TAG = "ActivityUtils";

	private final Logger logger = LogConfigure.getLogger(getClass());

	private static final int PAUSE = 500;
	private static final int MINIPAUSE = 300;
	private static final int ACTIVITYSYNCTIME = 50;

	private final Instrumentation inst;
	private ActivityMonitor activityMonitor;
	private Activity activity;

	private static ArrayList<Activity> activityList = new ArrayList<Activity>();

	private Stack<Activity> activityStack;
	private Timer activitySyncTimer;

	/**
	 * Constructor that takes in the instrumentation and the start activity.
	 * 
	 * @param inst
	 *            the {@code Instrumentation} instance.
	 * @param activity
	 *            the start {@code Activity}
	 * @param sleeper
	 *            the {@code Sleeper} instance
	 * 
	 */
	ActivityUtils(Instrumentation inst, Activity activity) {
		this.inst = inst;
		this.activity = activity;
		createStackAndPushStartActivity();
		activitySyncTimer = new Timer();
		setupActivityMonitor();
		setupActivityStackListener();
	}

	private void createStackAndPushStartActivity() {
		activityStack = new Stack<Activity>();
		if (activity != null)
			activityStack.push(activity);
	}

	private void setupActivityStackListener() {
		TimerTask activitySyncTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (activityMonitor != null) {
					Activity activity = activityMonitor.getLastActivity();
					if (activity != null) {
						if (activityStack.peek().equals(activity))
							return;

						if (!activity.isFinishing()) {
							activityStack.remove(activity);
							activityStack.push(activity);
						}
					}
				}
			}
		};
		activitySyncTimer.schedule(activitySyncTimerTask, 0, ACTIVITYSYNCTIME);
	}

	/**
	 * Returns a {@code List} of all the opened/active activities.
	 * 
	 * @return a {@code List} of all the opened/active activities
	 * 
	 */
	ArrayList<Activity> getAllOpenedActivities() {
		return new ArrayList<Activity>(this.activityStack);
	}

	/**
	 * This is were the activityMonitor is set up. The monitor will keep check
	 * for the currently active activity.
	 * 
	 */
	private void setupActivityMonitor() {

		try {
			IntentFilter filter = null;
			activityMonitor = inst.addMonitor(filter, null, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the Orientation (Landscape/Portrait) for the current activity.
	 * 
	 * @param orientation
	 *            An orientation constant such as
	 *            {@link android.content.pm.ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}
	 *            or
	 *            {@link android.content.pm.ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}
	 *            .
	 * 
	 */
	void setActivityOrientation(int orientation) {
		Activity activity = getCurrentActivity();
		activity.setRequestedOrientation(orientation);
	}

	/**
	 * Returns the current {@code Activity}, after sleeping a default pause
	 * length.
	 * 
	 * @return the current {@code Activity}
	 * 
	 */
	Activity getCurrentActivity() {
		return getCurrentActivity(true);
	}

	/**
	 * Waits for an activity to be started if one is not provided by the
	 * constructor.
	 * 
	 */
	private final void waitForActivityIfNotAvailable() {
		if (activity == null) {
			
			if (activityMonitor != null) {
				
				while (activityMonitor.getLastActivity() == null) {
					sleep(MINIPAUSE);
				}
				
			} else {
				sleep(MINIPAUSE);
				setupActivityMonitor();
				waitForActivityIfNotAvailable();
			}
		}
	}

	// /**
	// * Returns the current {@code Activity}.
	// *
	// * @param shouldSleepFirst
	// * whether to sleep a default pause first
	// * @return the current {@code Activity}
	// *
	// */
	// private Activity getCurrentActivity(boolean shouldSleepFirst) {
	// if (shouldSleepFirst) {
	// sleep(PAUSE);
	// inst.waitForIdleSync();
	// }
	//
	// waitForActivityIfNotAvailable();
	// if (activityMonitor != null) {
	// if (activityMonitor.getLastActivity() != null)
	// activity = activityMonitor.getLastActivity();
	// }
	// return activity;
	// }

	private Activity getCurrentActivity(boolean shouldSleepFirst) {
		if (shouldSleepFirst) {
			sleep(PAUSE);
			inst.waitForIdleSync();
		}
		waitForActivityIfNotAvailable();
		if (!activityStack.isEmpty()) {
			activity = activityStack.peek();
		}
		return activity;
	}

//	void updateActivities(Activity activity) {
//		Activity exitActivity;
//		boolean found = false;
//		for (int i = 0; i < activityList.size(); i++) {
//			exitActivity = activityList.get(i);
//			if (exitActivity.getClass().getName()
//					.equals(activity.getClass().getName())) {
//				found = true;
//			}
//		}
//
//		if (!found) {
//			activityList.add(activity);
//
//		} else {
//			return;
//		}
//	}

	/**
	 * Waits for the given {@link Activity}.
	 * 
	 * @param name
	 *            the name of the {@code Activity} to wait for e.g.
	 *            {@code "MyActivity"}
	 * @param timeout
	 *            the amount of time in milliseconds to wait
	 * @return {@code true} if {@code Activity} appears before the timeout and
	 *         {@code false} if it does not
	 * 
	 */
	boolean waitForActivity(String name, int timeout) {
		long now = System.currentTimeMillis();
		final long endTime = now + timeout;
		while (!getCurrentActivity().getClass().getSimpleName().equals(name)
				&& now < endTime) {
			now = System.currentTimeMillis();
		}

		if (now < endTime) {
			return true;

		} else {
			return false;
		}
	}

	// /**
	// * Returns to the given {@link Activity}.
	// *
	// * @param name
	// * the name of the {@code Activity} to return to, e.g.
	// * {@code "MyActivity"}
	// *
	// */
	// void goBackToActivity(String name) {
	// boolean found = false;
	// for (Activity activity : activityList) {
	// if (activity.getClass().getSimpleName().equals(name))
	// found = true;
	// }
	// if (found) {
	// while (!getCurrentActivity().getClass().getSimpleName()
	// .equals(name)) {
	// try {
	// inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
	// } catch (SecurityException e) {
	// Assert.assertTrue("Activity named " + name
	// + " can not be returned to", false);
	// }
	// }
	// } else {
	// for (int i = 0; i < activityList.size(); i++)
	// // Log.d(LOG_TAG, "Activity priorly opened: "
	// // + activityList.get(i).getClass().getSimpleName());
	// logger.info("Activity priorly opened: "
	// + activityList.get(i).getClass().getSimpleName());
	// Assert.assertTrue("No Activity named " + name
	// + " has been priorly opened", false);
	// }
	// }

	void goBackToActivity(String name) {
		ArrayList<Activity> activitiesOpened = getAllOpenedActivities();
		boolean found = false;

		for (int i = 0; i < activitiesOpened.size(); i++) {
			if (((Activity) activitiesOpened.get(i)).getClass().getSimpleName()
					.equals(name)) {
				found = true;
				break;
			}
		}

		if (found) {
			while (!getCurrentActivity().getClass().getSimpleName()
					.equals(name)) {
				try {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
				} catch (SecurityException ignored) {
				}
			}
			
		} else {
			
			Assert.assertTrue("No Activity named " + name
					+ " has been priorly opened", false);
		}
	}

	public void finalize() throws Throwable {
		try {
			if (this.activityMonitor != null)
				this.inst.removeMonitor(this.activityMonitor);
		} catch (Exception ignored) {
		}
		super.finalize();
	}

	void finishInactiveActivities() {
		for (Iterator<?> iter = this.activityStack.iterator(); iter.hasNext();) {
			Activity activity = (Activity) iter.next();
			if (activity != getCurrentActivity()) {
				finishActivity(activity);
				iter.remove();
			}
		}
	}

	private void finishActivity(Activity activity) {
		try {
			activity.finish();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	void finishOpenedActivities() {
		this.activitySyncTimer.cancel();
		ArrayList<Activity> activitiesOpened = getAllOpenedActivities();

		for (int i = activitiesOpened.size() - 1; i >= 0; i--) {
			sleep(100);
			finishActivity((Activity) activitiesOpened.get(i));
		}

		finishActivity(getCurrentActivity());
		sleep(MINIPAUSE);
		try {
			this.inst.sendKeyDownUpSync(4);
			sleep(100);
			this.inst.sendKeyDownUpSync(4);
		} catch (Throwable ignored) {
		}
		this.activityStack.clear();
	}

	// /**
	// * All activities that have been active are finished.
	// */
	// void finishAllActivities() throws Throwable {
	// // Log.i(LOG_TAG, "finishAllActivities() started");
	// logger.info("finishAllActivities() started");
	// try {
	// // Finish all opened activities
	// for (int i = activityList.size() - 1; i >= 0; i--) {
	// if (null != activityList.get(i)) {
	// activityList.get(i).finish();
	// sleep(100);
	// }
	// }
	//
	// // Finish the initial activity
	// getCurrentActivity().finish();
	// activityList.clear();
	//
	// // Remove the monitor added during startup
	// if (activityMonitor != null) {
	// inst.removeMonitor(activityMonitor);
	// }
	// } catch (Exception ignored) {
	// }
	// super.finalize();
	// // Log.i(LOG_TAG, "finishAllActivities() finished");
	// logger.info("finishAllActivities() finished");
	// }

	private static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignored) {
		}
	}
}
