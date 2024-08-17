package com.Hao.pro_netty_rpc.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.Hao.pro_netty_rpc.handler.param.ClientRequest;

public class DefaultFuture {

	//管理这些DefautFuture
	public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture=new ConcurrentHashMap<Long, DefaultFuture>();
	final Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private Response response;
	private static DefaultFuture df;
	

	public Response get() {
		lock.lock();
		try {
			while(!done()) {
				condition.await();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return this.response;
		
	
	}
	//done方法用于Response中判断response是否有值
	private boolean done() {
		if(this.response!=null) {
			return true;
		}
		return false;
	}
	
	//recive用于把response设置进去
	public static void recive(Response response) {
		DefaultFuture df = allDefaultFuture.get(response.getId());
		if(df!=null) {
			Lock lock = df.lock;
			lock.lock();
			try {
				df.setResponse(response);
				df.condition.signal();
				allDefaultFuture.remove(response.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
			
		}
	}
	
	//Response的get/set方法
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	
	//DefaultFuture的构造方法,把df设置进hashmap里
	public DefaultFuture(ClientRequest request) {
		allDefaultFuture.put(request.getId(), this);
	}

}
