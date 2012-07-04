/**
 * 
 */
package org.athrun.test.server.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.athrun.server.service.RemoteDeviceManager;
import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class TestHttpGet {

	@Test
	public void test() {
		String url = "http://10.13.123.22:8080/athrun/JpgGen.jpg?ts=0&serialNumber=015ED1E71701602D";
		boolean b = RemoteDeviceManager.httpGet(url);
		System.out.println(b);

		url = "http://10.13.123.22:8080/athrun/JpgGen.jpg?ts=0&serialNumber=CB511GY1JM";
		b = RemoteDeviceManager.httpGet(url);
		System.out.println(b);
	}
}
