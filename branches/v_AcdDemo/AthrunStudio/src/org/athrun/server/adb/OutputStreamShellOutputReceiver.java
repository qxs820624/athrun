/**
 * 
 */
package org.athrun.server.adb;

import java.io.IOException;
import java.io.OutputStream;

import org.athrun.ddmlib.IShellOutputReceiver;


/**
 * @author taichan
 *
 */
public class OutputStreamShellOutputReceiver implements IShellOutputReceiver {

	OutputStream os;
	
	public OutputStreamShellOutputReceiver(OutputStream os) {
		this.os = os;
	}
	
	public boolean isCancelled() {
		return false;
	}
	
	public void flush() {
	}
	
	public void addOutput(byte[] buf, int off, int len) {
		try {
			os.write(buf,off,len);
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
