/**
 * 
 */
package org.athrun.server.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author taichan
 * 
 */
public class DeviceConfig {

	public static class Device {
		public Device(String name) {
			this.name = name;
		}

		public String name;
		public Boolean rgb;
		public Integer width;

	}

	private static List<Device> deviceConfig;

	public static void load(FileInputStream fileInputStream)
			throws ParserConfigurationException, SAXException, IOException {
		deviceConfig = new ArrayList<Device>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(fileInputStream);
		Element root = doc.getDocumentElement();// 返回文档根元素
		NodeList nodelist = root.getChildNodes(); // 获取子节点
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if (node.getNodeType() == Node.TEXT_NODE) {// 忽略空白元素
				continue;
			}
			String nodeName = node.getNodeName();
			if (nodeName.equals("device")) {
				if (node.getTextContent().length() > 0) {
					Device device = new Device(node.getTextContent().trim());
					NamedNodeMap attributes = node.getAttributes();
					if (attributes.getLength() > 0) {
						Node namedItemRGB = attributes.getNamedItem("rgb");
						if (namedItemRGB != null) {
							if (namedItemRGB.getTextContent().equalsIgnoreCase(
									"true")) {
								device.rgb = true;
							}
						}
						Node namedItemWidth = attributes.getNamedItem("width");
						if (namedItemWidth != null) {
							if (!namedItemWidth.getTextContent().isEmpty()) {
								device.width = Integer.parseInt(namedItemWidth
										.getTextContent());
							}
						}
					}
					deviceConfig.add(device);
				}
			}
		}
	}

	public static boolean needAdjust(String sn) {
		Device device = getDevice(sn);
		if (device != null) {
			return true;
		}
		return false;
	}

	private static Device getDevice(String sn) {
		for (Device d : deviceConfig) {
			if (d.name.equals(sn)) {
				return d;
			}
		}
		return null;
	}

	public static boolean getAdjustRGB(String sn) {
		Device device = getDevice(sn);
		if (true == device.rgb) {
			return true;
		}
		return false;
	}

	public static Integer getAdjustWidth(String sn) {
		Device device = getDevice(sn);
		return device.width;		
	}
}
