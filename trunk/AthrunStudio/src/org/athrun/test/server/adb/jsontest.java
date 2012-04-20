/**
 * 
 */
package org.athrun.test.server.adb;

import static org.junit.Assert.*;

import net.sf.json.JSONObject;

import org.athrun.server.struts.HelloWorld;
import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class jsontest {

	@Test
	public void test() {
		HelloWorld h = new HelloWorld();
		h.setUserName("abc");
		JSONObject fromObject = JSONObject.fromObject(h);
		System.out.println(fromObject.toString(1));
	}

}
