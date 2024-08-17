package com.Hao.client.hanlder;

import com.Hao.client.core.DefaultFuture;
import com.Hao.client.param.Response;
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


		Response response = JSONObject.parseObject(msg.toString(), Response.class);
		System.out.println("接收服务器返回数据,"+JSONObject.toJSONString(response));
		DefaultFuture.recive(response);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
	}

}
