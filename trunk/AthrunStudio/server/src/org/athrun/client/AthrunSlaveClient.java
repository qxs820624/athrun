package org.athrun.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.athrun.client.AthrunMsg;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class AthrunSlaveClient {

	private final String host;
    private final int port;
    private static ChannelFuture future;

    public AthrunSlaveClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void run() throws IOException {
    	ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(
						new MessageDecoder(),
						new MessageEncoder(),
						new AthrunSlaveClientHandler());
			}
		});

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		future = bootstrap.connect(new InetSocketAddress(host,
				port));
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
		}
		
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		factory.releaseExternalResources();
    }
    
    public void shutdown() {
    	if (future != null) {
    		future.getChannel().close();
    	}
    }
    
    // 发送消息给服务端
    public synchronized static void sendAthrunMsg(AthrunMsg msg) {
    	Channel channel = future.getChannel();
    	if(channel != null) {
    		channel.write(msg);
    	}
    } 
	
	public static void main(String[] args) throws Exception {
        // Print usage if no argument is specified.
        if (args.length != 2) {
            System.err.println(
                    "Usage: " + AthrunSlaveClient.class.getSimpleName() +
                    " <host> <port>");
            return;
        }

        // Parse options.
        //String host = args[0];
        //int port = Integer.parseInt(args[1]);

        String host="127.0.0.1";
		int port = 53024;
		
        new AthrunSlaveClient(host, port).run();
    }
}