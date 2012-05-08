package org.athrun.ios.instruments;


/* instrument 接收返回的命令，几种处理类型的枚举 */
enum ReturnedType {
	guidType, arrayType, voidType, stringType, booleanType, exitType;

}

/**
 * @author ziyu.hch
 * 
 */
public class RunType {

	/**
	 * 设定用例运行方式 DEBUG = ture 则调试方式单步运行,速度较慢; DEBUG = false 则在需要时才进行通信,速度较快
	 */
	public static boolean DEBUG = false;

	public static int ArrayLength = 20;

	/*
	 * public static boolean isDEBUG() { return DEBUG; }
	 * 
	 * public static void setDEBUG(boolean dEBUG) { DEBUG = dEBUG; }
	 */
}