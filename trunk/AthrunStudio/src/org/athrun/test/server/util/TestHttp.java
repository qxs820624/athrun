/**
 * 
 */
package org.athrun.test.server.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

/**
 * @author taichan
 *
 */
public class TestHttp {

	@Test
	public void test() {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.execute(new HttpGet("http://www.google.com/"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
