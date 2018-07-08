package org.netty01.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {

	private int port ;
	public NettyServer(int port) {
		this.port = port;
	}
	public void start() {
		//NioEventLoopGroup是用来处理IO操作的多线程事件循环器
		NioEventLoopGroup parentGroup = new NioEventLoopGroup();//用来接收进来的连接
		NioEventLoopGroup childGroup = new NioEventLoopGroup();//用来处理已经被接收的连接;
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();//NIO服务的辅助启动类
			serverBootstrap.channel(NioServerSocketChannel.class);//
			serverBootstrap.group(parentGroup, childGroup); //EventLoop目的是为Channel处理IO操作
			serverBootstrap.childOption(ChannelOption.SO_BACKLOG, 128);//队列等待数
			serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//长连接
			/**
			 * ChannelPipeline是ChannelHandler的容器，它负责ChannelHandler的管理和
			 * 事件拦截与调度。Netty的ChannelPipeline和ChannelHandler机制类似于Servlet 
			 * 和Filter 过滤器，这类拦截器实际上是职责链模式的一种变形，
			 * 主要是为了方便事件的拦截和用户业务逻辑的定制。

			 * Netty的channel运用机制和Filter过滤器机制一样，它将Channel的数据管道抽象为
			 * ChannelPipeline. 消息在ChannelPipeline中流动和传递。
			 * ChannelPipeline 持有I/O事件拦截器ChannelHandler 的链表，
			 * 由ChannelHandler 对I/0 事件进行拦截和处理，可以方便地通过新增和删除ChannelHandler 
			 * 来实现小同的业务逻辑定制，不需要对已有的ChannelHandler进行修改，
			 * 能够实现对修改封闭和对扩展的支持。
			 */
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					
					pipeline.addLast("handler",new ServerHandler());
					pipeline.addLast("decoder",new StringDecoder());
					pipeline.addLast("encoder", new StringEncoder());
				}
			});
			ChannelFuture future = serverBootstrap.bind(port).sync();
			future.sync().channel().closeFuture();
		} catch (Exception e) {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		NettyServer server = new NettyServer(8888);
		server.start();
	}
}
