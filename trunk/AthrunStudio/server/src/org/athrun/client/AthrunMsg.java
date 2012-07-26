package org.athrun.client;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class AthrunMsg {

	private String info;

	private byte[] pictureContent;
	
	private int pictureContentLength = -1;

	private boolean isPicture;

	public boolean isPicture() {
		return isPicture;
	}

	public void setPicture(boolean isPicture) {
		this.isPicture = isPicture;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public AthrunMsg() {
		this.isPicture = false;
	}

	public byte[] getPictureContent() {
		return pictureContent;
	}

	public void setPictureContent(byte[] pictureContent) {
		this.pictureContent = pictureContent;
	}
	
	public int getPictureContentLength() {
		return pictureContentLength;
	}

	public void setPictureContentLength(int pictureContentLength) {
		this.pictureContentLength = pictureContentLength;
	}

	/*
	 * // д��Ϣ buf.writeInt(info_data_length); buf.writeBytes(info_data);
	 * 
	 * // д�Ƿ���ͼƬ buf.writeInt(isPicture);
	 * 
	 * // дͼƬ if (res.isPicture()) { buf.writeInt(picture_data_length);
	 * buf.writeBytes(picture_data); }
	 */
	public static AthrunMsg decode(byte[] data) {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(data);

		int info_data_length = buf.readInt();
		byte[] info_data = new byte[info_data_length];
		buf.readBytes(info_data);

		int isPicture = buf.readInt();
		int picture_data_length = 0;
		byte[] picture_data = null;

		if (isPicture == 1) {
			picture_data_length = buf.readInt();
			picture_data = new byte[picture_data_length];
			buf.readBytes(picture_data);
		}

		AthrunMsg msg = new AthrunMsg();
		msg.setInfo(new String(info_data));
		msg.setPicture((isPicture == 1) ? true : false);
		msg.setPictureContent(picture_data);

		return msg;
	}

	public void encode(ChannelBuffer buf) {

		byte[] info_data = info.getBytes();
		int info_data_length = info_data.length;

		int int_isPicture = isPicture ? 1 : 0;

		byte[] picture_data = pictureContent;
		int picture_data_length = 0;
		
		if (pictureContentLength != -1) {
			picture_data_length = pictureContentLength;
		} else {
			picture_data_length = (picture_data == null) ? 0
					: picture_data.length;
		}
		

		int total_length = 0;

		if (isPicture) {
			total_length = 4 + info_data_length + 4 + 4 + picture_data_length;
		} else {
			total_length = 4 + info_data_length + 4;
		}
		buf.writeInt(total_length);

		// д��Ϣ
		buf.writeInt(info_data_length);
		buf.writeBytes(info_data);

		// д�Ƿ���ͼƬ
		buf.writeInt(int_isPicture);

		// дͼƬ
		if (isPicture) {
			buf.writeInt(picture_data_length);
			buf.writeBytes(picture_data, 0, picture_data_length);
		}
	}
}
