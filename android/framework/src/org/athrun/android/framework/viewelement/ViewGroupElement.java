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
package org.athrun.android.framework.viewelement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.athrun.android.framework.utils.RClassUtils;

import android.app.Instrumentation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Class for Android {@code ViewGroup}.
 * @author bingyang.djj
 *
 */
public class ViewGroupElement extends ViewElement implements IViewGroupElement {
	private ViewGroup viewGroup;
    
	protected ViewGroupElement(Instrumentation inst, ViewGroup viewGroup) {
		super(inst, viewGroup);
		this.viewGroup = viewGroup;
	}
	
	@Override
	public <T extends ViewElement> T findElementById(String name, Class<T> returnType) {
		Constructor<?>[] constructors = returnType.getDeclaredConstructors();
		Object obj = null;
		View v = null;
		try {
			v = viewGroup.findViewById(RClassUtils.getIdByName(inst.getTargetContext().getPackageName(), name));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (null == v) {
			logger.error("findElementById(" + name + ") failed", new Throwable());
			return null;
		}
		
		ViewUtils.scrollInToScreenIfNeeded(inst, v);
		
		try {
			constructors[0].setAccessible(true);
			obj = constructors[0].newInstance(inst, v);
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
	
	public ViewElement findElementById(String name) {
		return findElementById(name, ViewElement.class);
	}

	@Override
	public <T extends ViewElement> T getChildByIndex(int index,
			Class<T> returnType) {
		Constructor<?>[] constructors = returnType.getDeclaredConstructors();
		Object obj = null;
		View view = getChildViewByIndex(index);
		if (null == view) {
			return null;
		}
		
		ViewUtils.scrollInToScreenIfNeeded(inst, view);
		
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
	
	public ViewElement getChildByIndex(int index) {
		return getChildByIndex(index, ViewElement.class);
	}
	
	/**
	 * Get all String values from ViewGroup, such as LinearLayout, RelativeLayout.
	 * @return All String values of ViewGroup.
	 */
	public String fetchText() {
		return getAllStringFromList(getAllTextView());
	}
	
	private String getAllStringFromList(ArrayList<TextView> textViewList) {
		StringBuilder stringBuilder = new StringBuilder();
		for (TextView textView : textViewList) {
			stringBuilder.append(textView.getText());
		}
		return stringBuilder.toString();
	}
	
	private ArrayList<TextView> getAllTextView() {
		ArrayList<TextView> textViewList = new ArrayList<TextView>();
		ArrayList<View> all = getAllChildren();
		for (View view : all) {
			if (view instanceof TextView) {
				textViewList.add((TextView) view);
			}
		}
		return textViewList;
	}
	
	private void getAllChildView(ViewGroup viewGroup, ArrayList<View> views) {
		ViewUtils.addChildren(views, viewGroup, true);
	}
	
	private ArrayList<View> getAllChildren() {
		ArrayList<View> allChindren = new ArrayList<View>();
		getAllChildView(viewGroup, allChindren);
		return allChindren;
	}
	
	/**
	 * Get child count of this ViewGroupElement.
	 * @return Child count of this ViewGroupElement.
	 */
	public int getChildCount() {
		return getAllChildren().size();
	}
	
	public int getDirectChildCount() {
		return getAllDirectChild().size();
	}
	
	private ArrayList<View> getAllDirectChild() {
		ArrayList<View> allDirectChild = new ArrayList<View>();
		
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View child = viewGroup.getChildAt(i);
			if (child.isShown()) {
				allDirectChild.add(child);
			}
		}
		
		return allDirectChild;
	}
	
	private View getChildViewByIndex(int index) {
		ArrayList<View> allChildren = getAllDirectChild();
		final int max = allChildren.size() - 1;
		
		if (index > max) {
			return null;
		} 
		
		return allChildren.get(index);
	}
}
