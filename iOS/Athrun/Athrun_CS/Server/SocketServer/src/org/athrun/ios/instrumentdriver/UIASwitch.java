/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIASwitch extends UIAElement {

	public UIASwitch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIASwitch(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置 switch类型元素的开、关状态 true: on , false :off
	 * 
	 * @param value
	 */
	public void setValue(Boolean value) throws Exception {
		MySocket.getVoid(this.guid + ".setValue(" + value + ")");
	}

}
