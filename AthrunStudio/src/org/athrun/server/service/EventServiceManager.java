/**
 * 
 */
package org.athrun.server.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.athrun.ddmlib.Log;
import org.athrun.server.utils.ForwardPortManager;
import org.athrun.server.utils.ReservedPortExhaust;

/**
 * @author taichan
 * 
 */
public class EventServiceManager {
	static Queue<String[]> pointerQueue = new LinkedList<String[]>();

	private static EventServiceManager instance;

	public static EventServiceManager getInstance() {
		if (instance == null) {
			instance = new EventServiceManager();
		}
		return instance;
	}

	private EventServiceManager() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}

					try {
						String[] lastCmd = null;
						while (true) {
							String[] cmd = pointerQueue.poll();
							if (cmd != null) {
								// 处理第一个
								Send(cmd[0], cmd[1]);
								// 略过10个

								for (int i = 0; i < 100; i++) {
									String[] cmdIgore = pointerQueue.poll();
									if (cmdIgore == null) {
										break;
									}
									System.out.println("略过" + cmdIgore[0] + ","
											+ cmdIgore[1]);
									lastCmd = cmdIgore;
								}
							} else {
								// 处理lastCmd
								if (lastCmd != null) {
									System.out.println("拿回" + lastCmd[0] + ","
											+ lastCmd[1]);
									Send(lastCmd[0], lastCmd[1]);
								}
								break;
							}
						}
					} catch (NoSuchElementException e) {
						// 说明已经被processPointerDownup处理过了
					}

					for (Object waitObj : waitingList) {
						synchronized (waitObj) {
							waitObj.notify();
						}
					}
				}
			}
		}, "EventServiceManager").start();
	}

	private static List<Object> waitingList = new ArrayList<Object>();

	/**
	 * @param serialNumber
	 * @param cmd
	 */
	private static void waitMoveFinish(String serialNumber, String cmd) {
		Object waitingObj = new Object();
		waitingList.add(waitingObj);
		synchronized (waitingObj) {
			try {
				waitingObj.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void processPointerMove(String serialNumber, String cmd) {
		String[] cmds = { serialNumber, cmd };
		pointerQueue.add(cmds);
	}

	public static void processPointerUp(String serialNumber, String cmd) {
		waitMoveFinish(serialNumber, cmd);
		Send(serialNumber, cmd);
	}

	public static void processPointerDown(String serialNumber, String cmd) {
		pointerQueue.clear();
		Send(serialNumber, cmd);
	}

	public static void Send(String serialNumber, String cmd) {
		try {
			Socket server;
			server = new Socket("127.0.0.1",
					ForwardPortManager.getEventPort(serialNumber));
			PrintWriter out = new PrintWriter(server.getOutputStream());
			out.print(cmd);
			out.flush();
			out.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReservedPortExhaust e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
