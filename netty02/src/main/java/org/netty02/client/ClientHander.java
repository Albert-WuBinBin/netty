package org.netty02.client;

import org.netty02.bean.Resp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHander extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof String) {
			String message = (String) msg;
			System.out.println("收到服务器消息"+message);
		}
		else if(msg instanceof Resp){
			Resp resp = (Resp) msg;
			System.out.println(resp.toString());
		}
	}	
}
