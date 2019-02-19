package org.netty02.server;

import java.io.File;
import java.io.FileOutputStream;

import org.netty02.bean.Req;
import org.netty02.bean.Resp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
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
		}
		else if(msg instanceof Req){
			Req req = (Req) msg;
			System.out.println("收到数据,"+req.toString());
			
			String path = System.getProperty("user.dir") + File.separatorChar + "receive" +  File.separatorChar + "001.png";
	        FileOutputStream fos = new FileOutputStream(path);
	        fos.write(req.getAttachment());
	        fos.close();
	        
			Resp resp = new Resp();
			resp.setResponseMessage("响应数据:"+req.getId());
			ctx.channel().writeAndFlush(resp);
//			.addListener(ChannelFutureListener.CLOSE);//添加监听，关闭channel
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
