package org.athrun.client;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

// Э���ʽ�ܼ򵥣��������Ӧ����ͷ4���ֽڱ�ʾ���������ݳ��ȣ���ݸó��ȿɵõ������塣
public class MessageDecoder extends FrameDecoder {
	 
    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() < 4) {
            return null;//(1)
        }
        int dataLength = buffer.getInt(buffer.readerIndex());
        if (buffer.readableBytes() < dataLength + 4) {
            return null;//(2)
        }
 
        buffer.skipBytes(4);//(3)
        byte[] decoded = new byte[dataLength];
        buffer.readBytes(decoded);
        
        return AthrunMsg.decode(decoded);
    }
}