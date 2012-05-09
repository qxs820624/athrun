/**
 * 
 */
package org.athrun.ios.instruments;

/**
 * @author ziyu.hch
 * 
 */
public class UIAElement {

	public UIAElement() {
	}

	public UIAElement(String guid) {
		this.guid = guid;
	}

	// -------------------------Bean property----------------------

	protected String guid;
	protected Rect rect;
	protected String name;
	protected String label;
	protected String value;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	// -----------------------------------------------------

	/**
	 * Get the element's parent element
	 * 
	 * @return parent element instance.
	 */
	public UIAElement parent() {

		return UIAElementHelp.getElement(this.guid + ".parent()");
	}

	/**
	 * 取得当前元素下的所有子元素
	 * 
	 * @return
	 */
	public UIAElement[] elements() {

		return UIAElementHelp.elementArray(this.guid + ".elements()");
	}

	public UIAElement[] ancestry() {

		return UIAElementHelp.elementArray(this.guid + ".ancestry()");
	}

	public UIAActivityIndicator[] activityIndicators() {

		return UIAElementHelp.activityIndicatorArray(this.guid
				+ ".activityIndicators()");
	}

	/**
	 * 取得当前元素下所有button类型的子元素
	 * 
	 * @return
	 */
	public UIAButton[] buttons() {

		return UIAElementHelp.buttonArray(this.guid + ".buttons()");
	}

	// -images
	public UIALink[] links() {

		return UIAElementHelp.linkArray(this.guid + ".links()");
	}

	public UIANavigationBar navigationBar() {

		return UIAElementHelp.getNavigationBar(this.guid + ".navigationBar()");
	}

	public UIANavigationBar[] navigationBars() {

		return UIAElementHelp.navigationBarArray(this.guid
				+ ".navigationBars()");
	}

	// pageIndicators

	public UIAPicker[] pickers() {

		return UIAElementHelp.pickerArray(this.guid + ".pickers()");
	}

	// popover
	// progressIndicators

	public UIAScrollView[] scrollViews() {

		return UIAElementHelp.scrollViewArray(this.guid + ".scrollViews()");
	}

	/**
	 * searchBars
	 * 
	 * @return UIASearchBar[]
	 */
	public UIASearchBar[] searchBars() {

		return UIAElementHelp.searchBarArray(this.guid + ".searchBars()");
	}

	public UIASecureTextField[] secureTextFields() {

		return UIAElementHelp.secureTextFieldArray(this.guid
				+ ".secureTextFields()");
	}

	// segmentedControls
	// sliders

	public UIAStaticText[] staticTexts() {

		return UIAElementHelp.staticTextArray(this.guid + ".staticTexts()");
	}

	public UIASwitch[] switches() {

		return UIAElementHelp.switchArray(this.guid + ".switches()");
	}

	public UIATabBar tabBar() {

		return UIAElementHelp.getTabBar(this.guid + ".tabBar()");
	}

	public UIATabBar[] tabBars() {

		return UIAElementHelp.tabBarArray(this.guid + ".tabBars()");
	}

	public UIATableView[] tableViews() {

		return UIAElementHelp.tableViewArray(this.guid + ".tableViews()");
	}

	public UIATextField[] textFields() {

		return UIAElementHelp.textFieldArray(this.guid + ".textFields()");
	}

	public UIATextView[] textViews() {

		return UIAElementHelp.textViewArray(this.guid + ".textViews()");
	}

	public UIAToolbar toolbar() {

		return UIAElementHelp.getToolbar(this.guid + ".toolbar()");
	}

	public UIAToolbar[] toolbars() {

		return UIAElementHelp.toolbarArray(this.guid + ".toolbars()");
	}

	public UIAWebView[] webViews() {

		return UIAElementHelp.webViewArray(this.guid + ".webViews()");
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

		return RunType.DEBUG ? this.label : MySocket.getText(this.guid
				+ ".label()");
	}

	public String name() {

		return RunType.DEBUG ? this.name : MySocket.getText(this.guid
				+ ".name()");
	}

	public String value() {

		return RunType.DEBUG ? this.value : MySocket.getText(this.guid
				+ ".value()");
	}

	/**
	 * Returns a UIAElement if its name attribute matches a specified string.
	 * 
	 * @param name
	 * @return a UIAElement
	 */
	public UIAElement withName(String name) {

		return UIAElementHelp.getElement(this.guid + ".withName('" + name
				+ "')");
	}

	// -----Logging Element Information-----

	public void logElement() {
		MySocket.getVoid(this.guid + ".logElement()");
	}

	public void logElementTree() {
		MySocket.getVoid(this.guid + ".logElementTree()");
	}
}