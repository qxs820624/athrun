/**
 * 
 */
package org.athrun.ios.instruments;

/**
 * @author ziyu.hch
 * 
 */
public class UIAActionSheet extends UIAElement {

	/**
	 * @param guid
	 */
	public UIAActionSheet(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIAButton cancelButton() {

		// String guid = RunType.DEBUG ? MySocket.getGuid(this.guid +
		// ".cancelButton()") : this.guid + ".cancelButton()";
		return new UIAButton(this.guid + ".cancelButton()");
	}

}
