/**
 * 
 */
package org.athrun.test.server.adb;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;

import org.athrun.server.service.EventManager;
import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class FileTest {

	@Test
	public void test() throws URISyntaxException {
		File file = new File(EventManager.class.getResource("/event/InjectAgent.jar").toURI().getPath());
		System.out.println(file.exists());
		System.out.println(file.getAbsolutePath());
	}

}
