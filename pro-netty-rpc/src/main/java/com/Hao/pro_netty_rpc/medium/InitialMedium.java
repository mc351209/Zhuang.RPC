package com.Hao.pro_netty_rpc.medium;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.springframework.aop.MethodMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.Hao.pro_netty_rpc.annotation.Remote;
import com.Hao.pro_netty_rpc.annotation.RemoteInvoke;

import io.netty.handler.codec.base64.Base64Encoder;

@Component
public class InitialMedium implements BeanPostProcessor{

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean.getClass().isAnnotationPresent(Remote.class)) {
			//测试,实际可以通过@Controller获得所有的类,因此可以获得对应的方法
			//System.out.println(bean.getClass().getName());
			
			Method[] methods = bean.getClass().getDeclaredMethods();
			for(Method m:methods) {
				//方法需要有一个唯一的识别,得到接口的方法
				String key = bean.getClass().getInterfaces()[0].getName()+"."+m.getName();
				//保存在media内的map中
				Map<String, BeanMethod> beanMap = Media.beanMap;
				BeanMethod beanMethod = new BeanMethod();
				beanMethod.setBeanObject(bean);
				beanMethod.setMethod(m);
				beanMap.put(key, beanMethod);
			}
		}
		return bean;
	}
	//初始化要获取所有的controller,获取他们的方法
	
	
}
