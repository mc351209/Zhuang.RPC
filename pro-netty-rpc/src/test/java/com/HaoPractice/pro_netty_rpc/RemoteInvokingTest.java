
package com.HaoPractice.pro_netty_rpc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.Hao.pro_netty_rpc.annotation.RemoteInvoke;
import com.Hao.pro_netty_rpc.client.Response;
import com.Hao.pro_netty_rpc.client.TcpClient;
import com.Hao.pro_netty_rpc.handler.param.ClientRequest;
import com.Hao.user.bean.User;
import com.Hao.user.remote.UserRemote;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokingTest.class)
@ComponentScan("com.Hao")
public class RemoteInvokingTest {
	@RemoteInvoke
	
	private UserRemote userRemote;
	
	@Test
	public void testSaveUser() {
		
		User u = new User();
		u.setId(1);
		u.setName("彭宇轩");
		userRemote.saveUser(u);
		
	}
	
	@Test
	public void testSaveUsers() {
		List<User> users = new ArrayList<>();
		User u = new User();
		u.setId(1);
		u.setName("彭宇轩");
		users.add(u);
		userRemote.saveUsers(users);
	}

}
