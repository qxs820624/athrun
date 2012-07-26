package org.athrun.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.athrun.ddmlib.IDevice;
import org.athrun.ddmlib.InstallException;
import org.athrun.server.service.CaptureManager;
import org.athrun.server.service.DeviceManager;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class AthrunSlaveClientHandler extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Object message = e.getMessage();
		if (message instanceof AthrunMsg) {
			AthrunMsg msg = (AthrunMsg) message;
			String info = msg.getInfo();
			System.out.println("recv from server: " + info);

			JSONObject jsonObj;

			jsonObj = JSONObject.fromObject(info);

			String key = jsonObj.getString("key");

			// ������������Ϣ
			if (key.equals("apk")) {
				// String serialNumber = jsonObj.getString("serialNumber");
				// AthrunMasterServer.register(serialNumber, e.getChannel());

				String apkFileName = jsonObj.getString("fileName");
				String serialNumber = jsonObj.getString("serialNumber");
				
				System.out.println("apkFileName: " + apkFileName);
				
				// 将apk存入apks文件夹
				if (msg.isPicture()) {
					byte[] pictureContent = msg.getPictureContent();

					String apksDir = AthrunSlaveClient.getApksDir();

					File apkFile = new File(apksDir + File.separator
							+ apkFileName);

					OutputStream out = null;

					try {
						out = new BufferedOutputStream(new FileOutputStream(
								apkFile));
						out.write(pictureContent, 0, pictureContent.length);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} finally {

						if (null != out) {
							try {
								out.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}

					//TODO: 安装产品apk，安装test instrument runner
					
					IDevice device = DeviceManager.getDeviceBySerialNumber(serialNumber);
					if (device != null) {
						
						try {
							device.installPackage(apkFile.getAbsolutePath(),true);
						} catch (InstallException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
					}
					
				}

				AthrunMsg ack = new AthrunMsg();

				JSONObject ackObj = new JSONObject();
				ackObj.put("key", "apk-ack");
				ack.setInfo(ackObj.toString());

				e.getChannel().write(ack);
			} else if (key.equals("run-monkey-test")) {
				//String serialNumber, String packageName, String activityName, int testCount
				
				String serialNumber = jsonObj.getString("serialNumber");
				String testcaseName = jsonObj.getString("testcaseName");
				String packageName = jsonObj.getString("packageName");
				String activityName = jsonObj.getString("activityName");
				int testCount = jsonObj.getInt("testCount");
				
				// 运行mokey
				CaptureManager.getInstance().runMonkeyTest(serialNumber, testcaseName, packageName, activityName, testCount);
			}

			// TODO: ����������Ϣ

		}

		/*
		 * long currentTimeMillis = buf.readInt() * 1000L;
		 * System.out.println(new Date(currentTimeMillis));
		 * e.getChannel().close();
		 */
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		AthrunMsg msg = new AthrunMsg();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key", "connect");
		jsonObject.put("serialNumber", "SHJ012345");

		msg.setInfo(jsonObject.toString());
		e.getChannel().write(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}