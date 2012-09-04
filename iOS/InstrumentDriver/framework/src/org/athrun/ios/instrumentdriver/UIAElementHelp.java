/**
 * 
 */
package org.athrun.ios.instrumentdriver;

import static org.athrun.ios.instrumentdriver.RunType.ArrayLength;
import static org.athrun.ios.instrumentdriver.RunType.DEBUG;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ziyu.hch
 * 
 */
class UIAElementHelp {

	private static <T> Object elementsJSONArray(String guid, Class<T> classType)
			throws Exception {

		String elementsJSON = MySocket.getJSONArray(guid);
		JSONArray jsonElementArray = JSONArray.fromObject(elementsJSON);

		return JSONArray.toArray(jsonElementArray, classType);
	}

	private static <T> Object getJSONObject(String guid, Class<T> classType)
			throws Exception {

		String elementJSON = MySocket.getJSONObject(guid);
		JSONObject element = JSONObject.fromObject(elementJSON);

		return JSONObject.toBean(element, classType);
	}

	private static String[] guidArray(String guid) {

		String[] guids = new String[ArrayLength];
		for (int i = 0; i < ArrayLength; i++) {
			String _guid = guid + "[" + i + "]";
			guids[i] = _guid;
		}
		return guids;
	}

	/**
	 * 返回UIAElement元素数组
	 * 
	 * @param guid
	 *            代表当前对象的脚本字符串
	 * 
	 * @return The UIAElement Array
	 * @throws Exception
	 */
	public static UIAElement[] elementArray(String guid) throws Exception {

		UIAElement[] elements;
		if (DEBUG) {
			elements = (UIAElement[]) elementsJSONArray(guid, UIAElement.class);

		} else {
			String[] guids = guidArray(guid);
			elements = new UIAElement[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAElement element = new UIAElement(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAButton[] buttonArray(String guid) throws Exception {

		UIAButton[] elements;
		if (DEBUG) {
			elements = (UIAButton[]) elementsJSONArray(guid, UIAButton.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAButton[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAButton element = new UIAButton(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAStaticText[] staticTextArray(String guid) throws Exception {

		UIAStaticText[] elements;
		if (DEBUG) {
			elements = (UIAStaticText[]) elementsJSONArray(guid,
					UIAStaticText.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAStaticText[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAStaticText element = new UIAStaticText(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIASwitch[] switchArray(String guid) throws Exception {

		UIASwitch[] elements;
		if (DEBUG) {
			elements = (UIASwitch[]) elementsJSONArray(guid, UIASwitch.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIASwitch[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIASwitch element = new UIASwitch(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAPageIndicator[] pageIndicatorArray(String guid)
			throws Exception {
		UIAPageIndicator[] elements = null;
		if (DEBUG) {
			elements = (UIAPageIndicator[]) elementsJSONArray(guid,
					UIAPageIndicator.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAPageIndicator[guids.length];
			for(int i = 0; i < guids.length; i++){
				UIAPageIndicator element = new UIAPageIndicator(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAPicker[] pickerArray(String guid) throws Exception {

		UIAPicker[] elements;
		if (DEBUG) {
			elements = (UIAPicker[]) elementsJSONArray(guid, UIAPicker.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAPicker[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAPicker element = new UIAPicker(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIANavigationBar[] navigationBarArray(String guid)
			throws Exception {

		UIANavigationBar[] elements;
		if (DEBUG) {
			elements = (UIANavigationBar[]) elementsJSONArray(guid,
					UIANavigationBar.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIANavigationBar[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIANavigationBar element = new UIANavigationBar(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAScrollView[] scrollViewArray(String guid) throws Exception {

		UIAScrollView[] elements;
		if (DEBUG) {
			elements = (UIAScrollView[]) elementsJSONArray(guid,
					UIAScrollView.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAScrollView[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAScrollView element = new UIAScrollView(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIASearchBar[] searchBarArray(String guid) throws Exception {

		UIASearchBar[] elements;
		if (DEBUG) {
			elements = (UIASearchBar[]) elementsJSONArray(guid,
					UIASearchBar.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIASearchBar[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIASearchBar element = new UIASearchBar(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIASecureTextField[] secureTextFieldArray(String guid)
			throws Exception {

		UIASecureTextField[] elements;
		if (DEBUG) {
			elements = (UIASecureTextField[]) elementsJSONArray(guid,
					UIASecureTextField.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIASecureTextField[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIASecureTextField element = new UIASecureTextField(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIATabBar[] tabBarArray(String guid) throws Exception {

		UIATabBar[] elements;
		if (DEBUG) {
			elements = (UIATabBar[]) elementsJSONArray(guid, UIATabBar.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIATabBar[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIATabBar element = new UIATabBar(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIATableView[] tableViewArray(String guid) throws Exception {

		UIATableView[] elements;
		if (DEBUG) {
			elements = (UIATableView[]) elementsJSONArray(guid,
					UIATableView.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIATableView[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIATableView element = new UIATableView(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIATextField[] textFieldArray(String guid) throws Exception {

		UIATextField[] elements;
		if (DEBUG) {
			elements = (UIATextField[]) elementsJSONArray(guid,
					UIATextField.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIATextField[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIATextField element = new UIATextField(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIATextView[] textViewArray(String guid) throws Exception {

		UIATextView[] elements;
		if (DEBUG) {
			elements = (UIATextView[]) elementsJSONArray(guid,
					UIATextView.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIATextView[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIATextView element = new UIATextView(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAToolbar[] toolbarArray(String guid) throws Exception {

		UIAToolbar[] elements;
		if (DEBUG) {
			elements = (UIAToolbar[]) elementsJSONArray(guid, UIAToolbar.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAToolbar[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAToolbar element = new UIAToolbar(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAWebView[] webViewArray(String guid) throws Exception {

		UIAWebView[] elements;
		if (DEBUG) {
			elements = (UIAWebView[]) elementsJSONArray(guid, UIAWebView.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAWebView[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAWebView element = new UIAWebView(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAActivityIndicator[] activityIndicatorArray(String guid)
			throws Exception {

		UIAActivityIndicator[] elements;
		if (DEBUG) {
			elements = (UIAActivityIndicator[]) elementsJSONArray(guid,
					UIAActivityIndicator.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAActivityIndicator[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAActivityIndicator element = new UIAActivityIndicator(
						guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIALink[] linkArray(String guid) throws Exception {

		UIALink[] elements;
		if (DEBUG) {
			elements = (UIALink[]) elementsJSONArray(guid, UIALink.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIALink[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIALink element = new UIALink(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAWindow[] windowArray(String guid) throws Exception {

		UIAWindow[] elements;
		if (DEBUG) {
			elements = (UIAWindow[]) elementsJSONArray(guid, UIAWindow.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAWindow[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAWindow element = new UIAWindow(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIATableCell[] cellsArray(String guid) throws Exception {

		UIATableCell[] elements;
		if (DEBUG) {
			elements = (UIATableCell[]) elementsJSONArray(guid,
					UIATableCell.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIATableCell[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIATableCell element = new UIATableCell(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAPickerWheel[] pickerWheelArray(String guid)
			throws Exception {

		UIAPickerWheel[] elements;
		if (DEBUG) {
			elements = (UIAPickerWheel[]) elementsJSONArray(guid,
					UIAPickerWheel.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIAPickerWheel[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIAPickerWheel element = new UIAPickerWheel(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIASlider[] sliderArray(String guid) throws Exception {

		UIASlider[] elements;
		if (DEBUG) {
			elements = (UIASlider[]) elementsJSONArray(guid, UIASlider.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIASlider[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIASlider element = new UIASlider(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIATableGroup[] tableGroup(String guid) throws Exception {

		UIATableGroup[] elements;
		if (DEBUG) {
			elements = (UIATableGroup[]) elementsJSONArray(guid,
					UIATableGroup.class);
		} else {
			String[] guids = guidArray(guid);
			elements = new UIATableGroup[guids.length];
			for (int i = 0; i < guids.length; i++) {
				UIATableGroup element = new UIATableGroup(guids[i]);
				elements[i] = element;
			}
		}
		return elements;
	}

	public static UIAElement getElement(String guid) throws Exception {

		UIAElement element;
		if (DEBUG) {
			element = (UIAElement) getJSONObject(guid, UIAElement.class);
		} else {
			element = new UIAElement(guid);
		}
		return element;
	}

	public static UIAButton getButton(String guid) throws Exception {

		UIAButton element;
		if (DEBUG) {
			element = (UIAButton) getJSONObject(guid, UIAButton.class);
		} else {
			element = new UIAButton(guid);
		}
		return element;
	}

	public static UIANavigationBar getNavigationBar(String guid)
			throws Exception {

		UIANavigationBar element;
		if (DEBUG) {
			element = (UIANavigationBar) getJSONObject(guid,
					UIANavigationBar.class);
		} else {
			element = new UIANavigationBar(guid);
		}
		return element;
	}

	public static UIAPopover getPopover(String guid) throws Exception {
		UIAPopover element;
		if (DEBUG) {
			element = (UIAPopover) getJSONObject(guid, UIAPopover.class);
		} else {
			element = new UIAPopover(guid);
		}
		return element;
	}

	public static UIATabBar getTabBar(String guid) throws Exception {

		UIATabBar element;
		if (DEBUG) {
			element = (UIATabBar) getJSONObject(guid, UIATabBar.class);
		} else {
			element = new UIATabBar(guid);
		}
		return element;
	}

	public static UIAToolbar getToolbar(String guid) throws Exception {

		UIAToolbar element;
		if (DEBUG) {
			element = (UIAToolbar) getJSONObject(guid, UIAToolbar.class);
		} else {
			element = new UIAToolbar(guid);
		}
		return element;
	}

	public static UIAWindow getWindow(String guid) throws Exception {

		UIAWindow element;
		if (DEBUG) {
			element = (UIAWindow) getJSONObject(guid, UIAWindow.class);
		} else {
			element = new UIAWindow(guid);
		}
		return element;
	}

	public static UIAActionSheet getActionSheet(String guid) throws Exception {

		UIAActionSheet element;
		if (DEBUG) {
			element = (UIAActionSheet) getJSONObject(guid, UIAActionSheet.class);
		} else {
			element = new UIAActionSheet(guid);
		}
		return element;
	}

	public static UIAKeyboard getKeyboard(String guid) throws Exception {

		UIAKeyboard element;
		if (DEBUG) {
			element = (UIAKeyboard) getJSONObject(guid, UIAKeyboard.class);
		} else {
			element = new UIAKeyboard(guid);
		}
		return element;
	}

	public static UIAStatusBar getStatusBar(String guid) throws Exception {

		UIAStatusBar element;
		if (DEBUG) {
			element = (UIAStatusBar) getJSONObject(guid, UIAStatusBar.class);
		} else {
			element = new UIAStatusBar(guid);
		}
		return element;
	}

	public static UIAAlert getAlert(String guid) throws Exception {

		UIAAlert element;
		if (DEBUG) {
			element = (UIAAlert) getJSONObject(guid, UIAAlert.class);
		} else {
			element = new UIAAlert(guid);
		}
		return element;
	}
}
