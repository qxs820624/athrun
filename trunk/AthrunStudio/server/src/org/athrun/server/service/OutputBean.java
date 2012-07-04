/**
 * 
 */
package org.athrun.server.service;

import java.io.OutputStream;

/**
 * @author taichan
 *
 */
public class OutputBean {

	private String serialNumber;
	private OutputStream output;

	/**
	 * @param output
	 * @param serialNumber
	 */
	public OutputBean(OutputStream output, String serialNumber) {
		// TODO Auto-generated constructor stub
		this.output = output;
		this.serialNumber = serialNumber;				
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the output
	 */
	public OutputStream getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(OutputStream output) {
		this.output = output;
	}

}
