/**
 * 
 */
package org.athrun.test.server.util;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class ipTest {

	@Test
	public void test() throws UnknownHostException {
		InetAddress ia = InetAddress.getByName("10.13.40.111");
		System.out.println(ia.getHostName());
		
	}
}
