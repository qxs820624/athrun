package org.athrun.android.framework.transform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.athrun.android.framework.transform.action.IAction;

public class AthrunTestCaseCreator {
	private List<File> xmlFiles = new ArrayList<File>();
	private String xmlFilePath;
	private String javaFilePath;
	
	public AthrunTestCaseCreator(String xmlFilePath, String javaFilePath) {
		this.xmlFilePath = xmlFilePath;
		this.javaFilePath = javaFilePath;
	}
	
	public void createTestCase() {
		getAllFiles();
		for (File xmlFile : xmlFiles) {
			AthrunTestCase testCase = new AthrunTestCase(xmlFile);
			testCase.toJavaFile(this.javaFilePath);
		}
	}
	
	private List<File> getAllFiles() {
		File root = new File(this.xmlFilePath);
		File[] files = root.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			this.xmlFiles.add(files[i]);
		}
		
		return this.xmlFiles;
	}
	
	private static void printlnUsage() {
		
	}
	
	public static void main(String[] args) {
		if (0 == args.length) {
			printlnUsage();
		}
	}

}
