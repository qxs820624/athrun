/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.athrun.server.struts;

import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.athrun.server.utils.PortBean;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;

/**
 * 
 * @author Meyyappan Muthuraman
 */

public class HelloWorld extends ActionSupport {

	private String message;

	private String userName;

	private Date date;

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public HelloWorld() {
	}

	public String execute() {
		setMessage("Hello " + "123");
		return "SUCCESS";
	}

	public String abc() {
		setMessage("Hello " + getUserName());
		return SUCCESS;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		try {
			JSONObject fromObject = JSONObject.fromObject(new PortBean(1234,
					1234, "abc"));			
			return fromObject.toString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "12";

	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}