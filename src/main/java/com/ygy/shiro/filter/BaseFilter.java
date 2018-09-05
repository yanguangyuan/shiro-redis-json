package com.ygy.shiro.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.CharacterEncodingFilter;


/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.filter
* describe: 跨域访问设置
* @author : 严光远
* creat_time: 2018年9月5日 下午3:11:16
* 
**/
public class BaseFilter extends CharacterEncodingFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		{
		    response.setDateHeader("Expires", -9177268251581743104L);
		    response.setHeader("Cache-Control", "no-cache");
		    response.setHeader("Prama", "no-cache");
		    response.setHeader("Cache-Control", "public");
		    response.setHeader("Access-Control-Allow-Origin", "*");
		    response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
		    response.setHeader("Access-Control-Max-Age", "36000");
		    response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With, token,device_type,req_uid,deviceType,reqUid");
		    response.setHeader("Access-Control-Expose-Headers", "token");


		    response.addHeader("P3P", "CP=IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT");
		    if (!(checkVueOptionalRequest(request)))
		    {
//		      doPrivateFilterInternal(request, response);
		      super.doFilterInternal(request, response, filterChain);
		    }
	}
	}

	private boolean checkVueOptionalRequest(HttpServletRequest request) {
		String method = request.getMethod();
		return (RequestMethod.OPTIONS.name().equals(method));
	}
}
