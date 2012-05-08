/**
 * 
 */
package org.athrun.ios.instruments;

import static org.athrun.ios.instruments.RunType.*;

/**
 * @author ziyu.hch
 * 
 */
public class UIAApplication {

	public String guid = null;

	public UIAApplication(String guid) {
		// TODO Auto-generated constructor stub
		this.guid = guid;
	}

	public UIAWindow[] windows() {

		return elementArray(".windows()");
	}

	/** 
	 *  
	 */
	public UIAWindow mainWindow() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".mainWindow()") : this.guid + ".mainWindow()";

		return new UIAWindow(guid);
	}

	public UIAActionSheet actionSheet() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".actionSheet()") : this.guid + ".actionSheet()";
		return new UIAActionSheet(guid);
	}

	public UIAKeyboard keyboard() {
		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".keyboard()") : this.guid + ".keyboard()";
		return new UIAKeyboard(guid);
	}

	public UIANavigationBar navigationBar() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".navigationBar()") : this.guid + ".navigationBar()";
		return new UIANavigationBar(guid);
	}

	public UIAStatusBar statusBar() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid
				+ ".statusBar()") : this.guid + ".statusBar()";
		return new UIAStatusBar(guid);
	}

	/**
	 * tabBar
	 */
	public UIATabBar tabBar() {

		String guid = RunType.DEBUG ? MySocket.getGuid(this.guid + ".tabBar()")
				: this.guid + ".tabBar()";

		return new UIATabBar(guid);
	}

	public UIAToolbar toolbar() {

		String guid = RunType.DEBUG ? MySocket
				.getGuid(this.guid + ".toolbar()") : this.guid + ".toolbar()";

		return new UIAToolbar(guid);
	}

	private String[] guidArray(String guid) {

		String[] guids = new String[ArrayLength];
		for (int i = 0; i < ArrayLength; i++) {
			String _guid = guid + "[" + i + "]";
			guids[i] = _guid;
		}
		return guids;
	}

	private UIAWindow[] elementArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAWindow[] elements = new UIAWindow[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAWindow button = new UIAWindow(guids[i]);
			elements[i] = button;
		}
		return elements;
	}
}
