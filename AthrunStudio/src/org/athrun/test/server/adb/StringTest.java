/**
 * 
 */
package org.athrun.test.server.adb;

import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class StringTest {

	@Test
	public void test() {
		String a = String.format("%03d", 3);
		String b = String.format("%03d", 100);
		System.out.println(a);
		System.out.println(b);
	}

}
