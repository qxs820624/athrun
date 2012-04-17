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
package org.athrun.android.framework.viewelememt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.app.Instrumentation;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

/**
 * Base element for Android {@code AbsListView}. Especially for {@code ListView} and {@code GridView}.
 * @author bingyang.djj
 *
 */
public class AbsListViewElement extends ViewGroupElement {
	private static final String LOG_TAG = "AbsListViewElement";
	private AbsListView absListView;
    
	protected AbsListViewElement(Instrumentation inst, AbsListView absListView) {
		super(inst, absListView);
		this.absListView = absListView;
	}

	@Override
	public <T extends ViewElement> T getChildByIndex(int index, Class<T> returnType) {
		Constructor<?>[] constructors = returnType.getDeclaredConstructors();
		Object obj = null;
		View view = getChildByIndex(index);
		if (null == view ) {
			return null;
		}
		
		try {
			constructors[0].setAccessible(true);
			obj = constructors[0].newInstance(inst, view);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnType.cast(obj);
	}
	
	/**
	 * Scroll the view to the specified line.
	 * @param line The 
	 */
	public void scrollToLine(final int line) {
		inst.waitForIdleSync();
				
		final int max = absListView.getAdapter().getCount() - 1;
		final int realLine;
		
		if (line <= max) {
			realLine = line;
			
		} else {
			realLine = max;
		}
		
		inst.runOnMainSync(new Runnable() {

			@Override
			public void run() {
//				Log.i(LOG_TAG, "scrollToLine(" + realLine + ")");
				logger.info("scrollToLine(" + realLine + ")");
				absListView.setSelection(realLine);
			}
		});
		inst.waitForIdleSync();
	}
	
	private View getChildByIndex(int index) {
		scrollToLine(index);
		
		final int max = absListView.getAdapter().getCount() - 1;
		final int realIndex;
		
		if (index <= max) {
			realIndex = index;
			
		} else {
			realIndex = max;
		}
		
		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < startTime
				+ getMaxTimeToFindView()) {
			if (null == absListView.getChildAt(realIndex - absListView.getFirstVisiblePosition())) {
//				Log.i(LOG_TAG, "getChildByIndex(" + realIndex
//						+ ") return null, sleep");
				logger.info("getChildByIndex(" + realIndex
						+ ") return null, sleep");
				sleep(RETRY_TIME);
				
			} else {
//				Log.i(LOG_TAG, "getChildByIndex(" + realIndex + ") succeed");
				logger.info("getChildByIndex(" + realIndex + ") succeed");
				return absListView.getChildAt(realIndex - absListView.getFirstVisiblePosition()); 
			}
		}
//		AthrunLog.e(LOG_TAG, "getChildByIndex(" + realIndex + ") return null" , new Throwable());
		logger.error("getChildByIndex(" + realIndex + ") return null." , new Throwable());
		
		return null;
	}
	
	/**
	 * Scroll the AbsListView to next screen.
	 */
	public void scrollToNextScreen() {
		inst.waitForIdleSync();
		
		if (isTheLastCompletelyShown()) {
			scrollToLine(absListView.getLastVisiblePosition() + 1);
			
		} else {
			scrollToLine(absListView.getLastVisiblePosition());
		}
	}
	
	private boolean isTheLastCompletelyShown() {
		int lastIndex = absListView.getLastVisiblePosition() - absListView.getFirstVisiblePosition();
		View lastItem = absListView.getChildAt(lastIndex);
		
		return !ViewUtils.isViewCenterOutOfScreen(lastItem);
	}
	 
	/**
	 * Returns the position within the adapter's data set for the first item displayed on screen.
	 * @return
	 */
	public int getFirstVisiblePosition() {
		inst.waitForIdleSync();
		return absListView.getFirstVisiblePosition();
	}
	
	/**
	 * Returns the position within the adapter's data set for the last item displayed on screen.
	 * @return
	 */
	public int getLastVisiblePosition() {
		inst.waitForIdleSync();
		return absListView.getLastVisiblePosition();
	}
}
