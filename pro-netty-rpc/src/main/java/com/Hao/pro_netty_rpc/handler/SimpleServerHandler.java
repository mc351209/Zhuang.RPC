package com.Hao.pro_netty_rpc.handler;

import com.Hao.pro_netty_rpc.client.Response;
import com.Hao.pro_netty_rpc.handler.param.ServerRequest;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ctx.channel().writeAndFlush("is ok\r\n");
		
		ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
		
		Response resp = new Response();
		resp.setId(request.getId());
		resp.setResultObject("It's ok");
		ctx.channel().writeAndFlush(JSONObject.toJSONString(resp));
		ctx.channel().writeAndFlush("\r\n");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent)evt;
			if(event.state().equals(IdleState.READER_IDLE)) {
				System.out.println("读空闲====");
				ctx.channel().close();
			}else if (event.state().equals(IdleState.WRITER_IDLE)) {
				System.out.println("写空闲====");
			}else if (event.state().equals(IdleState.ALL_IDLE)) {
				System.out.println("读写空闲");
				ctx.channel().writeAndFlush("ping\r\n");
			}
		}

	}

	
}
