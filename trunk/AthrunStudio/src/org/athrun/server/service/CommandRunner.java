/**
 * 
 */
package org.athrun.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.athrun.ddmlib.IDevice;

/**
 * @author taichan
 * 
 */
public class CommandRunner {

	/**
	 * @param device
	 * @param capturePort
	 * @param i
	 */
	public static void createForward(IDevice device, int capturePort,
			int remotePort) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("adb -s " + device.getSerialNumber());
		sb.append(" forward");
		sb.append(" tcp:" + capturePort);
		sb.append(" tcp:" + remotePort);
		try {
			String result = run(sb.toString());
			System.out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String run(String command) throws IOException,
			InterruptedException {
		StringBuilder output = new StringBuilder();
		System.out.println("执行命令：" + command);
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader er = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));

		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.trim().length() > 0) {
				output.append(line).append("\n");
			}
		}

		while ((line = er.readLine()) != null) {
			if (line.trim().length() > 0) {
				output.append(line).append("\n");
			}
		}

		p.waitFor();

		er.close();
		br.close();
		String result = output.toString();
		return result;
	}

	/**
	 * @param device
	 * @param absolutePath
	 * @param remotepath
	 */
	public static void pushFile(IDevice device, String absolutePath,
			String remotepath) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();

		sb.append("adb -s " + device.getSerialNumber());
		sb.append(" push");
		sb.append(" \"" + absolutePath + "\"");
		sb.append(" \"" + remotepath + "\"");
		try {
			String result = run(sb.toString());
			System.out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
