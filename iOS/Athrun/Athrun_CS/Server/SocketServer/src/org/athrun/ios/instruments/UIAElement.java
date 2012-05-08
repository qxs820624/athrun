/**
 * 
 */
package org.athrun.ios.instruments;

import static org.athrun.ios.instruments.RunType.*;

/**
 * @author ziyu.hch
 * 
 */
public class UIAElement {

	public String guid = null;

	public UIAElement(String guid) {
		this.guid = guid;
	}

	/**
	 * get the element's parent element
	 * 
	 * @return parent元素
	 */
	public UIAElement parent() {

		String guid = getGuid(".parent()");
		return new UIAElement(guid);
	}

	/**
	 * 取得当前元素下的所有子元素
	 * 
	 * @return
	 */
	public UIAElement[] elements() {

		return elementArray(".elements()");
	}

	public UIAElement[] ancestry() {

		return elementArray(".ancestry()");
	}

	public UIAActivityIndicator[] activityIndicators() {

		return activityIndicatorArray(".activityIndicators()");
	}

	/**
	 * 取得当前元素下所有button类型的子元素
	 * 
	 * @return
	 */
	public UIAElement[] buttons() {

		return elementArray(".buttons()");
	}

	// -images
	public UIALink[] links() {

		return linkArray(".links()");
	}

	public UIANavigationBar navigationBar() {

		String guid = getGuid(".navigationBar()");
		return new UIANavigationBar(guid);
	}

	public UIANavigationBar[] navigationBars() {

		return navigationBarArray(".navigationBars()");
	}

	// pageIndicators

	public UIAPicker[] pickers() {

		return pickerArray(".pickers()");
	}

	// popover
	// progressIndicators

	public UIAScrollView[] scrollViews() {

		return scrollViewArray(".scrollViews()");
	}

	/**
	 * searchBars
	 * 
	 * @return UIASearchBar[]
	 */
	public UIASearchBar[] searchBars() {

		return searchBarArray(".searchBars()");

	}

	public UIASecureTextField[] secureTextFields() {

		return secureTextFieldArray(".secureTextFields()");

	}

	// segmentedControls
	// sliders

	public UIAStaticText[] staticTexts() {

		return staticTextArray(".staticTexts()");
	}

	public UIASwitch[] switches() {

		return switchArray(".switches()");
	}

	public UIATabBar tabBar() {

		String guid = getGuid(".tabBar()");
		return new UIATabBar(guid);
	}

	public UIATabBar[] tabBars() {

		return tabBarArray(".tabBars()");
	}

	public UIATableView[] tableViews() {

		return tableViewArray(".tableViews()");
	}

	public UIATextField[] textFields() {

		return textFieldArray(".textFields()");
	}

	public UIATextView[] textViews() {

		return textViewArray(".textViews()");
	}

	public UIAToolbar toolbar() {

		String guid = getGuid(".toolbar()");
		return new UIAToolbar(guid);
	}

	public UIAToolbar[] toolbars() {

		return toolbarArray(".toolbars()");
	}

	public UIAWebView[] webViews() {

		return webViewArray(".webViews()");
	}

	// -------------Gestures and Actions------------
	/**
	 * tap 操作
	 */
	public void tap() {
		MySocket.getVoid(this.guid + ".tap()");
	}

	public void touchAndHold(int seconds) {
		MySocket.getVoid(this.guid + ".touchAndHold(" + seconds + ")");
	}

	public void scrollToVisible() {
		MySocket.getVoid(this.guid + ".scrollToVisible()");
	}

	/*
	 * -----Determining Element State-----
	 * 
	 * Use these methods to determine whether an element is still valid.
	 * 
	 * isValid checkIsValid waitForInvalid hasKeyboardFocus isEnabled isVisible
	 */

	public Boolean isValid() {
		return MySocket.getBoolen(this.guid + ".isValid()");
	}

	public Boolean checkIsValid() {
		return MySocket.getBoolen(this.guid + ".checkIsValid()");
	}

