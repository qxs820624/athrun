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

		// 往 ThreadPool 增加运行线程1，专门处理单数
		// 往 ThreadPool 增加运行线程2，专门处理双数

		// 处理数据1，丢到 ThreadPool 1
		// 处理数据2，丢到 ThreadPool 2
		// 处理数据3，丢到 ThreadPool 1
		// 处理数据4，丢到 ThreadPool 2
		// 处理数据5，丢到 ThreadPool 1
		// 处理数据4，丢到 ThreadPool 2
		// 处理数据1，丢到 ThreadPool 1
		

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
