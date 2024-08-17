package com.Hao.client.core;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelFuture;

public class ChannelManager {
	//用来管理链接,使用容器(支持读写)
	//读的时候是去请求的时候读,写的时候是ServerWatcher
	static Set<String> realServerPath = new HashSet<String>();
	static AtomicInteger position=new AtomicInteger(0);
	public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();
	
	public static void removeChannel(ChannelFuture channelFuture) {
		channelFutures.remove(channelFuture);	
	}
	
	public static void add(ChannelFuture channelFuture) {
		channelFutures.add(channelFuture);	
	}
	
	public static void clear() {
		channelFutures.clear();	
	}

	public static ChannelFuture get(AtomicInteger i) {
		int size = channelFutures.size();
		ChannelFuture channelFuture = null;
		if (i.get()>size) {
			channelFuture= channelFutures.get(0);
			ChannelManager.position=new AtomicInteger(1);
		}else {
			channelFuture= channelFutures.get(i.getAndIncrement());
		}
		
		if (!channelFuture.channel().isActive()) {
			channelFutures.remove(channelFuture);
			return get(position);
		}
		
		return channelFuture;
	}
	
	
}
