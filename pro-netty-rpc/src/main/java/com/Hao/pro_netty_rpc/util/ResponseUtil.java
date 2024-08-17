package com.Hao.pro_netty_rpc.util;

import com.Hao.pro_netty_rpc.client.Response;

public class ResponseUtil {
	public static Response createSuccessResult() {
		return new Response();
	}
	
	
	public static Response createFailResult(String code, String msg) {
		Response response = new Response();
		response.setCode(code);
		response.setMsg(msg);
		return response;
	}
	
	public static Response createSuccessResult(Object content) {
		Response response= new Response();
		response.setResultObject(content);
		return response;
	}
		
	
}

