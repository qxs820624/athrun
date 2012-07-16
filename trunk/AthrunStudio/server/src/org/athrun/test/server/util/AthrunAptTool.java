package org.athrun.test.server.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import brut.androlib.AndrolibException;

public class AthrunAptTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		signApk("D:\\eclipse_jee_indigo\\workspace\\apttooltest\\debug_taobao_200001@taobao_android1.6_3.2.0.apk");
	}
	
	private static String getTargetDir(String apkFilePath, String fileName) {
		String apksDir = apkFilePath.substring(0,
				apkFilePath.lastIndexOf(fileName));
		return apksDir + fileName.substring(0, fileName.lastIndexOf(".apk"));
	}
	
	private static void dApkInternal(String apkFilePath, String targetDir) {
		String[] args = new String[] { "d", "-f", apkFilePath, targetDir };

		try {
			brut.apktool.Main.main(args);
		} catch (AndrolibException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("dApkInternal: dApkInternal end");
	}

	// ʹ��apktool���н�����apktool d InstrumentRemoteRunner.apk tmts
	public static void dApk(String apkFilePath) {

		File file = new File(apkFilePath);
		if (!file.exists()) {
			return;
		}

		String fileName = file.getName();

		String targetDir = getTargetDir(apkFilePath, fileName);

		dApkInternal(apkFilePath, targetDir);
	}

	// ʹ��apktool���н�����apktool d InstrumentRemoteRunner.apk tmts
	public static void dApk(File apkFile) {
		if (!apkFile.exists()) {
			return;
		}

		String apkFilePath = apkFile.getAbsolutePath();
		String fileName = apkFile.getName();

		String targetDir = getTargetDir(apkFilePath, fileName);

		dApkInternal(apkFilePath, targetDir);
	}
	
	private static void bApkInternal(String targetDir) {
		String[] args = new String[] { "b", targetDir };

		try {
			brut.apktool.Main.main(args);
		} catch (AndrolibException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ʹ��apktool���д�����apktool b tmts
	public static void bApk(String apkFilePath) {
		File file = new File(apkFilePath);
		if (!file.exists()) {
			return;
		}

		String fileName = file.getName();

		String targetDir = getTargetDir(apkFilePath, fileName);

		bApkInternal(targetDir);
	}
	
	public static void bApk(File apkFile) {
		
		if (!apkFile.exists()) {
			return;
		}

		String apkFilePath = apkFile.getAbsolutePath();
		String fileName = apkFile.getName();

		String targetDir = getTargetDir(apkFilePath, fileName);

		bApkInternal(targetDir);
	}
	
	public static void signApkInternal(String apksDir, String targetFilePath) {
		String[] jarsignerCmd = new String[] {
				"jarsigner",
				"-keystore",
				apksDir + File.separator + "debug.keystore",
				"-storepass",
				"android",
				targetFilePath, "androiddebugkey" };

		File logFile = new File(apksDir + File.separator + "apttool.log");
		FileWriter fw = null;
		try {
			fw = new FileWriter(logFile);

			Process p = Runtime.getRuntime().exec(jarsignerCmd);

			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			char[] cbuf = new char[1024];
			int len = 0;

			while ((len = isr.read(cbuf)) >= 0) {
				String str = new String(cbuf, 0, len);
				fw.write(str);
			}

			// ��ͣ���Ҫ����䲻���٣�������ˣ��ᵼ��shell�ű�û�еõ�����ִ�У�
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO ������
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				fw.write(System.currentTimeMillis()
						+ ": catch interruptedexception: " + e.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			// close fw
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("sign over!!");
	}

	// ʹ��javaǩ��߶�apk����ǩ��
	// jarsigner -keystore debug.keystore -storepass android
	// taobao/dist/debug_taobao_200001@taobao_android1.6_3.2.0.apk
	// androiddebugkey
	public static void signApk(String apkFilePath) {
		File file = new File(apkFilePath);
		if (!file.exists()) {
			return;
		}

		String absolutePath = file.getAbsolutePath();
		String fileName = file.getName();

		String apksDir = absolutePath.substring(0,
				absolutePath.lastIndexOf(fileName) - 1);

		String targetDir = getTargetDir(apkFilePath, fileName);
		String targetFilePath = targetDir + File.separator + "dist" + File.separator
				+ fileName;
		signApkInternal(apksDir, targetFilePath);
	}
	
	public static void signApk(File apkFile) {
		
		if (!apkFile.exists()) {
			return;
		}

		String apkFilePath = apkFile.getAbsolutePath();
		String fileName = apkFile.getName();
		String apksDir = apkFilePath.substring(0,
				apkFilePath.lastIndexOf(fileName) - 1);
		
		String targetDir = getTargetDir(apkFilePath, fileName);
		String targetFilePath = targetDir + File.separator + "dist" + File.separator
				+ fileName;
		signApkInternal(apksDir, targetFilePath);
	}
	
	public static String getTargetApkPath(File apkFile) {
		if (!apkFile.exists()) {
			return "";
		}
		
		String apkFilePath = apkFile.getAbsolutePath();
		String fileName = apkFile.getName();
		
		String targetDir = getTargetDir(apkFilePath, fileName);
		return targetDir + File.separator + "dist" + File.separator	+ fileName;
	}
}
