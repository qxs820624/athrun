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

import android.app.Instrumentation;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Class for Android {@code WebView}.
 * @deprecated under construction
 * @author shidun
 * 
 */
public class WebViewElement {
	private static final String LOG_TAG = "WebViewElement";
	
	private WebView mWebView;
	private final Instrumentation inst;
	private final JavascriptInterface jInterface;
	private String currentScript;
	private final int maxTimeForWebViewReady = 30 * 1000; // 30s

	private WebViewElement(Instrumentation inst, WebView webview) {
		this.mWebView = webview;
		this.inst = inst;
		this.jInterface = JavascriptInterface.getInstance();
		this.jInterface.setTmtsWebView(this);
		initWebViewSettings(mWebView);
	}

	/**
	 * @param javascript
	 * @return javascript excute result
	 * @throws InterruptedException
	 */
	public String excuteJsAndReturn(final String script) {
		Log.d("Mywebview", "progress:" + mWebView.getProgress());
		waitForWebViewLoaded();
		loadJavascript("window.webdriver.excutejs(" + script + ")");
		Log.i(LOG_TAG, script);		
		return jInterface.getResult();
	}

	public void zoomIn() throws Exception {
		waitForWebViewLoaded();
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {

			@Override
			public void run() {
				mWebView.zoomIn();
			}
		});
		inst.waitForIdleSync();
	}

	public void zoomOut() throws Exception {
		waitForWebViewLoaded();
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				mWebView.zoomOut();
			}
		});
		inst.waitForIdleSync();
	}

	/**
	 * @deprecated please using executeJs instead	 * @param javascript
	 * @throws InterruptedException
	 */
	public void excuteJs(final String script) throws InterruptedException {
		executeJs(script);
	}

	/**
	 * @param javascript
	 * @throws InterruptedException
	 */
	public void executeJs(final String script) throws InterruptedException {		Log.d("Mywebview", "progress:" + mWebView.getProgress());		waitForWebViewLoaded();		loadJavascript(script);		Log.i(LOG_TAG, script);		Thread.sleep(500); //sleep a short time for js executing.	}
	public WebElement getElementById(String id) {
		loadJavascript("var element = document.getElementById('" + id + "');");
		Log.i(LOG_TAG, jInterface.getResult());
		return new WebElement(this);
	}

	public WebElement getElementsByClassName(String className) {
		loadJavascript("var element = document.getElementsByClassName('"
				+ className + "')[0];");
		Log.i(LOG_TAG, jInterface.getResult());
		return new WebElement(this);
	}

	/**
	 * @return webview
	 */
	public WebView getWebView() {
		return mWebView;
	}

	/**
	 * @return webview url
	 */
	public String getUrl() {
		return mWebView.getUrl();
	}

	/**
	 * @return webview title
	 */
	public String getTitle() {
		return mWebView.getTitle();
	}

	public JavascriptInterface getInterface() {
		return this.jInterface;
	}

	public String getCurrentScript() {
		return currentScript;
	}

	/**
	 * @return boolean
	 */
	private boolean waitForWebViewLoaded() {
		Boolean isOK = false;
		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < startTime + maxTimeForWebViewReady) {
			try {
				if (mWebView.getProgress() == 100) {
					isOK = true;
					break;
				} else {
					Thread.sleep(500);
					Log.d("Mywebview", "sleep:" + mWebView.getProgress());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isOK;
	}

	private void initWebViewSettings(WebView webView) {
		Log.d("Mywebview", "settings: " + webView);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(jInterface, "webdriver");
		initJavascript();
	}

	final String initJavascript = "var e = document.createEvent('HTMLEvents');"
			+ "e.initEvent('click', false, false);";

	private void initJavascript() {
		loadJavascript(initJavascript);
	}

	private void loadJavascript(String script) {
		this.currentScript = script;
		jInterface.setReady(false);
		mWebView.loadUrl("javascript:" + script);
	}
}
