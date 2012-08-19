package org.athrun.ios.instrumentdriver.config;

/**
 * 
 * @author bingxin
 *
 */
import java.io.File;

import org.apache.log4j.Logger;

public class DriverUtil {

	private static Logger logger=Logger.getLogger(DriverUtil.class.getName());

	public static String getApp(){
		String app = Config.get("target_app");
		if (app == null) {
			throw new Error("athrun.properties中没有设置target_app属性");
		}
		if (!new File(app).exists()) {
				throw new Error("配置的app不存在");
		}
		logger.debug("target_app:"+app);
		return app;
	}

	public static Boolean isDebug(){
		String isDebug = Config.get("isDebug");
		if(isDebug.equalsIgnoreCase("true")){
			logger.debug("debug模式开启");
			return true;
		}else{
			logger.debug("debug模式关闭");
			return false;
		}
	}

	public static Boolean isSimulator() {
		String device = Config.get("isRunSimulator");
		String udid = Config.get("udid");
		if (device == null || !device.equalsIgnoreCase("false") || udid == null) {
			 logger.debug("使用模拟器运行");
			return true;
		} else {
			logger.debug("使用真机运行");
			return false;
		}
	}
	
	public static String getUDID(){
		String udid = Config.get("udid");
		logger.debug("udid:"+udid);
		return udid;
	}
	
	public static int getTimeOut(){
		int timeout = 0;
		try {
			timeout = Integer.parseInt(Config.get("timeout"));
		} catch (Exception e) {
			timeout = 60;// default timeout=60s
			logger.debug("未设置超时时间，默认超时时间为1分钟");
		}
		if (timeout < 30){
			timeout = 60;
			logger.debug("超时时间设置过小，超时时间自动设为1分钟");
		}
		timeout *= 1000; // ms
		return timeout;
	}
}
