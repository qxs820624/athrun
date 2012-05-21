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
}
