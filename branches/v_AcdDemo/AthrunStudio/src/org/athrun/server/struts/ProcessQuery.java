/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.athrun.server.struts;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @author taichan
 */

public class ProcessQuery extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1359518557933912505L;
	
	private String query;

	public List<Device> getDevices() {
		return Devices.getCurrent().getDevices();
	}
	
	public String execute() {
		return "SUCCESS";
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

}