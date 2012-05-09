/**
 * 
 */
package org.athrun.ios.instruments;

import static org.athrun.ios.instruments.RunType.ArrayLength;
import static org.athrun.ios.instruments.RunType.DEBUG;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ziyu.hch
 * 
 */
class UIAElementHelp {

	private static <T> Object elementsJSONArray(String guid, Class<T> classType) {

		String elementsJSON = MySocket.getJSONArray(guid);
		JSONArray jsonElementArray = JSONArray.fromObject(elementsJSON);

		return JSONArray.toArray(jsonElementArray, classType);
	}

	private static <T> Object getJSONObject(String guid, Class<T> classType) {

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
	 */
	public static UIAElement[] elementArray(String guid) {

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

	public static UIAButton[] buttonArray(String guid) {

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

	public static UIAStaticText[] staticTextArray(String guid) {

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

	public static UIASwitch[] switchArray(String guid) {

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

	public static UIAPicker[] pickerArray(String guid) {

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

	public static UIANavigationBar[] navigationBarArray(String guid) {

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

	public static UIAScrollView[] scrollViewArray(String guid) {

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

	public static UIASearchBar[] searchBarArray(String guid) {

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

	public static UIASecureTextField[] secureTextFieldArray(String guid) {

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

	public static UIATabBar[] tabBarArray(String guid) {

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

	public static UIATableView[] tableViewArray(String guid) {

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

	public static UIATextField[] textFieldArray(String guid) {

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

	public static UIATextView[] textViewArray(String guid) {

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

	public static UIAToolbar[] toolbarArray(String guid) {

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

	public static UIAWebView[] webViewArray(String guid) {

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

	public static UIAActivityIndicator[] activityIndicatorArray(String guid) {

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

	public static UIALink[] linkArray(String guid) {

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

	public static UIAWindow[] windowArray(String guid) {

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

	public static UIAElement getElement(String guid) {

		UIAElement element;
		if (DEBUG) {
			element = (UIAElement) getJSONObject(guid, UIAElement.class);
		} else {
			element = new UIAElement(guid);
		}
		return element;
	}

	public static UIAButton getButton(String guid) {

		UIAButton element;
		if (DEBUG) {
			element = (UIAButton) getJSONObject(guid, UIAButton.class);
		} else {
			element = new UIAButton(guid);
		}
		return element;
	}

	public static UIANavigationBar getNavigationBar(String guid) {

		UIANavigationBar element;
		if (DEBUG) {
			element = (UIANavigationBar) getJSONObject(guid,
					UIANavigationBar.class);
		} else {
			element = new UIANavigationBar(guid);
		}
		return element;
	}

	public static UIATabBar getTabBar(String guid) {

		UIATabBar element;
		if (DEBUG) {
			element = (UIATabBar) getJSONObject(guid, UIATabBar.class);
		} else {
			element = new UIATabBar(guid);
		}
		return element;
	}

	public static UIAToolbar getToolbar(String guid) {

		UIAToolbar element;
		if (DEBUG) {
			element = (UIAToolbar) getJSONObject(guid, UIAToolbar.class);
		} else {
			element = new UIAToolbar(guid);
		}
		return element;
	}

	public static UIAWindow getWindow(String guid) {

		UIAWindow element;
		if (DEBUG) {
			element = (UIAWindow) getJSONObject(guid, UIAWindow.class);
		} else {
			element = new UIAWindow(guid);
		}
		return element;
	}

	public static UIAActionSheet getActionSheet(String guid) {

		UIAActionSheet element;
		if (DEBUG) {
			element = (UIAActionSheet) getJSONObject(guid, UIAActionSheet.class);
		} else {
			element = new UIAActionSheet(guid);
		}
		return element;
	}

	public static UIAKeyboard getKeyboard(String guid) {

		UIAKeyboard element;
		if (DEBUG) {
			element = (UIAKeyboard) getJSONObject(guid, UIAKeyboard.class);
		} else {
			element = new UIAKeyboard(guid);
		}
		return element;
	}

	public static UIAStatusBar getStatusBar(String guid) {

		UIAStatusBar element;
		if (DEBUG) {
			element = (UIAStatusBar) getJSONObject(guid, UIAStatusBar.class);
		} else {
			element = new UIAStatusBar(guid);
		}
		return element;
	}
}
