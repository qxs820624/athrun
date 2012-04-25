/**
 * 
 */
package org.athrun.server.service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author taichan
 * 
 */
public class OutputManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param serialNumber
	 * 
	 */
	public void clear(String serialNumber) {
		// TODO Auto-generated method stub
		synchronized (map) {
			if (map.containsKey(serialNumber)) {
				map.get(serialNumber).clear();
				map.remove(serialNumber);
			}
		}
	}

	/**
	 * @param serialNumber
	 * @return
	 */
	public boolean isEmpty(String serialNumber) {
		// TODO Auto-generated method stub
		return !map.containsKey(serialNumber);
	}

	/**
	 * @param serialNumber
	 * @return
	 */
	public List<OutputStream> getOutputStream(String serialNumber) {
		// TODO Auto-generated method stub
		return map.get(serialNumber);
	}

	/**
	 * @param outputBean
	 */
	public void add(OutputBean outputBean) {
		// TODO Auto-generated method stub
		String sn = outputBean.getSerialNumber();
		if(!lockMap.containsKey(sn)){
			lockMap.put(sn, new Object());
		}
		synchronized (map) {
			if (map.containsKey(sn)) {
				map.get(sn).add(outputBean.getOutput());
			} else {
				ArrayList<OutputStream> outList = new ArrayList<OutputStream>();
				outList.add(outputBean.getOutput());
				map.put(sn, outList);
			}
		}
	}
	
	public Object getlock(String serialNumber){
		return lockMap.get(serialNumber);
	}

	private Map<String, List<OutputStream>> map = new HashMap<String, List<OutputStream>>();

	private Map<String, Object> lockMap = new HashMap<String, Object>();
}
