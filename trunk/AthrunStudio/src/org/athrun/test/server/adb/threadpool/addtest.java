/**
 * 
 */
package org.athrun.test.server.adb.threadpool;

import static org.junit.Assert.*;

import org.apache.tomcat.util.threads.ThreadPool;
import org.junit.Test;

/**
 * @author taichan
 * 
 */
public class addtest {

	@Test
	public void test() {

		// �� ThreadPool ���������߳�1��ר�Ŵ�����
		// �� ThreadPool ���������߳�2��ר�Ŵ���˫��

		// ��������1������ ThreadPool 1
		// ��������2������ ThreadPool 2
		// ��������3������ ThreadPool 1
		// ��������4������ ThreadPool 2
		// ��������5������ ThreadPool 1
		// ��������4������ ThreadPool 2
		// ��������1������ ThreadPool 1
		

		ThreadPool tp = new ThreadPool();

	}

	public int math(int input) {
		int result = input;
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result = result + input * i;
		}

		return result;
	}

}
