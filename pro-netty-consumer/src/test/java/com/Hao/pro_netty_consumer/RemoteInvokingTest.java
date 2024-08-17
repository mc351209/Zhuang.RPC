package com.Hao.pro_netty_consumer;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.Hao.client.annotation.RemoteInvoke;
import com.Hao.client.param.Response;
import com.Hao.user.bean.User;
import com.Hao.user.remote.UserRemote;
import com.alibaba.fastjson.JSONObject;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokingTest.class)
@ComponentScan("com.Hao")
public class RemoteInvokingTest {
	@RemoteInvoke
	private UserRemote userRemote;
	@RemoteInvoke
	private UserRemote userRemote222;
	@Test
	public void testSaveUser() {
		User u = new User();
		u.setId(1);
		u.setName("轩轩");
		Response response = userRemote.saveUser(u);
		System.out.println(JSONObject.toJSONString(response));
	}
	
//	@Test
//	public void testSaveUsers() {
//		List<User> users = new ArrayList<>();
//		User u = new User();
//		u.setId(1);
//		u.setName("彭宇轩");
//		users.add(u);
//		userRemote.saveUsers(users);
//	}
//
}
