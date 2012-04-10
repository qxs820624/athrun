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

	// �洢����ķ�������blockס�Ƚ�ͼ��������ٷ��ء�
	static ArrayList<OutputStream> outputlist;

	static DataInputStream in;
	static PrintWriter out;

	static byte[] by = new byte[1024]; // һ�ζ�1024���ֽ�
	static byte[] memory = new byte[300000]; // ͼƬ�洢�ڴ�
	static boolean needCapture = false; // �����Ƿ����µĽ�ͼ����

	static Object locker = new Object(); // ��̬����
	static boolean firstTime = true; // ��֤��̬
	private static CaptureManager instance = new CaptureManager(); // ��̬

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
					// ������ͼ�߳�
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
				// ��ֻ֤������һ��
				firstTime = false;
			}

		}

	}

	public static CaptureManager getInstance() {
		return instance;
	}

	private static void GetCapture() {

		// ������ʱ��������ӣ�TODO��������Ϊ���Լ������
		InOutStructure inout = new InOutStructure(5678);
		in = inout.getIn();
		out = inout.GetOut();

		// ����ķ���ִ��
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
			out.println("q" + String.format("%03d", qualityRate)); // ȡ3λ
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
			out.println("r" + String.format("%03d", resizeRate)); // ȡ3λ
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
			// û��������������߼���˵������߳���ʱ�ڶ���ʱ�򻹿��Լӡ�
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
					System.out.println("snap�ͻ��˶Ͽ���������������");
					InOutStructure inout = new InOutStructure(5678);
					in = inout.getIn();
					out = inout.GetOut();
					e.printStackTrace();
				}

				// ��outputlist���в�������ֹ�µ��̼߳�����
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
							System.out.println("10�ν�ͼƽ��֡����" + 10.0 * 1000
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

	// ע��һ���ص�����
	// ������ͼ
	// ��ͼ��ɺ󣬻ص�����
	public void register(OutputStream output) {
		synchronized (outputlist) {
			outputlist.add(output);
		}
		StartCapture();

		// �ȴ��ص�����
		synchronized (output) {
			try {
				output.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// ������ͼ
	private void StartCapture() {
		needCapture = true;
	}

}
