package com.Hao.user.remote;

import java.util.List;

import javax.annotation.Resource;

import com.Hao.pro_netty_rpc.annotation.Remote;
import com.Hao.pro_netty_rpc.client.Response;
import com.Hao.pro_netty_rpc.util.ResponseUtil;
import com.Hao.user.bean.User;
import com.Hao.user.service.UserService;

@Remote
public class UserRemoteImpl implements UserRemote {
	
	@Resource
	private UserService userService;
	
	public Response saveUser(User user) {
		userService.save(user);
		return ResponseUtil.createSuccessResult(user);
	};
	
	public Response saveUsers(List<User> users) {
		userService.saveList(users);
		return ResponseUtil.createSuccessResult(users);
	};
}
