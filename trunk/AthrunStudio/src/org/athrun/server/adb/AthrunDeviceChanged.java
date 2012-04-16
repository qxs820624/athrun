/**
 * 
 */
package org.athrun.server.adb;

import java.util.ArrayList;
import java.util.List;

import org.athrun.ddmlib.Client;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import org.athrun.server.service.DeviceManager;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class AthrunDeviceChanged implements IDeviceChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#deviceConnected
	 * (com.android.ddmlib.IDevice)
	 */
	@Override
	public void deviceConnected(IDevice device) {
		System.out.println("deviceConnected: " + device.getSerialNumber());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#
	 * deviceDisconnected(com.android.ddmlib.IDevice)
	 */
	@Override
	public void deviceDisconnected(IDevice device) {
		// TODO Auto-generated method stub
		System.out.println("deviceDisconnected: " + device.getSerialNumber());
		System.out.println("˵���豸" + device.getSerialNumber() + "�Ѿ��ε���");
		CancelAdd(device.getSerialNumber());
		DeviceManager.remove(device);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener#deviceChanged
	 * (com.android.ddmlib.IDevice, int)
	 */
	@Override
	public void deviceChanged(IDevice device, int changeMask) {
		// TODO Auto-generated method stub
		System.out.println("deviceChanged: (" + changeMask + ")"
				+ device.getSerialNumber());
		if (changeMask == Client.CHANGE_PORT) {
			System.out.println("˵���豸" + device.getSerialNumber() + "�Ѿ���������");
			DelayAdd(device.getSerialNumber(), device);
		}
	}

	// �ӳ�����豸���Է��¼��仯̫�죬��ɲ���Ҫ���¼���Ӧ
	private void DelayAdd(String serialNumber, IDevice device) {
		boolean canAdd = serialList.add(serialNumber);
		if (canAdd) {
			System.out.println("�豸" + serialNumber + "������Delay�б�");
		} else {
			System.out.println("[Debug]�豸" + serialNumber + "������Ҫ�ټ���Delay�б�");
		}

		// TODO ���β��淶��Ҫ��
		currentSerialNumber = serialNumber;
		currentDevice = device;

		new Thread(new Runnable() {
			@Override
			public void run() {
				String serialNumber1 = currentSerialNumber;
				IDevice device1 = currentDevice;
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated method stub
				if (serialList.contains(serialNumber1)) {
					serialList.remove(serialNumber1);
					System.out.println("�豸" + serialNumber1 + "����Delay�б��д������");
					DeviceManager.add(device1);
				} else {
					System.out.println("[Debug]�豸" + serialNumber1 + "���Ѿ���ɾ��");
				}
			}
		}, serialNumber).start();
	}

	private static IDevice currentDevice;
	private static String currentSerialNumber;

	private static List<String> serialList = new ArrayList<String>();

	// ȡ������豸
	private void CancelAdd(String serialNumber) {
		boolean canRemove = serialList.remove(serialNumber);
		if (canRemove) {
			System.out.println("�豸" + serialNumber + "����Delay�б�ɾ��");
		} else {
			System.out.println("[Debug]�豸" + serialNumber
					+ "�������ڣ�����Ҫ��Delay�б�ɾ��");
		}
	}

}