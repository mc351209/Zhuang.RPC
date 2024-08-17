package com.Hao.client.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import com.Hao.client.annotation.RemoteInvoke;
import com.Hao.client.core.TcpClient;
import com.Hao.client.param.ClientRequest;
import com.Hao.client.param.Response;

@Component
public class InvokeProxy implements BeanPostProcessor{

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for(Field field : fields) {
			if (field.isAnnotationPresent(RemoteInvoke.class)) {
				field.setAccessible(true);
				
				//把类设置进去
				final Map<Method,Class>methodClassMap = new HashMap<Method,Class>();
				putMethodClass(methodClassMap,field);
				
				Enhancer enhancer = new Enhancer();
				//指定代理类需要实现的接口
				enhancer.setInterfaces(new Class[] {field.getType()});
				enhancer.setCallback(new MethodInterceptor() {
					//执行某些方法的时候进行拦截
					@Override
					public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
						//采用netty客户端调用服务器
						ClientRequest request = new ClientRequest();
						request.setCommand(methodClassMap.get(method).getName()+"."+method.getName());
						request.setContent(args[0]);
						Response response = TcpClient.send(request);
						
						return response;
					}
				});
				
				try {
					field.set(bean, enhancer.create());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return bean;

	}
	
	
	//对属性的所有方法和属性接口类型放入到一个map
	private void putMethodClass(Map<Method,Class> methodClassMap, Field field) {
		Method[] methods = field.getType().getDeclaredMethods();
		for(Method method : methods) {
			methodClassMap.put(method, field.getType());
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return bean;
	}
	
}
