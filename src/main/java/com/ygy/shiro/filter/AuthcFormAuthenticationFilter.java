package com.ygy.shiro.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygy.shiro.common.Constant;
import com.ygy.shiro.common.ResponseEntity;
import com.ygy.shiro.po.User;
import com.ygy.shiro.service.UserService;




public class AuthcFormAuthenticationFilter extends FormAuthenticationFilter {
	public static final Logger LOGGER = LogManager.getLogger(AuthcFormAuthenticationFilter.class);
	private UserService userService;
	
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//所有authc的访问都会先认证，认证通过放行，认证不通过执行onAccessDenied方法；
		Subject subject = getSubject(request, response);
		HttpServletRequest re = (HttpServletRequest) request;
		String requestUrl=re.getRequestURL().toString();
		System.out.println(requestUrl);
		System.out.println(re.getRemoteHost());
		boolean status=subject.isAuthenticated();
		return status;
	}
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//如果是登陆，直接执行登录认证；
		if (isLoginRequest(request, response) && isLoginSubmission(request, response)) {
            return executeLogin(request, response);
        }
		//如果不是登录还没有认证过且访问的是需要权限的
		//如果有token；
		HttpServletRequest req = (HttpServletRequest) request;
		ResponseEntity res = new ResponseEntity<>();
		String token=req.getHeader(Constant.TOKEN);
		if(token!=null) {
			res.setCode(Constant.LOGIN_EXPIRE_CODE);
			res.setRemark(Constant.LOGIN_EXPIRE_DESC);
		}else {
			res.setCode(Constant.NO_PERMISSION_CODE);
			res.setRemark(Constant.NO_PERMISSION_DESC);
		}
		writeJsonResult(res, response);
		return false;
	}
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		//登录认证成功
		ResponseEntity<String> res = new ResponseEntity<>();
		
//		String result = UUID.randomUUID().toString();
		Session session = subject.getSession();
		User user=null;
		try {
			user = userService.selectByUsernameOrMobile((String)token.getPrincipal());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(e.getMessage());
		}
		session.setAttribute("userInfo", user);
		System.out.println(session.getId());
		//异常处理
		String tokenInfo = "";
		//生成token,借用token来代替cookie;
		try {
			tokenInfo=session.getId().toString();
		}catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		res.setResult(tokenInfo);
		res.setCode(Constant.REQUEST_SUCCESS_CODE);
		res.setRemark(Constant.REQUEST_SUCCESS_DESC);
		writeJsonResult(res, response);
		return false;
	}
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		//登录认证失败
		ResponseEntity res = new ResponseEntity<>();
		if(e instanceof UnknownAccountException||e instanceof IncorrectCredentialsException) {
			res.setRemark("用户名或密码错误");
		}else if(e instanceof DisabledAccountException) {
			res.setRemark("用户已被禁用，请联系管理员开放");
		}else if(e instanceof Exception) {
			res.setRemark("登录失败");
		}
		res.setCode(Constant.REQUEST_FAIL_CODE);
		writeJsonResult(res, response);
		return false;
	}
	@Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		//认证token创建
        String host = this.getHost(request);
        String username = this.getUsername(request);
        String password = this.getPassword(request);
        boolean rememberMe = this.isRememberMe(request);
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if ( username == null && password == null ) {
            JSONObject loginObject = getRequestBody(httpServletRequest);
            if ( loginObject != null ) {
                username = loginObject.getString(this.getUsernameParam());
                password = loginObject.getString(this.getPasswordParam());
                if ( loginObject.containsKey(this.getRememberMeParam()) ) {
                    rememberMe = loginObject.getBoolean(this.getRememberMeParam());
                }

            }
        }
        return this.createToken(username, password, rememberMe, host);

    }
	/**
	 * 将结果转为json响应
	 * @author ygy
	 * @param result
	 * @param response
	 * 2018年8月31日 下午4:18:48
	 */
	private void writeJsonResult(ResponseEntity result, ServletResponse response) {

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json");
        String json = JSON.toJSONString(result);
        try (PrintWriter pw = httpResponse.getWriter()) {
            pw.write(json);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从请求中获取json对象
     * @author ygy
     * @param request
     * @return
     * 2018年8月31日 下午4:21:03
     */
    private JSONObject getRequestBody(HttpServletRequest request) {

        try {

            String json = getRequestBodyString(request);
            if (!StringUtils.isEmpty(json)) {
                return JSONObject.parseObject(json);
            }
        } catch (Exception e){
        	e.printStackTrace();
        	LOGGER.error("Form Auth Filter GetRequestBody Error ", e);
        }

        return null;
    }

    /**
     * 读取请求中的json字符串，request.getReader()一次请求只能获取一次流，第二次获取将抛出流关闭异常,
     * 及登陆成功后不能把信息放入springmvc处理（springmvc绑定参数还会有用到）
     * @author ygy
     * @param request
     * @return
     * @throws IOException
     * 2018年8月31日 下午4:20:02
     */
    private String getRequestBodyString(HttpServletRequest request) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        try (BufferedReader reader = request.getReader()) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        }
        return stringBuffer.toString();
    }
}
