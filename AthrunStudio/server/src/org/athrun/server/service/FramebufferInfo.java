/**
 * 
 */
package org.athrun.server.service;

/**
 * @author taichan
 * 
 */
public class FramebufferInfo {
	private String width;
	private String height;
	private String offset_red;
	private String offset_green;
	private String offset_blue;

	/**
	 * @param width
	 * @param height
	 * @param offset_red
	 * @param offset_green
	 * @param offset_blue
	 */
	public FramebufferInfo() {
		
	}
	
	// 避免解析失败导致的异常
	public void parseInfo(String framebufferInfo) {
		if (framebufferInfo == null) {
			return;
		}
		
		String width = "";
		String height = "";
		String offset_red = "";
		String offset_green = "";
		String offset_blue = "";
		boolean parseResult = false;
		
		try {
			String[] split = framebufferInfo.split(",");
			width = split[0];
			height = split[1];
			offset_red = split[2];
			offset_green = split[3];
			offset_blue = split[4];
			parseResult = true;
		} catch (ArrayIndexOutOfBoundsException ex) {
			//nothing to do
		}
		
		if (parseResult) {
			this.width = width;
			this.height = height;
			this.offset_red = offset_red;
			this.offset_green = offset_green;
			this.offset_blue = offset_blue;
		}
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @return the offset_red
	 */
	public String getOffset_red() {
		return offset_red;
	}

	/**
	 * @return the offset_green
	 */
	public String getOffset_green() {
		return offset_green;
	}

	/**
	 * @return the offset_blue
	 */
	public String getOffset_blue() {
		return offset_blue;
	}

	public String getSolution() {
		return width + " x " + height;
	}

}
