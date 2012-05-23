package org.athrun.ios.instrumentdriver.test;

import org.athrun.ios.instrumentdriver.*;

public class AthrunCSTest extends InstrumentDriverTestCase {

	public void testTest() throws Exception {

		UIATarget target = UIATarget.localTarget();
		UIAApplication app = target.frontMostApp();
		UIAWindow win = app.mainWindow();

		win.printElementTree();
		win.findElementByText("搜索", UIAButton.class).tap();

		UIAElement[] buttons = app.tabBar().buttons();

		assertEquals("首页", buttons[0].name());
		assertTrue(buttons[1].isEnabled());

		// 搜索宝贝
		win.searchBars()[0].tap();
		app.keyboard().typeString("iphone\\n");

		win.findElementByText("首页", UIAButton.class).tap();
		win.findElementByText("充值中心").tap();
		target.delay(2);
		//win.findElementByText("Error").tap();
	}
}
