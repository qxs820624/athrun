package org.athrun.ios.instrumentdriver.config;

/**
 * @author bingxin
 */
import java.io.FileInputStream;
import java.util.Properties;
/**
 * 
 * @author bingxin
 *
 */
public class Config {

	private static Properties prop;
	public static String get(String key) {
		if (prop == null) {
			prop = new Properties();
			try {
				String file = new Config().getClass()
						.getResource("/athrun.properties").getPath();
				prop.load(new FileInputStream(file));
			} catch (Exception e) {
				throw new Error("未找到athrun.properties文件，请检查配置");
			}
		}
		String value = prop.getProperty(key);
		if (value == null||value.trim()=="")
			return null;
		return value.trim();
	}
}
