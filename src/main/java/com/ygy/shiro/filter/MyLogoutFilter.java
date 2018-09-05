package com.ygy.shiro.filter;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import com.alibaba.fastjson.JSON;
import com.ygy.shiro.common.Constant;
import com.ygy.shiro.common.ResponseEntity;
import com.ygy.shiro.exception.MyExceptionResolver;

/**
 * project_name: shiro-redis-json package: com.ygy.shiro.filter describe:
 * 自定义logout，退出时不重定向，而是由前端控制路由；
 * 
 * @author : 严光远 creat_time: 2018年9月5日 上午10:54:30
 * 
 **/
public class MyLogoutFilter extends LogoutFilter {

	protected final static Logger LOGGER = LogManager.getLogger(MyLogoutFilter.class);

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		ResponseEntity result = new ResponseEntity<>();
		Subject subject = getSubject(request, response);
		try {
			subject.logout();
		} catch (SessionException ise) {
			LOGGER.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		}
		result.setCode(Constant.REQUEST_SUCCESS_CODE);
		result.setRemark(Constant.REQUEST_SUCCESS_DESC);
		// json返回
		PrintWriter out = null;
		try {
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
		return false;
	}
}
