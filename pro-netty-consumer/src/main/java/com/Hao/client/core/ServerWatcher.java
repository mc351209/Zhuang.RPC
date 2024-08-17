package com.Hao.client.core;

import java.util.HashSet;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import com.Hao.client.param.ClientRequest;
import com.Hao.client.zk.ZookeeperFactory;

import io.netty.channel.ChannelFuture;

public class ServerWatcher implements CuratorWatcher {

	@Override
	public void process(WatchedEvent event) throws Exception {
		
		CuratorFramework client = ZookeeperFactory.getClient();
		//event得到的path即是TcpClient里的常量类里的path
		String path = event.getPath();
		
		//保证一直监控最新的路径
		client.getChildren().usingWatcher(this).forPath(path);
		//发生变化之后的路径
		List<String> serverPaths = client.getChildren().forPath(path);	
		ChannelManager.realServerPath.clear();
		for(String serverPath:serverPaths) {
			String[] str = serverPath.split("#");
			ChannelManager.realServerPath.add(str[0]+"#"+str[1]);
		}
		
		ChannelManager.clear();
		//写入ChannelManager
		for(String realServer:ChannelManager.realServerPath) {
			String[] str = realServer.split("#");
			try {
				ChannelFuture channelFuture = TcpClient.b.connect(str[0], Integer.valueOf(str[1]));
				ChannelManager.add(channelFuture);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
