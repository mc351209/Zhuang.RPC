package com.Hao.client.core;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.Hao.client.param.ClientRequest;
import com.Hao.client.param.Response;

public class DefaultFuture {

	//管理这些DefautFuture
	public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture=new ConcurrentHashMap<Long, DefaultFuture>();
	final Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private Response response;
	//超时属性
	private long timeout=2*60*1000l;
	private long startTime=System.currentTimeMillis();
	
	
	
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public long getStartTime() {
		return startTime;
	}

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
	//get方法重载超时方法
	public Response get(long time) {
		lock.lock();
		try {
			while(!done()) {
				condition.await(time,TimeUnit.SECONDS);
				if ((System.currentTimeMillis()-startTime)>time) {
					System.out.println("请求超时了..");
					break;
				}
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

	
	//定时把超时任务清除,另外启动线程
	static class FutureThreat extends Thread{
		@Override
		public void run() {
			Set<Long>ids= allDefaultFuture.keySet();
			for(Long id:ids) {
				DefaultFuture dFuture =allDefaultFuture.get(id);
				if (dFuture==null) {
					allDefaultFuture.remove(id);
				}else {
					//假如链路超时
					if (dFuture.getTimeout()<(System.currentTimeMillis()-dFuture.getStartTime())) {
						Response resp = new Response();
						resp.setId(id);
						resp.setCode("333333");
						resp.setMsg("链路请求超时");
						DefaultFuture.recive(resp);
					}
				}
				
			}
		}
		
	}
	
	static {
		FutureThreat futureThreat = new FutureThreat();
		futureThreat.setDaemon(true);
		futureThreat.start(); 
	}
	
	
}
