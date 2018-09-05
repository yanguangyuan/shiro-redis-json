package com.ygy.shiro.exception;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygy.shiro.common.Constant;
import com.ygy.shiro.common.ResponseEntity;
import com.ygy.shiro.controller.UserController;


/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.exception
* describe: 自定义异常处理
* @author : 严光远
* creat_time: 2018年9月5日 上午10:15:41
* 
**/
public class MyExceptionResolver implements HandlerExceptionResolver {
	protected final static Logger LOGGER = LogManager.getLogger(MyExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		String errorMsg = "系统异常，请与管理员联系";
		ResponseEntity result = new ResponseEntity<>();
		// 如果是没有权限异常
		if (ex instanceof AuthorizationException) {
			result.setCode(Constant.NO_PERMISSION_CODE);
			result.setRemark(Constant.NO_PERMISSION_DESC);
		} else if(ex instanceof MyException){
			result.setCode(Constant.REQUEST_FAIL_CODE);
			result.setRemark(ex.getMessage());
		}
		else {
			result.setCode(Constant.REQUEST_FAIL_CODE);
			result.setRemark(errorMsg);
		}
		// json返回
		PrintWriter out = null;
		try {
			LOGGER.error("server has exception", ex);
			String json = JSON.toJSONString(result);
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.write(json);
			out.flush();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (out != null)
				out.close();
		}
		return null;
	}

}
