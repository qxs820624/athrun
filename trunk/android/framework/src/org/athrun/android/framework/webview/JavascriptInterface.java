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
 * @author shidun Custom module that is added to the WebView's JavaScript engine
 *         to enable callbacks to java code. This is required since WebView
 *         doesn't expose the underlying DOM.
 */
public final class JavascriptInterface {
	private volatile String result;
	private static volatile boolean resultReady;
	private static JavascriptInterface javascriptInterfaceInstance = new JavascriptInterface();
	private static long msecs = 1000;
	private static Integer count = 10;
	private WebViewElement tmtsWebView;
	public void setTmtsWebView(WebViewElement tmtsWebView) {
		this.tmtsWebView = tmtsWebView;
	}

	public JavascriptInterface() {
	}

	public static JavascriptInterface getInstance() {
		return javascriptInterfaceInstance;
	}

	/**
	 * @deprecated  please use executejs instead 
	 * @author shidun
	 */
	public void excutejs(String updated) {
		executejs(updated);
	}
	/**
	 * @author shidun
	 */
	public void executejs(String updated) {
		result = updated;
		setReady(true);
		Log.d("Athrun", "CallBack executejs: " + result);
	}
	public synchronized void setReady(boolean isReady){
		resultReady = isReady;
	}

	
	/**
	 * @author shidun A callback from JavaScript to Java that passes execution
	 *         result as a parameter.
	 * 
	 *         This method is accessible from WebView's JS DOM as
	 *         windows.webdriver.getResult().
	 * 
	 * @param result
	 *            Result that should be returned to Java code from WebView.
	 */
	public String getResult(){
		String result="";
		 try{
			 result = getResult(count);
			 Thread.sleep(200); //sleep a short time for js executing.
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		return result;
	}
	
	public String getResult(long retryTimes) throws Exception {
	 
		Log.d("Athrun", "retryTimes:" + retryTimes+"resultReady: "+resultReady+"tmtsWebView:"+tmtsWebView.getWebView());
		long startTime = System.currentTimeMillis();
		long waitTime = msecs;
		if (resultReady)
			return doGet(); 
		else {
			for (;;) {
				Thread.sleep(100);
				if (resultReady){
					Log.d("Athrun", "resultReady is: "+resultReady+" need doGet()");
					return doGet();
				}else {
					waitTime = msecs - (System.currentTimeMillis() - startTime);
					if (waitTime <= 0){
						if(retryTimes>0){
							if(tmtsWebView!=null){
								tmtsWebView.executeJs(tmtsWebView.getCurrentScript());
								Log.d("Athrun", "retryTimes2:" + retryTimes--);
								return getResult(retryTimes);
							}else{
								return "webView is null";
							}
						}else{
							return "out of waittime";
						}
					}
				}
			}
		}
		
	}
	private String doGet(){
		setReady(false);
		return result;
	}
}