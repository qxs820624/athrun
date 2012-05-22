/**
 * 
 */
package org.athrun.ios.instrumentdriver;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	protected String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
	 * 根据元素的显示文本查找元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @return 返回查找到满足条件的第一个元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	public UIAElement findElementByText(String text) throws Exception {

		return this.findElementByText(text, UIAElement.class);

	}

	/**
	 * 根据元素的显示文本和元素类型查找指定元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param elmentType
	 *            要查找元素的类型 ，如：UIABuuton.class
	 * @return 返回查找到满足条件的第一个元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	public <T> T findElementByText(String text, Class<T> elmentType)
			throws Exception {

		return this.findElementByText(text, 0, elmentType);
	}

	/**
	 * 根据元素的显示文本和索引查找指定元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param index
	 *            满足 text、 elementType 条件的第几个元素，第一个为 0
	 * @return 返回查找到满足条件的元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	public UIAElement findElementByText(String text, int index)
			throws Exception {

		return this.findElementByText(text, index, UIAElement.class);
	}

	/**
	 * 根据元素的显示文本和元素类型查找指定元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param elmentType
	 *            要查找元素的类型 ，如：UIABuuton.class
	 * @param index
	 *            满足 text、 elementType 条件的第几个元素，第一个为 0
	 * @return 返回查找到满足条件的元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	@SuppressWarnings("unchecked")
	public <T> T findElementByText(String text, int index,
			Class<T> elmentType) throws Exception {

		String[] type = elmentType.getName().split("\\.");
		String eType = type[type.length - 1];
		// invoke function findElement(root, text, index, elementType).
		String elementJSON = MySocket.getText("findElement('" + this.guid
				+ "','" + text + "'," + index + ",'" + eType + "')");

		if (elementJSON.equals("UIAElementNil")) {
			String exception = elementJSON + "  元素 " + this.guid
					+ " 下，根据条件: text ='" + text + "',index =" + index
					+ ",elmentType ='" + eType + "' 未能找到对应元素!";
			System.err.println(exception);
			throw new Exception(exception);
		}

		JSONObject element = JSONObject.fromObject(elementJSON);

		return (T) JSONObject.toBean(element, elmentType);
	}

	/**
	 * 根据元素的显示文本查找当前元素下所有满足条件的 element.
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @return UIAElement[] 返回查找到满足条件的元素数组
	 * @throws Exception
	 *             当根据指定的 text 未找到元素时，返回空JSON数组 "[]".
	 *             字符串导致转换成JSON数组对象失败，抛出异常结束用例
	 */
	public UIAElement[] findElementArrayByText(String text) throws Exception {
		return findElementArrayByText(text, UIAElement.class);
	}

	/**
	 * 根据元素的显示文本查找当前元素下所有满足条件的 element.
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param elmentType
	 *            要查找元素的类型 ，如：UIABuuton.class
	 * @return UIAElement[] 返回查找到满足条件的元素数组
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回空JSON数组 "[]".
	 *             字符串导致转换成JSON数组对象失败，抛出异常结束用例
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] findElementArrayByText(String text, Class<T> elmentType)
			throws Exception {

		String[] type = elmentType.getName().split("\\.");
		String eType = type[type.length - 1];

		String elementsJSON = MySocket.getText("findElements('" + this.guid
				+ "','" + text + "','" + eType + "')");

		JSONArray jsonElementArray = JSONArray.fromObject(elementsJSON);

		return (T[]) JSONArray.toArray(jsonElementArray, elmentType);
	}

	/**
	 * 树形结构打印出当前元素节点下的所有子元素
	 * 
	 * @throws Exception
	 *             C/S Socket 通讯异常
	 * 
	 */
	public void printElementTree() throws Exception {

		String elementTree = MySocket.getText("printElementTree('" + this.guid
				+ "')");

		System.out
				.println("-----------------------------------------------------");
		System.out.println("LogElementTree:");
		System.out.println(elementTree.replaceAll("###", "\n"));
		System.out
				.println("-----------------------------------------------------");
	}

	/**
	 * Get the element's parent element
	 * 
	 * @return parent element instance.
	 */
	public UIAElement parent() throws Exception {

		return UIAElementHelp.getElement(this.guid + ".parent()");
	}

	/**
	 * 取得当前元素下的所有子元素
	 * 
	 * @return
	 */
	public UIAElement[] elements() throws Exception {

		return UIAElementHelp.elementArray(this.guid + ".elements()");
	}

	public UIAElement[] ancestry() throws Exception {

		return UIAElementHelp.elementArray(this.guid + ".ancestry()");
	}

	public UIAActivityIndicator[] activityIndicators() throws Exception {

		return UIAElementHelp.activityIndicatorArray(this.guid
				+ ".activityIndicators()");
	}

	/**
	 * 取得当前元素下所有button类型的子元素
	 * 
	 * @return
	 */
	public UIAButton[] buttons() throws Exception {

		return UIAElementHelp.buttonArray(this.guid + ".buttons()");
	}

	// -images
	public UIALink[] links() throws Exception {

		return UIAElementHelp.linkArray(this.guid + ".links()");
	}

	public UIANavigationBar navigationBar() throws Exception {

		return UIAElementHelp.getNavigationBar(this.guid + ".navigationBar()");
	}

	public UIANavigationBar[] navigationBars() throws Exception {

		return UIAElementHelp.navigationBarArray(this.guid
				+ ".navigationBars()");
	}

	// pageIndicators

	public UIAPicker[] pickers() throws Exception {

		return UIAElementHelp.pickerArray(this.guid + ".pickers()");
	}

	// popover
	// progressIndicators

	public UIAScrollView[] scrollViews() throws Exception {

		return UIAElementHelp.scrollViewArray(this.guid + ".scrollViews()");
	}

	/**
	 * searchBars
	 * 
	 * @return UIASearchBar[]
	 */
	public UIASearchBar[] searchBars() throws Exception {

		return UIAElementHelp.searchBarArray(this.guid + ".searchBars()");
	}

	public UIASecureTextField[] secureTextFields() throws Exception {

		return UIAElementHelp.secureTextFieldArray(this.guid
				+ ".secureTextFields()");
	}

	// segmentedControls
	// sliders

	public UIAStaticText[] staticTexts() throws Exception {

		return UIAElementHelp.staticTextArray(this.guid + ".staticTexts()");
	}

	public UIASwitch[] switches() throws Exception {

		return UIAElementHelp.switchArray(this.guid + ".switches()");
	}

	public UIATabBar tabBar() throws Exception {

		return UIAElementHelp.getTabBar(this.guid + ".tabBar()");
	}

	public UIATabBar[] tabBars() throws Exception {

		return UIAElementHelp.tabBarArray(this.guid + ".tabBars()");
	}

	public UIATableView[] tableViews() throws Exception {

		return UIAElementHelp.tableViewArray(this.guid + ".tableViews()");
	}

	public UIATextField[] textFields() throws Exception {

		return UIAElementHelp.textFieldArray(this.guid + ".textFields()");
	}

	public UIATextView[] textViews() throws Exception {

		return UIAElementHelp.textViewArray(this.guid + ".textViews()");
	}

	public UIAToolbar toolbar() throws Exception {

		return UIAElementHelp.getToolbar(this.guid + ".toolbar()");
	}

	public UIAToolbar[] toolbars() throws Exception {

		return UIAElementHelp.toolbarArray(this.guid + ".toolbars()");
	}

	public UIAWebView[] webViews() throws Exception {

		return UIAElementHelp.webViewArray(this.guid + ".webViews()");
	}

	// -------------Gestures and Actions------------
	/**
	 * tap 操作
	 * 
	 * @throws Exception
	 */
	public void tap() throws Exception {

		MySocket.getVoid(this.guid + ".tap()");
	}

	public void touchAndHold(int seconds) throws Exception {

		MySocket.getVoid(this.guid + ".touchAndHold(" + seconds + ")");
	}

	public void scrollToVisible() throws Exception {

		MySocket.getVoid(this.guid + ".scrollToVisible()");
	}

	/*
	 * -----Determining Element State-----
	 * 
	 * Use these methods to determine whether an element is still valid.
	 * 
	 * isValid checkIsValid waitForInvalid hasKeyboardFocus isEnabled isVisible
	 */

	public Boolean isValid() throws Exception {

		return MySocket.getBoolen(this.guid + ".isValid()");
	}

	public Boolean checkIsValid() throws Exception {

		return MySocket.getBoolen(this.guid + ".checkIsValid()");
	}

	public Boolean waitForInvalid() throws Exception {

		return MySocket.getBoolen(this.guid + ".waitForInvalid()");
	}

	public Boolean hasKeyboardFocus() throws Exception {

		return MySocket.getBoolen(this.guid + ".hasKeyboardFocus()");
	}

	public Boolean isEnabled() throws Exception {

		return MySocket.getBoolen(this.guid + ".isEnabled()");
	}

	public Boolean isVisible() throws Exception {

		return MySocket.getBoolen(this.guid + ".isVisible()");
	}

	// ------Identifying Elements------

	public String label() throws Exception {

		return RunType.DEBUG ? this.label : MySocket.getText(this.guid
				+ ".label()");
	}

	public String name() throws Exception {

		return RunType.DEBUG ? this.name : MySocket.getText(this.guid
				+ ".name()");
	}

	public String value() throws Exception {

		return RunType.DEBUG ? this.value : MySocket.getText(this.guid
				+ ".value()");
	}

	/**
	 * Returns a UIAElement if its name attribute matches a specified string.
	 * 
	 * @param name
	 * @return a UIAElement
	 */
	public UIAElement withName(String name) throws Exception {

		return UIAElementHelp.getElement(this.guid + ".withName('" + name
				+ "')");
	}

	// -----Logging Element Information-----

	public void logElement() throws Exception {
		MySocket.getVoid(this.guid + ".logElement()");
	}

	public void logElementTree() throws Exception {
		MySocket.getVoid(this.guid + ".logElementTree()");
	}
}