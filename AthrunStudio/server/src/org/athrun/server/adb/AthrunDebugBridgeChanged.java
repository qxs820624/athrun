/**
 * 
 */
package org.athrun.server.adb;

import org.athrun.ddmlib.AndroidDebugBridge;
import org.athrun.ddmlib.AndroidDebugBridge.IDebugBridgeChangeListener;

/**
 * @author taichan
 * 
 */
public class AthrunDebugBridgeChanged implements IDebugBridgeChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.ddmlib.AndroidDebugBridge.IDebugBridgeChangeListener#
	 * bridgeChanged(com.android.ddmlib.AndroidDebugBridge)
	 */
	@Override
	public void bridgeChanged(AndroidDebugBridge bridge) {
		// TODO Auto-generated method stub
		System.out.println("bridgeChanged...");
	}

}
