package org.netty02.server;

import org.netty02.util.MarshallingFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {

	private int port ;
	public NettyServer(int port) {
		this.port = port;
	}
	public void start() {
		//NioEventLoopGroup是用来处理IO操作的多线程事件循环器
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();//用来接收进来的TCP连接
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();//会获取到真正的连接,然后和连接进行通信,比如读写解码编码等操作;
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();//NIO服务的辅助启动类,用于启动服务器和引导整个程序的初始化
			serverBootstrap.channel(NioServerSocketChannel.class);//执行channel类型
			serverBootstrap.group(bossGroup, workerGroup); //EventLoop目的是为Channel处理IO操作
			serverBootstrap.childOption(ChannelOption.SO_BACKLOG, 128);//队列等待数
			serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//长连接
			serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					
					pipeline.addLast(MarshallingFactory.decode());
					pipeline.addLast(MarshallingFactory.encode());
					
					pipeline.addLast(new ServerHandler());
				}
			});
			ChannelFuture future = serverBootstrap.bind(port).sync();
			future.sync().channel().closeFuture();
		} catch (Exception e) {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		NettyServer server = new NettyServer(8888);
		server.start();
	}
}
