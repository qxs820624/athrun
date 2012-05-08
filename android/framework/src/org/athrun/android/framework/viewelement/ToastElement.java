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

import org.apache.log4j.Logger;
import org.athrun.android.framework.LogConfigure;

/**
 * Class for Android {@code Toast}.
 * @author bingyang.djj
 *
 */

public final class ToastElement {
	private final Logger logger = LogConfigure.getLogger(getClass());
	
	private String message;
	
	private ToastElement(String message) {
		this.message = message;
		logger.info("Construct an instance of ToastElement with " + message + ".");
	}
	
	/**
	 * Return the message of the Toast.
	 * @return Message of the Toast.
	 */
	public String getText() {
		return this.message;
	}
}
