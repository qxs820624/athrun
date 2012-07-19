package org.athrun.server.service;

import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CaptureService {

	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public TaskResult capture(String serialNumber, OutputStream os) {
		CaptureTask task = new CaptureTask();
		task.setSerialNumber(serialNumber);
		// 设置截图请求时间
		task.setRequestTime(System.currentTimeMillis());
		
		Future<TaskResult> future = executor.submit(task);
		TaskResult tr = null;

		try {
			tr = future.get(20000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			future.cancel(true);
			e.printStackTrace();
		}
		
		return tr;
	}
	
	public TaskResult info(String serialNumber) {
		InfoTask task = new InfoTask();
		task.setSerialNumber(serialNumber);
		
		Future<TaskResult> future = executor.submit(task);
		TaskResult tr = null;
		
		try {
			tr = future.get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			future.cancel(true);
			e.printStackTrace();
		}

		if (tr != null) {
			System.out.println("info result: " + tr.getResult());
		}
		
		return tr;
	}
	
	public TaskResult adjustResize(String serialNumber, int resizeRate) {
		AdjustResizeTask task = new AdjustResizeTask();
		task.setSerialNumber(serialNumber);
		task.setResizeRate(resizeRate);
		
		Future<TaskResult> future = executor.submit(task);
		TaskResult tr = null;
		
		try {
			tr = future.get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			future.cancel(true);
			e.printStackTrace();
		}

		if (tr != null) {
			System.out.println("adjust resize result: " + tr.getResult());
		}
		
		return tr;
	}
	
	public TaskResult adjustQuality(String serialNumber, int qualityRate) {
		ExecuteTask task = new ExecuteTask();
		task.setSerialNumber(serialNumber);
		task.setCommand("q" + String.format("%03d", qualityRate));
		
		Future<TaskResult> future = executor.submit(task);
		TaskResult tr = null;
		
		try {
			tr = future.get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			future.cancel(true);
			e.printStackTrace();
		}

		if (tr != null) {
			System.out.println("kill result: " + tr.getResult());
		}
		
		return tr;
	}
	
	public TaskResult kill(String serialNumber) {
		ExecuteTask task = new ExecuteTask();
		task.setSerialNumber(serialNumber);
		task.setCommand("kill");
		
		Future<TaskResult> future = executor.submit(task);
		TaskResult tr = null;
		
		try {
			tr = future.get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			future.cancel(true);
			e.printStackTrace();
		}

		if (tr != null) {
			System.out.println("kill result: " + tr.getResult());
		}
		
		return tr;
	}
	
	public void stop() {
		executor.shutdown();
	}

}
