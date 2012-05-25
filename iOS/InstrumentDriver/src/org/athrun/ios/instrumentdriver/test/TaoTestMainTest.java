package org.athrun.ios.instrumentdriver.test;

import org.athrun.ios.instrumentdriver.UIAButton;
import org.athrun.ios.instrumentdriver.UIASecureTextField;
import org.athrun.ios.instrumentdriver.UIATextField;

public class TaoTestMainTest extends InstrumentDriverTestCase {

	public void testDemo1() throws Exception {

		win.findElementByText("Demo 1").tap();

		win.printElementTree();
		win.findElementByText("name", UIATextField.class).tap();
		app.keyboard().typeString("athrun\\n");

		win.findElementByText("", UIASecureTextField.class).tap();
		app.keyboard().typeString("abcdefg\\n");

		win.findElementByText("Hello Tao", UIAButton.class).tap();

		assertEquals("Hello,taobao!", win.staticTexts()[3].name());

		win.findElementByText("Back").tap();

	}

	public void testDemo2() throws Exception {

		win.findElementByText("Demo 2").tap();
		win.printElementTree();

		win.pickers()[0].wheels()[2].selectValue(2015);
		win.pickers()[1].wheels()[0].selectValue("1");
		win.pickers()[1].wheels()[1].selectValue("杭州");
		win.findElementByText("Back").tap();
	}

	public void testDemo3() throws Exception {

		win.findElementByText("Demo 3").tap();
		win.sliders()[0].dragToValue(0.12);
		target.frontMostApp().mainWindow().switches()[0].setTheValue(0);
		// app.navigationBar().leftButton().tap();
		win.findElementByText("Back", UIAButton.class).tap();
		target.scrollUp();
		target.scrollDown();
	}

}
