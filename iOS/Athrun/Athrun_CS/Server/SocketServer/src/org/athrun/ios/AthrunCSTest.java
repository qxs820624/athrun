package org.athrun.ios;

import org.athrun.instruments.UIAApplication;
import org.athrun.instruments.UIATarget;

public class AthrunCSTest extends IOSTestCase {
	public void testTest() throws Exception {

		UIATarget target = UIATarget.localTarget();
		UIAApplication app = target.frontMostApp();
		app.tabBar().buttons()[1].tap();
		String name = app.tabBar().buttons()[0].name();
		assertEquals("首页", name);
		assertTrue(app.tabBar().buttons()[1].isEnabled());
	}
}
