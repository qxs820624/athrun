/**
 * 
 */
package org.athrun.android.result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.athrun.android.result.junit.ErrorNode;

import org.athrun.android.result.junit.Testcase;
import org.athrun.android.result.junit.Testsuite;
import org.athrun.android.result.junit.Testsuites;
import org.athrun.android.result.kelude.Result;
import org.athrun.android.result.kelude.Results;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class JunitKeludeLogConverter {
	
//	public static void main(String[] args) throws IOException {
//		File file = new File("C:/AthrunLog/results_187_2213_1303124.xml");
//		InputStream is = new FileInputStream(file);		
//		File file2 = new File("C:/AthrunLog/1.xml");
//		FileOutputStream fo = new FileOutputStream(file2);		
//		convert(is, fo);
//		is.close();
//		fo.close();
//		
//		FileUtils.copyFile(file2, file);
//	}

	public static void convert(InputStream junitFile, OutputStream keludeFile) throws IOException {
		XStream xstreamJunit = new XStream();
		xstreamJunit.processAnnotations(Testsuites.class);
		xstreamJunit.processAnnotations(Testsuite.class);
		xstreamJunit.processAnnotations(Testcase.class);
		xstreamJunit.processAnnotations(ErrorNode.class);

		XStream xstreamKelude = new XStream();
		xstreamKelude.processAnnotations(Results.class);
		xstreamKelude.processAnnotations(Result.class);

		Testsuites tss = (Testsuites) xstreamJunit.fromXML(junitFile);

		Results keludeResults = new Results();

		for (Testsuite suite : tss.getTestSuites()) {
			for (Testcase tc : suite.getTestcases()) {
				keludeResults.add(new Result(tc));
			}
		}
		
		xstreamKelude.toXML(keludeResults, keludeFile);		
	}
}