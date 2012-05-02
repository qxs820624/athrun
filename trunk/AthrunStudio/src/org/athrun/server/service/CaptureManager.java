/**
 * 
 */
package org.athrun.server.service;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.ClientAbortException;
import org.athrun.ddmlib.AdbCommandRejectedException;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.SyncException;
import org.athrun.ddmlib.TimeoutException;
import org.athrun.server.utils.ForwardPortManager;
import org.athrun.server.utils.InOutStructure;
import org.athrun.server.utils.OneParameterRunnable;
import org.athrun.server.utils.ReservedPortExhaust;

/**
 * @author taichan
 * 
 */
public class CaptureManager {

	static final String remotePath = "/data/local/gsnap";

	// 存储请求的返回流，block住等截图结果出来再返回。
	static OutputManager capOutputManager = new OutputManager();

	private static CaptureManager instance = new CaptureManager(); // 单态

	static Map<String, Boolean> needCaptureList = new HashMap<String, Boolean>(); // 控制是否有新的截图请求
	static Map<String, Thread> threadlist = new HashMap<String, Thread>(); // 每个设备都会启动一个截图线程

	static Map<String, byte[]> bylist = new HashMap<String, byte[]>(); // 一次读1024个字节
	static Map<String, byte[]> memorylist = new HashMap<String, byte[]>(); // 图片存储内存
	static Map<String, Object> lockSocket = new HashMap<String, Object>();

	static Map<String, Integer> countermap = new HashMap<String, Integer>();
	static Map<String, Long> timemap = new HashMap<String, Long>();

