/**
 * 
 */
package org.athrun.android.result.kelude;

import org.athrun.android.result.junit.ErrorNode;
import org.athrun.android.result.junit.Testcase;

import com.ctc.wstx.util.StringUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author taichan
 * 
 */
@XStreamAlias("result")
public class Result {
	private String identifier;
	private String log;
	private String error_msg;
	private String error_stacktrace;
	private String result;
	private String duration;
	private String browser;

	/**
	 * @param tc
	 */
	public Result(Testcase tc) {
		// TODO Auto-generated constructor stub
		ErrorNode error = tc.getError();
		if (error.getContent().split(":").length > 0) {
			this.error_msg = error.getContent().split(":")[0];
		} else {
			this.error_msg = "";
		}
		this.error_stacktrace = error.getContent();
		this.identifier = tc.getClassname() + "#" + tc.getName();
		this.log = "no log in junit report";
		this.result = error.getContent().isEmpty() ? "pass" : "fail";
		this.duration = tc.getTime();
		this.browser = "";
	}
}
