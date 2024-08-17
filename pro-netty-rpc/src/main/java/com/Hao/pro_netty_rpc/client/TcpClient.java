package com.Hao.pro_netty_rpc.client;

import com.Hao.pro_netty_rpc.handler.SimpleClientHandler;
import com.Hao.pro_netty_rpc.handler.param.ClientRequest;
import com.Hao.user.bean.User;
import com.alibaba.fastjson.JSONObject;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpClient {
	
	static final Bootstrap b = new Bootstrap();
	static ChannelFuture f = null;
	static {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
            	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
            	ch.pipeline().addLast(new StringDecoder());
            	ch.pipeline().addLast(new SimpleClientHandler());
                ch.pipeline().addLast(new StringEncoder());
            }
        });
        String host = "localhost";
	    int port = 8084;
	    try {
			f = b.connect(host, port).sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	}
	
	//注意:为了解决长连接中的并发问题,可以通过request解决,避免一个请求对应另一个请求的相应
	//构建send方法,传入请求,然后返回结果.过程通过DefaultFuture来管理
	public static Response send(ClientRequest request) {
		f.channel().writeAndFlush(JSONObject.toJSONString(request));
		f.channel().writeAndFlush("\r\n");
		DefaultFuture df = new DefaultFuture(request);
		
		return df.get();
	}
	
//	public static void main(String[] args) {
////		ClientRequest request = new ClientRequest();
////		request.setContent("这是tcp的长连接测试");
////		Response response = TcpClient.send(request);
////		System.out.println(response.getResultObject());
//	
//	ClientRequest request = new ClientRequest();
//	User u = new User();
//	u.setId(1);
//	u.setName("彭宇轩");
//	request.setCommand("com.Hao.user.controller.UserController.saveUser");
//	request.setContent(u);
//	Response response = TcpClient.send(request);
//	System.out.println(response.getResultObject());
//	}
//	
	
}