	private void StartCaptureMonitor(String serialNumber) {
		if (!threadlist.containsKey(serialNumber)) {
			synchronized (threadlist) {
				if (!threadlist.containsKey(serialNumber)) {
					Thread thread = new Thread(new OneParameterRunnable(
							serialNumber) {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String serialNumber = (String) getParameter();
							try {
								GetCapture(serialNumber);
							} catch (ReservedPortExhaust e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					thread.start();
					threadlist.put(serialNumber, thread);
				}
			}
		}
	}

	private void RemoveCaptureMonitor(String serialNumber) {
		if (threadlist.containsKey(serialNumber)) {
			synchronized (threadlist) {
				if (threadlist.containsKey(serialNumber)) {
					Thread thread = threadlist.get(serialNumber);
					System.out.println("RemoveCaptureThread: " + serialNumber);
					thread.interrupt();
					threadlist.remove(serialNumber);
				}
			}
		}
	}

	public static CaptureManager getInstance() {
		return instance;
	}

	// 不停地处理所有的请求，分发到不同的thread来处理
	private static void GetCapture(String serialNumber)
			throws ReservedPortExhaust {

		// 下面的反复执行
		while (true) {

			if (needCaptureList.containsKey(serialNumber)
					&& needCaptureList.get(serialNumber) == true) {
				processRegister(serialNumber);
				needCaptureList.put(serialNumber, false);
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("进程从sleep过程中打断，设备:" + serialNumber);
				}
			}
		}
	}

	public static void processAdjustQuality(int qualityRate, String serialNumber)
			throws ReservedPortExhaust {
		assert (qualityRate >= 0);
		assert (qualityRate <= 100);
		synchronized (lockSocket.get(serialNumber)) {
			InOutStructure inOutStructure = InOutStructure
					.GetCaptureInOutBySerialNumber(serialNumber);

			inOutStructure.GetOut().println(
					"q" + String.format("%03d", qualityRate)); // 取3位
			inOutStructure.GetOut().flush();

			try {
				inOutStructure.getIn().read(bylist.get(serialNumber));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void processAdjustResize(int resizeRate, String serialNumber)
			throws ReservedPortExhaust {
		synchronized (lockSocket.get(serialNumber)) {

			InOutStructure inOutStructure = InOutStructure
					.GetCaptureInOutBySerialNumber(serialNumber);

			inOutStructure.GetOut().println(
					"r" + String.format("%03d", resizeRate)); // 取3位
			inOutStructure.GetOut().flush();

			try {
				inOutStructure.getIn().read(bylist.get(serialNumber));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected static void processRegister(String serialNumber)
			throws ReservedPortExhaust {
		synchronized (lockSocket.get(serialNumber)) {
			// 没有在这里加锁，逻辑上说，别的线程这时在读的时候还可以加。
			if (!capOutputManager.isEmpty(serialNumber)) {

				InOutStructure inOutStructure = InOutStructure
						.GetCaptureInOutBySerialNumber(serialNumber);
				inOutStructure.GetOut().println("snap");
				inOutStructure.GetOut().flush();

				int length = 0;

				try {
					for (int i = 0;;) {
						int read = inOutStructure.getIn().read(
								bylist.get(serialNumber));
						length += read;
						for (byte b : bylist.get(serialNumber)) {
							memorylist.get(serialNumber)[i] = b;
							i++;
						}
						if (read != 1024) {
							break;
						}
					}

				} catch (IOException e) {
					System.out.println("snap客户端断开，尝试重新连接");
					InOutStructure.reconnectCaptureInOut(serialNumber);
					e.printStackTrace();
				}

				// 对outputlist进行操作，防止新的线程加内容
				synchronized (capOutputManager.getlock(serialNumber)) {
					List<OutputStream> outlist = capOutputManager
							.getOutputStream(serialNumber);
					for (int i = 0; i < outlist.size(); i++) {
						OutputStream out = outlist.get(i);
						try {
							out.write(memorylist.get(serialNumber), 0, length);
							out.flush();
							out.close();
							synchronized (out) {
								out.notify();
							}

						} catch (ClientAbortException ce) {
							// about by browser
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					capOutputManager.clear(serialNumber);
					if (countermap.get(serialNumber) >= 9) {
						long end = System.currentTimeMillis();
						if (timemap.get(serialNumber) != 0) {
							System.out.println("10次截图平均帧数(" + serialNumber
									+ ")" + 10.0 * 1000
									/ (end - timemap.get(serialNumber)));
						}
						timemap.put(serialNumber, end);
						countermap.put(serialNumber, 0);
					}
					countermap.put(serialNumber,
							countermap.get(serialNumber) + 1);
				}
			}
		}
		return;
	}

	// 注册一个回调函数
	// 启动截图
	// 截图完成后，回调返回
	public void register(OutputStream output, String serialNumber) {
		synchronized (capOutputManager) {
			capOutputManager.add(new OutputBean(output, serialNumber));
		}
		StartCapture(serialNumber);

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
	private void StartCapture(String serialNumber) {
		synchronized (needCaptureList) {
			needCaptureList.put(serialNumber, true); // 可以替换旧的value值
		}
	}

	/**
	 * @param device
	 * @param serialNumber
	 * @throws ReservedPortExhaust 
	 */
	public void remove(String serialNumber) {
		// TODO Auto-generated method stub
		RemoveCaptureMonitor(serialNumber);
		KillCaptureService(serialNumber);
	}

	/**
	 * @param serialNumber
	 * @throws ReservedPortExhaust 
	 */
	private void KillCaptureService(String serialNumber) {
		synchronized (lockSocket.get(serialNumber)) {

			InOutStructure inOutStructure;
			try {
				inOutStructure = InOutStructure
						.GetCaptureInOutBySerialNumber(serialNumber);

				inOutStructure.GetOut().println("kill");
				inOutStructure.GetOut().flush();

				
			} catch (ReservedPortExhaust e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	/**
	 * @param serialNumber
	 */
	public void add(String serialNumber) {
		if (!lockSocket.containsKey(serialNumber)) {
			lockSocket.put(serialNumber, new Object());
		}
		if (!bylist.containsKey(serialNumber)) {
			bylist.put(serialNumber, new byte[1024]);
		}
		if (!memorylist.containsKey(serialNumber)) {
			memorylist.put(serialNumber, new byte[300000]);
		}
		if (!countermap.containsKey(serialNumber)) {
			countermap.put(serialNumber, 0);
		}
		if (!timemap.containsKey(serialNumber)) {
			timemap.put(serialNumber, (long) 0);
		}
		StartCaptureMonitor(serialNumber);
	}

	public static void uploadCaptureEvent(IDevice device) throws SyncException,
			IOException, TimeoutException, AdbCommandRejectedException,
			URISyntaxException {
//		 File file = new File(EventManager.class.getResource("/gsnap/gsnap")
//		 .toURI().getPath());
//		
//		 device.getSyncService().pushFile(file.getAbsolutePath(), remotePath,
//		 new NullSyncProgressMonitor());

		System.out.println("TODO: upload 改为命令行运行...");
		System.out.println("capture agent uploaded.");
	}

}
