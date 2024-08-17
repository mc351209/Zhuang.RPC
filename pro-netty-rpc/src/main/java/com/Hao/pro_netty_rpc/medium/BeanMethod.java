package com.Hao.pro_netty_rpc.medium;

import java.lang.reflect.Method;

public class BeanMethod {
	private Object beanObject;
	private Method method;
	
	//get/set方法
	public Object getBeanObject() {
		return beanObject;
	}
	public void setBeanObject(Object beanObject) {
		this.beanObject = beanObject;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	
	
}
