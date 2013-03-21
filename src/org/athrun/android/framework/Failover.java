/**
 * 
 */
package org.athrun.android.framework;

/**
 * for kelude use only;
 * <p>
 * 
 * @author jason.wuq
 */
public @interface Failover {
	int retryTimes() default 1;
}
