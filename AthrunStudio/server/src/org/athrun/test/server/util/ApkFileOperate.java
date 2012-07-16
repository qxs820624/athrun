package org.athrun.test.server.util;

import java.io.File;
import java.io.InputStream;

public class ApkFileOperate {

	private String pathOf7zexe;

	public String getPathOf7zexe() {
		return pathOf7zexe;
	}

	public void setPathOf7zexe(String pathOf7zexe) {
		this.pathOf7zexe = pathOf7zexe;
	}

	public void removeSignInfo(String apkPath) {
		File file = new File(apkPath);
		String name = file.getAbsolutePath();
		String newName = name.replace(
				name.substring(name.length() - 4, name.length()), ".zip");
		File file1 = new File(newName);
		if (file1.exists()) {
			file1.delete();
		}
		file.renameTo(file1);
		String path = getPathOf7zexe() + " d " + file1.getAbsolutePath()
				+ " META-INF";
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec("cmd /c " + path);
			InputStream input = process.getInputStream();
			input.close();
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String backName = file1.getAbsolutePath();
		File file2 = new File(backName.replace(
				backName.substring(backName.length() - 4, backName.length()),
				".apk"));
		if (file2.exists()) {
			file2.delete();
		}
		file1.renameTo(file2);
	}

	public static void main(String[] args) {
		ApkFileOperate apkFileOperate = new ApkFileOperate();
		apkFileOperate.setPathOf7zexe("D:\\cmdtool\\7z.exe");
		apkFileOperate
				.removeSignInfo("D:\\com.tmall.wireless.activity.TMSplashActivity.apk");
	}
}
