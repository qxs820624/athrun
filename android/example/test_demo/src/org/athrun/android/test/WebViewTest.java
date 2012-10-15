package org.athrun.android.test;

import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.Test;
import org.athrun.android.framework.webview.WebViewElement;

public class WebViewTest extends AthrunTestCase {
	private static final String LOG_TAG = "WebViewTest";

	public WebViewTest() throws Exception {
		super("org.athrun.android.app", "org.athrun.android.app.MainActivity");
	}

	@Test
	public void testZoomInOut() throws Exception {
		findElementByText("WebView").doClick();
		WebViewElement webview = findElementById("mywebview", WebViewElement.class);
		webview.zoomIn();
		webview.zoomIn();
		webview.zoomIn();
		webview.zoomOut();
		webview.zoomOut();
	}

	@Test
	public void testSearch() throws Exception {
		findElementByText("WebView").doClick();
		WebViewElement webview = findElementById("mywebview", WebViewElement.class);
		webview.excuteJs("document.getElementsByName('q')[0].value='iphone4s'");
		webview.excuteJs("document.getElementsByClassName('btn-bg')[0].click()");
		String result = webview.excuteJsAndReturn("document.body.innerText");
		assertTrue(result, result.contains("4500"));
		Thread.sleep(5000);
	}

	@Test
	public void testGetText() throws Exception {
		findElementByText("WebView").doClick();
		WebViewElement webview = findElementById("mywebview", WebViewElement.class);
		String result = webview.excuteJsAndReturn("document.body.innerText");
		assertTrue(result, result.contains("比价"));
		Thread.sleep(5000);
	}
}
