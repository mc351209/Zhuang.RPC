package com.Hao.pro_netty_rpc.handler;

import com.Hao.pro_netty_rpc.client.DefaultFuture;
import com.Hao.pro_netty_rpc.client.Response;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if ("ping".equals(msg.toString())) {
			ctx.channel().writeAndFlush("ping\r\n");
			return;
		}
		//ctx.channel().attr(AttributeKey.valueOf("sssss")).set(msg);
		//ctx.channel().close();
		
		Response response = JSONObject.parseObject(msg.toString(), Response.class);
		DefaultFuture.recive(response);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
	}

}
