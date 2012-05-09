/**
 * 
 */
package org.athrun.ios.instruments;

/**
 * @author ziyu.hch
 * 
 */
public class UIAScrollView extends UIAElement {

	public UIAScrollView() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAScrollView(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public void scrollUp() {
		MySocket.getVoid(this.guid + ".scrollUp()");
	}

	public void scrollDown() {
		MySocket.getVoid(this.guid + ".scrollDown()");
	}

	public void scrollLeft() {
		MySocket.getVoid(this.guid + ".scrollLeft()");
	}

	public void scrollRight() {
		MySocket.getVoid(this.guid + ".scrollRight()");
	}

	public void scrollToElementWithName(String name) {

		MySocket.getVoid(this.guid + ".scrollToElementWithName('" + name + "')");
	}
}
