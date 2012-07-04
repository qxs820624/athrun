/**
 * 
 */
package org.athrun.test.server.adb;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import org.athrun.server.utils.InOutStructure;
import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class SendsockTest {

	@Test
	public void test() throws IOException {
		InOutStructure inOutStructure = new InOutStructure(5678);
		inOutStructure.GetOut().println("info"); // 取3位
		inOutStructure.GetOut().flush();
		byte[] b = new byte[50];
		DataInputStream in = inOutStructure.getIn();

		in.read(b);
		String string = new String(b).trim();

		System.out.println(string);

	}
}
