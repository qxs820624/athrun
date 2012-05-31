/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIASlider extends UIAElement {

	public UIASlider() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIASlider(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public void dragToValue(double d) throws Exception {
		MySocket.getVoid(this.guid + ".dragToValue(" + d + ")");
	}

}
