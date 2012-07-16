package org.athrun.server.struts;

import java.io.BufferedInputStream;     
import java.io.BufferedOutputStream;     
import java.io.File;     
import java.io.FileInputStream;     
import java.io.FileOutputStream;     
import java.io.IOException;
import java.io.InputStream;     
import java.io.OutputStream;     
   
    
import org.apache.struts2.ServletActionContext;     
import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.InstallException;
import org.athrun.ddmlib.ShellCommandUnresponsiveException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.adb.OutputStreamShellOutputReceiver;
import org.athrun.server.service.DeviceManager;
import org.athrun.test.server.util.ActivityManager;
import org.athrun.test.server.util.ApkFileOperate;
import org.athrun.test.server.util.AppActivityContainer;
import org.athrun.test.server.util.AthrunAptTool;
    
import com.opensymphony.xwork2.ActionSupport;     
import org.athrun.test.server.util.ManifestReaderWriter;
    
public class FileUploadAction extends ActionSupport {     
         
    private static final long serialVersionUID = 572146812454l;     
    
    private File upload;     
    
    private String uploadContentType;     
    
    private String uploadFileName; 
    
    private String serialNumber;
    
    private String packageName;
    
    private String activityName;
    
    public String getUploadContentType() {     
        return uploadContentType;     
    }     
    
    public void setUploadContentType(String uploadContentType) {     
        this.uploadContentType = uploadContentType;     
    }     
    
    public File getUpload() {     
        return upload;     
    }     
    
    public void setUpload(File upload) {     
        this.upload = upload;     
    }     
    
    public String getUploadFileName() {     
        return uploadFileName;     
    }     
    
    public void setUploadFileName(String uploadFileName) {     
        this.uploadFileName = uploadFileName;     
    }     
    
    public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	private static void copy(File src, File dst) { 
    	
    	System.out.println("src file length: " + src.length());
        try {     
            InputStream in = null;     
            OutputStream out = null; 
            int i = 0;
            try {     
                in = new BufferedInputStream(new FileInputStream(src));     
                out = new BufferedOutputStream(new FileOutputStream(dst));     
                byte[] buffer = new byte[1024*10];     
                while ((i = in.read(buffer)) > 0) {     
                    out.write(buffer, 0, i);     
                }     
            } finally {     
                if (null != in) {     
                    in.close();     
                }     
                if (null != out) {     
                    out.close();     
                }     
            }     
        } catch (Exception e) {     
            e.printStackTrace();     
        }     
    }     
    
    @Override    
    public String execute() {     
        System.out.println(uploadFileName);     
             
        String imageFileName = uploadFileName;   
        String apksDir = ServletActionContext.getServletContext()     
                .getRealPath("/apks");
        File imageFile = new File(apksDir + File.separator + imageFileName);     
        copy(upload, imageFile);
        
        // 五和：因为重新打包，有的包会出现失败的情形，换一种思路，采用7z.exe来去除签名信息
        ApkFileOperate apkFileOperate = new ApkFileOperate();
		apkFileOperate.setPathOf7zexe(apksDir + File.separator + "7z.exe");
		apkFileOperate.removeSignInfo(imageFile.getAbsolutePath());
        
		System.out.println("remove sign info......");
		
		// 重新签名
        //AthrunAptTool.signApk(imageFile);
		AthrunAptTool.signApkInternal(apksDir, imageFile.getAbsolutePath());
        
        // 解包
        AthrunAptTool.dApk(imageFile);
        
        String imageFilePath = imageFile.getAbsolutePath();
        
        
        String productManifestDir = imageFilePath.substring(0, imageFilePath.lastIndexOf(".apk"));
        String productManifestPath = productManifestDir + File.separator + "AndroidManifest.xml";
        String productPkgName = ManifestReaderWriter.getProductPkgName(productManifestPath);
        
        
        
        // 从AndroidManifest.xml中读取activity信息，存入ActivityManager单例
        AppActivityContainer aac = ManifestReaderWriter.getActivityEntities(productManifestPath);
		if (aac != null) {
			ActivityManager.getInstance().put(serialNumber, aac);
			
			String appName = ManifestReaderWriter.getAppName(productManifestPath);
			String realAppName = ManifestReaderWriter.getRealAppName(productManifestDir, appName);
			
			String appIconName = ManifestReaderWriter.getAppIconName(productManifestPath);
			String realIconPath = ManifestReaderWriter.getRealIconPath(productManifestDir, appIconName);
			
			aac.setAppName(realAppName);
			aac.setAppIconPath(realIconPath);
		}
        
        File tmtsApkFile = new File(apksDir + File.separator + "InstrumentRemoteRunner.apk");
        // 解包
        AthrunAptTool.dApk(tmtsApkFile);
        
        String tmtsApkFilePath = tmtsApkFile.getAbsolutePath();
        String tmtsManifestDir = tmtsApkFilePath.substring(0, tmtsApkFilePath.lastIndexOf(".apk"));
        String tmtsManifestPath = tmtsManifestDir + File.separator + "AndroidManifest.xml";
        // 修改被测试包名
        ManifestReaderWriter.modifyTargetPackage(tmtsManifestPath, productPkgName);
        
        // 重新打包
        AthrunAptTool.bApk(tmtsApkFile);
        
        // 签名
        AthrunAptTool.signApk(tmtsApkFile);
        
        
        // 下面将程序安装到手机
        
        // 重新安装到手机
        //String serialNumber = "SH14MTJ01900";
        System.out.println("serialNumber: " + serialNumber);
        String modifiedProductApkPath = AthrunAptTool.getTargetApkPath(imageFile);
        String modifiedTmtsApkPath = AthrunAptTool.getTargetApkPath(tmtsApkFile);
        
        IDevice device = DeviceManager.getDeviceBySerialNumber(serialNumber);
		if (device != null) {
			
			try {
				device.installPackage(imageFile.getAbsolutePath(),true);
				//device.installPackage(modifiedProductApkPath, true);
				device.installPackage(modifiedTmtsApkPath, true);
			} catch (InstallException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        return SUCCESS;     
    }     
    
}  