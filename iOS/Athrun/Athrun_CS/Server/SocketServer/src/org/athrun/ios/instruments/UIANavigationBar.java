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

	public UIAButton leftButton() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".leftButton") : this.guid + ".leftButton";

		return new UIAButton(guid);
	}

	public UIAButton rightButton() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".rightButton") : this.guid + ".rightButton";

		return new UIAButton(guid);
	}

}
