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
	public FramebufferInfo(String framebufferInfo) {
		String[] split = framebufferInfo.split(",");
		this.width = split[0];
		this.height = split[1];
		this.offset_red = split[2];
		this.offset_green = split[3];
		this.offset_blue = split[4];
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
