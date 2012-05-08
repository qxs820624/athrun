/**
 * 
 */
package org.athrun.ios.instruments;

/**
 * @author ziyu.hch
 * 
 */
public class UIAWindow extends UIAElement {

	/**
	 * 
	 */
	public UIAWindow(String guid) {
		// TODO Auto-generated constructor stub
		super(guid);
	}

	// public Rect contentArea()

	/**
	 * navigationBar
	 * 
	 * @return navigationBar instance
	 */
	public UIANavigationBar navigationBar() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".navigationBar()") : this.guid + ".navigationBar()";

		return new UIANavigationBar(guid);
	}

	public UIANavigationBar[] navigationBars() {

		return navigationBarArray(".navigationBars()");
	}

	public UIATabBar tabBar() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid + ".tabBar()")
				: this.guid + ".tabBar()";

		return new UIATabBar(guid);
	}

	public UIATabBar[] tabBars() {

		return tabBarArray(".tabBars()");
	}

	public UIAToolbar toolbar() {

		String guid = RunType.DEBUG ? MySocket
				.getGuid(this.guid + ".toolbar()") : this.guid + ".toolbar()";

		return new UIAToolbar(guid);
	}

	public UIAToolbar[] toolbars() {

		return toolbarArray(".toolbars()");
	}
}