	public Boolean waitForInvalid() {
		return MySocket.getBoolen(this.guid + ".waitForInvalid()");
	}

	public Boolean hasKeyboardFocus() {
		return MySocket.getBoolen(this.guid + ".hasKeyboardFocus()");
	}

	public Boolean isEnabled() {
		return MySocket.getBoolen(this.guid + ".isEnabled()");
	}

	public Boolean isVisible() {
		return MySocket.getBoolen(this.guid + ".isVisible()");
	}

	// ------Identifying Elements------

	public String label() {

		return MySocket.getText(this.guid + ".label()");
	}

	public String name() {

		return MySocket.getText(this.guid + ".name()");
	}

	public String value() {

		return MySocket.getText(this.guid + ".value()");
	}

	/**
	 * Returns a UIAElement if its name attribute matches a specified string.
	 * 
	 * @param name
	 * @return a UIAElement
	 */
	public UIAElement withName(String name) {

		String guid = getGuid(".withName('" + name + "')");
		return new UIAElement(guid);
	}

	// -----Logging Element Information-----

	public void logElement() {
		MySocket.getVoid(this.guid + ".logElement()");
	}

	public void logElementTree() {
		MySocket.getVoid(this.guid + ".logElementTree()");
	}

	private String getGuid(String method) {

		return DEBUG ? MySocket.getGuid(this.guid + method) : this.guid
				+ method;
	}

	// private methods
	protected String[] guidArray(String guid) {

		String[] guids = new String[ArrayLength];
		for (int i = 0; i < ArrayLength; i++) {
			String _guid = guid + "[" + i + "]";
			guids[i] = _guid;
		}
		return guids;
	}

	protected UIAElement[] elementArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAElement[] elements = new UIAElement[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAElement element = new UIAElement(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIAStaticText[] staticTextArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAStaticText[] elements = new UIAStaticText[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAStaticText element = new UIAStaticText(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIASwitch[] switchArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIASwitch[] elements = new UIASwitch[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIASwitch element = new UIASwitch(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIAPicker[] pickerArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAPicker[] elements = new UIAPicker[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAPicker element = new UIAPicker(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	protected UIANavigationBar[] navigationBarArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIANavigationBar[] elements = new UIANavigationBar[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIANavigationBar element = new UIANavigationBar(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIAScrollView[] scrollViewArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAScrollView[] elements = new UIAScrollView[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAScrollView element = new UIAScrollView(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIASearchBar[] searchBarArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIASearchBar[] elements = new UIASearchBar[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIASearchBar element = new UIASearchBar(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIASecureTextField[] secureTextFieldArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIASecureTextField[] elements = new UIASecureTextField[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIASecureTextField element = new UIASecureTextField(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	protected UIATabBar[] tabBarArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIATabBar[] elements = new UIATabBar[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIATabBar element = new UIATabBar(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIATableView[] tableViewArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIATableView[] elements = new UIATableView[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIATableView element = new UIATableView(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIATextField[] textFieldArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIATextField[] elements = new UIATextField[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIATextField element = new UIATextField(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIATextView[] textViewArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIATextView[] elements = new UIATextView[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIATextView element = new UIATextView(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	protected UIAToolbar[] toolbarArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAToolbar[] elements = new UIAToolbar[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAToolbar element = new UIAToolbar(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIAWebView[] webViewArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAWebView[] elements = new UIAWebView[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAWebView element = new UIAWebView(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIAActivityIndicator[] activityIndicatorArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIAActivityIndicator[] elements = new UIAActivityIndicator[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIAActivityIndicator element = new UIAActivityIndicator(guids[i]);
			elements[i] = element;
		}
		return elements;
	}

	private UIALink[] linkArray(String method) {

		String[] guids = DEBUG ? MySocket.getGuidArray(this.guid + method)
				: guidArray(this.guid + method);
		UIALink[] elements = new UIALink[guids.length];
		for (int i = 0; i < guids.length; i++) {
			UIALink element = new UIALink(guids[i]);
			elements[i] = element;
		}
		return elements;
	}
}