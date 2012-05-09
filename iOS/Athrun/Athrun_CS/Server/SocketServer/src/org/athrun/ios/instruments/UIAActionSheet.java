/**
 * 
 */
package org.athrun.ios.instruments;

/**
 * @author ziyu.hch
 * 
 */
public class UIAActionSheet extends UIAElement {

	public UIAActionSheet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAActionSheet(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIAButton cancelButton() throws Exception {

		return UIAElementHelp.getButton(this.guid + ".cancelButton()");
	}

}
