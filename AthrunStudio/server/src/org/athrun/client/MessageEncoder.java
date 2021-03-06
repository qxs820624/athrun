package org.athrun.client;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class MessageEncoder extends OneToOneEncoder {
	 
    @Override
    protected Object encode(
            ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (!(msg instanceof AthrunMsg)) {
            return msg;//(1)
        }
 
        AthrunMsg res = (AthrunMsg)msg;
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer();//(2)
        res.encode(buf);
        
        return buf;
    }

}