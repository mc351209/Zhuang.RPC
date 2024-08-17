package com.HaoPractice.pro_netty_rpc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.Hao.pro_netty_rpc.client.Response;
import com.Hao.pro_netty_rpc.client.TcpClient;
import com.Hao.pro_netty_rpc.handler.param.ClientRequest;
import com.Hao.user.bean.User;

public class TestTCP {
	//构建request,并且设置内容(其中id自增生成)
	//tcpclient的send方法,通过fastjson讲request转换成json串并用\r\n分割,序列化后发送到服务器
	//服务器触发读事件,分割并反序列化再到simplehandler中fastjson转换成request对象
	//可以在其中设置业务逻辑,然后写一个respon对象返回,同样序列化和分割的过程
	
//	@Test
//	public void testGetResponse() {
//		ClientRequest request = new ClientRequest();
//		request.setContent("这是tcp的长连接测试");
//		Response response = TcpClient.send(request);
//		System.out.println(response.getResultObject());
//	}
	
	
	//测试调用UserController的方法
	
	@Test
	public void testSaveUser() {
		ClientRequest request = new ClientRequest();
		User u = new User();
		u.setId(1);
		u.setName("彭宇轩");
		request.setCommand("com.Hao.user.controller.UserController.saveUser");
		request.setContent(u);
		Response response = TcpClient.send(request);
		System.out.println(response.getResultObject());
	}
	
//	@Test
//	public void testSaveUsers() {
//		ClientRequest request = new ClientRequest();
//		List<User> users = new ArrayList<>();
//		User u = new User();
//		u.setId(1);
//		u.setName("彭宇轩");
//		users.add(u);
//		request.setCommand("com.Hao.user.controller.UserController.saveUser");
//		request.setContent(users);
//		Response response = TcpClient.send(request);
//		System.out.println(response.getResultObject());
//	}
	
}


