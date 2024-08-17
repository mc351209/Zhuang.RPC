package com.Hao.user.controller;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.Hao.client.param.Response;
import com.Hao.client.param.ResponseUtil;
import com.Hao.user.bean.User;
import com.Hao.user.service.UserService;

@Controller
public class UserController {
	
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
