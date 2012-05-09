package org.athrun.ios.test;

import org.athrun.ios.instruments.*;

public class AthrunCSTest extends IOSTestCase {

	public void testTest() throws Exception {

		UIATarget target = UIATarget.localTarget();
		UIAApplication app = target.frontMostApp();
		UIAWindow win = app.mainWindow();

		UIAElement[] buttons = app.tabBar().buttons();
		buttons[1].tap();

		assertEquals("首页", buttons[0].name());
		assertTrue(buttons[1].isEnabled());

		// 搜索宝贝
		win.searchBars()[0].tap();
		app.keyboard().typeString("iphone\\n");
		buttons[0].tap();
	}
}
