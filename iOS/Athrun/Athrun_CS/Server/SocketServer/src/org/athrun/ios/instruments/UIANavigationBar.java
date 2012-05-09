/**
 * 
 */
package org.athrun.ios.instruments;

/**
 * @author ziyu.hch
 * 
 */
public class UIANavigationBar extends UIAElement {

	/**
	 * @param guid
	 */
	public UIANavigationBar(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIANavigationBar() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UIAButton leftButton() {

		return UIAElementHelp.getButton(this.guid + ".leftButton()");
	}

	public UIAButton rightButton() {

		return UIAElementHelp.getButton(this.guid + ".rightButton()");
	}

}
