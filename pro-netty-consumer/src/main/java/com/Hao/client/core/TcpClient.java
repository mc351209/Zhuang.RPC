package com.Hao.client.core;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.Watcher;

import com.Hao.client.constant.Constants1;
import com.Hao.client.hanlder.SimpleClientHandler;
import com.Hao.client.param.ClientRequest;
import com.Hao.client.param.Response;
import com.Hao.client.zk.ZookeeperFactory;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSONB.Constants;


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
import io.netty.util.Constant;

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
	    
        //获取服务器列表
        CuratorFramework client = ZookeeperFactory.getClient();
        try {
			List<String> serverPaths = client.getChildren().forPath(Constants1.SERVER_PATH);
			
			CuratorWatcher watcher = new ServerWatcher();
			//加上zk监听服务器的变化,有变化的时候,会调用watcher的process方法,得到最新的path要放进36行的静态变量Set<String> realServerPath
			client.getChildren().usingWatcher(watcher).forPath(Constants1.SERVER_PATH);
			
			
			for(String serverPath:serverPaths) {
				String[] str = serverPath.split("#");
				ChannelManager.realServerPath.add(str[0]+"#"+str[1]);
				ChannelFuture channelFuture = TcpClient.b.connect(str[0], Integer.valueOf(str[1]));
				ChannelManager.add(channelFuture);
			}
			
			if (ChannelManager.realServerPath.size()>0) {
				String[] hostAndPort=ChannelManager.realServerPath.toArray()[0].toString().split("#");
				host = hostAndPort[0];
				port = Integer.valueOf(hostAndPort[1]);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        
        

//	    try {
//			f = b.connect(host, port).sync();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	    
	}
	
	
	//注意:为了解决长连接中的并发问题,可以通过request解决,避免一个请求对应另一个请求的相应
	//构建send方法,传入请求,然后返回结果.过程通过DefaultFuture来管理
	public static Response send(ClientRequest request) {
		f=ChannelManager.get(ChannelManager.position);
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
