package org.athrun.ios.instrumentdriver.test;

import org.athrun.ios.instrumentdriver.UIAButton;
import org.athrun.ios.instrumentdriver.UIASecureTextField;
import org.athrun.ios.instrumentdriver.UIATextField;

/**
 * @author ziyu.hch
 * 
 *         This is a demo case.
 */
public class TaoTestMainTest extends InstrumentDriverTestCase {

	public void testDemo1() throws Exception {

		win.printElementTree();

		win.findElementByText("Demo 1").touchAndHold(1);

		// win.findElementArrayByText("Demo 1")[0].buttons()[0].tap();
		// win.findElementByText("Demo 1").buttons()[0].tap();
		win.findElementByText("name", UIATextField.class).tap();
		// win.findElementByText("name",UIATextField.class).setValue("athrun");
		app.keyboard().typeString("athrun\n");

		win.findElementByText("", UIASecureTextField.class).tap();
		app.keyboard().typeString("abcdefg\\n");

		win.findElementByText("Hello Tao", UIAButton.class).tap();

		assertEquals("Hello,athrun!", win.staticTexts()[3].name());

		win.findElementByText("Back").tap();

	}

	public void testDemo2() throws Exception {

		win.findElementByText("Demo 2").touchAndHold(1);
		win.printElementTree();

		win.pickers()[0].wheels()[2].selectValue(2015);
		win.pickers()[1].wheels()[0].selectValue("1");
		win.pickers()[1].wheels()[1].selectValue("杭州");
		win.findElementByText("Back").tap();
	}

	public void testDemo3() throws Exception {

		win.findElementByText("Demo 3").touchAndHold(1);
		win.sliders()[0].dragToValue(0.12);
		target.frontMostApp().mainWindow().switches()[0].setValue(0);
		// app.navigationBar().leftButton().tap();
		win.findElementByText("Back", UIAButton.class).tap();
		target.scrollUp();
		target.scrollDown();
	}

}
