package org.netty01.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandlerAdapter是ChannelInboundHandler的一个简单实现，默认情况下不会做任何处理。
 * 可以将操作通过fire*方法传递到ChannelPipeline中的下一个ChannelHandler中让链中的下一个ChannelHandler去处理。
 * 需要注意的是信息经过channelRead方法处理之后不会自动释放
 * （因为信息不会被自动释放所以能将消息传递给下一个ChannelHandler处理）
 * 
 * @author wbb
 * 2019年2月14日
 */
public class ServerHandler extends ChannelInboundHandlerAdapter{
	
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("server active");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof String){
			String message = (String) msg;
			System.out.println("收到数据"+message);
			ctx.channel().writeAndFlush("hello,client$_hello,client2$_")
			.addListener(ChannelFutureListener.CLOSE);//添加监听，关闭channel
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
