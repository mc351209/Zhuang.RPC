package com.Hao.user.remote;

import java.util.List;

import com.Hao.pro_netty_rpc.client.Response;
import com.Hao.pro_netty_rpc.util.ResponseUtil;
import com.Hao.user.bean.User;

public interface UserRemote {
	public Response saveUser(User user);
	public Response saveUsers(List<User> users);
}
