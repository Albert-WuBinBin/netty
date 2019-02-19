package org.netty02.client;

import java.io.File;
import java.io.FileInputStream;

import org.netty02.bean.Req;
import org.netty02.util.GzipUtils;
import org.netty02.util.MarshallingFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
							
							pipeline.addLast(MarshallingFactory.decode());
							pipeline.addLast(MarshallingFactory.encode());
							
							pipeline.addLast(new ClientHander());
						}
					}); 
			ChannelFuture future = bootstrap.connect(address, port).sync();
			for (int i = 0; i < 5; i++) {
				Req req = new Req();
				req.setId(""+i);
				req.setName("name-"+i);
				req.setRequestMessage("message-"+i);
				
				String readPath = System.getProperty("user.dir") + File.separatorChar + "image" +  File.separatorChar + "001.png";
		        File file = new File(readPath);  
		        FileInputStream in = new FileInputStream(file);  
		        byte[] data = new byte[in.available()];  
		        in.read(data);  
		        in.close();  
		        req.setAttachment(GzipUtils.gzip(data));
				future.channel().writeAndFlush(req);
			}
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
