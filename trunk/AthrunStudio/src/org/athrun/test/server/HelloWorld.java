/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.athrun.test.server;

import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

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
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public HelloWorld() {
    }

    public String execute() {
        setMessage("Hello " + getUserName());
        return "SUCCESS";
    }
    
    public String abc(){
    	setMessage("Hello " + getUserName());
    	return SUCCESS;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
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
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}