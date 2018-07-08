package org.netty01.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient {

	private int port; 
	private String address ;
	public NettyClient(String address , int port) {
		this.address = address;
		this.port = port;
	}
	public void connect() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					 .channel(NioSocketChannel.class)
					 .handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("handler",new ClientHander());
							pipeline.addLast("encoder", new StringEncoder());
							pipeline.addLast("decoder",new StringEncoder());
						}
					}); 
			ChannelFuture future = bootstrap.connect(address, port).sync();
			Channel channel = future.channel();
			channel.writeAndFlush("hello,server");
			channel.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		NettyClient nettyClient = new NettyClient("127.0.0.1", 8888);
		nettyClient.connect();
	}
}
