package org.athrun.client;

import java.util.Date;

import net.sf.json.JSONObject;

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
			AthrunMsg msg = (AthrunMsg)message;
			System.out.println("recv from server: " + msg.getInfo());
			
			
			
		}
		
		/*
		long currentTimeMillis = buf.readInt() * 1000L;
		System.out.println(new Date(currentTimeMillis));
		e.getChannel().close();
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