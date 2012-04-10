/**
 * 
 */
package org.athrun.server.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.catalina.connector.ClientAbortException;
import org.athrun.server.utils.InOutStructure;

/**
 * @author taichan
 * 
 */
public class CaptureManager {

	// 存储请求的返回流，block住等截图结果出来再返回。
	static ArrayList<OutputStream> outputlist;

	static DataInputStream in;
	static PrintWriter out;

	static byte[] by = new byte[1024]; // 一次读1024个字节
	static byte[] memory = new byte[300000]; // 图片存储内存
	static boolean needCapture = false; // 控制是否有新的截图请求

	static Object locker = new Object(); // 单态的锁
	static boolean firstTime = true; // 保证单态
	private static CaptureManager instance = new CaptureManager(); // 单态

	private CaptureManager() {
		synchronized (locker) {
			if (firstTime) {				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				outputlist = new ArrayList<OutputStream>();

				try {
					// 启动截图线程
					Thread thread = new Thread(new Runnable() {

						@Override
						public void run() {
							GetCapture();
						}
					});
					thread.start();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 保证只被调用一次
				firstTime = false;
			}

		}

	}

	public static CaptureManager getInstance() {
		return instance;
	}

	private static void GetCapture() {

		// 启动的时候进行连接，TODO，后续改为可以检查连接
		InOutStructure inout = new InOutStructure(5678);
		in = inout.getIn();
		out = inout.GetOut();

		// 下面的反复执行
		while (true) {
			if (needCapture) {
				processRegister();
				needCapture = false;
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	static int counter = 0;
	static long time = 0;
	static Object lockSocket = new Object();

	public static void processAdjustQuality(int qualityRate) {
		assert (qualityRate >= 0);
		assert (qualityRate <= 100);
		synchronized (lockSocket) {
			out.println("q" + String.format("%03d", qualityRate)); // 取3位
			out.flush();

			try {
				in.read(by);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void processAdjustResize(int resizeRate) {
		synchronized (lockSocket) {
			out.println("r" + String.format("%03d", resizeRate)); // 取3位
			out.flush();

			try {
				in.read(by);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected static void processRegister() {
		synchronized (lockSocket) {
			// 没有在这里加锁，逻辑上说，别的线程这时在读的时候还可以加。
			if (!outputlist.isEmpty()) {

				out.println("snap");
				out.flush();
				int length = 0;

				try {
					for (int i = 0;;) {
						int read = in.read(by);
						length += read;
						for (byte b : by) {
							memory[i] = b;
							i++;
						}
						if (read != 1024) {
							break;
						}
					}

				} catch (IOException e) {
					System.out.println("snap客户端断开，尝试重新连接");
					InOutStructure inout = new InOutStructure(5678);
					in = inout.getIn();
					out = inout.GetOut();
					e.printStackTrace();
				}

				// 对outputlist进行操作，防止新的线程加内容
				synchronized (outputlist) {
					for (OutputStream outstr : outputlist) {
						try {
							outstr.write(memory, 0, length);
							outstr.flush();
							outstr.close();
							synchronized (outstr) {
								outstr.notify();
							}

						} catch (ClientAbortException ce) {
							// about by browser
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					outputlist.clear();
					if (counter >= 9) {
						long end = System.currentTimeMillis();
						if (time != 0) {
							System.out.println("10次截图平均帧数：" + 10.0 * 1000
									/ (end - time));
						}
						time = end;
						counter = 0;
					}
					counter++;
				}
			}
		}
		return;
	}

	// 注册一个回调函数
	// 启动截图
	// 截图完成后，回调返回
	public void register(OutputStream output) {
		synchronized (outputlist) {
			outputlist.add(output);
		}
		StartCapture();

		// 等待回调返回
		synchronized (output) {
			try {
				output.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 启动截图
	private void StartCapture() {
		needCapture = true;
	}

}
