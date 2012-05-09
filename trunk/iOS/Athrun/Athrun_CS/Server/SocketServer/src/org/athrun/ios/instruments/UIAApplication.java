/**
 * 
 */
package org.athrun.ios.instruments;

/**
 * @author ziyu.hch
 * 
 */
public class UIAApplication {

	private String guid = null;

	public UIAApplication(String guid) {
		// TODO Auto-generated constructor stub
		this.guid = guid;
	}

	public UIAWindow[] windows() {

		return UIAElementHelp.windowArray(this.guid + ".windows()");
	}

	/**
	 * mainWindow
	 */
	public UIAWindow mainWindow() {

		return UIAElementHelp.getWindow(this.guid + ".mainWindow()");
	}

	public UIAActionSheet actionSheet() {

		return UIAElementHelp.getActionSheet(this.guid + ".actionSheet()");
	}

	public UIAKeyboard keyboard() {

		return UIAElementHelp.getKeyboard(this.guid + ".keyboard()");
	}

	public UIANavigationBar navigationBar() {

		return UIAElementHelp.getNavigationBar(this.guid + ".navigationBar()");
	}

	public UIAStatusBar statusBar() {

		return UIAElementHelp.getStatusBar(this.guid + ".statusBar()");
	}

	/**
	 * tabBar
	 */
	public UIATabBar tabBar() {

		return UIAElementHelp.getTabBar(this.guid + ".tabBar()");
	}

	public UIAToolbar toolbar() {

		return UIAElementHelp.getToolbar(this.guid + ".toolbar()");
	}
}
