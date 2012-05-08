package org.athrun.ios.instruments;


public class UIATarget {

	private String guid = null;

	public UIATarget() {
		String guid = RunType.DEBUG ? MySocket
				.getGuid("UIATarget.localTarget()") : "UIATarget.localTarget()";
		this.guid = guid;
	}

	private UIATarget(String guid) {
		this.guid = guid;
	}

	/**
	 * localTarget
	 */
	public static UIATarget localTarget() {

		String guid = RunType.DEBUG ? MySocket
				.getGuid("UIATarget.localTarget()") : "UIATarget.localTarget()";

		return new UIATarget(guid);
	}

	/**
	 * 
	 */
	public UIAApplication frontMostApp() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".frontMostApp()") : this.guid + ".frontMostApp()";
		return new UIAApplication(guid);
	}

	public void delay(int second) {
		MySocket.getVoid(this.guid + "delay(" + second + ")");
	}
}
