package com.Hao.pro_netty_rpc.medium;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.Hao.pro_netty_rpc.client.Response;
import com.Hao.pro_netty_rpc.handler.param.ServerRequest;
import com.alibaba.fastjson.JSONObject;

public class Media {
	public static Map<String, BeanMethod> beanMap;
	static {
		beanMap = new HashMap<String, BeanMethod>();
	}
	
	private static Media m = null;
	private Media() {
		
	}
	//单例
	public static Media newInstance() {
		if (m==null) {
			m=new Media();
		}
		return m;
	}

	//反射处理业务
	@SuppressWarnings("unchecked")
	public Response process(ServerRequest request) {
		Response result =null;
		try {
			String command = request.getCommand();
			//通过命令获得之前在media获取到的所有method map里 <key,beanMethod>中符合的对象;其中beanMethod里包含bean对象和Method对象;
			BeanMethod beanMethod = beanMap.get(command);
			
			if (beanMethod==null){
				return null;
			}
			
			Object bean =beanMethod.getBeanObject();
			Method m = beanMethod.getMethod();
			Class<?> paramType = m.getParameterTypes()[0];
			Object content = request.getContent();
			
			Object args = JSONObject.parseObject(JSONObject.toJSONString(content), paramType);
			
			result = (Response) m.invoke(bean, args);
			result.setId(request.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
}
