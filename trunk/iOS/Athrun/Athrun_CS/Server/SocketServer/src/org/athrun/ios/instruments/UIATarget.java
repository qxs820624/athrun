package org.athrun.ios.instruments;

public class UIATarget {

	private String guid = null;

	public UIATarget() {

		this.guid = "UIATarget.localTarget()";
	}

	/**
	 * localTarget
	 */
	public static UIATarget localTarget() {

		return new UIATarget();
	}

	/**
	 * 
	 */
	public UIAApplication frontMostApp() {

		return new UIAApplication(this.guid + ".frontMostApp()");
	}

	public void delay(int second) throws Exception {
		MySocket.getVoid(this.guid + ".delay(" + second + ")");
	}
}
