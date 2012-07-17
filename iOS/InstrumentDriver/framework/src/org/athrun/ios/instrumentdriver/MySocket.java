package org.athrun.ios.instrumentdriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ziyu.hch
 * 
 */
public class MySocket {

	/* instrument 接收返回的命令，几种处理类型的枚举 */
	enum ReturnedType {
		voidType, stringType, booleanType, JSONObject, JSONArray, exitType
	}

	public static ServerSocket server = null;

	/*
	 * public static String getGuid(String script) {
	 * 
	 * return send(ReturnedType.guidType + "##" + script); }
	 */

	public static void sendExit() throws Exception {

		exit(ReturnedType.exitType.toString());
	}

	private static void exit(String exitMark) throws Exception {
		// TODO Auto-generated method stub
		if (server == null) {
			creatSocket();
		}
		String request = null;
		try {
			Socket socket = null;
			socket = server.accept();

			BufferedReader is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			PrintWriter os = new PrintWriter(socket.getOutputStream());
			request = is.readLine();

			System.out.println("Client request  : " + request);
			System.out.println("Server response : " + exitMark + "  -End-");

			os.print(exitMark);
			os.flush();

			// 刷新输出流，使Client马上收到该字符串

			os.close();
			is.close();
			socket.close();
			// server.close();
		} catch (Exception e) {

			System.out.println("Exception:" + e);
		}
	}

	/*
	 * public static String[] getGuidArray(String script) { String guids =
	 * send(ReturnedType.arrayType + "##" + script);
	 * 
	 * // 如果返回的guids为空，则代表数组没有取得任何元素，返回一个空数组 if (guids.isEmpty()) { return new
	 * String[0]; } return guids.split("#"); }
	 */

	public static String getJSONArray(String script) throws Exception {
		String guids = send(ReturnedType.JSONArray + "##" + script);
		return guids;
	}

	public static String getJSONObject(String script) throws Exception {
		String guids = send(ReturnedType.JSONObject + "##" + script);
		return guids;
	}

	public static String getText(String script) throws Exception {
		return send(ReturnedType.stringType + "##" + script);
	}

	public static Boolean getBoolen(String script) throws Exception {
		String returnStr = send(ReturnedType.booleanType + "##" + script);
		if (returnStr.equals("1")) {
			return true;
		} else {

			return false;
		}
	}

	public static void getVoid(String script) throws Exception {
		send(ReturnedType.voidType + "##" + script);
	}

	private static void creatSocket() throws Exception {
		try {
			System.out.println("The server listen to port: 5566");
			server = new ServerSocket(5566);
			// 创建一个ServerSocket在端口5566监听客户请求

		} catch (Exception e) {
			throw new Exception("can not listen to:" + e);
		}
	}

	private static String send(String script) throws Exception {

		if (server == null) {
			creatSocket();
		}
		String guid = null;
		String request = null;
		try {
			Socket socket = null;
			socket = server.accept();

			BufferedReader is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			PrintWriter os = new PrintWriter(socket.getOutputStream());

			StringBuffer builder = new StringBuffer();
			String temp = null;
			while ((temp = is.readLine()) != null) {
				builder.append(temp);
			}
			request = builder.toString();

			// 用例执行错误的时候，获取到发回的异常信息并抛出
			if (request.startsWith("Exception")) {
				System.err.println(guid);
				os.close();
				is.close();
				socket.close();
				throw new Exception(request);
			}
			System.out.println("Client request  : " + request);
			System.out.println("Server response : " + script);

			os.print(script);
			os.flush();

			// 刷新输出流，使Client马上收到该字符串

			os.close();
			is.close();
			socket.close();

			// 第二次建立socket，获取 上一步运行的结果

			socket = server.accept();

			is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			os = new PrintWriter(socket.getOutputStream());
			guid = is.readLine();

			// 用例执行错误的时候，获取到发回的异常信息并抛出
			if (guid.startsWith("Exception")) {
				System.err.println(guid);
				os.close();
				is.close();
				socket.close();
				throw new Exception(guid);
			}
			System.out.println("Client response : " + guid);
			System.out
					.println("Server request  : Case setp executed. Please request the next setp");
			os.print("null");
			os.flush();

			os.close();
			is.close();
			socket.close();

		} catch (Exception e) {
			throw e;
		}
		return guid;
	}
}
