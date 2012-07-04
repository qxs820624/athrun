/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.athrun.server.struts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.InstallException;
import org.athrun.server.service.DeviceManager;

import com.opensymphony.xwork2.ActionSupport;

/** 
 * @author wuhe
 */
public class InstallTestApk extends ActionSupport {

	private static final long serialVersionUID = -1359518557933912505L;
	
	private String serialNumber;
	
	private List<String> apkFileNames;
	
	public String execute() {
		String webRoot = ServletActionContext.getServletContext().getRealPath("/");
		String apksPath = webRoot + File.separator + "apks";
		File apksFolder = new File(apksPath);
		
		if (apksFolder.exists()) {
			apkFileNames = new ArrayList<String>();
			
			String[] childFileNames = apksFolder.list();
			for(String fileName : childFileNames) {
				apkFileNames.add(fileName);
			}
		}
		
		// 暂时硬编码
		//String productPkgName = "com.taobao.taobao";
		
        // 重新安装到手机
        IDevice device = DeviceManager.getDeviceBySerialNumber(serialNumber);
		if (device != null) {
			// 安装操作
		}
		
		//TODO: 把test apk 重新打包
		
		return "SUCCESS";
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	

	public List<String> getApkFileNames() {
		return apkFileNames;
	}

	public void setApkFileNames(List<String> apkFileNames) {
		this.apkFileNames = apkFileNames;
	}
}