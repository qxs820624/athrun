package org.athrun.ios.instrumentdriver;

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

	/**
	 * 向上滑动窗口,滑动一个范围，200单位 即向上滑动大约半个屏幕的高度
	 * 
	 * @throws Exception
	 */
	public void scrollUp() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 150,y : 300}, {x : 150,y : 100}, 1)");
	}

	/**
	 * 向下滑动窗口,滑动一个范围，200单位 即向下滑动大约半个屏幕的高度
	 * 
	 * @throws Exception
	 */
	public void scrollDown() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 150,y : 100}, {x : 150,y : 300}, 1)");
	}

	/**
	 * 向左边滑动窗口,滑动一个范围，200单位 （360*480）一般是换页
	 * 
	 * @throws Exception
	 */
	public void scrollLeft() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 260,y : 200}, {x : 60,y : 200}, 1)");
	}

	/**
	 * 向右边滑动窗口,滑动一个范围，200 一般是换页
	 * 
	 * @throws Exception
	 */
	public void scrollRight() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 60,y : 200}, {x : 260,y : 200}, 1)");
	}
}
