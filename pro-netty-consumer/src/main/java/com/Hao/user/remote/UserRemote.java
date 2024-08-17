package com.Hao.user.remote;

import java.util.List;

import com.Hao.client.param.Response;
import com.Hao.user.bean.User;

public interface UserRemote {
	public Response saveUser(User user);
	public Response saveUsers(List<User> users);
}
