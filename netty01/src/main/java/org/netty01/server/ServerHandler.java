package org.netty01.server;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("收到数据"+msg);
		if(msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			System.out.println(buf.toString(Charset.defaultCharset()));
			ctx.channel().writeAndFlush("hello,client");
		}	
	}

}
