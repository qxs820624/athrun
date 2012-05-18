package org.athrun.ios.test;

import org.athrun.ios.instruments.*;

public class AthrunCSTest extends IOSTestCase {

	public void testTest() throws Exception {

		UIATarget target = UIATarget.localTarget();
		UIAApplication app = target.frontMostApp();
		UIAWindow win = app.mainWindow();

		win.printElementTree();
		win.findElementByTextType("搜索", UIAButton.class).tap();

		UIAElement[] buttons = app.tabBar().buttons();

		assertEquals("首页", buttons[0].name());
		assertTrue(buttons[1].isEnabled());

		// 搜索宝贝
		win.searchBars()[0].tap();
		app.keyboard().typeString("iphone\\n");

		win.findElementByTextType("首页", UIAButton.class).tap();
		win.findElementByText("充值中心").tap();
		target.delay(2);
		win.findElementByText("Error").tap();
	}
}
