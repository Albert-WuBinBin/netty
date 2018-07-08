package org.netty01.client;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHander extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("收到服务器消息"+msg);
		if(msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			System.out.println(buf.toString(Charset.defaultCharset()));
		}
	}	
}
