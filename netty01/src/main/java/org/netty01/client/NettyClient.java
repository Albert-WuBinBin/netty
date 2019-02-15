package org.netty01.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
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
							ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
							pipeline.addLast(new DelimiterBasedFrameDecoder(1024,buf));
//							pipeline.addLast(new FixedLengthFrameDecoder(5));
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(new ClientHander());
						}
					}); 
			ChannelFuture future = bootstrap.connect(address, port).sync();
			future.channel().writeAndFlush("hello,server$_");
			future.channel().writeAndFlush("hello,server2$_");
			future.channel().writeAndFlush("hello,server3$_");
			future.channel().closeFuture().sync();
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
