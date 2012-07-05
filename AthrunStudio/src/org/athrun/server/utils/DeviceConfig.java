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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author taichan
 * 
 */
public class DeviceConfig {

	private static List<String> adjustDevices;

	public static void load(FileInputStream fileInputStream)
			throws ParserConfigurationException, SAXException, IOException {
		adjustDevices = new ArrayList<String>();
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
					adjustDevices.add(node.getTextContent().trim());
				}
			}
		}
	}

	public static boolean needAdjust(String sn) {
		return adjustDevices.contains(sn);
	}
}
