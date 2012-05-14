/**
 * 
 */
package org.athrun.test.server.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author taichan
 *
 */
public class regxTest {

	@Test
	public void test() {
		String a = "127.0.0.1:8080/AthrunTest".replaceFirst(".+?:", "t-taichan" + ":");
		System.out.println(a);
	}

}
