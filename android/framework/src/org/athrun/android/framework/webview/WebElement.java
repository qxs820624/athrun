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
package org.athrun.android.framework.webview;

import android.util.Log;

/**
 * @deprecated under construction
 * @author bingyang.djj
 *
 */
public class WebElement {
 private final WebViewElement mTmtsWebView;
	
	public WebElement(WebViewElement tmtsWebView){
		mTmtsWebView = tmtsWebView;
	}
	
	public WebElement nextSibling() {
		//TODO
		return this;
	}
	
	public String innerHTML(){
		return excuteJavaScriptAndReturn("element.innerHTML");	
	}
	
	public void click() {
		Log.d("AthrunActivity","click in webelement");
		loadJavascript("element.dispatchEvent(e)");
	}
	
	
	private String excuteJavaScriptAndReturn(String script) {
		loadJavascript("window.webdriver.executejs("+script+")");
		return JavascriptInterface.getInstance().getResult();
	}
	
	public void excuteJs(final String script) throws InterruptedException {
		loadJavascript(script);
	}
	
	private void loadJavascript(String script) {
//		this.currentScript = script;
		mTmtsWebView.getInterface().setReady(false);
		mTmtsWebView.getWebView().loadUrl("javascript:"+ script);
	}
}
